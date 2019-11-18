package com.new_chanages.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.contusfly.utils.Utils;
import com.contusfly.views.CircularImageView;
import com.new_chanages.models.ContactDetailsModel;
import com.new_chanages.models.ContactModel;
import com.polls.polls.R;

import java.util.ArrayList;

public class GridAdapterEditGroupContacts extends ArrayAdapter<ContactDetailsModel> {
    private final int resource;
    private Context mContext;
    ArrayList<ContactDetailsModel> list;

    public GridAdapterEditGroupContacts(@NonNull Context context, int resource, @NonNull ArrayList<ContactDetailsModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.mContext = context;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ContactDetailsModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RecordHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new RecordHolder();
            holder.iv_contact_pic = row.findViewById(R.id.iv_contact_pic);
            holder.iv_cancel = row.findViewById(R.id.iv_cancel);
            holder.tv_name = row.findViewById(R.id.tv_name);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        ContactDetailsModel item = list.get(position);
        String s2;

        String imageUrl = item.getProfile_image();
        holder.tv_name.setText(item.getName());
        if(imageUrl!=null && !imageUrl.equals(""))
        {
            Utils.loadImageWithGlideSingleImageRounderCorner(mContext, imageUrl, holder.iv_contact_pic, R.drawable.img_ic_user);
        }

        return row;
    }

    static class RecordHolder {
        CircularImageView iv_contact_pic;
        ImageView iv_cancel;
        TextView tv_name;
    }

}
