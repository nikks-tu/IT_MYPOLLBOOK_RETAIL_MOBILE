/*
 * @category ContusMessanger
 * @package com.time.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2016 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contusfly.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.contusfly.MApplication;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.RecentChat;
import com.contusfly.model.Rosters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 3/8/16.
 */
public class ChatUtils {

    private Context context;

    /**
     * Constructor
     */
    public ChatUtils(Context context) {
        this.context = context;
    }

    /**
     * Update chat history.
     *
     * @param chatMsg the chat msg
     */
    public int updateChatHistory(ChatMsg chatMsg) {
        try {
            if (!MDatabaseHelper.checkIDStatus(chatMsg.getMsgId(), Constants.TABLE_CHAT_DATA,
                    Constants.CHAT_COLUMN_ID)) {
                MDatabaseHelper.insertChatData(chatMsg);
                return 1;
            } else {
                MDatabaseHelper.updateChatData(chatMsg);
                return 0;
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return 0;
    }

    /**
     * Update recent chat history.
     *
     * @param chatMsg the chat msg
     * @param msgSeen the msg seen
     */
    public void updateRecentChatHistory(ChatMsg chatMsg, String msgSeen, String msgTo) {
        MApplication mApplication = (MApplication) context;
        try {
            String unReadStatus = mApplication.returnEmptyStringIfNull(MDatabaseHelper.getUnreadMsgCount(msgTo));
            int unReadCount = Constants.COUNT_ZERO;
            if (!unReadStatus.isEmpty())
                unReadCount = Integer.parseInt(unReadStatus) + Constants.COUNT_ONE;
            List<Rosters> rosterItem = MDatabaseHelper.getRosterInfo(msgTo, Constants.EMPTY_STRING);
            String rosterName;
            String rosterImage;
            if (!rosterItem.isEmpty()) {
                rosterImage = rosterItem.get(0).getRosterImage();
                rosterName = rosterItem.get(0).getRosterName();
                String chatType = mApplication.returnEmptyStringIfNull(chatMsg.getMsgChatType());
                RecentChat recentChat = new RecentChat();
                recentChat.setRecentChatId(msgTo);
                recentChat.setRecentChatImage(rosterImage);
                recentChat.setRecentChatIsSeen(msgSeen);
                recentChat.setRecentChatUnread(String.valueOf(unReadCount));
                recentChat.setRecentChatMsg(chatMsg.getMsgBody());
                recentChat.setRecentChatMsgId(chatMsg.getMsgId());
                recentChat.setRecentChatStatus(Constants.EMPTY_STRING);
                recentChat.setRecentChatTime(chatMsg.getMsgTime());

                recentChat.setRecentChatIsSender(String.valueOf(chatMsg.getSender()));
                if (chatType.equalsIgnoreCase(Constants.GROUP_CHAT))
                    recentChat.setRecentChatType(String.valueOf(Constants.CHAT_TYPE_GROUP));
                else
                    recentChat.setRecentChatType(String.valueOf(Constants.CHAT_TYPE_SINGLE));

                recentChat.setRecentChatName(rosterName);
                recentChat.setRecentChatMsgType(chatMsg.getMsgType());
                if (MDatabaseHelper.checkIDStatus(msgTo, Constants.TABLE_RECENT_CHAT_DATA,
                        Constants.RECENT_CHAT_USER_ID))
                    MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA, Constants.RECENT_CHAT_USER_ID + "=?",
                            new String[]{msgTo});
                MDatabaseHelper.insertRecentChat(recentChat);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public ChatMsg insertGroupStatusMessage(String gid, String statusMessage) {
        ChatMsg chatMsg = new ChatMsg();

        String msgId = gid + String.valueOf(Calendar.getInstance()
                .getTimeInMillis());
        MApplication mApplication = (MApplication) context;

        String msgTime = mApplication.returnEmptyStringIfNull(String.valueOf(Calendar.getInstance()
                .getTimeInMillis() / 1000));

        if (!MDatabaseHelper.checkIDStatus(gid, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID))
            Utils.insertUnknownUser(gid);

        chatMsg.setMsgId(msgId);
        chatMsg.setMsgFrom(mApplication.getStringFromPreference(Constants.USERNAME));
        chatMsg.setMsgTo(gid);
        chatMsg.setMsgTime(msgTime);
        chatMsg.setMsgStatus(String.valueOf(Constants.CHAT_REACHED_TO_RECEIVER));
        chatMsg.setSender(Constants.CHAT_FROM_RECEIVER);
        chatMsg.setMsgType(Constants.MSG_TYPE_STATUS);
        chatMsg.setMsgChatType(Constants.GROUP_CHAT);
        chatMsg.setChatToUser(gid);
        chatMsg.setChatReceivers(mApplication.getStringFromPreference(Constants.USERNAME));

        try {
            chatMsg.setMsgBody(URLDecoder.decode(statusMessage, "UTF-8").replaceAll("%", "%25"));
        } catch (UnsupportedEncodingException e) {
            LogMessage.e(e);
        }
        chatMsg.setMsgMediaIsDownloaded(String.valueOf(Constants.MEDIA_NOT_DOWNLOADED));
        chatMsg.setMsgVideoThumb(Constants.EMPTY_STRING);
        chatMsg.setMsgMediaLocalPath(Constants.EMPTY_STRING);

        MDatabaseHelper.insertChatData(chatMsg);
        updateRecentChatHistory(chatMsg, String.valueOf(Constants.COUNT_ZERO), gid);

        Intent intent = new Intent(Constants.ACTION_MESSAGE_FROM_ROSTER);
        intent.putExtra(Constants.CHAT_MESSAGE, chatMsg);
        context.getApplicationContext().sendBroadcast(intent);

        return chatMsg;
    }

    /**
     * Update pending notification.
     *
     * @param chatMsg the chat msg
     */
    public void updatePendingNotification(ChatMsg chatMsg, String msgTo) {
        try {
            List<Rosters> rosterItem = MDatabaseHelper.getRosterInfo(msgTo, Constants.EMPTY_STRING);
            if (rosterItem != null && !rosterItem.isEmpty()) {
                String id = chatMsg.getMsgId();
                ContentValues mValues = new ContentValues();
                mValues.put(Constants.PENDING_CHAT_ID, id);
                mValues.put(Constants.PENDING_CHAT_IMAGE, rosterItem.get(0).getRosterImage());
                mValues.put(Constants.PENDING_CHAT_MSG, chatMsg.getMsgBody());
                mValues.put(Constants.PENDING_CHAT_NAME, rosterItem.get(0).getRosterName());
                mValues.put(Constants.PENDING_CHAT_TIME, Calendar.getInstance().getTimeInMillis());
                mValues.put(Constants.PENDING_CHAT_TYPE, chatMsg.getMsgType());
                if (MDatabaseHelper.checkIDStatus(id, Constants.TABLE_PENDING_CHATS, Constants.PENDING_CHAT_ID))
                    MDatabaseHelper.updateValues(Constants.TABLE_PENDING_CHATS, mValues,
                            Constants.PENDING_CHAT_ID + "='" + id + "'");
                else
                    MDatabaseHelper.insertValues(Constants.TABLE_PENDING_CHATS, mValues);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }
}
