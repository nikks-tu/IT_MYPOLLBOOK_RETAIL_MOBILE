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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contus.chatlib.parcels.User;
import com.contus.chatlib.utils.ContusConstantValues;
import com.contusfly.MApplication;
import com.contusfly.service.ChatService;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ActivityBase.
 */
public abstract class ActivityBaseFragment extends Fragment {

    /**
     * The ctx.
     */
    protected Context ctx;

    /**
     * The intent filter.
     */
    private IntentFilter intentFilter;

    /**
     * The broadcast receiver.
     */
    private BroadcastReceiver broadcastReceiver;

    /**
     * The m password.
     */
    private String action, mUserName, mPassword;

    /**
     * The m application.
     */
    private MApplication mApplication;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity().getApplicationContext();
        mApplication = (MApplication) getActivity().getApplicationContext();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleMtd(intent);
            }
        };
        String deviceID = mApplication.returnEmptyStringIfNull(MApplication.getString(getActivity(), com.contus.app.Constants.GCM_REG_ID));
        if (mApplication.getBooleanFromPreference(Constants.IS_LOGGED_IN) && !deviceID.isEmpty()) {
            Intent mIntent = new Intent(getActivity(), ChatService.class);
            mIntent.setAction(ContusConstantValues.CONTUS_XMPP_ACTION_SET_DEVICEID);
            mIntent.putExtra("deviceId", deviceID);
            getActivity().startService(mIntent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Handle mtd.
     *
     * @param intent the intent
     */
    protected void handleMtd(Intent intent) {
        try {
            action = intent.getAction();
            if (action.equals(Constants.ACTION_MESSAGE_FROM_ROSTER)) {
                receivedChatMsg(intent);
            } else if (action.equals(Constants.ACTION_MESSAGE_SENT_TO_SERVER)) {
                msgSentToServer(intent);
            } else if (action.equals(Constants.ACTION_RECIPET_ACK_FROM_ROSTER)) {
                msgRecieptCallBack(intent);
            } else if (action.equals(Constants.ACTION_MSG_SEEN_FROM_ROSTER)) {
                msgSeenCallBack(intent);
            } else if (action
                    .equalsIgnoreCase(Constants.ACTION_MEDIA_UPLOAD_STATUS)) {
                updateMediaStatus(false, intent);
            } else if (action.equalsIgnoreCase(Constants.ACTION_MEDIA_UPLOADED)) {
                updateMediaStatus(true, intent);
            } else if (action
                    .equals(ContusConstantValues.CONTUS_XMPP_ACTION_GET_PROFILE)) {
                ArrayList<User> userProfile = intent
                        .getParcelableArrayListExtra("getProfile");
                getProfile(userProfile);
                profileUpDated(true);
            } else handleOtherAction(intent);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e+"");
        }
    }

    private void handleOtherAction(Intent intent) {
        if (action
                .equals(ContusConstantValues.CONTUS_XMPP_PROFILE_UPDATED)) {
            profileUpDated(intent.getBooleanExtra("prfUpdated", false));
        } else if (action
                .equalsIgnoreCase(ContusConstantValues.CONTUS_XMPP_PRESENCE_CHANGE)) {
            presenseUpdate();
        } else if (action
                .equals(ContusConstantValues.CONTUS_XMPP_GET_GROUPID)) {
            String grupId = intent.getExtras().getString(Constants.GROUP_ID);
            groupCreated(grupId);
        } else if (action
                .equals(ContusConstantValues.CONTUS_XMPP_GROUPMEMBER_ADDED)) {
            String result = intent.getExtras().getString(Constants.GROUP_ID);
            groupMemberAdded(result);
        } else if (action.equals(Constants.GROUP_MEMBER_REMOVED)) {
            String result = intent.getExtras().getString(Constants.GROUP_ID);
            groupMemberRemoved(result);
        } else if (action.equals(Constants.CONTUS_REMOVE_GROUP_USER)) {
            String result = intent.getExtras().getString(Constants.GROUP_ID);
            String resource_user = intent.getExtras().getString(Constants.RESOURCE);
            updteGroupUsers(result, resource_user);
        } else if (action.equals(Constants.CONTUS_ADD_GROUP_USER)) {
            String result = intent.getExtras().getString(Constants.GROUP_ID);
            String resource_user = intent.getExtras().getString(Constants.RESOURCE);
            updteAddedGroupUsers(result, resource_user);
        } else if (action
                .equals(ContusConstantValues.CONTUS_XMPP_GROUPINFO_UPDATE)) {
            String response = intent.getExtras().getString("response");
            groupInfoUpdated(response);
        } else if (action
                .equals(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP_RESPONSE)) {
            String response = intent.getExtras().getString("response");
            groupExitResponse(response);
        } else if (action.equals(Constants.CONTUS_NEW_GROUP)) {
            newGroupCreated();
        } else handleActions(intent);
    }

    private void handleActions(Intent intent) {
        if (action
                .equalsIgnoreCase(Constants.ACTION_AVAILABILITY_RESULT)) {
            availabilityCallBack(intent
                    .getStringExtra(Constants.ROSTER_AVAILABILITY));
        } else if (action
                .equalsIgnoreCase(ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE_RESULT)) {
            onTypingResult(intent.getStringExtra("user_grp_id"),
                    intent.getStringExtra("status"),
                    intent.getStringExtra("user_id"));
        }
    }

    /**
     * Gets the profile.
     *
     * @param userProfile the user profile
     * @return the profile
     */
    private void getProfile(List<User> userProfile) {
        for (int i = 0, size = userProfile.size(); i < size; i++) {
            User item = userProfile.get(i);
            String image = mApplication
                    .returnEmptyStringIfNull(item.getImage());
            String firstName = mApplication.returnEmptyStringIfNull(item
                    .getFirstname());
            if (!image.isEmpty() && !firstName.isEmpty()) {
                mApplication.storeStringInPreference(
                        Constants.USER_PROFILE_NAME, firstName);
                mApplication.storeStringInPreference(Constants.USER_IMAGE,
                        image);
                profileCallback();
            }
        }
    }

    /**
     * Group created.
     *
     * @param groupId the group id
     */
    protected void groupCreated(String groupId) {
        // Code will added on Overridden Place
    }

    /**
     * Group member added.
     *
     * @param groupId the group id
     */
    protected void groupMemberAdded(String groupId) {
        // Code will added on Overridden Place
    }

    /**
     * Group member removed.
     *
     * @param groupId the group id
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
    protected void updteGroupUsers(String userId, String userName) {
        // Code will added on Overridden Place
    }

    /**
     * Updte added group users.
     *
     * @param userId   the user id
     * @param userName the user name
     */
    protected void updteAddedGroupUsers(String userId, String userName) {
        // Code will added on Overridden Place
    }

    /**
     * Group info updated.
     *
     * @param response the response
     */
    protected void groupInfoUpdated(String response) {
        // Code will added on Overridden Place
    }

    /**
     * Group exit response.
     *
     * @param response the response
     */
    protected void groupExitResponse(String response) {
        // Code will added on Overridden Place
    }

    /**
     * New group created.
     */
    protected void newGroupCreated() {
        // Code will be added on Overridden Place
    }

    /**
     * Profile up dated.
     *
     * @param status the status
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
     * Availability call back.
     *
     * @param availability the availability
     */
    protected void availabilityCallBack(String availability) {
        // Code will added on Overridden Place
    }

    /**
     * On typing result.
     *
     * @param userName the user name
     * @param status   the status
     * @param userId   the user id
     */
    protected void onTypingResult(String userName, String status, String userId) {
        // Code will added on Overridden Place
    }


    /**
     * Presense update.
     */
    protected void presenseUpdate() {
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
     * Handle logged in.
     */
    protected void handleLoggedIn() {
        // Code will added on Overridden Place
    }

    /**
     * Received chat msg.
     *
     * @param chatData the chat data
     */
    protected void receivedChatMsg(Intent chatData) {
        // Code will added on Overridden Place
    }

    /**
     * Msg sent to server.
     *
     * @param chatData the chat data
     */
    protected void msgSentToServer(Intent chatData) {
        // Code will added on Overridden Place
    }

    /**
     * Msg reciept call back.
     *
     * @param chatData the chat data
     */
    protected void msgRecieptCallBack(Intent chatData) {
// Code will added on Overridden Place
    }

    /**
     * Msg seen call back.
     *
     * @param chatData the chat data
     */
    protected void msgSeenCallBack(Intent chatData) {
// Code will added on Overridden Place
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onStart()
     */

    /**
     * On start.
     */
    @Override
    public void onStart() {
        super.onStart();
        intentFilter = new IntentFilter(
                ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_REQUEST);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_ACTION_CHAT_REQUEST);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_ACTION_CHAT_RESPONSE);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_ROSTER);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_ERROR);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_PRESENCE_CHANGE);
        intentFilter.addAction(Constants.ACTION_MESSAGE_FROM_ROSTER);
        intentFilter.addAction(Constants.ACTION_MESSAGE_SENT_TO_SERVER);
        intentFilter.addAction(Constants.ACTION_RECIPET_ACK_FROM_ROSTER);
        intentFilter.addAction(Constants.ACTION_MSG_SEEN_FROM_ROSTER);
        intentFilter.addAction(Constants.ACTION_MEDIA_UPLOAD_STATUS);
        intentFilter.addAction(Constants.ACTION_MEDIA_UPLOADED);
        intentFilter.addAction(Constants.CONTUS_REMOVE_GROUP_USER);
        intentFilter.addAction(Constants.CONTUS_ADD_GROUP_USER);
        intentFilter.addAction(Constants.CONTUS_NEW_GROUP);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_PROFILE);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_GET_GROUPID);
        intentFilter.addAction(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_EXIT_GROUP_RESPONSE);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_GROUPMEMBER_ADDED);
        intentFilter.addAction(Constants.GROUP_MEMBER_REMOVED);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_GROUPINFO_UPDATE);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_ACTION_SET_DEVICEID);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_PROFILE_UPDATED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.ACTION_REFRESH_CONTACTS);
        intentFilter.addAction(Constants.ACTION_AVAILABILITY_RESULT);
        intentFilter
                .addAction(ContusConstantValues.CONTUS_XMPP_ACTION_MSG_COMPOSE_RESULT);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * Un register my receiver.
     */
    private void unRegisterMyReceiver() {
        try {
            if (broadcastReceiver != null)
                getActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e+"");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onDestroy()
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterMyReceiver();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onStop()
     */

    /**
     * On stop.
     */
    @Override
    public void onStop() {
        super.onStop();
        unRegisterMyReceiver();
    }


    @Override
    public void onPause() {
        super.onPause();
        unRegisterMyReceiver();
    }
}