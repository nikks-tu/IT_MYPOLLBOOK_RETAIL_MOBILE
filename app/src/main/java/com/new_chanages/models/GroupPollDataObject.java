package com.new_chanages.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GroupPollDataObject{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("pollLikesCounts")
    @Expose
    private Integer pollLikesCounts;
    @SerializedName("pollCommentsCounts")
    @Expose
    private Integer pollCommentsCounts;
    @SerializedName("userPollLikes")
    @Expose
    private Integer userPollLikes;
    @SerializedName("pollParticipateCount")
    @Expose
    private Integer pollParticipateCount;
    @SerializedName("pollcategories")
    @Expose
    private PollCategoriesObject pollcategories;
    @SerializedName("group_infos")
    @Expose
    private GroupInfosObject groupInfos;
    @SerializedName("user_polls_question")
    @Expose
    private UserPollsQuestionObject userPollsQuestion;
    @SerializedName("user_participate_polls")
    @Expose
    private ArrayList<GroupPollsParticipateObject> userParticipatePolls = null;

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

    public Integer getPollLikesCounts() {
        return pollLikesCounts;
    }

    public void setPollLikesCounts(Integer pollLikesCounts) {
        this.pollLikesCounts = pollLikesCounts;
    }

    public Integer getPollCommentsCounts() {
        return pollCommentsCounts;
    }

    public void setPollCommentsCounts(Integer pollCommentsCounts) {
        this.pollCommentsCounts = pollCommentsCounts;
    }

    public Integer getUserPollLikes() {
        return userPollLikes;
    }

    public void setUserPollLikes(Integer userPollLikes) {
        this.userPollLikes = userPollLikes;
    }

    public Integer getPollParticipateCount() {
        return pollParticipateCount;
    }

    public void setPollParticipateCount(Integer pollParticipateCount) {
        this.pollParticipateCount = pollParticipateCount;
    }

    public PollCategoriesObject getPollcategories() {
        return pollcategories;
    }

    public void setPollcategories(PollCategoriesObject pollcategories) {
        this.pollcategories = pollcategories;
    }

    public GroupInfosObject getGroupInfos() {
        return groupInfos;
    }

    public void setGroupInfos(GroupInfosObject groupInfos) {
        this.groupInfos = groupInfos;
    }

    public UserPollsQuestionObject getUserPollsQuestion() {
        return userPollsQuestion;
    }

    public void setUserPollsQuestion(UserPollsQuestionObject userPollsQuestion) {
        this.userPollsQuestion = userPollsQuestion;
    }

    public ArrayList<GroupPollsParticipateObject> getUserParticipatePolls() {
        return userParticipatePolls;
    }

    public void setUserParticipatePolls(ArrayList<GroupPollsParticipateObject> userParticipatePolls) {
        this.userParticipatePolls = userParticipatePolls;
    }

}