package com.contusfly.privatepolls;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.responsemodel.PrivatePollResponseModel;
import com.contus.restclient.PrivatePollDisplayRestClient;
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
 * Created by user on 3/2/16.
 */
public class PrivatePolls extends Activity implements EndLessListView.EndlessListener {
    //Endlesslistview
    private EndLessListView listUserPolls;
    //Footerview
    private View footerView;
    //Adview
    private AdView mAdView;
    //Userid
    private String userId;
    //Userpollcustom adapter
    private PrivatePollsCustomAdapter userPollCustomAdapter;
    //Get the last page
    private String getLastPageResponse;
    //page
    private int page = 1;
    //Get the current page
    private String getCurrentPageResponse;
    //Smooth progress bar
    private SmoothProgressBar userPollGoogleNow;
    //Relative layout
    private RelativeLayout internetConnection;
    //Relative layout
    private RelativeLayout relativeList;
    //Button retry
    private TextView btnRetryUserPoll;
    //no user poll
    private TextView noUserPoll;
    //user poll response
    private List<PrivatePollResponseModel.Results.Data> userPollResponse;
    //like count
    private ArrayList<Integer> userPollLikeCount = new ArrayList<Integer>();
    //likes user
    private ArrayList<Integer> userPollLikesUser = new ArrayList<Integer>();
    //Comments count
    private ArrayList<Integer> userPollcommentsCount = new ArrayList<Integer>();
    //poll id answer check
    private ArrayList<String> userPollIdAnwserCheck = new ArrayList<String>();
    //answer
    private ArrayList<String> userPollIdAnwser = new ArrayList<String>();
    //Fragment
    private Activity userPollsFragment;
    //participate count
    private ArrayList<Integer> userPollParticipateCount = new ArrayList<Integer>();
    //model
    private String model;
    //swipe refresh layout
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String groupId = "0";
    private String contactId = "0";
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privatepoll);
        listUserPolls = (EndLessListView) findViewById(R.id.listView);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_footer, null, false);
        mAdView = (AdView) findViewById(R.id.adView);
        userPollGoogleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        internetConnection = (RelativeLayout) findViewById(R.id.internetConnection);
        relativeList = (RelativeLayout) findViewById(R.id.list);
        noUserPoll = (TextView) findViewById(R.id.noCampaignResults);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        btnRetryUserPoll = (TextView) findViewById(R.id.internetRetry);
        userPollsFragment = this;
        type = userPollsFragment.getIntent().getStringExtra("type");
        //Getting the user id
        userId = MApplication.getString(userPollsFragment, Constants.USER_ID);
        if (type.equals("group")) {
            groupId = userPollsFragment.getIntent().getStringExtra("id");
            userPollRequestGroup();
        } else {
            type = "contact";
            contactId = userPollsFragment.getIntent().getStringExtra("id");
            userPollRequest();
        }

        //Model of the mobile using
        model = android.os.Build.MODEL;
        //if matches samsung s3
        if (Constants.SAMSUNG_S3.equals(model)) {
            //er-child layout information associated with RelativeLayout.
            RelativeLayout.LayoutParams userPollLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            userPollLayout.setMargins(20, 20, 5, 0);
            //Set the layout parameters associated with this view.
            listUserPolls.setLayoutParams(userPollLayout);
        }
        ///Add a fixed view to appear at the bottom of the list.
        listUserPolls.addFooterView(footerView);

        //set lisner
        listUserPolls.setListener(this);
        //request fro the api
//        userPollRequest();

        //Interface definition for a callback to be invoked when a view is clicked.
        btnRetryUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Requesting for the public poll
                userPollRequest();
            }
        });

        findViewById(R.id.imagBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /**
     * This method is used resquesting the user poll and binding into the adapter
     */
    private void userPollRequest() {
        //If net is connected
        if (MApplication.isNetConnected(userPollsFragment)) {
            //visible
            noUserPoll.setVisibility(View.GONE);
            //View gone
            internetConnection.setVisibility(View.GONE);
            //View visible
            relativeList.setVisibility(View.VISIBLE);
            /**Progree bar visiblity**/
            userPollGoogleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            userPollGoogleNow.progressiveStart();
            Log.e("userId", userId + "");
            Log.e("contactId", contactId + "");
            Log.e("type", type + "");
            Log.e("groupId", groupId + "");
            /**  Requesting the response from the given base url**/
            PrivatePollDisplayRestClient.getInstance().postPrivatePollDisplayApi(new String("get_chatpolls"), new String(contactId), new String(userId), new String(type), new String(groupId), new String(String.valueOf(page))
                    , new Callback<PrivatePollResponseModel>() {
                        @Override
                        public void success(PrivatePollResponseModel userPollResponseModel, Response response) {
                            Log.e("success", userPollResponseModel.getSuccess() + " ");
                            //If the success value is 1 then the below functionality will take place
                            //else if  get success equals 1 and PAGE 1 or get successqueals 0 and PAGE equal to 1 views will be visible
                            if (("1").equals(userPollResponseModel.getSuccess())) {

                                //clear the array list
                                userPollLikeCount.clear();
                                //clear the array list
                                userPollLikesUser.clear();
                                //clear the array list
                                userPollcommentsCount.clear();
                                //Getting the details from the response
                                userPollResponse = userPollResponseModel.getResults().getData();
                                //last page count
                                getLastPageResponse = userPollResponseModel.getResults().getLastPage();
                                //current page count
                                getCurrentPageResponse = userPollResponseModel.getResults().getCurrentPage();
                                //If the user poll response is not empty
                                if (!userPollResponse.isEmpty()) {
                                    //If get the current PAGE equal to 1
                                    if (Integer.parseInt(getCurrentPageResponse) == 1) {
                                        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                                        // The Adapter provides access to the data items.
                                        // The Adapter is also responsible for making a View for each item in the data set.
                                        userPollCustomAdapter = new PrivatePollsCustomAdapter(userPollsFragment, userPollResponse, R.layout.publicpoll_singleview, userId, listUserPolls);
                                        //Set bottom footer view
                                        listUserPolls.setLoadingView(R.layout.layout_loading);
                                        //Sets the data behind this ListView.
                                        listUserPolls.setPrivatePollAdapter(userPollCustomAdapter);
                                    } else if (Integer.parseInt(getCurrentPageResponse) <= Integer.parseInt(getLastPageResponse)) {
                                        //the data is added into the array adapterfrom the response
                                        listUserPolls.addPrivatePollData(userPollResponse);
                                        //clear the array list
                                        userPollLikeCount.clear();
                                        //clear the array list
                                        userPollLikesUser.clear();
                                        //clear the array list
                                        userPollcommentsCount.clear();
                                        //Loading the details from the arraylist
                                        userPollcommentsCount = MApplication.loadArray(userPollsFragment, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
                                        //Loading the details from the arraylist
                                        userPollLikesUser = MApplication.loadArray(userPollsFragment, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                                        //Loading the details from the arraylist
                                        userPollLikeCount = MApplication.loadArray(userPollsFragment, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
                                        //Loading the details from the arraylist
                                        userPollParticipateCount = MApplication.loadArray(userPollsFragment, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY, Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                                        //Loading the details from the arraylist
                                        userPollIdAnwserCheck = MApplication.loadStringArray(userPollsFragment, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY, Constants.USER_POLL_ID_ANSWER_SIZE);
                                        //Loading the details from the arraylist
                                        userPollIdAnwser = MApplication.loadStringArray(userPollsFragment, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.USER_POLL_ID_SELECTED_SIZE);
                                    }
                                }
                                /**
                                 * Looping the details from the prefernce
                                 */
                                for (int i = 0; userPollResponse.size() > i; i++) {
                                    //Adding the comments count in array list
                                    userPollcommentsCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignCommentsCounts()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
                                    //Adding the comments count in array list
                                    userPollLikesUser.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignUserLikes()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                                    //Adding the comments count in array list
                                    userPollLikeCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignLikesCounts()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
                                    //Adding the comments count in array list
                                    userPollParticipateCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getPollParticipateCount()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY, Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                                    if (!userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().isEmpty()) {
                                        for (int j = 0; userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().size() > j; j++) {
                                            if (userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getUserId().equals(userId)) {
                                                //adding the response to the array listt
                                                userPollIdAnwserCheck.add(userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollId());
                                                //adding the response to the array listt
                                                userPollIdAnwser.add(userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollAnswer());
                                                //Save the string array in prefernce
                                                MApplication.saveStringArray(userPollsFragment, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY, Constants.USER_POLL_ID_ANSWER_SIZE);
                                                //Save the string array in prefernce
                                                MApplication.saveStringArray(userPollsFragment, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.USER_POLL_ID_SELECTED_SIZE);
                                            }
                                        }
                                    }
                                }

                                //swipe refresh false
                            } else if (("0").equals(userPollResponseModel.getSuccess()) && page == 1) {
                                //visible
                                noUserPoll.setVisibility(View.VISIBLE);
                                //invisible
                                internetConnection.setVisibility(View.INVISIBLE);
                                //invisible
                                relativeList.setVisibility(View.INVISIBLE);
                                //Google progrss stop
                                userPollGoogleNow.progressiveStop();
                                //Google progrss stop
                                userPollGoogleNow.setVisibility(View.GONE);
                                //swipe refresh false
                            }
                            //Google progrss stop
                            userPollGoogleNow.progressiveStop();
                            //Google progrss stop
                            userPollGoogleNow.setVisibility(View.GONE);
                            //swipe refresh false
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Log.e("error", retrofitError.getMessage().toString() + " ");
                            //Failure the response
                            MApplication.errorMessage(retrofitError, userPollsFragment);
                        }
                    });

        } else {
            //internconnection visible
            internetConnection.setVisibility(View.VISIBLE);
            //visiblity gone
            relativeList.setVisibility(View.GONE);
        }

    }

    /**
     * This method is used resquesting the user poll and binding into the adapter
     */
    private void userPollRequestGroup() {
        //If net is connected
        if (MApplication.isNetConnected(userPollsFragment)) {
            //visible
            noUserPoll.setVisibility(View.GONE);
            //View gone
            internetConnection.setVisibility(View.GONE);
            //View visible
            relativeList.setVisibility(View.VISIBLE);
            /**Progree bar visiblity**/
            userPollGoogleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            userPollGoogleNow.progressiveStart();
            Log.e("userId", userId + "");
            Log.e("contactId", contactId + "");
            Log.e("type", type + "");
            Log.e("groupId", groupId + "");
            /**  Requesting the response from the given base url**/
            PrivatePollDisplayRestClient.getInstance().postPrivatePollDisplayApi(new String("get_chatpolls"), new String(userId), new String(contactId), new String(type), new String(groupId), new String(String.valueOf(page))
                    , new Callback<PrivatePollResponseModel>() {
                        @Override
                        public void success(PrivatePollResponseModel userPollResponseModel, Response response) {
                            Log.e("success", userPollResponseModel.getSuccess() + " ");
                            //If the success value is 1 then the below functionality will take place
                            //else if  get success equals 1 and PAGE 1 or get successqueals 0 and PAGE equal to 1 views will be visible
                            if (("1").equals(userPollResponseModel.getSuccess())) {

                                //clear the array list
                                userPollLikeCount.clear();
                                //clear the array list
                                userPollLikesUser.clear();
                                //clear the array list
                                userPollcommentsCount.clear();
                                //Getting the details from the response
                                userPollResponse = userPollResponseModel.getResults().getData();
                                //last page count
                                getLastPageResponse = userPollResponseModel.getResults().getLastPage();
                                //current page count
                                getCurrentPageResponse = userPollResponseModel.getResults().getCurrentPage();
                                //If the user poll response is not empty
                                if (!userPollResponse.isEmpty()) {
                                    //If get the current PAGE equal to 1
                                    if (Integer.parseInt(getCurrentPageResponse) == 1) {
                                        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                                        // The Adapter provides access to the data items.
                                        // The Adapter is also responsible for making a View for each item in the data set.
                                        userPollCustomAdapter = new PrivatePollsCustomAdapter(userPollsFragment, userPollResponse, R.layout.publicpoll_singleview, userId, listUserPolls);
                                        //Set bottom footer view
                                        listUserPolls.setLoadingView(R.layout.layout_loading);
                                        //Sets the data behind this ListView.
                                        listUserPolls.setPrivatePollAdapter(userPollCustomAdapter);
                                    } else if (Integer.parseInt(getCurrentPageResponse) <= Integer.parseInt(getLastPageResponse)) {
                                        //the data is added into the array adapterfrom the response
                                        listUserPolls.addPrivatePollData(userPollResponse);
                                        //clear the array list
                                        userPollLikeCount.clear();
                                        //clear the array list
                                        userPollLikesUser.clear();
                                        //clear the array list
                                        userPollcommentsCount.clear();
                                        //Loading the details from the arraylist
                                        userPollcommentsCount = MApplication.loadArray(userPollsFragment, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
                                        //Loading the details from the arraylist
                                        userPollLikesUser = MApplication.loadArray(userPollsFragment, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                                        //Loading the details from the arraylist
                                        userPollLikeCount = MApplication.loadArray(userPollsFragment, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
                                        //Loading the details from the arraylist
                                        userPollParticipateCount = MApplication.loadArray(userPollsFragment, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY, Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                                        //Loading the details from the arraylist
                                        userPollIdAnwserCheck = MApplication.loadStringArray(userPollsFragment, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY, Constants.USER_POLL_ID_ANSWER_SIZE);
                                        //Loading the details from the arraylist
                                        userPollIdAnwser = MApplication.loadStringArray(userPollsFragment, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.USER_POLL_ID_SELECTED_SIZE);
                                    }
                                }
                                /**
                                 * Looping the details from the prefernce
                                 */
                                for (int i = 0; userPollResponse.size() > i; i++) {
                                    //Adding the comments count in array list
                                    userPollcommentsCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignCommentsCounts()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
                                    //Adding the comments count in array list
                                    userPollLikesUser.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignUserLikes()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                                    //Adding the comments count in array list
                                    userPollLikeCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getCampaignLikesCounts()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
                                    //Adding the comments count in array list
                                    userPollParticipateCount.add(Integer.valueOf(userPollResponseModel.getResults().getData().get(i).getPollParticipateCount()));
                                    //Saving the string array in prefernce
                                    MApplication.saveArray(userPollsFragment, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY, Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                                    if (!userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().isEmpty()) {
                                        for (int j = 0; userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().size() > j; j++) {
                                            if (userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getUserId().equals(userId)) {
                                                //adding the response to the array listt
                                                userPollIdAnwserCheck.add(userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollId());
                                                //adding the response to the array listt
                                                userPollIdAnwser.add(userPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollAnswer());
                                                //Save the string array in prefernce
                                                MApplication.saveStringArray(userPollsFragment, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY, Constants.USER_POLL_ID_ANSWER_SIZE);
                                                //Save the string array in prefernce
                                                MApplication.saveStringArray(userPollsFragment, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.USER_POLL_ID_SELECTED_SIZE);
                                            }
                                        }
                                    }
                                }
                            } else if (("0").equals(userPollResponseModel.getSuccess()) && page == 1) {
                                //visible
                                noUserPoll.setVisibility(View.VISIBLE);
                                //invisible
                                internetConnection.setVisibility(View.INVISIBLE);
                                //invisible
                                relativeList.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Log.e("error", retrofitError.getMessage().toString() + " ");
                            //Failure the response
                            MApplication.errorMessage(retrofitError, userPollsFragment);
                        }
                    });
            //Google progrss stop
            userPollGoogleNow.progressiveStop();
            //Google progrss stop
            userPollGoogleNow.setVisibility(View.GONE);
            //swipe refresh false
        } else {
            //internconnection visible
            internetConnection.setVisibility(View.VISIBLE);
            //visiblity gone
            relativeList.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //If adapter is not null
        if (userPollCustomAdapter != null) {
            //adapter notify data set changed
            userPollCustomAdapter.notifyDataSetChanged();
        }
        //Googkle ad
        MApplication.googleAd(mAdView);
    }

    @Override
    public void loadData() {
        page++;
        userPollRequest();
    }
}
