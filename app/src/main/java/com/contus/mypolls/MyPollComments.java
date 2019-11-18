package com.contus.mypolls;

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
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/14/2015.
 */
public class MyPollComments extends Activity implements EndLessListView.EndlessListener {
    //Endless List view
    private EndLessListView list;
    //Activity
    private MyPollComments myPollsCommentsActivity;
    //User ID
    private String userId;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> dataResults;
    //Model class data is stored as list
    private List<CommentDisplayResponseModel.Results.Data> mDataResults;
   //Last PAGE
    private String getLastPage;
    //Current PAGE
    private String getCurrentPage;
    //Adapter
    private MyPollsCommentsAdapter adapter;
    //Edittext
    private EditText txtComments;
    //Poll id
    private String pollId;
    //Page
    private int page = 1;
    //Position
    private int commentsCountPosition;
    //Camapign COMMENTS count
    private ArrayList<Integer> myPollcommentsCount = new ArrayList<Integer>();
    //Prefernce COMMENTS count
    private ArrayList<Integer> prefrenceUserPollCommentsCount;
    //Progress bar
    private SmoothProgressBar googleNow;
    //Comments
    private String comments;
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
        myPollsCommentsActivity = this;
        list = (EndLessListView) findViewById(R.id.listView);
        txtComments = (EditText) findViewById(R.id.txtCountry);
        googleNow=(SmoothProgressBar)findViewById(R.id.google_now);
        imageAddComments=(ImageView)findViewById(R.id.imageAddComments);
        //User id from the prefernce
        userId = MApplication.getString(myPollsCommentsActivity, Constants.USER_ID);
        //Camapign id from the intent
        pollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //Position  from the intent
        commentsCountPosition=getIntent().getExtras().getInt(Constants.COMMENTS_COUNT_POSITION);
        //array list initialization
        dataResults = new ArrayList<>();
        //array list initialization
        mDataResults = new ArrayList<>();
        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
        // The Adapter provides access to the data items.
        // The Adapter is also responsible for making a View for each item in the data set.
        adapter = new MyPollsCommentsAdapter(myPollsCommentsActivity, dataResults, R.layout.activity_comments_singleview, pollId, pollId, commentsCountPosition);
        //Set bottom footer view
        list.setLoadingView(R.layout.layout_loading);
        //Camapign COMMENTS display request
        list.setmyPollsCommentsPoll(adapter);
        //Setting the lisner
        list.setListener(this);
    }
    /**
     * Camapign Comments display request
     *
     */
    private void myPollsCommentsDisplayRequest() {
        if (MApplication.isNetConnected(myPollsCommentsActivity)) {
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            CommentsDisplayRestClient.getInstance().postPollCommentsDetsils(new String("getPoll_comment"), new String(pollId), new String("10"), new Integer(page)
                    , new Callback<CommentDisplayResponseModel>() {
                @Override
                public void success(CommentDisplayResponseModel myPollComments, Response response) {
                    if(("1").equals(myPollComments.getSuccess())) {  //If the succes matches with the value 1 then the datat is binded into the adapter
                        myPollsCommentsDisplayResponse(myPollComments);
                    }
                    googleNow.progressiveStop();  //Progressive stop
                    googleNow.setVisibility(View.GONE);//Visiblity gone
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    MApplication.errorMessage(retrofitError, myPollsCommentsActivity);   //Error message popups when the user cannot able to coonect with the server
                    googleNow.progressiveStop();  //Progressbar stops
                    googleNow.setVisibility(View.GONE);//Progressbar became invisible
                }

            });
        }

    }

    /**
     * Campaign COMMENTS from the response
     * @param myPollComments -model class
     */
    private void myPollsCommentsDisplayResponse(CommentDisplayResponseModel myPollComments) {
        //Data from the response
        dataResults = myPollComments.getResults().getData();
        //Last PAGE from the response
        getLastPage = myPollComments.getResults().getLastPage();
        //Get the current PAGE from the response
        getCurrentPage = myPollComments.getResults().getCurrentPage();
        //If dataResults is not empty matches with the PAGE number
        if (!dataResults.isEmpty()) {
            //If the current PAGE matches with 1 initially the adapter is set
            //Else the data is added into the array adapterfrom the response
            if (Integer.parseInt(getCurrentPage) == 1) {
                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                // The Adapter provides access to the data items.
                // The Adapter is also responsible for making a View for each item in the data set.
                adapter = new MyPollsCommentsAdapter(myPollsCommentsActivity, dataResults, R.layout.activity_comments_singleview, pollId, userId, commentsCountPosition);
                //Set bottom footer view
                list.setLoadingView(R.layout.layout_loading);
                //Sets the data behind this ListView.
                list.setmyPollsCommentsPoll(adapter);
            } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                //the data is added into the array adapterfrom the response
                list.addmyPollCommentsData(dataResults);
            }

        }
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param commnetsView
     */
    public void onClick(final View commnetsView) {

        if(commnetsView.getId()==R.id.imagBackArrow){
            //Finishing the activity
            this.finish();
        }else if(commnetsView.getId()==R.id.imageAddComments){
            //Comments add request
            myPollsCommentsAddRequest();
        }


    }

    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        page++;
        //Campaign COMMENTS display request method
        myPollsCommentsDisplayRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Hiding the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Comemnt display request
        myPollsCommentsDisplayRequest();
    }
    /**
     * Campaign Comment add request
     */
    private void myPollsCommentsAddRequest() {
        if (MApplication.isNetConnected(myPollsCommentsActivity)) { //If net is connected the request for adding COMMENTS is done

            comments = MApplication.getEncodedString(txtComments.getText().toString().trim());  //   //Getting the input from edit text and Encoding the string
            if (!comments.isEmpty()) {  //If COMMENTS is not empty
                imageAddComments.setClickable(false);
                googleNow.setVisibility(View.VISIBLE);/* Progree bar visiblity**/
                googleNow.progressiveStart();  /*Progress bar start**/
                CommentsPollRestClient.getInstance().postPollComments(new String("poll_comment"), new String(userId), new String(pollId), new String(comments), new Callback<CommentDisplayResponseModel>() { /**  Requesting the response from the given base url**/
                    @Override
                    public void success(CommentDisplayResponseModel comments, Response response) {
                        //If success value equals 1 then the below functionality will take place
                        if (("1").equals(comments.getSuccess())) {
                            //Added by nikita
                            Toast.makeText(myPollsCommentsActivity, comments.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            myPollsCommentsAddResponse(comments);
                        }
                    }
                    @Override
                    public void failure(RetrofitError retrofitError) {
                        MApplication.errorMessage(retrofitError, myPollsCommentsActivity);   //Error message popups when the user cannot able to coonect with the server
                    }
                });
                //Progressbar stops
                googleNow.progressiveStop();
                //Visiblity gone
                googleNow.setVisibility(View.GONE);
            }
        }

    }
    /**
     * Campaign COMMENTS add resposne
     * @param comments
     */
    private void myPollsCommentsAddResponse(CommentDisplayResponseModel comments) {
        imageAddComments.setClickable(true);
        //Getting the data from the resposne
        if ( comments.getResults().getData().isEmpty()) {
            //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
            // The Adapter provides access to the data items.
            // The Adapter is also responsible for making a View for each item in the data set.
            adapter = new MyPollsCommentsAdapter(myPollsCommentsActivity, dataResults, R.layout.activity_comments_singleview, pollId, pollId, commentsCountPosition);
            //Set bottom footer view
            list.setLoadingView(R.layout.layout_loading);
            //Sets the data behind this ListView.
            list.setmyPollsCommentsPoll(adapter);
        } else {
            mDataResults.clear();
            //Adding the data from the array list
            mDataResults.add(comments.getResults().getData().get(0));
              //Add campaign cpmments data in array list
            list.addmyPollCommentsData(mDataResults);
            //Setting the text  empty
            txtComments.setText("");
            //Hiding the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //Loading the array lsit from the prefernce
        prefrenceUserPollCommentsCount= MApplication.loadArray(myPollsCommentsActivity, myPollcommentsCount,Constants.MY_POLL_COMMENTS_COUNT,Constants.MY_POLL_COMMENTS_SIZE);
        //Setting the COMMENTS count based on the position
        prefrenceUserPollCommentsCount.set(commentsCountPosition, Integer.valueOf(comments.getCount()));
        //Again saving the array in prefence
        MApplication.saveArray(myPollsCommentsActivity, prefrenceUserPollCommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);
        //Setting the count in prefernce
        MApplication.setString(myPollsCommentsActivity,Constants.COMMENTS_COUNT_POLL_REVIEW,comments.getCount());


    }

}
