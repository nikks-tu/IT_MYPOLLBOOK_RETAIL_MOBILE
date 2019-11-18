package com.contusfly.model;

/**
 * Created by user on 11/17/2015.
 */
public class GroupMsgStatus {

    private String msgId, msgSender, msgStatus, msgDeliveryTime, msgSeenTime,groupId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgSeenTime() {
        return msgSeenTime;
    }

    public void setMsgSeenTime(String msgSeenTime) {
        this.msgSeenTime = msgSeenTime;
    }

    public String getMsgDeliveryTime() {
        return msgDeliveryTime;
    }

    public void setMsgDeliveryTime(String msgDeliveryTime) {
        this.msgDeliveryTime = msgDeliveryTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }
}
