package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 8/8/2015.
 */
public class EditPersonalInfoModel {
    //Success
    @SerializedName("success")
    private String mSuccess;
    //Results
    @SerializedName("results")
    private Results mResults;

    /**
     * Get successs
     * @return mSuccess
     */
    public String getmSuccess() {
        return mSuccess;
    }

    /**
     * Get results
     * @return mResults
     */
    public Results getmResults() {
        return mResults;
    }


    /**
     * The class Results
     */
    public class Results{
        //User id
        @SerializedName("user_id")
        private String userId;
        //userName
        @SerializedName("user_name")
        private String userName;
        //profileImg
        @SerializedName("profile_img")
        private String profileImg;
        //userLocation
        @SerializedName("user_location")
        private String userLocation;
        @SerializedName("user_status")
        private String userStatus;

        public String getUserGender() {
            return userGender;
        }

        //User gender
        @SerializedName("user_gender")
        private String userGender;
        public String getUserStatus() {
            return userStatus;
        }
        /**
         * Get the user id
         * @return userId
         */
        public String getUserId() {
            return userId;
        }


        /**
         * Get the userName
         * @return userName
         */
        public String getUserName() {
            return userName;
        }


        /**
         *  Get the profileImg
         * @return profileImg
         */
        public String getProfileImg() {
            return profileImg;
        }

        /**
         * Get the userLocation
         * @return userLocation
         */
        public String getUserLocation() {
            return userLocation;
        }

        
    }
}
