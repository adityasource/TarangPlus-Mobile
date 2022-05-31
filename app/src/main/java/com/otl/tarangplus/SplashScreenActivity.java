package com.otl.tarangplus;

import android.app.Dialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.inmobi.sdk.InMobiSdk;
import com.inmobi.sdk.SdkInitializationListener;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.BroadcastReciver.NetworkChangeReceiver;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.fragments.InternetUpdateFragment;
import com.otl.tarangplus.fragments.LoginFragment;
import com.otl.tarangplus.fragments.MovieDetailsFragment;
import com.otl.tarangplus.model.AppConfigDetails;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.AppVersion;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.HomePageViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SplashScreenActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener {

    HomePageViewModel mViewModel;
    public static String TAG = SplashScreenActivity.class.getSimpleName();
    @BindView(R.id.popup_negetive_button)
    GradientTextView popupNegetiveButton;
    /* @BindView(R.id.container)
     FrameLayout container;*/
    @BindView(R.id.no_region_ui)
    RelativeLayout mNoRegionUI;
    @BindView(R.id.video_layout)
    VideoView videoHolder;
    private ApiService mApiService;
    AnalyticsProvider analyticsProvider;

    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Helper.setLightStatusBar(this);
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }
        analyticsProvider = new AnalyticsProvider(getApplicationContext());
        analyticsProvider.updateAppLaunch(this);
        //getHashkey();
       /* byte[] sha1 = {
                //96:FF:EA:8B:E3:52:2F:8D:7E:85:FD:B4:BF:6C:41:74:8A:73:69:83
                (byte)0x96, (byte)0xFF, (byte)0xEA, (byte)0x8B, (byte)0xE3, 0x52, (byte)0x2F, (byte)0x8D,
                (byte)0x7E, (byte)0x85, (byte)0xFD, (byte)0xB4, (byte)0xBF, 0x6C, 0x41, (byte)0x74, (byte)0x8A, 0x73, (byte)0x69, (byte)0x83
        };
        System.out.println("keyhashGooglePlaySignIn:"+ Base64.encodeToString(sha1, Base64.NO_WRAP));*/
        try {
            splashPlayer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mAnalyticsEvent = Analytics.getInstance(this);
        appEvents = new AppEvents(1, "", "Splash Screen", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Splash Screen",
                PreferenceHandler.getUserState(this), PreferenceHandler.getUserPeriod(this),
                PreferenceHandler.getUserPlanType(this), PreferenceHandler.getUserId(this));
        mAnalyticsEvent.logAppEvents(appEvents);
/**
 *
 */
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.otl.tarangplus",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        receiver = new NetworkChangeReceiver();
        receiver.setNetworkChangeListener(this);

        mApiService = new RestClient(this).getApiService();
        mViewModel = ViewModelProviders.of(this).get(HomePageViewModel.class);
        mViewModel.addLayoutTypesToDB();

        try {
            getAccountDetailsIfLoggedIn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: 19/07/19 No more needed in splash-screen

        // TODO: 15/02/19 once the soft update and hard update are completed please comment this proceedFurther()

        analyticsProvider.trackAppInstalls();

        //for light icon on statusbasr check comment insight method "setLightStatusBar"
        //Helper.clearLightStatusBar(this);
        inMobiInit();
    }

    private void inMobiInit() {
        //inmobi
        JSONObject consentObject = new JSONObject();
        try {
            // Provide correct consent value to sdk which is obtained by User
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, false);
            // Provide 0 if GDPR is not applicable and 1 if applicable
            consentObject.put("gdpr", "0");
            // Provide user consent in IAB format
            //consentObject.put(InMobiSdk.IM_GDPR_CONSENT_IAB, “ <<consent in IAB format>> ”);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InMobiSdk.init(this, "c3704c9c520a43619f170e12849ec0ac", consentObject, new SdkInitializationListener() {
            @Override
            public void onInitializationComplete(@Nullable Error error) {
                if (null != error) {
                    Log.e(TAG, "InMobi Init failed -" + error.getMessage());
                } else {
                    Log.d(TAG, "InMobi Init Successful");
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        try {

            splashPlayer();
            popupNegetiveButton.setVisibility(View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onRestart();
    }

    public void splashPlayer() {

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.otv);
        videoHolder.setVideoURI(video);
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                proceedFurther();
            }
        });

        videoHolder.start();

        videoHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                ((VideoView) v).stopPlayback();
                //proceedFurther();
                return true;
            }
        });
    }

    public void proceedFurther() {

        mApiService.getRegions().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<JsonObject>() {
            @Override
            public void call(final JsonObject object) {
                Helper.dismissProgressDialog();
                JsonObject response = object;
                if (response != null) {
                    JsonObject region = response.get("region").getAsJsonObject();
                    if (region != null) {

                        String countryCode = "";
                        if (!TextUtils.isEmpty(region.get("country_code2").getAsString())) {
                            countryCode = region.get("country_code2").getAsString();

                        }

                        try {

                            if (!TextUtils.isEmpty(region.get("ip").getAsString())) {
                                String ip = region.get("ip").getAsString();
                                Constants.IP = ip;
                                PreferenceHandler.setIp(SplashScreenActivity.this, ip);
                            }


                            if (!TextUtils.isEmpty(region.get("real_region_name").getAsString())) {
                                String state = region.get("real_region_name").getAsString();
                                Constants.STATE = state;
                            }

                            if (!TextUtils.isEmpty(region.get("city_name").getAsString())) {
                                String city = region.get("city_name").getAsString();
                                Constants.CITY = city;
                                PreferenceHandler.setCityName(SplashScreenActivity.this, city);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "proceedFurther: " + e.getLocalizedMessage());
                        }
                        if (countryCode != null && !countryCode.isEmpty()) {
                            Constants.REGION = countryCode;
                            PreferenceHandler.setRegion(SplashScreenActivity.this, countryCode);
                            mViewModel.addLayoutTypesToDB();
                            //if (Constants.REGION.equalsIgnoreCase(Constants.INDIA) || Constants.REGION.equalsIgnoreCase(Constants.USA))
                            goToHomePage();
                            /*else {
                                mNoRegionUI.setVisibility(View.VISIBLE);
                            }*/

//                            analyticsProvider.fireAppLaunch();

                        } else {
                            // TODO: 09/10/18 No country detected
                            Helper.showToast(SplashScreenActivity.this, getResources().getString(R.string.no_country_dec), R.drawable.ic_error_icon);
                            popupNegetiveButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        popupNegetiveButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    popupNegetiveButton.setVisibility(View.VISIBLE);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Helper.dismissProgressDialog();
                popupNegetiveButton.setVisibility(View.VISIBLE);
            }
        });


/*
        mViewModel.getRegions().observe(this, resource -> {
            Helper.dismissProgressDialog();

            Resource.Status status = resource.status;
            if (status == Resource.Status.SUCCESS) {
                JsonObject response = (JsonObject) resource.data;
                if (response != null) {
                    JsonObject region = response.get("region").getAsJsonObject();
                    if (region != null) {

                        String countryCode = "";
                        if (!TextUtils.isEmpty(region.get("country_code2").getAsString())) {
                            countryCode = region.get("country_code2").getAsString();

                        }

                        try {

                            if (!TextUtils.isEmpty(region.get("ip").getAsString())) {
                                String ip = region.get("ip").getAsString();
                                Constants.IP = ip;
                                PreferenceHandler.setIp(SplashScreenActivity.this, ip);
                            }


                            if (!TextUtils.isEmpty(region.get("real_region_name").getAsString())) {
                                String state = region.get("real_region_name").getAsString();
                                Constants.STATE = state;
                            }

                            if (!TextUtils.isEmpty(region.get("city_name").getAsString())) {
                                String city = region.get("city_name").getAsString();
                                Constants.CITY = city;
                                PreferenceHandler.setCityName(SplashScreenActivity.this, city);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "proceedFurther: " + e.getLocalizedMessage());
                        }
                        if (countryCode != null && !countryCode.isEmpty()) {
                            Constants.REGION = countryCode;
                            PreferenceHandler.setRegion(SplashScreenActivity.this, countryCode);
                            mViewModel.addLayoutTypesToDB();
                            //if (Constants.REGION.equalsIgnoreCase(Constants.INDIA) || Constants.REGION.equalsIgnoreCase(Constants.USA))
                            goToHomePage();
                            */
/*else {
                                mNoRegionUI.setVisibility(View.VISIBLE);
                            }*//*


//                            analyticsProvider.fireAppLaunch();

                        } else {
                            // TODO: 09/10/18 No country detected
                            Helper.showToast(SplashScreenActivity.this, getResources().getString(R.string.no_country_dec), R.drawable.ic_error_icon);
                            popupNegetiveButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        popupNegetiveButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    popupNegetiveButton.setVisibility(View.VISIBLE);
                }
            } else if (status == Resource.Status.ERROR) {
                Helper.dismissProgressDialog();
                popupNegetiveButton.setVisibility(View.VISIBLE);

            } else if (status == Resource.Status.LOADING) {
                Log.d(TAG, "Loading");
            }
        });
*/


        Helper.setLightStatusBar(this);

        if (PreferenceHandler.getNotificationOn(this).equals("") || PreferenceHandler.getNotificationOn(this).equals(null)) {
            PreferenceHandler.setNotificationOn(this, Constants.YES);
        }

        if (PreferenceHandler.getLang(this).equals("") || PreferenceHandler.getLang(this).equals(null)) {
            PreferenceHandler.setLang(this, Constants.ENGLISH);
        }

        boolean loggedIn = PreferenceHandler.isLoggedIn(getApplicationContext());
        if (loggedIn) {
            //analyticsProvider.updateActiveUsers(this);


            String analyticsUserId = PreferenceHandler.getAnalyticsUserId(getApplicationContext());
            //analyticsProvider.updateUserId(analyticsUserId, TAG, SplashScreenActivity.this);

            boolean svodActive = PreferenceHandler.getIsSubscribed(getApplicationContext());
//            if (svodActive)
//                analyticsProvider.updateSVODActive(svodActive, analyticsUserId, this);
//            else
//                analyticsProvider.updateRegisteredUsers(analyticsUserId, this);
        }

    }


    public void checkWeatherHarOrSoftUpdate(AppConfigDetails appConfigDetails) {

        if (appConfigDetails == null || appConfigDetails.getData() == null || appConfigDetails.getData().getAppVersion() == null) {
            popupNegetiveButton.setVisibility(View.VISIBLE);
            return;
        }


        try {
            if (appConfigDetails.getData().getAppVersion().isForce_upgrade()) {
                showHardUpdateDialogBox(appConfigDetails.getData().getAppVersion());
            } else if (appConfigDetails.getData().getAppVersion().isRecomended_upgrade()) {
                showSoftUpdateDialogBox(appConfigDetails.getData().getAppVersion());
            } else {
                proceedFurther();
            }


        } catch (Exception e) {
            e.printStackTrace();
            proceedFurther();
        }

    }

    public void showHardUpdateDialogBox(AppVersion appVersion) {
        Dialog dialog = new Dialog(SplashScreenActivity.this);
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);

        cancel.setVisibility(View.GONE);

        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);
        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText(appVersion.getTitle());
        warningMessage.setText(appVersion.getMessage());
        cancel.setText(appVersion.getCancel_btn());
        confirm.setText(appVersion.getProceed_btn());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.otl.tarangplus")));
                dialog.cancel();
                finish();
            }
        });
    }

    public void showSoftUpdateDialogBox(AppVersion appVersion) {
        Dialog dialog = new Dialog(SplashScreenActivity.this);
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);

        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);
        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText(appVersion.getTitle());
        warningMessage.setText(appVersion.getMessage());
        cancel.setText(appVersion.getCancel_btn());
        confirm.setText(appVersion.getProceed_btn());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                proceedFurther();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.otl.tarangplus")));
                dialog.cancel();
                finish();
            }
        });
    }


    private void goToHomePage() {

        Intent deepLinkIntent = getIntent();
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);

        boolean loggedIn = PreferenceHandler.isLoggedIn(this);
        if (!loggedIn) {
            goToLoginPage();
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (deepLinkIntent != null && deepLinkIntent.getData() != null) {
                intent.setData(deepLinkIntent.getData());
            }
            Bundle bundle = deepLinkIntent.getBundleExtra(Constants.FROM_NOTIFICATION);
            if (bundle != null) {
                intent.putExtra(Constants.FROM_NOTIFICATION, bundle);
            } else if (deepLinkIntent != null && deepLinkIntent.getExtras() != null && !TextUtils.isEmpty(deepLinkIntent.getExtras().getString("URL"))) {
                Uri myUri = Uri.parse(deepLinkIntent.getExtras().getString("URL"));
                intent.setData(myUri);
            }
            startActivity(intent);

            finish();
        }
    }

    private void goToLoginPage() {
        Intent intent = new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
        intent.putExtra(Constants.FROM_PAGE, SplashScreenActivity.TAG);
        startActivityForResult(intent, Constants.ACTION_LOGIN_CLICKED);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Helper.dismissProgressDialog();
        super.onDestroy();
    }

    @OnClick(R.id.popup_negetive_button)
    public void onViewClicked() {
        Helper.showProgressDialog(SplashScreenActivity.this);
        popupNegetiveButton.setVisibility(View.GONE);
        proceedFurther();
    }

    private void getAccountDetailsIfLoggedIn() {
        boolean loggedIn = PreferenceHandler.isLoggedIn(this);
        if (!loggedIn) {
            return;
        }
        mApiService.getAccountDetails(PreferenceHandler.getSessionId(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse resonse) {

                        if (resonse != null) {
                            Helper.dismissProgressDialog();
                            updateDetails(resonse);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                    }
                });
    }

    private void updateDetails(ListResonse response) {
        Data data = response.getData();
        if (data != null) {
            boolean subscribed = data.isSubscribed();
            PreferenceHandler.setIsSubscribed(this, subscribed);
            if (data.getActivePlans() != null) {
                List<String> activePlans = data.getActivePlans();
                PreferenceHandler.udpateActivePlans(activePlans);
            }
            if (data.getInActivePlans() != null) {
                List<String> inActivePlans = data.getInActivePlans();
                PreferenceHandler.udpateInActivePlans(inActivePlans);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NetworkChangeReceiver receiver;
    private InternetUpdateFragment framgment;
    private boolean isDiconnected = false;

    @Override
    public void onNetworkDisconnected() {

        framgment = new InternetUpdateFragment();
        if (!isDiconnected) {
            Helper.addFragment(SplashScreenActivity.this, framgment, InternetUpdateFragment.TAG);
            isDiconnected = true;
        }
    }

    @Override
    public void onNetworkConnected() {
        if (isDiconnected) {
            onBackPressed();
            isDiconnected = false;
            Helper.showProgressDialog(SplashScreenActivity.this);
            popupNegetiveButton.setVisibility(View.GONE);
            mViewModel.getRegions();
        }
    }

    public void getHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.i("Base64", Base64.encodeToString(md.digest(),Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

}
