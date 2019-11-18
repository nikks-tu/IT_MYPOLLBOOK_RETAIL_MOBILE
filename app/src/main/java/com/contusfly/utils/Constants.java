/**
 * @category ContusMessanger
 * @package com.contusfly.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.utils;

/**
 * The Class Constants.
 */
public class Constants {

    /**
     * The Constant NULL_STRING.
     */
    public static final String NULL_STRING = "null";

    public static final String XMPP_HOST = "52.32.79.152",
            XMPP_STUN_ID = "stun.l.google.com",
            XMPP_PRODUCT_KEY = "00f4675d2a936b2e6ad6f985d657bc09";

    /**
     * The Constant XMPP_PORT.
     */
    public static final int XMPP_PORT = 5222;

    /**
     * The Constant HOME_KEY.
     */
    public static final int XMPP_STUN_PORT = 19302, HOME_KEY = 12345, /**
     * The Roster not active.
     */
    ROSTER_OFFLINE = 0, /**
     * The Roster not active.
     */
    ROSTER_ONLNE = 1;

    /**
     * The Constant TAG.
     */
    public static final String TAG = "Contus Fly";

    /**
     * The Constant NULL_VALUE.
     */
    public static final String NULL_VALUE = null;

    /**
     * The Constant TITLE_TYPE.
     */
    public static final String EMPTY_STRING = "", TITLE = "title",
            TITLE_TYPE = "type";

    /**
     * The Constant CONTACTS_SELECTION.
     */
    public static final int ACTIVITY_REQ_CODE = 111, EDIT_REQ_CODE = 112,
            CUSTOM_TONE_REQ = 113, CHAT_SENT_FROM_SENDER = 0, CROP_IMAGE = 121,
            CHAT_REACHED_TO_SERVER = 1, CHAT_REACHED_TO_RECEIVER = 2,
            CHAT_SEEN_BY_RECEIVER = 3, CHAT_FROM_SENDER = 1,
            CHAT_FROM_RECEIVER = 2, NOTIFICATION_ID = 12, VIB_OFF = 0,
            VIB_SHORT = 3, VIB_LONG = 4, STATUS_UPDATE = 1,
            GROUP_NAME_UPDATE = 2, CHAT_TYPE_UNKNOWN = 3;

    /**
     * Chat has been received
     */
    public static final int CHAT_RECEIVED = 1;

    /**
     * The constant CHAT_SENT_FROM_SENDER.
     */
    public static final int CHAT_SENT_CLOCK = 0;

    /**
     * Receipt sent successfully
     */
    public static final int CHAT_RECEIVED_RECEIPT_SENT = 2;

    /**
     * Seen sent successfully
     */
    public static final int CHAT_RECEIVED_SEEN_SENT = 3;

    /**
     * The Constant ONE_SECOND.
     */
    public static final long ONE_SECOND = 1000;
    public static final int SELECT_MAP = 118;
    /**
     * The Pickfile result code.
     */
    public static final int PICKFILE_RESULT_CODE = 101;
    /**
     * Constant SELECT_CONTACT_REQ_CODE
     */
    public static final int SELECT_IMAGE_REQ_CODE = 125;
    /**
     * The Contact req code.
     */
    public static final int CONTACT_REQ_CODE = 114;
    /**
     * The Pick contact req code.
     */
    public static final int PICK_CONTACT_REQ_CODE = 123;
    /**
     * The Select contact req code.
     */
    public static final int SELECT_CONTACT_REQ_CODE = 124;

    /**
     * The Constant GCM_REG_ID.
     */
    public static final String GCM_REG_ID = "gcm_id";

    /**
     * The Constant CONVERSATION_SOUND.
     */
    /**
     * The Constant IS_PROFILE_LOGGED.
     */
    public static final String CHAT_ONGOING_NAME = "chat_ongoing_name",
            CHAT_MESSAGE = "chatmessage", MSG_TYPE_TEXT = "text",
            MSG_TYPE_AUDIO = "audio", MSG_TYPE_VIDEO = "video",
            MSG_TYPE_LOCATION = "location", MSG_TYPE_IMAGE = "image",
            GROUP_ID = "groupId", RESOURCE = "resource", MSG_TYPE_SENT_PATH = "Sent",
            NOTIFICATION_URI = "notification_uri",
            VIBRATION_TYPE = "vibration_type",
            CONVERSATION_SOUND = "conv_sound", LATITUDE = "latitude",
            LONGITUDE = "longitude", IS_PROFILE_LOGGED = "is_profile_logged",
            GCM_SENDER_ID = "83977557175";
    /**
     * The Msg type file.
     */
    public static final String MSG_TYPE_FILE = "file";
    /**
     * The Mime type file.
     */
    public static final String MIME_TYPE_FILE = "file/*";


    /**
     * The Constant ACTION_MEIDA_DOWNLOAD.
     */
    /**
     * The Constant ACTION_AVAILABILITY_RESULT.
     */
    public static final String ACTION_MESSAGE_SENT_TO_SERVER = "com.contusfly.service.action.senttoserver",
            ACTION_MESSAGE_FROM_ROSTER = "com.contusfly.service.action.messagefromroster",
            ACTION_RECIPET_ACK_FROM_ROSTER = "com.contusfly.service.action.messagereceiptfromroster",
            ACTION_MSG_SEEN_FROM_ROSTER = "com.contusfly.service.action.messageseencallback",
            ACTION_MEDIA_UPLOAD_STATUS = "com.contusfly.service.action.mediauploadcallback",
            ACTION_MEDIA_UPLOADED = "com.contusfly.service.action.mediauploaded",
            ACTION_MEIDA_UPLOAD = "com.contusly.service.action.mediaupload",
            ACTION_MEIDA_DOWNLOAD = "com.contusfly.service.action.mediadownload",
            ACTION_GET_AVAILABILITY = "com.contusfly.service.action.getavailability",
            ACTION_REFRESH_CONTACTS = "com.contusfly.service.action.synccontact",
            ACTION_AVAILABILITY_RESULT = "com.contusfly.service.action.availabilityresult",
            ACTION_GET_CHAT_HISTORY = "com.contusfly.service.action.getchathistory",
    /**
     * The Action meida download.
     */
    ACTION_LOGOUT = "com.time.service.action.logout";
    public static final String ACTION_DELETE_ACCOUNT = "com.contus.deleteaccount";

    /**
     * The Constant CONTUS_NEW_GROUP.
     */
    public static final String DELETE_MESSAGE = "com.contus.service.action.DELETE_MESSAGE";

    /**
     * The Constant CONTUS_ADD_GROUP_USER.
     */
    public static final String CONTUS_XMPP_GROUPINFO_CHANGED = "com.contus.service.action.GROUPINFOCHANGED";
    /**
     * The Action roster sync.
     */
    public static final String ACTION_ROSTER_SYNC = "com.contus.roster.sync";
    /**
     * The Constant PARAM_LOCATION.
     */
    public static final String PARAM_FILE_SIZE = "filesize",
            PARAM_DURATION = "duration", PARAM_MESSAGE = "message",
            PARAM_DESCRIPTION = "description", PARAM_LOCAL_PATH = "localpath",
            PARAM_FILE_URL = "fileurl", PARAM_FILE_THUMB = "filethumbnail",
            PARAM_FILE_TYPE = "filetype", PARAM_MSG_TYPE = "messagetype",
            PARAM_LOCATION = "location";

    /**
     * The Constant GROUP_MEMBER_REMOVED.
     */
    public static final String GROUP_MEMBER_REMOVED = "com.contus.service.action.GROUPMEMBEREMOVED";

    /**
     * The Constant CONTUS_REMOVE_GROUP_USER.
     */
    public static final String CONTUS_REMOVE_GROUP_USER = "com.contus.service.action.REMOVEGROUPUSER";

    /**
     * The Constant CONTUS_ADD_GROUP_USER.
     */
    public static final String CONTUS_ADD_GROUP_USER = "com.contus.service.action.ADDGROUPUSER";

    /**
     * The Constant CONTUS_NEW_GROUP.
     */
    public static final String CLEAR_CHAT = "com.contus.service.action.CLEAR_CHAT";
    public static final String ACTION_SESSION_EXPIRE = "com.contus.sessionexpire";

    /**
     * The Constant CONTUS_NEW_GROUP.
     */
    public static final String CONTUS_NEW_GROUP = "com.contus.service.action.NEW_GROUP";
    public static final String CONTACT = "contact";
    public static final String GROUP = "Groups";
    /**
     * The Constant MEDIA_URL.
     */
    public static final String USERNAME = "logged_username", MEDIA_URL = "url";
    /**
     * The Recent chat id.
     */
    public static final String RECENT_CHAT_USER_ID = "chat_user_id";
    /**
     * The Msg type status.
     */
    public static final String MSG_TYPE_STATUS = "status";
    /**
     * The chat Resource.
     */
    public static final String CHAT_RESOURCE = "chat_resource";
    /**
     * Type chat
     */
    public static final String CHAT = "chat";

    /**
     * Receipt in the server
     */
    public static final String RECEIPT = "receipts";

    /**
     * Seen in server
     */
    public static final String SEEN = "seen";
    public static final String GROUP_TYPE = "grouptype";
    public static final String MESSAGE_TO = "message_to";
    public static final String MESSAGE_FROM = "message_from";
    public static final String MESSAGE_TYPE = "message_type";
    public static final String MESSAGE_CONTENT = "message_content";
    public static final String MESSAGE_TIME = "message_time";
    public static final String API_MESSAGE_DELIVERED = "message/delivered";
    public static final String GROUP_FROM = "group_from";
    public static final String CHAT_MESSAGE_TYPE = "chat_message_type";
    public static final String IS_SINGLE_CHAT = "singleChat";
    /**
     * The Error.
     */
    public static final String ERROR = "error";
    /**
     * The Braodcast type.
     */
    public static final String BRAODCAST_TYPE = "broadcasttype";
    /**
     * The Msg type contact.
     */
    public static final String MSG_TYPE_CONTACT = "contact";
    /**
     * The Media download.
     */
    public static final String MEDIA_DOWNLOAD = "media_download";
    public static final String MESSAGE_ID = "message_id";
    /**
     * The Chat media name.
     */
    public static final String CHAT_MEDIA_NAME = "file_name";
    /**
     * The Recent chat msg id.
     */
    public static final String RECENT_CHAT_MSG_ID = "chat_msg_id";
    public static final String RESULT = "result";
    public static final String RESULT_TYPE = "result_type";
    public static final String PROFILE_UPDATE = "update_profile";
    /**
     * The Max upload size.
     */
    public static final String MAX_UPLOAD_SIZE = "max_upload_size";
    public static final String SELECTED_IMAGE = "Selected Image";

    /**
     * The Constant IS_BROADCAST.
     */
    public static final String
            IS_BROADCAST = "is_broadcast";
    /**
     * The Constant USER_PROFILE_NAME.
     */
    public static final String SECRET_KEY = "password",
            IS_LOGGED_IN = "is_logged_in",
            IS_CONTACTS_SYNCED = "is_contacts_synced",
            LAST_SYNC_TIME = "last_sync_time",
            IS_FROM_NOTIFICATION = "is_from_notication",
            USER_STATUS = "user_status", USER_IMAGE = "user_profile_image",
            USER_PROFILE_NAME = "user_profile_name";

    /**
     * The Constant GROUP_CHAT.
     */
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg",
            MIME_TYPE_IMAGE = "image/*", MIME_TYPE_VIDEO = "video/*",
            MIME_TYPE_AUDIO = "audio/*", MIME_TYPE_ALL = "video/* images/* ",
            GROUP_CHAT = "groupchat", COUNTRY = "country",
            COUNTRY_CODE = "country_code";


    /**
     * The Constant ROSTER_NOT_ACTIVE.
     */
    public static final int TAKE_PHOTO = 1, FROM_GALLERY = 2, TAKE_VIDEO = 3,
            COUNT_ZERO = 0, COUNT_ONE = 1, COUNT_TWO = 2, CHAT_TYPE_SINGLE = 0,
            CHAT_TYPE_GROUP = 1, ONE_KB = 1024, CHAT_TYPE_BROADCAST = 2,
            COUNT_THREE = 3, MEDIA_UPLOADING = 1, MEDIA_NOT_UPLOADED = 0,
            MEDIA_UPLOADED = 2, MEDIA_DOWNLADING = 3, MEDIA_DOWNLADED = 5,
            MEDIA_NOT_DOWNLOADED = 4, ROSTER_NOT_ACTIVE = -1;

    /**
     * The Constant ROSTER_CUSTOM_TONE.
     */
    public static final String TABLE_ROSTER = "table_roster",
            ROSTER_NAME = "roster_name", ROSTER_IMAGE = "roster_image",
            ROSTER_USER_ID = "roster_user_id", ROSTER_STATUS = "roster_status",
            ROSTER_AVAILABILITY = "roster_availability",
            ROSTER_LAST_SEEN = "roster_last_seen",
            ROSTER_TYPE = "roster_type", ROSTER_ADMINS = "roster_admins",
            ROSTER_GROUP_USERS = "roster_group_users",
            ROSTER_IS_BLOCKED = "roster_is_blocked",
            ROSTER_GROUP_STATUS = "roster_group_status",
            ROSTER_CUSTOM_TONE = "roster_custom_tone";


    /**
     * The Constant CHAT_TO_USER.
     */
    public static final String TABLE_CHAT_DATA = "table_chat_data",
            CHAT_COLUMN_ID = "chat_msg_id",
            CHAT_COLUMN_FROM_USER = "chat_from",
            CHAT_COLUMN_TO_USER = "chat_to", CHAT_COLUMN_MSG = "chat_msg",
            CHAT_MSG_TYPE = "chat_msg_type", CHAT_TYPE = "chat_type",
            CHAT_COLUMN_STATUS = "chat_status", CHAT_TIME = "chat_time",
            CHAT_MSG_IS_DOWNLOADED = "chat_is_downloaded",
            CHAT_MSG_THUMB = "chat_thumbnail",
            CHAT_COLUMN_RECEIVERS = "chat_receivers",
            CHAT_ENCODED_IMAGE = "chat_enc_image",
            CHAT_FILE_SIZE = "chat_file_size",
            CHAT_MEDIA_LOCAL_PATH = "chat_media_local_path",
            CHAT_MEDIA_SERVER_PATH = "chat_media_server_path",
            CHAT_FROM_TO_USER = "chat_from_to_user",
            CHAT_TO_USER = "chat_to_user";

    /**
     * The Constant RECENT_CHAT_USERS.
     */
    public static final String TABLE_RECENT_CHAT_DATA = "table_recent_chats",
            RECENT_CHAT_ID = "chat_user_id", RECENT_CHAT_MSG = "chat_msg",
            RECENT_CHAT_TYPE = "chat_type",
            RECENT_CHAT_UN_REED_COUNT = "chat_unreed_count",
            RECENT_CHAT_IS_SEEN = "chat_is_seen",
            RECENT_CHAT_STATUS = "chat_status",
            RECENT_LAST_CHAT_FROM = "last_chat_type",
            RECENT_CHAT_IMAGE = "chat_image",
            RECENT_CHAT_NAME = "chat_user_name",
            RECENT_CHAT_TIME = "chat_time",
            RECENT_CHAT_MSG_TYPE = "recent_chat_msg_type",
            RECENT_CHAT_GRP_NAME = "recent_chat_grpname",
            RECENT_CHAT_USERS = "recent_chat_users";

    /**
     * The Constant PENDING_CHAT_TIME.
     */
    public static final String TABLE_PENDING_CHATS = "table_pending_chats",
            PENDING_CHAT_ID = "chat_user_id", PENDING_CHAT_MSG = "chat_msg",
            PENDING_CHAT_TYPE = "chat_type", PENDING_CHAT_IMAGE = "chat_image",
            PENDING_CHAT_NAME = "chat_user_name",
            PENDING_CHAT_TIME = "chat_time";


    /**
     * The Constant STATUS_TXT.
     */
    public static final String TABLE_USER_STATUS = "table_user_status",
            STATUS_ID = "_id", STATUS_TXT = "status_txt";


    public static final String TABLE_GROUP_MSG_STATUS = "table_group_msg_status", CHAT_GROUP_ID = "group_id",
            CHAT_MSG_ID = "chat_msg_id", CHAT_USER_ID = "chat_user_id", CHAT_MSG_STATUS = "chat_msg_status",
            CHAT_MSG_DELIVERY_TIME = "chat_msg_d_time", CHAT_MSG_SEEN_TIME = "chat_msg_s_time", CHAT_IS_ACK = "chat_is_ack";
    ;
    public static final String USER_ID = "user_id";

    /**
     * The Constant COUNTRY_DIAL_CODE.
     */
    public static final String TABLE_COUNTRIES_LIST = "table_countries",
            COUNTRY_NAME = "country_name", COUNTRY_DIAL_CODE = "dial_code";
    public static String PHONE_NUMBER_CODE = "phone_number_code";
    public static String VIBRATION_TYPE_NAME = "vibrate_type_name";


    /**
     * Instantiates a new constants.
     */
    private Constants() {
        // Unused code added for sonar qube validation
    }
}
