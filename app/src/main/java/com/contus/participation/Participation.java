package com.contus.participation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.responsemodel.ParticipateResponseModel;
import com.contus.restclient.ParticipateRestClient;
import com.contus.views.EndLessListView;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/14/2015.
 */
public class Participation extends Activity implements EndLessListView.EndlessListener {
    //Endless list view
    private EndLessListView list;
    //Activity
    private Participation participationActivity;
    //web user text view
    private TextView txtWebUser;
    //Data results from the response
    private List<ParticipateResponseModel.Results.Data> dataResults;
    //Get last PAGE
    private String getLastPage;
    //Get current PAGE
    private String getCurrentPage;
    //Custom participation adapter
    private CustomParticpationPollAdapter customParticipatetion;
    //Poll id
    private String pollId;
    //Text view app user
    private TextView txtAppUser;
    //Page
    private int page;
    // Progress bar
    private SmoothProgressBar googleNow;
    // Layout internet connection
    private RelativeLayout internetConnection;
    // Relative layout
    private RelativeLayout relativeList;
    //Relative
    private RelativeLayout relative;
    //Internet retry
    private TextView internetRetry;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partipation);
        /**View are creating when the activity is started**/
        init();

    }

    /**
     * Instantiate the method
     */
    public void init() {
        /**Initializing the activity**/
        participationActivity = this;
        list = (EndLessListView) findViewById(R.id.listView);
        txtWebUser = (TextView) findViewById(R.id.txtWebUser);
        txtAppUser = (TextView) findViewById(R.id.txtAppUser);
        googleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        internetConnection = (RelativeLayout) findViewById(R.id.internetConnection);
        relativeList = (RelativeLayout) findViewById(R.id.list);
        relative = (RelativeLayout) findViewById(R.id.relative);
        internetRetry = (TextView) findViewById(R.id.internetRetry);
        //POll Id
        pollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //Setting the listner
        list.setListener(this);
        mAdView = (AdView) findViewById(R.id.adView);
        MApplication.googleAd(mAdView);
        //Setting the PAGE number
        page = 1;
        //Interface definition for a callback to be invoked when a view is clicked.
        internetRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Participation survey request
                participationSurveyRequest();
            }
        });
        //Participation survey request
        participationSurveyRequest();
    }

    /**
     * This method is used to request the poll survey and bind in the list view
     */
    private void participationSurveyRequest() {
        //If net is connected
        if (MApplication.isNetConnected(Participation.this)) {
            //View visible
            relative.setVisibility(View.VISIBLE);
            //Internet connection layout invisible
            internetConnection.setVisibility(View.GONE);
            //List visible
            relativeList.setVisibility(View.VISIBLE);
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            /**  Requesting the response from the given base url**/
            ParticipateRestClient.getInstance().postParticipate(new String("poll_participate_users"), new String(pollId), new Integer(page)
                    , new Callback<ParticipateResponseModel>() {
                @Override
                public void success(ParticipateResponseModel participateResponseModel, Response response) {
                    //The users participated using the web
                    String webusers = participateResponseModel.getWebcount();
                    //If the web users is empty then the web users is set as 0 in text view
                    //Else the web users count is set
                    if (("").equals(webusers)) {
                        txtWebUser.setText("0" + "  Web Users");
                    } else {
                        txtWebUser.setText(webusers + "  Web Users");
                    }
                    //If the succes matches with the value 1 then the datat is binded into the adapter
                    if (("1").equals(participateResponseModel.getSuccess())) {
                        participationSurveyResponse(participateResponseModel);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, participationActivity);
                    //Progressbar stops
                    googleNow.progressiveStop();
                    //Progressbar became invisible
                    googleNow.setVisibility(View.GONE);
                }

            });
        } else {
            //Relative invisible
            relative.setVisibility(View.INVISIBLE);
            //Internet connection visible
            internetConnection.setVisibility(View.VISIBLE);
            //Relative list visible
            relativeList.setVisibility(View.INVISIBLE);
        }
         //Progressive bar stop
        googleNow.progressiveStop();
        //Visiblity gone
        googleNow.setVisibility(View.GONE);
    }

    /**
     * This method is used to bind the data into the views
     * @param participateResponseModel participateResponseModel
     */
    private void participationSurveyResponse(ParticipateResponseModel participateResponseModel) {

        //Setting the app count in text view
        txtAppUser.setText(participateResponseModel.getResults().getTotal() + " App Users");
        //Details from the response
        dataResults = participateResponseModel.getResults().getData();
        //Getting the last PAGE
        getLastPage = participateResponseModel.getResults().getLastPage();
        //Getting the current PAGE
        getCurrentPage = participateResponseModel.getResults().getCurrentPage();
        //If details are not empty
        if (!dataResults.isEmpty()) {
            //If PAGE equal to 1 setting the adapter initially
            if (Integer.parseInt(getCurrentPage) == 1) {
                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                // The Adapter provides access to the data items.
                // The Adapter is also responsible for making a View for each item in the data set.
                customParticipatetion = new CustomParticpationPollAdapter(participationActivity, dataResults, R.layout.activity_likes_singleview);
                //Set bottom footer view
                list.setLoadingView(R.layout.layout_loading);
                //Sets the data behind this ListView.
                list.setCustomParticipationAdapter(customParticipatetion);
            } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                //the data is added into the array adapterfrom the response
                list.addCustomParticipate(dataResults);
            }
        }
    }

    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        page++;
        //Participation survey request
        participationSurveyRequest();
    }
    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param mParticipateAction
     */
    public void onClick(final View mParticipateAction) {
        if (mParticipateAction.getId() == R.id.imagBackArrow) {
            //finishing the activity
            this.finish();
        }
    }
}
