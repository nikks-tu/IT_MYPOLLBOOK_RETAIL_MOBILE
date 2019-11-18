package com.contus.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.contus.campaignparticipate.CampignPollCommentsAdapter;
import com.contus.campaignparticipate.ParticipateButtonCustomAdapter;
import com.contus.comments.PollCommentsAdapter;
import com.contus.likes.PollLikesAdapter;
import com.contus.mypolls.MyPollsCommentsAdapter;
import com.contus.mypolls.MyPollsCustomAdapter;
import com.contus.participation.CustomParticpationPollAdapter;
import com.contus.publicpoll.CampaignCommentsAdapter;
import com.contus.publicpoll.CampaignLikesPollAdapter;
import com.contus.publicpoll.CustomAdapter;
import com.contus.responsemodel.CommentDisplayResponseModel;
import com.contus.responsemodel.LikesResponseModel;
import com.contus.responsemodel.ParticipateResponseModel;
import com.contus.responsemodel.PrivatePollResponseModel;
import com.contus.responsemodel.PublicPollResponseModel;
import com.contus.responsemodel.UserPollResponseModel;
import com.contus.search.SearchAdapter;
import com.contus.search.SearchPollCommentsAdapter;
import com.contus.userpolls.UserPollCustomAdapter;
import com.contusfly.privatepolls.PrivatePollCommentsAdapter;
import com.contusfly.privatepolls.PrivatePollLikesAdapter;
import com.contusfly.privatepolls.PrivatePollsCustomAdapter;
import com.new_chanages.adapters.GroupPollsCustomAdapter;
import com.new_chanages.models.GroupPollsMainObject;

import java.util.List;

public class EndLessListView extends ListView implements OnScrollListener {
//Footer view
    private View footer;
    //boolean value
    private boolean isLoading;
    //Listner
    private EndlessListener listener;
    //Custom adapter
    private CustomAdapter adapter;
    //CampaignCommentsAdapter
    private CampaignCommentsAdapter commentsAdapter;
    //PollCommentsAdapter
    private PollCommentsAdapter pollCommentsAdapter;
    //PollCommentsAdapter
    private PrivatePollCommentsAdapter privatePollCommentsAdapter;
    //CampaignLikesPollAdapter
    private CampaignLikesPollAdapter customCampaignLikesAdapter;
    //PollLikesAdapter
    private PollLikesAdapter customLikesPollAdapter;
    //PollLikesAdapter
    private PrivatePollLikesAdapter priavteLikesPollAdapter;

    //UserPollCustomAdapter
    private UserPollCustomAdapter userPolladapter;
    private GroupPollsCustomAdapter groupPolladapter;
    //UserPollCustomAdapter
    private PrivatePollsCustomAdapter privatePolladapter;
    //MyPollsCustomAdapter
    private MyPollsCustomAdapter myPolladapter;
    //ParticipateButtonCustomAdapter
    private ParticipateButtonCustomAdapter campaignPollAdapter;
   //CampignPollCommentsAdapter
    private CampignPollCommentsAdapter campignpollsCommentsAdapter;
    //MyPollsCommentsAdapter
    private MyPollsCommentsAdapter myPollCommentsAdapter;
    //CustomParticpationPollAdapter
    private CustomParticpationPollAdapter customParticipationAdapter;
    //SearchAdapter
    private SearchAdapter searchAdapter;
    private SearchPollCommentsAdapter searchPollCommentsAdapter;

    /**
     * initializes a new instance of the ListView class.
     * @param context context
     * @param attrs attrs
     * @param defStyle
     */

    public EndLessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);
    }

    /**
     * initializes a new instance of the ListView class.
     * @param context
     * @param attrs
     */
    public EndLessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
//Set the listener that will receive notifications every time the list scrolls.
        this.setOnScrollListener(this);
    }

    public EndLessListView(Context context) {
        super(context);
        //Set the listener that will receive notifications every time the list scrolls.
        this.setOnScrollListener(this);
    }

    /**
     *  Set the listener that will receive notifications every time the list scrolls.
     * @param listener
     */
    public void setListener(EndlessListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//Returns the adapter currently in use in this ListView.
        if (getAdapter() == null)
            return;
        if (getAdapter().getCount() == 0)
            return;
        int l = visibleItemCount + firstVisibleItem;
        if (l >= totalItemCount && !isLoading) {
            // It is time to add new data. We call the listener
            this.addFooterView(footer);
            isLoading = true;
            listener.loadData();

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("","");
    }

    /**
     * This method is used to set the loading in footer view
     * @param resId
     */
    public void setLoadingView(int resId) {
        //Instantiates a layout XML file into its corresponding View objects. It is never used directly. Instead, use getLayoutInflater()
        LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(resId, null);
        //Add a fixed view to appear at the bottom of the list.
        this.addFooterView(footer);
    }

    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setAdapter(CustomAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        //remove the footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to set the adapter
     * @param customCommentsadapter
     */
    public void setAdapter(CampaignCommentsAdapter customCommentsadapter) {
        super.setAdapter(customCommentsadapter);
        this.commentsAdapter = customCommentsadapter;
        //remove the footer view
        this.removeFooterView(footer);
    }

    /**
     * This method is used to set the adapter
     * @param customLikesadapter
     */
    public void setCampaignLikesAdapter(CampaignLikesPollAdapter customLikesadapter) {
        super.setAdapter(customLikesadapter);
        this.customCampaignLikesAdapter = customLikesadapter;
        //remove the footer view
        this.removeFooterView(footer);
    }

    /**
     * This method is used to store the data from the response in array list
     * @param data
     */
    public void addUserPollCommentsData(List<CommentDisplayResponseModel.Results.Data> data) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        pollCommentsAdapter.addAll(data);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        pollCommentsAdapter.notifyDataSetChanged();
        isLoading = false;

    }

    /**
     * This method is used to store the data from the response in array list
     * @param data
     */
    public void addPrivatePollCommentsData(List<CommentDisplayResponseModel.Results.Data> data) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        privatePollCommentsAdapter.addAll(data);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        privatePollCommentsAdapter.notifyDataSetChanged();
        isLoading = false;

    }
    /**
     * This method is used to store the data from the response in array list
     * @param data
     */
    public void addCamapignCommetsData(List<CommentDisplayResponseModel.Results.Data> data) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        commentsAdapter.addAll(data);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        commentsAdapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to store the data from the response in array list
     * @param data
     */
    public void addNewData(List<PublicPollResponseModel.Data> data) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        adapter.addAll(data);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        adapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setPollLikesAdapter(PollLikesAdapter adapter) {
        super.setAdapter(adapter);
        this.customLikesPollAdapter = adapter;
        //remove the footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setPrivatePollLikesAdapter(PrivatePollLikesAdapter adapter) {
        super.setAdapter(adapter);
        this.priavteLikesPollAdapter = adapter;
        //remove the footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addPrivatePollLikesData(List<LikesResponseModel.Results.Data> dataResults) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        priavteLikesPollAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        priavteLikesPollAdapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addPollLikesData(List<LikesResponseModel.Results.Data> dataResults) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        customLikesPollAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        customLikesPollAdapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addCampaignLikesData(List<LikesResponseModel.Results.Data> dataResults) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        customCampaignLikesAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        customCampaignLikesAdapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addPollCommetsData(List<CommentDisplayResponseModel.Results.Data> dataResults) {
    //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        pollCommentsAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        pollCommentsAdapter.notifyDataSetChanged();
        isLoading = false;

    }
    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setCommentsPoll(PollCommentsAdapter adapter) {
        super.setAdapter(adapter);
        this.pollCommentsAdapter = adapter;
        //remove footer view
        this.removeFooterView(footer);
    }


    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setPrivateCommentsPoll(PrivatePollCommentsAdapter adapter) {
        super.setAdapter(adapter);
        this.privatePollCommentsAdapter = adapter;
        //remove footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setSearchCommentsPoll(SearchPollCommentsAdapter adapter) {
        super.setAdapter(adapter);
        this.searchPollCommentsAdapter = adapter;
        //remove footer view
        this.removeFooterView(footer);
    }

    /**
     * This method is used to store the data from the response in array list
     * @param data
     */
    public void addSearchPollCommentsData(List<CommentDisplayResponseModel.Results.Data> data) {
        //remove footer view
        this.removeFooterView(footer);
        //Add all data into the adapter
        searchPollCommentsAdapter.addAll(data);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        searchPollCommentsAdapter.notifyDataSetChanged();
        isLoading = false;

    }
    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setmyPollsCommentsPoll(MyPollsCommentsAdapter adapter) {
        super.setAdapter(adapter);
        this.myPollCommentsAdapter = adapter;
        //remove footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to store the data from the response in array list
     * @param data
     */
    public void addmyPollCommentsData(List<CommentDisplayResponseModel.Results.Data> data) {
        //add data
        myPollCommentsAdapter.addAll(data);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        myPollCommentsAdapter.notifyDataSetChanged();
        isLoading = false;

    }
    /**
     * This method is used to set the adapter
     * @param adapter
     */
    public void setCampignPollComents(CampignPollCommentsAdapter adapter) {
        super.setAdapter(adapter);
        this.campignpollsCommentsAdapter = adapter;
        //remove footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addCampignPollsCommetsData(List<CommentDisplayResponseModel.Results.Data> dataResults) {
        //remove footer view
        this.removeFooterView(footer);
        campignpollsCommentsAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        campignpollsCommentsAdapter.notifyDataSetChanged();
        isLoading = false;

    }
    /**
     * This method is used to set the adapter
     * @param userPolladapter
     */
    public void setUserPollAdapter(UserPollCustomAdapter userPolladapter) {
        super.setAdapter(userPolladapter);
        this.userPolladapter = userPolladapter;
        //remove footer view
        this.removeFooterView(footer);
    }

    /**
     * This method is used to set the adapter
     * @param privatePolladapter
     */
    public void setPrivatePollAdapter(PrivatePollsCustomAdapter privatePolladapter) {
        super.setAdapter(privatePolladapter);
        this.privatePolladapter = privatePolladapter;
        //remove footer view
        this.removeFooterView(footer);
    }

    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addPrivatePollData(List<PrivatePollResponseModel.Results.Data> dataResults) {
        //remove footer viewe
        this.removeFooterView(footer);
        //add data in array list
        privatePolladapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        privatePolladapter.notifyDataSetChanged();
        isLoading = false;
    }

    /**
     * This method is used to set the adapter
     * @param searchAdapter
     */
    public void setSearchPollAdapter(SearchAdapter searchAdapter) {
        super.setAdapter(searchAdapter);
        this.searchAdapter = searchAdapter;
        //remove footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addSearchPollData(List<UserPollResponseModel.Results.Data> dataResults) {
        //remove footer viewe
        this.removeFooterView(footer);
        //add data in array list
        searchAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        searchAdapter.notifyDataSetChanged();
        isLoading = false;
    }
     /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addUserPollData(List<UserPollResponseModel.Results.Data> dataResults) {
        //remove footer view
        this.removeFooterView(footer);
        //add data in array list
        userPolladapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        userPolladapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to set the adapter
     * @param myPolladapter
     */
    public void setMyPollAdapter(MyPollsCustomAdapter myPolladapter) {
        super.setAdapter(myPolladapter);
        this.myPolladapter = myPolladapter;
        //remove footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to set the adapter
     * @param customParticipationAdapter
     */
    public void setCustomParticipationAdapter(CustomParticpationPollAdapter customParticipationAdapter) {
        super.setAdapter(customParticipationAdapter);
        this.customParticipationAdapter = customParticipationAdapter;
        //remove footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to set the adapter
     * @param dataResults
     */
    public void addCustomParticipate(List<ParticipateResponseModel.Results.Data> dataResults) {
        //remove footer viewe
        this.removeFooterView(footer);
        //add data in array list
        customParticipationAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        customParticipationAdapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addMyPollData(List<UserPollResponseModel.Results.Data> dataResults) {
        //remove footer viewe
        this.removeFooterView(footer);
         //add data in array list
        myPolladapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        myPolladapter.notifyDataSetChanged();
        isLoading = false;
    }
    /**
     * This method is used to set the adapter
     * @param campaignPollAdapter
     */
    public void setCampaignPollAdapter(ParticipateButtonCustomAdapter campaignPollAdapter) {
        super.setAdapter(campaignPollAdapter);
        this.campaignPollAdapter = campaignPollAdapter;
        //remove footer view
        this.removeFooterView(footer);
    }
    /**
     * This method is used to store the data from the response in array list
     * @param dataResults
     */
    public void addCampaignPollData(List<UserPollResponseModel.Results.Data> dataResults) {
        //remove footer viewe
        this.removeFooterView(footer);
        //add data in array list
        campaignPollAdapter.addAll(dataResults);
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        campaignPollAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    public void addGroupPollData(GroupPollsMainObject pollResponse) {
        this.removeFooterView(footer);
        //add data in array list
        groupPolladapter.addAll(pollResponse.getResults().getData());
        //Notifies the attached observers that the underlying
        // data has been changed and any View reflecting the data set should refresh itself.
        groupPolladapter.notifyDataSetChanged();
        isLoading = false;
    }

    //Interface
    public interface EndlessListener {
        //load data empty method
        void loadData();
    }

    public void setGroupPollAdapter(GroupPollsCustomAdapter groupPollAdapter) {
        super.setAdapter(groupPollAdapter);
        this.groupPolladapter = groupPollAdapter;
        //remove footer view
        this.removeFooterView(footer);
    }

}