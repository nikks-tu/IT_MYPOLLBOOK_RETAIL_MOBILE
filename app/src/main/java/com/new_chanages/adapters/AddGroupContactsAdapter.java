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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.google.gson.JsonElement;
import com.new_chanages.SendEvent;
import com.new_chanages.activity.AllContactsActivity;
import com.new_chanages.models.ContactModel;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import retrofit2.Callback;

/**
 * The Class AdapterGroupUsers.
 */
public class AddGroupContactsAdapter extends BaseAdapter {

    /** The context. */
    private Context context;


    ArrayList<ContactModel> arrayList ;
    private LayoutInflater inflater;

    /** The rosters list. */
    private List<ContactModel> contactList;
    MDatabaseHelper dbHelper;
    /** The holder. */
    private ViewHolder holder;

    /** The m application. */
    private MApplication mApplication;

    /** The admin user. */
    private String userName, adminUser;

    int selectedPostion = -1;
    SendEvent event;
    TextView norecords_tv;

    /**
     * Instantiates a new adapter group users.
     * @param context the context
     * @param norecords_tv
     */
    public AddGroupContactsAdapter(SendEvent event, Context context, ArrayList<ContactModel> contactList, TextView norecords_tv) {
        this.context = context;
        this.contactList = contactList;
        this.arrayList = new ArrayList<ContactModel>();
        arrayList.addAll(contactList);
        this.event=event;
        this.norecords_tv=norecords_tv;
          dbHelper   = new MDatabaseHelper(context);



        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mApplication = (MApplication) context.getApplicationContext();
        userName = mApplication.getStringFromPreference(Constants.USERNAME);
    }




    public void setData(ArrayList<ContactModel> contactList) {
        Log.e("rostersList",contactList.size()+"");
        this.contactList = contactList;

    }


    @Override
    public int getCount() {
        return contactList.size();
    }


    @Override
    public ContactModel getItem(int position) {
        return contactList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {


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
                holder.root=view.findViewById(R.id.root);
                holder.txtStatus =  view.findViewById(R.id.txt_user_status);
                holder.iv_profile =  view.findViewById(R.id.iv_profile);
                holder.checkbox_user =  view.findViewById(R.id.checkbox_user);
                holder.tv_add =  view.findViewById(R.id.tv_add);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
               // holder.checkbox_user.setOnCheckedChangeListener(null);
            }



            selectedPostion = position;

           /* if(item.getIsContactSelected().equalsIgnoreCase("1"))
            {
                holder.checkbox_user.setChecked(true);
            }
            else
            {
                holder.checkbox_user.setChecked(false);
            }
*/
            holder.txt_number.setText(item.getContactName());

            if(!item.getContactPic().equals(""))
            {
                Utils.loadImageWithGlide(mApplication, contactList.get(position).getContactPic().replaceAll(" ", "%20"), holder.iv_profile, R.drawable.img_ic_user);
            }





            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(contactList.get(position).getIsContactSelected().equals("TRUE")){
                        contactList.get(position).setIsContactSelected("FALSE");
                        dbHelper.deleteContactFromSelected(contactList.get(position).getContactNumber());

                    }
                    else{
                        contactList.get(position).setIsContactSelected("TRUE");

                        dbHelper.addContactToSelected(contactList.get(position).getContactName(),
                                contactList.get(position).getContactNumber(), "true",
                                contactList.get(position).getContactPic(),
                                contactList.get(position).getContactMPBName(),"true");
                    }


                 notifyDataSetChanged();
                    event.update(contactList.get(position).getContactNumber(),0);

                }
            });

            if(contactList.get(position).getIsContactSelected().equals("TRUE")){
                holder.checkbox_user.setChecked(true);
            }else {
                holder.checkbox_user.setChecked(false);

            }






           /* if(item.getContactSelected().equals("false"))
            {
                holder.checkbox_user.setChecked(false);
            }
            else {
                holder.checkbox_user.setChecked(true);
            }*/


            // holder.checkbox_user.setChecked(false);
    /*        holder.tv_add.setOnClickListener(new View.OnClickListener() {
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
*/
            //holder.checkbox_user.setChecked(false);
         //   holder.checkbox_user.setEnabled(false);

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

    // Search Contact
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());

        contactList.clear();
        if (charText.length()==0){
            contactList.addAll(arrayList);
        }
        else {
            for (ContactModel model : arrayList){
                if (model.getContactName().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    contactList.add(model);
                }
            }
        }
        if(contactList.size()>0)
        {
            norecords_tv.setVisibility(View.GONE);
        }
        else {
            norecords_tv.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
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
        LinearLayout root;
        CheckBox checkbox_user;
    }




}
