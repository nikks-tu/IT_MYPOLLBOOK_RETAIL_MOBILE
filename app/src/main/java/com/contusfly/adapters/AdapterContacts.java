/**
 * @category ContusMessanger
 * @package com.contusfly.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AdapterContacts.
 */
public class AdapterContacts extends BaseAdapter {

    /**
     * The is selection.
     */
    private boolean isMultiSelect;
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

    /**
     * The listener.
     */
    private OnContactDeleteListener listener;
    private LayoutInflater mInflater;
    private ViewHolder holder;

    /**
     * The listener interface for receiving onContactDelete events. The class that is interested in
     * processing a onContactDelete event implements this interface, and the object created with
     * that class is registered with a component using the component's
     * <code>addOnContactDeleteListener<code> method. When the onContactDelete event occurs, that
     * object's appropriate method is invoked.
     */
    public interface OnContactDeleteListener {

        /**
         * On delete clicked.
         *
         * @param position the position
         */
        void onDeleteClicked(int position);
    }


    /**
     * Instantiates a new adapter contacts.
     *
     * @param context the context
     */
    public AdapterContacts(Context context) {
        this.context = context;
        mApplication = (MApplication) context.getApplicationContext();
    }

    /**
     * Sets the data.
     *
     * @param rostersList the new data
     */
    public void setData(List<Rosters> rostersList) {
        if (mTempData != null)
            mTempData.clear();
        this.mTempData = rostersList;
        this.rostersList = new ArrayList<>();
        this.rostersList.addAll(this.mTempData);
    }

    /**
     * Sets the on delete click listener.
     *
     * @param listener the new on delete click listener
     */
    public void setOnDeleteClickListener(OnContactDeleteListener listener) {
        this.listener = listener;
    }


    /**
     * Checks if is contact selection.
     */
    public void setIsMultiSelect(boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
    }


    /**
     * The Class ViewHolder.
     */
    public class ViewHolder {

        /**
         * The txt invite.
         */
        TextView txtName, txtInvite;

        /**
         * The txt status.
         */
        TextView txtStatus;

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

    @Override
    public int getCount() {
        return mTempData.size();
    }

    public Rosters getItem(int position) {
        return mTempData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (convertView == null) {
               /* create a new view of my layout and inflate it in the row */
            convertView = mInflater.inflate(R.layout.row_contact_item, parent, false);
            //view holder
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_chat_person);
            holder.txtStatus = (TextView) convertView
                    .findViewById(R.id.txt_user_status);
            holder.imgRoster = (ImageView) convertView.findViewById(R.id.img_contact);
            holder.txtInvite = (TextView) convertView.findViewById(R.id.txt_invite);
            holder.imgSelection = (ImageView) convertView.findViewById(R.id.img_tick);
            holder.checkBox = (RadioButton) convertView.findViewById(R.id.check_selection);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        try {
            final Rosters item = mTempData.get(position);
            holder.txtInvite.setVisibility(View.GONE);
            holder.txtStatus.setVisibility(View.VISIBLE);
            holder.txtName.setText(Utils.getDecodedString(item.getRosterName()));
            String rosterStatus = MApplication.returnEmptyStringIfNull(item.getRosterStatus());
            String rosterAvailability = MApplication.returnEmptyStringIfNull(item.getRosterAvailability());
            Utils.loadImageWithGlideProfileRounderCorner(context, item.getRosterImage(), holder.imgRoster, R.drawable.icon_profile);
            if (!rosterStatus.isEmpty())
                holder.txtStatus.setText(MApplication.getDecodedString(rosterStatus));
            else
                holder.txtStatus.setVisibility(View.GONE);
            if (isMultiSelect) {
                Log.e("check", isMultiSelect + "");
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        Log.e("click", "click");
                        if (isChecked)
                            item.setIsSelected(Constants.COUNT_ONE);
                        else
                            item.setIsSelected(Constants.COUNT_ZERO);
                    }
                });
                holder.checkBox.setChecked(Constants.COUNT_ONE == item.getIsSelected());
            } else {
                if (rosterAvailability.isEmpty()
                        || rosterAvailability.equalsIgnoreCase(String.valueOf(Constants.ROSTER_NOT_ACTIVE))) {
                    holder.txtInvite.setVisibility(View.VISIBLE);
                } else {
                    Log.e("roaster", rostersList.get(position).getRosterName());
                    holder.imgSelection.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
        return convertView;
    }


}
