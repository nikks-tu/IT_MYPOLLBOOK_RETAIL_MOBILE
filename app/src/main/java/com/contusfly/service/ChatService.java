/**
 * @category ContusMessanger
 * @package com.contusfly.service
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.contus.chatlib.XMPPConnectionController;
import com.contus.chatlib.listeners.GroupListener;
import com.contus.chatlib.listeners.OnTaskCompleted;
import com.contus.chatlib.listeners.RosterListener;
import com.contus.chatlib.listeners.XMPPLoginListener;
import com.contus.chatlib.models.Contact;
import com.contus.chatlib.models.Group;
import com.contus.chatlib.parcels.User;
import com.contus.chatlib.utils.ContusConstantValues;
import com.contusfly.MApplication;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.ChatUtils;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ImageCompressor;
import com.contusfly.utils.ImageUploadS3;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.MediaPaths;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.contusfly.utils.Constants.CHAT_RECEIVED_RECEIPT_SENT;

/**
 * The Class ChatService.
 */
public class ChatService extends Service {
    /**
     * Thread name for the service.
     */
    public static final String SERVICE_THREAD_NAME = "Contus" + ".Service";

    private XMPPConnectionController controller;

    private List<com.contus.chatlib.models.ChatMessage> smackGroupChatMessages;
    /**
     * The s service handler.
     */
    private volatile ServiceHandler sServiceHandler;
    /**
     * Singleton class for accessing singleton methods and objects.
     */
    private MApplication mApplication;

    /**
     * Message id used for the notification.
     */
    private String msgId = Constants.EMPTY_STRING;

    /**
     * Receiver of the message.
     */
    private String msgTo = Constants.EMPTY_STRING;
    /**
     * Actual chat message body.
     */
    private String msgBody = Constants.EMPTY_STRING;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = (MApplication) getApplicationContext();
        smackGroupChatMessages = new ArrayList<>();
        HandlerThread thread = new HandlerThread(SERVICE_THREAD_NAME);
        thread.start();
        Looper sServiceLooper = thread.getLooper();
        sServiceHandler = new ServiceHandler(sServiceLooper);
        Log.e("Service created::", "created::");
    }

    private void loadConnection(String resourceAppend) {
        com.contus.chatlib.ConnectionSettings settings = new com.contus.chatlib.ConnectionSettings();
        String username = mApplication.getStringFromPreference(Constants.USERNAME);
        String password = mApplication.getStringFromPreference(Constants.SECRET_KEY);
        settings.setXmppHost(Constants.XMPP_HOST);
        settings.setXmppDomain(Constants.XMPP_HOST);
        settings.setXmppPort(Constants.XMPP_PORT);
        settings.setXmppResource(resourceAppend + Utils.getResource(mApplication));

        settings.setUserName(username);
        settings.setPassword(password);

        controller = new XMPPConnectionController();
        controller.configureSettings(settings);
        controller.setLoginListener(new LoginListener());
        controller.setRosterListener(new ProfileCallbackListener());
        controller.setGroupListener(new GroupCallbackListener());
        controller.setMessageListener(new SmackMessageListener());
        controller.connect(getClass().getName());
    }

    private void updateGroupRemovedUser(String gid, Rosters item, String removedUser, String removingUser) {
        String loginUsername = mApplication.getStringFromPreference(Constants.USERNAME);

        ContentValues groupValues = new ContentValues();

        String groupWhere = Constants.ROSTER_USER_ID + "='" + gid + "'";

        MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_USER_ID + "=?",
                new String[]{gid});
        String name;
        if (loginUsername.equalsIgnoreCase(removedUser)) {
            groupValues.put(Constants.ROSTER_GROUP_STATUS, String.valueOf(Constants.COUNT_ONE));
            name = "You";
        } else {
            String groupUsers = item.getRosterGroupUsers();
            List<String> users = new LinkedList<>(Arrays.asList(groupUsers.split(",")));
            if (!users.contains(removedUser))
                return;
            users.remove(removedUser);
            groupValues.put(Constants.ROSTER_GROUP_USERS, TextUtils.join(",", users));

            List<Rosters> newUserRosterInfo = MDatabaseHelper.getRosterInfo(removedUser,
                    Constants.EMPTY_STRING);
            name = removedUser;
            if (newUserRosterInfo != null && !newUserRosterInfo.isEmpty()) {
                name = newUserRosterInfo.get(0).getRosterName();
            }
        }
        MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, groupValues, groupWhere);
        String status;
        if (removedUser.equals(removingUser))
            status = getString(R.string.text_group_exit_user);
        else
            status = getString(R.string.text_group_remove_user);
        String message = String.format(status, name);
        new ChatUtils(getApplicationContext()).insertGroupStatusMessage(gid, message);
        if (item.getRosterAdmin().equals(removedUser) && !loginUsername.equalsIgnoreCase(removedUser)) {
            controller.getGroupInfo(gid);
        }
    }

    private boolean checkGroup(com.contus.chatlib.models.ChatMessage chatMessage) {
        LogMessage.v("grouppp check enters", "entering");
        if (Constants.GROUP_CHAT.equalsIgnoreCase(chatMessage.getChat_type())) {
            String groupId = chatMessage.getFrom();
            List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(groupId,
                    Constants.ROSTER_GROUP_USERS);
            if (rosterInfo == null || rosterInfo.isEmpty()) {
                LogMessage.v("grouppp not available", "size::" + smackGroupChatMessages.size());
                smackGroupChatMessages.add(chatMessage);
                controller.getGroupInfo(chatMessage.getFrom());
                return false;
            }
        }
        return true;
    }

    private boolean checkMediaAvailable(ChatMsg chatmsg) {
        if (chatmsg.getMsgType().equals(Constants.MSG_TYPE_TEXT) || chatmsg.getMsgType().equals(Constants.MSG_TYPE_CONTACT)
                || chatmsg.getMsgType().equals(Constants.MSG_TYPE_LOCATION)) {
            return true;
        } else if (chatmsg.getMsgMediaServerPath() == null) {
            return false;
        }
        return true;
    }

    private synchronized void receivedMessage(com.contus.chatlib.models.ChatMessage chatmsg) {
        if (mApplication.getBooleanFromPreference(Constants.IS_CONTACTS_SYNCED)) {
            Intent intent = new Intent(Constants.ACTION_MESSAGE_FROM_ROSTER);
            String chatType = mApplication.returnEmptyStringIfNull(chatmsg.getChat_type());
            String chatFrom, toUserId;
            if (chatType.equalsIgnoreCase(Constants.GROUP_CHAT)) {
                chatType = Constants.GROUP_CHAT;
                chatFrom = mApplication.returnEmptyStringIfNull(chatmsg.getSender());
                toUserId = mApplication.returnEmptyStringIfNull(chatmsg.getFrom());
            } else {
                chatType = Constants.CHAT;
                chatFrom = chatmsg.getFrom();
                toUserId = chatFrom;
            }
            if (!chatFrom.equalsIgnoreCase(mApplication.getStringFromPreference(Constants.USERNAME)) && checkGroup(chatmsg)) {
                ChatMsg mChatMsg = convertMsgFormat(chatmsg, chatType, chatFrom);
                if (checkMediaAvailable(mChatMsg)) {
                    intent.putExtra(Constants.CHAT_MESSAGE, mChatMsg);
                    controller.sendMessageReceipt(chatFrom, msgId, Constants.CHAT, Constants.RECEIPT,
                            String.valueOf(System.currentTimeMillis() / 1000));
                    ChatUtils chatUtils = new ChatUtils(getApplicationContext());
                    int inserted = chatUtils.updateChatHistory(mChatMsg);
                    if (chatType.equals(Constants.GROUP_CHAT)) {
                        ContentValues mValues = new ContentValues();
                        mValues.put(Constants.CHAT_MSG_ID, chatmsg.getMid());
                        mValues.put(Constants.CHAT_MSG_STATUS, String.valueOf(Constants.CHAT_REACHED_TO_SERVER));
                        mValues.put(Constants.CHAT_IS_ACK, String.valueOf(Constants.COUNT_ONE));
                        mValues.put(Constants.GROUP_ID, chatmsg.getFrom());
                        mValues.put(Constants.CHAT_USER_ID, chatmsg.getSender());
                        MDatabaseHelper.insertValues(Constants.TABLE_GROUP_MSG_STATUS, mValues);
                    }

                    if (inserted == 1) {
                        String ongoingChatPerson = mApplication
                                .returnEmptyStringIfNull(mApplication.getStringFromPreference(Constants.CHAT_ONGOING_NAME));
                        String msgSeen = String.valueOf(Constants.COUNT_ZERO);
                        if (!ongoingChatPerson.equalsIgnoreCase(msgTo)) {
                            chatUtils.updatePendingNotification(mChatMsg, msgTo);
                            List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(msgTo, Constants.EMPTY_STRING);
                            String displayName = !rosterInfo.isEmpty() ? rosterInfo.get(0).getRosterName() : chatFrom;
                            new Utils().createNotification(mApplication, getApplicationContext(), displayName,
                                    mChatMsg.getMsgType(), msgBody, toUserId);
                            msgSeen = String.valueOf(Constants.COUNT_ONE);
                        }
                        chatUtils.updateRecentChatHistory(mChatMsg, msgSeen, msgTo);
                    }
                    getApplicationContext().sendBroadcast(intent);
                    if (mApplication.getBooleanFromPreference(Constants.MEDIA_DOWNLOAD)
                            && mApplication.isNetConnected(ChatService.this)) {
                        downloadMedia(mChatMsg);
                    }
                }
            }
        }
    }

    private void updateRoster(String rosterId, ContentValues mValues) {
        if (MDatabaseHelper.checkIDStatus(rosterId, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID)) {
            String where = Constants.ROSTER_USER_ID + "='" + rosterId + "'";
            MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, mValues, where);
        } else {

            mValues.put(Constants.ROSTER_USER_ID, rosterId);
            mValues.put(Constants.ROSTER_CUSTOM_TONE, Constants.EMPTY_STRING);
            MDatabaseHelper.insertValues(Constants.TABLE_ROSTER, mValues);
        }
    }

    /**
     * Gets the availability.
     *
     * @param availabity the roster availability
     * @return the availability
     */
    private int getAvailability(String availabity) {
        int availabilityStatus = Constants.COUNT_ZERO;

        if (availabity != null) {
            switch (availabity) {
                case "0":
                    availabilityStatus = Constants.ROSTER_OFFLINE;
                    break;
                case "1":
                    availabilityStatus = Constants.ROSTER_ONLNE;
                    break;
                default:
                    availabilityStatus = Constants.ROSTER_NOT_ACTIVE;
                    break;
            }
        }
        return availabilityStatus;
    }

    private void downloadMedia(ChatMsg mChatMsg) {
        if (mApplication.getBooleanFromPreference(Constants.MEDIA_DOWNLOAD)) {
            downloadFileFromServer(mChatMsg.getMsgMediaServerPath(), mChatMsg.getMsgId(),
                    mChatMsg.getMsgType());
        }
    }

    private void login(Intent intent) {
//        String username = mApplication.getStringFromPreference(Constants.USERNAME);
//        String password = mApplication.getStringFromPreference(Constants.SECRET_KEY);
//        if ((TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) && intent != null) {
//            username = intent.getStringExtra("username");
//            password = intent.getStringExtra("password");
//        }
//        LogMessage.v("loginnnn::servicelogin", "::username::" + username + "::password::" + password);
//        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
//            mClient.login(username, password);


        if (mApplication.isNetConnected(ChatService.this)) {
            if (controller == null)
                loadConnection("");
            else
                controller.login();
        }
    }

    /**
     * Handle event.
     *
     * @param intent the intent
     */
    public void handleEvent(Intent intent) {
        try {
            if (intent == null) {
                return;
            }
            if (mApplication.isNetConnected(this)) {
                String action = intent.getAction();

                if (action.equalsIgnoreCase(Constants.ACTION_LOGOUT)) {
                    mApplication.storeBooleanInPreference(Constants.ACTION_LOGOUT, true);
//                    controller.logout();
                } else if (action.equalsIgnoreCase(ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_REQUEST)) {
                    login(intent);
                } else if (action.equalsIgnoreCase(ContusConstantValues.CONTUS_XMPP_ACTION_NEW_CONNECTION_REQUEST)) {
                    XMPPConnectionController.isBindError = false;
                    loadConnection("New-");
                } else if (action.equalsIgnoreCase(ContusConstantValues.CONTUS_XMPP_ACTION_CHAT_REQUEST)) {
                    String msgType = intent.getStringExtra(Constants.CHAT_MSG_TYPE);
                    String toUser = intent.getStringExtra("to"), fromUser = intent.getStringExtra("from"),
                            msgChatId = intent.getStringExtra("mid");
                    String message = intent.getStringExtra("msg");
                    String mTime = intent.getStringExtra("mtime");
                    int chatType = intent.getIntExtra(Constants.CHAT_TYPE, Constants.CHAT_TYPE_SINGLE);
                    List<ChatMsg> mPendingMsgs = MDatabaseHelper.getExceptPendingMessage(msgChatId);
                    sendPendingMessages(mPendingMsgs);
                    sendChatReceipt();
                    String chatGroupToUSer = intent.getStringExtra(Constants.CHAT_TO_USER);
                    String[] users = null;
                    if (chatGroupToUSer != null)
                        users = chatGroupToUSer.split(",");
                    sendChatMessage(msgType, fromUser, toUser, msgChatId, chatType, message, users, mTime);
                } else if (action.equalsIgnoreCase(ContusConstantValues.CONTUS_XMPP_ACTION_GET_ROSTER)) {
                    controller.getContacts();
                } else if (action.equalsIgnoreCase(ContusConstantValues.CONTUS_XMPP_MESSAGE_SEND_SEEN)) {
                    String to = intent.getStringExtra("to");
                    String mid = intent.getStringExtra("mid");
                    String chattype = intent.getStringExtra("type");
                    controller.sendMessageReceipt(to, mid, chattype, Constants.SEEN, String.valueOf(System.currentTimeMillis() / 1000));
                } else if (action.equalsIgnoreCase(Constants.ACTION_MEIDA_UPLOAD)) {
                    ChatMsg chatMsg = intent.getParcelableExtra(Constants.CHAT_MESSAGE);
                    mediaUpload(chatMsg);

                } else if (action.equalsIgnoreCase(Constants.ACTION_MEIDA_DOWNLOAD)) {
                    downloadFileFromServer(intent.getStringExtra("url"), intent.getStringExtra("mid"),
                            intent.getStringExtra("type"));
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_ACTION_EDIT_PROFILE)) {
                    String status = Utils.getEncodedString(intent.getStringExtra("profStatus"));
                    String userName = Utils.getEncodedString(intent.getStringExtra("editUserName"));
                    String imageUrl = intent.getStringExtra("imageUrl");
                    controller.editProfile(userName, imageUrl, status);
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_CREATE_GROUP)) {
                    String groupName = intent.getStringExtra("groupName");
                    String groupImage = intent.getStringExtra("groupimage");
                    controller.createGroup(Utils.getEncodedString(groupName), "ContusFly GroupChat testing", groupImage);
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_ADD_GROUP_MEMBER)) {
                    String groupId = intent.getStringExtra(Constants.GROUP_ID);
                    String groupMember = intent.getStringExtra("groupMember");
                    controller.addGroupMember(groupId, groupMember);
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_GET_GROUP_INFO)) {
                    String groupId = intent.getStringExtra(Constants.GROUP_ID);
                    controller.getGroupInfo(groupId);
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_UPDATE_GROUP_INFO)) {
                    String groupId = intent.getStringExtra(Constants.GROUP_ID);
                    String groupName = intent.getStringExtra("groupName");
                    String groupimage = intent.getStringExtra("groupimage");
                    controller.getUpDateGroupInfo(groupId, Utils.getEncodedString(groupName), groupimage, "");
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_REMOVE_GROUP_MEMBER)) {
                    String groupId = intent.getStringExtra(Constants.GROUP_ID);
                    String groupMember = intent.getStringExtra("groupMember");
                    controller.removeGroupMember(groupId, groupMember);
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP)) {
                    String groupId = intent.getStringExtra(Constants.GROUP_ID);
                    controller.exitGroup(groupId);
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_GET_GROUPS)) {
                    controller.getGroups();
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_ACTION_SET_DEVICEID)) {
                    String deviceId = intent.getStringExtra("deviceId");
                    controller.sendDeviceId(deviceId);
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_ACTION_GET_PROFILE)) {
                    controller.getProfile(mApplication.getStringFromPreference(Constants.USERNAME));
                } else if (action.equals(Constants.ACTION_GET_AVAILABILITY)) {
                    LogMessage.v("getAvailability", "::" + intent.getStringExtra(Constants.ROSTER_USER_ID));
                    controller.getAvailability(intent.getStringExtra(Constants.ROSTER_USER_ID));
                } else if (action.equals(ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE)) {
                    controller.sendTypingStatus(intent.getStringExtra(Constants.USERNAME),
                            intent.getStringExtra(Constants.STATUS_TXT), intent.getStringExtra(Constants.CHAT_TYPE),
                            mApplication.getStringFromPreference(Constants.USERNAME));
                } else if (action.equals(Constants.ACTION_DELETE_ACCOUNT)) {
                    controller.deleteAccount();
                } else if (action.equals(Constants.DELETE_MESSAGE)) {
                    String messageId = intent.getStringExtra(Constants.MESSAGE_ID);
                    int chat = intent.getIntExtra(Constants.CHAT_TYPE, Constants.CHAT_TYPE_SINGLE);
                    if (Constants.CHAT_TYPE_SINGLE == chat)
                        controller.deleteMessage(messageId, "chat");
                    else if (Constants.CHAT_TYPE_GROUP == chat)
                        controller.deleteMessage(messageId, "groupchat");
                } else if (action.equals(Constants.CLEAR_CHAT)) {
                    String username = intent.getStringExtra(Constants.USERNAME);
                    int chat = intent.getIntExtra(Constants.CHAT_TYPE, Constants.CHAT_TYPE_SINGLE);
                    if (Constants.CHAT_TYPE_SINGLE == chat)
                        controller.clearChat(username, "chat");
                    else if (Constants.CHAT_TYPE_GROUP == chat)
                        controller.clearChat(username, "groupchat");
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private void sendPendingMessages(List<ChatMsg> mPendingMsgs) {
        for (ChatMsg message : mPendingMsgs) {
            String constructMessage = "";
            String msgType = message.getMsgType();
            String mTime = message.getMsgTime();
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)) {
                constructMessage = constructMsg(message.getMsgBody(), message.getMsgType(), Constants.EMPTY_STRING,
                        Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING);

            } else if (!Utils.checkIsEmpty(message.getMsgMediaServerPath())) {
                constructMessage = constructMsg(message.getMsgBody(), message.getMsgType(), message.getMsgMediaSize(),
                        message.getMsgMediaEncImage(), message.getMsgMediaServerPath(), message.getFileName(), Constants.EMPTY_STRING);
            }
            String pendingChatType;
            if (Integer.parseInt(message.getMsgChatType()) == 0) {
                pendingChatType = "chat";
                if (!constructMessage.isEmpty())
                    sendGroupChat(message.getMsgTo(), constructMessage, message.getMsgType(), message.getMsgId(), pendingChatType, mTime, "0");
            } else if (Integer.parseInt(message.getMsgChatType()) == 2) {
                pendingChatType = Constants.CHAT;
                String[] users = message.getChatReceivers().split(",");
                String toUser = message.getMsgTo();
                toUser = toUser.substring(toUser.lastIndexOf("_") + 1);
                if (!constructMessage.isEmpty())
                    for (String user : users) {
                        sendGroupChat(user, constructMessage, message.getMsgType(), message.getMsgId(), pendingChatType, mTime, toUser);
                    }
            } else {
                pendingChatType = Constants.GROUP_CHAT;
                if (!constructMessage.isEmpty())
                    sendGroupChat(message.getMsgTo(), constructMessage, message.getMsgType(), message.getMsgId(), pendingChatType, mTime, null);
            }
        }
    }

    private void sendChatReceipt() {
        List<ChatMsg> mPendingReceiptMessages = MDatabaseHelper.getNotSentReceipts(Constants.CHAT_RECEIVED);
        for (ChatMsg pendingReceipt : mPendingReceiptMessages) {
            controller.sendMessageReceipt(pendingReceipt.getMsgTo(), pendingReceipt.getMsgId(),
                    pendingReceipt.getMsgType(), Constants.RECEIPT, pendingReceipt.getMsgTime());
        }
    }

    private void sendChatMessage(String msgType, String fromUser, String toUser, String msgChatId,
                                 int chatType, String message, String[] users, String mTime) {
        try {
            String msgContent = "";
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)) {
                msgContent = constructMsg(message, msgType, Constants.EMPTY_STRING,
                        Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING);
            } else {
                List<ChatMsg> chatMsgs = MDatabaseHelper.getMediaInfo(fromUser, toUser, msgChatId);
                ChatMsg item = chatMsgs.get(0);
                if (!Utils.checkIsEmpty(item.getMsgMediaServerPath()))
                    msgContent = constructMsg(message, item.getMsgType(), item.getMsgMediaSize(),
                            item.getMsgMediaEncImage(), item.getMsgMediaServerPath(), item.getFileName(), Constants.EMPTY_STRING);
            }
            if (chatType == Constants.CHAT_TYPE_SINGLE && !Utils.checkIsEmpty(msgContent)) {
                sendGroupChat(toUser, msgContent, msgType, msgChatId, "chat", mTime, "0");
            } else if (chatType == Constants.CHAT_TYPE_BROADCAST && !Utils.checkIsEmpty(msgContent) && users != null) {
                String receiver = toUser.substring(toUser.lastIndexOf("_") + 1);
                for (String user : users) {
                    sendGroupChat(user, msgContent, msgType, msgChatId + "-" + user, "chat", mTime, receiver);
                }
            } else if (!Utils.checkIsEmpty(msgContent)) {
                sendGroupChat(toUser, msgContent, msgType, msgChatId, Constants.GROUP_CHAT, mTime, null);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public void sendGroupChat(String to, String msg, String messageType, String mid, String chatType, String mTime, String broadcastId) {
        com.contus.chatlib.models.ChatMessage chatMessage = new com.contus.chatlib.models.ChatMessage();
        String username = mApplication.getStringFromPreference(Constants.USERNAME);
        chatMessage.setFrom(username);
        chatMessage.setTo(to);
        chatMessage.setMsg_type(messageType);
        chatMessage.setChat_type(chatType);
        chatMessage.setMid(mid);
        chatMessage.setMsg_body(msg);
        chatMessage.setTime(mTime);
        chatMessage.setBroadcastId(broadcastId);
        controller.sendMessage(chatMessage);
    }

    private void mediaUpload(ChatMsg chatMsg) {
        if (chatMsg.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_IMAGE))
            compressImage(chatMsg);
        else if (chatMsg.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_VIDEO))
            uploadVideo(chatMsg);
        else
            uploadFile(chatMsg, chatMsg.getMsgType());
    }

    /**
     * Upload audio.
     *
     * @param msg the msg
     */
    private void uploadFile(ChatMsg msg, String msgType) {
        try {
            File audioFile = new File(msg.getMsgMediaLocalPath());
            ImageUploadS3 imgTask = new ImageUploadS3(getApplicationContext());
            imgTask.uplodingCallback(new UploadTaskFinished(msg));
            imgTask.executeUpload(audioFile, msgType, Constants.EMPTY_STRING,"POLLS/");
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Construct a chat message to send.
     *
     * @param msg      Actual message to send.
     * @param msgType  Type of the message.
     * @param fileSize Media file size.
     * @param thumb    Media thumb as string.
     * @param fileUrl  Media file URL.
     * @param duration Duration of the media if the media is Video.
     * @return the string
     */
    private String constructMsg(String msg, String msgType, String fileSize,
                                String thumb, String fileUrl, String fileName, String duration) {
        String constructedMsg = "";
        try {
            JSONObject jsonObject = new JSONObject();
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION))
                jsonObject.put(msgType, msg);
            else
                jsonObject.put(Constants.PARAM_MESSAGE, msg);
            jsonObject.put(Constants.PARAM_FILE_SIZE, fileSize);
            jsonObject.put(Constants.PARAM_DESCRIPTION, Constants.EMPTY_STRING);
            jsonObject.put(Constants.PARAM_DURATION, duration);
            jsonObject.put(Constants.PARAM_LOCAL_PATH, Constants.EMPTY_STRING);
            jsonObject.put(Constants.PARAM_FILE_URL, fileUrl);
            jsonObject.put(Constants.PARAM_MSG_TYPE, msgType);
            String fileType = "";
            if (fileUrl != null && !fileUrl.isEmpty()) {
                String[] splits = fileUrl.split(".");
                if (splits.length > 0)
                    fileType = splits[splits.length - 1];
            }
            jsonObject.put(Constants.PARAM_FILE_TYPE, fileType);
            jsonObject.put(Constants.CHAT_MEDIA_NAME, fileName);
            jsonObject.put(Constants.PARAM_FILE_THUMB, thumb);
            return URLEncoder.encode(jsonObject.toString(), "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return constructedMsg;
    }

    /**
     * Update the status of the message.
     *
     * @param chatMessage The chat message which to change the status.
     * @param status      The status to which to change.
     */
    public void updateMsgStatus(com.contus.chatlib.models.ChatMessage chatMessage, int status) {
        String messageID = chatMessage.getMid();
        String userId = chatMessage.getFrom();
        MDatabaseHelper.updateChatStatus(messageID, Constants.CHAT_COLUMN_STATUS, String.valueOf(status));
        ContentValues mValues = new ContentValues();
        mValues.put(Constants.RECENT_CHAT_STATUS, String.valueOf(status));
        MDatabaseHelper.updateValues(Constants.TABLE_RECENT_CHAT_DATA, mValues,
                Constants.RECENT_CHAT_USER_ID + "='" + userId + "' and "
                        + Constants.RECENT_CHAT_MSG_ID + "='" + messageID + "'");
    }

    /**
     * Convert msg format.
     *
     * @param chatMessage the chat message
     * @param chatType    the chat type
     * @param chatFrom    the chat from
     * @return the chat msg
     */
    private ChatMsg convertMsgFormat(com.contus.chatlib.models.ChatMessage chatMessage, String chatType, String chatFrom) {
        ChatMsg chatMsg = new ChatMsg();
        try {
            msgId = mApplication.returnEmptyStringIfNull(chatMessage.getMid());
            msgTo = mApplication.returnEmptyStringIfNull(chatMessage.getFrom());
            msgBody = mApplication.returnEmptyStringIfNull(chatMessage.getMsg_body());
            String msgTime = String.valueOf(Long.parseLong(chatMessage.getTime()));
            String msgType = chatMessage.getMsg_type();
            if (chatType.equalsIgnoreCase(Constants.GROUP_CHAT)) {
                msgTo = mApplication.returnEmptyStringIfNull(chatMessage.getFrom());
            }
            if (!MDatabaseHelper.checkIDStatus(msgTo, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID))
                Utils.insertUnknownUser(msgTo);

            if ((!TextUtils.isEmpty(msgId)) && (!TextUtils.isEmpty(msgTo)) && (!TextUtils.isEmpty(msgBody))) {
                chatMsg.setMsgId(msgId);
                chatMsg.setMsgFrom(mApplication.getStringFromPreference(Constants.USERNAME));
                chatMsg.setMsgTo(msgTo);
                chatMsg.setMsgTime(msgTime);
                chatMsg.setMsgStatus(String.valueOf(Constants.CHAT_RECEIVED));
                chatMsg.setSender(Constants.CHAT_FROM_RECEIVER);
                chatMsg.setMsgType(msgType);
                chatMsg.setMsgChatType(chatType);
                chatMsg.setChatToUser(chatFrom);
                chatMsg.setChatReceivers(mApplication.getStringFromPreference(Constants.USERNAME));
                msgBody = URLDecoder.decode(msgBody, "UTF-8");

                JSONObject jsonObject = new JSONObject(msgBody);
                chatMsg.setMsgMediaIsDownloaded(String.valueOf(Constants.MEDIA_DOWNLADED));
                if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT)) {
                    msgBody = jsonObject.getString(Constants.PARAM_MESSAGE);
                    chatMsg.setMsgBody(msgBody);
                } else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)
                        || msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)) {
                    msgBody = Constants.EMPTY_STRING;
                    chatMsg.setMsgBody(jsonObject.getString(msgType));
                } else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE)
                        || msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)
                        || msgType.equalsIgnoreCase(Constants.MSG_TYPE_AUDIO)
                        || msgType.equalsIgnoreCase(Constants.MSG_TYPE_FILE)) {
                    chatMsg.setFileName(jsonObject.getString(Constants.CHAT_MEDIA_NAME));
                    chatMsg.setMsgBody(jsonObject.getString(Constants.PARAM_MESSAGE));
                    chatMsg.setMsgMediaServerPath(jsonObject.getString(Constants.PARAM_FILE_URL));
                    chatMsg.setMsgMediaEncImage(jsonObject.getString(Constants.PARAM_FILE_THUMB));
                    chatMsg.setMsgMediaSize(jsonObject.getString(Constants.PARAM_FILE_SIZE));
                    chatMsg.setMsgMediaIsDownloaded(String.valueOf(Constants.MEDIA_NOT_DOWNLOADED));

                }
                chatMsg.setMsgVideoThumb(Constants.EMPTY_STRING);
                chatMsg.setMsgMediaLocalPath(Constants.EMPTY_STRING);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return chatMsg;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Start command", String.valueOf(startId));
        sendToServiceHandler(startId, intent);
        return START_STICKY;
    }

    /**
     * Send to service handler.
     *
     * @param startId the start id
     * @param intent  the intent
     */
    public void sendToServiceHandler(int startId, Intent intent) {
        if (sServiceHandler != null) {
            Log.e("Start command", String.valueOf(startId));
            Message msg = sServiceHandler.obtainMessage();
            Log.e("Start command", String.valueOf(msg));
            msg.arg1 = startId;
            msg.obj = intent;
            sServiceHandler.sendMessage(msg);
        }
    }

    /**
     * Upload image.
     *
     * @param chatmsg the chatmsg
     */
    private void uploadImage(ChatMsg chatmsg) {
        try {
            String filePath = mApplication.returnEmptyStringIfNull(chatmsg.getMsgMediaLocalPath());
            if (!filePath.isEmpty()) {
                ImageUploadS3 imgTask = new ImageUploadS3(getApplicationContext());
                imgTask.uplodingCallback(new UploadTaskFinished(chatmsg));
                Uri selectedImage = Uri.parse(filePath);
                File file = new File(filePath);
                String path = file.getAbsolutePath();
                Bitmap bitmap;
                if (path.startsWith("content"))
                    bitmap = mApplication.decodeStrem(file, selectedImage, this);
                else
                    bitmap = mApplication.decodeFile(file, 1);

                ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
                imgTask.executeUpload(file, chatmsg.getMsgType(), chatmsg.getMsgVideoThumb(),"POLLS/");
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private void compressImage(ChatMsg chatMsg) {
        new CompressImage(chatMsg).execute();
    }

    /**
     * Upload video.
     *
     * @param msg the msg
     */
    /**
     * Upload video.
     *
     * @param msg the msg
     */
    private void uploadVideo(final ChatMsg msg) {
        try {
            final File videoFile = new File(msg.getMsgMediaLocalPath());
            final String filePath = mApplication.returnEmptyStringIfNull(msg.getMsgMediaLocalPath());

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoFile.getAbsolutePath(),
                    MediaStore.Images.Thumbnails.MICRO_KIND);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumb.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            final String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Utils.startCompressing(filePath, this, new Utils.VideoCompression() {
                @Override
                public void onCompressionCompleted(String path) {
                    ImageUploadS3 imgTask = new ImageUploadS3(getApplicationContext());
                    imgTask.uplodingCallback(new UploadTaskFinished(msg));
                    imgTask.executeUpload(videoFile, Constants.MSG_TYPE_VIDEO, encodedImage,"POLLS/");
                }

            });
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }


    /**
     * Download file from server.
     *
     * @param url         url of the file
     * @param mid         mid to save the status
     * @param messageType message type
     */
    private void downloadFileFromServer(String url, String mid, String messageType) {
        InputStream input = null;
        OutputStream output = null;
        try {
            URL urls = new URL(url);
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            URLConnection conexion = urls.openConnection();
            conexion.connect();
            input = new BufferedInputStream(urls.openStream());
            String folderName;
            String basePath = getString(R.string.app_name) + "/";
            if (Constants.MSG_TYPE_IMAGE.equals(messageType)) {
                folderName = basePath + MediaPaths.MEDIA_PATH_IMAGE;
            } else if (Constants.MSG_TYPE_AUDIO.equals(messageType)) {
                folderName = basePath + MediaPaths.MEDIA_PATH_AUDIO;
            } else if (Constants.MSG_TYPE_VIDEO.equals(messageType)) {
                folderName = basePath + MediaPaths.MEDIA_PATH_VIDEO;
            } else {
                folderName = basePath + MediaPaths.MEDIA_PATH_FILE;
            }
            File file = new File(Environment.getExternalStorageDirectory() + "/" + folderName + "/" + fileName);
            File folder = file.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            output = new FileOutputStream(file);
            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            // Download Success.
            output.flush();
            Utils.scanMedia(this, file.getAbsolutePath());
            updateMediaStatus(file.getAbsolutePath(), mid, Constants.MEDIA_DOWNLADED, url);
        } catch (Exception e) {
            // Download Failed
            LogMessage.e(e);
            updateMediaStatus(Constants.EMPTY_STRING, mid, Constants.MEDIA_NOT_DOWNLOADED, url);
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                    LogMessage.e(e);
                }

            if (output != null)
                try {
                    input.close();
                } catch (IOException e) {
                    LogMessage.e(e);
                }
        }
    }

    /**
     * Update media status. sendMessage
     *
     * @param filePath  the file path
     * @param msgId     the msg id
     * @param status    the status
     * @param serverUrl the server url
     */
    private void updateMediaStatus(String filePath, String msgId, int status, String serverUrl) {
        ContentValues values = new ContentValues();
        values.put(Constants.CHAT_MSG_IS_DOWNLOADED, String.valueOf(status));
        values.put(Constants.CHAT_COLUMN_STATUS, String.valueOf(status));
        values.put(Constants.CHAT_MEDIA_LOCAL_PATH, filePath);
        values.put(Constants.CHAT_MEDIA_SERVER_PATH, serverUrl);
        MDatabaseHelper.updateValues(Constants.TABLE_CHAT_DATA, values, Constants.CHAT_COLUMN_ID + "='" + msgId + "'");
        values = new ContentValues();
        values.put(Constants.RECENT_CHAT_STATUS, String.valueOf(status));
        MDatabaseHelper.updateValues(Constants.TABLE_RECENT_CHAT_DATA, values,
                Constants.RECENT_CHAT_USER_ID + "='" + msgId + "'");
        Intent intent = new Intent(Constants.ACTION_MEDIA_UPLOAD_STATUS);
        intent.putExtra(Constants.CHAT_MSG_ID, msgId);
        getApplicationContext().sendBroadcast(intent);
    }

    private void updateGroupAddedUser(String gid, Rosters item, String addedUser) {
        String groupUsers = item.getRosterGroupUsers();
        List<String> users = new LinkedList<>(Arrays.asList(groupUsers.split(",")));
        if (!users.contains(addedUser)) {
            users.add(addedUser);
            ContentValues groupValues = new ContentValues();
            groupValues.put(Constants.ROSTER_GROUP_USERS, TextUtils.join(",", users));

            String groupWhere = Constants.ROSTER_USER_ID + "='" + gid + "'";
            MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, groupValues, groupWhere);

            MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_USER_ID + "=?",
                    new String[]{gid});

            List<Rosters> newUserRosterInfo = MDatabaseHelper.getRosterInfo(addedUser,
                    Constants.EMPTY_STRING);
            String name = addedUser;
            if (newUserRosterInfo != null && !newUserRosterInfo.isEmpty()) {
                name = newUserRosterInfo.get(0).getRosterName();
            }
            String message = String.format(getString(R.string.text_group_new_user), name);
            new ChatUtils(getApplicationContext()).insertGroupStatusMessage(gid, message);
        }
    }

    private void updateGroupNameOrImage(String gid, Rosters item, String groupName, String groupImage) {

        String roasterName = Utils.getDecodedString(item.getRosterName());
        if (!groupImage.isEmpty() && !item.getRosterImage().equals(groupImage)) {
            ContentValues groupValues = new ContentValues();
            groupValues.put(Constants.ROSTER_IMAGE, groupImage);
            String groupWhere = Constants.ROSTER_USER_ID + "='" + gid + "'";
            MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, groupValues, groupWhere);
            new ChatUtils(getApplicationContext())
                    .insertGroupStatusMessage(gid,
                            getString(R.string.text_group_image_changed));
            Intent intent = new Intent(Constants.CONTUS_XMPP_GROUPINFO_CHANGED);
            getApplicationContext().sendBroadcast(intent);
        } else if (!groupName.isEmpty() && !roasterName.equals(groupName)) {
            ContentValues groupValues = new ContentValues();
            groupValues.put(Constants.ROSTER_NAME, groupName);
            String groupWhere = Constants.ROSTER_USER_ID + "='" + gid + "'";
            MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, groupValues, groupWhere);
            String message = String.format(getString(R.string.text_group_name_changed),
                    groupName);
            new ChatUtils(getApplicationContext())
                    .insertGroupStatusMessage(gid, message);
            Intent intent = new Intent(Constants.CONTUS_XMPP_GROUPINFO_CHANGED);
            getApplicationContext().sendBroadcast(intent);

        }
    }

    /**
     * The Class ServiceHandler.
     */
    class ServiceHandler extends Handler {

        /**
         * Instantiates a new service handler.
         *
         * @param looper the looper
         */
        public ServiceHandler(Looper looper) {
            super(looper);
        }


        /**
         * Handle message.
         *
         * @param message the message
         */
        @Override
        public void handleMessage(Message message) {
            try {
                handleEvent((Intent) message.obj);
            } catch (Exception e) {
                LogMessage.e(e);
            }
        }
    }

    private class LoginListener implements XMPPLoginListener {

        @Override
        public void onLogin() {
            Utils.sendPendingMessages(ChatService.this);
            Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_REQUEST);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void onConnectionClosed() {
            stopSelf();
        }

        @Override
        public void onConnectionError() {
            if (mApplication.isNetConnected(ChatService.this)) {
                if (!XMPPConnectionController.isBindError)
                    controller.login();
            } else {
                stopSelf();
            }
        }
    }

    private class ProfileCallbackListener implements RosterListener {

        @Override
        public void profileUpdated(Boolean isUpDated) {
            Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_PROFILE_UPDATED);
            intent.putExtra("prfUpdated", isUpDated);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void rosterCallback(List<Contact> contacts) {
            if (contacts != null && !contacts.isEmpty()) {
                List<String> userIds = new ArrayList<>();
                for (int i = 0, count = contacts.size(); i < count; i++) {
                    Contact rosterObj = contacts.get(i);
                    String rosterName = rosterObj.getName();
                    if (!"Unknown".equals(rosterName) && rosterName != null && !rosterName.isEmpty()) {
                        String rosterId = rosterObj.getUser();
                        userIds.add(rosterId);
                        ContentValues mValues = new ContentValues();
                        mValues.put(Constants.ROSTER_IMAGE, rosterObj.getImage());
                        mValues.put(Constants.ROSTER_AVAILABILITY,
                                String.valueOf(getAvailability(rosterObj.getAvailability())));
                        mValues.put(Constants.ROSTER_LAST_SEEN, Constants.EMPTY_STRING);
                        mValues.put(Constants.ROSTER_STATUS, rosterObj.getStatus());
                        mValues.put(Constants.ROSTER_TYPE, String.valueOf(Constants.COUNT_ZERO));
                        mValues.put(Constants.ROSTER_ADMINS, Constants.EMPTY_STRING);
                        mValues.put(Constants.ROSTER_IS_BLOCKED, rosterObj.getBlocked());
                        String name = rosterName.trim().substring(0, 1).toUpperCase() + rosterName.substring(1, rosterName.length());
                        try {
                            mValues.put(Constants.ROSTER_NAME, URLDecoder.decode(name, "UTF-8").replaceAll("%", "%25"));
                        } catch (UnsupportedEncodingException e) {
                            LogMessage.e(e);
                        }
                        updateRoster(rosterId, mValues);

                        if (MDatabaseHelper.checkIDStatus(rosterId, Constants.TABLE_RECENT_CHAT_DATA,
                                Constants.RECENT_CHAT_USER_ID)) {
                            String where = Constants.RECENT_CHAT_USER_ID + "='" + rosterId + "'";
                            ContentValues values = new ContentValues();
                            values.put(Constants.RECENT_CHAT_NAME, rosterObj.getName().trim());
                            MDatabaseHelper.updateValues(Constants.TABLE_RECENT_CHAT_DATA, values, where);
                        }
                        mApplication.storeStringInPreference(Constants.LAST_SYNC_TIME, rosterObj.getLastSeen());
                    }
                }
                List<Rosters> nonContacts = MDatabaseHelper.getNonRosters(TextUtils.join(",", userIds), String.valueOf(Constants.COUNT_ZERO));
                for (int i = 0, size = nonContacts.size(); i < size; i++) {
                    Rosters nonContact = nonContacts.get(i);
                    String username = nonContact.getRosterID();
                    List<Rosters> groups = MDatabaseHelper.getGroupsOfUser(username, String.valueOf(Constants.COUNT_ONE));
                    for (int j = 0, groupSize = groups.size(); j < groupSize; j++) {
                        Rosters group = groups.get(j);
                        updateGroupRemovedUser(group.getRosterID(), group, username, username);
                    }
                    MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_USER_ID + "=?", new String[]{username});
                }
            }
        }


        @Override
        public void userProfileCallback(Contact contact) {
            mApplication.storeStringInPreference(Constants.USER_PROFILE_NAME, contact.getName());
            mApplication.storeStringInPreference(Constants.USER_IMAGE, contact.getImage());
            if (contact.getStatus() != null && !contact.getStatus().isEmpty())
                mApplication.storeStringInPreference(Constants.USER_STATUS, contact.getStatus());
            Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_ACTION_GET_PROFILE);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void userProfileUpdateCallback(com.contus.chatlib.models.ChatMessage chatMessage) {
            if (mApplication.getBooleanFromPreference(Constants.IS_CONTACTS_SYNCED))
                controller.getContacts();
        }

        @Override
        public void presenceCallback(String userName, String status) {
            if ("online".equalsIgnoreCase(status)) {
                ContentValues mValues = new ContentValues();
                mValues.put(Constants.ROSTER_LAST_SEEN, String.valueOf(Constants.COUNT_ONE));
                if (MDatabaseHelper.checkIDStatus(userName, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID))
                    MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, mValues,
                            Constants.ROSTER_USER_ID + "='" + userName + "'");
                Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_PRESENCE_CHANGE);
                getApplicationContext().sendBroadcast(intent);
            } else {
                controller.getAvailability(userName);
            }
        }

        @Override
        public void deleteAccount(String result, String resultType) {
            Intent intent = new Intent(Constants.ACTION_DELETE_ACCOUNT);
            intent.putExtra(Constants.RESULT, result);
            intent.putExtra(Constants.RESULT_TYPE, resultType);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void getCurrentAvailability(String userName, String availability, String lastSeen) {
            String currentAvailability;
            ContentValues values = new ContentValues();
            if (("1").equalsIgnoreCase(availability))
                currentAvailability = availability;
            else
                currentAvailability = lastSeen;
            values.put(Constants.ROSTER_LAST_SEEN, currentAvailability);
            MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, values,
                    Constants.ROSTER_USER_ID + "='" + userName + "'");
            String ongoingChatPerson = mApplication
                    .returnEmptyStringIfNull(mApplication.getStringFromPreference(Constants.CHAT_ONGOING_NAME));
            if (ongoingChatPerson.equals(userName)) {
                Intent intent = new Intent(Constants.ACTION_AVAILABILITY_RESULT);
                intent.putExtra(Constants.ROSTER_LAST_SEEN, currentAvailability);
                intent.putExtra(Constants.ROSTER_USER_ID, userName);
                getApplicationContext().sendBroadcast(intent);
            }
        }

        @Override
        public void onSessionExpire(String message) {
            Intent intent = new Intent(Constants.ACTION_SESSION_EXPIRE);
            intent.putExtra(Constants.RESULT, message);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void onSessionCleared() {
            loadConnection("");
        }
    }

    private class GroupCallbackListener implements GroupListener {

        @Override
        public void groupIdAdded(String groupId) {
            Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_GET_GROUPID);
            intent.putExtra(Constants.GROUP_ID, groupId);
            getBaseContext().sendBroadcast(intent);
        }

        @Override
        public void groupMemberAdded(String result, String resultType) {
            Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_GROUPMEMBER_ADDED);
            intent.putExtra(Constants.GROUP_ID, result);
            intent.putExtra(Constants.TITLE_TYPE, resultType);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void groupExitResponse(String response) {
            Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP_RESPONSE);
            intent.putExtra("response", response);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void groupRemoveMemberResponse(String response) {
            Intent intent = new Intent(Constants.GROUP_MEMBER_REMOVED);
            intent.putExtra(Constants.GROUP_ID, response);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void groupInfoUpdatedResponse(String response) {
            Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_GROUPINFO_UPDATE);
            intent.putExtra("response", response);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void groupChatNewUser(com.contus.chatlib.models.ChatMessage chatmsg) {
            LogMessage.v("grouppp", "::getGroupId::" + chatmsg.getFrom());

            String gid = chatmsg.getFrom();
            String addedUser = chatmsg.getSender();

            String loginUsername = mApplication.getStringFromPreference(Constants.USERNAME);
            List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(gid,
                    Constants.ROSTER_GROUP_USERS);
            if (!loginUsername.equalsIgnoreCase(addedUser) && rosterInfo != null && !rosterInfo.isEmpty()) {
                Rosters item = rosterInfo.get(0);
                updateGroupAddedUser(gid, item, addedUser);
            } else {
                controller.getGroupInfo(gid);
            }

            Intent intent = new Intent(Constants.CONTUS_ADD_GROUP_USER);
            intent.putExtra(Constants.GROUP_ID, chatmsg.getFrom());
            intent.putExtra(Constants.RESOURCE, chatmsg.getSender());
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void groupChatRemoveUser(com.contus.chatlib.models.ChatMessage chatmsg) {
            String gid = chatmsg.getFrom();
            String removedUser = chatmsg.getSender();
            String removingUser = chatmsg.getRemovingUser();
            controller.sendMessageReceipt(removingUser, chatmsg.getMid(), Constants.CHAT, Constants.RECEIPT,
                    String.valueOf(System.currentTimeMillis() / 1000));
            if (MDatabaseHelper.checkIDStatus(gid, Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_USER_ID)) {
                List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(gid,
                        Constants.ROSTER_GROUP_USERS);
                Rosters item = rosterInfo.get(0);
                updateGroupRemovedUser(gid, item, removedUser, removingUser);
            } else {
                controller.getGroupInfo(gid);
            }

            Intent intent = new Intent(Constants.CONTUS_REMOVE_GROUP_USER);
            intent.putExtra(Constants.GROUP_ID, chatmsg.getFrom());
            intent.putExtra(Constants.RESOURCE, chatmsg.getSender());
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void groupChatInfoUpdate(com.contus.chatlib.models.ChatMessage chatmsg) {
            String gid = chatmsg.getFrom();
            List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(gid,
                    Constants.ROSTER_GROUP_USERS);
            if (rosterInfo != null && !rosterInfo.isEmpty()) {
                Rosters item = rosterInfo.get(0);
                String groupImage = mApplication.returnEmptyStringIfNull(chatmsg.getGroupImage());
                String groupName = mApplication.returnEmptyStringIfNull(chatmsg.getGroupName());
                if (groupImage.contains("\\")) {
                    groupImage = groupImage.replace("\\\\", "\\");
                }
                if (groupName.contains("\\")) {
                    groupName = groupName.replace("\\\\", "\\");
                }
                updateGroupNameOrImage(gid, item, Utils.getDecodedString(groupName), groupImage);
            } else {
                controller.getGroupInfo(gid);
            }
        }

        @Override
        public void groupsCallback(List<Group> groups) {

            if (groups != null && !groups.isEmpty()) {
                for (int i = 0, count = groups.size(); i < count; i++) {
                    Group rosterObj = groups.get(i);

                    Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_GET_GROUP_INFO);
                    getApplicationContext().sendBroadcast(intent);

                    LogMessage.v("grouppp pending messages", "::size::" + smackGroupChatMessages.size());
                    try {
                        String groupImage = mApplication.returnEmptyStringIfNull(rosterObj.getImage());
                        String groupAdmin = mApplication.returnEmptyStringIfNull(rosterObj.getAdmin());
                        String groupUser = mApplication.returnEmptyStringIfNull(rosterObj.getUsers());
                        ContentValues mValues = new ContentValues();
                        mValues.put(Constants.ROSTER_NAME, URLDecoder.decode(rosterObj.getName(), "UTF-8").replaceAll("%", "%25"));
                        mValues.put(Constants.ROSTER_IMAGE, groupImage);
                        mValues.put(Constants.ROSTER_LAST_SEEN, Constants.EMPTY_STRING);
                        mValues.put(Constants.ROSTER_AVAILABILITY, Constants.EMPTY_STRING);
                        mValues.put(Constants.ROSTER_STATUS, Constants.EMPTY_STRING);
                        mValues.put(Constants.ROSTER_TYPE, String.valueOf(Constants.COUNT_ONE));
                        mValues.put(Constants.ROSTER_ADMINS, groupAdmin);
                        mValues.put(Constants.ROSTER_GROUP_USERS, groupUser);
                        mValues.put(Constants.ROSTER_IS_BLOCKED, Constants.EMPTY_STRING);

                        if (groupUser.contains(mApplication.getStringFromPreference(Constants.USERNAME)))
                            mValues.put(Constants.ROSTER_GROUP_STATUS, String.valueOf(Constants.COUNT_ZERO));
                        else
                            mValues.put(Constants.ROSTER_GROUP_STATUS, String.valueOf(Constants.COUNT_ONE));

                        List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(rosterObj.getId(),
                                Constants.ROSTER_GROUP_USERS);
                        if (rosterInfo != null && !rosterInfo.isEmpty()) {
                            String where = Constants.ROSTER_USER_ID + "='" + rosterObj.getId() + "'";
                            MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, mValues, where);
                            Rosters rosters = rosterInfo.get(0);
                            if (rosters.getRosterGroupStatus().equals(String.valueOf(Constants.COUNT_ONE))
                                    && groupUser.contains(mApplication.getStringFromPreference(Constants.USERNAME))) {
                                String name = "You";
                                String message = String.format(getString(R.string.text_group_new_user), name);
                                new ChatUtils(getApplicationContext()).insertGroupStatusMessage(rosterObj.getId(), message);

                                intent = new Intent(Constants.CONTUS_ADD_GROUP_USER);
                                intent.putExtra(Constants.GROUP_ID, rosters.getRosterID());
                                intent.putExtra(Constants.RESOURCE, mApplication.getStringFromPreference(Constants.USERNAME));
                                getApplicationContext().sendBroadcast(intent);
                            }
                        } else {
                            mValues.put(Constants.ROSTER_USER_ID, rosterObj.getId());
                            mValues.put(Constants.ROSTER_CUSTOM_TONE, Constants.EMPTY_STRING);
                            MDatabaseHelper.insertValues(Constants.TABLE_ROSTER, mValues);

                            new ChatUtils(getApplicationContext())
                                    .insertGroupStatusMessage(rosterObj.getId(), getString(R.string.text_group_created));
                        }

                        String[] groupUsers = groupUser.split(",");
                        String loggedInUser = mApplication.getStringFromPreference(Constants.USERNAME);
                        for (String userName : groupUsers) {
                            if (!userName.equalsIgnoreCase(loggedInUser) && !MDatabaseHelper.checkIDStatus(userName,
                                    Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID)) {
                                Utils.insertUnknownUser(userName);
                            }
                        }
                        if (smackGroupChatMessages.size() > 0) {
                            for (com.contus.chatlib.models.ChatMessage chatMessage : smackGroupChatMessages) {
                                receivedMessage(chatMessage);
                            }
                            smackGroupChatMessages.clear();
                        }
                        if (!MDatabaseHelper.checkIDStatus(msgTo, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID))
                            intent = new Intent(Constants.CONTUS_NEW_GROUP);

                        getApplicationContext().sendBroadcast(intent);
                    } catch (Exception e) {
                        LogMessage.e(e);
                    }
                }

            }

        }

        @Override
        public void groupCallback(Group group) {
            synchronized (this) {
                LogMessage.v("grouppp pending messages", "::size::" + smackGroupChatMessages.size());
                try {
                    String groupImage = mApplication.returnEmptyStringIfNull(group.getImage());
                    String groupAdmin = mApplication.returnEmptyStringIfNull(group.getAdmin());
                    String groupUser = mApplication.returnEmptyStringIfNull(group.getUsers());
                    ContentValues mValues = new ContentValues();
                    mValues.put(Constants.ROSTER_NAME, URLDecoder.decode(group.getName(), "UTF-8").replaceAll("%", "%25"));
                    mValues.put(Constants.ROSTER_IMAGE, groupImage);
                    mValues.put(Constants.ROSTER_LAST_SEEN, Constants.EMPTY_STRING);
                    mValues.put(Constants.ROSTER_AVAILABILITY, Constants.EMPTY_STRING);
                    mValues.put(Constants.ROSTER_STATUS, Constants.EMPTY_STRING);
                    mValues.put(Constants.ROSTER_TYPE, String.valueOf(Constants.COUNT_ONE));
                    mValues.put(Constants.ROSTER_ADMINS, groupAdmin);
                    mValues.put(Constants.ROSTER_GROUP_USERS, groupUser);
                    mValues.put(Constants.ROSTER_IS_BLOCKED, Constants.EMPTY_STRING);

                    if (groupUser.contains(mApplication.getStringFromPreference(Constants.USERNAME)))
                        mValues.put(Constants.ROSTER_GROUP_STATUS, String.valueOf(Constants.COUNT_ZERO));
                    else
                        mValues.put(Constants.ROSTER_GROUP_STATUS, String.valueOf(Constants.COUNT_ONE));

                    List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(group.getId(),
                            Constants.ROSTER_GROUP_USERS);
                    if (rosterInfo != null && !rosterInfo.isEmpty()) {
                        String where = Constants.ROSTER_USER_ID + "='" + group.getId() + "'";
                        MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, mValues, where);
                        Rosters rosters = rosterInfo.get(0);
                        if (rosters.getRosterGroupStatus().equals(String.valueOf(Constants.COUNT_ONE))
                                && groupUser.contains(mApplication.getStringFromPreference(Constants.USERNAME))) {
                            String name = "You";
                            String message = String.format(getString(R.string.text_group_new_user), name);
                            new ChatUtils(getApplicationContext()).insertGroupStatusMessage(group.getId(), message);

                            Intent intent = new Intent(Constants.CONTUS_ADD_GROUP_USER);
                            intent.putExtra(Constants.GROUP_ID, rosters.getRosterID());
                            intent.putExtra(Constants.RESOURCE, mApplication.getStringFromPreference(Constants.USERNAME));
                            getApplicationContext().sendBroadcast(intent);
                        }
                    } else {
                        mValues.put(Constants.ROSTER_USER_ID, group.getId());
                        mValues.put(Constants.ROSTER_CUSTOM_TONE, Constants.EMPTY_STRING);
                        MDatabaseHelper.insertValues(Constants.TABLE_ROSTER, mValues);

                        new ChatUtils(getApplicationContext())
                                .insertGroupStatusMessage(group.getId(), getString(R.string.text_group_created));
                    }

                    String[] groupUsers = groupUser.split(",");
                    String loggedInUser = mApplication.getStringFromPreference(Constants.USERNAME);
                    for (String userName : groupUsers) {
                        if (!userName.equalsIgnoreCase(loggedInUser) && !MDatabaseHelper.checkIDStatus(userName,
                                Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID)) {
                            Utils.insertUnknownUser(userName);
                        }
                    }
                    if (smackGroupChatMessages.size() > 0) {
                        for (com.contus.chatlib.models.ChatMessage chatMessage : smackGroupChatMessages) {
                            receivedMessage(chatMessage);
                        }
                        smackGroupChatMessages.clear();
                    }
                    if (!MDatabaseHelper.checkIDStatus(msgTo, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID)) {
                        Intent intent = new Intent(Constants.CONTUS_NEW_GROUP);
                        getApplicationContext().sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(Constants.CONTUS_XMPP_GROUPINFO_CHANGED);
                        getApplicationContext().sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    LogMessage.e(e);
                }
            }
        }
    }

    class SmackMessageListener implements com.contus.chatlib.listeners.MessageListener {

        @Override
        public void messageCallback(com.contus.chatlib.models.ChatMessage chatMessage) {
            receivedMessage(chatMessage);
        }

        @Override
        public void messageAcknowledgementCallback(com.contus.chatlib.models.ChatMessage chatMessage) {
            String id = chatMessage.getMid();
            String user = chatMessage.getFrom();
            id = id.replaceAll("-" + user, "");
            String isSent = MDatabaseHelper.getSpecificChat(id);
            if (Constants.GROUP_CHAT.equalsIgnoreCase(chatMessage.getChat_type()) && isSent.equals(String.valueOf(Constants.CHAT_SENT_CLOCK))) {
                updateGrpMsgAck(id, user, Constants.CHAT_REACHED_TO_SERVER, user);
                updateMsgStatus(chatMessage, Constants.CHAT_REACHED_TO_SERVER);
                Intent intent = new Intent(Constants.ACTION_MESSAGE_SENT_TO_SERVER);
                intent.putExtra(Constants.CHAT_MSG_ID, id);
                getApplicationContext().sendBroadcast(intent);
            } else if (chatMessage.getChat_type().equalsIgnoreCase(Constants.RECEIPT)) {
                updateGrpMsgAck(id, user, CHAT_RECEIVED_RECEIPT_SENT, user);
                updateMsgStatus(chatMessage, CHAT_RECEIVED_RECEIPT_SENT);
            } else if (chatMessage.getChat_type().equalsIgnoreCase(Constants.SEEN)) {
                updateGrpMsgAck(id, user, Constants.CHAT_RECEIVED_SEEN_SENT, user);
                updateMsgStatus(chatMessage, Constants.CHAT_RECEIVED_SEEN_SENT);
            } else if (chatMessage.getChat_type().equalsIgnoreCase(Constants.PROFILE_UPDATE)) {
                if (mApplication.getBooleanFromPreference(Constants.IS_CONTACTS_SYNCED))
                    controller.getContacts();
            } else if (!id.isEmpty() && isSent.equals(String.valueOf(Constants.CHAT_SENT_CLOCK))) {
                updateGrpMsgAck(id, user, Constants.CHAT_REACHED_TO_SERVER, user);
                updateMsgStatus(chatMessage, Constants.CHAT_REACHED_TO_SERVER);
                Intent intent = new Intent(Constants.ACTION_MESSAGE_SENT_TO_SERVER);
                intent.putExtra(Constants.CHAT_MSG_ID, id);
                getApplicationContext().sendBroadcast(intent);
            }
        }

        @Override
        public void messageReceiptCallback(com.contus.chatlib.models.ChatMessage chatMessage) {
            String id = chatMessage.getMid();
            String user = chatMessage.getFrom();
            id = id.replaceAll("-" + user, "");

            updateGrpMsgAck(id, user, Constants.CHAT_REACHED_TO_RECEIVER, user);
            if (!Constants.GROUP_CHAT.equalsIgnoreCase(chatMessage.getChat_type()))
                updateMsgStatus(chatMessage, Constants.CHAT_REACHED_TO_RECEIVER);
            Intent intent = new Intent(Constants.ACTION_RECIPET_ACK_FROM_ROSTER);
            intent.putExtra(Constants.CHAT_MSG_ID, id);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void messageSeenCallback(com.contus.chatlib.models.ChatMessage chatMessage) {
            String id = chatMessage.getMid();
            String user = chatMessage.getFrom();
            id = id.replaceAll("-" + user, "");
            updateGrpMsgAck(id, user, Constants.CHAT_SEEN_BY_RECEIVER, chatMessage.getFrom());
            if (!Constants.GROUP_CHAT.equalsIgnoreCase(chatMessage.getChat_type()))
                updateMsgStatus(chatMessage, Constants.CHAT_SEEN_BY_RECEIVER);

            Intent intent = new Intent(Constants.ACTION_MSG_SEEN_FROM_ROSTER);
            intent.putExtra(Constants.CHAT_MSG_ID, chatMessage.getMid());
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void getTypingStatus(String userGrpId, String status, String userId) {
            String ongoingChatPerson = mApplication
                    .returnEmptyStringIfNull(mApplication.getStringFromPreference(Constants.CHAT_ONGOING_NAME));
            if (ongoingChatPerson.equals(userGrpId)) {
                Intent intent = new Intent(ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE_RESULT);
                intent.putExtra("user_grp_id", userGrpId);
                intent.putExtra("status", status);
                intent.putExtra("user_id", userId);
                getApplicationContext().sendBroadcast(intent);
            }
        }

        @Override
        public void deleteMessage(boolean result) {
            Intent intent = new Intent(Constants.DELETE_MESSAGE);
            intent.putExtra("result", result);
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void clearChat(boolean result) {
            Intent intent = new Intent(Constants.CLEAR_CHAT);
            intent.putExtra("result", result);
            getApplicationContext().sendBroadcast(intent);
        }

        /**
         * Update grp msg ack.
         *
         * @param chatMessage the chat message
         */
        /**
         * Update grp msg ack.
         *
         * @param mid    message id
         * @param from   message from
         * @param status message status
         * @param userId user id
         */
        private void updateGrpMsgAck(String mid, String from, int status, String userId) {
            ContentValues mValues = new ContentValues();
            if (status == Constants.CHAT_REACHED_TO_SERVER) {
                mValues.put(Constants.CHAT_MSG_ID, mid);
                mValues.put(Constants.CHAT_MSG_STATUS, String.valueOf(status));
                mValues.put(Constants.CHAT_IS_ACK, String.valueOf(Constants.COUNT_ONE));
                mValues.put(Constants.GROUP_ID, userId);
                MDatabaseHelper.insertValues(Constants.TABLE_GROUP_MSG_STATUS, mValues);
            } else if (MDatabaseHelper.getMessageInfo(mid, from)) {
                mValues.put(Constants.CHAT_MSG_STATUS, String.valueOf(status));
                mValues.put(Constants.CHAT_USER_ID, from);
                String where = Constants.CHAT_MSG_ID + "='" + mid + "' AND " + Constants.CHAT_USER_ID + "='" + from + "'";
                MDatabaseHelper.updateValues(Constants.TABLE_GROUP_MSG_STATUS, mValues, where);
            } else {
                mValues.put(Constants.CHAT_USER_ID, from);
                mValues.put(Constants.CHAT_MSG_ID, mid);
                mValues.put(Constants.CHAT_MSG_STATUS, String.valueOf(status));
                mValues.put(Constants.CHAT_IS_ACK, String.valueOf(Constants.COUNT_ONE));
                mValues.put(Constants.GROUP_ID, userId);
                MDatabaseHelper.insertValues(Constants.TABLE_GROUP_MSG_STATUS, mValues);
            }
        }
    }

    /**
     * Class to compress the image and upload to server after compress
     */
    private class CompressImage extends AsyncTask<Void, Void, String> {

        private ChatMsg chatMsg;

        private CompressImage(ChatMsg chatMsg) {
            this.chatMsg = chatMsg;
        }

        @Override
        protected String doInBackground(Void... params) {
            String filePath = mApplication.returnEmptyStringIfNull(chatMsg.getMsgMediaLocalPath());
            String outFilePath = ImageCompressor.getCompressImageFilePath(ChatService.this);
            ImageCompressor.compressImage(filePath, outFilePath, true);
            Bitmap thumb = ImageCompressor.compressImageWithSize(filePath, 50f, 50f, "", false);

            ByteArrayOutputStream thumbStream = new ByteArrayOutputStream();
            thumb.compress(Bitmap.CompressFormat.JPEG, 90, thumbStream);
            byte[] thumbArray = thumbStream.toByteArray();
            thumbStream.reset();
            String encodedImage = Base64.encodeToString(thumbArray, Base64.DEFAULT);
            chatMsg.setMsgVideoThumb(encodedImage);
            return outFilePath;
        }

        @Override
        protected void onPostExecute(String compressPath) {
            super.onPostExecute(compressPath);
            chatMsg.setMsgMediaLocalPath(compressPath);
            uploadImage(chatMsg);
        }
    }

    /**
     * The Class UploadTaskFinished.
     */
    private class UploadTaskFinished implements OnTaskCompleted {

        /**
         * The m chat msg.
         */
        ChatMsg mChatMsg;

        /**
         * Instantiates a new upload task finished.
         *
         * @param chatmsg the chatmsg
         */
        public UploadTaskFinished(ChatMsg chatmsg) {
            this.mChatMsg = chatmsg;
        }

        @Override
        public void onTaskCompleted(URL url, String type, String encodedImg, int fileSize) {
            if (url != null) {
                LogMessage.v("chat service::::", "Task Completed::::" + url);
                String urlString = url.toString();
                ContentValues mValues = new ContentValues();
                mValues.put(Constants.CHAT_MSG_IS_DOWNLOADED, String.valueOf(Constants.MEDIA_UPLOADED));
                mValues.put(Constants.CHAT_MEDIA_SERVER_PATH, urlString);
                mValues.put(Constants.CHAT_ENCODED_IMAGE, encodedImg);
                mValues.put(Constants.CHAT_FILE_SIZE, String.valueOf(fileSize));
                MDatabaseHelper.updateValues(Constants.TABLE_CHAT_DATA, mValues,
                        Constants.CHAT_COLUMN_ID + "='" + mChatMsg.getMsgId() + "'");
                String[] users = null;
                if (mChatMsg.getChatReceivers() != null)
                    users = mChatMsg.getChatReceivers().split(",");
                sendChatMessage(mChatMsg.getMsgType(), mChatMsg.getMsgFrom(),
                        mChatMsg.getMsgTo(), mChatMsg.getMsgId(), Integer.parseInt(mChatMsg.getMsgChatType()),
                        mChatMsg.getMsgBody(), users, mChatMsg.getMsgTime());
                Intent intent = new Intent(Constants.ACTION_MEDIA_UPLOADED);
                intent.putExtra(Constants.CHAT_MSG_TYPE, type);
                intent.putExtra(Constants.CHAT_MSG_ID, mChatMsg.getMsgId());
                intent.putExtra(Constants.CHAT_MEDIA_NAME, mChatMsg.getFileName());
                getApplicationContext().sendBroadcast(intent);
            } else {
                updateMediaStatus(type, mChatMsg.getMsgId(), Constants.MEDIA_NOT_UPLOADED,
                        Constants.EMPTY_STRING);
            }
        }
    }
}
