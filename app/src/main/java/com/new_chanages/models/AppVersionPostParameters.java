package com.new_chanages.models;

public class AppVersionPostParameters {

    private String device_id;
    String action;

    public AppVersionPostParameters(String action, String group_id) {
        this.action = action;
        this.group_id = group_id;
    }

    String group_id;

    public AppVersionPostParameters(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }



}
