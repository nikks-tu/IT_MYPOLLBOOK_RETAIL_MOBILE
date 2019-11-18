/*
 * AccountVerification.java
 * <p/>
 * Account verification is done in ths class
 *
 * @category Contus
 * @package com.contus.activity
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */


package com.contus.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.chatlib.utils.ContusConstantValues;
import com.contus.residemenu.MenuActivity;
import com.contus.responsemodel.ContactResponseModel;
import com.contus.responsemodel.MobileNumberChange;
import com.contus.responsemodel.SMSgatewayResponseModel;
import com.contus.responsemodel.SMSverifyModel;
import com.contus.restclient.ContactsRestClient;
import com.contus.restclient.MobileNumberCheckRestClient;
import com.contus.restclient.SMSVerifyRestClient;
import com.contus.restclient.SMSgatewayRestClient;
import com.contusfly.MApplication;
import com.contusfly.activities.ActivityBase;
import com.contusfly.service.ChatService;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomToast;
import com.polls.polls.R;

import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*
 * Created by user on 6/29/2015.
 */
public class AccountVerification extends ActivityBase implements Constants,Utils.ContactResult  {
    public  Dialog customDialogBox;
    //Account verification activity
    AccountVerification mAccountVerification;
    //Relative layout
    private RelativeLayout rootView;
    //txt agree
    TextView txtAgreeAndCondition;
    //Divider line
    View txtView;
    //Text view phone number
    TextView txtPhoneNumber;
    //Phone code
    private EditText editPhoneCode;
    //Smooth progress bar
    private SmoothProgressBar googleNow;
    //phone number
    private String phoneNumber;
    //Phone number code
    private String phoneCode;
    //Country code
    private String code;
    //Receiver is registered
    private TextView txtResendCode;
    private boolean myReceiverIsRegistered = false;
    /** The m application. */
    public com.contusfly.MApplication mAccountVerificationApplication;
    /** The Constant DELAY_TIME. */
    private static final int DELAY_TIME = 5000;
    /**
     * handler for received Intents for the "my-event" event.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*Getting the code from receiver and setting the text in input fileld**/
            code = intent.getExtras().getString(Constants.OTPCODE);
            /*If code is not empty se t the text in text field**/
            if (!("").equals(code)) {
             /*Setting the text in input type***/
                editPhoneCode.setText(code);
            }

        }
    };
    private String password;
    private String deviceCountryCode;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountverification);
        /*View are creating when the activity is started**/
        init();
        setCountDownForOTP();
    }

    /**
     * Instantiate the method
     */
    private void init() {
        /*Initializing the activity**/
        mAccountVerification = this;
        rootView =  findViewById(R.id.rootlayout);
        txtAgreeAndCondition =  findViewById(R.id.txtAgreeAndCondition);
        txtPhoneNumber =  findViewById(R.id.txtPhoneNumber);
        txtResendCode = findViewById(R.id.txtResendCode);
        txtView = findViewById(R.id.view);
        editPhoneCode =  findViewById(R.id.editPhoneCode);
        googleNow =  findViewById(R.id.google_now);
        /*Getting the value from shared preference and storing the value in string**/
        phoneNumber = MApplication.getString(mAccountVerification, Constants.PHONE_NUMBER);
        phoneCode = MApplication.getString(mAccountVerification, Constants.PHONE_NUMBER_CODE);
        /*Setting the value in text**/
        txtPhoneNumber.setText("+" + phoneCode + " " + phoneNumber);
        /*Calling this method to hide view when the keypad opens**/
        MApplication.hideButtonKeypadOpens(mAccountVerification, rootView, txtAgreeAndCondition, txtView);
        /*Setting the visibility gone**/
        googleNow.setVisibility(View.GONE);
        mAccountVerificationApplication = (com.contusfly.MApplication) getApplication();
        MApplication.setBoolean(mAccountVerification,"test",false);
        //Telephone manager
        TelephonyManager tm = (TelephonyManager)getSystemService(Activity.TELEPHONY_SERVICE);
        //device country code.
        deviceCountryCode = tm.getNetworkCountryIso();
    }

    /**
     * Calling this method from xml file when performing the click on the views
     *
     * @param clickedView
     */
    public void onClick(View clickedView) {
        if (clickedView.getId() == R.id.txtAgreeAndCondition) {
            //OTP code verification done in this method
          checkOTPCodeVerification();
        } else if (clickedView.getId() == R.id.txtChange) {
            /*Finish the activity**/
            finish();
        } else if (clickedView.getId() == R.id.txtResendCode) {
            //This method is used to send the otp code again
            smsGateway();

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
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
    /**
     * This method is used for validation and request for the user entered  code matches the OTP code.
     */
    private void checkOTPCodeVerification() {
        /*Value from the edit text is stored in the string**/
        code = editPhoneCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            /*Toast message is displayed when the following field is empty**/
            Toast.makeText(mAccountVerification, getResources().getString(R.string.field_empty),
                    Toast.LENGTH_SHORT).show();
        } else {
            /*Request and checkOTPCodeResponse happens in this method when the above fields are validated correctly**/
            checkOTPCode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Storing the address in preference
        MApplication.setString(mAccountVerification, Constants.CITY, "");
      //Helper to register for and send broadcasts of Intents to local objects within your process.
        LocalBroadcastManager.getInstance(mAccountVerification).registerReceiver(
                mMessageReceiver, new IntentFilter(Constants.MSG_VERIFICATION_CODE));
       //Hides the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**(non-Javadoc)
        *
        * @see Fragment#onPause()
        */
    @Override
    public void onPause() {
        //If the reciver is registered,when the activity call this pause() overrride method,makes the reciver unregistered.
        if (myReceiverIsRegistered) {
            /*
             * Unregister since the activity is not visible
             */
            LocalBroadcastManager.getInstance(mAccountVerification)
                    .unregisterReceiver(mMessageReceiver);
            myReceiverIsRegistered = false;
        }
        super.onPause();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_FIRST_USER) {
            setResult(Activity.RESULT_FIRST_USER);
            finish();
        }
    }

    /**
     * Request and checkOTPCodeResponse api method
     */
    private void checkOTPCode() {
        if (MApplication.isNetConnected(mAccountVerification)) {

            //Progress bar visible
            googleNow.setVisibility(View.VISIBLE);
            /*Custom progressbar start**/
            googleNow.progressiveStart();
            /* Requesting the checkOTPCodeResponse from the given base url**/
            SMSVerifyRestClient.getInstance().postSMSgatewayVerify(new String("verify_api"), new String(phoneCode + phoneNumber), new String(code)
                    , new Callback<SMSverifyModel>() {
                @Override
                public void success(SMSverifyModel smsVerifyModel, Response response) {
                    //If the server response is success this method will be called.
                    checkOTPCodeResponse(smsVerifyModel);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to connect with the server
                    MApplication.errorMessage(retrofitError, mAccountVerification);
                    //Progress bar will stop
                    googleNow.progressiveStop();
                    //View will get invisible
                    googleNow.setVisibility(View.GONE);
                }

            });
        } else {
            //Toast message will display when the internet connection is not available
            Toast.makeText(mAccountVerification, getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Response is binded to the view when the the request for the OTP matches.
     * @param smsVerifyModel response from the server
     */
    private void checkOTPCodeResponse(SMSverifyModel smsVerifyModel) {
        /*If checkOTPCodeResponse is success this method is called**/
        String success = smsVerifyModel.getSuccess();
        //Message from the response
        String message = smsVerifyModel.getMsg();
        //If success is 1,the OTP code is successfully validated
        if (("1").equals(success)) {
            //This method will be called once the otp matches and check wether the user has already registered with the app.
            checkMobileNumber();
        } else {
            //progress bar stops
            googleNow.progressiveStop();
            /*Visibility gone**/
            googleNow.setVisibility(View.GONE);
            Toast.makeText(mAccountVerification, message,
                    Toast.LENGTH_SHORT).show();
        }
        //Toast message will display the message form the response OTP
       /* Toast.makeText(mAccountVerification, message,
                Toast.LENGTH_SHORT).show();*/

    }

    /**
     * Sending the request and getting the checkOTPCodeResponse using the method
     */
    private void smsGateway() {
        if (MApplication.isNetConnected(mAccountVerification)) {
            /*Progress bar visibility**/
            googleNow.setVisibility(View.VISIBLE);
            /*Progress bar start**/
            googleNow.progressiveStart();
            /*Request and checkOTPCodeResponse in retrofit**/
            SMSgatewayRestClient.getInstance().postSMSgateway(new String("sms_api"), new String(phoneCode + phoneNumber)
                    , new Callback<SMSgatewayResponseModel>() {
                @Override
                public void success(SMSgatewayResponseModel smsGatewayResponseModel, Response response) {
                    //If the success response form the server this method will be called
                    smsGateWayResponse(smsGatewayResponseModel);
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    /*Progressive bar stop**/
                    googleNow.progressiveStop();
                    /*Visibility gone**/
                    googleNow.setVisibility(View.GONE);
                    //Error message popups when the user cannot able to connect with the server
                    MApplication.errorMessage(retrofitError, mAccountVerification);
                }
            });
        }
    }

    private void smsGateWayResponse(SMSgatewayResponseModel smsGatewayResponseModel) {
        if (("0").equals(smsGatewayResponseModel.getSuccess())) {
            String msg = smsGatewayResponseModel.getMsg();

            //Commented by Nikita
            /*Toast.makeText(mAccountVerification, msg + " HI",
                    Toast.LENGTH_SHORT).show();*/
        }
        /*Progressive bar stop**/
        googleNow.progressiveStop();
        /*Visibility gone**/
        googleNow.setVisibility(View.GONE);
        setCountDownForOTP();
    }

    private void setCountDownForOTP(){
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtResendCode.setText("To resend OTP wait for\n " + ("\n00:"+(millisUntilFinished / 1000)));
                txtResendCode.setClickable(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                txtResendCode.setText(R.string.activity_account_resendcode);
                txtResendCode.setClickable(true);
            }

        }.start();
    }
    /**
     * his method will be called once the otp matches and check weather the user has already registered with the app.
     */
    private void checkMobileNumber() {
        if (MApplication.isNetConnected(mAccountVerification)) {
            googleNow.setVisibility(View.VISIBLE);
            /*Custom progressbar start**/
            googleNow.progressiveStart();

            /*  Requesting the checkOTPCodeResponse from the given base url**/
            MobileNumberCheckRestClient.getInstance().postCheckMobileData("mobilenoexistapi", this.phoneNumber, new Callback<MobileNumberChange>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void success(MobileNumberChange welcomeResponseModel, Response response) {
                    /*If checkOTPCodeResponse is success this method is called**/
                    googleNow.progressiveStop();
                    googleNow.setVisibility(View.GONE);
                    checkMobileNumberResponse(welcomeResponseModel);
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    /*Progressive bar stop**/
                    googleNow.progressiveStop();
                    /*Visibility gone**/
                    googleNow.setVisibility(View.GONE);
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, mAccountVerification);

                }
            });
        } else {
            //Toast message will display when the internet connection is not available
            Toast.makeText(mAccountVerification, getResources().getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The values from the checkOTPCodeResponse are retrived using model class
     *
     * @param mobileNumberChange model class
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkMobileNumberResponse(MobileNumberChange mobileNumberChange) {
        /*success value is retrieved from model class**/
        String success = mobileNumberChange.getSuccess();
        /*message value is retrieved from model class**/
        String msg = mobileNumberChange.getMsg();
        /*mobileKey value is retrieved from model class**/
        String mobileKey = mobileNumberChange.getResults().getMobileKey();
        /*Progressive bar stops**/

        if (("1").equals(success) && ("0").equals(mobileKey)) {
            /*Toast message is displayed when above condition satisfied**/
            //Commented by Nikita
            /*Toast.makeText(mAccountVerification, msg, Toast.LENGTH_SHORT).show();*/
            /*Progressive bar stop**/
            googleNow.progressiveStop();
            /*Visibility gone**/
            googleNow.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(),PersonalInfo.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, com.contusfly.utils.Constants.ACTIVITY_REQ_CODE);
        } else if (("2").equals(success) && ("1").equals(mobileKey)) {
            //setting the string
            MApplication.setString(AccountVerification.this, com.contusfly.utils.Constants.VIBRATION_TYPE,
                    "0");
            /*Toast message is displayed when the following field is empty**/
            String clickValue = "1";
            /*Getting the user id from checkOTPCodeResponse**/
           userId = mobileNumberChange.getResults().getUserId();
            /*Getting the profile image from checkOTPCodeResponse**/
            String profileImage = mobileNumberChange.getResults().getProfileImg();
            /*Getting the user name checkOTPCodeResponse**/
            String userName = mobileNumberChange.getResults().getUserName();
            password=mobileNumberChange.getResults().getPassword();
            /*Setting the username string in preference**/
            MApplication.setString(mAccountVerification, Constants.PROFILE_USER_NAME, userName);
            /*Setting the profileImage string in preference**/
            MApplication.setString(mAccountVerification, Constants.PROFILE_IMAGE, profileImage);
            /*Progressive bar stop**/
            googleNow.progressiveStop();
            /*Visibility gone**/
            googleNow.setVisibility(View.GONE);
            /*Setting the USER_ID string in preference**/
            MApplication.setString(mAccountVerification, Constants.USER_ID, userId);
            //saving in preference
            MApplication.storeBooleanInPreference(com.contusfly.utils.Constants.IS_CONTACTS_SYNCED, true);
            //saving in preference
            MApplication.storeStringInPreference(com.contusfly.utils.Constants.USERNAME,phoneCode + phoneNumber);
            //saving in preference
            MApplication.storeStringInPreference("password",password);
            Utils.getMobileContacts(this, this, deviceCountryCode);
            /*Custom dialog**/
            customDialog(mAccountVerification, msg, clickValue);

        }
        MApplication.setBoolean(getApplicationContext(),"notification click",false);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void customDialog(final Activity activity, final String msg, final String clickValue) {
        /* Custom dialog **/
        customDialogBox = new Dialog(activity);
        /* No title **/
        customDialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*setting the transparent window **/
        Objects.requireNonNull(customDialogBox.getWindow()).setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        /*Setting the custom dialog **/
        customDialogBox.setContentView(R.layout.custom_dialog);
        TextView txtResult = customDialogBox
                .findViewById(R.id.textView_dialog);
        /* Setting the value from string in to the text view* */
        txtResult.setText(msg);
        /* Setting the back button false **/
        customDialogBox.setCancelable(false);
        /* dialog show **/
        customDialogBox.show();
        /* Button initialization **/
        Button okButton =  customDialogBox
                .findViewById(R.id.okBtn);
        final Button cancelBtn = customDialogBox
                .findViewById(R.id.noBtn);
        /* button click listener **/
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (("1").equals(clickValue)) {
                    /* Calling the material design custom dialog **/
                    MApplication.materialdesignDialogStart(mAccountVerification);
                    MApplication.setBoolean(mAccountVerification,"test",true);
                    /*Setting the boolean value as true on the first time installation**/
                    MApplication.setBoolean(mAccountVerification, com.contus.app.Constants.FIRST_TIME, true);
                   /* activity.startService(new Intent(activity, ChatService.class).putExtra("username", phoneCode +
                            phoneNumber).putExtra("password", password).setAction(ContusConstantValues.CONTUS_XMPP_ACTION_CONNECTION_REQUEST))*/;
                    activity.startActivity(new Intent(AccountVerification.this,MenuActivity.class));
                    customDialogBox.cancel();// custom dialog cancel
                    MApplication.materialdesignDialogStop();
                    Intent intent=new Intent(AccountVerification.this,MenuActivity.class);
                    intent.putExtra("fromActivityName","yes");
                    startActivity(intent);
                    finish();
                }
            }

        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* custom dialog cancel **/
                customDialogBox.cancel();
            }
        });
    }

    @Override
    protected void handleLoggedIn() {

        if(MApplication.getBoolean(mAccountVerification,"test")) {
            try {
                MApplication.storeStringInPreference(com.contusfly.utils.Constants.USERNAME, phoneCode + phoneNumber);
                MApplication.storeStringInPreference(com.contusfly.utils.Constants.SECRET_KEY, password);
                MApplication.storeStringInPreference(com.contusfly.utils.Constants.USER_STATUS,
                        getString(R.string.default_status));
                MApplication.storeBooleanInPreference(com.contusfly.utils.Constants.IS_LOGGED_IN, true);
                MApplication.storeStringInPreference(com.contusfly.utils.Constants.CHAT_ONGOING_NAME,
                        com.contusfly.utils.Constants.NULL_VALUE);
                getProfileRosterFromXMPP();

                Intent intent = new Intent(getApplicationContext(),
                        MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                setResult(Activity.RESULT_FIRST_USER);
                finish();
            } catch (Exception e) {
                Log.e("", "" + e);
            }
        }
    }
    /**
     * Gets the roster from xmpp.
     *
     * @return the roster from xmpp
     */
    private void getProfileRosterFromXMPP() {
        startService(new Intent(this, ChatService.class)
                .setAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_PROFILE));
        startService(new Intent(this, ChatService.class)
                .setAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_ROSTER));
        startService(new Intent(this, ChatService.class)
                .setAction(ContusConstantValues.CONTUS_XMPP_GET_GROUPS));
        //material dialog stop
        MApplication.materialdesignDialogStop();
    }

    @Override
    public void OnContactResult(String mobileNos) {
        Log.e("mobileNo","mobileNo"+"");
        if (mobileNos != null && !mobileNos.isEmpty()) {
            contactsLoad(mobileNos);
        } else {
            CustomToast.showToast(this,
                    getString(R.string.text_no_contacts_to_sync));
        }
    }

    /*
     * Request and response api method
     * @param contactsString
     * @param contactsString
     */
    private void contactsLoad(String contactsString) {
        Log.e("contactsString",contactsString+"");
        if (MApplication.isNetConnected(AccountVerification.this)) {
            /* Requesting the response from the given base url**/
            ContactsRestClient.getInstance().postContactDetails(new String("contact_api"), new String(userId), new String(contactsString),
                    new Callback<ContactResponseModel>() {
                        @Override
                        public void success(ContactResponseModel contactResponseModel, retrofit.client.Response response) {
                            startService(new Intent(AccountVerification.this, ChatService.class).setAction(ContusConstantValues.CONTUS_XMPP_ACTION_GET_ROSTER));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Refresh contacts
                                    MApplication.setBoolean(AccountVerification.this,"contact_sync",false);
                                }
                            }, 3000);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //Error message popups when the user cannot able to coonect with the server
                            MApplication.errorMessage(error,AccountVerification.this);
                        }
                    });
        }
    }
}
