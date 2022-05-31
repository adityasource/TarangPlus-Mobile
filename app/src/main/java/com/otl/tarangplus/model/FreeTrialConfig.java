package com.otl.tarangplus.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FreeTrialConfig {

    @SerializedName("free_trail")
    @Expose
    private Boolean freeTrail;
    @SerializedName("free_trail_msg")
    @Expose
    private String freeTrailMsg;

    public Boolean getFreeTrail() {
        return freeTrail;
    }

    public void setFreeTrail(Boolean freeTrail) {
        this.freeTrail = freeTrail;
    }

    public String getFreeTrailMsg() {
        return freeTrailMsg;
    }

    public void setFreeTrailMsg(String freeTrailMsg) {
        this.freeTrailMsg = freeTrailMsg;
    }

}