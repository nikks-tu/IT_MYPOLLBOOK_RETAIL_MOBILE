package com.contus.responsemodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 10/23/2015.
 */
public class PollParticipateResponseModel {

    //Success
    @SerializedName("success")
    private String mParticipateSuccess;

    //Message
    @SerializedName("msg")
    private String mParticipatemsg;

    //Results
    @SerializedName("results")
    private Results mParticipatemsgresults;
    //Poll answer
    @SerializedName("totalCount")
    private String totalCount;

    /**
     * Get the success
     *
     * @return mParticipateSuccess
     */
    public String getSuccess() {
        return mParticipateSuccess;
    }


    /**
     * GEt the message
     *
     * @return
     */
    public String getMsg() {
        return mParticipatemsg;
    }
    /**
     * Get the totalCount
     *
     * @return totalCount
     */
    public String getTotalCount() {
        return totalCount;
    }


        /**
         * GEt the results
         *
         * @return results
         */

    public Results getResults() {
        return mParticipatemsgresults;
    }


    /**
     * The class results
     */
    public class Results {
        //Poll id
        @SerializedName("poll_id")
        private String pollId;

        //Poll answer
        @SerializedName("poll_answer")
        private String pollAnswer;


        /**
         * Get the poll id
         *
         * @return poll id
         */
        public String getPollId() {
            return pollId;
        }


        /**
         * Get the poll answer
         *
         * @return
         */
        public String getPollAnswer() {
            return pollAnswer;
        }


    }
}
