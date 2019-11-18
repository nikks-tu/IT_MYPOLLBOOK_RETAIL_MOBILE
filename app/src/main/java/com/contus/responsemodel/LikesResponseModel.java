/**
 * @category TeasorTrailor
 * @package com.contus.responsemodel
 * @version 1.0
 * @author Contus Team <developers@contus.in>
 * @copyright Copyright (C) 2015 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 9/28/2015.
 */
public class LikesResponseModel {
    //Success
    private String success;
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
     * Get the results
     *
     * @return results
     */
    public Results getResults() {
        return results;
    }
    /**
     * The class Results
     */
    public class Results {
        //Last PAGE
        @SerializedName("last_page")
        public String lastPage;
        //CUrrent PAGE
        @SerializedName("current_page")
        public String currentPage;
        //Current PAGE
        @SerializedName("per_page")
        public String perPage;
        //Data
        private List<Results.Data> data;

      ;
        /**
         * Get the  lastPage
         *
         * @return lastPage
         */
        public String getLastPage() {
            return lastPage;
        }

        /**
         * Get the currentPage
         *
         * @return currentPage
         */
        public String getCurrentPage() {
            return currentPage;
        }



        /**
         * Get the data
         *
         * @return data
         */
        public List<Data> getData() {
            return data;
        }

        /**
         * The class data
         */
        public class Data {

            //User details
            @SerializedName("user_details")
            private Results.Data.UserInfo userInfo;
            /**
             * Get the user details
             *
             * @return
             */

            public UserInfo getUserInfo() {
                return userInfo;
            }



            /**
             * The user info
             */
            public class UserInfo {
                //User pprofile image
                @SerializedName("image")
                private String image;
                //userName
                @SerializedName("name")
                private String userName;
                /**
                 * userProfileImg
                 *
                 * @return userProfileImg
                 */
                public String getUserProfileImg() {
                    return image;
                }



                /**
                 * Get the user name
                 *
                 * @return userName
                 */
                public String getUserName() {
                    return userName;
                }



            }

        }


    }
}
