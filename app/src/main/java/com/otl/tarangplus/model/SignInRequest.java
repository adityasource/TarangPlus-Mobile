package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ankith on 24/11/15.
 */

public class SignInRequest implements Serializable {

    @SerializedName("auth_token")
    @Expose
    private String mAuthToken;
    @SerializedName("user")
    @Expose
    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }
}
