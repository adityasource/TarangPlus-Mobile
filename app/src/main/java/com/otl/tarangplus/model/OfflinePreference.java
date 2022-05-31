package com.otl.tarangplus.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfflinePreference {

    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("display_name")
    @Expose
    private String displayName;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}

