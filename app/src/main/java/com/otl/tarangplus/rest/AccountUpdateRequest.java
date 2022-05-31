package com.otl.tarangplus.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srishesh on 10/26/18.
 */

public class AccountUpdateRequest {

 @SerializedName("user")
 @Expose
 private AccountUser user;

    public AccountUser getUser() {
        return user;
    }

    public void setUser(AccountUser user) {
        this.user = user;
    }
}
