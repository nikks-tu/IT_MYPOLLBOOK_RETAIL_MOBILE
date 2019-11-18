package com.contus.comments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.responsemodel.CommentDisplayResponseModel;
import com.contus.restclient.CommentsDisplayRestClient;
import com.contus.restclient.CommentsPollRestClient;
import com.contus.views.EndLessListView;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/14/2015.
 */
public class PollComments extends Activity implements EndLessListView.EndlessListener {
//Endless List view
    private EndLessListView list;
    //Activity
    private PollComments pollCommentsActivity;
    //User ID
    private String userId;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> dataResults;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> mDataResults;
    //Get the last PAGE
    private String getLastPage;
    //Current PAGE
    private String getCurrentPage;
    //Adapter class
    private PollCommentsAdapter adapter;
    //EditText
    private EditText addComments;
    //User poll id
    private String userPollId;
    //Page
    private int page = 1;
    //user pol COMMENTS count position
    private int userCommentsCountPosition;
    //User poll COMMENTS count
    private ArrayList<Integer> userPollcommentsCount = new ArrayList<Integer>();
    //Prefernce user poll  COMMENTS count
    private ArrayList<Integer> prefrenceUserPollCommentsCount;
    //Smooth progress bar
    private SmoothProgressBar pollGoogleNow;
    //Comments
    private String commentsPoll;
    private ImageView pollAddComments;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        /**View are creating when the activity is started**/
        initViews();
    }

    /**
     * Instantiate the method
     */
    public void initViews() {
        /**Initializing the activity**/
        pollCommentsActivity = this;
        list = (EndLessListView) findViewById(R.id.listView);
        addComments = (EditText) findViewById(R.id.txtCountry);
        pollGoogleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        pollAddComments =(ImageView)findViewById(R.id.imageAddComments);
        mAdView = (AdView) findViewById(R.id.adView);
        MApplication.googleAd(mAdView);
        //User id from the prefernce
        userId = MApplication.getString(pollCommentsActivity, Constants.USER_ID);
        //Poll id from the intent
        userPollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //Position  from the intent
        userCommentsCountPosition = getIntent().getExtras().getInt(Constants.COMMENTS_COUNT_POSITION);
        //array list initialization
        dataResults = new ArrayList<>();
        //array list initialization
        mDataResults=new ArrayList<>();
        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
        // The Adapter provides access to the data items.
        // The Adapter is also responsible for making a View for each item in the data set.
        adapter = new PollCommentsAdapter(pollCommentsActivity, dataResults, R.layout.activity_comments_singleview, userPollId, userPollId, userCommentsCountPosition);
        //Set bottom footer view
        list.setLoadingView(R.layout.layout_loading);
        //Sets the data behind this ListView.
        list.setCommentsPoll(adapter);
        //Setting the lisner
        list.setListener(this);
    }
    /**
     * Camapign Comments display request
     *
     * */
    private void pollCommentsDisplayRequest() {
        //If net is connected
        if (MApplication.isNetConnected(pollCommentsActivity)) {
            /*Progree bar visiblity**/
            pollGoogleNow.setVisibility(View.VISIBLE);
            /*Progress bar start**/
            pollGoogleNow.progressiveStart();
            /* Requesting the response from the given base url**/
            CommentsDisplayRestClient.getInstance().postPollCommentsDetsils(new String("getPoll_comment"), new String(userPollId), new String("10"), new Integer(page)
                    , new Callback<CommentDisplayResponseModel>() {
                @Override
                public void success(CommentDisplayResponseModel commentsPollDisplay, Response response) {
                    //If the succes matches with the value 1 then the datat is binded into the adapter
                 if(("1").equals(commentsPollDisplay.getSuccess())) {
                     commentsPollDisplay(commentsPollDisplay);

                 }
                    //Progressive stop
                    pollGoogleNow.progressiveStop();
                    //Visiblity gone
                    pollGoogleNow.setVisibility(View.GONE);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, pollCommentsActivity);
                    //Progressive stop
                    pollGoogleNow.progressiveStop();
                    //Visiblity gone
                    pollGoogleNow.setVisibility(View.GONE);
                }

            });

        }

    }
    /**
     * Campaign COMMENTS from the response
     * @param commentsPollDisplay -model class
     */
    private void commentsPollDisplay(CommentDisplayResponseModel commentsPollDisplay) {
        //Data from the response
        dataResults = commentsPollDisplay.getResults().getData();
        //Last PAGE from the response
        getLastPage = commentsPollDisplay.getResults().getLastPage();
        //Get the current PAGE from the response
        getCurrentPage = commentsPollDisplay.getResults().getCurrentPage();
        //If dataResults is not empty matches with the PAGE number
        if (!dataResults.isEmpty()) {
            //If the current PAGE matches with 1 initially the adapter is set
            //Else the data is added into the array adapterfrom the response
            if (Integer.parseInt(getCurrentPage) == 1) {
                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                // The Adapter provides access to the data items.
                // The Adapter is also responsible for making a View for each item in the data set.
                adapter = new PollCommentsAdapter(pollCommentsActivity, dataResults, R.layout.activity_comments_singleview, userPollId, userId, userCommentsCountPosition);
                //Set bottom footer view
                list.setLoadingView(R.layout.layout_loading);
                //Sets the data behind this ListView.
                list.setCommentsPoll(adapter);
            } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                //the data is added into the array adapterfrom the response
                list.addUserPollCommentsData(dataResults);
            }

        }
        //Progressive bar stop
        pollGoogleNow.progressiveStop();
        //Visiblity gone
        pollGoogleNow.setVisibility(View.GONE);
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param clickedView the clicked view
     */
    public void onClick(final View clickedView) {
            if(clickedView.getId()==R.id.imagBackArrow){
                //Finishing the activity
                this.finish();
            }else if(clickedView.getId()==R.id.imageAddComments){
                //Comments add request
                pollCommentsAddRequest();
            }
    }

    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        page++;
        //Campaign COMMENTS display request method
        pollCommentsDisplayRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Hiding the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Comemnt display request
        pollCommentsDisplayRequest();
    }

    /**
     * Campaign Comment add request
     */
    private void pollCommentsAddRequest() {
        //If net is connected the request for adding COMMENTS is done
        if (MApplication.isNetConnected(pollCommentsActivity)) {
            //   //Getting the input from edit text and Encoding the string
            commentsPoll = MApplication.getEncodedString(addComments.getText().toString().trim());
            //If COMMENTS is not empty
            if (!commentsPoll.isEmpty()) {
                pollAddComments.setClickable(false);
                /**Progree bar visiblity**/
                pollGoogleNow.setVisibility(View.VISIBLE);
                /**Progress bar start**/
                pollGoogleNow.progressiveStart();
                /**  Requesting the response from the given base url**/
                CommentsPollRestClient.getInstance().postPollComments(new String("poll_comment"), new String(userId), new String(userPollId), new String(commentsPoll)
                        , new Callback<CommentDisplayResponseModel>() {
                    @Override
                    public void success(CommentDisplayResponseModel comments, Response response) {
                        //If success value equals 1 then the below functionality will take place
                        if (("1").equals(comments.getSuccess())) {
                            //Added by nikita
                            Toast.makeText(pollCommentsActivity, comments.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            pollCommentsAddResponse(comments);
                        }
                        //Progressbar stops
                        pollGoogleNow.progressiveStop();
                        //Visiblity gone
                        pollGoogleNow.setVisibility(View.GONE);
                    }
                    @Override
                    public void failure(RetrofitError retrofitError) {
                        //Error message popups when the user cannot able to coonect with the server
                        MApplication.errorMessage(retrofitError, pollCommentsActivity);
                        //Progressbar stops
                        pollGoogleNow.progressiveStop();
                        //Visiblity gone
                        pollGoogleNow.setVisibility(View.GONE);
                    }
                });

            }
        }
    }
    /**
     * Campaign COMMENTS add resposne
     * @param comments
     */
    private void pollCommentsAddResponse(CommentDisplayResponseModel comments) {
        //Visiblity
        pollAddComments.setClickable(true);
        //Getting the data from the resposne
        if (comments.getResults().getData().isEmpty()) {
            //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
            // The Adapter provides access to the data items.
            // The Adapter is also responsible for making a View for each item in the data set.
            adapter = new PollCommentsAdapter(pollCommentsActivity, dataResults, R.layout.activity_comments_singleview, userPollId, userPollId, userCommentsCountPosition);
            //Set bottom footer view
            list.setLoadingView(R.layout.layout_loading);
            //Sets the data behind this ListView.
            list.setCommentsPoll(adapter);
        } else {
            mDataResults.clear();
            //Adding the data from the array list
            mDataResults.add(comments.getResults().getData().get(0));
            //  //Add campaign cpmments data in array list
            list.addPollCommetsData(mDataResults);
            adapter.notifyDataSetChanged();
            //Setting the text  empty
            addComments.setText("");
            //Hiding the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //Loading the array lsit from the prefernce
        prefrenceUserPollCommentsCount = MApplication.loadArray(pollCommentsActivity, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
        //Setting the COMMENTS count based on the position
        prefrenceUserPollCommentsCount.set(userCommentsCountPosition, Integer.valueOf(comments.getCount()));
        //Again saving the array in prefence
        MApplication.saveArray(pollCommentsActivity, prefrenceUserPollCommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
        //Setting the count in prefernce
        MApplication.setString(pollCommentsActivity, Constants.COMMENTS_COUNT_POLL_REVIEW, comments.getCount());

    }

}
