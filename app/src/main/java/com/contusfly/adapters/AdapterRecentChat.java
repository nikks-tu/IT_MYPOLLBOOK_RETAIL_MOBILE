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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.contusfly.MApplication;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.RecentChat;
import com.contusfly.utils.ChatMsgTime;
import com.contusfly.utils.Constants;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomTextView;
import com.polls.polls.R;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class AdapterRecentChat.
 */
public class AdapterRecentChat extends BaseAdapter {

    /**
     * The image loader.
     */
    private RequestManager imageLoader;

    /**
     * The m temp data.
     */
    private List<RecentChat> recentChats, mTempData;

    /**
     * The m application.
     */
    private MApplication mApplication;

    /**
     * The context.
     */
    private Context context;

    private ChatMsgTime chatMsgTime;
    private LayoutInflater mInflater;
    private ViewHolder holder;

    /**
     * Instantiates a new adapter recent chat.
     *
     * @param context the context
     */
    public AdapterRecentChat(Context context) {
        this.context = context;
        imageLoader = Glide.with(context);
        mApplication = (MApplication) context.getApplicationContext();
        chatMsgTime = new ChatMsgTime();
    }

    /**
     * Sets the data.
     *
     * @param recentChats the new data
     */
    public void setData(List<RecentChat> recentChats) {
        this.mTempData = recentChats;
        this.recentChats = new ArrayList<>();
        this.recentChats.addAll(this.mTempData);
    }

    /**
     * Removes the item.
     *
     * @param position the position
     */
    public void removeItem(int position) {
        if (recentChats.size() > position)
            recentChats.remove(position);
    }

    /**
     * Filter.
     *
     * @param filterKey the filter key
     */
    public void filter(String filterKey) {
        mTempData.clear();
        if (TextUtils.isEmpty(filterKey)) {
            mTempData.addAll(recentChats);
        } else {
            for (RecentChat mKey : recentChats) {
                if (mKey.getRecentChatName().toLowerCase()
                        .contains(filterKey.toLowerCase()))
                    mTempData.add(mKey);
            }
        }
        notifyDataSetChanged();
    }


    /**
     * Return formated count.
     *
     * @param count the count
     *
     * @return the string
     */
    private String returnFormatedCount(String count) {
        if (TextUtils.isDigitsOnly(count)) {
            int newCount = Integer.parseInt(count);
            if (newCount > 99)
                return "99+";
            return String.valueOf(newCount);
        } else
            return count;
    }

    /**
     * Sets the image status.
     *
     * @param holder  the holder
     * @param msgType the msg type
     */
    private void setImageStatus(ViewHolder holder, String msgType) {
        if (Constants.MSG_TYPE_AUDIO.equalsIgnoreCase(msgType)) {
            holder.imgMsgType.setImageResource(R.drawable.ls_ic_record);
            holder.txtMsg.setText(context.getString(R.string.text_audio));
        } else if (Constants.MSG_TYPE_IMAGE.equalsIgnoreCase(msgType)) {
            holder.imgMsgType.setImageResource(R.drawable.ls_ic_camera);
            holder.txtMsg.setText(context.getString(R.string.txt_image));
        } else if (Constants.MSG_TYPE_VIDEO.equalsIgnoreCase(msgType)) {
            holder.imgMsgType.setImageResource(R.drawable.ls_ic_video);
            holder.txtMsg.setText(context.getString(R.string.text_video));
        } else if (Constants.MSG_TYPE_LOCATION.equalsIgnoreCase(msgType)) {
            holder.imgMsgType.setImageResource(R.drawable.ic_location);
            holder.txtMsg.setText(context.getString(R.string.txt_location));
        } else
            holder.imgMsgType.setVisibility(View.GONE);
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
        return mTempData.size();
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
            convertView = mInflater.inflate(R.layout.row_recent_chat_item, parent, false);
            //view holder
            holder = new ViewHolder();
            holder.txtName = (CustomTextView) convertView.findViewById(R.id.txt_chat_person);
            holder.txtUnSeenCount = (TextView) convertView
                    .findViewById(R.id.txt_unseen_count);
            holder.txtGroupPName = (TextView) convertView.findViewById(R.id.txt_group_person);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_recentchat_time);
            holder.txtMsg = (CustomTextView) convertView
                    .findViewById(R.id.txt_recent_chatmsg);
            holder.imgRoster = (ImageView) convertView.findViewById(R.id.img_contact);
            holder.imgMsgStatus = (ImageView) convertView
                    .findViewById(R.id.img_recent_chat_status);
            holder.imgMsgType = (ImageView) convertView
                    .findViewById(R.id.img_recent_chat_type);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        try {
            RecentChat item = mTempData.get(position);
            holder.txtUnSeenCount.setVisibility(View.GONE);
            holder.txtGroupPName.setVisibility(View.GONE);
            holder.imgMsgType.setVisibility(View.GONE);
            holder.imgMsgStatus.setVisibility(View.GONE);

            String rosterImage = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatImage());
            String rosterType = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatType());
            int errorImg = R.drawable.icon_profile;
            boolean isGroupChat = false, isBroadcast = false;
            if (rosterType.equalsIgnoreCase(String
                    .valueOf(Constants.CHAT_TYPE_GROUP))) {
                errorImg = R.drawable.icon_grp;
                isGroupChat = true;
            } else if (rosterType.equalsIgnoreCase(String
                    .valueOf(Constants.CHAT_TYPE_BROADCAST))) {
                errorImg = R.drawable.icon_bcast;
                isBroadcast = true;
            }
            Utils.loadImageWithGlideProfileRounderCorner(context, rosterImage,
                    holder.imgRoster, errorImg);

            String name = item.getRecentChatName().equals("") ? item.getRecentChatId() : item.getRecentChatName();
            Utils.setEmojiText(holder.txtName, Utils.getDecodedString(name));
            String msg = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatMsg());
            if (!msg.isEmpty())
                holder.txtMsg.setText(URLDecoder.decode(msg, "UTF-8"));

            String unSeenCount = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatUnread());
            if (!unSeenCount.isEmpty()
                    && !unSeenCount.equalsIgnoreCase(String
                    .valueOf(Constants.COUNT_ZERO))) {
                holder.txtUnSeenCount.setVisibility(View.VISIBLE);
                holder.txtUnSeenCount.setText(returnFormatedCount(unSeenCount));
            }

            String msgType = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatMsgType());
            if (!msgType.isEmpty()) {
                holder.imgMsgType.setVisibility(View.VISIBLE);
                setImageStatus(holder, msgType);
            }

            String msgStatus = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatStatus());
            String isSender = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatIsSender());
            if (!isSender.isEmpty()
                    && Integer.parseInt(isSender) == Constants.CHAT_FROM_SENDER) {
                holder.imgMsgStatus.setVisibility(View.VISIBLE);
                if (isGroupChat) {
                    String status = MDatabaseHelper
                            .getGrpLastMsgStatus(
                                    mApplication
                                            .getStringFromPreference(Constants.USERNAME),
                                    item.getRecentChatId());
                    if (status != null && !status.isEmpty())
                        Utils.setRecentChatStatus(holder.imgMsgStatus,
                                Integer.parseInt(status));
                } else if (isBroadcast) {
                    msgStatus = MDatabaseHelper.getLastMsgStatus(mApplication
                            .getStringFromPreference(Constants.USERNAME), item
                            .getRecentChatId());
                    if (!msgStatus.isEmpty())
                        Utils.setRecentChatStatus(holder.imgMsgStatus,
                                Integer.parseInt(msgStatus));
                } else {
                    if (!msgStatus.isEmpty())
                        Utils.setRecentChatStatus(holder.imgMsgStatus,
                                Integer.parseInt(msgStatus));
                }
            }

            String chatTime = MApplication.returnEmptyStringIfNull(item
                    .getRecentChatTime());
            if (!chatTime.isEmpty() && TextUtils.isDigitsOnly(chatTime))
                holder.txtTime.setText(chatMsgTime.getDaySentMsg(context,
                        Long.parseLong(chatTime)));
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
        return convertView;
    }

    /**
     * The Class ViewHolder.
     */
    public class ViewHolder {

        /**
         * The txt un seen count.
         */
        TextView txtGroupPName, txtTime, txtUnSeenCount;

        /**
         * The txt msg.
         */
        CustomTextView txtMsg, txtName;

        /**
         * The img roster.
         */
        ImageView imgRoster;

        /**
         * The img msg type.
         */
        ImageView imgMsgStatus, imgMsgType;


    }

}


