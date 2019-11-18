package com.new_chanages.postParameters;

public class GetGroupsPostParameters {

    String action;
    String contacts;
    int    user_id;
    String group_creater;
    String group_name;
    String group_image;
    String group_id;

    public GetGroupsPostParameters(String action, int user_id, String group_id) {
        this.action = action;
        this.user_id = user_id;
        this.group_id = group_id;
    }

    public GetGroupsPostParameters(String action, String contacts) {
        this.action = action;
        this.contacts = contacts;
    }

    public GetGroupsPostParameters(String action, String group_creater, String group_name, String group_image, String contacts, String group_id) {
        this.action = action;
        this.group_creater = group_creater;
        this.group_name = group_name;
        this.group_image = group_image;
        this.contacts = contacts;
        this.group_id = group_id;
    }

    public GetGroupsPostParameters(String action, int user_id) {
        this.action = action;
        this.user_id = user_id;
    }

    public String getGroup_creater() {
        return group_creater;
    }

    public void setGroup_creater(String group_creater) {
        this.group_creater = group_creater;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_image() {
        return group_image;
    }

    public void setGroup_image(String group_image) {
        this.group_image = group_image;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
