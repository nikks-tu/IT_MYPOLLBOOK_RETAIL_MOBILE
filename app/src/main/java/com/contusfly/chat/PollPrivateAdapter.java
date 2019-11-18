package com.contusfly.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contus.responsemodel.PollPrivateResponseModel;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

import eu.janmuller.android.simplecropimage.Util;

/**
 * Created by user on 1/19/2016.
 */
public class PollPrivateAdapter extends BaseAdapter {
    //Camera options
    private final List<PollPrivateResponseModel.Results> response;
    //Array list
    private final ArrayList<Object> mArrayList;
    //Context
    Context context;
    //View holder
    private ViewHolder holder;
    //view
    private View mCustomDialogAdapter;

    /**
     * initializes a new instance of the ListView class.
     *
     * @param context       -activiy class
     * @param pollrResponse --string array
     */
    public PollPrivateAdapter(Context context, List<PollPrivateResponseModel.Results> pollrResponse) {
        this.context = context;
        this.response = pollrResponse;
        mArrayList=new ArrayList<>();
        mArrayList.add(pollrResponse);
    }

    @Override
    public View getView(int position, View mView, ViewGroup parent) {
        holder = null;
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //view
        mCustomDialogAdapter = mView;
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mCustomDialogAdapter == null) {
        /* create a new view of my layout and inflate it in the row */
            mCustomDialogAdapter = mInflater.inflate(R.layout.privatepoll_singleview, null);
            //Views holder class
            holder = new ViewHolder();
            //Profile image
            holder.campaignImage = (ImageView) mCustomDialogAdapter.findViewById(R.id.imgProfile);
            //User name
            holder.userName = (TextView) mCustomDialogAdapter.findViewById(R.id.txtGroupName);
            mCustomDialogAdapter.setTag(holder);
        } else {
            holder = (ViewHolder) mCustomDialogAdapter.getTag();
        }
        //Setting the cameraOptions in text view
        holder.userName.setText(MApplication.getDecodedString(response.get(position).getName()));
        Utils.loadImageWithGlideRounderCorner(context,response.get(position).getImage(),
                holder.campaignImage, R.drawable.icon_profile);
        //Setting the cameraOptions in text view
        //Return views
        return mCustomDialogAdapter;
    }

    @Override
    public int getCount() {
        return response.size();
    }

    @Override
    public Object getItem(int position) {
        return response.size();
    }

    @Override
    public long getItemId(int position) {
        return response.size();
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder {
        /*Textview**/
        private ImageView campaignImage;
        /*Textview**/
        private TextView userName;

    }
}
