package com.new_chanages.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contusfly.utils.Utils;
import com.new_chanages.models.TopUsersModel;
import com.polls.polls.R;

import java.util.ArrayList;

/**
 * Created by kishore.i on 3/11/2018.
 */

public class TopTenAdapter extends RecyclerView.Adapter<TopTenAdapter.ViewHolder> {

    private final ArrayList<TopUsersModel> mValues;
    private Context context;

    public TopTenAdapter(ArrayList<TopUsersModel> items) {
        mValues = items;

    }

    @Override
    public TopTenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_ten_user_item, parent, false);
        context=parent.getContext();
        return new TopTenAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TopTenAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
       holder.user_name.setText(mValues.get(position).getUsername());
       holder.points_tv.setText(mValues.get(position).getPoints());

        Utils.loadImageWithGlideRounderCorner(context, mValues.get(position).getImageurl(), holder.user_image,
                R.drawable.placeholder_image);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        int images []={R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5,R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,R.drawable.bg9,R.drawable.bg10};

        holder.root_layout.setBackgroundResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TopUsersModel mItem;
        LinearLayout root_layout;
        TextView user_name,points_tv;
        ImageView user_image;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            root_layout  = view.findViewById(R.id.root_layout);
            user_name   = view.findViewById(R.id.user_name);
            points_tv    = view.findViewById(R.id.points_tv);
            user_image    = view  .findViewById(R.id.user_image);
        }


    }
}
