package com.otl.tarangplus.Analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.SplashScreenActivity;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.fragments.RegisterFragment;
import com.otl.tarangplus.model.CampaignData;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.Item;
import com.saranyu.ott.instaplaysdk.InstaPlayView;

import org.json.JSONException;
import org.json.JSONObject;
import org.matomo.sdk.QueryParams;
import org.matomo.sdk.TrackMe;
import org.matomo.sdk.Tracker;
import org.matomo.sdk.extra.CustomDimension;
import org.matomo.sdk.extra.TrackHelper;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class AnalyticsProvider {


    private final Tracker mTracker;
    private long mBufferStart;

    private static Date mOnPlayStartTime;    // When the user clicks the play button
    private static Date mAppLaunchTime;    // When the user clicks the app icon


    //    Notification Analytics
    public static boolean IS_CLEVERTAP_ENABLED = true;

    //    Firebase
    private static boolean IS_FIREBASE_ENABLED = true;

/*    public void branchSearch(Item item, String keySearched, Context activity) {

        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.SEARCH);
        branchEvent.setDescription("search_term");
        branchEvent.setSearchQuery(keySearched);
        branchEvent.addCustomDataProperty("search_term", keySearched);
        if (item.getContentId() != null)
            branchEvent.addCustomDataProperty("content_id", item.getContentId());
        if (item.getCategory() != null)
            branchEvent.addCustomDataProperty("category", item.getCategory());
        if (item.getCatalogName() != null)
            branchEvent.addCustomDataProperty("content_type", item.getCatalogName());
        if (item.getGenres() != null && item.getGenres().size() > 0)
            branchEvent.addCustomDataProperty("genre", item.getGenres().get(0));
        if (item.getLanguage() != null)
            branchEvent.addCustomDataProperty("language", item.getLanguage());
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);

    }

    public void branchAddToWatchList(String title, Context activity) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_WISHLIST);
        branchEvent.setDescription("wishlist").
                addCustomDataProperty("title", title);
        getDataForBranch(branchEvent, activity);
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchViewItem(String title, Context activity) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM);
        branchEvent.setDescription("page_view").addCustomDataProperty("title", title);
        getDataForBranch(branchEvent, activity);
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchShare(String title, Context activity) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.SHARE);
        branchEvent.setDescription("content_share").addCustomDataProperty("title", title);
        getDataForBranch(branchEvent, activity);
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchViewPlans(Context activity) {
        BranchEvent branchEvent = new BranchEvent("View_Plans");
        branchEvent.setDescription("view_plans");
        branchEvent.addCustomDataProperty("user_id", user_id);
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchLogin(String analyticsUserId, Context activity) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.LOGIN);
        branchEvent.setDescription(FirebaseAnalytics.Event.LOGIN)
                .addCustomDataProperty(FirebaseAnalytics.Event.LOGIN, analyticsUserId);
        branchEvent.addCustomDataProperty("user_id", analyticsUserId);
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchCompleteRegestration(String analyticsUserId, Context activity, String region) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.COMPLETE_REGISTRATION);
        branchEvent.setDescription("registration_complete").addCustomDataProperty("user_id", analyticsUserId);
        if (region != null && region.equalsIgnoreCase("US")) {
            branchEvent.addCustomDataProperty("email_id", PreferenceHandler.getUserId(activity));
        } else {
            branchEvent.addCustomDataProperty("user_mobile", PreferenceHandler.getUserId(activity));
        }
        branchEvent.addCustomDataProperty("status_signup", "success");
        // branchEvent.addCustomDataProperty("otp_status", "success");
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchSignUpOtpFailure(String otp, String mobileno, Context activity) {

        motamoSignUpOtpFailure(otp, mobileno, activity);

        BranchEvent branchEvent = new BranchEvent("SignUp_OTP_Failure");
        branchEvent.addCustomDataProperty("otp_status", "failure");
        branchEvent.addCustomDataProperty("user_mobile", mobileno);
        branchEvent.addCustomDataProperty("OTP_Received", otp);
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchAddToCart(String[] events, Context context) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_CART);
        try {

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }
            branchEvent.addCustomDataProperty("productName", events[0]);
            branchEvent.addCustomDataProperty("currencyType", events[1]);
            if (events[1].equalsIgnoreCase("INR") || events[1].equalsIgnoreCase("₹")) {
                branchEvent.setCurrency(CurrencyType.INR);
            } else {
                branchEvent.setCurrency(CurrencyType.USD);
            }
            branchEvent.addCustomDataProperty("productPrice", events[2]);
        } catch (Exception e) {
        }
        getExtraDataForBranch(branchEvent, context);
        finallyLogged(branchEvent, context);
    }

    public void branchPurchase(String[] events, Context context) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE);
        try {

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            if (events[1].equalsIgnoreCase("INR") || events[1].equalsIgnoreCase("₹")) {
                branchEvent.setRevenue(Float.parseFloat(events[2])).setCurrency(CurrencyType.INR).setDescription(events[3]);
            } else {
                branchEvent.setRevenue(Double.parseDouble(events[2])).setCurrency(CurrencyType.USD).setDescription(events[3]);
            }
            branchEvent.addCustomDataProperty("transaction_id", events[0]);
            branchEvent.addCustomDataProperty("productName", events[4]);

        } catch (Exception e) {
        }
        getExtraDataForBranch(branchEvent, context);
        finallyLogged(branchEvent, context);
    }

    //Android.doViewcart(plan_names+"|"+currency+"|"+price);

    public void branchViewCart(String[] events, Context context) {
        BranchEvent branchEvent = new BranchEvent("View_Cart");
        try {

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }
            branchEvent.addCustomDataProperty("productName", events[0]);
            if (events[1].equalsIgnoreCase("INR") || events[1].equalsIgnoreCase("₹")) {
                branchEvent.setRevenue(Float.parseFloat(events[2])).setCurrency(CurrencyType.INR).setDescription(events[3]);
            } else {
                branchEvent.setRevenue(Double.parseDouble(events[2])).setCurrency(CurrencyType.USD).setDescription(events[3]);
            }

        } catch (Exception e) {
        }

        getExtraDataForBranch(branchEvent, context);
        finallyLogged(branchEvent, context);
    }

    public void branchPlanClick(String[] events, Context context) {
        // Android.doPlanclick(pack_name+"|"+pack_duration+"|"+plan_amount+"|"+currency);

        try {
            BranchEvent branchEvent = new BranchEvent("Add_To_Cart_" + events[0]);

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }
            branchEvent.addCustomDataProperty("pack_name", events[0]);
            branchEvent.addCustomDataProperty("pack_duration", events[1]);
            branchEvent.addCustomDataProperty("plan_amount", events[2]);
            branchEvent.addCustomDataProperty("currency", events[3]);
            getExtraDataForBranch(branchEvent, context);
            finallyLogged(branchEvent, context);
        } catch (Exception e) {
        }

    }


    public void branchApplyPromocode(String[] events, Context context) {
        try {
            BranchEvent branchEvent = new BranchEvent(events[0]);
            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            branchEvent.addCustomDataProperty("pack_name", events[1]);
            branchEvent.addCustomDataProperty("plan_amount", events[2]);
            branchEvent.addCustomDataProperty("pack_duration", events[3]);
            branchEvent.addCustomDataProperty("pack_amount", events[4]);
            branchEvent.addCustomDataProperty("coupon_name", events[5]);


            getExtraDataForBranch(branchEvent, context);
            finallyLogged(branchEvent, context);
        } catch (Exception e) {

        }
    }

    public void branchStartTrail(String[] events, Context context) {
        // Android.doStarttrail("0|USD")
        try {
            BranchEvent branchEvent = new BranchEvent("START_TRIAL");

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            if (events[1].equalsIgnoreCase("INR") || events[1].equalsIgnoreCase("₹")) {
                branchEvent.setCurrency(CurrencyType.INR);
            } else {
                branchEvent.setCurrency(CurrencyType.USD);
            }
            branchEvent.addCustomDataProperty("amount", events[0]);

            getExtraDataForBranch(branchEvent, context);
            finallyLogged(branchEvent, context);

        } catch (Exception e) {

        }
    }

    public void branchTrail(String[] events, Context context) {
        // Android.doFreeTrailUsaSucess("Free_Trial_USA_Success_Users"+"|"+"0"+"|"+"USD");
        try {
            BranchEvent branchEvent = new BranchEvent(events[0]);

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            if (events[1].equalsIgnoreCase("INR") || events[1].equalsIgnoreCase("₹")) {
                branchEvent.setCurrency(CurrencyType.INR);
            } else {
                branchEvent.setCurrency(CurrencyType.USD);
            }
            branchEvent.addCustomDataProperty("amount", events[1]);

            getExtraDataForBranch(branchEvent, context);
            finallyLogged(branchEvent, context);

        } catch (Exception e) {

        }
    }


    public void branchIniatePurchase(String[] events, Context context) {

        //  Android.doInitiate_purcahse(contentname+"|"+currency_symbol+"|"+amountcharged);

        try {
            BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.INITIATE_PURCHASE);

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            branchEvent.addCustomDataProperty("productName", events[0]);
            if (events[1].equalsIgnoreCase("INR") || events[1].equalsIgnoreCase("₹")) {
                branchEvent.setCurrency(CurrencyType.INR);
            } else {
                branchEvent.setCurrency(CurrencyType.USD);
            }
            branchEvent.addCustomDataProperty("productPrice", events[2]);

            getExtraDataForBranch(branchEvent, context);
            finallyLogged(branchEvent, context);

        } catch (Exception e) {

        }
    }

    public void branchTransactionSuccess(String[] events, Context context) {
        // Android.doTransactionsuccess(transaction_id+"|"+plan_name+"|"+price);
        BranchEvent branchEvent = new BranchEvent("Transaction_Success");

        try {

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            branchEvent.addCustomDataProperty("transaction_id", events[0]);
            branchEvent.addCustomDataProperty("pack_name", events[1]);
            branchEvent.addCustomDataProperty("pack_value", events[2]);


        } catch (Exception e) {

        }
        getExtraDataForBranch(branchEvent, context);
        finallyLogged(branchEvent, context);

    }

    public void branchTransactionFailed(String[] events, Context context) {
        // Android.doTransactionfailed(transaction_id+"|"+plan_name+"|"+price+"|"+status_message+"|"+message);
        BranchEvent branchEvent = new BranchEvent("Transaction_Failed");

        try {
            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            branchEvent.addCustomDataProperty("transaction_id", events[0]);
            branchEvent.addCustomDataProperty("pack_name", events[1]);
            branchEvent.addCustomDataProperty("pack_value", events[2]);
            branchEvent.addCustomDataProperty("transaction_status", events[3]);
            branchEvent.addCustomDataProperty("failure_reason", events[4]);

        } catch (Exception e) {

        }
        getExtraDataForBranch(branchEvent, context);
        finallyLogged(branchEvent, context);
    }

    public void branchsubMonthlyPack(String[] events, Context context) {

        //        Android.doSubsmonthlypack(transaction_id+""|""+plan_name);
        BranchEvent branchEvent = new BranchEvent("Subscription_MonthlyPacks");


        try {

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            branchEvent.addCustomDataProperty("transaction_id", events[0]);
            branchEvent.addCustomDataProperty("pack_name", events[1]);

        } catch (Exception e) {
        }
        getExtraDataForBranch(branchEvent, context);
        finallyLogged(branchEvent, context);
    }

    public void branchsubAnualPack(String[] events, Context context) {

        // Android.doSubsannualpack(transaction_id+"|"+plan_name);
        BranchEvent branchEvent = new BranchEvent("Subscription_AnnualPacks");

        try {

            if (PreferenceHandler.isLoggedIn(context)) {
                branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(context));
            }

            branchEvent.addCustomDataProperty("transaction_id", events[0]);
            branchEvent.addCustomDataProperty("pack_name", events[1]);

        } catch (Exception e) {

        }

        getExtraDataForBranch(branchEvent, context);
        finallyLogged(branchEvent, context);
    }


    public void branchSignUpOtpSuccess(Context activity) {

        motamoSignUpSuccess(activity);

        BranchEvent branchEvent = new BranchEvent("SignUp_OTP_Success");
        branchEvent.addCustomDataProperty("otp_status", "success");
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchSignUpOtpEntered(Context activity) {

        motamoSignUpOTPStep(activity);

        BranchEvent branchEvent = new BranchEvent("SignUp_OTP_Step");
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchSignUpInitiated(Context activity) {

        motamoSignUpInitiated(activity);

        BranchEvent branchEvent = new BranchEvent("SignUp_Initiated");
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchForgotPassword(Context activity) {

        motamoForgotPassword(activity);

        BranchEvent branchEvent = new BranchEvent("Forgot_Password");
        branchEvent.setDescription("forgot_password");
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }

    public void branchFreeTrialIndiaSuccess(Context activity) {

        netCoreFreeTrialIndiaSuccess(activity);
        matomoFreeTrialIndiaSuccess(activity);

        BranchEvent branchEvent = new BranchEvent("Free_Trial_India_Success_Users");
        branchEvent.setDescription("Free_Trial_India_Success_Users");
        if (PreferenceHandler.isLoggedIn(activity))
            branchEvent.addCustomDataProperty("user_id", PreferenceHandler.getAnalyticsUserId(activity));
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);
    }*/

    public void netCoreFreeTrialIndiaSuccess(Context activity) {

        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();
        if (PreferenceHandler.isLoggedIn(activity))
            bundle.putString("user_id", PreferenceHandler.getAnalyticsUserId(activity));
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        //NetcoreSDK.track(mContext, "Free_Trial_India_Success_Users", payloadDetails.toString());
    }

    public void matomoFreeTrialIndiaSuccess(Context activity) {
        TrackMe trackMe = new TrackMe();

        if (PreferenceHandler.isLoggedIn(activity))
            CustomDimension.setDimension(trackMe, 14, PreferenceHandler.getAnalyticsUserId(activity));
        getExtraDataForMotamo(trackMe, activity);
        /*todo aditya*/
        //TrackHelper.track(trackMe).event("Users", "Activity").name("Free_Trial_India_Success_Users").with(mTracker);

    }

    public void netCoreForgotPassword(Context activity) {
        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        //NetcoreSDK.track(mContext, "Forgot_Password", payloadDetails.toString());
    }

    public void netCoreSignUpInitiated(Context activity) {
        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        //NetcoreSDK.track(mContext, "SignUp_Initiated", payloadDetails.toString());
    }

    public void netCoreSignUpOTPStep(Context activity) {
        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        //NetcoreSDK.track(mContext, "SignUp_OTP_Step", payloadDetails.toString());
    }

    public void netCoreSignUpSuccess(Context activity) {

        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();
        bundle.putString("otp_status", "success");
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        //NetcoreSDK.track(mContext, "SignUp_OTP_Success", payloadDetails.toString());
    }

    public void netcoreLogin(String analyticsUserId, Context activity) {
        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();
        bundle.putString("user_id", analyticsUserId);
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        //NetcoreSDK.track(mContext, FirebaseAnalytics.Event.LOGIN, payloadDetails.toString());
    }


    public void netCoreSignUpOtpFailure(String otp, String mobileno, Context activity) {

        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();
        bundle.putString("otp_status", "failure");
        bundle.putString("user_mobile", mobileno);
        bundle.putString("OTP_Received", otp);
        getExtraDataForNetCOre(bundle, activity);

        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        //NetcoreSDK.track(mContext, "SignUp_OTP_Failure", payloadDetails.toString());
    }

    public void netCoreCompleteRegestration(String analyticsUserId, Context activity, String region) {

        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();

        bundle.putString("user_id", analyticsUserId);
        if (region != null && region.equalsIgnoreCase("US")) {
            bundle.putString("email_id", PreferenceHandler.getUserId(activity));
        } else {
            bundle.putString("user_mobile", PreferenceHandler.getUserId(activity));
        }
        bundle.putString("status_signup", "success");
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*todo aditya*/
        ////todo aditya //NetcoreSDK.track(mContext, "registration_complete", payloadDetails.toString());
    }


    public void branchMediaClickData(Context activity, long pos) {

       /* //trigger motamo to
        matomoMediaClickData(activity, pos);

        BranchEvent branchEvent = new BranchEvent("Media_Click_Data");
        branchEvent.setDescription("Media_Click_Data");
        branchEvent.addCustomDataProperty("content_duration", "" + pos);
        branchEvent.addCustomDataProperty("media_title", ma_ti);
        getDataForBranch(branchEvent, activity);
        getExtraDataForBranch(branchEvent, activity);
        finallyLogged(branchEvent, activity);*/
    }


 /*   public void branchonClickOfBanner(CatalogListItem catalogListItem, Context activity, String mediaType) {
        if (PreferenceHandler.isLoggedIn(activity)) {
            if (PreferenceHandler.getIsSubscribed(activity)) {
                BranchEvent branchEvent = new BranchEvent("Banner_Clicks_Subscribed_User");
                branchEvent.addCustomDataProperty("user_id", user_id);
                branchEvent.addCustomDataProperty("banner_type", "carousel");
                getExtraDataForBranch(branchEvent, activity);
                getDataForBranch(branchEvent, activity);
                if (mediaType.equalsIgnoreCase("media"))
                    getBannerData(branchEvent, activity, catalogListItem);
                finallyLogged(branchEvent, activity);
            } else {
                BranchEvent branchEvent = new BranchEvent("Banner_Clicks_Logged_In_User");
                branchEvent.addCustomDataProperty("user_id", user_id);
                branchEvent.addCustomDataProperty("banner_type", "carousel");
                getDataForBranch(branchEvent, activity);
                if (mediaType.equalsIgnoreCase("media"))
                    getBannerData(branchEvent, activity, catalogListItem);
                getExtraDataForBranch(branchEvent, activity);
                finallyLogged(branchEvent, activity);
            }
        } else {
            BranchEvent branchEvent = new BranchEvent("Banner_Clicks_Anonymous_User");
            branchEvent.addCustomDataProperty("banner_type", "carousel");
            getExtraDataForBranch(branchEvent, activity);
            if (mediaType.equalsIgnoreCase("media"))
                getBannerData(branchEvent, activity, catalogListItem);
            finallyLogged(branchEvent, activity);
        }
    }


    public void branchonClickOfTabs(String tabName, Context activity) {
        if (PreferenceHandler.isLoggedIn(activity)) {
            if (PreferenceHandler.getIsSubscribed(activity)) {
                BranchEvent branchEvent = new BranchEvent("Viewed_" + tabName + "_Subscribed");
                branchEvent.addCustomDataProperty("user_id", user_id);
                branchEvent.addCustomDataProperty("category_name", tabName);
                branchEvent.addCustomDataProperty("selected_tab", tabName);
                getExtraDataForBranch(branchEvent, activity);
                finallyLogged(branchEvent, activity);
            } else {
                BranchEvent branchEvent = new BranchEvent("Viewed_" + tabName + "_LoggedIn");
                branchEvent.addCustomDataProperty("user_id", user_id);
                branchEvent.addCustomDataProperty("category_name", tabName);
                branchEvent.addCustomDataProperty("selected_tab", tabName);
                getExtraDataForBranch(branchEvent, activity);
                finallyLogged(branchEvent, activity);
            }
        } else {
            BranchEvent branchEvent = new BranchEvent("Viewed_" + tabName + "_Anonymous");
            branchEvent.addCustomDataProperty("category_name", tabName);
            branchEvent.addCustomDataProperty("selected_tab", tabName);
            getExtraDataForBranch(branchEvent, activity);
            finallyLogged(branchEvent, activity);
        }

    }*/


    public void updateBufferStartTime(long bufferstart) {
        this.mBufferStart = bufferstart;
    }

    public double updateBufferDiff(long bufferEnd) {
        long buff_time = bufferEnd - mBufferStart;
        double buff_sec = (buff_time / 1000.0);
        mBufferStart = 0;


        DecimalFormat df = new DecimalFormat("0.0");
        String diffValue = df.format(Double.parseDouble("" + buff_sec));


//        Log.d(TAG, "updateBufferDiff: " + buff_time);

        TrackMe trackMe = new TrackMe();
        TrackMe tracker = getMatomoPayload(trackMe);
        TrackHelper.track(trackMe).event("Buffer", "Activity").name("buff_time").value(Float.parseFloat(diffValue)).with(mTracker);

//        Firebase
        if (IS_FIREBASE_ENABLED) {
            Bundle bundle = new Bundle();
            getFirebasePayload(bundle);   // Buff Diff
            bundle.putFloat(Firebase.Params.BUFFER_TIME, Float.parseFloat(diffValue));
            //MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.BUFFER_TIME, bundle);
        }
        return buff_sec;
    }

    public void netCoreOnClickOfBanner(CatalogListItem catalogListItem, Context activity, String mediaType) {

        try {
            Bundle bundle = new Bundle();
            JSONObject payloadDetails = new JSONObject();

            matomoOnClickOfBanner(catalogListItem, activity, mediaType);

            if (PreferenceHandler.isLoggedIn(activity)) {
                if (PreferenceHandler.getIsSubscribed(activity)) {
                    bundle.putString("user_id", user_id);
                    bundle.putString("banner_type", "carousel");
                    getExtraDataForNetCOre(bundle, activity);
                    getFirebasePayload(bundle);
                    if (mediaType.equalsIgnoreCase("media"))
                        getBannerDataForNetcore(bundle, activity, catalogListItem);

                    payloadDetails.put("payload", getJsonObj(bundle));
                    //todo aditya //NetcoreSDK.track(mContext, "Banner_Clicks_Subscribed_User", payloadDetails.toString());
                } else {
                    bundle.putString("user_id", user_id);
                    bundle.putString("banner_type", "carousel");
                    getExtraDataForNetCOre(bundle, activity);
                    getFirebasePayload(bundle);
                    if (mediaType.equalsIgnoreCase("media"))
                        getBannerDataForNetcore(bundle, activity, catalogListItem);

                    payloadDetails.put("payload", getJsonObj(bundle));
                    //todo aditya //NetcoreSDK.track(mContext, "Banner_Clicks_Logged_In_User", payloadDetails.toString());
                }
            } else {
                bundle.putString("banner_type", "carousel");
                getExtraDataForNetCOre(bundle, activity);

                if (mediaType.equalsIgnoreCase("media"))
                    getBannerDataForNetcore(bundle, activity, catalogListItem);
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "Banner_Clicks_Anonymous_User", payloadDetails.toString());
            }
        } catch (Exception e) {

        }
    }

    private void getBannerDataForNetcore(Bundle bundle, Context activity, CatalogListItem catalogListItem) {
        if (catalogListItem != null) {
            if (catalogListItem.getCatalogName() != null)
                bundle.putString("category", catalogListItem.getCatalogName());
            if (catalogListItem.getTitle() != null)
                bundle.putString("media_title", catalogListItem.getTitle());
        }

    }


    public void netCoreOnClickOfTabs(String tabName, Activity activity) {

        try {
            Bundle bundle = new Bundle();
            JSONObject payloadDetails = new JSONObject();
            motamoOnClickOfTabs(tabName, activity);

            if (PreferenceHandler.isLoggedIn(activity)) {
                if (PreferenceHandler.getIsSubscribed(activity)) {
                    getExtraDataForNetCOre(bundle, activity);
                    bundle.putString("user_id", user_id);
                    bundle.putString("selected_tab", tabName);
                    bundle.putString("category_name", tabName);
                    payloadDetails.put("payload", getJsonObj(bundle));
                    //todo aditya //NetcoreSDK.track(mContext, "Viewed_" + tabName + "_Subscribed", payloadDetails.toString());
                } else {
                    getExtraDataForNetCOre(bundle, activity);
                    bundle.putString("user_id", user_id);
                    bundle.putString("category_name", tabName);
                    bundle.putString("selected_tab", tabName);
                    payloadDetails.put("payload", getJsonObj(bundle));
                    //todo aditya //NetcoreSDK.track(mContext, "Viewed_" + tabName + "_LoggedIn", payloadDetails.toString());
                }
            } else {
                getExtraDataForNetCOre(bundle, activity);
                bundle.putString("category_name", tabName);
                bundle.putString("selected_tab", tabName);
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "Viewed_" + tabName + "_Anonymous", payloadDetails.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getExtraDataForNetCOre(Bundle bundle, Context activity) {

        if (PreferenceHandler.isLoggedIn(activity)) {
            bundle.putString("device_id", Helper.getAndroidDeviceId(activity));
            bundle.putString("device_make", Helper.getDeviceName());
            bundle.putString("os", Helper.getAndroidVersion());
            bundle.putString("session_id", PreferenceHandler.getSessionId(activity));
            bundle.putString("city", PreferenceHandler.getCityName(activity));
            bundle.putString("region", PreferenceHandler.getRegion(activity));
            bundle.putString("date_event", Helper.getDate());
            bundle.putString("time_event", Helper.getTime());
        } else {
            bundle.putString("device_id", Helper.getAndroidDeviceId(activity));
            bundle.putString("device_make", Helper.getDeviceName());
            bundle.putString("os", Helper.getAndroidVersion());
            bundle.putString("city", PreferenceHandler.getCityName(activity));
            bundle.putString("region", PreferenceHandler.getRegion(activity));
            bundle.putString("date_event", Helper.getDate());
            bundle.putString("time_event", Helper.getTime());
        }
    }

    public TrackMe getExtraDataForMotamo(TrackMe trackMe, Context activity) {

        this.device_make = Helper.getDeviceName();
        this.device_id = Helper.getAndroidDeviceId(activity);
        this.os = Helper.getAndroidVersion();
        this.city = PreferenceHandler.getCityName(activity);
        this.region = PreferenceHandler.getRegion(activity);
        this.date_event = Helper.getDate();
        this.time_event = Helper.getTime();

        if (PreferenceHandler.isLoggedIn(activity)) {
            this.session_id = PreferenceHandler.getSessionId(activity);
            CustomDimension.setDimension(trackMe, 23, device_id);
            CustomDimension.setDimension(trackMe, 28, device_make);
            CustomDimension.setDimension(trackMe, 29, os);
            CustomDimension.setDimension(trackMe, 27, session_id);
            CustomDimension.setDimension(trackMe, 26, city);
            CustomDimension.setDimension(trackMe, 25, region);
            CustomDimension.setDimension(trackMe, 30, date_event);
            CustomDimension.setDimension(trackMe, 31, time_event);
        } else {
            CustomDimension.setDimension(trackMe, 23, device_id);
            CustomDimension.setDimension(trackMe, 28, device_make);
            CustomDimension.setDimension(trackMe, 29, os);
            CustomDimension.setDimension(trackMe, 26, city);
            CustomDimension.setDimension(trackMe, 25, region);
            CustomDimension.setDimension(trackMe, 30, date_event);
            CustomDimension.setDimension(trackMe, 31, time_event);
        }
        return trackMe;
    }

    public TrackMe getDataForMotamo(TrackMe trackMe, Context activity) {


        CustomDimension.setDimension(trackMe, 2, media_language);
        if (!TextUtils.isEmpty(media_category))
            CustomDimension.setDimension(trackMe, 3, media_category);    // null
        CustomDimension.setDimension(trackMe, 4, media_content_type);   // linear
        CustomDimension.setDimension(trackMe, 5, media_genre);
        CustomDimension.setDimension(trackMe, 7, media_bitrate);
        CustomDimension.setDimension(trackMe, 8, user_age);    // not going
        CustomDimension.setDimension(trackMe, 9, user_gender);  // not going
        CustomDimension.setDimension(trackMe, 10, state_user);
        CustomDimension.setDimension(trackMe, 11, user_period);
        CustomDimension.setDimension(trackMe, 12, user_plan_type);
        CustomDimension.setDimension(trackMe, 13, user_custom_plan);
        CustomDimension.setDimension(trackMe, 15, item_id);   // content ID
        CustomDimension.setDimension(trackMe, 16, ma_ti);   // content ID

        if (PreferenceHandler.isLoggedIn(activity)) {
            CustomDimension.setDimension(trackMe, 14, user_id);
        }

        return trackMe;

    }


    public void matomoOnClickOfBanner(CatalogListItem catalogListItem, Context activity, String mediaType) {

        try {

            TrackMe trackMe = new TrackMe();

            if (PreferenceHandler.isLoggedIn(activity)) {
                if (PreferenceHandler.getIsSubscribed(activity)) {
                    CustomDimension.setDimension(trackMe, 32, banner_type);
                    if (mediaType.equalsIgnoreCase("media"))
                        getBannerDataForMotamo(trackMe, activity, catalogListItem);
                    getExtraDataForMotamo(trackMe, activity);
                    getDataForMotamo(trackMe, activity);
                    TrackHelper.track(trackMe).event("media", "Activity").name("Banner_Clicks_Subscribed_User").with(mTracker);
                } else {
                    CustomDimension.setDimension(trackMe, 32, banner_type);

                    if (mediaType.equalsIgnoreCase("media"))
                        getBannerDataForMotamo(trackMe, activity, catalogListItem);
                    getExtraDataForMotamo(trackMe, activity);
                    getDataForMotamo(trackMe, activity);
                    TrackHelper.track(trackMe).event("media", "Activity").name("Banner_Clicks_Logged_In_User").with(mTracker);
                }
            } else {
                CustomDimension.setDimension(trackMe, 32, banner_type);
                if (mediaType.equalsIgnoreCase("media"))
                    getBannerDataForMotamo(trackMe, activity, catalogListItem);
                getExtraDataForMotamo(trackMe, activity);
                getDataForMotamo(trackMe, activity);
                TrackHelper.track(trackMe).event("media", "Activity").name("Banner_Clicks_Anonymous_User").with(mTracker);
            }
        } catch (Exception e) {

        }
    }

    private TrackMe getBannerDataForMotamo(TrackMe trackMe, Context activity, CatalogListItem catalogListItem) {
        if (catalogListItem != null) {
            if (catalogListItem.getCatalogName() != null)
                CustomDimension.setDimension(trackMe, 3, catalogListItem.getCatalogName());
            if (catalogListItem.getTitle() != null)
                CustomDimension.setDimension(trackMe, 16, catalogListItem.getTitle());
        }

        return trackMe;
    }

    public void motamoOnClickOfTabs(String tabName, Context activity) {

        TrackMe trackMe = new TrackMe();
        if (PreferenceHandler.isLoggedIn(activity)) {
            if (PreferenceHandler.getIsSubscribed(activity)) {
                CustomDimension.setDimension(trackMe, 14, user_id);
                CustomDimension.setDimension(trackMe, 33, tabName);
                getExtraDataForMotamo(trackMe, activity);
                TrackHelper.track(trackMe).event("media", "Activity").name("Viewed_" + tabName + "_Subscribed").with(mTracker);
            } else {
                CustomDimension.setDimension(trackMe, 14, user_id);
                CustomDimension.setDimension(trackMe, 33, tabName);
                getExtraDataForMotamo(trackMe, activity);
                TrackHelper.track(trackMe).event("media", "Activity").name("Viewed_" + tabName + "_LoggedIn").with(mTracker);
            }
        } else {
            CustomDimension.setDimension(trackMe, 33, tabName);
            getExtraDataForMotamo(trackMe, activity);
            TrackHelper.track(trackMe).event("media", "Activity").name("Viewed_" + tabName + "_Anonymous").with(mTracker);
        }

    }

    public void matomoSearch(Item item, String keySearched, Context activity) {


        TrackMe trackMe = new TrackMe();

        CustomDimension.setDimension(trackMe, 34, keySearched);
        if (item.getContentId() != null)
            CustomDimension.setDimension(trackMe, 15, item.getContentId());
        if (item.getCategory() != null)
            CustomDimension.setDimension(trackMe, 3, item.getCategory());
        if (item.getCatalogName() != null)
            CustomDimension.setDimension(trackMe, 4, item.getCatalogName());
        if (item.getGenres() != null && item.getGenres().size() > 0)
            CustomDimension.setDimension(trackMe, 5, item.getGenres().get(0));
        if (item.getLanguage() != null)
            CustomDimension.setDimension(trackMe, 2, item.getLanguage());
        getExtraDataForMotamo(trackMe, activity);

        TrackHelper.track(trackMe).event("media", "Activity").name("search").with(mTracker);

    }

    public void matomoMediaClickData(Context activity, long pos) {
        TrackMe trackMe = new TrackMe();

        CustomDimension.setDimension(trackMe, 35, "" + pos);
        CustomDimension.setDimension(trackMe, 16, ma_ti);
        getExtraDataForMotamo(trackMe, activity);
        getDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("media", "Activity").name("Media_Click_Data").with(mTracker);
    }


    public void motamoForgotPassword(Context activity) {
        TrackMe trackMe = new TrackMe();
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("Forgot_Password").with(mTracker);
    }

    public void motamoSignUpInitiated(Context activity) {
        TrackMe trackMe = new TrackMe();
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("SignUp_Initiated").with(mTracker);
    }

    public void motamoSignUpOTPStep(Context activity) {
        TrackMe trackMe = new TrackMe();
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("SignUp_OTP_Step").with(mTracker);
    }

    public void motamoSignUpSuccess(Context activity) {

        TrackMe trackMe = new TrackMe();
        CustomDimension.setDimension(trackMe, 36, "success");
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("SignUp_OTP_Success").with(mTracker);

    }

    public void motamoSignUpOtpFailure(String otp, String mobileno, Context activity) {

        TrackMe trackMe = new TrackMe();
        CustomDimension.setDimension(trackMe, 36, "failure");
        CustomDimension.setDimension(trackMe, 37, mobileno);
        CustomDimension.setDimension(trackMe, 38, otp);
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("SignUp_OTP_Failure").with(mTracker);

    }

    public void motamoCompleteRegestration(String analyticsUserId, Context activity, String region) {

        TrackMe trackMe = new TrackMe();

        CustomDimension.setDimension(trackMe, 14, analyticsUserId);
        if (region != null && region.equalsIgnoreCase("US")) {
            CustomDimension.setDimension(trackMe, 40, PreferenceHandler.getUserId(activity));
        } else {
            CustomDimension.setDimension(trackMe, 37, PreferenceHandler.getUserId(activity));
        }

        CustomDimension.setDimension(trackMe, 39, "success");
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("registration_complete").with(mTracker);
    }


    public void netCorehMediaClickData(Context activity, long pos) {


        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();

        bundle.putString("content_duration", "" + pos);
        bundle.putString("media_title", ma_ti);
        getFirebasePayload(bundle);
        getExtraDataForNetCOre(bundle, activity);

        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //todo aditya //NetcoreSDK.track(mContext, "Media_Click_Data", payloadDetails.toString());
    }


    public void netCoreSearch(Item item, String keySearched, Context activity) {

        matomoSearch(item, keySearched, activity);

        Bundle bundle = new Bundle();
        JSONObject payloadDetails = new JSONObject();


        if (PreferenceHandler.isLoggedIn(activity)) {
            if (!TextUtils.isEmpty(PreferenceHandler.getAnalyticsUserId(activity)))
                bundle.putString("user_id", PreferenceHandler.getAnalyticsUserId(activity));
        }

        bundle.putString("search_term", keySearched);
        if (item.getContentId() != null)
            bundle.putString("content_id", item.getContentId());
        if (item.getCategory() != null)
            bundle.putString("category", item.getCategory());
        if (item.getCatalogName() != null)
            bundle.putString("content_type", item.getCatalogName());
        if (item.getGenres() != null && item.getGenres().size() > 0)
            bundle.putString("genre", item.getGenres().get(0));
        if (item.getLanguage() != null)
            bundle.putString("language", item.getLanguage());
        getExtraDataForNetCOre(bundle, activity);
        try {
            payloadDetails.put("payload", getJsonObj(bundle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //todo aditya //NetcoreSDK.track(mContext, "search", payloadDetails.toString());

    }


    private void getFirebasePayload(Bundle bundle) {

        bundle.putString(Firebase.Params.VIDEO_ID, item_id);
        bundle.putString(Firebase.Params.TITLE, ma_ti);
        bundle.putString(Firebase.Params.BITRATE, media_bitrate);
        bundle.putString(Firebase.Params.USER_STATE, state_user);
        bundle.putString(Firebase.Params.USER_PLAN, user_period);
        bundle.putString(Firebase.Params.CATEGORY, media_category);
        bundle.putString(Firebase.Params.CONTENT_TYPE, media_content_type);
        bundle.putString(Firebase.Params.GENRE, media_genre);
        bundle.putString(Firebase.Params.LANGUAGE, media_language);
        bundle.putString(Firebase.Params.USER_ID, user_id);
        bundle.putString(Firebase.Params.USER_PLAN_TYPE, user_plan_type);
        bundle.putString(Firebase.Params.USER_PERIOD, user_period);
        bundle.putString(Firebase.Params.USER_PLAN_TYPE, user_plan_type);

    }


    private void getFirebasePayloadLimited(Bundle bundle) {

        bundle.putString(Firebase.Params.TITLE, ma_ti);
        bundle.putString(Firebase.Params.CATEGORY, media_category);
        bundle.putString(Firebase.Params.CONTENT_TYPE, media_content_type);
        bundle.putString(Firebase.Params.GENRE, media_genre);
        bundle.putString(Firebase.Params.LANGUAGE, media_language);

    }

    private void getFirebasePayloadLimitedPageView(Bundle bundle, String screenView) {

        bundle.putString(Firebase.Params.TITLE, ma_ti);
        bundle.putString(Firebase.Params.CATEGORY, media_category);
        bundle.putString(Firebase.Params.CONTENT_TYPE, media_content_type);
        bundle.putString(Firebase.Params.GENRE, media_genre);
        bundle.putString(Firebase.Params.LANGUAGE, media_language);
        bundle.putString(Firebase.Params.PAGEVIEW, screenView);

    }

    public void updateMediaActive(String currentTime) {

        //not using current time - we may need in future
//        Log.d(TAG, "updateMediaActive: " + "media_active");
        TrackMe trackMe = new TrackMe();
        getMatomoPayload(trackMe);
        if (mContext != null)
            getExtraDataForMotamo(trackMe, mContext);

        Log.d("Trackme med_active", "updateMediaActive " + item_id);

        TrackHelper.track(trackMe).event("Users", "Activity").name("media_active").value(1f).with(mTracker);

        //        Firebase
        if (IS_FIREBASE_ENABLED) {
            Bundle bundle = new Bundle();
            getFirebasePayload(bundle);    // Media Active
            bundle.putInt(Firebase.Params.MEDIA_ACTIVE, 1);
            MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.MEDIA_ACTIVE, bundle);
        }
    }

    public void updateAppLaunch(Activity activity) {
//        Log.d(TAG, "updateAppLaunch: " + "app_launch");
        TrackMe trackMe = new TrackMe();
//        trackMe.set("app_launch", "1");
        addCampagianParameters(activity, trackMe);
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("app_launch").value(1f).with(mTracker);

        mAppLaunchTime = Calendar.getInstance().getTime();
    }

    public void addCampagianParameters(Activity activity, TrackMe trackMe) {
        if (PreferenceHandler.getReferalDetails(activity) != null) {
            CampaignData referalKeys = PreferenceHandler.getReferalDetails(activity);

            CustomDimension.setDimension(trackMe, 18, referalKeys.getPkCampaign());
            CustomDimension.setDimension(trackMe, 19, referalKeys.getPkKwd());
            CustomDimension.setDimension(trackMe, 20, referalKeys.getPkSource());
            CustomDimension.setDimension(trackMe, 21, referalKeys.getPkMedium());
            CustomDimension.setDimension(trackMe, 22, referalKeys.getPkContent());
            Log.e(TAG, "updateAppLaunch: " + referalKeys.toString());

        }
        try {
            CustomDimension.setDimension(trackMe, 23, device_id);
        } catch (Exception e) {
        }
    }

    public void updateActiveUsers(Activity activity) {
//        Log.d(TAG, "updateActiveUsers: " + "user_active");
        TrackMe trackMe = new TrackMe();
//        trackMe.set("user_active", "1");
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("user_active").value(1f).with(mTracker);

        //        Firebase
//        if (IS_FIREBASE_ENABLED) {
//            Bundle bundle = new Bundle();
//            bundle.putFloat(Firebase.Params.MEDIA_ACTIVE, 1f);
//            MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.MEDIA_ACTIVE, bundle);
//        }
    }


    public void updateRegisteredUsers(String userID, Activity activity) {
//        Log.d(TAG, "updateRegisteredUsers: " + "registered_active");
        TrackMe trackMe = new TrackMe();
        addCampagianParameters(activity, trackMe);
        CustomDimension.setDimension(trackMe, 14, userID);
        getExtraDataForMotamo(trackMe, activity);
        TrackHelper.track(trackMe).event("Users", "Activity").name("registered_active").value(1f).with(mTracker);

        //        Firebase
        if (IS_FIREBASE_ENABLED) {
            MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.ACTIVE_REGISTERED, null);
        }
    }

    public void updateSVODActive(boolean svodActive, String userID, Activity activity) {
        if (svodActive) {
//            Log.d(TAG, "updateSVODActive: " + svodActive + "");
            TrackMe trackMe = new TrackMe();
            addCampagianParameters(activity, trackMe);
            CustomDimension.setDimension(trackMe, 14, userID);
            getExtraDataForMotamo(trackMe, activity);
            TrackHelper.track(trackMe).event("Users", "Activity").name("svod_active").value(1f).with(mTracker);

            //        Firebase
            if (IS_FIREBASE_ENABLED) {
                MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.ACTIVE_SVOD, null);
            }
        }
    }


    public void updateUserId(String analyticsUserId, String tag, Activity mContext) {
//        Log.d(TAG, "updateUserId: " + analyticsUserId);

//        MyApplication.getInstance().getTracker().setUserId(analyticsUserId);

        if (!tag.equalsIgnoreCase(SplashScreenActivity.TAG)) {
            if (tag.equalsIgnoreCase(RegisterFragment.TAG)) {

                TrackMe trackMe = new TrackMe();

                CustomDimension.setDimension(trackMe, 14, analyticsUserId);
                addCampagianParameters(mContext, trackMe);
                getExtraDataForMotamo(trackMe, mContext);
                TrackHelper.track(trackMe).event("Users", "Activity").name("sign_up").value(1f).with(mTracker);

            } else {
                MyApplication.getInstance().getFirebaseInstance().logEvent(FirebaseAnalytics.Event.LOGIN, null);
                TrackMe trackMe = new TrackMe();
                addCampagianParameters(mContext, trackMe);
                CustomDimension.setDimension(trackMe, 14, analyticsUserId);
                getExtraDataForMotamo(trackMe, mContext);
                TrackHelper.track(trackMe).event("Users", "Activity").name("login").value(1f).with(mTracker);
                if (mContext != null) {
                    // branchLogin(analyticsUserId, mContext);
                }
            }

            MyApplication.getInstance().getFirebaseInstance().setUserId(analyticsUserId);

        }

//        NetCore
//        NetcoreSDK.setIdentity(mContext, analyticsUserId);
//        NetcoreSDK.login(mContext);
//        if (!tag.equalsIgnoreCase(SplashScreenActivity.TAG)) {
//            netcoreLogin(analyticsUserId, mContext);
//        }
    }

    public void completeRegestration(String analyticsUserId, String region) {
        if (mContext != null) {
            //branchCompleteRegestration(analyticsUserId, mContext, region);
            netCoreCompleteRegestration(analyticsUserId, mContext, region);
            motamoCompleteRegestration(analyticsUserId, mContext, region);
        }
    }

    public void resetUserId(String analyticsUserId, Context mContext) {
//        Log.d(TAG, "resetUserId: " + analyticsUserId);

        TrackMe trackMe = new TrackMe();
        TrackHelper.track(trackMe).event("Users", "Activity").name("logout").value(1f).with(mTracker);

        MyApplication.getInstance().getTracker().setUserId(null);


//        Firebase
        MyApplication.getInstance().getFirebaseInstance().setUserId(null);

        //        NetCore
//        NetcoreSDK.logout(mContext);
//        NetcoreSDK.clearIdentity(mContext);
    }

    public void trackAppInstalls() {
        TrackHelper.track().download().with(MyApplication.getInstance().getTracker());
    }


    public enum EventName {
        buffering, seek, play, pause, finish, error
    }

    private String ID_SITE = "4";
    private String REC = "1";

    private Context mContext;
    private Date mPageStartTime;
    private Date mVideoStartTime;
    private String a_id, ma_ti, ma_re, ma_mt, ma_pn, ma_st, ma_le, ma_ps, ma_ttp, ma_w, ma_h, ma_fs, url, idsite = ID_SITE, rec = REC, r = "1", h = "12", m = "12", s = "13", _id = "83f6ee534556fc3f", _idts = "1538557466", _idvc = "7", _idn = "0", _refts = "0", _viewts = "1538717389", cs = "windows-1252", send_image = "1", pdf = "1", qt = "0", realp = "0", wma = "0", dir = "0", fla = "0", java = "0", gears = "0", ag = "0", cookie = "1", res = "1600x900", gt_ms = "102", pv_id = "4ETZwn", cvar = "%7B%221%22%3A%5B%22Subscribe%22%2C%22Yes%22%5D%2C%222%22%3A%5B%22Saranyu%22%2C%222ndtest%22%5D%7D", urlref = "http%3A%2F%2F52.220.137.44%3A3002%2F";
    private String TAG = AnalyticsProvider.class.getSimpleName();
    private String media_language;
    private String app_type;
    private String media_category;
    private String media_content_type;
    private String media_genre;
    private String playlist;
    private String media_bitrate;
    private String user_age = "";
    private String user_gender = "";
    private String state_user = "";
    private String user_period = "";
    private String user_plan_type = "";
    private String user_custom_plan = "";
    private String user_id = "";
    private String show_title = "";
    private String item_id;   // content ID
    private String pk_campaign, pk_kwd, pk_source, pk_medium, pk_content;
    private String device_id, title_category;
    private String region, city, date_event, time_event, session_id, device_make, os, banner_type, category_name, media_name, OTP_Received, user_mobile, content_duration;


    /**
     * @param ma_id            - A unique id that is always the same while playing a media. As soon as the played media changes (new video or audio started), this ID has to change.
     * @param ma_ti            - The name / title of the media.
     * @param ma_re            - The URL of the media resource.
     * @param ma_mt            - video or audio depending on the type of the media.
     * @param ma_pn            - The name of the media player, for example html5.
     * @param ma_st            - The time in seconds for how long a user has been playing this media. This number should typically increase when you send a media tracking request. It should be 0 if the media was only visible/impressed but not played. Do not increase this number when a media is paused.
     * @param ma_le            - The duration (the length) of the media in seconds. For example if a video is 90 seconds long, the value should be 90.
     * @param ma_ps            - The progress / current position within the media. Defines basically at which position within the total length the user is currently playing.
     * @param ma_w             - The resolution width of the media in pixels. Only recommended being set for videos.
     * @param ma_h             - The resolution height of the media in pixels. Only recommended being set for videos.
     * @param ma_fs            - Should be 0 or 1 and defines whether the media is currently viewed in full screen.
     * @param url
     * @param language
     * @param mediaCategory
     * @param mediaContentType
     * @param mediaGenre
     * @param cur_playlist
     * @param bitrate
     * @param userage
     * @param gender
     * @param stateuser
     * @param userPlanType
     * @param userperiod
     * @param userCustomPlan
     * @param userId
     * @param mPlayer          - Instaplay player
     * @param mItemId          - Content ID
     */

    public void initProvider(String ma_id, String ma_ti, String ma_re, String ma_mt,
                             String ma_pn, String ma_st, String ma_le, String ma_ps, String ma_w, String ma_h, String ma_fs,
                             String url, String language, String mediaCategory, String mediaContentType,
                             String mediaGenre, String cur_playlist, String bitrate, String userage,
                             String gender, String stateuser, String userperiod, String userPlanType,
                             String userCustomPlan, String userId, InstaPlayView mPlayer, String mItemId,
                             Activity activity, String show_name) {
        Log.d("PlayerNameInAnalytics ", ma_pn);
        try {
            if (!TextUtils.isEmpty(ma_ps) && Integer.parseInt(ma_ps) < 0) {
                ma_ps = 0 + "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (!TextUtils.isEmpty(ma_st) && Integer.parseInt(ma_st) < 0) {
                ma_st = 0 + "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (!TextUtils.isEmpty(ma_le) && Long.parseLong(ma_le) < 0) {
                ma_le = 0 + "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        this.a_id = ma_id;
        this.ma_ti = ma_ti;
        this.ma_re = updateCustomDimens();
        this.ma_mt = ma_mt;
        this.ma_pn = ma_pn;
        this.ma_st = ma_st;
        this.ma_le = ma_le;
        this.ma_ps = ma_ps;
        this.ma_w = ma_w;
        this.ma_h = ma_h;
        this.ma_fs = ma_fs;
        this.url = url;
        this.idsite = idsite;
        this.rec = rec;
        this.app_type = Constants.AN_APP_TYPE;
        this.media_language = language;
        this.media_category = mediaCategory;
        this.media_content_type = mediaContentType;
        this.media_genre = mediaGenre;
        this.playlist = cur_playlist;
        this.media_bitrate = bitrate;
        this.user_age = userage;
        this.user_gender = gender;
        this.state_user = stateuser;
        this.user_period = userperiod;
        this.user_plan_type = userPlanType;
        this.user_custom_plan = userCustomPlan;
        this.user_id = userId;
        this.item_id = mItemId;
        this.show_title = show_name;
        setReferalValues(activity);

        /*Kiran*/
        if (ma_pn.equalsIgnoreCase("youtube") && mPlayer == null) {
            try {
                long millis = Integer.parseInt(ma_ps) * 1000;
                this.h = "" + TimeUnit.MILLISECONDS.toHours(millis);
                this.m = "" + (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
                this.s = "" + (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            long millis = mPlayer.getCurrentPosition();
            this.h = "" + TimeUnit.MILLISECONDS.toHours(mPlayer.getCurrentPosition());
            this.m = "" + (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
            this.s = "" + (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }
    }

    //keytool -list -v -keystore C:\D_Drive\TemplateApps\OTV\tarangtv.jks
    private TrackMe getMatomoPayload(TrackMe trackMe) {

        Log.d("Trackme Payload", "initialize " + item_id);
        CustomDimension.setDimension(trackMe, 1, "android");
        CustomDimension.setDimension(trackMe, 2, media_language);
        if (!TextUtils.isEmpty(media_category))
            CustomDimension.setDimension(trackMe, 3, media_category);    // null

        CustomDimension.setDimension(trackMe, 4, media_content_type);   // linear
        CustomDimension.setDimension(trackMe, 5, media_genre);
        // CustomDimension.setDimension(trackMe, 6, playlist);        // shd be checked
        CustomDimension.setDimension(trackMe, 6, item_id);   // content ID
        //CustomDimension.setDimension(trackMe, 7, media_bitrate);
        CustomDimension.setDimension(trackMe, 8, ma_ps);    // duration total
        CustomDimension.setDimension(trackMe, 9, ma_ti);  // not going
        CustomDimension.setDimension(trackMe, 10, state_user);
        CustomDimension.setDimension(trackMe, 11, user_period);
        CustomDimension.setDimension(trackMe, 12, user_plan_type);
        CustomDimension.setDimension(trackMe, 13, user_custom_plan);
        CustomDimension.setDimension(trackMe, 14, user_id);
        CustomDimension.setDimension(trackMe, 15, show_title);
        // CustomDimension.setDimension(trackMe, 16, PreferenceHandler.getNetworkType());   // content ID
        /* CustomDimension.setDimension(trackMe, 17, ma_le);   // total_duration
        CustomDimension.setDimension(trackMe, 18, pk_campaign);
        CustomDimension.setDimension(trackMe, 19, pk_kwd);
        CustomDimension.setDimension(trackMe, 20, pk_source);
        CustomDimension.setDimension(trackMe, 21, pk_medium);
        CustomDimension.setDimension(trackMe, 22, pk_content);
        CustomDimension.setDimension(trackMe, 23, device_id);*/
        if (!TextUtils.isEmpty(media_category))
            title_category = ma_ti + "|" + item_id + "|" + media_category + "|" + media_language + "|" + media_genre + "|" + media_content_type;
        else
            title_category = ma_ti + "|" + item_id + "|" + media_language + "|" + media_genre + "|" + media_content_type;
        CustomDimension.setDimension(trackMe, 7, title_category);


        return trackMe;
    }

    public void setReferalValues(Activity activity) {
        if (PreferenceHandler.getReferalDetails(activity) != null) {
            CampaignData referalKeys = PreferenceHandler.getReferalDetails(activity);
            this.pk_campaign = referalKeys.getPkCampaign() != null ? referalKeys.getPkCampaign() : "";
            this.pk_kwd = referalKeys.getPkKwd() != null ? referalKeys.getPkKwd() : "";
            this.pk_content = referalKeys.getPkContent() != null ? referalKeys.getPkContent() : "";
            this.pk_medium = referalKeys.getPkMedium() != null ? referalKeys.getPkMedium() : "";
            this.pk_source = referalKeys.getPkSource() != null ? referalKeys.getPkSource() : "";
        }
    }


    public AnalyticsProvider(Context context) {
        mContext = context;
        user_id = PreferenceHandler.getAnalyticsUserId(mContext);
        mTracker = MyApplication.getInstance().getTracker();
        device_id = Helper.getAndroidDeviceId(context);
        this.banner_type = "carousel";  //32 is the index
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getMa_ti() {
        return ma_ti;
    }


    public void setMa_ti(String ma_ti) {
        this.ma_ti = ma_ti;
    }

    public String getMa_re() {
        return ma_re;
    }

    public void setMa_re(String ma_re) {
        this.ma_re = ma_re;
    }

    public String getMa_mt() {
        return ma_mt;
    }

    public void setMa_mt(String ma_mt) {
        this.ma_mt = ma_mt;
    }

    public String getMa_pn() {
        return ma_pn;
    }

    public void setMa_pn(String ma_pn) {
        this.ma_pn = ma_pn;
    }

    public String getMa_st() {
        return ma_st;
    }

    public void setMa_st(String ma_st) {
        this.ma_st = ma_st;
    }

    public String getMa_le() {
        return ma_le;
    }

    public void setMa_le(String ma_le) {
        this.ma_le = ma_le;
    }

    public String getMa_ps() {
        return ma_ps;
    }

    public void setMa_ps(String ma_ps) {
        this.ma_ps = ma_ps;
    }

    public String getMa_ttp() {
        return ma_ttp;
    }

    public void setMa_ttp(String ma_ttp) {
        this.ma_ttp = ma_ttp;
    }

    public String getMa_w() {
        return ma_w;
    }

    public void setMa_w(String ma_w) {
        this.ma_w = ma_w;
    }

    public String getMa_h() {
        return ma_h;
    }

    public void setMa_h(String ma_h) {
        this.ma_h = ma_h;
    }

    public String getMa_fs() {
        return ma_fs;
    }

    public void setMa_fs(String ma_fs) {
        this.ma_fs = ma_fs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdsite() {
        return idsite;
    }

    public void setIdsite(String idsite) {
        this.idsite = idsite;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    public Date getmPageStartTime() {
        return mPageStartTime;
    }

    public void setmPageStartTime(Date mPageStartTime) {
        this.mPageStartTime = mPageStartTime;
    }

    public Date getmVideoStartTime() {
        return mVideoStartTime;
    }

    public void setmVideoStartTime(Date mVideoStartTime) {
        this.mVideoStartTime = mVideoStartTime;
    }

    public int timeTtp() {
        if (getmPageStartTime() != null && getmVideoStartTime() != null) {


            long diff = getmVideoStartTime().getTime() - getmPageStartTime().getTime();
            long seconds = diff / 1000;
//            long minutes = seconds / 60;
//            long hours = minutes / 60;
//            long days = hours / 24;

            return (int) seconds;
        } else return 0;
    }

    public String getDeviceID() {
        String id = "";
        if (mContext != null)
            id = PreferenceHandler.getDeviceId(mContext);
        return id;
    }

    public void updateAnalytics(String mediaType, EventName name) {
        if (TextUtils.isEmpty(ma_ti)) {
            setMa_ti("Unknown");
        }

        TrackMe trackMe = new TrackMe();
        trackMe.set(QueryParams.RECORD, "1");
        trackMe.set("ma_id", a_id);
        trackMe.set("ma_ti", ma_ti);
        trackMe.set("ma_re", updateCustomDimens());
        trackMe.set("ma_mt", ma_mt);
        trackMe.set("ma_pn", ma_pn);
        trackMe.set("ma_st", ma_st);
        trackMe.set("ma_le", ma_le);
        trackMe.set("ma_ps", ma_ps);
        trackMe.set("ma_ttp", "" + timeTtp());
        trackMe.set("ma_w", ma_w);
        trackMe.set("ma_h", ma_h);
        trackMe.set("ma_fs", ma_fs);
        trackMe.set("h", h);
        trackMe.set("h", m);
        trackMe.set("s", s);
        trackMe.set("send_image", send_image);


        if (mContext != null) {
            getExtraDataForMotamo(trackMe, mContext);
        }
        Log.d("Onanalytics: play 2", " " + name.name());
        TrackHelper.track(trackMe)
                .event("MediaVideo", name.name())
                .name("Android")
                .with(mTracker);

        TrackMe trackMeEvent = new TrackMe();
        getMatomoPayload(trackMeEvent);

        /*todo most important*/
        Log.d("Onanalytics -----", name.name() + "  " + item_id);


        if (name.name().equalsIgnoreCase(String.valueOf(EventName.play))) {
            TrackHelper.track(trackMeEvent)
                    .event("media", "playback")
                    .name("play")
                    .value(1f)
                    .with(mTracker);
            Log.d("Onanalytics: play 3", name.name() + "");

        } else if (name.name().equalsIgnoreCase(String.valueOf(EventName.seek))) {

            TrackHelper.track(trackMeEvent)
                    .event("media", "playback")
                    .name("playback_time")
                    .value(10f)
                    .with(mTracker);
            Log.d("Onanalytics: play 4", "play" + name.name());

        } else if (name.name().equalsIgnoreCase(String.valueOf(EventName.finish))) {

            TrackHelper.track(trackMeEvent)
                    .event("media", "playback")
                    .name("play_complete")
                    .value(1f)
                    .with(mTracker);

        }


//        FIREBASE

/*        if (name.name().equalsIgnoreCase(EventName.seek.toString())) {
            if (IS_FIREBASE_ENABLED) {
                Bundle bundle = new Bundle();
                getFirebasePayload(bundle);  // Heart Beat
                bundle.putInt(Firebase.Params.VIDEO_VIEW_TIME, 10);
                MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.VIDEO_VIEW_TIME, bundle);
            }
        } else if (name.name().equalsIgnoreCase(EventName.finish.toString())) {
            if (IS_FIREBASE_ENABLED) {
                Bundle bundle = new Bundle();
                getFirebasePayload(bundle);  // Media Finish
                MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.VIDEO_COMPLETE, bundle);
            }
        }*/

    }

    private String updateCustomDimens() {
        String payload;

        payload = "CID=" + item_id + "|"                // Content ID
                + "C=" + media_category + "|"           // Category
                + "T=" + media_content_type + "|"       // Theme
                + "G=" + media_genre + "|"              // Genres
                + "L=" + media_language + "|"           // Language
                + "B=" + media_bitrate + "|"            // Bitrate
                + "UID=" + user_id + "|"                // User ID ( Analytics ID )
                + "US=" + state_user + "|"              // User State
                + "UP=" + user_period + "|"             // User Plan
                + "UPT=" + user_plan_type + "|"         // User Plan Type
                + "UCP=" + user_custom_plan + "|";      // User Custom Plan


        return payload;
    }

/*
    private void getDataForBranch(BranchEvent branchEvent, Context activity) {
        branchEvent.addCustomDataProperty("content_id", item_id).
                addCustomDataProperty("category", media_category).
                addCustomDataProperty("content_type", media_content_type).
                addCustomDataProperty("genre", media_genre).
                addCustomDataProperty("language", media_language).
                addCustomDataProperty("user_state", state_user).
                addCustomDataProperty("user_period", user_period).
                addCustomDataProperty("user_plan_type", user_plan_type).
                addCustomDataProperty("user_custom_plan", user_custom_plan);

    }

    private void finallyLogged(BranchEvent branchEvent, Context activity) {
        // branchEvent.logEvent(activity);
    }

    private void getExtraDataForBranch(BranchEvent branchEvent, Context activity) {

        if (PreferenceHandler.isLoggedIn(activity)) {
            branchEvent.addCustomDataProperty("device_id", Helper.getAndroidDeviceId(activity)).
                    addCustomDataProperty("device_make", Helper.getDeviceName()).
                    addCustomDataProperty("os", Helper.getAndroidVersion()).
                    addCustomDataProperty("session_id", PreferenceHandler.getSessionId(activity)).
                    addCustomDataProperty("city", PreferenceHandler.getCityName(activity)).
                    addCustomDataProperty("region", PreferenceHandler.getRegion(activity)).
                    addCustomDataProperty("date_event", Helper.getDate()).
                    addCustomDataProperty("time_event", Helper.getTime());
        } else {
            branchEvent.addCustomDataProperty("device_id", Helper.getAndroidDeviceId(activity)).
                    addCustomDataProperty("device_make", Helper.getDeviceName()).
                    addCustomDataProperty("os", Helper.getAndroidVersion()).
                    addCustomDataProperty("city", PreferenceHandler.getCityName(activity)).
                    addCustomDataProperty("region", PreferenceHandler.getRegion(activity)).
                    addCustomDataProperty("date_event", Helper.getDate()).
                    addCustomDataProperty("time_event", Helper.getTime());
        }
    }

    private void getBannerData(BranchEvent branchEvent, Context activity, CatalogListItem catalogListItem) {
        if (catalogListItem != null) {
            if (catalogListItem.getCatalogName() != null)
                branchEvent.addCustomDataProperty("category", catalogListItem.getCatalogName());
            if (catalogListItem.getTitle() != null)
                branchEvent.addCustomDataProperty("media_title", catalogListItem.getTitle());
        }

    }
*/


    /**
     * If we need to refresh all the values, this method sets all to null. We might have to reinit using initProvider().
     */
    public void clearValues() {
        a_id = ma_ti = ma_re = ma_mt = ma_pn = ma_st = ma_le = ma_ps = ma_ttp = ma_w = ma_h = ma_fs = url = idsite = rec = null;
    }

    public void updateMedia(String mediaType, EventName name, String ma_st, String ma_ps, String ma_fs, InstaPlayView mPlayer) {

        this.ma_st = ma_st;
        this.ma_ps = ma_ps;
        this.ma_fs = ma_fs;

        /*Kiran*/
        long millis = mPlayer.getCurrentPosition();
        this.h = "" + TimeUnit.MILLISECONDS.toHours(mPlayer.getCurrentPosition());
        this.m = "" + (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        this.s = "" + (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));


        updateAnalytics(mediaType, name);
    }


    public void updateMediaYoutube(String mediaType, EventName name, String ma_st, String ma_ps, String ma_fs, YouTubePlayer mPlayer) {

        this.ma_st = ma_st;
        this.ma_ps = ma_ps;
        this.ma_fs = ma_fs;

        /*Kiran*/
        long millis = mPlayer.getCurrentTimeMillis();
        this.h = "" + TimeUnit.MILLISECONDS.toHours(mPlayer.getCurrentTimeMillis());
        this.m = "" + (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        this.s = "" + (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));


        updateAnalytics(mediaType, name);
    }


    public void firstPlayClick() {
        if (IS_FIREBASE_ENABLED) {
            Bundle bundle = new Bundle();
            //getFirebasePayload(bundle);    // First Play
//            bundle.putInt(Firebase.Params.VIDEO_VIEW_TIME, 10);
            // MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.VIDEO_VIEW, bundle);
            fireVideoStartTimeTaken();

            //
            mOnPlayStartTime = Calendar.getInstance().getTime();
        }

    }

    public void fireVideoStartTimeTaken() {
        if (IS_FIREBASE_ENABLED) {
            Bundle bundle = new Bundle();
            getFirebasePayload(bundle);   // VideoStartTimeTaken
            setmVideoStartTime(Calendar.getInstance().getTime());
            bundle.putInt(Firebase.Params.START_TTP, timeTtp());
            // MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.START_TTP, bundle);
        }
    }


    public void reportVideoError() {
//   Handled just like finish
    }

    public void reportVideoLaunchTime() {
//        if (mOnPlayStartTime == null)
//            return;
//        Date mOnPlayLaunchTime = Calendar.getInstance().getTime();
//        long tDelta = mOnPlayLaunchTime.getTime() - mOnPlayStartTime.getTime();
//        double elapsedSeconds = (tDelta / 1000.0);
//        mAppLaunchTime = null;
//
//        DecimalFormat df = new DecimalFormat("0.0");
//        String diffValue = df.format(Double.parseDouble("" + elapsedSeconds));
//
//
//        //       Matomo
//        TrackMe payload = new TrackMe();
//        getMatomoPayload(payload);
//
//        TrackHelper.track(payload)
//                .event("Users", "Activity")
//                .name(Firebase.Events.VIDEO_LAUNCH_TIME)
//                .value(Float.valueOf(diffValue))
//                .with(mTracker);

//       Firebase
//        Bundle bundle = new Bundle();
//        if (IS_FIREBASE_ENABLED) {
//            getFirebasePayload(bundle);
//            bundle.putString(Firebase.Params.VIDEO_LAUNCH_TIME, diffValue);
//            MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.VIDEO_LAUNCH_TIME, bundle);
//        }

    }

    public void reportApplaunchTime() {
//        if (mAppLaunchTime == null)
//            return;
//
//        Date mLaunchFinalTime = Calendar.getInstance().getTime();
//        long tDelta = mLaunchFinalTime.getTime() - mAppLaunchTime.getTime();
//        double elapsedSeconds = (tDelta / 1000.0);
//        mAppLaunchTime = null;
//
//        DecimalFormat df = new DecimalFormat("0.0");
//        String diffValue = df.format(Double.parseDouble("" + elapsedSeconds));
//
//
//        //       Matomo
//        TrackMe payload = new TrackMe();
//        TrackHelper.track(payload)
//                .event("Users", "Activity")
//                .name(Firebase.Events.APP_LAUNCH_TIME)
//                .value(Float.valueOf(diffValue))
//                .with(mTracker);

//       Firebase
//        Bundle bundle = new Bundle();
//        if (IS_FIREBASE_ENABLED) {
//            getFirebasePayload(bundle);
//            bundle.putString(Firebase.Params.APP_LAUNCH_TIME, diffValue);
//            MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.APP_LAUNCH_TIME, bundle);
//        }

    }

    public void reportAppError() {
//  Should discuss exactly what should be done.
    }

    public void reportAddToWatchList() {
/*//       Matomo
        TrackMe payload = new TrackMe();
        getMatomoPayload(payload);
        if (mContext != null)
            getExtraDataForMotamo(payload, mContext);

        TrackHelper.track(payload)
                .event("Users", "Activity")
                .name(Firebase.Events.ADD_TO_WATCHLIST)
                .value(1f)
                .with(mTracker);

        if (mContext != null) {
            branchAddToWatchList(ma_ti, mContext);
        }

//       Firebase
        Bundle bundle = new Bundle();
        getFirebasePayloadLimited(bundle);  // App Watchlist
//        if (IS_FIREBASE_ENABLED) {
//            MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.ADD_TO_WATCHLIST, bundle);
//        }

        if (mContext != null) {
            getExtraDataForNetCOre(bundle, mContext);
        }

//        Network Analytics
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "media watchlist", payloadDetails.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void getFirebasePayloadLimitedRemoveFromHistory(Bundle bundle, Item item) {

        try {
            bundle.putString(Firebase.Params.TITLE, item.getTitle());
            //bundle.putString(Firebase.Params.CATEGORY, item.getCategory());
            bundle.putString(Firebase.Params.GENRE, item.getGenres().toString());
            bundle.putString(Firebase.Params.LANGUAGE, item.getLanguage());
        } catch (Exception e) {
        }

    }

    public void reportRemoveFromWatchList(Item item) {
        Bundle bundle = new Bundle();
        getFirebasePayloadLimitedRemoveFromHistory(bundle, item);  // Remove Watchlist
//        Network Analytics
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                payloadDetails.put("payload", getJsonObj(bundle));
                if (mContext != null) {
                    getExtraDataForNetCOre(bundle, mContext);
                }
                //todo aditya //NetcoreSDK.track(mContext, "remove from watchlist", payloadDetails.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void reportRemoveFromWatchList() {/*
        Bundle bundle = new Bundle();
        getFirebasePayloadLimited(bundle);  // Remove Watchlist
        if (mContext != null) {
            getExtraDataForNetCOre(bundle, mContext);
        }
//        Network Analytics
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                payloadDetails.put("payload", getJsonObj(bundle));
                NetcoreSDK.track(mContext, "remove from watchlist", payloadDetails.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void reportAppShare(String shareUrl) {
//       Matomo
        TrackMe payload = new TrackMe();
        getMatomoPayload(payload);
        if (mContext != null)
            getExtraDataForMotamo(payload, mContext);

        TrackHelper.track(payload)
                .event("Users", "Activity")
                .name(Firebase.Events.SHARE)
                .value(1f)
                .with(mTracker);
       /* if (mContext != null)
            branchShare(ma_ti, mContext);*/

//       Firebase
        Bundle bundle = new Bundle();
        getFirebasePayloadLimited(bundle);

        if (mContext != null) {
            getExtraDataForNetCOre(bundle, mContext);
        }
        getFirebasePayload(bundle);

        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "media share", payloadDetails.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void fireAppLaunch(Context mContext) {
        if (Constants.IS_APP_LAUNCH_EVENT_FIRED)
            return;


        String userID = PreferenceHandler.getAnalyticsUserId(mContext);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CleverTapParams.COUNTRY_NAME, Constants.REGION);
        bundle.putString(Constants.CleverTapParams.STATE_NAME, Constants.STATE);
        bundle.putString(Constants.CleverTapParams.CITY_NAME, Constants.CITY);
        bundle.putString(Constants.CleverTapParams.APP_VERSION, BuildConfig.VERSION_NAME);

        if (mContext != null) {
            getExtraDataForNetCOre(bundle, mContext);
        }

        if (!TextUtils.isEmpty(userID))
            bundle.putString("identity", Constants.REGION + "_LOGGED_IN");
        else
            bundle.putString("identity", Constants.REGION + "_ANONYMOUS");

//        Network Analytics
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "app_launch", payloadDetails.toString());
                Constants.IS_APP_LAUNCH_EVENT_FIRED = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void reportNetworkError() {
//       Matomo
        TrackMe trackMe = new TrackMe();
        if (mContext != null)
            getExtraDataForMotamo(trackMe, mContext);
        TrackHelper.track(trackMe).event("Users", "Activity").name(Firebase.Events.NETWORK).value(1f).with(mTracker);

//       Firebase
//        Bundle bundle = new Bundle();
//        if (IS_FIREBASE_ENABLED) {
//            getFirebasePayload(bundle);
//            MyApplication.getInstance().getFirebaseInstance().logEvent(Firebase.Events.NETWORK, bundle);
//        }

    }


    public void reportCategoryTabSelected(String tabName) {
//        Log.d(TAG, "reportCategoryTabSelected: >>>>> " + tabName);

        if (TextUtils.isEmpty(tabName))
            return;

        Bundle bundle = new Bundle();
        bundle.putString("selected_tab", tabName);
//        getFirebasePayloadLimited(bundle);  // Remove Watchlist
//        Network Analytics
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "product category", payloadDetails.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    /*
     ************************************
     *
     *       CLEVER TAP
     *
     ************************************
     */

    public static void sendProfileDataToCleverTap(Map<String, Object> dataTobeSend, Context mContext) {

        try {
//            CleverTap
//            cleverTap = CleverTapAPI.getInstance(MyApplication.getInstance().getApplicationContext());
//            if (cleverTap != null)
//                cleverTap.profile.push(dataTobeSend);


//            NetCore
            /*todo aditya*/
            //JSONObject profileObj = new JSONObject(dataTobeSend);
            //NetcoreSDK.profile(mContext, profileObj);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMediaEventToCleverTap(Map<String, Object> dataTobeSend, Context mContext) {

//        NetCore
        if (IS_CLEVERTAP_ENABLED) {
            try {

                JSONObject payloadDetails = new JSONObject();
                JSONObject eventObj = new JSONObject(dataTobeSend);
                payloadDetails.put("payload", eventObj);
                //todo aditya //NetcoreSDK.track(mContext, "Media Event", payloadDetails.toString());
//                Log.d("pn.netcoresmartech.com", "sendMediaEvent: " + eventObj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public static Map<String, Object> getCleverTapData(Context mContext, long pos, Data currentItem) {
        Map<String, Object> objectMap = new HashMap<>();
        if (PreferenceHandler.isLoggedIn(mContext)) {
            objectMap.put("Identity", PreferenceHandler.getAnalyticsUserId(mContext.getApplicationContext()));                    // String or number   get User ID
        }
        objectMap.put("watch_time", pos);
        objectMap.put("media_name", currentItem.getTitle());
        objectMap.put("media_content_type", currentItem.getTheme());
        objectMap.put("media_genre", currentItem.getGenres().get(0));
        objectMap.put("media_language", currentItem.getLanguage());
        objectMap.put("media_category", currentItem.getCatalogObject().getPlanCategoryType());
        return objectMap;
    }


    public static Map<String, Object> getCleverTapData(Context mContext, long pos, Item currentItem) {
        Map<String, Object> objectMap = new HashMap<>();
        if (PreferenceHandler.isLoggedIn(mContext)) {
            objectMap.put("Identity", PreferenceHandler.getAnalyticsUserId(mContext.getApplicationContext()));                    // String or number   get User ID
        }
        objectMap.put("watch_time", pos);
        objectMap.put("media_name", currentItem.getTitle());
        objectMap.put("media_content_type", currentItem.getTheme());
        objectMap.put("media_genre", currentItem.getGenres().get(0));
        objectMap.put("media_language", currentItem.getLanguage());
        objectMap.put("media_category", currentItem.getCatalogObject().getPlanCategoryType());
        return objectMap;
    }



    /*
     ************************************
     *
     *       FIREBASE
     *
     ************************************
     */


    public static class Firebase {

        public static class Events {
            public static final String ACTIVE_SVOD = "active_svod";
            public static final String ACTIVE_REGISTERED = "active_registered";
            public static final String MEDIA_ACTIVE = "media_active";
            public static final String BUFFER_TIME = "buffer_time";
            public static final String VIDEO_VIEW_TIME = "video_view_time";
            public static final String VIDEO_VIEW = "video_view";
            public static final String VIDEO_COMPLETE = "video_complete";
            public static final String START_TTP = "start_ttp";
            public static final String NETWORK = "network_error";
            public static final String SHARE = "share";
            public static final String ADD_TO_WATCHLIST = "watchlist";
            public static final String VIDEO_LAUNCH_TIME = "video_launch_time";
            public static final String APP_LAUNCH_TIME = "app_launch_time";
        }

        public static class Params {
            public static final String START_TTP = "start_ttp";
            public static final String VIDEO_VIEW_TIME = "video_view_time";
            public static final String ACTIVE_SVOD = "active_svod";
            public static final String MEDIA_ACTIVE = "media_active";
            public static final String BUFFER_TIME = "buffer_time";
            public static final String VIDEO_ID = "video_id";
            public static final String TITLE = "title";
            public static final String BITRATE = "bitrate";
            public static final String USER_STATE = "user_state";
            public static final String USER_PLAN = "user_plan";
            public static final String USER_PLAN_TYPE = "user_plan_type";
            public static final String USER_PERIOD = "user_period";
            public static final String CATEGORY = "category";
            public static final String CONTENT_TYPE = "content_type";
            public static final String GENRE = "genre";
            public static final String LANGUAGE = "language";
            public static final String PAGEVIEW = "page_view";
            public static final String USER_ID = "user_id";
            public static final String VIDEO_LAUNCH_TIME = "video_launch_time";
            public static final String APP_LAUNCH_TIME = "app_launch_time";
            public static final String SHARE = "share";

        }


    }


    private JSONObject getJsonObj(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                json.put(key, JSONObject.wrap(bundle.get(key)));
            } catch (JSONException e) {
                //Handle exception here
            }
        }
        return json;
    }


    public static class Screens {
        public static final String HomeScreen = "HomeScreen";
        public static final String ShowDetailsScreen = "ShowDetailsScreen";
        public static final String EpisodeDetailsScreen = "EpisodeDetailsScreen";
        public static final String MovieDetailsScreen = "MovieDetailsScreen";
        public static final String TrailerDetailsScreen = "TrailerDetailsScreen";
        public static final String LinearAndLiveScreen = "LinearAndLiveScreen";
        public static final String Search = "Search";
        public static final String TV = "TV";
        public static final String ME = "ME";
        public static final String ViewPlans = "ViewPlans";
        public static final String PlayList = "PlayList";
        public static final String Login = "Login";
        public static final String Registration = "Registration";
        public static final String Settings = "Settings";
        public static final String UserDetails = "UserDetails";
        public static final String EditUserDetails = "EditUserDetails";
    }


    public void fireScreenView(String screenName) {
/*        TrackMe trackMe = new TrackMe();

        if (screenName.equalsIgnoreCase(Screens.MovieDetailsScreen) ||
                screenName.equalsIgnoreCase(Screens.ShowDetailsScreen) ||
                screenName.equalsIgnoreCase(Screens.EpisodeDetailsScreen) ||
                screenName.equalsIgnoreCase(Screens.LinearAndLiveScreen) ||
                screenName.equalsIgnoreCase(Screens.TrailerDetailsScreen)) {
            getMatomoPayload(trackMe);
            if (mContext != null)
                branchViewItem(ma_ti, mContext);
        }
        if (mContext != null)
            getExtraDataForMotamo(trackMe, mContext);

        TrackHelper.track(trackMe)
                .screen(screenName)
                .with(mTracker);

        if (IS_CLEVERTAP_ENABLED) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString(Firebase.Params.PAGEVIEW, screenName);
                getFirebasePayloadLimitedPageView(bundle, screenName);
                if (mContext != null) {
                    getExtraDataForNetCOre(bundle, mContext);
                }
                JSONObject payloadDetails = new JSONObject();
                if (screenName.equalsIgnoreCase(Screens.MovieDetailsScreen) ||
                        screenName.equalsIgnoreCase(Screens.ShowDetailsScreen) ||
                        screenName.equalsIgnoreCase(Screens.EpisodeDetailsScreen) ||
                        screenName.equalsIgnoreCase(Screens.LinearAndLiveScreen)) {
                    getFirebasePayload(bundle);
                    payloadDetails.put("payload", getJsonObj(bundle));
                    NetcoreSDK.track(mContext, "page_view", payloadDetails.toString());
                } else {
                    JSONObject payloadDetailss = new JSONObject();
                    Bundle bundles = new Bundle();
                    bundles.putString("screen_name", screenName);
                    if (mContext != null) {
                        getExtraDataForNetCOre(bundles, mContext);
                    }
                    payloadDetailss.put("payload", getJsonObj(bundles));
                    NetcoreSDK.track(mContext, "page_view", payloadDetailss.toString());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public void fireNetCoreViewPlanClick() {
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                Bundle bundle = new Bundle();
                bundle.putString("page_view", "view plans");
                if (mContext != null)
                    getExtraDataForNetCOre(bundle, mContext);
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "on_view_plans", payloadDetails.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fireNetCoreProfileUpdate(String user) {
        if (IS_CLEVERTAP_ENABLED) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("user", user);
                JSONObject payloadDetails = new JSONObject();
                if (mContext != null)
                    getExtraDataForNetCOre(bundle, mContext);
                payloadDetails.put("payload", getJsonObj(bundle));

                //todo aditya //NetcoreSDK.track(mContext, "profile_update", payloadDetails.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fireNetCoreIncompleteReg() {
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                Bundle bundle = new Bundle();
                bundle.putString("registration", "incomplete");
                if (mContext != null)
                    getExtraDataForNetCOre(bundle, mContext);
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "incomplete_registration", payloadDetails.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void userClickOnLiveTvNetCore() {
        if (IS_CLEVERTAP_ENABLED) {
            try {
                JSONObject payloadDetails = new JSONObject();
                Bundle bundle = new Bundle();
                bundle.putString("tv_clicked", "true");
                if (mContext != null)
                    getExtraDataForNetCOre(bundle, mContext);
                payloadDetails.put("payload", getJsonObj(bundle));
                //todo aditya //NetcoreSDK.track(mContext, "click_tv", payloadDetails.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
