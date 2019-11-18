package com.contusfly.chooseContactsDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.new_chanages.models.ContactModel;
import com.new_chanages.models.GroupsNameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/2/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * The Constant TAG.
     */
    public static final String TAG = "DBHelper";

    /**
     * The Constant DATABASE_NAME.
     */
    private static final String DATABASE_NAME = "MSVE 2017";

    /**
     * The Constant DATABASE_VERSION.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The Constant DATABASE_VERSION.
     */
    private static final String TABLE_CATEGORY_CONTACT = "selected_contacts";
    private static final String TABLE_GROUP_LIST = "group_list";
    private static final String TABLE_CONTACT_LIST = "contact_list";
    private static final String TABLE_SELECT_GROUP_CONTACT = "selected_group_contacts";
    /**
     * The Constant DATABASE_VERSION.
     */
    private static final String TABLE_CATEGORY_GROUP = "selected_groups";
    private static final String TABLE_IMAGES = "image_cache";

    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String CONTACT_ROASTER_ID = "roaster_id";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String GROUP_ROASTER_ID = "roaster_id";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String CONTACT_ROASTER_NAME = "roaster_name";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String GROUP_ROASTER_NAME = "roaster_name";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String CONTACT_IS_ACTIVE = "is_active";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String GROUP_IS_ACTIVE = "is_active";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String IS_FIELD_ACTIVE = "is_field_active";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String GROUP_IS_FIELD_ACTIVE = "group_is_field_active";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String IS_DONE = "is_done";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String ID = "id";
    /**
     * The Constant CATEGORY_AUTOINCREMENT_ID.
     */
    public static final String TYPE = "type";
    /**
     * The Constant SQL_CREATE_TABLE_CATEGORY.
     */

    public static final String CONTACT_ID = "contact_id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String CONTACT_STATUS = "contact_status";
    public static final String CONTACT_PIC = "contact_pic";
    public static final String CONTACT_MPB_NAME = "contact_mpb_name";



    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_STATUS = "group_status";




    private static final String SQL_TABLE_CONTACT_LIST = "CREATE TABLE "
            + TABLE_CONTACT_LIST + "(" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CONTACT_NAME + " TEXT NOT NULL,  "
            + CONTACT_NUMBER + " TEXT NOT NULL,"
            + CONTACT_STATUS + "TEXT,"
            + CONTACT_PIC + "TEXT,"
            + CONTACT_MPB_NAME + "TEXT" +");";


    private static final String SQL_TABLE_GROUP_CONTACT = "CREATE TABLE "
            + TABLE_SELECT_GROUP_CONTACT + "(" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CONTACT_NAME + " TEXT NOT NULL,  "
            + CONTACT_NUMBER + " TEXT NOT NULL,"
            + CONTACT_STATUS + " TEXT,"
            + CONTACT_PIC +" TEXT,"
            + CONTACT_MPB_NAME+" TEXT "+");";


    private static final String SQL_TABLE_GROUPS = "CREATE TABLE "
            + TABLE_GROUP_LIST + "(" +  GROUP_NAME
            + " TEXT NOT NULL,  " + GROUP_ID
            + " TEXT NOT NULL," + GROUP_STATUS
            +" TEXT"+ ");";

    private static final String SQL_TABLE_CONTACT = "CREATE TABLE "
            + TABLE_CATEGORY_CONTACT + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTACT_ROASTER_ID
            + " TEXT NOT NULL,  " + CONTACT_ROASTER_NAME
            + " TEXT NOT NULL," + TYPE
            + " TEXT," + IS_FIELD_ACTIVE
            + " INTEGER ," + CONTACT_IS_ACTIVE
            + " INTEGER NOT NULL," + IS_DONE
            + " INTEGER " + ");";
    /**
     * The Constant SQL_CREATE_TABLE_CATEGORY.
     */
    private static final String SQL_TABLE_GROUP = "CREATE TABLE "
            + TABLE_CATEGORY_GROUP + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GROUP_ROASTER_ID
            + " TEXT NOT NULL,  " + GROUP_ROASTER_NAME
            + " TEXT NOT NULL," + TYPE
            + " TEXT NOT NULL," + GROUP_IS_FIELD_ACTIVE
            + " INTEGER ," + GROUP_IS_ACTIVE
            + " INTEGER NOT NULL " + ");";

    //    private static final String SQL_CREATE_PROFILE_PIC = "CREATE TABLE "
//            + TABLE_CATEGORY_GROUP + "(" + ID
//            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "PROFILE_PIC_BYTE_ARRAY"
//            + " TEXT NOT NULL);";
    private static final String SQL_TABLE_IMAGE = "CREATE TABLE " + TABLE_IMAGES + "(" +
            "image_name" + " TEXT UNIQUE," +
            " image" + " BLOB);";

    private static final String SQL_DELETE_TABLE_CONTACTS =
            "DROP TABLE IF EXISTS " + TABLE_CONTACT_LIST;
    private static final String SQL_DELETE_TABLE_CONTACT_LIST =
            "DROP TABLE IF EXISTS " + TABLE_SELECT_GROUP_CONTACT;
    private static final String SQL_DELETE_TABLE_GROUPS =
            "DROP TABLE IF EXISTS " + TABLE_GROUP_LIST;
    private static final String SQL_DELETE_TABLE_GROUP =
            "DROP TABLE IF EXISTS " + TABLE_CATEGORY_CONTACT;
    private static final String SQL_DELETE_TABLE_IMAGE =
            "DROP TABLE IF EXISTS " + TABLE_CATEGORY_GROUP;
    private static final String SQL_DELETE_TABLE_GROUP_CONTACTS =
            "DROP TABLE IF EXISTS " + TABLE_IMAGES;




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TABLE_CONTACT);
        sqLiteDatabase.execSQL(SQL_TABLE_CONTACT_LIST);
        sqLiteDatabase.execSQL(SQL_TABLE_GROUPS);
        sqLiteDatabase.execSQL(SQL_TABLE_GROUP);
        sqLiteDatabase.execSQL(SQL_TABLE_IMAGE);
        sqLiteDatabase.execSQL(SQL_TABLE_GROUP_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_CONTACTS);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_CONTACT_LIST);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_GROUPS);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_GROUP);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_IMAGE);
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_GROUP_CONTACTS);


        sqLiteDatabase.execSQL(SQL_TABLE_CONTACT);
        sqLiteDatabase.execSQL(SQL_TABLE_CONTACT_LIST);
        sqLiteDatabase.execSQL(SQL_TABLE_GROUPS);
        sqLiteDatabase.execSQL(SQL_TABLE_GROUP);
        sqLiteDatabase.execSQL(SQL_TABLE_IMAGE);
        sqLiteDatabase.execSQL(SQL_TABLE_GROUP_CONTACT);
    }


    public void insertImageCache(String imageName, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image_name", imageName);
        values.put("image", imageByteArray);
        db.insertWithOnConflict("image_cache", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteImageCache(String imageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("image_cache", "image_name" + " = ?",
                new String[]{String.valueOf(imageName)});
        db.close();
    }

    public Bitmap getImageCache(String imageUrl) {
        Bitmap imageBitmap = null;
        String[] columns = new String[]{"image_name", "image"};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("image_cache", columns, "image_name = ? ", new String[]{imageUrl}, null, null, null);
//      Cursor cursor= db.rawQuery("select * from image_cache where image_name="+imageUrl,null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            byte[] imageByte = cursor.getBlob(cursor.getColumnIndex("image"));
            if(imageByte!=null)
                try{
                    imageBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

        }
        cursor.close();
        return imageBitmap;
    }

    public void addContact(String rosterID, String rosterName, String type, int is_fieled_active, int isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACT_ROASTER_ID, rosterID); // Contact Name
        values.put(CONTACT_ROASTER_NAME, rosterName);
        values.put(TYPE, type);// Contact Phone
        values.put(IS_FIELD_ACTIVE, is_fieled_active);
        values.put(CONTACT_IS_ACTIVE, isActive);
        // Inserting Row
        db.insert(TABLE_CATEGORY_CONTACT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addContactToList(String contactName, String contactNumber, String status, String pic, String mpbName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contactName); // Contact Name
        values.put(CONTACT_NUMBER, contactNumber);
        values.put(CONTACT_STATUS, status);// Contact Phone
        values.put(CONTACT_PIC, pic);
        values.put(CONTACT_MPB_NAME, mpbName);
        //Inserting Row
        db.insert(TABLE_CONTACT_LIST, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public void addContactToGroup(String contactName, String contactNumber, String status, String pic, String mpbName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contactName); // Contact Name
        values.put(CONTACT_NUMBER, contactNumber);
        values.put(CONTACT_STATUS, status);// Contact Phone
        values.put(CONTACT_PIC, pic);
        values.put(CONTACT_MPB_NAME, mpbName);
        //Inserting Row
        db.insert(TABLE_SELECT_GROUP_CONTACT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addGroupsToList(String groupName, String groupId, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, groupName); // Contact Name
        values.put(GROUP_ID, groupId);
        values.put(GROUP_STATUS, status);// Contact Phone
        // Inserting Row
        db.insert(TABLE_GROUP_LIST, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // Deleting single contact
    public void deleteContact(String rosterID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY_CONTACT, CONTACT_ROASTER_ID + " = ?",
                new String[]{String.valueOf(rosterID)});
        db.close();
    }

    public void deleteContactFromList(String contactNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT_LIST, CONTACT_NUMBER + " = ?",
                new String[]{contactNumber});
        db.close();
    }
    public void deleteContactFromGroup(String contactNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELECT_GROUP_CONTACT, CONTACT_NUMBER + " = ?",
                new String[]{contactNumber});
        db.close();
    }
    public void deleteGroupFromList(String groupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUP_LIST, GROUP_ID + " = ?",
                new String[]{groupId});
        db.close();
    }

    // Deleting single contact
    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY_CONTACT, null, null);
    }


    /**
     * Update.
     */
    public void update(String roassterId, String name, String type, int is_fieled_active, int value) {
        Log.e("roassterId", roassterId + "");
        Log.e("is_fieled_active", is_fieled_active + "");
        Log.e("value", value + "");
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_ROASTER_ID, roassterId); // Contact Name
        contentValues.put(CONTACT_ROASTER_NAME, name); // Contact Phone
        contentValues.put(TYPE, type); // Contact Phone
        contentValues.put(IS_FIELD_ACTIVE, is_fieled_active);
        contentValues.put(CONTACT_IS_ACTIVE, value);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CATEGORY_CONTACT, contentValues,
                CONTACT_ROASTER_ID + "=" + roassterId, null);
    }

    // code to get all contacts in a list view
    public List<ChooseContactsModelClass> getAllContacts() {
        List<ChooseContactsModelClass> contactList = new ArrayList<ChooseContactsModelClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_CONTACT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChooseContactsModelClass contact = new ChooseContactsModelClass();
                contact.setId(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setIsFiledActive(cursor.getInt(3));
                contact.setIsActive(cursor.getInt(4));
                contact.setIsDone(cursor.getInt(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
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
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public ArrayList<ContactModel> getAllGroupContactsList() {
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
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void updateContactList(String contact, String pic, String mpbName) {

        ContentValues contentValues = new ContentValues();
       /* contentValues.put(CONTACT_NAME, name); // Contact Name
        contentValues.put(CONTACT_NUMBER, contact); // Contact Phone
        contentValues.put(CONTACT_STATUS, status); */// Contact Phone
        contentValues.put(CONTACT_PIC, pic);
        contentValues.put(CONTACT_MPB_NAME, mpbName);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CONTACT_LIST, contentValues,
                CONTACT_NUMBER + "='" + contact + "'", null);
        /*
        db.update(TABLE_CONTACT_LIST, contentValues,
                CONTACT_NUMBER + "=" + contact, null);*/
    }

    public ArrayList<GroupsNameObject> getAllGroupList() {
        ArrayList<GroupsNameObject> contactList = new ArrayList<GroupsNameObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GroupsNameObject contact = new GroupsNameObject();
                contact.setGroupName(cursor.getString(0));
                contact.setGroupId(Integer.valueOf(cursor.getString(1)));
                contact.setGroupStatus(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int GetGroupListSize() {
        List<GroupsNameObject> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GroupsNameObject contact = new GroupsNameObject();
                contact.setGroupName(cursor.getString(0));
                contact.setGroupId(Integer.valueOf(cursor.getString(1)));
                contact.setGroupStatus(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList.size();
    }

    // code to get all contacts in a list view
    public List<ChooseContactsModelClass> getAllRemoveContactsDetails(int i, int s) {
        List<ChooseContactsModelClass> contactList = new ArrayList<ChooseContactsModelClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_CONTACT + " WHERE " + IS_FIELD_ACTIVE + "=" + i + " AND " + CONTACT_IS_ACTIVE + "=" + s;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChooseContactsModelClass contact = new ChooseContactsModelClass();
                contact.setId(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setIsFiledActive(cursor.getInt(3));
                contact.setIsActive(cursor.getInt(4));
                contact.setIsDone(cursor.getInt(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    // code to get all contacts in a list view
    public List<ChooseContactsModelClass> getAllRemoveGroupDetails(int i, int s) {
        List<ChooseContactsModelClass> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_GROUP + " WHERE " + GROUP_IS_FIELD_ACTIVE + "=" + i + " AND " + GROUP_IS_ACTIVE + "=" + s;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChooseContactsModelClass contact = new ChooseContactsModelClass();
                contact.setId(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setIsFiledActive(cursor.getInt(3));
                contact.setIsActive(cursor.getInt(4));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    // code to get all contacts in a list view
    public List<ChooseContactsModelClass> getAllContactsDetails(int s) {
        List<ChooseContactsModelClass> contactList = new ArrayList<ChooseContactsModelClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_CONTACT + " WHERE " + CONTACT_IS_ACTIVE + "=" + s;
        Log.e("test dfdsf", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChooseContactsModelClass contact = new ChooseContactsModelClass();
                contact.setId(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setIsFiledActive(cursor.getInt(3));
                contact.setIsActive(cursor.getInt(4));
                contact.setIsDone(cursor.getInt(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to get all contacts in a list view
    public List<ChooseContactsModelClass> getAllGroupDetails(int s) {
        List<ChooseContactsModelClass> contactList = new ArrayList<ChooseContactsModelClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_GROUP + " WHERE " + GROUP_IS_ACTIVE + "=" + s;
        Log.e("test dfdsf         ", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChooseContactsModelClass contact = new ChooseContactsModelClass();
                contact.setId(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setIsFiledActive(cursor.getInt(3));
                contact.setIsActive(cursor.getInt(4));
                contact.setIsDone(cursor.getInt(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    /**
     * Check event.
     *
     * @param productId the product id
     * @return true, if successful
     */
    public boolean checkEvent(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY_CONTACT,
                new String[]{CONTACT_ROASTER_ID}, CONTACT_ROASTER_ID + "="
                        + productId, null, null, null, null);

        if (cursor.moveToFirst())

            return true;
        else
            return false;

    }

    public void addGroup(String rosterID, String rosterName, String type, int isActive, int valuw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_ROASTER_ID, rosterID); // Contact Name
        values.put(GROUP_ROASTER_NAME, rosterName);
        values.put(TYPE, type);// Contact Phone
        values.put(GROUP_IS_FIELD_ACTIVE, isActive);
        values.put(GROUP_IS_ACTIVE, valuw);
        // Inserting Row
        db.insert(TABLE_CATEGORY_GROUP, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // Deleting single contact
    public void deleteGroup(String rosterID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY_GROUP, GROUP_ROASTER_ID + " = ?",
                new String[]{String.valueOf(rosterID)});
        db.close();
    }

    // Deleting single contact
    public void deleteGroupTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY_GROUP, null, null);
    }

    public void deletContactList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT_LIST, null, null);
    }


    public void deleteGroupList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUP_LIST, null, null);
    }


    /**
     * Update.
     */
    public void updateGroup(String roassterId, String name, String type, int value, int active) {
        Log.e("1", value + "");
        Log.e("roassterId", roassterId + "");
        ContentValues contentValues = new ContentValues();
        contentValues.put(GROUP_ROASTER_ID, roassterId); // Contact Name
        contentValues.put(GROUP_ROASTER_NAME, name); // Contact Phone
        contentValues.put(TYPE, type); // Contact Phone
        contentValues.put(GROUP_IS_FIELD_ACTIVE, value);
        contentValues.put(GROUP_IS_ACTIVE, active);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CATEGORY_GROUP, contentValues,
                CONTACT_ROASTER_ID + "=" + roassterId, null);
    }

    // code to get all contacts in a list view
    public List<ChooseContactsModelClass> getAllGroup() {
        List<ChooseContactsModelClass> contactList = new ArrayList<ChooseContactsModelClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY_GROUP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChooseContactsModelClass contact = new ChooseContactsModelClass();
                contact.setId(cursor.getString(1));
                contact.setName(cursor.getString(2));
                contact.setIsFiledActive(cursor.getInt(3));
                contact.setIsActive(cursor.getInt(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    /**
     * Check event.
     *
     * @param productId the product id
     * @return true, if successful
     */
    public boolean checkEventGroup(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY_GROUP,
                new String[]{GROUP_ROASTER_ID}, GROUP_ROASTER_ID + "="
                        + productId, null, null, null, null);

        if (cursor.moveToFirst())

            return true;
        else
            return false;

    }

}
