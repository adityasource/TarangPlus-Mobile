package com.otl.tarangplus.model;

public class LoginEvents {
    private String Email_ID;
    private String Phone;
    private String  type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LoginEvents(String email_ID, String phone) {
        Email_ID = email_ID;
        Phone = phone;
    }

    public LoginEvents(String email_ID, String phone, String type) {
        Email_ID = email_ID;
        Phone = phone;
        this.type = type;
    }

    public String getEmail_ID() {
        return Email_ID;
    }

    public void setEmail_ID(String email_ID) {
        Email_ID = email_ID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
