package com.contus.app;

/**
 * Created by user on 6/29/2015.
 */
public interface Constants {
    //new live  Url//
    //String LIVE_BASE_URL = "http://admin.mypollbook.com/";
    //new Test url//
    //String LIVE_BASE_URL = "https://uat.admin.mypollbook.com/mypbR/public/";
    //52.39.212.235 http://uatr.mypollbook.com/
    //http://182.18.177.27:78/mypbR/public/
    //QA
    String LIVE_BASE_URL = "http://182.18.177.27:78/mypbR/public/";
    //production
    //String LIVE_BASE_URL = "https://admin.mypollbook.com/";
    //String LIVE_BASE_URL = "http://182.18.177.27:78/mypbProdRetail/public/";
    //Black Card URL//
    //String LIVE_BASE_URL = "http://admin.mypollbook.com/BlackCard/public/";

    String TOP_10_USERS = LIVE_BASE_URL + "/api/v1/topusers";
    String GET_EARNING = LIVE_BASE_URL + "/api/v1/earningsactions";
    String GET_REDEEM_ITEMLIST = LIVE_BASE_URL + "/api/v1/earningsactions";
    String GET_REWARDS = LIVE_BASE_URL + "/api/v1/rewards";

    //one
    int LAYOUT_ONE = 1;
    //two
    int LAYOUT_TWO = 2;
    //three
    int LAYOUT_THREE = 3;
    //four
    int LAYOUT_FOUR = 4;
    String DEVICE_ID = "device_id";
    //terms and condition
    String TERMS_AND_CONDITION_ACTION = "com.contus.activity.TermsAndCondition";
    //register
    String REGISTERACTIVITY_ACTION = "com.contus.activity.RegisterActivity";
    //acount verification
    String ACCOUNTVERIFICATION_ACTION = "com.contus.activity.AccountVerification";
    //personal info
    String PERSONALINFO_ACTION = "com.contus.activity.PersonalInfo";
    //dialog posuition
    String DIALOG_POSITION = "dialog position";
    //country
    String COUNTRY = "country_name";
    //phone number
    String PHONE_NUMBER = "phone_number";
    //user id
    String ACCESS_TOKEN = "access_token";
    //number code
    String PHONE_NUMBER_CODE = "phone_number_code";
    //profiel image
    String PROFILE_IMAGE = "profile_image";
    //true
    String PROFILE_IMAGE_TRUE = "true";
    //country id
    String COUNTRY_ID = "country_id";
    //code
    String COUNTRY_CODE = "country_code";
    //user anme
    String PROFILE_USER_NAME = "profile_user_name";
    //city
    String CITY = "CITY";
    //type
    String TYPE = "type";
    //participate count
    String PARTICIPATE_COUNT = "participate_count";
    //gender
    String USER_GENDER = "gender";
    //first time
    String FIRST_TIME = "is_first_time";
    //poll answer options
    String POLL_ANSWER_OPTION = "poll_answer_option";
    //yse or no
    int ID_YES_OR_NO = 1;
    //multiple options
    int ID_MULTIPLE_OPTIONS = 2;
    //photo comparision
    int ID_PHOTO_COMPARISON = 3;
    //group
    int ID_ADD_GROUP = 1;
    //chat
    int ID_CHAT = 2;
    //email
    //App Version
    String APP_VERSION_CHECK = "api/v1/version";

    String SHIP_THE_ITEM = "api/v1/placingredeem";
    String GET_ADDRESS = "api/v1/getaddress";
    //Group related APIS
    String GET_GROUPS = "api/v1/getgroups";
    String CREATE_GROUP = "api/v1/creategroup";
    String EMAIL = "email";
    //status
    String STATUS = "status";
    //id
    String CAMPAIGN_ID = "campaign_ID";
    //name
    String CAMPAIGN_NAME = "campaign_name";
    String CAMPAIGN_DATE = "campaign_date";
    //category
    String CAMPAIGN_CATEGORY = "campaign_category";
    //logo
    String CAMPAIGN_LOGO = "campaign_logo";
    //youtube url
    String YOUTUBE_URL = "youtube_url";
    //updated date
    String DATE_UPDATED = "updated_date";
    //s3
    String SAMSUNG_S3 = "GT-I9300";
    //question1
    String QUESTION1 = "question1";
    //opt code
    String OTPCODE = "otp_code";
    //yes or not
    String YES_OR_NO = "yes_or_no";
    //admin polls api
    String PUBLICPOLLS_API = "adminPolls_api";
    //limit
    String LIMIT = "10";
    //admin polls api
    String PUBLICPOLLS_API_PATH = "/adminPollsApi";
    //campaign review
    String CAMPAIGN_REVIEW_API_PATH = "/campaignReview";
    //page
    String PAGE = "page";
    //limit
    String LIMIT_STRING = "limit";
    //search user poll
    String SEARCH_POLLS_API = "searchUserPoll";
    //search api path
    String SEARCH_API_PATH = "/searchUserPollApi";
    //search key
    String SEARCH = "search_key";
    //campaign likes user array
    String CAMPAIGN_LIKES_USER_ARRAY = "campaign_likes_array";
    //campaign like size
    String CAMPAIGN_LIKES_USER_SIZE = "campaign_likes_size";
    //Campaign like count
    String CAMPIGN_LIKES_COUNT_ARRAY = "campaign_likes_count_array";
    //campaign like count
    String CAMPIGN_LIKES_COUNT_SIZE = "campaign_likes__count_size";
    //msg verification code
    String MSG_VERIFICATION_CODE = "msg_verification_code";
    //campaign comments size
    String CAMPAIGN_COMMENTS_COUNT = "campaign_comments_count";
    //comments size
    String CAMPAIGN_COMMENTS_SIZE = "comments_size";
    //comment count position
    String COMMENTS_COUNT_POSITION = "campaign_comments_position";
    //poll review
    String COMMENTS_COUNT_POLL_REVIEW = "campaign_comments_count_REVIEW";
    //comments count
    String CAMPAIGN_POLL_COMMENTS_COUNT = "camapign_poll_comments_count";
    //likes user
    String CAMPAIGN_POLL_LIKES_USER_ARRAY = "camapign_poll_likes_user";
    //poll likes count
    String CAMPIGN_POLL_LIKES_COUNT_ARRAY = "camapign_poll_likes_count";
    //poll comments size
    String CAMPAIGN_POLL_COMMENTS_SIZE = "camapign_poll_comments_size";
    //poll likes user size
    String CAMPAIGN_POLL_LIKES_USER_SIZE = "camapign_poll_likes_user_size";
    //Poll like count size
    String CAMPIGN_POLL_LIKES_COUNT_SIZE = "camapign_poll_likes_count_size";
    //poll type
    String POLL_TYPE = "poll_type";
    //Youtube key
    String YT_KEY = "AIzaSyCGe2c6COIdsu_YlUdhSoaSGAQAQ2ibb5U";
    //comments count
    String USER_POLL_COMMENTS_COUNT = "user_poll_comments_count";
    //Comments size
    String USER_POLL_COMMENTS_SIZE = "user_poll_comments_size";
    //array size
    String USER_POLL_LIKES_USER_ARRAY = "user_poll_likes_array_size";
    public static final String PARAM_FILE_SIZE = "filesize", PARAM_DURATION = "duration", PARAM_MESSAGE = "message",
            PARAM_DESCRIPTION = "description", PARAM_LOCAL_PATH = "localpath", PARAM_FILE_URL = "fileurl",
            PARAM_FILE_THUMB = "filethumbnail", PARAM_FILE_TYPE = "filetype", PARAM_MSG_TYPE = "messagetype",
            PARAM_LOCATION = "location", PARAM_CONTACT = "contact";
    //user size
    String USER_POLL_LIKES_USER_SIZE = "user_poll_likes_user_size";
    //User poll likes array
    String USER_POLL_LIKES_COUNT_ARRAY = "user_poll_likes_count_array";
    //User poll count size
    String USER_POLL_LIKES_COUNT_SIZE = "user_poll_likes_count_size";
    //campaign share url
    String SHARE_CAMPAIGN_BASE_URL = Constants.LIVE_BASE_URL + "sharedcampaignpolls/";
    //Share poll url
    String SHARE_POLL_BASE_URL = Constants.LIVE_BASE_URL + "sharedpolls/";
    //answer array
    String USER_POLL_ID_ANSWER_ARRAY = "user_poll_id_answer_array";
    //answer array
    String USER_POLL_ID_ANSWER_SIZE = "user_poll_id_answer_size";
    //user poll id selected arrauy
    String USER_POLL_ID_ANSWER_SELECTED_ARRAY = "user_poll_id_answer_selected_array";
    //answer slected size
    String USER_POLL_ID_SELECTED_SIZE = "user_poll_id_answer_selected_size";
    //answer size
    String MY_POLL_ID_ANSWER_SIZE = "my_poll_id_answer_size";
    //Answer selected size
    String MY_POLL_ID_SELECTED_SIZE = "my_poll_id_answer_selected_size";
    //Answer array
    String MY_POLL_ID_ANSWER_ARRAY = "my_poll_id_answer_array";
    //answer selected array
    String MY_POLL_ID_ANSWER_SELECTED_ARRAY = "my_poll_id_answer_selected_array";
    //Campaign poll id answer array
    String CAMPAIGN_POLL_ID_ANSWER_ARRAY = "campaign_poll_id_answer_array";
    //Campaign poll id answer array
    String CAMPAIGN_POLL_ID_ANSWER_SELECTED_ARRAY = "campaign_poll_id_answer_selected_array";
    //Campaign poll id answe size
    String CAMPAIGN_POLL_ID_ANSWER_SIZE = "campaign_poll_id_answer_size";
    //campaign id selected size
    String CAMPAIGN_POLL_ID_SELECTED_SIZE = "campaign_poll_id_answer_selected_array";
    //Position
    String ARRAY_POSITION = "position";
    //My poll comments count
    String MY_POLL_COMMENTS_COUNT = "my_poll_comments_count";
    //Poll comments size
    String MY_POLL_COMMENTS_SIZE = "my_poll_comments_size";
    //My poll likes suer array
    String MY_POLL_LIKES_USER_ARRAY = "my_poll_likes_array_size";
    //My poll sarray size
    String MY_POLL_LIKES_USER_SIZE = "my_poll_likes_user_size";
    //My poll likes user size
    String MY_POLL_LIKES_COUNT_ARRAY = "my_poll_likes_count_array";
    //Likes count size
    String MY_POLL_LIKES_COUNT_SIZE = "my_poll_likes_count_size";
    //Category count array
    String CATEGORY_COUNT_ARRAY = "category_count_array";
    //Category count size
    String CATEGORY_COUNT_SIZE = "category_count_size";
    //Campaign participate count
    String CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY = "campaign_poll_participate_count_array";
    //Campaign participate count size
    String CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE = "campaign_poll_participate_count_size";
    //User poll participate count array
    String USER_POLL_PARTICIPATE_COUNT_ARRAY = "user_poll_participate_count_array";
    //User poll participate count size
    String USER_POLL_PARTICIPATE_COUNT_SIZE = "user_poll_participate_count_size";
    //My poll participate count array
    String MY_POLL_PARTICIPATE_COUNT_ARRAY = "my_poll_participate_count_array";
    //My poll participate count size
    String MY_POLL_PARTICIPATE_COUNT_SIZE = "my_poll_participate_count_size";
    //Search poll comments count
    String SEARCH_POLL_COMMENTS_COUNT = "search_poll_comments_count";
    //Search poll comments size
    String SEARCH_POLL_COMMENTS_SIZE = "search_poll_comments_size";
    //Search poll likes size
    String SEARCH_POLL_LIKES_USER_ARRAY = "search_poll_likes_array_size";
    //Search poll likes  user size
    String SEARCH_POLL_LIKES_USER_SIZE = "search_poll_likes_user_size";
    //Search poll likes  count array
    String SEARCH_POLL_LIKES_COUNT_ARRAY = "search_poll_likes_count_array";
    //Search poll likes  count size
    String SEARCH_POLL_LIKES_COUNT_SIZE = "search_poll_likes_count_size";
    //Search poll participate count array
    String SEARCH_POLL_PARTICIPATE_COUNT_ARRAY = "search_poll_participate_count_array";
    //Search poll participate count size
    String SEARCH_POLL_PARTICIPATE_COUNT_SIZE = "search_poll_participate_count_size";
    //Search poll poll id answer size
    String SEARCH_POLL_ID_ANSWER_SIZE = "search_poll_id_answer_size";
    //Search poll poll id answer selected size
    String SEARCH_POLL_ID_SELECTED_SIZE = "search_poll_id_answer_selected_size";
    //Search poll poll id answer array
    String SEARCH_POLL_ID_ANSWER_ARRAY = "search_poll_id_answer_array";
    //Search poll poll id answer selected array
    String SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY = "search_poll_id_answer_selected_array";
    //Delete campaign comments
    String DELETE_CAMP_COMMENTS = "/deleteCampComments";
    //action
    String ACTION = "action";
    //Camaping id
    String CAMPAIGN_UNIQUE_ID = "campaign_id";
    //User id
    String USER_ID = "user_id";
    //Comments id
    String COMMENT_ID = "comment_id";
    //Comments
    String COMMENTS = "comments";
    //Edit camp comments
    String EDIT_CAMP_COMMENTS = "/editCampComments";
    //Delete camp comments
    String DELETE_CAMP_POLL_COMMENTS = "/deletePollComments";
    //Poll comment id
    String POLL_COMMENT_ID = "poll_comment_id";
    //Poll  id
    String POLL_ID = "poll_id";
    //Poll comments
    String POLL_COMMENTS = "poll_comments";
    //edit poll comments
    String EDIT_POLL_COMMENTS = "/editPollComments";
    //Public poll api
    String PUBLIC_POLL_API = "/publicPollsApi";
    //Category id
    String CATEGORY_ID = "category_id";
    //Category api
    String CATEGORY_API = "/categoryapi";
    //Category select api
    String CATEGORY_SELECT_API = "/categoryselectapi";
    //Camp comments api
    String CAMP_COMMENTS_API = "/campaignCommentApi";
    //Pol comments api
    String POLL_COMMENTS_API = "/pollCommentApi";
    //Get poll comments api
    String GET_POLL_COMMENTS_API = "/getPollCommentApi";
    //Campaign get comments api
    String GET_CAMPAIGN_COMMENTS_API = "/campaignGetCommentApi";

    String CHECK_CONTACTS = "api/v1/existingcontacts";
    String GET_GROUP_POLLS = "api/v1/getgrouppolls";
    String GET_SINGLE_GROUP = "api/v1/getsinglegroup";
    String REMOVE_CONTACTS_FROM_GROUP = "api/v1/removecontactsfromgroup";
    //limit
    String PAGE_LIMIT = "limit";
    //Contact us
    String CONTACTUS_API = "/contactus";
    //Contact us
    String CONTACT_DETAILS = "/contactApi";
    //Countries
    String COUNTRY_API = "/countries";
    //Save user poll
    String SAVE_USER_POLLS_API = "/saveUserPolls";
    //user type
    String USER_TYPE = "user_type";
    //welcome api
    String WELCOME_API = "/welcome";
    //user polls api
    String USER_POLL_API = "/userpoll";
    String VERSION = "/version";
    String POLL_UNSEEN_API = "/userpoll/seen";
    //mypolls api
    String MYPOLL_API = "/myPollsApi";
    //terms api

    String TOP_10_USERS_retrofit = "/topusers";

    String EARNINGS_retrofit = "/earningsactions";

    String GET_REWARDS_RETROFIT =  "rewards";

    String TERMS_API = "/terms";
    //about us api
    String ABOUTSUS_API = "/aboutus";
    //sms verify api
    String SMS_VERIFY_API = "/smsVeryfyApi";
    //Phone number
    String PHONE_NUM = "phone_no";
    //optcode
    String OPTCODE = "optcode";
    //sms api
    String SMS_API = "/smsApi";
    //Country search
    String SEARCH_COUNTRY_API = "/countriessearch";
    //search key
    String SEARCH_KEY = "searchKey";
    //contact api
    String CONTACT_US_ACTION = "contactapi";
    //picture file
    String pictureFile = "file://";
    //Campaign
    String CAMPAIGN = "campaign";
    //Campaign
    String MOBILE_CONTACTS = "mobile_contacts";
    //Polls
    String POLLS = "polls";
    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    String SMS_ORIGIN = "VK-TSTMYI";
    //String LIVE_BASE_URL = "http://admin.mypollbook.com/";
    //String LIVE_BASE_URL = "http://182.18.177.27:78/Thinqpoll12-09-17/Thinqpoll/public/";
    //String LIVE_BASE_URL = "http://192.168.2.186/msve2747-thinqpoll-thinkdigital-web/public/";
    //String LIVE_BASE_URL="http://52.39.212.235/"
    //new Server Url//
    //String LIVE_BASE_URL = "http://34.215.115.42/mypollbook/public/";
    //String LIVE_BASE_URL = "http://admin.mypollbook.com/";
    String USER_NAME_CHAT = "user_name_chat";
    //Push notification sender id
    String SENDER_ID = "512194804194";
    //live url
    //String LIVE_BASE_URL="http://dev.contus.us/mobi/MSVE2747/public/";
    //search back press value
    String SEARCH_BACKPRESS_BOOLEAN = "serach_backpress_boolean";
    //gcm registration id
    String GCM_REG_ID = "gcm_registeration_id";
    //mobile number
    String MOBILE_NUMBER = "mobile_number";
    //fragment
    String FRAGMENT = "fragment";
    //fragment
    String ACTIVITY_GROUP_INFO = "activity_group_info";
    //fragment
    String ACTIVITY_GROUP_NAME_INFO = "activity_group_name_info";
    //fragment
    String ACTIVITY_GROUP_NAME_INFO_SIZE = "activity_group_name_info_size";
    //fragment
    String ACTIIVTY_GROUP_INFO_SIZE = "activity_group_size";
    String ACTIVITY_CONTACT_INFO = "activity_contact_info";
    //fragment
    String ACTIIVTY_CONTACT_INFO_SIZE = "activity_contact_size";
    String ACTIVITY_CATEGORY_INFO = "activity_category_info";
    //fragment
    String ACTIIVTY_CONTACT_NAME_INFO_SIZE = "activity_contact_name_size";
    String ACTIVITY_CONTACT_NAME_INFO = "activity_contact_name_info";
    //fragment
    String ACTIVITY_CATEGORY_INFO_SIZE = "activity_category_info_size";
    String ACTIVITY_CATEGORY_COMPLETE_INFO = "activity_category_complete_info";
    String ACTIVITY_CATEGORY__COMPLETE_INFO_SIZE = "activity_category_complete_info_size";
    String EMPTY_STRING = "";
    String SELECTED_PRODUCT="";
    String IS_IN_GROUP="IS_IN_GROUP";

    String GET_SHARE_URL ="/getshareurl" ;
    /**
     * The Constant IS_PROFILE_LOGGED.
     */
    public static final String CHAT_ONGOING_NAME = "chat_ongoing_name", CHAT_MESSAGE = "chatmessage",
            MSG_TYPE_TEXT = "text", MSG_TYPE_FILE = "file", MSG_TYPE_CONTACT = "contact", MSG_TYPE_AUDIO = "audio", MSG_TYPE_VIDEO = "video",
            MSG_TYPE_LOCATION = "location", MSG_TYPE_IMAGE = "image", GROUP_ID = "groupId", RESOURCE = "resource",
            MSG_TYPE_SENT_PATH = "Sent", NOTIFICATION_URI = "notification_uri", VIBRATION_TYPE = "vibration_type",
            CONVERSATION_SOUND = "conv_sound", LATITUDE = "latitude", LONGITUDE = "longitude",
            IS_PROFILE_LOGGED = "is_profile_logged", GCM_SENDER_ID = "83977557175";

    // String LIVE_BASE_URL="http://testing.contus.us/mobi/MSVE2747/public/";
    //String LIVE_BASE_URL="http://183.82.112.9/thinqpoll/public/";
    //  String LIVE_BASE_URL="http://release.contus.us/mobi/MSVE2747/public/";
    public static final String GET_MESSAGE_UNREAD_COUNT = "get_message_unread_count";
    public static final String KEY_NULL = "";
    public static final String getProfileOnline = "profile_from_service";
    String CONTACT_LIST = "CONTACT_LIST";
    String GET_GROUP_POLL_ID = "GET_GROUP_POLL_ID" ;
    String GET_GROUP_NAME = "GET_GROUP_NAME" ;
    //api key   AIzaSyBzCmvHuG1DM0STmTsL9jdrRMQMRc1fEEs   senderid 1086977209292
}
