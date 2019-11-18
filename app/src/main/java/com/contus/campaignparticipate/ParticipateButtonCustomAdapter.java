package com.contus.campaignparticipate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.likes.PollLikes;
import com.contus.responsemodel.LikeUnLikeResposneModel;
import com.contus.responsemodel.PollParticipateResponseModel;
import com.contus.responsemodel.UserPollResponseModel;
import com.contus.restclient.LikesAndUnLikeRestClient;
import com.contus.restclient.PollParticipateRestClient;
import com.contus.userpolls.FullImageView;
import com.contus.views.EndLessListView;
import com.contus.views.VideoLandscapeActivity;
import com.contusfly.MApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 10/6/2015.
 */
public class ParticipateButtonCustomAdapter extends ArrayAdapter<UserPollResponseModel.Results.Data> {
    //Layout
    private final int layoutId;
    //user id
    private final String userId;
    //Image share
    private final ImageView imgShare;
    //Activity
    private final Participate participateActivity;
    private final EndLessListView list;
    //Response from the server
    private UserPollResponseModel.Results.Data campaignPollResponse;
    //Youtube url
    private String youTubeUrl;
    //Likes
    private int mlikes;
    //Holder class for view1
    private ViewHolderLayoutOne holder1;
    //Holder class for view2
    private ViewHolderLayoutTwo holder2;
    //Holder class for view3
    private ViewHolderLayoutThree holder3;
    //Holder class for view4
    private ViewHolderLayoutFour holder4;
    //View id
    private String idRefrenceView;
    //Poll question
    private String pollQuestion;
    //Poll answer1
    private String pollAnswer1="";
    //Poll answer2
    private String pollAnswer2="";
    //Poll answer3
    private String pollAnswer3="";
    //Poll answer4
    private String pollAnswer4="";
    //Poll questionImage
    private String pollImage1="";
    //Poll questionImage2
    private String pollImage2="";
    //array list for saving the likes counts
    private ArrayList<Integer> prefrenceCampaignPollLikeCount = new ArrayList<Integer>();
    //array list for saving the likes counts
    private ArrayList<Integer> campaignPollLikeCount = new ArrayList<Integer>();
    //array list for saving the likes user
    private ArrayList<Integer> campaignPollLikesUser = new ArrayList<Integer>();
    //like user arrayList
    private ArrayList<Integer> preferenceCampaignPollLikeUser;
    //array list COMMENTS count
    private ArrayList<Integer> campaignPollcommentsCount = new ArrayList<Integer>();
    //prefernce array list COMMENTS count
    private ArrayList<Integer> prefrenceCampaignPollCommentsCount;
    //Poll type
    private String pollType;
    //Preference user poll id answer
    private ArrayList<String> preferenceCampaignPollIdAnswer;
    //Preference user poll id answer selected
    private ArrayList<String> preferenceCampaignPollIdAnswerSelected;
    //Poll id answer selected
    private ArrayList<String> camapignPollIdAnwserCheck = new ArrayList<String>();
    //poll id answer check
    private ArrayList<String> campaignPollIdAnwser = new ArrayList<String>();
    //Poll answer
    private String pollAnswer;
    //Poll answer selected id
    private String pollAnswerSelectedId;
    //Participate count
    private ArrayList<Integer> myParticipateCount = new ArrayList<Integer>();
    //Preference participate count
    private ArrayList<Integer> preferenceCampaignPollParticipateCount;
    //Image
    private String image="";
    //Adapter view
    private View mParticipateView;
    /**
     * OnClick listner for question image2
     */
    private View.OnClickListener mCampaignPollAdapterQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position-1;
            //Response from  the server based on the position
            campaignPollResponse = getItem(clickPosition);
            //Question image
            pollImage2 = campaignPollResponse.getPollquestionImage1();
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, pollImage2);
        }
    };
    /**
     * OnClick listner for question single image
     */

    private View.OnClickListener mCampaignPollAdapterSingleOption = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position-1;
            //Response from  the server based on the position
            campaignPollResponse = getItem(clickPosition);
            //Question image
            if (("").equals(campaignPollResponse.getPollquestionImage1())) {
                image = campaignPollResponse.getPollquestionImage();
            } else {
                image = campaignPollResponse.getPollquestionImage1();
            }
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, image);
        }
    };
    /**
     * OnClick listner for question question image1
     */
    private View.OnClickListener mCampaignPollAdapterQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position-1;
            //Response from  the server based on the position
            campaignPollResponse = getItem(clickPosition);
            //Question image
            pollImage1 = campaignPollResponse.getPollquestionImage();
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, pollImage1);
        }
    };
    /**
     * OnClick listner for question comment
     */
    private View.OnClickListener mCampaignPollAdapterCommentClickAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position-1;
            //This method will be called wto show the comment
            commentClickAction(clickPosition);
        }
    };
    /**
     * OnClick listner for question like unlike check box
     */
    private View.OnClickListener mCampaignPollAdapterLikeUnlikeCheckBox = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position-1;
            //Response from  the server based on the position
            campaignPollResponse = getItem(clickPosition);
            //Moving to the like activity
            likeClickAction(campaignPollResponse);
        }
    };

    /**
     * OnClick listner for question participant count
     */
    private View.OnClickListener mCampaignPollAdapterParticipateCounts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position-1;
            //Participant view method
            clickAction(clickPosition);
        }
    };
    /**
     * initializes a new instance of the ListView class.
     *
     * @param dataResults -reasponse from the server
     * @param layoutId    -layout id
     * @param userId      -userid
     *
     */
    public ParticipateButtonCustomAdapter(Participate campaignParticipate, List<UserPollResponseModel.Results.Data> dataResults, int layoutId, String userId,ImageView imgShare, EndLessListView list) {
        super(campaignParticipate,0,dataResults);
        this.layoutId = layoutId;
        this.participateActivity = campaignParticipate;
        this.userId = userId;
        this.imgShare=imgShare;
        this.list=list;
    }


    /**
     * Image full view method
     * @param clickPosition-position
     * @param pollImageMyPoll-image url
     */
    private void imageQuestionClickAction(int clickPosition, String pollImageMyPoll) {
        //response from the server
        campaignPollResponse = getItem(clickPosition);
        //Setting the time in prefernce
        MApplication.setString(participateActivity, Constants.DATE_UPDATED, MApplication.getTimeDifference(campaignPollResponse.getUpdatedAt()));
        //Moving from one activity to another activity
        Intent a = new Intent(participateActivity, FullImageView.class);
        //Passing the value from one activity to another
        a.putExtra(Constants.QUESTION1, pollImageMyPoll);
        //Starting the activity
        participateActivity.startActivity(a);
    }

    /**
     * Comment click ACTION
     * @param clickPosition-position
     */
    private void commentClickAction(int clickPosition) {
        //response from the server
        campaignPollResponse = getItem(clickPosition);
        String id = campaignPollResponse.getId();
        //Moving from one activity to another activity
        Intent details = new Intent(participateActivity, CampaignPollComments.class);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_TYPE, campaignPollResponse.getPollType());
        //Passing the value from one activity to another
        details.putExtra(Constants.COMMENTS_COUNT_POSITION, clickPosition);
        //Starting the activity
        participateActivity.startActivity(details);
    }


    @Override
    public View getView(final int position, View mView, ViewGroup parent) {
        mParticipateView=mView;
        //Geeting the json response based on the position
        campaignPollResponse = getItem(position);
        //poll type from the response
        idRefrenceView = campaignPollResponse.getPollType();
        //Poll question from the response
        pollQuestion = campaignPollResponse.getPollQuestion();
        //image from the response
        pollImage1 = campaignPollResponse.getPollquestionImage();
        //Image question from the response
        pollImage2 = campaignPollResponse.getPollquestionImage1();
        //poll answer form the response
        if(campaignPollResponse.getUserPollsAns().size()>0)
        {
            pollAnswer1 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();
            //poll answer form the response
            pollAnswer2 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();
            //poll answer form the response
            pollAnswer3 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3();
            //poll answer form the response
            pollAnswer4 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4();
            //youtube usrl from tthe response
            youTubeUrl = campaignPollResponse.getYouTubeUrl();
        }
        //Setting in prefernce
        MApplication.setString(participateActivity, Constants.YOUTUBE_URL, youTubeUrl);
        //If the value matches the the layout one view is binded.
        if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_ONE) {
            /* create a new view of my layout and inflate it in the row */
            mParticipateView = LayoutInflater.from(participateActivity).inflate(R.layout.activity_publicpoll_participate_firstview, null);
            //view holder class
            holder1 = new ViewHolderLayoutOne();
            holder1.radioYesOrNOCampaignPoll = (RadioGroup) mParticipateView.findViewById(R.id.YesOrNO);
            holder1.radioYesCampaignPoll = (RadioButton) mParticipateView.findViewById(R.id.radioYes);
            holder1.radioNoCampaignPoll = (RadioButton) mParticipateView.findViewById(R.id.radioNo);
            holder1.likeUnlikeYesOrNoCampaignPoll = (CheckBox) mParticipateView.findViewById(R.id.unLikeDislike);
            holder1.yesOrNoLikeCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtLike);
            holder1.imageQuestion1YesOrNoCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.choose);
            holder1.imageQuestion2YesOrNoCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.ChooseAdditional);
            holder1.singleOptionYesOrNoCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.singleOption);
            holder1.txtQuestionYesOrNoCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtStatus);
            holder1.txtCommentYesOrNoCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtCommentsCounts);
            holder1.txtYesOrNoCounts = (TextView) mParticipateView.findViewById(R.id.txtParticcipation);
            //Binding the data into the views in android
            validateViewLayoutOne(holder1, position);
            //If the value matches the the layout one view is binded.
        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_TWO) {
            /* create a new view of my layout and inflate it in the row */
            mParticipateView = LayoutInflater.from(participateActivity).inflate(R.layout.activity_publicpoll_participate_secondview, null);
            //view holder class
            holder2 = new ViewHolderLayoutTwo();
            holder2.radioGroupOptionsCampaignPoll = mParticipateView.findViewById(R.id.participate_options);
            holder2.radioOptions1MultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.option1);
            holder2.radioOptions2MultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.option2);
            holder2.radioOptions3MultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.option3);
            holder2.radioOptions4MultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.option4);
            holder2.likeUnlikeMultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.unLikeDislike);
            holder2.txtLikeMulipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.txtLike2);
            holder2.txtQuestionMultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.txtStatus);
            holder2.txtCommentsMultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.txtCommentsCounts);
            holder2.txtCountsMultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.txtParticcipation);
            holder2.imageQuestion1MultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.choose);
            holder2.imageQuestion2MultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.ChooseAdditional);
            holder2.singleOptionMultipleOptionsCampaignPoll = mParticipateView.findViewById(R.id.singleOption);
            //Binding the data into the views in android
            validateViewLayoutTwo(holder2, position);
        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_THREE) {
             /* create a new view of my layout and inflate it in the row */
            mParticipateView = LayoutInflater.from(participateActivity).inflate(R.layout.activity_publicpoll_participate_thirdview, null);
            //view holder class
            holder3 = new ViewHolderLayoutThree();
            holder3.radioOptions1PhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.option1);
            holder3.radioOptions2PhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.option2);
            holder3.radioOptions3PhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.option3);
            holder3.radioOptions4PhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.option4);
            holder3.likeUnlikePhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.unLikeDislike);
            holder3.txtLikePhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.txtLike3);
            holder3.txtQuestionPhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.txtStatus);
            holder3.imageAnswer1PhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.answer1);
            holder3.imageAnswer2PhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.answer2);
            holder3.imageAnswer3PhotoComparisonCampaignPoll = mParticipateView.findViewById(R.id.answer3);
            holder3.imageAnswer4PhotoComparisonCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.answer4);
            holder3.relativeAnswer3PhotoComparisonCampaignPoll = (RelativeLayout) mParticipateView.findViewById(R.id.ThirdOptionOption);
            holder3.relativeAnswer4PhotoComparisonCampaignPoll = (RelativeLayout) mParticipateView.findViewById(R.id.FourthOptionOption);
            holder3.likeUnlikePhotoComparisonCampaignPoll = (CheckBox) mParticipateView.findViewById(R.id.unLikeDislike);
            holder3.txtCommentsPhotoComparisonCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtCommentsCounts);
            holder3.txtCountPhotoComparisonCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtParticcipation);
            holder3.imageQuestion1PhotoComparisonCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.choose);
            holder3.imageQuestion2PhotoComparisonCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.ChooseAdditional);
            holder3.singleOptionPhotoComparisonCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.singleOption);
            //Binding the data into the views in android
            validateViewLayoutThree(holder3, position);
        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_FOUR) {
            /* create a new view of my layout and inflate it in the row */
            mParticipateView = LayoutInflater.from(participateActivity).inflate(R.layout.activity_publicpoll_participate_fourthview, null);
            //view holder class
            holder4 = new ViewHolderLayoutFour();
            holder4 = new ViewHolderLayoutFour();
            holder4.radioGroupOptionsYouTubeUrlCampaignPoll = (RadioGroup) mParticipateView.findViewById(R.id.participate_options);
            holder4.radioOptions1YouTubeUrlCampaignPoll = (RadioButton) mParticipateView.findViewById(R.id.option1);
            holder4.radioOptions2YouTubeUrlCampaignPoll = (RadioButton) mParticipateView.findViewById(R.id.option2);
            holder4.radioOptions3YouTubeUrlCampaignPoll = (RadioButton) mParticipateView.findViewById(R.id.option3);
            holder4.radioOptions4YouTubeUrlCampaignPoll = (RadioButton) mParticipateView.findViewById(R.id.option4);
            holder4.likeUnlikeYouTubeUrlCampaignPoll = (CheckBox) mParticipateView.findViewById(R.id.unLikeDislike);
            holder4.txtLikeYouTubeUrlCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtLike4);
            holder4.txtQuestionYouTubeUrlCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtStatus);
            holder4.txtCommentsYouTubeUrlCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtCommentsCounts);
            holder4.txtVideoUrlCountYouTubeUrlCampaignPoll = (TextView) mParticipateView.findViewById(R.id.txtParticcipation);
            holder4.imageQuestion1YouTubeUrlCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.choose);
            holder4.imageQuestion2YouTubeUrlCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.ChooseAdditional);
            holder4.singleOptionYouTubeUrlCampaignPoll = (SimpleDraweeView) mParticipateView.findViewById(R.id.singleOption);
            //Binding the data into the views in android
            validateViewLayoutFour(holder4, position);

        }
        //Interface definition for a callback to be invoked when a view is clicked.
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Campaign Id
                String campaignId = MApplication.getString(participateActivity, Constants.CAMPAIGN_ID);
                //base 64 campaig id
                String base64CampaignId = MApplication.convertByteCode(campaignId);
                //Url for sharing
                String shareUrl = Constants.SHARE_CAMPAIGN_BASE_URL.concat(base64CampaignId);
                //Sharing the url in gmail
               // MApplication.shareGmail(participateActivity, shareUrl, MApplication.getString(participateActivity, Constants.PROFILE_USER_NAME));

                MApplication.getSharedURL(shareUrl,participateActivity);
            }
        });



        return mParticipateView;
    }

    /**
     * Clicking the participate icon.
     *
     * @param clickPosition
     */
    private void clickAction(int clickPosition) {
        preferenceCampaignPollIdAnswer = MApplication.loadStringArray(participateActivity, camapignPollIdAnwserCheck, Constants.CAMPAIGN_POLL_ID_ANSWER_ARRAY, Constants.CAMPAIGN_POLL_ID_ANSWER_SIZE);

        //Response from the server when the particular cell is clicked.Based on the click position data is retrived from the response.
        campaignPollResponse = getItem(clickPosition);
        //Polltype from the response
        pollType = campaignPollResponse.getPollType();
        //Poll id from the response
        String pollId = campaignPollResponse.getId();
        if(!preferenceCampaignPollIdAnswer.isEmpty()) {
            if (preferenceCampaignPollIdAnswer.contains(campaignPollResponse.getId())) {
                //Load the participate count from  the saved prefernce
                preferenceCampaignPollParticipateCount = MApplication.loadArray(participateActivity, myParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
                //Setting the time in prefernce
                MApplication.setString(participateActivity, Constants.DATE_UPDATED, MApplication.getTimeDifference(campaignPollResponse.getUpdatedAt()));
                //It can be used with startActivity to launch an Activity.
                Intent a = new Intent(participateActivity, CampignPollReview.class);
                //Pushing the values from one activity to another activity
                a.putExtra(Constants.POLL_TYPE, pollType);
                a.putExtra(Constants.POLL_ID, pollId);
                a.putExtra(Constants.TYPE, Constants.CAMPAIGN);
                a.putExtra(Constants.ARRAY_POSITION, clickPosition);
                a.putExtra(Constants.PARTICIPATE_COUNT, String.valueOf(preferenceCampaignPollParticipateCount.get(clickPosition)));
                //Starting the activity
                participateActivity.startActivity(a);
            } else {
                Toast.makeText(participateActivity, "You must attend the poll to view the results",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(participateActivity, "You must attend the poll to view the results",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Layout view 1
     *
     * @param holder            -view holders
     * @param position-position is used when reusing the views
     */
    private void validateViewLayoutOne(final ViewHolderLayoutOne holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceCampaignPollLikeCount = MApplication.loadArray(participateActivity, campaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceCampaignPollCommentsCount = MApplication.loadArray(participateActivity, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceCampaignPollLikeUser = MApplication.loadArray(participateActivity, campaignPollLikesUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceCampaignPollIdAnswer = MApplication.loadStringArray(participateActivity, camapignPollIdAnwserCheck, Constants.CAMPAIGN_POLL_ID_ANSWER_ARRAY, Constants.CAMPAIGN_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceCampaignPollIdAnswerSelected = MApplication.loadStringArray(participateActivity, campaignPollIdAnwser, Constants.CAMPAIGN_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.CAMPAIGN_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferenceCampaignPollParticipateCount = MApplication.loadArray(participateActivity, myParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceCampaignPollIdAnswer.contains(campaignPollResponse.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceCampaignPollIdAnswer.indexOf(campaignPollResponse.getId());
            //Selected answer from the position
            String value = preferenceCampaignPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (value.equals("Yes")) {
                //Setting the radio options true
                holder.radioYesCampaignPoll.setChecked(true);
                //Setting the text color black
                holder.radioYesCampaignPoll.setTextColor(Color.BLACK);
            } else {
                //Setting the text color black
                holder.radioNoCampaignPoll.setTextColor(Color.BLACK);
                //Setting the radio options true
                holder.radioNoCampaignPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioNoCampaignPoll.setClickable(false);
            //Setting the clickable false
            holder.radioYesCampaignPoll.setClickable(false);
        }
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceCampaignPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYesOrNoCampaignPoll.setChecked(true);
        } else {
            holder.likeUnlikeYesOrNoCampaignPoll.setChecked(false);
        }
        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            holder.imageQuestion1YesOrNoCampaignPoll.setVisibility(View.GONE);
            holder.imageQuestion2YesOrNoCampaignPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.imageQuestion1YesOrNoCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion2YesOrNoCampaignPoll.setImageURI(Uri.parse(pollImage2));
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                holder.singleOptionYesOrNoCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionYesOrNoCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion1YesOrNoCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoCampaignPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.singleOptionYesOrNoCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionYesOrNoCampaignPoll.setImageURI(Uri.parse(pollImage2));
                holder.imageQuestion1YesOrNoCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoCampaignPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1YesOrNoCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoCampaignPoll.setVisibility(View.GONE);
            }
        }
        //setting the poll question in text view
        holder.txtQuestionYesOrNoCampaignPoll.setText(pollQuestion);
        //Setting the like count based on the position in the text view
        holder.yesOrNoLikeCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(position)));
        //Setting the answer1 in ratio options
        holder.radioYesCampaignPoll.setText(pollAnswer1);
        //Setting the answer12in ratio options
        holder.radioNoCampaignPoll.setText(pollAnswer2);
        //Getting the COMMENTS name from the prefence and binding the data into the text view
        holder.txtCommentYesOrNoCampaignPoll.setText(String.valueOf(prefrenceCampaignPollCommentsCount.get(position)));
        //Setting the like count based on the position in the text view
        holder.txtYesOrNoCounts.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(position)));
       holder1OnClickListner(holder,position);
    }

    private void holder1OnClickListner(final ViewHolderLayoutOne holder,final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtYesOrNoCounts.setOnClickListener(mCampaignPollAdapterParticipateCounts);
        holder.yesOrNoLikeCampaignPoll.setOnClickListener(mCampaignPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentYesOrNoCampaignPoll.setOnClickListener(mCampaignPollAdapterCommentClickAction);
        holder.imageQuestion1YesOrNoCampaignPoll.setOnClickListener(mCampaignPollAdapterQuestionImage1);
        holder.imageQuestion2YesOrNoCampaignPoll.setOnClickListener(mCampaignPollAdapterQuestionImage2);
        holder.singleOptionYesOrNoCampaignPoll.setOnClickListener(mCampaignPollAdapterSingleOption);
        holder.radioYesCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer selected id is set as 1
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();   //poll answer
                    optionSelection(clickPosition, holder1, null, null, null);  //Option selection request
                }
                return false;
            }
        });
        holder.radioNoCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "2";    //poll answer selected id is set as 1
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();   //poll answer
                    optionSelection(clickPosition, holder1, null, null, null);  //Option selection request
                }
                return false;
            }
        });

        // Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYesOrNoCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYesOrNoCampaignPoll.setChecked(true);
                    likesUnLikes(position, holder, null, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYesOrNoCampaignPoll.setChecked(false);
                    likesUnLikes(position, holder, null, null, null);
                }
            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder1.radioYesOrNOCampaignPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if (checkedId == R.id.radioYes) {//If the view id matches the below fuctinolity will take place
                    pollAnswerSelectedId = "1";    //poll answer selected id is set as 1
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();   //poll answer
                    optionSelection(clickPosition, holder1, null, null, null);  //Option selection request
                } else if (checkedId == R.id.radioNo) {
                    pollAnswerSelectedId = "2";    //poll answer selected id is set as 2
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2(); //poll answer
                    optionSelection(clickPosition, holder1, null, null, null);    //Option selection request
                }
            }
        });
    }

    /**
     * Layout view 2
     *
     * @param holder            -view holders
     * @param position-position is used when reusing the views
     */

    private void validateViewLayoutTwo(final ViewHolderLayoutTwo holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceCampaignPollLikeCount = MApplication.loadArray(participateActivity, campaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceCampaignPollCommentsCount = MApplication.loadArray(participateActivity, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceCampaignPollLikeUser = MApplication.loadArray(participateActivity, campaignPollLikesUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceCampaignPollIdAnswer = MApplication.loadStringArray(participateActivity, camapignPollIdAnwserCheck, Constants.CAMPAIGN_POLL_ID_ANSWER_ARRAY, Constants.CAMPAIGN_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceCampaignPollIdAnswerSelected = MApplication.loadStringArray(participateActivity, campaignPollIdAnwser, Constants.CAMPAIGN_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.CAMPAIGN_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferenceCampaignPollParticipateCount = MApplication.loadArray(participateActivity, myParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceCampaignPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeMultipleOptionsCampaignPoll.setChecked(true);
        } else {
            holder.likeUnlikeMultipleOptionsCampaignPoll.setChecked(false);
        }
        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            holder.imageQuestion1MultipleOptionsCampaignPoll.setVisibility(View.GONE);
            holder.imageQuestion2MultipleOptionsCampaignPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.imageQuestion1MultipleOptionsCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion2MultipleOptionsCampaignPoll.setImageURI(Uri.parse(pollImage2));
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                holder.singleOptionMultipleOptionsCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionMultipleOptionsCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion1MultipleOptionsCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsCampaignPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.singleOptionMultipleOptionsCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionMultipleOptionsCampaignPoll.setImageURI(Uri.parse(pollImage2));
                holder.imageQuestion1MultipleOptionsCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsCampaignPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1MultipleOptionsCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsCampaignPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.radioOptions1MultipleOptionsCampaignPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer1)) {
            holder.radioOptions1MultipleOptionsCampaignPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1MultipleOptionsCampaignPoll.setText(pollAnswer1);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            holder.radioOptions2MultipleOptionsCampaignPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions2MultipleOptionsCampaignPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2MultipleOptionsCampaignPoll.setText(pollAnswer2);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.radioOptions3MultipleOptionsCampaignPoll.setVisibility(View.GONE);
            holder.radioOptions4MultipleOptionsCampaignPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer3)) {
            holder.radioOptions3MultipleOptionsCampaignPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3MultipleOptionsCampaignPoll.setText(pollAnswer3);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.radioOptions4MultipleOptionsCampaignPoll.setVisibility(View.GONE);
                //If the poll answer option is not empty then the view will invisible
            } else if (!("").equals(pollAnswer4)) {
                holder.radioOptions4MultipleOptionsCampaignPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4MultipleOptionsCampaignPoll.setText(pollAnswer4);
            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceCampaignPollIdAnswer.contains(campaignPollResponse.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceCampaignPollIdAnswer.indexOf(campaignPollResponse.getId());
            //Selected answer from the position
            String value = preferenceCampaignPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (holder.radioOptions1MultipleOptionsCampaignPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions1MultipleOptionsCampaignPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions1MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions2MultipleOptionsCampaignPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions2MultipleOptionsCampaignPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions2MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions3MultipleOptionsCampaignPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions3MultipleOptionsCampaignPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions3MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions4MultipleOptionsCampaignPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions4MultipleOptionsCampaignPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions4MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
            }
            //Setting the clickable false
            holder.radioOptions1MultipleOptionsCampaignPoll.setClickable(false);
            holder.radioOptions2MultipleOptionsCampaignPoll.setClickable(false);
            holder.radioOptions3MultipleOptionsCampaignPoll.setClickable(false);
            holder.radioOptions4MultipleOptionsCampaignPoll.setClickable(false);
        }
        //setting the poll question in text view
        holder.txtCommentsMultipleOptionsCampaignPoll.setText(String.valueOf(prefrenceCampaignPollCommentsCount.get(position)));
        //Setting the like count based on the position in the text view
        holder.txtLikeMulipleOptionsCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(position)));
        //Setting the poll question in text view
        holder.txtQuestionMultipleOptionsCampaignPoll.setText(pollQuestion);
        //Setting the text participate count from the array list based on the position
        holder.txtCountsMultipleOptionsCampaignPoll.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(position)));
        holder2OnClickLisner(holder,position);
    }

    private void holder2OnClickLisner(final ViewHolderLayoutTwo holder,final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountsMultipleOptionsCampaignPoll.setOnClickListener(mCampaignPollAdapterParticipateCounts);
        holder.txtLikeMulipleOptionsCampaignPoll.setOnClickListener(mCampaignPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentsMultipleOptionsCampaignPoll.setOnClickListener(mCampaignPollAdapterCommentClickAction);
        holder.imageQuestion1MultipleOptionsCampaignPoll.setOnClickListener(mCampaignPollAdapterQuestionImage1);
        holder.imageQuestion2MultipleOptionsCampaignPoll.setOnClickListener(mCampaignPollAdapterQuestionImage2);
        holder.singleOptionMultipleOptionsCampaignPoll.setOnClickListener(mCampaignPollAdapterSingleOption);
        holder.radioOptions1MultipleOptionsCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer selected id is set as 1
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();   //poll answer
                    optionSelection(clickPosition, null, holder, null, null);  //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions2MultipleOptionsCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "2";    //poll answer selected id is set as 1
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();   //poll answer
                    optionSelection(clickPosition, null, holder, null, null);  //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions3MultipleOptionsCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "3";    //poll answer selected id is set as 1
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3();   //poll answer
                    optionSelection(clickPosition, null, holder, null, null);  //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions4MultipleOptionsCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "3";    //poll answer selected id is set as 1
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4();   //poll answer
                    optionSelection(clickPosition, null, holder, null, null);  //Option selection request
                }
                return false;
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeMultipleOptionsCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeMultipleOptionsCampaignPoll.setChecked(true);
                    likesUnLikes(position, null, holder, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeMultipleOptionsCampaignPoll.setChecked(false);
                    likesUnLikes(position, null, holder, null, null);
                }

            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsCampaignPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position;
                campaignPollResponse = getItem(clickPosition);
                optionClickAction(checkedId);
                optionSelection(clickPosition, null, holder, null, null);
            }
        });

    }

    /**
     * The OnCheckedChangeListener will be called when any widget like button, text, image etc is either clicked or touched or
     * focused upon by the user.
     *
     * @param checkedId
     */
    private void optionClickAction(int checkedId) {
        switch (checkedId) {
            case R.id.option1:
                //Setting the value for the variable
                pollAnswerSelectedId = "1";
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                break;
            case R.id.option2:
                //Setting the value for the variable
                pollAnswerSelectedId = "2";
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                break;
            case R.id.option3:
                //Setting the value for the variable
                pollAnswerSelectedId = "3";
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                break;
            case R.id.option4:
                //Setting the value for the variable
                pollAnswerSelectedId = "4";
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4();
                break;
            default:
                break;
        }

    }


    /**
     * This method is called when the user click the like count
     *
     * @param myPollResponse
     */

    private void likeClickAction(UserPollResponseModel.Results.Data myPollResponse) {
        //Poll id from the response
        String id = myPollResponse.getId();
        // It can be used with startActivity to launch an Activity
        Intent details = new Intent(participateActivity, PollLikes.class);
        //Passing the id form one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Start Activity
        participateActivity.startActivity(details);
    }

    /**
     * View hoder3
     *
     * @param holder            -view holder class
     * @param position-position
     */
    private void validateViewLayoutThree(final ViewHolderLayoutThree holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceCampaignPollLikeCount = MApplication.loadArray(participateActivity, campaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceCampaignPollCommentsCount = MApplication.loadArray(participateActivity, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceCampaignPollLikeUser = MApplication.loadArray(participateActivity, campaignPollLikesUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceCampaignPollIdAnswer = MApplication.loadStringArray(participateActivity, camapignPollIdAnwserCheck, Constants.CAMPAIGN_POLL_ID_ANSWER_ARRAY, Constants.CAMPAIGN_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceCampaignPollIdAnswerSelected = MApplication.loadStringArray(participateActivity, campaignPollIdAnwser, Constants.CAMPAIGN_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.CAMPAIGN_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferenceCampaignPollParticipateCount = MApplication.loadArray(participateActivity, myParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceCampaignPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikePhotoComparisonCampaignPoll.setChecked(true);
        } else {
            holder.likeUnlikePhotoComparisonCampaignPoll.setChecked(false);
        }
        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            holder.imageQuestion1PhotoComparisonCampaignPoll.setVisibility(View.GONE);
            holder.imageQuestion2PhotoComparisonCampaignPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.imageQuestion1PhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion2PhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollImage2));
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                holder.singleOptionPhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionPhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion1PhotoComparisonCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonCampaignPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.singleOptionPhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionPhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollImage2));
                holder.imageQuestion1PhotoComparisonCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonCampaignPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1PhotoComparisonCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonCampaignPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.imageAnswer1PhotoComparisonCampaignPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer1PhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
            holder.imageAnswer1PhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollAnswer1));
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            holder.imageAnswer1PhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollAnswer1));
            holder.imageAnswer2PhotoComparisonCampaignPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer2PhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
            holder.imageAnswer2PhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollAnswer2));
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.imageAnswer3PhotoComparisonCampaignPoll.setVisibility(View.GONE);
            holder.imageAnswer4PhotoComparisonCampaignPoll.setVisibility(View.GONE);
            holder.relativeAnswer3PhotoComparisonCampaignPoll.setVisibility(View.GONE);
            holder.relativeAnswer4PhotoComparisonCampaignPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer3)) {
            holder.imageAnswer3PhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
            holder.relativeAnswer3PhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
            holder.imageAnswer3PhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollAnswer3));
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.imageAnswer4PhotoComparisonCampaignPoll.setVisibility(View.INVISIBLE);
                holder.relativeAnswer4PhotoComparisonCampaignPoll.setVisibility(View.GONE);
                //If the poll answer option is not empty then the view will invisible
            } else if (!("").equals(pollAnswer4)) {
                holder.imageAnswer4PhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
                holder.imageAnswer4PhotoComparisonCampaignPoll.setImageURI(Uri.parse(pollAnswer4));
                holder.relativeAnswer4PhotoComparisonCampaignPoll.setVisibility(View.VISIBLE);
            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceCampaignPollIdAnswer.contains(campaignPollResponse.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceCampaignPollIdAnswer.indexOf(campaignPollResponse.getId());
            //Selected answer from the position
            String value = preferenceCampaignPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1().equals(value)) {
                holder.radioOptions1PhotoComparisonCampaignPoll.setChecked(true);
            } else if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2().equals(value)) {
                holder.radioOptions2PhotoComparisonCampaignPoll.setChecked(true);
            } else if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3().equals(value)) {
                holder.radioOptions3PhotoComparisonCampaignPoll.setChecked(true);
            } else if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4().equals(value)) {
                holder.radioOptions4PhotoComparisonCampaignPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1PhotoComparisonCampaignPoll.setClickable(false);
            holder.radioOptions2PhotoComparisonCampaignPoll.setClickable(false);
            holder.radioOptions3PhotoComparisonCampaignPoll.setClickable(false);
            holder.radioOptions4PhotoComparisonCampaignPoll.setClickable(false);
        }
        //Setting the poll question intext view
        holder.txtQuestionPhotoComparisonCampaignPoll.setText(pollQuestion);
        //Setting the prefernce like count
        holder.txtLikePhotoComparisonCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(position)));
        //Setting the participate count from the arraylist based on the position
        holder.txtCountPhotoComparisonCampaignPoll.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(position)));
        //Setting the COMMENTS from the arraylist  based on the position
        holder.txtCommentsPhotoComparisonCampaignPoll.setText(String.valueOf(prefrenceCampaignPollCommentsCount.get(position)));
        holderView3OnClickLisner(holder,position);
    }

    private void holderView3OnClickLisner(final ViewHolderLayoutThree holder,final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountPhotoComparisonCampaignPoll.setOnClickListener(mCampaignPollAdapterParticipateCounts);
        holder.txtLikePhotoComparisonCampaignPoll.setOnClickListener(mCampaignPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentsPhotoComparisonCampaignPoll.setOnClickListener(mCampaignPollAdapterCommentClickAction);
        holder.imageQuestion1PhotoComparisonCampaignPoll.setOnClickListener(mCampaignPollAdapterQuestionImage1);
        holder.imageQuestion2PhotoComparisonCampaignPoll.setOnClickListener(mCampaignPollAdapterQuestionImage2);
        holder.singleOptionPhotoComparisonCampaignPoll.setOnClickListener(mCampaignPollAdapterSingleOption);
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer1PhotoComparisonCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                campaignPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer1 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer1);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer2PhotoComparisonCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                campaignPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer2 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer2);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer3PhotoComparisonCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                campaignPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer3 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer3);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer4PhotoComparisonCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                campaignPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer4 = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4();
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer4);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikePhotoComparisonCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikePhotoComparisonCampaignPoll.setChecked(true);
                    likesUnLikes(position, null, null, holder, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikePhotoComparisonCampaignPoll.setChecked(false);
                    likesUnLikes(position, null, null, holder, null);

                }

            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions1PhotoComparisonCampaignPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int clickPosition = position;  //Click position
                campaignPollResponse = getItem(clickPosition);   //Response from the server based on the position
                pollAnswerSelectedId = "1";    //poll answer url
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions2PhotoComparisonCampaignPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int clickPosition = position;  //Click position
                campaignPollResponse = getItem(clickPosition); //Response from the server based on the position
                pollAnswerSelectedId = "2";  //poll answer url
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions3PhotoComparisonCampaignPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                campaignPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswerSelectedId = "3";
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions4PhotoComparisonCampaignPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                campaignPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswerSelectedId = "4";
                pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        holder.radioOptions1PhotoComparisonCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                    optionSelection(clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        holder.radioOptions1PhotoComparisonCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                    optionSelection(clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        holder.radioOptions2PhotoComparisonCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                    optionSelection(clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        holder.radioOptions3PhotoComparisonCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                    optionSelection(clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        holder.radioOptions4PhotoComparisonCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4();
                    optionSelection(clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
    }

    private void validateViewLayoutFour(final ViewHolderLayoutFour holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceCampaignPollLikeCount = MApplication.loadArray(participateActivity, campaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceCampaignPollCommentsCount = MApplication.loadArray(participateActivity, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceCampaignPollLikeUser = MApplication.loadArray(participateActivity, campaignPollLikesUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceCampaignPollIdAnswer = MApplication.loadStringArray(participateActivity, camapignPollIdAnwserCheck, Constants.CAMPAIGN_POLL_ID_ANSWER_ARRAY, Constants.CAMPAIGN_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceCampaignPollIdAnswerSelected = MApplication.loadStringArray(participateActivity, campaignPollIdAnwser, Constants.CAMPAIGN_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.CAMPAIGN_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferenceCampaignPollParticipateCount = MApplication.loadArray(participateActivity, myParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceCampaignPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYouTubeUrlCampaignPoll.setChecked(true);
        } else {
            holder.likeUnlikeYouTubeUrlCampaignPoll.setChecked(false);
        }
        //If the question image 1 and Question 2 is empty visiblity will be gone
        if (("").equals(pollImage1)&& ("").equals(pollImage2)) {
            holder.imageQuestion1YouTubeUrlCampaignPoll.setVisibility(View.GONE);
            holder.imageQuestion2YouTubeUrlCampaignPoll.setVisibility(View.GONE);
        } else {
            //If the question image 1 and Question 2 is not empty visiblity will be visible
            if (!("").equals(pollImage1)&& !("").equals(pollImage2)) {
                holder.imageQuestion1YouTubeUrlCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion2YouTubeUrlCampaignPoll.setImageURI(Uri.parse(pollImage2));
                //If the question image 1 is  not empty visiblity will be visible
            } else if (!("").equals(pollImage1)&& ("").equals(pollImage2)) {
                holder.singleOptionYouTubeUrlCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionYouTubeUrlCampaignPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion1YouTubeUrlCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlCampaignPoll.setVisibility(View.GONE);
                //If the question image 2 is not empty visiblity will be visible
            } else if (("").equals(pollImage1)&& !("").equals(pollImage2)) {
                holder.singleOptionYouTubeUrlCampaignPoll.setVisibility(View.VISIBLE);
                holder.singleOptionYouTubeUrlCampaignPoll.setImageURI(Uri.parse(pollImage2));
                holder.imageQuestion1YouTubeUrlCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlCampaignPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1YouTubeUrlCampaignPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlCampaignPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.radioOptions1YouTubeUrlCampaignPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions1YouTubeUrlCampaignPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1YouTubeUrlCampaignPoll.setText(pollAnswer1);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            holder.radioOptions2YouTubeUrlCampaignPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions2YouTubeUrlCampaignPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2YouTubeUrlCampaignPoll.setText(pollAnswer2);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.radioOptions3YouTubeUrlCampaignPoll.setVisibility(View.GONE);
            holder.radioOptions4YouTubeUrlCampaignPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer3)) {
            holder.radioOptions3YouTubeUrlCampaignPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3YouTubeUrlCampaignPoll.setText(pollAnswer3);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.radioOptions4YouTubeUrlCampaignPoll.setVisibility(View.GONE);
            } else if (!("").equals(pollAnswer4)){
                holder.radioOptions4YouTubeUrlCampaignPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4YouTubeUrlCampaignPoll.setText(pollAnswer4);

            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceCampaignPollIdAnswer.contains(campaignPollResponse.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceCampaignPollIdAnswer.indexOf(campaignPollResponse.getId());
            //Selected answer from the position
            String value = preferenceCampaignPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1().equals(value)) {
                holder.radioOptions1YouTubeUrlCampaignPoll.setChecked(true);
            } else if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2().equals(value)) {
                holder.radioOptions2YouTubeUrlCampaignPoll.setChecked(true);
            } else if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3().equals(value)) {
                holder.radioOptions3YouTubeUrlCampaignPoll.setChecked(true);
            } else if (campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4().equals(value)) {
                holder.radioOptions4YouTubeUrlCampaignPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1YouTubeUrlCampaignPoll.setClickable(false);
            holder.radioOptions2YouTubeUrlCampaignPoll.setClickable(false);
            holder.radioOptions3YouTubeUrlCampaignPoll.setClickable(false);
            holder.radioOptions4YouTubeUrlCampaignPoll.setClickable(false);
        }
        //setting the participate count url
        holder.txtVideoUrlCountYouTubeUrlCampaignPoll.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(position)));
        //Setting the like count in text view
        holder.txtLikeYouTubeUrlCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(position)));
        //Setting the poll question intext view
        holder.txtQuestionYouTubeUrlCampaignPoll.setText(pollQuestion);
        //Getting the COMMENTS name from the prefence and binding the data into the text view
        holder.txtCommentsYouTubeUrlCampaignPoll.setText(String.valueOf(prefrenceCampaignPollCommentsCount.get(position)));

     holder4OnClickListner(holder,position);
    }

    private void holder4OnClickListner(final ViewHolderLayoutFour holder,final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYouTubeUrlCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYouTubeUrlCampaignPoll.setChecked(true);
                    likesUnLikes(position, null, null, null, holder);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYouTubeUrlCampaignPoll.setChecked(false);
                    likesUnLikes(position, null, null, null, holder);
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtVideoUrlCountYouTubeUrlCampaignPoll.setOnClickListener(mCampaignPollAdapterParticipateCounts);
        holder.txtLikeYouTubeUrlCampaignPoll.setOnClickListener(mCampaignPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentsYouTubeUrlCampaignPoll.setOnClickListener(mCampaignPollAdapterCommentClickAction);
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsYouTubeUrlCampaignPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Response
                optionClickAction(checkedId);
                optionSelection(clickPosition, null, null, null, holder);
            }
        });
        holder.radioOptions1YouTubeUrlCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                    optionSelection(clickPosition, null, null, null, holder);
                }
                return false;
            }
        });
        holder.radioOptions2YouTubeUrlCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                    optionSelection(clickPosition, null, null, null, holder);
                }
                return false;
            }
        });
        holder.radioOptions3YouTubeUrlCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                    optionSelection(clickPosition, null, null, null, holder);
                }
                return false;
            }
        });
        holder.radioOptions4YouTubeUrlCampaignPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;//Click position
                campaignPollResponse = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";    //poll answer url
                    pollAnswer = campaignPollResponse.getUserPollsAns().get(0).getPollAnswer4();
                    optionSelection(clickPosition, null, null, null, holder);
                }
                return false;
            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.singleOptionYouTubeUrlCampaignPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click ACTION
                int clickPosition = position;
                //Response
                campaignPollResponse = getItem(clickPosition);
                //Getting the url from the response
                youTubeUrl = campaignPollResponse.getYouTubeUrl();
                //Setting in prefernce
                MApplication.setString(participateActivity, Constants.YOUTUBE_URL, youTubeUrl);
                //If net is connected video is played
                //else toast message
                if (MApplication.isNetConnected((Activity) participateActivity)) {
                    Intent a = new Intent(participateActivity, VideoLandscapeActivity.class);
                    participateActivity.startActivity(a);
                } else {
                    Toast.makeText(participateActivity, participateActivity.getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method is used for like and unlike the poll
     * @param clickPosition -position
     * @param holder1-holding views
     * @param holder2-holding views
     * @param holder3-holding views
     * @param holder4-holding views
     */
    private void likesUnLikes(final int clickPosition, final ViewHolderLayoutOne holder1, final ViewHolderLayoutTwo holder2, final ViewHolderLayoutThree holder3, final ViewHolderLayoutFour holder4) {
        //Response from the server
        campaignPollResponse = getItem(clickPosition);
        //Getting the id
        String pollId = campaignPollResponse.getId();
        MApplication.materialdesignDialogStart(participateActivity);

        LikesAndUnLikeRestClient.getInstance().postCampaignPollLikes(new String("poll_likes"), new String(userId), new String(pollId), new String(String.valueOf(mlikes))
                , new Callback<LikeUnLikeResposneModel>() {
            @Override
            public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                //If the value from the response is 1 then the user has successfully liked the poll
                if (("1").equals(likesUnlike.getResults())) {
                    //Changing the value in array list in a particular position
                    preferenceCampaignPollLikeUser.set(clickPosition, Integer.valueOf(1));
                    //Saving it in array
                    MApplication.saveArray(participateActivity, preferenceCampaignPollLikeUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
                } else {
                    //Changing the value in array list in a particular position
                    preferenceCampaignPollLikeUser.set(clickPosition, Integer.valueOf(0));
                    //Saving it in array
                    MApplication.saveArray(participateActivity, preferenceCampaignPollLikeUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
                }
                //Toast message will display
                Toast.makeText(participateActivity, likesUnlike.getMsg(),
                        Toast.LENGTH_SHORT).show();
                //Changing the value in array list in a particular position
                prefrenceCampaignPollLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                //Saving the array in prefernce
                MApplication.saveArray(participateActivity, prefrenceCampaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
                //If holder1 is not null
                if (holder1 != null) {
                    //Changing the value in textview
                    holder1.yesOrNoLikeCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(clickPosition)));
                    //If holder2 is not null
                } else if (holder2 != null) {
                    //Changing the value in textview
                    holder2.txtLikeMulipleOptionsCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(clickPosition)));
                    //If holder3 is not null
                } else if (holder3 != null) {
                    //Changing the value in textview
                    holder3.txtLikePhotoComparisonCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(clickPosition)));
                    //If holder4 is not null
                } else if (holder4 != null) {
                    //Changing the value in textview
                    holder4.txtLikeYouTubeUrlCampaignPoll.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(clickPosition)));
                }
                MApplication.materialdesignDialogStop();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, participateActivity);
                MApplication.materialdesignDialogStop();
            }
        });
    }

    /**
     * Selecting the poll answer request and response
     * @param clickPosition-position
     * @param holder1-view holder
     * @param holder2-view holder
     * @param holder3-view holder
     * @param holder4-view holder
     */
    private void optionSelection(final int clickPosition, final ViewHolderLayoutOne holder1, final ViewHolderLayoutTwo holder2, final ViewHolderLayoutThree holder3, final ViewHolderLayoutFour holder4) {
        //Response fromt server  is retrived based on the position
        campaignPollResponse = getItem(clickPosition);
        //Getting the id from the response
        String pollId = campaignPollResponse.getId();
        //Getting the poll answer id from the response
        String pollAnswerId = campaignPollResponse.getUserPollsAns().get(0).getId();
        PollParticipateRestClient.getInstance().postParticipateApi(new String("polls_participate"), new String(userId), new String(pollId), new String(pollAnswerId), new String(pollAnswer), new String(pollAnswerSelectedId), new String("1")
                , new Callback<PollParticipateResponseModel>() {
            @Override
            public void success(PollParticipateResponseModel pollParticipateResponseModel, Response response) {
                //If the success is 1 the data is binded into the views
                if (("1").equals(pollParticipateResponseModel.getSuccess())) {
                    //Adding the value in arraylist
                    preferenceCampaignPollIdAnswer.add(pollParticipateResponseModel.getResults().getPollId());
                    //Adding the value in arraylist
                    preferenceCampaignPollIdAnswerSelected.add(pollParticipateResponseModel.getResults().getPollAnswer());
                    //Increamenting the participate count on success 1
                    int participateCount = Integer.parseInt(campaignPollResponse.getPollParticipateCount()) + 1;
                    //Setting participate count in the arraylist
                    preferenceCampaignPollParticipateCount.set(clickPosition, participateCount);
                    //Saving in prefernce
                    MApplication.saveArray(participateActivity, preferenceCampaignPollParticipateCount, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_ARRAY, Constants.CAMPIGN_POLL_PARTICIPATE_COUNT_SIZE);
                    //Saving in prefernce
                    MApplication.saveStringArray(participateActivity, preferenceCampaignPollIdAnswer, Constants.CAMPAIGN_POLL_ID_ANSWER_ARRAY, Constants.CAMPAIGN_POLL_ID_ANSWER_SIZE);
                    //Saving in prefernce
                    MApplication.saveStringArray(participateActivity, preferenceCampaignPollIdAnswerSelected, Constants.CAMPAIGN_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.CAMPAIGN_POLL_ID_SELECTED_SIZE);
                    //If poll type equals 1
                    if (("1").equals(campaignPollResponse.getPollType())) {
                        //Setting the radio options as false
                        holder1.radioYesCampaignPoll.setClickable(false);
                        //Setting the radio options as false
                        holder1.radioNoCampaignPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder1.radioYesCampaignPoll.isChecked()) {
                            holder1.radioYesCampaignPoll.setTextColor(Color.BLACK);
                            //Setting the value in participate count
                            holder1.txtYesOrNoCounts.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(clickPosition)));
                        } else if(holder1.radioNoCampaignPoll.isChecked()) {
                            holder1.radioNoCampaignPoll.setTextColor(Color.BLACK);
                            //Setting the value in participate count
                            holder1.txtYesOrNoCounts.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(clickPosition)));
                        }

                        //If poll type equals 2
                    } else if (("2").equals(campaignPollResponse.getPollType())) {
                        //Setting the clickable as false
                        holder2.radioOptions1MultipleOptionsCampaignPoll.setClickable(false);
                        holder2.radioOptions2MultipleOptionsCampaignPoll.setClickable(false);
                        holder2.radioOptions3MultipleOptionsCampaignPoll.setClickable(false);
                        holder2.radioOptions4MultipleOptionsCampaignPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder2.radioOptions1MultipleOptionsCampaignPoll.isChecked()) {
                            holder2.radioOptions1MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions2MultipleOptionsCampaignPoll.isChecked()) {
                            holder2.radioOptions2MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions3MultipleOptionsCampaignPoll.isChecked()) {
                            holder2.radioOptions3MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions4MultipleOptionsCampaignPoll.isChecked()) {
                            holder2.radioOptions4MultipleOptionsCampaignPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder2.txtCountsMultipleOptionsCampaignPoll.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(clickPosition)));
                    } else if (("3").equals(campaignPollResponse.getPollType())) {
                        //Setting the clickable as false
                        holder3.radioOptions1PhotoComparisonCampaignPoll.setClickable(false);
                        holder3.radioOptions2PhotoComparisonCampaignPoll.setClickable(false);
                        holder3.radioOptions3PhotoComparisonCampaignPoll.setClickable(false);
                        holder3.radioOptions4PhotoComparisonCampaignPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder3.radioOptions1PhotoComparisonCampaignPoll.isEnabled()) {
                            holder3.radioOptions1PhotoComparisonCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions2PhotoComparisonCampaignPoll.isEnabled()) {
                            holder3.radioOptions2PhotoComparisonCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions3PhotoComparisonCampaignPoll.isEnabled()) {
                            holder3.radioOptions3PhotoComparisonCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions4PhotoComparisonCampaignPoll.isEnabled()) {
                            holder3.radioOptions4PhotoComparisonCampaignPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder3.txtCountPhotoComparisonCampaignPoll.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(clickPosition)));
                    } else if (("4").equals(campaignPollResponse.getPollType())) {
                        holder4.radioOptions1YouTubeUrlCampaignPoll.setClickable(false);
                        holder4.radioOptions2YouTubeUrlCampaignPoll.setClickable(false);
                        holder4.radioOptions3YouTubeUrlCampaignPoll.setClickable(false);
                        holder4.radioOptions4YouTubeUrlCampaignPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder4.radioOptions1YouTubeUrlCampaignPoll.isChecked()) {
                            holder4.radioOptions1YouTubeUrlCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions2YouTubeUrlCampaignPoll.isChecked()) {
                            holder4.radioOptions2YouTubeUrlCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions3YouTubeUrlCampaignPoll.isChecked()) {
                            holder4.radioOptions3YouTubeUrlCampaignPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions4YouTubeUrlCampaignPoll.isChecked()) {
                            holder4.radioOptions4YouTubeUrlCampaignPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder4.txtVideoUrlCountYouTubeUrlCampaignPoll.setText(String.valueOf(preferenceCampaignPollParticipateCount.get(clickPosition)));
                    }
                    //Toast message will display
                    Toast.makeText(participateActivity, pollParticipateResponseModel.getMsg(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(participateActivity, pollParticipateResponseModel.getMsg(),
                            Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();

            }
            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, participateActivity);
            }

        });
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutOne {
        //text view
        private TextView yesOrNoLikeCampaignPoll;
        //Radio group
        private RadioGroup radioYesOrNOCampaignPoll;
        //Radio button
        private RadioButton radioYesCampaignPoll;
        //Radio button
        private RadioButton radioNoCampaignPoll;
        //Check box like unlike
        private CheckBox likeUnlikeYesOrNoCampaignPoll;
        //SimpleDraweeView view
        private SimpleDraweeView imageQuestion1YesOrNoCampaignPoll;
        //SimpleDraweeView view
        private SimpleDraweeView imageQuestion2YesOrNoCampaignPoll;
        //SimpleDraweeView view
        private SimpleDraweeView singleOptionYesOrNoCampaignPoll;
        //TextView question
        private TextView txtQuestionYesOrNoCampaignPoll;
        //Text view comment
        private TextView txtCommentYesOrNoCampaignPoll;
        //Textview particcipate count
        private TextView txtYesOrNoCounts;
    }
    /**
     * A ViewHolderLayoutTwo object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutTwo {
        //text view
        private TextView txtLikeMulipleOptionsCampaignPoll;
        //Radio group
        private RadioGroup radioGroupOptionsCampaignPoll;
        //Radio button
        private RadioButton radioOptions1MultipleOptionsCampaignPoll;
        //Radio button
        private RadioButton radioOptions2MultipleOptionsCampaignPoll;
        //Radio button
        private RadioButton radioOptions3MultipleOptionsCampaignPoll;
        //Radio button
        private RadioButton radioOptions4MultipleOptionsCampaignPoll;
        //Chek box
        private CheckBox likeUnlikeMultipleOptionsCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView imageQuestion1MultipleOptionsCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView imageQuestion2MultipleOptionsCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView singleOptionMultipleOptionsCampaignPoll;
        //Text view
        private TextView txtQuestionMultipleOptionsCampaignPoll;
        //Text view
        private TextView txtCommentsMultipleOptionsCampaignPoll;
        //Text view
        private TextView txtCountsMultipleOptionsCampaignPoll;

    }
    /**
     * A ViewHolderLayoutThree object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutThree {
        //Simple drawer view
        private SimpleDraweeView imageAnswer1PhotoComparisonCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView imageAnswer2PhotoComparisonCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView imageAnswer3PhotoComparisonCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView imageAnswer4PhotoComparisonCampaignPoll;
        //Relative layout
        private RelativeLayout relativeAnswer3PhotoComparisonCampaignPoll;
        //Relative layout
        private RelativeLayout relativeAnswer4PhotoComparisonCampaignPoll;
        //Checkbox
        private CheckBox likeUnlikePhotoComparisonCampaignPoll;
        //Text view
        private TextView txtLikePhotoComparisonCampaignPoll;
        //Radio button
        private RadioButton radioOptions1PhotoComparisonCampaignPoll;
        //Radio button
        private RadioButton radioOptions2PhotoComparisonCampaignPoll;
        //Radio button
        private RadioButton radioOptions3PhotoComparisonCampaignPoll;
        //Radio button
        private RadioButton radioOptions4PhotoComparisonCampaignPoll;
        //Imageview
        private SimpleDraweeView imageQuestion1PhotoComparisonCampaignPoll;
        //Imageview
        private SimpleDraweeView imageQuestion2PhotoComparisonCampaignPoll;
        //Imageview
        private SimpleDraweeView singleOptionPhotoComparisonCampaignPoll;
        // Textview
        private TextView txtQuestionPhotoComparisonCampaignPoll;
        // Textview
        private TextView txtCommentsPhotoComparisonCampaignPoll;
        // Textview
        private TextView txtCountPhotoComparisonCampaignPoll;

    }
    /**
     * A ViewHolderLayoutFour object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutFour {
        //Text view
        private TextView txtLikeYouTubeUrlCampaignPoll;
        //RadioGroup
        private RadioGroup radioGroupOptionsYouTubeUrlCampaignPoll;
        //RadioButton
        private RadioButton radioOptions1YouTubeUrlCampaignPoll;
        //RadioButton
        private RadioButton radioOptions2YouTubeUrlCampaignPoll;
        //RadioButton
        private RadioButton radioOptions3YouTubeUrlCampaignPoll;
        //RadioButton
        private RadioButton radioOptions4YouTubeUrlCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView imageQuestion1YouTubeUrlCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView imageQuestion2YouTubeUrlCampaignPoll;
        //Simple drawer view
        private SimpleDraweeView singleOptionYouTubeUrlCampaignPoll;
        //Checkbox
        private CheckBox likeUnlikeYouTubeUrlCampaignPoll;
        //TextView
        private TextView txtQuestionYouTubeUrlCampaignPoll;
        //TextView
        private TextView txtCommentsYouTubeUrlCampaignPoll;
        //TextView
        private TextView txtVideoUrlCountYouTubeUrlCampaignPoll;

    }


}


