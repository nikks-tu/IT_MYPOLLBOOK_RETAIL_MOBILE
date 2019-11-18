/**
 * @category   ContusMessanger
 * @package    com.contusfly.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.model.GroupMsgStatus;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.ChatMsgTime;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.polls.polls.R;

import java.util.List;


/**
 * The Class AdapterMsgInfo.
 */
public class AdapterMsgInfo extends
        RecyclerView.Adapter<AdapterMsgInfo.ViewHolder> {

    /** The msg status. */
    private List<GroupMsgStatus> msgStatus;

    /** The context. */
    private Context context;

    /** The m application. */
    private MApplication mApplication;

    /** The is delivery. */
    private boolean isDelivery;

    private ChatMsgTime mChatMsgTime;

    /**
     * Instantiates a new adapter msg info.
     *
     * @param context the context
     */
    public AdapterMsgInfo(Context context) {
        this.context = context;
        mApplication = (MApplication) context.getApplicationContext();
        mChatMsgTime=new ChatMsgTime();
    }

    /**
     * Sets the data.
     *
     * @param msgStatus the msg status
     * @param isDelivery the is delivery
     */
    public void setData(List<GroupMsgStatus> msgStatus, boolean isDelivery) {
        this.msgStatus = msgStatus;
        this.isDelivery = isDelivery;
    }

    /**
     * On create view holder.
     *
     * @param parent the parent
     * @param viewType the view type
     * @return the view holder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.row_contact_item,
                parent, false));
    }

    /**
     * On bind view holder.
     *
     * @param holder the holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            GroupMsgStatus item = msgStatus.get(position);
            List<Rosters> rosterInfo = MDatabaseHelper.getRosterInfo(
                    item.getMsgSender(), Constants.EMPTY_STRING);
            Rosters roster = rosterInfo.get(0);
            String rosterImage = mApplication.returnEmptyStringIfNull(roster
                    .getRosterImage());
            int errorImg = R.drawable.icon_profile;
            if (!rosterImage.isEmpty()) {
                Utils.loadImageWithGlideProfileRounderCorner(context, rosterImage,
                        holder.imgRoster, errorImg);
            } else
                holder.imgRoster.setImageResource(errorImg);
            holder.txtName.setText(roster.getRosterName());
            String chatTime;
            if (isDelivery)
                chatTime = mApplication.returnEmptyStringIfNull(item
                        .getMsgDeliveryTime());
            else
                chatTime = mApplication.returnEmptyStringIfNull(item
                        .getMsgSeenTime());
            if (!chatTime.isEmpty() && TextUtils.isDigitsOnly(chatTime))
                holder.txtStatus.setText(mChatMsgTime.getDaySentMsg(context,Long.parseLong(chatTime)));
        } catch (Exception e) {
            LogMessage.e(Constants.TAG,e+"");
        }
    }

    /**
     * Gets the item count.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return msgStatus.size();
    }

    /**
     * The Class ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** The txt name. */
        TextView txtName;

        /** The txt status. */
        TextView txtStatus;

        /** The img roster. */
        ImageView imgRoster;

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
        }
    }
}
