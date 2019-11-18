package com.contus.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.responsemodel.UserPollResponseModel;
import com.contus.restclient.SearchRestClient;
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
 * Created by user on 10/9/2015.
 */
public class SearchActivity extends Activity implements EndLessListView.EndlessListener {
    /**List view**/
    private EditText editSearch;
    /**Textview**/
    private TextView noSearchResults;
    //String location
    private AdView mAdView;
    private TextView txtTitle;

    private String location;
    //floating action button
    private FloatingActionButton fab;
    //Endless lsit view
    private EndLessListView list;
    //user id
    private String userId;
    //search adapter
    private SearchAdapter adapter;
    //last  page
    private String getLastPage;
    //page
    private int page;
    //current page
    private String getCurrentPage;
    //Google now
    private SmoothProgressBar googleNow;
    //Internet connection
    private RelativeLayout internetConnection;
    //relativie lsit
    private RelativeLayout relativeList;
    //array list
    private List<UserPollResponseModel.Results.Data> dataResults;
    //array list
    private ArrayList<Integer> searchPollLikeCount = new ArrayList<Integer>();
    //array list
    private ArrayList<Integer> searchPollLikesUser = new ArrayList<Integer>();
    //array list
    private ArrayList<Integer> searchPollcommentsCount = new ArrayList<Integer>();
    //array list
    private ArrayList<String> searchPollIdAnwserCheck = new ArrayList<String>();
    //array list
    private ArrayList<String> searchPollIdAnwser = new ArrayList<String>();
    //search activity
    private SearchActivity mSearchActivity;
    //array list
    private ArrayList<Integer> searchPollParticipateCount = new ArrayList<Integer>();
    //swipe refresh layout
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //intailize the actiivty
        init();
    }

    public void init(){
        mSearchActivity =this;
        /**Initializing the list view**/
        list = (EndLessListView) findViewById(R.id.component_list);
        editSearch = (EditText) findViewById(R.id.editSearch);
        googleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        txtTitle = (TextView) findViewById(R.id.selectCountry);
        noSearchResults = (TextView) findViewById(R.id.noSearchResults);
        internetConnection = (RelativeLayout) findViewById(R.id.internetConnection);
        relativeList = (RelativeLayout) findViewById(R.id.list);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.activity_main_swipe_refresh_layout);
        //getting the value from prefernce
        mAdView = (AdView) findViewById(R.id.adView);
        MApplication.googleAd(mAdView);
        userId = MApplication.getString(mSearchActivity, Constants.USER_ID);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Light.ttf");
        txtTitle.setTypeface(face);

        //set listner
        list.setListener(this);
        page = 1;
        /**Text change listner**/
        editSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {        /**If search icon is clicked in the keypad the condition goes inside**/
                    location = editSearch.getText().toString().trim();
                    if (!location.isEmpty()) {
                        editSearch.setText("");      /**Value from the edittext**/
                        searchPollRequest();/**Request and response method is called**/
                    } else {
                        Toast.makeText(mSearchActivity, getResources().getString(R.string.enter_valid_location_name),   /**Toast message wthen search key is wrong**/Toast.LENGTH_SHORT).show();
                        editSearch.requestFocus();   /**Requesting focus**/
                    }
                    MApplication.hideKeyboard(mSearchActivity);    /**Hiding the keyborad**/
                    return true;
                }
                return false;
            }

        });
        //page equal to 1
        page=1;
        //model number usingn
        String model = android.os.Build.MODEL;
        if (Constants.SAMSUNG_S3.equals(model)) {
            //er-child layout information associated with RelativeLayout.
            RelativeLayout.LayoutParams userPollLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            userPollLayout.setMargins(20, 20, 5, 0);
            //Set the layout parameters associated with this view.
            list.setLayoutParams(userPollLayout);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //Interface definition for a callback to be invoked when a view is clicked.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //geting the value from the edittext
                location = editSearch.getText().toString().trim();
                MApplication.hideKeyboard(mSearchActivity);      /**Hiding the keyborad**/
                if (!location.isEmpty()) {  /**Request and response method is called**/
                    searchPollRequest();
                } else {
                    //Toast message will display
                    Toast.makeText(mSearchActivity,"Please enter the text to search", Toast.LENGTH_SHORT).show();    /**Toast message wthen search key is wrong**/
                    editSearch.requestFocus();/**Requesting focus**/
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**Request and response method is called**/
                searchPollRequest();
            }
        });

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
    private void searchPollRequest() {
        /**If internet connection is available**/
        if (MApplication.isNetConnected(mSearchActivity)) {
            //view gone
            internetConnection.setVisibility(View.GONE);
            //view visible
            relativeList.setVisibility(View.VISIBLE);
            //view invisble
            noSearchResults.setVisibility(View.INVISIBLE);
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            SearchRestClient.getInstance().searchUserPOll(new String(Constants.SEARCH_POLLS_API), new String(location),new Integer(page),new String(userId),new Callback<UserPollResponseModel>() {
                @Override
                public void success(UserPollResponseModel userPollResponseModel, Response response) {
                    if (("1").equals(userPollResponseModel.getSuccess())) {
                        //like count clear
                        searchPollLikeCount.clear();
                        searchPollLikesUser.clear();//clear
                        searchPollcommentsCount.clear();//clear
                        searchPollParticipateCount.clear();
                        //Data results
                        dataResults = userPollResponseModel.getResults().getData();
                        //get the last page
                        getLastPage = userPollResponseModel.getResults().getLastPage();
                        //get the current page
                        getCurrentPage = userPollResponseModel.getResults().getCurrentPage();
                        //If it is not empty
                        if (!dataResults.isEmpty()) {
                            //Current page equal to 1
                            if (Integer.parseInt(getCurrentPage) == 1) {
                                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                                // The Adapter provides access to the data items.
                                // The Adapter is also responsible for making a View for each item in the data set.
                                adapter = new SearchAdapter(mSearchActivity, dataResults, R.layout.publicpoll_singleview, userId,list);
                                //setting the bottom view
                                list.setLoadingView(R.layout.layout_loading);
                                //setting the adapter
                                list.setSearchPollAdapter(adapter);
                            } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                                //adding the value in array list
                                list.addSearchPollData(dataResults);
                                searchPollLikeCount.clear();//clear
                                searchPollLikesUser.clear();//clear
                                searchPollcommentsCount.clear();//clear
                                searchPollParticipateCount.clear();
                                //Loading the array from prefernce
                                searchPollcommentsCount = MApplication.loadArray(mSearchActivity, searchPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);
                                //Loading the array from prefernce
                                searchPollLikesUser = MApplication.loadArray(mSearchActivity, searchPollLikesUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
                                //Loading the array from prefernce
                                searchPollLikeCount = MApplication.loadArray(mSearchActivity, searchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
                                //Loading the array from prefernce
                                searchPollParticipateCount = MApplication.loadArray(mSearchActivity, searchPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);
                                //Loading the details from the arraylist
                                searchPollIdAnwserCheck= MApplication.loadStringArray(mSearchActivity, searchPollIdAnwserCheck, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);
                                //Loading the details from the arraylist
                                searchPollIdAnwser= MApplication.loadStringArray(mSearchActivity, searchPollIdAnwser, Constants.SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.SEARCH_POLL_ID_SELECTED_SIZE);
                            }

                        }
                        for (int i = 0; dataResults.size() > i; i++) {
                            //adding into the arraylist
                            searchPollcommentsCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignCommentsCounts()));
                            //saving in prefernce
                            MApplication.saveArray(mSearchActivity, searchPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);
                            //adding into the arraylist
                            searchPollLikesUser.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignUserLikes()));
                            //saving in prefernce
                            MApplication.saveArray(mSearchActivity, searchPollLikesUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
                            //adding into the arraylist
                            searchPollLikeCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignLikesCounts()));
                            //saving in prefernce
                            MApplication.saveArray(mSearchActivity, searchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
                            //adding into the arraylist
                            searchPollParticipateCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getPollParticipateCount()));
                            //saving in prefernce
                            MApplication.saveArray(mSearchActivity, searchPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);

                            if (!userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().isEmpty()){
                                for(int j=0;userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().size()>j;j++) {
                                    if (userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getUserId().equals(userId)) {
                                        //adding the response to the array listt
                                        searchPollIdAnwserCheck.add(userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollId());
                                        //adding the response to the array listt
                                        searchPollIdAnwser.add(userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollAnswer());
                                        //Save the string array in prefernce
                                        MApplication.saveStringArray(mSearchActivity, searchPollIdAnwserCheck, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);
                                        //Save the string array in prefernce
                                        MApplication.saveStringArray(mSearchActivity, searchPollIdAnwser, Constants.SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.SEARCH_POLL_ID_SELECTED_SIZE);
                                    }
                                }
                            }

                        }

                    } else if(("1").equals(userPollResponseModel.getSuccess())&&page==1||("0").equals(userPollResponseModel.getSuccess())&&page==1) {
                        //visible
                        noSearchResults.setVisibility(View.VISIBLE);
                        //invisible
                        internetConnection.setVisibility(View.INVISIBLE);
                        //invisible
                        relativeList.setVisibility(View.INVISIBLE);

                    }
    //progressive stop
                    googleNow.progressiveStop();
                    //visiblity gone
                    googleNow.setVisibility(View.GONE);
                    //swipe refresyh
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    MApplication.errorMessage(retrofitError, mSearchActivity);
                }

            });
        } else {
            //visiblity gone
            internetConnection.setVisibility(View.VISIBLE);
            //liost gone
            relativeList.setVisibility(View.GONE);
            //gone
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData() {
        //incrementing the page count
        page++;
        //adding the request
        searchPollRequest();
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if (clickedView.getId() == R.id.imgBackArrow) {
            MApplication.setBoolean(mSearchActivity, Constants.SEARCH_BACKPRESS_BOOLEAN, true);
            /**Finish the activity**/
            this.finish();
        } else if (clickedView.getId() == R.id.internetRetry) {
            /**Finish the activity**/
            location = editSearch.getText().toString().trim();
            if (!location.isEmpty()) {
                /**Request and response method is called**/
                searchPollRequest();
            } else {
                /**Hiding the keyborad**/
                MApplication.hideKeyboard(mSearchActivity);
                /**Toast message wthen search key is wrong**/
                Toast.makeText(mSearchActivity, "Please enter the text to search",
                        Toast.LENGTH_SHORT).show();
                /**Requesting focus**/
                editSearch.requestFocus();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //If adapter is not null
        if(adapter!=null){
            //notify data set changed
            adapter.notifyDataSetChanged();
        }
    }


}
