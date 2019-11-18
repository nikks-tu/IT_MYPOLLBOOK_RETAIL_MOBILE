package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 9/10/2015.
 */
public class CategoryPollResponseModel {
    //Success string
    @SerializedName("success")
    private String success;
    //message
    @SerializedName("msg")
    private String msg;
    //Results
    private List<Results> results;

    /**
     * Success Value
     *
     * @return -returns the value from the response
     */
    public String getSuccess() {
        return success;
    }


    /**
     * get the message
     * @return msg
     */
    public String getMsg() {
        return msg;
    }


    /**
     * Get the list
     * @return results
     */
    public List<Results> getResults() {
        return results;
    }

    /**
     * Contains the variable to retrive the details from the resposne
     */
    public class Results{
        //Category name
        @SerializedName("category_name")
        private String categoryName;
        //id
        @SerializedName("id")
        private String categoryId;
        //userCategory
        @SerializedName("user_category")
        private List<Results.UserCategory> userCategory;

        /**
         * GEt the categoryName
         * @return categoryName
         */
        public String getCategoryName() {
            return categoryName ;
        }


        /**
         * Get the categoryId
         * @return categoryId
         */

        public String getCategoryId() {
            return categoryId;
        }


        /**
         * Get the userCategory
         * @return userCategory
         */

        public List<UserCategory> getUserCategory() {
            return userCategory;
        }

        /**
         * Contains the variable to retrive the details from the resposne
         */
        public class UserCategory{
            //categoryId
            @SerializedName("category_id")
            private String categorySubId;
            /**
             * Get the categoryId
             * @return categoryId
             */
            public String getCategoryId() {
                return categorySubId;
            }


        }
    }
}
