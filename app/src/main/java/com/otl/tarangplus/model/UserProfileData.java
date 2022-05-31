package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileData {
    @SerializedName("profiles")
    @Expose
    private List<Profile> profiles = null;
    @SerializedName("auth_token")
    @Expose
    private String mAuthToken;
    @SerializedName("parental_control")
    @Expose
    private String parentalControl;
    @SerializedName("parental_pin")
    @Expose
    private String parentalPin;

    public String getParentalControl() {
        return parentalControl;
    }

    public void setParentalControl(String parentalControl) {
        this.parentalControl = parentalControl;
    }

    public String getParentalPin() {
        return parentalPin;
    }

    public void setParentalPin(String parentalPin) {
        this.parentalPin = parentalPin;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getmAuthToken() {
        return mAuthToken;
    }

    public void setmAuthToken(String mAuthToken) {
        this.mAuthToken = mAuthToken;
    }
}
