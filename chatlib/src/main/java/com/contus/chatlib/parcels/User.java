package com.contus.chatlib.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.contus.chatlib.enums.Availability;
import com.contus.chatlib.enums.RosterType;


/**
 * Created by user on 29/9/16.
 */
public class User implements Parcelable, IRoster {
    public static final Creator<User> CREATOR = new Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String address;
    private String country;
    private String firstname;
    private String id;
    private String image;
    private String lastname;
    private String password;
    private String status;
    private String thumbnail;
    private String username;
    private String isblocked;
    private String iqType;
    private String lastseen;
    private String lSyncTime;
    private String resource;
    private String groupId;
    private Availability availability;
    private RosterType userRosterType;
    private Jid userJid;

    public User() {
    }

    public User(Parcel in) {
        this.address = in.readString();
        this.country = in.readString();
        this.firstname = in.readString();
        this.id = in.readString();
        this.image = in.readString();
        this.userJid = (Jid)in.readParcelable(Jid.class.getClassLoader());
        this.lastname = in.readString();
        this.password = in.readString();
        this.status = in.readString();
        this.thumbnail = in.readString();
        this.username = in.readString();
        this.availability = (Availability)in.readSerializable();
        this.userRosterType = (RosterType)in.readSerializable();
        this.isblocked = in.readString();
        this.iqType = in.readString();
        this.lastseen = in.readString();
        this.lSyncTime = in.readString();
        this.resource = in.readString();
        this.groupId = in.readString();
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Availability getAvailability() {
        return this.availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public RosterType getRosterType() {
        return this.userRosterType;
    }

    public void setRosterType(RosterType userRosterType) {
        this.userRosterType = userRosterType;
    }

    public String getIsBlocked() {
        return this.isblocked;
    }

    public void setIsBlocked(String isblocked) {
        this.isblocked = isblocked;
    }

    public String getIqType() {
        return this.iqType;
    }

    public void setIqType(String iqType) {
        this.iqType = iqType;
    }

    public String getLastSeen() {
        return this.lastseen;
    }

    public void setLastSeen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getLastSyncTime() {
        return this.lSyncTime;
    }

    public void setLastSyncTime(String lSyncTime) {
        this.lSyncTime = lSyncTime;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.address);
        out.writeString(this.country);
        out.writeString(this.firstname);
        out.writeString(this.id);
        out.writeString(this.image);
        out.writeParcelable(this.userJid, 0);
        out.writeString(this.lastname);
        out.writeString(this.password);
        out.writeString(this.status);
        out.writeString(this.thumbnail);
        out.writeString(this.username);
        out.writeSerializable(this.availability);
        out.writeSerializable(this.userRosterType);
        out.writeString(this.isblocked);
        out.writeString(this.iqType);
        out.writeString(this.lastseen);
        out.writeString(this.lSyncTime);
        out.writeString(this.resource);
        out.writeString(this.groupId);
    }

    public RosterType getType() {
        return this.userRosterType;
    }

    public void setType(RosterType userRosterType) {
        this.userRosterType = userRosterType;
    }

    public Jid getJid() {
        return this.userJid;
    }

    public void setJid(Jid userJid) {
        this.userJid = userJid;
    }
}
