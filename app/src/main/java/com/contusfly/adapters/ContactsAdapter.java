package com.contusfly.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomTextView;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 2/3/16.
 */
public class ContactsAdapter extends BaseAdapter {

    /**
     * The is selection.
     */
    private boolean isSelection, isMultiSelect;

    /**
     * The m application.
     */
    private MApplication mApplication;

    /**
     * The rosters list.
     */
    private List<Rosters> rostersList;

    /**
     * The m temp data.
     */
    private List<Rosters> mTempData;

    /**
     * The context.
     */
    private Context context;



    public static final int Normal = 2;
    public static final int Footer = 3;
    private ViewHolder holder;
    private View mAdapterView;
    private String rosterStatus;
    private String rosterAvailability;

    /**
     * Instantiates a new adapter contacts.
     *
     * @param context the context
     */
    public ContactsAdapter(Context context) {
        this.context = context;
        mApplication = (MApplication) context.getApplicationContext();
    }
    /**
     * Sets the data.
     *
     * @param rostersList the new data
     */
    public void setData(List<Rosters> rostersList) {
        if (mTempData != null) {
            mTempData.clear();
        }
            this.mTempData = rostersList;
            this.rostersList = new ArrayList<>();
            this.rostersList.addAll(this.mTempData);

    }


    @Override
    public int getCount() {
        return mTempData.size();
    }

    @Override
    public Object getItem(int position) {
        return mTempData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View mView, ViewGroup parent) {
            Rosters item = mTempData.get(position);
        Log.e("item",mTempData.get(0).getRosterImage());
            holder = null;
            //getView() method to be able to inflate our custom layout & create a View class out of it,
            // we need an instance of LayoutInflater class
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            mAdapterView = mView;
            // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
            // It will happen when a new row appear and a old row in the other end roll out.
            if (mAdapterView == null) {
            /* create a new view of my layout and inflate it in the row */
                mAdapterView = mInflater.inflate(R.layout.row_contact_item, null);
                holder = new ViewHolder();
                holder.txtName = (TextView) mAdapterView.findViewById(R.id.txt_chat_person);
                holder.txtStatus = (CustomTextView) mAdapterView
                        .findViewById(R.id.txt_user_status);
                holder.imgRoster = (ImageView) mAdapterView.findViewById(R.id.img_contact);
                holder.txtInvite = (TextView) mAdapterView.findViewById(R.id.txt_invite);
                holder.imgSelection = (ImageView) mAdapterView.findViewById(R.id.img_tick);
                holder.checkBox = (RadioButton) mAdapterView.findViewById(R.id.check_selection);
                mAdapterView.setTag(holder);
            } else {
                holder = (ViewHolder) mAdapterView.getTag();
            }
            rosterStatus = mApplication.returnEmptyStringIfNull(item
                    .getRosterStatus());
            rosterAvailability = mApplication.returnEmptyStringIfNull(item
                    .getRosterAvailability());
            holder.txtInvite.setVisibility(View.GONE);
            holder.txtName.setText(item.getRosterName());
            if (rosterAvailability.isEmpty() || rosterAvailability.equalsIgnoreCase(String.valueOf(Constants.ROSTER_NOT_ACTIVE))) {
                holder.txtInvite.setVisibility(View.VISIBLE);
                holder.txtStatus.setText("");

            } else {
                if (!rosterStatus.isEmpty()) {
                    holder.txtStatus.setText(MApplication.getDecodedString(rosterStatus));
                }
            }
            Utils.loadImageWithGlideProfileRounderCorner(context, item.getRosterImage(),
                    holder.imgRoster, R.drawable.icon_profile);
            //Return views
            return mAdapterView;

    }
    /**
     * Filter.
     *
     * @param filterKey the filter key
     */
    public void filter(String filterKey) {
        mTempData.clear();
        if (TextUtils.isEmpty(filterKey)) {
            mTempData.addAll(rostersList);
        } else {
            for (Rosters mKey : rostersList) {
                if (mKey.getRosterName().toLowerCase()
                        .contains(filterKey.toLowerCase()))
                    mTempData.add(mKey);
            }
        }
    }
    /**
     * The Class ViewHolder.
     */
    public class ViewHolder  {

        /**
         * The txt invite.
         */
        TextView txtName, txtInvite;

        /**
         * The txt status.
         */
        CustomTextView txtStatus;

        /**
         * The img roster.
         */
        ImageView imgRoster;

        /**
         * The img selection.
         */
        ImageView imgSelection;

        /**
         * The check box.
         */
        RadioButton checkBox;

    }

}
