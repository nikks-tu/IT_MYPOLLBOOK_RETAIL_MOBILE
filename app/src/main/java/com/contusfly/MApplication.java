/**
 * @category ContusMessanger
 * @package com.contusfly
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.CustomRequest;
import com.contus.category.CategoryAdapter;
import com.contus.chatlib.utils.ContusConstantValues;
import com.contus.myprofile.EditProfile;
import com.contus.responsemodel.CategoryPollResponseModel;
import com.contus.restclient.UserPollRestClient;
import com.contus.views.CustomToastLogger;
import com.contusfly.chooseContactsDb.ChooseContactsModelClass;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.service.ChatService;
import com.contusfly.utils.Constants;
import com.contusfly.utils.InternalStorageContentProvider;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.ProgressWheel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.new_chanages.models.GetsharedURL_InputModel;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.contusfly.utils.PathUtils.isGooglePhotosUri;

/**
 * The Class MApplication.
 */
public class MApplication extends Application  {
    //property id
    private static final String PROPERTY_ID = "UA-65359288-1";
    //photo secret id
    private static final String CREATIVE_SDK_SAMPLE_CLIENT_SECRET = "dd2ca0bf-7db5-42a2-95e6-f6d142b07d01";
    //client id
    private static final String CREATIVE_SDK_SAMPLE_CLIENT_ID = "a726103a2d0a4a3dbdb32843c1d8b13d";
    //Dialog
    public static Dialog pDialog = null;
    //progress wheel
    private static ProgressWheel wheel;
    //custom dialog box
    private static Dialog customDialogBox;
    //choose image options
    private static String[] chooseImageOptions;
    //adapter
    private static ArrayAdapter dataStateAdapter;
    //dialog
    private static Dialog mListDialog;
    //select
    private static TextView select;
    //File uri
    private static Uri fileUri;
    //Category adapter
    private static CategoryAdapter categoryAdapter;
    //adrquest
    private static AdRequest adRequest;
    //bas id
    private static String base64Id;
    //Google analytics tracker
    static HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    //choose flag false
    public static boolean chooseFlag = false;
    //optcode delimiter
    private static String code;
    private static Bitmap myBitmap;
    private static Date convertedDate;
    /**
     * The resources.
     */
    private Resources resources;

    /**
     * The m preferences.
     */
    private SharedPreferences mPreferences;

    /**
     * The m editor.
     */
    private static SharedPreferences.Editor mEditor;

    /**
     * The Constant ROTATE_270_D.
     */
    private static final int THUMB_64 = 64, WIDTH_500 = 500, WIDTH_1000 = 1000,
            WIDTH_2000 = 2000, THUMB_200 = 200, WIDTH_4000 = 400,
            WIDTH_200 = 200, THUMB_120 = 120, THUMB_250 = 250, THUMB_300 = 300,
            THUMB_400 = 400, THUMB_450 = 450, THUMB_100 = 100, THUMB_150 = 150,
            TYPE_STREAM = 1, TYPE_BITMAP = 2, ROTATE_90_D = 90,
            ROTATE_180_D = 180, ROTATE_270_D = 270;

    /**
     * The thb size.
     */
    private int bitmapWidth, thbSize;

    /**
     * The project number.
     */
    private String projectNumber = "900934157555";

    /**
     * You can get instance by using Activity.getApplication()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        mPreferences = getSharedPreferences(getString(R.string.app_name),
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.apply();
        MDatabaseHelper databaseHelper = new MDatabaseHelper(this);
        databaseHelper.dummyMtd();
        insertCountries();
        if (getBooleanFromPreference(Constants.IS_LOGGED_IN)) {
            startService(new Intent(this, ChatService.class)
                    .setAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_ROSTER));
        }
        insertDefaultStatus();
        if (getStringFromPreference(Constants.USER_STATUS) == null)
            storeStringInPreference(Constants.USER_STATUS,
                    getString(R.string.default_status));
    }

    /**
     * Insert countries.
     */
    public void insertCountries() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (MDatabaseHelper
                            .getCountByTable(Constants.TABLE_COUNTRIES_LIST) == 0) {
                        InputStream input = getAssets().open("countries.txt");
                        int size = input.available();
                        byte[] buffer = new byte[size];
                        int value = input.read(buffer);
                        if (value > 0) LogMessage.v("mApp::", "" + value);
                        input.close();
                        String text = new String(buffer);
                        insertData(text);
                    }
                } catch (Exception ex) {
                    LogMessage.e(Constants.TAG, ex + "");
                }
                return null;
            }
        }.execute();
    }

    private void insertData(String text) {
        try {
            JSONArray object = new JSONArray(text);
            for (int i = 0, length = object.length(); i < length; i++) {
                JSONObject jsonObject = object.getJSONObject(i);
                ContentValues mValues = new ContentValues();
                mValues.put(Constants.COUNTRY_CODE,
                        jsonObject.getString("code"));
                mValues.put(Constants.COUNTRY_NAME,
                        jsonObject.getString("name"));
                mValues.put(Constants.COUNTRY_DIAL_CODE,
                        jsonObject.getString("dial_code"));
                MDatabaseHelper.insertValues(
                        Constants.TABLE_COUNTRIES_LIST, mValues);
            }
        } catch (Exception ex) {
            LogMessage.e(Constants.TAG, ex + "");
        }
    }

    /**
     * Insert default status.
     */
    private void insertDefaultStatus() {
        if (MDatabaseHelper.getCountByTable(Constants.TABLE_USER_STATUS) == 0) {
            String[] defaultStatus = resources
                    .getStringArray(R.array.default_status_values);
            for (String status : defaultStatus) {
                ContentValues mValues = new ContentValues();
                mValues.put(Constants.STATUS_TXT, status);
                MDatabaseHelper.insertValues(Constants.TABLE_USER_STATUS,
                        mValues);
            }
        }
    }

    /**
     * Show location alert.
     *
     * @param context the context
     */
    public void showLocationAlert(final Context context) {
        try {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            mBuilder.setMessage(getString(R.string.text_location_disabled));
            mBuilder.setPositiveButton(getString(R.string.text_enable),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent myIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(myIntent);
                        }
                    });
            mBuilder.setNegativeButton(getString(R.string.text_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = mBuilder.create();
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Start login service.
     *
     * @param context the context
     */
    public void startLoginService(Context context) {
        try {
            String userName = returnEmptyStringIfNull(getStringFromPreference(Constants.USERNAME));
            String password = returnEmptyStringIfNull(getStringFromPreference(Constants.SECRET_KEY));
            if (isNetConnected(this) && !userName.isEmpty() && !password.isEmpty())
                startService(new Intent(context, ChatService.class).putExtra("username", userName)
                        .putExtra("password", password)
                        .setAction(ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_REQUEST));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Store boolean in preference.
     *
     * @param key   the key
     * @param value the value
     */
    public static void storeBooleanInPreference(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * Gets the boolean from preference.
     *
     * @param key the key
     * @return the boolean from preference
     */
    public boolean getBooleanFromPreference(String key) {
        return mPreferences.getBoolean(key, false);
    }

    /**
     * Store string in preference.
     *
     * @param key   the key
     * @param value the value
     */
    public static void storeStringInPreference(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * Gets the string from preference.
     *
     * @param key the key
     * @return the string from preference
     */
    public String getStringFromPreference(String key) {
        return mPreferences.getString(key, Constants.NULL_VALUE);
    }

    /**
     * Clear all preference.
     */
    public static void clearAllPreference() {
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * Checks if is net connected.
     *
     * @param context the context
     * @return true, if is net connected
     */
    public boolean isNetConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    /**
     * Return empty string if null.
     *
     * @param stringValue the string value
     * @return the string
     */
    public static String returnEmptyStringIfNull(String stringValue) {
        if (stringValue != Constants.NULL_VALUE) {
            if ((!TextUtils.isEmpty(Constants.NULL_STRING))
                    && (!Constants.NULL_STRING.equalsIgnoreCase(stringValue))) {
                return stringValue.trim();
            } else
                return Constants.EMPTY_STRING;
        } else
            return Constants.EMPTY_STRING;
    }

    /**
     * Decode strem.
     *
     * @param fil           the fil
     * @param selectedImage the selected image
     * @param mContext      the m context
     * @return the bitmap
     */
    public Bitmap decodeStrem(File fil, Uri selectedImage, Context mContext) {

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver()
                    .openInputStream(selectedImage));
            final int thumbSize = getThbSize(bitmap, TYPE_STREAM);
            bitmap = Bitmap.createScaledBitmap(bitmap, thumbSize, thumbSize,
                    false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, THUMB_100, baos);
            bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos
                    .toByteArray()));
            bitmap = rotateImage(bitmap, fil.getAbsolutePath());
            return bitmap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Gets the thb size.
     *
     * @param bitmap the bitmap
     * @param type   the type
     * @return the thb size
     */
    public int getThbSize(Bitmap bitmap, int type) {
        thbSize = THUMB_64;
        bitmapWidth = bitmap.getWidth();
        if (type == TYPE_BITMAP) {
            thbSize = THUMB_120;
            thbSize = resizeBitMap(thbSize);
        } else if (type == TYPE_STREAM) {
            thbSize = THUMB_64;
            thbSize = resizeImage();
        }
        return thbSize;
    }

    /**
     * Resize image.
     *
     * @return the int
     */
    private int resizeImage() {
        try {
            if (bitmapWidth < WIDTH_200)
                thbSize = THUMB_64;
            else if (bitmapWidth < WIDTH_500)
                thbSize = THUMB_100;
            else if (bitmapWidth < WIDTH_1000)
                thbSize = THUMB_150;
            else if (bitmapWidth < WIDTH_2000)
                thbSize = THUMB_250;
            else if (bitmapWidth < WIDTH_4000)
                thbSize = THUMB_300;
            else if (bitmapWidth > WIDTH_4000)
                thbSize = THUMB_450;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return thbSize;
    }

    /**
     * Resize bit map.
     *
     * @param thbSize the thb size
     * @return the int
     */
    private int resizeBitMap(int thbSize) {
        int tempSize = thbSize;
        try {
            if (bitmapWidth < WIDTH_200)
                tempSize = THUMB_64;
            else if (bitmapWidth < WIDTH_500)
                tempSize = THUMB_200;
            else if (bitmapWidth < WIDTH_1000)
                tempSize = THUMB_250;
            else if (bitmapWidth < WIDTH_2000)
                tempSize = THUMB_300;
            else if (bitmapWidth < WIDTH_4000)
                tempSize = THUMB_400;
            else if (bitmapWidth > WIDTH_4000)
                tempSize = THUMB_450;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempSize;
    }

    /**
     * Decode file.
     *
     * @param f        the f
     * @param sampling the sampling
     * @return the bitmap
     */
    public Bitmap decodeFile(File f, int sampling) {
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(
                    new FileInputStream(f.getAbsolutePath()), null, o2);
            o2.inSampleSize = sampling;
            o2.inTempStorage = new byte[48 * 1024];
            o2.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(
                    new FileInputStream(f.getAbsolutePath()), null, o2);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, THUMB_100, baos);
            bitmap = rotateImage(bitmap, f.getAbsolutePath());
            return bitmap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rotate image.
     *
     * @param bmp      the bmp
     * @param imageUrl the image url
     * @return the bitmap
     */
    public Bitmap rotateImage(Bitmap bmp, String imageUrl) {
        if (bmp != null) {
            ExifInterface ei;
            int orientation = 0;
            Bitmap resizedBitmap;
            try {
                ei = new ExifInterface(imageUrl);
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int bmpWidth = bmp.getWidth();
            int bmpHeight = bmp.getHeight();
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(ROTATE_90_D);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(ROTATE_180_D);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(ROTATE_270_D);
                    break;
                default:
                    break;
            }

            resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
                    matrix, true);
            return resizedBitmap;
        } else {
            return bmp;
        }
    }

    /**
     * Take photo from camera.
     *
     * @param context     the context
     * @param path        the path
     * @param isCropImage the is crop image
     * @return the string
     */
    public String takePhotoFromCamera(Activity context, String path,
                                      boolean isCropImage) {
        String userProfileImg = null;
        try {
            Calendar todayDate = Calendar.getInstance();
            String dateToday = todayDate.get(Calendar.DAY_OF_MONTH) + "-"
                    + ((todayDate.get(Calendar.MONTH)) + 1) + "-"
                    + todayDate.get(Calendar.YEAR) + " "
                    + todayDate.get(Calendar.HOUR) + "-"
                    + todayDate.get(Calendar.MINUTE) + "-"
                    + todayDate.get(Calendar.SECOND);
            userProfileImg = String.format(path + "/%s.jpg", dateToday + "");
            Intent intent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File photo = new File(userProfileImg);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("exit", "false");
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (isCropImage) {
                String state = Environment.getExternalStorageState();
                File file;
                Uri fileUri;
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            Constants.TEMP_PHOTO_FILE_NAME);
                    fileUri = Uri.fromFile(file);
                } else
                    fileUri = InternalStorageContentProvider.CONTENT_URI;
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                        fileUri);
            } else
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            context.startActivityForResult(intent, Constants.TAKE_PHOTO);
            context.overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userProfileImg;
    }

    /**
     * Choose file from gallery.
     *
     * @param context  the context
     * @param mimeType the mime type
     */
    public void chooseFileFromGallery(Activity context, String mimeType) {
        try {
            Intent intent;
            if (mimeType.startsWith(Constants.MSG_TYPE_IMAGE)) {
                intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType(mimeType);
            } else if (mimeType.startsWith(Constants.MIME_TYPE_ALL)) {
                intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType(mimeType);
            } else if (mimeType.startsWith(Constants.MSG_TYPE_VIDEO)) {
                intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType(mimeType);
            } else
                intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            context.startActivityForResult(intent, Constants.FROM_GALLERY);
            context.overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the real path from uri.
     *
     * @param contentUri the content uri
     * @return the real path from uri
     */
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(contentUri, proj, null,
                    null, null);
            if (cursor == null)
                return null;
            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(columnIndex);
            cursor.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Start video recording.
     *
     * @param context the context
     * @return the file
     */
    public File startVideoRecording(Activity context) {
        File videoFile = null;
        try {
            String videoPath;
            Calendar todayDate = Calendar.getInstance();
            String dateToday = todayDate.get(Calendar.DAY_OF_MONTH) + "-" + ((todayDate.get(Calendar.MONTH)) + 1) + "-"
                    + todayDate.get(Calendar.YEAR) + " " + todayDate.get(Calendar.HOUR) + "-"
                    + todayDate.get(Calendar.MINUTE) + "-" + todayDate.get(Calendar.SECOND);
            videoPath = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name)
                    + "/" + getString(R.string.text_video) + "/" + Constants.MSG_TYPE_SENT_PATH + "/";
            File folder = new File(videoPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            videoFile = new File(videoPath, dateToday + ".mp4");
            Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intentVideo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            intentVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intentVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 90);
            context.startActivityForResult(intentVideo, Constants.TAKE_VIDEO);
            return videoFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoFile;
    }


    /**
     * This method is used to hide the bottom bar
     *
     * @param activity
     */
    public static void hideSystemUI(Activity activity) {
        //Return the window flags that have been explicitly set by the client, so will not be modified by getDecorView().
        View mDecorView = activity.getWindow().getDecorView();
        //Request that the visibility of the status bar or other screen/window decorations be changed.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * This is method is used to start the next activity
     *
     * @param action
     * @param mActivity
     */
    public static void startActivity(String action, Activity mActivity) {
        //An intent is an abstract description of an operation to be performed.
        // It can be used with startActivity to launch an Activity
        Intent startActivity = new Intent(action);
        /* Starting the activity **/
        mActivity.startActivity(startActivity);

    }

    /**
     * THis method is used the the bottom fixed buttom when the keypad opens
     *
     * @param mPersonalInfo
     * @param rootView
     * @param txtNext
     * @param txtView
     */
    public static void hideButtonKeypadOpens(Activity mPersonalInfo, final RelativeLayout rootView, final TextView txtNext, final View txtView) {
//A view tree observer is used to register listeners that can be notified of global changes in the view tree. Such global events include,
// but are not limited to, layout of the whole tree, beginning of the drawing pass, touch mode change
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Rect holds four integer coordinates for a rectangle.
                Rect r = new Rect();
                //Retrieve the overall visible display size in which the window this
                // view is attached to has been positioned in.
                rootView.getWindowVisibleDisplayFrame(r);
                //Calculating the height diffrence
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
                // if more than 100 pixels, its probably a keyboard...
                //ok now we know the keyboard is up...
                if (heightDiff > 300) {
                    txtNext.setVisibility(View.GONE);
                    txtView.setVisibility(View.GONE);
                } else {
                    //ok now we know the keyboard is down...
                    txtNext.setVisibility(View.VISIBLE);
                    txtView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * THis method is used the the bottom fixed buttom when the keypad opens
     *
     * @param mPersonalInfo
     * @param rootView
     * @param txtNext
     * @param txtView
     */
    public static void hideButtonEmojiconsOpens(Activity mPersonalInfo, final RelativeLayout rootView, final TextView txtNext, final View txtView) {
//A view tree observer is used to register listeners that can be notified of global changes in the view tree. Such global events include,
// but are not limited to, layout of the whole tree, beginning of the drawing pass, touch mode change
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Rect holds four integer coordinates for a rectangle.
                Rect r = new Rect();
                //Retrieve the overall visible display size in which the window this
                // view is attached to has been positioned in.
                rootView.getWindowVisibleDisplayFrame(r);
                //Calculating the height diffrence
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
                // if more than 100 pixels, its probably a keyboard...
                //ok now we know the keyboard is up...
                if (heightDiff > 300) {
                    txtNext.setVisibility(View.GONE);
                    txtView.setVisibility(View.GONE);
                } else {
                    //ok now we know the keyboard is down...
                    txtNext.setVisibility(View.VISIBLE);
                    txtView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * THis method is used the the bottom fixed buttom when the keypad opens
     *
     * @param mProfileEdit
     * @param mainView
     * @param txtDone
     * @param txtView
     */
    public static void hideButtonKeypadOpens(final EditProfile mProfileEdit, final RelativeLayout mainView, final TextView txtDone, final View txtView, final TextView txtCancel, final View view5) {
        //A view tree observer is used to register listeners that can be notified of global changes in the view tree. Such global events include,
// but are not limited to, layout of the whole tree, beginning of the drawing pass, touch mode change
        mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Rect holds four integer coordinates for a rectangle.
                Rect r = new Rect();
                //Retrieve the overall visible display size in which the window this
                // view is attached to has been positioned in.
                mainView.getWindowVisibleDisplayFrame(r);
                //Calculating the height diffrence
                int heightDiff = mainView.getRootView().getHeight() - (r.bottom - r.top);
                //Calculating the height diffrence
                if (heightDiff > 300) {
                    // if more than 100 pixels, its probably a keyboard...
                    //ok now we know the keyboard is up...
                    txtDone.setVisibility(View.GONE);
                    txtView.setVisibility(View.GONE);
                    txtCancel.setVisibility(View.GONE);
                    view5.setVisibility(View.GONE);

                } else {
                    //ok now we know the keyboard is down...
                    txtDone.setVisibility(View.VISIBLE);
                    txtView.setVisibility(View.VISIBLE);
                    txtCancel.setVisibility(View.VISIBLE);
                    view5.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * This method is used to set the status bar color
     *
     * @param activity activity
     * @param color    color
     */
    public static void settingStatusBarColor(Activity activity, int color) {
        //Retrieve the current Window for the activity.
        Window window = activity.getWindow();
        //If the condition matches
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Flag indicating that this Window is responsible for drawing the background for the system bars.
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //Window flag: request a translucent status bar with minimal system-provided background protection.
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //Sets the color of the status bar to color.
            window.setStatusBarColor(color);
        }
    }

    /**
     * This method is used to se the boolean in shared prefernce
     *
     * @param context context
     * @param key     key
     * @param value   value
     */
    public static void setBoolean(Context context, String key, boolean value) {
        /** Shared prefernce **/
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        /** Storing the boolean value in prefernce **/
        sp.edit().putBoolean(key, value).commit();

    }

    /**
     * This method is used to get the boolean in shared prefernce
     *
     * @param context context
     * @param key     key
     */
    public static boolean getBoolean(Context context, String key) {
        /** Shared prefernce **/
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        /** Returs the boolean value **/
        return sp.getBoolean(key, false);

    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    public static String getVerificationCode(String message) {
        code = message.substring(29);
        return code;
    }

    /**
     * This method is used to se the string in shared prefernce
     *
     * @param context context
     * @param key     key
     * @param value   value
     */
    public static String setString(Context context, String key, String value) {
        /** Shared preference **/
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        /** Storing the key value **/
        sp.edit().putString(key, value).commit();

        return key;
    }

    /**
     * This method is used to get the string in shared prefernce
     *
     * @param context context
     * @param key     key
     */
    public static String getString(Context context, String key) {
        try {
            /** Shared preference **/
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(context);
            /** Returns the value from string **/
            return sp.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Bitmap setPic(String mCurrentPhotoPath) {

        String encodedString = null;
        Bitmap scaledBitmap = null;
        try {
            String filePath = mCurrentPhotoPath;



            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath,options);



            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16*1024];

            try{
                bmp = BitmapFactory.decodeFile(filePath,options);
            }
            catch(OutOfMemoryError exception){
                exception.printStackTrace();

            }
            try{
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            }
            catch(OutOfMemoryError exception){
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float)options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);



            } catch (Exception e) {
                // TODO: handle exception
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return scaledBitmap;
    }


    public static void Imagecompression(File filepath)
    {
        //compression logic

        Bitmap bt = MApplication.setPic(filepath.getAbsolutePath());

        try {

            filepath.createNewFile();
            Bitmap bitmap = bt;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (Exception ae)
        {
            ae.printStackTrace();
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


   /* //decodes image and scales it to reduce memory consumption
    public static Uri decodeFileConvertUri(Activity activity, File f) {
        try {

            Bitmap bt =setPic(f);

            return Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bt, String.valueOf(Calendar.getInstance().getTimeInMillis()), "description"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    //decodes image and scales it to reduce memory consumption
    public static Uri decodeFileConvertUri(Activity activity, File f) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bt = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bt.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bt, String.valueOf(Calendar.getInstance().getTimeInMillis()), "description");
            return Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bt, String.valueOf(Calendar.getInstance().getTimeInMillis()), "description"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void gmailFeedback(Activity activity) {
        Resources resources = activity.getResources();
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it
        // doesn't hurt to try in case they fix it
        emailIntent.setType("message/rfc822");
        PackageManager pm = activity.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{MApplication.getString(activity, com.contus.app.Constants.EMAIL)});
        sendIntent.setType("text/plain");
        Intent openInChooser = Intent.createChooser(emailIntent,
                "Send Email");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(
                sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a
            // LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{MApplication.getString(activity, com.contus.app.Constants.EMAIL)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.app_name));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "your_text");
            } else if (packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName,
                        ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.app_name));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{MApplication.getString(activity, com.contus.app.Constants.EMAIL)});
                if (packageName.contains("android.gm")) {
                    intent.setType("message/rfc822");
                }
                intentList.add(new LabeledIntent(intent, packageName,
                        ri.loadLabel(pm), ri.icon));
            }
        }
        // convert intentList to array
        LabeledIntent[] extraIntents = intentList
                .toArray(new LabeledIntent[intentList.size()]);
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                extraIntents);
        activity.startActivity(openInChooser);

    }

    public static void gmail(Activity activity) {
        Resources resources = activity.getResources();
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it
        // doesn't hurt to try in case they fix it
        emailIntent.setType("message/rfc822");
        PackageManager pm = activity.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{MApplication.getString(activity, com.contus.app.Constants.EMAIL)});
        sendIntent.setType("text/plain");
        Intent openInChooser = Intent.createChooser(emailIntent,
                "Send Email");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(
                sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a
            // LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{MApplication.getString(activity, com.contus.app.Constants.EMAIL)});
                emailIntent.putExtra(Intent.EXTRA_TEXT, "your_text");
            } else if (packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName,
                        ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{MApplication.getString(activity, com.contus.app.Constants.EMAIL)});
                if (packageName.contains("android.gm")) {
                    intent.setType("message/rfc822");
                }
                intentList.add(new LabeledIntent(intent, packageName,
                        ri.loadLabel(pm), ri.icon));
            }
        }
        // convert intentList to array
        LabeledIntent[] extraIntents = intentList
                .toArray(new LabeledIntent[intentList.size()]);
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                extraIntents);
        activity.startActivity(openInChooser);

    }


    public static void shareGmail(Activity activity, String shareUrl, String string) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, string + " has shared a poll via MyPollBook");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Open the following URL to participate the poll: " + shareUrl);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));


        /*Resources resources = activity.getResources();
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it
        // doesn't hurt to try in case they fix it
        emailIntent.setType("message/rfc822");
        PackageManager pm = activity.getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/html");
        Intent openInChooser = Intent.createChooser(emailIntent,
                "Send Email");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(
                sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a
            // LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, string + "shared a poll on thinQPoll");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Open the following URL to participate the poll: " + shareUrl);

            } else if (packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName,
                        ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, string + " shared a poll on thinQPoll");
                intent.putExtra(Intent.EXTRA_TEXT, "Open the following URL to participate the poll: " + shareUrl);
                if (packageName.contains("android.gm")) {
                    intent.setType("message/rfc822");
                }
                intentList.add(new LabeledIntent(intent, packageName,
                        ri.loadLabel(pm), ri.icon));
            }
        }
        // convert intentList to array
        LabeledIntent[] extraIntents = intentList
                .toArray(new LabeledIntent[intentList.size()]);
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                extraIntents);
        activity.startActivity(openInChooser);*/

    }


    public static void getSharedURL(String url, final Activity context) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("share_url",url);

            Log.v("...", obj.toString());
        }
        catch (Exception ae)
        {

        }


        UserPollRestClient.getInstance().getShareURL(new GetsharedURL_InputModel(url), new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response1) {

                String result = CustomRequest.ResponcetoString(response1);
                try {
                    JSONObject response = new JSONObject(result);

                    int success=response.optInt("success");
                    String results=response.optString("results");

                    {

                        if(success==1)
                        {
                            MApplication.shareGmail(context, results, MApplication.getString(context, com.contus.app.Constants.PROFILE_USER_NAME));

                        }
                        else
                        {

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }

    /**
     * This method is used to convert the strin ginto base 564
     *
     * @param campaignId
     * @return
     */
    public static String convertByteCode(String campaignId) {
        try {
            //byte array
            byte[] data = campaignId.getBytes("UTF-8");
            //Base64-encode the given data and return a newly allocated String with the result.
            base64Id = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return base64Id;
    }

    /**
     * This method is used the save the string in prefernce
     *
     * @param context
     * @param str
     * @param name
     * @param pollOptionsSize
     * @return
     */
    public static boolean saveArray(Context context, ArrayList<Integer> str, String name, String pollOptionsSize) {
        //Interface for accessing and modifying preference data returned by getSharedPreferences(String, int).
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //nterface used for modifying values in a SharedPreferences object.
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt(pollOptionsSize, str.size()); /* sKey is an array */
        for (int i = 0; i < str.size(); i++) {
            //remove the array
            mEdit1.remove(name + i);
            mEdit1.putInt(name + i, str.get(i));
        }
        return mEdit1.commit();
    }


    /**
     * This method is used to load the integer array from the prefernce
     *
     * @param mContext
     * @param array
     * @param name
     * @param pollOptionsSize
     * @return
     */

    public static ArrayList<String> loadStringArray(Context mContext, ArrayList<String> array, String name, String pollOptionsSize) {
        //Interface for accessing and modifying preference data returned by getSharedPreferences(String, int).
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);
        //clearing the array
        array.clear();
        int size = mSharedPreference1.getInt(pollOptionsSize, 0);
        for (int i = 0; i < size; i++) {
            //adding into the array based on the size
            array.add(mSharedPreference1.getString(name + i, null));
        }
        return array;
    }

    /**
     * This method is used the save the string in prefernce
     *
     * @param context
     * @param str
     * @param name
     * @param pollOptionsSize
     * @return
     */
    public static boolean saveStringArray(Context context, ArrayList<String> str, String name, String pollOptionsSize) {
        //Interface for accessing and modifying preference data returned by getSharedPreferences(String, int).
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //nterface used for modifying values in a SharedPreferences object.
        SharedPreferences.Editor mEdit1 = sp.edit();
        //Getting the int
        mEdit1.putInt(pollOptionsSize, str.size()); /* sKey is an array */
        for (int i = 0; i < str.size(); i++) {
            //remove
            mEdit1.remove(name + i);
            //putting the string
            mEdit1.putString(name + i, str.get(i));

        }

        return mEdit1.commit();
    }

    /**
     * This method is used the save the string in prefernce
     *
     * @param context
     * @param str
     * @param name
     * @param pollOptionsSize
     * @return
     */
    public static boolean saveStringCustomModelArray(Context context, List<ChooseContactsModelClass> str, String name, String pollOptionsSize) {
        //Interface for accessing and modifying preference data returned by getSharedPreferences(String, int).
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //nterface used for modifying values in a SharedPreferences object.
        SharedPreferences.Editor mEdit1 = sp.edit();
        //Getting the int
        mEdit1.putInt(pollOptionsSize, str.size()); /* sKey is an array */
        for (int i = 0; i < str.size(); i++) {
            //remove
            mEdit1.remove(name + i);
            //putting the string
            mEdit1.putString(name + i, String.valueOf(str.get(i)));

        }

        return mEdit1.commit();
    }

    /**
     * This method is used to load the integer array from the prefernce
     *
     * @param mContext
     * @param array
     * @param name
     * @param pollOptionsSize
     * @return
     */
    public static ArrayList<Integer> loadArray(Context mContext, ArrayList<Integer> array, String name, String pollOptionsSize) {
        //Interface for accessing and modifying preference data returned by getSharedPreferences(String, int).
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);
        //clearing the array
        array.clear();
        //Getting the int
        int size = mSharedPreference1.getInt(pollOptionsSize, 0);
        for (int i = 0; i < size; i++) {
            //adding into the array based on the size
            array.add(mSharedPreference1.getInt(name + i, 0));
        }
        return array;
    }


    /**
     * Used to Check the Network Connectivity.
     *
     * @param activity the activity
     * @return true; If network available.
     */
    public static boolean isNetConnected(Activity activity) {
        /** Connnectivity mannager **/
        ConnectivityManager conMgr = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        /** Checking the network info if available returns true otherwise false **/
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }

    /**
     * Materialdesign dialog start.
     *
     * @param mActivity the m activity
     */
    public static void materialdesignDialogStart(final Activity mActivity) {
        //  Progress wheel class is called to display custom progress bar in
        //   material design
        wheel = new ProgressWheel(mActivity);
        /* Setting the circle radius **/
        wheel.setCircleRadius(100);
        /* Setting the bar width **/
        wheel.setBarWidth(8);
        /* Bar color **/
        wheel.setBarColor(mActivity.getResources().getColor(R.color.gplus_color_1));
        wheel.spin();
        /* Progress dialog **/
        pDialog = new Dialog(mActivity);
        pDialog.setCanceledOnTouchOutside(false);
        //Enable extended window features.
        pDialog.requestWindowFeature(mActivity.getWindow().FEATURE_NO_TITLE);
        //This method was deprecated in API level 16. use setBackground(Drawable) instead
        pDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        pDialog.setCancelable(false);
        /* Setting the progress wheel view in content view **/
        pDialog.setContentView(wheel);
        /* Progress dialog will display **/
        try{
            pDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Materialdesign dialog stop.
     */
    public static void materialdesignDialogStop() {
        if (wheel != null) {
            /** Stop spinning the wheel **/
            wheel.stopSpinning();
            /** Progress dialog dismiss **/
            pDialog.dismiss();
        }

    }

    /**
     * getting the path of the file
     *
     * @param
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(Activity activity, Uri uri)
            throws URISyntaxException {
        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(activity, uri)) {
            // DownloadsProvider

            if(isExternalStorageDocument(uri))
            {
                return getExternalStorageContent(uri);
            }
            else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                // return DownloadsProvider
                return getDataColumn(activity, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                // MediaProvider
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(activity, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            try {
                InputStream is = activity.getContentResolver().openInputStream(uri);
                if (is != null) {
                    Bitmap pictureBitmap = BitmapFactory.decodeStream(is);
                    //You can use this bitmap according to your purpose or Set bitmap to imageview
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//------------------------------------------------------------------------------
            // Return the remote address
            try{
                if (isGooglePhotosUri(uri))
                {
                    getImagePathFromInputStreamUri(activity, uri);
                    //return uri.getLastPathSegment();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return getDataColumn(activity, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;


    }




    /**
     * Get the local path of the media from the uri.
     *
     * @param uri
     * @param context
     * @return
     */
    public static String getPath(Uri uri, Context context) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Gets external storage content.
     *
     * @param uri the uri
     * @return the external storage content
     */
    @TargetApi(19)
    private static String getExternalStorageContent(Uri uri) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        if ("primary".equalsIgnoreCase(type))
            return Environment.getExternalStorageDirectory() + "/" + split[1];
        else {
            if (Environment.isExternalStorageRemovable())
                return System.getenv("EXTERNAL_STORAGE") + "/" + split[1];
            else
                return System.getenv("SECONDARY_STORAGE") + "/" + split[1];
        }
    }

    /**
     * Is external storage document boolean.
     *
     * @param uri the uri
     * @return the boolean
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        //Get the value of the data column for this Uri
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            //This is useful for MediaStore Uris, and other file-based ContentProviders.
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    

    /**
     * Gets the real path from uri.
     *
     * @param uri the uri
     * @return the real path from uri
     */
    public static String getRealPathFromURI(Activity activity, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String result = "";
        try {
            if (activity.getContentResolver() != null) {
                // Return a ContentResolver instance for your application's package.
                Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor == null) {
                    //Returns the path of this file.
                    result = uri.getPath();
                } else {
                  /*  //Move the cursor to the first row.
                    cursor.moveToFirst();
                    //Returns the zero-based index for the given column name, or -1 if the column doesn't exist. If you expect the column to exist use
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    //get string
                    result = cursor.getString(idx);
                    cursor.close();*/

                    //Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    if(cursor.moveToFirst()){
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        result = cursor.getString(columnIndex);
                    } else {
                        //boooo, cursor doesn't have rows ...
                    }
                    cursor.close();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * This method is used to hide the keyboard
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
//Central system API to the overall input method framework (IMF) architecture, which arbitrates interaction between applications and the current input method.
// You can retrieve an instance of this interface with Context.getSystemService().
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//Flag for hideSoftInputFromWindow(IBinder, int) to indicate that the soft input window should only be hidden
// if it was not explicitly shown by the user.
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * This method is used to display the custom dialog
     *
     * @param mActivity
     * @param txtCategory
     * @param dataResults
     */
    public static void customDialogList(final Activity mActivity, final TextView txtCategory, final List<CategoryPollResponseModel.Results> dataResults) {
        //Creates a dialog window that uses the default dialog theme.
        mListDialog = new Dialog(mActivity);
        //Enable extended window features.
        mListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setBackgroundDrawable
        mListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mListDialog.setContentView(R.layout.custom_dialog_adapter);
        select =  mListDialog.findViewById(R.id.selectCountry);
        //setting the text
        select.setText(mActivity.getResources().getString(R.string.choose_category));
        ListView list = (ListView) mListDialog.findViewById(R.id.component_list);
        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. The Adapter provides access to the data items.
        // The Adapter is also responsible for making a View for each item in the data set.
        categoryAdapter = new CategoryAdapter(mActivity, dataResults);
        //Sets the data behind this ListView.
        list.setAdapter(categoryAdapter);
        //show dialog
        mListDialog.show();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Setting the string in categoryid
                MApplication.setString(mActivity, com.contus.app.Constants.CATEGORY_ID, dataResults.get(position).getCategoryId());
                //setting the category name
                txtCategory.setText(dataResults.get(position).getCategoryName());
                //list dialog cancel
                mListDialog.cancel();

            }
        });

    }

    public static Uri getOutputMediaFileUri() {
        //// create a file to save the image
        return Uri.fromFile(MApplication.getOutputMediaFile());
    }

    /**
     * returning image / video
     */
    public static File getOutputMediaFile() {
        //Storage state if the media is present and mounted at its mount point with read/write access.
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File mediaFile = null;
        if (isSDPresent) {
            //The actual file referenced by a File may or may not exist.
            // It may also, despite the name File, be a directory or other non-regular file.
            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyPollBook");
            //Formats and parses dates in a locale-sensitive manner.
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
//            //Returns the path of this file.
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        return mediaFile;
    }

    /**
     * This method is used to get the differnce
     *
     * @param time
     * @return
     */
    private static long getDifference(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            //A specific moment in time, with millisecond precision. Values typically come from currentTimeMillis(),
            // and are always UTC, regardless of the system's time zone.
            Date convertedDate = dateFormat.parse(time);
            return System.currentTimeMillis() - convertedDate.getTime();
        } catch (java.text.ParseException e) {
            CustomToastLogger.logError(e.toString());
        }
        return 0;
    }

    /**
     * This method is used to get the time differnce
     *
     * @param time time
     * @return
     */
    public static String getTimeDifference(String time) {
        String timeDifference;
        long diff = getDifference(time);
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        int hours = (int) ((diff - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (diff - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours))
                / (1000 * 60);
        int diffNumber;
        if (days > 0) {
            diffNumber = days;
            timeDifference = days + " day";
        } else if (hours > 0) {
            diffNumber = hours;
            timeDifference = hours + " hour";
        } else if (min > 0) {
            diffNumber = min;
            timeDifference = min + " min";
        } else {
            diffNumber = 0;
            timeDifference = "now";
        }
        if (diffNumber > 1) {
            timeDifference += "s";
        }
        if (diffNumber != 0) {
            timeDifference += " Ago";
        }
        return timeDifference;
    }

    /**
     * This method is used to get the time differnce
     *
     * @param time time
     * @return
     */
    public static String getHours(String time) {
        SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date today = null;
        try {
            today = dateFromat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String todayStr = dateFromat.format(today);
        Log.e("todayStr", todayStr + "");
        return todayStr;
    }

    /**
     * This method is used to encode the string
     *
     * @param message
     * @return
     */
    public static String getEncodedString(String message) {
        String working = "";
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (!Character.isUnicodeIdentifierPart(c)) {
                working += String.format("\\u%04x", (int) message.charAt(i));
            } else {
                working += c;
            }
        }

        return working;
    }

    /**
     * This method is used to decode the string.
     *
     * @param message
     * @return
     */
    public static String getDecodedString(String message) {
        String working = message == null ? "" : message;
        int index;
        index = working.indexOf("\\u");
        while (index > -1) {
            int length = working.length();
            if (index > (length - 6))
                break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            //substring
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring, 16);
            //string start
            String stringStart = working.substring(0, index);
            String stringEnd = working.substring(numFinish);
            working = stringStart + ((char) number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        //Patterns are compiled regular expressions.
        // // Direct use of Pattern:
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        //The result of applying a Pattern to a given input.
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }






    /**
     * @param mTermsAndCondition
     * @param trackerId
     * @return
     */
    public static synchronized Tracker getTracker(Activity mTermsAndCondition, TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            //Google Analytics is a freemium web analytics service
            // offered by Google that tracks and reports website traffic.
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(mTermsAndCondition);
            // When dry run is set, hits will not be dispatched, but will still be logged as
            // though they were dispatched.
            analytics.setDryRun(false);
            // Set the log level to verbose.
            GoogleAnalytics.getInstance(mTermsAndCondition).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            // Set the log level to verbose.
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    /**
     * This method is used to concat the string with comma
     *
     * @param contactsChoosen
     * @return
     */
    public static String stringConcat(ArrayList<String> contactsChoosen) {
        //Replaces all matches for regularExpression within this
        // string with the given replacement.
        String value = contactsChoosen.toString().replaceAll("[\\s\\[\\]]", "");
        return value;
    }


    public enum TrackerName {
        // Tracker used only in this ap
        APP_TRACKER,
        // Tracker used by all the apps from a company. eg: roll-up tracking.
        GLOBAL_TRACKER,
        // Tracker used by all ecommerce transactions from a company.
        ECOMMERCE_TRACKER,
    }

    /**
     * This is used to display the google ad
     *
     * @param mAdView mAdView
     */
    public static void googleAd(AdView mAdView) {
        //Causes a device to receive test ads. The deviceId can be obtained by viewing the
        // logcat output after creating a new ad.
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        //Constructs an AdRequest with the specified attributes.
        mAdView.loadAd(adRequest);
    }

    /**
     * This method will return the server error and display te toast message
     *
     * @param retrofitError
     * @param activity
     */
    public static void errorMessage(RetrofitError retrofitError, Activity activity) {
        String errorDescription = retrofitError.getMessage();
        switch (retrofitError.getKind()) {
            case HTTP:
                //An internal error occurred while attempting to execute a request. It is best practice to
                // re-throw this exception so your application crashes.
                errorDescription = activity.getResources().getString(R.string.http_error);
                break;
            case NETWORK:
                //Whether or not this error was the result of a network error.
                errorDescription = activity.getResources().getString(R.string.error_connecting_error);
                break;
            case CONVERSION:
                // A non-200 HTTP status code was received from the server.
                errorDescription = activity.getResources().getString(R.string.error_passing_data);
                break;
            case UNEXPECTED:
                // An internal error occurred while attempting to execute a request. It is best practice to
                // re-throw this exception so your application crashes.
                errorDescription = activity.getResources().getString(R.string.error_unexpected);
                break;
            default:
                break;
        }
        //Toast message
        Toast.makeText(activity, errorDescription, Toast.LENGTH_SHORT).show();
    }

    /**
     * This mehod is used to track the screen
     *
     * @param mTermsAndCondition
     * @param classname
     */
    public static void screenTracker(Activity mTermsAndCondition, String classname) {
        /**Screen tracker**/
        Tracker t = getTracker(mTermsAndCondition, TrackerName.APP_TRACKER);
        /**Setting the screen name to display in alanytics**/
        t.setScreenName(classname);
        /**hit**/
        t.send(new HitBuilders.AppViewBuilder().build());
    }


    public static String getImagePathFromInputStreamUri(Activity activity, Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = activity.getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(activity, inputStream);

                filePath = photoFile.getPath();

            } catch (FileNotFoundException e) {
                // log
            } catch (IOException e) {
                // log
            }finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    private static File createTemporalFileFrom(Activity activity, InputStream inputStream) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile(activity);
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private static File createTemporalFile(Activity activity) {
        return new File(activity.getExternalCacheDir(), "tempFile.jpg"); // context needed
    }


}
