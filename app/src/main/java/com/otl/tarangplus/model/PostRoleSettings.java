
package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRoleSettings {

//    @SerializedName("ads")
//    @Expose
//    private Ads ads;

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

}
