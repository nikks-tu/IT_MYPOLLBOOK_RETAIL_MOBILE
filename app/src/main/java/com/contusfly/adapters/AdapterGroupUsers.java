/*
 * @category ContusMessanger
 * @package com.contusfly.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.List;

/*
 * The Class AdapterGroupUsers.
 */
public class AdapterGroupUsers extends BaseAdapter {

    /* The context. */
    private Context context;

    /* The inflater. */
    private LayoutInflater inflater;

    /* The rosters list. */
    private List<Rosters> rostersList;

    /* The holder. */
    private ViewHolder holder;

    /* The m application. */
    private MApplication mApplication;

    /* The admin user. */
    private String userName, adminUser;

    /*
     * Instantiates a new adapter group users.
     *
     * @param context
     *            the context
     */
    public AdapterGroupUsers(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mApplication = (MApplication) context.getApplicationContext();
        userName = mApplication.getStringFromPreference(Constants.USERNAME);
    }

    /*
     * Sets the data.
     * @param rostersList the rosters list
     * @param adminUser the admin user
     */
    public void setData(List<Rosters> rostersList, String adminUser) {
        Log.e("rostersList",rostersList.size()+"");
        this.rostersList = rostersList;
        this.adminUser = adminUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return rostersList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Rosters getItem(int position) {
        return rostersList.get(position);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        try {
            holder = null;
            Rosters item = getItem(position);
            Log.e("item","item");
            Log.e("item",item.getRosterName()+"");
            Log.e("id",item.getRosterID()+"");
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.row_group_users, null);
                holder.txtName = (TextView) view
                        .findViewById(R.id.txt_group_person);
                holder.txtStatus = (TextView) view
                        .findViewById(R.id.txt_user_status);
                holder.imgRoster = (ImageView) view
                        .findViewById(R.id.img_contact);
                holder.txtAdmin = (TextView) view.findViewById(R.id.txt_admin);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.txtAdmin.setVisibility(View.GONE);
            String rosterStatus = mApplication.returnEmptyStringIfNull(item
                    .getRosterStatus());
          Utils.loadImageWithGlideProfileRounderCorner(context, item.getRosterImage(),
                    holder.imgRoster, R.drawable.icon_profile);
            if (!rosterStatus.isEmpty())
                holder.txtStatus.setText(MApplication.getDecodedString(rosterStatus));
            else
                holder.txtStatus.setText(context.getResources().getString(R.string.default_status));
            if (adminUser.contains(item.getRosterID())) {
                holder.txtAdmin.setVisibility(View.VISIBLE);
            }
            if (item.getRosterID().equalsIgnoreCase(userName)) {
                holder.txtName.setText(context.getString(R.string.text_you));
            } else {
                if(!item.getRosterName().equals("")) {
                    holder.txtName.setText(item.getRosterName());
                }else{
                    holder.txtName.setText(item.getRosterID());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    /*
     * The Class ViewHolder.
     */
    private class ViewHolder {
        /** The txt admin. */
        TextView txtName, txtAdmin;

        /** The txt status. */
        TextView txtStatus;

        /** The img roster. */
        ImageView imgRoster;
    }
}
