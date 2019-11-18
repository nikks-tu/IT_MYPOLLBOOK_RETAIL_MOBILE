package com.contus.publicpoll;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.responsemodel.LikesResponseModel;
import com.contus.restclient.GetLikesRestClient;
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
public class CampaignLikes extends Activity implements EndLessListView.EndlessListener {
    //Endless list view
    private EndLessListView list;
    //Activity class
    private CampaignLikes campaignLikesActivity;
    //List model class
    private List<LikesResponseModel.Results.Data> dataResults;
    //Last PAGE
    private String getLastPage;
    //Current PAGE
    private String getCurrentPage;
    //Adapter
    private CampaignLikesPollAdapter adapter;
    //campaignId
    private String campaignId;
    //Page
    private int page = 1;
    //Smooth progress bar
    private SmoothProgressBar googleNow;
    //Relative layout
    private RelativeLayout internetConnection;
    //Internet connection layout
    private RelativeLayout relativeList;
    //Text view no likes
    private TextView txtNoLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        /**View are creating when the activity is started**/
        init();
    }
    /**
     * Instantiate the method
     */
    public void init(){
        /**Initializing the activity**/
        campaignLikesActivity = this;list = (EndLessListView) findViewById(R.id.listView);

        googleNow=(SmoothProgressBar)findViewById(R.id.google_now);
        relativeList = (RelativeLayout)findViewById(R.id.list);
        internetConnection = (RelativeLayout)findViewById(R.id.internetConnection);
        //Getting the poll id values from the intnet
        campaignId = getIntent().getExtras().getString(Constants.CAMPAIGN_ID);
        //Setting the listner
        list.setListener(this);
        //Initializing the array list
        dataResults=new ArrayList<>();
        //Text no likes
        txtNoLikes=(TextView)findViewById(R.id.txtNoLikes);
        //Request for display the poll likes
        campaignLikesRequest();
    }
    /**
     * Request for display the poll likes
     */
    private void campaignLikesRequest() {
        //If net is connected
        if (MApplication.isNetConnected(campaignLikesActivity)) {
            //view invisible
            txtNoLikes.setVisibility(View.INVISIBLE);
            //View gone
            internetConnection.setVisibility(View.GONE);
            //View visible
            relativeList.setVisibility(View.VISIBLE);
            /**Progree bar visiblity**/
            googleNow.setVisibility(View.VISIBLE);
            /**Progress bar start**/
            googleNow.progressiveStart();
            /**  Requesting the response from the given base url**/
            GetLikesRestClient.getInstance().getLikesDetailsCampaign(new String("getCamp_likes"), new String(campaignId), new String("10"), new Integer(page)
                    , new Callback<LikesResponseModel>() {
                @Override
                public void success(LikesResponseModel campaignLikes, Response response) {
                    //If the succes matches with the value 1 then the datat is binded into the adapter
                    //else if get success equals 1 and PAGE 1 or get successqueals 0 and PAGE equal to 1 views will be visible
                    if (("1").equals(campaignLikes.getSuccess())) {
                        campaignLikesResponse(campaignLikes);
                    }else if(("1").equals(campaignLikes.getSuccess())&&page==1||("0").equals(campaignLikes.getSuccess())&&page==1){
                        //View visible
                        txtNoLikes.setVisibility(View.VISIBLE);
                        //View invisible
                        relativeList.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, campaignLikesActivity);
                }
            });
        } else {
            //View visble
            internetConnection.setVisibility(View.VISIBLE);
            //View invlisible
            relativeList.setVisibility(View.GONE);
        }
        //Progressabr stops
        googleNow.progressiveStop();
        //Visiblity gone
        googleNow.setVisibility(View.GONE);
    }

    /**
     * This method is used to bind the data in adapter
     * @param campaignLikes likesResponseModel
     */
    private void campaignLikesResponse(LikesResponseModel campaignLikes) {
        //Like user results from the response
        dataResults = campaignLikes.getResults().getData();
        //Last PAGE from the response
        getLastPage = campaignLikes.getResults().getLastPage();
       //Current PAGE response
        getCurrentPage = campaignLikes.getResults().getCurrentPage();
        //If like user resuts is not empty sets the adapter
            if (!dataResults.isEmpty()) {
                //If the current PAGE equals to 1 initialy setting the adapter
                if (Integer.parseInt(getCurrentPage) == 1) {
                    //An Adapter object acts as a bridge between an AdapterView and the underlying data for that view.
                    // The Adapter provides access to the data items.
                    // The Adapter is also responsible for making a View for each item in the data set.
                    adapter = new CampaignLikesPollAdapter(campaignLikesActivity, dataResults, R.layout.activity_comments_singleview);
                    //Set bottom footer view
                    list.setLoadingView(R.layout.layout_loading);
                    //Sets the data behind this ListView.
                    list.setCampaignLikesAdapter(adapter);
                } else if (Integer.parseInt(getCurrentPage) <= Integer.parseInt(getLastPage)) {
                    //the data is added into the array adapterfrom the response
                    list.addCampaignLikesData(dataResults);
                }
            }
    }

    /**
     * Calling this method from xml file when performing the click on the ACTION
     * @param campaignLikeViewClickAction
     */
    public void onClick(final View campaignLikeViewClickAction) {
        if(campaignLikeViewClickAction.getId()==R.id.imagBackArrow){
            //Finishing the activity
            this.finish();
        }
    }

    @Override
    public void loadData() {
        //Increamenting the PAGE number on scrolling
        page++;
        //Request for display the poll likes
        campaignLikesRequest();
    }
}
