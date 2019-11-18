package com.new_chanages.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.contusfly.views.CircularImageView;
import com.polls.polls.R;

public class ContactNameRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
// View holder for gridview recycler view as we used in listview
public CircularImageView iv_contact_pic;
ImageView iv_cancel;
public TextView tv_name;




public ContactNameRCVHolder(View view) {
        super(view);
        // Find all views ids
        this.iv_contact_pic =  view.findViewById(R.id.iv_contact_pic);
        this.iv_cancel =  view.findViewById(R.id.iv_cancel);
        this.tv_name =  view.findViewById(R.id.tv_name);
}

        @Override
        public void onClick(View v) {
                int position = getAdapterPosition();
        }
}