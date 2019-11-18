package com.contus.mypolls;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.responsemodel.UserPollResponseModel;
import com.contus.restclient.UserPollRestClient;
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

public class MyPolls extends Fragment implements EndLessListView.EndlessListener {
    //endless list view
    private EndLessListView list;
    //view
    private View footerView;
    //adview
    private AdView mAdView;
    //user id
    private String userId;
    //custom adapter
    private MyPollsCustomAdapter myPollAdapter;
    //last page
    private String getLastPage;
    //page
    private int page ;
    //current page
    private String getCurrentPage;
    //smooth progress bar
    private SmoothProgressBar googleNowMyPoll;
    //internet connection
    private RelativeLayout myPollsinternetConnection;
    //relative list
    private RelativeLayout myPollsrelativeList;
    //button retry
    private TextView btnRetry;
    //noo my polss results
    private TextView noMyPollsResults;
    //resposne
    private List<UserPollResponseModel.Results.Data> myPollDataResponse;
    //like count
    private ArrayList<Integer> myPollLikeCount = new ArrayList<Integer>();
    //like user
    private ArrayList<Integer> myPollLikesUser = new ArrayList<Integer>();
    //comments count
    private ArrayList<Integer> myPollcommentsCount = new ArrayList<Integer>();
    //user polls fragment
    private Activity userPollsFragment;
    //poll id
    private ArrayList<String> myPollIdAnwserCheck = new ArrayList<String>();
    //choosen anser
    private ArrayList<String> myPollIdAnwser = new ArrayList<String>();
    //participate count
    private ArrayList<Integer> myParticipateCount = new ArrayList<Integer>();
    //refresh layout
    private SwipeRefreshLayout mSwipeRefreshLayout;

    EndLessListView.EndlessListener test;
    private MyPollsOnInteractionListner mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_fragemnt_mypoll, null);
        list =  root.findViewById(R.id.listView);
        //initializing the activity
        userPollsFragment =getActivity();
        footerView = ((LayoutInflater)userPollsFragment.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_footer, null, false);
        mAdView =  root.findViewById(R.id.adView);
        googleNowMyPoll =  root.findViewById(R.id.google_now);
        myPollsinternetConnection =  root.findViewById(R.id.internetConnection);
        myPollsrelativeList =  root.findViewById(R.id.list);
        noMyPollsResults =  root.findViewById(R.id.noCampaignResults);
        mSwipeRefreshLayout = root.findViewById(R.id.activity_main_swipe_refresh_layout);
        btnRetry =  root.findViewById(R.id.internetRetry);
        page = 1;
        //Model of the mobile using
        String model = android.os.Build.MODEL;
        //if matches samsung s3
        if (Constants.SAMSUNG_S3.equals(model)) {
            //er-child layout information associated with RelativeLayout.
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 5, 0);
            //Set the layout parameters associated with this view.
            list.setLayoutParams(params);
        }
        ///Add a fixed view to appear at the bottom of the list.
        list.addFooterView(footerView);
        //Getting the user id
        userId = MApplication.getString(userPollsFragment, Constants.USER_ID);
        //my poll request
        myPollRequest();
        //set lisner
        list.setListener(this);
        MApplication.setBoolean(userPollsFragment,"mypollfragment",true);
        //button retry
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPollRequest();
            }
        });
        //swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListener != null) {
                    mListener.OnMyPollsFragment("","");
                }  //my poll request
            }
        });
        Log.e("cretae","cretae");

        return root;
    }


    @Override
    public void loadData() {

        //Increamenting the PAGE number on scrolling
        page++;
        //Request for display the campaign
        myPollRequest();
    }

    /**
     * This method is used resquesting the user poll and binding into the adapter
     */
    private void myPollRequest() {

        //If net is connected
        if (MApplication.isNetConnected(getActivity())) {
            //View gone
            myPollsinternetConnection.setVisibility(View.GONE);
            //View visible
            myPollsrelativeList.setVisibility(View.VISIBLE);
            /*Progree bar visiblity**/
            googleNowMyPoll.setVisibility(View.VISIBLE);
            /*Progress bar start**/
            googleNowMyPoll.progressiveStart();
            /*  Requesting the response from the given base url**/
            UserPollRestClient.getInstance().postMyPollApi("myPolls",Integer.valueOf(page), userId
                    , new Callback<UserPollResponseModel>() {
                @Override
                public void success(UserPollResponseModel myPollResponseModel, Response response) {
                    //clear the array list
                    myPollLikeCount.clear();
                    //clear the array list
                    myPollLikesUser.clear();
                    //clear the array list
                    myPollcommentsCount.clear();
                    //clear the array list
                    myParticipateCount.clear();
                    //clear the array list
                    myPollIdAnwserCheck.clear();
                    //clear the array list
                    myPollIdAnwser.clear();
                    if (("1").equals(myPollResponseModel.getSuccess())) {
                        //Getting the details from the response
                        myPollDataResponse = myPollResponseModel.getResults().getData();
                        //last page count
                        getLastPage = myPollResponseModel.getResults().getLastPage();
                        //current page count
                        getCurrentPage = myPollResponseModel.getResults().getCurrentPage();
                        //If the user poll response is not empty
                        if (!myPollDataResponse.isEmpty()) {
                            Log.e("request","request");
                            //If get the current PAGE equal to 1
                            if (Integer.parseInt(getCurrentPage) == 1) {
                                //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                                // The Adapter provides access to the data items.
                                // The Adapter is also responsible for making a View for each item in the data set.
                                myPollAdapter = new MyPollsCustomAdapter(userPollsFragment, myPollDataResponse, R.layout.publicpoll_singleview, userId,list,noMyPollsResults);
                                //Set bottom footer view
                                list.setLoadingView(R.layout.layout_loading);
                                //Sets the data behind this ListView.
                                list.setMyPollAdapter(myPollAdapter);
                            }
                            else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                                //the data is added into the array adapterfrom the response
                                //clear the array list
                                list.addMyPollData(myPollDataResponse);
                                myPollLikeCount.clear();
                                //clear the array list
                                myPollLikesUser.clear();
                                //clear the array list
                                myPollcommentsCount.clear();
                                //clear the array list
                                myParticipateCount.clear();
                                //Loading the details from the arraylist
                                myPollcommentsCount = MApplication.loadArray(userPollsFragment, myPollcommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);
                                //Loading the details from the arraylist
                                myPollLikesUser = MApplication.loadArray(userPollsFragment, myPollLikesUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
                                //Loading the details from the arraylist
                                myPollLikeCount = MApplication.loadArray(userPollsFragment, myPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
                                //Loading the details from the arraylist
                                myParticipateCount = MApplication.loadArray(userPollsFragment, myParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE);
                                //Loading the details from the arraylist
                                myPollIdAnwserCheck= MApplication.loadStringArray(userPollsFragment, myPollIdAnwserCheck, Constants.MY_POLL_ID_ANSWER_ARRAY, Constants.MY_POLL_ID_ANSWER_SIZE);
                                //Loading the details from the arraylist
                                myPollIdAnwser= MApplication.loadStringArray(userPollsFragment, myPollIdAnwser, Constants.MY_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.MY_POLL_ID_SELECTED_SIZE);
                            }
                        }
                        /*
                         * Looping the details from the preference
                         */
                        for (int i = 0; myPollDataResponse.size() > i; i++) {
                            //Adding the comments count in array list
                            myPollcommentsCount.add(Integer.valueOf(myPollResponseModel.getResults().getData().get(i).getCampaignCommentsCounts()));
                            MApplication.saveArray(userPollsFragment, myPollcommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);
                            myPollLikesUser.add(Integer.valueOf(myPollResponseModel.getResults().getData().get(i).getCampaignUserLikes()));
                            MApplication.saveArray(userPollsFragment, myPollLikesUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
                            myPollLikeCount.add(Integer.valueOf(myPollResponseModel.getResults().getData().get(i).getCampaignLikesCounts()));
                            MApplication.saveArray(userPollsFragment, myPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
                            myParticipateCount.add(Integer.valueOf(myPollResponseModel.getResults().getData().get(i).getPollParticipateCount()));
                            MApplication.saveArray(userPollsFragment, myParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE);
                            if (!myPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().isEmpty()) {
                                for (int j = 0; myPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().size() > j; j++) {
                                    if (!myPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().isEmpty()) {
                                        myPollIdAnwserCheck.add(myPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(0).getPollId());
                                        myPollIdAnwser.add(myPollResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(0).getPollAnswer());
                                        MApplication.saveStringArray(userPollsFragment, myPollIdAnwserCheck, Constants.MY_POLL_ID_ANSWER_ARRAY, Constants.MY_POLL_ID_ANSWER_SIZE);
                                        MApplication.saveStringArray(userPollsFragment, myPollIdAnwser, Constants.MY_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.MY_POLL_ID_SELECTED_SIZE);
                                    }
                                }
                            }
                        }
                    }else if(("1").equals(myPollResponseModel.getSuccess())&&page==1||("0").equals(myPollResponseModel.getSuccess())&&page==1)
                    {
                        noMyPollsResults.setVisibility(View.VISIBLE);
                        myPollsinternetConnection.setVisibility(View.INVISIBLE);
                        myPollsrelativeList.setVisibility(View.INVISIBLE);
                    }
                    googleNowMyPoll.progressiveStop();
                    googleNowMyPoll.setVisibility(View.GONE);
                    //mSwipeRefreshLayout.setRefreshing(false);
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    MApplication.errorMessage(retrofitError, userPollsFragment);
                    googleNowMyPoll.progressiveStop();
                    googleNowMyPoll.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        } else {
            myPollsinternetConnection.setVisibility(View.VISIBLE);
            myPollsrelativeList.setVisibility(View.GONE);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if(myPollAdapter!=null){
            myPollAdapter.notifyDataSetChanged();
        }
        MApplication.googleAd(mAdView);
     Log.e("resume","resume");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface MyPollsOnInteractionListner {
        void OnMyPollsFragment(String type, String id);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            userPollsFragment = (Activity) context;
        }
        try {
            mListener = (MyPollsOnInteractionListner) userPollsFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(userPollsFragment.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
