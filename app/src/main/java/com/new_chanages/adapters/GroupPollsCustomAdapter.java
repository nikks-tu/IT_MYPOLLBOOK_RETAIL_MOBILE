package com.new_chanages.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import com.contus.activity.CustomRequest;
import com.contus.app.Constants;
import com.contus.comments.PollComments;
import com.contus.likes.PollLikes;
import com.contus.responsemodel.LikeUnLikeResposneModel;
import com.contus.responsemodel.PollParticipateResponseModel;
import com.contus.responsemodel.UserPollResponseModel;
import com.contus.restclient.LikesAndUnLikeRestClient;
import com.contus.restclient.PollParticipateRestClient;
import com.contus.restclient.UserPollRestClient;
import com.contus.userpolls.FullImageView;
import com.contus.userpolls.UserPollReview;
import com.contus.views.EndLessListView;
import com.contus.views.VideoLandscapeActivity;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.new_chanages.models.GetsharedURL_InputModel;
import com.new_chanages.models.GroupPollDataObject;
import com.polls.polls.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 10/6/2015.
 */
public class GroupPollsCustomAdapter extends ArrayAdapter<GroupPollDataObject> implements CompoundButton
        .OnCheckedChangeListener {

    //Layout
    private final int userPollLayoutId;

    //Check
    String chk ="";

    //user id
    private final String userId;

    //Activity
    private final Activity userPollActivity;

    private final EndLessListView list;

    //Response from the server
    private GroupPollDataObject userPollResponse;

    //Youtube url
    private String youTubeUrl;

    //Likes
    private int mlikes;

    //Holder class for view1
    private ViewHolderLayoutOne holderUserView1;

    //Holder class for view2
    private ViewHolderLayoutTwo holderUserView2;

    //Holder class for view3
    private ViewHolderLayoutThree holderUserView3;

    //Holder class for view4
    private ViewHolderLayoutFour holderUserView4;

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
    private ArrayList<Integer> prefrenceUserPollLikeCount = new ArrayList<Integer>();

    //array list for saving the likes counts
    private ArrayList<Integer> userPollLikeCount = new ArrayList<Integer>();

    //array list for saving the likes user
    private ArrayList<Integer> userPollLikesUser = new ArrayList<Integer>();

    //like user arrayList
    private ArrayList<Integer> preferenceUserPollLikeUser;

    //array list COMMENTS count
    private ArrayList<Integer> userPollcommentsCount = new ArrayList<Integer>();

    //prefernce array list COMMENTS count
    private ArrayList<Integer> prefrenceUserPollCommentsCount;

    //Poll type
    private String pollType;

    //Prefernce user poll id answer
    private ArrayList<String> preferenceUserPollIdAnswer;

    //Prefernce user poll id answer selected
    private ArrayList<String> preferenceUserPollIdAnswerSelected;

    //Poll id answer selected
    private ArrayList<String> userPollIdAnwserCheck = new ArrayList<String>();

    //poll id answer check
    private ArrayList<String> userPollIdAnwser = new ArrayList<String>();

    //Poll answer
    private String pollAnswer;

    //Poll answer selected id
    private String pollAnswerSelectedId;

    //Participate count
    private ArrayList<Integer> userPollParticipateCount = new ArrayList<Integer>();

    //Preference participate count
    private ArrayList<Integer> preferenceUserPollParticipateCount;

    //Image
    private String image;

    //view
    private View mMyPollsView;


    int clickPosition;

    /**
     * OnClick listner for question question image1
     */
    private View.OnClickListener mQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the list
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            clickPosition = position;
            //Response from  the server based on the position
            userPollResponse = getItem(clickPosition);
            //Question image
            pollImage1 = userPollResponse.getUserPollsQuestion().getPollQimage().replaceAll(" ", "%20");
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, pollImage1);
        }
    };

    /**
     * OnClick listner for question comment
     */
    private View.OnClickListener mCommentClickAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the list
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            clickPosition = position;
            //This method will be called wto show the comment
            commentClickAction(clickPosition);
        }
    };

    /**
     * OnClick listner for question like unlike check box
     */
    private View.OnClickListener mLikeUnlikeCheckBox = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            userPollResponse = getItem(clickPosition);
            //Moving to the like activity
            likeClickAction(userPollResponse);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mShareClickAction = new View.OnClickListener() {
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
    private View.OnClickListener mParticipateCounts = new View.OnClickListener() {
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
     * OnClick listner for question image2
     */
    private View.OnClickListener mQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            userPollResponse = getItem(clickPosition);
            //Question image
            pollImage2 = userPollResponse.getUserPollsQuestion().getPollQimage().replaceAll(" ", "%20");
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, pollImage2);
        }
    };

    /**
     * OnClick listner for question single image
     */

    private View.OnClickListener mSingleOption = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the list
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            clickPosition = position;
            //Response from  the server based on the position
            userPollResponse = getItem(clickPosition);
            //Question image
            if (("").equals(userPollResponse.getUserPollsQuestion().getPollsQimage2())) {
                image = userPollResponse.getUserPollsQuestion().getPollsQimage2().replaceAll(" ", "%20");
            } else {
                image = userPollResponse.getUserPollsQuestion().getPollsQimage2().replaceAll(" ", "%20");
            }
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, image);
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
    public GroupPollsCustomAdapter(Activity activity, List<GroupPollDataObject> dataResults, int
            layoutId, String userId, EndLessListView list) {
        super(activity, 0, dataResults);

        this.userPollLayoutId = layoutId;
        this.userPollActivity = activity;
        this.userId = userId;
        this.list = list;

    }

    /**
     * Image full view method
     *
     * @param clickPosition-position
     * @param pollImageMyPoll-image  url
     */
    private void imageQuestionClickAction(int clickPosition, String pollImageMyPoll) {

        boolean isAdmin = false;
        //response from the server
        userPollResponse = getItem(clickPosition);
        //Setting the time in preference
        MApplication.setString(userPollActivity, Constants.DATE_UPDATED, MApplication
                .getTimeDifference(userPollResponse.getUserPollsQuestion().getCreatedAt()));
        //Setting the name in preference
        MApplication.setString(userPollActivity, Constants.CAMPAIGN_NAME, userPollResponse.getUserPollsQuestion().getUserInfos().getName());
        //Setting the category in preference
        MApplication.setString(userPollActivity, Constants.CAMPAIGN_CATEGORY, userPollResponse.getPollcategories()
                .getCategories().getCategoryName());
        //Setting the logo in preference
        Log.i("ImageCLick", "imageQuestionClickAction: " + userPollResponse.getUserPollsQuestion().getUserInfos().getImage()
        );
        MApplication.setString(userPollActivity, Constants.CAMPAIGN_LOGO, userPollResponse.getUserPollsQuestion().getUserInfos()
                                                                                          .getImage());
        //Moving from one activity to another activity
        if(userPollResponse.getPollcategories().getCategories().getCategoryName()!=null)
        {
            if (userPollResponse.getPollcategories().getCategories().getCategoryName().equalsIgnoreCase("Admin")) {
                isAdmin = true;
            }
        }
        Intent a = new Intent(userPollActivity, FullImageView.class);
        //Passing the value from one activity to another
        a.putExtra(Constants.QUESTION1, pollImageMyPoll);
        a.putExtra("is_admin", isAdmin);
        //Starting the activity
        userPollActivity.startActivity(a);
    }

    @Override
    public View getView(final int position, View mView, ViewGroup parent) {

        mMyPollsView = mView;
        //Geeting the json response based on the position
        userPollResponse = getItem(position);
        //poll type from the response
        if(userPollResponse!=null) {
            idRefrenceView = userPollResponse.getUserPollsQuestion().getPollType();
            //Poll question from the response
            pollQuestion = userPollResponse.getUserPollsQuestion().getPollQuestion();
            //image from the response
            if(pollImage1!=null)
            pollImage1 = userPollResponse.getUserPollsQuestion().getPollQimage().replaceAll(" ", "%20");
            //Image question from the response
            if(pollImage2!=null)
            pollImage2 = userPollResponse.getUserPollsQuestion().getPollsQimage2().replaceAll(" ", "%20");
            //poll answer form the response
            if(pollAnswer1!=null)
            pollAnswer1 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
            //poll answer form the response
            if(pollAnswer2!=null)
            pollAnswer2 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2();
            //poll answer form the response
            if(pollAnswer3!=null)
            pollAnswer3 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3();
            //poll answer form the response
            if(pollAnswer4!=null)
            pollAnswer4 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4();
            //youtube url from the response
           // youTubeUrl = userPollResponse.getUserPollsQuestion().get;
            //Setting in preference
            MApplication.setString(userPollActivity, Constants.YOUTUBE_URL, youTubeUrl);
        }
        //If the value matches the the layout one view is binded.
        if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_ONE) {
            /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(userPollActivity).inflate(R.layout.userpoll_firstview, null);
            //view holder class
            holderUserView1 = new ViewHolderLayoutOne();
            holderUserView1.radioYesOrNoUserPoll =  mMyPollsView.findViewById(R.id.YesOrNO);
            holderUserView1.categoryImageViewFirst =  mMyPollsView.findViewById(R.id.imgCategory);
            holderUserView1.radioYesUserPoll =  mMyPollsView.findViewById(R.id.radioYes);
            holderUserView1.radioNoUserPoll =  mMyPollsView.findViewById(R.id.radioNo);
            holderUserView1.likeUnlikeYesOrNoPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderUserView1.txtLikeYesOrNoUserPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderUserView1.imageQuestion1YesOrNoPoll =  mMyPollsView.findViewById(R.id.choose);
            holderUserView1.imageQuestion2YesOrNoPoll =  mMyPollsView.findViewById(R.id.ChooseAdditional);
            holderUserView1.singleOptionYesOrNoPoll =  mMyPollsView.findViewById(R.id.singleOption);
            holderUserView1.txtQuestionYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderUserView1.txtCommentYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtCommentsCounts);
            holderUserView1.txtCountsYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtParticcipation);
            holderUserView1.txtNameYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtName);
            holderUserView1.txtTimeYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderUserView1.txtCategoryYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderUserView1.imgShareYesOrNoUserPoll =  mMyPollsView.findViewById(R.id.imgShare);
            holderUserView1.imgShareYesOrNoUserPoll.setVisibility(View.GONE);
            holderUserView1.imgProfileYesOrNoPoll =  mMyPollsView.findViewById(R.id.imgProfile);

            //Binding the data into the views in android
            validateViewLayoutOne(holderUserView1, position);
            //If the value matches the the layout one view is bind.
        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_TWO) {
            /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(userPollActivity).inflate(R.layout.userpoll_secondview, null);
            //view holder class
            holderUserView2 = new ViewHolderLayoutTwo();
            holderUserView2.radioGroupOptionsMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.participate_options);
            holderUserView2.radioOptions1MultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.option1);
            holderUserView2.radioOptions2MultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.option2);
            holderUserView2.radioOptions3MultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.option3);
            holderUserView2.radioOptions4MultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.option4);
            holderUserView2.likeUnlikeMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderUserView2.txtLikeMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderUserView2.txtNameMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.txtName);
            holderUserView2.txtTimeMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderUserView2.txtCategoryMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderUserView2.imgProfileMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.imgProfile);
            holderUserView2.categoryImageViewSecond =  mMyPollsView.findViewById(R.id.imgCategory);
            holderUserView2.txtQuestionMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderUserView2.txtCommentsMultipleOptionsUserPoll =  mMyPollsView
                    .findViewById(R.id.txtCommentsCounts);
            holderUserView2.txtCountsMultipleOptionsUserPoll =  mMyPollsView
                    .findViewById(R.id.txtParticcipation);
            holderUserView2.imageQuestion1MultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.choose);
            holderUserView2.imageQuestion2MultipleOptionsUserPoll =  mMyPollsView
                    .findViewById(R.id.ChooseAdditional);
            holderUserView2.singleOptionMultipleOptionsUserPoll =  mMyPollsView
                    .findViewById(R.id.singleOption);
            holderUserView2.imgShareMultipleOptionsUserPoll =  mMyPollsView.findViewById(R.id.imgShare);
            //Binding the data into the views in android
            holderUserView2.imgShareMultipleOptionsUserPoll.setVisibility(View.GONE);
            validateViewLayoutTwo(holderUserView2, position);
        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_THREE) {
             /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(userPollActivity).inflate(R.layout.userpoll_thirdview, null);
            //view holder class
            holderUserView3 = new ViewHolderLayoutThree();
            holderUserView3.radioOptions1PhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.option1);
            holderUserView3.radioOptions2PhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.option2);
            holderUserView3.radioOptions3PhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.option3);
            holderUserView3.radioOptions4PhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.option4);
            holderUserView3.likeUnlikePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderUserView3.txtLikePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderUserView3.txtQuestionPhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderUserView3.imageAnswer1PhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.answer1);
            holderUserView3.imageAnswer2PhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.answer2);
            holderUserView3.imageAnswer3PhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.answer3);
            holderUserView3.imageAnswer4PhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.answer4);
            holderUserView3.relativeAnswer3PhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.ThirdOptionOption);
            holderUserView3.relativeAnswer4PhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.FourthOptionOption);
            holderUserView3.likeUnlikePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderUserView3.txtLikePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderUserView3.txtCommentsPhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.txtCommentsCounts);
            holderUserView3.txtCountPhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.txtParticcipation);
            holderUserView3.imageQuestion1PhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.choose);
            holderUserView3.imageQuestion2PhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.ChooseAdditional);
            holderUserView3.singleOptionPhotoComparisonUserPoll =  mMyPollsView
                    .findViewById(R.id.singleOption);
            holderUserView3.txtNamePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.txtName);
            holderUserView3.txtTimePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderUserView3.txtCategoryPhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderUserView3.imgProfilePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.imgProfile);
            holderUserView3.imgSharePhotoComparisonUserPoll =  mMyPollsView.findViewById(R.id.imgShare);
            holderUserView3.categoryImageViewThird =  mMyPollsView.findViewById(R.id.imgCategory);
            holderUserView3.imgSharePhotoComparisonUserPoll.setVisibility(View.GONE);
            //Binding the data into the views in android
            validateViewLayoutThree(holderUserView3, position);

        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_FOUR) {
            /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(userPollActivity).inflate(R.layout.userpoll_fourthview, null);
            //view holder class
            holderUserView4 = new ViewHolderLayoutFour();
            holderUserView4 = new ViewHolderLayoutFour();
            holderUserView4.radioGroupOptionsYouTubeUrlUserPoll =  mMyPollsView
                    .findViewById(R.id.participate_options);
            holderUserView4.radioOptions1YouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.option1);
            holderUserView4.radioOptions2YouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.option2);
            holderUserView4.radioOptions3YouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.option3);
            holderUserView4.radioOptions4YouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.option4);
            holderUserView4.likeUnlikeYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderUserView4.txtLikeYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderUserView4.txtQuestionYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderUserView4.txtCommentsYouTubeUrlUserPoll =  mMyPollsView
                    .findViewById(R.id.txtCommentsCounts);
            holderUserView4.txtVideoUrlCountYouTubeUrlUserPoll =  mMyPollsView
                    .findViewById(R.id.txtParticcipation);
            holderUserView4.imageQuestion1YouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.choose);
            holderUserView4.imageQuestion2YouTubeUrlUserPoll =  mMyPollsView
                    .findViewById(R.id.ChooseAdditional);
            holderUserView4.singleOptionYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.singleOption);
            holderUserView4.txtNameYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.txtName);
            holderUserView4.txtTimeYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderUserView4.txtCategoryYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderUserView4.imgProfileYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.imgProfile);
            holderUserView4.categoryImageViewFourth =  mMyPollsView.findViewById(R.id.imgCategory);
            holderUserView4.imgShareYouTubeUrlUserPoll =  mMyPollsView.findViewById(R.id.imgShare);

            holderUserView4.imgShareYouTubeUrlUserPoll.setVisibility(View.GONE);
            //Binding the data into the views in android
            validateViewLayoutFour(holderUserView4, position);
        }
        return mMyPollsView;
    }

    /**
     * Clicking the participate icon.
     *
     * @param clickPosition
     */
    private void clickAction(int clickPosition) {

        preferenceUserPollIdAnswer = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY,
                        Constants.USER_POLL_ID_ANSWER_SIZE);
        //Response from the server when the particular cell is clicked.Based on the click position data is retrived
        // from the response.
        userPollResponse = getItem(clickPosition);
        //Polltype from the response
        pollType = userPollResponse.getUserPollsQuestion().getPollType();
        //Poll id from the response
        String pollId = String.valueOf(userPollResponse.getPollId());
        if (!preferenceUserPollIdAnswer.isEmpty()) {
            if (preferenceUserPollIdAnswer.contains(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()))) {
                //Load the participate count from  the saved prefernce
                preferenceUserPollParticipateCount = MApplication
                        .loadArray(userPollActivity, userPollParticipateCount, Constants
                                .USER_POLL_PARTICIPATE_COUNT_ARRAY, Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                //Setting the time in preference
                MApplication.setString(userPollActivity, Constants.DATE_UPDATED, MApplication
                        .getTimeDifference(userPollResponse.getUserPollsQuestion().getCreatedAt()));
                //Setting the username in preference
                MApplication.setString(userPollActivity, Constants.CAMPAIGN_NAME, userPollResponse.getUserPollsQuestion().getUserInfos().getName());
                //Setting the category name in preference
                MApplication.setString(userPollActivity, Constants.CAMPAIGN_CATEGORY, userPollResponse.getPollcategories()
                        .getCategories().getCategoryName());
                //Setting the userprofile in preference
                MApplication.setString(userPollActivity, Constants.CAMPAIGN_LOGO, userPollResponse.getUserPollsQuestion().getUserInfos()
                                                                                                  .getImage());
                //It can be used with startActivity to launch an Activity.
                Intent a = new Intent(userPollActivity, UserPollReview.class);
                //Pushing the values from one activity to another activity
                a.putExtra(Constants.POLL_TYPE, pollType);
                a.putExtra(Constants.POLL_ID, pollId);
                a.putExtra(Constants.TYPE, Constants.POLLS);
                a.putExtra(Constants.ARRAY_POSITION, clickPosition);
                a.putExtra(Constants.PARTICIPATE_COUNT, String
                        .valueOf(preferenceUserPollParticipateCount.get(clickPosition)));
                //Starting the activity
                userPollActivity.startActivity(a);
            } else {
                Toast.makeText(userPollActivity, "You must attend the poll to view the results",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(userPollActivity, "You must attend the poll to view the results",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Layout view 1
     * @param holder-           view holders
     * @param position-position is used when reusing the views
     */
    private void validateViewLayoutOne(final ViewHolderLayoutOne holder, final int position) {
        prefrenceUserPollLikeCount = MApplication
                .loadArray(userPollActivity, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants
                        .USER_POLL_LIKES_COUNT_SIZE);   //Load the like count from  the saved preference
        prefrenceUserPollCommentsCount = MApplication
                .loadArray(userPollActivity, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants
                        .USER_POLL_COMMENTS_SIZE);//Load the COMMENTS count from  the saved preference
        //Load the like user from  the saved preference
        preferenceUserPollLikeUser = MApplication
                .loadArray(userPollActivity, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants
                        .USER_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved preference
        preferenceUserPollIdAnswer = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY,
                        Constants.USER_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved preference
        preferenceUserPollIdAnswerSelected = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY,
                        Constants.USER_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved preference
        preferenceUserPollParticipateCount = MApplication
                .loadArray(userPollActivity, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY,
                        Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it wont be checked
        if (preferenceUserPollIdAnswer.contains(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()))) {
            //Getting the position of the particular value saved in preference
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()));
            //Selected answer from the position
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option will be checked ,text color will be black and clickable will
            // be false.
            if (value.equals("Yes")) {
                holder.radioYesUserPoll.setChecked(true); //Setting the radio options true
                holder.radioYesUserPoll.setTextColor(Color.BLACK);//Setting the text color black
            } else {
                holder.radioNoUserPoll.setTextColor(Color.BLACK);  //Setting the text color black
                holder.radioNoUserPoll.setChecked(true);//setChecked true
            }
            holder.radioYesUserPoll.setClickable(false);//Setting clickable false
            holder.radioNoUserPoll.setClickable(false);//Setting clickable false
        }
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYesOrNoPoll.setChecked(true);
        } else {
            holder.likeUnlikeYesOrNoPoll.setChecked(false);
        }
        //question images is empty then the visibility of the view will be gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            holder.imageQuestion1YesOrNoPoll.setVisibility(View.GONE);
            holder.imageQuestion2YesOrNoPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                //Loading into the glide
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage1, holder.imageQuestion1YesOrNoPoll,
                        R.drawable.placeholder_image);
                //Loading into the glide
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage2, holder.imageQuestion2YesOrNoPoll,
                        R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                //view visible
                holder.singleOptionYesOrNoPoll.setVisibility(View.VISIBLE);
                ////Loading using the glide into the imageview
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage1
                        .replaceAll(" ", "%20"), holder.singleOptionYesOrNoPoll, R.drawable.placeholder_image);
                //visibility gone
                holder.imageQuestion1YesOrNoPoll.setVisibility(View.GONE);
                //visibility gone
                holder.imageQuestion2YesOrNoPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                //Visibility visible
                holder.singleOptionYesOrNoPoll.setVisibility(View.VISIBLE);
                ////Loading using the gilde into the imageview
                if(pollImage2!=null)
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage2
                        .replaceAll(" ", "%20"), holder.singleOptionYesOrNoPoll, R.drawable.placeholder_image);
                //Visibility gone
                holder.imageQuestion1YesOrNoPoll.setVisibility(View.GONE);
                //Visibility gone
                holder.imageQuestion2YesOrNoPoll.setVisibility(View.GONE);
            } else {
                //Visibility gone
                holder.imageQuestion1YesOrNoPoll.setVisibility(View.GONE);
                //Visibility gone
                holder.imageQuestion2YesOrNoPoll.setVisibility(View.GONE);
            }
        }
        //setting the poll question in text view
        holder.txtQuestionYesOrNoPoll.setText(pollQuestion);
        //Setting the like count based on the position in the text view
        holder.txtLikeYesOrNoUserPoll.setText(String.valueOf(prefrenceUserPollLikeCount.get(position)));
        //Setting the answer1 in ratio options
        holder.radioYesUserPoll.setText(pollAnswer1);
        //Setting the answer12in ratio options
        holder.radioNoUserPoll.setText(pollAnswer2);
        //Getting the user name from the response and binding the data into the text view
        try {
            holder.txtNameYesOrNoPoll.setText(MApplication.getDecodedString(userPollResponse.getUserPollsQuestion().getUserInfos().getName()));
        }catch (Exception e){

        }
        //Getting the time from the response and binding the data into the text view
        holder.txtTimeYesOrNoPoll.setText(MApplication.getTimeDifference(userPollResponse.getUserPollsQuestion().getCreatedAt()));
        //Getting the category name from the response and binding the data into the text view
        holder.txtCategoryYesOrNoPoll.setText(userPollResponse.getPollcategories().getCategories().getCategoryName());
        //Getting the category name from the response and binding the data into the text view

        if (userPollResponse.getUserPollsQuestion().getUserInfos().getImage()!=null) {
            Utils.loadImageWithGlide(userPollActivity, userPollResponse.getUserPollsQuestion().getUserInfos().getImage()
                                                                       .replaceAll(" ", "%20"), holder
                    .imgProfileYesOrNoPoll, R.drawable.img_ic_user);
        } else {
            holder.txtCategoryYesOrNoPoll.setVisibility(View.INVISIBLE);
            holder.categoryImageViewFirst.setVisibility(View.INVISIBLE);
            holder.imgProfileYesOrNoPoll.setImageResource(R.drawable.ic_logo_icon);//
        }
        //Getting the COMMENTS name from the preference and binding the data into the text view
        holder.txtCommentYesOrNoPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));
        //Setting the like count based on the position in the text view
        holder.txtCountsYesOrNoPoll.setText(String.valueOf(preferenceUserPollParticipateCount.get(position)));
        //holderViewMethod
        holder1ViewsOnClickMethod(holder, position);
    }

    /**
     * OnClick click listeners for holderview1
     *
     * @param holder
     * @param position
     */
    @SuppressLint("ClickableViewAccessibility")
    private void holder1ViewsOnClickMethod(final ViewHolderLayoutOne holder, final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountsYesOrNoPoll.setOnClickListener(mParticipateCounts);
        holder.imgShareYesOrNoUserPoll.setOnClickListener(mShareClickAction);
        holder.txtLikeYesOrNoUserPoll.setOnClickListener(mLikeUnlikeCheckBox);
        holder.txtCommentYesOrNoPoll.setOnClickListener(mCommentClickAction);
        holder.imageQuestion1YesOrNoPoll.setOnClickListener(mQuestionImage1);
        holder.imageQuestion2YesOrNoPoll.setOnClickListener(mQuestionImage2);
        holder.singleOptionYesOrNoPoll.setOnClickListener(mSingleOption);
        holder.radioNoUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    //poll answer selected id is set as 2
                    pollAnswerSelectedId = "2";
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, holderUserView1, null, null, null);
                }
                return false;
            }
        });
        holder.radioYesUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, holderUserView1, null, null, null);
                }
                return false;
            }
        });

        // Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYesOrNoPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked
                // as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYesOrNoPoll.setChecked(true);
                    //Nikita
                 //   Toast.makeText(getContext(), userPollActivity.getString(R.string.rewards_add_toast), Toast.LENGTH_SHORT).show();
                    likesUnLikes(position, holder, null, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYesOrNoPoll.setChecked(false);
                    likesUnLikes(position, holder, null, null, null);
                }
            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holderUserView1.radioYesOrNoUserPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition); //Getting the item from the response
                if (checkedId == R.id.radioYes) {   //If the id matches the below functionality will take place
                    pollAnswerSelectedId = "1";     //poll answer selected id is set as 1
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, holderUserView1, null, null, null);

                    holderUserView1.radioNoUserPoll.setChecked(false);

                } else if (checkedId == R.id.radioNo) {
                    //poll answer selected id is set as 2
                    pollAnswerSelectedId = "2";
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, holderUserView1, null, null, null);

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
        prefrenceUserPollLikeCount = MApplication
                .loadArray(userPollActivity, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants
                        .USER_POLL_LIKES_COUNT_SIZE); //Load the like count from  the saved prefernce
        prefrenceUserPollCommentsCount = MApplication.loadArray(userPollActivity, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants
                        .USER_POLL_COMMENTS_SIZE); //Load the COMMENTS count from  the saved prefernce
        preferenceUserPollLikeUser = MApplication
                .loadArray(userPollActivity, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants
                        .USER_POLL_LIKES_USER_SIZE); //Load the like user from  the saved prefernce
        preferenceUserPollIdAnswer = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY,
                        Constants.USER_POLL_ID_ANSWER_SIZE);//Load the poll id from  the saved prefernce
        preferenceUserPollIdAnswerSelected = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY,
                        Constants.USER_POLL_ID_SELECTED_SIZE); //Load the answer from  the saved prefernce
        preferenceUserPollParticipateCount = MApplication
                .loadArray(userPollActivity, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY,
                        Constants.USER_POLL_PARTICIPATE_COUNT_SIZE); //Load the participate count  the saved prefernce
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeMultipleOptionsUserPoll.setChecked(true);
        } else {
            holder.likeUnlikeMultipleOptionsUserPoll.setChecked(false);
        }
        if (("").equals(pollImage1) && ("")
                .equals(pollImage2)) {   //question images is empty then the visiblity o fthe view will be gone
            holder.imageQuestion1MultipleOptionsUserPoll.setVisibility(View.GONE);
            holder.imageQuestion2MultipleOptionsUserPoll.setVisibility(View.GONE);
        } else {
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                //question images is not empty then the url is binded into the image view
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage1, holder
                        .imageQuestion1MultipleOptionsUserPoll, R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage2, holder
                        .imageQuestion2MultipleOptionsUserPoll, R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                holder.singleOptionMultipleOptionsUserPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage1, holder
                        .singleOptionMultipleOptionsUserPoll, R.drawable.placeholder_image);
                holder.imageQuestion1MultipleOptionsUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsUserPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.singleOptionMultipleOptionsUserPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage2, holder
                        .singleOptionMultipleOptionsUserPoll, R.drawable.placeholder_image);
                holder.imageQuestion1MultipleOptionsUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsUserPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1MultipleOptionsUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsUserPoll.setVisibility(View.GONE);
            }
        }
        if (pollAnswer1.equals("")) { //If the poll answer option is empty then the view will invisible
            holder.radioOptions1MultipleOptionsUserPoll.setVisibility(View.GONE);
        } else
            {//If the poll answer option is not empty then the view will invisible
            holder.radioOptions1MultipleOptionsUserPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1MultipleOptionsUserPoll.setText(pollAnswer1);
        }
        if (pollAnswer2.equals("")) {//If the poll answer option is empty then the view will invisible
            holder.radioOptions2MultipleOptionsUserPoll.setVisibility(View.GONE);
        } else  { //If the poll answer option is not empty then the view will invisible
            holder.radioOptions2MultipleOptionsUserPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2MultipleOptionsUserPoll.setText(pollAnswer2);
        }
        if (pollAnswer3.equals("")) { //If the poll answer option is empty then the view will invisible
            holder.radioOptions3MultipleOptionsUserPoll.setVisibility(View.GONE);
            holder.radioOptions4MultipleOptionsUserPoll.setVisibility(View.GONE);
        } else  {  //If the poll answer option is not empty then the view will invisible
            holder.radioOptions3MultipleOptionsUserPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3MultipleOptionsUserPoll.setText(pollAnswer3);
            if (pollAnswer4.equals("")) {   //If the poll answer option is empty then the view will invisible
                holder.radioOptions4MultipleOptionsUserPoll.setVisibility(View.GONE);
            } else { //If the poll answer option is not empty then the view will invisible
                holder.radioOptions4MultipleOptionsUserPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4MultipleOptionsUserPoll.setText(pollAnswer4);
            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceUserPollIdAnswer.contains(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()))) {
            //Getting the position of the particular value saved in preference
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()));
            //Selected answer from the position
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will
            // be false.
            if (holder.radioOptions1MultipleOptionsUserPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions1MultipleOptionsUserPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions1MultipleOptionsUserPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions2MultipleOptionsUserPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions2MultipleOptionsUserPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions2MultipleOptionsUserPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions3MultipleOptionsUserPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions3MultipleOptionsUserPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions3MultipleOptionsUserPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions4MultipleOptionsUserPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions4MultipleOptionsUserPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions4MultipleOptionsUserPoll.setTextColor(Color.BLACK);
            }
            //Setting the clickable false
            holder.radioOptions1MultipleOptionsUserPoll.setClickable(false);
            holder.radioOptions2MultipleOptionsUserPoll.setClickable(false);
            holder.radioOptions3MultipleOptionsUserPoll.setClickable(false);
            holder.radioOptions4MultipleOptionsUserPoll.setClickable(false);
        }
        //setting the poll question in text view
        holder.txtCommentsMultipleOptionsUserPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));
        //Setting the like count based on the position in the text view
        holder.txtLikeMultipleOptionsUserPoll.setText(String.valueOf(prefrenceUserPollLikeCount.get(position)));
        //Setting the user name in the textviw
        holder.txtNameMultipleOptionsUserPoll
                .setText(MApplication.getDecodedString(userPollResponse.getUserPollsQuestion().getUserInfos().getName()));
        //Setting the updating to=ime in text view
        holder.txtTimeMultipleOptionsUserPoll.setText(MApplication.getTimeDifference(userPollResponse.getUserPollsQuestion().getCreatedAt()));
        //Setting the poll question in text view
        holder.txtQuestionMultipleOptionsUserPoll.setText(pollQuestion);
        //Setting the category intext view
        holder.txtCategoryMultipleOptionsUserPoll.setText(userPollResponse.getPollcategories().getCategories().getCategoryName());
        //Setting the user profile image
        if (userPollResponse.getUserPollsQuestion().getUserInfos().getImage()!=null) {
            Utils.loadImageWithGlide(userPollActivity, userPollResponse.getUserPollsQuestion().getUserInfos().getImage()
                                                                       .replaceAll(" ", "%20"), holder
                    .imgProfileMultipleOptionsUserPoll, R.drawable.img_ic_user);
        } else {
            holder.txtCategoryMultipleOptionsUserPoll.setVisibility(View.INVISIBLE);
            holder.categoryImageViewSecond.setVisibility(View.INVISIBLE);
            holder.imgProfileMultipleOptionsUserPoll.setImageResource(R.drawable.ic_logo_icon);//
        }
        //Setting the text participate count from the array list based on the position
        holder.txtCountsMultipleOptionsUserPoll
                .setText(String.valueOf(preferenceUserPollParticipateCount.get(position)));
        //holder view2
        holder2ViewOnClickListner(holder, position);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void holder2ViewOnClickListner(final ViewHolderLayoutTwo holder, final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountsMultipleOptionsUserPoll.setOnClickListener(mParticipateCounts);
        holder.imgShareMultipleOptionsUserPoll.setOnClickListener(mShareClickAction);
        holder.txtLikeMultipleOptionsUserPoll.setOnClickListener(mLikeUnlikeCheckBox);
        holder.txtCommentsMultipleOptionsUserPoll.setOnClickListener(mCommentClickAction);
        holder.imageQuestion1MultipleOptionsUserPoll.setOnClickListener(mQuestionImage1);
        holder.imageQuestion2MultipleOptionsUserPoll.setOnClickListener(mQuestionImage2);
        holder.singleOptionMultipleOptionsUserPoll.setOnClickListener(mSingleOption);

        holder.radioOptions1MultipleOptionsUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                 clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    //poll answer selected id is set as 2
                    pollAnswerSelectedId = "1";
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, holder, null, null);
                }
                return false;
            }
        });

        holder.radioOptions2MultipleOptionsUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    //poll answer selected id is set as 2
                    pollAnswerSelectedId = "2";
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, holder, null, null);
                }
                return false;
            }
        });
        holder.radioOptions3MultipleOptionsUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                 clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    pollAnswerSelectedId = "3";     //poll answer selected id is set as 1
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, holder, null, null);
                }
                return false;
            }
        });
        holder.radioOptions4MultipleOptionsUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    pollAnswerSelectedId = "4";     //poll answer selected id is set as 1
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, holder, null, null);
                }
                return false;
            }
        });

//Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeMultipleOptionsUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked
                // as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeMultipleOptionsUserPoll.setChecked(true);
                  //  Toast.makeText(getContext(), userPollActivity.getString(R.string.rewards_add_toast), Toast.LENGTH_SHORT).show();
                    likesUnLikes(position, null, holder, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeMultipleOptionsUserPoll.setChecked(false);
                    likesUnLikes(position, null, holder, null, null);
                }

            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsMultipleOptionsUserPoll
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int clickPosition = position;
                        userPollResponse = getItem(clickPosition);
                        optionClickAction(checkedId);
                        optionSelection(userPollResponse, clickPosition, null, holder, null, null);
                    }
                });
    }

    /**
     * The OnCheckedChangeListener will be called when any widget like button, text, image etc is either clicked or
     * touched or focused upon by the user.
     *
     * @param checkedId
     */
    private void optionClickAction(int checkedId) {
        switch (checkedId) {
            case R.id.option1:
                //Setting the value for the variable
                pollAnswerSelectedId = "1";
                pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
                break;
            case R.id.option2:
                //Setting the value for the variable
                pollAnswerSelectedId = "2";
                pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2();
                break;
            case R.id.option3:
                //Setting the value for the variable
                pollAnswerSelectedId = "3";
                pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3();
                break;
            case R.id.option4:
                //Setting the value for the variable
                pollAnswerSelectedId = "4";
                pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4();
                break;
            default:
                break;
        }

    }

    /**
     * This method is when the user click ths share icon to share the poll
     *
     * @param clickPosition -position
     */

    private void shareClickAction(int clickPosition) {
        //Response from the server when the particular cell is clicked.Based on the click position data is retrived
        // from the response.
        userPollResponse = getItem(clickPosition);
        //Poll id from the response
        String pollId = String.valueOf(userPollResponse.getPollId());
        //Converting the poll id into base 64
        String base64CampaignId = MApplication.convertByteCode(pollId);
        //Total url
        String shareUrl = Constants.SHARE_POLL_BASE_URL.concat(base64CampaignId);
        //Sharing the url in gmail

        getSharedURL(shareUrl);

    }

    /**
     * This method is called when the user click the like count
     *
     * @param myPollResponse
     */

    private void likeClickAction(GroupPollDataObject myPollResponse) {
        //Poll id from the response
        String id = String.valueOf(myPollResponse.getPollId());
        // It can be used with startActivity to launch an Activity
        Intent details = new Intent(userPollActivity, PollLikes.class);
        //Passing the id form one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Start Activity
        userPollActivity.startActivity(details);
    }

    /**
     * View hoder3
     *
     * @param holder            -view holder class
     * @param position-position
     */
    private void validateViewLayoutThree(final ViewHolderLayoutThree holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceUserPollLikeCount = MApplication
                .loadArray(userPollActivity, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants
                        .USER_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceUserPollCommentsCount = MApplication
                .loadArray(userPollActivity, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants
                        .USER_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceUserPollLikeUser = MApplication
                .loadArray(userPollActivity, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants
                        .USER_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceUserPollIdAnswer = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY,
                        Constants.USER_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceUserPollIdAnswerSelected = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY,
                        Constants.USER_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferenceUserPollParticipateCount = MApplication
                .loadArray(userPollActivity, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY,
                        Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikePhotoComparisonUserPoll.setChecked(true);
        } else {

            holder.likeUnlikePhotoComparisonUserPoll.setChecked(false);
        }

        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            holder.imageQuestion1PhotoComparisonUserPoll.setVisibility(View.GONE);
            holder.imageQuestion2PhotoComparisonUserPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage1, holder
                        .imageQuestion1PhotoComparisonUserPoll, R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage2, holder
                        .imageQuestion2PhotoComparisonUserPoll, R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                holder.singleOptionPhotoComparisonUserPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage1, holder
                        .singleOptionPhotoComparisonUserPoll, R.drawable.placeholder_image);
                holder.imageQuestion1PhotoComparisonUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonUserPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.singleOptionPhotoComparisonUserPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage2, holder
                        .singleOptionPhotoComparisonUserPoll, R.drawable.placeholder_image);
                holder.imageQuestion1PhotoComparisonUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonUserPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1PhotoComparisonUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonUserPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.imageAnswer1PhotoComparisonUserPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer1)) {
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer1PhotoComparisonUserPoll.setVisibility(View.VISIBLE);
            if(pollAnswer1!=null)
            Utils.loadImageWithGlideRounderCorner(userPollActivity, pollAnswer1
                    .replaceAll(" ", "%20"), holder.imageAnswer1PhotoComparisonUserPoll, R.drawable.placeholder_image);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            if(pollAnswer2!=null)
            Utils.loadImageWithGlideRounderCorner(userPollActivity, pollAnswer1
                    .replaceAll(" ", "%20"), holder.imageAnswer1PhotoComparisonUserPoll, R.drawable.placeholder_image);
            holder.imageAnswer2PhotoComparisonUserPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer2PhotoComparisonUserPoll.setVisibility(View.VISIBLE);

            if(pollAnswer2!=null)
            Utils.loadImageWithGlideRounderCorner(userPollActivity, pollAnswer2
                    .replaceAll(" ", "%20"), holder.imageAnswer2PhotoComparisonUserPoll, R.drawable.placeholder_image);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.imageAnswer3PhotoComparisonUserPoll.setVisibility(View.GONE);
            holder.imageAnswer4PhotoComparisonUserPoll.setVisibility(View.GONE);
            holder.relativeAnswer3PhotoComparisonUserPoll.setVisibility(View.GONE);
            holder.relativeAnswer4PhotoComparisonUserPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer3)) {
            holder.imageAnswer3PhotoComparisonUserPoll.setVisibility(View.VISIBLE);
            holder.relativeAnswer3PhotoComparisonUserPoll.setVisibility(View.VISIBLE);
            if(pollAnswer3!=null)
            Utils.loadImageWithGlideRounderCorner(userPollActivity, pollAnswer3
                    .replaceAll(" ", "%20"), holder.imageAnswer3PhotoComparisonUserPoll, R.drawable.placeholder_image);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.imageAnswer4PhotoComparisonUserPoll.setVisibility(View.INVISIBLE);
                holder.relativeAnswer4PhotoComparisonUserPoll.setVisibility(View.GONE);
                //If the poll answer option is not empty then the view will invisible
            } else if (!("").equals(pollAnswer4)) {
                holder.imageAnswer3PhotoComparisonUserPoll.setVisibility(View.VISIBLE);
                if(pollAnswer4!=null)
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollAnswer4
                        .replaceAll(" ", "%20"), holder.imageAnswer4PhotoComparisonUserPoll, R.drawable
                        .placeholder_image);
                holder.relativeAnswer4PhotoComparisonUserPoll.setVisibility(View.VISIBLE);
            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceUserPollIdAnswer.contains(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()))) {
            //Getting the position of the particular value saved in preference
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()));
            //Selected answer from the position
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will
            // be false.
            if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1().equals(value)) {
                holder.radioOptions1PhotoComparisonUserPoll.setChecked(true);
            } else if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2().equals(value)) {
                holder.radioOptions2PhotoComparisonUserPoll.setChecked(true);
            } else if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3().equals(value)) {
                holder.radioOptions3PhotoComparisonUserPoll.setChecked(true);
            } else if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4().equals(value)) {
                holder.radioOptions4PhotoComparisonUserPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1PhotoComparisonUserPoll.setClickable(false);
            holder.radioOptions2PhotoComparisonUserPoll.setClickable(false);
            holder.radioOptions3PhotoComparisonUserPoll.setClickable(false);
            holder.radioOptions4PhotoComparisonUserPoll.setClickable(false);
        }
        //Setting the poll question intext view
        holder.txtQuestionPhotoComparisonUserPoll.setText(pollQuestion);
        //Setting the preference like count
        holder.txtLikePhotoComparisonUserPoll.setText(String.valueOf(prefrenceUserPollLikeCount.get(position)));
        //Setting the user name in textview
        holder.txtNamePhotoComparisonUserPoll
                .setText(MApplication.getDecodedString(userPollResponse.getUserPollsQuestion().getUserInfos().getName()));
        //Setting the updated time
        holder.txtTimePhotoComparisonUserPoll.setText(MApplication.getTimeDifference(userPollResponse.getUserPollsQuestion().getCreatedAt()));
        //Setting the category name
        holder.txtCategoryPhotoComparisonUserPoll.setText(userPollResponse.getPollcategories().getCategories().getCategoryName());
        //Setting the user profile image
        if(userPollResponse.getUserPollsQuestion().getUserInfos().getImage()!=null)
            Utils.loadImageWithGlide(userPollActivity, userPollResponse.getUserPollsQuestion().getUserInfos().getImage().replaceAll(" ", "%20"), holder.imgProfilePhotoComparisonUserPoll, R.drawable.img_ic_user);
        else {
            holder.txtCategoryPhotoComparisonUserPoll.setVisibility(View.INVISIBLE);
            holder.categoryImageViewThird.setVisibility(View.INVISIBLE);
            holder.imgProfilePhotoComparisonUserPoll.setImageResource(R.drawable.ic_logo_icon);//
        }
        //Setting the participate count from the arraylist based on the position
        holder.txtCountPhotoComparisonUserPoll
                .setText(String.valueOf(preferenceUserPollParticipateCount.get(position)));
        //Setting the COMMENTS from the arraylist  based on the position
        holder.txtCommentsPhotoComparisonUserPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));
        holder3ViewOnClick(holder, position);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void holder3ViewOnClick(final ViewHolderLayoutThree holder, final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountPhotoComparisonUserPoll.setOnClickListener(mParticipateCounts);
        holder.imgSharePhotoComparisonUserPoll.setOnClickListener(mShareClickAction);
        holder.txtLikePhotoComparisonUserPoll.setOnClickListener(mLikeUnlikeCheckBox);
        holder.txtCommentsPhotoComparisonUserPoll.setOnClickListener(mCommentClickAction);
        holder.imageQuestion1PhotoComparisonUserPoll.setOnClickListener(mQuestionImage1);
        holder.imageQuestion2PhotoComparisonUserPoll.setOnClickListener(mQuestionImage2);
        holder.singleOptionPhotoComparisonUserPoll.setOnClickListener(mSingleOption);
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer1PhotoComparisonUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                clickPosition = position;
                //Response from the server based on the position
                userPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer1 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer1);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer2PhotoComparisonUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                clickPosition = position;
                //Response from the server based on the position
                userPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer2 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer2);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer3PhotoComparisonUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                clickPosition = position;
                //Response from the server based on the position
                userPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer3 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer3);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer4PhotoComparisonUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                clickPosition = position;
                //Response from the server based on the position
                userPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer4 = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer4);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikePhotoComparisonUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked
                // as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikePhotoComparisonUserPoll.setChecked(true);
                 //   Toast.makeText(getContext(), userPollActivity.getString(R.string.rewards_add_toast), Toast.LENGTH_SHORT).show();
                    likesUnLikes(position, null, null, holder, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikePhotoComparisonUserPoll.setChecked(false);
                    likesUnLikes(position, null, null, holder, null);

                }

            }
        });
        holder.radioOptions1PhotoComparisonUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    //Click position
                    clickPosition = position;
                    //Response from the server based on the position
                    userPollResponse = getItem(clickPosition);
                    //poll answer url
                    pollAnswerSelectedId = "1";
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
                    optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        holder.radioOptions2PhotoComparisonUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    //Click position
                    clickPosition = position;
                    //Response from the server based on the position
                    userPollResponse = getItem(clickPosition);
                    //poll answer url
                    pollAnswerSelectedId = "2";
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2();
                    optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        holder.radioOptions3PhotoComparisonUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    //Click position
                    clickPosition = position;
                    //Response from the server based on the position
                    userPollResponse = getItem(clickPosition);
                    //poll answer url
                    pollAnswerSelectedId = "3";
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3();
                    optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        holder.radioOptions4PhotoComparisonUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    //Click position
                    clickPosition = position;
                    //Response from the server based on the position
                    userPollResponse = getItem(clickPosition);
                    //poll answer url
                    pollAnswerSelectedId = "4";
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4();
                    optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                }
                return false;
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions1PhotoComparisonUserPoll
                .setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //Click position
                        clickPosition = position;
                        //Response from the server based on the position
                        userPollResponse = getItem(clickPosition);
                        //poll answer url
                        pollAnswerSelectedId = "1";
                        pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
                        optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                    }

                });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions2PhotoComparisonUserPoll
                .setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //Click position
                        clickPosition = position;
                        //Response from the server based on the position
                        userPollResponse = getItem(clickPosition);
                        //poll answer url
                        pollAnswerSelectedId = "2";
                        pollAnswer = userPollResponse != null ? userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2() : null;
                        optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                    }

                });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions3PhotoComparisonUserPoll
                .setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //Click position
                        clickPosition = position;
                        //Response from the server based on the position
                        userPollResponse = getItem(clickPosition);
                        //poll answer url
                        pollAnswerSelectedId = "3";
                        pollAnswer = userPollResponse != null ? userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3() : null;
                        optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                    }

                });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions4PhotoComparisonUserPoll
                .setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //Click position
                        clickPosition = position;
                        //Response from the server based on the position
                        userPollResponse = getItem(clickPosition);
                        //poll answer url
                        pollAnswerSelectedId = "4";
                        pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4();
                        optionSelection(userPollResponse, clickPosition, null, null, holder, null);
                    }

                });
    }

    private void validateViewLayoutFour(final ViewHolderLayoutFour holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceUserPollLikeCount = MApplication
                .loadArray(userPollActivity, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants
                        .USER_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceUserPollCommentsCount = MApplication
                .loadArray(userPollActivity, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants
                        .USER_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceUserPollLikeUser = MApplication
                .loadArray(userPollActivity, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants
                        .USER_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceUserPollIdAnswer = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwserCheck, Constants.USER_POLL_ID_ANSWER_ARRAY,
                        Constants.USER_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceUserPollIdAnswerSelected = MApplication
                .loadStringArray(userPollActivity, userPollIdAnwser, Constants.USER_POLL_ID_ANSWER_SELECTED_ARRAY,
                        Constants.USER_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferenceUserPollParticipateCount = MApplication
                .loadArray(userPollActivity, userPollParticipateCount, Constants.USER_POLL_PARTICIPATE_COUNT_ARRAY,
                        Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false

        holderUserView4.imgShareYouTubeUrlUserPoll.setVisibility(View.GONE);
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYouTubeUrlUserPoll.setChecked(true);
        } else {
            holder.likeUnlikeYouTubeUrlUserPoll.setChecked(false);
        }
        //If the question image 1 and Question 2 is empty visiblity will be gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            holder.imageQuestion1YouTubeUrlUserPoll.setVisibility(View.GONE);
            holder.imageQuestion2YouTubeUrlUserPoll.setVisibility(View.GONE);
        } else {
            //If the question image 1 and Question 2 is not empty visiblity will be visible
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage1, holder
                        .imageQuestion1YouTubeUrlUserPoll, R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(userPollActivity, pollImage1, holder
                        .imageQuestion2YouTubeUrlUserPoll, R.drawable.placeholder_image);
                //If the question image 1 is  not empty visiblity will be visible
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                holder.singleOptionYouTubeUrlUserPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage1, holder
                        .singleOptionYouTubeUrlUserPoll, R.drawable.placeholder_image);
                holder.imageQuestion1YouTubeUrlUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlUserPoll.setVisibility(View.GONE);
                //If the question image 2 is not empty visiblity will be visible
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                holder.singleOptionYouTubeUrlUserPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(userPollActivity, pollImage2, holder
                        .singleOptionYouTubeUrlUserPoll, R.drawable.placeholder_image);
                holder.imageQuestion1YouTubeUrlUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlUserPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1YouTubeUrlUserPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlUserPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1)) {
            holder.radioOptions1YouTubeUrlUserPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions1YouTubeUrlUserPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1YouTubeUrlUserPoll.setText(pollAnswer1);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2)) {
            holder.radioOptions2YouTubeUrlUserPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            holder.radioOptions2YouTubeUrlUserPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2YouTubeUrlUserPoll.setText(pollAnswer2);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3)) {
            holder.radioOptions3YouTubeUrlUserPoll.setVisibility(View.GONE);
            holder.radioOptions3YouTubeUrlUserPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer3)) {
            holder.radioOptions3YouTubeUrlUserPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3YouTubeUrlUserPoll.setText(pollAnswer3);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4)) {
                holder.radioOptions4YouTubeUrlUserPoll.setVisibility(View.GONE);
            } else if (!("").equals(pollAnswer4)) {
                holder.radioOptions4YouTubeUrlUserPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4YouTubeUrlUserPoll.setText(pollAnswer4);

            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceUserPollIdAnswer.contains(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()))) {
            //Getting the position of the particular value saved in preference
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(String.valueOf(userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId()));
            //Selected answer from the position
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will
            // be false.
            if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1().equals(value)) {
                holder.radioOptions1YouTubeUrlUserPoll.setChecked(true);
            } else if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2().equals(value)) {
                holder.radioOptions2YouTubeUrlUserPoll.setChecked(true);
            } else if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3().equals(value)) {
                holder.radioOptions3YouTubeUrlUserPoll.setChecked(true);
            } else if (userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4().equals(value)) {
                holder.radioOptions4YouTubeUrlUserPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1YouTubeUrlUserPoll.setClickable(false);
            holder.radioOptions2YouTubeUrlUserPoll.setClickable(false);
            holder.radioOptions3YouTubeUrlUserPoll.setClickable(false);
            holder.radioOptions4YouTubeUrlUserPoll.setClickable(false);
        }
        //Setting the like count in text view
        holder.txtLikeYouTubeUrlUserPoll.setText(String.valueOf(prefrenceUserPollLikeCount.get(position)));
        //Setting the user name in text view
        holder.txtNameYouTubeUrlUserPoll
                .setText(MApplication.getDecodedString(userPollResponse.getUserPollsQuestion().getUserInfos().getName()));
        //Setting the updted time
        holder.txtTimeYouTubeUrlUserPoll.setText(MApplication.getTimeDifference(userPollResponse.getUserPollsQuestion().getCreatedAt()));
        //Setting the category name in text view
        holder.txtCategoryYouTubeUrlUserPoll.setText(userPollResponse.getPollcategories().getCategories().getCategoryName());
        //Setting the user profile image
        if (userPollResponse.getUserPollsQuestion().getUserInfos().getImage()!=null) {
            Utils.loadImageWithGlide(userPollActivity, userPollResponse.getUserPollsQuestion().getUserInfos().getImage()
                                                                       .replaceAll(" ", "%20"), holder
                    .imgProfileYouTubeUrlUserPoll, R.drawable.img_ic_user);
        } else {
            holder.txtCategoryYouTubeUrlUserPoll.setVisibility(View.INVISIBLE);
            holder.categoryImageViewFourth.setVisibility(View.INVISIBLE);
            holder.imgProfileYouTubeUrlUserPoll.setImageResource(R.drawable.ic_logo_icon);
        }
//
        //Setting the count from the arraylist
        holder.txtVideoUrlCountYouTubeUrlUserPoll
                .setText(String.valueOf(preferenceUserPollParticipateCount.get(position)));
        //Setting the question  in text view
        holder.txtQuestionYouTubeUrlUserPoll.setText(pollQuestion);
        //Setting the COMMENTS from the arraylist
        holder.txtCommentsYouTubeUrlUserPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));
        holderView4OnClickListner(holder, position);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void holderView4OnClickListner(final ViewHolderLayoutFour holder, final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYouTubeUrlUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked
                // as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYouTubeUrlUserPoll.setChecked(true);
                   // Toast.makeText(getContext(), userPollActivity.getString(R.string.rewards_add_toast), Toast.LENGTH_SHORT).show();
                    likesUnLikes(position, null, null, null, holder);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYouTubeUrlUserPoll.setChecked(false);
                    likesUnLikes(position, null, null, null, holder);

                }

            }
        });
        holder.radioOptions1YouTubeUrlUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    //poll answer selected id is set as 2
                    pollAnswerSelectedId = "1";
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer1();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, null, null, holder);
                }
                return false;
            }
        });

        holder.radioOptions2YouTubeUrlUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    //poll answer selected id is set as 2
                    pollAnswerSelectedId = "2";
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer2();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, null, null, holder);
                }
                return false;
            }
        });
        holder.radioOptions3YouTubeUrlUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    pollAnswerSelectedId = "3";     //poll answer selected id is set as 1
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer3();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, null, null, holder);
                }
                return false;
            }
        });
        holder.radioOptions4YouTubeUrlUserPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int clickPosition = position; //Click position
                userPollResponse = getItem(clickPosition);
                if (!view.isClickable()) {
                    pollAnswerSelectedId = "4";     //poll answer selected id is set as 1
                    //poll answer
                    pollAnswer = userPollResponse.getUserPollsQuestion().getPollAnswers().getPollAnswer4();
                    //Option selection request
                    optionSelection(userPollResponse, clickPosition, null, null, null, holder);
                }
                return false;
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtVideoUrlCountYouTubeUrlUserPoll.setOnClickListener(mParticipateCounts);

        holder.imgShareYouTubeUrlUserPoll.setOnClickListener(mShareClickAction);
        holder.txtLikeYouTubeUrlUserPoll.setOnClickListener(mLikeUnlikeCheckBox);
        holder.txtCommentsYouTubeUrlUserPoll.setOnClickListener(mCommentClickAction);
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsYouTubeUrlUserPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Click position
                int clickPosition = position;
                //Response
                userPollResponse = getItem(clickPosition);
                optionClickAction(checkedId);
                optionSelection(userPollResponse, clickPosition, null, null, null, holder);
            }
        });

        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.singleOptionYouTubeUrlUserPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickPosition = position; //Click ACTION
                userPollResponse = getItem(clickPosition); //Response
               //Commented by NIKITA
                //youTubeUrl = userPollResponse.getYouTubeUrl(); //Getting the url from the response
                MApplication.setString(userPollActivity, Constants.YOUTUBE_URL, youTubeUrl); //Setting in prefernce
                //If net is connected video is played
                //else toast message
                if (MApplication.isNetConnected(userPollActivity)) {
                    Intent a = new Intent(userPollActivity, VideoLandscapeActivity.class);
                    userPollActivity.startActivity(a);
                } else {
                    Toast.makeText(userPollActivity, userPollActivity.getResources()
                                                                     .getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void getSharedURL(String url) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("share_url",url);

            Log.v("...", obj.toString());
        }
        catch (Exception ae)
        {

        }


        UserPollRestClient.getInstance().getShareURL(new GetsharedURL_InputModel(url), new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response1) {

                String result =CustomRequest.ResponcetoString(response1);
                try {
                    JSONObject response = new JSONObject(result);

                    int success=response.optInt("success");
                    String results=response.optString("results");

                    {

                        if(success==1)
                        {
                            MApplication.shareGmail(userPollActivity, results, MApplication
                                    .getString(userPollActivity, Constants.PROFILE_USER_NAME));

                        }
                        else
                        {

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }

    /**
     * This method is used for like and unlike the poll
     *
     * @param clickPosition   -position
     * @param holder1-holding views
     * @param holder2-holding views
     * @param holder3-holding views
     * @param holder4-holding views
     */
    private void likesUnLikes(final int clickPosition, final ViewHolderLayoutOne holder1, final ViewHolderLayoutTwo
            holder2, final ViewHolderLayoutThree holder3, final ViewHolderLayoutFour holder4) {
        //Response from the server
        userPollResponse = getItem(clickPosition);
        //Getting the id
        MApplication.materialdesignDialogStart(userPollActivity);
        String pollId = String.valueOf(userPollResponse.getPollId());
        LikesAndUnLikeRestClient.getInstance()
                                .postCampaignPollLikes("poll_likes", userId, pollId, String.valueOf(mlikes)
                                        ,new Callback<LikeUnLikeResposneModel>() {
                                            @Override
                                            public void success(LikeUnLikeResposneModel likesUnlike, Response
                                                    response) {
                                                //If the value from the response is 1 then the user has successfully
                                                // liked the poll
                                                if (("1").equals(likesUnlike.getResults())) {
                                                    //Changing the value in array list in a particular position
                                                    preferenceUserPollLikeUser.set(clickPosition, Integer.valueOf(1));
                                                    //Saving it in array
                                                    MApplication
                                                            .saveArray(userPollActivity, preferenceUserPollLikeUser,
                                                                    Constants.USER_POLL_LIKES_USER_ARRAY, Constants
                                                                            .USER_POLL_LIKES_USER_SIZE);
                                                } else {
                                                    //Changing the value in array list in a particular position
                                                    preferenceUserPollLikeUser.set(clickPosition, Integer.valueOf(0));
                                                    //Saving it in array
                                                    MApplication
                                                            .saveArray(userPollActivity, preferenceUserPollLikeUser,
                                                                    Constants.USER_POLL_LIKES_USER_ARRAY, Constants
                                                                            .USER_POLL_LIKES_USER_SIZE);
                                                }
                                                //Toast message will display
                                                //Commented by nikita
                                                Toast.makeText(userPollActivity, likesUnlike.getMsg(),
                                                        Toast.LENGTH_SHORT).show();
                                                //Changing the value in array list in a particular position
                                                prefrenceUserPollLikeCount
                                                        .set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                                                //Saving the array in prefernce
                                                MApplication
                                                        .saveArray(userPollActivity, prefrenceUserPollLikeCount,
                                                                Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants
                                                                        .USER_POLL_LIKES_COUNT_SIZE);
                                                //If holder1 is not null
                                                if (holder1 != null) {
                                                    //Changing the value in textview
                                                    holder1.txtLikeYesOrNoUserPoll.setText(String
                                                            .valueOf(prefrenceUserPollLikeCount.get(clickPosition)));
                                                    //If holder2 is not null
                                                } else if (holder2 != null) {
                                                    //Changing the value in textview
                                                    holder2.txtLikeMultipleOptionsUserPoll.setText(String
                                                            .valueOf(prefrenceUserPollLikeCount.get(clickPosition)));
                                                    //If holder3 is not null
                                                } else if (holder3 != null) {
                                                    //Changing the value in textview
                                                    holder3.txtLikePhotoComparisonUserPoll.setText(String
                                                            .valueOf(prefrenceUserPollLikeCount.get(clickPosition)));
                                                    //If holder4 is not null
                                                } else if (holder4 != null) {
                                                    //Changing the value in textview
                                                    holder4.txtLikeYouTubeUrlUserPoll.setText(String
                                                            .valueOf(prefrenceUserPollLikeCount.get(clickPosition)));
                                                }
                                                MApplication.materialdesignDialogStop();

                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void failure(RetrofitError retrofitError) {
                                                ////Error message popups when the user cannot able to coonect with
                                                /// the server
                                                MApplication.errorMessage(retrofitError, userPollActivity);
                                                MApplication.materialdesignDialogStop();
                                            }
                                        });
    }

    /**
     * Selecting the poll answer request and response
     *
     * @param userPollResponse
     * @param clickPosition    -position
     * @param holder1          -view holder
     * @param holder2          -view holder
     * @param holder3          -view holder
     * @param holder4          -view holder
     */
    private void optionSelection(final GroupPollDataObject userPollResponse, final int clickPosition,
                                 final ViewHolderLayoutOne holder1, final ViewHolderLayoutTwo holder2, final
                                 ViewHolderLayoutThree holder3, final ViewHolderLayoutFour holder4) {

        //Getting the id from the response
        String pollId = String.valueOf(this.userPollResponse.getPollId());
        //Getting the poll answer id from the response
        String pollAnswerId = String.valueOf(this.userPollResponse.getUserPollsQuestion().getPollAnswers().getPollId());
        PollParticipateRestClient.getInstance()
                                 .postParticipateApi("polls_participate", userId, pollId, pollAnswerId, pollAnswer, pollAnswerSelectedId, "1"
                                         , new Callback<PollParticipateResponseModel>() {
                                             @Override
                                             public void success(PollParticipateResponseModel
                                                                         pollParticipateResponseModel, Response
                                                                         response) {
                                                 //If the success is 1 the data is binded into the views
                                                 if (("1").equals(pollParticipateResponseModel.getSuccess())) {
                                                     //Adding the value in arraylist
                                                     preferenceUserPollIdAnswer
                                                             .add(pollParticipateResponseModel.getResults()
                                                                                              .getPollId());
                                                     //Adding the value in arraylist
                                                     preferenceUserPollIdAnswerSelected
                                                             .add(pollParticipateResponseModel.getResults()
                                                                                              .getPollAnswer());
                                                     //Increamenting the participate count on success 1
                                                     int participateCount = Integer
                                                             .parseInt(pollParticipateResponseModel.getTotalCount());
                                                     //Setting participate count in the arraylist
                                                     preferenceUserPollParticipateCount
                                                             .set(clickPosition, participateCount);
                                                     //Saving in prefernce
                                                     MApplication
                                                             .saveArray(userPollActivity,
                                                                     preferenceUserPollParticipateCount, Constants
                                                                             .USER_POLL_PARTICIPATE_COUNT_ARRAY,
                                                                     Constants.USER_POLL_PARTICIPATE_COUNT_SIZE);
                                                     //Saving in prefernce
                                                     MApplication
                                                             .saveStringArray(userPollActivity,
                                                                     preferenceUserPollIdAnswer, Constants
                                                                             .USER_POLL_ID_ANSWER_ARRAY, Constants
                                                                             .USER_POLL_ID_ANSWER_SIZE);
                                                     //Saving in prefernce
                                                     MApplication
                                                             .saveStringArray(userPollActivity,
                                                                     preferenceUserPollIdAnswerSelected, Constants
                                                                             .USER_POLL_ID_ANSWER_SELECTED_ARRAY,
                                                                     Constants.USER_POLL_ID_SELECTED_SIZE);
                                                     //If poll type equals 1
                                                     if (("1").equals(userPollResponse.getUserPollsQuestion().getPollType())) {
                                                         Log.e("test", "test");
                                                         //Setting the radio options as false
                                                         holder1.radioYesUserPoll.setClickable(false);
                                                         //Setting the radio options as false
                                                         holder1.radioNoUserPoll.setClickable(false);
                                                         //If it is checked,then the text view sis set as black
                                                          if (holder1.radioYesUserPoll.isChecked()) {
                                                             holder1.radioYesUserPoll.setTextColor(Color.BLACK);
                                                         } else if (holder1.radioNoUserPoll.isChecked()) {
                                                             holder1.radioNoUserPoll.setTextColor(Color.BLACK);
                                                         }
                                                         Log.e("check", preferenceUserPollParticipateCount
                                                                 .get(clickPosition) + "");
                                                         //Setting the value in participate count
                                                         holder1.txtCountsYesOrNoPoll.setText(String
                                                                 .valueOf(preferenceUserPollParticipateCount
                                                                         .get(clickPosition)));
                                                         //If poll type equals 2
                                                     } else if (("2").equals(userPollResponse.getUserPollsQuestion().getPollType())) {
                                                         //Setting the clickable as false
                                                         holder2.radioOptions1MultipleOptionsUserPoll
                                                                 .setClickable(false);
                                                         holder2.radioOptions2MultipleOptionsUserPoll
                                                                 .setClickable(false);
                                                         holder2.radioOptions3MultipleOptionsUserPoll
                                                                 .setClickable(false);
                                                         holder2.radioOptions4MultipleOptionsUserPoll
                                                                 .setClickable(false);
                                                         //If it is checked,then the text view sis set as black
                                                         if (holder2.radioOptions1MultipleOptionsUserPoll.isChecked()) {
                                                             holder2.radioOptions1MultipleOptionsUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder2.radioOptions2MultipleOptionsUserPoll
                                                                 .isChecked()) {
                                                             holder2.radioOptions2MultipleOptionsUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder2.radioOptions3MultipleOptionsUserPoll
                                                                 .isChecked()) {
                                                             holder2.radioOptions3MultipleOptionsUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder2.radioOptions4MultipleOptionsUserPoll
                                                                 .isChecked()) {
                                                             holder2.radioOptions4MultipleOptionsUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         }
                                                         //Setting the value in participate count
                                                         holder2.txtCountsMultipleOptionsUserPoll.setText(String
                                                                 .valueOf(preferenceUserPollParticipateCount
                                                                         .get(clickPosition)));
                                                     } else if (("3").equals(userPollResponse.getUserPollsQuestion().getPollType())) {
                                                         //Setting the clickable as false
                                                         holder3.radioOptions1PhotoComparisonUserPoll
                                                                 .setClickable(false);
                                                         holder3.radioOptions2PhotoComparisonUserPoll
                                                                 .setClickable(false);
                                                         holder3.radioOptions3PhotoComparisonUserPoll
                                                                 .setClickable(false);
                                                         holder3.radioOptions4PhotoComparisonUserPoll
                                                                 .setClickable(false);
                                                         //If it is checked,then the text view sis set as black
                                                         if (holder3.radioOptions1PhotoComparisonUserPoll.isEnabled()) {
                                                             holder3.radioOptions1PhotoComparisonUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder3.radioOptions2PhotoComparisonUserPoll
                                                                 .isEnabled()) {
                                                             holder3.radioOptions2PhotoComparisonUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder3.radioOptions3PhotoComparisonUserPoll
                                                                 .isEnabled()) {
                                                             holder3.radioOptions3PhotoComparisonUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder3.radioOptions4PhotoComparisonUserPoll
                                                                 .isEnabled()) {
                                                             holder3.radioOptions4PhotoComparisonUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         }
                                                         //Setting the value in participate count
                                                         holder3.txtCountPhotoComparisonUserPoll.setText(String
                                                                 .valueOf(preferenceUserPollParticipateCount
                                                                         .get(clickPosition)));
                                                     } else if (("4").equals(userPollResponse.getUserPollsQuestion().getPollType())) {
                                                         holder4.radioOptions1YouTubeUrlUserPoll.setClickable(false);
                                                         holder4.radioOptions2YouTubeUrlUserPoll.setClickable(false);
                                                         holder4.radioOptions3YouTubeUrlUserPoll.setClickable(false);
                                                         holder4.radioOptions4YouTubeUrlUserPoll.setClickable(false);
                                                         //If it is checked,then the text view sis set as black
                                                         if (holder4.radioOptions1YouTubeUrlUserPoll.isChecked()) {
                                                             holder4.radioOptions1YouTubeUrlUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder4.radioOptions2YouTubeUrlUserPoll
                                                                 .isChecked()) {
                                                             holder4.radioOptions2YouTubeUrlUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder4.radioOptions3YouTubeUrlUserPoll
                                                                 .isChecked()) {
                                                             holder4.radioOptions3YouTubeUrlUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         } else if (holder4.radioOptions4YouTubeUrlUserPoll
                                                                 .isChecked()) {
                                                             holder4.radioOptions4YouTubeUrlUserPoll
                                                                     .setTextColor(Color.BLACK);
                                                         }
                                                         //Setting the value in participate count
                                                         holder4.txtVideoUrlCountYouTubeUrlUserPoll.setText(String
                                                                 .valueOf(preferenceUserPollParticipateCount
                                                                         .get(clickPosition)));
                                                     }
                                                     notifyDataSetChanged();
                                                     //Toast message will display
                                                     //Commented by nikita
                                                     Toast.makeText(userPollActivity, pollParticipateResponseModel
                                                                     .getMsg(),
                                                             Toast.LENGTH_SHORT).show();
                                                 } else {
                                                     Toast.makeText(userPollActivity, pollParticipateResponseModel
                                                                     .getMsg(),
                                                             Toast.LENGTH_SHORT).show();
                                                 }

                                             }

                                             @Override
                                             public void failure(RetrofitError retrofitError) {
                                                 ////Error message popups when the user cannot able to coonect with
                                                 /// the server
                                                 MApplication.errorMessage(retrofitError, userPollActivity);
                                             }

                                         });
    }

    /**
     * Comment click ACTION
     *
     * @param clickPosition-position
     */
    private void commentClickAction(int clickPosition) {
        //response from the server
        userPollResponse = getItem(clickPosition);
        String id = String.valueOf(userPollResponse.getPollId());
        //Moving from one activity to another activity
        Intent details = new Intent(userPollActivity, PollComments.class);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_TYPE, userPollResponse.getUserPollsQuestion().getPollType());
        //Passing the value from one activity to another
        details.putExtra(Constants.COMMENTS_COUNT_POSITION, clickPosition);
        //Starting the activity
        userPollActivity.startActivity(details);
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutOne {

        //text view
        private TextView txtLikeYesOrNoUserPoll;

        //Radio group
        private RadioGroup radioYesOrNoUserPoll;

        //Radio button
        private RadioButton radioYesUserPoll;

        //Radio button
        private RadioButton radioNoUserPoll;

        //Check box like unlike
        private CheckBox likeUnlikeYesOrNoPoll;

        //text view name
        private TextView txtNameYesOrNoPoll;

        //Text view time
        private TextView txtTimeYesOrNoPoll;

        //Text view category
        private TextView txtCategoryYesOrNoPoll;

        //Image view
        private ImageView imgProfileYesOrNoPoll;

        private ImageView categoryImageViewFirst;

        //SimpleDraweeView view
        private ImageView imageQuestion1YesOrNoPoll;

        //SimpleDraweeView view
        private ImageView imageQuestion2YesOrNoPoll;

        //SimpleDraweeView view
        private ImageView singleOptionYesOrNoPoll;

        //TextView question
        private TextView txtQuestionYesOrNoPoll;

        //Text view comment
        private TextView txtCommentYesOrNoPoll;

        //Textview particcipate count
        private TextView txtCountsYesOrNoPoll;

        //Image view share
        private ImageView imgShareYesOrNoUserPoll;
    }

    /**
     * A ViewHolderLayoutTwo object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutTwo {

        //text view
        private TextView txtLikeMultipleOptionsUserPoll;

        //Radio group
        private RadioGroup radioGroupOptionsMultipleOptionsUserPoll;

        //Radio button
        private RadioButton radioOptions1MultipleOptionsUserPoll;

        //Radio button
        private RadioButton radioOptions2MultipleOptionsUserPoll;

        //Radio button
        private RadioButton radioOptions3MultipleOptionsUserPoll;

        //Radio button
        private RadioButton radioOptions4MultipleOptionsUserPoll;

        //Chek box
        private CheckBox likeUnlikeMultipleOptionsUserPoll;

        //Simple drawer view
        private ImageView imageQuestion1MultipleOptionsUserPoll;

        //Simple drawer view
        private ImageView imageQuestion2MultipleOptionsUserPoll;

        //Simple drawer view
        private ImageView singleOptionMultipleOptionsUserPoll;

        //Text view
        private TextView txtQuestionMultipleOptionsUserPoll;

        //Text view
        private TextView txtCommentsMultipleOptionsUserPoll;

        //Text view
        private TextView txtCountsMultipleOptionsUserPoll;

        //Text view
        private TextView txtNameMultipleOptionsUserPoll;

        //Text view
        private TextView txtTimeMultipleOptionsUserPoll;

        //Text view
        private TextView txtCategoryMultipleOptionsUserPoll;

        //ImageView
        private ImageView imgProfileMultipleOptionsUserPoll;

        private ImageView categoryImageViewSecond;

        //ImageView
        private ImageView imgShareMultipleOptionsUserPoll;
    }    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.e("checkedListner", "checkedListner");

    }

    /**
     * A ViewHolderLayoutThree object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutThree {

        //Simple drawer view
        private ImageView imageAnswer1PhotoComparisonUserPoll;

        //Simple drawer view
        private ImageView imageAnswer2PhotoComparisonUserPoll;

        //Simple drawer view
        private ImageView imageAnswer3PhotoComparisonUserPoll;

        //Simple drawer view
        private ImageView imageAnswer4PhotoComparisonUserPoll;

        //Relative layout
        private RelativeLayout relativeAnswer3PhotoComparisonUserPoll;

        //Relative layout
        private RelativeLayout relativeAnswer4PhotoComparisonUserPoll;

        //Checkbox
        private CheckBox likeUnlikePhotoComparisonUserPoll;

        //Text view
        private TextView txtLikePhotoComparisonUserPoll;

        //Radio button
        private RadioButton radioOptions1PhotoComparisonUserPoll;

        //Radio button
        private RadioButton radioOptions2PhotoComparisonUserPoll;

        //Radio button
        private RadioButton radioOptions3PhotoComparisonUserPoll;

        //Radio button
        private RadioButton radioOptions4PhotoComparisonUserPoll;

        //Imageview
        private ImageView imageQuestion1PhotoComparisonUserPoll;

        //Imageview
        private ImageView imageQuestion2PhotoComparisonUserPoll;

        //Imageview
        private ImageView singleOptionPhotoComparisonUserPoll;

        // Textview
        private TextView txtQuestionPhotoComparisonUserPoll;

        // Textview
        private TextView txtCommentsPhotoComparisonUserPoll;

        // Textview
        private TextView txtCountPhotoComparisonUserPoll;

        // Textview
        private TextView txtNamePhotoComparisonUserPoll;

        // Textview
        private TextView txtTimePhotoComparisonUserPoll;

        // Textview
        private TextView txtCategoryPhotoComparisonUserPoll;

        //Imageview
        private ImageView imgProfilePhotoComparisonUserPoll;

        //Imageview
        private ImageView imgSharePhotoComparisonUserPoll;

        private ImageView categoryImageViewThird;
    }

    /**
     * A ViewHolderLayoutFour object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutFour {

        //Text view
        private TextView txtLikeYouTubeUrlUserPoll;

        //RadioGroup
        private RadioGroup radioGroupOptionsYouTubeUrlUserPoll;

        //RadioButton
        private RadioButton radioOptions1YouTubeUrlUserPoll;

        //RadioButton
        private RadioButton radioOptions2YouTubeUrlUserPoll;

        //RadioButton
        private RadioButton radioOptions3YouTubeUrlUserPoll;

        //RadioButton
        private RadioButton radioOptions4YouTubeUrlUserPoll;

        //Simple drawer view
        private ImageView imageQuestion1YouTubeUrlUserPoll;

        //Simple drawer view
        private ImageView imageQuestion2YouTubeUrlUserPoll;

        //Simple drawer view
        private ImageView singleOptionYouTubeUrlUserPoll;

        //Checkbox
        private CheckBox likeUnlikeYouTubeUrlUserPoll;

        //TextView
        private TextView txtQuestionYouTubeUrlUserPoll;

        //TextView
        private TextView txtCommentsYouTubeUrlUserPoll;

        //TextView
        private TextView txtVideoUrlCountYouTubeUrlUserPoll;

        //TextView
        private TextView txtNameYouTubeUrlUserPoll;

        //TextView
        private TextView txtTimeYouTubeUrlUserPoll;

        //TextView
        private TextView txtCategoryYouTubeUrlUserPoll;

        //TextView
        private ImageView imgProfileYouTubeUrlUserPoll;

        //ImageView
        private ImageView imgShareYouTubeUrlUserPoll;

        private ImageView categoryImageViewFourth;
    }



}


