package com.contus.pollreview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.participation.CustomParticpationPollAdapter;
import com.contus.responsemodel.ParticipateResponseModel;
import com.contus.restclient.PollReviewQuestionRestClient;
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
public class PollReviewQuestionParticipation extends Activity implements EndLessListView.EndlessListener {
    //Endless list view
    private EndLessListView list;
    //Activity
    private AdView mAdView;
    private PollReviewQuestionParticipation participationActivity;
    //web user text view
    private TextView webUserCount;
    //Data results from the response
    private List<ParticipateResponseModel.Results.Data> pollAnswerReview;
    //Get last PAGE
    private String getLastPage;
    //Get current PAGE
    private String getCurrentPage;
    //Custom participation adapter
    private CustomParticpationPollAdapter customPollAnswerReview;
    //Poll id
    private String pollIdAnswerReview;
    //Text view app user
    private TextView appUserCount;
    //Page
    private int pageCount;
    // Progress bar
    private SmoothProgressBar googleNow;
    // Layout internet connection
    private RelativeLayout relativeinternetConnection;
    // Relative layout
    private RelativeLayout relativeList;
    //Relative
    private RelativeLayout relative;
    //Intenet retry
    private TextView internetRetry;
    //Poll answer option
    private String pollAnswerOption;
    //Web users count
    private String webusersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partipation);
        /**View are creating when the activity is started**/
        mAdView = (AdView) findViewById(R.id.adView);
        init();

    }
    /**
     * Instantiate the method
     */
    public void init(){
        list = (EndLessListView)findViewById(R.id.listView);
        /**Initializing the activity**/
        participationActivity=this;
        webUserCount =(TextView)findViewById(R.id.txtWebUser);
        appUserCount =(TextView)findViewById(R.id.txtAppUser);
        googleNow=(SmoothProgressBar)findViewById(R.id.google_now);
        relativeinternetConnection = (RelativeLayout)findViewById(R.id.internetConnection);
        relativeList = (RelativeLayout)findViewById(R.id.list);
        relative=(RelativeLayout)findViewById(R.id.relative);
        internetRetry=(TextView)findViewById(R.id.internetRetry);
        //Poll id from the intent
        pollIdAnswerReview =getIntent().getExtras().getString(Constants.POLL_ID);
        //Poll answer clicked from the intent
        pollAnswerOption=getIntent().getExtras().getString(Constants.POLL_ANSWER_OPTION);
        //Setting the listner
        list.setListener(this);
        //Page number 1
        pageCount =1;
        //Interface definition for a callback to be invoked when a view is clicked.
        internetRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Participation survey request
                pollAnswerSurveyRequest();
            }
        });
        //Participation survey request
        pollAnswerSurveyRequest();
    }
    /**
     * This method is used to request the poll survey and bind in the list view
     */
    private void pollAnswerSurveyRequest() {
        //If net is connected
        if (MApplication.isNetConnected(PollReviewQuestionParticipation.this)) {
            //View is visible
            relative.setVisibility(View.VISIBLE);
            //Relative internet connection gone
            relativeinternetConnection.setVisibility(View.GONE);
            //List is visible
            relativeList.setVisibility(View.VISIBLE);
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            /**  Requesting the response from the given base url**/
            PollReviewQuestionRestClient.getInstance().postPollReview(new String("pollanswer_survey"), new String(MApplication.getString(this,Constants.USER_ID)), new String (pollIdAnswerReview),new String(pollAnswerOption),new Integer(pageCount)
                    , new Callback<ParticipateResponseModel>() {
                @Override
                public void success(ParticipateResponseModel participateResponseModel, Response response) {
                    //If the succes matches with the value 1 then the datat is binded into the adapter
                    if(("1").equals(participateResponseModel.getSuccess())) {
                        pollAnswerSurveyResponse(participateResponseModel);
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
        } else{
            //Relative invisible
            relative.setVisibility(View.INVISIBLE);
            //Internet connection visible
            relativeinternetConnection.setVisibility(View.VISIBLE);
            //Relative list visible
            relativeList.setVisibility(View.INVISIBLE);
        }
        //Progressive bar stop
        googleNow.progressiveStop();
        //Visiblity gone
        googleNow.setVisibility(View.GONE);
    }

    /**
     *  This method is used to bind the data into the views
     * @param participateResponseModel participateResponseModel
     */
    private void pollAnswerSurveyResponse(ParticipateResponseModel participateResponseModel) {
       //The users participated using the web
        webusersCount = participateResponseModel.getWebcount();
        //If the web users is empty then the web users is set as 0 in text view
        //Else the web users count is set
        if (("").equals(webusersCount)) {
            webUserCount.setText("0" + "  Web Users");
        } else {
            webUserCount.setText(webusersCount + "  Web Users");
        }
        //Setting the app count in text view
        appUserCount.setText(participateResponseModel.getResults().getTotal() + " App Users");
        //Details from the response
        pollAnswerReview = participateResponseModel.getResults().getData();
        //Getting the last PAGE
        getLastPage = participateResponseModel.getResults().getLastPage();
        //Getting the current PAGE
        getCurrentPage = participateResponseModel.getResults().getCurrentPage();
        //If details are not empty
        if (!pollAnswerReview.isEmpty()) {
            //If PAGE equal to 1 setting the adapter initially
            if (Integer.parseInt(getCurrentPage) == 1) {
                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                // The Adapter provides access to the data items.
                // The Adapter is also responsible for making a View for each item in the data set.
                customPollAnswerReview = new CustomParticpationPollAdapter(participationActivity, pollAnswerReview, R.layout.activity_likes_singleview);
                //Set bottom footer view
                list.setLoadingView(R.layout.layout_loading);
                //Sets the data behind this ListView.
                list.setCustomParticipationAdapter(customPollAnswerReview);
            } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                //the data is added into the array adapterfrom the response
                list.addCustomParticipate(pollAnswerReview);
            }
        }

    }

    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        pageCount++;
        //Participation survey request
        pollAnswerSurveyRequest();
    }


    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param pollAnswerSurveyClickAction
     */
    public void onClick(final View pollAnswerSurveyClickAction) {
        if (pollAnswerSurveyClickAction.getId() == R.id.imagBackArrow) {
            //finishing the activity
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MApplication.googleAd(mAdView);
    }
}
