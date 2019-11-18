package com.new_chanages.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contusfly.utils.Utils;
import com.new_chanages.holders.GroupNameRCVHolder;
import com.new_chanages.models.ContactDetailsModel;
import com.polls.polls.R;

import java.util.ArrayList;

public class GroupContactsAdapter extends RecyclerView.Adapter<GroupNameRCVHolder> {
    private ArrayList<ContactDetailsModel> arrayList;
    private Context context;
    private GroupNameRCVHolder listHolder;

    public GroupContactsAdapter(Context context, ArrayList<ContactDetailsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();
    }

    @Override
    public void onBindViewHolder(GroupNameRCVHolder holder, int position) {
        ContactDetailsModel model = arrayList.get(position);

        holder.tv_group_name.setText(model.getName());
        holder.tv_contact_num.setVisibility(View.VISIBLE);
        holder.tv_contact_num.setText(model.getMobile_number());
        holder.iv_arrow.setVisibility(View.GONE);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface faceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular.ttf");
        holder.tv_contact_num.setTypeface(face);
        holder.tv_group_name.setTypeface(faceRegular);
        String imageUrl = model.getProfile_image();
        if(imageUrl!=null && !imageUrl.equals(""))
        {
            Utils.loadImageWithGlideSingleImageRounderCorner(context, imageUrl, holder.iv_group_icon, R.drawable.img_ic_user);
        }
       //  holder.iv_group_icon.setImageBitmap(imageUrl);
    }

    @Override
    public GroupNameRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.groups_item_layout, viewGroup, false);

        listHolder = new GroupNameRCVHolder(mainGroup);
        return listHolder;

    }
}