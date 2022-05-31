package com.otl.tarangplus.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import java.io.IOException;
import java.io.InputStream;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.fragments.ContinueWatchingFragment;
import com.otl.tarangplus.fragments.HomePageFragment;
import com.otl.tarangplus.fragments.ListingFragment;
import com.otl.tarangplus.fragments.LiveTvDetailsFragment;
import com.otl.tarangplus.fragments.MePageFragment;
import com.otl.tarangplus.fragments.MovieDetailsFragment;
import com.otl.tarangplus.fragments.RegisterFragment;
import com.otl.tarangplus.fragments.SearchFragment;
import com.otl.tarangplus.fragments.ShowDetailsPageFragment;
import com.otl.tarangplus.fragments.SubscriptionWebViewFragment;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.HeartBeatData;
import com.otl.tarangplus.model.HeartBeatRequest;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.ListItemObject;
import com.otl.tarangplus.model.PlayList;
import com.otl.tarangplus.model.PlayListItem;
import com.saranyu.ott.instaplaysdk.InstaPlayView;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlResponseV2;
import com.otl.tarangplus.Database.AppDatabase;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.SignOutDetails;
import com.squareup.picasso.Picasso;
import com.vidgyor.activity.LiveTVFullScreenActivity;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class Helper {
    private static CustomeProgressBar mProgressDialog;
    private static CustomeProgressBar mProgressDialog2;

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public Helper(Activity mActivity) {
        mProgressDialog = new CustomeProgressBar(mActivity);
    }

    private static AppCompatActivity activity;

    public static void showProgressDialog2(Activity mActivity) {
        activity = (AppCompatActivity) mActivity;
        if (mProgressDialog2 != null) {
            mProgressDialog2.dismiss();
            mProgressDialog2 = null;
            mProgressDialog2 = new CustomeProgressBar(mActivity);
        }
        if (mProgressDialog2 != null && mProgressDialog2.activity == mActivity) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog2.setCancelable(false);
                    mProgressDialog2.show();
                }
            });
        } else {
            mProgressDialog2 = null;
            mProgressDialog2 = new CustomeProgressBar(mActivity);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog2.setCancelable(false);
                    mProgressDialog2.show();
                }
            });
        }
    }

    public static void dismissProgressDialog2() {
        if (mProgressDialog2 != null) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog2.dismiss();
                }
            });
        }

    }

    public static void showProgressDialog(Activity mActivity) {
        activity = (AppCompatActivity) mActivity;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            mProgressDialog = new CustomeProgressBar(mActivity);
        }
        if (mProgressDialog != null && mProgressDialog.activity == mActivity) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                }
            });
        } else {
            mProgressDialog = null;
            mProgressDialog = new CustomeProgressBar(mActivity);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                }
            });
        }
    }

    public static void showProgressDialog(final String title) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.show();

            }
        });

    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
        }

    }

    static public void runOnUIThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static void showToast(Activity mActivity, String message, int drawable) {
        if (mActivity != null && !TextUtils.isEmpty(message)) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) mActivity.findViewById(R.id.toast_layout_root));

            MyTextView text = (MyTextView) layout.findViewById(R.id.text);
            text.setText(message);

            ImageView image = (ImageView) layout.findViewById(R.id.image);
            image.setImageResource(drawable);

            Toast toast = new Toast(mActivity);
            toast.setGravity(Gravity.BOTTOM, 0, 150);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);

            try {
                // TODO: 06/10/18 Caused by: java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showToast(Activity mActivity, String title1, String description1, String image_url) {
        if (mActivity != null && !TextUtils.isEmpty(title1)) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View layout = inflater.inflate(R.layout.notification_toast, (ViewGroup) mActivity.findViewById(R.id.toast_layout_root));

            MyTextView title = (MyTextView) layout.findViewById(R.id.title);
            title.setText(title1);

            MyTextView description = (MyTextView) layout.findViewById(R.id.description);
            description.setText(description1);

            ImageView image = (ImageView) layout.findViewById(R.id.image);
            Picasso.get().load(image_url).placeholder(R.drawable.place_holder_16x9).into(image);
            Toast toast = new Toast(mActivity);
            toast.setGravity(Gravity.TOP, 0, 40);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);

            try {
                // TODO: 06/10/18 Caused by: java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addFragment(Activity mActivity, Fragment fragment, String tag) {
        FragmentTransaction transaction = ((AppCompatActivity) mActivity).getSupportFragmentManager().beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

        if (!fragment.isAdded()) {
            transaction.add(R.id.container, fragment, tag);
            transaction.addToBackStack(null);
//            transaction.commit();
            transaction.commitAllowingStateLoss();
        } else {
            Fragment fragment1 = ((AppCompatActivity) mActivity).getSupportFragmentManager().findFragmentByTag(tag);
            transaction.replace(R.id.container, fragment1, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public static void removeCurrentFragment(Activity mActivity, String tag) {
        if (mActivity != null) {
            FragmentTransaction transaction = ((AppCompatActivity) mActivity).getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

            Fragment fragment1 = ((AppCompatActivity) mActivity).getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment1 != null) {
                transaction.remove(fragment1);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    public static Fragment getCurrentFragment(Activity mActivity) {
        if (mActivity != null)
            return ((AppCompatActivity) mActivity).getSupportFragmentManager().findFragmentById(R.id.container);
        else return null;
    }

    public static void fullScreenView(Activity mActivity) {
        if (null != mActivity && null != mActivity.getCurrentFocus())
            mActivity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
           /* mActivity.getWindow().getDecorView().setSystemUiVisibility(
                              View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            *//*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*//*
         *//*| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN*//*
         *//*| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION*//*
         *//*| View.SYSTEM_UI_FLAG_FULLSCREEN*//*
         *//*| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY*//*);*/
    }

    public static void dismissKeyboard(Activity mActivity) {
        if (mActivity != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
            if (null != mActivity.getCurrentFocus())
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus()
                        .getApplicationWindowToken(), 0);
            //fullScreenView(mActivity);
        }
    }

    public static void dismissKeyboardWithoutFlags(Activity mActivity) {
        if (mActivity != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
            if (null != mActivity.getCurrentFocus())
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus()
                        .getApplicationWindowToken(), 0);
        }
    }

    public static boolean isKeyboardVisible(Activity mActivity) {
        try {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            return imm.isActive();
        } catch (Exception e) {
            return false;
        }
    }

    public static void moveToPageBasedOnListItemObject(Activity mActivity, CatalogListItem catalogListItem) {
        if (mActivity != null && catalogListItem != null && catalogListItem.getListItemObject() != null) {
            ListItemObject listItemObject = catalogListItem.getListItemObject();
            if (listItemObject.getLink() != null) {
                AnalyticsProvider analyticsProvider = new AnalyticsProvider(mActivity);
                if (listItemObject.getLink().equalsIgnoreCase(Constants.LINK_LOGIN)) {


                    if (PreferenceHandler.isLoggedIn(mActivity)) {
                        return;
                    }


                    Intent intent = new Intent(mActivity, OnBoardingActivity.class);
                    intent.putExtra(Constants.FROM_PAGE, HomePageFragment.TAG);
                    mActivity.startActivityForResult(intent, Constants.ACTION_LOGIN_CLICKED);
                } else if (listItemObject.getLink().equalsIgnoreCase(Constants.LINK_REGISTER)) {

                    if (PreferenceHandler.isLoggedIn(mActivity)) {
                        return;
                    }

                    Intent intent = new Intent(mActivity, OnBoardingActivity.class);
                    intent.putExtra(Constants.FROM_PAGE, RegisterFragment.TAG);
                    mActivity.startActivityForResult(intent, Constants.ACTION_LOGIN_CLICKED);
                } else if (listItemObject.getLink().equalsIgnoreCase(Constants.LINK_VIEW_PLAN)) {
                    SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FROM_WHERE, MePageFragment.TAG);
                    subscriptionWebViewFragment.setArguments(bundle);

                    analyticsProvider.fireNetCoreViewPlanClick();
                    //analyticsProvider.branchViewPlans(mActivity);
                    if (PreferenceHandler.isLoggedIn(mActivity)) {
                        bundle.putBoolean(Constants.IS_LOGGED_IN, true);
                    } else {
                        bundle.putBoolean(Constants.IS_LOGGED_IN, false);
                    }

                    Helper.addFragmentForDetailsScreen(mActivity, subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
                } else if (listItemObject.getLink().equalsIgnoreCase(Constants.STATIC_BANNER)) {

                    //it should not listen to any clicks

                } else if (listItemObject.getLink().equalsIgnoreCase(Constants.PREMIER)) {

                    //need to implement this

                } else if (!TextUtils.isEmpty(listItemObject.getLink())) {
                    ListingFragment fragment = new ListingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getDisplayTitle());
                    bundle.putString(Constants.HOME_LINK, listItemObject.getLink());
                    bundle.putString(Constants.THEME, catalogListItem.getTheme());
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getLayoutScheme());
                    fragment.setArguments(bundle);
                    Helper.addFragment(mActivity, fragment, ListingFragment.TAG);
                }

            }
        }
    }

    public static void moveToPageBasedOnTheme(Activity mActivity, CatalogListItem catalogListItem) {
        if (catalogListItem != null && catalogListItem.getTheme() != null) {
            if (catalogListItem.getTheme().equalsIgnoreCase("movie")
                /*|| catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("movie")*/) {
                MovieDetailsFragment fragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentID());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogID());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, MovieDetailsFragment.TAG);
            } else if (catalogListItem.getTheme().equalsIgnoreCase("show")
                    && (catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("show") ||
                    catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("shows"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentID());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogID());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, false);
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if ((catalogListItem.getTheme().equalsIgnoreCase("episode") ||
                    catalogListItem.getTheme().equalsIgnoreCase("show_episode")) &&
                    ((catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("show") ||
                            catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("shows") ||
                            catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("episode")))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentID());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogID());
                bundle.putString(Constants.SHOW_ID, catalogListItem.getShowThemeId());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, true);
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if ((catalogListItem.getTheme().equalsIgnoreCase("episode") ||
                    catalogListItem.getTheme().equalsIgnoreCase("album"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentID());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogID());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                bundle.putString(Constants.SHOW_ID, catalogListItem.getShowThemeId());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, true);
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if ((catalogListItem.getTheme().equalsIgnoreCase("show") ||
                    catalogListItem.getTheme().equalsIgnoreCase("album"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentID());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogID());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, false);
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if (catalogListItem.getTheme().equalsIgnoreCase("live")
                    || catalogListItem.getTheme().equalsIgnoreCase("linear")) {
     /*    if(catalogListItem.getPlayUrlType()!= null && catalogListItem.getPlayUrlType().equals("vidgyor")){
             Intent i = new Intent(mActivity, LiveTVFullScreenActivity.class);
             i.putExtra("channelName", catalogListItem.getChannelName());
             i.putExtra("packageName", "com.otl.tarangplus");
             mActivity.startActivity(i);
         }else{*/
             LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
             Bundle bundle = new Bundle();
             bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
             bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogID());
             bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentID());
             if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                 bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
             bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
             bundle.putString(Constants.THEME, catalogListItem.getTheme());
             bundle.putString(Constants.LAYOUT_TYPE_SELECTED, catalogListItem.getLayoutType());
             bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutType());
             fragment.setArguments(bundle);
             addFragmentForDetailsScreen(mActivity, fragment, LiveTvDetailsFragment.TAG);
         //}



            } else {
                MovieDetailsFragment fragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentID());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogID());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, MovieDetailsFragment.TAG);
            }
        }
    }


    // Added fourth param for auto play for continue watching item
    public static void moveToPageBasedOnTheme(Activity mActivity, Item catalogListItem, String tag) {
        if (catalogListItem != null && catalogListItem.getTheme() != null) {
            if (catalogListItem.getTheme().equalsIgnoreCase("live") ||
                    catalogListItem.getTheme().equalsIgnoreCase("linear")) {
             /*   if(catalogListItem.getPlayUrlType()!= null && catalogListItem.getPlayUrlType().equals("vidgyor")){
                    Intent i = new Intent(mActivity, LiveTVFullScreenActivity.class);
                    i.putExtra("channelName", catalogListItem.getChannelName());
                    i.putExtra("packageName", "com.otl.tarangplus");
                    mActivity.startActivity(i);
                }else {*/
                    LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                    bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                    bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                    if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                        bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                    bundle.putString(Constants.THEME, catalogListItem.getTheme());
                    bundle.putString(Constants.LAYOUT_TYPE_SELECTED, catalogListItem.getCatalogObject().getLayoutType());
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());
                    if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                        bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                    fragment.setArguments(bundle);
                    Helper.addFragmentForDetailsScreen(mActivity, fragment, LiveTvDetailsFragment.TAG);
               // }
            } else if (catalogListItem.getTheme().equalsIgnoreCase("movie")/* ||
                    catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("movie")*/) {
                MovieDetailsFragment fragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());
                if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                    bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, MovieDetailsFragment.TAG);
            } else if (catalogListItem.getTheme().equalsIgnoreCase("show")
                    && (catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("show") ||
                    catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("shows"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, false);
                if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                    bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if ((catalogListItem.getTheme().equalsIgnoreCase("episode") ||
                    catalogListItem.getTheme().equalsIgnoreCase("show_episode")) /*&&
                    ((catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("show") ||
                            catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("shows") ||
                            catalogListItem.getCatalogObject().getLayoutType().equalsIgnoreCase("episode")))*/) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                bundle.putString(Constants.SHOW_ID, catalogListItem.getShowThemeId());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, true);
                bundle.putString(Constants.SHOW_ID, catalogListItem.getShowThemeId());
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                    bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if ((catalogListItem.getTheme().equalsIgnoreCase("episode") ||
                    catalogListItem.getTheme().equalsIgnoreCase("album"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                bundle.putString(Constants.SHOW_ID, catalogListItem.getShowThemeId());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, true);
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                    bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if ((catalogListItem.getTheme().equalsIgnoreCase("show") ||
                    catalogListItem.getTheme().equalsIgnoreCase("album"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.FRIENDLY_ID, catalogListItem.getFriendlyId());
                bundle.putString(Constants.SHOW_ID, catalogListItem.getShowThemeId());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());

                bundle.putBoolean(Constants.IS_EPISODE, false);
                bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                    bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, ShowDetailsPageFragment.TAG);
            } else if (catalogListItem.getTheme().equalsIgnoreCase("live")
                    || catalogListItem.getTheme().equalsIgnoreCase("linear")) {

              /*  if(catalogListItem.getPlayUrlType()!= null && catalogListItem.getPlayUrlType().equals("vidgyor")){
                    Intent i = new Intent(mActivity, LiveTVFullScreenActivity.class);
                    i.putExtra("channelName", catalogListItem.getChannelName());
                    i.putExtra("packageName", "com.otl.tarangplus");
                    mActivity.startActivity(i);
                }else {*/
                    LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                    bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                    bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                    if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                        bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                    bundle.putString(Constants.DISPLAY_TITLE, catalogListItem.getTitle());
                    bundle.putString(Constants.THEME, catalogListItem.getTheme());
                    bundle.putString(Constants.LAYOUT_TYPE_SELECTED, catalogListItem.getCatalogObject().getLayoutType());
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutType());
                    if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                        bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                    fragment.setArguments(bundle);
                    addFragmentForDetailsScreen(mActivity, fragment, LiveTvDetailsFragment.TAG);
                //}
            } else {
                MovieDetailsFragment fragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.CONTENT_ID, catalogListItem.getContentId());
                bundle.putString(Constants.CATALOG_ID, catalogListItem.getCatalogId());
                if (catalogListItem.getCatalogObject() != null && catalogListItem.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, catalogListItem.getCatalogObject().getPlanCategoryType());
                if (!TextUtils.isEmpty(catalogListItem.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, catalogListItem.getCatalogObject().getLayoutScheme());
                if (!TextUtils.isEmpty(tag) && tag.equals(ContinueWatchingFragment.TAG))
                    bundle.putString(Constants.FROM_PAGE, ContinueWatchingFragment.TAG);
                fragment.setArguments(bundle);
                addFragmentForDetailsScreen(mActivity, fragment, MovieDetailsFragment.TAG);
            }
        }
    }

    public static Fragment getFragmentByTag(Activity mActivity, String tag) {
        FragmentManager fm = ((AppCompatActivity) mActivity).getSupportFragmentManager();
        Fragment videoFrag = fm.findFragmentByTag(tag);
        return videoFrag;

    }

    public static void addWithoutBackStack(Activity mActivity, Fragment fragment, String TAG) {
        if (mActivity != null) {
            FragmentManager fragmentManager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

            Fragment curFrag = fragmentManager.findFragmentByTag(TAG);
            if (curFrag == null || !curFrag.isVisible()) {
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();
            } else {
                // fragmentTransaction.replace(R.id.container, fragment, TAG);
                //fragmentTransaction.commit();
            }
        }
    }

    public static void addFragmentForDetailsScreen(Activity mActivity, Fragment fragment, String tag) {

        Fragment fragment2 = getFragmentByTag(mActivity, tag);
        if (fragment2 != null && fragment2.isVisible()) {
            FragmentTransaction transaction = ((AppCompatActivity) mActivity).getSupportFragmentManager().beginTransaction();
            if (fragment2 instanceof ShowDetailsPageFragment) {
                InstaPlayView in = ((ShowDetailsPageFragment) fragment2).getPlayerInfo();
                if (in != null) {
                    try {
                        in.stop();
                        in.release();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } else if (fragment2 instanceof MovieDetailsFragment) {
                InstaPlayView in = ((MovieDetailsFragment) fragment2).getPlayerInfo();
                if (in != null) {
                    try {
                        in.stop();
                        in.release();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (fragment2 instanceof LiveTvDetailsFragment) {
                InstaPlayView in = ((LiveTvDetailsFragment) fragment2).getPlayerInfo();
                if (in != null) {
                    try {
                        in.stop();
                        in.release();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            transaction.remove(fragment2).commitAllowingStateLoss();
        }

        FragmentTransaction transaction = ((AppCompatActivity) mActivity).getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.add(R.id.container, fragment, tag);
        transaction.addToBackStack(null);
//        transaction.commit();
        transaction.commitAllowingStateLoss();

    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    public static void clearLightStatusBar(Activity mActivity) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = mActivity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; // use XOR here for remove LIGHT_STATUS_BAR from flags
            mActivity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity != null) {
                View view = mActivity.getWindow().getDecorView();
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                mActivity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setLightStatusBar(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity != null) {
                View view = mActivity.getWindow().getDecorView();
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                mActivity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setLightStatusBarNonim(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity != null) {
                View view = mActivity.getWindow().getDecorView();
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                mActivity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setStatusBarColor(Activity mActivity) {
        Fragment currentFragment = getCurrentFragment(mActivity);
        if (currentFragment != null) {

            if (currentFragment instanceof HomePageFragment || currentFragment instanceof SearchFragment
                    || currentFragment instanceof MePageFragment) {
                setLightStatusBar(mActivity);
            } else {
                clearLightStatusBar(mActivity);
            }
        }
    }

    // Helper.getInstance(MainActivity.this).setLightStatusBar();
    //Helper.clearLightStatusBar(getActivity());

    public static String getPlayBackURL(String countryCode, boolean isPaid, SmartUrlResponseV2 smartObj) {
        List<SmartUrlResponseV2.AdaptiveUrls> adptive = smartObj.getAdaptive_urls();
        SmartUrlResponseV2.AdaptiveUrls adaptiveTest = new SmartUrlResponseV2.AdaptiveUrls();
        adaptiveTest.setProtocol("hls");
        if (countryCode.equalsIgnoreCase(Constants.INDIA) && !isPaid) {
            adaptiveTest.setLabel("mobile_free_in_logo");
        } else if (countryCode.equalsIgnoreCase(Constants.INDIA) && isPaid) {
            adaptiveTest.setLabel("mobile_paid_in_logo");
        } else if (countryCode.equalsIgnoreCase(Constants.USA) && !isPaid) {
            adaptiveTest.setLabel("mobile_free_us_logo");
        } else if (countryCode.equalsIgnoreCase(Constants.USA) && isPaid) {
            adaptiveTest.setLabel("mobile_paid_us_logo");
        }

        SmartUrlResponseV2.AdaptiveUrls adaptiveTestFailed = new SmartUrlResponseV2.AdaptiveUrls();
        if (countryCode.equalsIgnoreCase(Constants.INDIA) && !isPaid) {
            adaptiveTestFailed.setLabel("mobile_free_in");
        } else if (countryCode.equalsIgnoreCase(Constants.INDIA) && isPaid) {
            adaptiveTestFailed.setLabel("mobile_paid_in");
        } else if (countryCode.equalsIgnoreCase(Constants.USA) && !isPaid) {
            adaptiveTestFailed.setLabel("mobile_free_us");
        } else if (countryCode.equalsIgnoreCase(Constants.USA) && isPaid) {
            adaptiveTestFailed.setLabel("mobile_paid_us");
        }

        String play = containsLable(adptive, adaptiveTest.getLabel());
        if (TextUtils.isEmpty(play)) {
            play = containsLable(adptive, adaptiveTestFailed.getLabel());
        }

        if (TextUtils.isEmpty(play)) {
            try {
                play = smartObj.getAdaptive_urls().get(0).getPlayback_url();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return play;
    }

    public static String containsLable(Collection<SmartUrlResponseV2.AdaptiveUrls> c, String location) {
        for (SmartUrlResponseV2.AdaptiveUrls o : c) {
            if (o != null && o.getLabel() != null && o.getLabel().equalsIgnoreCase(location)) {
                return o.getPlayback_url();
            }
        }


        return "";
    }

    public static Map<String, Integer> get16By9Height(Activity activity, int dimension) {
        Map<String, Integer> map = new HashMap<>();
        if (activity != null) {
            int width = Constants.getDeviceWidth(activity) - dimension;
            map.put("WIDTH", width);
            int heightnew = (width * 9) / 16;
            map.put("HEIGHT", heightnew);
            return map;
        } else {
            map.put("HEIGHT", (int) (activity.getResources().getDimension(R.dimen.px_200)));
            return map;
        }

    }

    public static Map<String, Integer> get2By3Width(Activity activity, int padding) {
        Map<String, Integer> map = new HashMap<>();
        if (activity != null) {
            int width = Constants.getDeviceWidth(activity) -
                    /*((int) (activity.getResources().getDimension(R.dimen.px_1)))*/padding;
            int newWidth = (width / 3);
            map.put("WIDTH", newWidth);
            int heightnew = (width / 3);
            map.put("HEIGHT", heightnew);
            return map;
        } else {
            map.put("HEIGHT", (int) (activity.getResources().getDimension(R.dimen.px_200)));
            return map;
        }

    }

    public static void reportHeartBeat(Context mContext, ApiService mApiService, String contentID, String catalogID, long millis) {

        if (mContext == null)
            return;

        String sessionID = PreferenceHandler.getSessionId(mContext);

        if (TextUtils.isEmpty(sessionID) || TextUtils.isEmpty(contentID) || TextUtils.isEmpty(catalogID) || millis < 0)
            return;

        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

//        Log.e("HEARTBEAT", " ::::::::::::: " + hms);

        HeartBeatRequest request = new HeartBeatRequest();
        request.setAuthToken(Constants.API_KEY);

        HeartBeatData data = new HeartBeatData();
        data.setCatalogID(catalogID);
        data.setContentID(contentID);
        data.setPlaybackTime(hms);

        request.setHeartBeatData(data);

        mApiService.reportHeartBeat(sessionID, request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Log.e("something", "something");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public static void addWithoutBackStackAndKeepHome(Activity mActivity, Fragment fragment, String tag, int pageCount) {
        FragmentTransaction transaction = ((AppCompatActivity) mActivity).getSupportFragmentManager().beginTransaction();

        FragmentManager fm = ((AppCompatActivity) mActivity).getSupportFragmentManager();
        int counttillhome = 0;
        if (fm != null) {
            counttillhome = fm.getBackStackEntryCount() - pageCount;
        }
        for (int i = 0; i < counttillhome; ++i) {
            fm.popBackStack();
        }

        if (!fragment.isAdded()) {
            transaction.add(R.id.container, fragment, tag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } else {
            Fragment fragment1 = ((AppCompatActivity) mActivity).getSupportFragmentManager().findFragmentByTag(tag);
            transaction.replace(R.id.container, fragment1, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public static void clearLoginDetails(Activity activity) {
        String faq = PreferenceHandler.getFAQURL(MyApplication.getInstance());
        String help = PreferenceHandler.getHelpURL(MyApplication.getInstance());
        String privacyPolicy = PreferenceHandler.getPrivacyPolicy(MyApplication.getInstance());
        String termsAndConditions = PreferenceHandler.getTermsAndCondition(MyApplication.getInstance());
        String contactUs = PreferenceHandler.getContactUs(MyApplication.getInstance());
        String mediaActiveXMin = PreferenceHandler.getMediaActiveInterval(MyApplication.getInstance());
        String region = PreferenceHandler.getRegion(MyApplication.getInstance());

        PreferenceHandler.clearAll(activity);
        PreferenceHandler.setFacebook(MyApplication.getInstance(), "");
        PreferenceHandler.setFAQUrl(MyApplication.getInstance(), faq);
        PreferenceHandler.setHelpURL(MyApplication.getInstance(), help);
        PreferenceHandler.setPrivacyUrl(MyApplication.getInstance(), privacyPolicy);
        PreferenceHandler.setTermsAndConditionUrl(MyApplication.getInstance(), termsAndConditions);
        PreferenceHandler.setContactUsUrl(MyApplication.getInstance(), contactUs);
        PreferenceHandler.setMediaActiveInterval(mediaActiveXMin, MyApplication.getInstance());
        PreferenceHandler.setRegion(activity, region);


        Constants.CUR_SESSION_ID = "";
        Constants.SESSION_ID = "";
    }

    private static Executor executor = Executors.newSingleThreadExecutor();

    public static void deleteSearchHistory(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.recentSearchDao().deleteAllSearchHistory();
            }
        });
    }

    public static void resetToSensorOrientation(Activity activity) {
        Timer mRestoreOrientation = new Timer();
        mRestoreOrientation.schedule(new TimerTask() {
            @Override
            public void run() {
                Objects.requireNonNull(activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        }, 2000);
    }

    public static void resetToSensorOrientationWithoutDelay(Activity activity) {
        Objects.requireNonNull(activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }


    public static void setToPortAndResetToSensorOrientation(Activity activity) {
        Objects.requireNonNull(activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static boolean isHighResulutionDevice() {
        /*DisplayMetrics metrics = new DisplayMetrics();
        if (activity != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    return false;

                case DisplayMetrics.DENSITY_MEDIUM:
                    return false;

                case DisplayMetrics.DENSITY_HIGH:
                    return false;
            }
        }*/
//        if (activity != null)
//            return activity.getResources().getBoolean(R.bool.is_tablet);

        return false;
    }

    public static boolean isXHdPiResulutionDevice() {
        DisplayMetrics metrics = new DisplayMetrics();
        if (activity != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    return false;

                case DisplayMetrics.DENSITY_MEDIUM:
                    return false;

                case DisplayMetrics.DENSITY_HIGH:
                    return true;
            }
        }
        return false;
    }

    public static boolean isTablet() {
        if (activity != null)
            return activity.getResources().getBoolean(R.bool.is_tablet);
        return false;
    }

    public static void updateCleverTapDetails(Context mContext) {
        if (mContext != null) {
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            // String
            if (PreferenceHandler.isLoggedIn(mContext)) {
                profileUpdate.put(Constants.CleverTapParams.IDENTITY, PreferenceHandler.getAnalyticsUserId(mContext));                    // String or number   get User ID
                String activePlans = PreferenceHandler.getActivePlans();
                String inActivePlans = PreferenceHandler.getInActivePlans();
                if (!TextUtils.isEmpty(activePlans)) {
                    String[] split = activePlans.split(",");
//                    profileUpdate.put(Constants.CleverTapParams.ACTIVE_PLANS, split);
                    profileUpdate.put(Constants.CleverTapParams.ACTIVE_PLANS, activePlans);
                }

                if (!TextUtils.isEmpty(inActivePlans)) {
                    String[] split = inActivePlans.split(",");
//                    profileUpdate.put(Constants.CleverTapParams.INACTIVE_PLANS, split);
                    profileUpdate.put(Constants.CleverTapParams.INACTIVE_PLANS, inActivePlans);
                }

                if (PreferenceHandler.getIsSubscribed(mContext))
                    profileUpdate.put(Constants.CleverTapParams.IS_SUBSCRIBED, 1);
                else
                    profileUpdate.put(Constants.CleverTapParams.IS_SUBSCRIBED, 0);

                try {
                    String userPeriod = PreferenceHandler.getUserPeriod(mContext);
                    profileUpdate.put(Constants.CleverTapParams.USER_PERIOD, userPeriod);
                } catch (Exception e) {
                    //some time data is empty so need this.
                }


                profileUpdate.put(Constants.CleverTapParams.IS_LOGGEDIN, 1);

                profileUpdate.put(Constants.CleverTapParams.COUNTRY_NAME, Constants.REGION);
                profileUpdate.put(Constants.CleverTapParams.STATE_NAME, Constants.STATE);
                profileUpdate.put(Constants.CleverTapParams.CITY_NAME, Constants.CITY);
                profileUpdate.put(Constants.CleverTapParams.APP_VERSION, BuildConfig.VERSION_NAME);

                if (PreferenceHandler.getUserEmailID(mContext) != null && PreferenceHandler.getUserEmailID(mContext).length() > 0)
                    profileUpdate.put(Constants.CleverTapParams.EMAIL_ID, PreferenceHandler.getUserEmailID(mContext));

                if (PreferenceHandler.getUserId(mContext) != null && PreferenceHandler.getUserId(mContext).length() > 0)
                    profileUpdate.put(Constants.CleverTapParams.MOBILE_NO, PreferenceHandler.getUserId(mContext));

                if (PreferenceHandler.getUserName((MyApplication.getInstance())) != null)
                    profileUpdate.put(Constants.CleverTapParams.FIRST_NAME, PreferenceHandler.getUserName((MyApplication.getInstance())));

                if (PreferenceHandler.getUserPlanType((MyApplication.getInstance())) != null)
                    profileUpdate.put(Constants.CleverTapParams.USER_PLAN_TYPE, PreferenceHandler.getUserPlanType((MyApplication.getInstance())));


            }


            AnalyticsProvider.sendProfileDataToCleverTap(profileUpdate, mContext);
        }
    }


    public static void checkAndLogout(Activity context, ApiService mApiService) {
        Helper.showProgressDialog(context);
        SignOutDetails outDetails = new SignOutDetails();
        outDetails.setAuthToken(Constants.API_KEY);
        String sessionId = PreferenceHandler.getSessionId(context);

        mApiService.signOut(sessionId, outDetails)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        if (object != null) {
                            Helper.updateCleverTapDetails(context);
                            JsonElement element = object.get("message");
                            String msg = element.getAsString();
//                            Helper.showToast(context, msg, R.drawable.ic_check);
                            Helper.clearLoginDetails(context);
                            try {
                                LoginManager.getInstance().logOut();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            AnalyticsProvider provider = new AnalyticsProvider(context);
                            String analyticsUserId = PreferenceHandler.getAnalyticsUserId(context);
                            provider.resetUserId(analyticsUserId, context);
                            // TODO: 22/01/19
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                        Helper.clearLoginDetails(context);

                    }
                });
    }

    public static Map<String, String> getReferalThingsData(String referalLink) {

        try {
            Map<String, String> map = new HashMap<String, String>();
            String[] splits = referalLink.split("&");
            String[] keyValuePair = null;

            for (int i = 0; i < splits.length; i++) {
                keyValuePair = splits[i].split("=");
                for (int j = 0; j < keyValuePair.length; j += 2) {
                    map.put(keyValuePair[j], keyValuePair[j + 1]);
                }
            }

            return map;
        } catch (Exception e) {

            return null;
        }

    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static String getAndroidVersion() {
        String versionName = "";

        try {
            versionName = String.valueOf(Build.VERSION.RELEASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Android ver : " + versionName;
    }

    public static String getAndroidDeviceId(Context activity) {
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(activity.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return androidId;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "androidfailed";
        }
    }

    public static String getDate() {
        Date mDate = new Date();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        return dateFormat.format(mDate);
    }

    public static String getTime() {
        Date mDate = new Date();
        DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
        return timeFormat.format(mDate);
    }


    public List<PlayListItem> handleCustomLists(List<PlayList> playLists) {
        List<PlayListItem> listForItem = new ArrayList<>();
        if (playLists != null && Constants.PLAYLISTITEMS != null) {
            boolean status = false;
            List<PlayListItem> temUniversallist = new ArrayList<>();
            temUniversallist = Helper.doDeepCopy(Constants.PLAYLISTITEMS);
            for (PlayListItem playListitem : temUniversallist) {
                for (PlayList playList : playLists) {
                    if (playList != null && playListitem != null) {
                        if (playList.getName().equalsIgnoreCase(playListitem.getName())) {
                            status = true;
                            break;
                        } else {
                            status = false;
                        }
                    }
                }
                if (status) {
                    playListitem.setPreviouslySelected(true);
                    listForItem.add(playListitem);
                } else {
                    playListitem.setPreviouslySelected(false);
                    listForItem.add(playListitem);
                }
            }
        }
        return listForItem;
    }


    public static List<PlayListItem> doDeepCopy(List<PlayListItem> playListItemsGlobal) {
        if (playListItemsGlobal == null) {
            return new ArrayList<PlayListItem>();
        }
        List<PlayListItem> playListItemsTemp = new ArrayList<PlayListItem>();
        for (PlayListItem playListItem : playListItemsGlobal) {

            if (playListItem != null) {

                PlayListItem playListItem1 = new PlayListItem();
                playListItem1.setName(playListItem.getName());
                playListItem1.setType(playListItem.getType());
                playListItem1.setPlaylistId(playListItem.getPlaylistId());
                playListItem1.setSelected(playListItem.isSelected());
                playListItem1.setPreviouslySelected(playListItem.isPreviouslySelected());
                playListItemsTemp.add(playListItem1);
            }

        }
        return playListItemsTemp;
    }

    public static void detectedNetwork(Context c) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) c.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isWifiConn = false;
            boolean isMobileConn = false;
            for (Network network : connMgr.getAllNetworks()) {
                NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn |= networkInfo.isConnected();

                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn |= networkInfo.isConnected();
                }
            }
            Log.d("connection in wifi", "Wifi connected: " + isWifiConn);
            Log.d("connection in mobile", "Mobile connected: " + isMobileConn);

            if (isWifiConn) {
                PreferenceHandler.setNetworkType(c, "wifi");
            } else {
                PreferenceHandler.setNetworkType(c, "mobile_data");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
