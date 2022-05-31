
package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessControl {

    @SerializedName("is_free")
    @Expose
    private boolean isFree;
    @SerializedName("login_required")
    @Expose
    private Boolean loginRequired;
    @SerializedName("ads_available")
    @Expose
    private Boolean adsAvailable;
    @SerializedName("pre_role_settings")
    @Expose
    private PreRoleSettings preRoleSettings;
    @SerializedName("post_role_settings")
    @Expose
    private PostRoleSettings postRoleSettings;
    @SerializedName("overlay_settings")
    @Expose
    private OverlaySettings overlaySettings;
    @SerializedName("mid_role_settings")
    @Expose
    private MidRoleSettings midRoleSettings;
    @SerializedName("premium_tag")
    @Expose
    private boolean isPremiumTag;

    @SerializedName("vmap_url")
    @Expose
    private String vmap_url;

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isPremiumTag() {
        return isPremiumTag;
    }

    public void setPremiumTag(boolean premiumTag) {
        isPremiumTag = premiumTag;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Boolean getLoginRequired() {
        return loginRequired;
    }

    public void setLoginRequired(Boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public Boolean getAdsAvailable() {
        return adsAvailable;
    }

    public void setAdsAvailable(Boolean adsAvailable) {
        this.adsAvailable = adsAvailable;
    }

    public PreRoleSettings getPreRoleSettings() {
        return preRoleSettings;
    }

    public void setPreRoleSettings(PreRoleSettings preRoleSettings) {
        this.preRoleSettings = preRoleSettings;
    }

    public PostRoleSettings getPostRoleSettings() {
        return postRoleSettings;
    }

    public void setPostRoleSettings(PostRoleSettings postRoleSettings) {
        this.postRoleSettings = postRoleSettings;
    }

    public OverlaySettings getOverlaySettings() {
        return overlaySettings;
    }

    public void setOverlaySettings(OverlaySettings overlaySettings) {
        this.overlaySettings = overlaySettings;
    }

    public MidRoleSettings getMidRoleSettings() {
        return midRoleSettings;
    }

    public void setMidRoleSettings(MidRoleSettings midRoleSettings) {
        this.midRoleSettings = midRoleSettings;
    }

    public String getVmap_url() {
        return vmap_url;
    }

    public void setVmap_url(String vmap_url) {
        this.vmap_url = vmap_url;
    }
}
