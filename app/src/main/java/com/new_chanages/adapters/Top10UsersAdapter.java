package com.new_chanages.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.contusfly.utils.Utils;
import com.new_chanages.models.TopUsersModel;
import com.polls.polls.R;

import java.util.List;

/**
 * Created by kishore.i on 2/21/2018.
 */

public class Top10UsersAdapter extends RecyclerView.Adapter<Top10UsersAdapter.ViewHolder> {

    private final List<TopUsersModel> mValues;
    Context context;

    public Top10UsersAdapter(List<TopUsersModel> items) {
        mValues = items;

    }

    @Override
    public Top10UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topusers_item_layout, parent, false);
        context =parent.getContext();
        return new Top10UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Top10UsersAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.user_points.setText(mValues.get(position).getPoints());
        holder.user_namew.setText(mValues.get(position).getUsername());



        if(!TextUtils.isEmpty(mValues.get(position).getImageurl())) {
           /* Glide.with(holder.ic_user.getContext())
                    .load(mValues.get(position).getImageurl())
                    .placeholder(R.drawable.no_image)
                    .into(holder.ic_user);*/
            Utils.loadImageWithGlideSingleImageRounderCorner(context, mValues.get(position).getImageurl(), holder
                    .ic_user, R.drawable.no_image);

        }
        Log.v("url",mValues.get(position).getImageurl());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView user_namew;
        public final TextView user_points;
        public TopUsersModel mItem;
        ImageView  ic_user;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            user_namew = (TextView) view.findViewById(R.id.user_namew);
            user_points = (TextView) view.findViewById(R.id.user_points);
            ic_user      =(ImageView)view.findViewById(R.id.ic_user);
        }


    }
}
