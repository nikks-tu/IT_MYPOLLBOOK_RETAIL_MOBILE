package com.contus.userpolls;

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
import com.contus.comments.PollComments;
import com.contus.likes.PollLikes;
import com.contus.participation.Participation;
import com.contus.pollreview.PollReviewQuestionParticipation;
import com.contus.responsemodel.LikeUnLikeResposneModel;
import com.contus.responsemodel.PollReviewResponseModel;
import com.contus.restclient.LikesAndUnLikeRestClient;
import com.contus.restclient.PollReviewsRestClient;
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
public class UserPollReview extends Activity {
    //Userpoll review
    private UserPollReview mUserPollReview;
    //poll id
    private String userPollId;
    //user poll image1
    private String userpollImage1;
    //user poll image2
    private String userpollImage2;
    //userPollpollOptionParticipated
    private List<PollReviewResponseModel.Results.PollReview.ParticipatePoll> userPollpollOptionParticipated;
    //userPollName
    private String userPollName;
    //userPollCategory
    private String userPollCategory;
    //userPollLogo
    private String userPollLogo;
    //user poll updated time
    private String userPollupdatedTime;
    //user poll type
    private String userPolltype;
    //position
    private int userPolllistposition;
    //prefrenceUserPollLikeCount
    private ArrayList<Integer> prefrenceUserPollLikeCount;
    //userPollLikeCount
    private ArrayList<Integer> userPollLikeCount = new ArrayList<Integer>();
    //userPollLikesUser
    private ArrayList<Integer> userPollLikesUser = new ArrayList<Integer>();
    //userPollcommentsCount
    private ArrayList<Integer> userPollcommentsCount = new ArrayList<Integer>();
    //prefrenceUserPollCommentsCount
    private ArrayList<Integer> prefrenceUserPollCommentsCount;
    //preferenceUserPollLikeUser
    private ArrayList<Integer> preferenceUserPollLikeUser;
    //mUserPolllikeCount
    private String mUserPolllikeCount;
    //commentsCount
    private String commentsCount;
    //userPolllikeUser
    private String userPolllikeUser;
    //userId
    private String userId;
    //mlikes
    private Boolean share_id;
    private int mlikes;
    //Adview
    private AdView mAdView;
    //participate count
    private String userPollparticipateCount;
    //userpollAnswer1
    private String userpollAnswer1;
    //userpollAnswer2
    private String userpollAnswer2;
    //userpollAnswer3
    private String userpollAnswer3;
    //userpollAnswer4
    private String userpollAnswer4;
    //View holder
    private ViewHolder4 userPollView4;
    //txtcomments
    private TextView txtComments;
    //view holder
    private ViewHolder1 viewHolder1;
    //view holder
    private ViewHolder3 userPollView3;
    //view holder
    private ViewHolder2 viewHolder2;
    //like
    private TextView txtLike;
    //image
    private String image;
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mParticipantCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mUserPollReview, Participation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, userPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mLikeCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent details = new Intent(mUserPollReview, PollLikes.class);
            //Passing the value from one activity to another
            details.putExtra(Constants.POLL_ID, userPollId);
            //starting the activity
            mUserPollReview.startActivity(details);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSharePoll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            String base64CampaignId = MApplication.convertByteCode(userPollId);
            //share url
            String shareUrl = Constants.SHARE_POLL_BASE_URL.concat(base64CampaignId);
            //sharing in gmail
            MApplication.shareGmail(mUserPollReview, shareUrl, MApplication.getString(mUserPollReview,Constants.PROFILE_USER_NAME));
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mUserPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, userpollImage1);
            //starting the activity
            mUserPollReview.startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mUserPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, userpollImage2);
            //starting the activity
            mUserPollReview.startActivity(a);
        }
    };
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener pollAnswer1Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(UserPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "1");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, userPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener pollAnswer2Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(UserPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "2");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, userPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener pollAnswer3Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(UserPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "3");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, userPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener pollAnswer4Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(UserPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "4");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, userPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener singleImageView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (("").equals(userpollImage2)) {
                image = userpollImage1;
            } else {
                image = userpollImage2;
            }
            //Moving from one activity to another activity
            Intent a = new Intent(mUserPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, image);
            //starting the activity
            mUserPollReview.startActivity(a);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the user poll type from another activity
        userPolltype = getIntent().getExtras().getString(Constants.POLL_TYPE);
        if(("1").equals(userPolltype)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_firstview);
        }else if(("2").equals(userPolltype)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_secondview);
        }else if(("3").equals(userPolltype)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_thirdview);
        }else if(("4").equals(userPolltype)){
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
        mUserPollReview = this;

        share_id=getIntent().getExtras().getBoolean("SHARE",false);
        //Retrieves a map of extended data from the intent.
        userPollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //Retrieves a map of extended data from the intent.
        userPolltype = getIntent().getExtras().getString(Constants.TYPE);
        //Retrieves a map of extended data from the intent.
        userPolllistposition =getIntent().getExtras().getInt(Constants.ARRAY_POSITION);
        //user id from the prefernce
        userId = MApplication.getString(mUserPollReview, Constants.USER_ID);
        //participate count
        userPollparticipateCount =getIntent().getExtras().getString(Constants.PARTICIPATE_COUNT);
        //name
        userPollName = MApplication.getString(mUserPollReview, Constants.CAMPAIGN_NAME);
        //category
        userPollCategory = MApplication.getString(mUserPollReview, Constants.CAMPAIGN_CATEGORY);
        //logo
        userPollLogo = MApplication.getString(mUserPollReview, Constants.CAMPAIGN_LOGO);
        //date updated
        userPollupdatedTime = MApplication.getString(mUserPollReview, Constants.DATE_UPDATED);
        //User poll like count saved in the prefernce is loaded into the array list
        prefrenceUserPollLikeCount = MApplication.loadArray(mUserPollReview, userPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
        //User poll comments saved in the prefernce is loaded into the array list
        prefrenceUserPollCommentsCount = MApplication.loadArray(mUserPollReview, userPollcommentsCount, Constants.USER_POLL_COMMENTS_COUNT, Constants.USER_POLL_COMMENTS_SIZE);
        //User poll like user saved in the prefernce is loaded into the array list
        preferenceUserPollLikeUser = MApplication.loadArray(mUserPollReview, userPollLikesUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
        //Getting the value from the particular position in array list
        mUserPolllikeCount = String.valueOf(prefrenceUserPollLikeCount.get(userPolllistposition));
        //Getting the value from the particular position in array list
        commentsCount = String.valueOf(prefrenceUserPollCommentsCount.get(userPolllistposition));
        //Getting the value from the particular position in array list
        userPolllikeUser = String.valueOf(preferenceUserPollLikeUser.get(userPolllistposition));
        //Request for the poll survey details
        pollReviewRequest();
        //Setting the comments count in prefernce
        MApplication.setString(mUserPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW, commentsCount);
        mAdView = (AdView) findViewById(R.id.adView);
        //Google adview
        MApplication.googleAd(mAdView);
        //Initailizing the views
        txtComments = (TextView)findViewById(R.id.txtCommentsCounts);

        txtLike = (TextView)findViewById(R.id.txtLike2);
        //Register a callback to be invoked when this view is clicked.
        txtComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    An intent is an abstract description of an operation to be performed. It can be used with startActivity to
            // launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components
                Intent details = new Intent(mUserPollReview, PollComments.class);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_ID, userPollId);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_TYPE, userPolltype);
                //Add extended data to the intent.
                details.putExtra(Constants.COMMENTS_COUNT_POSITION, userPolllistposition);
                //Launch a new activity.
                mUserPollReview.startActivity(details);
            }
        });
    }

    private void pollReviewRequest() {
        MApplication.materialdesignDialogStart(this);
        /**If internet connection is available**/
        if (MApplication.isNetConnected(mUserPollReview)) {
            PollReviewsRestClient.getInstance().pollReview(new String("poll_review_polls"), new String(userPollId), new String(userPolltype), new Callback<PollReviewResponseModel>() {
                @Override
                public void success(PollReviewResponseModel pollReviewResponseModel, Response response) {
                    //user poll type
                    userPolltype = pollReviewResponseModel.getResults().getPollreview().get(0).getPollType();
                    if(("1").equals(userPolltype)){
                        firstReview(pollReviewResponseModel);
                    }else if(("2").equals(userPolltype)){
                        secondReview(pollReviewResponseModel);
                    }else if(("3").equals(userPolltype)){
                        thirdReview(pollReviewResponseModel);
                    }else if(("4").equals(userPolltype)){
                        fourthReview(pollReviewResponseModel);
                    }
                    MApplication.materialdesignDialogStop();
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError,mUserPollReview);
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
        userPollView4 =new ViewHolder4();
        //Initializing the views.
        userPollView4.scrollViewYouTubeLayout =(ScrollView)findViewById(R.id.scroll);
        userPollView4.userPollTxtQuestionYouTubeLayout = (TextView) findViewById(R.id.txtStatus);
        userPollView4.userPollimageQuestion1YouTubeLayout = (SimpleDraweeView)findViewById(R.id.choose);
        userPollView4.userPollimageQuestion2YouTubeLayout = (SimpleDraweeView)findViewById(R.id.ChooseAdditional);
        userPollView4.userPollsingleOptionYouTubeLayout = (SimpleDraweeView)findViewById(R.id.singleOption);
        userPollView4.userPollHeaderImageYouTubeLayout = (ImageView)findViewById(R.id.imgProfile);
        userPollView4.userPolltxtTimeYouTubeLayout = (TextView)findViewById(R.id.txtTime);
        userPollView4.userPollHeaderNameYouTubeLayout = (TextView)findViewById(R.id.txtName);
        userPollView4.userPollHeaderCategoryYouTubeLayout = (TextView)findViewById(R.id.txtCategory);
        userPollView4.likeUnlikeYouTubeLayout = (CheckBox)findViewById(R.id.unLikeDislike);
        userPollView4.userPolltxtFirstOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtFirstOptionCount);
        userPollView4.userPolltxtSecondOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtSecondOptionCount);
        userPollView4.userPolltxtThirdOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtThirdOptionCount);
        userPollView4.userPolltxtFourthOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtFourthOptionCount);
        userPollView4.userPollprogressbarFirstOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarFirstOption);
        userPollView4.userPollprogressbarSecondOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarSecondOption);
        userPollView4.userPollprogressbarThirdOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarThirdOption);
        userPollView4.userPollprogressbarFourthOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarFourthOption);
        userPollView4.userPollrelativePrgressBar3YouTubeLayout =(RelativeLayout)findViewById(R.id.relativeProgressbar3);
        userPollView4.userPollrelativePrgressBar4YouTubeLayout =(RelativeLayout)findViewById(R.id.relativeProgressbar4);
        userPollView4.userPolltxtCountsYouTubeLayout = (TextView) findViewById(R.id.txtParticcipation);
        userPollView4.imgShareYouTubeLayout =(ImageView)findViewById(R.id.imgShare);

        if(share_id){
            userPollView4.imgShareYouTubeLayout.setVisibility(View.GONE);
        }
        userPollView4.txtLikeYouTubeLayout = (TextView)findViewById(R.id.txtLike2);
        userPollView4.userPolloption1YouTubeLayoutYouTubeLayout = (TextView)findViewById(R.id.option1);
        userPollView4.userPolloption2YouTubeLayout = (TextView) findViewById(R.id.option2);
        userPollView4.userPolloption3YouTubeLayout = (TextView)findViewById(R.id.option3);
        userPollView4.userPolloption4YouTubeLayout = (TextView)findViewById(R.id.option4);
        userPollView4.userPollGroupAnswer1YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer1);
        userPollView4.userPollGroupAnswer2YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer2);
        userPollView4.userPollGroupAnswer3YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer3);
        userPollView4.userPollGroupAnswer4YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer4);
        //Setting the visiblity view.
        userPollView4.scrollViewYouTubeLayout.setVisibility(View.VISIBLE);
        //Getting the poll question image if available
        userpollImage1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //Getting the poll question image if available
        userpollImage2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        //Setting the participate count in text view
        userPollView4.userPolltxtCountsYouTubeLayout.setText(userPollparticipateCount);
        userPollView4.userPolltxtCountsYouTubeLayout.setEnabled(false);
        //Setting the url in image view
        userPollView4.userPollHeaderImageYouTubeLayout.setImageURI(Uri.parse(userPollLogo));
        //user poll name
        userPollView4.userPollHeaderNameYouTubeLayout.setText(MApplication.getDecodedString(userPollName));
        //Category is set into the text view
        userPollView4.userPollHeaderCategoryYouTubeLayout.setText(userPollCategory);
        //User poll updated time in text view
        userPollView4.userPolltxtTimeYouTubeLayout.setText(userPollupdatedTime);
        //Setting the like in text view
        userPollView4.txtLikeYouTubeLayout.setText(mUserPolllikeCount);
        //Setting the question in text view
        userPollView4.userPollTxtQuestionYouTubeLayout.setText(pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestion());
        //Setting the comment count in text view
        txtComments.setText(MApplication.getString(mUserPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(userPolllikeUser)) {
            userPollView4.likeUnlikeYouTubeLayout.setChecked(true);
        } else {
            userPollView4.likeUnlikeYouTubeLayout.setChecked(false);
        }
        //poll answer option from the response
        userpollAnswer1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        userpollAnswer2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        userpollAnswer3 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option from the response
        userpollAnswer4 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        userPollpollOptionParticipated = pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; userPollpollOptionParticipated.size() > i; i++) {
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
                userPollView4.userPolltxtFirstOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView4.userPollprogressbarFirstOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                userPollView4.userPollGroupAnswer1YouTubeLayout.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                userPollView4.userPolltxtSecondOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView4.userPollprogressbarSecondOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                userPollView4.userPollGroupAnswer2YouTubeLayout.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                userPollView4.userPolltxtThirdOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView4.userPollprogressbarThirdOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                userPollView4.userPollGroupAnswer3YouTubeLayout.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                userPollView4.userPolltxtFourthOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView4.userPollprogressbarFourthOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                userPollView4.userPollGroupAnswer4YouTubeLayout.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(userpollImage1) && ("").equals(userpollImage2)) {
            //view gone
            userPollView4.userPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
            //view gone
            userPollView4.userPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                //user poll image1 is set into the image view
                userPollView4.userPollimageQuestion1YouTubeLayout.setImageURI(Uri.parse(userpollImage1));
                //user poll image2 is set into the image view
                userPollView4.userPollimageQuestion2YouTubeLayout.setImageURI(Uri.parse(userpollImage2));
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(userpollImage1) && ("").equals(userpollImage2)) {
                //view visible
                userPollView4.userPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                userPollView4.userPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(userpollImage1));
                //view gone
                userPollView4.userPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                userPollView4.userPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                //view visible
                userPollView4.userPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //view is set into the image view
                userPollView4.userPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(userpollImage2));
                //view gone
                userPollView4.userPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                userPollView4.userPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            } else {
                //view gone
                userPollView4.userPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                userPollView4.userPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer1)) {
            userPollView4.userPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(userpollAnswer1)) {
            userPollView4.userPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.VISIBLE);
            userPollView4.userPolloption1YouTubeLayoutYouTubeLayout.setText(userpollAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer2)) {
            userPollView4.userPolloption2YouTubeLayout.setVisibility(View.GONE);
        } else if (!("").equals(userpollAnswer2)) {
            userPollView4.userPolloption2YouTubeLayout.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            userPollView4.userPolloption2YouTubeLayout.setText(userpollAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer3)) {
            //view gone
            userPollView4.userPolloption3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            userPollView4.userPolloption4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            userPollView4.userPollrelativePrgressBar3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            userPollView4.userPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            userPollView4.userPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.GONE);
            //view gone
            userPollView4.userPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
        } else if (!("").equals(userpollAnswer3)) {
            //visible
            userPollView4.userPolloption3YouTubeLayout.setVisibility(View.VISIBLE);
            //setting the data in text view
            userPollView4.userPolloption3YouTubeLayout.setText(userpollAnswer3);
            //visible
            userPollView4.userPollrelativePrgressBar3YouTubeLayout.setVisibility(View.VISIBLE);
            //visible
            userPollView4.userPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(userpollAnswer4)) {
                //view gone
                userPollView4.userPolloption4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                userPollView4.userPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                userPollView4.userPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
            } else if (!("").equals(userpollAnswer4)) {
                //view visible
                userPollView4.userPollrelativePrgressBar4YouTubeLayout.setVisibility(View.VISIBLE);
                //view visible
                userPollView4.userPolloption4YouTubeLayout.setVisibility(View.VISIBLE);
                //Setting the the text view
                userPollView4.userPolloption4YouTubeLayout.setText(userpollAnswer4);
                //view visible
                userPollView4.userPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.

        userPollView4.userPolltxtCountsYouTubeLayout.setOnClickListener(mParticipantCount);
        userPollView4.userPolltxtCountsYouTubeLayout.setEnabled(false);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.txtLikeYouTubeLayout.setOnClickListener(mLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.imgShareYouTubeLayout.setOnClickListener(mSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.userPollimageQuestion1YouTubeLayout.setOnClickListener(mQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.userPollimageQuestion1YouTubeLayout.setOnClickListener(mQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.userPollGroupAnswer1YouTubeLayout.setOnClickListener(pollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.userPollGroupAnswer2YouTubeLayout.setOnClickListener(pollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.userPollGroupAnswer3YouTubeLayout.setOnClickListener(pollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.userPollGroupAnswer4YouTubeLayout.setOnClickListener(pollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.userPollsingleOptionYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting the string in youtube url
                MApplication.setString(mUserPollReview, Constants.YOUTUBE_URL, pollResponseModelView4.getResults().getPollreview().get(0).getYoutubeUrl());
                //If net is connected
                if (MApplication.isNetConnected((Activity) mUserPollReview)) {
                    //An intent is an abstract description of an operation to be performed.
                    // It can be used with startActivity to launch an Activity
                    Intent a = new Intent(mUserPollReview, VideoLandscapeActivity.class);
                    //Start the activity
                    mUserPollReview.startActivity(a);
                } else {
                    //Toast message will display
                    Toast.makeText(mUserPollReview, mUserPollReview.getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView4.likeUnlikeYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {  //If check box is checked
                    mlikes = 1;  //Setting the int value in variable
                    userPollView4.likeUnlikeYouTubeLayout.setChecked(true);   //set checked true
                    likesUnLikes(userPolllistposition);  //Like unlike for the poll request
                } else {
                    mlikes = 0;//Setting the int value in variable
                    userPollView4.likeUnlikeYouTubeLayout.setChecked(false);    //set checked true
                    likesUnLikes(userPolllistposition); //Like unlike for the poll request
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
        userPollView3 =new ViewHolder3();
        //Initializing the views.
        userPollView3.scrollView = (ScrollView) findViewById(R.id.scroll);
        userPollView3.userPollPhotoComparisonTxtQuestion = (TextView) findViewById(R.id.txtStatus);
        userPollView3.userPollPhotoComparisonimageQuestion1 = (ImageView) findViewById(R.id.choose);
        userPollView3.userPollPhotoComparisonimageQuestion2 = (ImageView) findViewById(R.id.ChooseAdditional);
        userPollView3.userPollPhotoComparisonsingleOption = (ImageView) findViewById(R.id.singleOption);
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        userPollView3.userPollPhotoComparisonlikeUnlike = (CheckBox) findViewById(R.id.unLikeDislike);
        userPollView3.userPollPhotoComparisonTxtLike = (TextView) findViewById(R.id.txtLike2);
        userpollImage1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        userpollImage2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        userPollView3.userPollPhotoComparisonHeaderImage = (ImageView) findViewById(R.id.imgProfile);
        userPollView3.userPollPhotoComparisontxtTime = (TextView) findViewById(R.id.txtTime);
        userPollView3.userPollPhotoComparisonHeaderName = (TextView) findViewById(R.id.txtName);
        userPollView3.userPollPhotoComparisonHeaderCategory = (TextView) findViewById(R.id.txtCategory);
        Utils.loadImageWithGlideProfileRounderCorner(this,userPollLogo.replaceAll(" ", "%20"),userPollView3.userPollPhotoComparisonHeaderImage,R.drawable.placeholder_image);
        userPollView3.userPollPhotoComparisonHeaderName.setText(MApplication.getDecodedString(userPollName));
        userPollView3.userPollPhotoComparisonHeaderCategory.setText(userPollCategory);
        userPollView3.userPollPhotoComparisontxtTime.setText(userPollupdatedTime);
        userPollView3.userPollPhotoComparisontxtCounts = (TextView) findViewById(R.id.txtParticcipation);
        userPollView3.userPollPhotoComparisontxtCounts.setText(userPollparticipateCount);
        userPollView3.userPollPhotoComparisonlikeUnlike = (CheckBox) findViewById(R.id.unLikeDislike);
        userPollView3.userPollPhotoComparisonimageAnswer1 = (ImageView) findViewById(R.id.answer1);
        userPollView3.userPollPhotoComparisonimageAnswer2 = (ImageView) findViewById(R.id.answer2);
        userPollView3.userPollPhotoComparisonimageAnswer3 = (ImageView) findViewById(R.id.answer3);
        userPollView3.userPollPhotoComparisonimageAnswer4 = (ImageView) findViewById(R.id.answer4);
        userPollView3.userPollPhotoComparisonrelativePrgressBar3 = (RelativeLayout) findViewById(R.id.relativeProgressbar3);
        userPollView3.userPollPhotoComparisonrelativePrgressBar4 = (RelativeLayout) findViewById(R.id.relativeProgressbar4);
        userPollView3.userPollPhotoComparisonprogressbarFirstOption = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        userPollView3.userPollPhotoComparisonprogressbarSecondOption = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        userPollView3.userPollPhotoComparisonprogressbarThirdOption = (ProgressBar) findViewById(R.id.progressbarThirdOption);
        userPollView3.userPollPhotoComparisonprogressbarFourthOption = (ProgressBar) findViewById(R.id.progressbarFourthOption);
        userPollView3.userPollPhotoComparisonthirdOption = (RelativeLayout) findViewById(R.id.ThirdOptionOption);
        userPollView3.userPollPhotoComparisonfourthOption = (RelativeLayout) findViewById(R.id.FourthOptionOption);
        userPollView3.userPollPhotoComparisontxtFirstOptionCount = (TextView) findViewById(R.id.txtFirstOptionCount);
        userPollView3.userPollPhotoComparisontxtSecondOptionCount = (TextView) findViewById(R.id.txtSecondOptionCount);
        userPollView3.userPollPhotoComparisontxtThirdOptionCount = (TextView) findViewById(R.id.txtThirdOptionCount);
        userPollView3.userPollPhotoComparisontxtFourthOptionCount = (TextView) findViewById(R.id.txtFourthOptionCount);
        userPollView3.userPollPhotoComparisonGroupAnswer1 = (TextView) findViewById(R.id.pollGroupAnswer1);
        userPollView3.userPollPhotoComparisonGroupAnswer2 = (TextView) findViewById(R.id.pollGroupAnswer2);
        userPollView3.userPollPhotoComparisonGroupAnswer3 = (TextView) findViewById(R.id.pollGroupAnswer3);
        userPollView3.userPollPhotoComparisonGroupAnswer4 = (TextView) findViewById(R.id.pollGroupAnswer4);
        userPollView3.userPollPhotoComparisonimgShare = (ImageView) findViewById(R.id.imgShare);

        if(share_id){
            userPollView3.userPollPhotoComparisonimgShare.setVisibility(View.GONE);
        }
        //view visible
        userPollView3.scrollView.setVisibility(View.VISIBLE);
        //Setting the poll like count
        userPollView3.userPollPhotoComparisonTxtLike.setText(mUserPolllikeCount);
        //Setting the text in text view
        txtComments.setText(MApplication.getString(mUserPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
         //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(userPolllikeUser)) {
            userPollView3.userPollPhotoComparisonlikeUnlike.setChecked(true);
        } else {
            userPollView3.userPollPhotoComparisonlikeUnlike.setChecked(false);
        }
        //Setting the question in text view
        userPollView3.userPollPhotoComparisonTxtQuestion.setText(pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        userpollAnswer1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1().replaceAll(" ", "%20");
        //poll answer option from the response
        userpollAnswer2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2().replaceAll(" ", "%20");
        //poll answer option from the response
        userpollAnswer3 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3().replaceAll(" ", "%20");
        //poll answer option from the response
        userpollAnswer4 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4().replaceAll(" ", "%20");
        //Participater answer details from the answer
        userPollpollOptionParticipated = pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll();

        //Getting the values based on the size
        for (int i = 0; userPollpollOptionParticipated.size() > i; i++) {
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
                userPollView3.userPollPhotoComparisontxtFirstOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView3.userPollPhotoComparisonGroupAnswer1.setText(mPollCount);
                //Setting the poll count in text view
                userPollView3.userPollPhotoComparisonprogressbarFirstOption.setProgress(mPercentage);
            } else if (("2").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                userPollView3.userPollPhotoComparisontxtSecondOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView3.userPollPhotoComparisonprogressbarSecondOption.setProgress(mPercentage);
                //Setting the poll count in text view
                userPollView3.userPollPhotoComparisonGroupAnswer2.setText(mPollCount);
            } else if (("3").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                userPollView3.userPollPhotoComparisontxtThirdOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView3.userPollPhotoComparisonprogressbarThirdOption.setProgress(mPercentage);
                //Setting the poll count in text view
                userPollView3.userPollPhotoComparisonGroupAnswer3.setText(mPollCount);
            } else if (("4").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                userPollView3.userPollPhotoComparisontxtFourthOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                userPollView3.userPollPhotoComparisonprogressbarFourthOption.setProgress(mPercentage);
                //Setting the poll count in text view
                userPollView3.userPollPhotoComparisonGroupAnswer4.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(userpollImage1) && ("").equals(userpollImage2)) {
            //view gone
            userPollView3.userPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                Utils.loadImageWithGlideRounderCorner(this,userpollImage1,userPollView3.userPollPhotoComparisonimageQuestion1,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,userpollImage2,userPollView3.userPollPhotoComparisonimageQuestion2,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(userpollImage1) && ("").equals(userpollImage2)) {
                //view visible
                userPollView3.userPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideRounderCorner(this,userpollImage1,userPollView3.userPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                userPollView3.userPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                userPollView3.userPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                //view visible
                userPollView3.userPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                Utils.loadImageWithGlideRounderCorner(this,userpollImage2,userPollView3.userPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                userPollView3.userPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                userPollView3.userPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
            } else {
                //view gone
                userPollView3.userPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                userPollView3.userPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer1)) {
            userPollView3.userPollPhotoComparisonimageAnswer1.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(userpollAnswer1)) {
            userPollView3.userPollPhotoComparisonimageAnswer1.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(this,userpollAnswer1,userPollView3.userPollPhotoComparisonimageAnswer1,R.drawable.placeholder_image);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer2)) {
            userPollView3.userPollPhotoComparisonimageAnswer2.setVisibility(View.GONE);
        } else if (!("").equals(userpollAnswer2)) {
            userPollView3.userPollPhotoComparisonimageAnswer2.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(this,userpollAnswer2,userPollView3.userPollPhotoComparisonimageAnswer2,R.drawable.placeholder_image);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer3)) {
            //view gone
            userPollView3.userPollPhotoComparisonimageAnswer3.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonthirdOption.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonfourthOption.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonrelativePrgressBar3.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonGroupAnswer3.setVisibility(View.GONE);
            //view gone
            userPollView3.userPollPhotoComparisonGroupAnswer4.setVisibility(View.GONE);
        } else if (!("").equals(userpollAnswer3)) {
            //visible
            userPollView3.userPollPhotoComparisonimageAnswer3.setVisibility(View.VISIBLE);

            Utils.loadImageWithGlideRounderCorner(this,userpollAnswer3,userPollView3.userPollPhotoComparisonimageAnswer3,R.drawable.placeholder_image);
            //view gone
            userPollView3.userPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(userpollAnswer4)) {
                //view gone
                userPollView3.userPollPhotoComparisonfourthOption.setVisibility(View.GONE);
                //view gone
                userPollView3.userPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
                //view gone
                userPollView3.userPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
                //view gone
                userPollView3.userPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            } else if (!("").equals(userpollAnswer4)) {
                //view visible
                userPollView3.userPollPhotoComparisonfourthOption.setVisibility(View.VISIBLE);
                //view visible
                userPollView3.userPollPhotoComparisonimageAnswer4.setVisibility(View.VISIBLE);
                //Setting the the text view
                Utils.loadImageWithGlideRounderCorner(this,userpollAnswer4,userPollView3.userPollPhotoComparisonimageAnswer4,R.drawable.placeholder_image);
                //view visible
                userPollView3.userPollPhotoComparisonrelativePrgressBar4.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonlikeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) { //If check box is checked
                    mlikes = 1;   //Setting the int value in variable
                    userPollView3.userPollPhotoComparisonlikeUnlike.setChecked(true); //set checked true
                    likesUnLikes(userPolllistposition);   //Like unlike for the poll request
                } else {
                    mlikes = 0;   //Setting the int value in variable
                    userPollView3.userPollPhotoComparisonlikeUnlike.setChecked(false); //set checked true
                    likesUnLikes(userPolllistposition);   //Like unlike for the poll request
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisontxtCounts.setEnabled(false);
        userPollView3.userPollPhotoComparisontxtCounts.setOnClickListener(mParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonTxtLike.setOnClickListener(mLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonimgShare.setOnClickListener(mSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonimageQuestion1.setOnClickListener(mQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonimageQuestion2.setOnClickListener(mQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonGroupAnswer1.setOnClickListener(pollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonGroupAnswer2.setOnClickListener(pollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonGroupAnswer3.setOnClickListener(pollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonGroupAnswer4.setOnClickListener(pollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonsingleOption.setOnClickListener(singleImageView);
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonimageAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(mUserPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, userpollAnswer1);
                //Starting the activity
                mUserPollReview.startActivity(a);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonimageAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(mUserPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, userpollAnswer2);
                //Starting the activity
                mUserPollReview.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonimageAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(mUserPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, userpollAnswer3);
                //Starting the activity
                mUserPollReview.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        userPollView3.userPollPhotoComparisonimageAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(mUserPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, userpollAnswer4);
                //Starting the activity
                mUserPollReview.startActivity(a);
            }
        });
        }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 2 this view is called.
     * @param pollResponseModelView2
     */
    private void secondReview(PollReviewResponseModel pollResponseModelView2) {
        //view holder
        viewHolder2=new ViewHolder2();
        //Initializing the views.
        viewHolder2.scrollViewMultipleOptions =(ScrollView)findViewById(R.id.scroll);
        viewHolder2.userPollTxtQuestionMultipleOptions = (TextView) findViewById(R.id.txtStatus);
        viewHolder2.userPollimageQuestion1MultipleOptions = (ImageView)findViewById(R.id.choose);
        viewHolder2.userPollimageQuestion2MultipleOptions = (ImageView)findViewById(R.id.ChooseAdditional);
        viewHolder2.userPollsingleOptionMultipleOptions = (ImageView)findViewById(R.id.singleOption);
        viewHolder2.userPollHeaderImageMultipleOptions = (ImageView)findViewById(R.id.imgProfile);
        viewHolder2.userPolltxtTimeMultipleOptions = (TextView)findViewById(R.id.txtTime);
        viewHolder2.userPollHeaderNameMultipleOptions = (TextView)findViewById(R.id.txtName);
        viewHolder2.userPollHeaderCategoryMultipleOptions = (TextView)findViewById(R.id.txtCategory);
        txtComments = (TextView)findViewById(R.id.txtCommentsCounts);
        viewHolder2.likeUnlikeMultipleOptions = (CheckBox)findViewById(R.id.unLikeDislike);
        viewHolder2.userPollGroupAnswer1MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer1);
        viewHolder2.userPollGroupAnswer2MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer2);
        viewHolder2.userPollGroupAnswer3MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer3);
        viewHolder2.userPollGroupAnswer4MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer4);
        viewHolder2.imgShareMultipleOptions =(ImageView)findViewById(R.id.imgShare);

        if(share_id){
            viewHolder2.imgShareMultipleOptions.setVisibility(View.GONE);
        }

        viewHolder2.txtLikeMultipleOptions = (TextView)findViewById(R.id.txtLike2);
        viewHolder2.userPolloption1MultipleOptions = (TextView)findViewById(R.id.option1);
        viewHolder2.userPolloption2MultipleOptions = (TextView) findViewById(R.id.option2);
        viewHolder2.userPolloption3MultipleOptions = (TextView)findViewById(R.id.option3);
        viewHolder2.userPolloption4MultipleOptions = (TextView)findViewById(R.id.option4);
        viewHolder2.userPolltxtFirstOptionCountMultipleOptions =(TextView)findViewById(R.id.txtFirstOptionCount);
        viewHolder2.userPolltxtSecondOptionCountMultipleOptions =(TextView)findViewById(R.id.txtSecondOptionCount);
        viewHolder2.userPolltxtThirdOptionCountMultipleOptions =(TextView)findViewById(R.id.txtThirdOptionCount);
        viewHolder2.userPolltxtFourthOptionCountMultipleOptions =(TextView)findViewById(R.id.txtFourthOptionCount);
        viewHolder2.userPollprogressbarFirstOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarFirstOption);
        viewHolder2.userPollprogressbarSecondOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarSecondOption);
        viewHolder2.userPollprogressbarThirdOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarThirdOption);
        viewHolder2.userPollprogressbarFourthOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarFourthOption);
        viewHolder2.userPollrelativePrgressBar3MultipleOptions =(RelativeLayout)findViewById(R.id.relativeProgressbar3);
        viewHolder2.userPollrelativePrgressBar4MultipleOptions =(RelativeLayout)findViewById(R.id.relativeProgressbar4);
        viewHolder2.userPolltxtCountsMultipleOptions = (TextView) findViewById(R.id.txtParticcipation);
        //view visible
        viewHolder2.scrollViewMultipleOptions.setVisibility(View.VISIBLE);
        //setting the participate count
        viewHolder2.userPolltxtCountsMultipleOptions.setText(userPollparticipateCount);
        viewHolder2.userPolltxtCountsMultipleOptions.setEnabled(false);
        //question image
        userpollImage1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //question image 2 from the response
        userpollImage2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        Utils.loadImageWithGlideProfileRounderCorner(this,userPollLogo.replaceAll(" ", "%20"),viewHolder2.userPollHeaderImageMultipleOptions,R.drawable.placeholder_image);
        //setting the name in text view
        viewHolder2.userPollHeaderNameMultipleOptions.setText(MApplication.getDecodedString(userPollName));
        //setting the category in text view
        viewHolder2.userPollHeaderCategoryMultipleOptions.setText(userPollCategory);
        //setting the updated time in text view
        viewHolder2.userPolltxtTimeMultipleOptions.setText(userPollupdatedTime);
        //setting the poll count in text view
        viewHolder2.txtLikeMultipleOptions.setText(mUserPolllikeCount);
        //setting the comments count in text view
        txtComments.setText(MApplication.getString(mUserPollReview,Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(userPolllikeUser)) {
            viewHolder2.likeUnlikeMultipleOptions.setChecked(true);
        } else {
            viewHolder2.likeUnlikeMultipleOptions.setChecked(false);
        }
        //Setting the question intext view
        viewHolder2.userPollTxtQuestionMultipleOptions.setText(pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        userpollAnswer1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        userpollAnswer2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        userpollAnswer3 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option pollReviewResponseModel the response
        userpollAnswer4 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        userPollpollOptionParticipated = pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; userPollpollOptionParticipated.size() > i; i++) {
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
                viewHolder2.userPolltxtFirstOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                viewHolder2.userPollprogressbarFirstOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                viewHolder2.userPollGroupAnswer1MultipleOptions.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                viewHolder2.userPolltxtSecondOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                viewHolder2.userPollprogressbarSecondOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                viewHolder2.userPollGroupAnswer2MultipleOptions.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                viewHolder2.userPolltxtThirdOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                viewHolder2.userPollprogressbarThirdOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                viewHolder2.userPollGroupAnswer3MultipleOptions.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                viewHolder2.userPolltxtFourthOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                viewHolder2.userPollprogressbarFourthOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                viewHolder2.userPollGroupAnswer4MultipleOptions.setText(mPollCount);
            }
        }
//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(userpollImage1) && ("").equals(userpollImage2)) {
            //view gone
            viewHolder2.userPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,userpollImage1,viewHolder2.userPollimageQuestion1MultipleOptions,R.drawable.placeholder_image);
                //user poll image2 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,userpollImage2,viewHolder2.userPollimageQuestion2MultipleOptions,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(userpollImage1) && ("").equals(userpollImage2)) {
                //view visible
                viewHolder2.userPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,userpollImage1,viewHolder2.userPollsingleOptionMultipleOptions,R.drawable.placeholder_image);
                //view gone
                viewHolder2.userPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                viewHolder2.userPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                //view visible
                viewHolder2.userPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,userpollImage2,viewHolder2.userPollsingleOptionMultipleOptions,R.drawable.placeholder_image);
                //view gone
                viewHolder2.userPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                viewHolder2.userPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            } else {
                //view gone
                viewHolder2.userPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                viewHolder2.userPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer1)) {
            viewHolder2.userPolloption1MultipleOptions.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(userpollAnswer1)) {
            viewHolder2.userPolloption1MultipleOptions.setVisibility(View.VISIBLE);
            viewHolder2.userPolloption1MultipleOptions.setText(userpollAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer2)) {
            viewHolder2.userPolloption2MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(userpollAnswer2)) {
            viewHolder2.userPolloption2MultipleOptions.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            viewHolder2.userPolloption2MultipleOptions.setText(userpollAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(userpollAnswer3)) {
            //view gone
            viewHolder2.userPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPollrelativePrgressBar3MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPollGroupAnswer3MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPollrelativePrgressBar3MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //view gone
            viewHolder2.userPolloption4MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(userpollAnswer3)) {
            //visible
            viewHolder2.userPolloption3MultipleOptions.setVisibility(View.VISIBLE);
            //setting the data in text view
            viewHolder2.userPolloption3MultipleOptions.setText(userpollAnswer3);
            //view gone
            viewHolder2.userPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //progressbar
            viewHolder2.userPollrelativePrgressBar3MultipleOptions.setVisibility(View.VISIBLE);
            //group answer
            viewHolder2.userPollGroupAnswer3MultipleOptions.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(userpollAnswer4)) {
                //view gone
                viewHolder2.userPolloption4MultipleOptions.setVisibility(View.GONE);
                //view gone
                viewHolder2.userPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
                //view gone
                viewHolder2.userPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
                //view gone
                viewHolder2.userPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            } else if (!("").equals(userpollAnswer4)) {
                //view visible
                viewHolder2.userPollrelativePrgressBar4MultipleOptions.setVisibility(View.VISIBLE);
                //view visible
                viewHolder2.userPolloption4MultipleOptions.setVisibility(View.VISIBLE);
                //Setting the the text view
                viewHolder2.userPolloption4MultipleOptions.setText(userpollAnswer4);
                //view visible
                viewHolder2.userPollGroupAnswer4MultipleOptions.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.likeUnlikeMultipleOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {  //If check box is checked
                    mlikes = 1;//Setting the int value in variable
                    viewHolder2.likeUnlikeMultipleOptions.setChecked(true); //set checked true
                    likesUnLikes(userPolllistposition);//Like unlike for the poll request
                } else {
                    mlikes = 0;  //Setting the int value in variable
                    viewHolder2.likeUnlikeMultipleOptions.setChecked(false);    //set checked true
                    likesUnLikes(userPolllistposition);//Like unlike for the poll request
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPolltxtCountsMultipleOptions.setOnClickListener(mParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.txtLikeMultipleOptions.setOnClickListener(mLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.imgShareMultipleOptions.setOnClickListener(mSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPollimageQuestion1MultipleOptions.setOnClickListener(mQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPollimageQuestion2MultipleOptions.setOnClickListener(mQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPollGroupAnswer1MultipleOptions.setOnClickListener(pollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPollGroupAnswer2MultipleOptions.setOnClickListener(pollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPollGroupAnswer3MultipleOptions.setOnClickListener(pollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPollGroupAnswer4MultipleOptions.setOnClickListener(pollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder2.userPollsingleOptionMultipleOptions.setOnClickListener(singleImageView);

    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 1 this view is called.
     * @param pollResponseModelView1
     */
    private void firstReview(PollReviewResponseModel pollResponseModelView1) {
        //view holder
        viewHolder1 = new ViewHolder1();
        //Initialization
        viewHolder1.scrollViewYesOrNO = (ScrollView) findViewById(R.id.scroll);
        viewHolder1.userPollGroupAnswerYesOrNO = (TextView) findViewById(R.id.pollGroupAnswer);
        viewHolder1.userPollGroupAnswer1YesOrNO = (TextView) findViewById(R.id.pollGroupAnswer1);
        viewHolder1.userPollTxtQuestionYesOrNO = (TextView) findViewById(R.id.txtStatus);
        viewHolder1.userPollimageQuestion1YesOrNO = (ImageView) findViewById(R.id.choose);
        viewHolder1.userPollimageQuestion2YesOrNO = (ImageView) findViewById(R.id.ChooseAdditional);
        viewHolder1.userPollsingleOptionYesOrNO = (ImageView) findViewById(R.id.singleOption);
        viewHolder1.userPollHeaderImageYesOrNO = (ImageView) findViewById(R.id.imgProfile);
        viewHolder1.userPolltxtTimeYesOrNO = (TextView) findViewById(R.id.txtTime);
        viewHolder1.userPollHeaderNameYesOrNO = (TextView) findViewById(R.id.txtName);
        viewHolder1.userPollHeaderCategoryYesOrNO = (TextView) findViewById(R.id.txtCategory);
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        viewHolder1.likeUnlikeYesOrNO = (CheckBox) findViewById(R.id.unLikeDislike);
        viewHolder1.txtLikeYesOrNO = (TextView) findViewById(R.id.txtLike2);
        viewHolder1.userPolltxtFirstOptionCountYesOrNO = (TextView) findViewById(R.id.txtFirstOptionCount);
        viewHolder1.userPolltxtSecondOptionCountYesOrNO = (TextView) findViewById(R.id.txtSecondOptionCount);
        viewHolder1.userPolltxtCountsYesOrNO = (TextView) findViewById(R.id.txtParticcipation);
        viewHolder1.userPolloption1YesOrNO = (TextView) findViewById(R.id.option1);
        viewHolder1.userPolloption2YesOrNO = (TextView) findViewById(R.id.option2);
        viewHolder1.userPollprogressbarFirstOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        viewHolder1.userPollprogressbarSecondOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        viewHolder1.imgShareYesOrNO = (ImageView) findViewById(R.id.imgShare);

        if(share_id){
            viewHolder1.imgShareYesOrNO.setVisibility(View.GONE);
        }
        //Setting the visiblity
        viewHolder1.scrollViewYesOrNO.setVisibility(View.VISIBLE);
        //setting the participate count
        viewHolder1.userPolltxtCountsYesOrNO.setText(userPollparticipateCount);
        viewHolder1.userPolltxtCountsYesOrNO.setEnabled(false);
        //question image 1 fromt the response
        userpollImage1 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //question image 2 fromt the response
        userpollImage2 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        Utils.loadImageWithGlide(this,userPollLogo.replaceAll(" ", "%20"),viewHolder1.userPollHeaderImageYesOrNO,R.drawable.img_ic_user);
        //setting the name in text view
        viewHolder1.userPollHeaderNameYesOrNO.setText(MApplication.getDecodedString(userPollName));
        //setting the category in text view
        viewHolder1.userPollHeaderCategoryYesOrNO.setText(userPollCategory);
        //setting the time in text view
        viewHolder1.userPolltxtTimeYesOrNO.setText(userPollupdatedTime);
        //setting the like count
        viewHolder1.txtLikeYesOrNO.setText(mUserPolllikeCount);
        //setting the comments in text view
        txtComments.setText(MApplication.getString(mUserPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //setting the question in text view
        viewHolder1.userPollTxtQuestionYesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestion());
        //setting the option1 in text view
        viewHolder1.userPolloption1YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1());
        //setting the option2 in text view
        viewHolder1.userPolloption2YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2());
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(userPolllikeUser)) {
            viewHolder1.likeUnlikeYesOrNO.setChecked(true);
        } else {
            viewHolder1.likeUnlikeYesOrNO.setChecked(false);
        }
        //Participater answer details from the answer
        userPollpollOptionParticipated = pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        //Getting the values based on the size
        for (int i = 0; userPollpollOptionParticipated.size() > i; i++) {
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
                viewHolder1.userPolltxtFirstOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                viewHolder1.userPollprogressbarFirstOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                viewHolder1.userPollGroupAnswerYesOrNO.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                viewHolder1.userPolltxtSecondOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                viewHolder1.userPollprogressbarSecondOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                viewHolder1.userPollGroupAnswer1YesOrNO.setText(mPollCount);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.userPolltxtCountsYesOrNO.setOnClickListener(mParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.txtLikeYesOrNO.setOnClickListener(mLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.imgShareYesOrNO.setOnClickListener(mSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.userPollimageQuestion1YesOrNO.setOnClickListener(mQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.userPollimageQuestion2YesOrNO.setOnClickListener(mQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.userPollGroupAnswerYesOrNO.setOnClickListener(pollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.userPollGroupAnswer1YesOrNO.setOnClickListener(pollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.userPollsingleOptionYesOrNO.setOnClickListener(singleImageView);

//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(userpollImage1) && ("").equals(userpollImage2)) {
            //view gone
            viewHolder1.userPollimageQuestion1YesOrNO.setVisibility(View.GONE);
            //view gone
            viewHolder1.userPollimageQuestion2YesOrNO.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                Utils.loadImageWithGlideRounderCorner(this,userpollImage1,viewHolder1.userPollimageQuestion1YesOrNO,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,userpollImage2,viewHolder1.userPollimageQuestion2YesOrNO,R.drawable.placeholder_image);

                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(userpollImage1) && ("").equals(userpollImage2)) {
                //view visible
                viewHolder1.userPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,userpollImage1,viewHolder1.userPollsingleOptionYesOrNO,R.drawable.placeholder_image);
                //view gone
                viewHolder1.userPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                viewHolder1.userPollimageQuestion2YesOrNO.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(userpollImage1) && !("").equals(userpollImage2)) {
                //view visible
                viewHolder1.userPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,userpollImage2,viewHolder1.userPollsingleOptionYesOrNO,R.drawable.placeholder_image);
                //view gone
                viewHolder1.userPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                viewHolder1.userPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            } else {
                //view gone
                viewHolder1.userPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                viewHolder1.userPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            }
        }


        //Interface definition for a callback to be invoked when a view is clicked.
        viewHolder1.likeUnlikeYesOrNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {   //If check box is checked
                    mlikes = 1;     //Setting the int value in variable
                    viewHolder1.likeUnlikeYesOrNO.setChecked(true);   //set checked true
                    likesUnLikes(userPolllistposition);  //Like unlike for the poll request
                } else {
                    mlikes = 0;  //Setting the int value in variable
                    viewHolder1.likeUnlikeYesOrNO.setChecked(false); //set checked true
                    likesUnLikes(userPolllistposition); //Like unlike for the poll request
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
        MApplication.materialdesignDialogStart(this);
        LikesAndUnLikeRestClient.getInstance().postCampaignPollLikes(new String("poll_likes"), new String(userId), new String(userPollId), new String(String.valueOf(mlikes))
                , new Callback<LikeUnLikeResposneModel>() {
            @Override
            public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                //If the value from the response is 1 then the user has successfully liked the poll
                if (("1").equals(likesUnlike.getResults())) {
                    //Changing the value in array list in a particular position
                    preferenceUserPollLikeUser.set(clickPosition, Integer.valueOf(1));
                    //Saving it in array
                    MApplication.saveArray(mUserPollReview, preferenceUserPollLikeUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                } else {
                    //Changing the value in array list in a particular position
                    preferenceUserPollLikeUser.set(clickPosition, Integer.valueOf(0));
                    //Saving it in array
                    MApplication.saveArray(mUserPollReview, preferenceUserPollLikeUser, Constants.USER_POLL_LIKES_USER_ARRAY, Constants.USER_POLL_LIKES_USER_SIZE);
                }
                //Toast message will display
                Toast.makeText(mUserPollReview, likesUnlike.getMsg(),
                        Toast.LENGTH_SHORT).show();
                //Changing the value in array list in a particular position
                prefrenceUserPollLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                //Saving the array in prefernce
                MApplication.saveArray(mUserPollReview, prefrenceUserPollLikeCount, Constants.USER_POLL_LIKES_COUNT_ARRAY, Constants.USER_POLL_LIKES_COUNT_SIZE);
                //Changing the value in textview
                txtLike.setText(String.valueOf(prefrenceUserPollLikeCount.get(clickPosition)));
                MApplication.materialdesignDialogStop();
            }


            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, mUserPollReview);
                MApplication.materialdesignDialogStop();
            }

        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        //Setting the comments in text view
        txtComments.setText(MApplication.getString(mUserPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder4 {
        //Scroll
        private ScrollView scrollViewYouTubeLayout;
        //Textview
        private TextView userPolltxtCountsYouTubeLayout;
        //Textview
        private TextView userPollGroupAnswer1YouTubeLayout;
        //Textvie
        private TextView userPollGroupAnswer2YouTubeLayout;
        //Textview
        private TextView userPollGroupAnswer3YouTubeLayout;
        //Textview
        private TextView userPollGroupAnswer4YouTubeLayout;
        //Textview
        private TextView userPolloption3YouTubeLayout;
        //Textview
        private TextView userPolloption4YouTubeLayout;
        //Progressbar
        private ProgressBar userPollprogressbarThirdOptionYouTubeLayout, userPollprogressbarFourthOptionYouTubeLayout;
        //Imageview
        private ImageView userPollHeaderImageYouTubeLayout;
        //Imagewview
        private ImageView imgShareYouTubeLayout;
        //Textview
        private TextView userPolltxtFirstOptionCountYouTubeLayout;
        //Textview
        private TextView userPolltxtSecondOptionCountYouTubeLayout;
        //Textview
        private TextView userPolltxtThirdOptionCountYouTubeLayout;
        //Textview
        private TextView userPolltxtFourthOptionCountYouTubeLayout;
        //Relativelayout
        private RelativeLayout userPollrelativePrgressBar3YouTubeLayout;
        //Relativelayout
        private RelativeLayout userPollrelativePrgressBar4YouTubeLayout;
        //Textview
        private TextView userPollTxtQuestionYouTubeLayout;
        ///textview
        private TextView userPolloption1YouTubeLayoutYouTubeLayout;
        //Textview
        private TextView userPolloption2YouTubeLayout;
        //Imageview
        private SimpleDraweeView userPollimageQuestion1YouTubeLayout;
        //Imageview
        private SimpleDraweeView userPollimageQuestion2YouTubeLayout;
        //Imageview
        private SimpleDraweeView userPollsingleOptionYouTubeLayout;
        //Progressbar
        private ProgressBar userPollprogressbarFirstOptionYouTubeLayout;
        //Progressbar
        private ProgressBar userPollprogressbarSecondOptionYouTubeLayout;
        //Textview
        private TextView userPolltxtTimeYouTubeLayout;
        //Textview
        private TextView txtLikeYouTubeLayout;
        //Textview
        private TextView userPollHeaderCategoryYouTubeLayout;
        //Textview
        private TextView userPollHeaderNameYouTubeLayout;
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
        private TextView userPolltxtCountsYesOrNO;
        //Textview
        private TextView userPollGroupAnswerYesOrNO;
        //Textview
        private TextView userPollGroupAnswer1YesOrNO;
        //Imageview
        private ImageView userPollHeaderImageYesOrNO;
        //Imageview
        private ImageView imgShareYesOrNO;
        //Textview
        private TextView userPolltxtFirstOptionCountYesOrNO;
        //Textview
        private TextView userPolltxtSecondOptionCountYesOrNO;
        //Textview
        private TextView userPollTxtQuestionYesOrNO;
        //Textview
        private TextView userPolloption1YesOrNO;
        //Textview
        private TextView userPolloption2YesOrNO;
        //Imageview
        private ImageView userPollimageQuestion1YesOrNO;
        //Imageview
        private ImageView userPollimageQuestion2YesOrNO;
        //Imageview
        private ImageView userPollsingleOptionYesOrNO;
        //Progressbar
        private ProgressBar userPollprogressbarFirstOptionYesOrNO;
        //Progressbar
        private ProgressBar userPollprogressbarSecondOptionYesOrNO;
        //Textview
        private TextView userPolltxtTimeYesOrNO;
        //Textview
        private TextView txtLikeYesOrNO;
        //Textview
        private TextView userPollHeaderCategoryYesOrNO;
        //Textview
        private TextView userPollHeaderNameYesOrNO;
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
        private TextView userPollPhotoComparisontxtCounts;
        //Textview
        private TextView userPollPhotoComparisonGroupAnswer1;
        //Textview
        private TextView userPollPhotoComparisonGroupAnswer2;
        //Textview
        private TextView userPollPhotoComparisonGroupAnswer3;
        //Textview
        private TextView userPollPhotoComparisonGroupAnswer4;
        //Relative layout
        private RelativeLayout userPollPhotoComparisonthirdOption;
        //Relative layout
        private RelativeLayout userPollPhotoComparisonfourthOption;
        //Progressbar
        private ProgressBar userPollPhotoComparisonprogressbarThirdOption, userPollPhotoComparisonprogressbarFourthOption;
        //Imageview
        private ImageView userPollPhotoComparisonimageAnswer1;
        //Imageview
        private ImageView userPollPhotoComparisonimageAnswer2;
        //Imageview
        private ImageView userPollPhotoComparisonimageAnswer3;
        //Imageview
        private ImageView userPollPhotoComparisonimageAnswer4;
        //Imageview
        private ImageView userPollPhotoComparisonHeaderImage;
        //Imageview
        private ImageView userPollPhotoComparisonimgShare;
        //Textview
        private TextView userPollPhotoComparisontxtFirstOptionCount;
        //Textview
        private TextView userPollPhotoComparisontxtSecondOptionCount;
        //Textview
        private TextView userPollPhotoComparisontxtThirdOptionCount;
        //Textview
        private TextView userPollPhotoComparisontxtFourthOptionCount;
        //Relative layout
        private RelativeLayout userPollPhotoComparisonrelativePrgressBar3;
        //Relative layout
        private RelativeLayout userPollPhotoComparisonrelativePrgressBar4;
        //Textview
        private TextView userPollPhotoComparisonTxtQuestion;
        //Imageview
        private ImageView userPollPhotoComparisonimageQuestion1;
        //Imageview
        private ImageView userPollPhotoComparisonimageQuestion2;
        //Imageview
        private ImageView userPollPhotoComparisonsingleOption;
        //Progressbar
        private ProgressBar userPollPhotoComparisonprogressbarFirstOption;
        //Progressbar
        private ProgressBar userPollPhotoComparisonprogressbarSecondOption;
        //Textview
        private TextView userPollPhotoComparisontxtTime;
        //Textview
        private TextView userPollPhotoComparisonTxtLike;
        //Textview
        private TextView userPollPhotoComparisonHeaderCategory;
        //Textview
        private TextView userPollPhotoComparisonHeaderName;
        //Checkbox
        private CheckBox userPollPhotoComparisonlikeUnlike;
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder2 {
//Multiple ptions scrollview
        private ScrollView scrollViewMultipleOptions;
        //participate count
        private TextView userPolltxtCountsMultipleOptions;
        //Participate Count for answer1
        private TextView userPollGroupAnswer1MultipleOptions;
        //Participate Count for answer2
        private TextView userPollGroupAnswer2MultipleOptions;
        //Participate Count for answer3
        private TextView userPollGroupAnswer3MultipleOptions;
        //Participate Count for answer4
        private TextView userPollGroupAnswer4MultipleOptions;
        //option3
        private TextView userPolloption3MultipleOptions;
        //option4
        private TextView userPolloption4MultipleOptions;
        //Progressbar
        private ProgressBar userPollprogressbarThirdOptionMultipleOptions, userPollprogressbarFourthOptionMultipleOptions;
        //Imageview
        private ImageView userPollHeaderImageMultipleOptions;
        //Imageview
        private ImageView imgShareMultipleOptions;
        //Text view
        private TextView userPolltxtFirstOptionCountMultipleOptions;
        //Text view
        private TextView userPolltxtSecondOptionCountMultipleOptions;
        private TextView userPolltxtThirdOptionCountMultipleOptions;
        //Text view
        private TextView userPolltxtFourthOptionCountMultipleOptions;
        //Relative layout
        private RelativeLayout userPollrelativePrgressBar3MultipleOptions;
        //Relative layout
        private RelativeLayout userPollrelativePrgressBar4MultipleOptions;
        //Textview
        private TextView userPollTxtQuestionMultipleOptions;
        //Textview
        private TextView userPolloption1MultipleOptions;
        //Textview
        private TextView userPolloption2MultipleOptions;
        //Imageview
        private ImageView userPollimageQuestion1MultipleOptions;
        //Imageview
        private ImageView userPollimageQuestion2MultipleOptions;
        //Imageview
        private ImageView userPollsingleOptionMultipleOptions;
        //Progressabar
        private ProgressBar userPollprogressbarFirstOptionMultipleOptions;
        //Progressabar
        private ProgressBar userPollprogressbarSecondOptionMultipleOptions;
        //Textview
        private TextView userPolltxtTimeMultipleOptions;
        //Textview
        private TextView txtLikeMultipleOptions;
        //Textview
        private TextView userPollHeaderCategoryMultipleOptions;
        //Textview
        private TextView userPollHeaderNameMultipleOptions;
        //Checkbox
        private CheckBox likeUnlikeMultipleOptions;
    }
}
