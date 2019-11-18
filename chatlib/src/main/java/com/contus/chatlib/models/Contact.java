package com.contus.chatlib.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;


/**
 * Created by user on 16/6/16.
 */
public class Contact implements Parcelable {

    /**
     * The constant CREATOR.
     */
    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Gson().fromJson(in.readString(), Contact.class);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
    private String jid;
    private String id;
    private String name;
    private String user;
    private String last_seen;
    private String status;
    private String image;
    private String availability;
    private String accepted;
    private String blocked;

    /**
     * Instantiates a new Contact.
     */
    public Contact() {
        // Default Constructor
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets accepted.
     *
     * @return the accepted
     */
    public String getAccepted() {
        return accepted;
    }

    /**
     * Sets accepted.
     *
     * @param accepted the accepted
     */
    public void setAccepted(String accepted) {
        this.user = accepted;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.user = id;
    }

    /**
     * Gets blocked.
     *
     * @return the blocked
     */
    public String getBlocked() {
        return blocked;
    }

    /**
     * Sets blocked.
     *
     * @param blocked the blocked
     */
    public void setBlocked(String blocked) {
        this.user = blocked;
    }

    /**
     * Gets last_seen.
     *
     * @return the last_seen
     */
    public String getLastSeen() {
        return last_seen;
    }

    /**
     * Sets last_seen.
     *
     * @param last_seen the last_seen
     */
    public void setLastSeen(String last_seen) {
        this.last_seen = last_seen;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets jid.
     *
     * @return the jid
     */
    public String getJid() {
        return jid;
    }

    /**
     * Sets jid.
     *
     * @param jid the jid
     */
    public void setJid(String jid) {
        this.jid = jid;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(new Gson().toJson(this));
    }
}
