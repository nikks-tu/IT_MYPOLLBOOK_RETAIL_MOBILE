package com.contus.search;

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
 * Created by user on 12/17/2015.
 */
public class SearchPollComments extends Activity implements EndLessListView.EndlessListener {
    //Endless List view
    private EndLessListView list;
    //Activity
    private SearchPollComments searchPollCommentsActivity;
    //User ID
    private String userId;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> searchPollDataResults;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> mSearchDataResults;
    //Get the last PAGE
    private String searchGetLastPage;
    //Current PAGE
    private String getSearchCurrentPage;
    //Adapter class
    private SearchPollCommentsAdapter searchAdapter;
    //EditText
    private EditText searchAddComments;
    //User poll id
    private String searchUserPollId;
    //Page
    private int page = 1;
    //user pol COMMENTS count position
    private int searchUserCommentsCountPosition;
    //User poll COMMENTS count
    private ArrayList<Integer> searchUserPollcommentsCount = new ArrayList<Integer>();
    //Prefernce user poll  COMMENTS count
    private ArrayList<Integer> prefrencesearchUserPollCommentsCount;
    //Smooth progress bar
    private SmoothProgressBar googleNowSearch;
    //Comments
    private String commentSearchUser;
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
        searchPollCommentsActivity = this;
        list = (EndLessListView) findViewById(R.id.listView);
        searchAddComments = (EditText) findViewById(R.id.txtCountry);
        googleNowSearch = (SmoothProgressBar) findViewById(R.id.google_now);
        imageAddComments=(ImageView)findViewById(R.id.imageAddComments);
        //User id from the prefernce
        userId = MApplication.getString(searchPollCommentsActivity, Constants.USER_ID);
        //Poll id from the intent
        searchUserPollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //Position  from the intent
        searchUserCommentsCountPosition = getIntent().getExtras().getInt(Constants.COMMENTS_COUNT_POSITION);
        //array list initialization
        searchPollDataResults = new ArrayList<>();
        //array list initialization
        mSearchDataResults =new ArrayList<>();
        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
        // The Adapter provides access to the data items.
        // The Adapter is also responsible for making a View for each item in the data set.
        searchAdapter = new SearchPollCommentsAdapter(searchPollCommentsActivity, searchPollDataResults, R.layout.activity_comments_singleview, searchUserPollId, searchUserPollId, searchUserCommentsCountPosition);
        //Set bottom footer view
        list.setLoadingView(R.layout.layout_loading);
        //Sets the data behind this ListView.
        list.setSearchCommentsPoll(searchAdapter);
        //Setting the lisner
        list.setListener(this);
    }
    /**
     * Camapign Comments display request
     *
     * */
    private void searchPollCommentsDisplayRequest() {
        //If net is connected
        if (MApplication.isNetConnected(searchPollCommentsActivity)) {
            /**Progree bar visiblity**/
            googleNowSearch.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNowSearch.progressiveStart();
            /**  Requesting the response from the given base url**/
            CommentsDisplayRestClient.getInstance().postPollCommentsDetsils(new String("getPoll_comment"), new String(searchUserPollId), new String("10"), new Integer(page)
                    , new Callback<CommentDisplayResponseModel>() {
                        @Override
                        public void success(CommentDisplayResponseModel commentsPollDisplay, Response response) {
                            //If the succes matches with the value 1 then the datat is binded into the searchAdapter
                            if(("1").equals(commentsPollDisplay.getSuccess())) {
                                commentsSearchPollDisplay(commentsPollDisplay);
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            //Error message popups when the user cannot able to coonect with the server
                            MApplication.errorMessage(retrofitError, searchPollCommentsActivity);
                        }
                    });
            //Progressbar stops
            googleNowSearch.progressiveStop();
            //Progressbar became invisible
            googleNowSearch.setVisibility(View.GONE);
        }

    }
    /**
     * Campaign COMMENTS from the response
     * @param commentsPollDisplay -model class
     */
    private void commentsSearchPollDisplay(CommentDisplayResponseModel commentsPollDisplay) {
        //Data from the response
        searchPollDataResults = commentsPollDisplay.getResults().getData();
        //Last PAGE from the response
        searchGetLastPage = commentsPollDisplay.getResults().getLastPage();
        //Get the current PAGE from the response
        getSearchCurrentPage = commentsPollDisplay.getResults().getCurrentPage();
        //If searchPollDataResults is not empty matches with the PAGE number
        if (!searchPollDataResults.isEmpty()) {
            //If the current PAGE matches with 1 initially the searchAdapter is set
            //Else the data is added into the array adapterfrom the response
            if (Integer.parseInt(getSearchCurrentPage) == 1) {
                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                // The Adapter provides access to the data items.
                // The Adapter is also responsible for making a View for each item in the data set.
                searchAdapter = new SearchPollCommentsAdapter(searchPollCommentsActivity, searchPollDataResults, R.layout.activity_comments_singleview, searchUserPollId, userId, searchUserCommentsCountPosition);
                //Set bottom footer view
                list.setLoadingView(R.layout.layout_loading);
                //Sets the data behind this ListView.
                list.setSearchCommentsPoll(searchAdapter);
            } else if (Integer.parseInt(getSearchCurrentPage) <= Integer.parseInt(searchGetLastPage)) {
                //the data is added into the array adapterfrom the response
                list.addSearchPollCommentsData(searchPollDataResults);
            }

        }
        //Progressive bar stop
        googleNowSearch.progressiveStop();
        //Visiblity gone
        googleNowSearch.setVisibility(View.GONE);
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if(clickedView.getId()==R.id.imagBackArrow){
            //Finishing the activity
            this.finish();
        }else if(clickedView.getId()==R.id.imageAddComments){
            //Comments add request
            searchpollCommentsAddRequest();
        }
    }

    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        page++;
        //Campaign COMMENTS display request method
        searchPollCommentsDisplayRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Hiding the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Comemnt display request
        searchPollCommentsDisplayRequest();
    }

    /**
     * Campaign Comment add request
     */
    private void searchpollCommentsAddRequest() {
        //If net is connected the request for adding COMMENTS is done
        if (MApplication.isNetConnected(searchPollCommentsActivity)) {

            //   //Getting the input from edit text and Encoding the string
            commentSearchUser = MApplication.getEncodedString(searchAddComments.getText().toString().trim());
            //If COMMENTS is not empty
            if (!commentSearchUser.isEmpty()) {
                imageAddComments.setClickable(false);
                /**Progree bar visiblity**/
                googleNowSearch.setVisibility(View.VISIBLE);
                /**Progress bar start**/
                googleNowSearch.progressiveStart();
                /**  Requesting the response from the given base url**/
                CommentsPollRestClient.getInstance().postPollComments(new String("poll_comment"), new String(userId), new String(searchUserPollId), new String(commentSearchUser)
                        , new Callback<CommentDisplayResponseModel>() {
                            @Override
                            public void success(CommentDisplayResponseModel comments, Response response) {
                                //If success value equals 1 then the below functionality will take place
                                if (("1").equals(comments.getSuccess())) {
                                    searchPollCommentsAddResponse(comments);
                                }
                            }
                            @Override
                            public void failure(RetrofitError retrofitError) {
                                //Error message popups when the user cannot able to coonect with the server
                                MApplication.errorMessage(retrofitError, searchPollCommentsActivity);
                            }
                        });
                //Progressbar stops
                googleNowSearch.progressiveStop();
                //Visiblity gone
                googleNowSearch.setVisibility(View.GONE);
            }
        }
    }
    /**
     * Campaign COMMENTS add resposne
     * @param comments
     */
    private void searchPollCommentsAddResponse(CommentDisplayResponseModel comments) {
        imageAddComments.setClickable(true);
        //Getting the data from the resposne
        if (comments.getResults().getData().isEmpty()) {
            //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
            // The Adapter provides access to the data items.
            // The Adapter is also responsible for making a View for each item in the data set.
            searchAdapter = new SearchPollCommentsAdapter(searchPollCommentsActivity, searchPollDataResults, R.layout.activity_comments_singleview, searchUserPollId, searchUserPollId, searchUserCommentsCountPosition);
            //Set bottom footer view
            list.setLoadingView(R.layout.layout_loading);
            //Sets the data behind this ListView.
            list.setSearchCommentsPoll(searchAdapter);
        } else {
            mSearchDataResults.clear();
            //Adding the data from the array list
            mSearchDataResults.add(comments.getResults().getData().get(0));
            //  //Add campaign cpmments data in array list
            list.addSearchPollCommentsData(mSearchDataResults);
            //Setting the text  empty
            searchAddComments.setText("");
            //Hiding the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //Loading the array lsit from the prefernce
        prefrencesearchUserPollCommentsCount = MApplication.loadArray(searchPollCommentsActivity, searchUserPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);
        //Setting the COMMENTS count based on the position
        prefrencesearchUserPollCommentsCount.set(searchUserCommentsCountPosition, Integer.valueOf(comments.getCount()));
        //Again saving the array in prefence
        MApplication.saveArray(searchPollCommentsActivity, prefrencesearchUserPollCommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_COUNT);
        //Setting the count in searchPollCommentsActivity
        MApplication.setString(searchPollCommentsActivity, Constants.COMMENTS_COUNT_POLL_REVIEW, comments.getCount());

    }

}
