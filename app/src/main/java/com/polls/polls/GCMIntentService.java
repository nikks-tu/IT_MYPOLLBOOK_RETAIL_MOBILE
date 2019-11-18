package com.polls.polls;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.contus.chatlib.XMPPConnectionController;
import com.contusfly.MApplication;
import com.contusfly.chat.HomeChat;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.ChatUtils;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.google.android.gcm.GCMBaseIntentService;
import com.polls.polls.rest.ApiService;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.List;

public class GCMIntentService extends GCMBaseIntentService implements ApiService.OnTaskCompleted {

    /**
     * The m application.
     */
    private MApplication mApplication;

    private String messageFrom;

    /**
     * On message.
     *
     * @param context the context
     * @param intent  the intent
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        /**
         * Checking the Received msg body. IF not null
         * then load the send the notification.
         */
        try {
            LogMessage.v("Notification::", "msgFrom::");
            mApplication = (MApplication) context.getApplicationContext();
            if (intent != null) {
                if (intent.hasExtra(Constants.GROUP_TYPE)) {
                    String msgTo = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.MESSAGE_TO));
                    if (msgTo.equalsIgnoreCase(mApplication.getStringFromPreference(Constants.USERNAME))) {
                        createNotificationGroupCreated(getApplicationContext(), intent);
                    }
                } else {
                    String msgFrom = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.MESSAGE_FROM));
                    String msgTo = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.MESSAGE_TO));
                    String msgId = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.MESSAGE_ID));
                    String msgType = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.CHAT_MESSAGE_TYPE));
                    String chatType = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.CHAT_TYPE));
                    String msgData = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.MESSAGE_CONTENT));
                    String msgTime = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.MESSAGE_TIME));
                    String groupFrom = "";
                    String sender = msgFrom;
                    if (chatType.equalsIgnoreCase(Constants.GROUP_CHAT)) {
                        groupFrom = mApplication.returnEmptyStringIfNull(intent.getStringExtra(Constants.GROUP_FROM));
                        sender = groupFrom;
                    }
                    LogMessage.v("Notification::", "msgFrom::" + msgFrom + "::msgData::" + msgData);
                    if (!msgData.isEmpty() && msgTo.equalsIgnoreCase(mApplication.getStringFromPreference(Constants.USERNAME))) {
                        sendingDeliveryStatus(sender, msgId, msgTo, msgType, chatType);
                        constructChat(msgFrom, msgId, msgType, msgData, msgTo, msgTime, chatType, groupFrom);
                    } else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE) || msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)) {
                        sendingDeliveryStatus(msgFrom, msgId, msgTo, msgType, chatType);
                    }
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private void constructChat(String msgFrom, String msgId, String msgType, String msgData, String msgTo, String msgTime, String chatType, String groupFrom) {
        if (msgTo.equalsIgnoreCase(mApplication.getStringFromPreference(Constants.USERNAME)))
            msgTime = String.valueOf(Long.parseLong(msgTime));
        constructMsg(msgFrom, msgId, msgType, msgData, msgTime, chatType, groupFrom);
    }

    /**
     * Convenience method for constructing the notification message
     *
     * @param msgFrom  The sender of the chat message
     * @param msgType  The type of message which is being constructed.
     * @param msgData  The content of the message mostly the message body.
     * @param chatType
     */
    private void constructMsg(String msgFrom, String msgId, String msgType, String msgData, String msgTime, String chatType, String groupFrom) {

        try {
            String displayName = "";
            String groupName = "";
            String displayMsg;
            String title;
            String userId;
            msgData = XMPPConnectionController.decryptMsg(msgData, msgId);

            boolean isGroup = false;
            userId = msgFrom;
            /**
             * IF msgFrom contains @ then the msg received from Group chat
             * else
             * Single user.
             */
            if (chatType.equalsIgnoreCase(Constants.GROUP_CHAT)) {
                isGroup = true;
                String chatFrom = Utils.getUserFromJid(msgFrom);
                userId = Utils.getUserFromJid(groupFrom);
                List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(chatFrom, Constants.EMPTY_STRING);
                if (!rosterInfo.isEmpty()) {
                    groupName = rosterInfo.get(0).getRosterName();
                    rosterInfo = MDatabaseHelper.getRosterInfo(userId, Constants.EMPTY_STRING);
                    displayName = !rosterInfo.isEmpty() ? rosterInfo.get(0).getRosterName() : userId;
                    messageFrom = chatFrom;
                }
            } else {
                List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(msgFrom, Constants.EMPTY_STRING);
                displayName = !rosterInfo.isEmpty() ? rosterInfo.get(0).getRosterName() : msgFrom;
                messageFrom = msgFrom;
            }
            displayMsg = msgType;
            String messageData = URLDecoder.decode(msgData, "UTF-8");

            title = displayName;
            ChatMsg chatMsg = new ChatMsg();
            ChatUtils chatUtils = new ChatUtils(getApplicationContext());
            chatMsg.setMsgId(msgId);
            chatMsg.setMsgFrom(mApplication.getStringFromPreference(Constants.USERNAME));
            chatMsg.setMsgTo(messageFrom);
            chatMsg.setMsgTime(msgTime);
            chatMsg.setMsgStatus(String.valueOf(Constants.CHAT_REACHED_TO_RECEIVER));
            chatMsg.setSender(Constants.CHAT_FROM_RECEIVER);
            chatMsg.setMsgType(msgType);
            chatMsg.setMsgChatType(chatType);
            chatMsg.setChatToUser(Utils.getUserFromJid(msgFrom));
            chatMsg.setChatReceivers(mApplication.getStringFromPreference(Constants.USERNAME));
            chatUtils.updateChatHistory(chatMsg);
            JSONObject jsonObject = new JSONObject(messageData);
            chatMsg.setMsgMediaIsDownloaded(String.valueOf(Constants.MEDIA_DOWNLADED));
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT)) {
                displayMsg = URLDecoder.decode(jsonObject.getString(Constants.PARAM_MESSAGE), "UTF-8");
                String msgBody = jsonObject.getString(Constants.PARAM_MESSAGE);
                chatMsg.setMsgBody(msgBody);
            } else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)) {
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
            chatUtils.updateChatHistory(chatMsg);
            chatUtils.updatePendingNotification(chatMsg, messageFrom);
            chatUtils.updateRecentChatHistory(chatMsg, String.valueOf(Constants.COUNT_ZERO), messageFrom);
            /**
             * Appending the group name if isGroup is true.
             */
            if (isGroup) {
                if (displayName.isEmpty()) {
                    title = msgFrom;
                } else {
                    title = displayName + "@" + groupName;
                }
            }
            new Utils().createNotification(mApplication, getApplicationContext(), title, msgType, displayMsg, userId);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private void sendingDeliveryStatus(String msgFrom, String msgId, String msgTo, String msgType, String chatType) {
        try {

            RequestBody postBody = new FormEncodingBuilder()
                    .add(Constants.MESSAGE_ID, msgId)
                    .add(Constants.MESSAGE_FROM, msgTo)
                    .add(Constants.MESSAGE_TO, Utils.getUserFromJid(msgFrom))
                    .add(Constants.MESSAGE_TYPE, msgType)
                    .add(Constants.CHAT_TYPE, chatType).build();
            ApiService apiService = new ApiService(this, false);
            apiService.setOnTaskCompletionListener(this);
            apiService.setRequestBody(postBody);
            apiService.execute(Constants.XMPP_HOST + Constants.API_MESSAGE_DELIVERED);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    @Override
    protected void onError(Context context, String s) {
        LogMessage.v("onError", "::" + s);
    }

    @Override
    protected void onRegistered(Context context, String s) {
        LogMessage.v("onRegistered", "::" + s);
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        LogMessage.v("onUnregistered", "::" + s);
    }

    @Override
    protected String[] getSenderIds(Context context) {
        String[] ids = new String[1];
        ids[0] = Constants.GCM_SENDER_ID;
        return ids;
    }

    /**
     * Create notification for the new group created
     *
     * @param context
     * @param intent
     */
    private void createNotificationGroupCreated(Context context, Intent intent) {
        try {
            String message = mApplication.returnEmptyStringIfNull(intent.getStringExtra("message"));
            String groupName = mApplication.returnEmptyStringIfNull(intent.getStringExtra("group_created"));
            Intent notificationIntent;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(context);
            notBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notBuilder.setContentTitle(Utils.getDecodedString(message));
            notBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
            notBuilder.setContentText(Utils.getDecodedString(groupName));
            notBuilder.setAutoCancel(true);
            notificationIntent = new Intent(context, HomeChat.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.putExtra(Constants.IS_FROM_NOTIFICATION, true);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, Constants.NOTIFICATION_ID,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(Constants.NOTIFICATION_ID, notBuilder.build());

        } catch (Exception e) {
            LogMessage.e(e);
        }

    }

    @Override
    public void onApiResponse(String response) {
        if (!TextUtils.isEmpty(response))
            processDeliveryResponse(response);
    }

    private void processDeliveryResponse(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            boolean isError = responseObject.getBoolean(Constants.ERROR);
            if (!isError && responseObject.has(Constants.CHAT_MESSAGE_TYPE)) {
                String msgType = responseObject.getString(Constants.CHAT_MESSAGE_TYPE);
                if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE) || msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)) {
                    String msgFrom = responseObject.getString(Constants.MESSAGE_FROM);
                    String msgTo = responseObject.getString(Constants.MESSAGE_TO);
                    String msgId = responseObject.getString(Constants.MESSAGE_ID);
                    String chatType = responseObject.getString(Constants.CHAT_TYPE);
                    String msgData = responseObject.getString(Constants.MESSAGE_CONTENT);
                    String msgTime = responseObject.getString(Constants.MESSAGE_TIME);
                    String groupFrom = "";
                    if (chatType.equalsIgnoreCase(Constants.GROUP_CHAT))
                        groupFrom = responseObject.getString(Constants.GROUP_FROM);
                    constructChat(msgFrom, msgId, msgType, msgData, msgTo, msgTime, chatType, groupFrom);
                }
            }
        } catch (JSONException e) {
            LogMessage.e(e);
        }
    }
}
