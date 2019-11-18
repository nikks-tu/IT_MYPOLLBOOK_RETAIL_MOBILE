package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by user on 8/10/2015.
 */
public class ContactUsResponseModel {
    //success
    @SerializedName("success")
    private String success;
    //Results
    private ArrayList<ContactUsDetails> results;

    /**
     * Get the success
     * @return success
     */
    public String getSuccess() {
        return success;
    }
    /**
     * Get the results
     * @return results
     */
    public ArrayList<ContactUsDetails> getResults() {
        return results;
    }

    /**
     * Contains the variable to retrive the details from the resposne
     */
    public class ContactUsDetails{
        //admin email
        @SerializedName("admin_email")
        private String adminEmail;
        //Mobile number
        @SerializedName("admin_mobileno")
        private String adminMobileno;
        //contact number
        @SerializedName("admin_contactno")
        private String adminContactno;
        /**
         * adminEmail
         * @return adminEmail
         */
        public String getAdminEmail() {
            return adminEmail;
        }
        /**
         * Get the adminMobileno
         * @return adminMobileno
         */
        public String getAdminMobileno() {
            return adminMobileno;
        }
        /**Get the adminContactno
         *
         * @return adminContactno
         */
        public String getAdminContactno() {
            return adminContactno;
        }
    }
}
