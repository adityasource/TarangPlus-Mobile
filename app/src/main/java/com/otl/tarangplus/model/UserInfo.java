package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srishesh on 12/5/18.
 */

public class UserInfo {

    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("user_state")
    @Expose
    private String userState;
    @SerializedName("user_period")
    @Expose
    private String userPeriod;
    @SerializedName("user_pack_name")
    @Expose
    private String userPackName;
    @SerializedName("user_plan_type")
    @Expose
    private String userPlanType;
    @SerializedName("analytics_user_id")
    @Expose
    private String analyticsUserId;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserPeriod() {
        return userPeriod;
    }

    public void setUserPeriod(String userPeriod) {
        this.userPeriod = userPeriod;
    }

    public String getUserPackName() {
        return userPackName;
    }

    public void setUserPackName(String userPackName) {
        this.userPackName = userPackName;
    }

    public String getUserPlanType() {
        return userPlanType;
    }

    public void setUserPlanType(String userPlanType) {
        this.userPlanType = userPlanType;
    }

    public String getAnalyticsUserId() {
        return analyticsUserId;
    }

    public void setAnalyticsUserId(String analyticsUserId) {
        this.analyticsUserId = analyticsUserId;
    }
}
