package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 8/5/2015.
 */
public class PersonalInfoModel {
    //success
    private String success;
    //Results
    private Results results;

    /**
     * Get the success
     *
     * @return success
     */
    public String getSuccess() {
        return success;
    }

    /**
     * Get the results
     *
     * @return
     */
    public Results getResults() {
        return results;
    }
    /**
     * The class Results
     */
    public class Results {
        //userId
        @SerializedName("user_id")
        private String userId;
        //Profile image
        @SerializedName("profile_img")
        private String profileImg;
        //User name
        @SerializedName("user_name")
        private String userName;
        //User name
        @SerializedName("access_token")
        private String accessToken;
        //User name
        @SerializedName("password")
        private String password;
        /**
         * Get the user id
         *
         * @return userId
         */
        public String getUserId() {
            return userId;
        }
        /**
         * Get the user profile image
         *
         * @return
         */
        public String getProfileImage() {
            return profileImg;
        }



        /**
         * Get the user name
         *
         * @return
         */
        public String getUsername() {
            return userName;
        }


        public String getAccessToken() {
            return accessToken;
        }


        public String getPassword() {
            return password;
        }



    }

}
