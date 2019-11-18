package com.new_chanages.models;

public class ContactModel {

    int contactId;
    String contactName;
    String contactNumber;
    String isContactSelected;
    String contactPic;
    String contactMPBName;
    String isMPBContact;

    public ContactModel() {
    }


    public ContactModel(String contactName, String contactNumber, String isContactSelected) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.isContactSelected = isContactSelected;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactSelected() {
        return isContactSelected;
    }

    public void setContactSelected(String contactSelected) {
        isContactSelected = contactSelected;
    }
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getIsContactSelected() {
        return isContactSelected;
    }

    public void setIsContactSelected(String isContactSelected) {
        this.isContactSelected = isContactSelected;
    }

    public String getContactPic() {
        return contactPic;
    }

    public void setContactPic(String contactPic) {
        this.contactPic = contactPic;
    }


    public String getContactMPBName() {
        return contactMPBName;
    }

    public void setContactMPBName(String contactMPBName) {
        this.contactMPBName = contactMPBName;
    }

    public String getIsMPBContact() {
        return isMPBContact;
    }

    public void setIsMPBContact(String isMPBContact) {
        this.isMPBContact = isMPBContact;
    }


}
