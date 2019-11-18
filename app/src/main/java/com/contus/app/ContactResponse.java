package com.contus.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * ContactResponse.java
 * This class is used to load the contact user's wall data.
 *
 * @author Contus Team <developers@contus.in>
 * @version 1.0
 * @category Contus
 * @package com.poaap.chatpageview
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http ://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Created by user on 10/1/2015.
 */
public class ContactResponse {


    /**
     * The Name.
     */
    @SerializedName("name")
    @Expose
    private String name;


    /**
     * The Userid.
     */
    @SerializedName("id")
    @Expose
    private String userid;
    /**
     * The Access token.
     */
    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    /**
     * The Mobile number.
     */
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    /**
     * The Country.
     */
    @SerializedName("country")
    @Expose
    private String country;
    /**
     * The Status msg.
     */
    @SerializedName("status_msg")
    @Expose
    private String statusMsg;

    /**
     * The Username.
     */
    @SerializedName("username")
    @Expose
    private String username;

    /**
     * The Image.
     */
    @SerializedName("image")
    @Expose
    private String image;

    /**
     * The Is active.
     */
    @SerializedName("is_active")
    @Expose
    private String isActive;


    /**
     * The Profile name.
     */
    @SerializedName("profile_name")
    @Expose
    private String profileName;

    /**
     * Gets name.
     *
     * @return The message
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name The message
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets mobilenumber.
     *
     * @return The userId
     */
    public String getMobilenumber() {
        return mobileNumber;
    }




    /**
     * Gets access token.
     *
     * @return The verificationCode
     */
    public String getAccessToken() {
        return accessToken;
    }


    /**
     * Sets access token.
     *
     * @param accessToken the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    /**
     * Gets profile name.
     *
     * @return the profile name
     */
    public String getProfileName() {
        return profileName;
    }


    /**
     * Gets country.
     *
     * @return The verificationCode
     */
    public String getCountry() {
        return country;
    }


    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * Gets status msg.
     *
     * @return The verificationCode
     */
    public String getStatusMsg() {
        return statusMsg;
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
     * Gets user name.
     *
     * @return The verificationCode
     */
    public String getUserName() {
        return username;
    }


    /**
     * Sets user name.
     *
     * @param username the username
     */
    public void setUserName(String username) {
        this.username = username;
    }


    /**
     * Get is active string.
     *
     * @return the string
     */
    public String getIsActive(){
        return  isActive;
    }



    /**
     * Gets userid.
     *
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Sets userid.
     *
     * @param userid the userid
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

}
