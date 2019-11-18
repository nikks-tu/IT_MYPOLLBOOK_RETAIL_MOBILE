package com.contus.campaignparticipate;

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
public class CampignPollReview extends Activity {
    //Userpoll review
    private CampignPollReview mCampaignPollReview;
    //user poll type
    private String mCampaignPollType;
    //poll id
    private String mCampaignPollId;
    //user poll image1
    private String mCampaignImage1;
    //user poll image2
    private String mCampaignImage2;
    //mCampaignOptionParticipated
    private List<PollReviewResponseModel.Results.PollReview.ParticipatePoll> mCampaignOptionParticipated;
    //mCampaignPollName
    private String mCampaignPollName;
    //mCampaignPollCategory
    private String mCampaignPollCategory;
    //mCampaignPollLogo
    private String mCampaignPollLogo;
    //user poll updated time
    private String mCampaignPollupdatedTime;
    //position
    private int mCampaignPolllistposition;
    //prefrenceCampaignPollLikeCount
    private ArrayList<Integer> prefrenceCampaignPollLikeCount;
    //campaignPollLikeCount
    private ArrayList<Integer> campaignPollLikeCount = new ArrayList<Integer>();
    //campaignPollLikesUser
    private ArrayList<Integer> campaignPollLikesUser = new ArrayList<Integer>();
    //campaignPollcommentsCount
    private ArrayList<Integer> campaignPollcommentsCount = new ArrayList<Integer>();
    //prefrenceCampaignPollCommentsCount
    private ArrayList<Integer> prefrenceCampaignPollCommentsCount;
    //preferenceCampaignPollLikeUser
    private ArrayList<Integer> preferenceCampaignPollLikeUser;
    //campaignlikeCount
    private String campaignlikeCount;
    //campaigncommentsCount
    private String campaigncommentsCount;
    //campaignlikeUser
    private String campaignlikeUser;
    //userId
    private String userId;
    //mlikes
    private int mlikes;
    //Adview
    private AdView mAdView;
    //participate count
    private String campaignparticipateCount;
    //campaignAnswer1
    private String campaignAnswer1;
    //campaignAnswer2
    private String campaignAnswer2;
    //campaignAnswer3
    private String campaignAnswer3;
    //campaignAnswer4
    private String campaignAnswer4;
    //View holder
    private ViewHolder4 campaignPollView4;
    //txtcomments
    private TextView txtComments;
    //view holder
    private ViewHolder1 campaignPollView1;
    //view holder
    private ViewHolder3 campaignPollView3;
    //view holder
    private ViewHolder2 campaignPollView2;
    //like
    private TextView txtLike;
    //image
    private String image;

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollParticipantCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activ'ity
            Intent a = new Intent(mCampaignPollReview, Participation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, mCampaignPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollLikeCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent details = new Intent(mCampaignPollReview, PollLikes.class);
            //Passing the value from one activity to another
            details.putExtra(Constants.POLL_ID, mCampaignPollId);
            //starting the activity
            mCampaignPollReview.startActivity(details);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollSharePoll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            String base64CampaignId = MApplication.convertByteCode(mCampaignPollId);
            //share url
            String shareUrl = Constants.SHARE_POLL_BASE_URL.concat(base64CampaignId);

            //sharing in gmail
            MApplication.shareGmail(mCampaignPollReview, shareUrl, MApplication.getString(CampignPollReview.this, Constants.PROFILE_USER_NAME));
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mCampaignPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, mCampaignImage1);
            //starting the activity
            mCampaignPollReview.startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mCampaignPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, mCampaignImage2);
            //starting the activity
            mCampaignPollReview.startActivity(a);
        }
    };
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollAnswer1Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(CampignPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "1");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, mCampaignPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollAnswer2Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(CampignPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "2");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, mCampaignPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollAnswer3Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(CampignPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "3");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, mCampaignPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollAnswer4Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(CampignPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "4");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, mCampaignPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mCampaignPollSingleImageView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (("").equals(mCampaignImage2)) {
                image = mCampaignImage1;
            } else {
                image = mCampaignImage2;
            }
            //Moving from one activity to another activity
            Intent a = new Intent(mCampaignPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, image);
            //starting the activity
            mCampaignPollReview.startActivity(a);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the user poll type from another activity
        mCampaignPollType = getIntent().getExtras().getString(Constants.POLL_TYPE);
        if (("1").equals(mCampaignPollType)) {
            setContentView(R.layout.publicpoll_review1);
        } else if (("2").equals(mCampaignPollType)) {
            setContentView(R.layout.publicpoll_review2);
        } else if (("3").equals(mCampaignPollType)) {
            setContentView(R.layout.publicpoll_review3);
        } else if (("4").equals(mCampaignPollType)) {
            setContentView(R.layout.publicpoll_review4);
        }
        /**View are creating when the activity is started**/
        init();
    }

    /**
     * Instantiate the method
     */
    public void init() {
        /**Initializing the activity**/
        mCampaignPollReview = this;
        //Retrieves a map of extended data from the intent.
        mCampaignPollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //Retrieves a map of extended data from the intent.
        mCampaignPollType = getIntent().getExtras().getString(Constants.TYPE);
        //Retrieves a map of extended data from the intent.
        mCampaignPolllistposition = getIntent().getExtras().getInt(Constants.ARRAY_POSITION);
        //user id from the prefernce
        userId = MApplication.getString(mCampaignPollReview, Constants.USER_ID);
        //participate count
        campaignparticipateCount = getIntent().getExtras().getString(Constants.PARTICIPATE_COUNT);
        //name
        mCampaignPollName = MApplication.getString(mCampaignPollReview, Constants.CAMPAIGN_NAME);
        //category
        mCampaignPollCategory = MApplication.getString(mCampaignPollReview, Constants.CAMPAIGN_CATEGORY);
        //logo
        mCampaignPollLogo = MApplication.getString(mCampaignPollReview, Constants.CAMPAIGN_LOGO);
        //date updated
        mCampaignPollupdatedTime = MApplication.getString(mCampaignPollReview, Constants.DATE_UPDATED);
        //User poll like count saved in the prefernce is loaded into the array list
        prefrenceCampaignPollLikeCount = MApplication.loadArray(mCampaignPollReview, campaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
        //User poll comments saved in the prefernce is loaded into the array list
        prefrenceCampaignPollCommentsCount = MApplication.loadArray(mCampaignPollReview, campaignPollcommentsCount, Constants.CAMPAIGN_POLL_COMMENTS_COUNT, Constants.CAMPAIGN_POLL_COMMENTS_SIZE);
        //User poll like user saved in the prefernce is loaded into the array list
        preferenceCampaignPollLikeUser = MApplication.loadArray(mCampaignPollReview, campaignPollLikesUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
        //Getting the value from the particular position in array list
        campaignlikeCount = String.valueOf(prefrenceCampaignPollLikeCount.get(mCampaignPolllistposition));
        //Getting the value from the particular position in array list
        campaigncommentsCount = String.valueOf(prefrenceCampaignPollCommentsCount.get(mCampaignPolllistposition));
        //Getting the value from the particular position in array list
        campaignlikeUser = String.valueOf(preferenceCampaignPollLikeUser.get(mCampaignPolllistposition));
        //Request for the poll survey details
        pollReviewRequest();
        //Setting the comments count in prefernce
        MApplication.setString(mCampaignPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW, campaigncommentsCount);
        //Initailizing the views
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        mAdView = (AdView) findViewById(R.id.adView);
        //Google adview
        MApplication.googleAd(mAdView);
        txtLike = (TextView) findViewById(R.id.txtLike2);
        //Register a callback to be invoked when this view is clicked.
        txtComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    An intent is an abstract description of an operation to be performed. It can be used with startActivity to
                // launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components
                Intent details = new Intent(mCampaignPollReview, PollComments.class);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_ID, mCampaignPollId);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_TYPE, mCampaignPollType);
                //Add extended data to the intent.
                details.putExtra(Constants.COMMENTS_COUNT_POSITION, mCampaignPolllistposition);
                //Launch a new activity.
                mCampaignPollReview.startActivity(details);
            }
        });
    }


    private void pollReviewRequest() {
        if (MApplication.isNetConnected(mCampaignPollReview)) {   /**If internet connection is available**/
            MApplication.materialdesignDialogStart(this);
            PollReviewsRestClient.getInstance().pollCampaignReview(new String("poll_review_campaign"), new String(mCampaignPollId), new String(mCampaignPollType), new Callback<PollReviewResponseModel>() {
                @Override
                public void success(PollReviewResponseModel pollReviewResponseModel, Response response) {
                    mCampaignPollType = pollReviewResponseModel.getResults().getPollreview().get(0).getPollType();    //user poll type
                    if (("1").equals(mCampaignPollType)) {
                        firstReview(pollReviewResponseModel);
                    } else if (("2").equals(mCampaignPollType)) {
                        secondReview(pollReviewResponseModel);
                    } else if (("3").equals(mCampaignPollType)) {
                        thirdReview(pollReviewResponseModel);
                    } else if (("4").equals(mCampaignPollType)) {
                        fourthReview(pollReviewResponseModel);
                    }
                    MApplication.materialdesignDialogStop();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, mCampaignPollReview);
                    MApplication.materialdesignDialogStop();
                }
            });
        }
    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 4 this view is called.
     *
     * @param pollResponseModelView4
     */
    private void fourthReview(final PollReviewResponseModel pollResponseModelView4) {
        //view holder
        campaignPollView4 = new ViewHolder4();
        //Initializing the views.
        campaignPollView4.scrollViewYouTubeLayout = (ScrollView) findViewById(R.id.scroll);
        campaignPollView4.campaignPollTxtQuestionYouTubeLayout = (TextView) findViewById(R.id.txtStatus);
        campaignPollView4.campaignPollimageQuestion1YouTubeLayout = (SimpleDraweeView) findViewById(R.id.choose);
        campaignPollView4.campaignPollimageQuestion2YouTubeLayout = (SimpleDraweeView) findViewById(R.id.ChooseAdditional);
        campaignPollView4.campaignPollsingleOptionYouTubeLayout = (SimpleDraweeView) findViewById(R.id.singleOption);
        campaignPollView4.campaignPollHeaderImageYouTubeLayout = (SimpleDraweeView) findViewById(R.id.imgProfile);
        campaignPollView4.campaignPolltxtTimeYouTubeLayout = (TextView) findViewById(R.id.txtTime);
        campaignPollView4.campaignPollHeaderNameYouTubeLayout = (TextView) findViewById(R.id.txtName);
        campaignPollView4.campaignPollHeaderCategoryYouTubeLayout = (TextView) findViewById(R.id.txtCategory);
        campaignPollView4.likeUnlikeYouTubeLayout = (CheckBox) findViewById(R.id.unLikeDislike);
        campaignPollView4.campaignPolltxtFirstOptionCountYouTubeLayout = (TextView) findViewById(R.id.txtFirstOptionCount);
        campaignPollView4.campaignPolltxtSecondOptionCountYouTubeLayout = (TextView) findViewById(R.id.txtSecondOptionCount);
        campaignPollView4.campaignPolltxtThirdOptionCountYouTubeLayout = (TextView) findViewById(R.id.txtThirdOptionCount);
        campaignPollView4.campaignPolltxtFourthOptionCountYouTubeLayout = (TextView) findViewById(R.id.txtFourthOptionCount);
        campaignPollView4.relativieOption3 = (RelativeLayout) findViewById(R.id.relativieOption3);
        campaignPollView4.relativieOption4 = (RelativeLayout) findViewById(R.id.relativeOption4);
        campaignPollView4.campaignPollprogressbarFirstOptionYouTubeLayout = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        campaignPollView4.campaignPollprogressbarSecondOptionYouTubeLayout = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        campaignPollView4.campaignPollprogressbarThirdOptionYouTubeLayout = (ProgressBar) findViewById(R.id.progressbarThirdOption);
        campaignPollView4.campaignPollprogressbarFourthOptionYouTubeLayout = (ProgressBar) findViewById(R.id.progressbarFourthOption);
        campaignPollView4.campaignPollrelativePrgressBar3YouTubeLayout = (RelativeLayout) findViewById(R.id.relativeProgressbar3);
        campaignPollView4.campaignPollrelativePrgressBar4YouTubeLayout = (RelativeLayout) findViewById(R.id.relativeProgressbar4);
        campaignPollView4.campaignPolltxtCountsYouTubeLayout = (TextView) findViewById(R.id.txtParticcipation1);
        campaignPollView4.imgShareYouTubeLayout = (ImageView) findViewById(R.id.imgShare);
        campaignPollView4.txtLikeYouTubeLayout = (TextView) findViewById(R.id.txtLike2);
        campaignPollView4.campaignPolloption1YouTubeLayoutYouTubeLayout = (TextView) findViewById(R.id.option1);
        campaignPollView4.campaignPolloption2YouTubeLayout = (TextView) findViewById(R.id.option2);
        campaignPollView4.campaignPolloption3YouTubeLayout = (TextView) findViewById(R.id.option3);
        campaignPollView4.campaignPolloption4YouTubeLayout = (TextView) findViewById(R.id.option4);
        campaignPollView4.campaignPollGroupAnswer1YouTubeLayout = (TextView) findViewById(R.id.pollGroupAnswer1);
        campaignPollView4.campaignPollGroupAnswer2YouTubeLayout = (TextView) findViewById(R.id.pollGroupAnswer2);
        campaignPollView4.campaignPollGroupAnswer3YouTubeLayout = (TextView) findViewById(R.id.pollGroupAnswer3);
        campaignPollView4.campaignPollGroupAnswer4YouTubeLayout = (TextView) findViewById(R.id.pollGroupAnswer4);

        //Setting the visiblity view.
        campaignPollView4.scrollViewYouTubeLayout.setVisibility(View.VISIBLE);
        //Getting the poll question image if available
        mCampaignImage1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage();
        //Getting the poll question image if available
        mCampaignImage2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage2();
        //Setting the participate count in text view
        campaignPollView4.campaignPolltxtCountsYouTubeLayout.setText(campaignparticipateCount);
        //Setting the url in image view
        campaignPollView4.campaignPollHeaderImageYouTubeLayout.setImageURI(Uri.parse(mCampaignPollLogo));
        //user poll name
        campaignPollView4.campaignPollHeaderNameYouTubeLayout.setText(mCampaignPollName);
        //Category is set into the text view
        campaignPollView4.campaignPollHeaderCategoryYouTubeLayout.setText(mCampaignPollCategory);
        //User poll updated time in text view
        campaignPollView4.campaignPolltxtTimeYouTubeLayout.setText(mCampaignPollupdatedTime);
        //Setting the like in text view
        campaignPollView4.txtLikeYouTubeLayout.setText(campaignlikeCount);
        //Setting the question in text view
        campaignPollView4.campaignPollTxtQuestionYouTubeLayout.setText(pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestion());
        //Setting the comment count in text view
        txtComments.setText(MApplication.getString(mCampaignPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(campaignlikeUser)) {
            campaignPollView4.likeUnlikeYouTubeLayout.setChecked(true);
        } else {
            campaignPollView4.likeUnlikeYouTubeLayout.setChecked(false);
        }
        //poll answer option from the response
        campaignAnswer1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        campaignAnswer2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        campaignAnswer3 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option from the response
        campaignAnswer4 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        mCampaignOptionParticipated = pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; mCampaignOptionParticipated.size() > i; i++) {
            //Get the poll counts from the response
            String mPollCount = pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollCounts();
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
                campaignPollView4.campaignPolltxtFirstOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView4.campaignPollprogressbarFirstOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView4.campaignPollGroupAnswer1YouTubeLayout.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView4.campaignPolltxtSecondOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView4.campaignPollprogressbarSecondOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView4.campaignPollGroupAnswer2YouTubeLayout.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView4.campaignPolltxtThirdOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView4.campaignPollprogressbarThirdOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView4.campaignPollGroupAnswer3YouTubeLayout.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView4.campaignPolltxtFourthOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView4.campaignPollprogressbarFourthOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView4.campaignPollGroupAnswer4YouTubeLayout.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
            //view gone
            campaignPollView4.campaignPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
            //view gone
            campaignPollView4.campaignPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //user poll image1 is set into the image view
                campaignPollView4.campaignPollimageQuestion1YouTubeLayout.setImageURI(Uri.parse(mCampaignImage1));
                //user poll image2 is set into the image view
                campaignPollView4.campaignPollimageQuestion2YouTubeLayout.setImageURI(Uri.parse(mCampaignImage2));
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView4.campaignPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                campaignPollView4.campaignPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(mCampaignImage1));
                //view gone
                campaignPollView4.campaignPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                campaignPollView4.campaignPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView4.campaignPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //view is set into the image view
                campaignPollView4.campaignPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(mCampaignImage2));
                //view gone
                campaignPollView4.campaignPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                campaignPollView4.campaignPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            } else {
                //view gone
                campaignPollView4.campaignPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                campaignPollView4.campaignPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer1)) {
            campaignPollView4.campaignPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(campaignAnswer1)) {
            campaignPollView4.campaignPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.VISIBLE);
            campaignPollView4.campaignPolloption1YouTubeLayoutYouTubeLayout.setText(campaignAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer2)) {
            campaignPollView4.campaignPolloption2YouTubeLayout.setVisibility(View.GONE);
        } else if (!("").equals(campaignAnswer2)) {
            campaignPollView4.campaignPolloption2YouTubeLayout.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            campaignPollView4.campaignPolloption2YouTubeLayout.setText(campaignAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer3)) {
            //view gone
            campaignPollView4.campaignPolloption3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            campaignPollView4.campaignPolloption4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            campaignPollView4.campaignPollrelativePrgressBar3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            campaignPollView4.campaignPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            campaignPollView4.campaignPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.GONE);
            //view gone
            campaignPollView4.campaignPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
            //Visiblity gone
            campaignPollView4.relativieOption3.setVisibility(View.GONE);
            //Visiblity gone
            campaignPollView4.relativieOption4.setVisibility(View.GONE);
        } else if (!("").equals(campaignAnswer3)) {
            //visible
            campaignPollView4.campaignPolloption3YouTubeLayout.setVisibility(View.VISIBLE);
            //setting the data in text view
            campaignPollView4.campaignPolloption3YouTubeLayout.setText(campaignAnswer3);
            //visible
            campaignPollView4.campaignPollrelativePrgressBar3YouTubeLayout.setVisibility(View.VISIBLE);
            //visible
            campaignPollView4.campaignPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(campaignAnswer4)) {
                //view gone
                campaignPollView4.campaignPolloption4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                campaignPollView4.campaignPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                campaignPollView4.campaignPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
                //Visiblity gone
                campaignPollView4.relativieOption3.setVisibility(View.GONE);
                //Visiblity gone
                campaignPollView4.relativieOption4.setVisibility(View.GONE);
            } else if (!("").equals(campaignAnswer4)) {
                //view visible
                campaignPollView4.campaignPollrelativePrgressBar4YouTubeLayout.setVisibility(View.VISIBLE);
                //view visible
                campaignPollView4.campaignPolloption4YouTubeLayout.setVisibility(View.VISIBLE);
                //Setting the the text view
                campaignPollView4.campaignPolloption4YouTubeLayout.setText(campaignAnswer4);
                //view visible
                campaignPollView4.campaignPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPolltxtCountsYouTubeLayout.setOnClickListener(mCampaignPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.txtLikeYouTubeLayout.setOnClickListener(mCampaignPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.imgShareYouTubeLayout.setOnClickListener(mCampaignPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPollimageQuestion1YouTubeLayout.setOnClickListener(mCampaignPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPollimageQuestion1YouTubeLayout.setOnClickListener(mCampaignPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPollGroupAnswer1YouTubeLayout.setOnClickListener(mCampaignPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPollGroupAnswer2YouTubeLayout.setOnClickListener(mCampaignPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPollGroupAnswer3YouTubeLayout.setOnClickListener(mCampaignPollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPollGroupAnswer4YouTubeLayout.setOnClickListener(mCampaignPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.campaignPollsingleOptionYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting the string in youtube url
                MApplication.setString(mCampaignPollReview, Constants.YOUTUBE_URL, pollResponseModelView4.getResults().getPollreview().get(0).getYoutubeUrl());
                //If net is connected
                if (MApplication.isNetConnected((Activity) mCampaignPollReview)) {
                    //An intent is an abstract description of an operation to be performed.
                    // It can be used with startActivity to launch an Activity
                    Intent a = new Intent(mCampaignPollReview, VideoLandscapeActivity.class);
                    //Start the activity
                    mCampaignPollReview.startActivity(a);
                } else {
                    //Toast message will display
                    Toast.makeText(mCampaignPollReview, mCampaignPollReview.getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView4.likeUnlikeYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) { //If check box is checked
                    mlikes = 1;  //Setting the int value in variable
                    campaignPollView4.likeUnlikeYouTubeLayout.setChecked(true);  //set checked true
                    likesUnLikes(mCampaignPolllistposition);//Like unlike for the poll request
                } else {
                    mlikes = 0; //Setting the int value in variable
                    campaignPollView4.likeUnlikeYouTubeLayout.setChecked(false);  //set checked true
                    likesUnLikes(mCampaignPolllistposition);  //Like unlike for the poll request
                }
            }
        });
    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 3 this view is called.
     *
     * @param pollReviewResponseModelView3
     */
    private void thirdReview(PollReviewResponseModel pollReviewResponseModelView3) {
        //view holder
        campaignPollView3 = new ViewHolder3();
        //Initializing the views.
        campaignPollView3.scrollView = (ScrollView) findViewById(R.id.scroll);
        campaignPollView3.campaignPollPhotoComparisonTxtQuestion = (TextView) findViewById(R.id.txtStatus);
        campaignPollView3.campaignPollPhotoComparisonimageQuestion1 = (SimpleDraweeView) findViewById(R.id.choose);
        campaignPollView3.campaignPollPhotoComparisonimageQuestion2 = (SimpleDraweeView) findViewById(R.id.ChooseAdditional);
        campaignPollView3.campaignPollPhotoComparisonsingleOption = (SimpleDraweeView) findViewById(R.id.singleOption);
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        campaignPollView3.campaignPollPhotoComparisonlikeUnlike = (CheckBox) findViewById(R.id.unLikeDislike);
        campaignPollView3.campaignPollPhotoComparisonTxtLike = (TextView) findViewById(R.id.txtLike2);
        mCampaignImage1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage();
        mCampaignImage2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage2();
        campaignPollView3.campaignPollPhotoComparisonHeaderImage = (SimpleDraweeView) findViewById(R.id.imgProfile);
        campaignPollView3.campaignPollPhotoComparisontxtTime = (TextView) findViewById(R.id.txtTime);
        campaignPollView3.campaignPollPhotoComparisonHeaderName = (TextView) findViewById(R.id.txtName);
        campaignPollView3.campaignPollPhotoComparisonHeaderCategory = (TextView) findViewById(R.id.txtCategory);
        campaignPollView3.campaignPollPhotoComparisonHeaderImage.setImageURI(Uri.parse(mCampaignPollLogo));
        campaignPollView3.campaignPollPhotoComparisonHeaderName.setText(mCampaignPollName);
        campaignPollView3.campaignPollPhotoComparisonHeaderCategory.setText(mCampaignPollCategory);
        campaignPollView3.campaignPollPhotoComparisontxtTime.setText(mCampaignPollupdatedTime);
        campaignPollView3.campaignPollPhotoComparisontxtCounts = (TextView) findViewById(R.id.txtParticcipation);
        campaignPollView3.campaignPollPhotoComparisontxtCounts.setText(campaignparticipateCount);
        campaignPollView3.campaignPollPhotoComparisonimageAnswer1 = (SimpleDraweeView) findViewById(R.id.answer1);
        campaignPollView3.campaignPollPhotoComparisonimageAnswer2 = (SimpleDraweeView) findViewById(R.id.answer2);
        campaignPollView3.campaignPollPhotoComparisonimageAnswer3 = (SimpleDraweeView) findViewById(R.id.answer3);
        campaignPollView3.campaignPollPhotoComparisonimageAnswer4 = (SimpleDraweeView) findViewById(R.id.answer4);
        campaignPollView3.campaignPollPhotoComparisonrelativePrgressBar3 = (RelativeLayout) findViewById(R.id.relativeProgressbar3);
        campaignPollView3.campaignPollPhotoComparisonrelativePrgressBar4 = (RelativeLayout) findViewById(R.id.relativeProgressbar4);
        campaignPollView3.campaignPollPhotoComparisonprogressbarFirstOption = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        campaignPollView3.campaignPollPhotoComparisonprogressbarSecondOption = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        campaignPollView3.campaignPollPhotoComparisonprogressbarThirdOption = (ProgressBar) findViewById(R.id.progressbarThirdOption);
        campaignPollView3.campaignPollPhotoComparisonprogressbarFourthOption = (ProgressBar) findViewById(R.id.progressbarFourthOption);
        campaignPollView3.campaignPollPhotoComparisonthirdOption = (RelativeLayout) findViewById(R.id.ThirdOptionOption);
        campaignPollView3.campaignPollPhotoComparisonfourthOption = (RelativeLayout) findViewById(R.id.FourthOptionOption);
        campaignPollView3.campaignPollPhotoComparisontxtFirstOptionCount = (TextView) findViewById(R.id.txtFirstOptionCount);
        campaignPollView3.campaignPollPhotoComparisontxtSecondOptionCount = (TextView) findViewById(R.id.txtSecondOptionCount);
        campaignPollView3.campaignPollPhotoComparisontxtThirdOptionCount = (TextView) findViewById(R.id.txtThirdOptionCount);
        campaignPollView3.campaignPollPhotoComparisontxtFourthOptionCount = (TextView) findViewById(R.id.txtFourthOptionCount);
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer1 = (TextView) findViewById(R.id.pollGroupAnswer1);
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer2 = (TextView) findViewById(R.id.pollGroupAnswer2);
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer3 = (TextView) findViewById(R.id.pollGroupAnswer3);
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer4 = (TextView) findViewById(R.id.pollGroupAnswer4);
        campaignPollView3.campaignPollPhotoComparisonimgShare = (ImageView) findViewById(R.id.imgShare);
        //view visible
        campaignPollView3.scrollView.setVisibility(View.VISIBLE);
        //Setting the poll like count
        campaignPollView3.campaignPollPhotoComparisonTxtLike.setText(campaignlikeCount);
        //Setting the text in text view
        txtComments.setText(MApplication.getString(mCampaignPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(campaignlikeUser)) {
            campaignPollView3.campaignPollPhotoComparisonlikeUnlike.setChecked(true);
        } else {
            campaignPollView3.campaignPollPhotoComparisonlikeUnlike.setChecked(false);
        }
        //Setting the question in text view
        campaignPollView3.campaignPollPhotoComparisonTxtQuestion.setText(pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        campaignAnswer1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        campaignAnswer2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        campaignAnswer3 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option from the response
        campaignAnswer4 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        mCampaignOptionParticipated = pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll();

        //Getting the values based on the size
        for (int i = 0; mCampaignOptionParticipated.size() > i; i++) {
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
                campaignPollView3.campaignPollPhotoComparisontxtFirstOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView3.campaignPollPhotoComparisonGroupAnswer1.setText(mPollCount);
                //Setting the poll count in text view
                campaignPollView3.campaignPollPhotoComparisonprogressbarFirstOption.setProgress(mPercentage);
            } else if (("2").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView3.campaignPollPhotoComparisontxtSecondOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView3.campaignPollPhotoComparisonprogressbarSecondOption.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView3.campaignPollPhotoComparisonGroupAnswer2.setText(mPollCount);
            } else if (("3").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView3.campaignPollPhotoComparisontxtThirdOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView3.campaignPollPhotoComparisonprogressbarThirdOption.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView3.campaignPollPhotoComparisonGroupAnswer3.setText(mPollCount);
            } else if (("4").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView3.campaignPollPhotoComparisontxtFourthOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView3.campaignPollPhotoComparisonprogressbarFourthOption.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView3.campaignPollPhotoComparisonGroupAnswer4.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
            //view gone
            campaignPollView3.campaignPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            //view gone
            campaignPollView3.campaignPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage1,campaignPollView3.campaignPollPhotoComparisonimageQuestion1,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage2,campaignPollView3.campaignPollPhotoComparisonimageQuestion2,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView3.campaignPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage1,campaignPollView3.campaignPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                campaignPollView3.campaignPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                campaignPollView3.campaignPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView3.campaignPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage2,campaignPollView3.campaignPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                campaignPollView3.campaignPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                campaignPollView3.campaignPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
            } else {
                //view gone
                campaignPollView3.campaignPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                campaignPollView3.campaignPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer1)) {
            campaignPollView3.campaignPollPhotoComparisonimageAnswer1.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(campaignAnswer1)) {
            campaignPollView3.campaignPollPhotoComparisonimageAnswer1.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(this,campaignAnswer1,campaignPollView3.campaignPollPhotoComparisonimageAnswer1,R.drawable.placeholder_image);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer2)) {
            campaignPollView3.campaignPollPhotoComparisonimageAnswer2.setVisibility(View.GONE);
        } else if (!("").equals(campaignAnswer2)) {
            campaignPollView3.campaignPollPhotoComparisonimageAnswer2.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            Utils.loadImageWithGlideRounderCorner(this,campaignAnswer2,campaignPollView3.campaignPollPhotoComparisonimageAnswer2,R.drawable.placeholder_image);

        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer3)) {
            //view gone
            campaignPollView3.campaignPollPhotoComparisonimageAnswer3.setVisibility(View.GONE);
            //view gone
            campaignPollView3.campaignPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
            //view gone
            campaignPollView3.campaignPollPhotoComparisonthirdOption.setVisibility(View.GONE);
            //view gone
            campaignPollView3.campaignPollPhotoComparisonrelativePrgressBar3.setVisibility(View.GONE);
            //view gone
            campaignPollView3.campaignPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //view gone
            campaignPollView3.campaignPollPhotoComparisonfourthOption.setVisibility(View.GONE);
        } else if (!("").equals(campaignAnswer3)) {
            //visible
            campaignPollView3.campaignPollPhotoComparisonimageAnswer3.setVisibility(View.VISIBLE);
            //setting the data in text view
            Utils.loadImageWithGlideRounderCorner(this,campaignAnswer3,campaignPollView3.campaignPollPhotoComparisonimageAnswer3,R.drawable.placeholder_image);
            //view gone
            campaignPollView3.campaignPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(campaignAnswer4)) {
                //view gone
                campaignPollView3.campaignPollPhotoComparisonfourthOption.setVisibility(View.GONE);
                //view gone
                campaignPollView3.campaignPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
                //view gone
                campaignPollView3.campaignPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            } else if (!("").equals(campaignAnswer4)) {
                //view visible
                campaignPollView3.campaignPollPhotoComparisonfourthOption.setVisibility(View.VISIBLE);
                //view visible
                campaignPollView3.campaignPollPhotoComparisonimageAnswer4.setVisibility(View.VISIBLE);
                //Setting the the text view
                Utils.loadImageWithGlideRounderCorner(this,campaignAnswer4,campaignPollView3.campaignPollPhotoComparisonimageAnswer4,R.drawable.placeholder_image);
                //view visible
                campaignPollView3.campaignPollPhotoComparisonrelativePrgressBar4.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonlikeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If check box is checked
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;     //Setting the int value in variable
                    campaignPollView3.campaignPollPhotoComparisonlikeUnlike.setChecked(true);   //set checked true
                    likesUnLikes(mCampaignPolllistposition); //Like unlike for the poll request
                } else {
                    mlikes = 0;   //Setting the int value in variable
                    campaignPollView3.campaignPollPhotoComparisonlikeUnlike.setChecked(false);  //set checked true
                    likesUnLikes(mCampaignPolllistposition);  //Like unlike for the poll request
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisontxtCounts.setOnClickListener(mCampaignPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonTxtLike.setOnClickListener(mCampaignPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonimgShare.setOnClickListener(mCampaignPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonimageQuestion1.setOnClickListener(mCampaignPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonimageQuestion2.setOnClickListener(mCampaignPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer1.setOnClickListener(mCampaignPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer2.setOnClickListener(mCampaignPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer3.setOnClickListener(mCampaignPollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonGroupAnswer4.setOnClickListener(mCampaignPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonsingleOption.setOnClickListener(mCampaignPollSingleImageView);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonimageAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(mCampaignPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, campaignAnswer1);
                //Starting the activity
                mCampaignPollReview.startActivity(a);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonimageAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(mCampaignPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, campaignAnswer2);
                //Starting the activity
                mCampaignPollReview.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonimageAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(mCampaignPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, campaignAnswer3);
                //Starting the activity
                mCampaignPollReview.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView3.campaignPollPhotoComparisonimageAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(mCampaignPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, campaignAnswer4);
                //Starting the activity
                mCampaignPollReview.startActivity(a);
            }
        });
    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 2 this view is called.
     *
     * @param pollResponseModelView2
     */
    private void secondReview(PollReviewResponseModel pollResponseModelView2) {
        //view holder
        campaignPollView2 = new ViewHolder2();
        //Initializing the views.
        campaignPollView2.scrollViewMultipleOptions = (ScrollView) findViewById(R.id.scroll);
        campaignPollView2.campaignPollTxtQuestionMultipleOptions = (TextView) findViewById(R.id.txtStatus);
        campaignPollView2.campaignPollimageQuestion1MultipleOptions = (ImageView) findViewById(R.id.choose);
        campaignPollView2.campaignPollimageQuestion2MultipleOptions = (ImageView) findViewById(R.id.ChooseAdditional);
        campaignPollView2.campaignPollsingleOptionMultipleOptions = (ImageView) findViewById(R.id.singleOption);
        campaignPollView2.campaignPollHeaderImageMultipleOptions = (ImageView) findViewById(R.id.imgProfile);
        campaignPollView2.campaignPolltxtTimeMultipleOptions = (TextView) findViewById(R.id.txtTime);
        campaignPollView2.campaignPollHeaderNameMultipleOptions = (TextView) findViewById(R.id.txtName);
        campaignPollView2.campaignPollHeaderCategoryMultipleOptions = (TextView) findViewById(R.id.txtCategory);
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        campaignPollView2.likeUnlikeMultipleOptions = (CheckBox) findViewById(R.id.unLikeDislike);
        campaignPollView2.campaignPollGroupAnswer1MultipleOptions = (TextView) findViewById(R.id.pollGroupAnswer1);
        campaignPollView2.campaignPollGroupAnswer2MultipleOptions = (TextView) findViewById(R.id.pollGroupAnswer2);
        campaignPollView2.campaignPollGroupAnswer3MultipleOptions = (TextView) findViewById(R.id.pollGroupAnswer3);
        campaignPollView2.campaignPollGroupAnswer4MultipleOptions = (TextView) findViewById(R.id.pollGroupAnswer4);
        campaignPollView2.imgShareMultipleOptions = (ImageView) findViewById(R.id.imgShare);
        campaignPollView2.txtLikeMultipleOptions = (TextView) findViewById(R.id.txtLike2);
        campaignPollView2.campaignPolloption1MultipleOptions = (TextView) findViewById(R.id.option1);
        campaignPollView2.campaignPolloption2MultipleOptions = (TextView) findViewById(R.id.option2);
        campaignPollView2.campaignPolloption3MultipleOptions = (TextView) findViewById(R.id.option3);
        campaignPollView2.campaignPolloption4MultipleOptions = (TextView) findViewById(R.id.option4);
        campaignPollView2.campaignPolltxtFirstOptionCountMultipleOptions = (TextView) findViewById(R.id.txtFirstOptionCount);
        campaignPollView2.campaignPolltxtSecondOptionCountMultipleOptions = (TextView) findViewById(R.id.txtSecondOptionCount);
        campaignPollView2.campaignPolltxtThirdOptionCountMultipleOptions = (TextView) findViewById(R.id.txtThirdOptionCount);
        campaignPollView2.campaignPolltxtFourthOptionCountMultipleOptions = (TextView) findViewById(R.id.txtFourthOptionCount);
        campaignPollView2.campaignPollprogressbarFirstOptionMultipleOptions = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        campaignPollView2.campaignPollprogressbarSecondOptionMultipleOptions = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        campaignPollView2.campaignPollprogressbarThirdOptionMultipleOptions = (ProgressBar) findViewById(R.id.progressbarThirdOption);
        campaignPollView2.campaignPollprogressbarFourthOptionMultipleOptions = (ProgressBar) findViewById(R.id.progressbarFourthOption);
        campaignPollView2.campaignPollrelativePrgressBar3MultipleOptions = (RelativeLayout) findViewById(R.id.relativeProgressbar3);
        campaignPollView2.campaignPollrelativePrgressBar4MultipleOptions = (RelativeLayout) findViewById(R.id.relativeProgressbar4);
        campaignPollView2.campaignPolltxtCountsMultipleOptions = (TextView) findViewById(R.id.txtParticcipation);
        //view visible
        campaignPollView2.scrollViewMultipleOptions.setVisibility(View.VISIBLE);
        //setting the participate count
        campaignPollView2.campaignPolltxtCountsMultipleOptions.setText(campaignparticipateCount);
        //question image
        mCampaignImage1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage();
        //question image 2 from the response
        mCampaignImage2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage2();
        //setting the image uri
        campaignPollView2.campaignPollHeaderImageMultipleOptions.setImageURI(Uri.parse(mCampaignPollLogo));
        //setting the name in text view
        campaignPollView2.campaignPollHeaderNameMultipleOptions.setText(mCampaignPollName);
        //setting the category in text view
        campaignPollView2.campaignPollHeaderCategoryMultipleOptions.setText(mCampaignPollCategory);
        //setting the updated time in text view
        campaignPollView2.campaignPolltxtTimeMultipleOptions.setText(mCampaignPollupdatedTime);
        //setting the poll count in text view
        campaignPollView2.txtLikeMultipleOptions.setText(campaignlikeCount);
        //setting the comments count in text view
        txtComments.setText(MApplication.getString(mCampaignPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(campaignlikeUser)) {
            campaignPollView2.likeUnlikeMultipleOptions.setChecked(true);
        } else {
            campaignPollView2.likeUnlikeMultipleOptions.setChecked(false);
        }
        //Setting the question intext view
        campaignPollView2.campaignPollTxtQuestionMultipleOptions.setText(pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        campaignAnswer1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        campaignAnswer2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        campaignAnswer3 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option pollReviewResponseModel the response
        campaignAnswer4 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        mCampaignOptionParticipated = pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; mCampaignOptionParticipated.size() > i; i++) {
            //Get the poll counts from the response
            String mPollCount = pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollCounts();
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
                campaignPollView2.campaignPolltxtFirstOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView2.campaignPollprogressbarFirstOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView2.campaignPollGroupAnswer1MultipleOptions.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView2.campaignPolltxtSecondOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView2.campaignPollprogressbarSecondOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView2.campaignPollGroupAnswer2MultipleOptions.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView2.campaignPolltxtThirdOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView2.campaignPollprogressbarThirdOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView2.campaignPollGroupAnswer3MultipleOptions.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView2.campaignPolltxtFourthOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView2.campaignPollprogressbarFourthOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView2.campaignPollGroupAnswer4MultipleOptions.setText(mPollCount);
            }
        }
//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
            //view gone
            campaignPollView2.campaignPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
            //view gone
            campaignPollView2.campaignPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage1,campaignPollView2.campaignPollimageQuestion1MultipleOptions,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage2,campaignPollView2.campaignPollimageQuestion2MultipleOptions,R.drawable.placeholder_image);

                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView2.campaignPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage1,campaignPollView2.campaignPollsingleOptionMultipleOptions,R.drawable.placeholder_image);
                //view gone
                campaignPollView2.campaignPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                campaignPollView2.campaignPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView2.campaignPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,mCampaignImage2,campaignPollView2.campaignPollsingleOptionMultipleOptions,R.drawable.placeholder_image);

                campaignPollView2.campaignPollsingleOptionMultipleOptions.setImageURI(Uri.parse(mCampaignImage2));
                //view gone
                campaignPollView2.campaignPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                campaignPollView2.campaignPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            } else {
                //view gone
                campaignPollView2.campaignPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                campaignPollView2.campaignPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer1)) {
            campaignPollView2.campaignPolloption1MultipleOptions.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(campaignAnswer1)) {
            campaignPollView2.campaignPolloption1MultipleOptions.setVisibility(View.VISIBLE);
            campaignPollView2.campaignPolloption1MultipleOptions.setText(campaignAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer2)) {
            campaignPollView2.campaignPolloption2MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(campaignAnswer2)) {
            campaignPollView2.campaignPolloption2MultipleOptions.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            campaignPollView2.campaignPolloption2MultipleOptions.setText(campaignAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(campaignAnswer3)) {
            //view gone
            campaignPollView2.campaignPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            campaignPollView2.campaignPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            campaignPollView2.campaignPollrelativePrgressBar3MultipleOptions.setVisibility(View.GONE);
            //view gone
            campaignPollView2.campaignPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //view gone
            campaignPollView2.campaignPollGroupAnswer3MultipleOptions.setVisibility(View.GONE);
            //view gone
            campaignPollView2.campaignPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(campaignAnswer3)) {
            //visible
            campaignPollView2.campaignPolloption3MultipleOptions.setVisibility(View.VISIBLE);
            //setting the data in text view
            campaignPollView2.campaignPolloption3MultipleOptions.setText(campaignAnswer3);
            //view gone
            campaignPollView2.campaignPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //progressbar
            campaignPollView2.campaignPollrelativePrgressBar3MultipleOptions.setVisibility(View.VISIBLE);
            //group answer
            campaignPollView2.campaignPollGroupAnswer3MultipleOptions.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(campaignAnswer4)) {
                //view gone
                campaignPollView2.campaignPolloption4MultipleOptions.setVisibility(View.GONE);
                //view gone
                campaignPollView2.campaignPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
                //view gone
                campaignPollView2.campaignPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
            } else if (!("").equals(campaignAnswer4)) {
                //view visible
                campaignPollView2.campaignPollrelativePrgressBar4MultipleOptions.setVisibility(View.VISIBLE);
                //view visible
                campaignPollView2.campaignPolloption4MultipleOptions.setVisibility(View.VISIBLE);
                //Setting the the text view
                campaignPollView2.campaignPolloption4MultipleOptions.setText(campaignAnswer4);
                //view visible
                campaignPollView2.campaignPollGroupAnswer4MultipleOptions.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.likeUnlikeMultipleOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) { //If check box is checked
                    mlikes = 1;   //Setting the int value in variable
                    campaignPollView2.likeUnlikeMultipleOptions.setChecked(true);   //set checked true
                    likesUnLikes(mCampaignPolllistposition);    //Like unlike for the poll request
                } else {
                    mlikes = 0; //Setting the int value in variable
                    campaignPollView2.likeUnlikeMultipleOptions.setChecked(false);  //set checked true
                    likesUnLikes(mCampaignPolllistposition); //Like unlike for the poll request
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPolltxtCountsMultipleOptions.setOnClickListener(mCampaignPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.txtLikeMultipleOptions.setOnClickListener(mCampaignPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.imgShareMultipleOptions.setOnClickListener(mCampaignPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPollimageQuestion1MultipleOptions.setOnClickListener(mCampaignPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPollimageQuestion2MultipleOptions.setOnClickListener(mCampaignPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPollGroupAnswer1MultipleOptions.setOnClickListener(mCampaignPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPollGroupAnswer2MultipleOptions.setOnClickListener(mCampaignPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPollGroupAnswer3MultipleOptions.setOnClickListener(mCampaignPollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPollGroupAnswer4MultipleOptions.setOnClickListener(mCampaignPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView2.campaignPollsingleOptionMultipleOptions.setOnClickListener(mCampaignPollSingleImageView);

    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 1 this view is called.
     *
     * @param pollResponseModelView1
     */
    private void firstReview(PollReviewResponseModel pollResponseModelView1) {
        //view holder
        campaignPollView1 = new ViewHolder1();
        //Initialization
        campaignPollView1.scrollViewYesOrNO = (ScrollView) findViewById(R.id.scroll);
        campaignPollView1.campaignPollGroupAnswerYesOrNO = (TextView) findViewById(R.id.pollGroupAnswer);
        campaignPollView1.campaignPollGroupAnswer1YesOrNO = (TextView) findViewById(R.id.pollGroupAnswer1);
        campaignPollView1.campaignPollTxtQuestionYesOrNO = (TextView) findViewById(R.id.txtStatus);
        campaignPollView1.campaignPollimageQuestion1YesOrNO = (SimpleDraweeView) findViewById(R.id.choose);
        campaignPollView1.campaignPollimageQuestion2YesOrNO = (SimpleDraweeView) findViewById(R.id.ChooseAdditional);
        campaignPollView1.campaignPollsingleOptionYesOrNO = (SimpleDraweeView) findViewById(R.id.singleOption);
        campaignPollView1.campaignPollHeaderImageYesOrNO = (SimpleDraweeView) findViewById(R.id.imgProfile);
        campaignPollView1.campaignPolltxtTimeYesOrNO = (TextView) findViewById(R.id.txtTime);
        campaignPollView1.campaignPollHeaderNameYesOrNO = (TextView) findViewById(R.id.txtName);
        campaignPollView1.campaignPollHeaderCategoryYesOrNO = (TextView) findViewById(R.id.txtCategory);
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        campaignPollView1.likeUnlikeYesOrNO = (CheckBox) findViewById(R.id.unLikeDislike);
        campaignPollView1.txtLikeYesOrNO = (TextView) findViewById(R.id.txtLike2);
        campaignPollView1.campaignPolltxtFirstOptionCountYesOrNO = (TextView) findViewById(R.id.txtFirstOptionCount);
        campaignPollView1.campaignPolltxtSecondOptionCountYesOrNO = (TextView) findViewById(R.id.txtSecondOptionCount);
        campaignPollView1.campaignPolltxtCountsYesOrNO = (TextView) findViewById(R.id.txtParticcipation);
        campaignPollView1.campaignPolloption1YesOrNO = (TextView) findViewById(R.id.option1);
        campaignPollView1.campaignPolloption2YesOrNO = (TextView) findViewById(R.id.option2);
        campaignPollView1.campaignPollprogressbarFirstOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        campaignPollView1.campaignPollprogressbarSecondOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        campaignPollView1.imgShareYesOrNO = (ImageView) findViewById(R.id.imgShare);
        //Setting the visiblity
        campaignPollView1.scrollViewYesOrNO.setVisibility(View.VISIBLE);
        //setting the participate count
        campaignPollView1.campaignPolltxtCountsYesOrNO.setText(campaignparticipateCount);
        //question image 1 fromt the response
        mCampaignImage1 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage();
        //question image 2 fromt the response
        mCampaignImage2 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage2();
        //Setting the image
        campaignPollView1.campaignPollHeaderImageYesOrNO.setImageURI(Uri.parse(mCampaignPollLogo));
        //setting the name in text view
        campaignPollView1.campaignPollHeaderNameYesOrNO.setText(mCampaignPollName);
        //setting the category in text view
        campaignPollView1.campaignPollHeaderCategoryYesOrNO.setText(mCampaignPollCategory);
        //setting the time in text view
        campaignPollView1.campaignPolltxtTimeYesOrNO.setText(mCampaignPollupdatedTime);
        //setting the like count
        campaignPollView1.txtLikeYesOrNO.setText(campaignlikeCount);
        //setting the comments in text view
        txtComments.setText(MApplication.getString(mCampaignPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //setting the question in text view
        campaignPollView1.campaignPollTxtQuestionYesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestion());
        //setting the option1 in text view
        campaignPollView1.campaignPolloption1YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1());
        //setting the option2 in text view
        campaignPollView1.campaignPolloption2YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2());
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(campaignlikeUser)) {
            campaignPollView1.likeUnlikeYesOrNO.setChecked(true);
        } else {
            campaignPollView1.likeUnlikeYesOrNO.setChecked(false);
        }
        //Participater answer details from the answer
        mCampaignOptionParticipated = pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        //Getting the values based on the size
        for (int i = 0; mCampaignOptionParticipated.size() > i; i++) {
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
                campaignPollView1.campaignPolltxtFirstOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                campaignPollView1.campaignPollprogressbarFirstOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView1.campaignPollGroupAnswerYesOrNO.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                campaignPollView1.campaignPollGroupAnswer1YesOrNO.setText(mPollCount);
                //Setting the progress bar to fill
                campaignPollView1.campaignPollprogressbarSecondOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                campaignPollView1.campaignPolltxtSecondOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.campaignPolltxtCountsYesOrNO.setOnClickListener(mCampaignPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.txtLikeYesOrNO.setOnClickListener(mCampaignPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.imgShareYesOrNO.setOnClickListener(mCampaignPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.campaignPollimageQuestion1YesOrNO.setOnClickListener(mCampaignPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.campaignPollimageQuestion2YesOrNO.setOnClickListener(mCampaignPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.campaignPollGroupAnswerYesOrNO.setOnClickListener(mCampaignPollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.campaignPollGroupAnswer1YesOrNO.setOnClickListener(mCampaignPollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.campaignPollsingleOptionYesOrNO.setOnClickListener(mCampaignPollSingleImageView);

//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
            //view gone
            campaignPollView1.campaignPollimageQuestion1YesOrNO.setVisibility(View.GONE);
            //view gone
            campaignPollView1.campaignPollimageQuestion2YesOrNO.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //user poll image1 is set into the image view
                campaignPollView1.campaignPollimageQuestion1YesOrNO.setImageURI(Uri.parse(mCampaignImage1));
                //user poll image2 is set into the image view
                campaignPollView1.campaignPollimageQuestion2YesOrNO.setImageURI(Uri.parse(mCampaignImage2));
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(mCampaignImage1) && ("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView1.campaignPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                campaignPollView1.campaignPollsingleOptionYesOrNO.setImageURI(Uri.parse(mCampaignImage1));
                //view gone
                campaignPollView1.campaignPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                campaignPollView1.campaignPollimageQuestion2YesOrNO.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(mCampaignImage1) && !("").equals(mCampaignImage2)) {
                //view visible
                campaignPollView1.campaignPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //view is set into the image view
                campaignPollView1.campaignPollsingleOptionYesOrNO.setImageURI(Uri.parse(mCampaignImage2));
                //view gone
                campaignPollView1.campaignPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                campaignPollView1.campaignPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            } else {
                //view gone
                campaignPollView1.campaignPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                campaignPollView1.campaignPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        campaignPollView1.likeUnlikeYesOrNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {    //If check box is checked
                    mlikes = 1;    //Setting the int value in variable
                    campaignPollView1.likeUnlikeYesOrNO.setChecked(true);     //set checked true
                    likesUnLikes(mCampaignPolllistposition); //Like unlike for the poll request
                } else {
                    mlikes = 0; //Setting the int value in variable
                    campaignPollView1.likeUnlikeYesOrNO.setChecked(false);    //set checked true
                    likesUnLikes(mCampaignPolllistposition); //Like unlike for the poll request
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
        if (clickedView.getId() == R.id.imagBackArrow) {
            this.finish();
        }
    }

    /**
     * This method is used to like an unlike the poll using server request
     *
     * @param clickPosition
     */
    private void likesUnLikes(final int clickPosition) {
        //Response from the server
        MApplication.materialdesignDialogStart(this);
        LikesAndUnLikeRestClient.getInstance().postCampaignPollLikes(new String("poll_likes"), new String(userId), new String(mCampaignPollId), new String(String.valueOf(mlikes))
                , new Callback<LikeUnLikeResposneModel>() {
                    @Override
                    public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                        //If the value from the response is 1 then the user has successfully liked the poll
                        if (("1").equals(likesUnlike.getResults())) {
                            //Changing the value in array list in a particular position
                            preferenceCampaignPollLikeUser.set(clickPosition, Integer.valueOf(1));
                            //Saving it in array
                            MApplication.saveArray(mCampaignPollReview, preferenceCampaignPollLikeUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
                        } else {
                            //Changing the value in array list in a particular position
                            preferenceCampaignPollLikeUser.set(clickPosition, Integer.valueOf(0));
                            //Saving it in array
                            MApplication.saveArray(mCampaignPollReview, preferenceCampaignPollLikeUser, Constants.CAMPAIGN_POLL_LIKES_USER_ARRAY, Constants.CAMPAIGN_POLL_LIKES_USER_SIZE);
                        }
                        //Toast message will display
                        Toast.makeText(mCampaignPollReview, likesUnlike.getMsg(),
                                Toast.LENGTH_SHORT).show();
                        //Changing the value in array list in a particular position
                        prefrenceCampaignPollLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                        //Saving the array in prefernce
                        MApplication.saveArray(mCampaignPollReview, prefrenceCampaignPollLikeCount, Constants.CAMPIGN_POLL_LIKES_COUNT_ARRAY, Constants.CAMPIGN_POLL_LIKES_COUNT_SIZE);
                        //Changing the value in textview
                        txtLike.setText(String.valueOf(prefrenceCampaignPollLikeCount.get(clickPosition)));
                        MApplication.materialdesignDialogStop();
                    }


                    @Override
                    public void failure(RetrofitError retrofitError) {
                        ////Error message popups when the user cannot able to coonect with the server
                        MApplication.errorMessage(retrofitError, mCampaignPollReview);
                        MApplication.materialdesignDialogStop();
                    }

                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Setting the comments in text view
        txtComments.setText(MApplication.getString(mCampaignPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder4 {
        //Scroll
        private ScrollView scrollViewYouTubeLayout;
        //Textview
        private TextView campaignPolltxtCountsYouTubeLayout;
        //Textview
        private TextView campaignPollGroupAnswer1YouTubeLayout;
        //Textvie
        private TextView campaignPollGroupAnswer2YouTubeLayout;
        //Textview
        private TextView campaignPollGroupAnswer3YouTubeLayout;
        //Textview
        private TextView campaignPollGroupAnswer4YouTubeLayout;
        //Textview
        private TextView campaignPolloption3YouTubeLayout;
        //Textview
        private TextView campaignPolloption4YouTubeLayout;
        //Progressbar
        private ProgressBar campaignPollprogressbarThirdOptionYouTubeLayout, campaignPollprogressbarFourthOptionYouTubeLayout;
        //Imageview
        private SimpleDraweeView campaignPollHeaderImageYouTubeLayout;
        //Imagewview
        private ImageView imgShareYouTubeLayout;
        //Textview
        private TextView campaignPolltxtFirstOptionCountYouTubeLayout;
        //Textview
        private TextView campaignPolltxtSecondOptionCountYouTubeLayout;
        //Textview
        private TextView campaignPolltxtThirdOptionCountYouTubeLayout;
        //Textview
        private TextView campaignPolltxtFourthOptionCountYouTubeLayout;
        //Relativelayout
        private RelativeLayout campaignPollrelativePrgressBar3YouTubeLayout;
        //Relativelayout
        private RelativeLayout campaignPollrelativePrgressBar4YouTubeLayout;
        //Textview
        private TextView campaignPollTxtQuestionYouTubeLayout;
        ///textview
        private TextView campaignPolloption1YouTubeLayoutYouTubeLayout;
        //Textview
        private TextView campaignPolloption2YouTubeLayout;
        //Imageview
        private SimpleDraweeView campaignPollimageQuestion1YouTubeLayout;
        //Imageview
        private SimpleDraweeView campaignPollimageQuestion2YouTubeLayout;
        //Imageview
        private SimpleDraweeView campaignPollsingleOptionYouTubeLayout;
        //Progressbar
        private ProgressBar campaignPollprogressbarFirstOptionYouTubeLayout;
        //Progressbar
        private ProgressBar campaignPollprogressbarSecondOptionYouTubeLayout;
        //Textview
        private TextView campaignPolltxtTimeYouTubeLayout;
        //Textview
        private TextView txtLikeYouTubeLayout;
        //Textview
        private TextView campaignPollHeaderCategoryYouTubeLayout;
        //Textview
        private TextView campaignPollHeaderNameYouTubeLayout;
        //Checkbox
        private CheckBox likeUnlikeYouTubeLayout;
        //Relativelayout
        private RelativeLayout relativieOption3;
        //Relativelayout
        private RelativeLayout relativieOption4;
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder1 {
        //Scrollview
        private ScrollView scrollViewYesOrNO;
        //Textview
        private TextView campaignPolltxtCountsYesOrNO;
        //Textview
        private TextView campaignPollGroupAnswerYesOrNO;
        //Textview
        private TextView campaignPollGroupAnswer1YesOrNO;
        //Imageview
        private SimpleDraweeView campaignPollHeaderImageYesOrNO;
        //Imageview
        private ImageView imgShareYesOrNO;
        //Textview
        private TextView campaignPolltxtFirstOptionCountYesOrNO;
        //Textview
        private TextView campaignPolltxtSecondOptionCountYesOrNO;
        //Textview
        private TextView campaignPollTxtQuestionYesOrNO;
        //Textview
        private TextView campaignPolloption1YesOrNO;
        //Textview
        private TextView campaignPolloption2YesOrNO;
        //Imageview
        private ImageView campaignPollimageQuestion1YesOrNO;
        //Imageview
        private ImageView campaignPollimageQuestion2YesOrNO;
        //Imageview
        private ImageView campaignPollsingleOptionYesOrNO;
        //Progressbar
        private ProgressBar campaignPollprogressbarFirstOptionYesOrNO;
        //Progressbar
        private ProgressBar campaignPollprogressbarSecondOptionYesOrNO;
        //Textview
        private TextView campaignPolltxtTimeYesOrNO;
        //Textview
        private TextView txtLikeYesOrNO;
        //Textview
        private TextView campaignPollHeaderCategoryYesOrNO;
        //Textview
        private TextView campaignPollHeaderNameYesOrNO;
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
        private TextView campaignPollPhotoComparisontxtCounts;
        //Textview
        private TextView campaignPollPhotoComparisonGroupAnswer1;
        //Textview
        private TextView campaignPollPhotoComparisonGroupAnswer2;
        //Textview
        private TextView campaignPollPhotoComparisonGroupAnswer3;
        //Textview
        private TextView campaignPollPhotoComparisonGroupAnswer4;
        //Relative layout
        private RelativeLayout campaignPollPhotoComparisonthirdOption;
        //Relative layout
        private RelativeLayout campaignPollPhotoComparisonfourthOption;
        //Progressbar
        private ProgressBar campaignPollPhotoComparisonprogressbarThirdOption, campaignPollPhotoComparisonprogressbarFourthOption;
        //Imageview
        private ImageView campaignPollPhotoComparisonimageAnswer1;
        //Imageview
        private ImageView campaignPollPhotoComparisonimageAnswer2;
        //Imageview
        private ImageView campaignPollPhotoComparisonimageAnswer3;
        //Imageview
        private ImageView campaignPollPhotoComparisonimageAnswer4;
        //Imageview
        private ImageView campaignPollPhotoComparisonHeaderImage;
        //Imageview
        private ImageView campaignPollPhotoComparisonimgShare;
        //Textview
        private TextView campaignPollPhotoComparisontxtFirstOptionCount;
        //Textview
        private TextView campaignPollPhotoComparisontxtSecondOptionCount;
        //Textview
        private TextView campaignPollPhotoComparisontxtThirdOptionCount;
        //Textview
        private TextView campaignPollPhotoComparisontxtFourthOptionCount;
        //Relative layout
        private RelativeLayout campaignPollPhotoComparisonrelativePrgressBar3;
        //Relative layout
        private RelativeLayout campaignPollPhotoComparisonrelativePrgressBar4;
        //Textview
        private TextView campaignPollPhotoComparisonTxtQuestion;
        //Imageview
        private ImageView campaignPollPhotoComparisonimageQuestion1;
        //Imageview
        private ImageView campaignPollPhotoComparisonimageQuestion2;
        //Imageview
        private ImageView campaignPollPhotoComparisonsingleOption;
        //Progressbar
        private ProgressBar campaignPollPhotoComparisonprogressbarFirstOption;
        //Progressbar
        private ProgressBar campaignPollPhotoComparisonprogressbarSecondOption;
        //Textview
        private TextView campaignPollPhotoComparisontxtTime;
        //Textview
        private TextView campaignPollPhotoComparisonTxtLike;
        //Textview
        private TextView campaignPollPhotoComparisonHeaderCategory;
        //Textview
        private TextView campaignPollPhotoComparisonHeaderName;
        //Checkbox
        private CheckBox campaignPollPhotoComparisonlikeUnlike;
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder2 {
        //Multiple ptions scrollview
        private ScrollView scrollViewMultipleOptions;
        //participate count
        private TextView campaignPolltxtCountsMultipleOptions;
        //Participate Count for answer1
        private TextView campaignPollGroupAnswer1MultipleOptions;
        //Participate Count for answer2
        private TextView campaignPollGroupAnswer2MultipleOptions;
        //Participate Count for answer3
        private TextView campaignPollGroupAnswer3MultipleOptions;
        //Participate Count for answer4
        private TextView campaignPollGroupAnswer4MultipleOptions;
        //option3
        private TextView campaignPolloption3MultipleOptions;
        //option4
        private TextView campaignPolloption4MultipleOptions;
        //Progressbar
        private ProgressBar campaignPollprogressbarThirdOptionMultipleOptions, campaignPollprogressbarFourthOptionMultipleOptions;
        //Imageview
        private ImageView campaignPollHeaderImageMultipleOptions;
        //Imageview
        private ImageView imgShareMultipleOptions;
        //Text view
        private TextView campaignPolltxtFirstOptionCountMultipleOptions;
        //Text view
        private TextView campaignPolltxtSecondOptionCountMultipleOptions;
        private TextView campaignPolltxtThirdOptionCountMultipleOptions;
        //Text view
        private TextView campaignPolltxtFourthOptionCountMultipleOptions;
        //Relative layout
        private RelativeLayout campaignPollrelativePrgressBar3MultipleOptions;
        //Relative layout
        private RelativeLayout campaignPollrelativePrgressBar4MultipleOptions;
        //Textview
        private TextView campaignPollTxtQuestionMultipleOptions;
        //Textview
        private TextView campaignPolloption1MultipleOptions;
        //Textview
        private TextView campaignPolloption2MultipleOptions;
        //Imageview
        private ImageView campaignPollimageQuestion1MultipleOptions;
        //Imageview
        private ImageView campaignPollimageQuestion2MultipleOptions;
        //Imageview
        private ImageView campaignPollsingleOptionMultipleOptions;
        //Progressabar
        private ProgressBar campaignPollprogressbarFirstOptionMultipleOptions;
        //Progressabar
        private ProgressBar campaignPollprogressbarSecondOptionMultipleOptions;
        //Textview
        private TextView campaignPolltxtTimeMultipleOptions;
        //Textview
        private TextView txtLikeMultipleOptions;
        //Textview
        private TextView campaignPollHeaderCategoryMultipleOptions;
        //Textview
        private TextView campaignPollHeaderNameMultipleOptions;
        //Checkbox
        private CheckBox likeUnlikeMultipleOptions;
    }
}