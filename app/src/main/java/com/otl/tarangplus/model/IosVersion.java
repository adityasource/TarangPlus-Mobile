package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IosVersion {

    @SerializedName("current_version")
    @Expose
    private String currentVersion;
    @SerializedName("min_version")
    @Expose
    private String minVersion;
    @SerializedName("force_upgrade")
    @Expose
    private String forceUpgrade;
    @SerializedName("message")
    @Expose
    private String message;

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(String forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}