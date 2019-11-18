package com.contusfly.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/12/2016.
 */
public class AdapterGroupInfo extends RecyclerView.Adapter<AdapterGroupInfo.ViewHolder> {

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

    /**
     * The listener.
     */
    private OnContactDeleteListener listener;
    private ArrayList<String> listGroupid;
    private ArrayList<String> mGroupName;
    private DatabaseHelper db;
    private List<String> test;

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
    public AdapterGroupInfo(Context context) {
        this.context = context;
        mApplication = (MApplication) context.getApplicationContext();
        listGroupid=new ArrayList<>();
        db=new DatabaseHelper(context);
        test=new ArrayList<>();
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
     * On create view holder.
     *
     * @param parent   the parent
     * @param viewType the view type
     * @return the adapter contacts. view holder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.row_group_info,
                parent, false));

    }

    @Override
    public void onBindViewHolder(AdapterGroupInfo.ViewHolder holder, final int position) {

            final Rosters item = mTempData.get(position);
            holder.txtInvite.setVisibility(View.GONE);
            holder.txtName.setText(MApplication.getDecodedString(item.getRosterName()));
        Utils.loadImageWithGlideProfileRounderCorner(context,item.getRosterImage(),
                holder.imgRoster, R.drawable.icon_grp);
            for(int i=0;db.getAllGroupDetails(1).size()>i;i++) {
                if(!test.contains(db.getAllGroupDetails(1).get(i).getId())) {
                    test.add(db.getAllGroupDetails(1).get(i).getId());
                    Log.e("test", test.toString() + "");
                }
            }
        Log.e("test", test.toString() + "");
            if (test.contains(item.getRosterID())) {
                //Getting the position of the particular value saved in prefernce
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }


    }

    /**
     * Checks if is contact selection.
     *
     * @param isSelection the is selection
     */
    public void isContactSelection(boolean isSelection) {
        this.isSelection = isSelection;
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
        if(mTempData!=null) {
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
    }

    public Rosters getItem(int position) {
        return rostersList.get(position);
    }

}
