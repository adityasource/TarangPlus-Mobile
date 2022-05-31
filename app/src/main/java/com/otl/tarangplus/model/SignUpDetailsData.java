package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpDetailsData {
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("email_verification_flag")
    @Expose
    private Boolean emailVerificationFlag;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("registered_using")
    @Expose
    private String registeredUsing;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("add_profile")
    @Expose
    private Boolean addProfile;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("profile_obj")
    @Expose
    private ProfileObj profileObj;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Boolean getEmailVerificationFlag() {
        return emailVerificationFlag;
    }

    public void setEmailVerificationFlag(Boolean emailVerificationFlag) {
        this.emailVerificationFlag = emailVerificationFlag;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegisteredUsing() {
        return registeredUsing;
    }

    public void setRegisteredUsing(String registeredUsing) {
        this.registeredUsing = registeredUsing;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Boolean getAddProfile() {
        return addProfile;
    }

    public void setAddProfile(Boolean addProfile) {
        this.addProfile = addProfile;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public ProfileObj getProfileObj() {
        return profileObj;
    }

    public void setProfileObj(ProfileObj profileObj) {
        this.profileObj = profileObj;
    }

}
