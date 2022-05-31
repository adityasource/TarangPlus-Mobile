package com.otl.tarangplus.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.PreferenceHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsOfUserFragment extends Fragment {


    public static final String TAG = TermsOfUserFragment.class.getSimpleName();
    @BindView(R.id.category_back_img)
    ImageView category_back_img;
    @BindView(R.id.category_grad_back)
    TextView category_grad_back;
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
    WebView mWebview;
    @BindView(R.id.parent_view)
    LinearLayout parent_view;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.general_web_view, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String type = arguments.getString(Constants.TYPE);
            if (!TextUtils.isEmpty(type)) {
                String url = "";
                if (type.equalsIgnoreCase(Constants.Type.PRIVACY_POLICY)) {
                    url = PreferenceHandler.getPrivacyPolicy(MyApplication.getInstance());
                    header.setText(R.string.privacy_me_policy);
                } else if (type.equalsIgnoreCase(Constants.Type.TERMES_OF_USE)) {
                    //url = "http://staging.shemaroome.com/terms-and-conditions?type=mobile";
                    url = PreferenceHandler.getTermsAndCondition(MyApplication.getInstance());
                    header.setText(R.string.terms_me_text);
                } else if (type.equalsIgnoreCase(Constants.Type.CONTACT_US)) {
                    //url = "http://staging.shemaroome.com/terms-and-conditions?type=mobile";
                    url = PreferenceHandler.getContactUs(MyApplication.getInstance());
                    //header.setText(R.string.contact_me_us);
                }else if (type.equalsIgnoreCase(Constants.Type.DEEP_LINK_URL)) {
                    String title = arguments.getString(Constants.DISPLAY_TITLE);
                    header.setText(title);
                    url = arguments.getString(Constants.DEEP_LINK_URL);
                }

                mWebview.clearCache(true);
                mWebview.clearHistory();
                mWebview.getSettings().setJavaScriptEnabled(true);
                mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            mWebview.loadUrl("file:///android_asset/terms.html");

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

                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                        Toast.makeText(getActivity(), "Error:" + description, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

                mWebview.loadUrl(url);
            }
        }
        close.setVisibility(View.GONE);
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

    @OnClick(R.id.back)
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back) {
            getActivity().onBackPressed();
        }
    }
}
