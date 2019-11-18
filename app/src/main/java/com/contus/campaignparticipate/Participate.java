package com.contus.campaignparticipate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.responsemodel.UserPollResponseModel;
import com.contus.restclient.CampaignPollRestClient;
import com.contus.views.EndLessListView;
import com.contusfly.MApplication;
import com.contusfly.smoothprogressbar.SmoothProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
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
public class Participate extends Activity implements EndLessListView.EndlessListener {
    //Participate poll
    private Participate participatePoll;
    //Simple drawer view
    private SimpleDraweeView campaignHeaderImage;
    //header name
    private TextView campaignHeaderName;
    //category
    private TextView campaignHeaderCategory;
    private TextView getTimeShare;
    //campaign id
    private String camPaignId;
    //custom adapter
    private ParticipateButtonCustomAdapter campaignAdapter;
    //Endless list view
    private EndLessListView list;
    //Adview
    private AdView mAdView;
    //user id
    private String userId;
    //Response results
    private List<UserPollResponseModel.Results.Data> dataResults;
    //Get the last page
    private String getLastPage;
    //Page count
    private int page = 1;
    //Get the current page
    private String getCurrentPage;
    //smooth progress bar
    private SmoothProgressBar googleNow;
    //Relative layout
    private RelativeLayout internetConnection;
    //Relative lisy
    private RelativeLayout relativeList;
    //Poll like count
    private ArrayList<Integer> campaignPollLikeCount = new ArrayList<Integer>();
    //Campaign poll like user
    private ArrayList<Integer> campaignPollLikesUser = new ArrayList<Integer>();
    //Campaign poll comments count
    private ArrayList<Integer> campaignPollcommentsCount = new ArrayList<Integer>();
    //Participate
    private Participate campaignParticipate;
    //image share
    private ImageView imgShare;
    //Header view
    private RelativeLayout listHeaderView;
    //Campaign poll answer check
    private ArrayList<String> campaignPollIdAnwserCheck = new ArrayList<String>();
    //Camapign poll id answer
    private ArrayList<String> campaignPollIdAnwser = new ArrayList<String>();
    //Participate count
    private ArrayList<Integer> campaignPollParticipateCount = new ArrayList<Integer>();
    //Swipe refresh
    private SwipeRefreshLayout swipeRefreshLayout;
    private String campaignDate;
    private TextView noCampaignResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paricipation_buttonclick);
        /**View are creating when the activity is started**/
        init();
    }

    /**
     * Instantiate the method
     */
    public void init() {
        campaignParticipate = this;
        list = (EndLessListView) findViewById(R.id.listView);
        internetConnection = (RelativeLayout) findViewById(R.id.internetConnection);
        relativeList = (RelativeLayout) findViewById(R.id.list);
        noCampaignResults = (TextView) findViewById(R.id.noCampaignResults);
        mAdView = (AdView) findViewById(R.id.adView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        userId = MApplication.getString(Participate.this, Constants.USER_ID);
        //Model of the mobile using
        String model = android.os.Build.MODEL;
        //if matches samsung s3
        if (Constants.SAMSUNG_S3.equals(model)) {
            //er-child layout information associated with RelativeLayout.
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);
            //Set the layout parameters associated with this view.
            list.setLayoutParams(params);
        }
        list.setListener(this);
        participatePoll = this;
        googleNow = (SmoothProgressBar) findViewById(R.id.google_now);
        //campaign id from the intent
        camPaignId = getIntent().getExtras().getString(Constants.CAMPAIGN_ID);
        //Campaign name
        String campaignName = getIntent().getExtras().getString(Constants.CAMPAIGN_NAME);
        campaignDate = getIntent().getExtras().getString(Constants.CAMPAIGN_DATE);

        //camapign category
        String camPaignCategory = getIntent().getExtras().getString(Constants.CAMPAIGN_CATEGORY);
        //Campaing logo
        String camPaignLogo = getIntent().getExtras().getString(Constants.CAMPAIGN_LOGO);
        //Setting the string in prefernce
        MApplication.setString(Participate.this, Constants.CAMPAIGN_ID, camPaignId);
        //Setting the string in prefernce
        MApplication.setString(Participate.this, Constants.CAMPAIGN_NAME, campaignName);
        //Setting the string in prefernce
        MApplication.setString(Participate.this, Constants.CAMPAIGN_CATEGORY, camPaignCategory);
        //Setting the string in prefernce
        MApplication.setString(Participate.this, Constants.CAMPAIGN_LOGO, camPaignLogo);
        //Instantiates a layout XML file into its corresponding View objects. It is never used directly. Instead, use getLayoutInflater() or getSystemService(Class) to retrieve a standard LayoutInflater instance that is already
        // hooked up to the current context and correctly configured for the device you are running on
        LayoutInflater inflater = participatePoll.getLayoutInflater();
        listHeaderView = (RelativeLayout) inflater.inflate(R.layout.activity_publicpoll_participate_header, null);
        campaignHeaderImage = (SimpleDraweeView) listHeaderView.findViewById(R.id.imgProfile);
        campaignHeaderName = (TextView) listHeaderView.findViewById(R.id.txtName);
        campaignHeaderCategory = (TextView) listHeaderView.findViewById(R.id.txtCategory);
        getTimeShare = (TextView) listHeaderView.findViewById(R.id.timeShare);
        imgShare = (ImageView) listHeaderView.findViewById(R.id.imgShare);
        campaignHeaderImage.setImageURI(Uri.parse(camPaignLogo));//Setting in the image uri
        campaignHeaderName.setText(campaignName);//Setting the text
        campaignHeaderCategory.setText(camPaignCategory);//Setting the text
        getTimeShare.setText(campaignDate);//Setting the text
        list.addHeaderView(listHeaderView);//Add a fixed view to appear at the top of the list.
        publicPollCampaignResult();//server Request
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                //Swerver request on swipen refresh
                publicPollCampaignResult();
            }
        });
    }


    private void publicPollCampaignResult() {
        //iF net is connected
        if (MApplication.isNetConnected(Participate.this)) {
            //Visiblity is gone
            internetConnection.setVisibility(View.GONE);
            //visible
            relativeList.setVisibility(View.VISIBLE);
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            /**  Requesting the response from the given base url**/
            CampaignPollRestClient.getInstance().postCampaignPublicPoll(new String("publicpolls_api"), new String(camPaignId), new Integer(page), new String(userId)
                    , new Callback<UserPollResponseModel>() {
                        @Override
                        public void success(UserPollResponseModel welcomeResponseModel, Response response) {
                            //clear the array list

                            if (welcomeResponseModel.getSuccess().equals("1")) {
                                campaignPollLikeCount.clear();
                                //clear the array list
                                campaignPollLikesUser.clear();
                                //clear the array list
                                campaignPollcommentsCount.clear();
                                //clear the array list
                                campaignPollParticipateCount.clear();
                                //dataresults from the response
                                dataResults = welcomeResponseModel.getResults().getData();
                                //Getting the last page
                                getLastPage = welcomeResponseModel.getResults().getLastPage();
                                //Getting the current page
                                getCurrentPage = welcomeResponseModel.getResults().getCurrentPage();
                                //If data results is not empty
                                if (!dataResults.isEmpty()) {
                                    //If the current page is weqiual to 1
                                    if (Integer.parseInt(getCurrentPage) == 1) {
                                        //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                                        // The Adapter provides access to the data items.
                                        // The Adapter is also responsible for making a View for each item in the data set.
                                        campaignAdapter = new ParticipateButtonCustomAdapter(campaignParticipate, dataResults, R.layout.publicpoll_singleview, userId, imgShare, list);
                                        //Set bottom footer view
                                        list.setLoadingView(R.layout.layout_loading);
                                        //Sets the data behind this ListView.
                                        list.setCampaignPollAdapter(campaignAdapter);
                                        noCampaignResults.setVisibility(View.GONE);
                                    } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                                        //the data is added into the array adapterfrom the response
                                        list.addCampaignPollData(dataResults);
                                        campaignPollLikeCount.clear(); //clear
                                        campaignPollLikesUser.clear(); //clear
                                        campaignPollcommentsCount.clear(); //clear
                                        campaignPollParticipateCount.clear(); //clear
                                        campaignPollIdAnwserCheck.clear(); //clear
                                        campaignPollIdAnwser.clear(); //clear
                                        //Loading the details from the arraylist
                                        campaignPollcommentsCount = MApplication.loadArray(participatePoll, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
                                        //Loading the details from the arraylist
                                        campaignPollLikesUser = MApplication.loadArray(participatePoll, campaignPollLikesUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
                                        //Loading the details from the arraylist
                                        campaignPollLikeCount = MApplication.loadArray(participatePoll, campaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
                                        //Loading the details from the arraylist
                                        campaignPollParticipateCount = MApplication.loadArray(participatePoll, campaignPollParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
                                        //Loading the details from the arraylist
                                        campaignPollIdAnwserCheck = MApplication.loadStringArray(participatePoll, campaignPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY, Constants.USER_POLL_ID_ANSWER_SIZE);
                                        //Loading the details from the arraylist
                                        campaignPollIdAnwser = MApplication.loadStringArray(participatePoll, campaignPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.USER_POLL_ID_SELECTED_SIZE);
                                    }
                                    /**
                                     * Looping the details from the prefernce
                                     */
                                    for (int i = 0; dataResults.size() > i; i++) {
                                        //Adding the comments count in array list
                                        campaignPollcommentsCount.add(Integer.valueOf(welcomeResponseModel.getResults().getData().get(i).getCampaignCommentsCounts()));
                                        //Save in prefernce
                                        MApplication.saveArray(participatePoll, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
                                        //poll like user count
                                        campaignPollLikesUser.add(Integer.valueOf(welcomeResponseModel.getResults().getData().get(i).getCampaignUserLikes()));
                                        //Save in prefernce
                                        MApplication.saveArray(participatePoll, campaignPollLikesUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
                                        //poll like count
                                        campaignPollLikeCount.add(Integer.valueOf(welcomeResponseModel.getResults().getData().get(i).getCampaignLikesCounts()));
                                        //Save in prefernce
                                        MApplication.saveArray(participatePoll, campaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
                                        //participate count
                                        campaignPollParticipateCount.add(Integer.valueOf(welcomeResponseModel.getResults().getData().get(i).getPollParticipateCount()));
                                        //Save in prefernce
                                        MApplication.saveArray(participatePoll, campaignPollParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
                                        for (int j = 0; welcomeResponseModel.getResults().getData().get(i).getUserParticipatePolls().size() > j; j++) {
                                            if (welcomeResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getUserId().equals(userId)) {
                                                if (welcomeResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(j).getUserId().equals(userId)) {
                                                    //adding the response to the array listt
                                                    campaignPollIdAnwserCheck.add(welcomeResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(0).getPollId());
                                                    //adding the response to the array listt
                                                    campaignPollIdAnwser.add(welcomeResponseModel.getResults().getData().get(i).getUserParticipatePolls().get(0).getPollAnswer());
                                                    //Save the string array in prefernce
                                                    MApplication.saveStringArray(participatePoll, campaignPollIdAnwserCheck, Constants.CAMPAIGN_POLL_ID_ANSWER_ARRAY, Constants.CAMPAIGN_POLL_ID_ANSWER_SIZE);
                                                    //Save the string array in prefernce
                                                    MApplication.saveStringArray(participatePoll, campaignPollIdAnwser, Constants.CAMPAIGN_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.CAMPAIGN_POLL_ID_SELECTED_SIZE);
                                                }
                                            }
                                        }
                                    }
                                }
                                //Google progrss stop

                            } else if (("0").equals(welcomeResponseModel.getSuccess()) && page == 1) {
                                internetConnection.setVisibility(View.INVISIBLE);
                                //gone
                                relativeList.setVisibility(View.INVISIBLE);
                                noCampaignResults.setVisibility(View.VISIBLE);

                            }
                            googleNow.progressiveStop();
                            //visiblity gomne
                            googleNow.setVisibility(View.GONE);
                            //swipe refresh falseVISIBLE
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            //Failure the response
                            MApplication.errorMessage(retrofitError, participatePoll);
                            //Google progrss stop
                            googleNow.progressiveStop();
                            //visiblity gomne
                            googleNow.setVisibility(View.GONE);
                            //swipe refresh false
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

        } else {
            //visible
            internetConnection.setVisibility(View.VISIBLE);
            //gone
            relativeList.setVisibility(View.GONE);
            noCampaignResults.setVisibility(View.GONE);

        }

    }


    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if (clickedView.getId() == R.id.imagBackArrow) {
            //Finishing the activity
            this.finish();
        } else if (clickedView.getId() == R.id.internetRetry) {
            //public poll campaign result
            publicPollCampaignResult();
        }
    }

    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        page++;
        //Request for display the campaign
        publicPollCampaignResult();
    }


    @Override
    public void onResume() {
        super.onResume();
        //Google ad view
        MApplication.googleAd(mAdView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (campaignAdapter != null) {
            //adapter notify data set changed
            campaignAdapter.notifyDataSetChanged();
        }
    }
}
