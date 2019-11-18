package com.contus.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.Welcome;
import com.contus.responsemodel.DeleteResponseModel;
import com.contus.restclient.DeleteAccount;
import com.contusfly.MApplication;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.utils.Constants;
import com.contusfly.views.CommonAlertDialog;
import com.polls.polls.R;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/8/2015.
 */
public class Settings extends Fragment implements CommonAlertDialog.CommonDialogClosedListener {
    //Radio button
    private RadioButton radioNotificationOn;
    //relative layout
    private RelativeLayout relativeNotificationToneSettingsDetails;
    //relative layout
    private RelativeLayout notification;
    //relative layout
    private RelativeLayout vibrate;
    //TextView
    private TextView txtContactVibrateSettings;
    //relative layout
    private RelativeLayout clearConversation;
    //relative layout
    private RelativeLayout deleteMyAccount;
    //application
    private MApplication mApplication;
    //clear chat
    private boolean isClearChat;
    private String ringTonePath;
    private Intent Mringtone;
    private RingtoneManager mRingtoneManager;
    private Uri uri;

    /**
     * OnClick listner for question participant count
     */
    private View.OnClickListener mClearConversation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isClearChat = true;
            //Show alert dialog
            showAlertDialog(Constants.EMPTY_STRING,
                    getString(R.string.txt_are_you_sure_you_want_to_clear_conversation),
                    getString(R.string.text_yes), getString(R.string.text_no),
                    DIALOG_TYPE.DIALOG_DUAL, "clearconversation");
        }
    };
    /**
     * OnClick listner for question participant count
     */
    private View.OnClickListener mDeleteMyAccount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isClearChat = false;
            //Show alert dialog
            showAlertDialog(Constants.EMPTY_STRING,
                    getString(R.string.txt_are_you_sure_you_want_to_delete_ac),
                    getString(R.string.text_yes), getString(R.string.text_no),
                    DIALOG_TYPE.DIALOG_DUAL, "deletemyaccount");
        }
    };
    /**
     * OnClick listner for question participant count
     */
    private View.OnClickListener mNotificationOn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //tone settings
            handleToneSettings();
        }
    };
    /**
     * OnClick listner for question participant count
     */
    private View.OnClickListener mNotificationTone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //call list
            callList();
        }
    };
    /**
     * OnClick listner for question participant count
     */
    private View.OnClickListener mVibrate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //vibrate
            showList();
        }
    };
    private String userId;
    private String phoneNumber;
    private String phoneCode;
    private String notification_uri;
    private RadioButton imgVibrateSelect;
    private String[] tones;

    /**
     * Show list.
     */
    private void showList() {
        //Creates a builder for an alert dialog that uses the default alert dialog theme.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set the title
        builder.setTitle(getString(R.string.text_vib_type));

        builder.setItems(tones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //setting the text view
                txtContactVibrateSettings.setText(getString(R.string.text_vibarate) + " "
                        + tones[item]);
                if (item == 0) {
                    imgVibrateSelect.setChecked(false);
                } else {
                    imgVibrateSelect.setChecked(true);
                }
                //setting the string
                MApplication.setString(getActivity(), Constants.VIBRATION_TYPE,
                        String.valueOf(item));
                MApplication.setString(getActivity(), Constants.VIBRATION_TYPE_NAME,
                        tones[item]);
                //dialog dismiss
                dialog.dismiss();
            }
        });
        //alert diaolg
        AlertDialog alert = builder.create();
        //show
        alert.show();
    }

    /**
     * Call list.
     */
    private void callList() {

        //Starts the intent or Activity of the ringtone manager, opens popup box
        Mringtone = new Intent(mRingtoneManager.ACTION_RINGTONE_PICKER);

//specifies what type of tone we want, in this case "ringtone", can be notification if you want
        Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);

//gives the title of the RingtoneManager picker title
        Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");

//returns true shows the rest of the songs on the device in the default location
        Mringtone.getBooleanExtra(mRingtoneManager.EXTRA_RINGTONE_INCLUDE_DRM, true);
        Log.e("test",mApplication.getStringFromPreference(
                Constants.NOTIFICATION_URI)+"");
        if(mApplication.getStringFromPreference(
                Constants.NOTIFICATION_URI)==null){
            uri=(Uri)null;
        }else{
            uri= Uri.parse(mApplication.getStringFromPreference(
                    Constants.NOTIFICATION_URI));
        }

//chooses and keeps the selected item as a uri
        if ( uri != null ) {
            Log.e("uricall",uri+"");
            Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_EXISTING_URI,uri);
        } else {
            Log.e("uricallno",uri+"");
            Mringtone.putExtra(mRingtoneManager.EXTRA_RINGTONE_EXISTING_URI, uri);
        }
        startActivityForResult(Mringtone, 0);

    }

    /**
     * handleToneSettings
     */
    private void handleToneSettings() {
        boolean status = false;
        if (!radioNotificationOn.isChecked())
            status = true;
        //set checked
        radioNotificationOn.setChecked(status);
        //Set the boolean
        mApplication.setBoolean(getActivity(), Constants.CONVERSATION_SOUND, status);
    }

    /**
     * The Enum DIALOG_TYPE.
     */
    public enum DIALOG_TYPE {

        /**
         * The dialog single.
         */
        DIALOG_SINGLE,

        /**
         * The dialog dual.
         */
        DIALOG_DUAL
    }

    @Override
    public void onDialogClosed(CommonAlertDialog.DIALOG_TYPE dialogType, boolean isSuccess) {
        if (isSuccess)
            deleteRecords();
    }

    @Override
    public void listOptionSelected(int position) {
        Log.e("", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_settings, null);
        radioNotificationOn = (RadioButton) root.findViewById(R.id.imgSelect);
        imgVibrateSelect = (RadioButton) root.findViewById(R.id.imgVibrateSelect);
        notification = (RelativeLayout) root.findViewById(R.id.Notification);
        vibrate = (RelativeLayout) root.findViewById(R.id.vibrate);
        txtContactVibrateSettings = (TextView) root.findViewById(R.id.txtContactVibrateSettings);
        clearConversation = (RelativeLayout) root.findViewById(R.id.clearConversation);
        deleteMyAccount = (RelativeLayout) root.findViewById(R.id.deleteMyAccount);
        relativeNotificationToneSettingsDetails = (RelativeLayout) root.findViewById(R.id.relativeNotificationToneSettingsDetails);
        //Register a callback to be invoked when this view is clicked.
        relativeNotificationToneSettingsDetails.setOnClickListener(mNotificationTone);
        notification.setOnClickListener(mNotificationOn);
        vibrate.setOnClickListener(mVibrate);
        clearConversation.setOnClickListener(mClearConversation);
        deleteMyAccount.setOnClickListener(mDeleteMyAccount);
        mApplication = (MApplication) getActivity().getApplicationContext();
        userId = MApplication.getString(getActivity(), com.contus.app.Constants.USER_ID);
        phoneNumber = MApplication.getString(getActivity(), com.contus.app.Constants.PHONE_NUMBER);
        phoneCode = MApplication.getString(getActivity(), com.contus.app.Constants.PHONE_NUMBER_CODE);
        //array notification type
        tones = getResources().getStringArray(
                R.array.array_notification_type);
        if (MApplication.getBoolean(getActivity(), Constants.CONVERSATION_SOUND)) {
            radioNotificationOn.setChecked(true);
        } else {
            radioNotificationOn.setChecked(false);
        }
        //Register a callback to be invoked when the checked state of this button changes.
      /*  radioNotificationOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                //setting the boolean
                MApplication.setBoolean(getActivity(), Constants.CONVERSATION_SOUND, isChecked);
            }
        });*/
        String vibrateValue = MApplication.getString(getActivity(), Constants.VIBRATION_TYPE);
        Log.e("vibrateValue",vibrateValue+"");
        if (vibrateValue.equals("0")) {
            txtContactVibrateSettings.setText(getString(R.string.text_vibarate) + " "
                    + tones[0]);
            imgVibrateSelect.setChecked(false);
        } else {
            txtContactVibrateSettings.setText(getString(R.string.text_vibarate) + " "+MApplication.getString(getActivity(), Constants.VIBRATION_TYPE_NAME));
            imgVibrateSelect.setChecked(true);
        }

        mRingtoneManager = new RingtoneManager(getActivity());
        //return root
        return root;
    }

    /**
     * On activity result.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//sents the ringtone that is picked in the Ringtone Picker Dialog
            uri = data.getParcelableExtra(mRingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            Log.e("uri",uri+"");
            if (uri != null) {
//send the output of the selected to a string
                mApplication.storeStringInPreference(
                        Constants.NOTIFICATION_URI,
                        uri.toString());
            }else{
                mApplication.storeStringInPreference(
                        Constants.NOTIFICATION_URI,
                        null);
            }
//this passed the ringtone selected from the user to a new method

//inserts another line break for more data, this times adds the cursor count on the selected item

//set default ringtone
            try {
                Log.e("uririntone", uri + "");
                RingtoneManager.setActualDefaultRingtoneUri(getActivity(), resultCode, uri);
            } catch (Exception localException) {

            }
        }
    }

    /**
     * Show alert dialog.
     *
     * @param title             the title
     * @param msg               the msg
     * @param positiveString    the positive string
     * @param negativeString    the negative string
     * @param dialogType
     * @param clearconversation
     */
    public void showAlertDialog(String title, String msg, String positiveString, String negativeString, final DIALOG_TYPE dialogType, String clearconversation) {
        //Creates a builder for an alert dialog that uses the default alert dialog theme.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set message
        builder.setMessage(msg);
        if (!title.isEmpty())
            //setting the title
            builder.setTitle(title);
        if (dialogType.equals(DIALOG_TYPE.DIALOG_DUAL)) {
            builder.setNegativeButton(negativeString,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog
                            dialog.dismiss();
                        }
                    });
        }
        builder.setPositiveButton(positiveString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecords();
                        dialog.dismiss();
                    }
                });
        //create

        builder.create().show();
    }

    /**
     * Delete records.
     */
    private void deleteRecords() {
        //delete the record
        MDatabaseHelper.deleteRecord(Constants.TABLE_CHAT_DATA, null, null);
        //delete the record
        MDatabaseHelper.deleteRecord(Constants.TABLE_PENDING_CHATS, null, null);
        //delete the record
        MDatabaseHelper.deleteRecord(Constants.TABLE_RECENT_CHAT_DATA, null, null);
        //delete the record
        MDatabaseHelper.deleteRecord(Constants.TABLE_GROUP_MSG_STATUS, null, null);
        //clear chat
        if (!isClearChat) {
            /**  Requesting the response from the given base url**/
            DeleteAccount.getInstance().postDeleteData(new String("delete_account"), userId, phoneCode + phoneNumber
                    , new Callback<DeleteResponseModel>() {
                        @Override
                        public void success(DeleteResponseModel deleteResponseModel, Response response) {
                            if (("1").equals(deleteResponseModel.getSuccess())) {
                                MApplication.setBoolean(getActivity(), com.contus.app.Constants.FIRST_TIME, false);
                                //delete the record
                                MDatabaseHelper.deleteRecord(Constants.TABLE_ROSTER, null, null);
                                //delete the record
                                MDatabaseHelper.deleteRecord(Constants.TABLE_USER_STATUS, null, null);
                                //clear all the prefernce
                                MApplication.clearAllPreference();
                                Toast.makeText(getActivity(), deleteResponseModel.getMsg(), Toast.LENGTH_SHORT).show();/**Toast message wthen search key is wrong**/

                                //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent)
                                // or bindService(Intent, ServiceConnection, int) to communicate with a background Service.
                                Intent mIntent = new Intent(getActivity(), Welcome.class);
                                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //start activity
                                startActivity(mIntent);
                                //finish the activity
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            /**Error message will display when not able to connec t to the server**/
                            MApplication.errorMessage(retrofitError, getActivity());
                        }
                    });
        }


    }
}

