package com.new_chanages.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.contusfly.views.CircularImageView;
import com.polls.polls.R;

public class GroupNameRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
// View holder for gridview recycler view as we used in listview
public TextView tv_group_name;
public TextView tv_contact_num;
public View view;
public CircularImageView iv_group_icon;
public ImageView iv_arrow;




public GroupNameRCVHolder(View view) {
        super(view);
        // Find all views ids
        this.tv_group_name =  view.findViewById(R.id.tv_group_name);
        this.iv_group_icon = view.findViewById(R.id.iv_group_icon);
        this.iv_arrow = view.findViewById(R.id.iv_arrow);
        this.tv_contact_num = view.findViewById(R.id.tv_contact_num);        }

        @Override
        public void onClick(View v) {
                int position = getAdapterPosition();
        }
}