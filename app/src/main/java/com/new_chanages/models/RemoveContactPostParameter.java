package com.new_chanages.models;

public class RemoveContactPostParameter {
    String action;
    String contacts;
    String group_id;

    public RemoveContactPostParameter() {
    }

    public RemoveContactPostParameter(String action, String contacts, String group_id) {
        this.action = action;
        this.contacts = contacts;
        this.group_id = group_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
}
