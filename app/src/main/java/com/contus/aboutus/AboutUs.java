/**
 * AboutUs.java
 * <p/>
 * Abount us screen.The details are loaded from the response
 *
 * @category Contus
 * @package com.contus.activity
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contus.aboutus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.responsemodel.TermsAndConditionModel;
import com.contus.restclient.TermsConditionRestClient;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/2/2015.
 */
public class AboutUs extends Fragment {
    /**View**/
    private View parentView;
    /*8SmoothProgressbar**/
    private SmoothProgressBar googleNow;
    /**WebView**/
    private WebView webDetails;
    /**Admob view**/
    private AdView mAdView;
    /**Adrequestview view**/
    private AdRequest adRequest;
    /**RelativeLayout internet connection**/
    private RelativeLayout internetConnection;
    /**Textview btn retry**/
    private TextView btnRetry;
    /**Relative layout white space**/
    private RelativeLayout relativeWhiteSpace;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**Instantiates a layout XML file into its corresponding View objects. **/
        parentView = inflater.inflate(R.layout.fragment_aboutus, container, false);
        /**Initializing the SmoothProgressBar views**/
        googleNow = (SmoothProgressBar) parentView.findViewById(R.id.google_now);
        /**Initializing the webview*/
        webDetails = (WebView) parentView.findViewById(R.id.webDetails);
        internetConnection = (RelativeLayout) parentView.findViewById(R.id.internetConnection);
        relativeWhiteSpace = (RelativeLayout) parentView.findViewById(R.id.relativeWhiteSpace);
        btnRetry = (TextView) parentView.findViewById(R.id.internetRetry);
        /**Initializing the AdView*/
        mAdView = (AdView) parentView.findViewById(R.id.adView);
        /**Adrequest**/
        adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        /**Load ad in ad mob view**/
        mAdView.loadAd(adRequest);
       //Interface definition for a callback to be invoked when a view is clicked.
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform ACTION on click
                termsDetailsLoad();
            }
        });
        // Inflate the layout for this fragment
        return parentView;
    }


    /**
     * Request and response api method
     */
    private void termsDetailsLoad() {
        if (MApplication.isNetConnected(getActivity())) {
            internetConnection.setVisibility(View.GONE);
            /**Custom progressbar start**/
            googleNow.progressiveStart();
            /**  Requesting the response from the given base url**/
            TermsConditionRestClient.getInstance().postAboutData(new String("aboutusapi")
                    , new Callback<TermsAndConditionModel>() {
                @Override
                public void success(TermsAndConditionModel termsResponseModel, Response response) {
                    /**If response is success this method is called**/
                    termsResponse(termsResponseModel);
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, getActivity());
                    //Progressbar stops
                    googleNow.progressiveStop();
                    //Progressbar became invisible
                    googleNow.setVisibility(View.GONE);
                }

            });
        } else {
            //This layout will be visible when the internet connection is not available
            internetConnection.setVisibility(View.VISIBLE);
            //View invlisible
            webDetails.setVisibility(View.GONE);
            //View invisible
            relativeWhiteSpace.setVisibility(View.GONE);
        }
    }

    /**
     * The values from the response are retrived using model class
     * @param termsResponseModel model class
     */
    private void termsResponse(TermsAndConditionModel termsResponseModel) {
        /**welcome msg stored in model class are retrived**/
        String termsMessage = termsResponseModel.getResults().getInformation();
        /** Laod data into the web view **/
        webDetails.loadData(Html.fromHtml("<html><body>" + termsMessage + "</body></html>").toString(),"text/html", "utf-8");
        webDetails.setVisibility(View.VISIBLE);
        /**Progressbar stop**/
        googleNow.progressiveStop();
        /*8Setting the progress bar visiblity**/
        googleNow.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        /**Request and response from this method**/
        termsDetailsLoad();
    }
}
