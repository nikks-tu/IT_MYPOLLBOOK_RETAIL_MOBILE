package com.new_chanages.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.views.EndLessListView;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.google.android.gms.ads.AdView;
import com.new_chanages.adapters.GroupPollsCustomAdapter;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.models.GroupPollDataObject;
import com.new_chanages.models.GroupPollsMainObject;
import com.new_chanages.models.TopUsersModel;
import com.new_chanages.postParameters.GetGroupsPostParameters;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GroupPolls extends Fragment implements EndLessListView.EndlessListener {
    //Endlesslistview
    private EndLessListView listUserPolls;
    //Footerview
    View footerView;
    //Adview
    private AdView mAdView;
    //Userid
    private String userId;
    private String groupId;
    //Userpollcustom adapter
    private GroupPollsCustomAdapter groupPollCustomAdapter;
    //Get the last page
    String getLastPageResponse;
    //page
    private int page;
    //Get the current page
    String getCurrentPageResponse;
    //Smooth progress bar
    private SmoothProgressBar userPollGoogleNow;
    //Relative layout
    private RelativeLayout internetConnection;
    //Relative layout
    private RelativeLayout relativeList;
    //Button retry
    TextView btnRetryUserPoll;
    //no user poll
    private TextView noUserPoll, title, toolbar_title;
    Context context;
    //user poll response
    List<GroupPollDataObject> userPollResponse;
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
    private Activity groupPollsFragment;
    //participate count
    private ArrayList<Integer> userPollParticipateCount = new ArrayList<Integer>();
    //model
    String model;
    //swipe refresh layout
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GroupPollOnFragmentInteractionListner mListener;
    private Activity activity;
    private MApplication mApplication;
    RecyclerView top_ten_list;
    ArrayList<TopUsersModel> toptenList;
    Boolean versionFlag=true;
    String action = "getgrouppollsapi";
    String groupName = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_userpoll, null);
        listUserPolls =  root.findViewById(R.id.listView);
        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_footer, null, false);
        mAdView =  root.findViewById(R.id.adView);
        userPollGoogleNow =  root.findViewById(R.id.google_now);
        internetConnection =  root.findViewById(R.id.internetConnection);
        relativeList =  root.findViewById(R.id.list);
        noUserPoll =  root.findViewById(R.id.noCampaignResults);
        mSwipeRefreshLayout =  root.findViewById(R.id.activity_main_swipe_refresh_layout);
        btnRetryUserPoll =  root.findViewById(R.id.internetRetry);
        top_ten_list     =  root.findViewById(R.id.top_ten_list);
        title     =  root.findViewById(R.id.title);
        groupPollsFragment = getActivity();
        mApplication = new MApplication();
        top_ten_list.setVisibility(View.GONE);
        Toolbar toolbar = getActivity().findViewById(R.id.mToolbar);
        toolbar_title =  toolbar.findViewById(R.id.toolbar_title);
        title.setVisibility(View.GONE);
        ImageView img_add_group = toolbar.findViewById(R.id.img_add_group);
        img_add_group.setVisibility(View.GONE);
        page = 1;

        MApplication.setBoolean(groupPollsFragment, Constants.SEARCH_BACKPRESS_BOOLEAN, false);
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
        //Getting the user id
        userId = MApplication.getString(groupPollsFragment, Constants.USER_ID);
        //set listner
        listUserPolls.setListener(this);
        //request fro the api
        //userPollRequest();
        groupId = MApplication.getString(getContext(), Constants.GET_GROUP_POLL_ID);
        groupName = MApplication.getString(getContext(), Constants.GET_GROUP_NAME);
        toolbar_title.setText(groupName);
        serviceCall();
        //validateAppVersion();

        //showVersionPopup();
        //Interface definition for a callback to be invoked when a view is clicked.
        btnRetryUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnUserPollFragment("", "");
                }
            }
        });
        //swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListener != null) {
                    mListener.OnUserPollFragment("", "");
                }  //my poll request
            }
        });

        return root;
    }


    @Override
    public void loadData() {
        Log.e("load", "load");
        //Incrementing the PAGE number on scrolling
        page++;
        //Request for display the campaign
        //userPollRequest();
    }

    /**
     * This method is used requesting the user poll and binding into the adapter
     */


    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);

        Call<GroupPollsMainObject> call = service.getGroupPolls(new GetGroupsPostParameters(action, Integer.parseInt(userId), groupId));
        call.enqueue(new retrofit2.Callback<GroupPollsMainObject>() {
            @Override
            public void onResponse(Call<GroupPollsMainObject> call, retrofit2.Response<GroupPollsMainObject> response) {
                if(response.body()!=null){
                   response(response.body());
                }else {
                    /*Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();*/
                }
            }
            @Override
            public void onFailure(Call<GroupPollsMainObject> call, Throwable t) {
                Toast toast = Toast.makeText(getContext() , ""+t, Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    private void response(GroupPollsMainObject pollResponse) {
        //Google progress stop
        userPollGoogleNow.progressiveStop();
        //Google progress stop
        userPollGoogleNow.setVisibility(View.GONE);
        // MApplication.materialdesignDialogStop();
        //swipe refresh false
        mSwipeRefreshLayout.setRefreshing(false);

        if (("1").equals(pollResponse.getSuccess())) {
            if (groupPollsFragment != null) {
                MApplication.setBoolean(groupPollsFragment, Constants.SEARCH_BACKPRESS_BOOLEAN, false);
            }
            //clear the array list
            userPollLikeCount.clear();
            //clear the array list
            userPollLikesUser.clear();
            //clear the array list
            userPollcommentsCount.clear();
            userPollParticipateCount.clear();
            //Getting the details from the response
            userPollResponse = pollResponse.getResults().getData();
            //last page count
            getLastPageResponse = String.valueOf(pollResponse.getResults().getLastPage());
            //current page count
            getCurrentPageResponse = String.valueOf(pollResponse.getResults().getCurrentPage());
            /*Intent getCount = new Intent("get_user_poll_details");
            getCount.putExtra("admin_count", userPollResponse.get(0).getAdminCount());
            getCount.putExtra("user_count", userPollResponse.get(0).getUserCount());*/
            //LocalBroadcastManager.getInstance(mApplication).sendBroadcast(new Intent(getCount));

            //If the user poll response is not empty
            if (!userPollResponse.isEmpty()) {
                //If get the current PAGE equal to 1
                if (Integer.parseInt(getCurrentPageResponse) == 1) {
                    if (groupPollsFragment != null) {
                        // An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                        // The Adapter provides access to the data items.
                        // The Adapter is also responsible for making a View for each item in the data set.
                        groupPollCustomAdapter = new GroupPollsCustomAdapter(groupPollsFragment, userPollResponse, R.layout.publicpoll_singleview, userId, listUserPolls);
                        //Set bottom footer view
                        listUserPolls.setLoadingView(R.layout.layout_loading);
                        //Sets the data behind this ListView.
                        listUserPolls.setGroupPollAdapter(groupPollCustomAdapter);
                    }
                } else if (Integer.parseInt(getCurrentPageResponse) <= Integer.parseInt(getLastPageResponse)) {
                    //the data is added into the array adapter from the response
                    listUserPolls.addGroupPollData(pollResponse);
                    //clear the array list
                    userPollLikeCount.clear();
                    //clear the array list
                    userPollLikesUser.clear();
                    //clear the array list
                    userPollcommentsCount.clear();
                    userPollParticipateCount.clear();
                    //Loading the details from the arraylist
                    userPollcommentsCount = MApplication.loadArray(groupPollsFragment, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
                    //Loading the details from the arraylist
                    userPollLikesUser = MApplication.loadArray(groupPollsFragment, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                    //Loading the details from the arraylist
                    userPollLikeCount = MApplication.loadArray(groupPollsFragment, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
                    //Loading the details from the arraylist
                    userPollParticipateCount = MApplication.loadArray(groupPollsFragment, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY, Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                    //Loading the details from the arraylist
                    userPollIdAnwserCheck = MApplication.loadStringArray(groupPollsFragment, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY, Constants.USER_POLL_ID_ANSWER_SIZE);
                    //Loading the details from the arraylist
                    userPollIdAnwser = MApplication.loadStringArray(groupPollsFragment, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.USER_POLL_ID_SELECTED_SIZE);
                }
            }
            /*
             * Looping the details from the preference
             */
            //Commented by Nikita
            for (int i = 0; userPollResponse.size() > i; i++) {
                //Adding the comments count in array list
                userPollcommentsCount.add(Integer.valueOf(pollResponse.getResults().getData().get(i).getPollCommentsCounts()));
                //Saving the string array in preference
                MApplication.saveArray(groupPollsFragment, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
                //Adding the comments count in array list
                userPollLikesUser.add(Integer.valueOf(pollResponse.getResults().getData().get(i).getUserPollLikes()));
                //Saving the string array in preference
                MApplication.saveArray(groupPollsFragment, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                //Adding the comments count in array list
                userPollLikeCount.add(Integer.valueOf(pollResponse.getResults().getData().get(i).getPollLikesCounts()));
                //Saving the string array in preference
                MApplication.saveArray(groupPollsFragment, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
                //Adding the comments count in array list
                userPollParticipateCount.add(Integer.valueOf(pollResponse.getResults().getData().get(i).getPollParticipateCount()));
                //Saving the string array in preference
                MApplication.saveArray(groupPollsFragment, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY, Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                if (!pollResponse.getResults().getData().get(i).getUserParticipatePolls().isEmpty()) {
                    for (int j = 0; pollResponse.getResults().getData().get(i).getUserParticipatePolls().size() > j; j++) {
                        if (pollResponse.getResults().getData().get(i).getUserParticipatePolls().get(j).getUserId().equals(userId)) {
                            //adding the response to the array list
                            userPollIdAnwserCheck.add(String.valueOf(pollResponse.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollAnswerId()));
                            //adding the response to the array list
                            userPollIdAnwser.add(pollResponse.getResults().getData().get(i).getUserParticipatePolls().get(j).getPollAnswer());
                            //Save the string array in preference
                            MApplication.saveStringArray(groupPollsFragment, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY, Constants.USER_POLL_ID_ANSWER_SIZE);
                            //Save the string array in preference
                            MApplication.saveStringArray(groupPollsFragment, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.USER_POLL_ID_SELECTED_SIZE);
                        }
                    }
                }
            }
        } else if (("0").equals(pollResponse.getSuccess()) && page == 1) {
            //visible
            noUserPoll.setVisibility(View.VISIBLE);
            //invisible
            internetConnection.setVisibility(View.INVISIBLE);
            //invisible
            relativeList.setVisibility(View.INVISIBLE);
        }
        //   MApplication.materialdesignDialogStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        //If adapter is not null
        if (groupPollCustomAdapter != null) {
            //adapter notify data set changed
            groupPollCustomAdapter.notifyDataSetChanged();
        }
        //Google ad
        MApplication.googleAd(mAdView);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // MApplication.materialdesignDialogStop();
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

    public interface GroupPollOnFragmentInteractionListner {
        void OnUserPollFragment(String type, String id);
    }

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            userPollsFragment = (Activity) context;
        }
        try {
            mListener = (GroupPollOnFragmentInteractionListner) userPollsFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/

}
