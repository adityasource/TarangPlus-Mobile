package com.otl.tarangplus;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.comscore.Analytics;
import com.comscore.PublisherConfiguration;
import com.comscore.UsagePropertiesAutoUpdateMode;
import com.facebook.stetho.Stetho;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onesignal.OneSignal;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.model.CampaignData;
import com.otl.tarangplus.test.Root;
import com.saranyu.ott.instaplaysdk.DRM.DRMoffline.InstaDownloader;

import org.matomo.sdk.Matomo;
import org.matomo.sdk.Tracker;
import org.matomo.sdk.TrackerBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonObject;
import com.webengage.sdk.android.WebEngage;
import com.webengage.sdk.android.WebEngageActivityLifeCycleCallbacks;
import com.webengage.sdk.android.WebEngageConfig;

import static com.otl.tarangplus.Utils.Constants.YOUR_WEBENGAGE_LICENSE_CODE;

public class MyApplication extends Application {


    private static MyApplication instance;
    private Tracker mMatomoTracker;
    private FirebaseAnalytics mFirebaseAnalytics;
    InstallReferrerClient referrerClient;
    private com.otl.tarangplus.Analytics.Analytics mAnalyticsEvent;
    public static final Boolean isFirebaseActive = true;
    @Override
    public void onCreate() {
        super.onCreate();

        mAnalyticsEvent = com.otl.tarangplus.Analytics.Analytics.getInstance(getApplicationContext());

        WebEngageConfig webEngageConfig = new WebEngageConfig.Builder()
                .setWebEngageKey("~47b66119")
                .setDebugMode(true) // only in development mode
                .build();

        registerActivityLifecycleCallbacks(new WebEngageActivityLifeCycleCallbacks(this, webEngageConfig));
        Stetho.initializeWithDefaults(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        String jsonFileString = Helper.getJsonFromAssets(getApplicationContext(), "states.json");

        Gson gson = new Gson();
        //Type listUserType = new TypeToken<Root>() { }.getType();
        //Log.i("hereigooo", jsonFileString);

        Root users = gson.fromJson(jsonFileString, Root.class);
        for (int i = 0; i < users.filters.size(); i++) {
            for(int j=0; j < users.filters.get(i).states.size(); j++){
                Log.i("States",  "INSERT INTO state(state_id, state_name)	VALUES ('"+users.filters.get(i).states.get(j).code+"','"+users.filters.get(i).states.get(j).title+"');");

                for(int k=0; k < users.filters.get(i).states.get(j).districts.size(); k++){
                    //Log.i("District", "INSERT INTO district(district_id, district_name, state_id)VALUES ('"+users.filters.get(i).states.get(j).districts.get(k).code+"','"+users.filters.get(i).states.get(j).districts.get(k).title+"','"+users.filters.get(i).states.get(j).code+"');");
                }
            }
        }





        if(!isFirebaseActive){
            // OneSignal Initialization
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();
        }else {
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    try {
                        String token = task.getResult();
                        WebEngage.get().setRegistrationID(token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        InstaDownloader.getInstance().initialize(this);

        //FacebookSdk.sdkInitialize(getApplicationContext());

        referrerClient = InstallReferrerClient.newBuilder(this).build();
        instance = this;
        setUpNotificationCahnnel();

        //todo check this before push to the branch .
        if (!PreferenceHandler.getFirstInstalled(this)) {
            setUpInstallReferers();
            PreferenceHandler.setDeviceId(this, Helper.randomAlphaNumeric(16));
            PreferenceHandler.setFirstInstalled(this, true);
        }

        PublisherConfiguration publisher = new PublisherConfiguration.Builder()
                .publisherSecret(BuildConfig.COMSCORESECRET)
                .usagePropertiesAutoUpdateMode(UsagePropertiesAutoUpdateMode.FOREGROUND_AND_BACKGROUND)
                .applicationName(getResources().getString(R.string.app_name))
                .publisherId(BuildConfig.PUBLISHEID)
                .build();

        Analytics.getConfiguration().addClient(publisher);
        Analytics.start(getApplicationContext());

        if (isTrackingLimited()) {
            Analytics.getConfiguration().disable();
        }
    }

    private Boolean isTrackingLimited() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getBoolean("pref_tracking_disable", false);
    }

    public void setUpInstallReferers() {

        if (referrerClient != null) {
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // Connection established
                            Log.e("just smallupdate", "onInstallReferrer_service_okay: ");
                            ReferrerDetails response = null;
                            try {
                                response = referrerClient.getInstallReferrer();
                                String installReferrer = response.getInstallReferrer();
                                if (Helper.getReferalThingsData(installReferrer) != null) {
                                    final Map<String, String> referalThingsData = Helper.getReferalThingsData(installReferrer);
                                    CampaignData referalKeys = new CampaignData();

                                    if (referalThingsData.get("utm_source") != null)
                                        referalKeys.setPkSource(referalThingsData.get("utm_source"));
                                    else
                                        referalKeys.setPkSource("");

                                    if (referalThingsData.get("utm_medium") != null)
                                        referalKeys.setPkMedium(referalThingsData.get("utm_medium"));
                                    else
                                        referalKeys.setPkMedium("");

                                    if (referalThingsData.get("utm_content") != null) {
                                        referalKeys.setPkContent(referalThingsData.get("utm_content"));
                                    } else {
                                        referalKeys.setPkContent("");
                                    }

                                    if (referalThingsData.get("utm_campaign") != null) {
                                        referalKeys.setPkCampaign(referalThingsData.get("utm_campaign"));
                                    } else {
                                        referalKeys.setPkCampaign("");
                                    }

                                    if (referalThingsData.get("utm_term") != null) {
                                        referalKeys.setPkKwd(referalThingsData.get("utm_term"));
                                    } else {
                                        referalKeys.setPkKwd("");
                                    }
                                    referalKeys.setIsDataReceived(false);
                                    PreferenceHandler.setReferalDetails(MyApplication.this, referalKeys);
                                  sendCampaignData(referalKeys);
                                }

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            Log.e("just smallupdate", "onInstallReferrer_future_not Supported: ");
                            // API not available on the current Play Store app
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            Log.e("just smallupdate", "onInstallReferrer_service_unavaliable: ");
                            // Connection could not be established
                            break;

                        case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                            Log.e("just smallupdate", "onInstallReferrer_developer error: ");
                            // Connection could not be established
                            break;

                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                }
            });
        }
    }

    private void sendCampaignData(CampaignData data) {
        if (data!=null){
            mAnalyticsEvent.logCampaignData(data);
        }

    }


    private void setUpNotificationCahnnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "tarangplus_android_default";
            // The user-visible name of the channel.
            CharSequence name = "default";
            // The user-visible description of the channel.
            String description = "Notifications regarding tarangplus";
            int importance = NotificationManager.IMPORTANCE_MAX;
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications_new posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }
    }


    public synchronized Tracker getTracker() {
        if (mMatomoTracker != null) return mMatomoTracker;
        mMatomoTracker = TrackerBuilder.createDefault(BuildConfig.ANALYTICS_URL, BuildConfig.ANALYTICS_SITE_ID).build(Matomo.getInstance(this));
        return mMatomoTracker;
    }


    public FirebaseAnalytics getFirebaseInstance() {
        if (mFirebaseAnalytics == null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        return mFirebaseAnalytics;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
