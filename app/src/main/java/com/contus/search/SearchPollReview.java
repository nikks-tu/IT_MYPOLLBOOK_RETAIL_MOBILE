package com.contus.search;

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
public class SearchPollReview extends Activity {
    //Userpoll review
    private SearchPollReview mSearchPollReview;
    //poll id
    private String searchPollId;
    //user poll image1
    private String searchPollImage1;
    //user poll image2
    private String searchPollImage2;
    //searchPollOptionParticipated
    private List<PollReviewResponseModel.Results.PollReview.ParticipatePoll> searchPollOptionParticipated;
    //searchPollName
    private String searchPollName;
    //searchPollCategory
    private String searchPollCategory;
    //searchPollLogo
    private String searchPollLogo;
    //user poll updated time
    private String searchPollupdatedTime;
    //position
    private int searchPolllistposition;
    //prefrencesearchPollLikeCount
    private ArrayList<Integer> prefrencesearchPollLikeCount;
    //arraySearchPollLikeCount
    private ArrayList<Integer> arraySearchPollLikeCount = new ArrayList<Integer>();
    //searchPollLikesUser
    private ArrayList<Integer> searchPollLikesUser = new ArrayList<Integer>();
    //searchPollcommentsCount
    private ArrayList<Integer> searchPollcommentsCount = new ArrayList<Integer>();
    //prefrencesearchPollCommentsCount
    private ArrayList<Integer> prefrencesearchPollCommentsCount;
    //preferencesearchPollLikeUser
    private ArrayList<Integer> preferencesearchPollLikeUser;
    //searchPolllikeCount
    private String searchPolllikeCount;
    //commentsCount
    private String commentsCount;
    //searchPolllikeUser
    private String searchPolllikeUser;
    //userId
    private String userId;
    //mlikes
    private int mlikes;
    //Adview
    private AdView mAdView;
    //participate count
    private String userPollparticipateCount;
    //searchPollAnswer1
    private String searchPollAnswer1;
    //searchPollAnswer2
    private String searchPollAnswer2;
    //searchPollAnswer3
    private String searchPollAnswer3;
    //searchPollAnswer4
    private String searchPollAnswer4;
    //View holder
    private ViewHolder4 searchPollView4;
    //txtcomments
    private TextView txtComments;
    //view holder
    private ViewHolder1 searchPollView1;
    //view holder
    private ViewHolder3 searchPollView3;
    //view holder
    private ViewHolder2 searchPollView2;
    //like
    private TextView txtLike;
    //image
    private String image;
    //search polll type
    private String searchPollType;
    //poll type
    private String choosenSearchPolltype;
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollParticipantCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mSearchPollReview, Participation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, searchPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollLikeCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent details = new Intent(mSearchPollReview, PollLikes.class);
            //Passing the value from one activity to another
            details.putExtra(Constants.POLL_ID, searchPollId);
            //starting the activity
            mSearchPollReview.startActivity(details);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollSharePoll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            String base64CampaignId = MApplication.convertByteCode(searchPollId);
            //share url
            String shareUrl = Constants.SHARE_POLL_BASE_URL.concat(base64CampaignId);
            //sharing in gmail
            MApplication.shareGmail(mSearchPollReview, shareUrl, MApplication.getString(mSearchPollReview, Constants.PROFILE_USER_NAME));
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollQuestionImage1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mSearchPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, searchPollImage1);
            //starting the activity
            mSearchPollReview.startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollQuestionImage2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(mSearchPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, searchPollImage2);
            //starting the activity
            mSearchPollReview.startActivity(a);
        }
    };
    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchpollAnswer1Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(SearchPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "1");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, searchPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollpollAnswer2Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(SearchPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "2");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, searchPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollpollAnswer3Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(SearchPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "3");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, searchPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollAnswer4Participants = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Moving from one activity to another activity
            Intent a = new Intent(SearchPollReview.this, PollReviewQuestionParticipation.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ANSWER_OPTION, "4");
            //Passing the value from one activity to another
            a.putExtra(Constants.POLL_ID, searchPollId);
            //starting the activity
            startActivity(a);
        }
    };

    /**
     * OnClick listner for question like share check box
     */
    private View.OnClickListener mSearchPollsingleImageView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (("").equals(searchPollImage2)) {
                image = searchPollImage1;
            } else {
                image = searchPollImage2;
            }
            //Moving from one activity to another activity
            Intent a = new Intent(mSearchPollReview, FullImageView.class);
            //Passing the value from one activity to another
            a.putExtra(Constants.QUESTION1, image);
            //starting the activity
            mSearchPollReview.startActivity(a);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the user poll type from another activity
        searchPollType = getIntent().getExtras().getString(Constants.POLL_TYPE);
        if(("1").equals(searchPollType)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_firstview);
        }else if(("2").equals(searchPollType)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_secondview);
        }else if(("3").equals(searchPollType)){
            setContentView(R.layout.fragment_publicpoll_singlepoll_thirdview);
        }else if(("4").equals(searchPollType)){
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
        mSearchPollReview = this;
        //Retrieves a map of extended data from the intent.
        searchPollId = getIntent().getExtras().getString(Constants.POLL_ID);
        //Retrieves a map of extended data from the intent.
        choosenSearchPolltype = getIntent().getExtras().getString(Constants.TYPE);
        //Retrieves a map of extended data from the intent.
        searchPolllistposition =getIntent().getExtras().getInt(Constants.ARRAY_POSITION);
        //user id from the prefernce
        userId = MApplication.getString(mSearchPollReview, Constants.USER_ID);
        //participate count
        userPollparticipateCount =getIntent().getExtras().getString(Constants.PARTICIPATE_COUNT);
        //name
        searchPollName = MApplication.getString(mSearchPollReview, Constants.CAMPAIGN_NAME);
        //category
        searchPollCategory = MApplication.getString(mSearchPollReview, Constants.CAMPAIGN_CATEGORY);
        //logo
        searchPollLogo = MApplication.getString(mSearchPollReview, Constants.CAMPAIGN_LOGO);
        //date updated
        searchPollupdatedTime = MApplication.getString(mSearchPollReview, Constants.DATE_UPDATED);
        //User poll like count saved in the prefernce is loaded into the array list
        prefrencesearchPollLikeCount = MApplication.loadArray(mSearchPollReview, arraySearchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_SIZE);
        //User poll comments saved in the prefernce is loaded into the array list
        prefrencesearchPollCommentsCount = MApplication.loadArray(mSearchPollReview, searchPollcommentsCount, Constants.SEARCH_POLL_COMMENTS_COUNT, Constants.SEARCH_POLL_COMMENTS_SIZE);
        //User poll like user saved in the prefernce is loaded into the array list
        preferencesearchPollLikeUser = MApplication.loadArray(mSearchPollReview, searchPollLikesUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
        //Getting the value from the particular position in array list
        searchPolllikeCount = String.valueOf(prefrencesearchPollLikeCount.get(searchPolllistposition));
        //Getting the value from the particular position in array list
        commentsCount = String.valueOf(prefrencesearchPollCommentsCount.get(searchPolllistposition));
        //Getting the value from the particular position in array list
        searchPolllikeUser = String.valueOf(preferencesearchPollLikeUser.get(searchPolllistposition));
        //Request for the poll survey details
        pollReviewRequest();
        //Setting the comments count in prefernce
        MApplication.setString(mSearchPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW, commentsCount);
        //Initailizing the views
        txtComments = (TextView)findViewById(R.id.txtCommentsCounts);
        mAdView = (AdView) findViewById(R.id.adView);
        //Google adview
        MApplication.googleAd(mAdView);
        txtLike = (TextView)findViewById(R.id.txtLike2);
        //Register a callback to be invoked when this view is clicked.
        txtComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    An intent is an abstract description of an operation to be performed. It can be used with startActivity to
                // launch an Activity, broadcastIntent to send it to any interested BroadcastReceiver components
                Intent details = new Intent(mSearchPollReview, SearchPollComments.class);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_ID, searchPollId);
                //Add extended data to the intent.
                details.putExtra(Constants.POLL_TYPE, searchPollType);
                //Add extended data to the intent.
                details.putExtra(Constants.COMMENTS_COUNT_POSITION, searchPolllistposition);
                //Launch a new activity.
                mSearchPollReview.startActivity(details);
            }
        });
    }


    private void pollReviewRequest() {
        /**If internet connection is available**/
        if (MApplication.isNetConnected(mSearchPollReview)) {
            PollReviewsRestClient.getInstance().pollReview(new String("poll_review_polls"), new String(searchPollId), new String(choosenSearchPolltype), new Callback<PollReviewResponseModel>() {
                @Override
                public void success(PollReviewResponseModel pollReviewResponseModel, Response response) {
                    //user poll type
                    searchPollType = pollReviewResponseModel.getResults().getPollreview().get(0).getPollType();
                    if(("1").equals(searchPollType)){
                        firstReview(pollReviewResponseModel);
                    }else if(("2").equals(searchPollType)){
                        secondReview(pollReviewResponseModel);
                    }else if(("3").equals(searchPollType)){
                        thirdReview(pollReviewResponseModel);
                    }else if(("4").equals(searchPollType)){
                        fourthReview(pollReviewResponseModel);
                    }
                }
                @Override
                public void failure(RetrofitError retrofitError) {
                    //Error message popups when the user cannot able to coonect with the server
                    MApplication.errorMessage(retrofitError, mSearchPollReview);
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
        searchPollView4 =new ViewHolder4();
        //Initializing the views.
        searchPollView4.scrollViewYouTubeLayout =(ScrollView)findViewById(R.id.scroll);
        searchPollView4.searchPollTxtQuestionYouTubeLayout = (TextView) findViewById(R.id.txtStatus);
        searchPollView4.searchPollimageQuestion1YouTubeLayout = (SimpleDraweeView)findViewById(R.id.choose);
        searchPollView4.searchPollimageQuestion2YouTubeLayout = (SimpleDraweeView)findViewById(R.id.ChooseAdditional);
        searchPollView4.searchPollsingleOptionYouTubeLayout = (SimpleDraweeView)findViewById(R.id.singleOption);
        searchPollView4.searchPollHeaderImageYouTubeLayout = (ImageView)findViewById(R.id.imgProfile);
        searchPollView4.searchPollltxtTimeYouTubeLayout = (TextView)findViewById(R.id.txtTime);
        searchPollView4.searchPollHeaderNameYouTubeLayout = (TextView)findViewById(R.id.txtName);
        searchPollView4.searchPollHeaderCategoryYouTubeLayout = (TextView)findViewById(R.id.txtCategory);
        searchPollView4.likeUnlikeYouTubeLayout = (CheckBox)findViewById(R.id.unLikeDislike);
        searchPollView4.searchPolltxtFirstOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtFirstOptionCount);
        searchPollView4.searchPolltxtSecondOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtSecondOptionCount);
        searchPollView4.searchPolltxtThirdOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtThirdOptionCount);
        searchPollView4.searchPolltxtFourthOptionCountYouTubeLayout =(TextView)findViewById(R.id.txtFourthOptionCount);
        searchPollView4.searchPollprogressbarFirstOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarFirstOption);
        searchPollView4.searchPollprogressbarSecondOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarSecondOption);
        searchPollView4.searchPollprogressbarThirdOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarThirdOption);
        searchPollView4.searchPollprogressbarFourthOptionYouTubeLayout = (ProgressBar)findViewById(R.id.progressbarFourthOption);
        searchPollView4.searchPollrelativePrgressBar3YouTubeLayout =(RelativeLayout)findViewById(R.id.relativeProgressbar3);
        searchPollView4.searchPollrelativePrgressBar4YouTubeLayout =(RelativeLayout)findViewById(R.id.relativeProgressbar4);
        searchPollView4.searchPolltxtCountsYouTubeLayout = (TextView) findViewById(R.id.txtParticcipation);
        searchPollView4.imgShareYouTubeLayout =(ImageView)findViewById(R.id.imgShare);
        searchPollView4.txtLikeYouTubeLayout = (TextView)findViewById(R.id.txtLike2);
        searchPollView4.searchPolloption1YouTubeLayoutYouTubeLayout = (TextView)findViewById(R.id.option1);
        searchPollView4.searchPolloption2YouTubeLayout = (TextView) findViewById(R.id.option2);
        searchPollView4.searchPolloption3YouTubeLayout = (TextView)findViewById(R.id.option3);
        searchPollView4.searchPolloption4YouTubeLayout = (TextView)findViewById(R.id.option4);
        searchPollView4.searchPollGroupAnswer1YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer1);
        searchPollView4.searchPollGroupAnswer2YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer2);
        searchPollView4.searchPollGroupAnswer3YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer3);
        searchPollView4.searchPollGroupAnswer4YouTubeLayout =(TextView)findViewById(R.id.pollGroupAnswer4);
        //Setting the visiblity view.
        searchPollView4.scrollViewYouTubeLayout.setVisibility(View.VISIBLE);
        //Getting the poll question image if available
        searchPollImage1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //Getting the poll question image if available
        searchPollImage2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        //Setting the participate count in text view
        searchPollView4.searchPolltxtCountsYouTubeLayout.setText(userPollparticipateCount);
        Utils.loadImageWithGlideProfileRounderCorner(this,searchPollLogo.replaceAll(" ", "%20"),searchPollView4.searchPollHeaderImageYouTubeLayout,R.drawable.placeholder_image);
        //user poll name
        searchPollView4.searchPollHeaderNameYouTubeLayout.setText(searchPollName);
        //Category is set into the text view
        searchPollView4.searchPollHeaderCategoryYouTubeLayout.setText(searchPollCategory);
        //User poll updated time in text view
        searchPollView4.searchPollltxtTimeYouTubeLayout.setText(searchPollupdatedTime);
        //Setting the like in text view
        searchPollView4.txtLikeYouTubeLayout.setText(searchPolllikeCount);
        //Setting the question in text view
        searchPollView4.searchPollTxtQuestionYouTubeLayout.setText(pollResponseModelView4.getResults().getPollreview().get(0).getPollQuestion());
        //Setting the comment count in text view
        txtComments.setText(MApplication.getString(mSearchPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(searchPolllikeUser)) {
            searchPollView4.likeUnlikeYouTubeLayout.setChecked(true);
        } else {
            searchPollView4.likeUnlikeYouTubeLayout.setChecked(false);
        }
        //poll answer option from the response
        searchPollAnswer1 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        searchPollAnswer2 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        searchPollAnswer3 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option from the response
        searchPollAnswer4 = pollResponseModelView4.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        searchPollOptionParticipated = pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; searchPollOptionParticipated.size() > i; i++) {
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
                searchPollView4.searchPolltxtFirstOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView4.searchPollprogressbarFirstOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView4.searchPollGroupAnswer1YouTubeLayout.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView4.searchPolltxtSecondOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView4.searchPollprogressbarSecondOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView4.searchPollGroupAnswer2YouTubeLayout.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView4.searchPolltxtThirdOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView4.searchPollprogressbarThirdOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView4.searchPollGroupAnswer3YouTubeLayout.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView4.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView4.searchPolltxtFourthOptionCountYouTubeLayout.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView4.searchPollprogressbarFourthOptionYouTubeLayout.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView4.searchPollGroupAnswer4YouTubeLayout.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
            //view gone
            searchPollView4.searchPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
            //view gone
            searchPollView4.searchPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //user poll image1 is set into the image view
                searchPollView4.searchPollimageQuestion1YouTubeLayout.setImageURI(Uri.parse(searchPollImage1.replaceAll(" ", "%20")));
                //user poll image2 is set into the image view
                searchPollView4.searchPollimageQuestion2YouTubeLayout.setImageURI(Uri.parse(searchPollImage2.replaceAll(" ", "%20")));
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
                //view visible
                searchPollView4.searchPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                searchPollView4.searchPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(searchPollImage1.replaceAll(" ", "%20")));
                //view gone
                searchPollView4.searchPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                searchPollView4.searchPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //view visible
                searchPollView4.searchPollsingleOptionYouTubeLayout.setVisibility(View.VISIBLE);
                //view is set into the image view
                searchPollView4.searchPollsingleOptionYouTubeLayout.setImageURI(Uri.parse(searchPollImage2.replaceAll(" ", "%20")));
                //view gone
                searchPollView4.searchPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                searchPollView4.searchPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            } else {
                //view gone
                searchPollView4.searchPollimageQuestion1YouTubeLayout.setVisibility(View.GONE);
                //view gone
                searchPollView4.searchPollimageQuestion2YouTubeLayout.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer1)) {
            searchPollView4.searchPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(searchPollAnswer1)) {
            searchPollView4.searchPolloption1YouTubeLayoutYouTubeLayout.setVisibility(View.VISIBLE);
            searchPollView4.searchPolloption1YouTubeLayoutYouTubeLayout.setText(searchPollAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer2)) {
            searchPollView4.searchPolloption2YouTubeLayout.setVisibility(View.GONE);
        } else if (!("").equals(searchPollAnswer2)) {
            searchPollView4.searchPolloption2YouTubeLayout.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            searchPollView4.searchPolloption2YouTubeLayout.setText(searchPollAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer3)) {
            //view gone
            searchPollView4.searchPolloption3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            searchPollView4.searchPolloption4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            searchPollView4.searchPollrelativePrgressBar3YouTubeLayout.setVisibility(View.GONE);
            //view gone
            searchPollView4.searchPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
            //view gone
            searchPollView4.searchPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.GONE);
            //view gone
            searchPollView4.searchPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
        } else if (!("").equals(searchPollAnswer3)) {
            //visible
            searchPollView4.searchPolloption3YouTubeLayout.setVisibility(View.VISIBLE);
            //setting the data in text view
            searchPollView4.searchPolloption3YouTubeLayout.setText(searchPollAnswer3);
            //visible
            searchPollView4.searchPollrelativePrgressBar3YouTubeLayout.setVisibility(View.VISIBLE);
            //visible
            searchPollView4.searchPolltxtThirdOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(searchPollAnswer4)) {
                //view gone
                searchPollView4.searchPolloption4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                searchPollView4.searchPollrelativePrgressBar4YouTubeLayout.setVisibility(View.GONE);
                //view gone
                searchPollView4.searchPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.GONE);
            } else if (!("").equals(searchPollAnswer4)) {
                //view visible
                searchPollView4.searchPollrelativePrgressBar4YouTubeLayout.setVisibility(View.VISIBLE);
                //view visible
                searchPollView4.searchPolloption4YouTubeLayout.setVisibility(View.VISIBLE);
                //Setting the the text view
                searchPollView4.searchPolloption4YouTubeLayout.setText(searchPollAnswer4);
                //view visible
                searchPollView4.searchPolltxtFourthOptionCountYouTubeLayout.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPolltxtCountsYouTubeLayout.setOnClickListener(mSearchPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.txtLikeYouTubeLayout.setOnClickListener(mSearchPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.imgShareYouTubeLayout.setOnClickListener(mSearchPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPollimageQuestion1YouTubeLayout.setOnClickListener(mSearchPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPollimageQuestion1YouTubeLayout.setOnClickListener(mSearchPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPollGroupAnswer1YouTubeLayout.setOnClickListener(mSearchpollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPollGroupAnswer2YouTubeLayout.setOnClickListener(mSearchPollpollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPollGroupAnswer3YouTubeLayout.setOnClickListener(mSearchPollpollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPollGroupAnswer4YouTubeLayout.setOnClickListener(mSearchPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.searchPollsingleOptionYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting the string in youtube url
                MApplication.setString(mSearchPollReview, Constants.YOUTUBE_URL, pollResponseModelView4.getResults().getPollreview().get(0).getYoutubeUrl());
                //If net is connected
                if (MApplication.isNetConnected((Activity) mSearchPollReview)) {
                    //An intent is an abstract description of an operation to be performed.
                    // It can be used with startActivity to launch an Activity
                    Intent a = new Intent(mSearchPollReview, VideoLandscapeActivity.class);
                    //Start the activity
                    mSearchPollReview.startActivity(a);
                } else {
                    //Toast message will display
                    Toast.makeText(mSearchPollReview, mSearchPollReview.getResources().getString(R.string.check_internet_connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView4.likeUnlikeYouTubeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If check box is checked
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1; //Setting the int value in variable
                    //set checked true
                    searchPollView4.likeUnlikeYouTubeLayout.setChecked(true);
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
                } else {
                    mlikes = 0;     //Setting the int value in variable
                    //set checked true
                    searchPollView4.likeUnlikeYouTubeLayout.setChecked(false);
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
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
        searchPollView3 =new ViewHolder3();
        //Initializing the views.
        searchPollView3.scrollView = (ScrollView) findViewById(R.id.scroll);
        searchPollView3.searchPollPhotoComparisonTxtQuestion = (TextView) findViewById(R.id.txtStatus);
        searchPollView3.searchPollPhotoComparisonimageQuestion1 = (ImageView) findViewById(R.id.choose);
        searchPollView3.searchPollPhotoComparisonimageQuestion2 = (ImageView) findViewById(R.id.ChooseAdditional);
        searchPollView3.searchPollPhotoComparisonsingleOption = (ImageView) findViewById(R.id.singleOption);
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        searchPollView3.searchPollPhotoComparisonlikeUnlike = (CheckBox) findViewById(R.id.unLikeDislike);
        searchPollView3.searchPollPhotoComparisonTxtLike = (TextView) findViewById(R.id.txtLike2);
        searchPollImage1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        searchPollImage2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        searchPollView3.searchPollPhotoComparisonHeaderImage = (ImageView) findViewById(R.id.imgProfile);
        searchPollView3.searchPollPhotoComparisontxtTime = (TextView) findViewById(R.id.txtTime);
        searchPollView3.searchPollPhotoComparisonHeaderName = (TextView) findViewById(R.id.txtName);
        searchPollView3.searchPollPhotoComparisonHeaderCategory = (TextView) findViewById(R.id.txtCategory);
        Utils.loadImageWithGlideProfileRounderCorner(this,searchPollLogo.replaceAll(" ", "%20"),searchPollView3.searchPollPhotoComparisonHeaderImage,R.drawable.placeholder_image);
        searchPollView3.searchPollPhotoComparisonHeaderName.setText(searchPollName);
        searchPollView3.searchPollPhotoComparisonHeaderCategory.setText(searchPollCategory);
        searchPollView3.searchPollPhotoComparisontxtTime.setText(searchPollupdatedTime);
        searchPollView3.searchPollPhotoComparisontxtCounts = (TextView) findViewById(R.id.txtParticcipation);
        searchPollView3.searchPollPhotoComparisontxtCounts.setText(userPollparticipateCount);
        searchPollView3.searchPollPhotoComparisonimageAnswer1 = (ImageView) findViewById(R.id.answer1);
        searchPollView3.searchPollPhotoComparisonimageAnswer2 = (ImageView) findViewById(R.id.answer2);
        searchPollView3.searchPollPhotoComparisonimageAnswer3 = (ImageView) findViewById(R.id.answer3);
        searchPollView3.searchPollPhotoComparisonimageAnswer4 = (ImageView) findViewById(R.id.answer4);
        searchPollView3.searchPollPhotoComparisonrelativePrgressBar3 = (RelativeLayout) findViewById(R.id.relativeProgressbar3);
        searchPollView3.searchPollPhotoComparisonrelativePrgressBar4 = (RelativeLayout) findViewById(R.id.relativeProgressbar4);
        searchPollView3.searchPollPhotoComparisonprogressbarFirstOption = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        searchPollView3.searchPollPhotoComparisonprogressbarSecondOption = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        searchPollView3.searchPollPhotoComparisonprogressbarThirdOption = (ProgressBar) findViewById(R.id.progressbarThirdOption);
        searchPollView3.searchPollPhotoComparisonprogressbarFourthOption = (ProgressBar) findViewById(R.id.progressbarFourthOption);
        searchPollView3.searchPollPhotoComparisonthirdOption = (RelativeLayout) findViewById(R.id.ThirdOptionOption);
        searchPollView3.searchPollPhotoComparisonfourthOption = (RelativeLayout) findViewById(R.id.FourthOptionOption);
        searchPollView3.searchPollPhotoComparisontxtFirstOptionCount = (TextView) findViewById(R.id.txtFirstOptionCount);
        searchPollView3.searchPollPhotoComparisontxtSecondOptionCount = (TextView) findViewById(R.id.txtSecondOptionCount);
        searchPollView3.searchPollPhotoComparisontxtThirdOptionCount = (TextView) findViewById(R.id.txtThirdOptionCount);
        searchPollView3.searchPollPhotoComparisontxtFourthOptionCount = (TextView) findViewById(R.id.txtFourthOptionCount);
        searchPollView3.searchPollPhotoComparisonGroupAnswer1 = (TextView) findViewById(R.id.pollGroupAnswer1);
        searchPollView3.searchPollPhotoComparisonGroupAnswer2 = (TextView) findViewById(R.id.pollGroupAnswer2);
        searchPollView3.searchPollPhotoComparisonGroupAnswer3 = (TextView) findViewById(R.id.pollGroupAnswer3);
        searchPollView3.searchPollPhotoComparisonGroupAnswer4 = (TextView) findViewById(R.id.pollGroupAnswer4);
        searchPollView3.searchPollPhotoComparisonimgShare = (ImageView) findViewById(R.id.imgShare);
        //view visible
        searchPollView3.scrollView.setVisibility(View.VISIBLE);
        //Setting the poll like count
        searchPollView3.searchPollPhotoComparisonTxtLike.setText(searchPolllikeCount);
        //Setting the text in text view
        txtComments.setText(MApplication.getString(mSearchPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(searchPolllikeUser)) {
            searchPollView3.searchPollPhotoComparisonlikeUnlike.setChecked(true);
        } else {
            searchPollView3.searchPollPhotoComparisonlikeUnlike.setChecked(false);
        }
        //Setting the question in text view
        searchPollView3.searchPollPhotoComparisonTxtQuestion.setText(pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        searchPollAnswer1 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        searchPollAnswer2 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        searchPollAnswer3 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option from the response
        searchPollAnswer4 = pollReviewResponseModelView3.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        searchPollOptionParticipated = pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll();

        //Getting the values based on the size
        for (int i = 0; searchPollOptionParticipated.size() > i; i++) {
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
                searchPollView3.searchPollPhotoComparisontxtFirstOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView3.searchPollPhotoComparisonGroupAnswer1.setText(mPollCount);
                //Setting the poll count in text view
                searchPollView3.searchPollPhotoComparisonprogressbarFirstOption.setProgress(mPercentage);
            } else if (("2").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView3.searchPollPhotoComparisontxtSecondOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView3.searchPollPhotoComparisonprogressbarSecondOption.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView3.searchPollPhotoComparisonGroupAnswer2.setText(mPollCount);
            } else if (("3").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView3.searchPollPhotoComparisontxtThirdOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView3.searchPollPhotoComparisonprogressbarThirdOption.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView3.searchPollPhotoComparisonGroupAnswer3.setText(mPollCount);
            } else if (("4").equals(pollReviewResponseModelView3.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView3.searchPollPhotoComparisontxtFourthOptionCount.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView3.searchPollPhotoComparisonprogressbarFourthOption.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView3.searchPollPhotoComparisonGroupAnswer4.setText(mPollCount);
            }
        }
        //If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
            //view gone
            searchPollView3.searchPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage1, searchPollView3.searchPollPhotoComparisonimageQuestion1,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage2, searchPollView3.searchPollPhotoComparisonimageQuestion2,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
                //view visible
                searchPollView3.searchPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage2, searchPollView3.searchPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                searchPollView3.searchPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                searchPollView3.searchPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //view visible
                searchPollView3.searchPollPhotoComparisonsingleOption.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage2, searchPollView3.searchPollPhotoComparisonsingleOption,R.drawable.placeholder_image);
                //view gone
                searchPollView3.searchPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                searchPollView3.searchPollPhotoComparisonimageQuestion2.setVisibility(View.GONE);
            } else {
                //view gone
                searchPollView3.searchPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
                //view gone
                searchPollView3.searchPollPhotoComparisonimageQuestion1.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer1)) {
            searchPollView3.searchPollPhotoComparisonimageAnswer1.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(searchPollAnswer1)) {
            searchPollView3.searchPollPhotoComparisonimageAnswer1.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlideRounderCorner(this,searchPollAnswer1, searchPollView3.searchPollPhotoComparisonimageAnswer1,R.drawable.placeholder_image);

        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer2)) {
            searchPollView3.searchPollPhotoComparisonimageAnswer2.setVisibility(View.GONE);
        } else if (!("").equals(searchPollAnswer2)) {
            searchPollView3.searchPollPhotoComparisonimageAnswer2.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            Utils.loadImageWithGlideRounderCorner(this,searchPollAnswer2, searchPollView3.searchPollPhotoComparisonimageAnswer2,R.drawable.placeholder_image);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer3)) {
            //view gone
            searchPollView3.searchPollPhotoComparisonimageAnswer3.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisonthirdOption.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisonfourthOption.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisonrelativePrgressBar3.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisonfourthOption.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisontxtThirdOptionCount.setVisibility(View.GONE);
            //view gone
            searchPollView3.searchPollPhotoComparisontxtFourthOptionCount.setVisibility(View.GONE);
        } else if (!("").equals(searchPollAnswer3)) {
            //visible
            searchPollView3.searchPollPhotoComparisonimageAnswer3.setVisibility(View.VISIBLE);
            //setting the data in text view
            Utils.loadImageWithGlideRounderCorner(this,searchPollAnswer3, searchPollView3.searchPollPhotoComparisonimageAnswer2,R.drawable.placeholder_image);
            //view gone
            searchPollView3.searchPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(searchPollAnswer4)) {
                //view visible
                searchPollView3.searchPollPhotoComparisonfourthOption.setVisibility(View.GONE);
                //view gone
                searchPollView3.searchPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
                //view gone
                searchPollView3.searchPollPhotoComparisonimageAnswer4.setVisibility(View.GONE);
                //view gone
                searchPollView3.searchPollPhotoComparisonrelativePrgressBar4.setVisibility(View.GONE);
            } else if (!("").equals(searchPollAnswer4)) {
                //view visible
                searchPollView3.searchPollPhotoComparisonfourthOption.setVisibility(View.VISIBLE);
                //view visible
                searchPollView3.searchPollPhotoComparisonimageAnswer4.setVisibility(View.VISIBLE);
                //Setting the the text view
                Utils.loadImageWithGlideRounderCorner(this,searchPollAnswer4, searchPollView3.searchPollPhotoComparisonimageAnswer4,R.drawable.placeholder_image);
                //view visible
                searchPollView3.searchPollPhotoComparisonrelativePrgressBar4.setVisibility(View.VISIBLE);
                //view gone
                searchPollView3.searchPollPhotoComparisontxtFourthOptionCount.setVisibility(View.GONE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonlikeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If check box is checked
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;   //Setting the int value in variable
                    searchPollView3.searchPollPhotoComparisonlikeUnlike.setChecked(true);  //set checked true
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
                } else {
                    mlikes = 0; //Setting the int value in variable
                    searchPollView3.searchPollPhotoComparisonlikeUnlike.setChecked(false); //set checked true
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisontxtCounts.setOnClickListener(mSearchPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonTxtLike.setOnClickListener(mSearchPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonimgShare.setOnClickListener(mSearchPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonimageQuestion1.setOnClickListener(mSearchPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonimageQuestion2.setOnClickListener(mSearchPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonGroupAnswer1.setOnClickListener(mSearchpollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonGroupAnswer2.setOnClickListener(mSearchPollpollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonGroupAnswer3.setOnClickListener(mSearchPollpollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonGroupAnswer4.setOnClickListener(mSearchPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonsingleOption.setOnClickListener(mSearchPollsingleImageView);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonimageAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(mSearchPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, searchPollAnswer1);
                //Starting the activity
                mSearchPollReview.startActivity(a);
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonimageAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//An intent is an abstract description of an operation to be performed.
// It can be used with startActivity to launch an Activity
                Intent a = new Intent(mSearchPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, searchPollAnswer2);
                //Starting the activity
                mSearchPollReview.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonimageAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(mSearchPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, searchPollAnswer3);
                //Starting the activity
                mSearchPollReview.startActivity(a);
            }
        });
//Interface definition for a callback to be invoked when a view is clicked.
        searchPollView3.searchPollPhotoComparisonimageAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent a = new Intent(mSearchPollReview, FullImageView.class);
                //Add extended data to the intent.
                a.putExtra(Constants.QUESTION1, searchPollAnswer4);
                //Starting the activity
                mSearchPollReview.startActivity(a);
            }
        });
    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 2 this view is called.
     * @param pollResponseModelView2
     */
    private void secondReview(PollReviewResponseModel pollResponseModelView2) {
        //view holder
        searchPollView2 =new ViewHolder2();
        //Initializing the views.
        searchPollView2.scrollViewMultipleOptions =(ScrollView)findViewById(R.id.scroll);
        searchPollView2.searchPollTxtQuestionMultipleOptions = (TextView) findViewById(R.id.txtStatus);
        searchPollView2.searchPollimageQuestion1MultipleOptions = (ImageView) findViewById(R.id.choose);
        searchPollView2.searchPollimageQuestion2MultipleOptions = (ImageView)findViewById(R.id.ChooseAdditional);
        searchPollView2.searchPollsingleOptionMultipleOptions = (ImageView)findViewById(R.id.singleOption);
        searchPollView2.searchPollHeaderImageMultipleOptions = (ImageView)findViewById(R.id.imgProfile);
        searchPollView2.searchPolltxtTimeMultipleOptions = (TextView)findViewById(R.id.txtTime);
        searchPollView2.searchPollHeaderNameMultipleOptions = (TextView)findViewById(R.id.txtName);
        searchPollView2.searchPollHeaderCategoryMultipleOptions = (TextView)findViewById(R.id.txtCategory);
        txtComments = (TextView)findViewById(R.id.txtCommentsCounts);
        searchPollView2.likeUnlikeMultipleOptions = (CheckBox)findViewById(R.id.unLikeDislike);
        searchPollView2.searchPollGroupAnswer1MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer1);
        searchPollView2.searchPollGroupAnswer2MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer2);
        searchPollView2.searchPollGroupAnswer3MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer3);
        searchPollView2.searchPollGroupAnswer4MultipleOptions =(TextView)findViewById(R.id.pollGroupAnswer4);
        searchPollView2.imgShareMultipleOptions =(ImageView)findViewById(R.id.imgShare);
        searchPollView2.txtLikeMultipleOptions = (TextView)findViewById(R.id.txtLike2);
        searchPollView2.searchPolloption1MultipleOptions = (TextView)findViewById(R.id.option1);
        searchPollView2.searchPolloption2MultipleOptions = (TextView) findViewById(R.id.option2);
        searchPollView2.searchPolloption3MultipleOptions = (TextView)findViewById(R.id.option3);
        searchPollView2.searchPolloption4MultipleOptions = (TextView)findViewById(R.id.option4);
        searchPollView2.searchPolltxtFirstOptionCountMultipleOptions =(TextView)findViewById(R.id.txtFirstOptionCount);
        searchPollView2.searchPolltxtSecondOptionCountMultipleOptions =(TextView)findViewById(R.id.txtSecondOptionCount);
        searchPollView2.searchPolltxtThirdOptionCountMultipleOptions =(TextView)findViewById(R.id.txtThirdOptionCount);
        searchPollView2.searchPolltxtFourthOptionCountMultipleOptions =(TextView)findViewById(R.id.txtFourthOptionCount);
        searchPollView2.searchPollprogressbarFirstOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarFirstOption);
        searchPollView2.searchPollprogressbarSecondOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarSecondOption);
        searchPollView2.searchPollprogressbarThirdOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarThirdOption);
        searchPollView2.searchPollprogressbarFourthOptionMultipleOptions = (ProgressBar)findViewById(R.id.progressbarFourthOption);
        searchPollView2.searchPollrelativePrgressBar3MultipleOptions =(RelativeLayout)findViewById(R.id.relativeProgressbar3);
        searchPollView2.searchPollrelativePrgressBar4MultipleOptions =(RelativeLayout)findViewById(R.id.relativeProgressbar4);
        searchPollView2.searchPolltxtCountsMultipleOptions = (TextView) findViewById(R.id.txtParticcipation);
        //view visible
        searchPollView2.scrollViewMultipleOptions.setVisibility(View.VISIBLE);
        //setting the participate count
        searchPollView2.searchPolltxtCountsMultipleOptions.setText(userPollparticipateCount);
        //question image
        searchPollImage1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //question image 2 from the response
        searchPollImage2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        Utils.loadImageWithGlideProfileRounderCorner(this,searchPollLogo.replaceAll(" ", "%20"), searchPollView2.searchPollHeaderImageMultipleOptions,R.drawable.placeholder_image);
        //setting the name in text view
        searchPollView2.searchPollHeaderNameMultipleOptions.setText(searchPollName);
        //setting the category in text view
        searchPollView2.searchPollHeaderCategoryMultipleOptions.setText(searchPollCategory);
        //setting the updated time in text view
        searchPollView2.searchPolltxtTimeMultipleOptions.setText(searchPollupdatedTime);
        //setting the poll count in text view
        searchPollView2.txtLikeMultipleOptions.setText(searchPolllikeCount);
        //setting the comments count in text view
        txtComments.setText(MApplication.getString(mSearchPollReview,Constants.COMMENTS_COUNT_POLL_REVIEW));
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(searchPolllikeUser)) {
            searchPollView2.likeUnlikeMultipleOptions.setChecked(true);
        } else {
            searchPollView2.likeUnlikeMultipleOptions.setChecked(false);
        }
        //Setting the question intext view
        searchPollView2.searchPollTxtQuestionMultipleOptions.setText(pollResponseModelView2.getResults().getPollreview().get(0).getPollQuestion());
        //poll answer option from the response
        searchPollAnswer1 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1();
        //poll answer option from the response
        searchPollAnswer2 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2();
        //poll answer option from the response
        searchPollAnswer3 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer3();
        //poll answer option pollReviewResponseModel the response
        searchPollAnswer4 = pollResponseModelView2.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer4();
        //Participater answer details from the answer
        searchPollOptionParticipated = pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        for (int i = 0; searchPollOptionParticipated.size() > i; i++) {
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
                searchPollView2.searchPolltxtFirstOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView2.searchPollprogressbarFirstOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView2.searchPollGroupAnswer1MultipleOptions.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView2.searchPolltxtSecondOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView2.searchPollprogressbarSecondOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView2.searchPollGroupAnswer2MultipleOptions.setText(mPollCount);
            } else if (("3").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView2.searchPolltxtThirdOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView2.searchPollprogressbarThirdOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView2.searchPollGroupAnswer3MultipleOptions.setText(mPollCount);
            } else if (("4").equals(pollResponseModelView2.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView2.searchPolltxtFourthOptionCountMultipleOptions.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView2.searchPollprogressbarFourthOptionMultipleOptions.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView2.searchPollGroupAnswer4MultipleOptions.setText(mPollCount);
            }
        }
//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
            //view gone
            searchPollView2.searchPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
            //view gone
            searchPollView2.searchPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage1, searchPollView2.searchPollimageQuestion1MultipleOptions,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage2, searchPollView2.searchPollimageQuestion2MultipleOptions,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
                //view visible
                searchPollView2.searchPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage1, searchPollView2.searchPollsingleOptionMultipleOptions,R.drawable.placeholder_image);
                //view gone
                searchPollView2.searchPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                searchPollView2.searchPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //view visible
                searchPollView2.searchPollsingleOptionMultipleOptions.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage2, searchPollView2.searchPollsingleOptionMultipleOptions,R.drawable.placeholder_image);

                //view gone
                searchPollView2.searchPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                searchPollView2.searchPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            } else {
                //view gone
                searchPollView2.searchPollimageQuestion1MultipleOptions.setVisibility(View.GONE);
                //view gone
                searchPollView2.searchPollimageQuestion2MultipleOptions.setVisibility(View.GONE);
            }
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer1)) {
            searchPollView2.searchPolloption1MultipleOptions.setVisibility(View.GONE);
            //If user poll answer 1 is not empty then it is set in the text view
        } else if (!("").equals(searchPollAnswer1)) {
            searchPollView2.searchPolloption1MultipleOptions.setVisibility(View.VISIBLE);
            searchPollView2.searchPolloption1MultipleOptions.setText(searchPollAnswer1);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer2)) {
            searchPollView2.searchPolloption2MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(searchPollAnswer2)) {
            searchPollView2.searchPolloption2MultipleOptions.setVisibility(View.VISIBLE);
            //If user poll answer 2 is not empty then it is set in the text view
            searchPollView2.searchPolloption2MultipleOptions.setText(searchPollAnswer2);
        }
        //If the poll answer is empty then the view will be gone
        //else url is set into the image view
        if (("").equals(searchPollAnswer3)) {
            //view gone
            searchPollView2.searchPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            searchPollView2.searchPolloption3MultipleOptions.setVisibility(View.GONE);
            //view gone
            searchPollView2.searchPollrelativePrgressBar3MultipleOptions.setVisibility(View.GONE);
            //view gone
            searchPollView2.searchPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //view gone
            searchPollView2.searchPollGroupAnswer3MultipleOptions.setVisibility(View.GONE);
            //view gone
            searchPollView2.searchPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
        } else if (!("").equals(searchPollAnswer3)) {
            //visible
            searchPollView2.searchPolloption3MultipleOptions.setVisibility(View.VISIBLE);
            //setting the data in text view
            searchPollView2.searchPolloption3MultipleOptions.setText(searchPollAnswer3);
            //view gone
            searchPollView2.searchPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
            //progressbar
            searchPollView2.searchPollrelativePrgressBar3MultipleOptions.setVisibility(View.VISIBLE);
            //group answer
            searchPollView2.searchPollGroupAnswer3MultipleOptions.setVisibility(View.VISIBLE);
            //If the poll answer is empty then the view will be gone
            //else url is set into the image view
            if (("").equals(searchPollAnswer4)) {
                //view gone
                searchPollView2.searchPolloption4MultipleOptions.setVisibility(View.GONE);
                //view gone
                searchPollView2.searchPollrelativePrgressBar4MultipleOptions.setVisibility(View.GONE);
                //view gone
                searchPollView2.searchPollGroupAnswer4MultipleOptions.setVisibility(View.GONE);
            } else if (!("").equals(searchPollAnswer4)) {
                //view visible
                searchPollView2.searchPollrelativePrgressBar4MultipleOptions.setVisibility(View.VISIBLE);
                //view visible
                searchPollView2.searchPolloption4MultipleOptions.setVisibility(View.VISIBLE);
                //Setting the the text view
                searchPollView2.searchPolloption4MultipleOptions.setText(searchPollAnswer4);
                //view visible
                searchPollView2.searchPollGroupAnswer4MultipleOptions.setVisibility(View.VISIBLE);
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.likeUnlikeMultipleOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If check box is checked
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;     //Setting the int value in variable
                    searchPollView2.likeUnlikeMultipleOptions.setChecked(true); //set checked true
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
                } else {
                    mlikes = 0;   //Setting the int value in variable
                    searchPollView2.likeUnlikeMultipleOptions.setChecked(false); //set checked true
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
                }
            }
        });
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPolltxtCountsMultipleOptions.setOnClickListener(mSearchPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.txtLikeMultipleOptions.setOnClickListener(mSearchPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.imgShareMultipleOptions.setOnClickListener(mSearchPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPollimageQuestion1MultipleOptions.setOnClickListener(mSearchPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPollimageQuestion2MultipleOptions.setOnClickListener(mSearchPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPollGroupAnswer1MultipleOptions.setOnClickListener(mSearchpollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPollGroupAnswer2MultipleOptions.setOnClickListener(mSearchPollpollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPollGroupAnswer3MultipleOptions.setOnClickListener(mSearchPollpollAnswer3Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPollGroupAnswer4MultipleOptions.setOnClickListener(mSearchPollAnswer4Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView2.searchPollsingleOptionMultipleOptions.setOnClickListener(mSearchPollsingleImageView);

    }

    /**
     * This method is used to view the youtube view layout.If the polltype matches with 1 this view is called.
     * @param pollResponseModelView1
     */
    private void firstReview(PollReviewResponseModel pollResponseModelView1) {
        //view holder
        searchPollView1 = new ViewHolder1();
        //Initialization
        searchPollView1.scrollViewYesOrNO = (ScrollView) findViewById(R.id.scroll);
        searchPollView1.searchPollGroupAnswerYesOrNO = (TextView) findViewById(R.id.pollGroupAnswer);
        searchPollView1.searchPollGroupAnswer1YesOrNO = (TextView) findViewById(R.id.pollGroupAnswer1);
        searchPollView1.searchPollTxtQuestionYesOrNO = (TextView) findViewById(R.id.txtStatus);
        searchPollView1.searchPollimageQuestion1YesOrNO = (ImageView) findViewById(R.id.choose);
        searchPollView1.searchPollimageQuestion2YesOrNO = (ImageView) findViewById(R.id.ChooseAdditional);
        searchPollView1.searchPollsingleOptionYesOrNO = (ImageView) findViewById(R.id.singleOption);
        searchPollView1.searchPollHeaderImageYesOrNO = (ImageView) findViewById(R.id.imgProfile);
        searchPollView1.searchPolltxtTimeYesOrNO = (TextView) findViewById(R.id.txtTime);
        searchPollView1.searchPollHeaderNameYesOrNO = (TextView) findViewById(R.id.txtName);
        searchPollView1.searchPollHeaderCategoryYesOrNO = (TextView) findViewById(R.id.txtCategory);
        txtComments = (TextView) findViewById(R.id.txtCommentsCounts);
        searchPollView1.likeUnlikeYesOrNO = (CheckBox) findViewById(R.id.unLikeDislike);
        searchPollView1.txtLikeYesOrNO = (TextView) findViewById(R.id.txtLike2);
        searchPollView1.searchPolltxtFirstOptionCountYesOrNO = (TextView) findViewById(R.id.txtFirstOptionCount);
        searchPollView1.searchPolltxtSecondOptionCountYesOrNO = (TextView) findViewById(R.id.txtSecondOptionCount);
        searchPollView1.searchPolltxtCountsYesOrNO = (TextView) findViewById(R.id.txtParticcipation);
        searchPollView1.searchPolloption1YesOrNO = (TextView) findViewById(R.id.option1);
        searchPollView1.searchPolloption2YesOrNO = (TextView) findViewById(R.id.option2);
        searchPollView1.searchPollprogressbarFirstOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarFirstOption);
        searchPollView1.searchPollprogressbarSecondOptionYesOrNO = (ProgressBar) findViewById(R.id.progressbarSecondOption);
        searchPollView1.imgShareYesOrNO = (ImageView) findViewById(R.id.imgShare);
        //Setting the visiblity
        searchPollView1.scrollViewYesOrNO.setVisibility(View.VISIBLE);
        //setting the participate count
        searchPollView1.searchPolltxtCountsYesOrNO.setText(userPollparticipateCount);
        //question image 1 fromt the response
        searchPollImage1 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage().replaceAll(" ", "%20");
        //question image 2 fromt the response
        searchPollImage2 = pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestionimage2().replaceAll(" ", "%20");
        Utils.loadImageWithGlideProfileRounderCorner(this,searchPollLogo.replaceAll(" ", "%20"), searchPollView1.searchPollHeaderImageYesOrNO,R.drawable.placeholder_image);
        //setting the name in text view
        searchPollView1.searchPollHeaderNameYesOrNO.setText(searchPollName);
        //setting the category in text view
        searchPollView1.searchPollHeaderCategoryYesOrNO.setText(searchPollCategory);
        //setting the time in text view
        searchPollView1.searchPolltxtTimeYesOrNO.setText(searchPollupdatedTime);
        //setting the like count
        searchPollView1.txtLikeYesOrNO.setText(searchPolllikeCount);
        //setting the comments in text view
        txtComments.setText(MApplication.getString(mSearchPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
        //setting the question in text view
        searchPollView1.searchPollTxtQuestionYesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollQuestion());
        //setting the option1 in text view
        searchPollView1.searchPolloption1YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer1());
        //setting the option2 in text view
        searchPollView1.searchPolloption2YesOrNO.setText(pollResponseModelView1.getResults().getPollreview().get(0).getPollAnswer().get(0).getPollAnswer2());
        //If the value matches with 1 then setting as checked
        //else unchecked
        if (("1").equals(searchPolllikeUser)) {
            searchPollView1.likeUnlikeYesOrNO.setChecked(true);
        } else {
            searchPollView1.likeUnlikeYesOrNO.setChecked(false);
        }
        //Participater answer details from the answer
        searchPollOptionParticipated = pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll();
        //Getting the values based on the size
        //Getting the values based on the size
        for (int i = 0; searchPollOptionParticipated.size() > i; i++) {
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
                searchPollView1.searchPolltxtFirstOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
                //Setting the progress bar to fill
                searchPollView1.searchPollprogressbarFirstOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView1.searchPollGroupAnswerYesOrNO.setText(mPollCount);
            } else if (("2").equals(pollResponseModelView1.getResults().getPollreview().get(0).getParticipatePoll().get(i).getPollAnswerOption())) {
                //Setting the text as percentage
                searchPollView1.searchPollGroupAnswer1YesOrNO.setText(mPollCount);
                //Setting the progress bar to fill
                searchPollView1.searchPollprogressbarSecondOptionYesOrNO.setProgress(mPercentage);
                //Setting the poll count in text view
                searchPollView1.searchPolltxtSecondOptionCountYesOrNO.setText(String.valueOf(mPercentage) + "%");
            }
        }
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.searchPolltxtCountsYesOrNO.setOnClickListener(mSearchPollParticipantCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.txtLikeYesOrNO.setOnClickListener(mSearchPollLikeCount);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.imgShareYesOrNO.setOnClickListener(mSearchPollSharePoll);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.searchPollimageQuestion1YesOrNO.setOnClickListener(mSearchPollQuestionImage1);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.searchPollimageQuestion2YesOrNO.setOnClickListener(mSearchPollQuestionImage2);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.searchPollGroupAnswerYesOrNO.setOnClickListener(mSearchpollAnswer1Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.searchPollGroupAnswer1YesOrNO.setOnClickListener(mSearchPollpollAnswer2Participants);
        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.searchPollsingleOptionYesOrNO.setOnClickListener(mSearchPollsingleImageView);

//If the user poll image 1 and image 2 is empty then the both the visiblity is gone
        if (("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
            //view gone
            searchPollView1.searchPollimageQuestion1YesOrNO.setVisibility(View.GONE);
            //view gone
            searchPollView1.searchPollimageQuestion2YesOrNO.setVisibility(View.GONE);
        } else {
            //If the  image 1 is not empty and image 2 is not empty then the image is set into the image view
            if (!("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage1, searchPollView1.searchPollimageQuestion1YesOrNO,R.drawable.placeholder_image);
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage2, searchPollView1.searchPollimageQuestion2YesOrNO,R.drawable.placeholder_image);
                //If the  image is  empty and image 2 is not empty then the image2 is set into the image view
            } else if (!("").equals(searchPollImage1) && ("").equals(searchPollImage2)) {
                //view visible
                searchPollView1.searchPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //user poll image1 is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage1, searchPollView1.searchPollsingleOptionYesOrNO,R.drawable.placeholder_image);
                //view gone
                searchPollView1.searchPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                searchPollView1.searchPollimageQuestion2YesOrNO.setVisibility(View.GONE);
                //If the  image is not  empty and image  is empty then the image2 is set into the image view
            } else if (("").equals(searchPollImage1) && !("").equals(searchPollImage2)) {
                //view visible
                searchPollView1.searchPollsingleOptionYesOrNO.setVisibility(View.VISIBLE);
                //view is set into the image view
                Utils.loadImageWithGlideRounderCorner(this,searchPollImage2, searchPollView1.searchPollsingleOptionYesOrNO,R.drawable.placeholder_image);
                //view gone
                searchPollView1.searchPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                searchPollView1.searchPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            } else {
                //view gone
                searchPollView1.searchPollimageQuestion1YesOrNO.setVisibility(View.GONE);
                //view gone
                searchPollView1.searchPollimageQuestion2YesOrNO.setVisibility(View.GONE);
            }
        }


        //Interface definition for a callback to be invoked when a view is clicked.
        searchPollView1.likeUnlikeYesOrNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If check box is checked
                if (((CheckBox) v).isChecked()) {
                    mlikes = 1;    //Setting the int value in variable
                    searchPollView1.likeUnlikeYesOrNO.setChecked(true); //set checked true
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
                } else {
                    mlikes = 0;   //Setting the int value in variable
                    searchPollView1.likeUnlikeYesOrNO.setChecked(false);     //set checked true
                    //Like unlike for the poll request
                    likesUnLikes(searchPolllistposition);
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
        LikesAndUnLikeRestClient.getInstance().postCampaignPollLikes(new String("poll_likes"), new String(userId), new String(searchPollId), new String(String.valueOf(mlikes))
                , new Callback<LikeUnLikeResposneModel>() {
            @Override
            public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                //If the value from the response is 1 then the user has successfully liked the poll
                if (("1").equals(likesUnlike.getResults())) {
                    //Changing the value in array list in a particular position
                    preferencesearchPollLikeUser.set(clickPosition, Integer.valueOf(1));
                    //Saving it in array
                    MApplication.saveArray(mSearchPollReview, preferencesearchPollLikeUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
                } else {
                    //Changing the value in array list in a particular position
                    preferencesearchPollLikeUser.set(clickPosition, Integer.valueOf(0));
                    //Saving it in array
                    MApplication.saveArray(mSearchPollReview, preferencesearchPollLikeUser, Constants.SEARCH_POLL_LIKES_USER_ARRAY, Constants.SEARCH_POLL_LIKES_USER_SIZE);
                }
                //Toast message will display
                Toast.makeText(mSearchPollReview, likesUnlike.getMsg(),
                        Toast.LENGTH_SHORT).show();
                //Changing the value in array list in a particular position
                prefrencesearchPollLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                //Saving the array in prefernce
                MApplication.saveArray(mSearchPollReview, prefrencesearchPollLikeCount, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY, Constants.SEARCH_POLL_LIKES_COUNT_ARRAY);
                //Changing the value in textview
                txtLike.setText(String.valueOf(prefrencesearchPollLikeCount.get(clickPosition)));
            }


            @Override
            public void failure(RetrofitError retrofitError) {
                ////Error message popups when the user cannot able to coonect with the server
                MApplication.errorMessage(retrofitError, mSearchPollReview);
            }

        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        //Setting the comments in text view
        txtComments.setText(MApplication.getString(mSearchPollReview, Constants.COMMENTS_COUNT_POLL_REVIEW));
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder4 {
        //Scroll
        private ScrollView scrollViewYouTubeLayout;
        //Textview
        private TextView searchPolltxtCountsYouTubeLayout;
        //Textview
        private TextView searchPollGroupAnswer1YouTubeLayout;
        //Textvie
        private TextView searchPollGroupAnswer2YouTubeLayout;
        //Textview
        private TextView searchPollGroupAnswer3YouTubeLayout;
        //Textview
        private TextView searchPollGroupAnswer4YouTubeLayout;
        //Textview
        private TextView searchPolloption3YouTubeLayout;
        //Textview
        private TextView searchPolloption4YouTubeLayout;
        //Progressbar
        private ProgressBar searchPollprogressbarThirdOptionYouTubeLayout, searchPollprogressbarFourthOptionYouTubeLayout;
        //Imageview
        private ImageView searchPollHeaderImageYouTubeLayout;
        //Imagewview
        private ImageView imgShareYouTubeLayout;
        //Textview
        private TextView searchPolltxtFirstOptionCountYouTubeLayout;
        //Textview
        private TextView searchPolltxtSecondOptionCountYouTubeLayout;
        //Textview
        private TextView searchPolltxtThirdOptionCountYouTubeLayout;
        //Textview
        private TextView searchPolltxtFourthOptionCountYouTubeLayout;
        //Relativelayout
        private RelativeLayout searchPollrelativePrgressBar3YouTubeLayout;
        //Relativelayout
        private RelativeLayout searchPollrelativePrgressBar4YouTubeLayout;
        //Textview
        private TextView searchPollTxtQuestionYouTubeLayout;
        ///textview
        private TextView searchPolloption1YouTubeLayoutYouTubeLayout;
        //Textview
        private TextView searchPolloption2YouTubeLayout;
        //Imageview
        private SimpleDraweeView searchPollimageQuestion1YouTubeLayout;
        //Imageview
        private SimpleDraweeView searchPollimageQuestion2YouTubeLayout;
        //Imageview
        private SimpleDraweeView searchPollsingleOptionYouTubeLayout;
        //Progressbar
        private ProgressBar searchPollprogressbarFirstOptionYouTubeLayout;
        //Progressbar
        private ProgressBar searchPollprogressbarSecondOptionYouTubeLayout;
        //Textview
        private TextView searchPollltxtTimeYouTubeLayout;
        //Textview
        private TextView txtLikeYouTubeLayout;
        //Textview
        private TextView searchPollHeaderCategoryYouTubeLayout;
        //Textview
        private TextView searchPollHeaderNameYouTubeLayout;
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
        private TextView searchPolltxtCountsYesOrNO;
        //Textview
        private TextView searchPollGroupAnswerYesOrNO;
        //Textview
        private TextView searchPollGroupAnswer1YesOrNO;
        //Imageview
        private ImageView searchPollHeaderImageYesOrNO;
        //Imageview
        private ImageView imgShareYesOrNO;
        //Textview
        private TextView searchPolltxtFirstOptionCountYesOrNO;
        //Textview
        private TextView searchPolltxtSecondOptionCountYesOrNO;
        //Textview
        private TextView searchPollTxtQuestionYesOrNO;
        //Textview
        private TextView searchPolloption1YesOrNO;
        //Textview
        private TextView searchPolloption2YesOrNO;
        //Imageview
        private ImageView searchPollimageQuestion1YesOrNO;
        //Imageview
        private ImageView searchPollimageQuestion2YesOrNO;
        //Imageview
        private ImageView searchPollsingleOptionYesOrNO;
        //Progressbar
        private ProgressBar searchPollprogressbarFirstOptionYesOrNO;
        //Progressbar
        private ProgressBar searchPollprogressbarSecondOptionYesOrNO;
        //Textview
        private TextView searchPolltxtTimeYesOrNO;
        //Textview
        private TextView txtLikeYesOrNO;
        //Textview
        private TextView searchPollHeaderCategoryYesOrNO;
        //Textview
        private TextView searchPollHeaderNameYesOrNO;
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
        private TextView searchPollPhotoComparisontxtCounts;
        //Textview
        private TextView searchPollPhotoComparisonGroupAnswer1;
        //Textview
        private TextView searchPollPhotoComparisonGroupAnswer2;
        //Textview
        private TextView searchPollPhotoComparisonGroupAnswer3;
        //Textview
        private TextView searchPollPhotoComparisonGroupAnswer4;
        //Relative layout
        private RelativeLayout searchPollPhotoComparisonthirdOption;
        //Relative layout
        private RelativeLayout searchPollPhotoComparisonfourthOption;
        //Progressbar
        private ProgressBar searchPollPhotoComparisonprogressbarThirdOption, searchPollPhotoComparisonprogressbarFourthOption;
        //Imageview
        private ImageView searchPollPhotoComparisonimageAnswer1;
        //Imageview
        private ImageView searchPollPhotoComparisonimageAnswer2;
        //Imageview
        private ImageView searchPollPhotoComparisonimageAnswer3;
        //Imageview
        private ImageView searchPollPhotoComparisonimageAnswer4;
        //Imageview
        private ImageView searchPollPhotoComparisonHeaderImage;
        //Imageview
        private ImageView searchPollPhotoComparisonimgShare;
        //Textview
        private TextView searchPollPhotoComparisontxtFirstOptionCount;
        //Textview
        private TextView searchPollPhotoComparisontxtSecondOptionCount;
        //Textview
        private TextView searchPollPhotoComparisontxtThirdOptionCount;
        //Textview
        private TextView searchPollPhotoComparisontxtFourthOptionCount;
        //Relative layout
        private RelativeLayout searchPollPhotoComparisonrelativePrgressBar3;
        //Relative layout
        private RelativeLayout searchPollPhotoComparisonrelativePrgressBar4;
        //Textview
        private TextView searchPollPhotoComparisonTxtQuestion;
        //Imageview
        private ImageView searchPollPhotoComparisonimageQuestion1;
        //Imageview
        private ImageView searchPollPhotoComparisonimageQuestion2;
        //Imageview
        private ImageView searchPollPhotoComparisonsingleOption;
        //Progressbar
        private ProgressBar searchPollPhotoComparisonprogressbarFirstOption;
        //Progressbar
        private ProgressBar searchPollPhotoComparisonprogressbarSecondOption;
        //Textview
        private TextView searchPollPhotoComparisontxtTime;
        //Textview
        private TextView searchPollPhotoComparisonTxtLike;
        //Textview
        private TextView searchPollPhotoComparisonHeaderCategory;
        //Textview
        private TextView searchPollPhotoComparisonHeaderName;
        //Checkbox
        private CheckBox searchPollPhotoComparisonlikeUnlike;
    }

    /**
     * A ViewHolderLayoutOne object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder2 {
        //Multiple ptions scrollview
        private ScrollView scrollViewMultipleOptions;
        //participate count
        private TextView searchPolltxtCountsMultipleOptions;
        //Participate Count for answer1
        private TextView searchPollGroupAnswer1MultipleOptions;
        //Participate Count for answer2
        private TextView searchPollGroupAnswer2MultipleOptions;
        //Participate Count for answer3
        private TextView searchPollGroupAnswer3MultipleOptions;
        //Participate Count for answer4
        private TextView searchPollGroupAnswer4MultipleOptions;
        //option3
        private TextView searchPolloption3MultipleOptions;
        //option4
        private TextView searchPolloption4MultipleOptions;
        //Progressbar
        private ProgressBar searchPollprogressbarThirdOptionMultipleOptions, searchPollprogressbarFourthOptionMultipleOptions;
        //Imageview
        private ImageView searchPollHeaderImageMultipleOptions;
        //Imageview
        private ImageView imgShareMultipleOptions;
        //Text view
        private TextView searchPolltxtFirstOptionCountMultipleOptions;
        //Text view
        private TextView searchPolltxtSecondOptionCountMultipleOptions;
        //Textview
        private TextView searchPolltxtThirdOptionCountMultipleOptions;
        //Text view
        private TextView searchPolltxtFourthOptionCountMultipleOptions;
        //Relative layout
        private RelativeLayout searchPollrelativePrgressBar3MultipleOptions;
        //Relative layout
        private RelativeLayout searchPollrelativePrgressBar4MultipleOptions;
        //Textview
        private TextView searchPollTxtQuestionMultipleOptions;
        //Textview
        private TextView searchPolloption1MultipleOptions;
        //Textview
        private TextView searchPolloption2MultipleOptions;
        //Imageview
        private ImageView searchPollimageQuestion1MultipleOptions;
        //Imageview
        private ImageView searchPollimageQuestion2MultipleOptions;
        //Imageview
        private ImageView searchPollsingleOptionMultipleOptions;
        //Progressabar
        private ProgressBar searchPollprogressbarFirstOptionMultipleOptions;
        //Progressabar
        private ProgressBar searchPollprogressbarSecondOptionMultipleOptions;
        //Textview
        private TextView searchPolltxtTimeMultipleOptions;
        //Textview
        private TextView txtLikeMultipleOptions;
        //Textview
        private TextView searchPollHeaderCategoryMultipleOptions;
        //Textview
        private TextView searchPollHeaderNameMultipleOptions;
        //Checkbox
        private CheckBox likeUnlikeMultipleOptions;
    }
}
