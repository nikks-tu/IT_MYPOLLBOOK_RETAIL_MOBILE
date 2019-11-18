/**
 * @category ContusMessanger
 * @package com.contusfly.fragments
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.activities.ActivityChatView;
import com.contusfly.activities.ActivityGroupInfo;
import com.contusfly.activities.ActivityUserProfile;
import com.contusfly.adapters.AdapterRecentChat;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.RecentChat;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.contusfly.views.CommonAlertDialog;
import com.polls.polls.R;

import java.util.List;

/**
 * The Class FragmentRecentChats.
 */
public class FragmentRecentChats extends Fragment implements
        CommonAlertDialog.CommonDialogClosedListener {

    /**
     * The recent chats.
     */
    private List<RecentChat> recentChats;

    /**
     * The adapter recent chat.
     */
    private AdapterRecentChat adapterRecentChat;

    /**
     * The context.
     */
    private Activity context;

    /**
     * The list recent.
     */
    private ListView listRecent;

    /**
     * The m dialog.
     */
    private CommonAlertDialog mDialog;

    /**
     * The Constant MARK_AS_READ.
     */
    private static final int START_CHAT = 0, VIEW_INFO = 1,
            DELETE_CHAT = 2, EMAIL_CHAT = 3, MARK_AS_READ = 4;

    /**
     * The item pos.
     */
    private int itemPos;

    /**
     * The m application.
     */
    private MApplication mApplication;

    /**
     * The is un read.
     */
    private boolean isClearChat, isUnRead;

    /**
     * The listener.
     */
    private OnDataChangeListener listener;
    private View footerView;

    /**
     * The listener interface for receiving onDataChange events. The class that is interested in
     * processing a onDataChange event implements this interface, and the object created with that
     * class is registered with a component using the component's <code>addOnDataChangeListener<code>
     * method. When the onDataChange event occurs, that object's appropriate method is invoked.
     *
     * @see
     */
    public interface OnDataChangeListener {

        /**
         * On data changed.
         */
        void onDataChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    /**
     * Sets the on data change listener.
     *
     * @param listener the new on data change listener
     */
    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.listener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
    }

    /**
     * Inits the views.
     *
     * @param view the view
     */
    private void initViews(View view) {
        context = getActivity();
        mDialog = new CommonAlertDialog(context);
        mApplication = (MApplication) context.getApplicationContext();
        mDialog.setOnDialogCloseListener(this);
        listRecent = (ListView) view.findViewById(R.id.list_contacts);
        adapterRecentChat = new AdapterRecentChat(context);
        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_footer, null, false);
        listRecent.addFooterView(footerView);
        listRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocalBroadcastManager.getInstance(mApplication).sendBroadcast(new Intent("clear_unseen_count_chat"));
                openChatView(position);
            }
        });
        listRecent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemPos = position;
                String[] arrayItems = getResources().getStringArray(
                        R.array.array_recent_chat_menu);
                mDialog.showListDialog(Constants.EMPTY_STRING,
                        arrayItems);
                return true;
            }
        });
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.txt_no_chats));
        listRecent.setEmptyView(emptyView);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        setChatAdapter();
    }

    /**
     * Sets the chat adapter.
     */
    public void setChatAdapter() {
        try {
            recentChats = MDatabaseHelper.getRecentChatHistory();
            adapterRecentChat.setData(recentChats);
            listRecent.setAdapter(adapterRecentChat);
            if (recentChats.isEmpty()) {
                listRecent.setVisibility(View.GONE);
            } else
                listRecent.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Filter list.
     *
     * @param filterKey the filter key
     */
    public void filterList(String filterKey) {
        adapterRecentChat.filter(filterKey);
        adapterRecentChat.notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.contusfly.views.CommonAlertDialog.CommonDialogClosedListener#
     * onDialogClosed(com.contusfly.views.CommonAlertDialog.DIALOG_TYPE,
     * boolean)
     */
    @Override
    public void onDialogClosed(CommonAlertDialog.DIALOG_TYPE dialogType,
                               boolean isSuccess) {
        if (isSuccess) {
            if (isClearChat)
                clearChat(true);
            else
                clearChat(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.contusfly.views.CommonAlertDialog.CommonDialogClosedListener#
     * listOptionSelected(int)
     */
    @Override
    public void listOptionSelected(int position) {
        switch (position) {
            case START_CHAT:
                openChatView(itemPos);
                break;
            case VIEW_INFO:
                openChatInfo(itemPos);
                break;
        /*case CLEAR_CHAT:
            isClearChat = true;
            mDialog.showAlertDialog(Constants.EMPTY_STRING,
                    getString(R.string.txt_are_you_sure_you_want_to_clear),
                    getString(R.string.text_Ok),
                    getString(R.string.text_cancel),
                    CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
            break;*/
            case DELETE_CHAT:
                isClearChat = false;
                mDialog.showAlertDialog(Constants.EMPTY_STRING,
                        getString(R.string.txt_are_you_sure_you_want_to_delete),
                        getString(R.string.text_Ok),
                        getString(R.string.text_cancel),
                        CommonAlertDialog.DIALOG_TYPE.DIALOG_DUAL);
                break;
            case EMAIL_CHAT:
                Utils.sendEmailConversation(context, recentChats.get(itemPos)
                        .getRecentChatId(), recentChats.get(itemPos)
                        .getRecentChatName());
                break;
            case MARK_AS_READ:
                updateRecentChat();
                break;
            default:
                break;
        }
    }

    /**
     * Update recent chat.
     */
    private void updateRecentChat() {
        try {
            RecentChat recentChat = recentChats.get(itemPos);
            ContentValues value = new ContentValues();
            if (isUnRead) {
                value.put(Constants.RECENT_CHAT_UN_REED_COUNT,
                        String.valueOf(Constants.COUNT_ZERO));
                recentChat.setRecentChatUnread(Constants.EMPTY_STRING);
            } else {
                value.put(Constants.RECENT_CHAT_UN_REED_COUNT,
                        String.valueOf(Constants.COUNT_ONE));
                recentChat.setRecentChatUnread(String
                        .valueOf(Constants.COUNT_ONE));
            }
            MDatabaseHelper.updateValues(
                    Constants.TABLE_RECENT_CHAT_DATA,
                    value,
                    Constants.RECENT_CHAT_ID + "='"
                            + recentChat.getRecentChatId() + "'");
            adapterRecentChat.notifyDataSetChanged();
            listener.onDataChanged();
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Open chat view.
     *
     * @param position the position
     */
    private void openChatView(int position) {
        if (recentChats.size() > position) {
            RecentChat item = recentChats.get(position);
            String chatType = mApplication.returnEmptyStringIfNull(item
                    .getRecentChatType());
            String userID = item.getRecentChatId();
            startActivity(new Intent(context, ActivityChatView.class).putExtra(
                    Constants.ROSTER_USER_ID, userID).putExtra(Constants.CHAT_TYPE,
                    chatType));
        }
    }

    /**
     * Open chat info.
     *
     * @param position the position
     */
    private void openChatInfo(int position) {
        RecentChat item = recentChats.get(position);
        Intent mIntent;
        String chatType = item.getRecentChatType();
        if (chatType.equalsIgnoreCase(String
                .valueOf(Constants.CHAT_TYPE_SINGLE))) {
            mIntent = new Intent(context, ActivityUserProfile.class);
        } else {
            mIntent = new Intent(context, ActivityGroupInfo.class);
        }
        mIntent.putExtra(Constants.USER_IMAGE, item.getRecentChatImage());
        mIntent.putExtra(Constants.USER_PROFILE_NAME, item.getRecentChatName());
        mIntent.putExtra(Constants.ROSTER_USER_ID, item.getRecentChatId());
        context.startActivity(mIntent);
    }

    /**
     * Clear chat.
     *
     * @param isUpdate the is update
     */
    private void clearChat(boolean isUpdate) {
        try {
            RecentChat item = recentChats.get(itemPos);
            String toUser = item.getRecentChatId();
            String fromUser = mApplication
                    .getStringFromPreference(Constants.USERNAME);
            MDatabaseHelper.deleteRecord(Constants.TABLE_CHAT_DATA,
                    Constants.CHAT_COLUMN_FROM_USER + "=? AND "
                            + Constants.CHAT_COLUMN_TO_USER + "=?",
                    new String[]{fromUser, toUser});
            item.setRecentChatMsg(getString(R.string.text_conversation_cleared));
            item.setRecentChatMsgType(Constants.MSG_TYPE_TEXT);
            item.setRecentChatStatus(Constants.EMPTY_STRING);
            item.setRecentChatUnread(Constants.EMPTY_STRING);
            if (isUpdate)
                MDatabaseHelper.updateRecentChat(item);
            else {
                MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA,
                        Constants.RECENT_CHAT_ID + "=?",
                        new String[]{toUser});
                recentChats.remove(itemPos);
                adapterRecentChat.removeItem(itemPos);
            }
            adapterRecentChat.notifyDataSetChanged();
            listener.onDataChanged();
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * On typing result.
     *
     * @param userName the user name
     * @param status   the status
     * @param userId   the user id
     */
    public void onTypingResult(String userName, String status, String userId) {
    }
}
