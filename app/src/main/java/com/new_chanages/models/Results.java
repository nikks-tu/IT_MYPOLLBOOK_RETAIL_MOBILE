package com.new_chanages.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {

    @SerializedName("address")
    @Expose
    private Address address;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("address_id")
    @Expose
    private String address_id;

    @SerializedName("type")
    @Expose
    private String type;

    public Address getAddress ()
    {
        return address;
    }

    public void setAddress (Address address)
    {
        this.address = address;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getAddress_id ()
    {
        return address_id;
    }

    public void setAddress_id (String address_id)
    {
        this.address_id = address_id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [address = "+address+", user_id = "+user_id+", address_id = "+address_id+", type = "+type+"]";
    }
}
