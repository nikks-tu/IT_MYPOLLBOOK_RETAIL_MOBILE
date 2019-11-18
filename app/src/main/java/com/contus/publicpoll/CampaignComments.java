package com.contus.publicpoll;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
public class CampaignComments extends Activity implements EndLessListView.EndlessListener {
    //Endless List view
    private EndLessListView list;
    //Activity
    private CampaignComments campaignCommentsActivity;
    //User ID
    private String userId;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> dataResults;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> mDataResults;
    //Current PAGE
    private String getCurrentPage;
    //Adapter class
    private CampaignCommentsAdapter adapter;
    //EditText
    private EditText addComments;
    //Comments
    private String comments;
    //Campaing iD
    private String campaignId;
    // PAGE
    private int page = 1;
    //Smooth progress bar
    private SmoothProgressBar googleNow;
    //Internet connection
    private RelativeLayout internetConnection;
    //Mcomments
    private String mComments;
    //Comments count position
    private int commentsCountPosition;
    //Total COMMENTS
    private String totalComments;
    //Prefernce COMMENTS count
    private ArrayList<Integer> prefrenceCommentsCount;
    //Camapign COMMENTS count
    private ArrayList<Integer> campaigncommentsCount = new ArrayList<Integer>();
    //Campaign Comments add
    private List<CommentDisplayResponseModel.Results.Data> campaignCommentsAdd;
    //imageview
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
        campaignCommentsActivity = this;
        list = (EndLessListView) findViewById(R.id.listView);
        addComments = (EditText) findViewById(R.id.txtCountry);
        googleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        internetConnection = (RelativeLayout) findViewById(R.id.internetConnection);
        imageAddComments=(ImageView)findViewById(R.id.imageAddComments);
        //User id from the prefernce
        userId = MApplication.getString(campaignCommentsActivity, Constants.USER_ID);
        //Camapign id from the intent
        campaignId = getIntent().getExtras().getString(Constants.CAMPAIGN_ID);
        //Position  from the intent
        commentsCountPosition = getIntent().getExtras().getInt(Constants.COMMENTS_COUNT_POSITION);
        //array list initialization
        dataResults = new ArrayList<>();
        //array list initialization
        mDataResults = new ArrayList<>();
        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
        // The Adapter provides access to the data items.
        // The Adapter is also responsible for making a View for each item in the data set.
        adapter = new CampaignCommentsAdapter(campaignCommentsActivity, dataResults, R.layout.activity_comments_singleview, totalComments, commentsCountPosition, campaignId, userId);
        //Set bottom footer view
        list.setLoadingView(R.layout.layout_loading);
        //Sets the data behind this ListView.
        list.setAdapter(adapter);
        //Camapign COMMENTS display request
        campaignCommentsDisplayRequest(page);
        //Setting the lisner
        list.setListener(this);
    }

    /**
     * Camapign Comments display request
     * @param page
     */
    public void campaignCommentsDisplayRequest(int page) {
        //Page number
        int pageNo = page;
        //If net is connected
        if (MApplication.isNetConnected(campaignCommentsActivity)) {
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            /**  Requesting the response from the given base url**/
            CommentsDisplayRestClient.getInstance().postCommentsDetsils(new String("getCamp_comments"), new String(campaignId), new String("10"), new Integer(pageNo)
                    , new Callback<CommentDisplayResponseModel>() {
                @Override
                public void success(CommentDisplayResponseModel welcomeResponseModel, Response response) {
                    //If the succes matches with the value 1 then the datat is binded into the adapter
                    if (("1").equals(welcomeResponseModel.getSuccess())) {
                        campaignCommentsDisplayResponse(welcomeResponseModel);
                    }
                    //Progressive stop
                    googleNow.progressiveStop();
                    //Visiblity gone
                    googleNow.setVisibility(View.GONE);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, campaignCommentsActivity);
                    //Progressbar stops
                    googleNow.progressiveStop();
                    //Progressbar became invisible
                    googleNow.setVisibility(View.GONE);
                }

            });
        }
    }

    /**
     * Campaign COMMENTS from the response
     * @param welcomeResponseModel -model class
     */
    private void campaignCommentsDisplayResponse(CommentDisplayResponseModel welcomeResponseModel) {
        //Toatl COMMENTS from the response
        totalComments = welcomeResponseModel.getResults().getTotal();
        //Data from the response
        dataResults = welcomeResponseModel.getResults().getData();
        //Get the current PAGE from the response
        getCurrentPage = welcomeResponseModel.getResults().getCurrentPage();
        //If dataResults is not empty matches with the PAGE number
        if (!dataResults.isEmpty()) {
            //If the current PAGE matches with 1 initially the adapter is set
            //Else the data is added into the array adapterfrom the response
            if (Integer.parseInt(getCurrentPage) == 1) {
                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                // The Adapter provides access to the data items.
                // The Adapter is also responsible for making a View for each item in the data set.
                adapter = new CampaignCommentsAdapter(campaignCommentsActivity, dataResults, R.layout.activity_comments_singleview, totalComments, commentsCountPosition, campaignId, userId);
                //Set bottom footer view
                list.setLoadingView(R.layout.layout_loading);
                //Sets the data behind this ListView.
                list.setAdapter(adapter);
            } else {
             //the data is added into the array adapterfrom the response
                list.addCamapignCommetsData(dataResults);
            }
        }
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param campaignCommentsClick
     */
    public void onClick(final View campaignCommentsClick) {
        if (campaignCommentsClick.getId() == R.id.imagBackArrow) {
            //Finishing the activity
            this.finish();
        } else if (campaignCommentsClick.getId() == R.id.imageAddComments) {
            //Comments add request
            campaignCommentsAddRequest();
        }
    }


    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        page++;
        //Campaign COMMENTS display request method
        campaignCommentsDisplayRequest(page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Hiding the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Campaign Comment add request
     */
    private void campaignCommentsAddRequest() {
        //If net is connected the request for adding COMMENTS is done
        if (MApplication.isNetConnected(campaignCommentsActivity)) {
            imageAddComments.setClickable(false);
            //Getting the input from edit text
            mComments = addComments.getText().toString().trim();
            //Encoding the string
            comments = MApplication.getEncodedString(mComments);
            //If COMMENTS is not empty
            if (!comments.isEmpty()) {
                //Internet connection layout visiblity gone
                internetConnection.setVisibility(View.GONE);
                /**Progree bar visiblity**/
                googleNow.setVisibility(View.VISIBLE);
                /**Progress bar start**/
                googleNow.progressiveStart();
                /**  Requesting the response from the given base url**/
                CommentsPollRestClient.getInstance().postComments(new String("campaign_comment"), new String(userId), new String(campaignId), new String(comments)
                        , new Callback<CommentDisplayResponseModel>() {
                    @Override
                    public void success(CommentDisplayResponseModel comments, Response response) {
                        //If success value equals 1 then the below functionality will take place
                        if (("1").equals(comments.getSuccess())) {
                            campaignCommentsAddResponse(comments);
                        }
                        //Progressbar stops
                        googleNow.progressiveStop();
                        //Visiblity gone
                        googleNow.setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        //Error message popups when the user cannot able to coonect with the server
                        MApplication.errorMessage(retrofitError, campaignCommentsActivity);
                        //Progressbar stops
                        googleNow.progressiveStop();
                        //Progressbar became invisible
                        googleNow.setVisibility(View.GONE);
                    }

                });
            }
        }
    }

    /**
     * Campaign COMMENTS add resposne
     * @param comments
     */
    private void campaignCommentsAddResponse(CommentDisplayResponseModel comments) {
        //Getting the data from the resposne
        campaignCommentsAdd = comments.getResults().getData();
        imageAddComments.setClickable(true);
        //If the data is empty
        if (campaignCommentsAdd.isEmpty()) {
            //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
            // The Adapter provides access to the data items.
            // The Adapter is also responsible for making a View for each item in the data set.
            adapter = new CampaignCommentsAdapter(campaignCommentsActivity, dataResults, R.layout.activity_comments_singleview, totalComments, commentsCountPosition, campaignId, campaignId);
            //Set bottom footer view
            list.setLoadingView(R.layout.layout_loading);
            //Sets the data behind this ListView.
            list.setAdapter(adapter);
        } else {
            mDataResults.clear();
            //Adding the data from the array list
            mDataResults.add(comments.getResults().getData().get(0));
            //Add campaign cpmments data in array list
            list.addCamapignCommetsData(mDataResults);
            //Setting the text  empty
            addComments.setText("");
            //Hiding the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //Loading the array lsit from the prefernce
        prefrenceCommentsCount = MApplication.loadArray(campaignCommentsActivity, campaigncommentsCount, Constants.CAMPAIGN_COMMENTS_COUNT, Constants.CAMPAIGN_COMMENTS_SIZE);
        //Setting the COMMENTS count based on the position
        prefrenceCommentsCount.set(commentsCountPosition, Integer.valueOf(comments.getCount()));
        //Again saving the array in prefence
        MApplication.saveArray(campaignCommentsActivity, prefrenceCommentsCount, Constants.CAMPAIGN_COMMENTS_COUNT, Constants.CAMPAIGN_COMMENTS_SIZE);
        //Setting the count in prefernce
        MApplication.setString(campaignCommentsActivity, Constants.COMMENTS_COUNT_POLL_REVIEW, comments.getCount());
    }
}
