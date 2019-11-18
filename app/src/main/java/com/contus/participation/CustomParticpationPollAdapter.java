package com.contus.participation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contus.responsemodel.ParticipateResponseModel;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 7/14/2015.
 */
public class CustomParticpationPollAdapter extends ArrayAdapter<ParticipateResponseModel.Results.Data> {
    //Data results
    private final List<ParticipateResponseModel.Results.Data> dataResults;
    //Layout id
    private final int customParticipationLayoutId;
    //Activity context
    Context context;
    //View holder class
    private ViewHolder holder;
    private View mView;

    /**
     * initializes a new instance of the ListView class.
     *
     * @param activity-activity
     * @param dataResults-dataResults
     * @param layoutId-layoutId
     */
    public CustomParticpationPollAdapter(Activity activity, List<ParticipateResponseModel.Results.Data> dataResults, int layoutId) {
        super(activity, 0, dataResults);
        this.context = activity;
        this.customParticipationLayoutId = layoutId;
        this.dataResults = dataResults;
    }

    /*private view holder class*/
    private class ViewHolder {
        private TextView mobileUserName;
        private ImageView imgProfile;
    }

    @Override
    public View getView(int position, View mPollView, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mView = mPollView;
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mView == null) {
             /* create a new view of my layout and inflate it in the row */
            mView = mInflater.inflate(customParticipationLayoutId, null);
            //Views holder class
            holder = new ViewHolder();
            holder.mobileUserName = (TextView) mView.findViewById(R.id.txtGroupName);
            holder.imgProfile = (ImageView) mView.findViewById(R.id.imgProfile);
            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }
        if (dataResults.get(position).getUserInfo() != null) {
            //Seting the user name frpm the response
            holder.mobileUserName.setText(MApplication.getDecodedString(dataResults.get(position).getUserInfo().getUserName()));
            //Getting the category name from the response and binding the data into the text view
            Utils.loadImageWithGlideProfileRounderCorner(context, dataResults.get(position).getUserInfo().getUserProfileImage().replaceAll(" ", "%20"), holder.imgProfile, R.drawable.icon_profile);
        }
        return mView;
    }

}