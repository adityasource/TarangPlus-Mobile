package com.otl.tarangplus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.mediarouter.app.MediaRouteButton;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comscore.Analytics;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.BroadcastReciver.NetworkChangeReceiver;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.FirebaseConfig;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.customeUI.SaranyuButtomNavView;
import com.otl.tarangplus.dialogs.WhoIsWatchingDialog;
import com.otl.tarangplus.fragments.AccountDetailsFragment;
import com.otl.tarangplus.fragments.ContinueWatchingFragment;
import com.otl.tarangplus.fragments.EditProfilesFragment;
import com.otl.tarangplus.fragments.HomePageFragment;
import com.otl.tarangplus.fragments.HomePageItemsFragment;
import com.otl.tarangplus.fragments.InternetUpdateFragment;
import com.otl.tarangplus.fragments.ListingFragment;
import com.otl.tarangplus.fragments.LiveTvDetailsFragment;
import com.otl.tarangplus.fragments.MePageFragment;
import com.otl.tarangplus.fragments.MoreListingFragment;
import com.otl.tarangplus.fragments.MovieDetailsFragment;
import com.otl.tarangplus.fragments.SearchFragment;
import com.otl.tarangplus.fragments.ShowDetailsPageFragment;
import com.otl.tarangplus.fragments.SubscribeBottomSheetDialog;
import com.otl.tarangplus.fragments.SubscriptionWebViewFragment;
import com.otl.tarangplus.fragments.TvFragment;
import com.otl.tarangplus.fragments.UpdatePersonalFragment;
import com.otl.tarangplus.fragments.WatchListFragment;
import com.otl.tarangplus.fragments.WhoIsWatchingFragment;
import com.otl.tarangplus.interfaces.GetVersion;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.HomePageViewModel;
import com.vidgyor.livemidroll.vidgyorPlayerManager.VidgyorStatusInit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NetworkChangeReceiver.NetworkChangeListener,
        MovieDetailsFragment.OnEventListener, ShowDetailsPageFragment.OnEventListener,
        LiveTvDetailsFragment.OnEventListener, SubscriptionWebViewFragment.OnEventListener,
        GetVersion.VersionCheck {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_UPDATE_REQUIRED = "update_required";
    private static final String KEY_CURRENT_VERSION = "current_version";
    private static final String KEY_UPDATE_URL = "some";
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    SaranyuButtomNavView navigation;
    @BindView(R.id.nav_shadow)
    TextView navShadow;

    Fragment mHomePageFragment;
    Fragment mSearchFragment;
    Fragment tvFragment;
    Fragment mMePageFragment;

    private ApiService apiService;
    AnalyticsProvider mAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    public GetVersion.VersionCheck versionCheck;
    /* CAST */
    private IntroductoryOverlay mIntroductoryOverlay;
    private CastStateListener mCastStateListener;
    private MenuItem mediaRouteMenuItem;
    //private CastContext mCastContext;
    public Toolbar toolbar;
    HomePageViewModel mViewModel;

    private SaranyuButtomNavView.BottomItemClickEvent mOnNavigationItemSelectedListener
            = new SaranyuButtomNavView.BottomItemClickEvent() {

        @Override
        public void clickedItem(View view) {
            boolean connected = NetworkChangeReceiver.isNetworkConnected(getApplicationContext());
            if (connected) {
                switch (view.getId()) {
                    case R.id.home_btn:
                        toolbar.setVisibility(View.GONE);
                        pausePlayerIfPlaying();
                        Helper.addWithoutBackStack(MainActivity.this, mHomePageFragment,
                                HomePageFragment.TAG);
                        Helper.setLightStatusBar(MainActivity.this);
                        ContinueWatchingFragment.updateData(PreferenceHandler.isLoggedIn(mContext));
                        break;
                    case R.id.home_search:
                        toolbar.setVisibility(View.GONE);
                        pausePlayerIfPlaying();
                        mSearchFragment = new SearchFragment();
                        Helper.addFragmentForDetailsScreen(MainActivity.this, mSearchFragment, SearchFragment.TAG);
                        Helper.setLightStatusBar(MainActivity.this);
                        break;
                    case R.id.home_tv:
                        toolbar.setVisibility(View.GONE);
                        pausePlayerIfPlaying();
                        tvFragment = new TvFragment();
                        Helper.addFragmentForDetailsScreen(MainActivity.this, tvFragment, TvFragment.TAG);
                        //Helper.clearLightStatusBar(MainActivity.this);
                        break;
                    case R.id.home_me:
                        toolbar.setVisibility(View.GONE);
                        pausePlayerIfPlaying();
                        mMePageFragment = new MePageFragment();
                        Helper.addFragmentForDetailsScreen(MainActivity.this, mMePageFragment, MePageFragment.TAG);
                        //Helper.setLightStatusBar(MainActivity.this);
                        break;
                }
            }
        }
    };
    private boolean isDiconnected = false;
    private boolean doubleBackToExitPressedOnce;
    private MainActivity mContext;
    private AlertDialog dialog;
    private FirebaseConfig firebaseConfig;

    private void pausePlayerIfPlaying() {
        Fragment currentFragment = Helper.getCurrentFragment(MainActivity.this);
        if (currentFragment != null) {
            if (currentFragment instanceof MovieDetailsFragment) {
                ((MovieDetailsFragment) currentFragment).pauseThePlayer();
            } else if (currentFragment instanceof ShowDetailsPageFragment) {
                ((ShowDetailsPageFragment) currentFragment).pauseThePlayer();
            } else if (currentFragment instanceof LiveTvDetailsFragment) {
                ((LiveTvDetailsFragment) currentFragment).pauseThePlayer();
            }
        }
    }

    private NetworkChangeReceiver receiver;
    private InternetUpdateFragment framgment;


    public void setVisibilityForToolBar(boolean b) {
        if (Helper.getCurrentFragment(MainActivity.this) instanceof MovieDetailsFragment || Helper.getCurrentFragment(MainActivity.this) instanceof ShowDetailsPageFragment ||
                Helper.getCurrentFragment(MainActivity.this) instanceof LiveTvDetailsFragment) {
            toolbar.setVisibility(View.VISIBLE);
        } else if (Helper.getCurrentFragment(MainActivity.this) instanceof SubscriptionWebViewFragment) {
            if (b) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        } else {
            toolbar.setVisibility(View.GONE);

        }
    }

    private void setupActionBar() {
        setVisibilityForToolBar(false);
        //toolbar.setTitle("Instaplay SDK CAST");
        /*Remove comment for Chrome cast*/
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
        try {
            HomePageFragment homePageFragment = (HomePageFragment) Helper.getFragmentByTag(this, HomePageFragment.TAG);
            homePageFragment.checkForDeepLinkingStatus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.d(TAG, " Firebase onNewIntent");
        super.onNewIntent(intent);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Helper.setLightStatusBar(this);
        mContext = this;
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("com.otl.tarangplus_FCM-MESSAGE"));
        apiService = new RestClient(mContext).getApiService();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = findViewById(R.id.toolbar_main);
        mViewModel = ViewModelProviders.of(this).get(HomePageViewModel.class);
        try {
            mViewModel.addLayoutTypesToDB();
        } catch (Exception e) {
        }
        mHomePageFragment = new HomePageFragment();
        //replaced homepage tag with HomePageFragment.class.getSimpleName()
        boolean loggedIn = PreferenceHandler.isLoggedIn(getApplicationContext());
        Helper.addWithoutBackStack(MainActivity.this, mHomePageFragment,
                HomePageFragment.TAG);
        navigation.setCustomObjectListener(mOnNavigationItemSelectedListener);

        receiver = new NetworkChangeReceiver();
        receiver.setNetworkChangeListener(this);
        setupActionBar();
        MovieDetailsFragment.setOnEventListener(this);
        ShowDetailsPageFragment.setOnEventListener(this);
        LiveTvDetailsFragment.setOnEventListener(this);
        SubscriptionWebViewFragment.setOnEventListener(this);
        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                Log.d(TAG, "onCastStateChanged: " + newState);
                if (newState != CastState.NO_DEVICES_AVAILABLE) {
                    showIntroductoryOverlay();
                }
            }
        };

       // mCastContext = CastContext.getSharedInstance(this);

        versionCheck = this;
        firebaseConfig = new FirebaseConfig(versionCheck);

        if (PreferenceHandler.getNotificationOn(MainActivity.this) == null ||
                PreferenceHandler.getNotificationOn(MainActivity.this).equals("")) {
            FirebaseMessaging.getInstance().subscribeToTopic("tarangplus")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, " Firebase onComplete");
                        }
                    });
        }

        float androidVersionFloat = Float.parseFloat(PreferenceHandler.getAndroidVersion(MainActivity.this));
        int androidVersion = (int) androidVersionFloat;
        boolean isForced = PreferenceHandler.getForceUpgrade(MainActivity.this);
        if (androidVersion > 0 && androidVersion > BuildConfig.VERSION_CODE)
            onUpdateNeeded(isForced, BuildConfig.PLAYSTORE_URL);

    }

    private Dialog dialogUpdate;
    private void onUpdateNeeded(final boolean forcedUpgrade, final String url) {

        dialogUpdate = new Dialog(MainActivity.this);
        dialogUpdate.setContentView(R.layout.watch_later_layout);
        dialogUpdate.setCancelable(false);

        if (forcedUpgrade) {
            dialogUpdate.setCancelable(false);
        } else {
            dialogUpdate.setCancelable(true);
        }

        dialogUpdate.show();

        MyTextView title = (MyTextView) dialogUpdate.findViewById(R.id.title);
        GradientTextView cancel = (GradientTextView) dialogUpdate.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialogUpdate.findViewById(R.id.confirm);
        MyTextView warningMessage = (MyTextView) dialogUpdate.findViewById(R.id.warning);

        title.setVisibility(View.VISIBLE);
        cancel.setText("Cancel");
        if (forcedUpgrade)
            cancel.setText("Close App");

        title.setText("UPDATE");
        confirm.setText("Update");

        if (forcedUpgrade) {
            warningMessage.setText("Please update the app to continue enjoying Tarang Plus");
        } else {
            warningMessage.setText("If you want to enjoy new features, please update the app");
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (forcedUpgrade)
                    finish();
                else
                    dialogUpdate.dismiss();

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialogUpdate.dismiss();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void onUpdateNeededOld(final Long version, final boolean forcedUpgrade, final String url) {
        dialog = new AlertDialog.Builder(this)
                .setTitle("Update Available")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    dialog.dismiss();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (forcedUpgrade)
                            finish();
                        else
                            dialog.dismiss();
                    }
                })
                .create();


        if (forcedUpgrade) {
            dialog.setCancelable(false);
            dialog.setMessage("You are required to upgrade the app to enjoy the new features of TarangPlus.");
        } else {
            dialog.setMessage("Please, upgrade the app to enjoy the new features of TarangPlus.");
        }
        dialog.show();
    }

    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                            MainActivity.this, mediaRouteMenuItem)
                            .setTitleText("Introducing Cast")
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mAnalytics = new AnalyticsProvider(this);
        mAnalytics.fireAppLaunch(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
        getAccountDetailsIfLoggedIn();
        if (firebaseConfig != null) {
            firebaseConfig.fetchNewImage(this);
        }
        Analytics.notifyEnterForeground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
        Analytics.notifyExitForeground();
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
//            e.printStackTrace();
        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Constants.FULLSCREENFLAG) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                //Helper.setLightStatusBar(this);
            } else {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onNetworkDisconnected() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        framgment = new InternetUpdateFragment();
        if (!isDiconnected) {
            pausePlayerIfPlaying();
            Helper.addFragment(MainActivity.this, framgment, InternetUpdateFragment.TAG);
            isDiconnected = true;
        }
        Fragment currentFragment = Helper.getCurrentFragment(this);
        if (currentFragment instanceof ShowDetailsPageFragment) {
            SubscribeBottomSheetDialog sheetFragment = ((ShowDetailsPageFragment) currentFragment).bottomSheetFragment;
            if (sheetFragment != null) {
                sheetFragment.dismiss();
            }
        } else if (currentFragment instanceof MovieDetailsFragment) {
            SubscribeBottomSheetDialog sheetFragment = ((MovieDetailsFragment) currentFragment).bottomSheetFragment;
            if (sheetFragment != null) {
                sheetFragment.dismiss();
            }
        } else if (currentFragment instanceof LiveTvDetailsFragment) {
            SubscribeBottomSheetDialog sheetFragment = ((LiveTvDetailsFragment) currentFragment).bottomSheetFragment;
            if (sheetFragment != null) {
                sheetFragment.dismiss();
            }
        } else if (currentFragment instanceof MePageFragment) {
            Dialog logoutPopUp = ((MePageFragment) currentFragment).getLogoutPopUp();
            if (logoutPopUp != null) {
                logoutPopUp.dismiss();
            }
        } else if (currentFragment instanceof AccountDetailsFragment) {
            Dialog logoutPopUp = ((AccountDetailsFragment) currentFragment).getLogoutPopUp();
            if (logoutPopUp != null) {
                logoutPopUp.dismiss();
            }
        }

        AnalyticsProvider provider = new AnalyticsProvider(getApplicationContext());
        provider.reportNetworkError();

        // TODO: 25/02/19 add analytics
    }

    @Override
    public void onNetworkConnected() {

        if (isDiconnected) {
            Fragment currentFragment = Helper.getCurrentFragment(this);
            if (currentFragment != null) {
                if (currentFragment instanceof InternetUpdateFragment) {
                    onBackPressed();
                }
            }
            isDiconnected = false;

            Fragment fragment = Helper.getCurrentFragment(this);
            if (fragment instanceof MovieDetailsFragment || fragment instanceof ShowDetailsPageFragment || fragment instanceof LiveTvDetailsFragment)
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.cast_menu, menu);

        try {
            mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(MainActivity.this, menu,
                    R.id.media_route_menu_item);

            MediaRouteButton mediaRouteButton = (androidx.mediarouter.app.MediaRouteButton) MenuItemCompat.getActionView(mediaRouteMenuItem);
            colorWorkaroundForCastIcon(mediaRouteButton);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }


    public void setStatusBarColor(boolean shouldChangeStatusBarTintToDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (shouldChangeStatusBarTintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        }
    }

    private void colorWorkaroundForCastIcon(MediaRouteButton button) {
      /*  if (button == null) return;
        Context castContext = new ContextThemeWrapper(this, android.support.v7.mediarouter.R.style.Theme_MediaRouter);

        TypedArray a = castContext.obtainStyledAttributes(null,
                android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0);
        Drawable drawable = a.getDrawable(
      4e          android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable);
        a.recycle();
        DrawableCompat.setTint(drawable, this.getResources().getColor(R.color.white));
        drawable.setState(button.getDrawableState());
        button.setRemoteIndicatorDrawable(drawable);*/
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Fragment currentFragment = Helper.getCurrentFragment(MainActivity.this);
        if (currentFragment != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            if (currentFragment instanceof MovieDetailsFragment) {
                MovieDetailsFragment fragment = (MovieDetailsFragment) currentFragment;
                fragment.updateOrientationChange(newConfig.orientation);
                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setLandscapeMargin(lp);
                } else {
                    setPortraitMargin(lp);
                }
            } else if (currentFragment instanceof ShowDetailsPageFragment) {
                ShowDetailsPageFragment fragment = (ShowDetailsPageFragment) currentFragment;
                fragment.updateOrientationChange(newConfig.orientation);
                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setLandscapeMargin(lp);
                } else {
                    setPortraitMargin(lp);
                }
            } else if (currentFragment instanceof LiveTvDetailsFragment) {
                LiveTvDetailsFragment fragment = (LiveTvDetailsFragment) currentFragment;
                fragment.updateOrientationChange(newConfig.orientation);
                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setLandscapeMargin(lp);
                } else {
                    setPortraitMargin(lp);
                }
            } else if (currentFragment instanceof HomePageFragment) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                navigation.setVisibility(View.VISIBLE);
                navShadow.setVisibility(View.VISIBLE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                navigation.setVisibility(View.VISIBLE);
                navShadow.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setLandscapeMargin(RelativeLayout.LayoutParams lp) {
        //lp.setMargins(Helper.dpToPx(100), Helper.dpToPx(-1), Helper.dpToPx(92), Helper.dpToPx(0));
        //toolbar.setLayoutParams(lp);

        navigation.setVisibility(View.GONE);
        navShadow.setVisibility(View.GONE);
    }

    private void setPortraitMargin(RelativeLayout.LayoutParams lp) {
        //lp.setMargins(Helper.dpToPx(100), Helper.dpToPx(25), Helper.dpToPx(0), Helper.dpToPx(0));
        //toolbar.setLayoutParams(lp);

        navigation.setVisibility(View.VISIBLE);
        navShadow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
//        mCastContext = CastContext.getSharedInstance(this);
        Fragment currentFragment = Helper.getCurrentFragment(MainActivity.this);
        if (currentFragment != null) {

            if (currentFragment instanceof MovieDetailsFragment) {
                int orientation = getResources().getConfiguration().orientation;
                //Helper.fullScreenView(MainActivity.this);
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ((MovieDetailsFragment) currentFragment).updateOrientationChange(Configuration.ORIENTATION_PORTRAIT);
                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    super.onBackPressed();
                    //showExitMessage();
                }

            } else if (currentFragment instanceof ShowDetailsPageFragment) {
                int orientation = getResources().getConfiguration().orientation;
                //Helper.fullScreenView(MainActivity.this);
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ((ShowDetailsPageFragment) currentFragment).updateOrientationChange(Configuration.ORIENTATION_PORTRAIT);
                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    super.onBackPressed();
                }
            } else if (currentFragment instanceof LiveTvDetailsFragment) {
                int orientation = getResources().getConfiguration().orientation;
                //Helper.fullScreenView(MainActivity.this);
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ((LiveTvDetailsFragment) currentFragment).updateOrientationChange(Configuration.ORIENTATION_PORTRAIT);
                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ((LiveTvDetailsFragment) currentFragment).vidgyorPlayerrelease();
                    super.onBackPressed();
                }
            } else if (currentFragment instanceof HomePageFragment) {
                //finish();
                ContinueWatchingFragment.updateData(PreferenceHandler.isLoggedIn(mContext));
                showExitMessage();
            } else if (currentFragment instanceof EditProfilesFragment) {
                super.onBackPressed();
                Fragment currentFragmentTemp = Helper.getCurrentFragment(MainActivity.this);
                if (currentFragmentTemp instanceof WhoIsWatchingFragment) {
                    ((WhoIsWatchingFragment) currentFragmentTemp).getAllProfiles();
                }
            } else if (currentFragment instanceof WhoIsWatchingFragment) {
                super.onBackPressed();
                Fragment currentFragmentTemp = Helper.getCurrentFragment(MainActivity.this);
                if (currentFragmentTemp instanceof MePageFragment) {
                    ((MePageFragment) currentFragmentTemp).setDataAccordingly();
                }
            } else if (currentFragment instanceof WhoIsWatchingDialog) {
                super.onBackPressed();
                Fragment currentFragmentTemp = Helper.getCurrentFragment(MainActivity.this);
                if (currentFragmentTemp instanceof WhoIsWatchingDialog) {
                    ((WhoIsWatchingDialog) currentFragmentTemp).getProfiles();
                }
            } else if (currentFragment instanceof UpdatePersonalFragment) {
                super.onBackPressed();
                AccountDetailsFragment fragment = (AccountDetailsFragment) Helper.getCurrentFragment(MainActivity.this);
                fragment.onResume();
            } else if (currentFragment instanceof InternetUpdateFragment) {
                boolean connected = NetworkChangeReceiver.isNetworkConnected(getApplicationContext());
                if (connected)
                    super.onBackPressed();
                // don't do anything
            } else {
                super.onBackPressed();
                //showExitMessage();
            }

        } else {
            //super.onBackPressed();
            showExitMessage();
        }


        /**
         * This below code is for handling bottom navigation states
         * **/

        changeBottomTab();

        /***********/

        try {
            //  Helper.setStatusBarColor(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeBottomTab() {
        Fragment currentFragment;
        currentFragment = Constants.getCurrentFragment(MainActivity.this);
        if (currentFragment != null) {
            if (currentFragment instanceof SearchFragment)
                setSelectedNavUI(Constants.TABS.SEARCH);
            else if (currentFragment instanceof TvFragment)
                setSelectedNavUI(Constants.TABS.TV);
            else if (currentFragment instanceof LiveTvDetailsFragment)
                setSelectedNavUI(Constants.TABS.TV);
            else if (currentFragment instanceof MePageFragment)
                setSelectedNavUI(Constants.TABS.ME);
            else if (currentFragment instanceof HomePageFragment) {
                setSelectedNavUI(Constants.TABS.HOME);
                ContinueWatchingFragment.updateData(PreferenceHandler.isLoggedIn(mContext));
                // Added below code to handel bottom nav selected state
            } else if (currentFragment instanceof MovieDetailsFragment || currentFragment instanceof ShowDetailsPageFragment
                    || currentFragment instanceof MoreListingFragment) {
                selectBottomTab();
            } else if (currentFragment instanceof WatchListFragment) {
                setSelectedNavUI(Constants.TABS.ME);
                WatchListFragment fragment = (WatchListFragment) Helper.getCurrentFragment(MainActivity.this);
                fragment.resetAndUpdate();
            } else if (currentFragment instanceof ListingFragment) {
                try {
                    String fromWhere = currentFragment.getArguments().getString(Constants.FROM_WHERE);
                    if (fromWhere.equalsIgnoreCase(HomePageItemsFragment.TAG)) {
                        setSelectedNavUI(Constants.TABS.HOME);
                    } else if (fromWhere.equalsIgnoreCase(TvFragment.TAG)) {
                        setSelectedNavUI(Constants.TABS.TV);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //ContinueWatchingFragment.updateData(PreferenceHandler.isLoggedIn(mContext));
            }
        }
    }

    private void selectBottomTab() {

        FragmentTransaction transaction = ((AppCompatActivity) this).getSupportFragmentManager().beginTransaction();
        FragmentManager fm = ((AppCompatActivity) this).getSupportFragmentManager();
        int counttillhome = 0;
        if (fm != null) {
            List<Fragment> fragments = fm.getFragments();

            if (fragments == null)
                return;

            for (int i = fragments.size() - 1; i >= 0; --i) {
                Fragment currentFragment = fragments.get(i);
                if (currentFragment != null) {
                    if (currentFragment instanceof SearchFragment) {
                        setSelectedNavUI(Constants.TABS.SEARCH);
                        break;
                    } else if (currentFragment instanceof TvFragment) {
                        setSelectedNavUI(Constants.TABS.TV);
                        break;
                    } else if (currentFragment instanceof LiveTvDetailsFragment) {
                        setSelectedNavUI(Constants.TABS.TV);
                        break;
                    } else if (currentFragment instanceof MePageFragment || currentFragment instanceof WatchListFragment) {
                        setSelectedNavUI(Constants.TABS.ME);
                        break;
                    } else if (currentFragment instanceof HomePageFragment) {
                        setSelectedNavUI(Constants.TABS.HOME);
                        ContinueWatchingFragment.updateData(PreferenceHandler.isLoggedIn(mContext));
                        break;
                    }
                }

            }
        }

    }

    private void showExitMessage() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }


        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, R.string.exit_alert, Toast.LENGTH_SHORT).show();

        Helper.showToast(MainActivity.this, getResources().getString(R.string.exit_alert), R.drawable.ic_error_icon);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Fragment fragment2 = Helper.getFragmentByTag(MainActivity.this, MePageFragment.TAG);
        if (fragment2 != null && fragment2.isVisible()) {
            MePageFragment newFragmet = (MePageFragment) fragment2;
            newFragmet.setDataAccordingly();
        }

        Fragment currentFragment = Helper.getCurrentFragment(this);
        if (currentFragment != null) {
            if (currentFragment instanceof MePageFragment) {
                MePageFragment newFragmet = (MePageFragment) currentFragment;
                newFragmet.setDataAccordingly();
            }
        }

        checkIfDetailPageisThere();
        ContinueWatchingFragment.setAdapterAndUpdate(PreferenceHandler.isLoggedIn(mContext), mContext);


    }

    private void checkIfDetailPageisThere() {

        if (mContext == null)
            mContext = MainActivity.this;

        Fragment fragmentM = Helper.getFragmentByTag(mContext, MovieDetailsFragment.TAG);
        Fragment fragmentS = Helper.getFragmentByTag(mContext, ShowDetailsPageFragment.TAG);
        Fragment fragmentL = Helper.getFragmentByTag(mContext, LiveTvDetailsFragment.TAG);

        try {
            if (fragmentM != null) {
                ((MovieDetailsFragment) fragmentM).getDetails();
            }
            if (fragmentS != null) {
                ((ShowDetailsPageFragment) fragmentS).getDetails();
            }
            if (fragmentL != null) {
                ((LiveTvDetailsFragment) fragmentL).getDetails();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        //Helper.clearLightStatusBar(this);
        VidgyorStatusInit.stopCasting();
        Helper.dismissProgressDialog();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.ACTION_ME_LOGIN || requestCode == Constants.ACTION_ME_LOGIN) {
          /*  FragmentManager fragmentManager = getSupportFragmentManager();
            //this will clear the back stack and displays no animation on the screen
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            setSelectedNavUI(Constants.TABS.HOME);
           */
            Fragment currentFragment = Helper.getCurrentFragment(MainActivity.this);
            if (currentFragment != null) {
                if (currentFragment instanceof SubscriptionWebViewFragment) {
                    SubscriptionWebViewFragment subscriptionWebViewFragment = (SubscriptionWebViewFragment) currentFragment;
                    subscriptionWebViewFragment.refreshAfterLogin2();
                }
            }


        }
    }

    public void setSelectedNavUI(String selectedNavUI) {
        navigation.setSelectedUI(selectedNavUI);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Fragment fragment = Helper.getCurrentFragment(MainActivity.this);

        if (fragment instanceof SubscriptionWebViewFragment) {
            SubscriptionWebViewFragment bf = (SubscriptionWebViewFragment) fragment;
            boolean t = bf.myOnKeyDown(keyCode, super.onKeyDown(keyCode, event));
            return t;
        }

        return super.onKeyDown(keyCode, event);
    }


    /*For analytics data*/

    private void getAccountDetailsIfLoggedIn() {
        boolean loggedIn = PreferenceHandler.isLoggedIn(this);
        if (!loggedIn) {
            return;
        }
        apiService.getAccountDetails(PreferenceHandler.getSessionId(this))
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

    private void updateDetails(ListResonse resonse) {
        Data data = resonse.getData();
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
            Helper.updateCleverTapDetails(mContext);
        }
    }


    private void updateCleverTapDetails(Context mContext) {
//        if (mContext != null) {
//            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
//            // String
//            if (PreferenceHandler.isLoggedIn(mContext)) {
//                profileUpdate.put("Identity", PreferenceHandler.getAnalyticsUserId(mContext));                    // String or number   get User ID
//
//                String activePlans = PreferenceHandler.getActivePlans();
//                String inActivePlans = PreferenceHandler.getInActivePlans();
//                if (!TextUtils.isEmpty(activePlans)) {
//                    String[] split = activePlans.split(",");
//                    profileUpdate.put("active_plans", split);
//                }
//
//                if (!TextUtils.isEmpty(inActivePlans)) {
//                    String[] split = inActivePlans.split(",");
//                    profileUpdate.put("inactive_plans", split);
//                }
//
//                profileUpdate.put("is_subscribed", PreferenceHandler.getIsSubscribed(mContext));
//                profileUpdate.put("is_loggedIn", true);
//            }
//
//
//            if (PreferenceHandler.getNotificationEnabled(mContext))
//                profileUpdate.put("MSG-push", true);                        // Enable push notifications_new
//            else
//                profileUpdate.put("MSG-push", false);                        // Enable push notifications_new
//
//            AnalyticsProvider.sendProfileDataToCleverTap(profileUpdate);
//        }
    }


    @Override
    public void onEvent(boolean b) {
        setVisibilityForToolBar(b);
    }

    @Override
    public void OnVersionChanged(Long version, boolean forcedUpgrade, String url) {
      /*  Long currentVersion = Long.parseLong(BuildConfig.VERSION_CODE + "");

        if (version.longValue() > currentVersion.longValue()) {
            onUpdateNeeded(version, forcedUpgrade, url);
        }*/

    }

    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String image_URL = intent.getStringExtra("imageurl");
            String description = intent.getStringExtra("description");
            Helper.showToast(MainActivity.this, title, description, image_URL);
            Log.d(TAG, " Firebase mHandler");
        }
    };
}