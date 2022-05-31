
package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MidRoleSettings {

    //    @SerializedName("ads")
//    @Expose
//    private Ads ads;
    @SerializedName("midroll_position")
    @Expose
    private ArrayList<Integer> midrollPositions;

    @SerializedName("ads_url")
    @Expose
    private String adsUrl;

    public String getAdsUrl() {
        return adsUrl;
    }

    public void setAdsUrl(String adsUrl) {
        this.adsUrl = adsUrl;
    }


//    public Ads getAds() {
//        return ads;
//    }
//
//    public void setAds(Ads ads) {
//        this.ads = ads;
//    }


    public ArrayList<Integer> getMidrollPositions() {
        return midrollPositions;
    }

    public void setMidrollPositions(ArrayList<Integer> midrollPositions) {
        this.midrollPositions = midrollPositions;
    }
}
