package com.contus.publicpoll;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contus.responsemodel.LikesResponseModel;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.List;

/**
 * Created by user on 7/8/2015.
 */
public class CampaignLikesPollAdapter extends ArrayAdapter<LikesResponseModel.Results.Data> {
    //Layout id
    private final int campaignLikesAdapter;
    //Activity
    Context campaignLikesActivity;
    //Likes user profiel
    private String campaignLikesUserProfile;
    //Response from the model class
    private LikesResponseModel.Results.Data campaignLikesResults;
    //View holder
    private ViewHolder holder;

    /**
     * initializes a new instance of the ListView class.
     * @param activity-activity
     * @param dataResults-dataResults
     * @param layoutId-layoutId
     */
    public CampaignLikesPollAdapter(CampaignLikes activity, List<LikesResponseModel.Results.Data> dataResults, int layoutId) {
        super(activity, 0, dataResults);
        this.campaignLikesAdapter = layoutId;
        this.campaignLikesActivity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                campaignLikesActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        //if (convertView == null) {
             /* create a new view of my layout and inflate it in the row */
            convertView = mInflater.inflate(R.layout.activity_likes_singleview, parent, false);
            //Views holder class
            holder = new ViewHolder();
            //Profile image
            holder.campaignImage = (ImageView) convertView.findViewById(R.id.imgProfile);
            //User name
            holder.userName = (TextView) convertView.findViewById(R.id.txtGroupName);
            convertView.setTag(holder);
        //} else {
         //   holder = (ViewHolder) convertView.getTag();
       // }
        //Get the reposne from the particular position
        campaignLikesResults = getItem(position);
        //Gettting the user profile image
        campaignLikesUserProfile = campaignLikesResults.getUserInfo().getUserProfileImg();
        //Set the profiel image from the resposne
        Utils.loadImageWithGlideProfileRounderCorner(campaignLikesActivity,campaignLikesUserProfile,holder.campaignImage,R.drawable.placeholder_image);
        //Set the url in imageview
       // holder.campaignImage.setImageURI(Uri.parse(campaignLikesUserProfile));
         //Setting the user name in text view
        holder.userName.setText(campaignLikesResults.getUserInfo().getUserName());
         //Returning the views
        return convertView;
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder {
        //User name
        private TextView userName;
        //Campaign image
        private ImageView campaignImage;
    }
}