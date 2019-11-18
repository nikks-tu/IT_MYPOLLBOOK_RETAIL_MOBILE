package com.contus.chatlib.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;


/**
 * Created by user on 16/6/16.
 */
public class Group implements Parcelable {

    /**
     * The constant CREATOR.
     */
    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Gson().fromJson(in.readString(), Group.class);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
    private String jid;
    private String id;
    private String name;
    private String admin;
    private String description;
    private String is_active;
    private String image;
    private String users;

    /**
     * Instantiates a new Contact.
     */
    public Group() {
        // Default Constructor
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
     * Gets group name.
     *
     * @return the group name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets group name.
     *
     * @param name the group name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets admin.
     *
     * @return the nick name
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * Sets nick name.
     *
     * @param admin the nick name
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public String getUsers() {
        return users;
    }

    /**
     * Sets users.
     *
     * @param users the users
     */
    public void setUsers(String users) {
        this.users = users;
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
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getActive() {
        return is_active;
    }

    /**
     * Sets name.
     *
     * @param is_active the name
     */
    public void setActive(String is_active) {
        this.is_active = is_active;
    }

    public String getDescription() {
        return description;
    }

    public void SetDescription(String description) {
        this.description = description;
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
