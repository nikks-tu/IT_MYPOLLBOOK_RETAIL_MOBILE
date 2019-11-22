package com.new_chanages.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.CustomDialogAdapter;
import com.contus.app.Constants;
import com.contus.chatlib.listeners.OnTaskCompleted;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.utils.ImageUploadS3;
import com.contusfly.utils.Utils;
import com.contusfly.views.CircularImageView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.new_chanages.adapters.GroupContactsAdapter;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.models.AppVersionPostParameters;
import com.new_chanages.models.ContactDetailsModel;
import com.new_chanages.postParameters.GetGroupsPostParameters;
import com.polls.polls.R;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditGroupActivity extends AppCompatActivity  implements OnTaskCompleted {
    RecyclerView group_member_recyclr_view;
    CardView cv_profile_card;
    EditText edt_group_name;
    CircularImageView iv_group_icon;
    TextView tv_add_participant;
    TextView tv_title;
    TextView tv_members_count;
    View view_line1;
    ImageView iv_back, iv_save, iv_edit;
    Context mContext;
    static String result;
    String createGroupAction = "creategroupapi";
    String group_creater;
    String group_name;
    String group_image="";
    String contacts="";
    String group_id="";
    //uri image capture
    Uri mImageCaptureUri;
    //image file uri
    private Uri imageFileUri;
    //file path
    private File filepath;
    //image url
    String imageUrl;
    //profile image
    private String updateProfileImage = "";
    //access token
    private String accessToken;
    //chosen photo
    String updatedPhoto;
    //edit profile list dialog
    private Dialog editProfileListDialog;
    private ImageView imgCancel;
    AlertDialog.Builder builder;
    //image s3 bucket
    private ImageUploadS3 imageS3Bucket;
    ArrayList<ContactDetailsModel> contactList;
    String singleGroupApiAction = "getsinglegroupapi";
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    GroupContactsAdapter contactsAdapter;
    final int PERMISSION_ALL = 1;
    String checkContactAction="existingcontactsapi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_create_layout);
        group_member_recyclr_view = findViewById(R.id.group_member_recyclr_view);
        group_member_recyclr_view.setLayoutManager(new LinearLayoutManager(this));
        initialize();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        tv_add_participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                dbHelper.deletContactList();
                MApplication.setString(mContext, Constants.CONTACT_LIST, "");
                Intent intent = new Intent(mContext, AllContactsActivity.class);
                intent.putExtra("fromActivity", "Edit");
                startActivity(intent);
            }
        });

        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
                if(!group_name.equals(""))
                {
                    if(group_name.length()<5)
                    {
                        Toast.makeText(mContext, "Group name should be more than 5 letters!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(!contacts.equals(""))
                        {
                            group_image = updateProfileImage;
                            iv_save.setEnabled(false);
                            serviceCall();
                        }
                        else {

                            Toast.makeText(mContext, "Please add some contacts!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else {
                    Toast.makeText(mContext, "Please enter group name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show alert dialog
                showAlertDialog(com.contusfly.utils.Constants.EMPTY_STRING,
                        getString(R.string.remove_the_photo),
                        getString(R.string.text_yes), getString(R.string.text_no));
            }
        });


        iv_group_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(mContext, PERMISSIONS)){
                        ActivityCompat.requestPermissions(EditGroupActivity.this  , PERMISSIONS, PERMISSION_ALL);
                        Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        editProfileChoosePic(EditGroupActivity.this);
                    }
                }
                else

                {
                    editProfileChoosePic(EditGroupActivity.this);
                }
            }
        });


        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableViews();


            }
        });

    }


    private void getInputs() {
        group_name = edt_group_name.getText().toString();
        group_image = updateProfileImage;
    }

    private void enableViews() {
        edt_group_name.setEnabled(true);
        imgCancel.setEnabled(true);
        iv_save.setVisibility(View.VISIBLE);
        tv_add_participant.setVisibility(View.VISIBLE);
        tv_add_participant.setEnabled(true);
        iv_edit.setVisibility(View.GONE);
        imgCancel.setVisibility(View.VISIBLE);
        iv_group_icon.setEnabled(true);


    }

    /**
     * Choose the profile picture
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
        ListView list =  editProfileListDialog.findViewById(R.id.component_list);
        //splits this string using the supplied regularExpression
        String[] cameraOptions = new String[]{getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_pic), getResources().getString(R.string.cancel_gallery)};
        //dialog adapter
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



    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);

        Call<JsonElement> call = service.createGroup(new GetGroupsPostParameters(createGroupAction, group_creater, group_name, group_image, contacts, group_id));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    JsonObject jsonObject = response.body().getAsJsonObject();
                   if( jsonObject.get("success").getAsString().equals("1"))
                   {
                       Toast.makeText(mContext, "Details Updated successfully!", Toast.LENGTH_SHORT).show();
                       finish();
                   }
                   else {
                       Toast.makeText(mContext, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                   }
                }else {
                    /*Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();*/
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast toast = Toast.makeText(mContext , ""+t, Toast.LENGTH_LONG);
                toast.show();

            }
        });

    }



    private void serviceCallForGettingGroupDetails() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);

        Call<JsonElement> call = service.getSingleGroup(new AppVersionPostParameters(singleGroupApiAction, group_id));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject resultObject = jsonObject.get("results").getAsJsonObject();
                   if( jsonObject.get("success").getAsString().equals("1"))
                   {
                       //Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                       edt_group_name.setText(resultObject.get("group_name").getAsString());
                       String image = resultObject.get("group_image").getAsString();
                       Utils.loadImageWithGlideSingleImageRounderCorner(getApplicationContext(), image, iv_group_icon, R.drawable.img_ic_user);
                       contactList = new ArrayList<>();
                       JsonArray contactArray = resultObject.get("contacts").getAsJsonArray();
                       for (int i=0; i<contactArray.size(); i++)
                       {
                           JsonObject object = contactArray.get(i).getAsJsonObject();
                           ContactDetailsModel model = new ContactDetailsModel();
                           model.setCountry_code(object.get("country_code").getAsString());
                           model.setName(object.get("name").getAsString());
                           model.setMobile_number(object.get("mobile_number").getAsString());
                           model.setProfile_image(object.get("profile_image").getAsString());
                           contactList.add(model);
                           if(contacts.equals(""))
                           {
                               contacts = model.getMobile_number();
                           }
                           else {
                               contacts = contacts + ","+ model.getMobile_number();
                           }
                       }
                       if(contactList.size()>0)
                       {
                           MDatabaseHelper db = new MDatabaseHelper(mContext);
                           for(int i=0; i<contactList.size(); i++)
                           {
                               db.addContactToSelected(contactList.get(i).getName(), contactList.get(i).getMobile_number(),
                                       "true", contactList.get(i).getProfile_image(), "", "true");
                           }
                           db.close();
                           tv_members_count.setText(String.valueOf(contactList.size()));
                           contactsAdapter = new GroupContactsAdapter(mContext, contactList);
                           group_member_recyclr_view.setAdapter(contactsAdapter);
                       }
                      // Toast.makeText(mContext, contacts, Toast.LENGTH_SHORT).show();
                       disableViews();
                   }
                   else {
                       Toast.makeText(mContext, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                   }
                }else {
                    /*Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();*/
                }

            }

            private void disableViews() {
                tv_title.setEnabled(false);
                tv_add_participant.setEnabled(false);
                iv_group_icon.setEnabled(false);
                imgCancel.setEnabled(false);
                imgCancel.setVisibility(View.GONE);
                edt_group_name.setEnabled(false);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast toast = Toast.makeText(mContext , ""+t, Toast.LENGTH_LONG);
                toast.show();

            }
        });

    }


    /**
     * Take picture intent.
     */
    public void updatePictureIntent() {
        try {
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
            //Storing the calender date to the string
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
            mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
                    filepath = new File(getUpdatePicturePath(this, mImageCaptureUri));
                    //set boolean
                    MApplication.setBoolean(mContext, Constants.PROFILE_IMAGE_TRUE, true);
                    //set image urinew Handler().postDelayed()
                    Utils.loadImageWithGlide(mContext, filepath, iv_group_icon, R.drawable.img_ic_user);
                    imgCancel.setVisibility(View.VISIBLE);
                    //visible Calling the material design custom dialog *
                    MApplication.materialdesignDialogStart(this);
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
 if(url.startsWith("content://com.google.android.apps.photos.content")) {
                        filepath=new File(MApplication.getPath(mImageCaptureUri, mContext));
                        }
                        else {

                        filepath = new File(MApplication.getPath(this, mImageCaptureUri));
 }

                    } catch (URISyntaxException e) {
                        Log.e("", "", e);
                    }
                    //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent
                    // to send it to any interested BroadcastReceiver components, and startService(Intent)
                    try {
/*

                        Intent newIntent1 = new AviaryIntent.Builder(this)
                               .setData(mImageCaptureUri) // input image src
                               .withOutput(Uri.parse(Constants.pictureFile + filepath)) // output file
                               .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                               .withOutputQuality(70)
                               .withOutputSize(MegaPixels.Mp1) // output quality
                               .build();
                       // start the activity
                       startActivityForResult(newIntent1, 15);*/


                        //set boolean
                        MApplication.setBoolean(this, Constants.PROFILE_IMAGE_TRUE, true);
                        //set image uri
                        Utils.loadImageWithGlide(mContext, filepath, iv_group_icon, R.drawable.img_ic_user);
                        imgCancel.setVisibility(View.VISIBLE);
                        //visible Calling the material design custom dialog *

                        MApplication.materialdesignDialogStart(this);
                        //s3 bucket
                        imageS3Bucket.executeUpload(filepath, "image", "","PROFILES/");
                    }    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    //file path
                    filepath = new File(getUpdatePicturePath(this, imageFileUri));
//*
//An                    intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent
                    // to send it to any interested BroadcastReceiver components, and startService(Intent)
                  /*  Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(imageFileUri) // input image src
                            .withOutput(Uri.parse(Constants.pictureFile + filepath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/


                    //set boolean
                    MApplication.setBoolean(this, Constants.PROFILE_IMAGE_TRUE, true);


                    //set image uri
                    Utils.loadImageWithGlide(mContext, filepath, iv_group_icon, R.drawable.img_ic_user);
                    imgCancel.setVisibility(View.VISIBLE);//visible Calling the material design custom dialog *

                    MApplication.materialdesignDialogStart(this);
                    //s3 bucket
                    imageS3Bucket.executeUpload(filepath, "image", "","PROFILES/");
                }
                break;
            default:
                break;
        }
    }


    public void showAlertDialog(String title, String msg, String positiveString, String negativeString) {
        //Creates a builder for an alert dialog that uses the default alert dialog theme.
        //builder = new AlertDialog.Builder(EditProfile.this);
        if(Build.VERSION.SDK_INT >= 21)
        {
            builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
        }
        else
            builder = new AlertDialog.Builder(mContext);
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
                       /* db.deleteImageCache("profile_pic");
                        isDelete = false;*/

                        Utils.loadImageWithGlide(mContext, "", iv_group_icon, R.drawable.img_ic_user);
                        dialog.dismiss();
                    }
                });
        //create

        builder.create().show();
    }

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

    private void initialize() {
        mContext = EditGroupActivity.this;
        group_creater = MApplication.getString(mContext, Constants.USER_ID);
        edt_group_name = findViewById(R.id.edt_group_name);
        iv_group_icon = findViewById(R.id.iv_group_icon);
        tv_title = findViewById(R.id.tv_title);
        tv_members_count = findViewById(R.id.tv_members_count);
        tv_add_participant = findViewById(R.id.tv_add_participant);
        group_member_recyclr_view.setVisibility(View.VISIBLE);
        iv_back = findViewById(R.id.iv_back);
        iv_save = findViewById(R.id.iv_save);
        iv_edit = findViewById(R.id.iv_edit);
        iv_edit.setVisibility(View.VISIBLE);
        imgCancel =  findViewById(R.id.imgCancel);
        iv_save.setVisibility(View.GONE);
        //Uploading an image in S3 bucket
        imageS3Bucket = new ImageUploadS3(getApplicationContext());
        //call back method
        tv_members_count.setVisibility(View.VISIBLE);
        imageS3Bucket.uplodingCallback(this);
        MApplication.setString(mContext, Constants.CONTACT_LIST, "");
        group_id = MApplication.getString(mContext, Constants.GET_GROUP_POLL_ID);
        serviceCallForGettingGroupDetails();
        MDatabaseHelper db = new MDatabaseHelper(mContext);
        db.deleteSelectedContacts();
        db.close();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("", "Permission callback called-------");
        switch (requestCode) {
            case PERMISSION_ALL: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("", "Storage services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                        editProfileChoosePic(this);

                    } else {
                        Log.d("", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Please provide required permissions!", "Information",
                                    new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    hasPermissions(mContext, PERMISSIONS);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Please enable permissions from settings", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message,String title, DialogInterface.OnClickListener okListener) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        android.app.AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= 21)
            builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        else
            builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        alertDialog.setTitle(title);
        builder.create().show();
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", okListener);
        alertDialog.setButton("Cancel", okListener);
        alertDialog.show();

    }

    @Override
    public void onTaskCompleted(URL url, String s, String s1, int i) {
        Log.e("url", url + "");
        //profile image from the s3 bucket


        String CurrentString = url.toString();
        String[] separated = CurrentString.split(getString(R.string.regex_com));
        CurrentString=separated[1];
        updateProfileImage = CurrentString;
        /* Calling the material design custom dialog **/
        MApplication.materialdesignDialogStop();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void serviceCallForExistingContacts() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);

        Call<JsonElement> call = service.checkExistingContacts(new GetGroupsPostParameters(checkContactAction, contacts));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    if( jsonObject.get("success").getAsString().equals("1"))
                    {
                        JsonArray result = jsonObject.get("results").getAsJsonArray();
                        contactList = new ArrayList<>();
                        for (int i=0; i<result.size(); i++)
                        {
                            JsonObject object = result.get(i).getAsJsonObject();
                            ContactDetailsModel model = new ContactDetailsModel();
                            model.setCountry_code(object.get("country_code").getAsString());
                            model.setName(object.get("name").getAsString());
                            model.setMobile_number(object.get("mobile_number").getAsString());
                            model.setProfile_image(object.get("profile_image").getAsString());
                            contactList.add(model);
                        }
                        if(contactList.size()>0)
                        {
                            tv_members_count.setText(String.valueOf(contactList.size()));
                            contactsAdapter = new GroupContactsAdapter(mContext, contactList);
                            group_member_recyclr_view.setAdapter(contactsAdapter);
                        }
                    }
                    else {
                        Toast.makeText(mContext, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    /*Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();*/
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast toast = Toast.makeText(mContext , ""+t, Toast.LENGTH_LONG);
                toast.show();

            }
        });

    }

    @Override
    protected void onResume() {
        String isContactAvailable = MApplication.getString(mContext, Constants.CONTACT_LIST);
        if(!isContactAvailable.equals(""))
        {
            String myContact = MApplication.getString(mContext, Constants.PHONE_NUMBER);
            contacts = contacts+","+myContact;
            contacts = contacts +","+isContactAvailable;
            serviceCallForExistingContacts();
        }
        super.onResume();
    }

}
