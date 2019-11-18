package com.contus.publicpoll;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.campaignparticipate.Participate;
import com.contus.responsemodel.LikeUnLikeResposneModel;
import com.contus.responsemodel.PublicPollResponseModel;
import com.contus.restclient.LikesAndUnLikeRestClient;
import com.contusfly.MApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 7/8/2015.
 */
public class CustomAdapter extends ArrayAdapter<PublicPollResponseModel.Data> {
    //Layout id
    private final int publicLayoutId;
    //User id
    private final String userId;
    //Activity
    Activity context;
    //Campaign
    private String campaign;
    //data from the response
    private PublicPollResponseModel.Data publicPollResponse;
    //Updated date
    private String updatedDate;
    //Mlikes value
    private int mlikes;
    //View holder
    private ViewHolder holder = null;
    //Layout inflanter
    private LayoutInflater mInflater;
    //Campaign like count
    private ArrayList<Integer> campaignLikeCount = new ArrayList<Integer>();
    //Campaign like user
    private ArrayList<Integer> campaignLikesUser = new ArrayList<Integer>();
    //Comments count
    private ArrayList<Integer> commentsCount = new ArrayList<Integer>();
    //Loading the like count in prefernce
    private ArrayList<Integer> prefernceLikeCount;
    //Loading the like user in prefernce
    private ArrayList<Integer> preferenceLikeUser;
    //Comments count
    private ArrayList<Integer> prefrenceCommentsCount;

    /**
     * initializes a new instance of the ListView class.
     *
     * @param activity    activity
     * @param dataResults dataResults
     * @param layoutId    layoutId
     * @param userId      userId
     */
    public CustomAdapter(Activity activity, List<PublicPollResponseModel.Data> dataResults, int layoutId, String userId) {
        super(activity, 0, dataResults);
        this.publicLayoutId = layoutId;
        this.context = activity;
        this.userId = userId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (convertView == null) {
               /* create a new view of my layout and inflate it in the row */
            convertView = mInflater.inflate(publicLayoutId, parent, false);
            //view holder
            holder = new ViewHolder();
            holder.imgFullView = (SimpleDraweeView) convertView.findViewById(R.id.imgBanner);
            holder.campaignImage = (SimpleDraweeView) convertView.findViewById(R.id.imgProfile);
            holder.participate = (TextView) convertView.findViewById(R.id.participate);
            holder.campaignName = (TextView) convertView.findViewById(R.id.txtName);
            holder.campaignCategory = (TextView) convertView.findViewById(R.id.txtCategory);
            holder.campaignDesciption = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.pollDescription = (TextView) convertView.findViewById(R.id.txtImageDescription);
            holder.timeShare = (TextView) convertView.findViewById(R.id.timeShare);
            holder.txtLike = (TextView) convertView.findViewById(R.id.txtLike2);
            holder.txtComments = (TextView) convertView.findViewById(R.id.txtCommentsCounts);
            holder.likeUnlike = (CheckBox) convertView.findViewById(R.id.unLikeDislike);
            holder.imgShare = (ImageView) convertView.findViewById(R.id.imgShare);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtLike.setTag(position);
        //Get the object details from the particular position
        publicPollResponse = getItem(position);
        //Loadignt he array list from the prefernce
        prefrenceCommentsCount = MApplication.loadArray(context, commentsCount, Constants.CAMPAIGN_COMMENTS_COUNT, Constants.CAMPAIGN_COMMENTS_SIZE);
        //Setting the COMMENTS count in text view
        holder.txtComments.setText(String.valueOf(prefrenceCommentsCount.get(position)));
        //Loading the like user detils from the prefernce
        preferenceLikeUser = MApplication.loadArray(context, campaignLikesUser, Constants.CAMPAIGN_LIKES_USER_ARRAY, Constants.CAMPAIGN_LIKES_USER_SIZE);
        //Loading the like count detils from the prefernce
        prefernceLikeCount = MApplication.loadArray(context, campaignLikeCount, Constants.CAMPIGN_LIKES_COUNT_ARRAY, Constants.CAMPIGN_LIKES_COUNT_SIZE);
        //Setting the likes count in text view
        holder.txtLike.setText(String.valueOf(prefernceLikeCount.get(position)));
        //If the details from the loaded array list prefernce has the array value 1
        //then the radio option will checked
        //Otherwise it wont be checked
        if (preferenceLikeUser.get(position).equals(1)) {
            holder.likeUnlike.setChecked(true);
        } else {
            holder.likeUnlike.setChecked(false);
        }
        //Setting the campaign image
        holder.imgFullView.setImageURI(Uri.parse(publicPollResponse.getCampImage()));
        //Get the partner logo
        campaign = publicPollResponse.getPartner().getPartnerLogo();
        //Get the updated time
        updatedDate = publicPollResponse.getUpdatedAt();
        //Loading the camapign image image uri into the image view
        holder.campaignImage.setImageURI(Uri.parse(campaign));
        //Setting the updated time
        holder.timeShare.setText(MApplication.getTimeDifference(updatedDate));
        //Getting the updated time
        holder.campaignName.setText(publicPollResponse.getCampName());
        //Getting the category name and setting into the text view
        holder.campaignCategory.setText(publicPollResponse.getCategory().getCategoryName());
        //Get the partner tag line  and setting in text view
        holder.campaignDesciption.setText(publicPollResponse.getPartner().getPartnerTagLine());
        //Get the camp description
        holder.pollDescription.setText(publicPollResponse.getCampDescription());
        //Interface definition for a callback to be invoked when a view is clicked.
        holder.participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Click position
                int clickPosition = position;
                //Requesting the campaign poll class
                participateView(clickPosition);

            }
        });

        //Interface definition for a callback to be invoked when a view is clicked.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Requesting the campaign poll class
                participateView(clickPosition);
            }
        });

        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Click position
                int clickPosition = position;
                //Getting the response from the aprticular position
                publicPollResponse = getItem(clickPosition);
                //Getting the id
                String campaignId = publicPollResponse.getId();
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity,
                Intent details = new Intent(context, CampaignComments.class);
//                //Add extended data to the intent.
                details.putExtra(Constants.CAMPAIGN_ID, campaignId);
                //Add extended data to the intent.
                details.putExtra(Constants.COMMENTS_COUNT_POSITION, clickPosition);
                //Starting the activity
                context.startActivity(details);
            }
        });

        //Interface definition for a callback to be invoked when a view is clicked.
        holder.txtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Click position
                int clickPosition = position;
                //Getting the response from the aprticular position
                publicPollResponse = getItem(clickPosition);
                //Getting the id
                String campaignId = publicPollResponse.getId();
                //An intent is an abstract description of an operation to be performed.
                // It can be used with startActivity to launch an Activity
                Intent details = new Intent(context, CampaignLikes.class);
                //Add extended data to the intent.
                details.putExtra(Constants.CAMPAIGN_ID, campaignId);
                //Starting the activity
                context.startActivity(details);
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click position
                int clickPosition = position;
                //Getting the response from the aprticular position
                publicPollResponse = getItem(clickPosition);
                //Getting the id
                String campaignId = publicPollResponse.getId();
                //Converted the string into base 64
                String base64CampaignId = MApplication.convertByteCode(campaignId);
                //url to share
                String shareUrl = Constants.SHARE_CAMPAIGN_BASE_URL.concat(base64CampaignId);
                //Sharing the url in gmail

                MApplication.getSharedURL(shareUrl,context);
                //MApplication.shareGmail(context, shareUrl, MApplication.getString(context, Constants.PROFILE_USER_NAME));
            }
        });

//Listen views
        listenViews(holder, position);
        //return vies
        return convertView;
    }

    /**
     * @param clickPosition
     */
    private void participateView(int clickPosition) {
        //Getting the response from the aprticular position
        publicPollResponse = getItem(clickPosition);
        //campaign id
        String campaignId = publicPollResponse.getId();
        //partner logo
        String partnerLogo = publicPollResponse.getPartner().getPartnerLogo();
        //campaing name
        String campaignName = publicPollResponse.getCampName();
        String getShareTime = publicPollResponse.getUpdatedAt();

        //campaign category
        String camapignCategory = publicPollResponse.getCategory().getCategoryName();
        //An intent is an abstract description of an operation to be performed.
        // It can be used with startActivity to launch an Activity
        Intent details = new Intent(context, Participate.class);
        //Add extended data to the intent.
        details.putExtra(Constants.CAMPAIGN_ID, campaignId);
        //Add extended data to the intent.
        details.putExtra(Constants.CAMPAIGN_NAME, campaignName);
        details.putExtra(Constants.CAMPAIGN_DATE, MApplication.getTimeDifference(getShareTime));
        //Add extended data to the intent.
        details.putExtra(Constants.CAMPAIGN_CATEGORY, camapignCategory);
        //Add extended data to the intent.
        details.putExtra(Constants.CAMPAIGN_LOGO, partnerLogo);
        //starting the activity
        context.startActivity(details);
    }

    /**
     * Listen the like unlike views
     *
     * @param holder   holder
     * @param position
     */
    private void listenViews(final ViewHolder holder, final int position) {
        holder.likeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If checkbox is  checked
                if (((CheckBox) v).isChecked()) {
                    //then the variable value is assigned as 1
                    mlikes = 1;
                    //Set checked as true
                    holder.likeUnlike.setChecked(true);
                    //Request for like and unlike
                    likesUnLikes(holder, position);
                } else {
                    //then the variable value is assigned as 0
                    mlikes = 0;
                    //Set checked false
                    holder.likeUnlike.setChecked(false);
                    //Request for like and unlike
                    likesUnLikes(holder, position);
                }
            }
        });
    }

    /**
     * @param v
     * @param clickPosition
     */
    private void likesUnLikes(final ViewHolder v, final int clickPosition) {
        //Material dialog starts
        MApplication.materialdesignDialogStart(context);
        //Object based on click position
        publicPollResponse = getItem(clickPosition);
        //Get campaing id
        String campaignId = publicPollResponse.getId();
        LikesAndUnLikeRestClient.getInstance().postLikes(new String("Camp_likes"), new String(userId), new String(campaignId), new String(String.valueOf(mlikes))
                , new Callback<LikeUnLikeResposneModel>() {
                    @Override
                    public void success(LikeUnLikeResposneModel likesUnlike, Response response) {
                        //If below condition matches with 1 then the user has liked the poll
                        //else the user has unkliked the poll so value will be as 0
                        if (("1").equals(likesUnlike.getResults())) {
                            //Change the vlaue in particular position loaded from the prefernce
                            preferenceLikeUser.set(clickPosition, Integer.valueOf(1));
                            //Saving the array in prefernce
                            MApplication.saveArray(context, preferenceLikeUser, Constants.CAMPAIGN_LIKES_USER_ARRAY, Constants.CAMPAIGN_LIKES_USER_SIZE);
                        } else {
                            //Change the vlaue in particular position loaded from the prefernce
                            preferenceLikeUser.set(clickPosition, Integer.valueOf(0));
                            //Saving the array in prefernce
                            MApplication.saveArray(context, preferenceLikeUser, Constants.CAMPAIGN_LIKES_USER_ARRAY, Constants.CAMPAIGN_LIKES_USER_SIZE);
                        }
                        //Toast message will display the message
                        Toast.makeText(context, likesUnlike.getMsg(), Toast.LENGTH_SHORT).show();
                        //Changet the like count in particular position
                        prefernceLikeCount.set(clickPosition, Integer.valueOf(likesUnlike.getCount()));
                        //Saving the array in prefernce
                        MApplication.saveArray(context, prefernceLikeCount, Constants.CAMPIGN_LIKES_COUNT_ARRAY, Constants.CAMPIGN_LIKES_COUNT_SIZE);
                        //Setting the count in text view
                        v.txtLike.setText(String.valueOf(prefernceLikeCount.get(clickPosition)));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        //Error message popups when the user cannot able to coonect with the server
                        MApplication.errorMessage(retrofitError, context);
                    }
                });
        //Progress bar stops
        MApplication.materialdesignDialogStop();
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder {
        //Imageview
        private SimpleDraweeView imgFullView;
        //Campaing name
        private TextView campaignName;
        //Campaing category
        private TextView campaignCategory;
        //Campaign description
        private TextView campaignDesciption;
        //Participate
        private TextView participate;
        //Time share
        private TextView timeShare;
        //Poll description
        private TextView pollDescription;
        //Campaign image
        private SimpleDraweeView campaignImage;
        //Text like
        private TextView txtLike;
        //Text COMMENTS
        private TextView txtComments;
        //Like un like
        private CheckBox likeUnlike;
        //Image view
        private ImageView imgShare;
    }
}


