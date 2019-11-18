package com.contus.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
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
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 10/6/2015.
 */
public class SearchAdapter extends ArrayAdapter<UserPollResponseModel.Results.Data> {
    //Layout
    private final int searchLayoutId;
    //user id
    private final String userId;
    //Activity
    private final Activity context;
    private final EndLessListView list;
    //Response from the server
    private UserPollResponseModel.Results.Data searchPollResponseModel;
    //Youtube url
    private String youTubeUrl;
    //Likes
    private int mlikes;
    //Holder class for view1
    private ViewHolderLayoutOne holderSearchView1;
    //Holder class for view2
    private ViewHolderLayoutTwo holderSearchView2;
    //Holder class for view3
    private ViewHolderLayoutThree holderSearchView3;
    //Holder class for view4
    private ViewHolderLayoutFour holderSearchView4;
    //View id
    private String idRefrenceView;
    //Poll question
    private String pollQuestion;
    //Poll answer1
    private String pollAnswer1;
    //Poll answer2
    private String pollAnswer2;
    //Poll answer3
    private String pollAnswer3;
    //Poll answer4
    private String pollAnswer4;
    //Poll questionImage
    private String pollImage1;
    //Poll questionImage2
    private String questionImage2MyPoll;
    //array list for saving the likes counts
    private ArrayList<Integer> prefrenceSearchPollLikeCount = new ArrayList<Integer>();
    //array list for saving the likes counts
    private ArrayList<Integer> searchPollLikeCount = new ArrayList<Integer>();
    //array list for saving the likes user
    private ArrayList<Integer> searchPollLikesUser = new ArrayList<Integer>();
    //like user arrayList
    private ArrayList<Integer> preferenceSearchPollLikeUser;
    //array list COMMENTS count
    private ArrayList<Integer> searchPollcommentsCount = new ArrayList<Integer>();
    //prefernce array list COMMENTS count
    private ArrayList<Integer> prefrenceSearchPollCommentsCount;
    //Poll type
    private String pollType;
    //Prefernce user poll id answer
    private ArrayList<String> preferenceSearchPollIdAnswer;
    //Prefernce user poll id answer selected
    private ArrayList<String> preferenceSearchPollIdAnswerSelected;
    //Poll id answer selected
    private ArrayList<String> searchPollIdAnwserCheck = new ArrayList<String>();
    //poll id answer check
    private ArrayList<String> searchPollIdAnwser = new ArrayList<String>();
    //Poll answer
    private String pollAnswer;
    //Poll answer selected id
    private String pollAnswerSelectedId;
    //Participate count
    private ArrayList<Integer> searchPollParticipateCount = new ArrayList<Integer>();
    //Prefernce participate count
    private ArrayList<Integer> searchUserPollParticipateCount;
    //Image
    private String image;
    //view
    private View mSearchView;
    /**
     * OnClick listner for question image2
     */
    private View.OnClickListener mSearchAdapterQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            searchPollResponseModel = getItem(clickPosition);
            //Question image
            questionImage2MyPoll = searchPollResponseModel.getPollquestionImage1();
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, questionImage2MyPoll);
        }
    };
    /**
     * OnClick listner for question single image
     */

    private View.OnClickListener mSearchAdapterSingleOption = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            searchPollResponseModel = getItem(clickPosition);
            //Question image
            if (("").equals(searchPollResponseModel.getPollquestionImage1())) {
                image = searchPollResponseModel.getPollquestionImage();
            } else {
                image = searchPollResponseModel.getPollquestionImage1();
            }
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, image);
        }
    };
    /**
     * OnClick listner for question question image1
     */
    private View.OnClickListener mSearchAdapterQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            searchPollResponseModel = getItem(clickPosition);
            //Question image
            pollImage1 = searchPollResponseModel.getPollquestionImage();
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, pollImage1);
        }
    };
    /**
     * OnClick listner for question comment
     */
    private View.OnClickListener mSearchAdapterCommentClickAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //This method will be called wto show the comment
            commentClickAction(clickPosition);
        }
    };
    /**
     * OnClick listner for question like unlike check box
     */
    private View.OnClickListener mSearchAdapterLikeUnlikeCheckBox = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            searchPollResponseModel = getItem(clickPosition);
            //Moving to the like activity
            likeClickAction(searchPollResponseModel);
        }
    };
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchAdapterShareClickAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Sharing the poll method
            shareClickAction(clickPosition);
        }
    };
    /**
     * OnClick listner for question participant count
     */
    private View.OnClickListener mSearchAdapterParticipateCounts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Participant view method
            clickAction(clickPosition);
        }
    };


    /**
     * initializes a new instance of the ListView class.
     *
     * @param activity    -activity
     * @param dataResults -reasponse from the server
     * @param layoutId    -layout id
     * @param userId      -userid
     * @param list
     */
    public SearchAdapter(Activity activity, List<UserPollResponseModel.Results.Data> dataResults, int layoutId, String userId, EndLessListView list) {
        super(activity, 0, dataResults);
        this.searchLayoutId = layoutId;
        this.context = activity;
        this.userId = userId;
        this.list = list;
    }


    /**
     * This method is when the user click ths share icon to share the poll
     *
     * @param clickPosition -position
     */

    private void shareClickAction(int clickPosition) {
        //Response from the server when the particular cell is clicked.Based on the click position data is retrived from the response.
        searchPollResponseModel = getItem(clickPosition);
        //Poll id from the response
        String pollId = searchPollResponseModel.getId();
        //Converting the poll id into base 64
        String base64CampaignId = MApplication.convertByteCode(pollId);
        //Total url
        String shareUrl = Constants.SHARE_POLL_BASE_URL.concat(base64CampaignId);
        //Sharing the url in gmail
        MApplication.shareGmail(context, shareUrl, MApplication.getString(context, Constants.PROFILE_USER_NAME));
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
        Intent details = new Intent(context, PollLikes.class);
        //Passing the id form one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Start Activity
        context.startActivity(details);
    }

    /**
     * Image full view method
     * @param clickPosition-position
     * @param pollImageMyPoll-image url
     */
    private void imageQuestionClickAction(int clickPosition, String pollImageMyPoll) {
        //response from the server
        searchPollResponseModel = getItem(clickPosition);
        //Setting the time in prefernce
        MApplication.setString(context, Constants.DATE_UPDATED, MApplication.getTimeDifference(searchPollResponseModel.getUpdatedAt()));
        //Setting the name in prefernce
        MApplication.setString(context, Constants.CAMPAIGN_NAME, searchPollResponseModel.getUserInfo().getUserName());
        //Setting the category in prefernce
        MApplication.setString(context, Constants.CAMPAIGN_CATEGORY, searchPollResponseModel.getCategory().getCategory().getCategoryName());
        //Setting the logo in prefernce
        MApplication.setString(context, Constants.CAMPAIGN_LOGO, searchPollResponseModel.getUserInfo().getUserProfileImg());
        //Moving from one activity to another activity
        Intent a = new Intent(context, FullImageView.class);
        //Passing the value from one activity to another
        a.putExtra(Constants.QUESTION1, pollImageMyPoll.replaceAll(" ", "%20"));
        //Starting the activity
        context.startActivity(a);
    }

    /**
     * Comment click ACTION
     * @param clickPosition-position
     */
    private void commentClickAction(int clickPosition) {
        //response from the server
        searchPollResponseModel = getItem(clickPosition);
        String id = searchPollResponseModel.getId();
        //Moving from one activity to another activity
        Intent details = new Intent(context, SearchPollComments.class);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_TYPE, searchPollResponseModel.getPollType());
        //Passing the value from one activity to another
        details.putExtra(Constants.COMMENTS_COUNT_POSITION, clickPosition);
        //Starting the activity
        context.startActivity(details);
    }


    @Override
    public View getView(final int position, View mView, ViewGroup parent) {
        mSearchView=mView;
        //Geeting the json response based on the position
        searchPollResponseModel = getItem(position);
        //poll type from the response
        idRefrenceView = searchPollResponseModel.getPollType();
        //Poll question from the response
        pollQuestion = searchPollResponseModel.getPollQuestion();
        //image from the response
        pollImage1 = searchPollResponseModel.getPollquestionImage().replaceAll(" ", "%20");
        //Image question from the response
        questionImage2MyPoll = searchPollResponseModel.getPollquestionImage1().replaceAll(" ", "%20");
        //poll answer form the response
        pollAnswer1 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1();
        //poll answer form the response
        pollAnswer2 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2();
        //poll answer form the response
        pollAnswer3 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3();
        //poll answer form the response
        pollAnswer4 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4();
        //youtube usrl from tthe response
        youTubeUrl = searchPollResponseModel.getYouTubeUrl();
        //Setting in prefernce
        MApplication.setString(context, Constants.YOUTUBE_URL, youTubeUrl);
        //If the value matches the the layout one view is binded.
        if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_ONE) {
            /* create a new view of my layout and inflate it in the row */
            mSearchView = LayoutInflater.from(context).inflate(R.layout.userpoll_firstview, null);
            //view holder class
            holderSearchView1 = new ViewHolderLayoutOne();
            holderSearchView1.radioYesOrNOSearchPoll = (RadioGroup) mSearchView.findViewById(R.id.YesOrNO);
            holderSearchView1.radioYesSearchPoll = (RadioButton) mSearchView.findViewById(R.id.radioYes);
            holderSearchView1.radioNoSearchPoll = (RadioButton) mSearchView.findViewById(R.id.radioNo);
            holderSearchView1.likeUnlikeYesOrNoSearchPoll = (CheckBox) mSearchView.findViewById(R.id.unLikeDislike);
            holderSearchView1.yesOrNoLikeSearchPoll = (TextView) mSearchView.findViewById(R.id.txtLike2);
            holderSearchView1.imageQuestion1YesOrNoSearchPoll = (ImageView) mSearchView.findViewById(R.id.choose);
            holderSearchView1.imageQuestion2YesOrNoSearchPoll = (ImageView) mSearchView.findViewById(R.id.ChooseAdditional);
            holderSearchView1.singleOptionYesOrNoSearchPoll = (ImageView) mSearchView.findViewById(R.id.singleOption);
            holderSearchView1.txtQuestionYesOrNoSearchPoll = (TextView) mSearchView.findViewById(R.id.txtStatus);
            holderSearchView1.txtCommentYesOrNoSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCommentsCounts);
            holderSearchView1.txtYesOrNoCounts = (TextView) mSearchView.findViewById(R.id.txtParticcipation);
            holderSearchView1.txtNameYesOrNoSearchPoll = (TextView) mSearchView.findViewById(R.id.txtName);
            holderSearchView1.txtTimeYesOrNoSearchPoll = (TextView) mSearchView.findViewById(R.id.txtTime);
            holderSearchView1.txtCategoryYesOrNoSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCategory);
            holderSearchView1.imgProfileYesOrNoSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgProfile);
            holderSearchView1.imgShareYesOrNoSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgShare);
            //Binding the data into the views in android
            validateViewLayoutOne(holderSearchView1, position);
            //If the value matches the the layout one view is binded.
        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_TWO) {
            /* create a new view of my layout and inflate it in the row */
            mSearchView = LayoutInflater.from(context).inflate(R.layout.userpoll_secondview, null);
            //view holder class
            holderSearchView2 = new ViewHolderLayoutTwo();
            holderSearchView2.radioGroupOptionsSearchPoll = (RadioGroup) mSearchView.findViewById(R.id.participate_options);
            holderSearchView2.radioOptions1MultipleOptionsSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option1);
            holderSearchView2.radioOptions2MultipleOptionsSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option2);
            holderSearchView2.radioOptions3MultipleOptionsSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option3);
            holderSearchView2.radioOptions4MultipleOptionsSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option4);
            holderSearchView2.likeUnlikeMultipleOptionsSearchPoll = (CheckBox) mSearchView.findViewById(R.id.unLikeDislike);
            holderSearchView2.txtLikeMulipleOptionsSearchPoll = (TextView) mSearchView.findViewById(R.id.txtLike2);
            holderSearchView2.txtNameMultipleOptionsSearchPoll = (TextView) mSearchView.findViewById(R.id.txtName);
            holderSearchView2.txtTimeMultipleOptionsSearchPoll = (TextView) mSearchView.findViewById(R.id.txtTime);
            holderSearchView2.txtCategoryMultipleOptionsSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCategory);
            holderSearchView2.imgProfileMultipleOptionsSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgProfile);
            holderSearchView2.txtQuestionMultipleOptionsSearchPoll = (TextView) mSearchView.findViewById(R.id.txtStatus);
            holderSearchView2.txtCommentsMultipleOptionsSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCommentsCounts);
            holderSearchView2.txtCountsMultipleOptionsSearchPoll = (TextView) mSearchView.findViewById(R.id.txtParticcipation);
            holderSearchView2.imageQuestion1MultipleOptionsSearchPoll = (ImageView) mSearchView.findViewById(R.id.choose);
            holderSearchView2.imageQuestion2MultipleOptionsSearchPoll = (ImageView) mSearchView.findViewById(R.id.ChooseAdditional);
            holderSearchView2.singleOptionMultipleOptionsSearchPoll = (ImageView) mSearchView.findViewById(R.id.singleOption);
            holderSearchView2.imgShareMultipleOptionsSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgShare);
            //Binding the data into the views in android
            validateViewLayoutTwo(holderSearchView2, position);

        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_THREE) {
             /* create a new view of my layout and inflate it in the row */
            mSearchView = LayoutInflater.from(context).inflate(R.layout.userpoll_thirdview, null);
            //view holder class
            holderSearchView3 = new ViewHolderLayoutThree();
            holderSearchView3.radioOptions1PhotoComparisonSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option1);
            holderSearchView3.radioOptions2PhotoComparisonSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option2);
            holderSearchView3.radioOptions3PhotoComparisonSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option3);
            holderSearchView3.radioOptions4PhotoComparisonSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option4);
            holderSearchView3.likeUnlikePhotoComparisonSearchPoll = (CheckBox) mSearchView.findViewById(R.id.unLikeDislike);
            holderSearchView3.txtLikePhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtLike2);
            holderSearchView3.txtQuestionPhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtStatus);
            holderSearchView3.imageAnswer1PhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.answer1);
            holderSearchView3.imageAnswer2PhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.answer2);
            holderSearchView3.imageAnswer3PhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.answer3);
            holderSearchView3.imageAnswer4PhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.answer4);
            holderSearchView3.relativeAnswer3PhotoComparisonSearchPoll = (RelativeLayout) mSearchView.findViewById(R.id.ThirdOptionOption);
            holderSearchView3.relativeAnswer4PhotoComparisonSearchPoll = (RelativeLayout) mSearchView.findViewById(R.id.FourthOptionOption);
            holderSearchView3.likeUnlikePhotoComparisonSearchPoll = (CheckBox) mSearchView.findViewById(R.id.unLikeDislike);
            holderSearchView3.txtLikePhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtLike2);
            holderSearchView3.txtCommentsPhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCommentsCounts);
            holderSearchView3.txtCountPhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtParticcipation);
            holderSearchView3.imageQuestion1PhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.choose);
            holderSearchView3.imageQuestion2PhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.ChooseAdditional);
            holderSearchView3.singleOptionPhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.singleOption);
            holderSearchView3.txtNamePhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtName);
            holderSearchView3.txtTimePhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtTime);
            holderSearchView3.txtCategoryPhotoComparisonSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCategory);
            holderSearchView3.imgProfilePhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgProfile);
            holderSearchView3.imgSharePhotoComparisonSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgShare);
            //Binding the data into the views in android
            validateViewLayoutThree(holderSearchView3, position);


        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_FOUR) {
            /* create a new view of my layout and inflate it in the row */
            mSearchView = LayoutInflater.from(context).inflate(R.layout.userpoll_fourthview, null);
            //view holder class
            holderSearchView4 = new ViewHolderLayoutFour();
            holderSearchView4 = new ViewHolderLayoutFour();
            holderSearchView4.radioGroupOptionsYouTubeUrlSearchPoll = (RadioGroup) mSearchView.findViewById(R.id.participate_options);
            holderSearchView4.radioOptions1YouTubeUrlSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option1);
            holderSearchView4.radioOptions2YouTubeUrlSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option2);
            holderSearchView4.radioOptions3YouTubeUrlSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option3);
            holderSearchView4.radioOptions4YouTubeUrlSearchPoll = (RadioButton) mSearchView.findViewById(R.id.option4);
            holderSearchView4.likeUnlikeYouTubeUrlSearchPoll = (CheckBox) mSearchView.findViewById(R.id.unLikeDislike);
            holderSearchView4.txtLikeYouTubeUrlSearchPoll = (TextView) mSearchView.findViewById(R.id.txtLike2);
            holderSearchView4.txtQuestionYouTubeUrlSearchPoll = (TextView) mSearchView.findViewById(R.id.txtStatus);
            holderSearchView4.txtCommentsYouTubeUrlSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCommentsCounts);
            holderSearchView4.txtVideoUrlCountYouTubeUrlSearchPoll = (TextView) mSearchView.findViewById(R.id.txtParticcipation);
            holderSearchView4.imageQuestion1YouTubeUrlSearchPoll = (ImageView) mSearchView.findViewById(R.id.choose);
            holderSearchView4.imageQuestion2YouTubeUrlSearchPoll = (ImageView) mSearchView.findViewById(R.id.ChooseAdditional);
            holderSearchView4.singleOptionYouTubeUrlSearchPoll = (ImageView) mSearchView.findViewById(R.id.singleOption);
            holderSearchView4.txtNameYouTubeUrlSearchPoll = (TextView) mSearchView.findViewById(R.id.txtName);
            holderSearchView4.txtTimeYouTubeUrlSearchPoll = (TextView) mSearchView.findViewById(R.id.txtTime);
            holderSearchView4.txtCategoryYouTubeUrlSearchPoll = (TextView) mSearchView.findViewById(R.id.txtCategory);
            holderSearchView4.imgProfileYouTubeUrlSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgProfile);
            holderSearchView4.imgShareYouTubeUrlSearchPoll = (ImageView) mSearchView.findViewById(R.id.imgShare);
            //Binding the data into the views in android
            validateViewLayoutFour(holderSearchView4, position);

        }


        return mSearchView;
    }

    /**
     * Clicking the participate icon.
     *
     * @param clickPosition
     */
    private void clickAction(int clickPosition) {
        searchPollIdAnwserCheck= MApplication.loadStringArray(context, searchPollIdAnwserCheck, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);
            //Response from the server when the particular cell is clicked.Based on the click position data is retrived from the response.
            searchPollResponseModel = getItem(clickPosition);
            //Polltype from the response
            pollType = searchPollResponseModel.getPollType();
            //Poll id from the response
            String pollId = searchPollResponseModel.getId();
        if(!searchPollIdAnwserCheck.isEmpty()) {
            if(searchPollIdAnwserCheck.contains(pollId)) {
            //Load the participate count from  the saved prefernce
            searchUserPollParticipateCount = MApplication.loadArray(context, searchPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);
            //Setting the time in prefernce
            MApplication.setString(context, Constants.DATE_UPDATED, MApplication.getTimeDifference(searchPollResponseModel.getUpdatedAt()));
            //Setting the username in prefernce
            MApplication.setString(context, Constants.CAMPAIGN_NAME, searchPollResponseModel.getUserInfo().getUserName());
            //Setting the categoryname in prefernce
            MApplication.setString(context, Constants.CAMPAIGN_CATEGORY, searchPollResponseModel.getCategory().getCategory().getCategoryName());
            //Setting the userprofile in prefernce
            MApplication.setString(context, Constants.CAMPAIGN_LOGO, searchPollResponseModel.getUserInfo().getUserProfileImg());
            //It can be used with startActivity to launch an Activity.
            Intent a = new Intent(context, SearchPollReview.class);
            //Pushing the values from one activity to another activity
            a.putExtra(Constants.POLL_TYPE, pollType);
            a.putExtra(Constants.POLL_ID, pollId);
            a.putExtra(Constants.TYPE, Constants.POLLS);
            a.putExtra(Constants.ARRAY_POSITION, clickPosition);
            a.putExtra(Constants.PARTICIPATE_COUNT, String.valueOf(searchUserPollParticipateCount.get(clickPosition)));
            //Starting the activity
            context.startActivity(a);
        }else {
            Toast.makeText(context, "You must attend the poll to view the results",
                    Toast.LENGTH_SHORT).show();
        }
    }else {
        Toast.makeText(context, "You must attend the poll to view the results",
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
        prefrenceSearchPollLikeCount = MApplication.loadArray(context, searchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);   //Load the like count from  the saved prefernce
        prefrenceSearchPollCommentsCount = MApplication.loadArray(context, searchPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);//Load the COMMENTS count from  the saved prefernce
        preferenceSearchPollLikeUser = MApplication.loadArray(context, searchPollLikesUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);  //Load the like user from  the saved prefernce
        preferenceSearchPollIdAnswer = MApplication.loadStringArray(context, searchPollIdAnwserCheck, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);  //Load the poll id from  the saved prefernce
        preferenceSearchPollIdAnswerSelected = MApplication.loadStringArray(context, searchPollIdAnwser, Constants.SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.SEARCH_POLL_ID_SELECTED_SIZE); //Load the answer from  the saved prefernce
        //Load the participate count  the saved prefernce
        searchUserPollParticipateCount = MApplication.loadArray(context, searchPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);

        //Setting the answer1 in ratio options
        holder.radioYesSearchPoll.setText(pollAnswer1);
        //Setting the answer12in ratio options
        holder.radioNoSearchPoll.setText(pollAnswer2);
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceSearchPollIdAnswer.contains(searchPollResponseModel.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceSearchPollIdAnswer.indexOf(searchPollResponseModel.getId());
            //Selected answer from the position
            String value = preferenceSearchPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (holder.radioYesSearchPoll.getText().toString().equalsIgnoreCase(value)) {
                //Setting the radio options true
                holder.radioYesSearchPoll.setChecked(true);
                //Setting the text color black
                holder.radioYesSearchPoll.setTextColor(Color.BLACK);
            } else if (holder.radioNoSearchPoll.getText().toString().equalsIgnoreCase(value)) {
                //Setting the text color black
                holder.radioNoSearchPoll.setTextColor(Color.BLACK);
                //Setting the radio options true
                holder.radioNoSearchPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioNoSearchPoll.setClickable(false);
            //Setting the clickable false
            holder.radioYesSearchPoll.setClickable(false);
        }
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceSearchPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYesOrNoSearchPoll.setChecked(true);
        } else {
            holder.likeUnlikeYesOrNoSearchPoll.setChecked(false);
        }
        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(pollImage1) && ("").equals(questionImage2MyPoll)) {
            holder.imageQuestion1YesOrNoSearchPoll.setVisibility(View.GONE);
            holder.imageQuestion2YesOrNoSearchPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(questionImage2MyPoll)) {
                Utils.loadImageWithGlideRounderCorner(context,pollImage1,holder.imageQuestion1YesOrNoSearchPoll,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(context,questionImage2MyPoll,holder.imageQuestion2YesOrNoSearchPoll,R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(questionImage2MyPoll)) {
                holder.singleOptionYesOrNoSearchPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(context,pollImage1,holder.singleOptionYesOrNoSearchPoll,R.drawable.placeholder_image);
                holder.imageQuestion1YesOrNoSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoSearchPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(questionImage2MyPoll)) {
                holder.singleOptionYesOrNoSearchPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(context,questionImage2MyPoll,holder.singleOptionYesOrNoSearchPoll,R.drawable.placeholder_image);
                holder.imageQuestion1YesOrNoSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoSearchPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1YesOrNoSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoSearchPoll.setVisibility(View.GONE);
            }
        }
        //setting the poll question in text view
        holder.txtQuestionYesOrNoSearchPoll.setText(pollQuestion);
        //Setting the like count based on the position in the text view
        holder.yesOrNoLikeSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(position)));

        if(searchPollResponseModel.getUserInfo()!=null) {
            //Getting the user name from the response and binding the data into the text view
            holder.txtNameYesOrNoSearchPoll.setText(searchPollResponseModel.getUserInfo().getUserName());
            //Getting the category name from the response and binding the data into the text view
            Utils.loadImageWithGlide(context, searchPollResponseModel.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"), holder.imgProfileYesOrNoSearchPoll, R.drawable.placeholder_image);
        }
        //Getting the time from the response and binding the data into the text view
        holder.txtTimeYesOrNoSearchPoll.setText(MApplication.getTimeDifference(searchPollResponseModel.getUpdatedAt()));
        //Getting the category name from the response and binding the data into the text view
        holder.txtCategoryYesOrNoSearchPoll.setText(searchPollResponseModel.getCategory().getCategory().getCategoryName());

        //Getting the COMMENTS name from the prefence and binding the data into the text view
        holder.txtCommentYesOrNoSearchPoll.setText(String.valueOf(prefrenceSearchPollCommentsCount.get(position)));
        //Setting the like count based on the position in the text view
        holder.txtYesOrNoCounts.setText(String.valueOf(searchUserPollParticipateCount.get(position)));
        //onClick listner method
        holder1SearchOnClickListner(holder,position);
    }

    private void holder1SearchOnClickListner(final ViewHolderLayoutOne holder, final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtYesOrNoCounts.setOnClickListener(mSearchAdapterParticipateCounts);
        holder.imgShareYesOrNoSearchPoll.setOnClickListener(mSearchAdapterShareClickAction);
        holder.yesOrNoLikeSearchPoll.setOnClickListener(mSearchAdapterLikeUnlikeCheckBox);
        holder.txtCommentYesOrNoSearchPoll.setOnClickListener(mSearchAdapterCommentClickAction);
        holder.imageQuestion1YesOrNoSearchPoll.setOnClickListener(mSearchAdapterQuestionImage1);
        holder.imageQuestion2YesOrNoSearchPoll.setOnClickListener(mSearchAdapterQuestionImage2);
        holder.singleOptionYesOrNoSearchPoll.setOnClickListener(mSearchAdapterSingleOption);

        holder.radioYesSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1();     //poll answer
                    optionSelection(clickPosition, holderSearchView1, null, null, null);   //Option selection request
                }
                return false;
            }
        });
        holder.radioNoSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2();     //poll answer
                    optionSelection(clickPosition, holderSearchView1, null, null, null);   //Option selection request
                }
                return false;
            }
        });
        // Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYesOrNoSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYesOrNoSearchPoll.setChecked(true);
                    likesUnLikes(position, holder, null, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYesOrNoSearchPoll.setChecked(false);
                    likesUnLikes(position, holder, null, null, null);
                }
            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holderSearchView1.radioYesOrNOSearchPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if (checkedId == R.id.radioYes) {   //If the view id matches the below fuctinolity will take place
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1();     //poll answer
                    optionSelection(clickPosition, holderSearchView1, null, null, null);   //Option selection request
                } else if (checkedId == R.id.radioNo) {
                    pollAnswerSelectedId = "2";   //poll answer selected id is set as 2
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2();   //poll answer
                    optionSelection(clickPosition, holderSearchView1, null, null, null);  //Option selection request
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
        prefrenceSearchPollLikeCount = MApplication.loadArray(context, searchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceSearchPollCommentsCount = MApplication.loadArray(context, searchPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceSearchPollLikeUser = MApplication.loadArray(context, searchPollLikesUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceSearchPollIdAnswer = MApplication.loadStringArray(context, searchPollIdAnwserCheck, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceSearchPollIdAnswerSelected = MApplication.loadStringArray(context, searchPollIdAnwser, Constants.SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.SEARCH_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        searchUserPollParticipateCount = MApplication.loadArray(context, searchPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceSearchPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeMultipleOptionsSearchPoll.setChecked(true);
        } else {
            holder.likeUnlikeMultipleOptionsSearchPoll.setChecked(false);
        }
        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(pollImage1) && ("").equals(questionImage2MyPoll)) {
            holder.imageQuestion1MultipleOptionsSearchPoll.setVisibility(View.GONE);
            holder.imageQuestion2MultipleOptionsSearchPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(questionImage2MyPoll)) {
                Utils.loadImageWithGlideRounderCorner(context,pollImage1,holder.imageQuestion1MultipleOptionsSearchPoll,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(context,questionImage2MyPoll,holder.imageQuestion2MultipleOptionsSearchPoll,R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(questionImage2MyPoll)) {
                holder.singleOptionMultipleOptionsSearchPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(context,pollImage1,holder.singleOptionMultipleOptionsSearchPoll,R.drawable.placeholder_image);
                holder.imageQuestion1MultipleOptionsSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsSearchPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(questionImage2MyPoll)) {
                holder.singleOptionMultipleOptionsSearchPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(context,questionImage2MyPoll,holder.singleOptionMultipleOptionsSearchPoll,R.drawable.placeholder_image);
                holder.imageQuestion1MultipleOptionsSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsSearchPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1MultipleOptionsSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsSearchPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.radioOptions1MultipleOptionsSearchPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer1)) {
            holder.radioOptions1MultipleOptionsSearchPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1MultipleOptionsSearchPoll.setText(pollAnswer1);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            holder.radioOptions2MultipleOptionsSearchPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions2MultipleOptionsSearchPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2MultipleOptionsSearchPoll.setText(pollAnswer2);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.radioOptions3MultipleOptionsSearchPoll.setVisibility(View.GONE);
            holder.radioOptions4MultipleOptionsSearchPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer3)) {
            holder.radioOptions3MultipleOptionsSearchPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3MultipleOptionsSearchPoll.setText(pollAnswer3);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.radioOptions4MultipleOptionsSearchPoll.setVisibility(View.GONE);
                //If the poll answer option is not empty then the view will invisible
            } else if (!("").equals(pollAnswer4)) {
                holder.radioOptions4MultipleOptionsSearchPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4MultipleOptionsSearchPoll.setText(pollAnswer4);
            }
        }
        Log.e("preferenceSearchPol",preferenceSearchPollIdAnswer+"");
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceSearchPollIdAnswer.contains(searchPollResponseModel.getId())) {
            Log.e("searchPoll",searchPollResponseModel.getId()+"");
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceSearchPollIdAnswer.indexOf(searchPollResponseModel.getId());
            Log.e("answeredPosition",answeredPosition+"");
            //Selected answer from the position
            String value = preferenceSearchPollIdAnswerSelected.get(answeredPosition);
            Log.e("value",value+"");
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (holder.radioOptions1MultipleOptionsSearchPoll.getText().equals(value)) {
                Log.e("1","1");
                //Setting the radio options true
                holder.radioOptions1MultipleOptionsSearchPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions1MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions2MultipleOptionsSearchPoll.getText().equals(value)) {
                Log.e("2","2");
                //Setting the radio options true
                holder.radioOptions2MultipleOptionsSearchPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions2MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions3MultipleOptionsSearchPoll.getText().equals(value)) {
                Log.e("3","3");
                //Setting the radio options true
                holder.radioOptions3MultipleOptionsSearchPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions3MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions4MultipleOptionsSearchPoll.getText().equals(value)) {
                Log.e("4","4");
                //Setting the radio options true
                holder.radioOptions4MultipleOptionsSearchPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions4MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
            }
            //Setting the clickable false
            holder.radioOptions1MultipleOptionsSearchPoll.setClickable(false);
            holder.radioOptions2MultipleOptionsSearchPoll.setClickable(false);
            holder.radioOptions3MultipleOptionsSearchPoll.setClickable(false);
            holder.radioOptions4MultipleOptionsSearchPoll.setClickable(false);
        }
        //setting the poll question in text view
        holder.txtCommentsMultipleOptionsSearchPoll.setText(String.valueOf(prefrenceSearchPollCommentsCount.get(position)));
        //Setting the like count based on the position in the text view
        holder.txtLikeMulipleOptionsSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(position)));
        //Setting the user name in the textviw
        holder.txtNameMultipleOptionsSearchPoll.setText(searchPollResponseModel.getUserInfo().getUserName());
        //Setting the updating to=ime in text view
        holder.txtTimeMultipleOptionsSearchPoll.setText(MApplication.getTimeDifference(searchPollResponseModel.getUpdatedAt()));
        //Setting the poll question in text view
        holder.txtQuestionMultipleOptionsSearchPoll.setText(pollQuestion);
        //Setting the category intext view
        holder.txtCategoryMultipleOptionsSearchPoll.setText(searchPollResponseModel.getCategory().getCategory().getCategoryName());
        //Setting the user profile image
        Utils.loadImageWithGlide(context,searchPollResponseModel.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"),holder.imgProfileMultipleOptionsSearchPoll,R.drawable.placeholder_image);
        //Setting the text participate count from the array list based on the position
        holder.txtCountsMultipleOptionsSearchPoll.setText(String.valueOf(searchUserPollParticipateCount.get(position)));
        holder2SearchOnClickListner(holder,position);
    }

    private void holder2SearchOnClickListner(final ViewHolderLayoutTwo holder,final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountsMultipleOptionsSearchPoll.setOnClickListener(mSearchAdapterParticipateCounts);
        holder.imgShareMultipleOptionsSearchPoll.setOnClickListener(mSearchAdapterShareClickAction);
        holder.txtLikeMulipleOptionsSearchPoll.setOnClickListener(mSearchAdapterLikeUnlikeCheckBox);
        holder.txtCommentsMultipleOptionsSearchPoll.setOnClickListener(mSearchAdapterCommentClickAction);
        holder.imageQuestion1MultipleOptionsSearchPoll.setOnClickListener(mSearchAdapterQuestionImage1);
        holder.imageQuestion2MultipleOptionsSearchPoll.setOnClickListener(mSearchAdapterQuestionImage2);
        holder.singleOptionMultipleOptionsSearchPoll.setOnClickListener(mSearchAdapterSingleOption);
//Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeMultipleOptionsSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeMultipleOptionsSearchPoll.setChecked(true);
                    likesUnLikes(position, null, holder, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeMultipleOptionsSearchPoll.setChecked(false);
                    likesUnLikes(position, null, holder, null, null);
                }

            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsSearchPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position;
                searchPollResponseModel = getItem(clickPosition);
                optionClickAction(checkedId);
                optionSelection(clickPosition, null, holder, null, null);
            }
        });
        holder.radioOptions1MultipleOptionsSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1();     //poll answer
                    optionSelection(clickPosition, null, holder, null, null);   //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions2MultipleOptionsSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2();     //poll answer
                    optionSelection(clickPosition, null, holder, null, null);   //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions3MultipleOptionsSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3();     //poll answer
                    optionSelection(clickPosition, null, holder, null, null);   //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions4MultipleOptionsSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4();     //poll answer
                    optionSelection(clickPosition, null, holder, null, null);   //Option selection request
                }
                return false;
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
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1();
                break;
            case R.id.option2:
                //Setting the value for the variable
                pollAnswerSelectedId = "2";
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2();
                break;
            case R.id.option3:
                //Setting the value for the variable
                pollAnswerSelectedId = "3";
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3();
                break;
            case R.id.option4:
                //Setting the value for the variable
                pollAnswerSelectedId = "4";
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4();
                break;
            default:
                break;
        }

    }



    /**
     * View hoder3
     *
     * @param holder            -view holder class
     * @param position-position
     */
    private void validateViewLayoutThree(final ViewHolderLayoutThree holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceSearchPollLikeCount = MApplication.loadArray(context, searchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceSearchPollCommentsCount = MApplication.loadArray(context, searchPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceSearchPollLikeUser = MApplication.loadArray(context, searchPollLikesUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceSearchPollIdAnswer = MApplication.loadStringArray(context, searchPollIdAnwserCheck, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceSearchPollIdAnswerSelected = MApplication.loadStringArray(context, searchPollIdAnwser, Constants.SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.SEARCH_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        searchUserPollParticipateCount = MApplication.loadArray(context, searchPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceSearchPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikePhotoComparisonSearchPoll.setChecked(true);
        } else {
            holder.likeUnlikePhotoComparisonSearchPoll.setChecked(false);
        }
        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(pollImage1) && ("").equals(questionImage2MyPoll)) {
            holder.imageQuestion1PhotoComparisonSearchPoll.setVisibility(View.GONE);
            holder.imageQuestion2PhotoComparisonSearchPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(questionImage2MyPoll)) {
                Utils.loadImageWithGlideRounderCorner(context,pollImage1,holder.imageQuestion1PhotoComparisonSearchPoll,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(context,questionImage2MyPoll,holder.imageQuestion2PhotoComparisonSearchPoll,R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(questionImage2MyPoll)) {
                holder.singleOptionPhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(context,pollImage1,holder.singleOptionPhotoComparisonSearchPoll,R.drawable.placeholder_image);
                holder.imageQuestion1PhotoComparisonSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonSearchPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(questionImage2MyPoll)) {
                holder.singleOptionPhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(context,questionImage2MyPoll,holder.singleOptionPhotoComparisonSearchPoll,R.drawable.placeholder_image);
                holder.imageQuestion1PhotoComparisonSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonSearchPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1PhotoComparisonSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonSearchPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.imageAnswer1PhotoComparisonSearchPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer1PhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(context,pollAnswer1.replaceAll(" ", "%20"),holder.imageAnswer1PhotoComparisonSearchPoll,R.drawable.placeholder_image);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            Utils.loadImageWithGlideRounderCorner(context,pollAnswer1.replaceAll(" ", "%20"),holder.imageAnswer1PhotoComparisonSearchPoll,R.drawable.placeholder_image);
            holder.imageAnswer2PhotoComparisonSearchPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer2PhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(context,pollAnswer2.replaceAll(" ", "%20"),holder.imageAnswer2PhotoComparisonSearchPoll,R.drawable.placeholder_image);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.imageAnswer3PhotoComparisonSearchPoll.setVisibility(View.GONE);
            holder.imageAnswer4PhotoComparisonSearchPoll.setVisibility(View.GONE);
            holder.relativeAnswer3PhotoComparisonSearchPoll.setVisibility(View.GONE);
            holder.relativeAnswer4PhotoComparisonSearchPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer3)) {
            holder.imageAnswer3PhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
            holder.relativeAnswer3PhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(context,pollAnswer3.replaceAll(" ", "%20"),holder.imageAnswer3PhotoComparisonSearchPoll,R.drawable.placeholder_image);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.imageAnswer4PhotoComparisonSearchPoll.setVisibility(View.INVISIBLE);
                holder.relativeAnswer4PhotoComparisonSearchPoll.setVisibility(View.GONE);
                //If the poll answer option is not empty then the view will invisible
            } else if (!("").equals(pollAnswer4)) {
                holder.imageAnswer4PhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideRounderCorner(context,pollAnswer4.replaceAll(" ", "%20"),holder.imageAnswer4PhotoComparisonSearchPoll,R.drawable.placeholder_image);
                holder.relativeAnswer4PhotoComparisonSearchPoll.setVisibility(View.VISIBLE);
            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceSearchPollIdAnswer.contains(searchPollResponseModel.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceSearchPollIdAnswer.indexOf(searchPollResponseModel.getId());
            //Selected answer from the position
            String value = preferenceSearchPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1().equalsIgnoreCase(value)) {
                holder.radioOptions1PhotoComparisonSearchPoll.setChecked(true);
            } else if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2().equalsIgnoreCase(value)) {
                holder.radioOptions2PhotoComparisonSearchPoll.setChecked(true);
            } else if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3().equalsIgnoreCase(value)) {
                holder.radioOptions3PhotoComparisonSearchPoll.setChecked(true);
            } else if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4().equalsIgnoreCase(value)) {
                holder.radioOptions4PhotoComparisonSearchPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1PhotoComparisonSearchPoll.setClickable(false);
            holder.radioOptions2PhotoComparisonSearchPoll.setClickable(false);
            holder.radioOptions3PhotoComparisonSearchPoll.setClickable(false);
            holder.radioOptions4PhotoComparisonSearchPoll.setClickable(false);
        }
        //Setting the poll question intext view
        holder.txtQuestionPhotoComparisonSearchPoll.setText(pollQuestion);
        //Setting the prefernce like count
        holder.txtLikePhotoComparisonSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(position)));
        //Setting the user name in textview
        holder.txtNamePhotoComparisonSearchPoll.setText(searchPollResponseModel.getUserInfo().getUserName());
        //Setting the updated time
        holder.txtTimePhotoComparisonSearchPoll.setText(MApplication.getTimeDifference(searchPollResponseModel.getUpdatedAt()));
        //Setting the category name
        holder.txtCategoryPhotoComparisonSearchPoll.setText(searchPollResponseModel.getCategory().getCategory().getCategoryName());
        //Setting the user profile image
        Utils.loadImageWithGlide(context,searchPollResponseModel.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"),holder.imgProfilePhotoComparisonSearchPoll,R.drawable.placeholder_image);
        //Setting the participate count from the arraylist based on the position
        holder.txtCountPhotoComparisonSearchPoll.setText(String.valueOf(searchUserPollParticipateCount.get(position)));
        //Setting the COMMENTS from the arraylist  based on the position
        holder.txtCommentsPhotoComparisonSearchPoll.setText(String.valueOf(prefrenceSearchPollCommentsCount.get(position)));
         holder3SearchOnClickListner(holder,position);
    }

    private void holder3SearchOnClickListner(final ViewHolderLayoutThree holder,final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountPhotoComparisonSearchPoll.setOnClickListener(mSearchAdapterParticipateCounts);
        holder.imgSharePhotoComparisonSearchPoll.setOnClickListener(mSearchAdapterShareClickAction);
        holder.txtLikePhotoComparisonSearchPoll.setOnClickListener(mSearchAdapterLikeUnlikeCheckBox);
        holder.txtCommentsPhotoComparisonSearchPoll.setOnClickListener(mSearchAdapterCommentClickAction);
        holder.imageQuestion1PhotoComparisonSearchPoll.setOnClickListener(mSearchAdapterQuestionImage1);
        holder.imageQuestion2PhotoComparisonSearchPoll.setOnClickListener(mSearchAdapterQuestionImage2);
        holder.singleOptionPhotoComparisonSearchPoll.setOnClickListener(mSearchAdapterSingleOption);
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer1PhotoComparisonSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswer1 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer1);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer2PhotoComparisonSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswer2 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer2);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer3PhotoComparisonSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswer3 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer3);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer4PhotoComparisonSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswer4 = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer4);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikePhotoComparisonSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikePhotoComparisonSearchPoll.setChecked(true);
                    likesUnLikes(position, null, null, holder, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikePhotoComparisonSearchPoll.setChecked(false);
                    likesUnLikes(position, null, null, holder, null);

                }

            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions1PhotoComparisonSearchPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswerSelectedId = "1";
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions2PhotoComparisonSearchPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswerSelectedId = "2";
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions3PhotoComparisonSearchPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswerSelectedId = "3";
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions4PhotoComparisonSearchPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                searchPollResponseModel = getItem(clickPosition);
                //poll answer url
                pollAnswerSelectedId = "4";
                pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4();
                optionSelection(clickPosition, null, null, holder, null);
            }

        });
        holder.radioOptions1PhotoComparisonSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1();     //poll answer
                    optionSelection(clickPosition, null, null, holder, null);   //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions2PhotoComparisonSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2();     //poll answer
                    optionSelection(clickPosition, null, null, holder, null);   //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions3PhotoComparisonSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3();     //poll answer
                    optionSelection(clickPosition, null, null, holder, null);   //Option selection request
                }
                return false;
            }
        });
        holder.radioOptions4PhotoComparisonSearchPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position;  //Click position
                searchPollResponseModel = getItem(clickPosition);  //Getting the item from the response
                if(!view.isClickable()){
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    pollAnswer = searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4();     //poll answer
                    optionSelection(clickPosition, null, null, holder, null);   //Option selection request
                }
                return false;
            }
        });

    }

    private void validateViewLayoutFour(final ViewHolderLayoutFour holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceSearchPollLikeCount = MApplication.loadArray(context, searchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceSearchPollCommentsCount = MApplication.loadArray(context, searchPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceSearchPollLikeUser = MApplication.loadArray(context, searchPollLikesUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceSearchPollIdAnswer = MApplication.loadStringArray(context, searchPollIdAnwserCheck, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceSearchPollIdAnswerSelected = MApplication.loadStringArray(context, searchPollIdAnwser, Constants.SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.SEARCH_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        searchUserPollParticipateCount = MApplication.loadArray(context, searchPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceSearchPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYouTubeUrlSearchPoll.setChecked(true);
        } else {
            holder.likeUnlikeYouTubeUrlSearchPoll.setChecked(false);
        }
        //If the question image 1 and Question 2 is empty visiblity will be gone
        if (("").equals(pollImage1)&& ("").equals(questionImage2MyPoll)) {
            holder.imageQuestion1YouTubeUrlSearchPoll.setVisibility(View.GONE);
            holder.imageQuestion2YouTubeUrlSearchPoll.setVisibility(View.GONE);
        } else {
            //If the question image 1 and Question 2 is not empty visiblity will be visible
            if (!("").equals(pollImage1)&& !("").equals(questionImage2MyPoll)) {
                holder.imageQuestion1YouTubeUrlSearchPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion2YouTubeUrlSearchPoll.setImageURI(Uri.parse(questionImage2MyPoll));
                //If the question image 1 is  not empty visiblity will be visible
            } else if (!("").equals(pollImage1)&& ("").equals(questionImage2MyPoll)) {
                holder.singleOptionYouTubeUrlSearchPoll.setVisibility(View.VISIBLE);
                holder.singleOptionYouTubeUrlSearchPoll.setImageURI(Uri.parse(pollImage1));
                holder.imageQuestion1YouTubeUrlSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlSearchPoll.setVisibility(View.GONE);
                //If the question image 2 is not empty visiblity will be visible
            } else if (("").equals(pollImage1)&& !("").equals(questionImage2MyPoll)) {
                holder.singleOptionYouTubeUrlSearchPoll.setVisibility(View.VISIBLE);
                holder.singleOptionYouTubeUrlSearchPoll.setImageURI(Uri.parse(questionImage2MyPoll));
                holder.imageQuestion1YouTubeUrlSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlSearchPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1YouTubeUrlSearchPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlSearchPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.radioOptions1YouTubeUrlSearchPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions1YouTubeUrlSearchPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1YouTubeUrlSearchPoll.setText(pollAnswer1);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            holder.radioOptions2YouTubeUrlSearchPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions2YouTubeUrlSearchPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2YouTubeUrlSearchPoll.setText(pollAnswer2);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.radioOptions3YouTubeUrlSearchPoll.setVisibility(View.GONE);
            holder.radioOptions3YouTubeUrlSearchPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer3)) {
            holder.radioOptions3YouTubeUrlSearchPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3YouTubeUrlSearchPoll.setText(pollAnswer3);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.radioOptions4YouTubeUrlSearchPoll.setVisibility(View.GONE);
            } else if (!("").equals(pollAnswer4)){
                holder.radioOptions4YouTubeUrlSearchPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4YouTubeUrlSearchPoll.setText(pollAnswer4);

            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceSearchPollIdAnswer.contains(searchPollResponseModel.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceSearchPollIdAnswer.indexOf(searchPollResponseModel.getId());
            //Selected answer from the position
            String value = preferenceSearchPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer1().equals(value)) {
                holder.radioOptions1YouTubeUrlSearchPoll.setChecked(true);
            } else if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer2().equals(value)) {
                holder.radioOptions2YouTubeUrlSearchPoll.setChecked(true);
            } else if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer3().equals(value)) {
                holder.radioOptions3YouTubeUrlSearchPoll.setChecked(true);
            } else if (searchPollResponseModel.getUserPollsAns().get(0).getPollAnswer4().equals(value)) {
                holder.radioOptions4YouTubeUrlSearchPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1YouTubeUrlSearchPoll.setClickable(false);
            holder.radioOptions2YouTubeUrlSearchPoll.setClickable(false);
            holder.radioOptions3YouTubeUrlSearchPoll.setClickable(false);
            holder.radioOptions4YouTubeUrlSearchPoll.setClickable(false);
        }
        //Setting the like count in text view
        holder.txtLikeYouTubeUrlSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(position)));
        //Setting the user name in text view
        holder.txtNameYouTubeUrlSearchPoll.setText(searchPollResponseModel.getUserInfo().getUserName());
        //Setting the updted time
        holder.txtTimeYouTubeUrlSearchPoll.setText(MApplication.getTimeDifference(searchPollResponseModel.getUpdatedAt()));
        //Setting the category name in text view
        holder.txtCategoryYouTubeUrlSearchPoll.setText(searchPollResponseModel.getCategory().getCategory().getCategoryName());
        //Setting the user profile image
        Utils.loadImageWithGlide(context,searchPollResponseModel.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"),holder.imgProfileYouTubeUrlSearchPoll,R.drawable.placeholder_image);
        //Setting the count from the arraylist
        holder.txtVideoUrlCountYouTubeUrlSearchPoll.setText(String.valueOf(searchUserPollParticipateCount.get(position)));
        //Setting the question  in text view
        holder.txtQuestionYouTubeUrlSearchPoll.setText(pollQuestion);
        //Setting the COMMENTS from the arraylist
        holder.txtCommentsYouTubeUrlSearchPoll.setText(String.valueOf(prefrenceSearchPollCommentsCount.get(position)));

        holder4SearchOnClickListner(holder,position);
    }

    private void holder4SearchOnClickListner(final ViewHolderLayoutFour holder,final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYouTubeUrlSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYouTubeUrlSearchPoll.setChecked(true);
                    likesUnLikes(position, null, null, null, holder);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYouTubeUrlSearchPoll.setChecked(false);
                    likesUnLikes(position, null, null, null, holder);

                }

            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtVideoUrlCountYouTubeUrlSearchPoll.setOnClickListener(mSearchAdapterParticipateCounts);
        holder.imgShareYouTubeUrlSearchPoll.setOnClickListener(mSearchAdapterShareClickAction);
        holder.txtLikeYouTubeUrlSearchPoll.setOnClickListener(mSearchAdapterLikeUnlikeCheckBox);
        holder.txtCommentsYouTubeUrlSearchPoll.setOnClickListener(mSearchAdapterCommentClickAction);
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsYouTubeUrlSearchPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Click position
                int clickPosition = position;
                //Response
                searchPollResponseModel = getItem(clickPosition);
                optionClickAction(checkedId);
                optionSelection(clickPosition, null, null, null, holder);
            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.singleOptionYouTubeUrlSearchPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickPosition = position;  //Click ACTION
                searchPollResponseModel = getItem(clickPosition); //Response
                youTubeUrl = searchPollResponseModel.getYouTubeUrl();   //Getting the url from the response
                MApplication.setString(context, Constants.YOUTUBE_URL, youTubeUrl);     //Setting in prefernce
                //If net is connected video is played
                //else toast message
                if (MApplication.isNetConnected((Activity) context)) {
                    Intent a = new Intent(context, VideoLandscapeActivity.class);
                    context.startActivity(a);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection),
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
        searchPollResponseModel = getItem(clickPosition);
        //Getting the id
        String pollId = searchPollResponseModel.getId();
        LikesAndUnLikeRestClient.getInstance().postCampaignPollLikes(new String("poll_likes"), new String(userId), new String(pollId), new String(String.valueOf(mlikes))
                , new Callback<LikeUnLikeResposneModel>() {
            @Override
            public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                //If the value from the response is 1 then the user has successfully liked the poll
                if (("1").equals(likesUnlike.getResults())) {
                    //Changing the value in array list in a particular position
                    preferenceSearchPollLikeUser.set(clickPosition, Integer.valueOf(1));
                    //Saving it in array
                    MApplication.saveArray(context, preferenceSearchPollLikeUser, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
                } else {
                    //Changing the value in array list in a particular position
                    preferenceSearchPollLikeUser.set(clickPosition, Integer.valueOf(0));
                    //Saving it in array
                    MApplication.saveArray(context, preferenceSearchPollLikeUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
                }
                Log.e("preferenceSearch",preferenceSearchPollLikeUser+"");
                //Toast message will display
                Toast.makeText(context, likesUnlike.getMsg(),
                        Toast.LENGTH_SHORT).show();
                //Changing the value in array list in a particular position
                prefrenceSearchPollLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                //Saving the array in prefernce
                MApplication.saveArray(context, prefrenceSearchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
                //If holder1 is not null
                if (holder1 != null) {
                    //Changing the value in textview
                    holder1.yesOrNoLikeSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(clickPosition)));
                    //If holder2 is not null
                } else if (holder2 != null) {
                    //Changing the value in textview
                    holder2.txtLikeMulipleOptionsSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(clickPosition)));
                    //If holder3 is not null
                } else if (holder3 != null) {
                    //Changing the value in textview
                    holder3.txtLikePhotoComparisonSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(clickPosition)));
                    //If holder4 is not null
                } else if (holder4 != null) {
                    //Changing the value in textview
                    holder4.txtLikeYouTubeUrlSearchPoll.setText(String.valueOf(prefrenceSearchPollLikeCount.get(clickPosition)));
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, context);
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
        searchPollResponseModel = getItem(clickPosition);
        //Getting the id from the response
        String pollId = searchPollResponseModel.getId();
        //Getting the poll answer id from the response
        String pollAnswerId = searchPollResponseModel.getUserPollsAns().get(0).getId();
        PollParticipateRestClient.getInstance().postParticipateApi(new String("polls_participate"), new String(userId), new String(pollId), new String(pollAnswerId), new String(pollAnswer), new String(pollAnswerSelectedId), new String("1")
                , new Callback<PollParticipateResponseModel>() {
            @Override
            public void success(PollParticipateResponseModel pollParticipateResponseModel, Response response) {
                //If the success is 1 the data is binded into the views
                if (("1").equals(pollParticipateResponseModel.getSuccess())) {
                    //Adding the value in arraylist
                    preferenceSearchPollIdAnswer.add(pollParticipateResponseModel.getResults().getPollId());
                    //Adding the value in arraylist
                    preferenceSearchPollIdAnswerSelected.add(pollParticipateResponseModel.getResults().getPollAnswer());
                    //Increamenting the participate count on success 1
                    int participateCount = Integer.parseInt(searchPollResponseModel.getPollParticipateCount()) + 1;
                    //Setting participate count in the arraylist
                    searchUserPollParticipateCount.set(clickPosition, participateCount);
                    //Saving in prefernce
                    MApplication.saveArray(context, searchUserPollParticipateCount, Constants.SEARCH_POLL_PARTICIPATE_COUNT_ARRAY, Constants.SEARCH_POLL_PARTICIPATE_COUNT_SIZE);
                    //Saving in prefernce
                    MApplication.saveStringArray(context, preferenceSearchPollIdAnswer, Constants.SEARCH_POLL_ID_ANSWER_ARRAY, Constants.SEARCH_POLL_ID_ANSWER_SIZE);
                    //Saving in prefernce
                    MApplication.saveStringArray(context, preferenceSearchPollIdAnswerSelected, Constants.SEARCH_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.SEARCH_POLL_ID_SELECTED_SIZE);
                    //If poll type equals 1
                    if (("1").equals(searchPollResponseModel.getPollType())) {
                        //Setting the radio options as false
                        holder1.radioYesSearchPoll.setClickable(false);
                        //Setting the radio options as false
                        holder1.radioNoSearchPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder1.radioYesSearchPoll.isChecked()) {
                            holder1.radioYesSearchPoll.setTextColor(Color.BLACK);
                        } else {
                            holder1.radioNoSearchPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder1.txtYesOrNoCounts.setText(String.valueOf(searchUserPollParticipateCount.get(clickPosition)));
                        //If poll type equals 2
                    } else if (("2").equals(searchPollResponseModel.getPollType())) {
                        //Setting the clickable as false
                        holder2.radioOptions1MultipleOptionsSearchPoll.setClickable(false);
                        holder2.radioOptions2MultipleOptionsSearchPoll.setClickable(false);
                        holder2.radioOptions3MultipleOptionsSearchPoll.setClickable(false);
                        holder2.radioOptions4MultipleOptionsSearchPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder2.radioOptions1MultipleOptionsSearchPoll.isChecked()) {
                            holder2.radioOptions1MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions2MultipleOptionsSearchPoll.isChecked()) {
                            holder2.radioOptions2MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions3MultipleOptionsSearchPoll.isChecked()) {
                            holder2.radioOptions3MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions4MultipleOptionsSearchPoll.isChecked()) {
                            holder2.radioOptions4MultipleOptionsSearchPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder2.txtCountsMultipleOptionsSearchPoll.setText(String.valueOf(searchUserPollParticipateCount.get(clickPosition)));
                    } else if (("3").equals(searchPollResponseModel.getPollType())) {
                        //Setting the clickable as false
                        holder3.radioOptions1PhotoComparisonSearchPoll.setClickable(false);
                        holder3.radioOptions2PhotoComparisonSearchPoll.setClickable(false);
                        holder3.radioOptions3PhotoComparisonSearchPoll.setClickable(false);
                        holder3.radioOptions4PhotoComparisonSearchPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder3.radioOptions1PhotoComparisonSearchPoll.isEnabled()) {
                            holder3.radioOptions1PhotoComparisonSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions2PhotoComparisonSearchPoll.isEnabled()) {
                            holder3.radioOptions2PhotoComparisonSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions3PhotoComparisonSearchPoll.isEnabled()) {
                            holder3.radioOptions3PhotoComparisonSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions4PhotoComparisonSearchPoll.isEnabled()) {
                            holder3.radioOptions4PhotoComparisonSearchPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder3.txtCountPhotoComparisonSearchPoll.setText(String.valueOf(searchUserPollParticipateCount.get(clickPosition)));
                    } else if (("4").equals(searchPollResponseModel.getPollType())) {
                        holder4.radioOptions1YouTubeUrlSearchPoll.setClickable(false);
                        holder4.radioOptions2YouTubeUrlSearchPoll.setClickable(false);
                        holder4.radioOptions3YouTubeUrlSearchPoll.setClickable(false);
                        holder4.radioOptions4YouTubeUrlSearchPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder4.radioOptions1YouTubeUrlSearchPoll.isChecked()) {
                            holder4.radioOptions1YouTubeUrlSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions2YouTubeUrlSearchPoll.isChecked()) {
                            holder4.radioOptions2YouTubeUrlSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions3YouTubeUrlSearchPoll.isChecked()) {
                            holder4.radioOptions3YouTubeUrlSearchPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions4YouTubeUrlSearchPoll.isChecked()) {
                            holder4.radioOptions4YouTubeUrlSearchPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder4.txtVideoUrlCountYouTubeUrlSearchPoll.setText(String.valueOf(searchUserPollParticipateCount.get(clickPosition)));
                    }
                    //Toast message will display
                    Toast.makeText(context, pollParticipateResponseModel.getMsg(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, pollParticipateResponseModel.getMsg(),
                            Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, context);
            }
        });
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutOne {
        //text view
        private TextView yesOrNoLikeSearchPoll;
        //Radio group
        private RadioGroup radioYesOrNOSearchPoll;
        //Radio button
        private RadioButton radioYesSearchPoll;
        //Radio button
        private RadioButton radioNoSearchPoll;
        //Check box like unlike
        private CheckBox likeUnlikeYesOrNoSearchPoll;
        //text view name
        private TextView txtNameYesOrNoSearchPoll;
        //Text view time
        private TextView txtTimeYesOrNoSearchPoll;
        //Text view category
        private TextView txtCategoryYesOrNoSearchPoll;
        //Image view
        private ImageView imgProfileYesOrNoSearchPoll;
        //SimpleDraweeView view
        private ImageView imageQuestion1YesOrNoSearchPoll;
        //SimpleDraweeView view
        private ImageView imageQuestion2YesOrNoSearchPoll;
        //SimpleDraweeView view
        private ImageView singleOptionYesOrNoSearchPoll;
        //TextView question
        private TextView txtQuestionYesOrNoSearchPoll;
        //Text view comment
        private TextView txtCommentYesOrNoSearchPoll;
        //Textview particcipate count
        private TextView txtYesOrNoCounts;
        //Image view share
        private ImageView imgShareYesOrNoSearchPoll;
    }
    /**
     * A ViewHolderLayoutTwo object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutTwo {
        //text view
        private TextView txtLikeMulipleOptionsSearchPoll;
        //Radio group
        private RadioGroup radioGroupOptionsSearchPoll;
        //Radio button
        private RadioButton radioOptions1MultipleOptionsSearchPoll;
        //Radio button
        private RadioButton radioOptions2MultipleOptionsSearchPoll;
        //Radio button
        private RadioButton radioOptions3MultipleOptionsSearchPoll;
        //Radio button
        private RadioButton radioOptions4MultipleOptionsSearchPoll;
        //Chek box
        private CheckBox likeUnlikeMultipleOptionsSearchPoll;
        //Simple drawer view
        private ImageView imageQuestion1MultipleOptionsSearchPoll;
        //Simple drawer view
        private ImageView imageQuestion2MultipleOptionsSearchPoll;
        //Simple drawer view
        private ImageView singleOptionMultipleOptionsSearchPoll;
        //Text view
        private TextView txtQuestionMultipleOptionsSearchPoll;
        //Text view
        private TextView txtCommentsMultipleOptionsSearchPoll;
        //Text view
        private TextView txtCountsMultipleOptionsSearchPoll;
        //Text view
        private TextView txtNameMultipleOptionsSearchPoll;
        //Text view
        private TextView txtTimeMultipleOptionsSearchPoll;
        //Text view
        private TextView txtCategoryMultipleOptionsSearchPoll;
        //ImageView
        private ImageView imgProfileMultipleOptionsSearchPoll;
        //ImageView
        private ImageView imgShareMultipleOptionsSearchPoll;
    }
    /**
     * A ViewHolderLayoutThree object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutThree {
        //Simple drawer view
        private ImageView imageAnswer1PhotoComparisonSearchPoll;
        //Simple drawer view
        private ImageView imageAnswer2PhotoComparisonSearchPoll;
        //Simple drawer view
        private ImageView imageAnswer3PhotoComparisonSearchPoll;
        //Simple drawer view
        private ImageView imageAnswer4PhotoComparisonSearchPoll;
        //Relative layout
        private RelativeLayout relativeAnswer3PhotoComparisonSearchPoll;
        //Relative layout
        private RelativeLayout relativeAnswer4PhotoComparisonSearchPoll;
        //Checkbox
        private CheckBox likeUnlikePhotoComparisonSearchPoll;
        //Text view
        private TextView txtLikePhotoComparisonSearchPoll;
        //Radio button
        private RadioButton radioOptions1PhotoComparisonSearchPoll;
        //Radio button
        private RadioButton radioOptions2PhotoComparisonSearchPoll;
        //Radio button
        private RadioButton radioOptions3PhotoComparisonSearchPoll;
        //Radio button
        private RadioButton radioOptions4PhotoComparisonSearchPoll;
        //Imageview
        private ImageView imageQuestion1PhotoComparisonSearchPoll;
        //Imageview
        private ImageView imageQuestion2PhotoComparisonSearchPoll;
        //Imageview
        private ImageView singleOptionPhotoComparisonSearchPoll;
        // Textview
        private TextView txtQuestionPhotoComparisonSearchPoll;
        // Textview
        private TextView txtCommentsPhotoComparisonSearchPoll;
        // Textview
        private TextView txtCountPhotoComparisonSearchPoll;
        // Textview
        private TextView txtNamePhotoComparisonSearchPoll;
        // Textview
        private TextView txtTimePhotoComparisonSearchPoll;
        // Textview
        private TextView txtCategoryPhotoComparisonSearchPoll;
        //Imageview
        private ImageView imgProfilePhotoComparisonSearchPoll;
        //Imageview
        private ImageView imgSharePhotoComparisonSearchPoll;
    }
    /**
     * A ViewHolderLayoutFour object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutFour {
        //Text view
        private TextView txtLikeYouTubeUrlSearchPoll;
        //RadioGroup
        private RadioGroup radioGroupOptionsYouTubeUrlSearchPoll;
        //RadioButton
        private RadioButton radioOptions1YouTubeUrlSearchPoll;
        //RadioButton
        private RadioButton radioOptions2YouTubeUrlSearchPoll;
        //RadioButton
        private RadioButton radioOptions3YouTubeUrlSearchPoll;
        //RadioButton
        private RadioButton radioOptions4YouTubeUrlSearchPoll;
        //Simple drawer view
        private ImageView imageQuestion1YouTubeUrlSearchPoll;
        //Simple drawer view
        private ImageView imageQuestion2YouTubeUrlSearchPoll;
        //Simple drawer view
        private ImageView singleOptionYouTubeUrlSearchPoll;
        //Checkbox
        private CheckBox likeUnlikeYouTubeUrlSearchPoll;
        //TextView
        private TextView txtQuestionYouTubeUrlSearchPoll;
        //TextView
        private TextView txtCommentsYouTubeUrlSearchPoll;
        //TextView
        private TextView txtVideoUrlCountYouTubeUrlSearchPoll;
        //TextView
        private TextView txtNameYouTubeUrlSearchPoll;
        //TextView
        private TextView txtTimeYouTubeUrlSearchPoll;
        //TextView
        private TextView txtCategoryYouTubeUrlSearchPoll;
        //TextView
        private ImageView imgProfileYouTubeUrlSearchPoll;
        //ImageView
        private ImageView imgShareYouTubeUrlSearchPoll;
    }


}


