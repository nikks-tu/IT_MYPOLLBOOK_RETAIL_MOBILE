package com.new_chanages.models;

public class ContactDetailsModel {
    String name;
    String id;

    public ContactDetailsModel() {
    }

    String mobile_number;

    public ContactDetailsModel(String name, String mobile_number, String country_code, String profile_image,String id) {
        this.name = name;
        this.mobile_number = mobile_number;
        this.country_code = country_code;
        this.profile_image = profile_image;
        this.id=id;
    }

    String country_code;
    String profile_image;

    public  String getId(){return id;}

    public  void setId(String id)
    {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }


}
