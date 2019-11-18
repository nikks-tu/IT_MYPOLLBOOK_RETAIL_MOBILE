package com.contusfly.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;


/**
 * Created by user on 29/2/16.
 */

/**
 * The Activity class which acts as the base class for all other Activity classes for this
 * application, All other activities must extend this class so that they can receive the callbacks
 * of the chat service through the overriding methods of other this class all the callbacks
 * will be received from the broadcast receiver of this class.This class defines the a chat message with the whole information this is the
 * primary class which can be considered as an atomic component which is discrete
 * from other messages.
 *
 * @author ContusTeam <developers@contus.in>
 * @version 1.1
 */
public class Contact implements Parcelable {

    /**
     * The Constant CREATOR.
     */
    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            String json = in.readString();
            return new Gson().fromJson(json, Contact.class);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    /**
     * The is saved.
     */
    private boolean isSaved;

    /**
     * The contact nos.
     */
    private String contactName, /**
     * The Contact nos.
     */
    contactNos;

    /**
     * The is selected.
     */
    private int isSelected;

    /**
     * Gets the creator.
     *
     * @return the creator
     */
    public static Creator<Contact> getCREATOR() {
        return CREATOR;
    }

    /**
     * -     * Instantiates a new Contact.
     * -
     */
    public Contact() {
        // Default Constructor

    }


    /**
     * Checks if is saved.
     *
     * @return true, if is saved
     */
    public boolean isSaved() {
        return isSaved;
    }

    /**
     * Sets the saved.
     *
     * @param saved the new saved
     */
    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    /**
     * Gets the contact nos.
     *
     * @return the contact nos
     */
    public String getContactNos() {
        return contactNos;
    }

    /**
     * Sets the contact nos.
     *
     * @param contactNos the new contact nos
     */
    public void setContactNos(String contactNos) {
        this.contactNos = contactNos;
    }

    /**
     * Gets the contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the contact name.
     *
     * @param contactName the new contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
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
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
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
}
