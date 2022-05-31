package com.otl.tarangplus.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampaginThings {

    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("campaign_data")
    @Expose
    private CampaignData campaignData;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public CampaignData getCampaignData() {
        return campaignData;
    }

    public void setCampaignData(CampaignData campaignData) {
        this.campaignData = campaignData;
    }

}
