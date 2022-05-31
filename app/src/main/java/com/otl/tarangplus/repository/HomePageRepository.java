package com.otl.tarangplus.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.model.Analytics;
import com.otl.tarangplus.model.AppConfigDetails;
import com.otl.tarangplus.model.ConfigParams;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.HomeScreenResponse;
import com.otl.tarangplus.model.LayoutScheme;
import com.otl.tarangplus.model.ParamsHash2;
import com.otl.tarangplus.model.WebPages;
import com.otl.tarangplus.Database.AppDatabase;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomePageRepository {


    private static final String TAG = "HomePageRepo";
    private static HomePageRepository ourInstance;
    private ApiService mApiService;
    final MutableLiveData<Resource> homeScreenTabs = new MutableLiveData<>();
    MutableLiveData<Resource> homeScreenResponceUsingDisplayTitle = new MutableLiveData<>();
    private MutableLiveData<Resource> layoutSchemeResponse = new MutableLiveData<>();
    private LiveData<LayoutDbScheme> dbScheme = new MutableLiveData<>();
    private Executor executor = Executors.newSingleThreadExecutor();
    private AppDatabase mDb;
    private MutableLiveData<Resource> regionResponse = new MutableLiveData<>();


    public static HomePageRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new HomePageRepository(context);
        }
        return ourInstance;
    }

    private HomePageRepository(Context context) {
        RestClient mRestClient = new RestClient(context);
        mApiService = mRestClient.getApiService();
        mDb = AppDatabase.getInstance(context);
    }


    public MutableLiveData<Resource> getHomeScreenTabs() {
        return getHomePageTab();
    }

    public MutableLiveData<Resource> getHomeScreenResponceUsingDisplayTitl(String id) {
        return getHomeScreenResponceUsingDisplayTitle(id);
    }


    private MutableLiveData<Resource> getHomePageTab() {

        homeScreenTabs.setValue(Resource.loading(""));

        mApiService.getHomeScreenTabs().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<HomeScreenResponse>() {
            @Override
            public void call(HomeScreenResponse homeScreenResponse) {
                homeScreenTabs.setValue(Resource.success(homeScreenResponse));
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.toString());
                homeScreenTabs.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
            }
        });
        return homeScreenTabs;
    }

    private MutableLiveData<Resource> getHomeScreenResponceUsingDisplayTitle(String data) {

        homeScreenResponceUsingDisplayTitle.setValue(Resource.loading(""));


        mApiService.getHomeScreenDetailsBasedOnHomeLink(data).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<HomeScreenResponse>() {
            @Override
            public void call(HomeScreenResponse homeScreenResponse) {
                homeScreenResponceUsingDisplayTitle.setValue(Resource.success(homeScreenResponse));
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
//                Log.e(TAG, throwable.toString());
                homeScreenResponceUsingDisplayTitle.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
            }
        });
        return homeScreenResponceUsingDisplayTitle;
    }

    public MutableLiveData<Resource> getConfigLayout() {
        return getLayoutScheme();
    }

    private MutableLiveData<Resource> getLayoutScheme() {


        layoutSchemeResponse.setValue(Resource.loading(""));

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // if (mDb.layoutSchemeDao().countSchemes() <= 0) {
                addDataToDB();
                //  } else {
                // Constants.layoutDbScheme = mDb.layoutSchemeDao().getAll();
                // }
            }
        });

        return layoutSchemeResponse;
    }

    public LiveData<LayoutDbScheme> getLayoutByScheme(final String scheme) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                dbScheme = mDb.layoutSchemeDao().findByName(scheme);
            }
        });

        return dbScheme;
    }

    public void addDataToDB() {
        String verCode = BuildConfig.VERSION_CODE + "";
        mApiService.getConfigParms(verCode).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<AppConfigDetails>() {
            @Override
            public void call(final AppConfigDetails appConfigDetails) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Data data = appConfigDetails.getData();
                        if (data != null) {
                            ParamsHash2 hash2 = data.getParamsHash2();
                            if (hash2 != null) {
                                for (LayoutScheme scheme : hash2.getConfigParams().getLayoutScheme()) {
                                    LayoutDbScheme layoutDbScheme = new LayoutDbScheme();
                                    layoutDbScheme.setScheme(scheme.getScheme());
                                    layoutDbScheme.setStartColor(/*scheme.getStartColor()*/"ffffff");
                                    layoutDbScheme.setEndColor(/*scheme.getEndColor()*/"ffffff");
                                    layoutDbScheme.setMiddleColor(/*scheme.getMiddleColor()*/"ffffff");
                                    layoutDbScheme.setImageUrl(scheme.getImageUrl());
                                    mDb.layoutSchemeDao().insertAll(layoutDbScheme);
                                }
                                ConfigParams configParams = hash2.getConfigParams();

                                if (configParams != null) {
                                    if (configParams.getFreeTrialConfig() != null) {
                                        PreferenceHandler.setIsTrailAvaliable(MyApplication.getInstance(), configParams.getFreeTrialConfig().getFreeTrail());
                                        if (configParams.getFreeTrialConfig().getFreeTrail()) {
                                            PreferenceHandler.setStartTrailMessage(MyApplication.getInstance(), configParams.getFreeTrialConfig().getFreeTrailMsg());
                                        }
                                    }
                                    if (configParams.getAndroidVersion() != null) {
                                        PreferenceHandler.setForceUpgrade(MyApplication.getInstance(), Boolean.parseBoolean(configParams.getAndroidVersion().getForceUpgrade()));
                                        PreferenceHandler.setAndroidVersion(MyApplication.getInstance(), configParams.getAndroidVersion().getCurrentVersion());
                                    }
                                }

                                if (configParams != null) {
                                    WebPages webPages = configParams.getWebPages();
                                    if (webPages != null) {
                                        String faq = webPages.getFaq();
                                        String help = webPages.getHelp();
                                        String privacyPolicy = webPages.getPrivacyPolicy();
                                        String termsAndConditions = webPages.getTermsAndConditions();
                                        String contactUs = webPages.getContactUs();
                                        PreferenceHandler.setFAQUrl(MyApplication.getInstance(), faq);
                                        PreferenceHandler.setHelpURL(MyApplication.getInstance(), help);
                                        PreferenceHandler.setPrivacyUrl(MyApplication.getInstance(), privacyPolicy);
                                        PreferenceHandler.setTermsAndConditionUrl(MyApplication.getInstance(), termsAndConditions);
                                        PreferenceHandler.setContactUsUrl(MyApplication.getInstance(), contactUs);
                                    }
                                    Analytics analytics = configParams.getAnalytics();

                                    if (analytics != null) {
                                        String mediaActiveXMin = analytics.getMediaActiveXMin();
                                        PreferenceHandler.setMediaActiveInterval(mediaActiveXMin, MyApplication.getInstance());
                                        PreferenceHandler.setCleverMediaEnabled(analytics.isClevertap_mediaevents(), MyApplication.getInstance());
                                        AnalyticsProvider.IS_CLEVERTAP_ENABLED = analytics.isClevertap_mediaevents();
                                        AnalyticsProvider.IS_CLEVERTAP_ENABLED = true;  // TODO: 09/04/19 handle this
                                    }

                                    if (configParams.getProfileLimit() > 0)
                                        PreferenceHandler.setUserProfileLimit(MyApplication.getInstance(), configParams.getProfileLimit());
                                    else
                                        PreferenceHandler.setUserProfileLimit(MyApplication.getInstance(), 5);
                                }
                                Constants.PAYMENTURL = hash2.getConfigParams().getPaymentURL();
                                Constants.REGISTERED_DEVICES_URL = hash2.getConfigParams().getRegisteredDevicesURL();
                                if (Constants.REGISTERED_DEVICES_URL == null)
                                    Constants.REGISTERED_DEVICES_URL = "";
                                Constants.layoutDbScheme = mDb.layoutSchemeDao().getAll();
                            }
                        }

                    }
                });
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                layoutSchemeResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
            }
        });
    }

    public MutableLiveData<Resource> getRegions() {
        return getUserRegion();
    }

    private MutableLiveData<Resource> getUserRegion() {

        regionResponse.setValue(Resource.loading(""));

        mApiService.getRegions().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<JsonObject>() {
            @Override
            public void call(final JsonObject object) {
                try {
                    regionResponse.setValue(Resource.success(object));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                regionResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
            }
        });


        return regionResponse;
    }
}
