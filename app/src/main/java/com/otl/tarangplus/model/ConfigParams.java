package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigParams {

    @SerializedName("android_version")
    @Expose
    private AndroidVersion androidVersion;
    @SerializedName("ios_version")
    @Expose
    private IosVersion iosVersion;
    @SerializedName("dnd")
    @Expose
    private List<Object> dnd = null;
    @SerializedName("offline_preferences")
    @Expose
    private List<Object> offlinePreferences = null;
    @SerializedName("reminder_interval")
    @Expose
    private String reminderInterval;
    @SerializedName("faq")
    @Expose
    private Object faq;
    @SerializedName("contact_us")
    @Expose
    private Object contactUs;
    @SerializedName("t&c")
    @Expose
    private Object tC;
    @SerializedName("privacy_policy")
    @Expose
    private Object privacyPolicy;
    @SerializedName("web_portal_url")
    @Expose
    private Object webPortalUrl;
    @SerializedName("offline_deletion_days")
    @Expose
    private String offlineDeletionDays;
    @SerializedName("global_view_count")
    @Expose
    private String globalViewCount;
    @SerializedName("commentable")
    @Expose
    private String commentable;
    @SerializedName("constant_quality")
    @Expose
    private String constantQuality;

    @SerializedName("payment_url")
    @Expose
    private String paymentURL;
    @SerializedName("registered_devices_url")
    @Expose
    private String registeredDevicesURL;




    @SerializedName("layout_scheme")
    @Expose
    private List<LayoutScheme> layoutScheme = null;

    @SerializedName("web_pages")
    private WebPages webPages;

    @SerializedName("profile_limit")
    @Expose
    private int profileLimit;


    @SerializedName("free_trial_config")
    @Expose
    private FreeTrialConfig freeTrialConfig;

    public FreeTrialConfig getFreeTrialConfig() {
        return freeTrialConfig;
    }

    public void setFreeTrialConfig(FreeTrialConfig freeTrialConfig) {
        this.freeTrialConfig = freeTrialConfig;
    }

    public int getProfileLimit() {
        return profileLimit;
    }

    public void setProfileLimit(int profileLimit) {
        this.profileLimit = profileLimit;
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Analytics analytics) {
        this.analytics = analytics;
    }

    @SerializedName("analytics")
    @Expose
    private Analytics analytics;

    public WebPages getWebPages() {
        return webPages;
    }

    public void setWebPages(WebPages webPages) {
        this.webPages = webPages;
    }

    public AndroidVersion getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(AndroidVersion androidVersion) {
        this.androidVersion = androidVersion;
    }

    public IosVersion getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(IosVersion iosVersion) {
        this.iosVersion = iosVersion;
    }

    public List<Object> getDnd() {
        return dnd;
    }

    public void setDnd(List<Object> dnd) {
        this.dnd = dnd;
    }

    public List<Object> getOfflinePreferences() {
        return offlinePreferences;
    }

    public void setOfflinePreferences(List<Object> offlinePreferences) {
        this.offlinePreferences = offlinePreferences;
    }

    public String getReminderInterval() {
        return reminderInterval;
    }

    public void setReminderInterval(String reminderInterval) {
        this.reminderInterval = reminderInterval;
    }

    public Object getFaq() {
        return faq;
    }

    public void setFaq(Object faq) {
        this.faq = faq;
    }

    public Object getContactUs() {
        return contactUs;
    }

    public void setContactUs(Object contactUs) {
        this.contactUs = contactUs;
    }

    public Object getTC() {
        return tC;
    }

    public void setTC(Object tC) {
        this.tC = tC;
    }

    public Object getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(Object privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public Object getWebPortalUrl() {
        return webPortalUrl;
    }

    public void setWebPortalUrl(Object webPortalUrl) {
        this.webPortalUrl = webPortalUrl;
    }

    public String getOfflineDeletionDays() {
        return offlineDeletionDays;
    }

    public void setOfflineDeletionDays(String offlineDeletionDays) {
        this.offlineDeletionDays = offlineDeletionDays;
    }

    public String getGlobalViewCount() {
        return globalViewCount;
    }

    public void setGlobalViewCount(String globalViewCount) {
        this.globalViewCount = globalViewCount;
    }

    public String getCommentable() {
        return commentable;
    }

    public void setCommentable(String commentable) {
        this.commentable = commentable;
    }

    public String getConstantQuality() {
        return constantQuality;
    }

    public void setConstantQuality(String constantQuality) {
        this.constantQuality = constantQuality;
    }

    public List<LayoutScheme> getLayoutScheme() {
        return layoutScheme;
    }

    public void setLayoutScheme(List<LayoutScheme> layoutScheme) {
        this.layoutScheme = layoutScheme;
    }

    public String getPaymentURL() {
        return paymentURL;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
    }

    public String getRegisteredDevicesURL() {
        return registeredDevicesURL;
    }

    public void setRegisteredDevicesURL(String registeredDevicesURL) {
        this.registeredDevicesURL = registeredDevicesURL;
    }
}