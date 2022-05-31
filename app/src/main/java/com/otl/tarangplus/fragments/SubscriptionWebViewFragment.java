package com.otl.tarangplus.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.dialogs.FreePackDialog;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.model.AppEvents;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionWebViewFragment extends Fragment {
    public static final String TAG = SubscriptionWebViewFragment.class.getSimpleName();
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;
    @BindView(R.id.webview)
    WebView webview;
    private String wherefrom = "";
    private boolean isLoggedin;
    boolean isFromDetailPage = false;
    Activity context;
    String type = "";
    String extraLink = "";

    public SubscriptionWebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.subscription_web_fragment, container, false);
        ButterKnife.bind(this, inflate);
        mAnalyticsEvent = Analytics.getInstance(getContext());
        header.setText(getString(R.string.subscription_header));
        close.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        context = getActivity();
        webview.setWebChromeClient(new WebChromeClient());
        MyWebViewClient my = new MyWebViewClient();
        webview.setWebViewClient(my);
        webview.clearCache(true);
        webview.clearHistory();
        clearCookies(getActivity());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
        CookieManager.getInstance().setAcceptCookie(true);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        mAnalyticsEvent = Analytics.getInstance(getContext());
        appEvents = new AppEvents(1, "", "View Plans", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "View Plans",
                PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()),
                PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);

        //GetAruments
        wherefrom = getArguments().getString(Constants.FROM_WHERE);
        isLoggedin = getArguments().getBoolean(Constants.IS_LOGGED_IN);
        isFromDetailPage = getArguments().getBoolean(Constants.IS_FROM_DETAIL_PAGE, false);

        if (mOnEventListener != null) {
            mOnEventListener.onEvent(isFromDetailPage);
        }


        try {
            if (getArguments() != null)
                extraLink = getArguments().getString(Constants.EXTRA_LINK);

            if(extraLink == null)
                extraLink = "";

            type = getArguments().getString(Constants.TYPE);
        } catch (Exception e) {

        }

        refreshAfterLogin();
        return inflate;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mOnEventListener != null) {
            mOnEventListener.onEvent(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        public MyWebViewClient() {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Helper.showProgressDialog(getActivity());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            Helper.dismissProgressDialog();
            Helper.dismissProgressDialog2();
            try {
                if (PreferenceHandler.isLoggedIn(getActivity()) && url.equalsIgnoreCase(Constants.PAYMENTURL + "?user_id=" + PreferenceHandler.getSessionId(getActivity()) + "&user_login_id="
                        + PreferenceHandler.getUserId(getActivity()) + "&user_region=" + Constants.REGION + "&browser=" + "android" +"&user_analytic_id="+ PreferenceHandler.getAnalyticsUserId(getActivity()))) {
                    webview.clearHistory();
                    webview.clearCache(true);
                } else if (url.equalsIgnoreCase(Constants.PAYMENTURL + "?browser=android&user_login_status=false")) {
                    webview.clearHistory();
                    webview.clearCache(true);
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean myOnKeyDown(int keyCode, boolean returns) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return false;
        }
        return returns;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void success(String toast) {
            Log.d("Payment", "success");
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Helper.dismissProgressDialog();
                        if (toast != null && toast.equalsIgnoreCase("welcome")) {
                            Bundle arguments = getArguments();
                            if (arguments != null &&
                                    arguments.getString(Constants.WHO_INITIATED_THIS) != null &&
                                    arguments.getString(Constants.WHO_INITIATED_THIS).equalsIgnoreCase(OtpScreen.TAG)) {
                                if (PreferenceHandler.getIsFreeTrail(getActivity())) {
                                    FreePackDialog freePackDialog = new FreePackDialog();
                                    Helper.addFragment(getActivity(), freePackDialog, freePackDialog.TAG);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.TYPE, type);
                                    WelcomeDialogFragment welcomeDialogFragment = new WelcomeDialogFragment();
                                    welcomeDialogFragment.setArguments(bundle);
                                    Helper.addFragment(getActivity(), welcomeDialogFragment, welcomeDialogFragment.TAG);
                                }

                            }
                        } else if (wherefrom.equals(MePageFragment.TAG)) {
                            Helper.showToast(getActivity(), toast, R.drawable.ic_check);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            checkIfDetailPageisThere();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            manager.popBackStackImmediate();
                        }
                    }

                });
            }
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void failed(String toast) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Helper.dismissProgressDialog();
                        if (wherefrom.equals(MePageFragment.TAG)) {
                            Helper.showToast(getActivity(), toast, R.drawable.ic_error_icon);

//                            Helper.addWithoutBackStack(getActivity(), SubscriptionWebViewFragment.this, HomePageFragment.TAG);
//                            Handler handler2 = new Handler();
//                            handler2.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (getActivity() != null)
//                                        ((MainActivity) getActivity()).changeBottomTab();
//
//                                }
//                            }, 300);

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            checkIfDetailPageisThere();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            manager.popBackStackImmediate();
                        }
                    }
                });
            }
        }

        @JavascriptInterface
        public void doLoginOnAndroid(String toast) {
            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialog();
                    }
                });


        }


        public void showDialog() {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(R.layout.login_to_proceed);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            final GradientTextView proceed = dialog.getWindow().findViewById(R.id.popup_positive_button);
            final GradientTextView cancel = dialog.getWindow().findViewById(R.id.popup_negetive_button);

            cancel.setOnClickListener(view -> dialog.dismiss());

            proceed.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), OnBoardingActivity.class);
                if (!TextUtils.isEmpty(wherefrom))
                    intent.putExtra(Constants.FROM_PAGE, wherefrom);
                intent.putExtra(Constants.CARRYING_PAGE, SubscriptionWebViewFragment.TAG);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent,
                        Constants.ACTION_ME_LOGIN);
                dialog.dismiss();
            });
        }

        private void checkIfDetailPageisThere() {

            Fragment fragmentM = Helper.getFragmentByTag(getActivity(), MovieDetailsFragment.TAG);
            Fragment fragmentS = Helper.getFragmentByTag(getActivity(), ShowDetailsPageFragment.TAG);
            Fragment fragmentL = Helper.getFragmentByTag(getActivity(), LiveTvDetailsFragment.TAG);

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

    }

    @OnClick(R.id.back)
    public void backPress() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            Bundle arguments = getArguments();
            if (arguments != null &&
                    arguments.getString(Constants.WHO_INITIATED_THIS) != null &&
                    arguments.getString(Constants.WHO_INITIATED_THIS).equalsIgnoreCase(RegisterFragment.TAG)) {
                Helper.dismissProgressDialog();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (arguments != null &&
                    arguments.getString(Constants.WHO_INITIATED_THIS) != null &&
                    arguments.getString(Constants.WHO_INITIATED_THIS).equalsIgnoreCase(OtpScreen.TAG)) {
                if (PreferenceHandler.getIsFreeTrail(getActivity())) {
                    FreePackDialog freePackDialog = new FreePackDialog();
                    Helper.addFragment(getActivity(), freePackDialog, freePackDialog.TAG);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.TYPE, type);
                    WelcomeDialogFragment welcomeDialogFragment = new WelcomeDialogFragment();
                    welcomeDialogFragment.setArguments(bundle);
                    Helper.addFragment(getActivity(), welcomeDialogFragment, welcomeDialogFragment.TAG);
                }

            } else {
                getActivity().onBackPressed();
                Helper.dismissProgressDialog();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.ViewPlans);
    }

    public interface OnEventListener {
        void onEvent(boolean b);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Helper.dismissProgressDialog();
        Helper.dismissProgressDialog2();
    }

    private static OnEventListener mOnEventListener;

    public static void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    public void refreshAfterLogin(){
        if (!Constants.PAYMENTURL.equals("")) {

            isLoggedin = PreferenceHandler.isLoggedIn(getActivity());
            Helper.showProgressDialog(getActivity());
            Log.d("URLSINFORMATION ",PreferenceHandler.getSessionId(getActivity()) + "&user_login_id="
                    + PreferenceHandler.getUserId(getActivity()));
            if (isLoggedin && !PreferenceHandler.getSessionId(getActivity()).equals("b16e4bf2afd8d4ab472adbb48ef1a2d8") &&
                    !PreferenceHandler.getUserId(getActivity()).equals("") && PreferenceHandler.getAnalyticsUserId(getActivity()) != null) {
//                Log.d(TAG, "onCreateView: " + "Login");
                webview.loadUrl(Constants.PAYMENTURL + "?user_id=" + PreferenceHandler.getSessionId(getActivity()) + "&user_login_id="
                        + PreferenceHandler.getUserId(getActivity()) + "&user_region=" + Constants.REGION + "&browser=" + "android"
                        + extraLink +"&user_analytic_id="+ PreferenceHandler.getAnalyticsUserId(getActivity()));
            } else {
//                Log.d(TAG, "onCreateView: " + "Not Login");
                webview.loadUrl(Constants.PAYMENTURL + "?browser=android&user_login_status=false");
            }
            CookieManager.getInstance().setAcceptCookie(true);


        } else {
            Toast.makeText(getActivity(), "Something went wrong, please re-login", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshAfterLogin2(){
        if (!Constants.PAYMENTURL.equals("")) {

            isLoggedin = PreferenceHandler.isLoggedIn(getActivity());
            Helper.showProgressDialog2(getActivity());
            if (isLoggedin && !PreferenceHandler.getSessionId(getActivity()).equals("b16e4bf2afd8d4ab472adbb48ef1a2d8") &&
                    !PreferenceHandler.getUserId(getActivity()).equals("") && PreferenceHandler.getAnalyticsUserId(getActivity()) != null) {
//                Log.d(TAG, "onCreateView: " + "Login");
                webview.loadUrl(Constants.PAYMENTURL + "?user_id=" + PreferenceHandler.getSessionId(getActivity()) + "&user_login_id="
                        + PreferenceHandler.getUserId(getActivity()) + "&user_region=" + Constants.REGION + "&browser=" + "android" + extraLink +"&user_analytic_id="+ PreferenceHandler.getAnalyticsUserId(getActivity()));
            } else {
//                Log.d(TAG, "onCreateView: " + "Not Login");
                webview.loadUrl(Constants.PAYMENTURL + "?browser=android&user_login_status=false");
            }
            CookieManager.getInstance().setAcceptCookie(true);


        } else {
            Toast.makeText(getActivity(), "Something went wrong, please re-login", Toast.LENGTH_SHORT).show();
        }
    }
}
