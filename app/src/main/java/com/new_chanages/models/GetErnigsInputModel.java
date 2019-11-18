package com.new_chanages.models;

public class GetErnigsInputModel {

    String action;
    String user_id;

    public GetErnigsInputModel(String action, String user_id) {
        this.action = action;
        this.user_id = user_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
