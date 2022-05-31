package com.otl.tarangplus.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.otl.tarangplus.model.CampaignData;
import com.otl.tarangplus.model.ReferalKeys;
import com.otl.tarangplus.MyApplication;

import java.util.List;

public class PreferenceHandler {

    public static final String USER_DETAIL = "USER_DETAIL";
    public static final String PERMANENT_STORE = "premenent_store";

    static public SharedPreferences.Editor getUserDetailEditor(Context c) {

            SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
            return preferences.edit();

    }

    static public String getLang(Context c) {
        try {
            SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
            //return preferences.getString("LangEnglish", Constants.NO);
            return preferences.getString("AppLang","");

        } catch (Exception e) {
            return null;
        }

    }
    static public void setLang(Context c, String lang) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("AppLang", lang);
        editor.commit();
    }

    static public String getNotificationOn(Context c) {
        try {
            SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
            return preferences.getString("NotificationOn", "");

        } catch (Exception e) {
            return null;
        }

    }
    static public void setNotificationOn(Context c, String type) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("NotificationOn", type);
        editor.commit();
    }


    static public boolean isLoggedIn(Context c) {
        try {
            SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
            return preferences.getBoolean("isLoggedIn", false);
        } catch (Exception e) {
            return false;
        }

    }
    static public void setLoggedInType(Context c, String type) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("loggedInType", type);
        editor.commit();
    }

    static public String getLoggedInType(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString("loggedInType", "");
    }

    static public void setIsLoggedIn(Context c, boolean isLoggedIn) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.commit();
    }

    static public void setUserAge(Context c, String age) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("age", age);
        editor.commit();
    }

    static public String getUserAge(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString("age", "");
    }


    static public void setUserGender(Context c, String gender) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("gender", gender);
        editor.commit();
    }

    static public String getUserGender(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString("gender", "");
    }

    static public void setParentEnabled(Context c, boolean isParentEnabled) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putBoolean("isParentEnabled", isParentEnabled);
        editor.commit();
    }

    static public boolean getIsParentControlEnabled(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean("isParentEnabled", false);
    }

    static public String getSessionId(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        Constants.CUR_SESSION_ID = preferences.getString("sessionID", "");
        return preferences.getString("sessionID", "");
    }

    static public void setSessionId(Context c, String sessionId) {
        Constants.CUR_SESSION_ID = sessionId;
        SharedPreferences.Editor editor = null;
        try {
            editor = getUserDetailEditor(c);
        }catch (Exception ex){
            return;
        }
        editor.putString("sessionID", sessionId);
        editor.commit();
    }

    static public void setRegion(Context c, String region) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString(Constants.REGION_TAG, region);
        editor.commit();
    }

    static public String getRegion(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.REGION_TAG, "IN");
    }

    static public void setCurrentProfileName(Context c, String name) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString(Constants.PROFILE_NAME, name);
        editor.commit();
    }

    static public void setSVODActive(Context c, boolean name) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putBoolean(Constants.SVOD_ACTIVE, name);
        editor.commit();
    }

    static public boolean getSVODActive(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.SVOD_ACTIVE, false);
    }

    static public String getCurrentProfileName(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.PROFILE_NAME, "");
    }

    static public void setCurrentProfileID(Context c, String profileId) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString(Constants.PROFILE_ID, profileId);
        editor.commit();
    }

    static public String getCurrentProfileID(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.PROFILE_ID, "");
    }

    static public boolean isKidsProfileActive(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.IS_KID_PROFILE_SELECTED, false);
    }

    static public void setIsKidProfile(Context c, boolean isKid) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putBoolean(Constants.IS_KID_PROFILE_SELECTED, isKid);
        editor.commit();
    }


    public static void clearAll(Context c) {
        if (c != null) {
            SharedPreferences.Editor editor = getUserDetailEditor(c);
            editor.clear();
            editor.commit();
            Constants.CUR_SESSION_ID = "";
        }
    }

    public static void setNotificationEnabled(Context c, boolean enabled) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putBoolean(Constants.IS_NOTIFICATION_ENABLED, enabled);
        editor.commit();
//        setCleverTapNotification(c, enabled);

    }

    public static void setAndroidVersion(Context c, String str) {
        SharedPreferences.Editor editor = getUserDetailEditorPer(c);
        editor.putString("ANDROID_VERSION", str);
        editor.commit();
    }
    static public String getAndroidVersion(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, Context.MODE_PRIVATE);
        return preferences.getString("ANDROID_VERSION", "0.0");
    }

    public static void setForceUpgrade(Context c, Boolean force) {
        SharedPreferences.Editor editor = getUserDetailEditorPer(c);
        editor.putBoolean("FORCE_UPGRADE", force);
        editor.commit();
    }
    static public Boolean getForceUpgrade(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, Context.MODE_PRIVATE);
        return preferences.getBoolean("FORCE_UPGRADE", false);
    }

    private static void setCleverTapNotification(Context c, boolean enabled) {
//        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
//        profileUpdate.put("Identity", PreferenceHandler.getAnalyticsUserId(c));                    // String or number   get User ID
//        profileUpdate.put("MSG-push", enabled);                        // Enable push notifications_new
//        CleverTapAPI cleverTap = null;
//        try {
//            cleverTap = CleverTapAPI.getInstance(c);
//            cleverTap.profile.push(profileUpdate);
//        } catch (CleverTapMetaDataNotFoundException e) {
//            e.printStackTrace();
//        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
//            cleverTapPermissionsNotSatisfied.printStackTrace();
//        }
    }

    public static boolean getNotificationEnabled(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.IS_NOTIFICATION_ENABLED, true);
    }

    public static void isProfileChanged(Context c, boolean b) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putBoolean(Constants.IS_PROFILE_SELECTED, b);
        editor.commit();
    }

    public static boolean getProfileSelected(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.IS_PROFILE_SELECTED, false);
    }

    static public void setUserLoginId(Context c, String userId) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("User_Id", userId);
        editor.commit();
    }

    static public String getUserId(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString("User_Id", "");
    }


    static public void setPaymentUrl(Context c, String userId) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("payment_URL", userId);
        editor.commit();
    }

    static public String getPaymentUrl(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString("payment_URL", "");
    }

    public static void setAnalyticsUserId(Context c, String analyticsUserId) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        if (analyticsUserId == null) {
            editor.putString(Constants.ANALYTICS_ID, "");
        } else
            editor.putString(Constants.ANALYTICS_ID, analyticsUserId);
        editor.commit();
    }

    static public String getAnalyticsUserId(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.ANALYTICS_ID, null);
    }

    public static void setUserPeriod(Context context, String userPeriod) {
        SharedPreferences.Editor editor = getUserDetailEditor(context);
        editor.putString(Constants.USER_PERIOD, userPeriod);
        editor.commit();
    }

    static public String getUserPeriod(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_PERIOD, null);
    }
    public static void setUserState(Context instance, String userState) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putString(Constants.USER_STATE_VALUE, userState);
        editor.commit();
    }

    public static String getUserState(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_STATE_VALUE, "");
    }

    public static void setUserPackName(Context context, String userPeriod) {
        SharedPreferences.Editor editor = getUserDetailEditor(context);
        editor.putString(Constants.USER_PACK_NAME, userPeriod);
        editor.commit();
    }

    static public String getPackName(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_PACK_NAME, null);
    }


    public static void setUserPlanType(Context context, String userPeriod) {
        SharedPreferences.Editor editor = getUserDetailEditor(context);
        editor.putString(Constants.USER_PLAN_TYPE, userPeriod);
        editor.commit();
    }

    static public String getUserPlanType(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_PLAN_TYPE, null);
    }

    public static void setIsSubscribed(Context context, boolean subscribed) {
        SharedPreferences.Editor editor = getUserDetailEditor(context);
        editor.putBoolean(Constants.IS_SUBSCRIBED, subscribed);
        editor.commit();
    }

    static public boolean getIsSubscribed(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.IS_SUBSCRIBED, false);
    }

    public static void setFAQUrl(MyApplication instance, String faq) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putString(Constants.FAQ_URL, faq);
        editor.commit();
    }

    public static String getFAQURL(MyApplication instance) {
        SharedPreferences preferences = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.FAQ_URL, "");
    }

    public static void setHelpURL(MyApplication instance, String help) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putString(Constants.HELP_URL, help);
        editor.commit();
    }

    public static String getHelpURL(MyApplication instance) {
        SharedPreferences preferences = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.HELP_URL, "");
    }

    public static void setPrivacyUrl(MyApplication instance, String privacyPolicy) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putString(Constants.PRIVACY_POLICY, privacyPolicy);
        editor.commit();
    }

    public static String getPrivacyPolicy(MyApplication instance) {
        SharedPreferences preferences = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.PRIVACY_POLICY, "");
    }

    public static void setTermsAndConditionUrl(MyApplication instance, String termsAndConditions) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putString(Constants.TERMS_AND_CONDITION, termsAndConditions);
        editor.commit();
    }

    public static String getTermsAndCondition(MyApplication instance) {
        SharedPreferences preferences = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.TERMS_AND_CONDITION, "");
    }

    public static void setContactUsUrl(MyApplication instance, String termsAndConditions) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putString(Constants.CONTACT_US, termsAndConditions);
        editor.commit();
    }

    public static String getContactUs(MyApplication instance) {
        SharedPreferences preferences = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.CONTACT_US, "");
    }

    public static void setMediaActiveInterval(String mediaActiveXMin, MyApplication instance) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putString(Constants.MEDIA_ACTIVE_INTERVAL, mediaActiveXMin);
        editor.commit();
    }

    public static String getMediaActiveInterval(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.MEDIA_ACTIVE_INTERVAL, null);
    }

    public static String getUserName(MyApplication myApplication) {
        SharedPreferences preferences = myApplication.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.USER_NAME, "");
    }

    public static void setUserName(MyApplication instace, String name) {
        SharedPreferences.Editor editor = getUserDetailEditor(instace);
        editor.putString(Constants.USER_NAME, name);
        editor.commit();
    }

    public static void setUserProfileLimit(MyApplication instance, int limit) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putInt(Constants.PROFILE_LIMIT, limit);
        editor.commit();
    }

    public static int getUserProfileLimit(MyApplication application) {
        SharedPreferences preferences = application.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getInt(Constants.PROFILE_LIMIT, 5);
    }

    public static void setCleverMediaEnabled(boolean clevertap_mediaevents, MyApplication instance) {
        SharedPreferences.Editor editor = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.IS_CLEVER_TAP_ENABLED, clevertap_mediaevents);
        editor.commit();
    }

    public static boolean isCleverTapEnabled(MyApplication application) {
        SharedPreferences preferences = application.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.IS_CLEVER_TAP_ENABLED, false);
    }

    public static void udpateSubcategories(List<String> subCategories) {
        String join = TextUtils.join(",", subCategories);
        SharedPreferences.Editor editor = MyApplication.getInstance().getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SUB_CATEGORY_LIST, join);
        editor.commit();
    }

    public static String getSubcategoryIds() {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.SUB_CATEGORY_LIST, "");
    }


    public static void udpateActivePlans(List<String> activePlans) {
        String join = TextUtils.join(",", activePlans);
        SharedPreferences.Editor editor = MyApplication.getInstance().getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SUB_ACTIVE_PLANS, join);
        editor.commit();
    }

    public static String getActivePlans() {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.SUB_ACTIVE_PLANS, "");
    }


    public static void udpateInActivePlans(List<String> inActivePlans) {
        String join = TextUtils.join(",", inActivePlans);
        SharedPreferences.Editor editor = MyApplication.getInstance().getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SUB_INACTIVE_PLANS, join);
        editor.commit();
    }

    public static String getInActivePlans() {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.SUB_INACTIVE_PLANS, "");
    }


    public static void setFirstInstalled(MyApplication instance, boolean installed) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putBoolean(Constants.FIRST_INSTALLED, installed);
        editor.commit();
    }

    public static boolean getFirstInstalled(MyApplication instance) {
        SharedPreferences preferences = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.FIRST_INSTALLED, false);
    }


    public static CampaignData getReferalDetails(Context myApplication) {
        SharedPreferences preferences = myApplication.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("MyObject", "");
        return gson.fromJson(json, CampaignData.class);

    }

    public static void setReferalDetails(Context application, CampaignData referenceObj) {
        SharedPreferences.Editor editor = getUserDetailEditor(application);
        Gson gson = new Gson();
        String json = gson.toJson(referenceObj);
        editor.putString("MyObject", json);
        editor.commit();
    }


    public static void setDataSendToOTT(Context instance, boolean installed) {
        SharedPreferences.Editor editor = getUserDetailEditor(instance);
        editor.putBoolean(Constants.DATA_SENT_TO_OTT, installed);
        editor.commit();
    }

    public static boolean getDataSendToOTT(Context instance) {
        SharedPreferences preferences = instance.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.DATA_SENT_TO_OTT, false);
    }

    public static ReferalKeys getReferalDetails(MyApplication myApplication) {
        SharedPreferences preferences = myApplication.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("MyObject", "");
        return gson.fromJson(json, ReferalKeys.class);

    }

    public static void setReferalDetails(MyApplication instace, ReferalKeys referenceObj) {
        SharedPreferences.Editor editor = getUserDetailEditor(instace);
        Gson gson = new Gson();
        String json = gson.toJson(referenceObj);
        editor.putString("MyObject", json);
        editor.commit();
    }

    public static void setTextForPackDetails(Context context, String userPeriod) {
        SharedPreferences.Editor editor = getUserDetailEditor(context);
        editor.putString(Constants.SETPACKDETAILSTEXT, userPeriod);
        editor.commit();
    }

    static public String getTextForPackDetails(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.SETPACKDETAILSTEXT, "We have a lot of entertainment in store \n for you! Browse through the plans to \n know what suits you best.");
    }

    public static void setIsFreeTrail(Context context, boolean subscribed) {
        SharedPreferences.Editor editor = getUserDetailEditor(context);
        editor.putBoolean("free-trail", subscribed);
        editor.commit();
    }

    static public boolean getIsFreeTrail(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean("free-trail", false);
    }

    static public SharedPreferences.Editor getUserDetailEditorPer(Context c) {
        if (c == null) {
            return null;
        }
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, c.MODE_PRIVATE);
        return preferences.edit();
    }


    static public String getDeviceId(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, c.MODE_PRIVATE);
        return preferences.getString(Constants.DEVICE_ID, "");
    }


    public static void setDeviceId(Context context, String deviceId) {
        SharedPreferences.Editor editor = getUserDetailEditorPer(context);
        editor.putString(Constants.DEVICE_ID, deviceId);
        editor.commit();
    }

    static public String getIp(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString("ip", "");
    }

    static public void setIp(Context c, String ip) {
        SharedPreferences.Editor editor = getUserDetailEditor(c);
        editor.putString("ip", ip);
        editor.commit();
    }



    public static String getUserEmailID(Context myApplication) {
        SharedPreferences preferences = myApplication.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getString(Constants.EMAIL_ID, "");
    }

    public static void setUserEMailId(Context instace, String name) {
        SharedPreferences.Editor editor = getUserDetailEditor(instace);
        editor.putString(Constants.EMAIL_ID, name);
        editor.commit();
    }

    static public String getStartTrailMessage(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, c.MODE_PRIVATE);
        return preferences.getString(Constants.START_TRAIL_MESSAGE, "");
    }


    public static void setStartTrailMessage(Context context, String message) {
        SharedPreferences.Editor editor = getUserDetailEditorPer(context);
        editor.putString(Constants.START_TRAIL_MESSAGE, message);
        editor.commit();
    }

    public static void setIsTrailAvaliable(Context context, boolean subscribed) {
        SharedPreferences.Editor editor = getUserDetailEditor(context);
        editor.putBoolean("free-trail-boo", subscribed);
        editor.commit();
    }

    static public boolean getIsTrailAvaliable(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        return preferences.getBoolean("free-trail-boo", false);
    }

    static public String getCityName(Context c) {
        if(c == null ){
            return "";
        }
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, Context.MODE_PRIVATE);
        return preferences.getString("city_name", "");
    }

    static public void setCityName(Context c, String cityName) {
        if(c == null ){
            return;
        }
        SharedPreferences.Editor editor = getUserDetailEditorPer(c);
        editor.putString("city_name", cityName);
        editor.commit();
    }

    public static void setFacebook(Context c, String str) {
        SharedPreferences.Editor editor = getUserDetailEditorPer(c);
        editor.putString("facebook", str);
        editor.commit();
    }
    static public String getFacebook(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, Context.MODE_PRIVATE);
        return preferences.getString("facebook", "");
    }
    public static void setGoogle(Context c, String str) {
        SharedPreferences.Editor editor = getUserDetailEditorPer(c);
        editor.putString("google", str);
        editor.commit();
    }
    static public String getGoogle(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, Context.MODE_PRIVATE);
        return preferences.getString("google", "");
    }
    public static void setNetworkType(Context c, String str) {
        SharedPreferences.Editor editor = getUserDetailEditorPer(c);
        editor.putString("networkType", str);
        editor.commit();
    }
 /*   static public String getNetworkType() {
        SharedPreferences preferences = c.getSharedPreferences(PERMANENT_STORE, Context.MODE_PRIVATE);
        return preferences.getString("networkType", "");
    }*/



}

