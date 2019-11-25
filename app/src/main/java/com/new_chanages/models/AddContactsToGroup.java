package com.new_chanages.models;

public class AddContactsToGroup {
    String action;
    String contacts;
    String group_id;

    public AddContactsToGroup(String action, String contacts, String group_id) {
        this.action = action;
        this.contacts = contacts;
        this.group_id = group_id;
    }

}
