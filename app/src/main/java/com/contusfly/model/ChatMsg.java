/**
 * @category ContusMessanger
 * @package com.contusfly.model
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * The Class ChatMsg.
 */
public class ChatMsg implements Parcelable {


    /**
     * The msg id.
     */
    private String msgId;

    /**
     * The msg from.
     */
    private String msgFrom;

    /**
     * The msg to.
     */
    private String msgTo;

    /**
     * The msg time.
     */
    private String msgTime;

    /**
     * The msg status.
     */
    private String msgStatus;

    /**
     * The msg body.
     */
    private String msgBody;

    /**
     * The msg type.
     */
    private String msgType;

    /**
     * The msg video thumb.
     */
    private String msgVideoThumb;

    /**
     * The msg media size.
     */
    private String msgMediaSize;

    /**
     * The msg media is downloaded.
     */
    private String msgMediaIsDownloaded;

    /**
     * The msg media local path.
     */
    private String msgMediaLocalPath;

    /**
     * The msg media server path.
     */
    private String msgMediaServerPath;

    /**
     * The msg media enc image.
     */
    private String msgMediaEncImage;

    /**
     * The msg chat type.
     */
    private String msgChatType;

    /**
     * The is sender.
     */
    private int isSender, /**
     * The Is selected.
     */
    isSelected;

    /**
     * The File name.
     */
    private String fileName;

    /**
     * The chat to user.
     */
    private String chatToUser;

    /**
     * The chat receivers.
     */
    private String chatReceivers = "";

    /**
     * The Constant CREATOR.
     */
    public static final Creator<ChatMsg> CREATOR = new Creator<ChatMsg>() {
        @Override
        public ChatMsg createFromParcel(Parcel in) {
            String json = in.readString();
            return new Gson().fromJson(json, ChatMsg.class);
        }

        @Override
        public ChatMsg[] newArray(int size) {
            return new ChatMsg[size];
        }
    };

    /**
     * Gets the creator.
     *
     * @return the creator
     */
    public static Creator<ChatMsg> getCREATOR() {
        return CREATOR;
    }


    /**
     * Instantiates a new chat msg.
     */
    public ChatMsg() {
        //Constructor
    }


    /**
     * Gets the msg media server path.
     *
     * @return the msg media server path
     */
    public String getMsgMediaServerPath() {
        return msgMediaServerPath;
    }

    /**
     * Sets the msg media server path.
     *
     * @param msgMediaServerPath the new msg media server path
     */
    public void setMsgMediaServerPath(String msgMediaServerPath) {
        this.msgMediaServerPath = msgMediaServerPath;
    }

    /**
     * Gets the msg chat type.
     *
     * @return the msg chat type
     */
    public String getMsgChatType() {
        return msgChatType;
    }

    /**
     * Sets the msg chat type.
     *
     * @param msgChatType the new msg chat type
     */
    public void setMsgChatType(String msgChatType) {
        this.msgChatType = msgChatType;
    }

    /**
     * Gets the msg type.
     *
     * @return the msg type
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * Sets the msg type.
     *
     * @param msgType the new msg type
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * Gets the msg video thumb.
     *
     * @return the msg video thumb
     */
    public String getMsgVideoThumb() {
        return msgVideoThumb;
    }

    /**
     * Sets the msg video thumb.
     *
     * @param msgVideoThumb the new msg video thumb
     */
    public void setMsgVideoThumb(String msgVideoThumb) {
        this.msgVideoThumb = msgVideoThumb;
    }

    /**
     * Gets the msg media size.
     *
     * @return the msg media size
     */
    public String getMsgMediaSize() {
        return msgMediaSize;
    }

    /**
     * Sets the msg media size.
     *
     * @param msgMediaSize the new msg media size
     */
    public void setMsgMediaSize(String msgMediaSize) {
        this.msgMediaSize = msgMediaSize;
    }

    /**
     * Gets the msg media is downloaded.
     *
     * @return the msg media is downloaded
     */
    public String getMsgMediaIsDownloaded() {
        return msgMediaIsDownloaded;
    }

    /**
     * Sets the msg media is downloaded.
     *
     * @param msgMediaIsDownloaded the new msg media is downloaded
     */
    public void setMsgMediaIsDownloaded(String msgMediaIsDownloaded) {
        this.msgMediaIsDownloaded = msgMediaIsDownloaded;
    }

    /**
     * Gets the msg media local path.
     *
     * @return the msg media local path
     */
    public String getMsgMediaLocalPath() {
        return msgMediaLocalPath;
    }

    /**
     * Sets the msg media local path.
     *
     * @param msgMediaLocalPath the new msg media local path
     */
    public void setMsgMediaLocalPath(String msgMediaLocalPath) {
        this.msgMediaLocalPath = msgMediaLocalPath;
    }

    /**
     * Gets the msg media enc image.
     *
     * @return the msg media enc image
     */
    public String getMsgMediaEncImage() {
        return msgMediaEncImage;
    }

    /**
     * Sets the msg media enc image.
     *
     * @param msgMediaEncImage the new msg media enc image
     */
    public void setMsgMediaEncImage(String msgMediaEncImage) {
        this.msgMediaEncImage = msgMediaEncImage;
    }

    /**
     * Gets the sender.
     *
     * @return the sender
     */
    public int getSender() {
        return isSender;
    }

    /**
     * Sets the sender.
     *
     * @param isSender the new sender
     */
    public void setSender(int isSender) {
        this.isSender = isSender;
    }

    /**
     * Gets the msg id.
     *
     * @return the msg id
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * Sets the msg id.
     *
     * @param msgId the new msg id
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /**
     * Gets the msg from.
     *
     * @return the msg from
     */
    public String getMsgFrom() {
        return msgFrom;
    }

    /**
     * Sets the msg from.
     *
     * @param msgFrom the new msg from
     */
    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    /**
     * Gets the msg to.
     *
     * @return the msg to
     */
    public String getMsgTo() {
        return msgTo;
    }

    /**
     * Sets the msg to.
     *
     * @param msgTo the new msg to
     */
    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    /**
     * Gets the msg time.
     *
     * @return the msg time
     */
    public String getMsgTime() {
        return msgTime;
    }

    /**
     * Sets the msg time.
     *
     * @param msgTime the new msg time
     */
    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    /**
     * Gets the msg status.
     *
     * @return the msg status
     */
    public String getMsgStatus() {
        return msgStatus;
    }

    /**
     * Sets the msg status.
     *
     * @param msgStatus the new msg status
     */
    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    /**
     * Gets the msg body.
     *
     * @return the msg body
     */
    public String getMsgBody() {
        return msgBody;
    }

    /**
     * Sets the msg body.
     *
     * @param msgBody the new msg body
     */
    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    /**
     * Gets the chat to user.
     *
     * @return the chat to user
     */
    public String getChatToUser() {
        return chatToUser;
    }

    /**
     * Sets the chat to user.
     *
     * @param chatToUser the new chat to user
     */
    public void setChatToUser(String chatToUser) {
        this.chatToUser = chatToUser;
    }

    /**
     * Gets the chat receivers.
     *
     * @return the chat receivers
     */
    public String getChatReceivers() {
        return chatReceivers;
    }

    /**
     * Sets the chat receivers.
     *
     * @param chatReceivers the new chat to user
     */
    public void setChatReceivers(String chatReceivers) {
        this.chatReceivers = chatReceivers;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Gets the checks if is selected.
     *
     * @return the checks if is selected
     */
    public int getIsSelected() {
        return isSelected;
    }

    /**
     * Sets the checks if is selected.
     *
     * @param isSelected the new checks if is selected
     */
    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String json = new Gson().toJson(this);
        dest.writeString(json);
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets file name.
     *
     * @param fileName the file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
