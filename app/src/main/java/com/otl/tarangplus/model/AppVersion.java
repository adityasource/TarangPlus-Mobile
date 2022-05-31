package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersion {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("upgrade_type")
    @Expose
    private String upgradeType;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("force_upgrade")
    @Expose
    private boolean force_upgrade;
    @SerializedName("recomended_upgrade")
    @Expose
    private boolean recomended_upgrade;
    @SerializedName("cancel_btn")
    @Expose
    private String cancel_btn;
    @SerializedName("proceed_btn")
    @Expose
    private String proceed_btn;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isForce_upgrade() {
        return force_upgrade;
    }

    public void setForce_upgrade(boolean force_upgrade) {
        this.force_upgrade = force_upgrade;
    }

    public boolean isRecomended_upgrade() {
        return recomended_upgrade;
    }

    public void setRecomended_upgrade(boolean recomended_upgrade) {
        this.recomended_upgrade = recomended_upgrade;
    }

    public String getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(String upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getCancel_btn() {
        return cancel_btn;
    }

    public void setCancel_btn(String cancel_btn) {
        this.cancel_btn = cancel_btn;
    }

    public String getProceed_btn() {
        return proceed_btn;
    }

    public void setProceed_btn(String proceed_btn) {
        this.proceed_btn = proceed_btn;
    }
}