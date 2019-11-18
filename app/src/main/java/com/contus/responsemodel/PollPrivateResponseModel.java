package com.contus.responsemodel;

import java.util.List;

/**
 * Created by user on 1/19/2016.
 */
public class PollPrivateResponseModel {
    //success
    private String success;
    //Results
    private List<Results> results;

    /**
     * Get the success
     * @return-success
     */
    public String getSuccess() {
        return success;
    }
    /**
     * Get the results
     * @return- results
     */
    public List<Results> getResults() {
        return results;
    }
//Class the results
    public class Results{
      //Name
        private String name;
    //Image
        private String image;
    //type
    private String type;

    //type
    private String id;
    /**
     * Get the name
     * @return name
     */
        public String getName() {
            return name;
        }
    /**
     * Get the image
     * @return image
     */
        public String getImage() {
            return image;
        }
    /**
     * Get the type
     * @return type
     */
    public String getType() {
        return type;
    }
    /**
     * Get the type
     * @return type
     */
    public String getId() {
        return id;
    }

    }


}
