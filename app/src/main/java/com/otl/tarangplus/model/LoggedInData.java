package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoggedInData {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("free_trail")
    @Expose
    private Boolean freeTrail;
    @SerializedName("welcome_msg")
    @Expose
    private String welcomeMsg;

    public Boolean getFreeTrail() {
        return freeTrail;
    }

    public void setFreeTrail(Boolean freeTrail) {
        this.freeTrail = freeTrail;
    }

    public String getWelcomeMsg() {
        return welcomeMsg;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}