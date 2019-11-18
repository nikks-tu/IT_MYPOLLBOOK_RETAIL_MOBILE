/*
 * RegisterActivity.java
 * <p/>
 * Getting the country and code from response
 *
 * @category Contus
 * @package com.contus.activity
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */


package com.contus.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.responsemodel.SMSgatewayResponseModel;
import com.contus.restclient.SMSgatewayRestClient;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.polls.polls.R;

import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 6/29/2015.
 */
public class RegisterActivity extends Activity implements Constants {
    //Activity
    @SuppressLint("StaticFieldLeak")
    public static RegisterActivity mRegisterActivity;
    //Country
    private TextView editCountry;
    //phone number code
    private TextView txtPhoneNumberCode;
    //Edittext phone number
    private EditText editPhoneNumber;
    //Code
    private String code;
    //Phone number
    private String phoneNumber;
    //Internet connection
    private RelativeLayout internetConnection;
    //Main Layout
    private RelativeLayout mainLayout;
    //Text View
    private TextView txtPlus;
    //Smooth progressbar
    private SmoothProgressBar googleNow;

    private TextView txtNext;
    //Manifest.permission.SEND_SMS,
    String[] PERMISSIONS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA
    };
    private final int PERMISSION_ALL = 1;
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*View are creating when the activity is started**/
        init();
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit MyPollBook", Toast.LENGTH_SHORT);

        editPhoneNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                txtNext.setEnabled(true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                txtNext.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                txtNext.setEnabled(true);
            }
        });

    }

    /**
     * Instantiate the method
     */
    private void init() {
        /*Initializing the activity**/
        mRegisterActivity = this;
        /*Screen tracker**/
        Tracker trackerRegister = ((MApplication) getApplication()).getTracker(mRegisterActivity, MApplication.TrackerName.APP_TRACKER);
        /*Setting the screen name to display in analytics**/
        trackerRegister.setScreenName("Register Screen");
        /*hit**/
        trackerRegister.send(new HitBuilders.AppViewBuilder().build());
        /*Initializing the textview**/
        //Text next
        txtNext = findViewById(R.id.txtAgreeAndCondition);
        txtNext.setEnabled(true);
        /*Initializing the edittext**/
        editCountry = findViewById(R.id.txtCountry);
        /*Initializing the edittext**/
        txtPhoneNumberCode = findViewById(R.id.txtPhoneNumberCode);
        /*Initializing the edittext**/
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        googleNow = findViewById(R.id.google_now);
        /*Initializing the relative layout**/
        mainLayout = findViewById(R.id.mainLayout);
        internetConnection = findViewById(R.id.internetConnection);
        txtPlus = findViewById(R.id.txtPlus);
        /*Initializing the view**/
        //Divider line
        View txtView = findViewById(R.id.view);
        /*initializing the relative layout**/
        //Root view
        RelativeLayout rootView = findViewById(R.id.rootlayout);
        /*Calling this method to hide the button when the keypad opens**/
        MApplication.hideButtonKeypadOpens(mRegisterActivity, rootView, txtNext, txtView);
        /*Setting the string value as default**/
        MApplication.setString(mRegisterActivity, Constants.DIALOG_POSITION, "-1");
        //Setting the opt code as null in preference
        MApplication.setString(mRegisterActivity, Constants.OTPCODE, "");

    }

    /**
     * The onclicklistener will be called when any widget like button, text, image etc is either clicked or touched or
     * focused upon by the user.
     *
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if (clickedView.getId() == R.id.txtAgreeAndCondition) {
            //nikita
            if(isWorkingInternetPersent()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!hasPermissions(this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                        //  processInsideSplash();
                    }
                    else
                    {
                        txtNext.setEnabled(false);
                        registerValidation();
                    }
                }
                else{
                    txtNext.setEnabled(false);
                    registerValidation();
                }
            }
            // Internet Warning
            else{
                showAlertDialog(RegisterActivity.this, "Internet Connection",
                        "You need an active Internet Connection for this app", false);
            }

        } else if (clickedView.getId() == R.id.txtCountry) {
            /*calling custom list dialog method**/
            Intent a = new Intent(RegisterActivity.this, CountryActivity.class);
            startActivity(a);
        } else if (clickedView.getId() == R.id.internetRetry) {
            /*calling custom list dialog method**/
            response();
        } else if (clickedView.getId() == R.id.txtPhoneNumberCode) {
            Intent a = new Intent(RegisterActivity.this, CountryActivity.class);
            startActivity(a);
        }
    }

    private void registerValidation() {
        /*Getting the value from edit text***/
        code = txtPhoneNumberCode.getText().toString().trim();
        /*Getting the value from edit text**/
        phoneNumber = editPhoneNumber.getText().toString().trim();
        MApplication.setString(mRegisterActivity, Constants.PHONE_NUMBER, phoneNumber);
        //If edittext country matches
        if (editCountry.getText().toString().trim().equals(getResources().getString(R.string.activity_register_select))) {
            //Toast message will display if the country is not chosen
            Toast.makeText(mRegisterActivity, getResources().getString(R.string.select_your_country),
                    Toast.LENGTH_SHORT).show();
            //If the code field is empty
        } else if (TextUtils.isEmpty(code)) {
            // toast message will display
            Toast.makeText(mRegisterActivity, getResources().getString(R.string.enter_your_country_code),
                    Toast.LENGTH_SHORT).show();
            //Requesting focus
            txtPhoneNumberCode.requestFocus();
            //If the code length is less than 2
        } else if (code.length() < 1) {
            // toast message will display
            Toast.makeText(mRegisterActivity, getResources().getString(R.string.enter_valid_code),
                    Toast.LENGTH_SHORT).show();
            //Requesting focus
            txtPhoneNumberCode.requestFocus();
            //If the phone number field is empty
        } else if (TextUtils.isEmpty(phoneNumber)) {
            //Toast message will display
            Toast.makeText(mRegisterActivity, getResources().getString(R.string.enter_your_phone_number),
                    Toast.LENGTH_SHORT).show();
            //Requesting focus
            editPhoneNumber.requestFocus();
            //If the phone number field is less than 2
        } else if (phoneNumber.length() < 9) {
            //Toast message will display
            Toast.makeText(mRegisterActivity, getResources().getString(R.string.entered_mobile_number_invalid),
                    Toast.LENGTH_SHORT).show();
            //Requesting focus
            editPhoneNumber.requestFocus();
        } else {
            MApplication.setString(mRegisterActivity, Constants.PHONE_NUMBER, phoneNumber);
            //Request to the server
            smsGateway();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        /*Google analytics will start screen tracking**/
        GoogleAnalytics.getInstance(this).reportActivityStart(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_FIRST_USER) {
            setResult(Activity.RESULT_FIRST_USER);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*Google analytics will stop screen tracking**/
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        txtNext.setEnabled(true);
        //Hides the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        /*Sending the request and getting the response using the method**/
        response();

    }


    /**
     * Sending the request and getting the response using the method
     */
    private void response() {
        /*If internet connection is available**/
        if (MApplication.isNetConnected(mRegisterActivity)) {
            /*Getting the country value from shared prefernce**/
            String countryName = MApplication.getString(mRegisterActivity, Constants.COUNTRY);
            /*Getting the phone number code value from shared prefernce**/
            String phoneCode = MApplication.getString(mRegisterActivity, Constants.PHONE_NUMBER_CODE);
            /*If country name is not null and phone code is not null**/
            if (countryName != null && phoneCode != null) {
                /*Comparing the values**/
                if (MApplication.getString(mRegisterActivity, Constants.COUNTRY).equals(getResources().getString(R.string.activity_register_select))) {
                    /*Setting the text color**/
                    editCountry.setTextColor(getResources().getColor(R.color.view_color));
                    //Setting the text color
                    txtPhoneNumberCode.setTextColor(getResources().getColor(R.color.view_color));
                } else {
                    /*Setting the text color**/
                    editCountry.setTextColor(Color.BLACK);
                    /*Setting the text color**/
                    txtPlus.setTextColor(getResources().getColor(android.R.color.black));
                }
                /*Setting the country value for text**/
                editCountry.setText(countryName);
                /*Setting the phone code value for text**/
                txtPhoneNumberCode.setText(phoneCode);
                /*Setting the color for text**/
                txtPhoneNumberCode.setTextColor(Color.BLACK);
                /*Layout visibility**/
                mainLayout.setVisibility(View.VISIBLE);
                /*Internet connection invisible**/
                internetConnection.setVisibility(View.INVISIBLE);
            }
        } else {
            /*Layout visibility**/
            mainLayout.setVisibility(View.INVISIBLE);
            /*Internet connection invisible**/
            internetConnection.setVisibility(View.VISIBLE);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            // Do what ever you want
            exitToast.show();
            doubleBackToExitPressedOnce = false;
        } else{
            finishAffinity();
            finish();
            /*Setting the text**/
            editCountry.setText(MApplication.getString(mRegisterActivity, Constants.COUNTRY));
            // Do exit app or back press here
            super.onBackPressed();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getWindow().getCurrentFocus()).getWindowToken(), 0);
            }
        }
        return ret;
    }

    /**
     * Sending the request and getting the response using the method
     */
    private void smsGateway() {
        if (MApplication.isNetConnected(mRegisterActivity)) {
            Log.e("check", code + phoneNumber + "");
            /*Progress bar visibility*/
            googleNow.setVisibility(View.VISIBLE);
            /*Progress bar start*/
            googleNow.progressiveStart();
            /*Request and response in retrofit*/
            SMSgatewayRestClient.getInstance().postSMSgateway("sms_api", code + phoneNumber
                    , new Callback<SMSgatewayResponseModel>() {
                        @Override
                        public void success(SMSgatewayResponseModel smsGateWayResponse, Response response) {
                            //Response from the server if the api request is success
                            smsGateWayResponse(smsGateWayResponse);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            //Error message popups when the user cannot able to connect with the server
                           // MApplication.errorMessage(retrofitError, mRegisterActivity);
                        }

                    });
            /*Progressive bar stop**/
            googleNow.progressiveStop();
            /*Visibility gone**/
            googleNow.setVisibility(View.GONE);
        }
    }

    private void smsGateWayResponse(SMSgatewayResponseModel smsGateWayResponse) {


       /* if (("0").equals(smsGateWayResponse.getSuccess())) {
            String msg = smsGateWayResponse.getMsg();
            Toast.makeText(mRegisterActivity, msg, Toast.LENGTH_SHORT).show();
        } else {*/
        MApplication.setString(mRegisterActivity, Constants.PHONE_NUMBER, phoneNumber);
        /*starting the activity**/
        Intent a = new Intent(RegisterActivity.this, AccountVerification.class);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /* Starting the activity **/
        startActivityForResult(a, com.contusfly.utils.Constants.ACTIVITY_REQ_CODE);
        /* }*/
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

    // Check Internet Connection
    public boolean isWorkingInternetPersent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    // Alert Dialog for Internet

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= 21)
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle(title);
        builder.create().show();
        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
