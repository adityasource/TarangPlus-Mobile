package com.otl.tarangplus.model;

public class UserEvents {
    private String Name;
    private String Phone;
    private String Email;
    private String Address;
    private String State;
    private String Country;
    private String DOB;
    private String type;

    public UserEvents(String name, String phone) {
        Name = name;
        Phone = phone;
    }

    public UserEvents(String name, String phone, String email, String type) {
        Name = name;
        Phone = phone;
        Email = email;
        this.type = type;
    }

    public UserEvents(String name, String phone, String email, String address, String state, String country, String DOB) {
        Name = name;
        Phone = phone;
        Email = email;
        Address = address;
        State = state;
        Country = country;
        this.DOB = DOB;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
}
