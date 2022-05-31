package com.otl.tarangplus.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srishesh on 11/27/18.
 */

public class SignOutDetails {
    @SerializedName("auth_token")
    @Expose
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String mAuthToken) {
        this.authToken = mAuthToken;
    }
}
