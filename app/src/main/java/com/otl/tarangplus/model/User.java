package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ankith on 24/11/15.
 */

public class User implements Serializable {

    @SerializedName("email_id")
    @Expose
    private String emailId;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("firstname")
    @Expose
    private String mFirstName;

    @SerializedName("lastname")
    @Expose
    private String mLastName;

    @SerializedName("confirm_password")
    @Expose
    private String confirmPassword;

    @SerializedName("key")
    @Expose
    private String mKey;

    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("ext_account_email_id")
    @Expose
    private String extAccountEmailId;

    @SerializedName("uid")
    @Expose
    private String userId;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @SerializedName("user_id")
    @Expose
    private String uId;



    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("profile_pic")
    @Expose
    private String profilePic;

    @SerializedName("parental_control")
    @Expose
    private String parentalControl;

    @SerializedName("parental_pin")
    @Expose
    private String parentalPin;

    @SerializedName("current_password")
    @Expose
    private String currentPassword;

    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;

    @SerializedName("device_data")
    @Expose
    private DeviceData deviceData;
    @SerializedName("current_sign_in_ip")
    @Expose
    private String currentSignInIp;

    public String getCurrentSignInIp() {
        return currentSignInIp;
    }

    public void setCurrentSignInIp(String currentSignInIp) {
        this.currentSignInIp = currentSignInIp;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

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

    public String getGdprConsentData() {
        return gdprConsentData;
    }

    public void setGdprConsentData(String gdprConsentData) {
        this.gdprConsentData = gdprConsentData;
    }

    public String getGdprCookiesData() {
        return gdprCookiesData;
    }

    public void setGdprCookiesData(String gdprCookiesData) {
        this.gdprCookiesData = gdprCookiesData;
    }

    @SerializedName("gdpr_consent_data")
    @Expose
    private String gdprConsentData;

    @SerializedName("gdpr_cookies_data")
    @Expose
    private String gdprCookiesData;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String mConfermPassword) {
        this.confirmPassword = mConfermPassword;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getExtAccountEmailId() {
        return extAccountEmailId;
    }

    public void setExtAccountEmailId(String extAccountEmailId) {
        this.extAccountEmailId = extAccountEmailId;
    }
}
