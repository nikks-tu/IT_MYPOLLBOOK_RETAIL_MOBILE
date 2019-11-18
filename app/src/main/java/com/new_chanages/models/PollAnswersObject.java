package com.new_chanages.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PollAnswersObject  {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("poll_answer1")
    @Expose
    private String pollAnswer1;
    @SerializedName("poll_answer2")
    @Expose
    private String pollAnswer2;
    @SerializedName("poll_answer3")
    @Expose
    private String pollAnswer3;
    @SerializedName("poll_answer4")
    @Expose
    private String pollAnswer4;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public String getPollAnswer1() {
        return pollAnswer1;
    }

    public void setPollAnswer1(String pollAnswer1) {
        this.pollAnswer1 = pollAnswer1;
    }

    public String getPollAnswer2() {
        return pollAnswer2;
    }

    public void setPollAnswer2(String pollAnswer2) {
        this.pollAnswer2 = pollAnswer2;
    }

    public String getPollAnswer3() {
        return pollAnswer3;
    }

    public void setPollAnswer3(String pollAnswer3) {
        this.pollAnswer3 = pollAnswer3;
    }

    public String getPollAnswer4() {
        return pollAnswer4;
    }

    public void setPollAnswer4(String pollAnswer4) {
        this.pollAnswer4 = pollAnswer4;
    }

}