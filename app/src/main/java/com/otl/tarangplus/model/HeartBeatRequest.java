package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by saranyu on 1/23/17.
 */

public class HeartBeatRequest implements Serializable {

    @SerializedName("auth_token")
    @Expose
    private String mAuthToken;
    @SerializedName("listitem")
    @Expose
    private HeartBeatData mHeartBeatData;

    public String getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public HeartBeatData getHeartBeatData() {
        return mHeartBeatData;
    }

    public void setHeartBeatData(HeartBeatData heartBeatData) {
        mHeartBeatData = heartBeatData;
    }
}
