package com.new_chanages.models;

public class ShipmentAdress_Model
{
    private String pincode;

    private String address;

    private String city;

    private String mobile_no;

    private String last_name;

    private String points;

    private boolean change_default_addr;

    private String user_id;

    private String product_id;

    private boolean use_default_addr;

    private String action;

    private String state;

    private String landmark;

    private String first_name;

    private String email;

    public String getPincode ()
    {
        return pincode;
    }

    public void setPincode (String pincode)
    {
        this.pincode = pincode;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getMobile_no ()
    {
        return mobile_no;
    }

    public void setMobile_no (String mobile_no)
    {
        this.mobile_no = mobile_no;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getPoints ()
    {
        return points;
    }

    public void setPoints (String points)
    {
        this.points = points;
    }

    public boolean getChange_default_addr ()
    {
        return change_default_addr;
    }

    public void setChange_default_addr (boolean change_default_addr)
    {
        this.change_default_addr = change_default_addr;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getProduct_id ()
    {
        return product_id;
    }

    public void setProduct_id (String product_id)
    {
        this.product_id = product_id;
    }

    public boolean getUse_default_addr ()
    {
        return use_default_addr;
    }

    public void setUse_default_addr (boolean use_default_addr)
    {
        this.use_default_addr = use_default_addr;
    }

    public String getAction ()
    {
        return action;
    }

    public void setAction (String action)
    {
        this.action = action;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getLandmark ()
    {
        return landmark;
    }

    public void setLandmark (String landmark)
    {
        this.landmark = landmark;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pincode = "+pincode+", address = "+address+", city = "+city+", mobile_no = "+mobile_no+", last_name = "+last_name+", points = "+points+", change_default_addr = "+change_default_addr+", user_id = "+user_id+", product_id = "+product_id+", use_default_addr = "+use_default_addr+", action = "+action+", state = "+state+", landmark = "+landmark+", first_name = "+first_name+", email = "+email+"]";
    }
}
