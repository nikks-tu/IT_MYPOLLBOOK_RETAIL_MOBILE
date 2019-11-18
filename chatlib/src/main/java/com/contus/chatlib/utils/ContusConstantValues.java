package com.contus.chatlib.utils;

import java.util.Locale;

/**
 * Created by user on 4/10/16.
 */
public class ContusConstantValues {
    public static final String CONTUS_XMPP_ACTION_CONNECTION_REQUEST = "com.contus.service.action.CONNECT";
    public static final String CONTUS_XMPP_ACTION_NEW_CONNECTION_REQUEST = "com.contus.service.action.NEW_CONNECT";
    public static final String CONTUS_XMPP_ACTION_CONNECTION_ERROR = "com.contus.service.action.ERROR";
    public static final String CONTUS_XMPP_ACTION_CHAT_REQUEST = "com.contus.service.action.CHAT";
    public static final String CONTUS_XMPP_ACTION_GET_ROSTER = "com.contus.service.action.ROSTER";
    public static final String CONTUS_XMPP_ACTION_GET_PROFILE = "com.contus.service.action.PROFILE";
    public static final String CONTUS_XMPP_ACTION_SET_DEVICEID = "com.contus.service.action.SETDEVICEID";
    public static final String CONTUS_XMPP_ACTION_GET_ROSTER_TOUSER = "com.contus.service.action.TOUSER";
    public static final String CONTUS_XMPP_ACTION_EDIT_PROFILE = "com.contus.service.action.EDITPROFILE";
    public static final String CONTUS_XMPP_ACTION_SEND_PROFILE_REQ = "com.contus.service.action.SENDPROFILE";
    public static final String CONTUS_XMPP_PROFILE_UPDATED = "com.contus.service.action.PROFILEUPDATED";
    public static final String CONTUS_XMPP_CREATE_GROUP = "com.contus.service.action.CREATEGROUP";
    public static final String CONTUS_XMPP_GET_GROUPID = "com.contus.service.action.GETGROUPID";
    public static final String CONTUS_XMPP_ADD_GROUP_MEMBER = "com.contus.service.action.ADDGROUPMEMBER";
    public static final String CONTUS_XMPP_GROUPMEMBER_ADDED = "com.contus.service.action.GROUPMEMBERADDED";
    public static final String CONTUS_XMPP_GET_GROUP_INFO = "com.contus.service.action.GETGROUPINFO";
    public static final String CONTUS_XMPP_UPDATE_GROUP_INFO = "com.contus.service.action.UPDATEGROUPINFO";
    public static final String CONTUS_XMPP_GET_GROUPS = "com.contus.service.action.GETGROUPS";
    public static final String CONTUS_XMPP_GROUPINFO_UPDATE = "com.contus.service.action.GROUPSINFOUPDATE";
    public static final String CONTUS_XMPP_REMOVE_GROUP_MEMBER = "com.contus.service.action.REMOVEGROUPMEMBER";
    public static final String CONTUS_XMPP_EXIT_GROUP = "com.contus.service.action.EXITGROUP";
    public static final String CONTUS_XMPP_EXIT_GROUP_RESPONSE = "com.contus.service.action.EXITGROUPRESPONSE";
    public static final String CONTUS_XMPP_GET_ROSTER = "com.contus.service.action.GETROSTER";
    public static final String CONTUS_XMPP_MESSAGE_SEND_RECEPIT = "com.contus.service.action.SENDRECEPIT";
    public static final String CONTUS_XMPP_MESSAGE_SEND_SEEN = "com.contus.service.action.SENDSEEN";
    public static final String CONTUS_XMPP_MESSAGE_SEND_CHAT_INVITATION = "com.contus.service.action.INVITATION";
    public static final String CONTUS_XMPP_MESSAGE_GET_CHAT_HISTORY = "com.contus.service.action.CHATHISTORY";
    public static final String CONTUS_XMPP_PRESENCE_CHANGE = "com.contus.service.action.PRESENCE";
    public static final String CONTUS_XMPP_ISDEVICE_ADDED = "com.contus.service.action.DEVICEADDED";
    public static final String CONTUS_SCREEN_ON_OFF = "com.contus.service.action.SCREENACTION";
    public static final String CONTUS_XMPP_LOGIN_RESPONSE_TYPE = "xmpptype";
    public static final String CONTUS_XMPP_RESPONSE_ERROR = "xmpperror";
    public static final String CONTUS_XMPP_LOGIN_RESPONSE_DATA = "xmppresponse";
    public static final String CONNECTION_KEY = "connection";
    public static final String CONTUS_XMPP_ACTION_CHAT_RESPONSE = "com.example.service.action.CHAT_RESPONSE";
    public static final String CONTUS_XMPP_ACTION_CHAT_RECEIPT_UPDATE = "com.example.service.action.CHATRECEIPTUPDATE";
    public static final String CONTUS_XMPP_ACTION_CHAT_ACK = "com.example.service.action.CHATRECEIPTACK";
    public static final String CONTUS_XMPP_ACTION_CHAT_SEEN_UPDATE = "com.example.service.action.CHATSEENUPDATE";
    public static final String CONTUS_XMPP_ACTION_MSG_COMPOSE = "com.chat.service.action.compose";
    public static final String CONTUS_XMPP_ACTION_MSG_COMPOSE_RESULT = "com.chat.service.action.compose.result";
    public static final String CHAT_RESPONSE = "chatresponse";
    public static final String LOG_TAG = "webrtcadjava";
    public static final String CHAT_KEY = "chat";
    public static final String ROSTER_KEY = "roster";
    public static final String SET_GROUP = "set_group";
    public static final String ERROR = "Error";
    public static final String BUCKET_NAME;

    public static final String CONTUS_XMPP_GET_BROADCAST_ID = "com.contus.service.action.GET_BROADCAST_ID";
    public static final String CONTUS_XMPP_BROADCAST_MEMBER_ADDED = "com.contus.service.action.BROADCAST_MEMBER_ADDED";
    public static final String CONTUS_XMPP_BROADCAST_DELETE_RESPONSE = "com.contus.service.action.BROADCAST_DELETE_RESPONSE";
    public static final String CONTUS_XMPP_BROADCAST_INFO_UPDATE = "com.contus.service.action.BROADCAST_INFO_UPDATE";
    public static final String CONTUS_XMPP_CREATE_BROADCAST = "com.contus.service.action.CREATE_BROADCAST";
    public static final String CONTUS_XMPP_ADD_BROADCAST_MEMBER = "com.contus.service.action.ADD_BROADCAST_MEMBER";
    public static final String CONTUS_XMPP_REMOVE_BROADCAST_MEMBER = "com.contus.service.action.REMOVE_BROADCAST_MEMBER";
    public static final String CONTUS_XMPP_UPDATE_BROADCAST_INFO = "com.contus.service.action.UPDATE_BROADCAST_INFO";
    public static final String CONTUS_XMPP_DELETE_BROADCAST = "com.contus.service.action.DELETE_BROADCAST";
    public static byte[] KEY_VALUE;

    static {
        BUCKET_NAME = "contustestbucket".toLowerCase(Locale.US);
        KEY_VALUE = new byte[]{(byte) 118, (byte) 101, (byte) 114, (byte) 115, (byte) 101, (byte) 97, (byte) 112, (byte) 112, (byte) 108, (byte) 105, (byte) 99, (byte) 97, (byte) 116, (byte) 105, (byte) 111, (byte) 110};
    }

    private ContusConstantValues() {
    }
}
