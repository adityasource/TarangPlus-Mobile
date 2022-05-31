package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampaignData {

    @SerializedName("pk_campaign")
    @Expose
    private String pkCampaign;
    @SerializedName("pk_kwd")
    @Expose
    private String pkKwd;
    @SerializedName("pk_source")
    @Expose
    private String pkSource;
    @SerializedName("pk_medium")
    @Expose
    private String pkMedium;
    @SerializedName("pk_content")
    @Expose
    private String pkContent;
    @SerializedName("is_data_received")
    @Expose
    private Boolean isDataReceived;

    public String getPkCampaign() {
        return pkCampaign;
    }

    public void setPkCampaign(String pkCampaign) {
        this.pkCampaign = pkCampaign;
    }

    public String getPkKwd() {
        return pkKwd;
    }

    public void setPkKwd(String pkKwd) {
        this.pkKwd = pkKwd;
    }

    public String getPkSource() {
        return pkSource;
    }

    public void setPkSource(String pkSource) {
        this.pkSource = pkSource;
    }

    public String getPkMedium() {
        return pkMedium;
    }

    public void setPkMedium(String pkMedium) {
        this.pkMedium = pkMedium;
    }

    public String getPkContent() {
        return pkContent;
    }

    public void setPkContent(String pkContent) {
        this.pkContent = pkContent;
    }

    public Boolean getIsDataReceived() {
        return isDataReceived;
    }

    public void setIsDataReceived(Boolean isDataReceived) {
        this.isDataReceived = isDataReceived;
    }

}
