/**
 * @category ContusMessanger
 * @package com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.contus.chatlib.utils.ContusConstantValues;
import com.contusfly.MApplication;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;

/**
 * The Class ActivityBase.
 */
public abstract class ActivityBase extends AppCompatActivity {

    /**
     * The ctx.
     */
    protected Context ctx;

    /**
     * The broadcast receiver.
     */
    private BroadcastReceiver broadcastReceiver;

    /**
     * The m application.
     */
    private MApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this.getApplicationContext();
        mApplication = (MApplication) getApplicationContext();
        /**
         * Here we create a broadcast receiver instance and handle the on receive to
         * pass the events to appropriate methods.
         */
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleMtd(intent);
            }
        };
    }


    /**
     * Method to handle all the broadcasts from the chat service.
     *
     * @param intent source intent of the broadcast.PS: Method has been divided into multiple levels
     *               for method complexity.
     */
    protected void handleMtd(Intent intent) {
        try {
            String action = intent.getAction();
            if (ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_REQUEST.equals(action))
                handleLoggedIn();
            else if (Constants.ACTION_MESSAGE_FROM_ROSTER.equals(action))
                receivedChatMsg(intent);
            else if (Constants.ACTION_MESSAGE_SENT_TO_SERVER.equals(action))
                msgSentToServer(intent);
            else if (Constants.ACTION_RECIPET_ACK_FROM_ROSTER.equals(action))
                msgReceiptCallBack(intent);
            else if (Constants.ACTION_MSG_SEEN_FROM_ROSTER.equals(action))
                msgSeenCallBack(intent);
            else if (Constants.ACTION_MEDIA_UPLOAD_STATUS.equalsIgnoreCase(action))
                updateMediaStatus(false, intent);
            else if (Constants.ACTION_MEDIA_UPLOADED.equalsIgnoreCase(action))
                updateMediaStatus(true, intent);
            else
                handleOtherAction(intent, action);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }


    /**
     * Method to handle all the broadcasts from the chat service.
     *
     * @param intent source intent of the broadcast.               <p/>               PS: Method has
     *               been divided into multiple levels for method complexity.
     */
    private void handleOtherAction(Intent intent, String action) {
        if (ContusConstantValues.CONTUS_XMPP_PROFILE_UPDATED.equals(action))
            profileUpDated(intent.getBooleanExtra("prfUpdated", false));
        else if (ContusConstantValues.CONTUS_XMPP_PRESENCE_CHANGE.equalsIgnoreCase(action))
            presenceUpdate(intent.getStringExtra(Constants.USERNAME), intent.getStringExtra(Constants.ROSTER_LAST_SEEN));
        else if (ContusConstantValues.CONTUS_XMPP_GET_GROUPID.equals(action))
            groupCreated(intent.getExtras().getString(Constants.GROUP_ID));
        else if (ContusConstantValues.CONTUS_XMPP_GROUPMEMBER_ADDED.equals(action))
            groupMemberAdded(intent.getExtras().getString(Constants.GROUP_ID), intent.getStringExtra(Constants.TITLE_TYPE));
        else if (Constants.GROUP_MEMBER_REMOVED.equals(action))
            groupMemberRemoved(intent.getExtras().getString(Constants.GROUP_ID));
        else if (Constants.CONTUS_REMOVE_GROUP_USER.equals(action))
            updateGroupUsers(intent.getExtras().getString(Constants.GROUP_ID),
                    intent.getExtras().getString(Constants.RESOURCE));
        else if (Constants.CONTUS_ADD_GROUP_USER.equals(action))
            updateAddedGroupUsers(intent.getExtras().getString(Constants.GROUP_ID),
                    intent.getExtras().getString(Constants.RESOURCE));
        else if (ContusConstantValues.CONTUS_XMPP_GROUPINFO_UPDATE.equals(action))
            groupInfoUpdated(intent.getExtras().getString("response"));
        else if (Constants.CONTUS_XMPP_GROUPINFO_CHANGED.equals(action))
            groupInfoChanged();
        else
            handleActions(intent, action);
    }

    /**
     * Handle actions.
     *
     * @param intent the intent
     */
    private void handleActions(Intent intent, String action) {
        if (Constants.ACTION_AVAILABILITY_RESULT.equalsIgnoreCase(action))
            availabilityCallBack(intent.getStringExtra(Constants.ROSTER_USER_ID),
                    intent.getStringExtra(Constants.ROSTER_LAST_SEEN));
        else if (ContusConstantValues.CONTUS_XMPP_EXIT_GROUP_RESPONSE.equals(action))
            groupExitResponse(intent.getExtras().getString("response"));
        else if (Constants.CONTUS_NEW_GROUP.equals(action))
            newGroupCreated();
        else if (ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE_RESULT.equalsIgnoreCase(action))
            onTypingResult(intent.getStringExtra("user_grp_id"), intent.getStringExtra("status"),
                    intent.getStringExtra("user_id"));
        else if (Constants.ACTION_ROSTER_SYNC.equalsIgnoreCase(action))
            rosterSyncSuccess();
        else if (ContusConstantValues.CONTUS_XMPP_ACTION_GET_PROFILE.equals(action)) {
            getProfile();
            profileUpDated(true);
        } else if (Constants.ACTION_DELETE_ACCOUNT.equalsIgnoreCase(action)) {
            String result = intent.getStringExtra(Constants.RESULT);
            String resultType = intent.getStringExtra(Constants.RESULT_TYPE);
            deleteAccount(result, resultType);
        } else if (Constants.ACTION_SESSION_EXPIRE.equalsIgnoreCase(action)) {
            String result = intent.getStringExtra(Constants.RESULT);
            showSessionExpireAlert(result);
        } else if (Constants.DELETE_MESSAGE.equalsIgnoreCase(action)) {
            boolean isSuccess = intent.getBooleanExtra("result", false);
            onDeleteMessage(isSuccess);
        } else if (Constants.CLEAR_CHAT.equalsIgnoreCase(action)) {
            boolean isSuccess = intent.getBooleanExtra("result", false);
            onClearChat(isSuccess);
        }
    }

    /**
     * Gets the profile.
     */
    private void getProfile() {
        String status = mApplication
                .returnEmptyStringIfNull(mApplication.getStringFromPreference(Constants.USER_STATUS));
        if (!status.isEmpty() && !MDatabaseHelper.checkIDStatus(status, Constants.TABLE_USER_STATUS, Constants.STATUS_TXT)) {
            ContentValues values = new ContentValues();
            values.put(Constants.STATUS_TXT, status);
            MDatabaseHelper.insertValues(Constants.TABLE_USER_STATUS, values);
        }
        profileCallback();
    }

    /**
     * Callback method which is called when a group creation is completed.
     *
     * @param groupId The unique id of the created group.
     */
    protected void groupCreated(String groupId) {
        // Code will added on Overridden Place
    }

    /**
     * Callback method called when new user is added to the group.
     *
     * @param groupId The group id in which the user was added.
     */
    protected void groupMemberAdded(String groupId, String type) {
        // Code will added on Overridden Place
    }

    /**
     * Callback when a user is removed from the group.
     *
     * @param groupId The group id from which a user was removed.
     */
    protected void groupMemberRemoved(String groupId) {
        // Code will added on Overridden Place
    }

    /**
     * Updte group users.
     *
     * @param userId   the user id
     * @param userName the user name
     */
    protected void updateGroupUsers(String userId, String userName) {
        // Code will added on Overridden Place
    }

    protected void deleteAccount(String result, String resultType) {

    }

    protected void showSessionExpireAlert(String message) {
        // Code will added on Overridden Place
    }

    /**
     * Callback method when user info is updated .
     *
     * @param userId   The user id whose info is updated.
     * @param userName The user name of the updated user.
     */
    protected void updateAddedGroupUsers(String userId, String userName) {
    }

    /**
     * Callback method when a group info is updated.
     *
     * @param response the response of the group update.
     */
    protected void groupInfoUpdated(String response) {
        // Code will added on Overridden Place
    }

    /**
     * Callback method when a group info is updated.
     */
    protected void groupInfoChanged() {
        // Code will added on Overridden Place
    }

    /**
     * Callback response when the current user request to leave a group.
     *
     * @param response Reponse string of the exit request.
     */
    protected void groupExitResponse(String response) {
        // Code will added on Overridden Place
    }

    /**
     * Callback method when a new group is created with the current user in it.
     */
    protected void newGroupCreated() {
        // Code will be added on Overridden Place
    }

    /**
     * Callback method when the user data is updated in the server.
     *
     * @param status boolean whether the status update is completed "yes" when completed else "no".
     */
    protected void profileUpDated(boolean status) {
        // Code will added on Overridden Place
    }

    /**
     * Profile callback.
     */
    protected void profileCallback() {
        // Code will added on Overridden Place
    }

    /**
     * Availability call back.            contactSyncSuccess();
     *
     * @param userID       the user id
     * @param availability the availability
     */
    protected void availabilityCallBack(String userID, String availability) {
        // Code will added on Overridden Place
    }

    /**
     * Callback when a roster synchronization gets completed.
     */
    protected void rosterSyncSuccess() {
        // Code will added on Overridden Place
    }


    /**
     * Callback when a contact synchronization request gets succeeded.
     */
    protected void contactSyncSuccess() {
        // Code will added on Overridden Place
    }

    /**
     * Callback method which gets called when the typing status of the requested user is changed.
     *
     * @param userName The name of the requested user.
     * @param status   The typing status of the user.
     * @param userId   The user id of the requested user.
     */
    protected void onTypingResult(String userName, String status, String userId) {
        // Code will added on Overridden Place
    }

    /**
     * Callback method when the user presence is updated for a user.
     */
    protected void presenceUpdate(String userName, String status) {
        // Code will added on Overridden Place
    }

    /**
     * Update media status.
     *
     * @param isUploadComplete the is upload complete
     * @param data             the data
     */
    protected void updateMediaStatus(boolean isUploadComplete, Intent data) {
        // Code will added on Overridden Place
    }


    /**
     *
     */
    protected void handleLoggedIn() {
        // Code will added on Overridden Place
    }

    /**
     * Callback method called when a new message is received.
     *
     * @param chatData The intent with the chat data.
     */
    protected void receivedChatMsg(Intent chatData) {
        // Code will added on Overridden Place
    }

    /**
     * Callback method called when sent message is sent to server.
     *
     * @param chatData The intent with the chat data.
     */
    protected void msgSentToServer(Intent chatData) {
        // Code will added on Overridden Place
    }

    /**
     * Msg reciept call back.
     *
     * @param chatData the chat data
     */
    protected void msgReceiptCallBack(Intent chatData) {
        // Code will added on Overridden Place
    }

    /**
     * Callback method when the receiver reads the sent message.
     *
     * @param chatData The intent with the chat data.
     */
    protected void msgSeenCallBack(Intent chatData) {
        // Code will added on Overridden Place
    }

    protected void onDeleteMessage(boolean isSuccess) {

    }

    protected void onClearChat(boolean isSuccess) {

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_REQUEST);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_ACTION_CHAT_REQUEST);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_ACTION_CHAT_RESPONSE);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_ROSTER);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_ERROR);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_PRESENCE_CHANGE);
        intentFilter.addAction(Constants.ACTION_MESSAGE_FROM_ROSTER);
        intentFilter.addAction(Constants.ACTION_MESSAGE_SENT_TO_SERVER);
        intentFilter.addAction(Constants.ACTION_RECIPET_ACK_FROM_ROSTER);
        intentFilter.addAction(Constants.ACTION_MSG_SEEN_FROM_ROSTER);
        intentFilter.addAction(Constants.ACTION_MEDIA_UPLOAD_STATUS);
        intentFilter.addAction(Constants.ACTION_MEDIA_UPLOADED);
        intentFilter.addAction(Constants.CONTUS_REMOVE_GROUP_USER);
        intentFilter.addAction(Constants.CONTUS_ADD_GROUP_USER);
        intentFilter.addAction(Constants.CONTUS_NEW_GROUP);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_PROFILE);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_GET_GROUPID);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP_RESPONSE);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_GROUPMEMBER_ADDED);
        intentFilter.addAction(Constants.GROUP_MEMBER_REMOVED);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_GROUPINFO_UPDATE);
        intentFilter.addAction(Constants.CONTUS_XMPP_GROUPINFO_CHANGED);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_ACTION_SET_DEVICEID);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_PROFILE_UPDATED);
        intentFilter.addAction(Constants.ACTION_REFRESH_CONTACTS);
        intentFilter.addAction(Constants.ACTION_AVAILABILITY_RESULT);
        intentFilter.addAction(Constants.ACTION_ROSTER_SYNC);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE_RESULT);
        intentFilter.addAction(Constants.ACTION_DELETE_ACCOUNT);
        intentFilter.addAction(Constants.ACTION_SESSION_EXPIRE);
        intentFilter.addAction(Constants.DELETE_MESSAGE);
        intentFilter.addAction(Constants.CLEAR_CHAT);

        // add broadcast action
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_GET_BROADCAST_ID);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_BROADCAST_MEMBER_ADDED);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_BROADCAST_INFO_UPDATE);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_BROADCAST_DELETE_RESPONSE);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * Un register my receiver.
     */
    private void unRegisterMyReceiver() {
        try {
            if (broadcastReceiver != null)
                unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterMyReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        unRegisterMyReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}