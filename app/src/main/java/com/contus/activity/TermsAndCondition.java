/**
 * TearmsAndCondition.java
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

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;

import com.contus.app.Constants;
import com.contus.responsemodel.TermsAndConditionModel;
import com.contus.restclient.TermsConditionRestClient;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.polls.polls.R;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/9/2015.
 */
public class TermsAndCondition extends Activity implements Constants {
    /**SmoothProgressBar**/
    private SmoothProgressBar googleNow;
    /**Webview**/
    private WebView txtInformation;
    /**Terms and condition activity**/
    private TermsAndCondition mTermsAndCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        /**View are creating when the activity is started**/
        init();
    }

    /**
     * Instantiate the method
     */
    private void init() {
        //Initializing the activity
        mTermsAndCondition =this;
        //smooth progress bar
        googleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        //web view
        txtInformation = (WebView) findViewById(R.id.txtInformation);

    }

    /**
     * The onclicklistener will be called when any widget like button, text, image etc is either clicked or touched or
     * focused upon by the user.
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if(clickedView.getId()==R.id.imgBackArrow){
            //Finishes the activity
            finish();
        }
    }

    /**
     * The values from the response are retrived using model class
     * @param termsResponseModel model class
     */
    private void termsResponse(TermsAndConditionModel termsResponseModel) {
        /**welcome msg stored in model class are retrived**/
        String termsMessage = termsResponseModel.getResults().getInformation();
        /** SHort description **/
        txtInformation.setBackgroundColor(Color.TRANSPARENT);
        /** Laod data into the web view **/

        /*txtInformation.loadData(Html.fromHtml("<html><body>" + termsMessage + "</body></html>").toString(),
                "text/html", "utf-8");*/

       // txtInformation.loadUrl("www.mypollbook.com/tnc.htm");
       //progressive stop
        //txtInformation.loadData(termsMessage, "text/html", null);
        txtInformation.loadData(Html.fromHtml("<html><body>" + termsMessage + "</body></html>").toString(),"text/html", "utf-8");
        googleNow.progressiveStop();
        //set visiblity gone
        googleNow.setVisibility(View.GONE);
    }
    /**
     * Request and response api method
     */
    private void termsDetailsLoad() {
        /**Custom progressbar start**/
        googleNow.progressiveStart();
        /**  Requesting the response from the given base url**/
        TermsConditionRestClient.getInstance().postTermsData(new String("termsapi")
                , new Callback<TermsAndConditionModel>() {
            @Override
            public void success(TermsAndConditionModel termsResponseModel, Response response) {
                /**If response is success this method is called**/
                termsResponse(termsResponseModel);
            //    Log.d("terms&conditions",response.toString());
               // txtInformation.loadData("", "text/html; charset=utf-8", "UTF-8");
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                //Progresisve stop
                googleNow.progressiveStop();
                //Set visiblity gone
                googleNow.setVisibility(View.GONE);
                //Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, mTermsAndCondition);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        /**Sending the request and getting the response using the method**/
        termsDetailsLoad();
    }
}
