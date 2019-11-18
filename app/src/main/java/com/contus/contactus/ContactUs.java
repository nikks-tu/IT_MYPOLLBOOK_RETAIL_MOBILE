package com.contus.contactus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.responsemodel.ContactUsResponseModel;
import com.contus.restclient.ContactUsRestClient;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/2/2015.
 */
public class ContactUs extends Fragment {
    //View
    View parentView;
    //Image view chat
    FloatingActionButton imgChat;
    //Image view call
  //  private ImageView imgCall;
    //Progressbar
    private SmoothProgressBar googleNow;
    //Contact number display
   // private TextView txtContactusContactDisplay;
    //Mobile number display
 //   private TextView txtContactusMobilenoDisplay;
    //Email us display
    private TextView txtContactusEmailusDisplay;
    //Scroll bar
    private LinearLayout relative;
    //Internet connection relative layout
    private RelativeLayout internetConnection;
    //Conatct us retry button
    TextView internetContactUsRetry;
    //Google ad
    private AdView mAdView;
    private Activity contectFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_contactus, container, false);
        mAdView =  parentView.findViewById(R.id.adView);
        googleNow =  parentView.findViewById(R.id.google_now);
        relative =  parentView.findViewById(R.id.scrollView3);
   //     txtContactusContactDisplay =  parentView.findViewById(R.id.txt_contactus_contact_display);
  //      txtContactusMobilenoDisplay = parentView.findViewById(R.id.txt_contactus_mobileno_display);
        txtContactusEmailusDisplay =  parentView.findViewById(R.id.txt_contactus_emailus_display);
        internetConnection =  parentView.findViewById(R.id.internetConnection);
        internetContactUsRetry =  parentView.findViewById(R.id.internetRetry);
        imgChat =  parentView.findViewById(R.id.imgChat);
//        imgCall =  parentView.findViewById(R.id.imgCall);
        contectFragment=getActivity();
       //Interface definition for a callback to be invoked when a view is clicked.
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Email to contact
                MApplication.gmail(contectFragment);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
   /*     imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mobile number call option is performed from the app
                callToMobile();
            }
        }); */
        //Interface definition for a callback to be invoked when a view is clicked.
        internetContactUsRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If net is connected contact us detail request will be happened
                //else the view will be invisible
                if (MApplication.isNetConnected(contectFragment)) {
                    //Request for contact details
                    contactUsDetailsLoad();
                } else {
                    //View invisible
                    relative.setVisibility(View.INVISIBLE);
                    //View visible
                    internetConnection.setVisibility(View.VISIBLE);
                }
            }
        });
        //Returning the view
        return parentView;
    }

    /**
     * callToMobile
     */
    private void callToMobile() {
        if(!MApplication.getString(contectFragment,Constants.MOBILE_NUMBER).isEmpty()) {
            //Activity Action: Perform a call to someone specified by the data.
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            //Set the data this intent is operating on.
            phoneIntent.setData(Uri.parse("tel:"+MApplication.getString(contectFragment,Constants.MOBILE_NUMBER)));
            try {
                //Starting the ACTION
                startActivity(phoneIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                //Toast message will display if the call failed
                Toast.makeText(
                        contectFragment, getResources().getString(R.string.call_failed), Toast.LENGTH_SHORT).show();
                Log.e("",""+ex);
            }
        }

    }

    /**
     * Request and response api method
     */
    private void contactUsDetailsLoad() {
        //Invisible
        internetConnection.setVisibility(View.INVISIBLE);
        /*Custom progressbar start**/
        googleNow.progressiveStart();
        /*  Requesting the response from the given base url**/
        ContactUsRestClient.getInstance().postContactUs(Constants.CONTACT_US_ACTION
                , new Callback<ContactUsResponseModel>() {
            @Override
            public void success(ContactUsResponseModel welcomeResponseModel, Response response) {
                /*If response is success this method is called**/
                contactUsResponse(welcomeResponseModel);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                //Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError,contectFragment);
                //Progressbar stops
                googleNow.progressiveStop();
                //Progressbar became invisible
                googleNow.setVisibility(View.GONE);
            }
        });
    }

    /**
     * This method is used to bind the data in views
     * @param contactUsResponseModel contactUsResponseModel
     */
    private void contactUsResponse(ContactUsResponseModel contactUsResponseModel) {
    //If the success message quuals to 1 then the data are binded into views
        if (("1").equals(contactUsResponseModel.getSuccess())) {
            //View visible
            relative.setVisibility(View.VISIBLE);
            //View gone
            googleNow.setVisibility(View.GONE);
           //Setting the contact number in text view
   //         txtContactusContactDisplay.setText(contactUsResponseModel.getResults().get(0).getAdminContactno());
            //Setting the mobile number in text view
   //         txtContactusMobilenoDisplay.setText(contactUsResponseModel.getResults().get(0).getAdminMobileno());
            MApplication.setString(contectFragment,Constants.MOBILE_NUMBER,contactUsResponseModel.getResults().get(0).getAdminMobileno());
            //Setting the email us in text view
            txtContactusEmailusDisplay.setText(contactUsResponseModel.getResults().get(0).getAdminEmail());
            //Setting the email in prefernce
            MApplication.setString(contectFragment, Constants.EMAIL, contactUsResponseModel.getResults().get(0).getAdminEmail());
            //Progress bat stops
            googleNow.progressiveStop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //If net is connected
        if (MApplication.isNetConnected(contectFragment)) {
            //Contact us request to the server
            contactUsDetailsLoad();
        } else {
            //View invisible
            relative.setVisibility(View.INVISIBLE);
            //Goggle now visiblity gone
            googleNow.setVisibility(View.GONE);
            //Internet connecyion visible
            internetConnection.setVisibility(View.VISIBLE);
        }
        //Google ad
        MApplication.googleAd(mAdView);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            contectFragment = (Activity) context;
        }

    }
}
