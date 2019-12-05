package com.contusfly.createpoll;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
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
import com.contus.mypolls.ImageModel;
import com.contus.responsemodel.CreatePollResponseModel;
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
import retrofit.mime.TypedFile;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by user on 7/15/2015.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class PhotoComparison extends Activity implements OnTaskCompleted {

    private Activity mPhotoComparison;
    private EditText txtCategory;
    private SmoothProgressBar googleNow;
    Uri mPhotoComaprisonImageUri;
    File photoComparisonFilePath;
    private SimpleDraweeView imageChoose;
    private SimpleDraweeView imageAdditional;
    private Uri fileUri;
    private SimpleDraweeView option1;
    private SimpleDraweeView option2;
    private SimpleDraweeView option3;
    private SimpleDraweeView option4;
    private EditText editQuestion;
    private String userId;
    private File question1;
    private File question2;
    private File answer1;
    private File answer2;
    private File answer3;
    private File answer4;
    private ArrayList<String> contacts;
    String category;
    private String question;
    private Dialog listDialog;
    private String mContact="Public";
    private String clickAction;
    String choosenPhoto;
    private Uri imageFileUri;
    private File filepath;
    private Bitmap bitmap;
    private ArrayList<String> listGroupid;
    private ArrayList<String> mArrayList;
    private ArrayList<String> mCategory;
    private ArrayList<String> mGroupName;
    private ArrayList<String> mContactName;
    TextView txtTitle;
    private TextView txtPublic;
    private TextView txtGroup;
    private TextView txtContacts;
    private ArrayList<String> listContactId;
    private String mGroupId;
    private String mContactsId;
    private String categoryId;
    String action="getgroupsapi";
    Uri uri1;
    Uri uri2;
    Uri uri3;
    Uri uri4;
    Uri uri5;
    Uri uri;
    String contactName;
    ArrayList<ImageModel> Imageslist;
    int listposition;

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private final int PERMISSION_ALL = 1;
    ArrayList<GroupsNameObject> groupsList;
    ImageUploadS3 imgTask;
    String image_choose_url="",image_addition_url="",option1_url="",option2_url="",option3_url="",option4_url="";
    private Button create;
    boolean isgroupPoll=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fresco initialization
        Fresco.initialize(this);
        setContentView(R.layout.activity_photo_comparison);
        init();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        serviceCallForGroups();
    }

    private void init() {
        /*Initializing the activity**/
        mPhotoComparison = this;
        txtCategory =  findViewById(R.id.txtCategory);
        googleNow =  findViewById(R.id.google_now);
        editQuestion =  findViewById(R.id.editQuestion);
        userId = MApplication.getString(mPhotoComparison, Constants.USER_ID);
        AdView mAdView =  findViewById(R.id.adView);
        imageChoose =  findViewById(R.id.choose);
        imageAdditional =  findViewById(R.id.ChooseAdditional);
        option1 =  findViewById(R.id.option1);
        option2 =  findViewById(R.id.option2);
        option3 =  findViewById(R.id.option3);
        option4 =  findViewById(R.id.option4);
        txtTitle =  findViewById(R.id.title);
        txtPublic =  findViewById(R.id.txtPublic);
        txtGroup =  findViewById(R.id.txtGroup);
        txtContacts =  findViewById(R.id.txtContacts);
        create      = findViewById(R.id.create);

        mCategory = new ArrayList<>();
        listGroupid = new ArrayList<>();
        mArrayList = new ArrayList<>();
        mContactName = new ArrayList<>();
        mGroupName = new ArrayList<>();

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Light.ttf");
        txtTitle.setTypeface(face);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);
        categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);

        //Uploading an image in S3 bucket
        imgTask = new ImageUploadS3(getApplicationContext());
        //call back method
        imgTask.uplodingCallback(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onClick(final View clickedView) {

        if (clickedView.getId() == R.id.imagBackArrow) {
            finish();
        } else if (clickedView.getId() == R.id.relativeLayout) {
           /* Intent a = new Intent(PhotoComparison.this, CreatePollOptions.class);
            startActivity(a);*/
        } else if (clickedView.getId() == R.id.create) {
            checkValidation();
        } else if (clickedView.getId() == R.id.txtCategory) {
            YesOrNo.CategoryPollRequest(googleNow, mPhotoComparison, txtCategory);
        } else if (clickedView.getId() == R.id.choose) {
            clickAction = "1";
            MApplication.setString(PhotoComparison.this, "clikaction", clickAction);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!hasPermissions(PhotoComparison.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(PhotoComparison.this, PERMISSIONS, PERMISSION_ALL);
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

        } else if (clickedView.getId() == R.id.option1) {
            clickAction = "3";
            MApplication.setString(PhotoComparison.this, "clikaction", clickAction);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!hasPermissions(PhotoComparison.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(PhotoComparison.this, PERMISSIONS, PERMISSION_ALL);
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

        } else if (clickedView.getId() == R.id.option2) {
            if (!MApplication.getString(mPhotoComparison, "filePathOption1").isEmpty()) {
                clickAction = "4";
                MApplication.setString(PhotoComparison.this, "clikaction", clickAction);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(PhotoComparison.this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(PhotoComparison.this, PERMISSIONS, PERMISSION_ALL);
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

            } else
                Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_option1), Toast.LENGTH_SHORT).show();

        } else if (clickedView.getId() == R.id.option3) {
            if (!MApplication.getString(mPhotoComparison, "filePathOption2").isEmpty()) {
                clickAction = "5";
                MApplication.setString(PhotoComparison.this, "clikaction", clickAction);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(PhotoComparison.this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(PhotoComparison.this, PERMISSIONS, PERMISSION_ALL);
                      //  Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
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

            } else
                Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_option2), Toast.LENGTH_SHORT).show();


        } else if (clickedView.getId() == R.id.option4) {
            if (!MApplication.getString(mPhotoComparison, "filePathOption3").isEmpty()) {
                clickAction = "6";
                MApplication.setString(PhotoComparison.this, "clikaction", clickAction);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(PhotoComparison.this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(PhotoComparison.this, PERMISSIONS, PERMISSION_ALL);
                      //  Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
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

            } else {
                Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_option3), Toast.LENGTH_SHORT).show();
            }

        } else if (clickedView.getId() == R.id.ChooseAdditional) {
            if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion1").isEmpty()) {
                clickAction = "2";
                MApplication.setString(PhotoComparison.this, "clikaction", clickAction);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(PhotoComparison.this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(PhotoComparison.this, PERMISSIONS, PERMISSION_ALL);
                      //  Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
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

            } else {
                Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_question1), Toast.LENGTH_SHORT).show();
            }


        }
        else if(clickedView.getId() == R.id.txtGroup)  {
            mContact="";
            if(AppConstents.GROUPlIST.size()>0)
            {
                Intent intent=new Intent(PhotoComparison.this, GroupSelection.class);
                intent.putExtra("DATA",AppConstents.GROUPlIST);
                startActivityForResult(intent,20);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please wait data loading",Toast.LENGTH_SHORT).show();
            }




        }
        else if(clickedView.getId() == R.id.txtPublic) {
            txtGroup.setTextColor(mPhotoComparison.getResources().getColor(R.color.grey_color));
            txtPublic.setTextColor(mPhotoComparison.getResources().getColor(R.color.blue_color));
            mContact = "Public";
            isgroupPoll=false;
        }
    }

    private void checkValidation() {
        String mQuestion1 = MApplication.getString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion1");
        String mQuestion2 = MApplication.getString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion2");
        String mAnswer1 = MApplication.getString(mPhotoComparison, "filePathOption1");
        String mAnswer2 = MApplication.getString(mPhotoComparison, "filePathOption2");
        String mAnswer3 = MApplication.getString(mPhotoComparison, "filePathOption3");
        String mAnswer4 = MApplication.getString(mPhotoComparison, "filePathOption4");
        category = txtCategory.getText().toString().trim();
        question = editQuestion.getText().toString().trim();
        if (TextUtils.isEmpty(category)) {
            Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_your_category),
                    Toast.LENGTH_SHORT).show();
            txtCategory.requestFocus();
        } else if (TextUtils.isEmpty(question)) {
            Toast.makeText(mPhotoComparison, getResources().getString(R.string.enter_enter_question),
                    Toast.LENGTH_SHORT).show();
            editQuestion.requestFocus();
        } else if (question.length() < 2) {
            Toast.makeText(mPhotoComparison, getResources().getString(R.string.enter_valid_question),
                    Toast.LENGTH_SHORT).show();
            editQuestion.requestFocus();
        } else if (mAnswer1.isEmpty()) {
            Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_option1),
                    Toast.LENGTH_SHORT).show();
        } else if (mAnswer2.isEmpty()) {
            Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_option2),
                    Toast.LENGTH_SHORT).show();
        } else {

            if(isgroupPoll)
            {
                create.setEnabled(false);
                //deleteThePoll(mClickPosition);//This method is used to delete the poll
                createPollSubmit();
            }
            else {
                //Creates a builder for an alert dialog that uses the default alert dialog theme.
                AlertDialog.Builder builder;
                //Dialog text visible By NIKITA
                if (Build.VERSION.SDK_INT >= 21)
                    builder = new AlertDialog.Builder(PhotoComparison.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
                else
                    builder = new AlertDialog.Builder(PhotoComparison.this);
                //set message
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

                                create.setEnabled(false);
                                //deleteThePoll(mClickPosition);//This method is used to delete the poll
                                createPollSubmit();
                                dialog.dismiss();
                            }
                        });
                //create
                builder.create().show();
            }
        }
    }
public void createPollSubmit(){


    question1 = new File(MApplication.getString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion1"));
    question2 = new File(MApplication.getString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion2"));
    answer1 = new File(MApplication.getString(mPhotoComparison, "filePathOption1"));
    answer2 = new File(MApplication.getString(mPhotoComparison, "filePathOption2"));
    answer3 = new File(MApplication.getString(mPhotoComparison, "filePathOption3"));
    answer4 = new File(MApplication.getString(mPhotoComparison, "filePathOption4"));
    Log.e("question1", question1 + "");
    Log.e("question2", question2 + "");
    Log.e("answer1", answer1 + "");
    Log.e("answer2", answer2 + "");
    Log.e("answer3", answer3 + "");
    Log.e("answer4", answer4 + "");

    Imageslist= new ArrayList<>();
    ImageModel model = new ImageModel();
    model.setType("question1");

    model.setFile(question1);
    Imageslist.add(model);
    ImageModel model1 = new ImageModel();
    model1.setType("question2");
    model1.setFile(question2);
    Imageslist.add(model1);
    ImageModel model2 = new ImageModel();
    model2.setType("answer1");
    model2.setFile(answer1);
    Imageslist.add(model2);
    ImageModel model3 = new ImageModel();
    model3.setType("answer2");
    model3.setFile(answer2);
    Imageslist.add(model3);
    ImageModel model4 = new ImageModel();
    model4.setType("answer3");
    model4.setFile(answer3);
    Imageslist.add(model4);
    ImageModel model5 = new ImageModel();
    model5.setType("answer4");
    model5.setFile(answer4);
    Imageslist.add(model5);

    listposition=0;

    googleNow.setVisibility(View.VISIBLE);
    googleNow.progressiveStart();
    upLoad(0);




   /* if (!question1.isFile() && !question2.isFile()) {
        if (answer1.isFile() && answer2.isFile() && answer3.isFile() && answer4.isFile()) {
            createPollMultipelImagesAnswerNotEmpty();
        } else if (answer1.isFile() && answer2.isFile() && answer3.isFile() && !answer4.isFile()) {
            createPollMultipelImagesAnswer4Empty();
        } else if (answer1.isFile() && answer2.isFile() && !answer3.isFile() && answer4.isFile()) {
            createPollMultipelImagesAnswer3Empty();
        } else if (answer1.isFile() && answer2.isFile() && !answer3.isFile() && !answer4.isFile()) {
            Log.e("click", "click");
            createPollMultipelImagesEmpty();
        }
    } else {
        if (question1.isFile() || question2.isFile()) {
                   *//* if (question1.isFile()&& !question2.isFile() && !answer3.isFile() && answer4.isFile()) {
                        createPollMultipelQuestion1Answer4();
                    }*//*

            if (question1.isFile() && !question2.isFile() && answer3.isFile() && !answer4.isFile()) {
                createPollMultipelQuestion1Answer3();
            } else if (question1.isFile() && !question2.isFile() && answer3.isFile() && answer4.isFile()) {
                createPollMultipelQuestion1();
            } else if (question1.isFile() && !question2.isFile() && !answer3.isFile() && !answer4.isFile()) {
                createPollMultipelQuestion1OptionEmpty();
            } else if (question1.isFile() && question2.isFile() && !answer3.isFile() && !answer4.isFile()) {
                createPollMultipelImagesQuestionEmpty();
            } else if (question1.isFile() && question2.isFile() && answer3.isFile() && !answer4.isFile()) {
                createMultipleImagesOption3();
            } else if (question1.isFile() && question2.isFile() && answer3.isFile() && !answer4.isFile()) {
                createMultipleImagesOption3();
            } else {
                createPollMultipelImages();
            }

                   *//* if (!mQuestion1.isEmpty()&& mQuestion2.isEmpty() && mAnswer3.isEmpty() && !mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1Answer4();
                    } else if (!mQuestion1.isEmpty() && mQuestion2.isEmpty() && !mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1Answer3();
                    } else if (!mQuestion1.isEmpty() && mQuestion2.isEmpty() && !mAnswer3.isEmpty() && !mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1();
                    } else if (!mQuestion1.isEmpty() && mQuestion2.isEmpty() && mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1OptionEmpty();
                    } else if (!mQuestion1.isEmpty() && !mQuestion2.isEmpty() && mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                        createPollMultipelImagesQuestionEmpty();
                    } else if (!mQuestion1.isEmpty() && !mQuestion2.isEmpty() && !mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                        Log.e("question1",question1+"");
                        createMultipleImagesOption3();
                    } else if (!mQuestion1.isEmpty() && !mQuestion2.isEmpty() && mAnswer3.isEmpty() && !mAnswer4.isEmpty()) {
                        createMultipleImagesOption4();
                    } else {
                        createPollMultipelImages();
                    }    *//*
        }
    }

            *//*if(mQuestion1!=null||mQuestion2!=null){
                if(mQuestion1==null&&mQuestion2!=null) {
                    Toast.makeText(mPhotoComparison, getResources().getString(R.string.select_question1), Toast.LENGTH_SHORT).show();
                }else {
                    if (!mQuestion1.isEmpty()&& mQuestion2.isEmpty() && mAnswer3.isEmpty() && !mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1Answer4();
                    } else if (!mQuestion1.isEmpty() && mQuestion2.isEmpty() && !mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1Answer3();
                    } else if (!mQuestion1.isEmpty() && mQuestion2.isEmpty() && !mAnswer3.isEmpty() && !mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1();
                    } else if (!mQuestion1.isEmpty() && mQuestion2.isEmpty() && mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                        createPollMultipelQuestion1OptionEmpty();
                    } else if (!mQuestion1.isEmpty() && !mQuestion2.isEmpty() && mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                        createPollMultipelImagesQuestionEmpty();
                    } else if (!mQuestion1.isEmpty() && !mQuestion2.isEmpty() && !mAnswer3.isEmpty() && mAnswer4.isEmpty()) {
                      Log.e("question1",question1+"");
                        createMultipleImagesOption3();
                    } else if (!mQuestion1.isEmpty() && !mQuestion2.isEmpty() && mAnswer3.isEmpty() && !mAnswer4.isEmpty()) {
                        createMultipleImagesOption4();
                    } else {
                        createPollMultipelImages();
                    }
                }
            }else{
                if(!mAnswer1.isEmpty()&&!mAnswer2.isEmpty()&&!mAnswer3.isEmpty()&&!mAnswer4.isEmpty()) {
                    createPollMultipelImagesAnswerNotEmpty();
                }else if(!mAnswer1.isEmpty()&&!mAnswer2.isEmpty()&&!mAnswer3.isEmpty()&&mAnswer4.isEmpty()){
                    createPollMultipelImagesAnswer4Empty();
                }else if(!mAnswer1.isEmpty()&&!mAnswer2.isEmpty()&&mAnswer3.isEmpty()&&!mAnswer4.isEmpty()){
                    createPollMultipelImagesAnswer3Empty();
                }else if(!mAnswer1.isEmpty()&&!mAnswer2.isEmpty()&&mAnswer3.isEmpty()&&mAnswer4.isEmpty()){
                    createPollMultipelImagesEmpty();
                }
            }*/


}

    private void upLoad(int position) {


        if(Imageslist.size()>0)
        {

                imgTask.executeUpload(Imageslist.get(position).getFile(), "image", "","POLLS/");

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCategory = MApplication.loadStringArray(mPhotoComparison, mCategory, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO, com.contus.app.Constants.ACTIVITY_CATEGORY_INFO_SIZE);
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
        if (!db.getAllGroupDetails(1).isEmpty()) {
            for (int i = 0; db.getAllGroupDetails(1).size() > i; i++) {
                txtGroup.setVisibility(View.VISIBLE);
                if (!listGroupid.contains(db.getAllGroupDetails(1).get(i).getId())) {
                    mGroupName.add(db.getAllGroupDetails(1).get(i).getName());
                    listGroupid.add(db.getAllGroupDetails(1).get(i).getId());
                }
                contactName = mGroupName.toString().replaceAll("[\\s\\[\\]]", "");
            }
            contactName = mGroupName.toString().replaceAll("[\\s\\[\\]]", "");
            txtGroup.setText(contactName);
            mGroupId = listGroupid.toString().replaceAll("[\\s\\[\\]]", "");
        } else {
            txtGroup.setVisibility(View.GONE);
        }

        if (!db.getAllContactsDetails(1).isEmpty()) {
            for (int i = 0; db.getAllContactsDetails(1).size() > i; i++) {
                txtContacts.setVisibility(View.VISIBLE);
                if (!mArrayList.contains(db.getAllContactsDetails(1).get(i).getId())) {
                    mContactName.add(db.getAllContactsDetails(1).get(i).getName());
                    mArrayList.add(db.getAllContactsDetails(1).get(i).getId());
                }
            }
            contactName = mContactName.toString().replaceAll("[\\s\\[\\]]", "");
            txtContacts.setText(contactName);
            mContactsId = mArrayList.toString().replaceAll("[\\s\\[\\]]", "");
        } else {
            txtContacts.setVisibility(View.GONE);
        }

        if(AppConstents.GROUPlIST.size()>0)
        {
            txtGroup.setVisibility(View.VISIBLE);
        }
        else
        {
            txtGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 20:

                if (resultCode == RESULT_OK) {
                    txtPublic.setTextColor(mPhotoComparison.getResources().getColor(R.color.grey_color));
                    txtGroup.setTextColor(mPhotoComparison.getResources().getColor(R.color.blue_color));
                    mContact = "";
                    isgroupPoll = true;

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

                    /*DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                    ArrayList<GroupsNameObject> list = new ArrayList<>();
                    list = dbhelper.getAllGroupList();
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i == 0) {
                                mContact = String.valueOf(list.get(i).getGroupId());
                            } else {
                                mContact = mContact + "," + list.get(i).getGroupId();
                            }
                        }
                    }*/
                }
                break;
            case 15:
                if (resultCode == RESULT_OK) {
                    mPhotoComaprisonImageUri = data.getData();
                    photoComparisonFilePath = new File(getRealPathFromURI(mPhotoComparison, mPhotoComaprisonImageUri));


                    //image Compression
                    MApplication.Imagecompression(photoComparisonFilePath);

                    if (MApplication.getString(PhotoComparison.this, "clikaction").equals("1")) {
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion1", photoComparisonFilePath.toString());
                        uri = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionQuestion1", uri.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        imageChoose.setImageURI(uri);
                        question1 = photoComparisonFilePath;

                        image_choose_url=photoComparisonFilePath.toString();
                       // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("2")) {
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion2", photoComparisonFilePath.toString());
                        uri1 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionQuestion2", uri1.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        imageAdditional.setImageURI(uri1);
                        question2 = photoComparisonFilePath;

                        image_addition_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("3")) {
                        MApplication.setString(mPhotoComparison, "filePathOption1", photoComparisonFilePath.toString());
                        uri2 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "option1", uri2.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        option1.setImageURI(uri2);

                        option1_url=photoComparisonFilePath.toString();
                       // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("4")) {
                        MApplication.setString(mPhotoComparison, "filePathOption2", photoComparisonFilePath.toString());
                        uri3 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        MApplication.setString(mPhotoComparison, "option2", uri3.toString());
                        option2.setImageURI(uri3);
                        option2_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("5")) {
                        MApplication.setString(mPhotoComparison, "filePathOption3", photoComparisonFilePath.toString());
                        uri4 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        MApplication.setString(mPhotoComparison, "option3", uri4.toString());
                        option3.setImageURI(uri4);
                        option3_url=photoComparisonFilePath.toString();
                       // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("6")) {
                        MApplication.setString(mPhotoComparison, "filePathOption4", photoComparisonFilePath.toString());
                        uri5 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        MApplication.setString(mPhotoComparison, "option4", uri5.toString());
                        option4.setImageURI(uri5);

                        option4_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    }
                }
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    Log.e("clickAction0", clickAction + "");
                    try {
                        mPhotoComaprisonImageUri = data.getData();
                        if(mPhotoComaprisonImageUri!=null)
                        {
                            photoComparisonFilePath = new File(MApplication.getPath(mPhotoComparison, mPhotoComaprisonImageUri));
                        }
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    //image Compression
                    MApplication.Imagecompression(photoComparisonFilePath);

                    /*Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(mPhotoComaprisonImageUri) // input image src
                            .withOutput(Uri.parse("file://" + photoComparisonFilePath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/


                    if (MApplication.getString(PhotoComparison.this, "clikaction").equals("1")) {
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion1", photoComparisonFilePath.toString());
                        uri = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionQuestion1", uri.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        imageChoose.setImageURI(uri);
                        question1 = photoComparisonFilePath;

                        image_choose_url=photoComparisonFilePath.toString();
                        // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("2")) {
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion2", photoComparisonFilePath.toString());
                        uri1 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionQuestion2", uri1.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        imageAdditional.setImageURI(uri1);
                        question2 = photoComparisonFilePath;

                        image_addition_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("3")) {
                        MApplication.setString(mPhotoComparison, "filePathOption1", photoComparisonFilePath.toString());
                        uri2 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "option1", uri2.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        option1.setImageURI(uri2);

                        option1_url=photoComparisonFilePath.toString();
                        // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("4")) {
                        MApplication.setString(mPhotoComparison, "filePathOption2", photoComparisonFilePath.toString());
                        uri3 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        MApplication.setString(mPhotoComparison, "option2", uri3.toString());
                        option2.setImageURI(uri3);
                        option2_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("5")) {
                        MApplication.setString(mPhotoComparison, "filePathOption3", photoComparisonFilePath.toString());
                        uri4 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        MApplication.setString(mPhotoComparison, "option3", uri4.toString());
                        option3.setImageURI(uri4);
                        option3_url=photoComparisonFilePath.toString();
                        // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("6")) {
                        MApplication.setString(mPhotoComparison, "filePathOption4", photoComparisonFilePath.toString());
                        uri5 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        MApplication.setString(mPhotoComparison, "option4", uri5.toString());
                        option4.setImageURI(uri5);

                        option4_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    }
                }
                break;


            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    photoComparisonFilePath = new File(getRealPathFromURI(mPhotoComparison, imageFileUri));

                    //image Compression
                    MApplication.Imagecompression(photoComparisonFilePath);

                    /*Intent newIntent1 = new AviaryIntent.Builder(this)
                            .setData(imageFileUri) // input image src
                            .withOutput(Uri.parse(Constants.pictureFile + filepath)) // output file
                            .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                            .withOutputSize(MegaPixels.Mp5) // output size
                            .withOutputQuality(90) // output quality
                            .build();
                    // start the activity
                    startActivityForResult(newIntent1, 15);*/


                    if (MApplication.getString(PhotoComparison.this, "clikaction").equals("1")) {
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion1", photoComparisonFilePath.toString());
                        uri = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionQuestion1", uri.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        imageChoose.setImageURI(uri);
                        question1 = photoComparisonFilePath;

                        image_choose_url=photoComparisonFilePath.toString();
                        // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("2")) {
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionFilePathQuestion2", photoComparisonFilePath.toString());
                        uri1 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "imagePhotoComparisionQuestion2", uri1.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        imageAdditional.setImageURI(uri1);
                        question2 = photoComparisonFilePath;

                        image_addition_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("3")) {
                        MApplication.setString(mPhotoComparison, "filePathOption1", photoComparisonFilePath.toString());
                        uri2 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        MApplication.setString(mPhotoComparison, "option1", uri2.toString());
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        option1.setImageURI(uri2);

                        option1_url=photoComparisonFilePath.toString();
                        // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("4")) {
                        MApplication.setString(mPhotoComparison, "filePathOption2", photoComparisonFilePath.toString());
                        uri3 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        MApplication.setString(mPhotoComparison, "option2", uri3.toString());
                        option2.setImageURI(uri3);
                        option2_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("5")) {
                        MApplication.setString(mPhotoComparison, "filePathOption3", photoComparisonFilePath.toString());
                        uri4 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option4").isEmpty()) {
                            option4.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option4")));
                        }
                        MApplication.setString(mPhotoComparison, "option3", uri4.toString());
                        option3.setImageURI(uri4);
                        option3_url=photoComparisonFilePath.toString();
                        // imgTask.executeUpload(filepath, "image", "","POLLS/");
                    } else if (MApplication.getString(PhotoComparison.this, "clikaction").equals("6")) {
                        MApplication.setString(mPhotoComparison, "filePathOption4", photoComparisonFilePath.toString());
                        uri5 = MApplication.decodeFileConvertUri(mPhotoComparison, photoComparisonFilePath);
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1").isEmpty()) {
                            imageChoose.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2").isEmpty()) {
                            imageAdditional.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "imagePhotoComparisionQuestion2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option1").isEmpty()) {
                            option1.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option1")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option2").isEmpty()) {
                            option2.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option2")));
                        }
                        if (!MApplication.getString(mPhotoComparison, "option3").isEmpty()) {
                            option3.setImageURI(Uri.parse(MApplication.getString(mPhotoComparison, "option3")));
                        }
                        MApplication.setString(mPhotoComparison, "option4", uri5.toString());
                        option4.setImageURI(uri5);

                        option4_url=photoComparisonFilePath.toString();
                        //imgTask.executeUpload(filepath, "image", "","POLLS/");
                    }
                }
                break;
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createMultipleImagesOption3() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
           // mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /**Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesOption3("save_userpolls", userId, "users", "3", new TypedFile("image*//*", question1), new TypedFile("image*//*", question2), question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer3), categoryId, mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {
                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            createPhotoComparisonResponse(createResponseModel);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                        }
                    });

        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void createPhotoComparisonResponse(CreatePollResponseModel createResponseModel) {
        if (createResponseModel.getSuccess().equals("1")) {
            finish();
        }
        Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                Toast.LENGTH_SHORT).show();
        MApplication.materialdesignDialogStop();
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createMultipleImagesOption4() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
           // mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /**Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesOption4(new String("save_userpolls"), new String(userId), "users", "3", new TypedFile("image*//*", question1), new TypedFile("image*//*", question2), question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer4), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            createPhotoComparisonResponse(createResponseModel);
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            googleNow.progressiveStop();
                            googleNow.setVisibility(View.GONE);
                        }
                    });
        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelQuestion1() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
            //mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            Log.e("categoryId", categoryId + "");
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /**Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestion1(new String("save_userpolls"), new String(userId), "users", "3", new TypedFile("image*//*", question1), question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer3), new TypedFile("image*//*", answer4), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();

                        }

                    });

        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelQuestion1OptionEmpty() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
         //   mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestion1OptionsEmpty("save_userpolls", userId, "users", "3", new TypedFile("image*//*", question1), question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), categoryId, mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }

                    });

        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelQuestion1Answer3() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
          //  mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            Log.e("categoryId", categoryId + "");
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestion1Answer3("save_userpolls", userId, "users", "3", new TypedFile("image*//*", question1), question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer3), categoryId, mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }
                    });
        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelQuestion1Answer4() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
         //   mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestion1Answer4("save_userpolls", userId, new String("users"), "3", new TypedFile("image*//*", question1), question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer4), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();

                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }


                    });
        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelImages() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
         //   mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);

            if(image_choose_url.length()<5)
            {
                Imageslist.get(0).setUrl("");
            }
            if(image_addition_url.length()<5)
            {
                Imageslist.get(1).setUrl("");
            }
            if(option1_url.length()<5)
            {
                Imageslist.get(2).setUrl("");
            }
            if(option2_url.length()<5)
            {
                Imageslist.get(4).setUrl("");
            }
            if(option3_url.length()<5)
            {
                Imageslist.get(4).setUrl("");
            }
            if(option4_url.length()<5)
            {
                Imageslist.get(5).setUrl("");
            }
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImages("save_userpolls", userId, "users", "3", Imageslist.get(0).getUrl(), Imageslist.get(1).getUrl(), question, Imageslist.get(2).getUrl(), Imageslist.get(3).getUrl(), Imageslist.get(4).getUrl(), Imageslist.get(5).getUrl(), categoryId, mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }

                    });
        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelImagesQuestionEmpty() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
           // mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateQuestion1Empty("save_userpolls", userId, "users", "3", new TypedFile("image*//*", question1), new TypedFile("image*//*", question2), new String(question), new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            MApplication.materialdesignDialogStop();
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }

                    });

        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelImagesEmpty() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
          //  mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestionAnswer("save_userpolls", userId, "users", "3", question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }
                    });
        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
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
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelImagesAnswer4Empty() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
          //  mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /**Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestionAnswer4Empty("save_userpolls", userId, "users",
                    "3", question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer3), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }
                    });
        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelImagesAnswerNotEmpty() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
           // mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            Log.e("categoryId", categoryId + "");
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestionAnswer1("save_userpolls", userId, "users", "3", question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer3), new TypedFile("image*//*", answer4), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }

                    });

        } else {
            Toast.makeText(mPhotoComparison, mPhotoComparison.getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void createPollMultipelImagesAnswer3Empty() {
        if (MApplication.isNetConnected(mPhotoComparison)) {
           // mContact = mCategory.toString().replaceAll("[\\s\\[\\]]", "");
            categoryId = MApplication.getString(mPhotoComparison, Constants.CATEGORY_ID);
            MApplication.materialdesignDialogStart(mPhotoComparison);
            /*Request and response in retrofit**/
            CreatePollRestClient.getInstance().postCreateMultipleImagesQuestionAnswer3Empty("save_userpolls", userId, "users", "3", question, new TypedFile("image*//*", answer1), new TypedFile("image*//*", answer2), new TypedFile("image*//*", answer4), new String(categoryId), mContact, mGroupId, mContactsId
                    , new Callback<CreatePollResponseModel>() {

                        @Override
                        public void success(CreatePollResponseModel createResponseModel, Response response) {
                            if (createResponseModel.getSuccess().equals("1")) {
                                finish();
                            }
                            MApplication.materialdesignDialogStop();
                            Toast.makeText(mPhotoComparison, createResponseModel.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        public void failure(RetrofitError retrofitError) {
                            MApplication.errorMessage(retrofitError, mPhotoComparison);
                            MApplication.materialdesignDialogStop();
                        }

                    });
        }
    }

    private void choosePic() {
        listDialog = new Dialog(mPhotoComparison);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        listDialog.setContentView(R.layout.custom_dialog_adapter);
        ListView list = (ListView) listDialog.findViewById(R.id.component_list);
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

    public static String getRealPathFromURI(Activity activity, Uri uri) {

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
        Log.e("result", result + "");
        return result;
    }


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
        choosenPhoto = String.format(
                Environment.getExternalStorageDirectory() + "/"
                        + getResources().getString(R.string.app_name)
                        + "/%s.png", "profilepic(" + new_Date + ")");

        // "vehicle_Info(" + new_Date + ")"
        File photo = new File(choosenPhoto);
        imageFileUri = Uri.fromFile(photo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(intent,
                11);
        listDialog.cancel();


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

   /* @Override
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


    //MApplication.getString(PhotoComparison.this, "clikaction").equals("1")


    //on completion of imnage upload
    @Override
    public void onTaskCompleted(URL url, String type, String encodedImg, int fileSize) {


        if(url!=null)
        {
            String chooseurl = url.toString();
            String[] separated1 = chooseurl.split(getString(R.string.regex_com));
            chooseurl=separated1[1];
            Imageslist.get(listposition).setUrl(chooseurl);
        }


        listposition++;

        if(listposition<Imageslist.size())
        {
            upLoad(listposition);
        }
        else
        {
            googleNow.setVisibility(View.GONE);
            googleNow.progressiveStop();
            createPollMultipelImages();
        }


       /* if(MApplication.getString(PhotoComparison.this, "clikaction").equals("1"))
        {
            image_choose_url=url.toString();
            Log.v("image_choose_url",url.toString());
        }
        else if(MApplication.getString(PhotoComparison.this, "clikaction").equals("2"))
        {
            image_addition_url=url.toString();
            Log.v("image_addition_url",url.toString());
        }
        else if(MApplication.getString(PhotoComparison.this, "clikaction").equals("3"))
        {
            option1_url=url.toString();
            Log.v("option1_url",url.toString());
        }
        else if(MApplication.getString(PhotoComparison.this, "clikaction").equals("4"))
        {
            option2_url=url.toString();
            Log.v("option2_url",url.toString());
        }
        else if(MApplication.getString(PhotoComparison.this, "clikaction").equals("5"))
        {
            option3_url=url.toString();
            Log.v("option3_url",url.toString());
        }
        else if(MApplication.getString(PhotoComparison.this, "clikaction").equals("6"))
        {
            option4_url=url.toString();
            Log.v("image_addition_url",url.toString());
        }*/




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
                                                    hasPermissions(PhotoComparison.this, PERMISSIONS);
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

    private void showPopUp(final ArrayList<GroupsNameObject> groupsList)
    {
        DatabaseHelper dbhelper = new DatabaseHelper(mPhotoComparison);
        dbhelper.deleteGroupList();

        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Groups");

        LayoutInflater inflater = getLayoutInflater();
        final View PopupLayout = inflater.inflate(R.layout.groups_layout, null);
        helpBuilder.setView(PopupLayout);

        final AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

        ListView lv_group_list         =   PopupLayout.findViewById(R.id.lv_group_list);
        TextView tv_done = PopupLayout.findViewById(R.id.tv_done);
        List mylist = new ArrayList();
        for(int i=0;i<groupsList.size();i++)
        {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", groupsList.get(i).getGroupName());
            mylist.add(map);
        }
        GroupCheckListAdapter sd = new GroupCheckListAdapter(mPhotoComparison, groupsList);
        lv_group_list.setAdapter(sd);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbhelper = new DatabaseHelper(mPhotoComparison);
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
                            AppConstents.GROUPlIST =new ArrayList<>();
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
                                // Toast.makeText(mYesOrNo, "Test"+groupsList.size(), Toast.LENGTH_SHORT).show();

                                AppConstents.GROUPlIST=groupsList;
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
                Toast.makeText(mPhotoComparison, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
