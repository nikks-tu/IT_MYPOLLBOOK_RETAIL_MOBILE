package com.new_chanages.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPollsQuestionObject {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("poll_type")
    @Expose
    private String pollType;
    @SerializedName("poll_question")
    @Expose
    private String pollQuestion;
    @SerializedName("poll_qimage")
    @Expose
    private String pollQimage;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("polls_qimage2")
    @Expose
    private String pollsQimage2;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("isanswered")
    @Expose
    private Integer isanswered;
    @SerializedName("user_infos")
    @Expose
    private UserInfosObject userInfos;
    @SerializedName("poll_answers")
    @Expose
    private PollAnswersObject pollAnswers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPollType() {
        return pollType;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public String getPollQimage() {
        return pollQimage;
    }

    public void setPollQimage(String pollQimage) {
        this.pollQimage = pollQimage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPollsQimage2() {
        return pollsQimage2;
    }

    public void setPollsQimage2(String pollsQimage2) {
        this.pollsQimage2 = pollsQimage2;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsanswered() {
        return isanswered;
    }

    public void setIsanswered(Integer isanswered) {
        this.isanswered = isanswered;
    }

    public UserInfosObject getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(UserInfosObject userInfos) {
        this.userInfos = userInfos;
    }

    public PollAnswersObject getPollAnswers() {
        return pollAnswers;
    }

    public void setPollAnswers(PollAnswersObject pollAnswers) {
        this.pollAnswers = pollAnswers;
    }

}