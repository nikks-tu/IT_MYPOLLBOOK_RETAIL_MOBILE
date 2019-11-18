/**
 * @category ContusMessanger
 * @package com.contusfly.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class AdapterContacts1 extends
        RecyclerView.Adapter<AdapterContacts1.ViewHolder> {

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

    /**
     * The listener interface for receiving onContactDelete events. The class
     * that is interested in processing a onContactDelete event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addOnContactDeleteListener<code> method. When
     * the onContactDelete event occurs, that object's appropriate
     * method is invoked.
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
    public AdapterContacts1(Context context) {
        this.context = context;
        mApplication = (MApplication) context.getApplicationContext();
    }

    /**
     * Sets the data.
     *
     * @param rostersList the new data
     */
    public void setData(List<Rosters> rostersList) {
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
     * On create view holder.
     *
     * @param parent   the parent
     * @param viewType the view type
     * @return the adapter contacts. view holder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.row_contact_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("roaster","roaster");
        Log.e("roaster",rostersList.get(position).getRosterName());
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
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
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

                    holder.imgSelection.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }


    /**
     * Checks if is contact selection.
     */
    public void setIsMultiSelect(boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
    }



    /**
     * Gets the item count.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return mTempData.size();
    }

    /**
     * The Class ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

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

        /**
         * Instantiates a new view holder.
         *
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_chat_person);
            txtStatus = (TextView) view
                    .findViewById(R.id.txt_user_status);
            imgRoster = (ImageView) view.findViewById(R.id.img_contact);
            txtInvite = (TextView) view.findViewById(R.id.txt_invite);
            imgSelection = (ImageView) view.findViewById(R.id.img_tick);
            checkBox = (RadioButton) view.findViewById(R.id.check_selection);
        }
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

    public Rosters getItem(int position) {
        Log.e("position",position+"");
        return rostersList.get(position);
    }

}
