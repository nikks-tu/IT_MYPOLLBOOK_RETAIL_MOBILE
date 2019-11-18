package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 8/7/2015.
 */
public class MyPersonalInfoModel {
    //Success
    @SerializedName("success")
    private String success;
    //Results
    @SerializedName("results")
    private Results results;

    /**
     * Get the success
     * @return success
     */

    public String getSuccess() {
        return success;
    }


    /**
     * Get the results
     * @return
     */
    public Results getResults() {
        return results;
    }


    /**
     * The results
     */

    public class Results{
        //User id
        @SerializedName("user_id")
        private String userId;
        //Latitude
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longtitude")
        private String longtitude;
        //User name
        @SerializedName("user_name")
        private String userName;
        //Profiel image
        @SerializedName("profile_img")
        private String profileImg;
        //User gender
        @SerializedName("user_gender")
        private String userGender;
        //Country id
        @SerializedName("user_country_id")
        private String countryId;
        //User status
        @SerializedName("user_status")
        private String userStatus;
        //User location
        @SerializedName("user_location")
        private String userLocation;

        public String getAccessToken() {
            return accessToken;
        }

        @SerializedName("access_token")
        private String accessToken;



        @SerializedName("friends_count")
        private String friendsCount;

        public String getFriendsCount() {
            return friendsCount;
        }
        /**
         * Get the userid
         * @return
         */
        public String getUserId() {
            return userId;
        }


        /**
         * Get the latitude
         * @return latitude
         */
        public String getLatitude() {
            return latitude;
        }


        /**
         * Get the longitide
         * @return longtitude
         */
        public String getLongtitude() {
            return longtitude;
        }


        /**
         * Get the user name
         * @return userName
         */

        public String getUserName() {
            return userName;
        }


        /**
         * Get the profile image
         * @return
         */
        public String getProfileImg() {
            return profileImg;
        }


        /**
         * Get the user gender
         * @return
         */
        public String getUserGender() {
            return userGender;
        }


        /**
         * Get the country id
         * @return
         */
        public String getCountryId() {
            return countryId;
        }


        /**
         * Get the user status
         * @return user status
         */
        public String getUserStatus() {
            return userStatus;
        }


        /**
         * Get the user location
         * @return userLocation
         */
        public String getUserLocation() {
            return userLocation;
        }

    }

}
