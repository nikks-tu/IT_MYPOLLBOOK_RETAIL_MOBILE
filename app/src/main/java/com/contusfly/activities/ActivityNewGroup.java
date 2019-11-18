/**
 * @category   ContusMessanger
 * @package    com.contusfly.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.activity.CustomDialogAdapter;
import com.contus.chatlib.listeners.OnTaskCompleted;
import com.contusfly.MApplication;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ImageUploadS3;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomToast;
import com.contusfly.views.DoProgressDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.polls.polls.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;

import com.contusfly.views.CustomEditText;
import com.contusfly.emoji.EmojiconsPopup;

/**
 * The Class ActivityNewGroup.
 */
public class ActivityNewGroup extends AppCompatActivity implements
        Utils.OnOptionSelection, View.OnClickListener, OnTaskCompleted {

    private static String result;
    /** The image view. */
    private SimpleDraweeView imageView;

    /** The edit text. */
    private CustomEditText editText;

    /** The m application. */
    private MApplication mApplication;

    /** The emojicons popup. */
    private EmojiconsPopup emojiconsPopup;

    /** The m utils. */
    private Utils mUtils;

    /** The img smiley. */
    private ImageView imgSmiley;

    /** The txt count. */
    private TextView txtCount;

    /** The img url. */
    private String imgUrl = Constants.EMPTY_STRING;

    /** The m file temp. */
    private File mFileTemp;

    /** The image task. */
    private ImageUploadS3 imageTask;

    /** The progress dialog. */
    private DoProgressDialog progressDialog;
    private View txtView;
    private TextView txtNext;
    private RelativeLayout rootView;
    private Uri mImageCaptureUri;
    private File filepath;
    private Dialog editProfileListDialog;
    private Uri imageFileUri;
    private String updatedPhoto;

    /**
     * On create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

    }

    /**
     * On post create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mApplication = (MApplication) getApplication();
        emojiconsPopup = new EmojiconsPopup(findViewById(R.id.view_new_group),
                this);
        mUtils = new Utils();
        mUtils.setOnOptionListener(this);
        imageTask = new ImageUploadS3(getApplicationContext());
        imageTask.uplodingCallback(this);
        imageView = (SimpleDraweeView) findViewById(R.id.imagGroup);
        imgSmiley = (ImageView) findViewById(R.id.img_grp_smiley);
        editText = (CustomEditText) findViewById(R.id.edt_gsub);
        rootView=(RelativeLayout)findViewById(R.id.view_new_group);
        Utils.setUpKeyboard(emojiconsPopup, imgSmiley, editText);
        txtCount = (TextView) findViewById(R.id.txt_size);
        txtView = findViewById(R.id.view1);
        txtNext = (TextView) findViewById(R.id.txtCreate);
        /**Calling this method to hide the button when the keypad opens**/
        MApplication.hideButtonEmojiconsOpens(ActivityNewGroup.this, rootView, txtNext, txtView);
        txtNext.setText(getString(R.string.text_next));
        txtNext.setOnClickListener(this);
        setControls();
    }

    /**
     * Sets the controls.
     */
    private void setControls() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Overridden Method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                txtCount.setText(String.valueOf(25 - editText.getText()
                        .toString().trim().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Overridden Method
            }
        });
        imageView.setOnClickListener(this);
        imgSmiley.setOnClickListener(this);
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(),
                    Constants.TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), Constants.TEMP_PHOTO_FILE_NAME);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_grp_smiley:
                popupKeyBoard();
                break;
            case R.id.imagGroup:
                editProfileChoosePic(ActivityNewGroup.this);
                break;
            case R.id.txtCreate:
                validateData();
                break;
            case R.id.imagBackArrow:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * Validate data.
     */
    private void validateData() {
        String subject = editText.getText().toString().trim();
        if (subject.isEmpty()) {
            CustomToast.showToast(this, getString(R.string.text_empty_subject));
        } else {
            finish();
            startActivityForResult(
                    new Intent(this, ActivityCreateGroup.class).putExtra(
                            Constants.USER_PROFILE_NAME, subject).putExtra(
                            Constants.USER_IMAGE, imgUrl),
                    Constants.ACTIVITY_REQ_CODE);
        }
    }

    /**
     * Popup key board.
     */
    private void popupKeyBoard() {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,
                InputMethodManager.SHOW_IMPLICIT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!emojiconsPopup.isShowing()) {
                    emojiconsPopup.showAtBottomPending();
                } else {
                    emojiconsPopup.dismiss();
                }
            }
        }, 100);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.contusfly.utils.Utils.OnOptionSeletction#onOptionSelected(java.lang
     * .String, int)
     */
    @Override
    public void onOptionSelected(String type, int position) {
        if (position == 0) {
            mApplication.chooseFileFromGallery(this, Constants.MIME_TYPE_IMAGE);
        } else {
            mApplication.takePhotoFromCamera(this,
                    Environment.getExternalStorageDirectory() + "/"
                            + getString(R.string.app_name) + "/"
                            + getString(R.string.text_profile_photos), false);
        }
    }

    /**
     * On activity result.
     *
     * @param requestCode
     *            the request code
     * @param resultCode
     *            the result code
     * @param data
     *            the data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 15:

                if (resultCode == RESULT_OK) {
                    //file uri
                    mImageCaptureUri = data.getData();
                    //file path
                    filepath = new File(getGroupPicturePath(ActivityNewGroup.this, mImageCaptureUri));
                    //set image uri
                    imageView.setImageURI(Uri.parse("file://" + filepath));
                    //s3 bucket
                    imageTask.executeUpload(filepath, "image", "","POLLS/");

                }
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    try {
                        //file uri
                        mImageCaptureUri = data.getData();
                        //file path
                        filepath = new File(MApplication.getPath(ActivityNewGroup.this, mImageCaptureUri));
                    } catch (URISyntaxException e) {
                        Log.e("", "", e);
                    }
                    /*//An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent
                    // to send it to any interested BroadcastReceiver components, and startService(Intent)
                    Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(mImageCaptureUri) // input image src
                            .withOutput(Uri.parse(com.contus.app.Constants.pictureFile + filepath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/

                    //set image uri
                    imageView.setImageURI(Uri.parse("file://" + filepath));
                    //s3 bucket
                    imageTask.executeUpload(filepath, "image", "","POLLS/");
                }
                break;
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    //file path
                    filepath = new File(getGroupPicturePath(ActivityNewGroup.this, imageFileUri));
                    /*//An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent
                    // to send it to any interested BroadcastReceiver components, and startService(Intent)
                    Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(imageFileUri) // input image src
                            .withOutput(Uri.parse(com.contus.app.Constants.pictureFile + filepath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/

                    //set image uri
                    imageView.setImageURI(Uri.parse("file://" + filepath));
                    //s3 bucket
                    imageTask.executeUpload(filepath, "image", "","POLLS/");
                }
                break;
            default:
                break;
        }
    }

    /**
     * Choose the profiel picture
     * @param activity
     */
    private void editProfileChoosePic(final Activity activity) {
        //A dialog is a small window that prompts the user to make a decision or enter additional information.
        editProfileListDialog = new Dialog(activity);
        //Enable extended window features.
        editProfileListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Change the background of this window to a custom Drawable.
        editProfileListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editProfileListDialog.setContentView(R.layout.custom_dialog_adapter);
        ListView list = (ListView) editProfileListDialog.findViewById(R.id.component_list);
        //plits this string using the supplied regularExpression
        String[] cameraOptions = new String[]{getResources().getString(R.string.take_photo),getResources().getString(R.string.choose_pic),getResources().getString(R.string.cancel_gallery)};
        //dilog adapter
        CustomDialogAdapter adapter = new CustomDialogAdapter(this, cameraOptions);
        list.setAdapter(adapter);
        //show
        editProfileListDialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        updatePictureIntent();
                        break;
                    case 1:
                        //An intent is an abstract description of an operation to be performed.
                        // It can be used with startActivity to launch an Activity
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.action_complete_using)), 10);
                        editProfileListDialog.cancel();
                        break;
                    case 2:
                        //cancel the dialog
                        editProfileListDialog.cancel();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", imageFileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        imageFileUri = savedInstanceState.getParcelable("file_uri");
    }


    /**
     * Take picture intent.
     */
    public void updatePictureIntent() {
        Intent mIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //Returns the current state of the primary shared/external storage media.
        File mFolder = new File(Environment.getExternalStorageDirectory() + "/"
                + getResources().getString(R.string.app_name));
//If folder does not exist
        if (!mFolder.exists()) {
            //create the folder
            mFolder.mkdir();
        }
        //Calendar is an abstract base class for converting between a Date object and a set of integer
        // fields such as YEAR, MONTH, DAY, HOUR, and so on. (A Date object represents a specific instant in time with millisecond precision.
        final Calendar c = Calendar.getInstance();
        //Storing the calended ate to the string
        String mDate = c.get(Calendar.DAY_OF_MONTH) + "-" + ((c.get(Calendar.MONTH)) + 1) + "-" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);
        //Image name
        updatedPhoto = String.format(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name) + "/%s.png", "profilepic(" + mDate + ")");
        //Converting the bitmap into the fiel
        File photo = new File(updatedPhoto);
        // f set, the activity will not be launched if it is already running at the top of the history stack.
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //The name of the Intent-extra used to control the orientation of a ViewImage or a MovieView.
        mIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //file uri
        imageFileUri=Uri.fromFile(photo);
        //image file uri
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageFileUri);
        startActivityForResult(mIntent, 11);
        //list dialog cancel
        editProfileListDialog.cancel();
    }
    /**
     * Handle gallery.
     *
     * @param uri
     *            the uri
     */
    private void handleGallery(Uri uri) {
        try {
            InputStream inputStream = null;
            inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
            Utils.copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();
            Log.e("mFileTemp",mFileTemp+"");
            Utils.cropImage(this, mFileTemp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Upload image.
     */
    private void uploadImage() {
        try {
            //    Bitmap photo = BitmapFactory.decodeFile(mFileTemp.getPath());
            Log.e("file","file://"+mFileTemp.getPath()+"");
            imageView.setImageURI(Uri.parse("file://"+mFileTemp.getPath()));
            //   ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //   photo.compress(Bitmap.CompressFormat.PNG, 0, stream);
            progressDialog = new DoProgressDialog(this);
            progressDialog.showProgress(getString(R.string.text_loading));
            imageTask.executeUpload(mFileTemp, Constants.MSG_TYPE_IMAGE, "","POLLS/");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * On task completed.
     *
     * @param url
     *            the url
     * @param s
     *            the s
     * @param s1
     *            the s1
     * @param i
     *            the i
     */
    @Override
    public void onTaskCompleted(URL url, String s, String s1, int i) {
        Log.e("imgUrl","imgUrl");
        if (progressDialog != null)
            progressDialog.dismiss();
        if (url != null) {
            imgUrl = url.toString();
            Log.e("imgUrl",imgUrl);
        }
    }

    private static String getGroupPicturePath(Activity activity, Uri uri) {
        //Cursor implementations are not required to be synchronized so code using a Cursor from multiple threads should
        // perform its own synchronization when using the Cursor.
        Cursor editPicCursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (editPicCursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {
            //move to the first
            editPicCursor.moveToFirst();
            //Get the coulm index
            int idx = editPicCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            //get the value from the index
            result = editPicCursor.getString(idx);
            //close the cursor
            editPicCursor.close();
        }
        return result;
    }
}
