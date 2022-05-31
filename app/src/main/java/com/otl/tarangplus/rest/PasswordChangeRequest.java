package com.otl.tarangplus.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.otl.tarangplus.model.User;

/**
 * Created by srishesh on 11/14/18.
 */

public class PasswordChangeRequest {

    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("user")
    @Expose
    private User user;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
