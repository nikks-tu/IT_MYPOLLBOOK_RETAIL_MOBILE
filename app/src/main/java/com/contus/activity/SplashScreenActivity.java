/*
 * SplashScreenActivity.java
 * <p/>
 * Displays the splash screen for milliseconds.
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
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.residemenu.MenuActivity;
import com.contusfly.MApplication;
import com.contusfly.utils.ContusPreferences;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gcm.GCMRegistrar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.netcompss.ffmpeg4android.Prefs;
import com.new_chanages.api_interface.AppVersionDataInterface;
import com.new_chanages.models.AppVersionMainObject;
import com.new_chanages.models.AppVersionPostParameters;
import com.polls.polls.BuildConfig;
import com.polls.polls.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by user on 6/29/2015.
 */
public class SplashScreenActivity extends Activity implements Constants {
    /**
     * Splash time
     **/
    int splashTimeOut = 1000;
    String deviceId;
    //Contus preference
    private ContusPreferences contusPreferences;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        //Initializing the activity
        context = SplashScreenActivity.this;
        contusPreferences = new ContusPreferences(this);
        //Checking the device
        //GCMRegistrar.checkDevice(this);
        //device id from the mobile
        //String deviceId = GCMRegistrar.getRegistrationId(this);
        //Setting the device id in preference
        //////////FCM Code///////////////////////////////////
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseApp.initializeApp(context);
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
//                    txtMessage.setText(message);
                }
            }
        };
        FirebaseApp.initializeApp(context);
        displayFirebaseRegId();
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        deviceId=FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(this, FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_SHORT).show();
        contusPreferences.setDeviceToken(deviceId);
        //Log.d("deviceIdSpl",deviceId);
        //Set is notify false
        contusPreferences.setIsNotify(false);
        //Checking the device
        //GCMRegistrar.checkDevice(this);
        //Getting the registration id
        //deviceId = GCMRegistrar.getRegistrationId(this);
        //if device id is not empty
        if (deviceId != null && deviceId.isEmpty()) {
            //Registering is using t=sender id
            GCMRegistrar.register(this, SENDER_ID);
        }


        //if device id is not null
        if (deviceId != null && deviceId.length() > 0) {
            //Setting the preference

            contusPreferences.setDeviceToken(deviceId);
            //setting in preference
            com.contusfly.MApplication.setString(SplashScreenActivity.this, Constants.GCM_REG_ID, deviceId);
        }
        Log.e("deviceId   ", deviceId + "");
        /*Setting the layout full screen**/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Full screen view
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        /*layout**/
        setContentView(R.layout.activity_splash);
        /*Added by Nikita*/
        if(isWorkingInternetPersent()){
          processInsideSplash();
        }
        // Internet Warning
        else{
            showAlertDialog(this, "Internet Connection",
                    "You need an active Internet Connection for this app", false);
        }

    }

    public void processInsideSplash(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //validateAppVersion();
                validateAppWithRetrofit();
                //Getting the boolean from preference
            }
        }, splashTimeOut);
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

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        deviceId = pref.getString("regId", null);

        Log.e(Prefs.TAG, "Firebase reg id: " + deviceId);

        //if (!TextUtils.isEmpty(regId))
            //txtRegId.setText("Firebase Reg Id: " + regId);
       // else
            //txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    public void validateAppVersion(){
        final int versionName = BuildConfig.VERSION_CODE;
        final HashMap<String, Integer> paramss = new HashMap<>();
        paramss.put("device_id", versionName);
        CustomRequest.makeJsonObjectRequest(SplashScreenActivity.this,Constants.LIVE_BASE_URL+"api/v1/version",new JSONObject(paramss), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
//            dialog.dismiss();
                Log.i("onErrormessage", "message= " +message);
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.i("PCCmessage", "message " + response.toString());
                JSONObject result = response;
                int version_no=0;
                try {
                    Log.i("PCCmessage", "message " + result.getString("msg"));

                    version_no =Integer.parseInt(result.getString("results"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (versionName<version_no) {
                        context = SplashScreenActivity.this;
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(SplashScreenActivity.this);
                        final View view = inflater.inflate(R.layout.versionpopup, null);
                        alertDialog.setView(view);
                        final androidx.appcompat.app.AlertDialog alert = alertDialog.show();
                        TextView updateText =  view.findViewById(R.id.id_updateText);
                        TextView update =  view.findViewById(R.id.id_update);
                        TextView cancel =  view.findViewById(R.id.cancel);
                        updateText.setText(result.getString("msg"));
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.polls.polls&hl=en" )));

                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (alert != null && alert.isShowing()) {
                                    alert.dismiss();

                                    if (!MApplication.getBoolean(SplashScreenActivity.this,
                                            FIRST_TIME)) {
                                        //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                                        // to communicate with a background Service.
                                        Intent intent = new Intent(getApplicationContext(),
                                                Welcome.class);
                                        //starting the activity
                                        startActivity(intent);
                                    } else {
                                        ((MApplication) getApplication()).startLoginService(SplashScreenActivity.this);
                                        //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                                        // to communicate with a background Service.
                                        Intent intent = new Intent(getApplicationContext(),
                                                MenuActivity.class);
                                        //starting the activity
                                        startActivity(intent);
                                    }

                                    finish();
                                }
                            }
                        });

                        alert.show();
                        alert.setCancelable(false);
                    }
                    else
                    {
                        if (!MApplication.getBoolean(SplashScreenActivity.this,
                                FIRST_TIME)) {
                            //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                            // to communicate with a background Service.
                            Intent intent = new Intent(getApplicationContext(),
                                    Welcome.class);
                            //starting the activity
                            startActivity(intent);
                        } else {
                            ((MApplication) getApplication()).startLoginService(SplashScreenActivity.this);
                            //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                            // to communicate with a background Service.
                            Intent intent = new Intent(getApplicationContext(),
                                    MenuActivity.class);

                            /*Intent intent = new Intent(getApplicationContext(),
                                    Rewards_Activty.class);*/
                            //starting the activity
                            startActivity(intent);
                        }

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void validateAppWithRetrofit(){
        final int versionName = BuildConfig.VERSION_CODE;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL).client(client)
                //.baseUrl("http://182.18.177.27/TUUserManagement/api/user/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AppVersionDataInterface service = retrofit.create(AppVersionDataInterface.class);

        Call<AppVersionMainObject> call = service.getStringScalar(new AppVersionPostParameters(String.valueOf(versionName)));
        call.enqueue(new Callback<AppVersionMainObject>() {
            @Override
            public void onResponse(Call<AppVersionMainObject> call, Response<AppVersionMainObject> response) {
                int version_no;
                if(response.body()!=null) {
                    version_no = Integer.parseInt(response.body().getResults());
                    String imageUploadBaseUrl = response.body().getImageUploadBaseUrl();


                    MApplication.setString(getApplicationContext(), "image_upload_url", imageUploadBaseUrl);
                    // Toast.makeText(context, "Got Response", Toast.LENGTH_SHORT).show();
                    if (versionName < version_no) {
                        context = SplashScreenActivity.this;
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(SplashScreenActivity.this);
                        final View view = inflater.inflate(R.layout.versionpopup, null);
                        alertDialog.setView(view);
                        final androidx.appcompat.app.AlertDialog alert = alertDialog.show();
                        TextView updateText = view.findViewById(R.id.id_updateText);
                        TextView update = view.findViewById(R.id.id_update);
                        TextView cancel = view.findViewById(R.id.cancel);
                        updateText.setText(response.body().getMsg());
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.polls.polls&hl=en")));
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (alert != null && alert.isShowing()) {
                                    alert.dismiss();

                                    if (!MApplication.getBoolean(SplashScreenActivity.this,
                                            FIRST_TIME)) {
                                        //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                                        // to communicate with a background Service.
                                        Intent intent = new Intent(getApplicationContext(), Welcome.class);
                                        //starting the activity
                                        startActivity(intent);
                                    } else {
                                        ((MApplication) getApplication()).startLoginService(SplashScreenActivity.this);
                                        //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                                        // to communicate with a background Service.
                                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                        //starting the activity
                                        startActivity(intent);
                                    }

                                    finish();
                                }
                            }
                        });

                        alert.show();
                        alert.setCancelable(false);
                    }
                    else
                    {
                        if (!MApplication.getBoolean(SplashScreenActivity.this,
                                FIRST_TIME)) {
                            //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                            // to communicate with a background Service.
                            Intent intent = new Intent(getApplicationContext(),
                                    Welcome.class);
                            //starting the activity
                            startActivity(intent);
                        } else {
                            ((MApplication) getApplication()).startLoginService(SplashScreenActivity.this);
                            //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent) or bindService(Intent, ServiceConnection, int)
                            // to communicate with a background Service.
                            Intent intent = new Intent(getApplicationContext(),
                                    MenuActivity.class);

                        /*Intent intent = new Intent(getApplicationContext(),
                                Rewards_Activty.class);*/
                            //starting the activity
                            startActivity(intent);
                        }

                        finish();
                        //}

                    }
                }

            }

            @Override
            public void onFailure(Call<AppVersionMainObject> call, Throwable t) {
                Toast.makeText(context, "Error" +t, Toast.LENGTH_SHORT).show();
            }
        });

    }


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
