/**
 * @category ContusMessanger
 * @package com.contusfly.model
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.new_chanages.models.ContactModel;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MDatabaseHelper.
 */
public class MDatabaseHelper extends SQLiteOpenHelper {

    /**
     * The Constant APP_DB_VERSION.
     */
    private static final int APP_DB_VERSION = 4;

    /**
     * The Constant TYPE_TEXT.
     */
    private static final String TEXT_NOT_NULL = " text not null ", /**
     * The Text.
     */
    TEXT = " text , ",
    /**
     * The Text primary key.
     */
    TEXT_PRIMARY_KEY = " text primary key", /**
     * The Create table string.
     */
    CREATE_TABLE_STRING = "create table if not exists ",
    /**
     * The Type text.
     */
    TYPE_TEXT = " text ";
    private static final String TABLE_CONTACT_LIST = "contact_list";
    private static final String TABLE_SELECT_GROUP_CONTACT = "selected_group_contacts";
    public static final String CONTACT_ID = "contact_id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String CONTACT_STATUS = "contact_status";
    public static final String CONTACT_PIC = "contact_pic";
    public static final String CONTACT_MPB_NAME = "contact_mpb_name";
    public static final String IS_IN_MPB_CONTACT = "is_in_mpb";


    private static final String CREATE_TABLE_CONTACT_LIST = "CREATE TABLE "
            + TABLE_CONTACT_LIST + "(" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CONTACT_NAME + " TEXT NOT NULL,  "
            + CONTACT_NUMBER + " TEXT NOT NULL,"
            + CONTACT_STATUS + " TEXT,"
            + CONTACT_PIC + " TEXT,"
            + CONTACT_MPB_NAME + " TEXT,"
            + IS_IN_MPB_CONTACT + " TEXT" +");";


    private static final String CREATE_TABLE_GROUP_CONTACT = "CREATE TABLE "
            + TABLE_SELECT_GROUP_CONTACT + "(" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CONTACT_NAME + " TEXT NOT NULL,  "
            + CONTACT_NUMBER + " TEXT NOT NULL,"
            + CONTACT_STATUS + " TEXT,"
            + CONTACT_PIC +" TEXT,"
            + CONTACT_MPB_NAME + " TEXT,"
            + IS_IN_MPB_CONTACT + " TEXT" +");";

    /**
     * The Constant CREATE_TABLE_ROSTER.
     */
    private static final String CREATE_TABLE_ROSTER = CREATE_TABLE_STRING + Constants.TABLE_ROSTER + "("
            + Constants.ROSTER_USER_ID + TEXT_PRIMARY_KEY + " , " + Constants.ROSTER_NAME + TEXT_NOT_NULL + " , "
            + Constants.ROSTER_IMAGE + TEXT + Constants.ROSTER_STATUS + TEXT + Constants.ROSTER_TYPE + TEXT
            + Constants.ROSTER_ADMINS + TEXT + Constants.ROSTER_GROUP_USERS + TEXT + Constants.ROSTER_IS_BLOCKED + TEXT
            + Constants.ROSTER_GROUP_STATUS + TEXT + Constants.ROSTER_LAST_SEEN + TEXT + Constants.ROSTER_AVAILABILITY
            + TEXT + Constants.ROSTER_CUSTOM_TONE + TYPE_TEXT + " )";


    /**
     * The Constant CREATE_TABLE_CHAT_DATA.
     */
    private static final String CREATE_TABLE_CHAT_DATA = CREATE_TABLE_STRING + Constants.TABLE_CHAT_DATA + "("
            + Constants.CHAT_COLUMN_ID + TEXT_PRIMARY_KEY + " , " + Constants.CHAT_COLUMN_FROM_USER + TEXT_NOT_NULL
            + ", " + Constants.CHAT_COLUMN_TO_USER + TEXT_NOT_NULL + ", "
            + Constants.CHAT_COLUMN_RECEIVERS + TEXT_NOT_NULL + ", " + Constants.CHAT_COLUMN_MSG + TEXT
            + Constants.CHAT_MSG_TYPE + TEXT + Constants.CHAT_TYPE + TEXT + Constants.CHAT_COLUMN_STATUS + TEXT_NOT_NULL
            + ", " + Constants.CHAT_MSG_IS_DOWNLOADED + TEXT + Constants.CHAT_TIME + TEXT + Constants.CHAT_MSG_THUMB
            + TEXT + Constants.CHAT_ENCODED_IMAGE + TEXT + Constants.CHAT_FILE_SIZE + TEXT
            + Constants.CHAT_MEDIA_LOCAL_PATH + TEXT + Constants.CHAT_MEDIA_NAME + TEXT + Constants.CHAT_TO_USER + TEXT + Constants.CHAT_MEDIA_SERVER_PATH
            + TEXT + Constants.CHAT_FROM_TO_USER + TYPE_TEXT + " )";

    /**
     * The Constant CREATE_TABLE_RECENT_CHAT_DATA.
     */
    private static final String CREATE_TABLE_RECENT_CHAT_DATA = CREATE_TABLE_STRING + Constants.TABLE_RECENT_CHAT_DATA
            + "(" + Constants.RECENT_CHAT_USER_ID + TEXT_PRIMARY_KEY + " , " + Constants.RECENT_CHAT_MSG_ID + TEXT_NOT_NULL
            + " , " + Constants.RECENT_CHAT_MSG + TEXT_NOT_NULL
            + ", " + Constants.RECENT_CHAT_TYPE + TEXT_NOT_NULL + ", " + Constants.RECENT_CHAT_STATUS + TEXT
            + Constants.RECENT_LAST_CHAT_FROM + TEXT_NOT_NULL + ", " + Constants.RECENT_CHAT_IMAGE + TEXT
            + Constants.RECENT_CHAT_UN_REED_COUNT + TEXT + Constants.RECENT_CHAT_IS_SEEN + TEXT
            + Constants.RECENT_CHAT_NAME + TEXT + Constants.RECENT_CHAT_MSG_TYPE + TEXT + Constants.RECENT_CHAT_GRP_NAME
            + TEXT + Constants.RECENT_CHAT_USERS + TEXT + Constants.RECENT_CHAT_TIME + TYPE_TEXT + " )";

    /**
     * The Constant CREATE_TABLE_PENDING_CHATS.
     */
    private static final String CREATE_TABLE_PENDING_CHATS = CREATE_TABLE_STRING + Constants.TABLE_PENDING_CHATS + "("
            + Constants.PENDING_CHAT_ID + TEXT_PRIMARY_KEY + "," + Constants.PENDING_CHAT_MSG + TEXT_NOT_NULL + ","
            + Constants.PENDING_CHAT_NAME + TEXT_NOT_NULL + "," + Constants.PENDING_CHAT_IMAGE + TEXT
            + Constants.PENDING_CHAT_TIME + TEXT + Constants.PENDING_CHAT_TYPE + TEXT_NOT_NULL + " ) ";

    /**
     * The Constant CRATE_TABLE_USER_STATUS.
     */
    private static final String CRATE_TABLE_USER_STATUS = CREATE_TABLE_STRING + Constants.TABLE_USER_STATUS + "("
            + Constants.STATUS_ID + " integer primary key autoincrement , " + Constants.STATUS_TXT + TYPE_TEXT + " )";

    /**
     * The Constant CREATE_TABLE_GROUP_MSG_STATUS.
     */
    private static final String CREATE_TABLE_GROUP_MSG_STATUS = CREATE_TABLE_STRING + Constants.TABLE_GROUP_MSG_STATUS
            + "(" + Constants.CHAT_MSG_ID + TEXT_NOT_NULL + "," + Constants.GROUP_ID + TEXT_NOT_NULL + ","
            + Constants.CHAT_USER_ID + TEXT
            + Constants.CHAT_MSG_STATUS + TEXT + Constants.CHAT_MSG_DELIVERY_TIME + TEXT + Constants.CHAT_IS_ACK + TEXT
            + Constants.CHAT_MSG_SEEN_TIME + TYPE_TEXT + " ) ";

    /**
     * The Constant CREATE_TABLE_COUNTRIES.
     */
    private static final String CREATE_TABLE_COUNTRIES = CREATE_TABLE_STRING + Constants.TABLE_COUNTRIES_LIST
            + "( _id integer primary key autoincrement, " + Constants.COUNTRY_CODE + TEXT_NOT_NULL + ","
            + Constants.COUNTRY_NAME + TEXT_NOT_NULL + "," + Constants.COUNTRY_DIAL_CODE + TEXT_NOT_NULL + " ) ";

    /**
     * The db helper.
     */
    private static SQLiteDatabase dbHelper;

    /**
     * Instantiates a new m database helper.
     *
     * @param context the context
     */
    public MDatabaseHelper(Context context) {
        super(context, context.getString(R.string.app_name) + ".db", null, APP_DB_VERSION);
        dbHelper = getWritableDatabase();
    }

    /**
     * Insert values.
     *
     * @param tableName the table name
     * @param values    the values
     */
    public static void insertValues(String tableName, ContentValues values) {
        try {
            dbHelper.insert(tableName, null, values);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Update values.
     *
     * @param tableName the table name
     * @param values    the values
     * @param where     the where
     */
    public static void updateValues(String tableName, ContentValues values, String where) {
        try {
            dbHelper.update(tableName, values, where, null);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * @param messageId
     */
    public static boolean getMessageInfo(String messageId, String userId) {
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_GROUP_MSG_STATUS, null,
                    Constants.CHAT_MSG_ID + "=? AND " + Constants.CHAT_USER_ID + "=?",
                    new String[]{messageId, userId}, null, null, null);
            if (mCursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return false;
    }

    /**
     * Check id status.
     *
     * @param id         the id
     * @param tableName  the table name
     * @param columnName the column name
     *
     * @return true, if successful
     */
    public static boolean checkIDStatus(String id, String tableName, String columnName) {
        Cursor cursor;
        cursor = dbHelper.query(tableName, null, columnName + "=?", new String[]{id}, null, null, null);
        boolean status = false;
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0)
                    status = true;
                cursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return status;
    }

    /**
     * Insert chat data.
     *
     * @param mChatMsg the m chat msg
     */
    public static void insertChatData(ChatMsg mChatMsg) {
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.CHAT_COLUMN_ID, mChatMsg.getMsgId());
            addDataInValues(values, mChatMsg);
            dbHelper.insert(Constants.TABLE_CHAT_DATA, null, values);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Update chat data.
     *
     * @param mChatMsg the m chat msg
     */
    public static void updateChatData(ChatMsg mChatMsg) {
        try {
            ContentValues values = new ContentValues();
            addDataInValues(values, mChatMsg);
            dbHelper.update(Constants.TABLE_CHAT_DATA, values, Constants.CHAT_COLUMN_ID + "=?",
                    new String[]{String.valueOf(mChatMsg.getMsgId())});
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the specific chat.
     *
     * @param chatId the chat id
     *
     * @return the specific chat
     */
    public static String getSpecificChat(String chatId) {
        String chatStatus = String.valueOf(Constants.CHAT_SENT_CLOCK);
        Cursor cursor = dbHelper.query(Constants.TABLE_CHAT_DATA, new String[]{Constants.CHAT_COLUMN_STATUS},
                Constants.CHAT_COLUMN_ID + "=?", new String[]{chatId}, null, null, null);
        if (cursor != null && cursor.getCount() > Constants.COUNT_ZERO) {
            cursor.moveToFirst();
            chatStatus = cursor.getString(cursor.getColumnIndex(Constants.CHAT_COLUMN_STATUS));
            cursor.close();
        }
        return chatStatus;
    }

    /**
     * Insert recent chat.
     *
     * @param recentChat the recent chat
     */
    public static void insertRecentChat(RecentChat recentChat) {
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.RECENT_CHAT_USER_ID, recentChat.getRecentChatId());
            addRecentChatInValues(values, recentChat);
            dbHelper.insert(Constants.TABLE_RECENT_CHAT_DATA, null, values);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Update recent chat.
     *
     * @param recentChat the recent chat
     */
    public static void updateRecentChat(RecentChat recentChat) {
        try {
            ContentValues values = new ContentValues();
            addRecentChatInValues(values, recentChat);
            dbHelper.update(Constants.TABLE_RECENT_CHAT_DATA, values, Constants.RECENT_CHAT_USER_ID + "=?",
                    new String[]{String.valueOf(recentChat.getRecentChatId())});
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Delete record.
     *
     * @param tableName    the table name
     * @param condition    the condition
     * @param conditionArg the condition arg
     */
    public static void deleteRecord(String tableName, String condition, String[] conditionArg) {
        try {
            dbHelper.delete(tableName, condition, conditionArg);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }




    public void deleteContactList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT_LIST, null, null);
    }
    /**
     * Adds the data in values.
     *
     * @param values   the values
     * @param mChatMsg the m chat msg
     */
    private static void addDataInValues(ContentValues values, ChatMsg mChatMsg) {
        values.put(Constants.CHAT_COLUMN_FROM_USER, mChatMsg.getMsgFrom());
        values.put(Constants.CHAT_COLUMN_TO_USER, mChatMsg.getMsgTo());
        values.put(Constants.CHAT_COLUMN_MSG, mChatMsg.getMsgBody());
        values.put(Constants.CHAT_TIME, mChatMsg.getMsgTime());
        values.put(Constants.CHAT_COLUMN_STATUS, mChatMsg.getMsgStatus());
        values.put(Constants.CHAT_MSG_TYPE, mChatMsg.getMsgType());
        values.put(Constants.CHAT_TYPE, mChatMsg.getMsgChatType());
        values.put(Constants.CHAT_MSG_IS_DOWNLOADED, mChatMsg.getMsgMediaIsDownloaded());
        values.put(Constants.CHAT_MSG_THUMB, mChatMsg.getMsgVideoThumb());
        values.put(Constants.CHAT_ENCODED_IMAGE, mChatMsg.getMsgMediaEncImage());
        values.put(Constants.CHAT_FILE_SIZE, mChatMsg.getMsgMediaSize());
        values.put(Constants.CHAT_MEDIA_LOCAL_PATH, mChatMsg.getMsgMediaLocalPath());
        values.put(Constants.CHAT_MEDIA_NAME, mChatMsg.getFileName());
        values.put(Constants.CHAT_FROM_TO_USER, mChatMsg.getSender());
        values.put(Constants.CHAT_COLUMN_RECEIVERS, mChatMsg.getChatReceivers());
        values.put(Constants.CHAT_MEDIA_SERVER_PATH, mChatMsg.getMsgMediaServerPath());
        values.put(Constants.CHAT_TO_USER, mChatMsg.getChatToUser());
    }

    /**
     * Adds the recent chat in values.
     *
     * @param values     the values
     * @param recentChat the recent chat
     */
    private static void addRecentChatInValues(ContentValues values, RecentChat recentChat) {
        values.put(Constants.RECENT_CHAT_IMAGE, recentChat.getRecentChatImage());
        values.put(Constants.RECENT_CHAT_IS_SEEN, recentChat.getRecentChatIsSeen());
        values.put(Constants.RECENT_CHAT_MSG_ID, recentChat.getRecentChatMsgId());
        values.put(Constants.RECENT_CHAT_MSG, recentChat.getRecentChatMsg());
        values.put(Constants.RECENT_CHAT_NAME, recentChat.getRecentChatName());
        values.put(Constants.RECENT_CHAT_STATUS, recentChat.getRecentChatStatus());
        values.put(Constants.RECENT_CHAT_TIME, recentChat.getRecentChatTime());
        values.put(Constants.RECENT_CHAT_TYPE, recentChat.getRecentChatType());
        values.put(Constants.RECENT_CHAT_UN_REED_COUNT, recentChat.getRecentChatUnread());
        values.put(Constants.RECENT_LAST_CHAT_FROM, recentChat.getRecentChatIsSender());
        values.put(Constants.RECENT_CHAT_MSG_TYPE, recentChat.getRecentChatMsgType());
        values.put(Constants.RECENT_CHAT_GRP_NAME, recentChat.getRecentChatGrpName());
        values.put(Constants.RECENT_CHAT_USERS, recentChat.getRecentChatUsers());
    }

    /**
     * Update chat status.
     *
     * @param id         the id
     * @param columnName the column name
     * @param status     the status
     */
    public static void updateChatStatus(String id, String columnName, String status) {
        try {
            ContentValues values = new ContentValues();
            values.put(columnName, status);
            dbHelper.update(Constants.TABLE_CHAT_DATA, values, Constants.CHAT_COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the roster info.
     *
     * @param rosterId   the roster id
     * @param rosterType the roster type
     *
     * @return the roster info
     */
    public static List<Rosters> getRosterInfo(String rosterId, String rosterType) {
        List<Rosters> rostersList = new ArrayList<>();
        try {
            Cursor mCursor;
            if (rosterId != null && !rosterId.isEmpty()) {
                mCursor = dbHelper.query(Constants.TABLE_ROSTER, null, Constants.ROSTER_USER_ID + "=?", new String[]{rosterId}, null, null, null);
            } else
                mCursor = dbHelper.query(Constants.TABLE_ROSTER, null, Constants.ROSTER_TYPE + "=?", new String[]{rosterType}, null, null,
                        Constants.ROSTER_AVAILABILITY + " DESC , " + Constants.ROSTER_NAME + " ASC ");
            if (mCursor != null) {
                int count = mCursor.getCount();
                mCursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    addRosterInList(mCursor, rostersList);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return rostersList;
    }

    /**
     * Gets the roster info.
     *
     * @param rosterIds  the roster id
     * @param rosterType the roster type
     *
     * @return the roster info
     */
    public static List<Rosters> getNonRosters(String rosterIds, String rosterType) {
        List<Rosters> rostersList = new ArrayList<>();
        try {
            String usersCondition = " AND " + Constants.ROSTER_USER_ID + " not in (" + rosterIds + ")";
            Cursor mCursor = dbHelper.query(Constants.TABLE_ROSTER, null, Constants.ROSTER_TYPE + "=? " + usersCondition,
                    new String[]{rosterType}, null, null, null);
            if (mCursor != null) {
                int count = mCursor.getCount();
                mCursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    addRosterInList(mCursor, rostersList);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return rostersList;
    }

    /**
     * Gets the roster info.
     *
     * @param rosterId   the roster id
     * @param rosterType the roster type
     *
     * @return the roster info
     */
    public static List<Rosters> getGroupsOfUser(String rosterId, String rosterType) {
        List<Rosters> rostersList = new ArrayList<>();
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_ROSTER, null,
                    Constants.ROSTER_TYPE + "=? AND " + Constants.ROSTER_GROUP_USERS + " LIKE ?",
                    new String[]{rosterType, "%" + rosterId + "%"}, null, null, null);
            if (mCursor != null) {
                int count = mCursor.getCount();
                mCursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    addRosterInList(mCursor, rostersList);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return rostersList;
    }

    /**
     * Gets the last msg status.
     *
     * @param fromUser the from user
     * @param toUser   the to user
     *
     * @return the last msg status
     */
    public static String getLastMsgStatus(String fromUser, String toUser) {
        String status = "";
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null,
                    Constants.CHAT_COLUMN_FROM_USER + "=? AND " + Constants.CHAT_COLUMN_TO_USER + " =?",
                    new String[]{fromUser, toUser}, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToLast();
                status = mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_COLUMN_STATUS));
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return status;
    }

    /**
     * Gets the remaining rosters.
     *
     * @param rosters the rosters
     *
     * @return the remaining rosters
     */
    public static List<Rosters> getRemainingRosters(String rosters) {
        List<Rosters> rostersList = new ArrayList<>();
        try {
            Cursor mCursor;
            mCursor = dbHelper.query(Constants.TABLE_ROSTER, null, Constants.ROSTER_TYPE + "=?",
                    new String[]{String.valueOf(Constants.COUNT_ZERO)}, null, null,
                    Constants.ROSTER_AVAILABILITY + " ASC, " + Constants.ROSTER_NAME + " ASC ");
            if (mCursor != null) {
                int count = mCursor.getCount();
                mCursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    String rosterId = mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_USER_ID));
                    String rosterAvailability = mCursor
                            .getString(mCursor.getColumnIndex(Constants.ROSTER_AVAILABILITY));
                    if (!rosters.contains(rosterId)
                            && !rosterAvailability.equalsIgnoreCase(String.valueOf(Constants.ROSTER_NOT_ACTIVE)))
                        addRosterInList(mCursor, rostersList);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return rostersList;
    }

    /**
     * Adds the roster in list.
     *
     * @param mCursor     the m cursor
     * @param rostersList the rosters list
     */
    private static void addRosterInList(Cursor mCursor, List<Rosters> rostersList) {
        Rosters item = new Rosters();
        item.setRosterID(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_USER_ID)));
        item.setRosterName(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_NAME)));
        item.setRosterImage(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_IMAGE)));
        item.setRosterStatus(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_STATUS)));
        item.setRosterAvailability(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_AVAILABILITY)));
        item.setRosterLastSeen(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_LAST_SEEN)));
        item.setRosterType(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_TYPE)));
        item.setRosterAdmin(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_ADMINS)));
        item.setRosterGroupUsers(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_GROUP_USERS)));
        item.setRosterIsBlocked(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_IS_BLOCKED)));
        item.setRosterGroupStatus(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_GROUP_STATUS)));
        rostersList.add(item);
    }

    /**
     * Gets the online contacts.
     *
     * @return the online contacts
     */
    public static List<Rosters> getOnlineContacts() {
        List<Rosters> rostersList = new ArrayList<>();
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_ROSTER, null,
                    Constants.ROSTER_TYPE + "=? AND " + Constants.ROSTER_AVAILABILITY + " !=?",
                    new String[]{String.valueOf(Constants.COUNT_ZERO), String.valueOf(Constants.ROSTER_NOT_ACTIVE)}, null,
                    null, Constants.ROSTER_NAME + " ASC ");
            if (mCursor != null) {
                int count = mCursor.getCount();
                mCursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    Rosters item = new Rosters();
                    item.setRosterID(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_USER_ID)));
                    item.setRosterName(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_NAME)));
                    item.setRosterImage(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_IMAGE)));
                    item.setRosterStatus(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_STATUS)));
                    item.setRosterAvailability(String.valueOf(Constants.COUNT_ONE));
                    item.setRosterType(mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_TYPE)));
                    rostersList.add(item);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return rostersList;
    }

    /**
     * Gets the recent chat history.
     *
     * @return the recent chat history
     */
    public static List<RecentChat> getRecentChatHistory() {
        List<RecentChat> recentChats = new ArrayList<>();
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_RECENT_CHAT_DATA, null, null, null, null, null, null);

            if (mCursor != null) {
                int count = mCursor.getCount();
                mCursor.moveToLast();
                for (int i = count - 1; i >= 0; i--) {
                    RecentChat item = new RecentChat();
                    item.setRecentChatId(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_USER_ID)));
                    item.setRecentChatImage(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_IMAGE)));
                    item.setRecentChatIsSeen(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_IS_SEEN)));
                    item.setRecentChatMsgId(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_MSG_ID)));
                    item.setRecentChatMsg(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_MSG)));
                    item.setRecentChatName(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_NAME)));
                    item.setRecentChatStatus(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_STATUS)));
                    item.setRecentChatTime(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_TIME)));
                    item.setRecentChatType(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_TYPE)));
                    item.setRecentChatUnread(
                            mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_UN_REED_COUNT)));
                    item.setRecentChatMsgType(
                            mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_MSG_TYPE)));
                    item.setRecentChatGrpName(
                            mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_GRP_NAME)));
                    item.setRecentChatIsSender(
                            mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_LAST_CHAT_FROM)));
                    item.setRecentChatUsers(mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_USERS)));
                    recentChats.add(item);
                    mCursor.moveToPrevious();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return recentChats;
    }

    /**
     * Gets the chat history.
     *
     * @param msgFrom the msg from
     * @param msgTo   the msg to
     *
     * @return the chat history
     */
    public static List<ChatMsg> getChatHistory(String msgFrom, String msgTo) {
        Cursor cursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null,
                Constants.CHAT_COLUMN_FROM_USER + "=?" + " AND " + Constants.CHAT_COLUMN_TO_USER + "=? OR ("
                        + Constants.CHAT_TYPE + "=" + Constants.CHAT_TYPE_BROADCAST + " AND "
                        + Constants.CHAT_COLUMN_RECEIVERS + " LIKE ?" + ")",
                new String[]{msgFrom, msgTo, "%" + msgTo + "%"}, null, null, null, null);
        return addChatData(cursor);
    }

    /**
     * Gets the particular chat.
     *
     * @param msgId the msg id
     *
     * @return the particular chat
     */
    public static List<ChatMsg> getParticularChat(String msgId) {
        Cursor cursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null, Constants.CHAT_COLUMN_ID + "=?",
                new String[]{msgId}, null, null, null, null);
        return addChatData(cursor);
    }

    /**
     * Gets the media info.
     *
     * @param msgFrom the msg from
     * @param msgTo   the msg to
     * @param chatId  the chat id
     *
     * @return the media info
     */
    public static List<ChatMsg> getMediaInfo(String msgFrom, String msgTo, String chatId) {
        Cursor cursor = dbHelper.query(
                Constants.TABLE_CHAT_DATA, null, Constants.CHAT_COLUMN_FROM_USER + "=?" + " AND "
                        + Constants.CHAT_COLUMN_TO_USER + "=? AND " + Constants.CHAT_COLUMN_ID + "=?",
                new String[]{msgFrom, msgTo, chatId}, null, null, null);
        return addChatData(cursor);
    }

    /**
     * Gets the pending msg.
     *
     * @return the pending msg
     */
    public static List<ChatMsg> getPendingMsg() {
        Cursor cursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null, Constants.CHAT_COLUMN_STATUS + "=?",
                new String[]{String.valueOf(Constants.CHAT_SENT_CLOCK)}, null, null, null);
        return addChatData(cursor);
    }

    /**
     * Gets the pending msg with out current message ID.
     *
     * @return the pending msg
     */
    public static List<ChatMsg> getExceptPendingMessage(String messageId) {
        Cursor cursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null, Constants.CHAT_COLUMN_STATUS + "=?" + " AND "
                        + Constants.CHAT_COLUMN_ID + "!=?",
                new String[]{String.valueOf(Constants.CHAT_SENT_CLOCK), messageId}, null, null, null);
        return addChatData(cursor);
    }

    /**
     * Get all the un sent receipt from
     */
    public static List<ChatMsg> getNotSentReceipts(int chatStatus) {
        Cursor cursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null, Constants.CHAT_COLUMN_STATUS + "=?" + " AND " + Constants.CHAT_FROM_TO_USER + "=?",
                new String[]{String.valueOf(chatStatus), String.valueOf(Constants.CHAT_FROM_RECEIVER)}, null, null, null);
        return addChatData(cursor);
    }

    /**
     * Gets the status msgs.
     *
     * @return the status msgs
     */
    public static List<String> getStatusMsgs() {
        List<String> statusMsg = new ArrayList<>();
        Cursor cursor = dbHelper.query(Constants.TABLE_USER_STATUS, null, null, null, null, null,
                Constants.STATUS_ID + " DESC ");
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0, size = cursor.getCount(); i < size; i++) {
                statusMsg.add(i, cursor.getString(cursor.getColumnIndex(Constants.STATUS_TXT)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return statusMsg;
    }

    /**
     * Gets the un seen msgs.
     *
     * @param msgFrom the msg from
     * @param msgTo   the msg to
     *
     * @return the un seen msgs
     */
    public static List<String> getUnSeenMsgs(String msgFrom, String msgTo) {
        List<String> listIds = new ArrayList<>();
        Cursor cursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null,
                Constants.CHAT_COLUMN_FROM_USER + "=?" + " AND " + Constants.CHAT_COLUMN_TO_USER + "=? AND "
                        + Constants.CHAT_FROM_TO_USER + " =? AND " + Constants.CHAT_COLUMN_STATUS + " =? ",
                new String[]{msgFrom, msgTo, String.valueOf(Constants.CHAT_FROM_RECEIVER),
                        String.valueOf(Constants.CHAT_RECEIVED)},
                null, null, null);
        if (cursor != null) {
            cursor.moveToLast();
            for (int count = cursor.getCount(), i = count - 1; i >= 0; i--) {
                listIds.add(cursor.getString(cursor.getColumnIndex(Constants.CHAT_COLUMN_ID)));
                cursor.moveToPrevious();
            }
            cursor.close();
        }
        return listIds;
    }

    /**
     * Adds the chat data.
     *
     * @param tempCursor the temp cursor
     *
     * @return the list
     */
    private static List<ChatMsg> addChatData(Cursor tempCursor) {
        List<ChatMsg> chatHistory = new ArrayList<>();
        try {
            if (tempCursor != null) {
                tempCursor.moveToFirst();
                for (int i = 0, cursorCount = tempCursor.getCount(); i < cursorCount; i++) {
                    ChatMsg chatMessage = new ChatMsg();
                    chatMessage.setMsgId(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_COLUMN_ID)));
                    chatMessage.setMsgFrom(
                            tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_COLUMN_FROM_USER)));
                    chatMessage
                            .setMsgTo(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_COLUMN_TO_USER)));
                    chatMessage.setMsgBody(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_COLUMN_MSG)));
                    chatMessage.setMsgTime(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_TIME)));
                    chatMessage.setMsgStatus(
                            tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_COLUMN_STATUS)));
                    chatMessage.setMsgType(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_MSG_TYPE)));
                    chatMessage.setMsgVideoThumb(
                            tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_MSG_THUMB)));
                    chatMessage
                            .setMsgMediaSize(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_FILE_SIZE)));
                    chatMessage.setMsgMediaIsDownloaded(
                            tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_MSG_IS_DOWNLOADED)));
                    chatMessage.setMsgMediaLocalPath(
                            tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_MEDIA_LOCAL_PATH)));
                    chatMessage.setMsgMediaServerPath(
                            tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_MEDIA_SERVER_PATH)));
                    chatMessage.setMsgMediaEncImage(
                            tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_ENCODED_IMAGE)));
                    chatMessage.setMsgChatType(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_TYPE)));
                    chatMessage.setSender(Integer
                            .parseInt(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_FROM_TO_USER))));
                    chatMessage.setChatToUser(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_TO_USER)));
                    chatMessage.setChatReceivers(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_COLUMN_RECEIVERS)));
                    chatMessage.setIsSelected(Constants.COUNT_ZERO);
                    chatMessage.setFileName(tempCursor.getString(tempCursor.getColumnIndex(Constants.CHAT_MEDIA_NAME)));
                    tempCursor.moveToNext();
                    chatHistory.add(chatMessage);
                }
                tempCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return chatHistory;
    }

    /**
     * Gets the unread msg count.
     *
     * @param recentChatID the recent chat id
     *
     * @return the unread msg count
     */
    public static String getUnreadMsgCount(String recentChatID) {
        String unreadCount = String.valueOf(Constants.COUNT_ZERO);
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_RECENT_CHAT_DATA,
                    new String[]{Constants.RECENT_CHAT_UN_REED_COUNT}, Constants.RECENT_CHAT_USER_ID + "=?",
                    new String[]{recentChatID}, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                unreadCount = mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_UN_REED_COUNT));
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return unreadCount;
    }

    /**
     * Gets the pending notifications.
     *
     * @return the pending notifications
     */
    public static List<PendingNotification> getPendingNotifications() {
        List<PendingNotification> pendingNotifications = new ArrayList<>();
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_PENDING_CHATS, null, null, null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToLast();
                for (int count = mCursor.getCount(), i = count - 1; i >= 0; i--) {
                    PendingNotification item = new PendingNotification();
                    item.setPendingChatId(mCursor.getString(mCursor.getColumnIndex(Constants.PENDING_CHAT_ID)));
                    item.setPendingChatName(mCursor.getString(mCursor.getColumnIndex(Constants.PENDING_CHAT_NAME)));
                    item.setPendingChatImg(mCursor.getString(mCursor.getColumnIndex(Constants.PENDING_CHAT_IMAGE)));
                    item.setPendingChatMsg(mCursor.getString(mCursor.getColumnIndex(Constants.PENDING_CHAT_MSG)));
                    item.setPendingChatTime(mCursor.getString(mCursor.getColumnIndex(Constants.PENDING_CHAT_TIME)));
                    item.setPendingChatType(mCursor.getString(mCursor.getColumnIndex(Constants.PENDING_CHAT_TYPE)));
                    pendingNotifications.add(item);
                    mCursor.moveToPrevious();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return pendingNotifications;
    }

    public static List<PendingNotification> getDistictValues() {
        List<PendingNotification> pendingNotifications = new ArrayList<>();
        try {
            Cursor mCursor = dbHelper.rawQuery("select DISTINCT " + Constants.PENDING_CHAT_NAME + " from " + Constants.TABLE_PENDING_CHATS, null);
            if (mCursor != null) {
                mCursor.moveToLast();
                for (int count = mCursor.getCount(), i = count - 1; i >= 0; i--) {
                    PendingNotification item = new PendingNotification();
                    item.setPendingChatName(mCursor.getString(mCursor.getColumnIndex(Constants.PENDING_CHAT_NAME)));
                    pendingNotifications.add(item);
                    mCursor.moveToPrevious();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return pendingNotifications;
    }

    /**
     * Gets the count by table.
     *
     * @param tableName the table name
     *
     * @return the count by table
     */
    public static int getCountByTable(String tableName) {
        Cursor mCursor = dbHelper.query(tableName, null, null, null, null, null, null);
        int count = 0;
        if (mCursor != null) {
            count = mCursor.getCount();
            mCursor.close();
        }
        return count;
    }

    public static List<GroupMsgStatus> getGroupUnSeenMessages(String status, String groupId) {
        List<GroupMsgStatus> statusList = new ArrayList<>();
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_GROUP_MSG_STATUS, null,
                    Constants.CHAT_MSG_STATUS + " =? AND " + Constants.GROUP_ID + "=?",
                    new String[]{String.valueOf(status), groupId}, null, null, null);
            int count;
            if (mCursor != null) {
                count = mCursor.getCount();
                mCursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    GroupMsgStatus msgStatus = new GroupMsgStatus();
                    msgStatus.setMsgId(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_ID)));
                    msgStatus.setMsgSender(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_USER_ID)));
                    msgStatus.setMsgStatus(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_STATUS)));
                    msgStatus.setMsgDeliveryTime(
                            mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_DELIVERY_TIME)));
                    msgStatus.setMsgSeenTime(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_SEEN_TIME)));
                    statusList.add(msgStatus);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }

        } catch (Exception e) {
            LogMessage.e(e);
        }
        return statusList;
    }

    /**
     * Gets the group msg details.
     *
     * @param msgId  the msg id
     * @param status the status
     *
     * @return the group msg details
     */
    public static List<GroupMsgStatus> getGroupMsgDetails(String msgId, int status, String strReceivers) {
        List<GroupMsgStatus> statusList = new ArrayList<>();
        try {
            String usersCondition = " AND " + Constants.CHAT_USER_ID + " in (" + strReceivers + ")";
            Cursor mCursor = dbHelper.query(Constants.TABLE_GROUP_MSG_STATUS, null,
                    Constants.CHAT_MSG_ID + "=? AND " + Constants.CHAT_MSG_STATUS + " =?",
                    new String[]{msgId, String.valueOf(status)}, null, null, null);

            /*Cursor mCursor = dbHelper.query(Constants.TABLE_GROUP_MSG_STATUS, null,
                    Constants.CHAT_MSG_ID + "=?",
                    new String[]{msgId}, null, null, null);*/
            int count;
            if (mCursor != null) {
                count = mCursor.getCount();
                mCursor.moveToFirst();
                for (int i = 0; i < count; i++) {
                    GroupMsgStatus msgStatus = new GroupMsgStatus();
                    msgStatus.setMsgId(msgId);
                    msgStatus.setMsgSender(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_USER_ID)));
                    msgStatus.setMsgStatus(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_STATUS)));
                    msgStatus.setMsgDeliveryTime(
                            mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_DELIVERY_TIME)));
                    msgStatus.setMsgSeenTime(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_SEEN_TIME)));
                    statusList.add(msgStatus);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return statusList;
    }

    /**
     * Gets the group msg status.
     *
     * @param msgId        the msg id
     * @param isSeenStatus the is seen status
     *
     * @return the group msg status
     */
    public static int getGroupMsgStatus(String msgId, boolean isSeenStatus, String strReceivers) {
        int count = 0;
        try {
            String usersCondition = " AND " + Constants.CHAT_USER_ID + " in (" + strReceivers + ")";
            Cursor mCursor = dbHelper.query(Constants.TABLE_GROUP_MSG_STATUS, null, Constants.CHAT_MSG_ID + "=? "
                            + usersCondition,
                    new String[]{msgId}, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0, length = mCursor.getCount(); i < length; i++) {
                    count = checkMsgStatus(mCursor, isSeenStatus, count);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return count;
    }

    /**
     * Check msg status.
     *
     * @param mCursor      the m cursor
     * @param isSeenStatus the is seen status
     * @param rowCount     the row count
     *
     * @return the int
     */
    public static int checkMsgStatus(Cursor mCursor, boolean isSeenStatus, int rowCount) {
        int count = rowCount;
        String status = mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_STATUS));
        if (status != null && !status.isEmpty()) {
            int msgStatus = Integer.parseInt(status);
            if (isSeenStatus) {
                if (msgStatus == Constants.CHAT_SEEN_BY_RECEIVER)
                    count++;
            } else {
                if (msgStatus >= Constants.CHAT_REACHED_TO_RECEIVER)
                    count++;
            }
        }
        return count;
    }

    /**
     * Gets the roster custom tone.
     *
     * @param rosterId the roster id
     *
     * @return the roster custom tone
     */
    public static String getRosterCustomTone(String rosterId) {
        String toneUri = Constants.EMPTY_STRING;
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_ROSTER, null, Constants.ROSTER_USER_ID + "=? ",
                    new String[]{rosterId}, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                toneUri = mCursor.getString(mCursor.getColumnIndex(Constants.ROSTER_CUSTOM_TONE));
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return toneUri;
    }

    /**
     * Gets the user media.
     *
     * @param fromUser the from user
     * @param toUser   the to user
     *
     * @return the user media
     */
    public static List<MediaMetaData> getUserMedia(String fromUser, String toUser) {
        List<MediaMetaData> mediaList = new ArrayList<>();
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null,
                    "(" + Constants.CHAT_COLUMN_FROM_USER + "=? AND " + Constants.CHAT_COLUMN_TO_USER + " =?) AND ("
                            + Constants.CHAT_MSG_IS_DOWNLOADED + " =? OR " + Constants.CHAT_MSG_IS_DOWNLOADED + " =?)",
                    new String[]{fromUser, toUser, String.valueOf(Constants.MEDIA_DOWNLADED),
                            String.valueOf(Constants.MEDIA_UPLOADED)},
                    null, null, null);
            if (mCursor != null) {
                mCursor.moveToLast();
                for (int i = 0, cursorCount = mCursor.getCount(); i < cursorCount; i++) {
                    MediaMetaData item = new MediaMetaData();
                    String msgType = mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_TYPE));
                    if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_AUDIO)
                            || msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)
                            || msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE)) {
                        item.setMediaPath(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MEDIA_LOCAL_PATH)));
                        item.setMediaThumb(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_ENCODED_IMAGE)));
                        item.setMediaType(msgType);
                        item.setMessageId(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_ID)));
                        mediaList.add(item);
                    }
                    mCursor.moveToPrevious();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return mediaList;
    }

    /**
     * Gets the user media.
     *
     * @param messageId the message id
     *
     * @return the user media
     */
    public static MediaMetaData getMessageMedia(String messageId) {
        MediaMetaData item = new MediaMetaData();
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null,
                    Constants.CHAT_MSG_ID + "=?",
                    new String[]{messageId},
                    null, null, null);
            if (mCursor != null && mCursor.moveToFirst()) {
                String msgType = mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_TYPE));
                if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_AUDIO)
                        || msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)
                        || msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE)) {
                    item.setMediaPath(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MEDIA_LOCAL_PATH)));
                    item.setMediaThumb(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_ENCODED_IMAGE)));
                    item.setMediaType(msgType);
                    item.setMessageId(mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_ID)));
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return item;
    }

    /**
     * Gets the grp last msg status.
     *
     * @param fromUser the from user
     * @param toUser   the to user
     *
     * @return the grp last msg status
     */
    public static String getGrpLastMsgStatus(String fromUser, String toUser) {
        String status = "";
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null,
                    Constants.CHAT_COLUMN_FROM_USER + "=? AND " + Constants.CHAT_COLUMN_TO_USER + " =? ",
                    new String[]{fromUser, toUser}, null, null, null);
            if (mCursor != null) {
                mCursor.moveToLast();
                String msgID = mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_COLUMN_ID));
                String strReceivers = mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_COLUMN_RECEIVERS));
                status = returnGrpStatus(msgID, strReceivers);
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return status;
    }

    /**
     * Return grp status.
     *
     * @param msgId the msg id
     *
     * @return the string
     */
    private static String returnGrpStatus(String msgId, String strReceivers) {
        int seenCount = MDatabaseHelper.getGroupMsgStatus(msgId, true, strReceivers);
        int totalUsers = TextUtils.split(strReceivers, ",").length;
        if (seenCount == totalUsers)
            return String.valueOf(Constants.CHAT_SEEN_BY_RECEIVER);
        else {
            int deliveryCount = MDatabaseHelper.getGroupMsgStatus(msgId, false, strReceivers);
            if (deliveryCount == totalUsers)
                return String.valueOf(Constants.CHAT_REACHED_TO_RECEIVER);
            else
                return MDatabaseHelper.getGrpMsgStatus(msgId);
        }
    }

    /**
     * Gets the media count.
     *
     * @param fromUser the from user
     * @param toUser   the to user
     *
     * @return the media count
     */
    public static int getMediaCount(String fromUser, String toUser) {
        int count = 0;
        try {
            String messageType = Constants.CHAT_MSG_TYPE + " IN ('video','audio','image') AND ";
            Cursor mCursor = dbHelper.query(Constants.TABLE_CHAT_DATA, null, messageType +
                            "(" + Constants.CHAT_COLUMN_FROM_USER + "=? AND " + Constants.CHAT_COLUMN_TO_USER + " =?) AND ("
                            + Constants.CHAT_MSG_IS_DOWNLOADED + " =? OR " + Constants.CHAT_MSG_IS_DOWNLOADED + " =?)",
                    new String[]{fromUser, toUser, String.valueOf(Constants.MEDIA_UPLOADED),
                            String.valueOf(Constants.MEDIA_DOWNLADED)},
                    null, null, null);
            if (mCursor != null) {
                count = mCursor.getCount();
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return count;
    }

    /**
     * Gets the unread chats count.
     *
     * @return the unread chats count
     */
    public static int getUnreadChatsCount() {
        int count = 0;
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_RECENT_CHAT_DATA, null, null, null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0, length = mCursor.getCount(); i < length; i++) {
                    String unreadMsg = mCursor.getString(mCursor.getColumnIndex(Constants.RECENT_CHAT_UN_REED_COUNT));
                    if (!unreadMsg.isEmpty() && !unreadMsg.equalsIgnoreCase("0"))
                        count++;
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return count;
    }

    /**
     * Gets the grp msg status.
     *
     * @param msgId the msg id
     *
     * @return the grp msg status
     */
    public static String getGrpMsgStatus(String msgId) {
        String status = Constants.EMPTY_STRING;
        try {
            Cursor mCursor = dbHelper.query(Constants.TABLE_GROUP_MSG_STATUS,
                    new String[]{Constants.CHAT_MSG_STATUS},
                    Constants.CHAT_MSG_ID + "=? AND " + Constants.CHAT_IS_ACK + "=?", new String[]{msgId, String.valueOf(Constants.CHAT_REACHED_TO_SERVER)}, null,
                    null, null);
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                status = mCursor.getString(mCursor.getColumnIndex(Constants.CHAT_MSG_STATUS));
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return status;
    }

    /**
     * Gets the countries.
     *
     * @param countryCode the country code
     *
     * @return the countries
     */
    public static List<Countries> getCountries(String countryCode) {
        List<Countries> countries = new ArrayList<>();
        try {
            Cursor mCursor;
            if (countryCode != null && !countryCode.isEmpty())
                mCursor = dbHelper.query(Constants.TABLE_COUNTRIES_LIST, null, Constants.COUNTRY_CODE + "=?",
                        new String[]{countryCode}, null, null, null, null);
            else
                mCursor = dbHelper.query(Constants.TABLE_COUNTRIES_LIST, null, null, null, null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0, size = mCursor.getCount(); i < size; i++) {
                    Countries item = new Countries();
                    item.setCountryNo(mCursor.getString(mCursor.getColumnIndex(Constants.COUNTRY_DIAL_CODE)));
                    item.setCountryName(mCursor.getString(mCursor.getColumnIndex(Constants.COUNTRY_NAME)));
                    item.setCountryCode(mCursor.getString(mCursor.getColumnIndex(Constants.COUNTRY_CODE)));
                    countries.add(item);
                    mCursor.moveToNext();
                }
                mCursor.close();
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return countries;
    }

    /**
     * Dummy mtd.
     */
    public void dummyMtd() {
        // Dummy Method
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_ROSTER);
            db.execSQL(CREATE_TABLE_CHAT_DATA);
            db.execSQL(CREATE_TABLE_RECENT_CHAT_DATA);
            db.execSQL(CREATE_TABLE_PENDING_CHATS);
            db.execSQL(CRATE_TABLE_USER_STATUS);
            db.execSQL(CREATE_TABLE_GROUP_MSG_STATUS);
            db.execSQL(CREATE_TABLE_COUNTRIES);
            db.execSQL(CREATE_TABLE_CONTACT_LIST);
            db.execSQL(CREATE_TABLE_GROUP_CONTACT);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }



    private static final String SQL_DELETE_TABLE_ROASTER =
            "DROP TABLE IF EXISTS " + Constants.TABLE_ROSTER;

    private static final String SQL_DELETE_TABLE__CHAT_DATA =
            "DROP TABLE IF EXISTS " + Constants.TABLE_CHAT_DATA;

    private static final String SQL_DELETE_TABLE_RECENT_CHAT_DATA =
            "DROP TABLE IF EXISTS " + Constants.TABLE_RECENT_CHAT_DATA;

    private static final String SQL_DELETE_TABLE_PENDING_CHATS =
            "DROP TABLE IF EXISTS " + Constants.TABLE_PENDING_CHATS;

    private static final String SQL_DELETE_TABLE_USER_STATUS =
            "DROP TABLE IF EXISTS " + Constants.TABLE_USER_STATUS;

    private static final String SQL_DELETE_TABLE_GROUP_MSG_STATUS =
            "DROP TABLE IF EXISTS " + Constants.TABLE_GROUP_MSG_STATUS;

    private static final String SQL_DELETE_TABLE_COUNTRIES =
            "DROP TABLE IF EXISTS " + Constants.TABLE_COUNTRIES_LIST;

    private static final String SQL_DELETE_TABLE_CONTACT_LIST =
            "DROP TABLE IF EXISTS " + TABLE_CONTACT_LIST;

    private static final String SQL_DELETE_TABLE_SELECTED_CONTACT_LIST =
            "DROP TABLE IF EXISTS " + TABLE_SELECT_GROUP_CONTACT;
    /*
     * (non-Javadoc)
     *
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.
     * sqlite .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade Method will be used when db get Updates
        db.execSQL(SQL_DELETE_TABLE_ROASTER);
        db.execSQL(SQL_DELETE_TABLE__CHAT_DATA);
        db.execSQL(SQL_DELETE_TABLE_RECENT_CHAT_DATA);
        db.execSQL(SQL_DELETE_TABLE_PENDING_CHATS);
        db.execSQL(SQL_DELETE_TABLE_USER_STATUS);
        db.execSQL(SQL_DELETE_TABLE_GROUP_MSG_STATUS);
        db.execSQL(SQL_DELETE_TABLE_COUNTRIES);
        db.execSQL(SQL_DELETE_TABLE_CONTACT_LIST);
        db.execSQL(SQL_DELETE_TABLE_SELECTED_CONTACT_LIST);

        db.execSQL(CREATE_TABLE_ROSTER);
        db.execSQL(CREATE_TABLE_CHAT_DATA);
        db.execSQL(CREATE_TABLE_RECENT_CHAT_DATA);
        db.execSQL(CREATE_TABLE_PENDING_CHATS);
        db.execSQL(CRATE_TABLE_USER_STATUS);
        db.execSQL(CREATE_TABLE_GROUP_MSG_STATUS);
        db.execSQL(CREATE_TABLE_COUNTRIES);
        db.execSQL(CREATE_TABLE_CONTACT_LIST);
        db.execSQL(CREATE_TABLE_GROUP_CONTACT);
    }

    public ArrayList<ContactModel> getAllContactsList() {
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel contact = new ContactModel();
                contact.setContactName(cursor.getString(1));
                contact.setContactNumber(cursor.getString(2));
                contact.setContactSelected(cursor.getString(3));
                contact.setContactPic(cursor.getString(4));
                contact.setContactMPBName(cursor.getString(5));
                contact.setIsMPBContact(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public ArrayList<ContactModel> getAllSelectedContacts() {
        ArrayList<ContactModel> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SELECT_GROUP_CONTACT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel contact = new ContactModel();
                contact.setContactName(cursor.getString(1));
                contact.setContactNumber(cursor.getString(2));
                contact.setContactSelected(cursor.getString(3));
                contact.setContactPic(cursor.getString(4));
                contact.setContactMPBName(cursor.getString(5));
                contact.setIsMPBContact(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public int getAllContactsListSize() {
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SELECT_GROUP_CONTACT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel contact = new ContactModel();
                contact.setContactName(cursor.getString(1));
                contact.setContactNumber(cursor.getString(2));
                contact.setContactSelected(cursor.getString(3));
                contact.setContactPic(cursor.getString(4));
                contact.setContactMPBName(cursor.getString(5));
                contact.setIsMPBContact(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList.size();
    }

    public int getAllSeletedContactSize() {
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SELECT_GROUP_CONTACT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel contact = new ContactModel();
                contact.setContactName(cursor.getString(1));
                contact.setContactNumber(cursor.getString(2));
                contact.setContactSelected(cursor.getString(3));
                contact.setContactPic(cursor.getString(4));
                contact.setContactMPBName(cursor.getString(5));
                contact.setIsMPBContact(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList.size();
    }

    public void addContactToList(String contactName, String contactNumber, String status, String pic, String mpbName, String isMPBContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contactName); // Contact Name
        values.put(CONTACT_NUMBER, contactNumber);
        values.put(CONTACT_STATUS, status);// Contact Phone
        values.put(CONTACT_PIC, pic);
        values.put(CONTACT_MPB_NAME, mpbName);
        values.put(IS_IN_MPB_CONTACT, isMPBContact);
        //Inserting Row
        db.insert(TABLE_CONTACT_LIST, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addContactToSelected(String contactName, String contactNumber, String status, String pic, String mpbName, String isMPBContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contactName); // Contact Name
        values.put(CONTACT_NUMBER, contactNumber);
        values.put(CONTACT_STATUS, status);// Contact Phone
        values.put(CONTACT_PIC, pic);
        values.put(CONTACT_MPB_NAME, mpbName);
        values.put(IS_IN_MPB_CONTACT, isMPBContact);
        //Inserting Row
        db.insert(TABLE_SELECT_GROUP_CONTACT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void updateContactList(String contact, String pic, String mpbName, String isMPBContact) {

        ContentValues contentValues = new ContentValues();
       /* contentValues.put(CONTACT_NAME, name); // Contact Name
        contentValues.put(CONTACT_NUMBER, contact); // Contact Phone
        contentValues.put(CONTACT_STATUS, status); */// Contact Phone
        contentValues.put(CONTACT_PIC, pic);
        contentValues.put(CONTACT_MPB_NAME, mpbName);
        contentValues.put(IS_IN_MPB_CONTACT, isMPBContact);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CONTACT_LIST, contentValues,
                CONTACT_NUMBER + "='" + contact + "'", null);
        /*
        db.update(TABLE_CONTACT_LIST, contentValues,
                CONTACT_NUMBER + "=" + contact, null);*/
    }

    public void deleteContactFromList(String contactNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT_LIST, CONTACT_NUMBER + " = ?",
                new String[]{contactNumber});
        db.close();
    }

    public void deleteContactFromSelected(String contactNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELECT_GROUP_CONTACT, CONTACT_NUMBER + " = ?",
                new String[]{contactNumber});
        db.close();
    }


    public void deleteSelectedContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELECT_GROUP_CONTACT, null, null);
    }


}
