package com.contusfly.createpoll;

import android.Manifest;
import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.CustomDialogAdapter;
import com.contus.app.Constants;
import com.contus.chatlib.listeners.OnTaskCompleted;
import com.contus.responsemodel.CategoryPollResponseModel;
import com.contus.responsemodel.CreatePollResponseModel;
import com.contus.restclient.CategoryRestClient;
import com.contus.restclient.CreatePollRestClient;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.contusfly.utils.ImageUploadS3;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.new_chanages.AppConstents;
import com.new_chanages.activity.GroupSelection;
import com.new_chanages.adapters.GroupCheckListAdapter;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.models.GroupsNameObject;
import com.new_chanages.postParameters.GetGroupsPostParameters;
import com.polls.polls.R;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by user on 7/15/2015.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class YesOrNo extends Activity implements OnTaskCompleted {
    private static Activity mYesOrNo;
    private EditText txtCategory;
    private static List<CategoryPollResponseModel.Results> dataResults;
    private SmoothProgressBar googleNow;
    private SimpleDraweeView imageChoose;
    Uri mImageCaptureUri;
    File filepath;
    private SimpleDraweeView imageAdditional;
    private String userId;
    File question1;
    File question2;
    private EditText editQuestion;
    boolean isgroupPoll=false;
    String category;
    private String question;
    private Dialog listDialog;
    String choosenPhoto;
    private Uri imageFileUri;
    private ArrayList<String> listGroupid;
    private ArrayList<String> mArrayList;
    private ArrayList<String> mCategory;
    private ArrayList<String> mGroupName;
    private ArrayList<String> mContactName;
    ArrayList<String> mArrayGroupId;
    ArrayList<String> mArrayContactId;
    private String mGroupId;
    private String mContactsId;
    private String categoryId;
    private String mContact = "Public";
    String action="getgroupsapi";
    TextView txtTitle;
    private TextView txtGroup;
    private TextView txtPublic;
    private TextView txtContacts;
    Uri uri1;
    private Uri uri;
    String contactName;
    ImageUploadS3 imgTask;
    String image_choose_url, image_addition_url;
    boolean isimagechoose = true;
    private File choose_file_path, additinal_file_path;
    private Button create;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    String userType="users";
    ArrayList<GroupsNameObject> groupsList;
    final int PERMISSION_ALL = 1;
    public static void CategoryPollRequest(final SmoothProgressBar googleNow, final Activity activity, final TextView txtCategory) {
        if (MApplication.isNetConnected(activity)) {
            MApplication.materialdesignDialogStart(activity);
            String userId = MApplication.getString(activity, Constants.USER_ID);
            /* Requesting the response from the given base url**/
            CategoryRestClient.getInstance().postCategoryData("categoryapi_list", userId
                    , new Callback<CategoryPollResponseModel>() {
                        @Override
                        public void success(CategoryPollResponseModel welcomeResponseModel, Response response) {

                            googleNow.setVisibility(View.GONE);
                            googleNow.progressiveStart();

                            dataResults = welcomeResponseModel.getResults();
                            if (dataResults.size() != 0) {
                                MApplication.setBoolean(activity, Constants.YES_OR_NO, true);

                                MApplication.customDialogList(activity, txtCategory, dataResults);
                            }
                            googleNow.setVisibility(View.GONE);
                            googleNow.progressiveStop();
                            MApplication.materialdesignDialogStop();

                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            String errorDescription = retrofitError.getMessage();
                            switch (retrofitError.getKind()) {
                                case HTTP:
                                    errorDescription = mYesOrNo.getResources().getString(R.string.http_error);
                                    break;
                                case NETWORK:
                                    errorDescription = mYesOrNo.getResources().getString(R.string.error_connecting_error);
                                    break;
                                case CONVERSION:
                                    errorDescription = mYesOrNo.getResources().getString(R.string.error_passing_data);
                                    break;
                                case UNEXPECTED:
                                    errorDescription = mYesOrNo.getResources().getString(R.string.error_unexpected);
                                    break;
                                default:
                                    break;
                            }
                            Toast.makeText(mYesOrNo, errorDescription,
                                    Toast.LENGTH_SHORT).show();
                            MApplication.materialdesignDialogStop();

                        }

                    });
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_yesorno_options);
        init();
        AdView mAdView =  findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());




    }

    private void init() {
        /*Initializing the activity**/
        mYesOrNo = this;
        txtCategory = findViewById(R.id.txtCategory);
        editQuestion = findViewById(R.id.editQuestion);
        imageChoose = findViewById(R.id.choose);
        imageAdditional = findViewById(R.id.ChooseAdditional);
        googleNow = findViewById(R.id.google_now);
        txtPublic = findViewById(R.id.txtPublic);
        txtGroup = findViewById(R.id.txtGroup);
        txtTitle = findViewById(R.id.title);
        txtContacts = findViewById(R.id.txtContacts);
        create = findViewById(R.id.create);
        userId = MApplication.getString(mYesOrNo, Constants.USER_ID);
        mCategory = new ArrayList<>();
        listGroupid = new ArrayList<>();
        mArrayList = new ArrayList<>();
        mContactName = new ArrayList<>();
        mGroupName = new ArrayList<>();
        mArrayContactId = new ArrayList<>();
        mArrayGroupId = new ArrayList<>();
        groupsList = new ArrayList<>();
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Light.ttf");
        txtTitle.setTypeface(face);
        //Uploading an image in S3 bucket
        imgTask = new ImageUploadS3(getApplicationContext());
        //call back method
        imgTask.uplodingCallback(this);

        serviceCallForGroups();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCategory.clear();
        mCategory = MApplication.loadStringArray(mYesOrNo, mCategory, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO_SIZE);
        Log.e("mCategory", mCategory + "");
        mArrayList.clear();
        mContactName.clear();
        listGroupid.clear();
        mGroupName.clear();
        DatabaseHelper db = new DatabaseHelper(this);

        if (mCategory.contains("Public")) {
            txtPublic.setVisibility(View.VISIBLE);
        } else {
            txtPublic.setVisibility(View.GONE);
        }
     /*   if (!db.getAllGroupDetails(1).isEmpty()) {
            for (int i = 0; db.getAllGroupDetails(1).size() > i; i++) {
                txtGroup.setVisibility(View.VISIBLE);
                if (!listGroupid.contains(db.getAllGroupDetails(1).get(i).getId())) {
                    mGroupName.add(db.getAllGroupDetails(1).get(i).getName());
                    listGroupid.add(db.getAllGroupDetails(1).get(i).getId());
                }
                contactName = mGroupName.toString().replaceAll(getString(R.string.regex_for_posts), "");
            }
            contactName = mGroupName.toString().replaceAll(getString(R.string.regex_for_posts), "");
            txtGroup.setText(MApplication.getDecodedString(contactName));
            mGroupId = listGroupid.toString().replaceAll(getString(R.string.regex_for_posts), "");
        } else {
            txtGroup.setVisibility(View.GONE);
        }
*/


        if (!db.getAllContactsDetails(1).isEmpty()) {
            for (int i = 0; db.getAllContactsDetails(1).size() > i; i++) {
                txtContacts.setVisibility(View.VISIBLE);
                if (!mArrayList.contains(db.getAllContactsDetails(1).get(i).getId())) {
                    mContactName.add(db.getAllContactsDetails(1).get(i).getName());
                    mArrayList.add(db.getAllContactsDetails(1).get(i).getId());
                }
            }
            contactName = mContactName.toString().replaceAll(getString(R.string.regex_for_posts), "");
            txtContacts.setText(MApplication.getDecodedString(contactName));
            mContactsId = mArrayList.toString().replaceAll(getString(R.string.regex_for_posts), "");
        } else {
            txtContacts.setVisibility(View.GONE);
        }
    }

    public void onClick(final View clickedView) {
        switch (clickedView.getId()) {
            case R.id.imagBackArrow: {
                finish();
            }
            break;
            case R.id.relativeLayout: {
               /* Intent a = new Intent(YesOrNo.this, CreatePollOptions.class);
                startActivity(a);*/
            }
            break;

            case R.id.create: {

                category = txtCategory.getText().toString().trim();
                question = editQuestion.getText().toString().trim();
                if (TextUtils.isEmpty(category)) {
                    Toast.makeText(mYesOrNo, getResources().getString(R.string.select_your_category),
                            Toast.LENGTH_SHORT).show();
                    txtCategory.requestFocus();
                } else if (TextUtils.isEmpty(question)) {
                    Toast.makeText(mYesOrNo, getResources().getString(R.string.enter_enter_question),
                            Toast.LENGTH_SHORT).show();
                    editQuestion.requestFocus();
                } else if (question.length() < 2) {
                    Toast.makeText(mYesOrNo, getResources().getString(R.string.enter_valid_question),
                            Toast.LENGTH_SHORT).show();
                    editQuestion.requestFocus();
                } else {


                    if(isgroupPoll){
                        googleNow.setVisibility(View.VISIBLE);
                        googleNow.progressiveStart();
                        imgTask.executeUpload(choose_file_path, "image", "", "POLLS/");
                    }
                    else{
                        //Creates a builder for an alert dialog that uses the default alert dialog theme.
                        AlertDialog.Builder builder;
                        //= new AlertDialog.Builder(YesOrNo.this);
                        //set message

                        if (Build.VERSION.SDK_INT >= 21)
                            builder = new AlertDialog.Builder(YesOrNo.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
                        else
                            builder = new AlertDialog.Builder(YesOrNo.this);
                        builder.setMessage("Please validate your poll for appropriate grammar, spellings and content before submitting it. Otherwise it will be rejected.");
                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dialog
                                        dialog.dismiss();
                                    }
                                });

                        builder.setPositiveButton("Create",

                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //deleteThePoll(mClickPosition);//This method is used to delete the poll

                                        create.setEnabled(false);

                                        googleNow.setVisibility(View.VISIBLE);
                                        googleNow.progressiveStart();
                                        imgTask.executeUpload(choose_file_path, "image", "", "POLLS/");

                                        dialog.dismiss();
                                    }
                                });
                        //create
                        builder.create().show();
                    }

                }


            }
            break;
            case R.id.choose: {
                MApplication.chooseFlag = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(YesOrNo.this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(YesOrNo.this, PERMISSIONS, PERMISSION_ALL);
                        //Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        choosePic();
                    }
                }
                else
                {
                    choosePic();
                }

            }
            break;
            case R.id.ChooseAdditional: {
                MApplication.chooseFlag = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(YesOrNo.this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(YesOrNo.this, PERMISSIONS, PERMISSION_ALL);
                       // Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        choosePic();
                    }
                }
                else
                {
                    choosePic();
                }
            }
            break;
            case R.id.txtGroup: {
                mContact="";
                if(AppConstents.GROUPlIST.size()!=0) {
                    Intent intent = new Intent(YesOrNo.this, GroupSelection.class);
                    intent.putExtra("DATA", AppConstents.GROUPlIST);
                    startActivityForResult(intent, 20);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please wait data loading",Toast.LENGTH_SHORT).show();
                }


            }
            break;
            case R.id.txtPublic: {
                isgroupPoll=false;
                txtGroup.setTextColor(mYesOrNo.getResources().getColor(R.color.grey_color));
                txtPublic.setTextColor(mYesOrNo.getResources().getColor(R.color.blue_color));
                mContact = "Public";
            }
            break;

            case R.id.txtCategory: {
                CategoryPollRequest(googleNow, mYesOrNo, txtCategory);
            }
            break;
            default:
                break;
        }
    }

    public void createPollSubmit() {

        create.setEnabled(false);
        question1 = new File(MApplication.getString(mYesOrNo, "imageFilePathQuestion1"));
        question2 = new File(MApplication.getString(mYesOrNo, "imageFilePathQuestion2"));


        if (!MApplication.getString(mYesOrNo, "imageFilePathQuestion1").isEmpty() || !MApplication.getString(mYesOrNo, "imageFilePathQuestion2").isEmpty()) {
            if (MApplication.getString(mYesOrNo, "imageFilePathQuestion1").isEmpty() && !MApplication.getString(mYesOrNo, "imageFilePathQuestion2").isEmpty()) {
                createPollYesOrNOPollQuestion2();
            } else if (!MApplication.getString(mYesOrNo, "imageFilePathQuestion1").isEmpty() && MApplication.getString(mYesOrNo, "imageFilePathQuestion2").isEmpty()) {
                createPollYesOrNOQuestion1();
            } else {
                createPollYesOrNO();
            }
        } else {
            createPollYesOrNOEmpty();
        }
    }


    private void serviceCallForGroups() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);
        Call<JsonElement> call = service.getAllGroups(new GetGroupsPostParameters(action, Integer.parseInt(userId)));
        call.enqueue(new retrofit2.Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                //hideloader();
                if (response.body() != null) {
                    JsonObject result = response.body().getAsJsonObject();
                    if (!result.isJsonNull()) {
                        String success = result.get("success").getAsString();
                        if(success.equals("1"))
                        {
                            groupsList = new ArrayList<>();
                            AppConstents.GROUPlIST=new ArrayList<>();
                            JsonArray jsonArray = result.get("results").getAsJsonArray();
                            if(jsonArray.size()>0)
                            {
                                for (int i=0; i<jsonArray.size();i++)
                                {
                                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                    GroupsNameObject object = new GroupsNameObject();

                                    object.setGroupId(jsonObject.get("group_id").getAsInt());
                                    object.setGroupImage(jsonObject.get("group_image").getAsString());
                                    object.setGroupName(jsonObject.get("group_name").getAsString());
                                    object.setGroupStatus("FALSE");
                                    groupsList.add(object);
                                }

                                AppConstents.GROUPlIST =groupsList;
                               // Toast.makeText(mYesOrNo, "Test"+groupsList.size(), Toast.LENGTH_SHORT).show();
                            }
                            if(groupsList.size()>0)
                            {
                                txtGroup.setVisibility(View.VISIBLE);
                            }
                            else {
                                txtGroup.setVisibility(View.GONE);
                            }
                        }
                        else {
                        }

                    } else {

                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //Toast.makeText(mYesOrNo, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 20:

                if (resultCode == RESULT_OK) {
                    txtPublic.setTextColor(mYesOrNo.getResources().getColor(R.color.grey_color));
                    txtGroup.setTextColor(mYesOrNo.getResources().getColor(R.color.blue_color));

                    isgroupPoll = true;
                    mContact="";

                    if(AppConstents.GROUPlIST.size()>0)
                    {
                        for (int i = 0; i < AppConstents.GROUPlIST.size(); i++) {
                            if(AppConstents.GROUPlIST.get(i).getGroupStatus().equalsIgnoreCase("TRUE"))
                            {
                                if (i == 0) {
                                    mContact = String.valueOf(AppConstents.GROUPlIST.get(i).getGroupId());
                                } else {
                                    mContact = mContact + "," + AppConstents.GROUPlIST.get(i).getGroupId();
                                }
                            }
                        }
                    }


                }
                break;
            case 15:
                if (resultCode == RESULT_OK) {
                    Log.e("mImageCaptureUri", "");
                    mImageCaptureUri = data.getData();
                    Log.e("mImageCaptureUri", mImageCaptureUri + "");
                    filepath = new File(getRealPathFromURI(mYesOrNo, mImageCaptureUri));

                    //image Compression
                    MApplication.Imagecompression(filepath);

                    Log.e("filepath", filepath + "");
                    if (MApplication.chooseFlag) {

                        uri = MApplication.decodeFileConvertUri(YesOrNo.this, filepath);
                        MApplication.setString(mYesOrNo, "imageQuestion1", uri.toString());
                        MApplication.setString(mYesOrNo, "imageFilePathQuestion1", filepath.toString());
                        if (!MApplication.getString(mYesOrNo, "imageQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mYesOrNo, "imageQuestion2")));
                        }

                        choose_file_path = filepath;
                        // imgTask.executeUpload(filepath, "image", "");
                        System.out.println("New date Fount" + uri);

                        imageChoose.setImageURI(uri);
                    } else {
                        uri1 = MApplication.decodeFileConvertUri(YesOrNo.this, filepath);
                        MApplication.setString(mYesOrNo, "imageFilePathQuestion2", filepath.toString());
                        MApplication.setString(mYesOrNo, "imageQuestion2", uri1.toString());
                        if (!MApplication.getString(mYesOrNo, "imageQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mYesOrNo, "imageQuestion1")));
                        }
                        imageAdditional.setImageURI(uri1);
                        imageChoose.setImageURI(uri);

                        //imgTask.executeUpload(filepath, "image", "");
                        additinal_file_path = filepath;
                    }
                }
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    try {
                        mImageCaptureUri = data.getData();
                        filepath = new File(MApplication.getPath(mYesOrNo, mImageCaptureUri));

                        //image Compression
                        MApplication.Imagecompression(filepath);

                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    /*Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(mImageCaptureUri) // input image src
                            .withOutput(Uri.parse("file://" + filepath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/


                    if (MApplication.chooseFlag) {

                        uri = MApplication.decodeFileConvertUri(YesOrNo.this, filepath);
                        MApplication.setString(mYesOrNo, "imageQuestion1", uri.toString());
                        MApplication.setString(mYesOrNo, "imageFilePathQuestion1", filepath.toString());
                        if (!MApplication.getString(mYesOrNo, "imageQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mYesOrNo, "imageQuestion2")));
                        }

                        choose_file_path = filepath;
                        // imgTask.executeUpload(filepath, "image", "");
                        System.out.println("New date Fount" + uri);

                        imageChoose.setImageURI(uri);
                    } else {
                        uri1 = MApplication.decodeFileConvertUri(YesOrNo.this, filepath);
                        MApplication.setString(mYesOrNo, "imageFilePathQuestion2", filepath.toString());
                        MApplication.setString(mYesOrNo, "imageQuestion2", uri1.toString());
                        if (!MApplication.getString(mYesOrNo, "imageQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mYesOrNo, "imageQuestion1")));
                        }
                        imageAdditional.setImageURI(uri1);
                        imageChoose.setImageURI(uri);

                        //imgTask.executeUpload(filepath, "image", "");
                        additinal_file_path = filepath;
                    }
                }
                break;


            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    filepath = new File(MApplication.getRealPathFromURI(mYesOrNo, imageFileUri));

                    //image Compression
                    MApplication.Imagecompression(filepath);



                    /*Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(imageFileUri) // input image src
                            .withOutput(Uri.parse("file://" + filepath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/

                    if (MApplication.chooseFlag) {

                        uri = MApplication.decodeFileConvertUri(YesOrNo.this, filepath);
                        MApplication.setString(mYesOrNo, "imageQuestion1", uri.toString());
                        MApplication.setString(mYesOrNo, "imageFilePathQuestion1", filepath.toString());
                        if (!MApplication.getString(mYesOrNo, "imageQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mYesOrNo, "imageQuestion2")));
                        }

                        choose_file_path = filepath;
                        // imgTask.executeUpload(filepath, "image", "");
                        System.out.println("New date Fount" + uri);

                        imageChoose.setImageURI(uri);
                    } else {
                        uri1 = MApplication.decodeFileConvertUri(YesOrNo.this, filepath);
                        MApplication.setString(mYesOrNo, "imageFilePathQuestion2", filepath.toString());
                        MApplication.setString(mYesOrNo, "imageQuestion2", uri1.toString());
                        if (!MApplication.getString(mYesOrNo, "imageQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mYesOrNo, "imageQuestion1")));
                        }
                        imageAdditional.setImageURI(uri1);
                        imageChoose.setImageURI(uri);

                        //imgTask.executeUpload(filepath, "image", "");
                        additinal_file_path = filepath;
                    }


                }
                break;
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

    private void choosePic() {
        listDialog = new Dialog(mYesOrNo);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        listDialog.setContentView(R.layout.custom_dialog_adapter);
        ListView list =  listDialog.findViewById(R.id.component_list);
        String[] cameraOptions = new String[]{"Take Photo", "Choose from gallery", "Cancel"};
        CustomDialogAdapter adapter = new CustomDialogAdapter(this, cameraOptions);
        list.setAdapter(adapter);
        listDialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                      takePictureIntent();
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,
                                "Complete ACTION using"), 10);
                        listDialog.cancel();
                        break;
                    case 2:
                        listDialog.cancel();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Take picture intent.
     *
     * @param
     */
    public void takePictureIntent() {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File folder = new File(Environment.getExternalStorageDirectory() + "/"
                + getResources().getString(R.string.app_name));

        if (!folder.exists()) {
            folder.mkdir();
        }
        final Calendar c = Calendar.getInstance();
        String new_Date = c.get(Calendar.DAY_OF_MONTH) + "-"
                + ((c.get(Calendar.MONTH)) + 1) + "-" + c.get(Calendar.YEAR)
                + " " + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE)
                + "-" + c.get(Calendar.SECOND);
        choosenPhoto = String.format(Environment.getExternalStorageDirectory() + "/"
                + getResources().getString(R.string.app_name)
                + "/%s.jpeg", "profilepic(" + new_Date + ")");
        File photo = new File(choosenPhoto);
        imageFileUri = Uri.fromFile(photo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(intent, 11);
        listDialog.cancel();
    }


    /**
     * Sending the request and getting the response using the method
     */
    private void createPollYesOrNO() {
        if (MApplication.isNetConnected(mYesOrNo)) {
            if (!mCategory.isEmpty()) {
                categoryId = MApplication.getString(mYesOrNo, com.contus.app.Constants.CATEGORY_ID);
                Log.e("categoryId", categoryId + "");
                //mContact = mCategory.toString().replaceAll(getString(R.string.regex_for_posts), "");
                MApplication.materialdesignDialogStart(mYesOrNo);
                /*Request and response in retrofit**/

/*
                CreatePollRestClient.getInstance().postCreatePoll(new String("save_userpolls"), new String(userId), new String("users"), new String("1"), new TypedFile("image*//*
                 */
                /*", question1), new TypedFile("image*//*
                 */
/*", question2), new String(question), new String("Yes"), new String("No"), new String(""), new String(""), new String(categoryId), mContact, mGroupId, mContactsId
                        , new Callback<CreatePollResponseModel>() {

                            @Override
                            public void success(CreatePollResponseModel createResponseModel, Response response) {
                                if (createResponseModel.getSuccess().equals("1")) {
                                    finish();
                                }
                                Toast.makeText(mYesOrNo, createResponseModel.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                                MApplication.materialdesignDialogStop();
                            }


                            @Override
                            public void failure(RetrofitError retrofitError) {
                                */
                /*Progressive bar stop**//*

                                googleNow.progressiveStop();
                                */
                /*Visiblity gone**//*

                                googleNow.setVisibility(View.GONE);
                                */
                /*Retrofit error description**//*

                                String errorDescription = retrofitError.getMessage();

                                switch (retrofitError.getKind()) {
                                    case HTTP:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.http_error);
                                        break;
                                    case NETWORK:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_connecting_error);
                                        break;
                                    case CONVERSION:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_passing_data);
                                        break;
                                    case UNEXPECTED:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_unexpected);
                                        break;
                                    default:
                                        break;
                                }
                                Toast.makeText(mYesOrNo, errorDescription,
                                        Toast.LENGTH_SHORT).show();

                                MApplication.materialdesignDialogStop();
                            }

                        });
*/

                String CurrentString = image_addition_url;
                String[] separated = CurrentString.split(getString(R.string.regex_com));
                CurrentString = separated[1];


                String chooseurl = image_choose_url;
                String[] separated1 = chooseurl.split(getString(R.string.regex_com));
                chooseurl = separated1[1];


                CreatePollRestClient.getInstance().postCreatePoll_with_url("save_userpolls", userId, userType, "1",
                        chooseurl, CurrentString, question, "Yes", "No", "", "", categoryId, mContact, mGroupId, mContactsId
                        , new Callback<CreatePollResponseModel>() {

                            @Override
                            public void success(CreatePollResponseModel createResponseModel, Response response) {
                                if (createResponseModel.getSuccess().equals("1")) {
                                    finish();
                                }
                                Toast.makeText(mYesOrNo, createResponseModel.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                                MApplication.materialdesignDialogStop();
                            }


                            @Override
                            public void failure(RetrofitError retrofitError) {
                                /*Progressive bar stop**/
                                googleNow.progressiveStop();
                                /*Visibility gone**/
                                googleNow.setVisibility(View.GONE);
                                /*Retrofit error description**/
                                String errorDescription = retrofitError.getMessage();

                                switch (retrofitError.getKind()) {
                                    case HTTP:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.http_error);
                                        break;
                                    case NETWORK:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_connecting_error);
                                        break;
                                    case CONVERSION:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_passing_data);
                                        break;
                                    case UNEXPECTED:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_unexpected);
                                        break;
                                    default:
                                        break;
                                }
                                Toast.makeText(mYesOrNo, errorDescription,
                                        Toast.LENGTH_SHORT).show();

                                MApplication.materialdesignDialogStop();
                            }

                        });


            } else {
                Toast.makeText(mYesOrNo, mYesOrNo.getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Sending the request and getting the response using the method
     */
    private void createPollYesOrNOPollQuestion2() {
        if (MApplication.isNetConnected(mYesOrNo)) {
            if (!mCategory.isEmpty()) {
                categoryId = MApplication.getString(mYesOrNo, com.contus.app.Constants.CATEGORY_ID);
                Log.e("categoryId", categoryId + "");
                //mContact = mCategory.toString().replaceAll(getString(R.string.regex_for_posts), "");
                MApplication.materialdesignDialogStart(mYesOrNo);

                String CurrentString = image_addition_url;
                String[] separated = CurrentString.split(getString(R.string.regex_com));
                CurrentString = separated[1];

                /*Request and response in retrofit**/
                CreatePollRestClient.getInstance().postCreatePollPollQuestion2("save_userpolls", userId, userType,
                        "1", "", CurrentString, question, "Yes", "No", "",
                        "", categoryId, mContact, mGroupId, mContactsId
                        , new Callback<CreatePollResponseModel>() {

                            @Override
                            public void success(CreatePollResponseModel createResponseModel, Response response) {
                                if (createResponseModel.getSuccess().equals("1")) {
                                    finish();
                                }
                                Toast.makeText(mYesOrNo, createResponseModel.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                                MApplication.materialdesignDialogStop();
                            }


                            @Override
                            public void failure(RetrofitError retrofitError) {
                                /*Progressive bar stop**/
                                googleNow.progressiveStop();
                                /*Visibility gone**/
                                googleNow.setVisibility(View.GONE);
                                /*Retrofit error description**/
                                String errorDescription = retrofitError.getMessage();

                                switch (retrofitError.getKind()) {
                                    case HTTP:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.http_error);
                                        break;
                                    case NETWORK:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_connecting_error);
                                        break;
                                    case CONVERSION:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_passing_data);
                                        break;
                                    case UNEXPECTED:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_unexpected);
                                        break;
                                    default:
                                        break;
                                }
                                Toast.makeText(mYesOrNo, errorDescription,
                                        Toast.LENGTH_SHORT).show();

                                MApplication.materialdesignDialogStop();
                            }

                        });

            } else {
                Toast.makeText(mYesOrNo, mYesOrNo.getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollYesOrNOQuestion1() {
        if (MApplication.isNetConnected(mYesOrNo)) {
            Log.e("question1", "question1" + "");
            if (!mCategory.isEmpty()) {
                categoryId = MApplication.getString(mYesOrNo, com.contus.app.Constants.CATEGORY_ID);
                Log.e("categoryId", categoryId + "");
                //mContact = mCategory.toString().replaceAll(getString(R.string.regex_for_posts), "");
                MApplication.materialdesignDialogStart(mYesOrNo);
                /*Request and response in retrofit**/

                String CurrentString = image_choose_url;
                String[] separated = CurrentString.split(getString(R.string.regex_com));
                CurrentString = separated[1];


                CreatePollRestClient.getInstance().postCreatePollPollQuestion1("save_userpolls", userId, userType,
                        "1", CurrentString, "", question, "Yes", "No", "",
                        "", categoryId, mContact, mGroupId, mContactsId
                        , new Callback<CreatePollResponseModel>() {

                            @Override
                            public void success(CreatePollResponseModel createResponseModel, Response response) {
                                if (createResponseModel.getSuccess().equals("1")) {
                                    finish();
                                }
                                Toast.makeText(mYesOrNo, createResponseModel.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                                MApplication.materialdesignDialogStop();
                            }


                            @Override
                            public void failure(RetrofitError retrofitError) {
                                /*Progressive bar stop**/
                                googleNow.progressiveStop();
                                /*Visiblity gone**/
                                googleNow.setVisibility(View.GONE);
                                /*Retrofit error description**/
                                String errorDescription = retrofitError.getMessage();

                                switch (retrofitError.getKind()) {
                                    case HTTP:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.http_error);
                                        break;
                                    case NETWORK:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_connecting_error);
                                        break;
                                    case CONVERSION:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_passing_data);
                                        break;
                                    case UNEXPECTED:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_unexpected);
                                        break;
                                    default:
                                        break;
                                }
                                Toast.makeText(mYesOrNo, errorDescription,
                                        Toast.LENGTH_SHORT).show();

                                MApplication.materialdesignDialogStop();
                            }

                        });
            } else {
                Toast.makeText(mYesOrNo, mYesOrNo.getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Sending the request and getting the response using the method
     */
    private void createPollYesOrNOEmpty() {
        if (MApplication.isNetConnected(mYesOrNo)) {
            if (!mCategory.isEmpty()) {
                //mContact = mCategory.toString().replaceAll(getString(R.string.regex_for_posts), "");
                googleNow.setVisibility(View.GONE);
                categoryId = MApplication.getString(mYesOrNo, com.contus.app.Constants.CATEGORY_ID);
                Log.e("categoryId", categoryId + "");
                /*Progress bar start**/
                MApplication.materialdesignDialogStart(mYesOrNo);
                /*Request and response in retrofit**/
                CreatePollRestClient.getInstance().postCreatePollPollEmpty("save_userpolls", userId, userType,
                        "1", "", "", question, "Yes", "No", "",
                        "", categoryId, mContact, mGroupId, mContactsId
                        , new Callback<CreatePollResponseModel>() {

                            @Override
                            public void success(CreatePollResponseModel createResponseModel, Response response) {
                                if (createResponseModel.getSuccess().equals("1")) {
                                    finish();
                                }
                                Toast.makeText(mYesOrNo, createResponseModel.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                                /*Progressive bar stop**/
                                googleNow.progressiveStop();
                                /*Visibility gone**/
                                googleNow.setVisibility(View.GONE);
                                MApplication.materialdesignDialogStop();
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                /*Progressive bar stop**/
                                googleNow.progressiveStop();
                                /*Visibility gone**/
                                googleNow.setVisibility(View.GONE);
                                /*Retrofit error description**/
                                String errorDescription = retrofitError.getMessage();

                                switch (retrofitError.getKind()) {
                                    case HTTP:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.http_error);
                                        break;
                                    case NETWORK:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_connecting_error);
                                        break;
                                    case CONVERSION:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_passing_data);
                                        break;
                                    case UNEXPECTED:
                                        errorDescription = mYesOrNo.getResources().getString(R.string.error_unexpected);
                                        break;
                                    default:
                                        break;
                                }
                                Toast.makeText(mYesOrNo, errorDescription,
                                        Toast.LENGTH_SHORT).show();
                                MApplication.materialdesignDialogStop();

                            }

                        });
            } else {
                Toast.makeText(mYesOrNo, mYesOrNo.getResources().getString(R.string.check_internet_connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Activity activity, Uri uri) {
        String result;
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {

            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();


        }
        return result;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", imageFileUri);
    }

  /*  @Override
    protected void onDestroy() {
        super.onDestroy();
        MApplication.materialdesignDialogStop();
    }
*/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageFileUri = savedInstanceState.getParcelable("file_uri");
    }

    //on completion of image upload
    @Override
    public void onTaskCompleted(URL url, String type, String encodedImg, int fileSize) {


        if (isimagechoose) {
            isimagechoose = false;
            if(url!=null)
            {
                image_choose_url = url.toString();
                Log.v("image_choose_url", url.toString());
            }

            imgTask.executeUpload(additinal_file_path, "image", "", "POLLS/");
        } else {
            if(url!=null)
            {
                image_addition_url = url.toString();
                Log.v("image_addition_url", url.toString());
            }
            googleNow.setVisibility(View.GONE);
            googleNow.progressiveStop();

            createPollSubmit();

        }


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
                        // else any one or both the permissions are not granted
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                        choosePic();
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
                                                    hasPermissions(YesOrNo.this, PERMISSIONS);
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
/*    private void showPopUp(final ArrayList<GroupsNameObject> groupsList)
    {
        DatabaseHelper dbhelper = new DatabaseHelper(mYesOrNo);
        dbhelper.deleteGroupList();

        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(YesOrNo.this, R.style.AppCompatAlertDialogStyle);
        helpBuilder.setTitle("Select Group(s)");

        LayoutInflater inflater = getLayoutInflater();
        final View PopupLayout = inflater.inflate(R.layout.groups_layout, null);
        helpBuilder.setView(PopupLayout);

        final AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

        ListView lv_group_list=   PopupLayout.findViewById(R.id.lv_group_list);
        TextView tv_done = PopupLayout.findViewById(R.id.tv_done);
        List mylist = new ArrayList();
        for(int i=0;i<groupsList.size();i++)
        {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", groupsList.get(i).getGroupName());
            mylist.add(map);
        }
        GroupCheckListAdapter sd = new GroupCheckListAdapter(mYesOrNo, groupsList);
        lv_group_list.setAdapter(sd);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatabaseHelper dbhelper = new DatabaseHelper(mYesOrNo);
               ArrayList<GroupsNameObject> list= new ArrayList<>();
               list = dbhelper.getAllGroupList();
               if(list.size()>0)
               {
                   for (int i=0; i<list.size(); i++)
                   {
                       if(i==0)
                       {
                           mContact = String.valueOf(list.get(i).getGroupId());
                       }
                       else {
                           mContact = mContact+","+ list.get(i).getGroupId();
                       }
                   }
               }
               // Toast.makeText(mYesOrNo, ""+userType, Toast.LENGTH_SHORT).show();  helpDialog.dismiss();
                helpDialog.dismiss();
            }


        });

    }*/
}


