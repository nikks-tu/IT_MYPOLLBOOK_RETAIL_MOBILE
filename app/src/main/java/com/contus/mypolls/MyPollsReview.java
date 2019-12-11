package com.contus.mypolls;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.likes.PollLikes;
import com.contus.participation.Participation;
import com.contus.pollreview.PollReviewQuestionParticipation;
import com.contus.responsemodel.LikeUnLikeResposneModel;
import com.contus.responsemodel.PollReviewResponseModel;
import com.contus.restclient.LikesAndUnLikeRestClient;
import com.contus.restclient.PollReviewsRestClient;
import com.contus.userpolls.FullImageView;
import com.contus.views.VideoLandscapeActivity;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdView;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 10/26/2015.
 */
public class MyPollsReview extends Activity {
    //Userpoll review
    private MyPollsReview myPollsReviewActivity;
    //user poll type
    private String myPollType;
    //poll id
    private String pollIdMyPoll;
    //user poll image1
    private String pollImage1;
    //user poll image2
    private String pollImage2;
    //pollOptionParticipated
    private List<PollReviewResponseModel.Results.PollReview.ParticipatePoll> pollOptionParticipated;
    //myPOllName
    private String myPOllName;
    //myPollCategory
    private String myPollCategory;
    //myPollUserLogo
    private String myPollUserLogo;
    //user poll updated time
    private String myPollUpdatedTime;
    //user poll type
    private String typeMyPoll;
    //position
    private int listpositionMyPoll;
    //prefrenceMyPollLikeCount
    private ArrayList<Integer> prefrenceMyPollLikeCount;
    //campaignPollLikeCount
    private ArrayList<Integer> campaignPollLikeCount = new ArrayList<Integer>();
    //campaignPollLikesUser
    private ArrayList<Integer> campaignPollLikesUser = new ArrayList<Integer>();
    //campaignPollcommentsCount
    private ArrayList<Integer> campaignPollcommentsCount = new ArrayList<Integer>();
    //prefrenceMyPollCommentsCount
    private ArrayList<Integer> prefrenceMyPollCommentsCount;
    //preferenceMyPollLikeUser
    private ArrayList<Integer> preferenceMyPollLikeUser;
    //likeCountMyPoll
    private String likeCountMyPoll;
    //commentsCountMyPoll
    private String commentsCountMyPoll;
    //likeUserMyPoll
    private String likeUserMyPoll;
    //userId

    private Boolean Shareid;
    private String userId;
    //mlikes
    private int mlikes;
    //Adview
    private AdView mAdView;
    //participate count
    private String participateCountMyPoll;
    //pollAnswer1
    private String pollAnswer1;
    //pollAnswer2
    private String pollAnswer2;
    //pollAnswer3
    private String pollAnswer3;
    //pollAnswer4
    private String pollAnswer4;
    //View holder
    private ViewHolder4 myPollView4;
    //txtcomments
    private TextView txtCommentsMyPoll;
    //view holder
    private ViewHolder1 myPollView1;
    //view holder
    private ViewHolder3 myPollView3;
    //view holder
    private ViewHolder2 myPollView2;
    //like
    private TextView txtLikeMyPoll;
    //image
    private String image;
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollParticipantCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, Participation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, pollIdMyPoll);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollLikeCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent details = new Intent(myPollsReviewActivity, PollLikes.class);
            //Passing the value from one activity to another
            details.putExtra(Constants.POLL_ID, pollIdMyPoll);
            //starting the activity
            myPollsReviewActivity.startActivity(details);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollSharePoll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            String base64CampaignId = MApplication.convertByteCode(pollIdMyPoll);
            //share url
            String shareUrl = Constants.SHARE_POLL_BASE_URL.concat(base64CampaignId);
            //sharing in gmail
            MApplication.shareGmail(myPollsReviewActivity, shareUrl, MApplication.getString(myPollsReviewActivity, Constants.PROFILE_USER_NAME));
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, pollImage1);
            //starting the activity
            myPollsReviewActivity.startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, pollImage2);
            //starting the activity
            myPollsReviewActivity.startActivity(a);
        }
    };
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollAnswer1Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "1");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, pollIdMyPoll);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollAnswer2Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "2");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, pollIdMyPoll);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollAnswer3Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "3");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, pollIdMyPoll);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollAnswer4Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "4");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, pollIdMyPoll);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener myPollsingleImageView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (("").equals(pollImage2)) {
                image = pollImage1;
            } else {
                image = pollImage2;
            }
            //Moving from one activity to another activity
            Intent a = new Intent(myPollsReviewActivity, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, image);
            //starting the activity
            myPollsReviewActivity.startActivity(a);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Get the user poll type from another activity
        myPollType = getIntent().getExtras().getString(Constants.POLL_TYPE);
        if(("1").equals(myPollType)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_firstview);
        }else if(("2").equals(myPollType)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_secondview);
        }else if(("3").equals(myPollType)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_thirdview);
        }else if(("4").equals(myPollType)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_fourthview);
        }
        /**View are creating when the activity is started**/
        init();
    }
    /**
     * Instantiate the method
     */
    public void init() {
        /**Initializing the activity**/
        myPollsReviewActivity = this;

        Shareid=getIntent().getExtras().getBoolean("SHARE",false);
        //Retrieves a map of extended data from the intent.
        pollIdMyPoll = getIntent().getExtras().getString(Constants.POLL_ID);
        //Retrieves a map of extended data from the intent.
        typeMyPoll = getIntent().getExtras().getString(Constants.TYPE);
        //Retrieves a map of extended data from the intent.
        listpositionMyPoll =getIntent().getExtras().getInt(Constants.ARRAY_POSITION);
        //user id from the prefernce
        userId = MApplication.getString(myPollsReviewActivity, Constants.USER_ID);
        //participate count
        participateCountMyPoll =getIntent().getExtras().getString(Constants.PARTICIPATE_COUNT);
        //name
        myPOllName = MApplication.getString(myPollsReviewActivity, Constants.CAMPAIGN_NAME);
        //category
        myPollCategory = MApplication.getString(myPollsReviewActivity, Constants.CAMPAIGN_CATEGORY);
        //logo
        myPollUserLogo = MApplication.getString(myPollsReviewActivity, Constants.CAMPAIGN_LOGO);
        //date updated
        myPollUpdatedTime = MApplication.getString(myPollsReviewActivity, Constants.DATE_UPDATED);
        //User poll like count saved in the prefernce is loaded into the array list
        prefrenceMyPollLikeCount = MApplication.loadArray(myPollsReviewActivity, campaignPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
        //User poll comments saved in the prefernce is loaded into the array list
        prefrenceMyPollCommentsCount = MApplication.loadArray(myPollsReviewActivity, campaignPollcommentsCount, Constants.MY_POLL_COMMENTS_COUNT, Constants.MY_POLL_COMMENTS_SIZE);
        //User poll like user saved in the prefernce is loaded into the array list
        preferenceMyPollLikeUser = MApplication.loadArray(myPollsReviewActivity, campaignPollLikesUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
        //Getting the value from the particular position in array list
        likeCountMyPoll = String.valueOf(prefrenceMyPollLikeCount.get(listpositionMyPoll));
        //Getting the value from the particular position in array list
        commentsCountMyPoll = String.valueOf(prefrenceMyPollCommentsCount.get(listpositionMyPoll));
        //Getting the value from the particular position in array list
        likeUserMyPoll = String.valueOf(preferenceMyPollLikeUser.get(listpositionMyPoll));
        //Request for the poll survey details
        pollReviewRequest();
        //Setting the comments count in prefernce
        MApplication.setString(myPollsReviewActivity, Constants.COMMENTS_COUNT_POLL_REVIEW, commentsCountMyPoll);
        mAdView = (AdView) findViewById(R.id.adView);
        //Google adview
        MApplication.googleAd(mAdView);
        //Initailizing the views
        txtCommentsMyPoll = (TextView)findViewById(R.id.txtCommentsCounts);

        txtLikeMyPoll = (TextView)findViewById(R.id.txtLike2);
        //Register a callback to be invoked when this view is clicked.
        txtCommentsMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    An intent is an abstract description of an operation to be performed. It can be used with startActivity to
                // launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components
                Intent details = new Intent(myPollsReviewActivity, MyPollComments.class);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_ID, pollIdMyPoll);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_TYPE, myPollType);
                //Add extended data to the intent.
                details.putExtra(Constants.COMMENTS_COUNT_POSITION, listpositionMyPoll);
                //Launch a new activity.
                myPollsReviewActivity.startActivity(details);
            }
        });
    }


    private void pollReviewRequest() {
        if (MApplication.isNetConnected(myPollsReviewActivity)) {  /**If internet connection is available**/
            MApplication.materialdesignDialogStart(this);
            PollReviewsRestClient.getInstance().pollReview(new String("poll_review_polls"), new String(pollIdMyPoll), new String(typeMyPoll), new Callback<PollReviewResponseModel>() {
                @Override
                public void success(PollReviewResponseModel pollReviewResponseModel, Response response) {
                    myPollType = pollReviewResponseModel.getResults().getPollreview().get(0).getPollType();    //user poll type
                    if(("1").equals(myPollType)){
                        firstReview(pollReviewResponseModel);
                    }else if(("2").equals(myPollType)){
                        secondReview(pollReviewResponseModel);
                    }else if(("3").equals(myPollType)){
                        thirdReview(pollReviewResponseModel);
                    }else if(("4").equals(myPollType)){
                        fourthReview(pollReviewResponseModel);
                    }
                    MApplication.materialdesignDialogStop();
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, myPollsReviewActivity);
                    MApplication.materialdesignDialogStop();
                }
            });
        }
    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 4 this view is called.
     * @param pollResponseModelView4
     */
    private void fourthReview(final PollReviewResponseModel pollResponseModelView4) {
        //view holder
        myPollView4 =new ViewHolder4();
        //Initializing the views.
        myPollView4.scrollViewYouTubeLayout =(ScrollView)findViewById(R.id.scroll);
        myPollView4.myPollTxtQuestionYouTubeLayout = (TextView) findViewById(R.id.txtStatus);
        myPollView4.myPollimageQuestion1YouTubeLayout = (SimpleDraweeView)findViewById(R.id.choose);
        myPollView4.myPollimageQuestion2YouTubeLayout = (SimpleDraweeView)findViewById(R.id.ChooseAdditional);
        myPollView4.myPollsingleOptionYouTubeLayout = (SimpleDraweeView)findViewById(R.id.singleOption);
        myPollView4.myPollHeaderImageYouTubeLayout = (ImageView)findViewById(R.id.imgProfile);
        myPollView4.myPolltxtTimeYouTubeLayout = (TextView)findViewById(R.id.txtTime);
        myPollView4.myPollHeaderNameYouTubeLayout = (TextView)findViewById(R.id.txtName);
        myPollView4.myPollHeaderCategoryYouTubeLayout = (TextView)findViewById(R.id.txtCategory);
        myPollView4.likeUnlikeYouTubeLayout = (CheckBox)findViewById(R.id.unLikeDislike);
        myPollView4.myPolltxtFirstOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtFirstOptionCount);
        myPollView4.myPolltxtSecondOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtSecondOptionCount);
        myPollView4.myPolltxtThirdOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtThirdOptionCount);
        myPollView4.myPolltxtFourthOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtFourthOptionCount);
        myPollView4.myPollprogressbarFirstOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarFirstOption);
        myPollView4.myPollprogressbarSecondOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarSecondOption);
        myPollView4.myPollprogressbarThirdOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarThirdOption);
        myPollView4.myPollprogressbarFourthOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarFourthOption);
        myPollView4.myPollrelativePrgressBar3YouTubeLayout =(RelativeLayout)findViewById(R.id.relativeProgressbar3);
        myPollView4.myPollrelativePrgressBar4YouTubeLayout =(RelativeLayout)findViewById(R.id.relativeProgressbar4);
        myPollView4.myPolltxtCountsYouTubeLayout = (TextView) findViewById(R.id.txtParticcipation);
        myPollView4.imgShareYouTubeLayout =(ImageView)findViewById(R.id.imgShare);

        if(Shareid){
            myPollView4.imgShareYouTubeLayout.setVisibility(View.GONE);
        }else{
            myPollView4.imgShareYouTubeLayout.setVisibility(View.VISIBLE);
        }


        myPollView4.txtLikeYouTubeLayout = (TextView)findViewById(R.id.txtLike2);
        myPollView4.myPolloption1YouTubeLayoutYouTubeLayout = (TextView)findViewById(R.id.option1);
        myPollView4.myPolloption2YouTubeLayout = (TextView) findViewById(R.id.option2);
        myPollView4.myPolloption3YouTubeLayout = (TextView)findViewById(R.id.option3);
        myPollView4.myPolloption4YouTubeLayout = (TextView)findViewById(R.id.option4);
        myPollView4.myPollGroupAnswer1YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer1);
        myPollView4.myPollGroupAnswer2YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer2);
        myPollView4.myPollGroupAnswer3YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer3);
        myPollView4.myPollGroupAnswer4YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer4);
        //Setting the visiblity view.
        myPollView4.scrollViewYouTubeLayout.setVisibility(View.VISIBLE);
        //Getting the poll question image if available
        pollImage1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //Getting the poll question image if available
        pollImage2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        //Setting the participate count in text view
        myPollView4.myPolltxtCountsYouTubeLayout.setText(participateCountMyPoll);
        Utils.loadImageWithGlideProfileRounderCorner(this,myPollUserLogo.replaceAll(" ", "%20"),myPollView4.myPollHeaderImageYouTubeLayout,R.drawable.placeholder_image);
        //user poll name
        myPollView4.myPollHeaderNameYouTubeLayout.setText(MApplication.getDecodedString(myPOllName));
        //Category is set into the text view
        myPollView4.myPollHeaderCategoryYouTubeLayout.setText(myPollCategory);
        //User poll updated time in text view
        myPollView4.myPolltxtTimeYouTubeLayout.setText(myPollUpdatedTime);
        //Setting the like in text view
        myPollView4.txtLikeYouTubeLayout.setText(likeCountMyPoll);
        //Setting the question in text view
        myPollView4.myPollTxtQuestionYouTubeLayout.setText(pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestion());
        //Setting the comment count in text view
        txtCommentsMyPoll.setText(MApplication.getString(myPollsReviewActivity, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(likeUserMyPoll)) {
            myPollView4.likeUnlikeYouTubeLayout.setChecked(true);
        } else {
            myPollView4.likeUnlikeYouTubeLayout.setChecked(false);
        }
        //poll answer option from the response
        pollAnswer1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        pollAnswer2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        pollAnswer3 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option from the response
        pollAnswer4 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        pollOptionParticipated = pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; pollOptionParticipated.size() > i; i++) {
            //Get the poll counts from the response
            String mPollCount=pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollCounts();
            //Converting the poll counts into the float value
            float pollCount = Float.parseFloat(mPollCount);
            //Get the poll total count
            float totalPollCount = Float.valueOf(pollResponseModelView4.getResults().getPollTotalCount());
            //Total percentage value of the poll
            float percentage = pollCount * 100 / totalPollCount;
            //Converting the value into the double
            Double d = new Double(percentage);
            //percentage
            int mPercentage = d.intValue();
            //If the poll answer option matches with 1
            if (("1").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView4.myPolltxtFirstOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView4.myPollprogressbarFirstOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView4.myPollGroupAnswer1YouTubeLayout.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView4.myPolltxtSecondOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView4.myPollprogressbarSecondOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView4.myPollGroupAnswer2YouTubeLayout.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView4.myPolltxtThirdOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView4.myPollprogressbarThirdOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView4.myPollGroupAnswer3YouTubeLayout.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView4.myPolltxtFourthOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView4.myPollprogressbarFourthOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView4.myPollGroupAnswer4YouTubeLayout.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            //view gone
            myPollView4.myPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
            //view gone
            myPollView4.myPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                //user poll image1 is set into the image view

                myPollView4.myPollimageQuestion1YouTubeLayout.setImageURI(Uri.parse(pollImage1));
                //user poll image2 is set into the image view
                myPollView4.myPollimageQuestion2YouTubeLayout.setImageURI(Uri.parse(pollImage2));
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                //view visible
                myPollView4.myPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                myPollView4.myPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(pollImage1));
                //view gone
                myPollView4.myPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                myPollView4.myPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                //view visible
                myPollView4.myPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //view is set into the image view
                myPollView4.myPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(pollImage2));
                //view gone
                myPollView4.myPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                myPollView4.myPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            } else {
                //view gone
                myPollView4.myPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                myPollView4.myPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer1)) {
            myPollView4.myPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(pollAnswer1)) {
            myPollView4.myPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.VISIBLE);
            myPollView4.myPolloption1YouTubeLayoutYouTubeLayout.setText(pollAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer2)) {
            myPollView4.myPolloption2YouTubeLayout.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            myPollView4.myPolloption2YouTubeLayout.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            myPollView4.myPolloption2YouTubeLayout.setText(pollAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer3)) {
            //view gone
            myPollView4.myPolloption3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            myPollView4.myPolloption4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            myPollView4.myPollrelativePrgressBar3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            myPollView4.myPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            myPollView4.myPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.GONE);
            //view gone
            myPollView4.myPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer3)) {
            //visible
            myPollView4.myPolloption3YouTubeLayout.setVisibility(View.VISIBLE);
            //setting the data in text view
            myPollView4.myPolloption3YouTubeLayout.setText(pollAnswer3);
            //visible
            myPollView4.myPollrelativePrgressBar3YouTubeLayout.setVisibility(View.VISIBLE);
            //visible
            myPollView4.myPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(pollAnswer4)) {
                //view gone
                myPollView4.myPolloption4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                myPollView4.myPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                myPollView4.myPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
            } else if (!("").equals(pollAnswer4)) {
                //view visible
                myPollView4.myPollrelativePrgressBar4YouTubeLayout.setVisibility(View.VISIBLE);
                //view visible
                myPollView4.myPolloption4YouTubeLayout.setVisibility(View.VISIBLE);
                //Setting the the text view
                myPollView4.myPolloption4YouTubeLayout.setText(pollAnswer4);
                //view visible
                myPollView4.myPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPolltxtCountsYouTubeLayout.setOnClickListener(myPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.txtLikeYouTubeLayout.setOnClickListener(myPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.imgShareYouTubeLayout.setOnClickListener(myPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPollimageQuestion1YouTubeLayout.setOnClickListener(myPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPollimageQuestion1YouTubeLayout.setOnClickListener(myPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPollGroupAnswer1YouTubeLayout.setOnClickListener(myPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPollGroupAnswer2YouTubeLayout.setOnClickListener(myPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPollGroupAnswer3YouTubeLayout.setOnClickListener(myPollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPollGroupAnswer4YouTubeLayout.setOnClickListener(myPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.myPollsingleOptionYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting the string in youtube url
                MApplication.setString(myPollsReviewActivity, Constants.YOUTUBE_URL, pollResponseModelView4.getResults().getPollreview().get(0).getYoutubeUrl());
                //If net is connected
                if (MApplication.isNetConnected((Activity) myPollsReviewActivity)) {
                    //An intent is an abstract description of an operation to be performed.
                    // It can be used with startActivity to launch an Activity
                    Intent a = new Intent(myPollsReviewActivity, VideoLandscapeActivity.class);
                    //Start the activity
                    myPollsReviewActivity.startActivity(a);
                } else {
                    //Toast message will display
                    Toast.makeText(myPollsReviewActivity, myPollsReviewActivity.getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView4.likeUnlikeYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {   //If check box is checked
                    mlikes = 1;  //Setting the int value in variable
                    myPollView4.likeUnlikeYouTubeLayout.setChecked(true);  //set checked true
                    likesUnLikes(listpositionMyPoll);   //Like unlike for the poll request
                } else {
                    mlikes = 0;  //Setting the int value in variable
                    myPollView4.likeUnlikeYouTubeLayout.setChecked(false);    //set checked true
                    likesUnLikes(listpositionMyPoll); //Like unlike for the poll request
                }
            }
        });
    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 3 this view is called.
     * @param pollReviewResponseModelView3
     */
    private void thirdReview(PollReviewResponseModel pollReviewResponseModelView3) {
        //view holder
        myPollView3 =new ViewHolder3();
        //Initializing the views.
        myPollView3.scrollView = (ScrollView) findViewById(R.id.scroll);
        myPollView3.myPollPhotoComparisonTxtQuestion = (TextView) findViewById(R.id.txtStatus);
        myPollView3.myPollPhotoComparisonimageQuestion1 = (ImageView) findViewById(R.id.choose);
        myPollView3.myPollPhotoComparisonimageQuestion2 = (ImageView) findViewById(R.id.ChooseAdditional);
        myPollView3.myPollPhotoComparisonsingleOption = (ImageView) findViewById(R.id.singleOption);
        txtCommentsMyPoll = (TextView) findViewById(R.id.txtCommentsCounts);
        myPollView3.myPollPhotoComparisonlikeUnlike = (CheckBox) findViewById(R.id.unLikeDislike);
        myPollView3.myPollPhotoComparisonTxtLike = (TextView) findViewById(R.id.txtLike2);
        pollImage1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        pollImage2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        myPollView3.myPollPhotoComparisonHeaderImage = (ImageView) findViewById(R.id.imgProfile);
        myPollView3.myPollPhotoComparisontxtTime = (TextView) findViewById(R.id.txtTime);
        myPollView3.myPollPhotoComparisonHeaderName = (TextView) findViewById(R.id.txtName);
        myPollView3.myPollPhotoComparisonHeaderCategory = (TextView) findViewById(R.id.txtCategory);
        Utils.loadImageWithGlideProfileRounderCorner(this,myPollUserLogo.replaceAll(" ", "%20"),myPollView3.myPollPhotoComparisonHeaderImage,R.drawable.placeholder_image);
        myPollView3.myPollPhotoComparisonHeaderName.setText(MApplication.getDecodedString(myPOllName));
        myPollView3.myPollPhotoComparisonHeaderCategory.setText(myPollCategory);
        myPollView3.myPollPhotoComparisontxtTime.setText(myPollUpdatedTime);
        myPollView3.myPollPhotoComparisontxtCounts = (TextView) findViewById(R.id.txtParticcipation);
        myPollView3.myPollPhotoComparisontxtCounts.setText(participateCountMyPoll);
        myPollView3.myPollPhotoComparisonimageAnswer1 = (ImageView) findViewById(R.id.answer1);
        myPollView3.myPollPhotoComparisonimageAnswer2 = (ImageView) findViewById(R.id.answer2);
        myPollView3.myPollPhotoComparisonimageAnswer3 = (ImageView) findViewById(R.id.answer3);
        myPollView3.myPollPhotoComparisonimageAnswer4 = (ImageView) findViewById(R.id.answer4);
        myPollView3.myPollPhotoComparisonrelativePrgressBar3 = (RelativeLayout) findViewById(R.id.relativeProgressbar3);
        myPollView3.myPollPhotoComparisonrelativePrgressBar4 = (RelativeLayout) findViewById(R.id.relativeProgressbar4);
        myPollView3.myPollPhotoComparisonprogressbarFirstOption = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        myPollView3.myPollPhotoComparisonprogressbarSecondOption = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        myPollView3.myPollPhotoComparisonprogressbarThirdOption = (ProgressBar) findViewById(R.id.progressbarThirdOption);
        myPollView3.myPollPhotoComparisonprogressbarFourthOption = (ProgressBar) findViewById(R.id.progressbarFourthOption);
        myPollView3.myPollPhotoComparisonthirdOption = (RelativeLayout) findViewById(R.id.ThirdOptionOption);
        myPollView3.myPollPhotoComparisonfourthOption = (RelativeLayout) findViewById(R.id.FourthOptionOption);
        myPollView3.myPollPhotoComparisontxtFirstOptionCount = (TextView) findViewById(R.id.txtFirstOptionCount);
        myPollView3.myPollPhotoComparisontxtSecondOptionCount = (TextView) findViewById(R.id.txtSecondOptionCount);
        myPollView3.myPollPhotoComparisontxtThirdOptionCount = (TextView) findViewById(R.id.txtThirdOptionCount);
        myPollView3.myPollPhotoComparisontxtFourthOptionCount = (TextView) findViewById(R.id.txtFourthOptionCount);
        myPollView3.myPollPhotoComparisonGroupAnswer1 = (TextView) findViewById(R.id.pollGroupAnswer1);
        myPollView3.myPollPhotoComparisonGroupAnswer2 = (TextView) findViewById(R.id.pollGroupAnswer2);
        myPollView3.myPollPhotoComparisonGroupAnswer3 = (TextView) findViewById(R.id.pollGroupAnswer3);
        myPollView3.myPollPhotoComparisonGroupAnswer4 = (TextView) findViewById(R.id.pollGroupAnswer4);
        myPollView3.myPollPhotoComparisonimgShare = (ImageView) findViewById(R.id.imgShare);

        if(Shareid){
            myPollView3.myPollPhotoComparisonimgShare .setVisibility(View.GONE);
        }else{
            myPollView3.myPollPhotoComparisonimgShare .setVisibility(View.VISIBLE);
        }

        //view visible
        myPollView3.scrollView.setVisibility(View.VISIBLE);
        //Setting the poll like count
        myPollView3.myPollPhotoComparisonTxtLike.setText(likeCountMyPoll);
        //Setting the text in text view
        txtCommentsMyPoll.setText(MApplication.getString(myPollsReviewActivity, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(likeUserMyPoll)) {
            myPollView3.myPollPhotoComparisonlikeUnlike.setChecked(true);
        } else {
            myPollView3.myPollPhotoComparisonlikeUnlike.setChecked(false);
        }
        //Setting the question in text view
        myPollView3.myPollPhotoComparisonTxtQuestion.setText(pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        pollAnswer1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1().replaceAll(" ", "%20");
        //poll answer option from the response
        pollAnswer2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2().replaceAll(" ", "%20");
        //poll answer option from the response
        pollAnswer3 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3().replaceAll(" ", "%20");
        //poll answer option from the response
        pollAnswer4 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4().replaceAll(" ", "%20");
        //Participater answer details from the answer
        pollOptionParticipated = pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll();

        //Getting the values based on the size
        for (int i = 0; pollOptionParticipated.size() > i; i++) {
            //Get the poll counts from the response
            String mPollCount = pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollCounts();
            //Converting the poll counts into the float value
            float pollCount = Float.parseFloat(mPollCount);
            //Get the poll total count
            float totalPollCount = Float.valueOf(pollReviewResponseModelView3.getResults().getPollTotalCount());
            //Total percentage value of the poll
            float percentage = pollCount * 100 / totalPollCount;
            //Converting the value into the double
            Double d = new Double(percentage);
            //percentage
            int mPercentage = d.intValue();
            //If the poll answer option matches with 1
            if (("1").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView3.myPollPhotoComparisontxtFirstOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView3.myPollPhotoComparisonGroupAnswer1.setText(mPollCount);
                //Setting the poll count in text view
                myPollView3.myPollPhotoComparisonprogressbarFirstOption.setProgress(mPercentage);
            } else if (("2").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView3.myPollPhotoComparisontxtSecondOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView3.myPollPhotoComparisonprogressbarSecondOption.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView3.myPollPhotoComparisonGroupAnswer2.setText(mPollCount);
            } else if (("3").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView3.myPollPhotoComparisontxtThirdOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView3.myPollPhotoComparisonprogressbarThirdOption.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView3.myPollPhotoComparisonGroupAnswer3.setText(mPollCount);
            } else if (("4").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView3.myPollPhotoComparisontxtFourthOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView3.myPollPhotoComparisonprogressbarFourthOption.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView3.myPollPhotoComparisonGroupAnswer4.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            //view gone
            myPollView3.myPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage1,myPollView3.myPollPhotoComparisonimageQuestion1,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,pollImage2,myPollView3.myPollPhotoComparisonimageQuestion2,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                //view visible
                myPollView3.myPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage1,myPollView3.myPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                myPollView3.myPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                myPollView3.myPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                //view visible
                myPollView3.myPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage2,myPollView3.myPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                myPollView3.myPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                myPollView3.myPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
            } else {
                //view gone
                myPollView3.myPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                myPollView3.myPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer1)) {
            myPollView3.myPollPhotoComparisonimageAnswer1.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(pollAnswer1)) {
            myPollView3.myPollPhotoComparisonimageAnswer1.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(this,pollAnswer1,myPollView3.myPollPhotoComparisonimageAnswer1,R.drawable.placeholder_image);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer2)) {
            myPollView3.myPollPhotoComparisonimageAnswer2.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            myPollView3.myPollPhotoComparisonimageAnswer2.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            Utils.loadImageWithGlideRounderCorner(this,pollAnswer2,myPollView3.myPollPhotoComparisonimageAnswer2,R.drawable.placeholder_image);

        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer3)) {
            //view gone
            myPollView3.myPollPhotoComparisonimageAnswer3.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisonthirdOption.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisonrelativePrgressBar3.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisonfourthOption.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisontxtThirdOptionCount.setVisibility(View.GONE);
            //view gone
            myPollView3.myPollPhotoComparisontxtFourthOptionCount.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer3)) {
            //visible
            myPollView3.myPollPhotoComparisonimageAnswer3.setVisibility(View.VISIBLE);
            //setting the data in text view
            Utils.loadImageWithGlideRounderCorner(this,pollAnswer3,myPollView3.myPollPhotoComparisonimageAnswer3,R.drawable.placeholder_image);

            myPollView3.myPollPhotoComparisonimageAnswer3.setImageURI(Uri.parse(pollAnswer3));
            //view gone
            myPollView3.myPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(pollAnswer4)) {
                //view gone
                myPollView3.myPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
                //view gone
                myPollView3.myPollPhotoComparisonfourthOption.setVisibility(View.GONE);
                //view gone
                myPollView3.myPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
                //view gone
                myPollView3.myPollPhotoComparisontxtFourthOptionCount.setVisibility(View.GONE);
            } else if (!("").equals(pollAnswer4)) {
                //view visible
                myPollView3.myPollPhotoComparisonfourthOption.setVisibility(View.VISIBLE);
                //view visible
                myPollView3.myPollPhotoComparisonimageAnswer4.setVisibility(View.VISIBLE);
                //Setting the the text view
                Utils.loadImageWithGlideRounderCorner(this,pollAnswer4,myPollView3.myPollPhotoComparisonimageAnswer4,R.drawable.placeholder_image);
                //view visible
                myPollView3.myPollPhotoComparisonrelativePrgressBar4.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonlikeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {  //If check box is checked
                    mlikes = 1;   //Setting the int value in variable
                    myPollView3.myPollPhotoComparisonlikeUnlike.setChecked(true); //set checked true
                    likesUnLikes(listpositionMyPoll);//Like unlike for the poll request
                } else {
                    mlikes = 0; //Setting the int value in variable
                    myPollView3.myPollPhotoComparisonlikeUnlike.setChecked(false); //set checked true
                    likesUnLikes(listpositionMyPoll);  //Like unlike for the poll request
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisontxtCounts.setOnClickListener(myPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonTxtLike.setOnClickListener(myPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonimgShare.setOnClickListener(myPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonimageQuestion1.setOnClickListener(myPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonimageQuestion2.setOnClickListener(myPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonGroupAnswer1.setOnClickListener(myPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonGroupAnswer2.setOnClickListener(myPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonGroupAnswer3.setOnClickListener(myPollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonGroupAnswer4.setOnClickListener(myPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonsingleOption.setOnClickListener(myPollsingleImageView);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonimageAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(myPollsReviewActivity, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, pollAnswer1);
                //Starting the activity
                myPollsReviewActivity.startActivity(a);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonimageAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(myPollsReviewActivity, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, pollAnswer2);
                //Starting the activity
                myPollsReviewActivity.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonimageAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(myPollsReviewActivity, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, pollAnswer3);
                //Starting the activity
                myPollsReviewActivity.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        myPollView3.myPollPhotoComparisonimageAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(myPollsReviewActivity, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, pollAnswer4);
                //Starting the activity
                myPollsReviewActivity.startActivity(a);
            }
        });
    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 2 this view is called.
     * @param pollResponseModelView2
     */
    private void secondReview(PollReviewResponseModel pollResponseModelView2) {
        //view holder
        myPollView2 =new ViewHolder2();
        //Initializing the views.
        myPollView2.scrollViewMultipleOptions =(ScrollView)findViewById(R.id.scroll);
        myPollView2.myPollTxtQuestionMultipleOptions = (TextView) findViewById(R.id.txtStatus);
        myPollView2.myPollimageQuestion1MultipleOptions = (ImageView)findViewById(R.id.choose);
        myPollView2.myPollimageQuestion2MultipleOptions = (ImageView)findViewById(R.id.ChooseAdditional);
        myPollView2.myPollsingleOptionMultipleOptions = (ImageView)findViewById(R.id.singleOption);
        myPollView2.myPollHeaderImageMultipleOptions = (ImageView)findViewById(R.id.imgProfile);
        myPollView2.myPolltxtTimeMultipleOptions = (TextView)findViewById(R.id.txtTime);
        myPollView2.myPollHeaderNameMultipleOptions = (TextView)findViewById(R.id.txtName);
        myPollView2.myPollHeaderCategoryMultipleOptions = (TextView)findViewById(R.id.txtCategory);
        txtCommentsMyPoll = (TextView)findViewById(R.id.txtCommentsCounts);
        myPollView2.likeUnlikeMultipleOptions = (CheckBox)findViewById(R.id.unLikeDislike);
        myPollView2.myPollGroupAnswer1MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer1);
        myPollView2.myPollGroupAnswer2MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer2);
        myPollView2.myPollGroupAnswer3MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer3);
        myPollView2.myPollGroupAnswer4MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer4);
        myPollView2.imgShareMultipleOptions =(ImageView)findViewById(R.id.imgShare);
        if(Shareid){
            myPollView2.imgShareMultipleOptions.setVisibility(View.GONE);
        }else{
            myPollView2.imgShareMultipleOptions.setVisibility(View.VISIBLE);
        }

        myPollView2.txtLikeMultipleOptions = (TextView)findViewById(R.id.txtLike2);
        myPollView2.myPolloption1MultipleOptions = (TextView)findViewById(R.id.option1);
        myPollView2.myPolloption2MultipleOptions = (TextView) findViewById(R.id.option2);
        myPollView2.myPolloption3MultipleOptions = (TextView)findViewById(R.id.option3);
        myPollView2.myPolloption4MultipleOptions = (TextView)findViewById(R.id.option4);
        myPollView2.myPolltxtFirstOptionCountMultipleOptions =(TextView)findViewById(R.id.txtFirstOptionCount);
        myPollView2.myPolltxtSecondOptionCounMultipleOptionst =(TextView)findViewById(R.id.txtSecondOptionCount);
        myPollView2.myPolltxtThirdOptionCountMultipleOptions =(TextView)findViewById(R.id.txtThirdOptionCount);
        myPollView2.myPolltxtFourthOptionCountMultipleOptions =(TextView)findViewById(R.id.txtFourthOptionCount);
        myPollView2.myPollprogressbarFirstOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarFirstOption);
        myPollView2.myPollprogressbarSecondOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarSecondOption);
        myPollView2.myPollprogressbarThirdOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarThirdOption);
        myPollView2.myPollprogressbarFourthOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarFourthOption);
        myPollView2.myPollrelativePrgressBar3MultipleOptions =(RelativeLayout)findViewById(R.id.relativeProgressbar3);
        myPollView2.myPollrelativePrgressBar4MultipleOptions =(RelativeLayout)findViewById(R.id.relativeProgressbar4);
        myPollView2.myPolltxtCountsMultipleOptions = (TextView) findViewById(R.id.txtParticcipation);
        //view visible
        myPollView2.scrollViewMultipleOptions.setVisibility(View.VISIBLE);
        //setting the participate count
        myPollView2.myPolltxtCountsMultipleOptions.setText(participateCountMyPoll);
        //question image
        pollImage1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //question image 2 from the response
        pollImage2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        Utils.loadImageWithGlideProfileRounderCorner(this,myPollUserLogo.replaceAll(" ", "%20"), myPollView2.myPollHeaderImageMultipleOptions,R.drawable.placeholder_image);
        //setting the name in text view
        myPollView2.myPollHeaderNameMultipleOptions.setText(MApplication.getDecodedString(myPOllName));
        //setting the category in text view
        myPollView2.myPollHeaderCategoryMultipleOptions.setText(myPollCategory);
        //setting the updated time in text view
        myPollView2.myPolltxtTimeMultipleOptions.setText(myPollUpdatedTime);
        //setting the poll count in text view
        myPollView2.txtLikeMultipleOptions.setText(likeCountMyPoll);
        //setting the comments count in text view
        txtCommentsMyPoll.setText(MApplication.getString(myPollsReviewActivity, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(likeUserMyPoll)) {
            myPollView2.likeUnlikeMultipleOptions.setChecked(true);
        } else {
            myPollView2.likeUnlikeMultipleOptions.setChecked(false);
        }
        //Setting the question intext view
        myPollView2.myPollTxtQuestionMultipleOptions.setText(pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        pollAnswer1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        pollAnswer2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        pollAnswer3 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option pollReviewResponseModel the response
        pollAnswer4 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        pollOptionParticipated = pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; pollOptionParticipated.size() > i; i++) {
            //Get the poll counts from the response
            String mPollCount=pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollCounts();
            //Converting the poll counts into the float value
            float pollCount = Float.parseFloat(mPollCount);
            //Get the poll total count
            float totalPollCount = Float.valueOf(pollResponseModelView2.getResults().getPollTotalCount());
            //Total percentage value of the poll
            float percentage = pollCount * 100 / totalPollCount;
            //Converting the value into the double
            Double d = new Double(percentage);
            //percentage
            int mPercentage = d.intValue();
            //If the poll answer option matches with 1
            if (("1").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView2.myPolltxtFirstOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView2.myPollprogressbarFirstOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView2.myPollGroupAnswer1MultipleOptions.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView2.myPolltxtSecondOptionCounMultipleOptionst.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView2.myPollprogressbarSecondOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView2.myPollGroupAnswer2MultipleOptions.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView2.myPolltxtThirdOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView2.myPollprogressbarThirdOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView2.myPollGroupAnswer3MultipleOptions.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView2.myPolltxtFourthOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView2.myPollprogressbarFourthOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView2.myPollGroupAnswer4MultipleOptions.setText(mPollCount);
            }
        }
//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            //view gone
            myPollView2.myPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
            //view gone
            myPollView2.myPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage1,myPollView2.myPollimageQuestion1MultipleOptions,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,pollImage2,myPollView2.myPollimageQuestion2MultipleOptions,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                //view visible
                myPollView2.myPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage1,myPollView2.myPollsingleOptionMultipleOptions,R.drawable.placeholder_image);
                //view gone
                myPollView2.myPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                myPollView2.myPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                //view visible
                myPollView2.myPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage2,myPollView2.myPollsingleOptionMultipleOptions,R.drawable.placeholder_image);
                //view gone
                myPollView2.myPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                myPollView2.myPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            } else {
                //view gone
                myPollView2.myPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                myPollView2.myPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer1)) {
            myPollView2.myPolloption1MultipleOptions.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(pollAnswer1)) {
            myPollView2.myPolloption1MultipleOptions.setVisibility(View.VISIBLE);
            myPollView2.myPolloption1MultipleOptions.setText(pollAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer2)) {
            myPollView2.myPolloption2MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer2)) {
            myPollView2.myPolloption2MultipleOptions.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            myPollView2.myPolloption2MultipleOptions.setText(pollAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(pollAnswer3)) {
            //view gone
            myPollView2.myPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            myPollView2.myPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            myPollView2.myPollrelativePrgressBar3MultipleOptions.setVisibility(View.GONE);
            //view gone
            myPollView2.myPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //view gone
            myPollView2.myPollGroupAnswer3MultipleOptions.setVisibility(View.GONE);
            //view gone
            myPollView2.myPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(pollAnswer3)) {
            //visible
            myPollView2.myPolloption3MultipleOptions.setVisibility(View.VISIBLE);
            //setting the data in text view
            myPollView2.myPolloption3MultipleOptions.setText(pollAnswer3);
            //view gone
            myPollView2.myPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //progressbar
            myPollView2.myPollrelativePrgressBar3MultipleOptions.setVisibility(View.VISIBLE);
            //group answer
            myPollView2.myPollGroupAnswer3MultipleOptions.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(pollAnswer4)) {
                //view gone
                myPollView2.myPolloption4MultipleOptions.setVisibility(View.GONE);
                //view gone
                myPollView2.myPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
                //view gone
                myPollView2.myPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
            } else if (!("").equals(pollAnswer4)) {
                //view visible
                myPollView2.myPollrelativePrgressBar4MultipleOptions.setVisibility(View.VISIBLE);
                //view visible
                myPollView2.myPolloption4MultipleOptions.setVisibility(View.VISIBLE);
                //Setting the the text view
                myPollView2.myPolloption4MultipleOptions.setText(pollAnswer4);
                //view visible
                myPollView2.myPollGroupAnswer4MultipleOptions.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.likeUnlikeMultipleOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If check box is checked
                if (((CheckBox) v).isChecked()) {
                    //Setting the int value in variable
                    mlikes = 1;
                    //set checked true
                    myPollView2.likeUnlikeMultipleOptions.setChecked(true);
                    //Like unlike for the poll request
                    likesUnLikes(listpositionMyPoll);
                } else {
                    //Setting the int value in variable
                    mlikes = 0;
                    //set checked true
                    myPollView2.likeUnlikeMultipleOptions.setChecked(false);
                    //Like unlike for the poll request
                    likesUnLikes(listpositionMyPoll);
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPolltxtCountsMultipleOptions.setOnClickListener(myPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.txtLikeMultipleOptions.setOnClickListener(myPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.imgShareMultipleOptions.setOnClickListener(myPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPollimageQuestion1MultipleOptions.setOnClickListener(myPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPollimageQuestion2MultipleOptions.setOnClickListener(myPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPollGroupAnswer1MultipleOptions.setOnClickListener(myPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPollGroupAnswer2MultipleOptions.setOnClickListener(myPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPollGroupAnswer3MultipleOptions.setOnClickListener(myPollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPollGroupAnswer4MultipleOptions.setOnClickListener(myPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView2.myPollsingleOptionMultipleOptions.setOnClickListener(myPollsingleImageView);

    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 1 this view is called.
     * @param pollResponseModelView1
     */
    private void firstReview(PollReviewResponseModel pollResponseModelView1) {
        //view holder
        myPollView1 = new ViewHolder1();
        //Initialization
        myPollView1.scrollViewYesOrNO = (ScrollView) findViewById(R.id.scroll);
        myPollView1.myPollGroupAnswerYesOrNO = (TextView) findViewById(R.id.pollGroupAnswer);
        myPollView1.myPollGroupAnswer1YesOrNO = (TextView) findViewById(R.id.pollGroupAnswer1);
        myPollView1.myPollTxtQuestionYesOrNO = (TextView) findViewById(R.id.txtStatus);
        myPollView1.myPollimageQuestion1YesOrNO = (ImageView) findViewById(R.id.choose);
        myPollView1.myPollimageQuestion2YesOrNO = (ImageView) findViewById(R.id.ChooseAdditional);
        myPollView1.myPollsingleOptionYesOrNO = (ImageView) findViewById(R.id.singleOption);
        myPollView1.myPollHeaderImageYesOrNO = (ImageView) findViewById(R.id.imgProfile);
        myPollView1.myPolltxtTimeYesOrNO = (TextView) findViewById(R.id.txtTime);
        myPollView1.myPollHeaderNameYesOrNO = (TextView) findViewById(R.id.txtName);
        myPollView1.myPollHeaderCategoryYesOrNO = (TextView) findViewById(R.id.txtCategory);
        txtCommentsMyPoll = (TextView) findViewById(R.id.txtCommentsCounts);
        myPollView1.likeUnlikeYesOrNO = (CheckBox) findViewById(R.id.unLikeDislike);
        myPollView1.txtLikeYesOrNO = (TextView) findViewById(R.id.txtLike2);
        myPollView1.myPolltxtFirstOptionCountYesOrNO = (TextView) findViewById(R.id.txtFirstOptionCount);
        myPollView1.myPolltxtSecondOptionCountYesOrNO = (TextView) findViewById(R.id.txtSecondOptionCount);
        myPollView1.myPolltxtCountsYesOrNO = (TextView) findViewById(R.id.txtParticcipation);
        myPollView1.myPolloption1YesOrNO = (TextView) findViewById(R.id.option1);
        myPollView1.myPolloption2YesOrNO = (TextView) findViewById(R.id.option2);
        myPollView1.myPollprogressbarFirstOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        myPollView1.myPollprogressbarSecondOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        myPollView1.imgShareYesOrNO = (ImageView) findViewById(R.id.imgShare);

      if(Shareid){
          myPollView1.imgShareYesOrNO.setVisibility(View.GONE);
      }else{
          myPollView1.imgShareYesOrNO.setVisibility(View.VISIBLE);
      }

        //Setting the visiblity
        myPollView1.scrollViewYesOrNO.setVisibility(View.VISIBLE);
        //setting the participate count
        myPollView1.myPolltxtCountsYesOrNO.setText(participateCountMyPoll);
        //question image 1 fromt the response
        pollImage1 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //question image 2 fromt the response
        pollImage2 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        Utils.loadImageWithGlideProfileRounderCorner(this,myPollUserLogo.replaceAll(" ", "%20"), myPollView1.myPollHeaderImageYesOrNO,R.drawable.placeholder_image);
        //setting the name in text view
        myPollView1.myPollHeaderNameYesOrNO.setText(MApplication.getDecodedString(myPOllName));
        //setting the category in text view
        myPollView1.myPollHeaderCategoryYesOrNO.setText(myPollCategory);
        //setting the time in text view
        myPollView1.myPolltxtTimeYesOrNO.setText(myPollUpdatedTime);
        //setting the like count
        myPollView1.txtLikeYesOrNO.setText(likeCountMyPoll);
        //setting the comments in text view
        txtCommentsMyPoll.setText(MApplication.getString(myPollsReviewActivity, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //setting the question in text view
        myPollView1.myPollTxtQuestionYesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestion());
        //setting the option1 in text view
        myPollView1.myPolloption1YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1());
        //setting the option2 in text view
        myPollView1.myPolloption2YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2());
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(likeUserMyPoll)) {
            myPollView1.likeUnlikeYesOrNO.setChecked(true);
        } else {
            myPollView1.likeUnlikeYesOrNO.setChecked(false);
        }
        //Participater answer details from the answer
        pollOptionParticipated = pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        //Getting the values based on the size
        for (int i = 0; pollOptionParticipated.size() > i; i++) {
            //Get the poll counts from the response
            String mPollCount = pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollCounts();
            //Converting the poll counts into the float value
            float pollCount = Float.parseFloat(mPollCount);
            //Get the poll total count
            float totalPollCount = Float.valueOf(pollResponseModelView1.getResults().getPollTotalCount());
            //Total percentage value of the poll
            float percentage = pollCount * 100 / totalPollCount;
            //Converting the value into the double
            Double d = new Double(percentage);
            //percentage
            int mPercentage = d.intValue();
            //If the poll answer option matches with 1
            if (("1").equals(pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView1.myPolltxtFirstOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                myPollView1.myPollprogressbarFirstOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView1.myPollGroupAnswerYesOrNO.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                myPollView1.myPollGroupAnswer1YesOrNO.setText(mPollCount);
                //Setting the progress bar to fill
                myPollView1.myPollprogressbarSecondOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                myPollView1.myPolltxtSecondOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.myPolltxtCountsYesOrNO.setOnClickListener(myPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.txtLikeYesOrNO.setOnClickListener(myPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.imgShareYesOrNO.setOnClickListener(myPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.myPollimageQuestion1YesOrNO.setOnClickListener(myPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.myPollimageQuestion2YesOrNO.setOnClickListener(myPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.myPollGroupAnswerYesOrNO.setOnClickListener(myPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.myPollGroupAnswer1YesOrNO.setOnClickListener(myPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.myPollsingleOptionYesOrNO.setOnClickListener(myPollsingleImageView);

//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(pollImage1) && ("").equals(pollImage2)) {
            //view gone
            myPollView1.myPollimageQuestion1YesOrNO.setVisibility(View.GONE);
            //view gone
            myPollView1.myPollimageQuestion2YesOrNO.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(pollImage1) && !("").equals(pollImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage1, myPollView1.myPollimageQuestion1YesOrNO,R.drawable.placeholder_image);
                //user poll image2 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage2, myPollView1.myPollimageQuestion2YesOrNO,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(pollImage1) && ("").equals(pollImage2)) {
                //view visible
                myPollView1.myPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage1, myPollView1.myPollsingleOptionYesOrNO,R.drawable.placeholder_image);
                //view gone
                myPollView1.myPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                myPollView1.myPollimageQuestion2YesOrNO.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(pollImage1) && !("").equals(pollImage2)) {
                //view visible
                myPollView1.myPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,pollImage1, myPollView1.myPollsingleOptionYesOrNO,R.drawable.placeholder_image);
                //view gone
                myPollView1.myPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                myPollView1.myPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            } else {
                //view gone
                myPollView1.myPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                myPollView1.myPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            }
        }


        //Interface definition for a callback to be invoked when a view is clicked.
        myPollView1.likeUnlikeYesOrNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {  //If check box is checked
                    mlikes = 1; //Setting the int value in variable
                    myPollView1.likeUnlikeYesOrNO.setChecked(true);  //set checked true
                    //Like unlike for the poll request
                    likesUnLikes(listpositionMyPoll);
                } else {
                    mlikes = 0;  //Setting the int value in variable
                    myPollView1.likeUnlikeYesOrNO.setChecked(false);   //set checked true
                    likesUnLikes(listpositionMyPoll); //Like unlike for the poll request
                }
            }
        });
    }
    /**
     * Calling this method from xml file when performing the click on the ACTION
     *
     * @param clickedView
     */
    public void onClick(final View clickedView) {
        if(clickedView.getId()==R.id.imagBackArrow){
            this.finish();
        }
    }

    /**
     * This method is used to like an unlike the poll using server request
     * @param clickPosition
     */
    private void likesUnLikes(final int clickPosition) {
        //Response from the server
        MApplication.materialdesignDialogStart(myPollsReviewActivity);
        LikesAndUnLikeRestClient.getInstance().postCampaignPollLikes(new String("poll_likes"), new String(userId), new String(pollIdMyPoll), new String(String.valueOf(mlikes))
                , new Callback<LikeUnLikeResposneModel>() {
            @Override
            public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                //If the value from the response is 1 then the user has successfully liked the poll
                if (("1").equals(likesUnlike.getResults())) {
                    //Changing the value in array list in a particular position
                    preferenceMyPollLikeUser.set(clickPosition, Integer.valueOf(1));
                    //Saving it in array
                    MApplication.saveArray(myPollsReviewActivity, preferenceMyPollLikeUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
                } else {
                    //Changing the value in array list in a particular position
                    preferenceMyPollLikeUser.set(clickPosition, Integer.valueOf(0));
                    //Saving it in array
                    MApplication.saveArray(myPollsReviewActivity, preferenceMyPollLikeUser, Constants.MY_POLL_LIKES_USER_ARRAY, Constants.MY_POLL_LIKES_USER_SIZE);
                }
                //Toast message will display
                Toast.makeText(myPollsReviewActivity, likesUnlike.getMsg(),
                        Toast.LENGTH_SHORT).show();
                //Changing the value in array list in a particular position
                prefrenceMyPollLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                //Saving the array in prefernce
                MApplication.saveArray(myPollsReviewActivity, prefrenceMyPollLikeCount, Constants.MY_POLL_LIKES_COUNT_ARRAY, Constants.MY_POLL_LIKES_COUNT_SIZE);
                //Changing the value in textview
                txtLikeMyPoll.setText(String.valueOf(prefrenceMyPollLikeCount.get(clickPosition)));
                MApplication.materialdesignDialogStop();
            }


            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, myPollsReviewActivity);
                MApplication.materialdesignDialogStop();
            }

        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        //Setting the comments in text view
        txtCommentsMyPoll.setText(MApplication.getString(myPollsReviewActivity, Constants.COMMENTS_COUNT_POLL_REVIEW));
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder4 {
        //Scroll
        private ScrollView scrollViewYouTubeLayout;
        //Textview
        private TextView myPolltxtCountsYouTubeLayout;
        //Textview
        private TextView myPollGroupAnswer1YouTubeLayout;
        //Textvie
        private TextView myPollGroupAnswer2YouTubeLayout;
        //Textview
        private TextView myPollGroupAnswer3YouTubeLayout;
        //Textview
        private TextView myPollGroupAnswer4YouTubeLayout;
        //Textview
        private TextView myPolloption3YouTubeLayout;
        //Textview
        private TextView myPolloption4YouTubeLayout;
        //Progressbar
        private ProgressBar myPollprogressbarThirdOptionYouTubeLayout, myPollprogressbarFourthOptionYouTubeLayout;
        //Imageview
        private ImageView myPollHeaderImageYouTubeLayout;
        //Imagewview
        private ImageView imgShareYouTubeLayout;
        //Textview
        private TextView myPolltxtFirstOptionCountYouTubeLayout;
        //Textview
        private TextView myPolltxtSecondOptionCountYouTubeLayout;
        //Textview
        private TextView myPolltxtThirdOptionCountYouTubeLayout;
        //Textview
        private TextView myPolltxtFourthOptionCountYouTubeLayout;
        //Relativelayout
        private RelativeLayout myPollrelativePrgressBar3YouTubeLayout;
        //Relativelayout
        private RelativeLayout myPollrelativePrgressBar4YouTubeLayout;
        //Textview
        private TextView myPollTxtQuestionYouTubeLayout;
        ///textview
        private TextView myPolloption1YouTubeLayoutYouTubeLayout;
        //Textview
        private TextView myPolloption2YouTubeLayout;
        //Imageview
        private SimpleDraweeView myPollimageQuestion1YouTubeLayout;
        //Imageview
        private SimpleDraweeView myPollimageQuestion2YouTubeLayout;
        //Imageview
        private SimpleDraweeView myPollsingleOptionYouTubeLayout;
        //Progressbar
        private ProgressBar myPollprogressbarFirstOptionYouTubeLayout;
        //Progressbar
        private ProgressBar myPollprogressbarSecondOptionYouTubeLayout;
        //Textview
        private TextView myPolltxtTimeYouTubeLayout;
        //Textview
        private TextView txtLikeYouTubeLayout;
        //Textview
        private TextView myPollHeaderCategoryYouTubeLayout;
        //Textview
        private TextView myPollHeaderNameYouTubeLayout;
        //Checkbox
        private CheckBox likeUnlikeYouTubeLayout;

    }
    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder1 {
        //Scrollview
        private ScrollView scrollViewYesOrNO;
        //Textview
        private TextView myPolltxtCountsYesOrNO;
        //Textview
        private TextView myPollGroupAnswerYesOrNO;
        //Textview
        private TextView myPollGroupAnswer1YesOrNO;
        //Imageview
        private ImageView myPollHeaderImageYesOrNO;
        //Imageview
        private ImageView imgShareYesOrNO;
        //Textview
        private TextView myPolltxtFirstOptionCountYesOrNO;
        //Textview
        private TextView myPolltxtSecondOptionCountYesOrNO;
        //Textview
        private TextView myPollTxtQuestionYesOrNO;
        //Textview
        private TextView myPolloption1YesOrNO;
        //Textview
        private TextView myPolloption2YesOrNO;
        //Imageview
        private ImageView myPollimageQuestion1YesOrNO;
        //Imageview
        private ImageView myPollimageQuestion2YesOrNO;
        //Imageview
        private ImageView myPollsingleOptionYesOrNO;
        //Progressbar
        private ProgressBar myPollprogressbarFirstOptionYesOrNO;
        //Progressbar
        private ProgressBar myPollprogressbarSecondOptionYesOrNO;
        //Textview
        private TextView myPolltxtTimeYesOrNO;
        //Textview
        private TextView txtLikeYesOrNO;
        //Textview
        private TextView myPollHeaderCategoryYesOrNO;
        //Textview
        private TextView myPollHeaderNameYesOrNO;
        //Checkbox
        private CheckBox likeUnlikeYesOrNO;
    }
    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder3 {
        //Multiple ptions scrollview
        private ScrollView scrollView;
        //Textview
        private TextView myPollPhotoComparisontxtCounts;
        //Textview
        private TextView myPollPhotoComparisonGroupAnswer1;
        //Textview
        private TextView myPollPhotoComparisonGroupAnswer2;
        //Textview
        private TextView myPollPhotoComparisonGroupAnswer3;
        //Textview
        private TextView myPollPhotoComparisonGroupAnswer4;
        //Relative layout
        private RelativeLayout myPollPhotoComparisonthirdOption;
        //Relative layout
        private RelativeLayout myPollPhotoComparisonfourthOption;
        //Progressbar
        private ProgressBar myPollPhotoComparisonprogressbarThirdOption, myPollPhotoComparisonprogressbarFourthOption;
        //Imageview
        private ImageView myPollPhotoComparisonimageAnswer1;
        //Imageview
        private ImageView myPollPhotoComparisonimageAnswer2;
        //Imageview
        private ImageView myPollPhotoComparisonimageAnswer3;
        //Imageview
        private ImageView myPollPhotoComparisonimageAnswer4;
        //Imageview
        private ImageView myPollPhotoComparisonHeaderImage;
        //Imageview
        private ImageView myPollPhotoComparisonimgShare;
        //Textview
        private TextView myPollPhotoComparisontxtFirstOptionCount;
        //Textview
        private TextView myPollPhotoComparisontxtSecondOptionCount;
        //Textview
        private TextView myPollPhotoComparisontxtThirdOptionCount;
        //Textview
        private TextView myPollPhotoComparisontxtFourthOptionCount;
        //Relative layout
        private RelativeLayout myPollPhotoComparisonrelativePrgressBar3;
        //Relative layout
        private RelativeLayout myPollPhotoComparisonrelativePrgressBar4;
        //Textview
        private TextView myPollPhotoComparisonTxtQuestion;
        //Imageview
        private ImageView myPollPhotoComparisonimageQuestion1;
        //Imageview
        private ImageView myPollPhotoComparisonimageQuestion2;
        //Imageview
        private ImageView myPollPhotoComparisonsingleOption;
        //Progressbar
        private ProgressBar myPollPhotoComparisonprogressbarFirstOption;
        //Progressbar
        private ProgressBar myPollPhotoComparisonprogressbarSecondOption;
        //Textview
        private TextView myPollPhotoComparisontxtTime;
        //Textview
        private TextView myPollPhotoComparisonTxtLike;
        //Textview
        private TextView myPollPhotoComparisonHeaderCategory;
        //Textview
        private TextView myPollPhotoComparisonHeaderName;
        //Checkbox
        private CheckBox myPollPhotoComparisonlikeUnlike;
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder2 {
        //Multiple ptions scrollview
        private ScrollView scrollViewMultipleOptions;
        //participate count
        private TextView myPolltxtCountsMultipleOptions;
        //Participate Count for answer1
        private TextView myPollGroupAnswer1MultipleOptions;
        //Participate Count for answer2
        private TextView myPollGroupAnswer2MultipleOptions;
        //Participate Count for answer3
        private TextView myPollGroupAnswer3MultipleOptions;
        //Participate Count for answer4
        private TextView myPollGroupAnswer4MultipleOptions;
        //option3
        private TextView myPolloption3MultipleOptions;
        //option4
        private TextView myPolloption4MultipleOptions;
        //Progressbar
        private ProgressBar myPollprogressbarThirdOptionMultipleOptions, myPollprogressbarFourthOptionMultipleOptions;
        //Imageview
        private ImageView myPollHeaderImageMultipleOptions;
        //Imageview
        private ImageView imgShareMultipleOptions;
        //Text view
        private TextView myPolltxtFirstOptionCountMultipleOptions;
        //Text view
        private TextView myPolltxtSecondOptionCounMultipleOptionst;
        private TextView myPolltxtThirdOptionCountMultipleOptions;
        //Text view
        private TextView myPolltxtFourthOptionCountMultipleOptions;
        //Relative layout
        private RelativeLayout myPollrelativePrgressBar3MultipleOptions;
        //Relative layout
        private RelativeLayout myPollrelativePrgressBar4MultipleOptions;
        //Textview
        private TextView myPollTxtQuestionMultipleOptions;
        //Textview
        private TextView myPolloption1MultipleOptions;
        //Textview
        private TextView myPolloption2MultipleOptions;
        //Imageview
        private ImageView myPollimageQuestion1MultipleOptions;
        //Imageview
        private ImageView myPollimageQuestion2MultipleOptions;
        //Imageview
        private ImageView myPollsingleOptionMultipleOptions;
        //Progressabar
        private ProgressBar myPollprogressbarFirstOptionMultipleOptions;
        //Progressabar
        private ProgressBar myPollprogressbarSecondOptionMultipleOptions;
        //Textview
        private TextView myPolltxtTimeMultipleOptions;
        //Textview
        private TextView txtLikeMultipleOptions;
        //Textview
        private TextView myPollHeaderCategoryMultipleOptions;
        //Textview
        private TextView myPollHeaderNameMultipleOptions;
        //Checkbox
        private CheckBox likeUnlikeMultipleOptions;
    }
}
