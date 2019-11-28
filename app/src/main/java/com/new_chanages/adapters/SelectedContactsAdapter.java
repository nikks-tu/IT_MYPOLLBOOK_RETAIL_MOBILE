package com.new_chanages.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contusfly.model.MDatabaseHelper;
import com.contusfly.utils.Utils;
import com.contusfly.views.CircularImageView;
import com.new_chanages.SendEvent;
import com.new_chanages.models.ContactModel;
import com.polls.polls.R;

import java.util.ArrayList;

public class SelectedContactsAdapter extends RecyclerView.Adapter<SelectedContactsAdapter.ViewHolder> {
    private ArrayList<ContactModel> arrayList;
    SendEvent event;
    private Context context;
    MDatabaseHelper dbHelper;


    public SelectedContactsAdapter(SendEvent event,Context context, ArrayList<ContactModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.event=event;
        dbHelper = new MDatabaseHelper(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_selected_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ContactModel model = arrayList.get(position);

        String imageUrl = model.getContactPic();

        holder.tv_name.setText(model.getContactName());
        if (imageUrl != null && !imageUrl.equals("")) {
            Utils.loadImageWithGlideSingleImageRounderCorner(context, imageUrl, holder.iv_contact_pic, R.drawable.img_ic_user);
        }

        holder.iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  String ph=arrayList.get(position).getContactNumber();
                arrayList.remove(position);
                notifyDataSetChanged();
                  event.update(ph, 1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


   /* @Override
    public int getItemCount() {
        // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();
    }

    @Override
    public void onBindViewHolder(ContactNameRCVHolder holder, int position) {
        ContactModel model = arrayList.get(position);

        String imageUrl = model.getContactPic();

        holder.tv_name.setText(model.getContactName());
        if (imageUrl != null && !imageUrl.equals("")) {
            Utils.loadImageWithGlideSingleImageRounderCorner(context, imageUrl, holder.iv_contact_pic, R.drawable.img_ic_user);
        }

*/



    class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView iv_contact_pic;
        ImageView iv_cancel;
        public TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.iv_contact_pic =  itemView.findViewById(R.id.iv_contact_pic);
            this.iv_cancel =  itemView.findViewById(R.id.iv_cancel);
            this.tv_name =  itemView.findViewById(R.id.tv_name);
        }
    }




}