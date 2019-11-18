package com.contus.campaignparticipate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.contus.app.Constants;
import com.contus.responsemodel.CommentDisplayResponseModel;
import com.contus.restclient.CommentsDisplayRestClient;
import com.contus.restclient.CommentsPollRestClient;
import com.contus.views.EndLessListView;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/14/2015.
 */
public class CampaignPollComments extends Activity implements EndLessListView.EndlessListener {
    //Endless List view
    private EndLessListView list;
    //Activity
    private CampaignPollComments mActivityComments;
    //User ID
    private String userId;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> dataResults;
    //Model class data is stored as list
    private ArrayList<CommentDisplayResponseModel.Results.Data> mDataResults;
    //Get the last PAGE
    private String getLastPage;
    //Current PAGE
    private String getCurrentPage;
    //Adapter class
    private CampignPollCommentsAdapter adapter;
    //EditText
    private EditText txtCountry;
    //User poll id
    private String pollId;
    //Page
    private int page = 1;
    //user pol COMMENTS count position
    private int commentsCountPosition;
    //User poll COMMENTS count
    private ArrayList<Integer> campaignPollcommentsCount = new ArrayList<Integer>();
    //Prefernce user poll  COMMENTS count
    private ArrayList<Integer> prefrenceCampaignPollCommentsCount;
    //Smooth progress bar
    private SmoothProgressBar googleNow;
    //Comments
    private String comments;
    //poll add commenst
    private ImageView imageAddComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        /**View are creating when the activity is started**/
        init();

    }
    /**
     * Instantiate the method
     */
    public void init() {
        /**Initializing the activity**/
        list = (EndLessListView) findViewById(R.id.listView);
        mActivityComments = this;
        txtCountry = (EditText) findViewById(R.id.txtCountry);
        googleNow=(SmoothProgressBar)findViewById(R.id.google_now);
        imageAddComments=(ImageView)findViewById(R.id.imageAddComments);
        //User id from the prefernce
        userId = MApplication.getString(mActivityComments, Constants.USER_ID);
        //poll id from the prefernce
        pollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //comments count position
        commentsCountPosition=getIntent().getExtras().getInt(Constants.COMMENTS_COUNT_POSITION);
        //user id
        userId = MApplication.getString(mActivityComments, Constants.USER_ID);
        //array list
        dataResults = new ArrayList<>();
        //array list
        mDataResults = new ArrayList<>();
        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
        // The Adapter provides access to the data items.
        // The Adapter is also responsible for making a View for each item in the data set.
        adapter = new CampignPollCommentsAdapter(mActivityComments, dataResults, R.layout.activity_comments_singleview, pollId, commentsCountPosition);
       //setting the foolter view
        list.setLoadingView(R.layout.layout_loading);
        //setting the adapter
        list.setCampignPollComents(adapter);
        //setting the listner
        list.setListener(this);
    }

    private void campaignPollCommentsDisplayRequest() {
        if (MApplication.isNetConnected(mActivityComments)) {
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            /**  Requesting the response from the given base url**/
            CommentsDisplayRestClient.getInstance().postPollCommentsDetsils(new String("getPoll_comment"), new String(pollId), new String("10"), new Integer(page)
                    , new Callback<CommentDisplayResponseModel>() {
                @Override
                public void success(CommentDisplayResponseModel welcomeResponseModel, Response response) {
                    if(("1").equals(welcomeResponseModel.getSuccess())) {
                        campaignPollCommentsDisplayResponse(welcomeResponseModel);
                    }
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    MApplication.errorMessage(retrofitError, mActivityComments);
                }

            });
            //progress bar stops
            googleNow.progressiveStop();
            //visiblity gomne
            googleNow.setVisibility(View.GONE);
        }

    }

    /**
     * campaignPollCommentsDisplayResponse
     * @param welcomeResponseModel
     */
    private void campaignPollCommentsDisplayResponse(CommentDisplayResponseModel welcomeResponseModel) {
        //data results
        dataResults = welcomeResponseModel.getResults().getData();
        //getting the last page
        getLastPage = welcomeResponseModel.getResults().getLastPage();
        //Getting the current page
        getCurrentPage = welcomeResponseModel.getResults().getCurrentPage();
        //If data results is not empty
        if (!dataResults.isEmpty()) {
            //Matching the current page equal to1
            if (Integer.parseInt(getCurrentPage) == 1) {
                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                // The Adapter provides access to the data items.
                // The Adapter is also responsible for making a View for each item in the data set.
                adapter = new CampignPollCommentsAdapter(mActivityComments, dataResults, R.layout.activity_comments_singleview, pollId, commentsCountPosition);
             //setting the bottom view
                list.setLoadingView(R.layout.layout_loading);
                //setting the adapter
                list.setCampignPollComents(adapter);
            } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                //the data is added into the array adapterfrom the response
                list.addCampignPollsCommetsData(dataResults);
            }

        }
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param campaignPollCommentsClick
     */
    public void onClick(final View campaignPollCommentsClick) {
        if(campaignPollCommentsClick.getId()==R.id.imagBackArrow){
            //Finishing the activity
            this.finish();
        }else if(campaignPollCommentsClick.getId()==R.id.imageAddComments){
//Campaign comkment add request
            campaignCommentsPollAddRequest();
        }
    }

    @Override
    public void loadData() {
//Increamenting the PAGE number on scrolling
        page++;
        //Request for display the campaign
        campaignPollCommentsDisplayRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //hiding the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
  //Comments disply request
        campaignPollCommentsDisplayRequest();

    }

    /**
     * Comments add request
     */
    private void campaignCommentsPollAddRequest() {
        if (MApplication.isNetConnected(mActivityComments)) {//If net is connected
            comments = MApplication.getEncodedString(txtCountry.getText().toString().trim());//comments from the edit text
            if (!comments.isEmpty()) {
           imageAddComments.setClickable(false);//visible
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
                /**  Requesting the response from the given base url**/
                CommentsPollRestClient.getInstance().postPollComments(new String("poll_comment"), new String(userId), new String(pollId), new String(comments)
                        , new Callback<CommentDisplayResponseModel>() {
                    @Override
                    public void success(CommentDisplayResponseModel comments, Response response) {
                        if (("1").equals(comments.getSuccess())) {
                            campaignCommentsPollAddResponse(comments);//add request comments
                        }
                    }
                    @Override
                    public void failure(RetrofitError retrofitError) {
                        MApplication.errorMessage(retrofitError, mActivityComments);//error resposne
                    }
                });
                googleNow.progressiveStop();//progress bar stops
                googleNow.setVisibility(View.GONE);//visiblity gone
            }
        }

    }

    /**
     * campaignCommentsPollAddResponse
     * @param comments
     */
    private void campaignCommentsPollAddResponse(CommentDisplayResponseModel comments) {
//array list
        List<CommentDisplayResponseModel.Results.Data> lisData = comments.getResults().getData();
        //if its is empty
        if (lisData.isEmpty()) {
            //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
            // The Adapter provides access to the data items.
            // The Adapter is also responsible for making a View for each item in the data set.
            adapter = new CampignPollCommentsAdapter(mActivityComments, dataResults, R.layout.activity_comments_singleview, pollId, commentsCountPosition);
           //setting in bottom view
            list.setLoadingView(R.layout.layout_loading);
            //setting the adapter
            list.setCampignPollComents(adapter);
        } else {
            mDataResults.clear();
            //Adding the data from the array list
            mDataResults.add(comments.getResults().getData().get(0));
            //adding the details into the array list
            list.addCampignPollsCommetsData(mDataResults);
            //setting the text empty
            txtCountry.setText("");
            //hihding the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //visinle
        imageAddComments.setClickable(true);
        //loading the array
        prefrenceCampaignPollCommentsCount= MApplication.loadArray(mActivityComments,campaignPollcommentsCount,Constants.CAMPAIGN_POLL_COMMENTS_COUNT,Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
       //setting the in particular position
        prefrenceCampaignPollCommentsCount.set(commentsCountPosition, Integer.valueOf(comments.getCount()));
     //saving oin prefernce
        MApplication.saveArray(mActivityComments, prefrenceCampaignPollCommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
        //saving oin prefernce
        MApplication.setString(mActivityComments, Constants.COMMENTS_COUNT_POLL_REVIEW,comments.getCount());

    }

}
