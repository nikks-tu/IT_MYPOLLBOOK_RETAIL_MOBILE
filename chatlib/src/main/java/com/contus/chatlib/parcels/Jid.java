package com.contus.chatlib.parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 29/9/16.
 */
public class Jid implements Parcelable {
    public static final Creator<Jid> CREATOR = new Creator() {
        public Jid createFromParcel(Parcel in) {
            return new Jid(in);
        }

        public Jid[] newArray(int size) {
            return new Jid[size];
        }
    };
    private String host;
    private String resource;
    private String username;

    public Jid() {
    }

    public Jid(Parcel in) {
        this.host = in.readString();
        this.resource = in.readString();
        this.username = in.readString();
    }

    public static Jid parse(String jid) {
        Jid returnJid = new Jid();
        String[] s = jid.split("@");
        returnJid.setUserName(s[0]);
        if(s.length > 1) {
            String[] s1 = s[1].split("/");
            returnJid.setHost(s1[0]);
            if(s1.length > 1) {
                returnJid.setResource(s1[1]);
            } else {
                returnJid.setResource("");
            }
        }

        return returnJid;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getUserName() {
        return this.username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String toString() {
        String result = String.format(this.username + "@" + this.host, new Object[0]);
        if(this.resource != null && !this.resource.isEmpty()) {
            result = result + "/" + this.resource;
        }

        return result;
    }

    public String toStringWithoutResource() {
        return String.format(this.username + "@" + this.host, new Object[0]);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.host);
        out.writeString(this.resource);
        out.writeString(this.username);
    }
}
