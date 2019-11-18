package com.new_chanages.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupPollsParticipateObject {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("poll_answer_id")
    @Expose
    private Integer pollAnswerId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("poll_answer")
    @Expose
    private String pollAnswer;
    @SerializedName("poll_answer_option")
    @Expose
    private Integer pollAnswerOption;

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

    public Integer getPollAnswerId() {
        return pollAnswerId;
    }

    public void setPollAnswerId(Integer pollAnswerId) {
        this.pollAnswerId = pollAnswerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPollAnswer() {
        return pollAnswer;
    }

    public void setPollAnswer(String pollAnswer) {
        this.pollAnswer = pollAnswer;
    }

    public Integer getPollAnswerOption() {
        return pollAnswerOption;
    }

    public void setPollAnswerOption(Integer pollAnswerOption) {
        this.pollAnswerOption = pollAnswerOption;
    }

}