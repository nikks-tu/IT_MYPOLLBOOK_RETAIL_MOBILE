package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 10/20/2015.
 */
public class CampaignCommentDelete {
    //Campaign comment success value
    @SerializedName("success")
    private String mSuccess;
    //Campaign comment message
    @SerializedName("msg")
    private String message;
    //Campaign comment results
    @SerializedName("results")
    private String mResults;
    @SerializedName("count")
    private String mCount;
    /**
     * Success Value
     * @return -returns the value from the response
     */
    public String getSuccess() {
        return mSuccess;
    }


    /**
     * Message
     * @returnreturns the value from the response
     */
    public String getMessage() {
        return message;
    }


    /**
     * Results
     * @return-returns the value as 1 or 0
     */
    public String getResults() {
        return mResults;
    }


    /**
     * Total campaign COMMENTS count
     * @return-Campaign COMMENTS total count
     */
    public String getCount() {
        return mCount;
    }

}
