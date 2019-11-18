/**
 * MobileNumberChangeModel.java
 * <p>
 * Model class for mobile number change response
 *
 * @category Contus
 * @package com.contus.activity
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */


package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 8/18/2015.
 */
public class MobileNumberChange {
    //Results
    @SerializedName("results")
    private Results results;
    //Message
    @SerializedName("msg")
    private String msg;
    //Succes
    @SerializedName("success")
    private String success;
    //User gender
    @SerializedName("user_gender")
    private String userGender;



    //City
    private String city;

    /**
     * Get the results
     * @return results
     */

    public Results getResults() {
        return results;
    }

    /**
     * Get the messsage
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Get the success
     * @return success
     */
    public String getSuccess() {
        return success;
    }

    /**
     * The class results
     */
    public class Results {
        //Mobile key
        @SerializedName("mobileKey")
        private String mobileKey;
        //Profile image
        @SerializedName("profile_img")
        private String profileImg;
        //user name
        @SerializedName("user_name")
        private String userName;
        //user id
        @SerializedName("user_id")
        private String userId;

        public String getPassword() {
            return password;
        }

        @SerializedName("password")
        private String password;
        /**
         * Get the user name
         * @return userName
         */
        public String getUserName() {
            return userName;
        }

        /**
         * Get the user id
         * @return userId
         */
        public String getUserId() {
            return userId;
        }

        /**
         * Get the mobile key
         * @return
         */
        public String getMobileKey() {
            return mobileKey;
        }

        /**
         * Get the profile image
         * @return
         */

        public String getProfileImg() {
            return profileImg;
        }
    }
}
