package com.contus.smsreciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.contus.app.Constants;
import com.contusfly.MApplication;

/**
 * Created by Ravi on 09/07/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    //Get simple class name
    private static final String TAG = SmsReceiver.class.getSimpleName();
    //Helper to register for and send broadcasts of Intents to local objects within your process.
    // This is has a number of advantages over sending global broadcasts with sendBroadcast(Intent)
    private Intent startBroadcast;

    @Override
    public void onReceive(Context context, Intent intent) {
       //A mapping from String values to various Parcelable types.
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                //array details
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    //A Short Message Service message.
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    //Sender address
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    //Message
                    String message = currentMessage.getDisplayMessageBody();
                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(Constants.SMS_ORIGIN.toLowerCase())) {
                        return;
                    }
                    // verification code from sms
                    String verificationCode = MApplication.getVerificationCode(message);
                    //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity
                    startBroadcast = new Intent(Constants.MSG_VERIFICATION_CODE);
                    //broadcastIntent to send it to any interested BroadcastReceiver components
                    startBroadcast.putExtra(Constants.OTPCODE,verificationCode);
                    //Helper to register for and send broadcasts of Intents to local objects within your process.
                    // This is has a number of advantages over sending global broadcasts with sendBroadcast(Intent)
                    LocalBroadcastManager.getInstance(context)
                            .sendBroadcast(startBroadcast);
                }
            }
        } catch (Exception e) {
          Log.i("","",e);
        }
    }
}
