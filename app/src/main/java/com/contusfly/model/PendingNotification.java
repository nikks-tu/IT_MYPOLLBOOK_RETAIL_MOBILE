/**
 * @category   ContusMessanger
 * @package    com.contusfly.model
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.model;

/**
 * The Class PendingNotification.
 */
public class PendingNotification {

    /** The pending chat type. */
    private String pendingChatId, pendingChatName, pendingChatTime,
            pendingChatMsg, pendingChatImg, pendingChatType;

    /**
     * Gets the pending chat id.
     *
     * @return the pending chat id
     */
    public String getPendingChatId() {
        return pendingChatId;
    }

    /**
     * Sets the pending chat id.
     *
     * @param pendingChatId
     *            the new pending chat id
     */
    public void setPendingChatId(String pendingChatId) {
        this.pendingChatId = pendingChatId;
    }

    /**
     * Gets the pending chat name.
     *
     * @return the pending chat name
     */
    public String getPendingChatName() {
        return pendingChatName;
    }

    /**
     * Gets the pending chat time.
     *
     * @return the pending chat time
     */
    public String getPendingChatTime() {
        return pendingChatTime;
    }

    /**
     * Sets the pending chat time.
     *
     * @param pendingChatTime
     *            the new pending chat time
     */
    public void setPendingChatTime(String pendingChatTime) {
        this.pendingChatTime = pendingChatTime;
    }

    /**
     * Sets the pending chat name.
     *
     * @param pendingChatName
     *            the new pending chat name
     */
    public void setPendingChatName(String pendingChatName) {
        this.pendingChatName = pendingChatName;
    }

    /**
     * Gets the pending chat msg.
     *
     * @return the pending chat msg
     */
    public String getPendingChatMsg() {
        return pendingChatMsg;
    }

    /**
     * Sets the pending chat msg.
     *
     * @param pendingChatMsg
     *            the new pending chat msg
     */
    public void setPendingChatMsg(String pendingChatMsg) {
        this.pendingChatMsg = pendingChatMsg;
    }

    /**
     * Gets the pending chat img.
     *
     * @return the pending chat img
     */
    public String getPendingChatImg() {
        return pendingChatImg;
    }

    /**
     * Sets the pending chat img.
     *
     * @param pendingChatImg
     *            the new pending chat img
     */
    public void setPendingChatImg(String pendingChatImg) {
        this.pendingChatImg = pendingChatImg;
    }

    /**
     * Gets the pending chat type.
     *
     * @return the pending chat type
     */
    public String getPendingChatType() {
        return pendingChatType;
    }

    /**
     * Sets the pending chat type.
     *
     * @param pendingChatType
     *            the new pending chat type
     */
    public void setPendingChatType(String pendingChatType) {
        this.pendingChatType = pendingChatType;
    }
}
