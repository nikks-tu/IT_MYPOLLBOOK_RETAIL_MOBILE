/**
 * @category ContusMessanger
 * @package com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contus.chatlib.utils.ContusConstantValues;
import com.contusfly.MApplication;
import com.contusfly.adapters.AdapterChat;
import com.contusfly.emoji.EmojiconsPopup;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.Contact;
import com.contusfly.model.GroupMsgStatus;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.RecentChat;
import com.contusfly.model.Rosters;
import com.contusfly.service.ChatService;
import com.contusfly.utils.ChatMsgTime;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ImageLoader;
import com.contusfly.utils.ItemClickSupport;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.MediaController;
import com.contusfly.utils.MediaPathLoader;
import com.contusfly.utils.MediaPaths;
import com.contusfly.utils.PathUtils;
import com.contusfly.utils.Utils;
import com.contusfly.views.CommonAlertDialog;
import com.contusfly.views.CustomEditText;
import com.contusfly.views.CustomRecyclerView;
import com.contusfly.views.CustomTextView;
import com.contusfly.views.CustomToast;
import com.contusfly.views.DoProgressDialog;
import com.contusfly.views.TimerProgressDialog;
import com.polls.polls.R;

import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


/**
 * The Class ActivityChatView.
 */
public class ActivityChatView extends ActivityBase implements
        View.OnClickListener, CommonAlertDialog.CommonDialogClosedListener,
        Utils.OnOptionSelection, AdapterChat.OnChatItemClickListener,
        TextWatcher {

    private static final String BUNDLE_MEDIA_PATH = "bundle_media_path";
    /**
     * Chat typing time
     */
    private static final long CHAT_TYPING_TIME = 3000;
    /**
     * The m application.
     */
    private MApplication mApplication;
    /**
     * The list chats.
     */
    private CustomRecyclerView listChats;
    /**
     * The edt chat msg.
     */
    private CustomEditText edtChatMsg;
    /**
     * The img send.
     */
    private ImageView imgUserPic;
    /**
     * The Image rec.
     */
    private ImageView imageRec;
    /**
     * The Img send.
     */
    private ImageView imgSend;
    /**
     * The txt ok.
     */
    private TextView txtStatus;
    /**
     * The Txt no msg.
     */
    private TextView txtNoMsg;
    /**
     * The Txt timer.
     */
    private TextView txtTimer;
    /**
     * The Txt ok.
     */
    private TextView txtOk;
    private CustomTextView txtName;
    /**
     * The chat data.
     */
    private List<ChatMsg> chatData;
    private String lastSelectedMessageId = "";
    /**
     * The last known position.
     */
    private int lastKnownPosition;
    /**
     * The is sender chat.
     */
    private boolean isSingleSelection;
    /**
     * The Is single chat.
     */
    private boolean isSingleChat;
    /**
     * The Is broadcast.
     */
    private boolean isBroadcast;
    /**
     * The Is type text.
     */
    private boolean isTypeText;
    /**
     * The Is sender chat.
     */
    private boolean isSenderChat;
    /**
     * The temp msg.
     */
    private String fromUser;
    /**
     * The To user.
     */
    private String toUser;

    /**
     * The To user name.
     */
    private String toUserName = Constants.EMPTY_STRING;
    /**
     * The To user img.
     */
    private String toUserImg = Constants.EMPTY_STRING;
    /**
     * The Last seen.
     */
    private String lastSeen = Constants.EMPTY_STRING;
    /**
     * The Media path.
     */
    private String mediaPath = Constants.EMPTY_STRING;
    /**
     * The Chat type.
     */
    private String chatType;
    /**
     * The User type.
     */
    private String userType;
    /**
     * The Chat txt type.
     */
    private String chatTxtType;
    /**
     * The Group users.
     */
    private String groupUsers = Constants.EMPTY_STRING;
    /**
     * The chat receivers.
     */
    private String chatReceivers = Constants.EMPTY_STRING;
    /**
     * The Group status.
     */
    private String groupStatus = Constants.EMPTY_STRING;
    /**
     * The Temp status.
     */
    private String tempStatus = Constants.EMPTY_STRING;
    /**
     * The adapter chat data.
     */
    private AdapterChat adapterChatData;
    /**
     * The m media player.
     */
    private MediaPlayer mMediaPlayer;
    /**
     * The common alert dialog.
     */
    private CommonAlertDialog commonAlertDialog;
    /**
     * The emojicons popup.
     */
    private EmojiconsPopup emojiconsPopup;
    /**
     * The video file.
     */
    private File videoFile;
    private String maxSize;
    /**
     * The view chat.
     */
    private LinearLayout viewChatControls;
    /**
     * The View chat.
     */
    private LinearLayout viewChat;
    /**
     * The dialog recording.
     */
    private Dialog dialogRecording;
    /**
     * The media recorder.
     */
    private MediaRecorder mediaRecorder;
    /**
     * The typing handler.
     */
    private Handler mediaHandler;
    /**
     * The Typing handler.
     */
    private Handler typingHandler;
    /**
     * The media state.
     */
    private int totalDuration;
    /**
     * The Media state.
     */
    private int mediaState;
    /**
     * The toolbar.
     */
    private Toolbar toolbar;
    /**
     * The action mode.
     */
    private ActionMode actionMode;
    private String downloaded = "";
    /**
     * Calculate the typing time for send the gone response after 3 seconds
     */
    private long typingTime;

    private ImageView imgSmiley;

    private MediaController mediaController;

    private TimerProgressDialog dialog;

    /**
     * The Recorder.
     */
    private Runnable recorder = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    totalDuration += 1;
                    mediaState = Constants.COUNT_ONE;
                    if (totalDuration >= 40) {
                        handleRecording();
                    } else
                        mediaHandler.postDelayed(recorder, 1000);
                    setFormattedDuration();
                }
            });
        }
    };

    /**
     * The typing thread.
     */
    private Runnable typingThread = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - typingTime >= CHAT_TYPING_TIME) {
                sendGoneStatus();
            }
        }
    };
    /**
     * The m action mode callback.
     */
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            configureActionMode(mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            prepareActionMode(menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() != R.id.action_forward) {
                mode.finish();
            }
            return onClickAction(item.getItemId());
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            lastSelectedMessageId = "";
            adapterChatData.setLastSelectedMessageId("");
            adapterChatData.notifyDataSetChanged();
        }
    };

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mApplication = (MApplication) getApplication();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent mIntent = getIntent();
        toUser = mIntent.getStringExtra(Constants.ROSTER_USER_ID);
        chatType = mIntent.getStringExtra(Constants.CHAT_TYPE);

        commonAlertDialog = new CommonAlertDialog(this);
        commonAlertDialog.setOnDialogCloseListener(this);
        emojiconsPopup = new EmojiconsPopup(findViewById(R.id.root_view), this);
        new Utils().setOnOptionListener(this);
        adapterChatData = new AdapterChat(this, toUser);
        mediaController = new MediaController(this);
        adapterChatData.setmMediaController(mediaController);
        adapterChatData.setOnDownloadClickListener(this);
        listChats = (CustomRecyclerView) findViewById(R.id.list_chat_view);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setStackFromEnd(true);
        listChats.setLayoutManager(mManager);
        maxSize = mApplication.returnEmptyStringIfNull(mApplication.getStringFromPreference(Constants.MAX_UPLOAD_SIZE));
        edtChatMsg = (CustomEditText) findViewById(R.id.edt_chat_msg);
        imgUserPic = (ImageView) findViewById(R.id.img_user_pic);
        txtName = (CustomTextView) findViewById(R.id.txt_chat_name);
        txtStatus = (TextView) findViewById(R.id.txt_last_seen);
        txtNoMsg = (TextView) findViewById(R.id.txt_no_msg);
        imgSmiley = (ImageView) findViewById(R.id.img_chat_smiley);
        edtChatMsg.setEmojiImage(imgSmiley);
        viewChatControls = (LinearLayout) findViewById(R.id.chat_controls);
        viewChat = (LinearLayout) findViewById(R.id.view_chat);
        findViewById(R.id.view_back).setOnClickListener(this);
        imgSend = (ImageView) findViewById(R.id.img_chat_send);
        findViewById(R.id.img_chat_camera).setOnClickListener(this);
        findViewById(R.id.img_chat_video).setOnClickListener(this);
        findViewById(R.id.img_chat_audio).setOnClickListener(this);
        findViewById(R.id.img_chat_attach).setOnClickListener(this);
        findViewById(R.id.view_info).setOnClickListener(this);
        findViewById(R.id.img_location).setOnClickListener(this);
        imgSmiley.setOnClickListener(this);
        imgSend.setOnClickListener(this);
        edtChatMsg.addTextChangedListener(this);
        fromUser = mApplication.getStringFromPreference(Constants.USERNAME);
        adapterChatData.setChatType(chatType);
        setData(mIntent);
        Utils.setUpKeyboard(emojiconsPopup, imgSmiley, edtChatMsg);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mediaPath)) {
            outState.putString(BUNDLE_MEDIA_PATH, mediaPath);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mediaPath = savedInstanceState.getString(BUNDLE_MEDIA_PATH);
            maximumSize();
        }
    }

    /**
     * Sets the data.
     *
     * @param mIntent the new data
     */
    private void setData(Intent mIntent) {
        MDatabaseHelper.deleteRecord(Constants.TABLE_PENDING_CHATS, null, null);
        getDataFromDb();
        if (chatType
                .equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_GROUP))) {
            isSingleChat = false;
            chatTxtType = Constants.GROUP_CHAT;
            setSubTitles();
        } else if (chatType.equalsIgnoreCase(String
                .valueOf(Constants.CHAT_TYPE_BROADCAST))) {
            isBroadcast = true;
            isSingleChat = false;
            chatTxtType = "chat";
            setSubTitles();
        } else {
            isSingleChat = true;
            chatTxtType = "chat";
            LogMessage.v("achtview::getAvailability", "::" + toUser);
            startService(new Intent(this, ChatService.class).setAction(
                    Constants.ACTION_GET_AVAILABILITY).putExtra(
                    Constants.ROSTER_USER_ID, toUser));
        }
        setChatAdapter();

        if (mIntent.hasExtra(Constants.CHAT_MESSAGE)) {
            ChatMsg msg = mIntent.getParcelableExtra(Constants.CHAT_MESSAGE);
            prepareChatMsg(msg);
        }
        loadUserImage();

        ItemClickSupport.addTo(listChats).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                chatData = adapterChatData.getData();
                onItemLongClick(chatData.get(position), position);
                return true;

            }
        });

        ItemClickSupport.addTo(listChats).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (!TextUtils.isEmpty(lastSelectedMessageId)) {
                    adapterChatData.setLastSelectedMessageId("");
                    actionMode.finish();
                    lastSelectedMessageId = "";
                    actionMode = null;
                }
            }
        });
    }

    private void loadUserImage() {
        int errorImg = R.drawable.img_ic_user;
        if (isBroadcast)
            errorImg = R.drawable.icon_bcast;
        else if (!isSingleChat)
            errorImg = R.drawable.icon_grp;

        ImageLoader.loadImageWithGlide(this, toUserImg, imgUserPic, errorImg);
    }

    /**
     * Gets the data from db.
     */
    private void getDataFromDb() {
        List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(toUser,
                Constants.EMPTY_STRING);
        if (!rosterInfo.isEmpty()) {
            Rosters item = rosterInfo.get(0);
            toUserName = mApplication.returnEmptyStringIfNull(item
                    .getRosterName());
            toUserImg = mApplication.returnEmptyStringIfNull(item
                    .getRosterImage());
            groupUsers = mApplication.returnEmptyStringIfNull(item
                    .getRosterGroupUsers());
            groupStatus = mApplication.returnEmptyStringIfNull(item
                    .getRosterGroupStatus());
            userType = item.getRosterType();
            txtName.setText((Utils.getDecodedString(toUserName)));
            if (!TextUtils.isEmpty(groupUsers)) {
                List<String> users = new LinkedList<>(Arrays.asList(TextUtils.split(groupUsers, ",")));
                users.remove(fromUser);
                chatReceivers = TextUtils.join(",", users);
            } else
                chatReceivers = toUser;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.storeStringInPreference(Constants.CHAT_ONGOING_NAME,
                toUser);
        updateMessageSeen(null);
        setChatAdapter();
        getDataFromDb();
        if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_GROUP))
                || chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_BROADCAST))) {
            setSubTitles();
        }
        loadUserImage();
        updateRecentChat();
        NotificationManagerCompat.from(this).cancel(Constants.NOTIFICATION_ID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mApplication.storeStringInPreference(Constants.CHAT_ONGOING_NAME,
                Constants.NULL_VALUE);
        if (mMediaPlayer != null)
            mMediaPlayer.release();
    }

    /**
     * Sets the chat adapter.
     */
    private void setChatAdapter() {
        try {
            chatData = MDatabaseHelper.getChatHistory(fromUser, toUser);
            adapterChatData.setData(chatData);
            listChats.setAdapter(adapterChatData);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* Case for image chat send on click*/
            case R.id.img_chat_send:
                String chatMsg = edtChatMsg.getText().toString().trim();
                LogMessage.e("message", "" + chatMsg);
                validateChat(chatMsg);
                break;
            /* Case for image chat smiley on click*/
            case R.id.img_chat_smiley:
                if (emojiconsPopup.isShowing())
                    imgSmiley.setImageResource(R.drawable.ic_smily);
                else
                    imgSmiley.setImageResource(R.drawable.input_keypad);
                Utils.showEmojiKeyboard(this, edtChatMsg, emojiconsPopup);
                break;
            /* Case for taking photo from camera on click*/
            case R.id.img_chat_camera:
                mediaPath = mApplication.takePhotoFromCamera(this,
                        Environment.getExternalStorageDirectory() + "/"
                                + getString(R.string.app_name) + "/" + MediaPaths.MEDIA_PATH_IMAGE
                        , false);
                break;
            /* Case for starting video record on click*/
            case R.id.img_chat_video:
                videoFile = mApplication.startVideoRecording(this);
                break;
            /* Case for starting audio record on click*/
            case R.id.img_chat_audio:
                showRecordingDialog();
                break;
            /* Case for attaching media files on click*/
            case R.id.img_chat_attach:
                commonAlertDialog.showListDialog(
                        getString(R.string.txt_choose_option), getResources()
                                .getStringArray(R.array.array_gallery_selection));
                break;
            /* Case for backpress on click*/
            case R.id.view_back:
                onBackPressed();
                break;
            default:
                handleOtherOnClicks(v);
                break;
        }
    }

    /**
     * Handle other on clicks.
     *
     * @param v the v
     */
    private void handleOtherOnClicks(View v) {
        switch (v.getId()) {
            case R.id.view_info:
                openInfoActivity();
                break;
            case R.id.txt_cancel:
                stopPlayer(false);
                dialogRecording.dismiss();
                break;
            case R.id.txt_ok:
                handleRecording();
                break;
            case R.id.img_location:
                LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    startActivityForResult(new Intent(this, ActivitySelectMapView.class), Constants.SELECT_MAP);
                else
                    mApplication.showLocationAlert(this);
                break;
            default:
                break;
        }
    }

    /**
     * Open info activity.
     */
    private void openInfoActivity() {
        Intent mIntent;
        if (isSingleChat)
            mIntent = new Intent(this, ActivityUserProfile.class);
        else
            mIntent = new Intent(this, ActivityGroupInfo.class);
        mIntent.putExtra(Constants.USER_PROFILE_NAME, toUserName);
        mIntent.putExtra(Constants.USER_IMAGE, toUserImg);
        mIntent.putExtra(Constants.ROSTER_USER_ID, toUser);
        mIntent.putExtra(Constants.USER_STATUS, txtStatus.getText().toString());
        startActivityForResult(mIntent, Constants.ACTIVITY_REQ_CODE);
    }

    /**
     * Validate chat: Check the chat msg whether is empty or not.
     *
     * @param chatMsg chat message
     */
    private void validateChat(String chatMsg) {
        try {
            if (!chatMsg.isEmpty()) {
                sendGoneStatus();
                edtChatMsg.setText(Constants.EMPTY_STRING);
                prepareChatMsg(Constants.MSG_TYPE_TEXT, chatMsg);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Handle recording.
     */
    private void handleRecording() {
        if (mediaState == Constants.COUNT_ZERO)
            startRecording();
        else {
            dialogRecording.dismiss();
            stopPlayer(true);
            Utils.scanMedia(this, mediaPath);
            boolean result = Utils.checkFileSize(this, mediaPath);
            if (result) {
                prepareChatMsg(Constants.MSG_TYPE_AUDIO, "");

            } else {
                showUploadalert(maxSize);
            }
        }
    }

    /**
     * Prepare the chat message depending on the message type.
     *
     * @param msgType The type of message being sent.
     * @param chatMsg
     */
    private void prepareChatMsg(String msgType, String chatMsg) {
        try {
            String randomString = UUID.randomUUID().toString().replace("-", "");
            String lastChatId = randomString + "-" + (System.currentTimeMillis() / 1000);
            ChatMsg mChatMessage = new ChatMsg();
            mChatMessage.setMsgId(lastChatId);
            mChatMessage.setMsgFrom(fromUser);
            mChatMessage.setMsgTo(toUser);
            mChatMessage.setChatReceivers(chatReceivers);
            mChatMessage.setMsgTime(String.valueOf(Calendar.getInstance()
                    .getTimeInMillis() / 1000));
            mChatMessage.setMsgStatus(String
                    .valueOf(Constants.CHAT_SENT_CLOCK));
            mChatMessage.setMsgType(msgType);
            mChatMessage.setMsgChatType(chatType);
            mChatMessage.setSender(Constants.CHAT_FROM_SENDER);
            sendMessage(mChatMessage, msgType, mChatMessage.getMsgTime(), chatMsg);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Prepare chat msg.
     *
     * @param msg the msg
     */
    private void prepareChatMsg(ChatMsg msg) {
        try {
            String lastChatId = fromUser + "-" + (System.currentTimeMillis() / 1000)
                    + new Random(100).nextInt();
            msg.setMsgId(lastChatId);
            msg.setMsgFrom(fromUser);
            msg.setMsgTo(toUser);
            msg.setChatReceivers(chatReceivers);
            msg.setMsgTime(String.valueOf(Calendar.getInstance()
                    .getTimeInMillis() / 1000));
            msg.setMsgStatus(String
                    .valueOf(Constants.CHAT_SENT_CLOCK));
            msg.setMsgChatType(chatType);
            msg.setSender(Constants.CHAT_FROM_SENDER);
            String msgType = msg.getMsgType();
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)) {
                sendChatMsg(msg.getMsgBody(), msgType, msg.getMsgTime(), lastChatId);
            } else
                sendChatMsg(Constants.EMPTY_STRING, msgType, msg.getMsgTime(), lastChatId);
            updateChatList(msg);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Send the  built message to the XMPP server.
     *
     * @param mChatMessage the m chat message
     * @param msgType      the msg type
     * @param chatMsg      chat message
     */
    private void sendMessage(ChatMsg mChatMessage, String msgType, String mTime, String chatMsg) {
        try {
            LogMessage.e("sendMessage", "3");
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)) {
                mChatMessage.setMsgBody(chatMsg);
                sendChatMsg(chatMsg, msgType, mTime, mChatMessage.getMsgId());
            } else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_AUDIO)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_FILE)) {
                mChatMessage.setMsgBody(Constants.EMPTY_STRING);
                mChatMessage.setMsgMediaServerPath(Constants.EMPTY_STRING);
                mChatMessage.setMsgMediaEncImage(Constants.EMPTY_STRING);
                mChatMessage.setMsgMediaIsDownloaded(String
                        .valueOf(Constants.MEDIA_UPLOADING));
                mChatMessage.setMsgMediaLocalPath(mediaPath);
                File file = new File(mediaPath);
                mChatMessage.setFileName(file.getPath());
            }
            updateChatList(mChatMessage);
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_AUDIO)
                    || msgType.equalsIgnoreCase(Constants.MSG_TYPE_FILE))
                uploadFileToServer(mChatMessage);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Send the Text message to server.
     *
     * @param chatMsg    The actual message user have sent.
     * @param msgType    The type of the message.
     * @param lastChatId message id
     */
    private void sendChatMsg(String chatMsg, String msgType, String mTime, String lastChatId) {
        if (mApplication.isNetConnected(this)) {
            LogMessage.e("sendMessage", "4");
            Intent chatIntent = new Intent(this, ChatService.class);
            chatIntent.putExtra("to", toUser);
            chatIntent.putExtra("mid", lastChatId);
            chatIntent.putExtra("from", fromUser);
            chatIntent.putExtra(Constants.CHAT_MSG_TYPE, msgType);
            chatIntent.putExtra("msg", chatMsg);
            chatIntent.putExtra("mtime", mTime);
            chatIntent.putExtra(Constants.CHAT_TO_USER, groupUsers);
            chatIntent
                    .putExtra(Constants.CHAT_TYPE, Integer.parseInt(chatType));
            chatIntent
                    .setAction(ContusConstantValues.CONTUS_XMPP_ACTION_CHAT_REQUEST);
            startService(chatIntent);
            playSendTone(R.raw.outgoing);
        }
    }

    /**
     * Update chat list.
     *
     * @param chatData the chat data
     */
    private void updateChatList(ChatMsg chatData) {
        try {
            LogMessage.e("sendMessage", "5");
            if (!MDatabaseHelper.checkIDStatus(chatData.getMsgId(),
                    Constants.TABLE_CHAT_DATA, Constants.CHAT_COLUMN_ID))
                MDatabaseHelper.insertChatData(chatData);
            else
                MDatabaseHelper.updateChatData(chatData);
            this.chatData.add(chatData);
            adapterChatData.notifyDataSetChanged();
            listChats.smoothScrollToPosition(this.chatData.size() - 1);
            invalidateOptionsMenu();
            updateRecentChat(chatData);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Update recent chat.
     *
     * @param chatData the chat data
     */
    private void updateRecentChat(ChatMsg chatData) {
        try {
            String chatId = chatData.getMsgTo();
            RecentChat recentChat = new RecentChat();
            recentChat.setRecentChatId(chatId);
            recentChat
                    .setRecentChatUnread(String.valueOf(Constants.COUNT_ZERO));
            recentChat.setRecentChatType(chatType);
            recentChat.setRecentChatTime(chatData.getMsgTime());
            recentChat.setRecentChatStatus(chatData.getMsgStatus());
            recentChat.setRecentChatName(toUserName);
            recentChat
                    .setRecentChatIsSeen(String.valueOf(Constants.COUNT_ZERO));
            recentChat.setRecentChatIsSender(String
                    .valueOf(chatData.getSender()));
            recentChat.setRecentChatMsg(chatData.getMsgBody());
            recentChat.setRecentChatMsgId(chatData.getMsgId());
            recentChat.setRecentChatImage(toUserImg);
            recentChat.setRecentChatMsgType(chatData.getMsgType());
            recentChat.setRecentChatUsers(chatData.getChatReceivers());
            if (MDatabaseHelper.checkIDStatus(chatId,
                    Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_USER_ID))
                MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA,
                        Constants.RECENT_CHAT_USER_ID + "=?",
                        new String[]{chatId});
            MDatabaseHelper.insertRecentChat(recentChat);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Update the recent chat table with the latest chat data.
     */
    private void updateRecentChat() {
        ContentValues value = new ContentValues();
        value.put(Constants.RECENT_CHAT_UN_REED_COUNT,
                String.valueOf(Constants.COUNT_ZERO));
        MDatabaseHelper.updateValues(Constants.TABLE_RECENT_CHAT_DATA, value,
                Constants.RECENT_CHAT_USER_ID + "='" + toUser + "'");
    }

    /**
     * Play send tone.
     *
     * @param tone the tone
     */
    private void playSendTone(int tone) {
        if (mApplication.getBooleanFromPreference(Constants.CONVERSATION_SOUND)) {
            mMediaPlayer = MediaPlayer.create(this, tone);
            mMediaPlayer.start();
        }
    }

    /**
     * Update the current chat list with the latest data from the db.
     *
     * @param chatMsgId the chat msg id
     */
    private void updateList(String chatMsgId) {
        try {
            List<ChatMsg> data = MDatabaseHelper.getParticularChat(chatMsgId);
            if (!data.isEmpty()) {
                checkMsgID(chatMsgId, data);
            }
            adapterChatData.notifyDataSetChanged();
            updateRecentChat();
            invalidateOptionsMenu();
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Method to check for a available ID of the message.
     *
     * @param chatMsgId the chat msg id
     * @param data      the data
     */
    private void checkMsgID(String chatMsgId, List<ChatMsg> data) {
        try {
            for (int i = 0, length = chatData.size(); i < length; i++) {
                ChatMsg item = chatData.get(i);
                if (item.getMsgId().equalsIgnoreCase(chatMsgId)) {
                    chatData.set(i, data.get(0));
                    break;
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Method to upload media to the server.
     *
     * @param mChatMessage The message which contains the media.
     */
    private void uploadFileToServer(ChatMsg mChatMessage) {
        if (mApplication.isNetConnected(this)) {
            MDatabaseHelper.updateChatStatus(mChatMessage.getMsgId(),
                    Constants.CHAT_MSG_IS_DOWNLOADED,
                    String.valueOf(Constants.MEDIA_UPLOADING));
            LogMessage.v("Chat VIew:::", ":pathLL:" + mChatMessage.getMsgMediaLocalPath());
            startService(new Intent(this, ChatService.class).setAction(
                    Constants.ACTION_MEIDA_UPLOAD).putExtra(
                    Constants.CHAT_MESSAGE, mChatMessage));
        } else {
            MDatabaseHelper.updateChatStatus(mChatMessage.getMsgId(),
                    Constants.CHAT_MSG_IS_DOWNLOADED,
                    String.valueOf(Constants.MEDIA_NOT_UPLOADED));
        }
        updateList(mChatMessage.getMsgId());
    }

    @Override
    protected void availabilityCallBack(String userID, String availability) {
        lastSeen = availability;
        updateUserAvailability();
    }

    @Override
    protected void presenceUpdate(String userName, String status) {
        LogMessage.v("presenceUpdate", "chatview");
        if (!txtStatus.getText().toString().equals(status)) {
            if (status.equals("online")) {
                lastSeen = String.valueOf(Constants.COUNT_ONE);
            }
            updateUserAvailability();
        }
    }

    @Override
    protected void updateMediaStatus(boolean isUploadComplete, Intent data) {
        updateList(data.getStringExtra(Constants.CHAT_MSG_ID));
    }

    @Override
    protected void receivedChatMsg(Intent chatData) {
        if (chatData != null && chatData.hasExtra(Constants.CHAT_MESSAGE)) {
            ChatMsg mChatMessage = chatData
                    .getParcelableExtra(Constants.CHAT_MESSAGE);
            if (mChatMessage != null
                    && toUser.equalsIgnoreCase(mChatMessage.getMsgTo())) {
                updateChatDataInList(mChatMessage);
                updateMessageSeen(mChatMessage);
                updateRecentChat();
                playSendTone(R.raw.incoming);
            }
        }
    }

    @Override
    protected void groupInfoChanged() {
        getDataFromDb();
        loadUserImage();
    }

    @Override
    protected void updateAddedGroupUsers(String userId, String userName) {
        getDataFromDb();
        setSubTitles();
    }

    @Override
    protected void updateGroupUsers(String userId, String userName) {
        getDataFromDb();
        setSubTitles();
    }

    private void updateChatDataInList(ChatMsg mChatMessage) {
        try {
            chatData.add(mChatMessage);
            adapterChatData.notifyDataSetChanged();
            listChats.smoothScrollToPosition(chatData.size() - 1);
            invalidateOptionsMenu();
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    @Override
    protected void msgSentToServer(Intent chatData) {
        updateList(chatData.getStringExtra(Constants.CHAT_MSG_ID));
    }

    @Override
    protected void msgReceiptCallBack(Intent chatData) {
        updateList(chatData.getStringExtra(Constants.CHAT_MSG_ID));
    }

    @Override
    protected void msgSeenCallBack(Intent chatData) {
        updateList(chatData.getStringExtra(Constants.CHAT_MSG_ID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_menu:
                isSingleSelection = false;
                commonAlertDialog.showAlertDialog(Constants.EMPTY_STRING,
                        getString(R.string.txt_are_you_sure_you_want_to_clear),
                        getString(R.string.text_yes), getString(R.string.text_no), CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
                break;
            case R.id.action_email:
                Utils.sendEmailConversation(this, toUser, toUserName);
                break;
            case R.id.action_custom_tone:
                String notification = MDatabaseHelper.getRosterCustomTone(toUser);
                Utils.showCustomTones(this, Constants.CUSTOM_TONE_REQ, notification);
                break;
            case R.id.action_add_user:
                Utils.addContactInMobile(this, "", "+" + toUser);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_mode, menu);
    }

    @Override
    public void listOptionSelected(int position) {
        switch (position) {
            case 0:
                mApplication.chooseFileFromGallery(this, Constants.MIME_TYPE_AUDIO);
                break;
            case 1:
                mApplication.chooseFileFromGallery(this, Constants.MIME_TYPE_IMAGE);
                break;
            case 2:
                mApplication.chooseFileFromGallery(this, Constants.MIME_TYPE_VIDEO);
                break;
            case 3:
                uploadFile();
                break;
            default:
                break;
        }
    }

    /**
     * Select Document File to Upload
     */
    private void uploadFile() {
        Intent data = new Intent();
        data.setType("application/*");
        data.setAction(Intent.ACTION_GET_CONTENT);
        data.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(data, Constants.PICKFILE_RESULT_CODE);
    }

    @Override
    public void onDialogClosed(CommonAlertDialog.DIALOG_TYPE dialogType, boolean isSuccess) {
        if (isSuccess)
            handleOnDialogClose();
    }

    /**
     * Handle on dialog close.
     */
    private void handleOnDialogClose() {
        try {
            String query, msgBody = getString(R.string.text_conversation_cleared), msgTime = String
                    .valueOf(Calendar.getInstance().getTimeInMillis() / 1000), msgStatus = Constants.EMPTY_STRING, msgType = Constants.MSG_TYPE_TEXT;
            String[] args;
            ChatMsg msg;
            String lastChatId = "";
            int chatIsSender = Constants.CHAT_FROM_RECEIVER;
            List<ChatMsg> chatMsgs = adapterChatData.getData();
            if (isSingleSelection) {
                msg = chatMsgs.get(lastKnownPosition);
                query = Constants.CHAT_COLUMN_FROM_USER + "=? AND "
                        + Constants.CHAT_COLUMN_TO_USER + "=? AND "
                        + Constants.CHAT_COLUMN_ID + " =? ";
                args = new String[]{fromUser, toUser, msg.getMsgId()};
            } else {
                query = Constants.CHAT_COLUMN_FROM_USER + "=? AND "
                        + Constants.CHAT_COLUMN_TO_USER + "=?";
                args = new String[]{fromUser, toUser};
            }
            boolean isDelete = true, clearChat = false;
            if (isSingleSelection) {
                int size = chatMsgs.size();
                if (size == 1 || lastKnownPosition == 0) {
                    isDelete = false;
                    clearChat = true;
                } else
                    isDelete = (lastKnownPosition + 1) == size;

                if (size != 1)
                    clearChat = false;

                if (isDelete) {
                    msg = chatMsgs.get(lastKnownPosition - 1);
                    lastChatId = msg.getMsgId();
                    msgBody = msg.getMsgBody();
                    msgTime = msg.getMsgTime();
                    msgStatus = msg.getMsgStatus();
                    msgType = msg.getMsgType();
                    chatIsSender = msg.getSender();
                }
            } else {
                lastChatId = fromUser + "-" + (System.currentTimeMillis() / 1000);
            }
            MDatabaseHelper
                    .deleteRecord(Constants.TABLE_CHAT_DATA, query, args);
            if (isDelete || clearChat) {
                ChatMsg mChatMessage = new ChatMsg();
                mChatMessage.setMsgId(lastChatId);
                mChatMessage.setMsgFrom(fromUser);
                mChatMessage.setMsgTo(toUser);
                mChatMessage.setMsgBody(msgBody);
                mChatMessage.setMsgTime(msgTime);
                mChatMessage.setMsgStatus(msgStatus);
                mChatMessage.setMsgType(msgType);
                mChatMessage.setMsgChatType(chatType);
                mChatMessage.setSender(chatIsSender);
                updateRecentChat(mChatMessage);
            }
            if (isSingleSelection)
                chatData.remove(lastKnownPosition);
            else chatData.clear();
            adapterChatData.notifyDataSetChanged();
            invalidateOptionsMenu();
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Update message seen.
     *
     * @param chatMessage the msg id
     */
    private void updateMessageSeen(ChatMsg chatMessage) {
        if (mApplication.isNetConnected(this)) {
            if (chatMessage == null) {
                if (chatTxtType.equals(Constants.GROUP_CHAT)) {
                    List<GroupMsgStatus> msgStatuses = MDatabaseHelper.getGroupUnSeenMessages(String.valueOf(Constants.CHAT_RECEIVED_RECEIPT_SENT), toUser);
                    for (GroupMsgStatus messageStatus : msgStatuses) {
                        startService(new Intent(ctx, ChatService.class)
                                .putExtra("to", messageStatus.getMsgSender())
                                .putExtra("mid", messageStatus.getMsgId())
                                .putExtra("type", Constants.CHAT)
                                .setAction(
                                        ContusConstantValues.CONTUS_XMPP_MESSAGE_SEND_SEEN));
                    }
                } else {
                    List<String> unSeenId = MDatabaseHelper.getUnSeenMsgs(fromUser,
                            toUser);
                    for (int i = 0, length = unSeenId.size(); i < length; i++) {
                        String id = mApplication.returnEmptyStringIfNull(unSeenId
                                .get(i));
                        startService(new Intent(ctx, ChatService.class)
                                .putExtra("to", toUser)
                                .putExtra("mid", id)
                                .putExtra("type", chatTxtType)
                                .setAction(
                                        ContusConstantValues.CONTUS_XMPP_MESSAGE_SEND_SEEN));
                    }
                }
            } else {
                startService(new Intent(ctx, ChatService.class)
                        .putExtra("to", chatMessage.getChatToUser())
                        .putExtra("mid", chatMessage.getMsgId())
                        .putExtra("type", Constants.CHAT)
                        .setAction(
                                ContusConstantValues.CONTUS_XMPP_MESSAGE_SEND_SEEN));
            }
        }
    }

    @Override
    public void onOptionSelected(String type, int postion) {
        if (postion == 0) {
            if (type.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE))
                mApplication.chooseFileFromGallery(this,
                        Constants.MIME_TYPE_IMAGE);
            else if (type.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO))
                mApplication.chooseFileFromGallery(this,
                        Constants.MIME_TYPE_VIDEO);
            else if (type.equalsIgnoreCase(Constants.MSG_TYPE_FILE))
                mApplication.chooseFileFromGallery(this,
                        Constants.MIME_TYPE_FILE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            switch (requestCode) {
                case Constants.FROM_GALLERY:
                    if (data != null) {
                        Uri selectedUri = data.getData();
                        String mimeType = getContentResolver().getType(selectedUri);
                        if (mimeType == null) {
                            mimeType = getMimeType(selectedUri.toString());
                        }
                        if (mimeType != null) {
                            mediaPath = mApplication.getRealPathFromURI(selectedUri);
                            if (TextUtils.isEmpty(mediaPath)) {
                                new FilePathLoader(mimeType, selectedUri).execute();
                            } else {
                                handleMediaPathOfTheMessage(mimeType);
                            }
                        }
                    }
                    break;
                case Constants.TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {
                        maximumSize();
                    }
                    break;
                case Constants.TAKE_VIDEO:
                    handleVideoResult(resultCode);
                    break;
                case Constants.ACTIVITY_REQ_CODE:
                    handleResult(resultCode);
                    break;
                case Constants.PICKFILE_RESULT_CODE:
                    handleResultfile(resultCode, data);
                    break;
                case Constants.SELECT_IMAGE_REQ_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        mediaPath = data.getStringExtra(Constants.SELECTED_IMAGE);
                        String msgType = data.getStringExtra(Constants.CHAT_TYPE);
                        prepareChatMsg(msgType, "");
                    }
                default:
                    handleOtherResult(requestCode, resultCode, data);
                    break;
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private void handleMediaPathOfTheMessage(String mimeType) {
        if (mimeType.startsWith(Constants.MSG_TYPE_IMAGE)) {
            startActivityForResult(new Intent(this, ImagePreviewActivity.class)
                            .putExtra(Constants.SELECTED_IMAGE, mediaPath)
                            .putExtra(Constants.CHAT_TYPE, Constants.MSG_TYPE_IMAGE),
                    Constants.SELECT_IMAGE_REQ_CODE);
        } else if (mimeType.startsWith(Constants.MSG_TYPE_VIDEO)) {
            startActivityForResult(new Intent(this, ImagePreviewActivity.class)
                            .putExtra(Constants.SELECTED_IMAGE, mediaPath)
                            .putExtra(Constants.CHAT_TYPE, Constants.MSG_TYPE_VIDEO),
                    Constants.SELECT_IMAGE_REQ_CODE);
        } else {
            handleGalleryIntent(mimeType);
        }
    }

    /**
     * Maximum size.
     */
    private void maximumSize() {
        if (!TextUtils.isEmpty(mediaPath)) {
            Utils.scanMedia(this, mediaPath);
            boolean result = Utils.checkFileSize(this, mediaPath);
            if (result)
                prepareChatMsg(Constants.MSG_TYPE_IMAGE, "");
            else
                showUploadalert(maxSize);
        }
    }

    /**
     * Handle video result.
     *
     * @param resultCode the result code
     */
    private void handleVideoResult(int resultCode) {
        if (videoFile != null && resultCode == RESULT_OK) {
            mediaPath = videoFile.getAbsolutePath();
            Utils.scanMedia(this, mediaPath);
            boolean result = Utils.checkFileSize(this, mediaPath);

            if (result)
                prepareChatMsg(Constants.MSG_TYPE_VIDEO, "");
            else
                showUploadalert(maxSize);

        }
    }

    /**
     * Handle resultfile.
     *
     * @param resultCode the result code
     * @param data       the data
     */
    private void handleResultfile(int resultCode, Intent data) {
        boolean result = Utils.checkFileSize(this, mediaPath);
        if (resultCode == RESULT_OK && data != null) {
            String fileDetails = PathUtils.getPath(this, data.getData());
            if (isValidFileType(fileDetails)) {
                mediaPath = fileDetails;
                if (result)
                    prepareChatMsg(Constants.MSG_TYPE_FILE, "");
                else
                    showUploadalert(maxSize);

            } else
                showFileValidation();
        }
    }

    private void showFileValidation() {
        commonAlertDialog.showAlertDialog(Constants.EMPTY_STRING,
                getString(R.string.text_wrong_extension),
                Constants.EMPTY_STRING, getString(R.string.text_Ok), CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
    }

    /**
     * Is valid file type boolean.
     *
     * @param fileType the file type
     *
     * @return the boolean
     */
    private boolean isValidFileType(String fileType) {
        String[] extensions = new String[]{"doc", "xls", "txt", "pdf", "ppt", "xlsx", "docx"};
        for (String extension : extensions) {
            if (fileType.endsWith(extension))
                return true;
        }
        return false;
    }

    /**
     * Handle other result.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the data
     */
    private void handleOtherResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.CUSTOM_TONE_REQ:
                if (data != null && resultCode == RESULT_OK)
                    Utils.updateRosterTone(data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                            .toString(), toUser);
                break;
            case Constants.CONTACT_REQ_CODE:
                handleContactResult();
                break;
            case Constants.PICK_CONTACT_REQ_CODE:
                if (data != null && resultCode == Activity.RESULT_OK)
                    handleContactSelection(data);
                break;
            case Constants.SELECT_CONTACT_REQ_CODE:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleSelectionResult(data);
                break;
            case Constants.SELECT_MAP:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("latitude", data.getDoubleExtra(Constants.LATITUDE, 0));
                        jsonObject.put("longitude", data.getDoubleExtra(Constants.LONGITUDE, 0));
                        jsonObject.put("isError", false);
                        String chatMsg = jsonObject.toString();
                        prepareChatMsg(Constants.MSG_TYPE_LOCATION, chatMsg);
                    } catch (Exception e) {
                        LogMessage.d("Json::", "Error");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Handle contact result.
     */
    private void handleContactResult() {
        try {
            Contact contact = Utils.isContactAvailable(this, toUser);
            if (contact.isSaved()) {
                userType = String.valueOf(Constants.CHAT_TYPE_SINGLE);
                ContentValues mValues = new ContentValues();
                mValues.put(Constants.ROSTER_TYPE, userType);
                mValues.put(Constants.ROSTER_NAME, contact.getContactName());
                mValues.put(Constants.ROSTER_AVAILABILITY, String.valueOf(Constants.COUNT_ONE));
                MDatabaseHelper.updateValues(Constants.TABLE_ROSTER,
                        mValues, Constants.ROSTER_USER_ID + " = '" + toUser + "'");
                invalidateOptionsMenu();
                MDatabaseHelper.updateValues(Constants.TABLE_RECENT_CHAT_DATA, mValues, Constants.RECENT_CHAT_USER_ID + " =' " + toUser + "'");
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Handle contact selection.
     *
     * @param data the data
     */
    private void handleContactSelection(Intent data) {
        try {
            ContentResolver cr = getContentResolver();
            Uri contactData = data.getData();
            Cursor cursor = cr.query(contactData, null, null, null, null);
            List<Contact> contacts = new ArrayList<>();
            if (cursor != null) {
                cursor.moveToFirst();
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if ("1".equalsIgnoreCase(hasPhone)) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id},
                            null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            Contact item = new Contact();
                            String number = pCur.getString(
                                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    .replace("-", "").replaceAll("\\s", "");
                            if (checkContact(contacts, number)) {
                                item.setContactName(name);
                                item.setContactNos(number);
                                item.setIsSelected(1);
                                contacts.add(item);
                            }
                        }
                        pCur.close();
                    }
                } else CustomToast.showToast(this, getString(R.string.text_no_mobile_nos));
                cursor.close();
            }
            if (!contacts.isEmpty()) {
                startActivityForResult(new Intent(this, ActivityPickContact.class).
                                putParcelableArrayListExtra(Constants.USERNAME, (ArrayList<Contact>) contacts),
                        Constants.SELECT_CONTACT_REQ_CODE);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Check contact.
     *
     * @param contacts the contacts
     * @param number   the number
     *
     * @return true, if successful
     */
    private boolean checkContact(List<Contact> contacts, String number) {
        for (Contact contact : contacts) {
            if (contact.getContactNos().contains(number))
                return false;
        }
        return true;
    }

    /**
     * Handle selection result.
     *
     * @param data the data
     */
    private void handleSelectionResult(Intent data) {
        try {
            List<Contact> contactData = data.getParcelableArrayListExtra(Constants.USERNAME);
            String numbers = "";
            for (Contact contact : contactData) {
                if (contact.getIsSelected() == 1)
                    numbers += contact.getContactNos() + ",";
            }
            numbers = numbers.substring(0, numbers.length() - 1);
            String name = mApplication.returnEmptyStringIfNull(contactData.get(0).getContactName());
            if (name.isEmpty())
                name = getString(R.string.text_unknown);
            String chatMsg = String.valueOf(name + "," + numbers);
            prepareChatMsg(Constants.MSG_TYPE_CONTACT, chatMsg);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Handle result.
     *
     * @param resultCode the result code
     */
    private void handleResult(int resultCode) {
        if (!isSingleChat) {
            if (resultCode == RESULT_FIRST_USER) {
                MDatabaseHelper.deleteRecord(Constants.TABLE_CHAT_DATA,
                        Constants.CHAT_COLUMN_FROM_USER + "=? AND "
                                + Constants.CHAT_COLUMN_TO_USER + "=?",
                        new String[]{fromUser, toUser});
                finish();
            } else {
                getDataFromDb();
                setSubTitles();
            }
        }
    }

    /**
     * Handle gallery intent.
     *
     * @param mimeType the mime type
     */
    private void handleGalleryIntent(String mimeType) {
        boolean result = Utils.checkFileSize(this, mediaPath);

        if (result) {
            if (mimeType.startsWith(Constants.MSG_TYPE_FILE))
                prepareChatMsg(Constants.MSG_TYPE_FILE, "");
            else
                prepareChatMsg(Constants.MSG_TYPE_AUDIO, "");
        } else

            showUploadalert(maxSize);
    }

    private void showUploadalert(String maxSize) {

        String message = String.format(getString(R.string.text_upload), maxSize + " " + "MB");
        commonAlertDialog.showAlertDialog(Constants.EMPTY_STRING, message,
                Constants.EMPTY_STRING, getString(R.string.text_Ok),
                CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
    }

    @Override
    public void onDownloadClicked(ChatMsg item) {
        if (mApplication.isNetConnected(this)) {
            item.setMsgMediaIsDownloaded(String
                    .valueOf(Constants.MEDIA_DOWNLADING));
            adapterChatData.notifyDataSetChanged();
            startService(new Intent(this, ChatService.class)
                    .setAction(Constants.ACTION_MEIDA_DOWNLOAD)
                    .putExtra("mid", item.getMsgId())
                    .putExtra("type", item.getMsgType())
                    .putExtra("url", item.getMsgMediaServerPath()));
        } else
            CustomToast.showToast(this,
                    getString(R.string.text_download_failed));
    }

    @Override
    public void onRetryClicked(ChatMsg item) {
        uploadFileToServer(item);
    }

    /**
     * Update user availability.
     */
    private void updateUserAvailability() {
        try {
            if (isSingleChat && !lastSeen.isEmpty() && !isBroadcast) {
                if ("1".equalsIgnoreCase(lastSeen))
                    txtStatus.setText(getString(R.string.text_online));
                else updateStatus();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Update status.
     */
    private void updateStatus() {
        String status;
        if (android.text.format.DateFormat.is24HourFormat(ActivityChatView.this)) {
            status = ChatMsgTime.chatViewGetDay(this, Long.parseLong(lastSeen), 24);
        } else {
            status = ChatMsgTime.chatViewGetDay(this, Long.parseLong(lastSeen), 12);
            String amplifier = status.substring(status.length() - 2, status.length()).toUpperCase();
            String date = status.substring(0, status.length() - 2);
            status = date + amplifier;
        }
        txtStatus.setText(status);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (chatData.isEmpty()) {
            menu.findItem(R.id.action_menu).setEnabled(false);
            menu.findItem(R.id.action_email).setEnabled(false);
        } else {
            menu.findItem(R.id.action_menu).setEnabled(true);
            menu.findItem(R.id.action_email).setEnabled(true);
        }
        if (userType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_UNKNOWN)))
            menu.findItem(R.id.action_add_user).setVisible(true);
        return true;
    }

    @Override
    public void onSenderItemClicked(ChatMsg item) {
        handleOnListClick(item);
    }

    @Override
    public void onReceiverItemClicked(ChatMsg item) {
        handleOnListClick(item);
    }

    @Override
    public void onSenderItemLongClick(ChatMsg item, int position) {
        onItemLongClick(item, position);
    }

    @Override
    public void onReceiverItemLongClick(ChatMsg item, int position) {
        onItemLongClick(item, position);
    }

    /**
     * Handle on list click.
     *
     * @param item the item
     */
    private void handleOnListClick(ChatMsg item) {
        try {
            if (item.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)) {
                String locationStr = item.getMsgBody();
                JSONObject jsonObject = new JSONObject(locationStr);
                startActivity(new Intent(this, ActivityMapView.class).putExtra(
                        Constants.LATITUDE, jsonObject.getString("latitude")).putExtra(
                        Constants.LONGITUDE, jsonObject.getString("longitude")));
            } else if (item.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_CONTACT)
                    && item.getSender() == Constants.CHAT_FROM_RECEIVER) {
                String[] msgBody = item.getMsgBody().split(",");
                Utils.addContactInMobile(this, msgBody[0], msgBody[1]);
            } else if (item.getMsgMediaIsDownloaded().equalsIgnoreCase(
                    String.valueOf(Constants.MEDIA_DOWNLADED))
                    || item.getMsgMediaIsDownloaded().equalsIgnoreCase(
                    String.valueOf(Constants.MEDIA_UPLOADED))) {
                if (item.getMsgType()
                        .equalsIgnoreCase(Constants.MSG_TYPE_IMAGE)) {
                    startActivity(new Intent(this, ActivityImageView.class)
                            .putExtra(Constants.MEDIA_URL,
                                    item.getMsgMediaLocalPath()));
                } else if (item.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)) {

                    Utils.openMediaFile(this, item.getMsgMediaLocalPath(), Constants.MSG_TYPE_VIDEO);
                } else if (item.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_FILE)) {
                    fileView(item);
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * File view.
     *
     * @param item the item
     */
    private void fileView(ChatMsg item) {
        String filepath = item.getMsgMediaLocalPath();

        Intent intent = new Intent(Intent.ACTION_VIEW);

        File file = new File(filepath);
        if (filepath.contains(".pdf")) {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        } else if (filepath.contains(".doc") || filepath.contains(".docx")) {
            intent.setDataAndType(Uri.fromFile(file), "application/msword");

        } else if (filepath.contains(".xls") || filepath.contains(".xlsx")) {

            intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
        } else if (filepath.contains(".txt")) {
            intent.setDataAndType(Uri.fromFile(file), "text/plain");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        fileError(intent);
    }

    /**
     * File error.
     *
     * @param intent the intent
     */
    private void fileError(Intent intent) {
        try {
            startActivity(intent);
        } catch (Exception e) {
            LogMessage.e(e);
            CustomToast.showToast(this, "There is no supported app installed");
        }
    }

    @Override
    public void onBackPressed() {
        adapterChatData.stopPlayer();
        super.onBackPressed();
    }

    /**
     * Sets the sub titles.
     */
    private void setSubTitles() {
        try {
            String subTitle = Constants.EMPTY_STRING;
            if (chatTxtType.equalsIgnoreCase(Constants.GROUP_CHAT)) {
                if (groupStatus.equalsIgnoreCase(String
                        .valueOf(Constants.COUNT_ZERO))) {
                    viewChat.setVisibility(View.VISIBLE);
                    viewChatControls.setVisibility(View.VISIBLE);
                    txtNoMsg.setVisibility(View.GONE);
                    subTitle = getString(R.string.text_you);
                } else {
                    viewChat.setVisibility(View.GONE);
                    viewChatControls.setVisibility(View.GONE);
                    txtNoMsg.setVisibility(View.VISIBLE);
                }
            }
            String[] usersList = TextUtils.split(groupUsers, ",");
            for (String userName : usersList) {
                List<Rosters> userItem = MDatabaseHelper.getRosterInfo(
                        userName, String.valueOf(Constants.COUNT_ZERO));
                if (!userItem.isEmpty())
                    subTitle = subTitle + ", "
                            + userItem.get(0).getRosterName();
            }
            if (subTitle.startsWith(","))
                subTitle = subTitle.substring(1, subTitle.length());
            txtStatus.setText(Utils.getDecodedString(subTitle.trim()));
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Show recording dialog.
     */
    private void showRecordingDialog() {
        try {
            dialogRecording = new Dialog(this);
            dialogRecording.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogRecording.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogRecording.setContentView(R.layout.dialog_audio_recording);
            dialogRecording.setCancelable(false);
            txtTimer = (TextView) dialogRecording.findViewById(R.id.txt_timer);
            txtOk = (TextView) dialogRecording.findViewById(R.id.txt_ok);
            txtOk.setOnClickListener(this);
            imageRec = (ImageView) dialogRecording
                    .findViewById(R.id.img_recording);
            dialogRecording.findViewById(R.id.txt_cancel).setOnClickListener(
                    this);
            dialogRecording.show();
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Start recording.
     */
    private void startRecording() {
        try {
            txtOk.setOnClickListener(null);
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            Calendar todayDate = Calendar.getInstance();
            String dateToday = todayDate.get(Calendar.DAY_OF_MONTH) + "-"
                    + ((todayDate.get(Calendar.MONTH)) + 1) + "-"
                    + todayDate.get(Calendar.YEAR) + " "
                    + todayDate.get(Calendar.HOUR) + "-"
                    + todayDate.get(Calendar.MINUTE) + "-"
                    + todayDate.get(Calendar.SECOND);
            String audioPath = Environment.getExternalStorageDirectory() + "/"
                    + getString(R.string.app_name) + "/"
                    + MediaPaths.MEDIA_PATH_AUDIO + "/";
            File folder = new File(audioPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String path = "Audio_" + dateToday + ".aac";
            mediaRecorder.setAudioSamplingRate(44100);
            mediaRecorder.setAudioEncodingBitRate(96000);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            File audioFile = new File(folder, path);
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaHandler = new Handler();
            totalDuration = 0;
            mediaState = Constants.COUNT_ZERO;
            mediaRecorder.prepare();
            mediaRecorder.start();
            mediaHandler.post(recorder);
            imageRec.setImageResource(R.drawable.onrecord);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            imageRec.startAnimation(anim);
            mediaPath = audioFile.getAbsolutePath();
            txtOk.setText(getString(R.string.text_send));
            txtOk.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtOk.setOnClickListener(ActivityChatView.this);
                }
            }, 500);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Sets the formatted duration.
     */
    private void setFormattedDuration() {
        String formatted;
        if (totalDuration < 60) {
            if (totalDuration < 10)
                formatted = "00:0" + totalDuration;
            else
                formatted = "00:" + totalDuration;
        } else {
            int sec = totalDuration % 60;
            int min = totalDuration / 60;
            if (min < 10 && sec < 10)
                formatted = "0" + min + ":0" + sec;
            else if (min < 10 && sec >= 10)
                formatted = "0" + min + ":" + sec;
            else if (min >= 10 && sec < 10)
                formatted = min + ":0" + sec;
            else
                formatted = min + ":" + sec;
        }
        txtTimer.setText(formatted);
    }

    /**
     * Stop player.
     *
     * @param isSend the is send
     */
    private void stopPlayer(boolean isSend) {
        try {
            if (mediaRecorder != null)
                mediaRecorder.stop();
            if (mediaHandler != null && recorder != null)
                mediaHandler.removeCallbacks(recorder);
            mediaState = Constants.COUNT_ZERO;
            if (isSend)
                txtOk.setText(getString(R.string.text_send));
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * On click action boolean.
     *
     * @param itemId the item id
     *
     * @return boolean
     */
    private boolean onClickAction(int itemId) {
        switch (itemId) {
            case R.id.action_forward:
                ChatMsg msg = chatData.get(lastKnownPosition);
                startActivity(new Intent(ActivityChatView.this,
                        ActivityShareMsg.class).putExtra(
                        Constants.CHAT_MESSAGE, msg));
                return true;
            case R.id.action_delete:
                if (chatTxtType.equalsIgnoreCase(Constants.GROUP_CHAT)) {
                    deleteGroup();
                } else {
                    deleteChat();
                }
                return true;
            case R.id.action_copy:
                copyOrShareMsg();
                return true;
            case R.id.action_info:
                showMessageInfo();
                return true;
            default:
                return false;
        }
    }

    /**
     * Delete group.
     */
    private void deleteGroup() {
        isSingleSelection = true;
        commonAlertDialog.showAlertDialog(Constants.EMPTY_STRING,
                getString(R.string.txt_are_you_sure_you_want_to_delete_group),
                getString(R.string.text_yes), getString(R.string.text_no),
                CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
    }

    /**
     * Configure action mode.
     *
     * @param mode the mode
     * @param menu the menu
     */
    private void configureActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_context_mode, menu);
        menu.findItem(R.id.action_forward).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_copy).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_delete).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_info).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (!isSingleChat && !isBroadcast && isSenderChat)
            menu.findItem(R.id.action_info).setVisible(true);

        if (downloaded.contains("4")) {
            menu.findItem(R.id.action_forward).setVisible(false);
        }
    }

    /**
     * Prepare action mode.
     *
     * @param menu the menu
     */
    private void prepareActionMode(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_copy);
        if (isTypeText) {
            item.setTitle(getString(R.string.text_copy));
            item.setIcon(R.drawable.icon_copy);
        } else {
            String msgType = chatData.get(lastKnownPosition).getMsgType();
            item.setTitle(getString(R.string.text_share));
            item.setIcon(R.drawable.icon_share);
            item.setVisible(true);
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_CONTACT) ||
                    msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION))
                item.setVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        //Code wont be added
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (edtChatMsg.getText().toString().trim().length() > 0) {
            imgSend.setImageResource(R.drawable.abc_send_select);
            startService(new Intent(this, ChatService.class)
                    .setAction(
                            ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE)
                    .putExtra(Constants.USERNAME, toUser)
                    .putExtra(Constants.STATUS_TXT,
                            getString(R.string.text_typing))
                    .putExtra(Constants.CHAT_TYPE, chatTxtType));
            typingTime = System.currentTimeMillis();
            typingHandler = new Handler();
            typingHandler.postDelayed(typingThread, 3000);
        } else {
            startService(new Intent(this, ChatService.class)
                    .setAction(
                            ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE)
                    .putExtra(Constants.USERNAME, toUser)
                    .putExtra(Constants.STATUS_TXT, Constants.EMPTY_STRING)
                    .putExtra(Constants.CHAT_TYPE, chatTxtType));
            imgSend.setImageResource(R.drawable.abc_send_normal);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        //Code wont be added
    }

    /**
     * Delete chat.
     */
    private void deleteChat() {
        isSingleSelection = true;
        commonAlertDialog.showAlertDialog(Constants.EMPTY_STRING,
                getString(R.string.txt_are_you_sure_you_want_to_delete_msg),
                getString(R.string.text_yes), getString(R.string.text_no),
                CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
    }

    /**
     * Copy or share msg.
     */
    private void copyOrShareMsg() {
        try {
            if (isTypeText) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String msg = URLDecoder.decode(
                        adapterChatData.getData().get(lastKnownPosition)
                                .getMsgBody(), "UTF-8").replaceAll("%", "%25");
                ClipData clip = ClipData.newPlainText("text", msg);
                clipboard.setPrimaryClip(clip);
            } else {
                ChatMsg msg = chatData.get(lastKnownPosition);
                String msgType = msg.getMsgType();
                String path = msg.getMsgMediaLocalPath();
                Intent mIntent = new Intent(Intent.ACTION_SEND);
                Uri uri = Uri.fromFile(new File(path));
                mIntent.putExtra(Intent.EXTRA_STREAM, uri);
                mIntent.setType(msgType + "/*");
                startActivity(mIntent);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Show message info.
     */
    public void showMessageInfo() {
        ChatMsg item = chatData.get(lastKnownPosition);
        startActivity(new Intent(this, ActivityMessageInfo.class).putExtra(
                Constants.CHAT_MSG_ID, item.getMsgId())
                .putExtra(Constants.ROSTER_GROUP_USERS, item.getChatReceivers()));
    }

    @Override
    protected void onTypingResult(String userName, String status, String userID) {
        String statusMsg = mApplication.returnEmptyStringIfNull(status);
        if (!userID.equalsIgnoreCase(fromUser) && toUser.equalsIgnoreCase(userName)) {
            if (statusMsg.isEmpty())
                txtStatus.setText(tempStatus);
            else
                handleTypingResult(status, userID);
        }
    }

    /**
     * Handle typing result.
     *
     * @param status the status
     * @param userID the user id
     */
    private void handleTypingResult(String status, String userID) {
        String userStatus = status;
        String textStatus = txtStatus.getText().toString();
        if (chatTxtType.equalsIgnoreCase(Constants.GROUP_CHAT)) {
            List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(
                    userID, Constants.EMPTY_STRING);
            if (!rosterInfo.isEmpty()) {
                userStatus = Utils.getDecodedString(rosterInfo.get(0).getRosterName()) + " "
                        + getString(R.string.text_typing);
                if (!userStatus.equals(textStatus))
                    tempStatus = txtStatus.getText().toString();
                txtStatus.setText(userStatus);
            }
        } else {
            userStatus = getString(R.string.text_typing);
            if (!userStatus.equals(textStatus)) {
                tempStatus = textStatus;
            }
            txtStatus.setText(userStatus);
        }
    }

    /**
     * Send gone status.
     */
    private void sendGoneStatus() {
        startService(new Intent(this, ChatService.class)
                .setAction(ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE)
                .putExtra(Constants.USERNAME, toUser)
                .putExtra(Constants.STATUS_TXT, Constants.EMPTY_STRING)
                .putExtra(Constants.CHAT_TYPE, chatTxtType));
        if (typingHandler != null && typingThread != null)
            typingHandler.removeCallbacks(typingThread);
    }

    /**
     * On item long click.
     *
     * @param chatMsg  the chat msg
     * @param position the position
     */
    private void onItemLongClick(ChatMsg chatMsg, int position) {

        try {
            int selection = chatMsg.getIsSelected();
            if (!TextUtils.isEmpty(lastSelectedMessageId)) {
                adapterChatData.setLastSelectedMessageId("");
                actionMode.finish();
                lastSelectedMessageId = "";
            } else if (selection == 0) {
                downloaded = mApplication.returnEmptyStringIfNull(chatMsg.getMsgMediaIsDownloaded());
                adapterChatData.setLastSelectedMessageId(chatMsg.getMsgId());
                String msgType = chatMsg.getMsgType();
                if (Constants.MSG_TYPE_TEXT.equalsIgnoreCase(msgType))
                    isTypeText = true;
                else if (Constants.MSG_TYPE_CONTACT.equalsIgnoreCase(msgType) ||
                        Constants.MSG_TYPE_LOCATION.equalsIgnoreCase(msgType)) {
                    isTypeText = false;
                }
                lastKnownPosition = position;
                lastSelectedMessageId = chatMsg.getMsgId();
                isSenderChat = chatMsg.getSender() == Constants.CHAT_FROM_SENDER;

                actionMode = toolbar.startActionMode(mActionModeCallback);

            } else {
                adapterChatData.setLastSelectedMessageId("");
            }
            adapterChatData.notifyDataSetChanged();
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private class FilePathLoader extends AsyncTask<Void, Void, String> {
        private String mimeType;
        private Uri mediaUri;

        /**
         * The m dialog.
         */
        private DoProgressDialog mDialog;

        private FilePathLoader(String mimeType, Uri mediaUri) {
            this.mimeType = mimeType;
            this.mediaUri = mediaUri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new DoProgressDialog(ActivityChatView.this);
            mDialog.showProgress();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (mimeType.startsWith(Constants.MSG_TYPE_IMAGE)) {
                Uri googleUri = MediaPathLoader
                        .getImageUrlWithAuthority(ActivityChatView.this, mediaUri);
                return mApplication.getRealPathFromURI(googleUri);
            } else if (mimeType.startsWith(Constants.MSG_TYPE_VIDEO)) {
                return MediaPathLoader.storeMedialInTempFile(ActivityChatView.this, mediaUri);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDialog.dismiss();
            mediaPath = s;
            handleMediaPathOfTheMessage(mimeType);
        }
    }
}
