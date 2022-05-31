package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareRelatedData {

    @SerializedName("data")
    @Expose
    private ShareData data;

    @SerializedName("auth_token")
    @Expose
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ShareData getData() {
        return data;
    }

    public void setData(ShareData data) {
        this.data = data;
    }

}
