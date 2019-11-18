package com.new_chanages.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.contusfly.utils.Utils;
import com.new_chanages.holders.ContactNameRCVHolder;
import com.new_chanages.holders.GroupNameRCVHolder;
import com.new_chanages.models.ContactDetailsModel;
import com.new_chanages.models.ContactModel;
import com.polls.polls.R;

import java.util.ArrayList;

public class SelectedContactsAdapter extends RecyclerView.Adapter<ContactNameRCVHolder> {
    private ArrayList<ContactModel> arrayList;
    private Context context;
    private ContactNameRCVHolder listHolder;

    public SelectedContactsAdapter(Context context, ArrayList<ContactModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();
    }

    @Override
    public void onBindViewHolder(ContactNameRCVHolder holder, int position) {
        ContactModel model = arrayList.get(position);

        String imageUrl = model.getContactPic();
        holder.tv_name.setText(model.getContactName());
        if(imageUrl!=null && !imageUrl.equals(""))
        {
            Utils.loadImageWithGlideSingleImageRounderCorner(context, imageUrl, holder.iv_contact_pic, R.drawable.img_ic_user);
        }
    }

    @Override
    public ContactNameRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_selected_contact, viewGroup, false);

        listHolder = new ContactNameRCVHolder(mainGroup);
        return listHolder;

    }
}