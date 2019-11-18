/*
 * @category ContusMessanger
 * @package com.contusfly.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.new_chanages.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.new_chanages.models.ContactModel;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AdapterGroupUsers.
 */
public class AddGroupContactsAdapter extends BaseAdapter {

    /** The context. */
    private Context context;

    /** The inflater. */
    private LayoutInflater inflater;

    /** The rosters list. */
    private List<ContactModel> contactList;

    /** The holder. */
    private ViewHolder holder;

    /** The m application. */
    private MApplication mApplication;

    /** The admin user. */
    private String userName, adminUser;

    int selectedPostion = -1;

    /**
     * Instantiates a new adapter group users.
     * @param context the context
     */
    public AddGroupContactsAdapter(Context context, ArrayList<ContactModel> contactList) {
        this.context = context;
        this.contactList = contactList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mApplication = (MApplication) context.getApplicationContext();
        userName = mApplication.getStringFromPreference(Constants.USERNAME);
    }


    public void setData(ArrayList<ContactModel> contactList) {
        Log.e("rostersList",contactList.size()+"");
        this.contactList = contactList;
    }
    /**
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return contactList.size();
    }
    /**
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public ContactModel getItem(int position) {
        return contactList.get(position);
    }
    /**
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        try {
            holder = null;
            final ContactModel item = getItem(position);
            Log.e("item","item");
            Log.e("item",item.getContactName()+"");
            Log.e("id",item.getContactNumber()+"");
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.row_add_group_contact, null);
                holder.txt_number =  view.findViewById(R.id.txt_number);
                holder.txtStatus =  view.findViewById(R.id.txt_user_status);
                holder.iv_profile =  view.findViewById(R.id.iv_profile);
                holder.checkbox_user =  view.findViewById(R.id.checkbox_user);
                holder.tv_add =  view.findViewById(R.id.tv_add);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
                holder.checkbox_user.setOnCheckedChangeListener(null);
            }
            selectedPostion = position;

            holder.txt_number.setText(item.getContactName());
            holder.checkbox_user.setFocusable(false);
            if(!item.getContactPic().equals(""))
            {
                Utils.loadImageWithGlide(mApplication, contactList.get(position).getContactPic().replaceAll(" ", "%20"), holder.iv_profile, R.drawable.img_ic_user);
            }

            if(item.getContactSelected().equals("false"))
            {
                holder.checkbox_user.setChecked(false);
            }
            else {
                holder.checkbox_user.setChecked(true);
            }
            // holder.checkbox_user.setChecked(false);
            holder.tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.tv_add.getText().equals("ADD"))
                    {
                        holder.tv_add.setText("REMOVE");
                    }
                    else {
                        holder.tv_add.setText("ADD");
                           }
                }
            });

            //holder.checkbox_user.setChecked(false);
            holder.checkbox_user.setEnabled(false);

          /*  holder.checkbox_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!buttonView.isChecked())
                    {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        dbHelper.deleteContactFromList(contactList.get(position).getContactNumber());
                        contactList.get(position).setContactSelected("false");
                        //notifyDataSetChanged();
                        *//* if(item.getContactNumber().equals(contactList.get(position).getContactNumber()))
                        {
                            holder.checkbox_user.setChecked(false);
                        }
                        *//*
                    }
                    else {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        dbHelper.addContactToList(contactList.get(position).getContactName(), contactList.get(position).getContactNumber(), "true",
                                contactList.get(position).getContactPic(),contactList.get(position).getContactMPBName());
                        contactList.get(position).setContactSelected("true");
                        //notifyDataSetChanged();
                       *//* if(item.getContactNumber().equals(contactList.get(position).getContactNumber()))
                        {
                            holder.checkbox_user.setChecked(true);
                        }*//*
                    }
                }
            });*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    /**
     * The Class ViewHolder.
     */
    private class ViewHolder {
        /** The txt admin. */
        TextView txt_number;
        /** The txt status. */
        TextView txtStatus;
        /** The img roster. */
        ImageView iv_profile;
        TextView tv_add;
        CheckBox checkbox_user;
    }


}
