package com.otl.tarangplus.fragments;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.viewModel.SettingsViewModel;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.PreferenceHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HelpFragment extends Fragment {

    public static String TAG = HelpFragment.class.getSimpleName();
    @BindView(R.id.category_back_img)
    ImageView mCategoryBackImg;
    @BindView(R.id.category_grad_back)
    TextView mCategoryGradBack;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.header)
    MyTextView mHeader;
    @BindView(R.id.close)
    AppCompatImageView mClose;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    Unbinder unbinder;

    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.progress)
    ProgressBar progressBar;


    private SettingsViewModel settingsViewModel;
    private View mRootView;


    public HelpFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_help, container, false);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeader.setText(R.string.help);
        mClose.setVisibility(View.GONE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else if (getActivity().getFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        mClose.setVisibility(View.GONE);
        //String url = "http://staging.shemaroome.com/faq?type=mobile";
        String url = PreferenceHandler.getHelpURL(MyApplication.getInstance());

        mWebview.clearCache(true);
        mWebview.clearHistory();
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });

        mWebview.addJavascriptInterface(new WebViewJavaScriptInterface(getActivity()), "Android");
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar != null) {

                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {


            }
        });

        mWebview.loadUrl(url);
    }

    public class WebViewJavaScriptInterface {

        private Context context;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context) {
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void success(String message) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
