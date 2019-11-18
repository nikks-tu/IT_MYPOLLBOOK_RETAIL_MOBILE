package com.contus.feedback;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * Created by user on 7/17/2015.
 */
public class Feedback extends Fragment {
    //View
    private View parentView;
    //Text send mail
    private TextView txtSendMail;
    //email
    private String email;
    //smooth progress bar
    private SmoothProgressBar googleNow;
    //relative layout
    private RelativeLayout relative;
    //Internet connection
    private RelativeLayout internetConnection;
    //button retry
    private TextView internetFeedbackButtonRetry;
    //Google ad
    private AdView mAdView;
    private Activity feebackFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**View are creating when the activity is started**/
        parentView = inflater.inflate(R.layout.fragment_feedback, container, false);
        txtSendMail = (TextView) parentView.findViewById(R.id.txtSendMail);
        googleNow = (SmoothProgressBar) parentView.findViewById(R.id.google_now);
        relative = (RelativeLayout) parentView.findViewById(R.id.relative);
        mAdView = (AdView) parentView.findViewById(R.id.adView);
        internetConnection = (RelativeLayout) parentView.findViewById(R.id.internetConnection);
        internetFeedbackButtonRetry = (TextView) parentView.findViewById(R.id.internetRetry);
        feebackFragment=getActivity();
        //Interface definition for a callback to be invoked when a view is clicked.
        txtSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Giving the feedback through email
                MApplication.gmailFeedback(feebackFragment);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        internetFeedbackButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If net is connected
                if (MApplication.isNetConnected(feebackFragment)) {
                   // details request
                    feedBackDetailsLoad();
                } else {
                    //View invisible
                    relative.setVisibility(View.INVISIBLE);
                    //View visible
                    internetConnection.setVisibility(View.VISIBLE);
                }
            }
        });
         //return views
        return parentView;
    }

    /**
     * Request and response api method
     */
    private void feedBackDetailsLoad() {
        internetConnection.setVisibility(View.INVISIBLE);
        /**Custom progressbar start**/
        googleNow.progressiveStart();
        /**  Requesting the response from the given base url**/
        ContactUsRestClient.getInstance().postContactUs(new String("contactapi")
                , new Callback<ContactUsResponseModel>() {
            @Override
            public void success(ContactUsResponseModel welcomeResponseModel, Response response) {
                /**If response is success this method is called**/
                feedbackResponse(welcomeResponseModel);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                //Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, feebackFragment);
                //Progressbar stops
                googleNow.progressiveStop();
                //Progressbar became invisible
                googleNow.setVisibility(View.GONE);
            }

        });
    }

    /**
     * Feedback resposne from the server
     * @param contactUsResponseModel contactUsResponseModel
     */
    private void feedbackResponse(ContactUsResponseModel contactUsResponseModel) {
        //If the succes matches with the value 1 then the datat is binded into the adapter
        //else if get success equals 1 and PAGE 1 or get successqueals 0 and PAGE equal to 1 views will be visible
        if (("1").equals(contactUsResponseModel.getSuccess())) {
            //view visible
            relative.setVisibility(View.VISIBLE);
            //view gone
            googleNow.setVisibility(View.GONE);
            /**welcome msg stored in model class are retrived**/
            email = contactUsResponseModel.getResults().get(0).getAdminEmail();
            //Setting the string
            MApplication.setString(feebackFragment, Constants.EMAIL, email);
            //progressbar stops
            googleNow.progressiveStop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //If net is connected
        if (MApplication.isNetConnected(feebackFragment)) {
            // details request
            feedBackDetailsLoad();
        } else {
            //view invisble
            relative.setVisibility(View.INVISIBLE);
            //view gone
            googleNow.setVisibility(View.GONE);
            //view visible
            internetConnection.setVisibility(View.VISIBLE);
        }
        //Google ad
        MApplication.googleAd(mAdView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            feebackFragment = (Activity) context;
        }

    }
}
