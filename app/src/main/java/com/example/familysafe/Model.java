package com.example.familysafe;

public class Model {
    String contactName;
    String contactNo;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Model(String contactName, String contactNo) {
        this.contactName = contactName;
        this.contactNo = contactNo;
    }
}
