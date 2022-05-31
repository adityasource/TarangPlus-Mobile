package com.otl.tarangplus.Analytics;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CampaignData;
import com.otl.tarangplus.model.MediaplaybackEvents;
import com.otl.tarangplus.model.PaymentEvent;

public class Analytics {
    private static Analytics analytics_instance = null;
    private static FirebaseAnalytics mFirebaseAnalytics;
    public static Context mContext;


    public Analytics() {
    }

    public static Analytics getInstance(Context context) {
        mContext = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (analytics_instance == null)
            analytics_instance = new Analytics();

        return analytics_instance;
    }

    public void logCampaignData(CampaignData data) {
        if(data == null ){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.UTM_SOURCE, data.getPkSource());
        bundle.putString(Constants.UTM_MEDIUM, data.getPkMedium());
        bundle.putString(Constants.UTM_CONTENT, data.getPkContent());
        bundle.putString(Constants.UTM_CAMPAIGN, data.getPkCampaign());
        bundle.putString(Constants.UTM_TERM, data.getPkKwd());

        mFirebaseAnalytics.logEvent(Constants.APP_CAMPAIGN_EVENT, bundle);

    }

    public void logMediaEvent(MediaplaybackEvents events) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EVENT_VALUE, events.getE_v());
        bundle.putString(Constants.APPLICATION, events.getApp());

        if (!events.getE_t().equals(Constants.EVENT_TYPE_PLAY)) {
            bundle.putDouble(Constants.EVENT_TIME, events.getE_tm());
        }
        bundle.putString(Constants.EVENT_TYPE, events.getE_t());
        bundle.putString(Constants.MEDIA_LANGUAGE, events.getM_l());
        bundle.putString(Constants.MEDIA_CONTENT_TYPE, events.getM_c_t());
        bundle.putString(Constants.MEDIA_GENRE, events.getM_g());
        bundle.putString(Constants.VIDEO_ID, events.getV_id());
        bundle.putString(Constants.TITLE_CATEGORY, events.getT_cat());
        bundle.putDouble(Constants.TOTAL_DURATION, events.getT_dur());
        bundle.putString(Constants.TITLE, events.getTitle());
        bundle.putString(Constants.USER_STATE_F, events.getU_st());
        bundle.putString(Constants.USERS_PERIOD, events.getU_p());
        bundle.putString(Constants.USERS_PLAN_TYPE, events.getU_p_r());
        bundle.putString(Constants.USERS_ID, events.getU_id());
        bundle.putString(Constants.SHOW_TITLE, events.getS_t());
        bundle.putString(Constants.CONTENT_OWNER, events.getC_o());
        bundle.putString(Constants.LIST_NAME, events.getL_name());
//        Gson gson = new Gson();
//        String s = gson.toJson(bundle);
//        Log.e("MediaplaybackEvents",s);
        mFirebaseAnalytics.logEvent(Constants.MEDIA_EVENT_NAME + "_" + events.getE_t(), bundle);
    }

    public void logAppEvents(AppEvents appEvents) {
        /*  event_value(e_v)..
    type(typ)..
    visit(vst)..
    application(app)..
    search_query(s_q)..
    event_type(e_t)..
    title(title)..
    user_state(u_st)..
    user_period(u_p)..
    user_plan_type(u_p_r)..
    user_id(u_id)..*/
        Bundle bundle = new Bundle();

        if (appEvents != null && appEvents.getE_t() != null
                && (appEvents.getE_t().equalsIgnoreCase(Constants.LINK_LOGIN) ||
                appEvents.getE_t().equalsIgnoreCase(Constants.LINK_REGISTER))) {
            CampaignData campaignData = PreferenceHandler.getReferalDetails(mContext);
            if (campaignData != null && !TextUtils.isEmpty(campaignData.getPkSource())) {
                bundle.putString(Constants.UTM_SOURCE, campaignData.getPkSource());
            }
        }

        bundle.putInt(Constants.EVENT_VALUE, appEvents.getE_v());
        bundle.putString(Constants.TYPE, appEvents.getTyp());
        bundle.putString(Constants.VISIT, appEvents.getVst());
        bundle.putString(Constants.APPLICATION, appEvents.getApp());
        bundle.putString(Constants.SEARCH_QUERY_F, appEvents.getS_q());
        bundle.putString(Constants.EVENT_TYPE, appEvents.getE_t());
        bundle.putString(Constants.TITLE, appEvents.getTitle());
        bundle.putString(Constants.USER_STATE_F, appEvents.getU_st());
        bundle.putString(Constants.USERS_PERIOD, appEvents.getU_p());
        bundle.putString(Constants.USERS_PLAN_TYPE, appEvents.getU_p_r());
        bundle.putString(Constants.USERS_ID, appEvents.getU_id());
//        Gson gson = new Gson();
//        String s = gson.toJson(bundle);
//        Log.e("AppEvents",s+" "+Constants.APP_EVENTS+"_"+appEvents.getE_t());
        mFirebaseAnalytics.logEvent(Constants.APP_EVENTS + "_" + appEvents.getE_t(), bundle);

    }

    public void paymentEvents(PaymentEvent paymentEvent, String eventType) {
     /*   public String u_id;//user id
        public String o_id;//odered id
        public String s_id;// session id
        public String a_id;//analytics id
        public String pri;//price
        public String p_typ;// payment type
        public String cur;//currency
        public String status;//Subsciption status
        public String pln_name;//plan name*/
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID_payment, paymentEvent.getU_id());
        bundle.putString(Constants.ORDER_ID, paymentEvent.getO_id());
        bundle.putString(Constants.SESSION_ID_PAMENT, paymentEvent.getS_id());
        bundle.putString(Constants.PRICE, paymentEvent.getPri());
        bundle.putString(Constants.PAYMENT_TYPE, paymentEvent.getP_typ());
        bundle.putString(Constants.CURRENCY, paymentEvent.getCur());
        bundle.putString(Constants.STATUS, paymentEvent.getStatus());
        bundle.putString(Constants.PLAN_TYPE, paymentEvent.getP_typ());
//        Gson gson = new Gson();
//        String s = gson.toJson(bundle);
//        Log.e("PaymentEvent",s);
        mFirebaseAnalytics.logEvent(Constants.PAYMENT_EVENT + "_" + eventType, bundle);

    }

}
