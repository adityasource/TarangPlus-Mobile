package com.otl.tarangplus.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.adapters.HomePagerAdapter;
import com.otl.tarangplus.adapters.KidsAdapter;
import com.otl.tarangplus.customeUI.ContentWrappingViewPager;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CampaignData;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.CategoryEvents;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.HomeScreenResponse;
import com.otl.tarangplus.model.LoggedInData;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.PlayListDataResponse;
import com.otl.tarangplus.model.ShareData;
import com.otl.tarangplus.model.ShareRecivedData;
import com.otl.tarangplus.model.ShareRelatedData;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by VPotadar on 30/08/18.
 */


public class HomePageFragment extends UniversalFragment {


    private boolean JUSTCHANGES;
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;

    @Override
    protected void languageChanged() {
        JUSTCHANGES = true;
        configureTabDetails();
    }

    public static final String TAG = HomePageFragment.class.getName();
    public HomePagerAdapter mHomePagerAdapter;
    public KidsAdapter mKidsAdapter;

    @BindView(R.id.pager)
    ContentWrappingViewPager mPager;
    @BindView(R.id.tab)
    TabLayout mTabView;
    @BindView(R.id.image_id)
    ImageView mImageBackGround;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mCollapsToolBar;
    @BindView(R.id.nestedScroll)
    NestedScrollView scrollView;

    private ApiService mApiService;

    int[] colors = new int[2];
    private AnalyticsProvider mAnalytics;
    boolean isFromBranchIO;
    private String mDeepLinkUrl;
    WebEngageAnalytics webEngageAnalytics;
    PageEvents pageEvents;



    public HomePageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this, mRootView);
        mAnalytics = new AnalyticsProvider(getContext());
        mTabView.setupWithViewPager(mPager);
        webEngageAnalytics = new WebEngageAnalytics();
        mAnalyticsEvent = Analytics.getInstance(getContext());

        Helper.detectedNetwork(getActivity());
        mTabView.setTabRippleColor(null);
//        configureTabDetails();
        mApiService = new RestClient(getActivity()).getApiService();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setSelectedNavUI(Constants.TABS.HOME);
        setListners();
        configureTabDetails();
        checkForDeepLinkingStatus();
        fireScreenView();
        checkPlayList();

    }

    private void checkPlayList() {

        if (PreferenceHandler.isLoggedIn(getActivity())) {

            mApiService.getAllPlaylist(PreferenceHandler.getSessionId(getActivity()), 0, 20).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PlayListDataResponse>() {
                @Override
                public void call(PlayListDataResponse playListResponse) {

                    Log.d(TAG, "checkPlayList : " + "");
                    Constants.addToLocalList(playListResponse.getPlayListResponse().getPlaylistItems());
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    //show toast
                    Log.e("ERROR IN PLAYLIST", "YES");
                }
            });

        }

    }

    public void checkForDeepLinkingStatus() {
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                /*todo added for branch*/
               /* if (data.getHost() != null &&
                        data.getHost().equalsIgnoreCase("shemaroome.app.link"))
                    return;*/

                String fullUrl = data.toString();
                mDeepLinkUrl = fullUrl;
                if (!TextUtils.isEmpty(fullUrl.trim())) {
                    Uri myUri = Uri.parse(fullUrl);
                    URL url = null;
                    try {
                        url = new URL(fullUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }


                    if (url != null && url.getPath() != null) {
                        if (url.getPath().contains("signup") || url.getPath().contains("register")) {
                            Intent regIntent = new Intent(getActivity(), OnBoardingActivity.class);
                            regIntent.putExtra("requestCode", Constants.ACTION_SIGNUP_CLICKED);
                            regIntent.putExtra(Constants.FROM_PAGE, HomePageFragment.TAG);
                            startActivityForResult(regIntent, Constants.ACTION_SIGNUP_CLICKED);
                            return;
                        }
                    }


                    ShareRelatedData shareRelatedData = new ShareRelatedData();
                    shareRelatedData.setAuthToken(Constants.API_KEY);
                    ShareData shareData = new ShareData();
                    if (url != null && url.getPath() != null)
                        shareData.setContentTypeUrl(url.getPath());
                    shareRelatedData.setData(shareData);
                    mApiService.getDetailsFromShareUrl(shareRelatedData).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(shareRecivedData -> {

                      /*  if (isFromBranchIO) {
                            isFromBranchIO = false;
                            return;
                        }*/

//                        Log.d("shareData",shareData.toString());
                        if (!PreferenceHandler.isKidsProfileActive(getActivity()))
                            moveToCorrespondingPage(shareRecivedData);
                    }, throwable -> {
                        Log.e("shareData", throwable.getMessage());
                    });
                    //contentType=video&contentId=538
                }
            }
            Bundle bundle = intent.getBundleExtra(Constants.FROM_NOTIFICATION);
            if (bundle != null) {
                if (!PreferenceHandler.isKidsProfileActive(getActivity()))
                    moveToCorrespondingPage(bundle);
            }
        }
    }

    public void checkForDeepLinkingStatus(String fullUrl) {

        if (!TextUtils.isEmpty(fullUrl.trim())) {
            Uri myUri = Uri.parse(fullUrl);
            URL url = null;
            try {
                url = new URL(fullUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            if (url != null && url.getPath() != null) {
                if (url.getPath().contains("signup") || url.getPath().contains("register")) {
                    Intent regIntent = new Intent(getActivity(), OnBoardingActivity.class);
                    regIntent.putExtra("requestCode", Constants.ACTION_SIGNUP_CLICKED);
                    regIntent.putExtra(Constants.FROM_PAGE, HomePageFragment.TAG);
                    startActivityForResult(regIntent, Constants.ACTION_SIGNUP_CLICKED);
                    return;
                }
            }


            ShareRelatedData shareRelatedData = new ShareRelatedData();
            shareRelatedData.setAuthToken(Constants.API_KEY);
            ShareData shareData = new ShareData();
            if (url != null && url.getPath() != null)
                shareData.setContentTypeUrl(url.getPath());
            shareRelatedData.setData(shareData);
            mApiService.getDetailsFromShareUrl(shareRelatedData).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(shareRecivedData -> {
//                        Log.d("shareData",shareData.toString());
                if (!PreferenceHandler.isKidsProfileActive(getActivity()))
                    moveToCorrespondingPage(shareRecivedData);
            }, throwable -> {
                Log.e("shareData", throwable.getMessage());
            });
            //contentType=video&contentId=538
        }
    }


    private void moveToCorrespondingPage(Bundle notificationBundle) {

        String contentId = notificationBundle.getString("contentid");
        String catalogId = notificationBundle.getString("catalogid");
        String theme = notificationBundle.getString("theme");
        String showId = notificationBundle.getString("showid");
        String layoutType = notificationBundle.getString("layout_type");
        String layoutScheme = notificationBundle.getString("layout_scheme");
        String planCategoryType = notificationBundle.getString("plan_category_type");

        if (theme != null && theme.equalsIgnoreCase("livetv") ||
                theme.equalsIgnoreCase("linear")) {
            LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
            bundle.putString(Constants.CATALOG_ID, catalogId);
            bundle.putString(Constants.CONTENT_ID, contentId);
            bundle.putString(Constants.THEME, "live");
            bundle.putString(Constants.LAYOUT_TYPE_SELECTED, layoutType);
            bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, LiveTvDetailsFragment.TAG);
        } else if (theme.equalsIgnoreCase("movie") || theme.equalsIgnoreCase("video")) {
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
            bundle.putString(Constants.CONTENT_ID, contentId);
            bundle.putString(Constants.CATALOG_ID, catalogId);
            bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, MovieDetailsFragment.TAG);
        } else if (theme.equalsIgnoreCase("show") ||
                theme.equalsIgnoreCase("show") ||
                theme.equalsIgnoreCase("shows")) {
            ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
            bundle.putString(Constants.CONTENT_ID, contentId);
            bundle.putString(Constants.CATALOG_ID, catalogId);
            bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
            bundle.putBoolean(Constants.IS_EPISODE, false);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
        } else if ((theme.equalsIgnoreCase("episode") ||
                theme.equalsIgnoreCase("show_episode")) &&
                ((theme.equalsIgnoreCase("show") ||
                        theme.equalsIgnoreCase("shows") ||
                        theme.equalsIgnoreCase("episode")))) {
            ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
            bundle.putString(Constants.CONTENT_ID, contentId);
            bundle.putString(Constants.CATALOG_ID, catalogId);
            bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);

            bundle.putBoolean(Constants.IS_EPISODE, true);
            bundle.putString(Constants.SHOW_ID, showId);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
        } else if ((theme.equalsIgnoreCase("episode") ||
                theme.equalsIgnoreCase("album"))) {
            ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
            bundle.putString(Constants.CONTENT_ID, contentId);
            bundle.putString(Constants.CATALOG_ID, catalogId);
            bundle.putString(Constants.SHOW_ID, showId);
            bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);

            bundle.putBoolean(Constants.IS_EPISODE, true);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
        } else if ((theme.equalsIgnoreCase("show") ||
                theme.equalsIgnoreCase("album"))) {
            ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
            bundle.putString(Constants.CONTENT_ID, contentId);
            bundle.putString(Constants.CATALOG_ID, catalogId);
            bundle.putString(Constants.SHOW_ID, showId);
            bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);

            bundle.putBoolean(Constants.IS_EPISODE, false);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);

        } else {
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
            bundle.putString(Constants.CONTENT_ID, contentId);
            bundle.putString(Constants.CATALOG_ID, catalogId);
            bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
            fragment.setArguments(bundle);
        }
    }

    private void moveToCorrespondingPage(ShareRecivedData shareRecivedData) {

        if (shareRecivedData != null && shareRecivedData.getData() != null) {
            Data data1 = shareRecivedData.getData();
            String contentId = data1.getContentId();
            String catalogId = data1.getCatalogId();
            String theme = data1.getTheme();
            String showId = data1.getShowID();
            String layoutType = data1.getLayoutType();
            String layoutScheme = data1.getLayoutScheme();
            String planCategoryType = data1.getPlanCategoryType();
            if (theme != null && theme.equalsIgnoreCase("livetv") ||
                    theme.equalsIgnoreCase("linear")) {
                LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
                bundle.putString(Constants.CATALOG_ID, catalogId);
                bundle.putString(Constants.CONTENT_ID, contentId);
                bundle.putString(Constants.THEME, "live");
                bundle.putString(Constants.LAYOUT_TYPE_SELECTED, layoutType);
                bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, LiveTvDetailsFragment.TAG);
            } else if (theme.equalsIgnoreCase("movie") || theme.equalsIgnoreCase("video")) {
                MovieDetailsFragment fragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
                bundle.putString(Constants.CONTENT_ID, contentId);
                bundle.putString(Constants.CATALOG_ID, catalogId);
                bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, MovieDetailsFragment.TAG);
            } else if (theme.equalsIgnoreCase("show") ||
                    theme.equalsIgnoreCase("show") ||
                    theme.equalsIgnoreCase("shows")) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
                bundle.putString(Constants.CONTENT_ID, contentId);
                bundle.putString(Constants.CATALOG_ID, catalogId);
                bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
                bundle.putBoolean(Constants.IS_EPISODE, false);
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
            } else if ((theme.equalsIgnoreCase("episode") ||
                    theme.equalsIgnoreCase("show_episode")) &&
                    ((theme.equalsIgnoreCase("show") ||
                            theme.equalsIgnoreCase("shows") ||
                            theme.equalsIgnoreCase("episode")))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
                bundle.putString(Constants.CONTENT_ID, contentId);
                bundle.putString(Constants.CATALOG_ID, catalogId);
                bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);

                bundle.putBoolean(Constants.IS_EPISODE, true);
                bundle.putString(Constants.SHOW_ID, showId);
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
            } else if ((theme.equalsIgnoreCase("episode") ||
                    theme.equalsIgnoreCase("album"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
                bundle.putString(Constants.CONTENT_ID, contentId);
                bundle.putString(Constants.CATALOG_ID, catalogId);
                bundle.putString(Constants.SHOW_ID, showId);
                bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);

                bundle.putBoolean(Constants.IS_EPISODE, true);
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
            } else if ((theme.equalsIgnoreCase("show") ||
                    theme.equalsIgnoreCase("album"))) {
                ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
                bundle.putString(Constants.CONTENT_ID, contentId);
                bundle.putString(Constants.CATALOG_ID, catalogId);
                bundle.putString(Constants.SHOW_ID, showId);
                bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);

                bundle.putBoolean(Constants.IS_EPISODE, false);
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);

            } else if (theme.equalsIgnoreCase("webpage")) {
                Uri uri = Uri.parse(mDeepLinkUrl);

                String confirmation_token = uri.getQueryParameter("confirmation_token");

                verifyEmailApi(confirmation_token);

            } else if (theme.equalsIgnoreCase("forgot_password")) {
                String url = data1.getUrl();
                String title = data1.getTitle();

                TermsOfUserFragment fragment = new TermsOfUserFragment();

                Bundle bundle = new Bundle();
                bundle.putString(Constants.DISPLAY_TITLE, "Forgot Password");
                bundle.putString(Constants.DEEP_LINK_URL, mDeepLinkUrl);
                bundle.putString(Constants.TYPE, Constants.Type.DEEP_LINK_URL);

                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, TermsOfUserFragment.TAG);

            } else {
                MovieDetailsFragment fragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
                bundle.putString(Constants.CONTENT_ID, contentId);
                bundle.putString(Constants.CATALOG_ID, catalogId);
                bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
                fragment.setArguments(bundle);
            }

        }

    }

    private void verifyEmailApi(String confirmation_token) {
        mApiService.verifyEmail(confirmation_token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoggedInData>() {
                    @Override
                    public void call(LoggedInData loggedInData) {
                        Helper.dismissProgressDialog();

                        Log.d(TAG, "call: " + loggedInData);
                        try {
                            String msg = loggedInData.getData().getMessages().get(0).getValue();
//                            Helper.showToast(getActivity(), getString(R.string.email_verified), R.drawable.ic_cross);
                            showVerificationConfirmationPopUp(msg);
                        } catch (Exception e) {
                            Helper.showToast(getActivity(), "Oops something went wrong please try again!", R.drawable.ic_cross);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();

                        String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                        Helper.showToast(getActivity(), errorMessage, R.drawable.ic_cross);
                        Helper.dismissProgressDialog();
//                        analyticsProvider.branchSignUpOtpFailure(finalFin1, mobileNum, getActivity());
                    }
                });
    }


    private void showVerificationConfirmationPopUp(String msg) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);

        warningMessage.setText(msg);
        title.setText("Verified Successfully!");
        //cancel.setText("No");
        cancel.setVisibility(View.GONE);
        confirm.setText("Login");
        title.setVisibility(View.VISIBLE);
        /*cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = PreferenceHandler.getLang(getActivity());
                dialog.cancel();
            }
        });*/
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                startActivity((new Intent(getActivity(), OnBoardingActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK)));

            }
        });

    }


//    private void moveToCorrespondingPage(ShareRecivedData shareRecivedData) {
//
//        if (shareRecivedData != null && shareRecivedData.getData() != null) {
//            Data data1 = shareRecivedData.getData();
//            String contentId = data1.getContentId();
//            String catalogId = data1.getCatalogId();
//            String theme = data1.getTheme();
//            String showId = data1.getShowID();
//            String layoutType = data1.getLayoutType();
//            String layoutScheme = data1.getLayoutScheme();
//            String planCategoryType = data1.getPlanCategoryType();
//            String type = data1.getType();
//            if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Media")) {
//                if (theme != null && theme.equalsIgnoreCase("live") || theme.equalsIgnoreCase("linear")) {
//                    LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
//                    bundle.putString(Constants.CATALOG_ID, catalogId);
//                    bundle.putString(Constants.CONTENT_ID, contentId);
//                    bundle.putString(Constants.THEME, theme);
//                    bundle.putString(Constants.LAYOUT_TYPE_SELECTED, layoutType);
//                    bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
//                    fragment.setArguments(bundle);
//                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, LiveTvDetailsFragment.TAG);
//                } else if (theme.equalsIgnoreCase("movie") || theme.equalsIgnoreCase("video")) {
//                    MovieDetailsFragment fragment = new MovieDetailsFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
//                    bundle.putString(Constants.CONTENT_ID, contentId);
//                    bundle.putString(Constants.CATALOG_ID, catalogId);
//                    bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
//                    fragment.setArguments(bundle);
//                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, MovieDetailsFragment.TAG);
//                } else if (theme.equalsIgnoreCase("show") ||
//                        theme.equalsIgnoreCase("show") ||
//                        theme.equalsIgnoreCase("shows")) {
//                    ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
//                    bundle.putString(Constants.CONTENT_ID, contentId);
//                    bundle.putString(Constants.CATALOG_ID, catalogId);
//                    bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
//                    bundle.putBoolean(Constants.IS_EPISODE, false);
//                    fragment.setArguments(bundle);
//                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
//                } else if ((theme.equalsIgnoreCase("episode") ||
//                        theme.equalsIgnoreCase("show_episode")) &&
//                        ((theme.equalsIgnoreCase("show") ||
//                                theme.equalsIgnoreCase("shows") ||
//                                theme.equalsIgnoreCase("episode")))) {
//                    ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
//                    bundle.putString(Constants.CONTENT_ID, contentId);
//                    bundle.putString(Constants.CATALOG_ID, catalogId);
//                    bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
//
//                    bundle.putBoolean(Constants.IS_EPISODE, true);
//                    bundle.putString(Constants.SHOW_ID, showId);
//                    fragment.setArguments(bundle);
//                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
//                } else if ((theme.equalsIgnoreCase("episode") ||
//                        theme.equalsIgnoreCase("album"))) {
//                    ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
//                    bundle.putString(Constants.CONTENT_ID, contentId);
//                    bundle.putString(Constants.CATALOG_ID, catalogId);
//                    bundle.putString(Constants.SHOW_ID, showId);
//                    bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
//
//                    bundle.putBoolean(Constants.IS_EPISODE, true);
//                    fragment.setArguments(bundle);
//                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
//                } else if ((theme.equalsIgnoreCase("show") || theme.equalsIgnoreCase("album"))) {
//                    ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
//                    bundle.putString(Constants.CONTENT_ID, contentId);
//                    bundle.putString(Constants.CATALOG_ID, catalogId);
//                    bundle.putString(Constants.SHOW_ID, showId);
//                    bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
//
//                    bundle.putBoolean(Constants.IS_EPISODE, false);
//                    fragment.setArguments(bundle);
//                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, ShowDetailsPageFragment.TAG);
//
//                } else {
//                    MovieDetailsFragment fragment = new MovieDetailsFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, planCategoryType);
//                    bundle.putString(Constants.CONTENT_ID, contentId);
//                    bundle.putString(Constants.CATALOG_ID, catalogId);
//                    bundle.putString(Constants.LAYOUT_SCHEME, layoutScheme);
//                    fragment.setArguments(bundle);
//                }
//            } else if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("listing-page")) {
//                String homeLinkUrl = data1.getUrl();
//                String friendlyId = data1.getFriendlyId();
//
//                ListingFragment fragment = new ListingFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.DISPLAY_TITLE, data1.getTitle());
//                bundle.putString(Constants.HOME_LINK, homeLinkUrl);
//                bundle.putString(Constants.THEME, data1.getTheme());
//                bundle.putString(Constants.LAYOUT_SCHEME, data1.getLayoutScheme());
//                fragment.setArguments(bundle);
//                Helper.addFragment(getActivity(), fragment, ListingFragment.TAG);
//
//            } else if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("web-page")) {
//                String url = data1.getUrl();
//                String title = data1.getTitle();
//
//                TermsOfUserFragment fragment = new TermsOfUserFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.DISPLAY_TITLE, title);
//                bundle.putString(Constants.DEEP_LINK_URL, url);
//                bundle.putString(Constants.TYPE, Constants.Type.DEEP_LINK_URL);
//
//                fragment.setArguments(bundle);
//                Helper.addFragmentForDetailsScreen(getActivity(), fragment, TermsOfUserFragment.TAG);
//            }
//        }
//
//    }


    @Override
    public void onStop() {
        super.onStop();
        //isFromBranchIO = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Branch init
/*        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    try {

                        if(referringParams.getBoolean("+clicked_branch_link") && referringParams.getBoolean("+is_first_session")){
                            //save to shared preference
                            setDataAndStoreToReferalThings(referringParams);
                        }

                        if (referringParams.getString("$canonical_url") != null) {
                            checkForDeepLinkingStatus(referringParams.getString("$canonical_url"));
                            Log.e("BRANCH_link", referringParams.getString("$canonical_url"));
                            isFromBranchIO = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("BRANCH", referringParams.toString());
                } else {
                    Log.e("BRANCH", error.getMessage());
                }
            }
        }, getActivity().getIntent().getData(), getActivity());*/

    }

    public void setDataAndStoreToReferalThings(JSONObject referringParams) {
        try {
            CampaignData referalKeys = new CampaignData();

            if (referringParams.getString("~channel") != null) {
                referalKeys.setPkSource(referringParams.getString("~channel"));
            } else {
                referalKeys.setPkSource("");
            }

            if (referringParams.getString("~campaign") != null) {
                referalKeys.setPkCampaign(referringParams.getString("~campaign"));
            } else {
                referalKeys.setPkCampaign("");
            }

            if (referringParams.getString("~feature") != null) {
                referalKeys.setPkMedium(referringParams.getString("~feature"));
            } else {
                referalKeys.setPkMedium("");
            }

            if (referringParams.getString("$og_title") != null) {
                referalKeys.setPkContent(referringParams.getString("$og_title"));
            } else {
                referalKeys.setPkContent("");
            }

            if (referringParams.getString("$marketing_title") != null) {
                referalKeys.setPkKwd(referringParams.getString("$marketing_title"));
            } else {
                referalKeys.setPkKwd("");
            }
            //PreferenceHandler.setReferalDetails(getActivity(), referalKeys);
        } catch (Exception e) {

        }
    }

    private void setTabCustomView() {
        for (int i = 0; i < mTabView.getTabCount(); i++) {
            TabLayout.Tab tab = mTabView.getTabAt(i);
            if (tab != null) {
                Objects.requireNonNull(tab.setCustomView(R.layout.tab_selected_layout));
                setTabUnSelectedUI(tab);
            }
        }
        Objects.requireNonNull(mTabView.getTabAt(0)).select();
    }

    private int currentTab = -1;

    public void setListners() {
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                mTabView));
        mTabView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, 0);
                        //scrollView.fullScroll(View.FOCUS_UP);
                        scrollView.smoothScrollTo(0, 0);
                    }
                });
                String category = tab.getText().toString() != null ?  tab.getText().toString() :"";

                WebEngageAnalytics webEngageAnalytics = new WebEngageAnalytics();
                webEngageAnalytics.categoryEvent(new CategoryEvents(category,""));
                mCollapsToolBar.setExpanded(true, true);
                if (!JUSTCHANGES) {
                    currentTab = tab.getPosition();
                }else{
                    JUSTCHANGES = false;
                }
                    mPager.setCurrentItem(tab.getPosition());
                if (tab.getCustomView() != null)
                    setTabSelectedUI(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null)
                    setTabUnSelectedUI(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null)
                    setTabSelectedUI(tab);
            }
        });
    }

    public void configureTabDetails() {

        Helper.showProgressDialog(getActivity());
        mApiService.getHomeScreenTabs().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<HomeScreenResponse>() {
            @Override
            public void call(HomeScreenResponse homeScreenResponse) {
                Helper.dismissProgressDialog();
                if (homeScreenResponse != null && homeScreenResponse.getData() != null
                        && homeScreenResponse.getData().getCatalogListItems() != null &&
                        homeScreenResponse.getData().getCatalogListItems().size() > 0)
                    for (CatalogListItem catalogListItem : homeScreenResponse.getData().getCatalogListItems()) {
                        if (catalogListItem.getDisplayTitle() != null)
                            Constants.tabsPresent.add(catalogListItem.getDisplayTitle().toLowerCase());
                    }

                viewPagerSetUp(homeScreenResponse);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                Helper.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setTabUnSelectedUI(TabLayout.Tab tab) {
        TextView txt = (TextView) tab.getCustomView().findViewById(R.id.textView);
        txt.setTextColor(getResources().getColor(R.color.txt_color));
        txt.setPadding(0, 0, 0, 0);
        txt.setBackground(null);
        txt.setText(tab.getText());
    }


    private void setTabSelectedUI(TabLayout.Tab tab) {

        String temp;
        if (tab.getCustomView() != null)
            temp = tab.getText() != null ? tab.getText().toString() : "all";
        else
            return;

        /*Get lists of color schemes from constants and get required based on title of the tab */
        //LayoutDbScheme schemeval = Constants.getSchemeColor(temp.replace(' ', '_'));
        //if (schemeval != null) {
        // colors[0] = Color.parseColor("#" + schemeval.getStartColor());
        //colors[1] = Color.parseColor("#" + schemeval.getEndColor());
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BL_TR, colors);
        // gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        // gd.setCornerRadius(50f);
        TextView txt = (TextView) tab.getCustomView().findViewById(R.id.textView);
        // Picasso.get().load(schemeval.getImageUrl()).into(mImageBackGround);
        txt.setTextColor(getResources().getColor(R.color.txt_color));
        txt.setPadding((int) getResources().getDimension(R.dimen.px_16),
                (int) getResources().getDimension(R.dimen.px_8),
                (int) getResources().getDimension(R.dimen.px_16),
                (int) getResources().getDimension(R.dimen.px_8));
        txt.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_btn_corner_filled_for_white));
        txt.setText(tab.getText());

        pageEvents = new PageEvents(tab.getText().toString());
        webEngageAnalytics.screenViewedEvent(pageEvents);
        //mAnalytics.reportCategoryTabSelected(tab.getText().toString());
       /* try {
            mAnalytics.branchonClickOfTabs(tab.getText().toString().replace(" ", "_").toLowerCase(), getActivity());
        } catch (Exception e) {
        }*/

        //}
    }

    public void viewPagerSetUp(final HomeScreenResponse homeScreenResponse) {
        if (PreferenceHandler.isKidsProfileActive(getActivity())) {
            mKidsAdapter = new KidsAdapter(getChildFragmentManager(), getActivity(), homeScreenResponse, mTabView);
            mPager.setAdapter(mKidsAdapter);
            setTabCustomView();  // Added to set custom view for TABS
            mPager.setOffscreenPageLimit(1);
            mKidsAdapter.setOnDisplayClickListener(new HomePagerAdapter.DisplayTitleClickListener() {
                @Override
                public void onClickDisplayTitle(String title) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, 0);
                            scrollView.fullScroll(View.FOCUS_UP);
                            scrollView.smoothScrollTo(0, 0);
                        }
                    });
                    mCollapsToolBar.setExpanded(true, true);
                    //mPager.scrollTo(0,0);
                    if (homeScreenResponse != null) {
                        Data data = homeScreenResponse.getData();
                        if (data != null) {
                            /**
                             * Go to specific tabs based on homescreen item selections
                             */
                            List<CatalogListItem> catalogListItems = data.getCatalogListItems();
                            for (int i = 0; i < catalogListItems.size(); i++) {

                                if (catalogListItems.get(i).getDisplayTitle().equalsIgnoreCase(title)) {
                                    currentTab = i;
                                    mPager.setCurrentItem(i);
                                }
                            }
                        }
                    }
                }
            });
        } else {
            mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), getActivity(), homeScreenResponse, mTabView);
            mPager.setAdapter(mHomePagerAdapter);
            setTabCustomView();  // Added to set custom view for TABS

            if (mHomePagerAdapter != null)
                mPager.setOffscreenPageLimit(mHomePagerAdapter.getCount() - 1);
            else
                mPager.setOffscreenPageLimit(8);

            if (currentTab != -1)
                mPager.setCurrentItem(currentTab);

            mHomePagerAdapter.setOnDisplayClickListener(new HomePagerAdapter.DisplayTitleClickListener() {
                @Override
                public void onClickDisplayTitle(String title) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_UP);
                            scrollView.smoothScrollTo(0, 0);
                        }
                    });
                    mCollapsToolBar.setExpanded(true, true);
                    //mPager.scrollTo(0,0);
                    if (homeScreenResponse != null) {
                        Data data = homeScreenResponse.getData();
                        if (data != null) {
                            /**
                             * Go to specific tabs based on homescreen item selections
                             */
                            List<CatalogListItem> catalogListItems = data.getCatalogListItems();
                            for (int i = 0; i < catalogListItems.size(); i++) {

                                if (catalogListItems.get(i).getDisplayTitle().equalsIgnoreCase(title)) {
                                    currentTab = i;
                                    mPager.setCurrentItem(i);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.HomeScreen);
        pageEvents = new PageEvents(Constants.HomeScreen);
        webEngageAnalytics.homeEvent(pageEvents);
    }
}
