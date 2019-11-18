package com.contus.mypolls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.activity.CustomRequest;
import com.contus.app.Constants;
import com.contus.likes.PollLikes;
import com.contus.responsemodel.LikeUnLikeResposneModel;
import com.contus.responsemodel.PollDeleteResponseModel;
import com.contus.responsemodel.PollParticipateResponseModel;
import com.contus.responsemodel.UserPollResponseModel;
import com.contus.restclient.LikesAndUnLikeRestClient;
import com.contus.restclient.PollDelete;
import com.contus.restclient.PollParticipateRestClient;
import com.contus.restclient.UserPollRestClient;
import com.contus.userpolls.FullImageView;
import com.contus.views.EndLessListView;
import com.contus.views.VideoLandscapeActivity;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.new_chanages.models.GetsharedURL_InputModel;
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
public class MyPollsCustomAdapter extends ArrayAdapter<UserPollResponseModel.Results.Data> {
    //Layout
    private final int myPollLayoutId;
    //user id
    private final String userId;
    //Activity
    private final Activity myPollActivity;
    private final EndLessListView list;
    private final List<UserPollResponseModel.Results.Data> dataResults;
    private final TextView noMyPollsResults;
    //Response from the server
    private UserPollResponseModel.Results.Data myPollResponse;
    //Youtube url
    private String youTubeUrl;
    //Likes
    private int mlikes;
    //Holder class for view1
    private ViewHolderLayoutOne holderMyPollView1;
    //Holder class for view2
    private ViewHolderLayoutTwo holderMyPollView2;
    //Holder class for view3
    private ViewHolderLayoutThree holderMyPollView3;
    //Holder class for view4
    private ViewHolderLayoutFour holderMyPollView4;
    //View id
    private String idRefrenceView;
    //Poll question
    private String pollQuestion;
    //Poll answer1
    private String pollAnswer1MyPoll;
    //Poll answer2
    private String pollAnswer2MyPoll;
    //Poll answer3
    private String pollAnswer3MyPoll;
    //Poll answer4
    private String pollAnswer4MyPoll;
    //Poll questionImage
    private String questionImage1MyPoll;
    //Poll questionImage2
    private String questionImage2MyPoll;
    //array list for saving the likes counts
    private ArrayList<Integer> prefrenceMyPollLikeCount = new ArrayList<Integer>();
    //array list for saving the likes counts
    private ArrayList<Integer> myPollLikeCount = new ArrayList<Integer>();
    //array list for saving the likes user
    private ArrayList<Integer> myPollLikesUser = new ArrayList<Integer>();
    //like user arrayList
    private ArrayList<Integer> preferenceUserPollLikeUser;
    //array list COMMENTS count
    private ArrayList<Integer> myPollcommentsCount = new ArrayList<Integer>();
    //prefernce array list COMMENTS count
    private ArrayList<Integer> prefrenceUserPollCommentsCount;
    //Poll type
    private String pollType;
    //Prefernce user poll id answer
    private ArrayList<String> preferenceUserPollIdAnswer;
    //Prefernce user poll id answer selected
    private ArrayList<String> preferenceUserPollIdAnswerSelected;
    //Poll id answer selected
    private ArrayList<String> userPollIdAnswerCheck = new ArrayList<String>();
    //poll id answer check
    private ArrayList<String> userPollIdAnswer = new ArrayList<String>();
    //Poll answer
    private String pollAnswer;
    //Poll answer selected id
    private String pollAnswerSelectedId;
    //Participate count
    private ArrayList<Integer> myParticipateCount = new ArrayList<>();
    //Preference participate count
    private ArrayList<Integer> preferencemyParticipateCount;
    //Image
    private String image;
    //view
    private View mMyPollsView;
    /**
     * OnClick listner for question image2
     */
    private View.OnClickListener mMyPollAdapterQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            myPollResponse = getItem(clickPosition);
            //Question image
            questionImage2MyPoll = myPollResponse.getPollquestionImage1();
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(clickPosition, questionImage2MyPoll);
        }
    };
    /**
     * OnClick listner for question single image
     */

    private View.OnClickListener mMyPollAdapterSingleOption = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the lsit
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            //Response from  the server based on the position
            myPollResponse = getItem(position);
            //Question image
            if (("").equals(myPollResponse.getPollquestionImage1())) {
                image = myPollResponse.getPollquestionImage();
            } else {
                image = myPollResponse.getPollquestionImage1();
            }
            //This method will be called wto show th eimage in full view
            imageQuestionClickAction(position, image);
        }
    };
    /**
     * OnClick listner for question question image1
     */
    private View.OnClickListener mMyPollAdapterQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the list
            //Click position
            //Response from  the server based on the position
            myPollResponse = getItem(list.getPositionForView((View) v.getParent()));
            //Question image
            questionImage1MyPoll = myPollResponse.getPollquestionImage();
            //This method will be called wto show th image in full view
            imageQuestionClickAction(list.getPositionForView((View) v.getParent()), questionImage1MyPoll);
        }
    };
    /**
     * OnClick listner for question comment
     */
    private View.OnClickListener mMyPollAdapterCommentClickAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the list
            //Click position
            int clickPosition = list.getPositionForView((View) v.getParent());
            //This method will be called wto show the comment
            commentClickAction(clickPosition);
        }
    };
    /**
     * OnClick listner for question like unlike check box
     */
    private View.OnClickListener mMyPollAdapterLikeUnlikeCheckBox = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the list
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Response from  the server based on the position
            myPollResponse = getItem(clickPosition);
            //Moving to the like activity
            likeClickAction(myPollResponse);
        }
    };
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mMyPollAdapterShareClickAction = new View.OnClickListener() {
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
    private View.OnClickListener mMyPollAdapterParticipateCounts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Position from the list
            final int position = list.getPositionForView((View) v.getParent());
            //Click position
            int clickPosition = position;
            //Participant view method
            clickAction(clickPosition);
        }
    };
    //poll id
    private String pollId;


    /**
     * initializes a new instance of the ListView class.
     *
     * @param activity         -activity
     * @param dataResults      -reasponse from the server
     * @param layoutId         -layout id
     * @param userId           -userid
     * @param list
     * @param noMyPollsResults
     */
    public MyPollsCustomAdapter(Activity activity, List<UserPollResponseModel.Results.Data> dataResults, int layoutId, String userId, EndLessListView list, TextView noMyPollsResults) {
        super(activity, 0, dataResults);
        this.myPollLayoutId = layoutId;
        this.myPollActivity = activity;
        this.userId = userId;
        this.list = list;
        this.dataResults = dataResults;
        this.noMyPollsResults = noMyPollsResults;
    }

    @Override
    public View getView(final int position, View mView, ViewGroup parent) {
        mMyPollsView = mView;
        //Geeting the json response based on the position
        myPollResponse = getItem(position);
        Log.e("myPollResponse", myPollResponse + "");
        //poll type from the response
        idRefrenceView = myPollResponse.getPollType();
        //Poll question from the response
        pollQuestion = myPollResponse.getPollQuestion();
        Log.e("pollQuestion", pollQuestion + "");
        //image from the response
        questionImage1MyPoll = myPollResponse.getPollquestionImage().replaceAll(" ", "%20");
        //Image question from the response
        questionImage2MyPoll = myPollResponse.getPollquestionImage1().replaceAll(" ", "%20");
        //poll answer form the response
        if(myPollResponse.getUserPollsAns().size()>0)
        {
            pollAnswer1MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer1();
            //poll answer form the response
            pollAnswer2MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer2();

            //poll answer form the response
            pollAnswer3MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer3();
            //poll answer form the response
            pollAnswer4MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer4();
        }
        //youtube usrl from tthe response
        youTubeUrl = myPollResponse.getYouTubeUrl();
        //Setting in prefernce
        MApplication.setString(myPollActivity, Constants.YOUTUBE_URL, youTubeUrl);
        //If the value matches the the layout one view is binded.
        if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_ONE) {
            /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(myPollActivity).inflate(R.layout.userpoll_firstview, null);
            //view holder class
            holderMyPollView1 = new ViewHolderLayoutOne();
            holderMyPollView1.radioYesOrNOMyPOll =  mMyPollsView.findViewById(R.id.YesOrNO);
            holderMyPollView1.radioYesMyPOll =  mMyPollsView.findViewById(R.id.radioYes);
            holderMyPollView1.radioNoMyPoll =  mMyPollsView.findViewById(R.id.radioNo);
            holderMyPollView1.likeUnlikeYesOrNoMyPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderMyPollView1.yesOrNoLikeMyPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderMyPollView1.imageQuestion1YesOrNoMyPoll =  mMyPollsView.findViewById(R.id.choose);
            holderMyPollView1.imageQuestion2YesOrNoMyPoll =  mMyPollsView.findViewById(R.id.ChooseAdditional);
            holderMyPollView1.singleOptionYesOrNoPoll =  mMyPollsView.findViewById(R.id.singleOption);
            holderMyPollView1.txtQuestionYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderMyPollView1.txtCommentYesOrNoPoll =  mMyPollsView.findViewById(R.id.txtCommentsCounts);
            holderMyPollView1.txtYesOrNoCounts =  mMyPollsView.findViewById(R.id.txtParticcipation);
            holderMyPollView1.txtNameYesOrNoMyPOll =  mMyPollsView.findViewById(R.id.txtName);
            holderMyPollView1.txtTimeYesOrNoMyPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderMyPollView1.txtCategoryYesOrNoMyPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderMyPollView1.imgProfileYesOrNoMyPoll =  mMyPollsView.findViewById(R.id.imgProfile);
            holderMyPollView1.imgShareYesOrNoMyPOll =  mMyPollsView.findViewById(R.id.imgShare);
            holderMyPollView1.imgdelete= mMyPollsView.findViewById(R.id.imgdelete);
          //  holderMyPollView1.imgShareYesOrNoMyPOll.setImageDrawable(myPollActivity.getResources().getDrawable(R.drawable.arrow_down));
            //Binding the data into the views in android
            holderMyPollView1.imgdelete.setVisibility(View.VISIBLE);
            validateViewLayoutOne(holderMyPollView1, position);
            //If the value matches the the layout one view is binded.
            holderMyPollView1.imgShareYesOrNoMyPOll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareClickAction(position);
                }
            });
            holderMyPollView1.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(position,
                            myPollActivity.getResources().getString(R.string.txt_are_you_sure_you_want_to_delete_poll),
                            myPollActivity.getResources().getString(R.string.text_yes), myPollActivity.getResources().getString(R.string.text_no));
                }
            });
        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_TWO) {
            /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(myPollActivity).inflate(R.layout.userpoll_secondview, null);
            //view holder class
            holderMyPollView2 = new ViewHolderLayoutTwo();
            holderMyPollView2.radioGroupOptionsMyPOll =  mMyPollsView.findViewById(R.id.participate_options);
            holderMyPollView2.radioOptions1MultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.option1);
            holderMyPollView2.radioOptions2MultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.option2);
            holderMyPollView2.radioOptions3MultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.option3);
            holderMyPollView2.radioOptions4MultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.option4);
            holderMyPollView2.likeUnlikeMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderMyPollView2.txtLikeMulipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderMyPollView2.txtNameMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.txtName);
            holderMyPollView2.txtTimeMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderMyPollView2.txtCategoryMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderMyPollView2.imgProfileMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.imgProfile);
            holderMyPollView2.txtQuestionMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderMyPollView2.txtCommentsMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.txtCommentsCounts);
            holderMyPollView2.txtCountsMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.txtParticcipation);
            holderMyPollView2.imageQuestion1MultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.choose);
            holderMyPollView2.imageQuestion2MultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.ChooseAdditional);
            holderMyPollView2.singleOptionMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.singleOption);
            holderMyPollView2.imgShareMultipleOptionsMyPoll =  mMyPollsView.findViewById(R.id.imgShare);
            holderMyPollView2.imgdelete = mMyPollsView.findViewById(R.id.imgdelete);
            // holderMyPollView2.imgShareMultipleOptionsMyPoll.setImageDrawable(myPollActivity.getResources().getDrawable(R.drawable.arrow_down));
            //Binding the data into the views in android
            holderMyPollView2.imgdelete.setVisibility(View.VISIBLE);
            validateViewLayoutTwo(holderMyPollView2, position);
            holderMyPollView2.imgShareMultipleOptionsMyPoll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareClickAction(position);
                }
            });
            holderMyPollView2.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(position,
                            myPollActivity.getResources().getString(R.string.txt_are_you_sure_you_want_to_delete_poll),
                            myPollActivity.getResources().getString(R.string.text_yes), myPollActivity.getResources().getString(R.string.text_no));
                }
            });

        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_THREE) {
             /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(myPollActivity).inflate(R.layout.userpoll_thirdview, null);
            //view holder class
            holderMyPollView3 = new ViewHolderLayoutThree();
            holderMyPollView3.radioOptions1PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.option1);
            holderMyPollView3.radioOptions2PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.option2);
            holderMyPollView3.radioOptions3PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.option3);
            holderMyPollView3.radioOptions4PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.option4);
            holderMyPollView3.likeUnlikePhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderMyPollView3.txtLikePhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderMyPollView3.txtQuestionPhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderMyPollView3.imageAnswer1PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.answer1);
            holderMyPollView3.imageAnswer2PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.answer2);
            holderMyPollView3.imageAnswer3PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.answer3);
            holderMyPollView3.imageAnswer4PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.answer4);
            holderMyPollView3.relativeAnswer3PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.ThirdOptionOption);
            holderMyPollView3.relativeAnswer4PhotoComparisonMyPoll = mMyPollsView.findViewById(R.id.FourthOptionOption);
            holderMyPollView3.likeUnlikePhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderMyPollView3.txtLikePhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderMyPollView3.txtCommentsPhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtCommentsCounts);
            holderMyPollView3.txtCountPhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtParticcipation);
            holderMyPollView3.imageQuestion1PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.choose);
            holderMyPollView3.imageQuestion2PhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.ChooseAdditional);
            holderMyPollView3.singleOptionPhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.singleOption);
            holderMyPollView3.txtNamePhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtName);
            holderMyPollView3.txtTimePhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderMyPollView3.txtCategoryPhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderMyPollView3.imgProfilePhotoComparisonMyPoll =  mMyPollsView.findViewById(R.id.imgProfile);
            holderMyPollView3.imgSharePhotoComparisonMyPoll = mMyPollsView.findViewById(R.id.imgShare);
            holderMyPollView3.imgdelete =  mMyPollsView.findViewById(R.id.imgdelete);
            // holderMyPollView3.imgSharePhotoComparisonMyPoll.setImageDrawable(myPollActivity.getResources().getDrawable(R.drawable.arrow_down));
            holderMyPollView3.imgdelete.setVisibility(View.VISIBLE);
            //Binding the data into the views in android
            validateViewLayoutThree(holderMyPollView3, position);
            holderMyPollView3.imgSharePhotoComparisonMyPoll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareClickAction(position);
                }
            });
            holderMyPollView3.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(position,
                            myPollActivity.getResources().getString(R.string.txt_are_you_sure_you_want_to_delete_poll),
                            myPollActivity.getResources().getString(R.string.text_yes), myPollActivity.getResources().getString(R.string.text_no));
                }
            });


        } else if (Integer.parseInt(idRefrenceView) == Constants.LAYOUT_FOUR) {
            /* create a new view of my layout and inflate it in the row */
            mMyPollsView = LayoutInflater.from(myPollActivity).inflate(R.layout.userpoll_fourthview, null);
            //view holder class
            holderMyPollView4 = new ViewHolderLayoutFour();
            holderMyPollView4 = new ViewHolderLayoutFour();
            holderMyPollView4.radioGroupOptionsYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.participate_options);
            holderMyPollView4.radioOptions1YouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.option1);
            holderMyPollView4.radioOptions2YouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.option2);
            holderMyPollView4.radioOptions3YouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.option3);
            holderMyPollView4.radioOptions4YouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.option4);
            holderMyPollView4.likeUnlikeYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.unLikeDislike);
            holderMyPollView4.txtLikeYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.txtLike2);
            holderMyPollView4.txtQuestionYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.txtStatus);
            holderMyPollView4.txtCommentsYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.txtCommentsCounts);
            holderMyPollView4.txtVideoUrlCountYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.txtParticcipation);
            holderMyPollView4.imageQuestion1YouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.choose);
            holderMyPollView4.imageQuestion2YouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.ChooseAdditional);
            holderMyPollView4.singleOptionYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.singleOption);
            holderMyPollView4.txtNameYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.txtName);
            holderMyPollView4.txtTimeYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.txtTime);
            holderMyPollView4.txtCategoryYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.txtCategory);
            holderMyPollView4.imgProfileYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.imgProfile);
            holderMyPollView4.imgShareYouTubeUrlMyPoll =  mMyPollsView.findViewById(R.id.imgShare);
            holderMyPollView4.imgdelete =  mMyPollsView.findViewById(R.id.imgdelete);
            holderMyPollView4.imgdelete.setVisibility(View.VISIBLE);
            // holderMyPollView4.imgShareYouTubeUrlMyPoll.setImageDrawable(myPollActivity.getResources().getDrawable(R.drawable.arrow_down));

            //Binding the data into the views in android
            validateViewLayoutFour(holderMyPollView4, position);
            holderMyPollView4.imgShareYouTubeUrlMyPoll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareClickAction(position);
                }
            });
            holderMyPollView4.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(position,
                            myPollActivity.getResources().getString(R.string.txt_are_you_sure_you_want_to_delete_poll),
                            myPollActivity.getResources().getString(R.string.text_yes), myPollActivity.getResources().getString(R.string.text_no));
                }
            });

        }


        return mMyPollsView;
    }

    /**
     * Custom dialog to share and delete the poll
     *
     * @param clickPosition
     * @param view
     */
    private void customDialog(final int clickPosition, View view) {
        Context wrapper = new ContextThemeWrapper(myPollActivity, R.style.PopupMenu);
        //Android Popup Menu displays the menu below the anchor text if space is available otherwise above the anchor text. I
        //It disappears if you click outside the popup menu.
        PopupMenu mPopUP = new PopupMenu(wrapper, view);
        // use getLayoutInflater() or getSystemService(Class) to retrieve a standard LayoutInflater instance that is already hooked up to
        // the current context and correctly configured for the device you are running on.
        mPopUP.getMenuInflater().inflate(R.menu.menu_delete,
                mPopUP.getMenu());
        mPopUP.show();//Show the cuastom dialog
        mPopUP.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_share:
                        shareClickAction(clickPosition);//This method is used to share the poll
                        break;
                    case R.id.ic_delete:
                        showAlertDialog(clickPosition,
                                myPollActivity.getResources().getString(R.string.txt_are_you_sure_you_want_to_delete_poll),
                                myPollActivity.getResources().getString(R.string.text_yes), myPollActivity.getResources().getString(R.string.text_no));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * The Enum DIALOG_TYPE.
     */
    public enum DIALOG_TYPE {

        /**
         * The dialog single.
         */
        DIALOG_SINGLE,

        /**
         * The dialog dual.
         */
        DIALOG_DUAL
    }

    /**
     * Show alert dialog.
     *
     * @param
     * @param msg            the msg
     * @param positiveString the positive string
     * @param negativeString the negative string
     * @param
     * @param
     */
    public void showAlertDialog(final int mClickPosition, String msg, String positiveString, String negativeString) {
        //Creates a builder for an alert dialog that uses the default alert dialog theme.
        AlertDialog.Builder builder;

        if(Build.VERSION.SDK_INT >= 21)
        {
            builder = new AlertDialog.Builder(myPollActivity, R.style.AppCompatAlertDialogStyle);
        }
        else
        builder = new AlertDialog.Builder(myPollActivity);
        //set message
        builder.setMessage(msg);
        builder.setNegativeButton(negativeString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton(positiveString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteThePoll(mClickPosition);//This method is used to delete the poll
                        dialog.dismiss();
                    }
                });
        //create
        builder.create().show();
    }

    /**
     * Clicking the participate icon.
     *
     * @param clickPosition
     */
    private void clickAction(int clickPosition) {
        //Response from the server when the particular cell is clicked.Based on the click position data is retrived from the response.
        myPollResponse = getItem(clickPosition);
        //Polltype from the response
        pollType = myPollResponse.getPollType();
        //Poll id from the response
        pollId = myPollResponse.getId();
        //Load the participate count from  the saved prefernce
        preferencemyParticipateCount = MApplication.loadArray(myPollActivity, myParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE);
        //Setting the time in prefernce
        MApplication.setString(myPollActivity, Constants.DATE_UPDATED, MApplication.getTimeDifference(myPollResponse.getUpdatedAt()));
        //Setting the username in prefernce
        MApplication.setString(myPollActivity, Constants.CAMPAIGN_NAME, myPollResponse.getUserInfo().getUserName());
        //Setting the categoryname in preference
        MApplication.setString(myPollActivity, Constants.CAMPAIGN_CATEGORY, myPollResponse.getCategory().getCategory().getCategoryName());
        //Setting the userprofile in preference
        MApplication.setString(myPollActivity, Constants.CAMPAIGN_LOGO, myPollResponse.getUserInfo().getUserProfileImg());
        //It can be used with startActivity to launch an Activity.
        Intent a = new Intent(myPollActivity, MyPollsReview.class);
        //Pushing the values from one activity to another activity
        a.putExtra(Constants.POLL_TYPE, pollType);
        a.putExtra(Constants.POLL_ID, pollId);
        a.putExtra(Constants.TYPE, Constants.POLLS);
        a.putExtra(Constants.ARRAY_POSITION, clickPosition);
        a.putExtra(Constants.PARTICIPATE_COUNT, String.valueOf(preferencemyParticipateCount.get(clickPosition)));
        //Starting the activity
        myPollActivity.startActivity(a);
    }

    /**
     * Layout view 1
     *
     * @param holder            -view holders
     * @param position-position is used when reusing the views
     */
    private void validateViewLayoutOne(final ViewHolderLayoutOne holder, final int position) {
        prefrenceMyPollLikeCount = MApplication.loadArray(myPollActivity, myPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE); //Load the like count from  the saved prefernce
        prefrenceUserPollCommentsCount = MApplication.loadArray(myPollActivity, myPollcommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);  //Load the COMMENTS count from  the saved prefernce
        preferenceUserPollLikeUser = MApplication.loadArray(myPollActivity, myPollLikesUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE); //Load the like user from  the saved prefernce
        preferenceUserPollIdAnswer = MApplication.loadStringArray(myPollActivity, userPollIdAnswerCheck, Constants.MY_POLL_ID_ANSWER_ARRAY, Constants.MY_POLL_ID_ANSWER_SIZE);   //Load the poll id from  the saved prefernce
        preferenceUserPollIdAnswerSelected = MApplication.loadStringArray(myPollActivity, userPollIdAnswer, Constants.MY_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.MY_POLL_ID_SELECTED_SIZE);  //Load the answer from  the saved prefernce
        preferencemyParticipateCount = MApplication.loadArray(myPollActivity, myParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE); //Load the participate count  the saved prefernce
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceUserPollIdAnswer.contains(myPollResponse.getId())) {
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(myPollResponse.getId());
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            if (("Yes").equals(value)) {
                holder.radioYesMyPOll.setChecked(true);   //Setting the radio options true
                holder.radioYesMyPOll.setTextColor(Color.BLACK);  //Setting the text color black
            } else {
                holder.radioNoMyPoll.setTextColor(Color.BLACK);//Setting the text color black
                holder.radioNoMyPoll.setChecked(true); //Setting the radio options true
            }
            holder.radioNoMyPoll.setClickable(false); //Setting the clickable false
            holder.radioYesMyPOll.setClickable(false);  //Setting the clickable false
        }
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYesOrNoMyPoll.setChecked(true);
        } else {
            holder.likeUnlikeYesOrNoMyPoll.setChecked(false);
        }
        if (("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) { //question images is empty then the visiblity o fthe view will be gone
            holder.imageQuestion1YesOrNoMyPoll.setVisibility(View.GONE);
            holder.imageQuestion2YesOrNoMyPoll.setVisibility(View.GONE);
        } else {
            if (!("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) { //question images is not empty then the url is binded into the image view
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage1MyPoll, holder.imageQuestion1YesOrNoMyPoll, R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage2MyPoll, holder.imageQuestion2YesOrNoMyPoll, R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) {
                holder.singleOptionYesOrNoPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage1MyPoll, holder.singleOptionYesOrNoPoll, R.drawable.placeholder_image);
                holder.imageQuestion1YesOrNoMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoMyPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) {
                holder.singleOptionYesOrNoPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage2MyPoll, holder.singleOptionYesOrNoPoll, R.drawable.placeholder_image);
                holder.imageQuestion1YesOrNoMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoMyPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1YesOrNoMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2YesOrNoMyPoll.setVisibility(View.GONE);
            }
        }
        holder.txtQuestionYesOrNoPoll.setText(pollQuestion);  //setting the poll question in text view
        holder.yesOrNoLikeMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(position)));  //Setting the like count based on the position in the text view
        holder.radioYesMyPOll.setText(pollAnswer1MyPoll); //Setting the answer1 in ratio options
        holder.radioNoMyPoll.setText(pollAnswer2MyPoll);    //Setting the answer12in ratio options
        holder.txtNameYesOrNoMyPOll.setText(MApplication.getDecodedString(myPollResponse.getUserInfo().getUserName()));//Getting the user name from the response and binding the data into the text view
        holder.txtTimeYesOrNoMyPoll.setText(MApplication.getTimeDifference(myPollResponse.getUpdatedAt())); //Getting the time from the response and binding the data into the text view
        if(myPollResponse.getCategory()!=null)
        holder.txtCategoryYesOrNoMyPoll.setText(myPollResponse.getCategory().getCategory().getCategoryName()); //Getting the category name from the response and binding the data into the text view
        Utils.loadImageWithGlide(myPollActivity, myPollResponse.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"), holder.imgProfileYesOrNoMyPoll, R.drawable.img_ic_user);
        holder.txtCommentYesOrNoPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));  //Getting the COMMENTS name from the prefence and binding the data into the text view
        holder.txtYesOrNoCounts.setText(String.valueOf(preferencemyParticipateCount.get(position)));   //Setting the like count based on the position in the text view
        //OnClick listner
        holder1ViewOnClickListner(holder, position);
    }

    /**
     * holder1ViewOnClickListner
     *
     * @param holder
     * @param position
     */

    @SuppressLint("ClickableViewAccessibility")
    private void holder1ViewOnClickListner(final ViewHolderLayoutOne holder, final int position) {
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtYesOrNoCounts.setOnClickListener(mMyPollAdapterParticipateCounts);
        holder.yesOrNoLikeMyPoll.setOnClickListener(mMyPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentYesOrNoPoll.setOnClickListener(mMyPollAdapterCommentClickAction);
        holder.imageQuestion1YesOrNoMyPoll.setOnClickListener(mMyPollAdapterQuestionImage1);
        holder.imageQuestion2YesOrNoMyPoll.setOnClickListener(mMyPollAdapterQuestionImage2);
        holder.singleOptionYesOrNoPoll.setOnClickListener(mMyPollAdapterSingleOption);
        holder.radioYesMyPOll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    int clickPosition = position;  //Click position
                    myPollResponse = getItem(clickPosition); //Getting the item from the response
                    pollAnswerSelectedId = "1";//poll answer selected id is set as 1
                    pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer1(); //poll answer
                    optionSelection(clickPosition, holderMyPollView1, null, null, null, pollAnswerSelectedId, pollAnswer);
                }
                return false;
            }
        });
        holder.radioNoMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    int clickPosition = position;  //Click position
                    myPollResponse = getItem(clickPosition); //Getting the item from the response
                    pollAnswerSelectedId = "2";//poll answer selected id is set as 1
                    pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer2(); //poll answer
                    optionSelection(clickPosition, holderMyPollView1, null, null, null, pollAnswerSelectedId, pollAnswer);
                }
                return false;
            }
        });
        if (("1").equals(myPollResponse.getStatus())) {
            holder.txtNameYesOrNoMyPOll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_approved, 0);
            //////////////////
            holder.imgShareYesOrNoMyPOll.setVisibility(View.VISIBLE);
        } else {
            holder.txtNameYesOrNoMyPOll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pending, 0);
            holder.imgShareYesOrNoMyPOll.setVisibility(View.GONE);
        }
        // Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYesOrNoMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYesOrNoMyPoll.setChecked(true);
                    likesUnLikes(position, holder, null, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYesOrNoMyPoll.setChecked(false);
                    likesUnLikes(position, holder, null, null, null);
                }
            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holderMyPollView1.radioYesOrNOMyPOll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position;  //Click position
                myPollResponse = getItem(clickPosition); //Getting the item from the response
                if (checkedId == R.id.radioYes) {//If the view id matches the below fuctinolity will take place
                    pollAnswerSelectedId = "1";//poll answer selected id is set as 1
                    if(myPollResponse.getUserPollsAns().size()>0)
                    pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer1(); //poll answer
                    optionSelection(clickPosition, holderMyPollView1, null, null, null, pollAnswerSelectedId, pollAnswer); //Option selection request
                } else if (checkedId == R.id.radioNo) {
                    pollAnswerSelectedId = "2";//poll answer selected id is set as 2
                    if(myPollResponse.getUserPollsAns().size()>0)
                        pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer2(); //poll answer
                    optionSelection(clickPosition, holderMyPollView1, null, null, null, pollAnswerSelectedId, pollAnswer); //Option selection request
                }
            }
        });
        holderMyPollView1.imgShareYesOrNoMyPOll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Click position
                int clickPosition = position;
                // customDialog(clickPosition, view);

            }
        });
    }


    private void deleteThePoll(final int clickPosition) {
        myPollResponse = getItem(clickPosition);
        pollId = myPollResponse.getId();
        //Material dialog starts
        MApplication.materialdesignDialogStart(myPollActivity);
        /**  Requesting the response from the given base url**/
        PollDelete.getInstance().postPollDelete(new String("deletemypoll"), new String(userId), new String(pollId), new Callback<PollDeleteResponseModel>() {
            @Override
            public void success(PollDeleteResponseModel campaignCommentDelete, Response response) {
                //If the succes matches with the value 1 then the corresponding row is removed
                //else corresponding row wont remove
                if (("1").equals(campaignCommentDelete.getSuccess())) {
                    //remove the particular cell
                    dataResults.remove(clickPosition);
                    if (campaignCommentDelete.getResults().equals("0")) {
                        Log.e("click", "click");
                        noMyPollsResults.setVisibility(View.VISIBLE);
                        list.setVisibility(View.GONE);
                    } else {
                        list.setVisibility(View.VISIBLE);
                        noMyPollsResults.setVisibility(View.GONE);
                    }
                    //Notifies the attached observers that the underlying data has been changed
                    // and any View reflecting the data set should refresh itself.
                    notifyDataSetChanged();
                }
                Toast.makeText(myPollActivity, campaignCommentDelete.getMsg(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                //Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, myPollActivity);
            }
        });
        //Progress bar stops
        MApplication.materialdesignDialogStop();
    }


    /**
     * Layout view 2
     *
     * @param holder            -view holders
     * @param position-position is used when reusing the views
     */

    private void validateViewLayoutTwo(final ViewHolderLayoutTwo holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceMyPollLikeCount = MApplication.loadArray(myPollActivity, myPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceUserPollCommentsCount = MApplication.loadArray(myPollActivity, myPollcommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceUserPollLikeUser = MApplication.loadArray(myPollActivity, myPollLikesUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceUserPollIdAnswer = MApplication.loadStringArray(myPollActivity, userPollIdAnswerCheck, Constants.MY_POLL_ID_ANSWER_ARRAY, Constants.MY_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved preference
        preferenceUserPollIdAnswerSelected = MApplication.loadStringArray(myPollActivity, userPollIdAnswer, Constants.MY_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.MY_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved preference
        preferencemyParticipateCount = MApplication.loadArray(myPollActivity, myParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeMultipleOptionsMyPoll.setChecked(true);
        } else {
            holder.likeUnlikeMultipleOptionsMyPoll.setChecked(false);
        }
        //question images is empty then the visibility of the view will be gone
        if (("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) {
            holder.imageQuestion1MultipleOptionsMyPoll.setVisibility(View.GONE);
            holder.imageQuestion2MultipleOptionsMyPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) {
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage1MyPoll, holder.imageQuestion1MultipleOptionsMyPoll, R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage2MyPoll, holder.imageQuestion2MultipleOptionsMyPoll, R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) {
                holder.singleOptionMultipleOptionsMyPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage1MyPoll, holder.singleOptionMultipleOptionsMyPoll, R.drawable.placeholder_image);
                holder.imageQuestion1MultipleOptionsMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsMyPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) {
                holder.singleOptionMultipleOptionsMyPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage2MyPoll, holder.singleOptionMultipleOptionsMyPoll, R.drawable.placeholder_image);
                holder.imageQuestion1MultipleOptionsMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsMyPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1MultipleOptionsMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2MultipleOptionsMyPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1MyPoll)) {
            holder.radioOptions1MultipleOptionsMyPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer1MyPoll)) {
            holder.radioOptions1MultipleOptionsMyPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1MultipleOptionsMyPoll.setText(pollAnswer1MyPoll);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2MyPoll)) {
            holder.radioOptions2MultipleOptionsMyPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer2MyPoll)) {
            holder.radioOptions2MultipleOptionsMyPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2MultipleOptionsMyPoll.setText(pollAnswer2MyPoll);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3MyPoll)) {
            holder.radioOptions3MultipleOptionsMyPoll.setVisibility(View.GONE);
            holder.radioOptions4MultipleOptionsMyPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer3MyPoll)) {
            holder.radioOptions3MultipleOptionsMyPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3MultipleOptionsMyPoll.setText(pollAnswer3MyPoll);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4MyPoll)) {
                holder.radioOptions4MultipleOptionsMyPoll.setVisibility(View.GONE);
                //If the poll answer option is not empty then the view will invisible
            } else if (!("").equals(pollAnswer4MyPoll)) {
                holder.radioOptions4MultipleOptionsMyPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4MultipleOptionsMyPoll.setText(pollAnswer4MyPoll);
            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceUserPollIdAnswer.contains(myPollResponse.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(myPollResponse.getId());
            //Selected answer from the position
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (holder.radioOptions1MultipleOptionsMyPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions1MultipleOptionsMyPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions1MultipleOptionsMyPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions2MultipleOptionsMyPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions2MultipleOptionsMyPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions2MultipleOptionsMyPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions3MultipleOptionsMyPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions3MultipleOptionsMyPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions3MultipleOptionsMyPoll.setTextColor(Color.BLACK);
            } else if (holder.radioOptions4MultipleOptionsMyPoll.getText().equals(value)) {
                //Setting the radio options true
                holder.radioOptions4MultipleOptionsMyPoll.setChecked(true);
                //Setting the text color black
                holder.radioOptions4MultipleOptionsMyPoll.setTextColor(Color.BLACK);
            }
            //Setting the clickable false
            holder.radioOptions1MultipleOptionsMyPoll.setClickable(false);
            holder.radioOptions2MultipleOptionsMyPoll.setClickable(false);
            holder.radioOptions3MultipleOptionsMyPoll.setClickable(false);
            holder.radioOptions4MultipleOptionsMyPoll.setClickable(false);
        }

        //setting the poll question in text view
        holder.txtCommentsMultipleOptionsMyPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));
        //Setting the like count based on the position in the text view
        holder.txtLikeMulipleOptionsMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(position)));
        //Setting the user name in the textviw
        holder.txtNameMultipleOptionsMyPoll.setText(MApplication.getDecodedString(myPollResponse.getUserInfo().getUserName()));
        //Setting the updating to=ime in text view
        holder.txtTimeMultipleOptionsMyPoll.setText(MApplication.getTimeDifference(myPollResponse.getUpdatedAt()));
        //Setting the poll question in text view
        holder.txtQuestionMultipleOptionsMyPoll.setText(pollQuestion);
        holderView2MyPollOnClickListner(holder, position);


    }

    @SuppressLint("ClickableViewAccessibility")
    private void holderView2MyPollOnClickListner(final ViewHolderLayoutTwo holder, final int position) {
        if (("1").equals(myPollResponse.getStatus())) {
            holder.txtNameMultipleOptionsMyPoll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_approved, 0);
            holder.imgShareMultipleOptionsMyPoll.setVisibility(View.VISIBLE);
        } else {
            holder.txtNameMultipleOptionsMyPoll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pending, 0);
            holder.imgShareMultipleOptionsMyPoll.setVisibility(View.GONE);
        }
        //Setting the category intext view
        if (myPollResponse.getCategory()!=null)
        holder.txtCategoryMultipleOptionsMyPoll.setText(myPollResponse.getCategory().getCategory().getCategoryName());
        Utils.loadImageWithGlide(myPollActivity, myPollResponse.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"), holder.imgProfileMultipleOptionsMyPoll, R.drawable.img_ic_user);
        //Setting the text participate count from the array list based on the position
        holder.txtCountsMultipleOptionsMyPoll.setText(String.valueOf(preferencemyParticipateCount.get(position)));
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountsMultipleOptionsMyPoll.setOnClickListener(mMyPollAdapterParticipateCounts);
        holder.txtLikeMulipleOptionsMyPoll.setOnClickListener(mMyPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentsMultipleOptionsMyPoll.setOnClickListener(mMyPollAdapterCommentClickAction);
        holder.imageQuestion1MultipleOptionsMyPoll.setOnClickListener(mMyPollAdapterQuestionImage1);
        holder.imageQuestion2MultipleOptionsMyPoll.setOnClickListener(mMyPollAdapterQuestionImage2);
        holder.singleOptionMultipleOptionsMyPoll.setOnClickListener(mMyPollAdapterSingleOption);
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeMultipleOptionsMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeMultipleOptionsMyPoll.setChecked(true);
                    likesUnLikes(position, null, holder, null, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeMultipleOptionsMyPoll.setChecked(false);
                    likesUnLikes(position, null, holder, null, null);
                }

            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsMyPOll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int clickPosition = position;
                myPollResponse = getItem(clickPosition);
                optionClickAction(checkedId);
                optionSelection(clickPosition, null, holder, null, null, pollAnswerSelectedId, pollAnswer);
            }
        });
        holder.imgShareMultipleOptionsMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickPosition = position;
              //  customDialog(clickPosition, view);

            }
        });
        holder.radioOptions1MultipleOptionsMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    int clickPosition = position;
                    myPollResponse = getItem(clickPosition);
                    optionClickAction(view.getId());
                    optionSelection(clickPosition, null, holder, null, null, pollAnswerSelectedId, pollAnswer);
                }
                return false;
            }
        });
        holder.radioOptions2MultipleOptionsMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    int clickPosition = position;
                    myPollResponse = getItem(clickPosition);
                    optionClickAction(view.getId());
                    optionSelection(clickPosition, null, holder, null, null, pollAnswerSelectedId, pollAnswer);
                }
                return false;
            }
        });
        holder.radioOptions3MultipleOptionsMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    int clickPosition = position;
                    myPollResponse = getItem(clickPosition);
                    optionClickAction(view.getId());
                    optionSelection(clickPosition, null, holder, null, null, pollAnswerSelectedId, pollAnswer);
                }
                return false;
            }
        });
        holder.radioOptions4MultipleOptionsMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    int clickPosition = position;
                    myPollResponse = getItem(clickPosition);
                    optionClickAction(view.getId());
                    optionSelection(clickPosition, null, holder, null, null, pollAnswerSelectedId, pollAnswer);
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
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                break;
            case R.id.option2:
                //Setting the value for the variable
                pollAnswerSelectedId = "2";
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                break;
            case R.id.option3:
                //Setting the value for the variable
                pollAnswerSelectedId = "3";
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                break;
            case R.id.option4:
                //Setting the value for the variable
                pollAnswerSelectedId = "4";
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer4();
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
        //Response from the server when the particular cell is clicked.Based on the click position data is retrived from the response.
        myPollResponse = getItem(clickPosition);
        //Poll id from the response
        pollId = myPollResponse.getId();
        //Converting the poll id into base 64
        String base64CampaignId = MApplication.convertByteCode(pollId);
        //Total url
        String shareUrl = Constants.SHARE_POLL_BASE_URL.concat(base64CampaignId);
        //Sharing the url in gmail

        getSharedURL(shareUrl);



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
                            MApplication.shareGmail(myPollActivity, results, MApplication.getString(myPollActivity, Constants.PROFILE_USER_NAME));

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
     * This method is called when the user click the like count
     *
     * @param myPollResponse
     */

    private void likeClickAction(UserPollResponseModel.Results.Data myPollResponse) {
        //Poll id from the response
        String id = myPollResponse.getId();
        // It can be used with startActivity to launch an Activity
        Intent details = new Intent(myPollActivity, PollLikes.class);
        //Passing the id form one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Start Activity
        myPollActivity.startActivity(details);
    }

    /**
     * View hoder3
     *
     * @param holder            -view holder class
     * @param position-position
     */
    private void validateViewLayoutThree(final ViewHolderLayoutThree holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceMyPollLikeCount = MApplication.loadArray(myPollActivity, myPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceUserPollCommentsCount = MApplication.loadArray(myPollActivity, myPollcommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceUserPollLikeUser = MApplication.loadArray(myPollActivity, myPollLikesUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceUserPollIdAnswer = MApplication.loadStringArray(myPollActivity, userPollIdAnswerCheck, Constants.MY_POLL_ID_ANSWER_ARRAY, Constants.MY_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceUserPollIdAnswerSelected = MApplication.loadStringArray(myPollActivity, userPollIdAnswer, Constants.MY_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.MY_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferencemyParticipateCount = MApplication.loadArray(myPollActivity, myParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikePhotoComparisonMyPoll.setChecked(true);
        } else {
            holder.likeUnlikePhotoComparisonMyPoll.setChecked(false);
        }
        //question images is empty then the visiblity o fthe view will be gone
        if (("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) {
            holder.imageQuestion1PhotoComparisonMyPoll.setVisibility(View.GONE);
            holder.imageQuestion2PhotoComparisonMyPoll.setVisibility(View.GONE);
        } else {
            //question images is not empty then the url is binded into the image view
            if (!("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) {
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage1MyPoll, holder.imageQuestion1PhotoComparisonMyPoll, R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage2MyPoll, holder.imageQuestion2PhotoComparisonMyPoll, R.drawable.placeholder_image);
                //If the image1 question is not empty then image 2 is empty then
                //view is visible based on that
            } else if (!("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) {
                holder.singleOptionPhotoComparisonMyPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage1MyPoll, holder.singleOptionPhotoComparisonMyPoll, R.drawable.placeholder_image);
                holder.imageQuestion1PhotoComparisonMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonMyPoll.setVisibility(View.GONE);
                //If the image1 question is  empty then image 2 is not  empty then
                //view is visible based on that
            } else if (("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) {
                holder.singleOptionPhotoComparisonMyPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage2MyPoll, holder.singleOptionPhotoComparisonMyPoll, R.drawable.placeholder_image);
                holder.imageQuestion1PhotoComparisonMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonMyPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1PhotoComparisonMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2PhotoComparisonMyPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1MyPoll)) {
            holder.imageAnswer1PhotoComparisonMyPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer1MyPoll)) {
            Log.e("pollAnswer1MyPoll", pollAnswer1MyPoll + "");
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer1PhotoComparisonMyPoll.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(myPollActivity, pollAnswer1MyPoll.replaceAll(" ", "%20"), holder.imageAnswer1PhotoComparisonMyPoll, R.drawable.placeholder_image);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2MyPoll)) {
            holder.imageAnswer1PhotoComparisonMyPoll.setImageURI(Uri.parse(pollAnswer1MyPoll.replaceAll(" ", "%20")));
            holder.imageAnswer2PhotoComparisonMyPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2MyPoll)) {
            //If the poll answer option is not empty then the view will invisible
            holder.imageAnswer2PhotoComparisonMyPoll.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(myPollActivity, pollAnswer2MyPoll.replaceAll(" ", "%20"), holder.imageAnswer2PhotoComparisonMyPoll, R.drawable.placeholder_image);

        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3MyPoll)) {
            holder.imageAnswer3PhotoComparisonMyPoll.setVisibility(View.GONE);
            holder.imageAnswer4PhotoComparisonMyPoll.setVisibility(View.GONE);
            holder.relativeAnswer3PhotoComparisonMyPoll.setVisibility(View.GONE);
            holder.relativeAnswer4PhotoComparisonMyPoll.setVisibility(View.GONE);
            holder.imageAnswer4PhotoComparisonMyPoll.setVisibility(View.GONE);
            holder.relativeAnswer4PhotoComparisonMyPoll.setVisibility(View.GONE);
            //If the poll answer option is not empty then the view will invisible
        } else if (!("").equals(pollAnswer3MyPoll)) {
            holder.imageAnswer3PhotoComparisonMyPoll.setVisibility(View.VISIBLE);
            holder.relativeAnswer3PhotoComparisonMyPoll.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(myPollActivity, pollAnswer3MyPoll.replaceAll(" ", "%20"), holder.imageAnswer3PhotoComparisonMyPoll, R.drawable.placeholder_image);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4MyPoll)) {
                holder.imageAnswer4PhotoComparisonMyPoll.setVisibility(View.GONE);
                holder.relativeAnswer4PhotoComparisonMyPoll.setVisibility(View.GONE);
                //If the poll answer option is not empty then the view will invisible
            } else if (!("").equals(pollAnswer4MyPoll)) {
                holder.imageAnswer4PhotoComparisonMyPoll.setVisibility(View.VISIBLE);
                holder.relativeAnswer4PhotoComparisonMyPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideRounderCorner(myPollActivity, pollAnswer4MyPoll.replaceAll(" ", "%20"), holder.imageAnswer4PhotoComparisonMyPoll, R.drawable.placeholder_image);

            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceUserPollIdAnswer.contains(myPollResponse.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(myPollResponse.getId());
            //Selected answer from the position
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (myPollResponse.getUserPollsAns().get(0).getPollAnswer1().equals(value)) {
                holder.radioOptions1PhotoComparisonMyPoll.setChecked(true);
            } else if (myPollResponse.getUserPollsAns().get(0).getPollAnswer2().equals(value)) {
                holder.radioOptions2PhotoComparisonMyPoll.setChecked(true);
            } else if (myPollResponse.getUserPollsAns().get(0).getPollAnswer3().equals(value)) {
                holder.radioOptions3PhotoComparisonMyPoll.setChecked(true);
            } else if (myPollResponse.getUserPollsAns().get(0).getPollAnswer4().equals(value)) {
                holder.radioOptions4PhotoComparisonMyPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1PhotoComparisonMyPoll.setClickable(false);
            holder.radioOptions2PhotoComparisonMyPoll.setClickable(false);
            holder.radioOptions3PhotoComparisonMyPoll.setClickable(false);
            holder.radioOptions4PhotoComparisonMyPoll.setClickable(false);
        }
        //Setting the poll question intext view
        holder.txtQuestionPhotoComparisonMyPoll.setText(pollQuestion);
        //Setting the prefernce like count
        holder.txtLikePhotoComparisonMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(position)));
        //Setting the user name in textview
        holder.txtNamePhotoComparisonMyPoll.setText(MApplication.getDecodedString(myPollResponse.getUserInfo().getUserName()));
        //Setting the updated time
        holder.txtTimePhotoComparisonMyPoll.setText(MApplication.getTimeDifference(myPollResponse.getUpdatedAt()));
        //Setting the category name
        holder.txtCategoryPhotoComparisonMyPoll.setText(myPollResponse.getCategory().getCategory().getCategoryName());
        //Setting the user profile image
        Utils.loadImageWithGlide(myPollActivity, myPollResponse.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"), holder.imgProfilePhotoComparisonMyPoll, R.drawable.img_ic_user);
        //Setting the participate count from the arraylist based on the position
        holder.txtCountPhotoComparisonMyPoll.setText(String.valueOf(preferencemyParticipateCount.get(position)));
        //Setting the COMMENTS from the arraylist  based on the position
        holder.txtCommentsPhotoComparisonMyPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));
        holderView3OnClickListner(holder, position);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void holderView3OnClickListner(final ViewHolderLayoutThree holder, final int position) {
        //If status is 1 then the poll is approved
        if (("1").equals(myPollResponse.getStatus())) {
            holder.txtNamePhotoComparisonMyPoll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_approved, 0);
            holder.imgSharePhotoComparisonMyPoll.setVisibility(View.VISIBLE);
        } else {
            holder.txtNamePhotoComparisonMyPoll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pending, 0);
            holder.imgSharePhotoComparisonMyPoll.setVisibility(View.GONE);
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtCountPhotoComparisonMyPoll.setOnClickListener(mMyPollAdapterParticipateCounts);
        holder.imgSharePhotoComparisonMyPoll.setOnClickListener(mMyPollAdapterShareClickAction);
        holder.txtLikePhotoComparisonMyPoll.setOnClickListener(mMyPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentsPhotoComparisonMyPoll.setOnClickListener(mMyPollAdapterCommentClickAction);
        holder.imageQuestion1PhotoComparisonMyPoll.setOnClickListener(mMyPollAdapterQuestionImage1);
        holder.imageQuestion2PhotoComparisonMyPoll.setOnClickListener(mMyPollAdapterQuestionImage2);
        holder.singleOptionPhotoComparisonMyPoll.setOnClickListener(mMyPollAdapterSingleOption);
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer1PhotoComparisonMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Response from the server based on the position
                myPollResponse = getItem(clickPosition);
                //poll answer url
                pollAnswer1MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer1().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(clickPosition, pollAnswer1MyPoll);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer2PhotoComparisonMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                //Response from the server based on the position
                myPollResponse = getItem(position);
                //poll answer url
                pollAnswer2MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer2().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(position, pollAnswer2MyPoll);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer3PhotoComparisonMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                //Response from the server based on the position
                myPollResponse = getItem(position);
                //poll answer url
                pollAnswer3MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer3().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(position, pollAnswer3MyPoll);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.imageAnswer4PhotoComparisonMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                //Response from the server based on the position
                myPollResponse = getItem(position);
                //poll answer url
                pollAnswer4MyPoll = myPollResponse.getUserPollsAns().get(0).getPollAnswer4().replaceAll(" ", "%20");
                //Calling a method to on click
                imageQuestionClickAction(position, pollAnswer4MyPoll);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikePhotoComparisonMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikePhotoComparisonMyPoll.setChecked(true);
                    likesUnLikes(position, null, null, holder, null);
                } else {
                    mlikes = 0;
                    holder.likeUnlikePhotoComparisonMyPoll.setChecked(false);
                    likesUnLikes(position, null, null, holder, null);

                }

            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions1PhotoComparisonMyPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                // holder.radioOptions1PhotoComparisonMyPoll.setClickable(true);
                //Response from the server based on the position
                myPollResponse = getItem(position);
                //poll answer url
                pollAnswerSelectedId = "1";
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                optionSelection(position, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions2PhotoComparisonMyPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                //Response from the server based on the position
                myPollResponse = getItem(position);
                //poll answer url
                pollAnswerSelectedId = "2";
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                optionSelection(position, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions3PhotoComparisonMyPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                //Response from the server based on the position
                myPollResponse = getItem(position);
                //poll answer url
                pollAnswerSelectedId = "3";
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                optionSelection(position, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
            }

        });
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.radioOptions4PhotoComparisonMyPoll.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Click position
                //Response from the server based on the position
                myPollResponse = getItem(position);
                //poll answer url
                pollAnswerSelectedId = "4";
                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer4();
                optionSelection(position, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
            }

        });
        holder.imgSharePhotoComparisonMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // customDialog(clickPosition, view);

            }
        });
        holder.radioOptions1PhotoComparisonMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    //Response from the server based on the position
                    myPollResponse = getItem(position);
                    //poll answer url
                    pollAnswerSelectedId = "1";
                    pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer1();
                    optionSelection(position, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
                }
                return false;
            }
        });
        holder.radioOptions2PhotoComparisonMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    //Response from the server based on the position
                    myPollResponse = getItem(position);
                    //poll answer url
                    pollAnswerSelectedId = "1";
                    pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer2();
                    optionSelection(position, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
                }
                return false;
            }
        });
        holder.radioOptions3PhotoComparisonMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    holder.radioOptions2PhotoComparisonMyPoll.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (!view.isClickable()) {
                                int clickPosition = position;
                                //Response from the server based on the position
                                myPollResponse = getItem(clickPosition);
                                //poll answer url
                                pollAnswerSelectedId = "1";
                                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer3();
                                optionSelection(clickPosition, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
                            }
                            return false;
                        }
                    });
                }
                return false;
            }
        });
        holder.radioOptions4PhotoComparisonMyPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.isClickable()) {
                    holder.radioOptions2PhotoComparisonMyPoll.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (!view.isClickable()) {
                                int clickPosition = position;
                                //Response from the server based on the position
                                myPollResponse = getItem(clickPosition);
                                //poll answer url
                                pollAnswerSelectedId = "1";
                                pollAnswer = myPollResponse.getUserPollsAns().get(0).getPollAnswer4();
                                optionSelection(clickPosition, null, null, holder, null, pollAnswerSelectedId, pollAnswer);
                            }
                            return false;
                        }
                    });
                }
                return false;
            }
        });
    }

    private void validateViewLayoutFour(final ViewHolderLayoutFour holder, final int position) {
        //Load the like count from  the saved prefernce
        prefrenceMyPollLikeCount = MApplication.loadArray(myPollActivity, myPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
        //Load the COMMENTS count from  the saved prefernce
        prefrenceUserPollCommentsCount = MApplication.loadArray(myPollActivity, myPollcommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);
        //Load the like user from  the saved prefernce
        preferenceUserPollLikeUser = MApplication.loadArray(myPollActivity, myPollLikesUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
        //Load the poll id from  the saved prefernce
        preferenceUserPollIdAnswer = MApplication.loadStringArray(myPollActivity, userPollIdAnswerCheck, Constants.MY_POLL_ID_ANSWER_ARRAY, Constants.MY_POLL_ID_ANSWER_SIZE);
        //Load the answer from  the saved prefernce
        preferenceUserPollIdAnswerSelected = MApplication.loadStringArray(myPollActivity, userPollIdAnswer, Constants.MY_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.MY_POLL_ID_SELECTED_SIZE);
        //Load the participate count  the saved prefernce
        preferencemyParticipateCount = MApplication.loadArray(myPollActivity, myParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE);
        //If the array has 1,then the radio options is set checked as true
        //else it is set as false
        if (preferenceUserPollLikeUser.get(position).equals(1)) {
            holder.likeUnlikeYouTubeUrlMyPoll.setChecked(true);
        } else {
            holder.likeUnlikeYouTubeUrlMyPoll.setChecked(false);
        }
        //If the question image 1 and Question 2 is empty visiblity will be gone
        if (("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) {
            holder.imageQuestion1YouTubeUrlMyPoll.setVisibility(View.GONE);
            holder.imageQuestion2YouTubeUrlMyPoll.setVisibility(View.GONE);
        } else {
            //If the question image 1 and Question 2 is not empty visiblity will be visible
            if (!("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) {
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage1MyPoll, holder.imageQuestion1YouTubeUrlMyPoll, R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(myPollActivity, questionImage2MyPoll, holder.imageQuestion2YouTubeUrlMyPoll, R.drawable.placeholder_image);
                //If the question image 1 is  not empty visiblity will be visible
            } else if (!("").equals(questionImage1MyPoll) && ("").equals(questionImage2MyPoll)) {
                holder.singleOptionYouTubeUrlMyPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage1MyPoll, holder.singleOptionYouTubeUrlMyPoll, R.drawable.placeholder_image);
                holder.imageQuestion1YouTubeUrlMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlMyPoll.setVisibility(View.GONE);
                //If the question image 2 is not empty visiblity will be visible
            } else if (("").equals(questionImage1MyPoll) && !("").equals(questionImage2MyPoll)) {
                holder.singleOptionYouTubeUrlMyPoll.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideSingleImageRounderCorner(myPollActivity, questionImage2MyPoll, holder.singleOptionYouTubeUrlMyPoll, R.drawable.placeholder_image);
                holder.imageQuestion1YouTubeUrlMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlMyPoll.setVisibility(View.GONE);
            } else {
                holder.imageQuestion1YouTubeUrlMyPoll.setVisibility(View.GONE);
                holder.imageQuestion2YouTubeUrlMyPoll.setVisibility(View.GONE);
            }
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer1MyPoll)) {
            holder.radioOptions1YouTubeUrlMyPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2MyPoll)) {
            holder.radioOptions1YouTubeUrlMyPoll.setVisibility(View.VISIBLE);
            holder.radioOptions1YouTubeUrlMyPoll.setText(pollAnswer1MyPoll);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer2MyPoll)) {
            holder.radioOptions2YouTubeUrlMyPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2MyPoll)) {
            holder.radioOptions2YouTubeUrlMyPoll.setVisibility(View.VISIBLE);
            holder.radioOptions2YouTubeUrlMyPoll.setText(pollAnswer2MyPoll);
        }
        //If the poll answer option is empty then the view will invisible
        if (("").equals(pollAnswer3MyPoll)) {
            holder.radioOptions3YouTubeUrlMyPoll.setVisibility(View.GONE);
            holder.radioOptions3YouTubeUrlMyPoll.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer3MyPoll)) {
            holder.radioOptions3YouTubeUrlMyPoll.setVisibility(View.VISIBLE);
            holder.radioOptions3YouTubeUrlMyPoll.setText(pollAnswer3MyPoll);
            //If the poll answer option is empty then the view will invisible
            if (("").equals(pollAnswer4MyPoll)) {
                holder.radioOptions4YouTubeUrlMyPoll.setVisibility(View.GONE);
            } else if (!("").equals(pollAnswer4MyPoll)) {
                holder.radioOptions4YouTubeUrlMyPoll.setVisibility(View.VISIBLE);
                holder.radioOptions4YouTubeUrlMyPoll.setText(pollAnswer4MyPoll);

            }
        }
        //If the loaded poll id matches with the response id based on the position
        //then the radio option will be checked otherwise it woont be checked
        if (preferenceUserPollIdAnswer.contains(myPollResponse.getId())) {
            //Getting the position of the particular value saved in prefernce
            int answeredPosition = preferenceUserPollIdAnswer.indexOf(myPollResponse.getId());
            //Selected answer from the position
            String value = preferenceUserPollIdAnswerSelected.get(answeredPosition);
            //If the value matches then the radio option willbec cheked ,text color will be black and clickable will be false.
            if (myPollResponse.getUserPollsAns().get(0).getPollAnswer1().equals(value)) {
                holder.radioOptions1YouTubeUrlMyPoll.setChecked(true);
            } else if (myPollResponse.getUserPollsAns().get(0).getPollAnswer2().equals(value)) {
                holder.radioOptions2YouTubeUrlMyPoll.setChecked(true);
            } else if (myPollResponse.getUserPollsAns().get(0).getPollAnswer3().equals(value)) {
                holder.radioOptions3YouTubeUrlMyPoll.setChecked(true);
            } else if (myPollResponse.getUserPollsAns().get(0).getPollAnswer4().equals(value)) {
                holder.radioOptions4YouTubeUrlMyPoll.setChecked(true);
            }
            //Setting the clickable false
            holder.radioOptions1YouTubeUrlMyPoll.setClickable(false);
            holder.radioOptions2YouTubeUrlMyPoll.setClickable(false);
            holder.radioOptions3YouTubeUrlMyPoll.setClickable(false);
            holder.radioOptions4YouTubeUrlMyPoll.setClickable(false);
        }
        //Setting the like count in text view
        holder.txtLikeYouTubeUrlMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(position)));
        //Setting the user name in text view
        holder.txtNameYouTubeUrlMyPoll.setText(MApplication.getDecodedString(myPollResponse.getUserInfo().getUserName()));
        //Setting the updted time
        holder.txtTimeYouTubeUrlMyPoll.setText(MApplication.getTimeDifference(myPollResponse.getUpdatedAt()));
        //Setting the category name in text view
        holder.txtCategoryYouTubeUrlMyPoll.setText(myPollResponse.getCategory().getCategory().getCategoryName());
        //Setting the user profile image
        Utils.loadImageWithGlide(myPollActivity, myPollResponse.getUserInfo().getUserProfileImg().replaceAll(" ", "%20"), holder.imgProfileYouTubeUrlMyPoll, R.drawable.img_ic_user);
        //Setting the count from the arraylist
        holder.txtVideoUrlCountYouTubeUrlMyPoll.setText(String.valueOf(preferencemyParticipateCount.get(position)));
        //Setting the question  in text view
        holder.txtQuestionYouTubeUrlMyPoll.setText(pollQuestion);

        //Setting the COMMENTS from the arraylist
        holder.txtCommentsYouTubeUrlMyPoll.setText(String.valueOf(prefrenceUserPollCommentsCount.get(position)));

//Interface definition for a callback to be invoked when a view is clicked.
        holder.likeUnlikeYouTubeUrlMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the check box is checked then the int mLikes is set as 1 then the radio button will be checked as true
                //else radio option is checked as false
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;
                    holder.likeUnlikeYouTubeUrlMyPoll.setChecked(true);
                    likesUnLikes(position, null, null, null, holder);
                } else {
                    mlikes = 0;
                    holder.likeUnlikeYouTubeUrlMyPoll.setChecked(false);
                    likesUnLikes(position, null, null, null, holder);

                }

            }
        });
        holderView4OnClickListner(holder, position);
    }

    private void holderView4OnClickListner(final ViewHolderLayoutFour holder, final int position) {
        //If poll is approved by admin
        if (("1").equals(myPollResponse.getStatus())) {
            holder.txtNameYouTubeUrlMyPoll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_approved, 0);
            holder.imgShareYouTubeUrlMyPoll.setVisibility(View.VISIBLE);
        } else {
            holder.txtNameYouTubeUrlMyPoll.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pending, 0);
            holder.imgShareYouTubeUrlMyPoll.setVisibility(View.GONE);
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtVideoUrlCountYouTubeUrlMyPoll.setOnClickListener(mMyPollAdapterParticipateCounts);
        holder.imgShareYouTubeUrlMyPoll.setOnClickListener(mMyPollAdapterShareClickAction);
        holder.txtLikeYouTubeUrlMyPoll.setOnClickListener(mMyPollAdapterLikeUnlikeCheckBox);
        holder.txtCommentsYouTubeUrlMyPoll.setOnClickListener(mMyPollAdapterCommentClickAction);
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.radioGroupOptionsYouTubeUrlMyPoll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Click position
                int clickPosition = position;
                //Response
                myPollResponse = getItem(clickPosition);
                optionClickAction(checkedId);
                optionSelection(clickPosition, null, null, null, holder, pollAnswerSelectedId, pollAnswer);
            }
        });
        //Interface definition for a callback to be invoked when the checked state of a compound button changed.
        holder.singleOptionYouTubeUrlMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickPosition = position;    //Click ACTION
                myPollResponse = getItem(clickPosition);    //Response
                youTubeUrl = myPollResponse.getYouTubeUrl(); //Getting the url from the response
                MApplication.setString(myPollActivity, Constants.YOUTUBE_URL, youTubeUrl);//Setting in prefernce
                //If net is connected video is played
                //else toast message
                if (MApplication.isNetConnected((Activity) myPollActivity)) {
                    Intent a = new Intent(myPollActivity, VideoLandscapeActivity.class);
                    myPollActivity.startActivity(a);
                } else {
                    Toast.makeText(myPollActivity, myPollActivity.getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgShareYouTubeUrlMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickPosition = position;
               // customDialog(clickPosition, view);

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
    private void likesUnLikes(final int clickPosition, final ViewHolderLayoutOne holder1, final ViewHolderLayoutTwo holder2, final ViewHolderLayoutThree holder3, final ViewHolderLayoutFour holder4) {
        //Response from the server
        myPollResponse = getItem(clickPosition);
        //Getting the id
        pollId = myPollResponse.getId();
        MApplication.materialdesignDialogStart(myPollActivity);
        LikesAndUnLikeRestClient.getInstance().postCampaignPollLikes("poll_likes", userId, pollId, String.valueOf(mlikes)
                , new Callback<LikeUnLikeResposneModel>() {
                    @Override
                    public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                        //If the value from the response is 1 then the user has successfully liked the poll
                        if (("1").equals(likesUnlike.getResults())) {
                            //Changing the value in array list in a particular position
                            preferenceUserPollLikeUser.set(clickPosition, Integer.valueOf(1));
                            //Saving it in array
                            MApplication.saveArray(myPollActivity, preferenceUserPollLikeUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
                        } else {
                            //Changing the value in array list in a particular position
                            preferenceUserPollLikeUser.set(clickPosition, Integer.valueOf(0));
                            //Saving it in array
                            MApplication.saveArray(myPollActivity, preferenceUserPollLikeUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
                        }
                        //Toast message will display
                        Toast.makeText(myPollActivity, likesUnlike.getMsg(),
                                Toast.LENGTH_SHORT).show();
                        //Changing the value in array list in a particular position
                        prefrenceMyPollLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                        //Saving the array in preference
                        MApplication.saveArray(myPollActivity, prefrenceMyPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
                        //If holder1 is not null
                        if (holder1 != null) {
                            //Changing the value in textview
                            holder1.yesOrNoLikeMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(clickPosition)));
                            //If holder2 is not null
                        } else if (holder2 != null) {
                            //Changing the value in textview
                            holder2.txtLikeMulipleOptionsMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(clickPosition)));
                            //If holder3 is not null
                        } else if (holder3 != null) {
                            //Changing the value in textview
                            holder3.txtLikePhotoComparisonMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(clickPosition)));
                            //If holder4 is not null
                        } else if (holder4 != null) {
                            //Changing the value in textview
                            holder4.txtLikeYouTubeUrlMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(clickPosition)));
                        }
                        MApplication.materialdesignDialogStop();

                        notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        ////Error message popups when the user cannot able to coonect with the server
                        MApplication.errorMessage(retrofitError, myPollActivity);
                        MApplication.materialdesignDialogStop();
                    }
                });
    }

    /**
     * Selecting the poll answer request and response
     *
     * @param clickPosition        -position
     * @param holder1              -view holder
     * @param holder2              -view holder
     * @param holder3              -view holder
     * @param holder4              -view holder
     * @param pollAnswerSelectedId
     * @param pollAnswer
     */
    private void optionSelection(final int clickPosition, final ViewHolderLayoutOne holder1, final ViewHolderLayoutTwo holder2, final ViewHolderLayoutThree holder3, final ViewHolderLayoutFour holder4, String pollAnswerSelectedId, String pollAnswer) {
        //Response fromt server  is retrived based on the position
        myPollResponse = getItem(clickPosition);
        //Getting the id from the response
        pollId = myPollResponse.getId();
        String pollAnswerId="";
        //Getting the poll answer id from the response
        if(myPollResponse.getUserPollsAns().size()>0){
           pollAnswerId  = myPollResponse.getUserPollsAns().get(0).getId();
        }

        Log.e("userId", userId + "");
        Log.e("pollId", pollId + "");
        Log.e("pollAnswerId", pollAnswerId + "");
        Log.e("pollAnswer", pollAnswer + "");
        Log.e("pollAnswerSelectedId", pollAnswerSelectedId + "");
        PollParticipateRestClient.getInstance().postParticipateApi(new String("polls_participate"), userId, pollId, pollAnswerId, pollAnswer, pollAnswerSelectedId, "1", new Callback<PollParticipateResponseModel>() {
            @Override
            public void success(PollParticipateResponseModel pollParticipateResponseModel, Response response) {
                //If the success is 1 the data is binded into the views
                if (("1").equals(pollParticipateResponseModel.getSuccess())) {
                    //Adding the value in arraylist
                    preferenceUserPollIdAnswer.add(pollParticipateResponseModel.getResults().getPollId());
                    //Adding the value in arraylist
                    preferenceUserPollIdAnswerSelected.add(pollParticipateResponseModel.getResults().getPollAnswer());
                    //Incrementing the participate count on success 1
                    int participateCount = Integer.parseInt(myPollResponse.getPollParticipateCount()) + 1;
                    //Setting participate count in the arraylist
                    preferencemyParticipateCount.set(clickPosition, participateCount);
                    //Saving in preference
                    MApplication.saveArray(myPollActivity, preferencemyParticipateCount, Constants.MY_POLL_PARTICIPATE_COUNT_ARRAY, Constants.MY_POLL_PARTICIPATE_COUNT_SIZE);
                    //Saving in preference
                    MApplication.saveStringArray(myPollActivity, preferenceUserPollIdAnswer, Constants.MY_POLL_ID_ANSWER_ARRAY, Constants.MY_POLL_ID_ANSWER_SIZE);
                    //Saving in preference
                    MApplication.saveStringArray(myPollActivity, preferenceUserPollIdAnswerSelected, Constants.MY_POLL_ID_ANSWER_SELECTED_ARRAY, Constants.MY_POLL_ID_SELECTED_SIZE);
                    //If poll type equals 1
                    if (("1").equals(myPollResponse.getPollType())) {
                        //Setting the radio options as false
                        holder1.radioYesMyPOll.setClickable(false);
                        //Setting the radio options as false
                        holder1.radioNoMyPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder1.radioYesMyPOll.isChecked()) {
                            holder1.radioYesMyPOll.setTextColor(Color.BLACK);
                            //Setting the value in participate count
                            holder1.txtYesOrNoCounts.setText(String.valueOf(preferencemyParticipateCount.get(clickPosition)));
                        } else if (holder1.radioNoMyPoll.isChecked()) {
                            holder1.radioNoMyPoll.setTextColor(Color.BLACK);
                            //Setting the value in participate count
                            holder1.txtYesOrNoCounts.setText(String.valueOf(preferencemyParticipateCount.get(clickPosition)));
                        }
                        //If poll type equals 2
                    } else if (("2").equals(myPollResponse.getPollType())) {
                        //Setting the clickable as false
                        holder2.radioOptions1MultipleOptionsMyPoll.setClickable(false);
                        holder2.radioOptions2MultipleOptionsMyPoll.setClickable(false);
                        holder2.radioOptions3MultipleOptionsMyPoll.setClickable(false);
                        holder2.radioOptions4MultipleOptionsMyPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder2.radioOptions1MultipleOptionsMyPoll.isChecked()) {
                            holder2.radioOptions1MultipleOptionsMyPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions2MultipleOptionsMyPoll.isChecked()) {
                            holder2.radioOptions2MultipleOptionsMyPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions3MultipleOptionsMyPoll.isChecked()) {
                            holder2.radioOptions3MultipleOptionsMyPoll.setTextColor(Color.BLACK);
                        } else if (holder2.radioOptions4MultipleOptionsMyPoll.isChecked()) {
                            holder2.radioOptions4MultipleOptionsMyPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder2.txtCountsMultipleOptionsMyPoll.setText(String.valueOf(preferencemyParticipateCount.get(clickPosition)));
                    } else if (("3").equals(myPollResponse.getPollType())) {
                        //Setting the clickable as false
                        holder3.radioOptions1PhotoComparisonMyPoll.setClickable(false);
                        holder3.radioOptions2PhotoComparisonMyPoll.setClickable(false);
                        holder3.radioOptions3PhotoComparisonMyPoll.setClickable(false);
                        holder3.radioOptions4PhotoComparisonMyPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder3.radioOptions1PhotoComparisonMyPoll.isEnabled()) {
                            holder3.radioOptions1PhotoComparisonMyPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions2PhotoComparisonMyPoll.isEnabled()) {
                            holder3.radioOptions2PhotoComparisonMyPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions3PhotoComparisonMyPoll.isEnabled()) {
                            holder3.radioOptions3PhotoComparisonMyPoll.setTextColor(Color.BLACK);
                        } else if (holder3.radioOptions4PhotoComparisonMyPoll.isEnabled()) {
                            holder3.radioOptions4PhotoComparisonMyPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder3.txtCountPhotoComparisonMyPoll.setText(String.valueOf(preferencemyParticipateCount.get(clickPosition)));
                    } else if (("4").equals(myPollResponse.getPollType())) {
                        holder4.radioOptions1YouTubeUrlMyPoll.setClickable(false);
                        holder4.radioOptions2YouTubeUrlMyPoll.setClickable(false);
                        holder4.radioOptions3YouTubeUrlMyPoll.setClickable(false);
                        holder4.radioOptions4YouTubeUrlMyPoll.setClickable(false);
                        //If it is checked,then the text view sis set as black
                        if (holder4.radioOptions1YouTubeUrlMyPoll.isChecked()) {
                            holder4.radioOptions1YouTubeUrlMyPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions2YouTubeUrlMyPoll.isChecked()) {
                            holder4.radioOptions2YouTubeUrlMyPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions3YouTubeUrlMyPoll.isChecked()) {
                            holder4.radioOptions3YouTubeUrlMyPoll.setTextColor(Color.BLACK);
                        } else if (holder4.radioOptions4YouTubeUrlMyPoll.isChecked()) {
                            holder4.radioOptions4YouTubeUrlMyPoll.setTextColor(Color.BLACK);
                        }
                        //Setting the value in participate count
                        holder4.txtVideoUrlCountYouTubeUrlMyPoll.setText(String.valueOf(preferencemyParticipateCount.get(clickPosition)));
                    }
                    //Toast message will display
                    Toast.makeText(myPollActivity, pollParticipateResponseModel.getMsg(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(myPollActivity, pollParticipateResponseModel.getMsg(),
                            Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, myPollActivity);
            }

        });
    }


    /**
     * Image full view method
     *
     * @param clickPosition-position
     * @param pollImageMyPoll-image  url
     */
    private void imageQuestionClickAction(int clickPosition, String pollImageMyPoll) {
        //response from the server
        myPollResponse = getItem(clickPosition);
        //Setting the time in prefernce
        MApplication.setString(myPollActivity, Constants.DATE_UPDATED, MApplication.getTimeDifference(myPollResponse.getUpdatedAt()));
        //Setting the name in prefernce
        MApplication.setString(myPollActivity, Constants.CAMPAIGN_NAME, myPollResponse.getUserInfo().getUserName());
        //Setting the category in prefernce
        MApplication.setString(myPollActivity, Constants.CAMPAIGN_CATEGORY, myPollResponse.getCategory().getCategory().getCategoryName());
        //Setting the logo in prefernce
        MApplication.setString(myPollActivity, Constants.CAMPAIGN_LOGO, myPollResponse.getUserInfo().getUserProfileImg());
        //Moving from one activity to another activity
        Intent a = new Intent(myPollActivity, FullImageView.class);
        //Passing the value from one activity to another
        a.putExtra(Constants.QUESTION1, pollImageMyPoll.replaceAll(" ", "%20"));
        //Starting the activity
        myPollActivity.startActivity(a);
    }

    /**
     * Comment click ACTION
     *
     * @param clickPosition-position
     */
    private void commentClickAction(int clickPosition) {
        //response from the server
        myPollResponse = getItem(clickPosition);
        String id = myPollResponse.getId();
        //Moving from one activity to another activity
        Intent details = new Intent(myPollActivity, MyPollComments.class);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_ID, id);
        //Passing the value from one activity to another
        details.putExtra(Constants.POLL_TYPE, myPollResponse.getPollType());
        //Passing the value from one activity to another
        details.putExtra(Constants.COMMENTS_COUNT_POSITION, clickPosition);
        //Starting the activity
        myPollActivity.startActivity(details);
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutOne {
        //text view
        private TextView yesOrNoLikeMyPoll;
        //Radio group
        private RadioGroup radioYesOrNOMyPOll;
        //Radio button
        private RadioButton radioYesMyPOll;
        //Radio button
        private RadioButton radioNoMyPoll;
        //Check box like unlike
        private CheckBox likeUnlikeYesOrNoMyPoll;
        //text view name
        private TextView txtNameYesOrNoMyPOll;
        //Text view time
        private TextView txtTimeYesOrNoMyPoll;
        //Text view category
        private TextView txtCategoryYesOrNoMyPoll;
        //Image view
        private ImageView imgProfileYesOrNoMyPoll;
        //SimpleDraweeView view
        private ImageView imageQuestion1YesOrNoMyPoll;
        //SimpleDraweeView view
        private ImageView imageQuestion2YesOrNoMyPoll;
        //SimpleDraweeView view
        private ImageView singleOptionYesOrNoPoll;
        //TextView question
        private TextView txtQuestionYesOrNoPoll;
        //Text view comment
        private TextView txtCommentYesOrNoPoll;
        //Textview particcipate count
        private TextView txtYesOrNoCounts;
        //Image view share
        private ImageView imgShareYesOrNoMyPOll,imgdelete;
    }

    /**
     * A ViewHolderLayoutTwo object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutTwo {
        //text view
        private TextView txtLikeMulipleOptionsMyPoll;
        //Radio group
        private RadioGroup radioGroupOptionsMyPOll;
        //Radio button
        private RadioButton radioOptions1MultipleOptionsMyPoll;
        //Radio button
        private RadioButton radioOptions2MultipleOptionsMyPoll;
        //Radio button
        private RadioButton radioOptions3MultipleOptionsMyPoll;
        //Radio button
        private RadioButton radioOptions4MultipleOptionsMyPoll;
        //Chek box
        private CheckBox likeUnlikeMultipleOptionsMyPoll;
        //Simple drawer view
        private ImageView imageQuestion1MultipleOptionsMyPoll;
        //Simple drawer view
        private ImageView imageQuestion2MultipleOptionsMyPoll;
        //Simple drawer view
        private ImageView singleOptionMultipleOptionsMyPoll;
        //Text view
        private TextView txtQuestionMultipleOptionsMyPoll;
        //Text view
        private TextView txtCommentsMultipleOptionsMyPoll;
        //Text view
        private TextView txtCountsMultipleOptionsMyPoll;
        //Text view
        private TextView txtNameMultipleOptionsMyPoll;
        //Text view
        private TextView txtTimeMultipleOptionsMyPoll;
        //Text view
        private TextView txtCategoryMultipleOptionsMyPoll;
        //ImageView
        private ImageView imgProfileMultipleOptionsMyPoll;
        //ImageView
        private ImageView imgShareMultipleOptionsMyPoll,imgdelete;
    }

    /**
     * A ViewHolderLayoutThree object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutThree {
        //Simple drawer view
        private ImageView imageAnswer1PhotoComparisonMyPoll;
        //Simple drawer view
        private ImageView imageAnswer2PhotoComparisonMyPoll;
        //Simple drawer view
        private ImageView imageAnswer3PhotoComparisonMyPoll;
        //Simple drawer view
        private ImageView imageAnswer4PhotoComparisonMyPoll;
        //Relative layout
        private RelativeLayout relativeAnswer3PhotoComparisonMyPoll;
        //Relative layout
        private RelativeLayout relativeAnswer4PhotoComparisonMyPoll;
        //Checkbox
        private CheckBox likeUnlikePhotoComparisonMyPoll;
        //Text view
        private TextView txtLikePhotoComparisonMyPoll;
        //Radio button
        private RadioButton radioOptions1PhotoComparisonMyPoll;
        //Radio button
        private RadioButton radioOptions2PhotoComparisonMyPoll;
        //Radio button
        private RadioButton radioOptions3PhotoComparisonMyPoll;
        //Radio button
        private RadioButton radioOptions4PhotoComparisonMyPoll;
        //Imageview
        private ImageView imageQuestion1PhotoComparisonMyPoll;
        //Imageview
        private ImageView imageQuestion2PhotoComparisonMyPoll;
        //Imageview
        private ImageView singleOptionPhotoComparisonMyPoll;
        // Textview
        private TextView txtQuestionPhotoComparisonMyPoll;
        // Textview
        private TextView txtCommentsPhotoComparisonMyPoll;
        // Textview
        private TextView txtCountPhotoComparisonMyPoll;
        // Textview
        private TextView txtNamePhotoComparisonMyPoll;
        // Textview
        private TextView txtTimePhotoComparisonMyPoll;
        // Textview
        private TextView txtCategoryPhotoComparisonMyPoll;
        //Imageview
        private ImageView imgProfilePhotoComparisonMyPoll;
        //Imageview
        private ImageView imgSharePhotoComparisonMyPoll,imgdelete;
    }

    /**
     * A ViewHolderLayoutFour object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolderLayoutFour {
        //Text view
        private TextView txtLikeYouTubeUrlMyPoll;
        //RadioGroup
        private RadioGroup radioGroupOptionsYouTubeUrlMyPoll;
        //RadioButton
        private RadioButton radioOptions1YouTubeUrlMyPoll;
        //RadioButton
        private RadioButton radioOptions2YouTubeUrlMyPoll;
        //RadioButton
        private RadioButton radioOptions3YouTubeUrlMyPoll;
        //RadioButton
        private RadioButton radioOptions4YouTubeUrlMyPoll;
        //Simple drawer view
        private ImageView imageQuestion1YouTubeUrlMyPoll;
        //Simple drawer view
        private ImageView imageQuestion2YouTubeUrlMyPoll;
        //Simple drawer view
        private ImageView singleOptionYouTubeUrlMyPoll;
        //Checkbox
        private CheckBox likeUnlikeYouTubeUrlMyPoll;
        //TextView
        private TextView txtQuestionYouTubeUrlMyPoll;
        //TextView
        private TextView txtCommentsYouTubeUrlMyPoll;
        //TextView
        private TextView txtVideoUrlCountYouTubeUrlMyPoll;
        //TextView
        private TextView txtNameYouTubeUrlMyPoll;
        //TextView
        private TextView txtTimeYouTubeUrlMyPoll;
        //TextView
        private TextView txtCategoryYouTubeUrlMyPoll;
        //TextView
        private ImageView imgProfileYouTubeUrlMyPoll;
        //ImageView
        private ImageView imgShareYouTubeUrlMyPoll,imgdelete;
    }


}


