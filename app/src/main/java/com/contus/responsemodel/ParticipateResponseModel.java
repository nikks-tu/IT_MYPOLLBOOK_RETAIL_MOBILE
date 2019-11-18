package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 10/30/2015.
 */
public class ParticipateResponseModel {
    //Success
    private String success;
    //Web count
    private String webcount;
    //Results
    private Results results;




    /**
     * Get the success
     *
     * @return
     */
    public String getSuccess() {
        return success;
    }



    /**
     * Get the web count
     *
     * @return
     */
    public String getWebcount() {
        return webcount;
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
     * Teh class results
     */

    public class Results {
        //Total
        private String total;
        //Last PAGE
        @SerializedName("last_page")
        private String lastPageParticpate;

        //Current PAGE
        @SerializedName("current_page")
        private String currentPageParticpate;
        //Get the total
        public String getTotal() {
            return total;
        }
        //Data
            @SerializedName("data")
            private List<Results.Data> dataParticpate;



        /**
         * Get the current PAGE
         *
         * @return currentPageParticpate
         */

        public String getCurrentPage() {
            return currentPageParticpate;
        }


        /**
         * Get the last PAGE
         *
         * @return last PAGE
         */
        public String getLastPage() {
            return lastPageParticpate;
        }



        /**
         * Get the data
         *
         * @return
         */
        public List<Data> getData() {
            return dataParticpate;
        }



        /**
         * The class data
         */
        public class Data {


            //User info
            @SerializedName("user_infos")
            private Results.Data.UserInfos userInfo;


            /**
             * Get the user info
             *
             * @return userInfo
             */
            public UserInfos getUserInfo() {
                return userInfo;
            }



            /**
             * The class user info
             */
            public class UserInfos {
                //User name
                @SerializedName("name")
                private String userName;

                //User profiel iamge
                @SerializedName("image")
                private String userProfileImage;
                /**
                 * Get the user name
                 *
                 * @return userName
                 */
                public String getUserName() {
                    return userName;
                }



                /**
                 * Get the user profile image
                 *
                 * @return userProfileImage
                 */
                public String getUserProfileImage() {
                    return userProfileImage;
                }



            }
        }

    }

}
