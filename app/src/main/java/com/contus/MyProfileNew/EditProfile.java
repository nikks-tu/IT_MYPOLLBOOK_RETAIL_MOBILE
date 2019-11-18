package com.contus.MyProfileNew;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.CustomDialogAdapter;
import com.contus.activity.GooglePlacesAutocompleteActivity;
import com.contus.app.Constants;
import com.contus.chatlib.listeners.OnTaskCompleted;
import com.contus.chatlib.utils.ContusConstantValues;
import com.contus.responsemodel.EditPersonalInfoModel;
import com.contus.restclient.EditPersonalInfoRestClient;
import com.contusfly.MApplication;
import com.contusfly.activities.ActivityBase;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.service.ChatService;
import com.contusfly.utils.ImageUploadS3;
import com.contusfly.utils.Utils;
import com.contusfly.views.CircularImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.polls.polls.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/16/2015.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class EditProfile extends ActivityBase implements OnTaskCompleted {
    static String result;
    boolean isDelete = true;
    //edit profile
    private EditProfile mProfileEdit;
    //textview
    TextView txtDone;
    //textview
    TextView txtCancel;
    //view
    View view5;
    //relative layout
    RelativeLayout rootView;
    //edit text
    private EditText editUserName;
    //view
    View txtView;
    //Imageview
    private CircularImageView imageView;
    //uer name
    private String userName;
    //textview
    private TextView editLocation;

    TextView txtTitle;

    //String
    private String location;
    //Edit text
    private EditText editStatus;
    //string
    private String status;
    //uer id
    private String userId;
    //profile image
    String profileImage;
    //uri image capture
    Uri mImageCaptureUri;
    //file path
    private File filepath;
    //image url
    String imageUrl;
    //country id
    private String countryID;
    //user city
    String userCity;
    //radio group
    RadioGroup radioGender;
    //gender
    private String gender;
    //radio button
    private RadioButton txtMale;
    //radio female
    private RadioButton txtFemale;
    //gender
    private String mGender;
    //image file uri
    private Uri imageFileUri;
    //edit profiel list dialog
    private Dialog editProfileListDialog;
    //image s3 bucket
    private ImageUploadS3 imageS3Bucket;
    //profile image
    private String updateProfileImage = "";
    //access token
    private String accessToken;
    //chossen photo
    String updatedPhoto;
    private MApplication mApplication;
    ImageView imgLocation;
    private TextView txtCount;
    private ImageView imgCancel;
    private DatabaseHelper db;
    AlertDialog.Builder builder;

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    final int PERMISSION_ALL = 1;

    private static String getUpdatePicturePath(Activity activity, Uri uri) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //frasco initialization
        Fresco.initialize(EditProfile.this);
        setContentView(R.layout.activity_edit_profile);
        init();
    }

    /**
     * Initializing the activity
     */
    private void init() {
        /*Initializing the activity**/
        mProfileEdit = this;
        txtDone = findViewById(R.id.txtNext);
        txtCancel = findViewById(R.id.txtCancel);
        editUserName =  findViewById(R.id.editUserName);
        editLocation =  findViewById(R.id.editLocation);
        editStatus =  findViewById(R.id.editStatus);
        txtTitle =  findViewById(R.id.txtProfile);
        txtMale =  findViewById(R.id.txtMale);
        txtFemale =  findViewById(R.id.txtFemale);
        radioGender =  findViewById(R.id.gender);
        imageView =  findViewById(R.id.imageView);
        imgLocation =  findViewById(R.id.imgLocation);
        txtView = findViewById(R.id.view3);
        rootView =  findViewById(R.id.rootView);
        txtCount =  findViewById(R.id.txt_size);
        imgCancel =  findViewById(R.id.imgCancel);
        view5 = findViewById(R.id.view5);
        updateProfileImage = MApplication.getString(EditProfile.this, Constants.PROFILE_IMAGE);


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Light.ttf");
        txtTitle.setTypeface(face);

        txtTitle.setText(getString(R.string.txt_edit_profile));

        if (updateProfileImage.isEmpty()) {
            updateProfileImage = "empty";
        }
        db = new DatabaseHelper(EditProfile.this);


        mApplication = (MApplication) getApplication();
        //Lollipop version status bar balack
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MApplication.settingStatusBarColor(mProfileEdit, getResources().getColor(android.R.color.black));
        }
        //Hiding the button when the keypad opens
        MApplication.hideButtonKeypadOpens(mProfileEdit, rootView, txtDone, txtView);
        //Setting the text
        editUserName.setText(MApplication.getDecodedString(MApplication.getString(mProfileEdit, Constants.PROFILE_USER_NAME)));
        //Setting the text
        editLocation.setText(MApplication.getString(mProfileEdit, Constants.CITY));
        //Setting the text
        imageUrl = MApplication.getString(mProfileEdit, Constants.PROFILE_IMAGE).replaceAll(" ", "%20");
        //Setting the text
        gender = MApplication.getString(mProfileEdit, Constants.USER_GENDER);
        //Getting the user id from the preference
        userId = MApplication.getString(mProfileEdit, Constants.USER_ID);
        //Getting the user id from the preference
        userName = MApplication.getString(mProfileEdit, Constants.PROFILE_USER_NAME);
        //Getting the user id from the preference
        countryID = MApplication.getString(mProfileEdit, Constants.COUNTRY_ID);
        //Getting the user id from the preference
        accessToken = MApplication.getString(mProfileEdit, Constants.ACCESS_TOKEN);
        //Getting the user id from the preference
        status = MApplication.getString(mProfileEdit, Constants.STATUS);
        if (status != null) {
            //Setting the text
            editStatus.setText(MApplication.getDecodedString(status));
          //  txtCount.setText(String.valueOf(50 - editStatus.getText().toString().trim().length()));
        }
        /*
         * If the the value matches with 1, then the radio but will set as 1
         */
        if (("1").equals(gender)) {
            //set checked true
            txtFemale.setChecked(true);
            //set checked color black
            txtMale.setTextColor(getResources().getColor(R.color.view_color));
            //tyext color black
            txtFemale.setTextColor(Color.BLACK);
            mGender = String.valueOf(txtFemale.getText());
        } else {
            txtFemale.setTextColor(getResources().getColor(R.color.view_color));
            txtMale.setTextColor(Color.BLACK);
            txtMale.setChecked(true);
            mGender = String.valueOf(txtMale.getText());
        }
        //Uploading an image in S3 bucket
        imageS3Bucket = new ImageUploadS3(getApplicationContext());
        //call back method
        imageS3Bucket.uplodingCallback(this);
        //Setting the boolean
        MApplication.setBoolean(mProfileEdit, Constants.PROFILE_IMAGE_TRUE, false);
        Log.e("imageUrl", imageUrl + "");
        //If image url is not empty
        if (imageUrl.equals("empty") || imageUrl.isEmpty()) {
            imgCancel.setVisibility(View.INVISIBLE);
        } else {
            Bitmap image = db.getImageCache("profile_pic");
            if (image != null) {
                imageView.setImageBitmap(image);
            } else {
                Utils.loadImageWithGlide(EditProfile.this, imageUrl, imageView, R.drawable.img_ic_user);
            }
        }


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show alert dialog
                showAlertDialog(com.contusfly.utils.Constants.EMPTY_STRING,
                        getString(R.string.remove_the_photo),
                        getString(R.string.text_yes), getString(R.string.text_no));
            }
        });
//A button with two states, checked and unchecked.
// When the button is pressed or clicked, the state changes automatically.
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.txtMale) {
                    //value from the selected radio button
                    mGender = String.valueOf(txtMale.getText());
                    //setting the text color
                    txtFemale.setTextColor(getResources().getColor(R.color.view_color));
                    //Setting the text color black
                    txtMale.setTextColor(Color.BLACK);
                } else if (checkedId == R.id.txtFemale) {
                    //value from the selected radio button
                    mGender = String.valueOf(txtFemale.getText());
                    //setting the text color
                    txtFemale.setTextColor(Color.BLACK);
                    //Setting the text color black
                    txtMale.setTextColor(getResources().getColor(R.color.view_color));
                }
            }
        });
        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(EditProfile.this, GooglePlacesAutocompleteActivity.class);
                //   Intent a = new Intent(EditProfile.this, LocationActivity.class);
                startActivity(a);
            }
        });
        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(EditProfile.this, GooglePlacesAutocompleteActivity.class);
              //  Intent a = new Intent(EditProfile.this, LocationActivity.class);
                startActivity(a);
            }
        });

        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Overridden Method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.e("count", editStatus.getText()
                        .toString().trim().length() + "");
                txtCount.setText(String.valueOf(50 - editStatus.getText()
                        .toString().trim().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Overridden Method
            }
        });
    }

    /**
     * On click listner
     *
     * @param clickedView-click view
     */

    public void onClick(final View clickedView) {
        if (clickedView.getId() == R.id.txtNext) {
            profileUpdateRequest();
        } else if (clickedView.getId() == R.id.txtCancel) {
            this.finish();
        } else if (clickedView.getId() == R.id.imageView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!hasPermissions(EditProfile.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(EditProfile.this, PERMISSIONS, PERMISSION_ALL);
                    Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editProfileChoosePic(mProfileEdit);
                }
            }



        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    /**
     *
     */
    private void profileUpdateRequest() {
        //value from the editext
        userName = editUserName.getText().toString().trim();
        //value from the editext
        location = editLocation.getText().toString().trim();
        //Value from the field status
        status = MApplication.getEncodedString(editStatus.getText().toString().trim());
        //If text utils is empty
        if (TextUtils.isEmpty(userName)) {
            //Display the toast message
            Toast.makeText(mProfileEdit, getResources().getString(R.string.enter_your_username),
                    Toast.LENGTH_SHORT).show();
            //If it is not checked
        } else if (!txtMale.isChecked() && !txtFemale.isChecked()) {
            //Toast message will display
            Toast.makeText(mProfileEdit, getResources().getString(R.string.enter_select_gender),
                    Toast.LENGTH_SHORT).show();
            //if it is empty
        } else if (TextUtils.isEmpty(location)) {
            //Toastmessage will display
            Toast.makeText(mProfileEdit, getResources().getString(R.string.enter_select_location),
                    Toast.LENGTH_SHORT).show();
            //If status is empty
        } else if (TextUtils.isEmpty(status)) {
            //Toast message will display
            Toast.makeText(mProfileEdit, getResources().getString(R.string.enter_enter_status),
                    Toast.LENGTH_SHORT).show();
        } else {
            //User profile request
            updateProfileRequest();
        }

    }

    /**
     * Choose the profiel picture
     *
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
        String[] cameraOptions = new String[]{getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_pic), getResources().getString(R.string.cancel_gallery)};
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
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.action_complete_using)), 10);
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
     * Take picture intent.
     */
    public void updatePictureIntent() {
        try {


            Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            imageFileUri = Uri.fromFile(photo);
            //image file uri
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(mIntent, 11);
            //list dialog cancel
            editProfileListDialog.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 15:
                if (resultCode == RESULT_OK) {
                    //file uri
                    mImageCaptureUri = data.getData();
                    //file path
                    filepath = new File(getUpdatePicturePath(mProfileEdit, mImageCaptureUri));
                    //set boolean
                    MApplication.setBoolean(mProfileEdit, Constants.PROFILE_IMAGE_TRUE, true);


                    //set image uri
                    Utils.loadImageWithGlide(EditProfile.this, filepath, imageView, R.drawable.img_ic_user);
                    imgCancel.setVisibility(View.VISIBLE);//visible
                    /* Calling the material design custom dialog **/
                    MApplication.materialdesignDialogStart(mProfileEdit);
                    //s3 bucket
                    imageS3Bucket.executeUpload(filepath, "image", "","PROFILES/");
                }
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    try {
                        //file uri
                        mImageCaptureUri = data.getData();
                        String url = Objects.requireNonNull(data.getData()).toString();
                        //file path
                       /* if(url.startsWith("content://com.google.android.apps.photos.content")) {
                        filepath=new File(MApplication.getPath(mImageCaptureUri,mProfileEdit));
                        }
                        else {*/
                            filepath = new File(MApplication.getPath(mProfileEdit, mImageCaptureUri));
                       /* }*/
                    } catch (URISyntaxException e) {
                        Log.e("", "", e);
                    }
                    //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent
                    // to send it to any interested BroadcastReceiver components, and startService(Intent)
                   try {


                       /*Intent newIntent1 = new AviaryIntent.Builder(this)
                               .setData(mImageCaptureUri) // input image src
                               .withOutput(Uri.parse(Constants.pictureFile + filepath)) // output file
                               .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                               .withOutputQuality(70)
                               .withOutputSize(MegaPixels.Mp1) // output quality
                               .build();
                       // start the activity
                       startActivityForResult(newIntent1, 15);*/

                       //set boolean
                       MApplication.setBoolean(mProfileEdit, Constants.PROFILE_IMAGE_TRUE, true);


                       //set image uri
                       Utils.loadImageWithGlide(EditProfile.this, filepath, imageView, R.drawable.img_ic_user);
                       imgCancel.setVisibility(View.VISIBLE);//visible
                       /*Calling the material design custom dialog **/
                       MApplication.materialdesignDialogStart(mProfileEdit);
                       //s3 bucket
                       imageS3Bucket.executeUpload(filepath, "image", "","PROFILES/");
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
                break;
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    //file path
                    filepath = new File(getUpdatePicturePath(mProfileEdit, imageFileUri));
                    /*//An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent
                    // to send it to any interested BroadcastReceiver components, and startService(Intent)
                    Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(imageFileUri) // input image src
                            .withOutput(Uri.parse(Constants.pictureFile + filepath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/

                    //set boolean
                    MApplication.setBoolean(mProfileEdit, Constants.PROFILE_IMAGE_TRUE, true);


                    //set image uri
                    Utils.loadImageWithGlide(EditProfile.this, filepath, imageView, R.drawable.img_ic_user);
                    imgCancel.setVisibility(View.VISIBLE);//visible
                    /** Calling the material design custom dialog **/
                    MApplication.materialdesignDialogStart(mProfileEdit);
                    //s3 bucket
                    imageS3Bucket.executeUpload(filepath, "image", "","PROFILES/");
                }
                break;
            default:
                break;
        }


    }

    /**
     * Request and response api method
     */
    private void updateProfileRequest() {
        if (MApplication.isNetConnected(mProfileEdit)) {
            /*Calling the material design custom dialog **/
            //Value fromt he prefernce
            String latitude = MApplication.getString(mProfileEdit, Constants.LATITUDE);
            Log.e("latitude", latitude + "");
            //Value from the prefernce
            String longitude = MApplication.getString(mProfileEdit, Constants.LONGITUDE);
            Log.e("longitude", longitude + "");
            //Value from the prefernce
            location = MApplication.getString(mProfileEdit, Constants.CITY);

            /*  Requesting the response from the given base url**/
            MApplication.materialdesignDialogStart(EditProfile.this);
            EditPersonalInfoRestClient.getInstance()
                    .postEditPersonalInfo("editProfileapi",
                            userId, userName, mGender, status, countryID, longitude,
                            latitude, updateProfileImage, location, accessToken, new Callback<EditPersonalInfoModel>() {
                                @Override
                                public void success(EditPersonalInfoModel welcomeResponseModel, Response response) {
                                    //Update the profiel response
                                    updateProfileResponse(welcomeResponseModel);
                                }

                                @Override
                                public void failure(RetrofitError retrofitError) {
                                    //error message if the request failures
                                    MApplication.errorMessage(retrofitError, mProfileEdit);
                                }

                            });


        } else {
            //toast message
            Toast.makeText(mProfileEdit, getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The values from the response are retrived using model class
     *
     * @param welcomeResponseModel model class
     */
    private void updateProfileResponse(EditPersonalInfoModel welcomeResponseModel) {
        /**welcome msg stored in model class are retrived**/
        String success = welcomeResponseModel.getmSuccess();

        if (("1").equals(success)) {

            //user id
            userId = welcomeResponseModel.getmResults().getUserId();
            //profile image
            profileImage = welcomeResponseModel.getmResults().getProfileImg().replaceAll(" ", "%20");
            //user city
            userCity = welcomeResponseModel.getmResults().getUserLocation();
            //user name
            userName = welcomeResponseModel.getmResults().getUserName();
            //gender
            gender = welcomeResponseModel.getmResults().getUserGender();
            //status
            status = welcomeResponseModel.getmResults().getUserStatus();
            //storing in prefernce
            MApplication.setString(mProfileEdit, Constants.CITY, userCity);
            //storing in prefernce
            MApplication.setString(mProfileEdit, Constants.PROFILE_IMAGE, profileImage);
            //storing in prefernce
            MApplication.setString(mProfileEdit, Constants.PROFILE_USER_NAME, userName);
            //finish the activity
            updateUserProfilePic();
            isDelete = true;

            //finish the activity
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Location from the preference
        location = MApplication.getString(mProfileEdit, Constants.CITY);
        if (location != null) {
            //Setting the text view
            editLocation.setText(location);
        }
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

    @Override
    public void onTaskCompleted(URL url, String s, String s1, int i) {
        Log.e("url", url + "");
        //profile image from the s3 bucket


        String CurrentString = url.toString();
        String[] separated = CurrentString.split("com/");
        CurrentString=separated[1];
        updateProfileImage = CurrentString;
        /* Calling the material design custom dialog **/
        MApplication.materialdesignDialogStop();

    }

    /**
     * Update user profile pic.
     */
    private void updateUserProfilePic() {
        userName = editUserName.getText().toString().trim();
        status = editStatus.getText().toString().trim();
        Log.e("userName", userName + "");
        Log.e("updateProfileImage", updateProfileImage + "");
        Log.e("status", status + "");
        startService(new Intent(EditProfile.this, ChatService.class)
                .putExtra("editUserName", userName)
                .putExtra("imageUrl", updateProfileImage)
                .putExtra("profStatus", status)
                .setAction(ContusConstantValues.CONTUS_XMPP_ACTION_EDIT_PROFILE));
        mApplication.storeStringInPreference(Constants.PROFILE_IMAGE, updateProfileImage);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(filepath.getAbsolutePath());
            ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, imageOutputStream);
            if (imageOutputStream != null) {
                byte[] byteArray = imageOutputStream.toByteArray();
                if (isDelete)
                    db.insertImageCache("profile_pic", byteArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MApplication.materialdesignDialogStop();
    }

    /**
     * Show alert dialog.
     *
     * @param title          the title
     * @param msg            the msg
     * @param positiveString the positive string
     * @param negativeString the negative string
     * @param
     * @param
     */
    public void showAlertDialog(String title, String msg, String positiveString, String negativeString) {
        //Creates a builder for an alert dialog that uses the default alert dialog theme.
         //builder = new AlertDialog.Builder(EditProfile.this);
        if(Build.VERSION.SDK_INT >= 21)
        {
            builder = new AlertDialog.Builder(EditProfile.this, R.style.AppCompatAlertDialogStyle);
        }
        else
            builder = new AlertDialog.Builder(EditProfile.this);
        //set message
        builder.setMessage(msg);
        if (!title.isEmpty())
            //setting the title
            builder.setTitle(title);
        builder.setNegativeButton(negativeString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton(positiveString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imgCancel.setVisibility(View.INVISIBLE);
                        updateProfileImage = "";
                        db.deleteImageCache("profile_pic");
                        isDelete = false;

                        Utils.loadImageWithGlide(EditProfile.this, "", imageView, R.drawable.img_ic_user);
                        dialog.dismiss();
                    }
                });
        //create

        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                else
                    return true;
            }
        }
        return true;
    }

}
