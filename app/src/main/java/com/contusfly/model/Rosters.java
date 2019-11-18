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


/**
 * The Class Rosters.
 */
public class Rosters implements Parcelable {

    /**
     * The roster custom tone.
     */
    private String rosterName, rosterStatus, rosterImage, rosterAvailability,
            rosterID, rosterType, rosterAdmin, rosterGroupUsers,
            rosterIsBlocked, rosterGroupStatus, rosterLastSeen;

    /**
     * The is selected.
     */
    private int isSelected;

    /**
     * The Constant CREATOR.
     */
    public static final Creator<Rosters> CREATOR = new Creator<Rosters>() {
        @Override
        public Rosters createFromParcel(Parcel in) {
            return new Rosters(in);
        }

        @Override
        public Rosters[] newArray(int size) {
            return new Rosters[size];
        }
    };

    /**
     * Instantiates a new rosters.
     */
    public Rosters() {
    }

    /**
     * Instantiates a new rosters.
     *
     * @param in the in
     */
    public Rosters(Parcel in) {
        rosterID = in.readString();
        rosterName = in.readString();
        rosterStatus = in.readString();
        rosterImage = in.readString();
        rosterAvailability = in.readString();
        rosterType = in.readString();
        isSelected = in.readInt();
        rosterAdmin = in.readString();
        rosterLastSeen = in.readString();
    }

    /**
     * Sets the roster is blocked.
     *
     * @param rosterIsBlocked the new roster is blocked
     */
    public void setRosterIsBlocked(String rosterIsBlocked) {
        this.rosterIsBlocked = rosterIsBlocked;
    }

    /**
     * Gets the roster group status.
     *
     * @return the roster group status
     */
    public String getRosterGroupStatus() {
        return rosterGroupStatus;
    }

    /**
     * Sets the roster group status.
     *
     * @param rosterGroupStatus the new roster group status
     */
    public void setRosterGroupStatus(String rosterGroupStatus) {
        this.rosterGroupStatus = rosterGroupStatus;
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

    /**
     * Gets the roster admin.
     *
     * @return the roster admin
     */
    public String getRosterAdmin() {
        return rosterAdmin;
    }

    /**
     * Sets the roster admin.
     *
     * @param rosterAdmin the new roster admin
     */
    public void setRosterAdmin(String rosterAdmin) {
        this.rosterAdmin = rosterAdmin;
    }

    /**
     * Gets the roster group users.
     *
     * @return the roster group users
     */
    public String getRosterGroupUsers() {
        return rosterGroupUsers;
    }

    /**
     * Sets the roster group users.
     *
     * @param rosterGroupUsers the new roster group users
     */
    public void setRosterGroupUsers(String rosterGroupUsers) {
        this.rosterGroupUsers = rosterGroupUsers;
    }

    /**
     * Gets the roster id.
     *
     * @return the roster id
     */
    public String getRosterID() {
        return rosterID;
    }

    /**
     * Sets the roster id.
     *
     * @param rosterID the new roster id
     */
    public void setRosterID(String rosterID) {
        this.rosterID = rosterID;
    }

    /**
     * Gets the creator.
     *
     * @return the creator
     */
    public static Creator<Rosters> getCREATOR() {
        return CREATOR;
    }

    /**
     * Gets the roster name.
     *
     * @return the roster name
     */
    public String getRosterName() {
        return rosterName;
    }

    /**
     * Sets the roster name.
     *
     * @param rosterName the new roster name
     */
    public void setRosterName(String rosterName) {
        this.rosterName = rosterName;
    }

    /**
     * Gets the roster status.
     *
     * @return the roster status
     */
    public String getRosterStatus() {
        return rosterStatus;
    }

    /**
     * Sets the roster status.
     *
     * @param rosterStatus the new roster status
     */
    public void setRosterStatus(String rosterStatus) {
        this.rosterStatus = rosterStatus;
    }

    /**
     * Gets the roster image.
     *
     * @return the roster image
     */
    public String getRosterImage() {
        return rosterImage;
    }

    /**
     * Sets the roster image.
     *
     * @param rosterImage the new roster image
     */
    public void setRosterImage(String rosterImage) {
        this.rosterImage = rosterImage;
    }

    /**
     * Gets the roster availability.
     *
     * @return the roster availability
     */
    public String getRosterAvailability() {
        return rosterAvailability;
    }

    /**
     * Sets the roster availability.
     *
     * @param rosterAvailability the new roster availability
     */
    public void setRosterAvailability(String rosterAvailability) {
        this.rosterAvailability = rosterAvailability;
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
     * Gets the roster last seen.
     *
     * @return the roster last seen
     */
    public String getRosterLastSeen() {
        return rosterLastSeen;
    }

    /**
     * Sets the roster last seen.
     *
     * @param rosterLastSeen the new roster last seen
     */
    public void setRosterLastSeen(String rosterLastSeen) {
        this.rosterLastSeen = rosterLastSeen;
    }

    /**
     * Gets the roster type.
     *
     * @return the roster type
     */
    public String getRosterType() {
        return rosterType;
    }

    /**
     * Sets the roster type.
     *
     * @param rosterType the new roster type
     */
    public void setRosterType(String rosterType) {
        this.rosterType = rosterType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rosterID);
        dest.writeString(rosterName);
        dest.writeString(rosterStatus);
        dest.writeString(rosterImage);
        dest.writeString(rosterAvailability);
        dest.writeString(rosterType);
        dest.writeInt(isSelected);
        dest.writeString(rosterAdmin);
        dest.writeString(rosterLastSeen);
    }
}
