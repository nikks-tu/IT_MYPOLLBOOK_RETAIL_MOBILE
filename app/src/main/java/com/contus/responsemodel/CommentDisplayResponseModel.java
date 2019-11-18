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
 * Created by user on 9/24/2015.
 */
public class CommentDisplayResponseModel {

    //Success string
    private String success;

    //Message
    private String msg;

    //Results
    private Results results;

    //count
    private String count;
    /**
     * Success Value
     *
     * @return -returns the value from the response
     */
    public String getSuccess() {
        return success;
    }


    /**
     * Get the list
     *
     * @return results
     */
    public Results getResults() {
        return results;
    }


    /**
     * Get count
     *
     * @return count
     */
    public String getCount() {
        return count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    /**
     * Contains the variable to retrive the details from the resposne
     */
    public class Results {
        //total
        @SerializedName("total")
        private String totalCommentsPagination;
        //last PAGE
        @SerializedName("last_page")
        private String lastPageCommentsPagination;
        //Current PAGE
        @SerializedName("current_page")
        private String currentPageCommentsPagination;
        //data
        @SerializedName("data")
        private List<Data> dataCommentsPagination;
        /**
         * Toatal COMMENTS
         * @return totalCommentsPagination
         */
        public String getTotal() {
            return totalCommentsPagination;
        }


        /**
         * Get the last PAGE
         * @return last PAGE
         */
        public String getLastPage() {
            return lastPageCommentsPagination;
        }


        /**
         * current PAGE
         * @return currentPageCommentsPagination
         */
        public String getCurrentPage() {
            return currentPageCommentsPagination;
        }


        /**
         * Get the details
         * @return dataCommentsPagination
         */
        public List<Data> getData() {
            return dataCommentsPagination;
        }
        /**
         * Contains the variable to retrive the details from the resposne
         */
        public class Data {
            //id
            private String id;
            //COMMENTS
            private String comments;
            //userInfo
            @SerializedName("user_infos")
            private UserInfo userInfo;
            //user id
            @SerializedName("user_id")
            private String userId;

            //user id
            @SerializedName("updated_at")
            private String updatedAt;
            /**
             * Get the id
             * @return id
             */
            public String getId() {
                return id;
            }


            /**
             * Get the COMMENTS
             * @return COMMENTS
             */
            public String getComments() {
                return comments;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }
            /**
             * Get the userInfo
             * @return userInfo
             */
            public UserInfo getUserInfo() {
                return userInfo;
            }

            /**
             * Get the userId
             * @return userId
             */
            public String getUserId() {
                return userId;
            }


            /**
             * Contains the variable to retrive the details from the resposne
             */
            public class UserInfo {
                //userProfileImg
                @SerializedName("image")
                private String userProfileImg;

                //userName
                @SerializedName("name")
                private String userName;
                /**
                 * userProfileImg
                 * @return userProfileImg
                 */
                public String getUserProfileImg() {
                    return userProfileImg;
                }
                /**
                 * userName
                 * @return userName
                 */
                public String getUserName() {
                    return userName;
                }


            }

        }


    }
}
