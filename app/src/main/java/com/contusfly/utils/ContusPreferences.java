package com.contusfly.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.polls.polls.R;


public class ContusPreferences {

    public SharedPreferences appSharedPrefs;
    public Editor prefsEditor;
    public Context context;

    public static final String PRESENT_USER_NAME = "presentusername";
    public static final String PRESENT_NOTIFY_LASTSEEN = "notifycount";
    public static final String PRESENT_USER_ID = "presentuserid";
    public static final String PRESENT_RESOURCE = "presentresource";
    public static final String PRESENT_USER_PD = "presentuserpd";
    public static final String FIRST_TIME_LOGIN = "isFirst";
    public static final String PRESENT_TO_NAME = "presentToUsername";
    public static final String DEVICE_TOKEN = "devicetoken";
    public static final String COUNTRY_CODE = "countrycode";
    public static final String COUNTRY_ID = "countryid";
    public static final String ACCESS_TOKEN = "accesstoken";
    public static final String MOBILE = "mobile";
    public static final String MOBILE_CODE = "mobile";
    public static final String USER_MOBILE = "mobile";
    public static final String IS_DEVICE_ID_ADDED = "isdeviceidadded";
    public static final String APP_VERSION = "appversion";
    public static final String UNSEEN_MSG_COUNT = "unseenmsgcount";
    public static final String IS_LOGGEDIN = "isloggedin";
    public static final String MESSAGE_ID = "messageId";
    public static final String MULTILOGIN = "multiLogin";
    public static final String IS_BACKGROUND = "background";
    public static final String LST_SCROLL_HANDLING = "listback";
    public static final String IS_CHATVIEW_FG = "ischatviewfg";
    public static final String CONTACT_LOAD = "contactload";
    public static final String USER_PROFILE_IMG = "user_prof_image";
    public static final String USER_PROFILE_NAME = "user_prof_name";
    public static final String USER_PROFILE_STATUS = "user_prof_status";
    public static final String IS_FIRST_PROGRESS = "is_first_progress";
    public static final String ROSTER_LAST_SYNC_TIME = "roster_last_sync_time";
    public static final String USER_ID = "user_id";
    public static final String MUSIC_PALY = "media_paly";
    public static final String CHAT_NOTIFY = "notify";
    public static final String PLAY_POS = "play_pos";
    public static final String ARTIST_POS = "artist_pos";
    public static final String GCM_NOTIFICATION = "gcm_notification";
    public static final String NOTIFICATION_URI = "notification_uri";


    public ContusPreferences() {

    }

    @SuppressLint("CommitPrefEdits")
    public ContusPreferences(Context ctx) {
        this.context = ctx;
        String appPrefsString = "ContusPreference";
        appSharedPrefs = context.getSharedPreferences(appPrefsString,
                Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public String getDeviceToken() {

        return appSharedPrefs.getString(DEVICE_TOKEN, "");

    }

    public void setDeviceToken(String deviceToken) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(DEVICE_TOKEN, deviceToken);
        prefsEditor.commit();

    }

    public String getCountryCode() {

        return appSharedPrefs.getString(COUNTRY_CODE, "");

    }

    public void setCountryCode(String CountryCode) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(COUNTRY_CODE, CountryCode);
        prefsEditor.commit();

    }
    public String getCountryId() {

        return appSharedPrefs.getString(COUNTRY_ID, "");

    }

    public void setCountryId(String CountryId) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(COUNTRY_ID, CountryId);
        prefsEditor.commit();

    }
    public String getAccessToken() {

        return appSharedPrefs.getString(ACCESS_TOKEN, "");

    }

    public void setAccessToken(String accessToken) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(ACCESS_TOKEN, accessToken);
        prefsEditor.commit();

    }

    public String getMobile() {

        return appSharedPrefs.getString(MOBILE, "");

    }

    public void setMobile(String mobile) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(MOBILE, mobile);
        prefsEditor.commit();

    }

    public String getMobileCode() {

        return appSharedPrefs.getString(MOBILE_CODE, "");

    }

    public void setMobileCode(String mobileCode) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(MOBILE_CODE, mobileCode);
        prefsEditor.commit();

    }

    public String getUserMobile() {

        return appSharedPrefs.getString(USER_MOBILE, "");

    }

    public void setUserMobile(String usrMobile) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(USER_MOBILE, usrMobile);
        prefsEditor.commit();

    }
    public String getPrfImg() {

        return appSharedPrefs.getString(USER_PROFILE_IMG, "");

    }

    public void setPrfImg(String prfImg) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(USER_PROFILE_IMG, prfImg);
        prefsEditor.commit();

    }

    public String getPrfStatus() {

        return appSharedPrefs.getString(USER_PROFILE_STATUS, "Hi,I am in PoaApp");

    }

    public void setPrfStatus(String prfstatus) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(USER_PROFILE_STATUS, prfstatus);
        prefsEditor.commit();

    }

    public String getNotificationUri() {
        return appSharedPrefs.getString(NOTIFICATION_URI, "android.resource://com.contalk/"
                + R.raw.contusnotification);
    }

    public void setNotificationUri(String notificationUri) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(NOTIFICATION_URI, notificationUri);
        prefsEditor.commit();
    }

    public String getPrfUName() {
        return appSharedPrefs.getString(USER_PROFILE_NAME, "");
    }

    public void setPrfUName(String puName) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(USER_PROFILE_NAME, puName);
        prefsEditor.commit();

    }

    public String getLastSyncTime() {

        return appSharedPrefs.getString(ROSTER_LAST_SYNC_TIME, "");

    }

    public void setLastSyncTime(String synTime) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(ROSTER_LAST_SYNC_TIME, synTime);
        prefsEditor.commit();

    }

    public String getMessageId() {

        return appSharedPrefs.getString(MESSAGE_ID, "");

    }

    public void setMessageId(String mid) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(MESSAGE_ID, mid);
        prefsEditor.commit();

    }

    // account username

    public String getPresentUserName() {

        return appSharedPrefs.getString(PRESENT_USER_NAME, "");

    }

    public void setPresentUserName(String name) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PRESENT_USER_NAME, name);
        prefsEditor.commit();

    }



    public int getUnseenNotifyCount() {

        return appSharedPrefs.getInt(PRESENT_NOTIFY_LASTSEEN, 0);

    }

    public void setUnseenNotifyCount(int count) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt(PRESENT_NOTIFY_LASTSEEN, count);
        prefsEditor.commit();

    }

    public int getArtistPosition() {

        return appSharedPrefs.getInt(ARTIST_POS, -1);

    }

    public void setArtistPosition(int count) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt(ARTIST_POS, count);
        prefsEditor.commit();

    }

    public int getPlayPosition() {

        return appSharedPrefs.getInt(PLAY_POS, -1);

    }

    public void setPlayPosition(int count) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt(PLAY_POS, count);
        prefsEditor.commit();

    }

    public void setIsBackground(int count) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt(IS_BACKGROUND, count);
        prefsEditor.commit();

    }

    public int getIsBackground() {

        return appSharedPrefs.getInt(IS_BACKGROUND, 0);

    }

    public String getAppVersion() {

        return appSharedPrefs.getString(APP_VERSION, "");

    }

    public void setAppVersion(String version) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(APP_VERSION, version);
        prefsEditor.commit();
    }

    /*
     * check user login to app firstime default true else false
     */
    public void setIsFirstLogin(Boolean isFirst) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(FIRST_TIME_LOGIN, isFirst);
        prefsEditor.commit();
    }

    public Boolean getIsFirstLogin() {
        return appSharedPrefs.getBoolean(FIRST_TIME_LOGIN, true);

    }


    public void setMediaPlay(Boolean play) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(MUSIC_PALY, play);
        prefsEditor.commit();
    }

    public Boolean getMediaPlay() {
        return appSharedPrefs.getBoolean(MUSIC_PALY, true);

    }

    public void setGcmNotification(Boolean play) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(GCM_NOTIFICATION, play);
        prefsEditor.commit();
    }

    public Boolean getGcmNotification() {
        return appSharedPrefs.getBoolean(GCM_NOTIFICATION, false);

    }

    public void setListScroll(Boolean isScroll) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(LST_SCROLL_HANDLING, isScroll);
        prefsEditor.commit();
    }

    public Boolean getListScroll() {

        return appSharedPrefs.getBoolean(LST_SCROLL_HANDLING, true);

    }


    public void setContactLoad(Boolean isload) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(IS_CHATVIEW_FG, isload);
        prefsEditor.commit();
    }

    public Boolean getContactLoad() {

        return appSharedPrefs.getBoolean(IS_CHATVIEW_FG, false);

    }
    public void setActivityBack(Boolean isFg) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(IS_CHATVIEW_FG, isFg);
        prefsEditor.commit();
    }

    public Boolean getActivityBack() {

        return appSharedPrefs.getBoolean(IS_CHATVIEW_FG, false);

    }

    public void setIsFirstPg(Boolean isFirstPg) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(IS_FIRST_PROGRESS, isFirstPg);
        prefsEditor.commit();
    }

    public Boolean getIsFirstPg() {

        return appSharedPrefs.getBoolean(IS_FIRST_PROGRESS, false);

    }

    public void setIsDeviceIdAdded(Boolean isAdded) {

        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(IS_DEVICE_ID_ADDED, isAdded);
        prefsEditor.commit();

    }

    public Boolean getIsDeviceIdAdded() {

        return appSharedPrefs.getBoolean(IS_DEVICE_ID_ADDED, false);

    }

    public void setIsNotify(Boolean notify) {

        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(CHAT_NOTIFY, notify);
        prefsEditor.commit();

    }

    public Boolean getIsNotify() {

        return appSharedPrefs.getBoolean(CHAT_NOTIFY, false);

    }

    public void setMultiLogin(Boolean isLoggedIn) {

        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(MULTILOGIN, isLoggedIn);
        prefsEditor.commit();

    }

    public Boolean getMultiLogin() {

        return appSharedPrefs.getBoolean(MULTILOGIN, false);

    }

    public void setIsLoggedIn(Boolean isLoggedIn) {

        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(IS_LOGGEDIN, isLoggedIn);
        prefsEditor.commit();

    }

    public Boolean getIsLoggedIn() {

        return appSharedPrefs.getBoolean(IS_LOGGEDIN, false);

    }

    // account password

    public String getPresentUserPassword() {

        return appSharedPrefs.getString(PRESENT_USER_PD, "");

    }

    public void setPresentUserPassword(String name) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PRESENT_USER_PD, name);
        prefsEditor.commit();

    }


    public String getUserid() {

        return appSharedPrefs.getString(USER_ID, "");

    }

    public void setUserId(String userId) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(USER_ID, userId);
        prefsEditor.commit();

    }

    public String getToUserName() {

        return appSharedPrefs.getString(PRESENT_TO_NAME, "");

    }

    public void setToUserName(String toUserName) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(PRESENT_TO_NAME, toUserName);
        prefsEditor.commit();

    }

    public void removePreferenceValue(String Key) {

        // initialize it
        // appSharedPrefs.edit().remove(Key).commit();
        // prefsEditor = appSharedPrefs.edit();
        // prefsEditor.remove(Key);
        // prefsEditor.commit();

        appSharedPrefs.edit().remove(Key).commit();

    }

    /**
     * This method is used to set,last searched category's name into a
     * preference.
     *
     * @param term
     */
    public void clear() {
        // SharedPreferences settings =
        // context.getSharedPreferences("ContusPreference",
        // Context.MODE_PRIVATE);
        appSharedPrefs.edit().clear().commit();
        // this.prefsEditor = appSharedPrefs.edit();
        // prefsEditor.clear();
        // prefsEditor.commit();

    }

}