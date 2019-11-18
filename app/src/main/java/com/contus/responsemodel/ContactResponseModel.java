package com.contus.responsemodel;

import java.util.List;

/**
 * Created by Prem on 10-12-2015.
 */
public class ContactResponseModel {
    //success
    private String success;
    //message
    private String msg;
   //Results
    private List<Results> results;


    /**
     * Get the success
     * @return
     */
    public String getSuccess() {
        return success;
    }

    /**
     * Get the message
     * @return
     */

    public String getMsg() {
        return msg;
    }

    /**\
     * Get the results values
     * @return
     */

    public List<Results> getResults() {
        return results;
    }

    /**
     * Get the results
     */

    public class Results{
        //username
        private String username;
        //private status message
        private String statusMsg;
        //id
        private String id;
        //image
        private String image;
        /**
         * Get the name
         * @return name
         */
        public String getName() {
            return username;
        }

        /**
         * Get the status message
         * @return
         */

        public String getStatusMsg() {
            return statusMsg;
        }

        /**
         * Get the id
         * @return
         */


        public String getId() {
            return id;
        }

        /**
         * Get the image
         * @return
         */

        public String getImage() {
            return image;
        }





    }


}
