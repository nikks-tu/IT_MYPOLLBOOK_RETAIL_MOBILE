package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 8/3/2015.
 */
public class WelcomeResponseModel {
    //Welcome message details
    private List<UserDetails> results;
    //Success
    private String success;

    /**
     *Return the success from the response
     * @return success value
     */
    public String getSuccess() {
        return success;
    }

    /**
     *Return the details from the response
     * @return results-userDetails as list
     */

    public List<UserDetails> getData() {
        return results;
    }
        /**
         * The Class UserDetails.
         */
        public class UserDetails {
            @SerializedName("welcome_msg")
            private String welcomeMsg;
            /**
             * Get welcome message
             * @return welcomeMsg-from response
             */
            public String getWelcomeMsg() {
                return welcomeMsg;
            }

        }
    }


