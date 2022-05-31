package com.otl.tarangplus.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.inmobi.ads.AdMetaInfo;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.ads.listeners.InterstitialAdEventListener;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.adapters.LinearTvAdapter;
import com.otl.tarangplus.adapters.NextProgramsAdapter;
import com.otl.tarangplus.adapters.TvListItemAdapter;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AccessControl;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.ChannelLogo;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.MediaplaybackEvents;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.PlayListResponse;
import com.otl.tarangplus.model.TitleEvents;
import com.otl.tarangplus.model.UserInfo;
import com.otl.tarangplus.model.WatchCount;
import com.otl.tarangplus.model.sLogo169;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.saranyu.ott.instaplaysdk.InstaCastItem;
import com.saranyu.ott.instaplaysdk.InstaMediaItem;
import com.saranyu.ott.instaplaysdk.InstaPlayView;
import com.saranyu.ott.instaplaysdk.ads.Adds;
import com.saranyu.ott.instaplaysdk.mediatracks.AudioTrack;
import com.saranyu.ott.instaplaysdk.mediatracks.CaptionTrack;
import com.saranyu.ott.instaplaysdk.mediatracks.VideoTrack;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlFetcher2;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlResponseV2;
import com.squareup.picasso.Picasso;

import com.vidgyor.livemidroll.vidgyorPlayerManager.PlayerManager;
import com.vidgyor.livemidroll.vidgyorPlayerManager.VidgyorStatusInit;
import com.vidgyor.networkcheck.VidgyorNetworkManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static com.saranyu.ott.instaplaysdk.InstaPlayView.SORT_ORDER_ASCENDING;

public class LiveTvDetailsFragment extends UniversalFragment {
    @Override
    protected void languageChanged() {
        getDetails();
    }

    private static final long LIVETV_TIME_LIMIT = 1000 * 60 * 60;

    private Analytics mAnalyticsEvent;
    private MediaplaybackEvents mediaplaybackEvents;
    private AppEvents appEvents;
    private long bufferStartTime;

    private static boolean IS_MEDIA_ACTIVE_SENT = false;
    private static boolean IS_PLAY_EVENT_FIRED = false;
    public static String TAG = LiveTvDetailsFragment.class.getSimpleName();
    private boolean IS_SUBSCRIBED = false;
    private ViewHolder viewHolder;
    private ApiService mApiService;
    private AnalyticsProvider mAnalytics;
    private ListResonse currentItem, othersChannel;
    private String displayTitle;
    private String adaptiveURL;
    private EventListener mEventListener;
    private boolean isExpanded = false;
    private ImageView mPlayerChannelLogoView, mPlayerLiveTag;
    private static String CHANNEL_LOGO = "";
    private TvListItemAdapter tvListItemAdapter;
    private LinearTvAdapter adapter;
    private String plainCategory;
    private int PAGEINDEX = 0;
    private boolean IS_LAST_ELEMENT = false;
    private NextProgramsAdapter nexAdapter;
    private String USER_STATE;
    private String MEDIA_TYPE = "MediaVideo";
    public SubscribeBottomSheetDialog bottomSheetFragment;
    private boolean BUFFER_STARTED = true;
    private long ANALYTICS_ACTIVE_INTERVAL;
    private CountDownTimer mActiveCountDown;
    private boolean IS_TEMP_PAUSED = false;   // This is used to disable resume of content when paused as this is for Live and Linear
    private static String analytics_unique_id;
    private static long analytics_time_spent = 0;

    private CastStateListener mCastStateListener;
    private CastContext mCastContext;
    private IntroductoryOverlay mIntroductoryOverlay;
    private MenuItem mediaRouteMenuItem;
    private InstaCastItem mInstaCastItem;
    private static boolean IS_CAST_CONNECTED = false;
    private static boolean IS_TEMP_CAST = false;
    private InMobiInterstitial interstitialAd;
    private InterstitialAd mInterstitialAdGoogle;
    private AdManagerInterstitialAd mAdmanagerInterstitialAd;

    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;
    private TitleEvents titleEvents;
    private String categoryName;
    private String contentType;
    private String titleImage;
    private String categoryID;
    private String titleName;
    private String titleID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_tv_details, container, false);
        viewHolder = new ViewHolder(view);
     /*   //InMobi
        interstitialAd = new InMobiInterstitial(getActivity(), 1598949363916L,
                mInterstitialAdEventListener);
        //Google
        AdRequest adRequest = new AdRequest.Builder().build();*/

        mAnalyticsEvent = Analytics.getInstance(getContext());

        //Google admanager
        AdManagerAdRequest adManagerRequest = new AdManagerAdRequest.Builder().build();
        AdManagerInterstitialAd.load(getActivity(), BuildConfig.GOOGLEADID, adManagerRequest, adManagerInterstitialAdLoadCallback);

        webEngageAnalytics = new WebEngageAnalytics();
       // InterstitialAd.load(getActivity(), BuildConfig.GOOGLEADID, adRequest, /*interstitialAdLoadCallback*/null);
        return view;
    }

    AdManagerInterstitialAdLoadCallback adManagerInterstitialAdLoadCallback = new AdManagerInterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull AdManagerInterstitialAd adManagerInterstitialAd) {
            mAdmanagerInterstitialAd = adManagerInterstitialAd;
            showAds();
            mAdmanagerInterstitialAd.setFullScreenContentCallback(fullScreenContentCallbackAdManager);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.i(TAG, loadAdError.getMessage());
            mAdmanagerInterstitialAd = null;
        }
    };
/*
    InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            Log.d("Googleadloaded", "true");
            mInterstitialAdGoogle = interstitialAd;
            mInterstitialAdGoogle.setFullScreenContentCallback(fullScreenContentCallback);
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.d("Googleadloaded", loadAdError.getMessage());
            mInterstitialAdGoogle = null;
            super.onAdFailedToLoad(loadAdError);
        }
    };
*/
    FullScreenContentCallback fullScreenContentCallbackAdManager = new FullScreenContentCallback() {
        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
        }

        @Override
        public void onAdShowedFullScreenContent() {
            mAdmanagerInterstitialAd = null;
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
        }
    };

/*
    FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
        @Override
        public void onAdShowedFullScreenContent() {
            mInterstitialAdGoogle = null;
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
        }

        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
        }
    };
*/

    private void hideUI() {
        viewHolder.instaContainer.setVisibility(View.GONE);
        viewHolder.scroll_layout.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideUI();
        RestClient mRestClient = new RestClient(getActivity());
        mApiService = mRestClient.getApiService();
        if (getActivity() != null)
            (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //Helper.clearLightStatusBar(getActivity());

        /*** Analytics **/
        mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.setmPageStartTime(Calendar.getInstance().getTime());
        Window window = getActivity().getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (mOnEventListener != null) {
            mOnEventListener.onEvent(true);
        }

        mCastStateListener = state -> {
            if (state != CastState.NO_DEVICES_AVAILABLE) {
                showIntroductoryOverlay();
            }

            if (state == CastState.NOT_CONNECTED) {
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else if (state == CastState.CONNECTED) {
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        };
        mCastContext = CastContext.getSharedInstance(getActivity());
        if (mCastContext != null)
            if (mCastContext.getCastState() == 3 || mCastContext.getCastState() == 4) {
                IS_CAST_CONNECTED = true;
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }

        viewHolder.nextPlayRecyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        viewHolder.other_channels_recycler_view.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));


        if (IS_TEMP_CAST && IS_CAST_CONNECTED) {
            IS_TEMP_CAST = false;
        }

    /*    Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                // your code here...
                getViewCount();
            }
        };*/

// schedule the task to run starting now and then every hour...
        /*timer.schedule(hourlyTask, 0l, 20000);*/


        getDetails();


        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareUrl = currentItem.getData().getShareUrl();
                if (!TextUtils.isEmpty(shareUrl)) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    String shareText = "Hey !" +
                            "\n" +
                            "I'm watching some really amazing videos on TarangPlus. Check this one. It's one of my favourites!";
                    if (!TextUtils.isEmpty(shareUrl)) {
                        i.putExtra(Intent.EXTRA_TEXT, shareText + " \n " + shareUrl.toLowerCase());
                    }
                    i.setType("text/plain");
                    startActivity(Intent.createChooser(i, "Share via"));

                    boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
                    if (loggedIn)
                        mAnalytics.reportAppShare(shareUrl);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewHolder.other_channels_recycler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!viewHolder.other_channels_recycler_view.canScrollHorizontally(1) && !IS_LAST_ELEMENT) {
                        getDetails();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewHolder.nextPlayRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!viewHolder.nextPlayRecyclerView.canScrollHorizontally(1) && !IS_LAST_ELEMENT) {
                        getDetails();
                    }
                }
            });
        }

        String mediaActiveInterval = PreferenceHandler.getMediaActiveInterval(getActivity());

        if (mediaActiveInterval != null) {
            long l = Long.parseLong(mediaActiveInterval);
            ANALYTICS_ACTIVE_INTERVAL = l * 60 * 1000;
        } else {
            ANALYTICS_ACTIVE_INTERVAL = 120000;
        }
    }

    private void getViewCount() {
        final String content_id = getArguments().getString(Constants.CONTENT_ID);
        mApiService.getViewCount(content_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WatchCount>() {
                    @Override
                    public void call(WatchCount watchCount) {
                        if (watchCount != null) {
                            viewHolder.livecount.setVisibility(View.VISIBLE);
                            viewHolder.livecount.setText(watchCount.getViews_count());
                        /*    viewHolder.view_count.setVisibility(View.VISIBLE);
                            viewHolder.view_count.setText(watchCount.getViews_count() +" watching now");*/
                        } else {
                            viewHolder.view_count.setVisibility(View.GONE);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    InterstitialAdEventListener mInterstitialAdEventListener = new InterstitialAdEventListener() {

        @Override
        public void onAdLoadSucceeded(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull AdMetaInfo adMetaInfo) {
            Log.d("InterstitialAd", "onAdLoadSucceeded");
            if (interstitialAd != null)
                interstitialAd.show();
        }

        @Override
        public void onAdLoadFailed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
            Log.d("InterstitialAd", "onAdLoadFailed");
        }

        @Override
        public void onAdWillDisplay(@NonNull InMobiInterstitial inMobiInterstitial) {
            Log.d("InterstitialAd", "onAdWillDisplay");
            super.onAdWillDisplay(inMobiInterstitial);
        }

        @Override
        public void onAdDisplayed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull AdMetaInfo adMetaInfo) {
            // super.onAdDisplayed(inMobiInterstitial, adMetaInfo);
            Log.d("InterstitialAd", "onAdDisplayed");

        }

        @Override
        public void onAdDismissed(@NonNull InMobiInterstitial inMobiInterstitial) {
            Log.d("InterstitialAd", "onAdDismissed");
            //super.onAdDismissed(inMobiInterstitial);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        viewHolder.mInstaPlayView.setCastContext(mCastContext);
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
                            getActivity(), mediaRouteMenuItem)
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

    private InstaCastItem getCastMediaItem(String adaptiveUrl) {

        String des = "";
        if (currentItem.getData().getDescription().length() > 250)
            des = currentItem.getData().getDescription().substring(0, 250);


        String portImage = currentItem.getData().getThumbnails().getLarge23().getUrl();
        if (!TextUtils.isEmpty(portImage))
            portImage = currentItem.getData().getThumbnails().getLarge169().getUrl();

        return new InstaCastItem(currentItem.getData().getTitle(),
                des,
                adaptiveUrl,
                portImage,
                currentItem.getData().getThumbnails().getLarge169().getUrl());
    }


    public void getDetails() {
        releaseVidPlayer();
        IS_TEMP_PAUSED = false;   // need to disable while switching content

        if (IS_TEMP_CAST && IS_CAST_CONNECTED) {
            IS_TEMP_CAST = false;
            return;
        }

        final String content_id = getArguments().getString(Constants.CONTENT_ID);
        final String catalog_id = getArguments().getString(Constants.CATALOG_ID);
        String displayTitle = getArguments().getString(Constants.DISPLAY_TITLE);
        // String channel_id = ;

        Helper.showProgressDialog(getActivity());

        String layoutScheme = getArguments().getString(Constants.LAYOUT_SCHEME);
        if (!TextUtils.isEmpty(layoutScheme)) {
            setTopbarUI(Constants.getSchemeColor(layoutScheme));
        }

        mApiService.getNextPrograms(catalog_id, content_id, "any")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        currentItem = listResonse;
                        viewHolder.scroll_layout.scrollTo(0, 0);
                        if (listResonse != null) {
                            Data responseData = listResonse.getData();
                            viewHolder.meta_data_holder.removeAllViews();
                            if (responseData != null) {
                                Map<String, ArrayList> additionalData = responseData.getItemAdditionalData();
                                if (additionalData != null && additionalData.size() > 0) {
                                    for (TreeMap.Entry<String, ArrayList> entry : additionalData.entrySet()) {
                                        String key = entry.getKey();
                                        ArrayList value = entry.getValue();
                                        updateUI(key, value);

                                    }
                                } else {
                                    viewHolder.meta_data_holder.setVisibility(View.GONE);
//                                    viewHolder.downArrowHolder.setVisibility(View.GONE);
                                }
                            }

                            if (PreferenceHandler.isLoggedIn(getActivity()))
                                getUserStates(currentItem, responseData);
                            else if (responseData != null) {
                                getSmartURL(responseData);
                                //showAds();
                                isAdAvailableOnGoogle =true;
                            }
                            setUpUI(responseData);
                            showUI();
                            Helper.dismissProgressDialog();
                            String theme = getArguments().getString(Constants.THEME);
                            if (!TextUtils.isEmpty(theme) && theme.equalsIgnoreCase("live")) {
                                viewHolder.playNext.setVisibility(View.GONE);
                                viewHolder.nextPlayRecyclerView.setVisibility(View.GONE);
                            } else {
                                viewHolder.playNext.setVisibility(View.VISIBLE);
                                viewHolder.nextPlayRecyclerView.setVisibility(View.VISIBLE);
                                getNextPlayableItems();
                            }
                            getOtherChannels(currentItem.getData().getCatalogObject().getPlanCategoryType());
                        } else {
                            Helper.dismissProgressDialog();
                            showEmptyMessage();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        showEmptyMessage();
                        Helper.dismissProgressDialog();
                    }
                });
        //TODO //


    }


    private void getNextPlayableItems() {
        final String content_id = getArguments().getString(Constants.CONTENT_ID);
        final String catalog_id = getArguments().getString(Constants.CATALOG_ID);

        mApiService.getNextPrograms(catalog_id, content_id, "any")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        if (listResonse != null) {
                            Data data = listResonse.getData();
                            List<Item> list = data.getItems();
                            if (list != null && list.size() > 0) {
                                viewHolder.nextPlayRecyclerView.setVisibility(View.VISIBLE);
                                viewHolder.playNext.setVisibility(View.VISIBLE);
                                updateNextProgramsList(list);
                            } else {
                                viewHolder.nextPlayRecyclerView.setVisibility(View.GONE);
                                viewHolder.playNext.setVisibility(View.GONE);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void updateNextProgramsList(List<Item> list) {
        nexAdapter = new NextProgramsAdapter(getActivity());
        viewHolder.nextPlayRecyclerView.setAdapter(nexAdapter);
        viewHolder.nextPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        nexAdapter.updateNextProgramms(list);
    }

    private void getOtherChannels(String planCategoryType) {
        plainCategory = planCategoryType;
        String theme = getArguments().getString(Constants.THEME);
        if (!TextUtils.isEmpty(theme)) {
            if (theme.equalsIgnoreCase("linear")) {

                String category = "all";
                if (PreferenceHandler.isKidsProfileActive(getActivity()))
                    category = "kids";

                mApiService.getAllChannelList(theme, PAGEINDEX, category)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ListResonse>() {
                            @Override
                            public void call(ListResonse listResonse) {
                                try {
                                    if (listResonse != null) {
                                        PAGEINDEX++;
                                        if (listResonse.getData().getCatalogListItems().size() < Constants.PAGE_LIST_LIMIT) {
                                            IS_LAST_ELEMENT = true;
                                        }

                                        Iterator<CatalogListItem> iterator = listResonse.getData().getCatalogListItems().iterator();
                                        while (iterator.hasNext()) {
                                            CatalogListItem next = iterator.next();
                                            if (next.getContentID().equalsIgnoreCase(currentItem.getData().getContentId())) {
                                                listResonse.getData().getCatalogListItems().remove(next);
                                                break;
                                            }
                                        }
                                        setUpOtherChannelsLive(listResonse);
                                    }
                                } catch (Exception e) {
                                    setUpOtherChannelsLive(listResonse);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
            } else if (theme.equalsIgnoreCase("live")) {
                String catalog_id = getArguments().getString(Constants.CATALOG_ID);
                if (TextUtils.isEmpty(catalog_id))
                    catalog_id = currentItem.getData().getCatalogId();

                mApiService.getLiveChannels(catalog_id, planCategoryType, "any", PAGEINDEX)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ListResonse>() {
                            @Override
                            public void call(ListResonse listResonse) {
                                try {
                                    if (listResonse != null) {
                                        for (Item element : listResonse.getData().getItems()) {
                                            PAGEINDEX++;
                                            if (listResonse.getData().getItems().size() < Constants.PAGE_LIST_LIMIT) {
                                                IS_LAST_ELEMENT = true;
                                            }
                                            if (element.getContentId().equalsIgnoreCase(currentItem.getData().getContentId()))
                                                listResonse.getData().getItems().remove(element);
                                        }
                                        setUpOtherChannelsLive(planCategoryType, listResonse);
                                    }
                                } catch (Exception e) {
                                    setUpOtherChannelsLive(planCategoryType, listResonse);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
            }
        }
    }

    private void setUpOtherChannelsLive(String planCategory, ListResonse resonse) {
        if (resonse != null) {
            othersChannel = resonse;
            Data data = resonse.getData();
            if (data != null) {
                List<Item> items = data.getItems();
                if (items != null && items.size() > 0) {
                    tvListItemAdapter = new TvListItemAdapter(getActivity());

                    String theme = getArguments().getString(Constants.THEME);
                    if (!TextUtils.isEmpty(theme) && theme.equalsIgnoreCase("live")) {
                        if (!(planCategory.contains("Live") || planCategory.contains("live"))) {
                            viewHolder.mOthersTitle.setText("More in " + planCategory + " Live");
                        }
                    }
                    viewHolder.other_channels_recycler_view.setAdapter(tvListItemAdapter);
                    viewHolder.other_channels_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                    tvListItemAdapter.appendSetTvItems(items);
                    tvListItemAdapter.notifyDataSetChanged();
                    tvListItemAdapter.setItemClickListener(new TvListItemAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(Item item) {
                            PAGEINDEX = 0;
                            Bundle arguments = getArguments();
                            arguments.putString(Constants.CATALOG_ID, item.getCatalogId());
                            arguments.putString(Constants.CONTENT_ID, item.getContentId());
                            arguments.putString(Constants.THEME, item.getTheme());
                            arguments.putString(Constants.DISPLAY_TITLE, item.getTitle());
                            String layoutType = item.getCatalogObject().getLayoutType();
                            if (TextUtils.isEmpty(layoutType)) {
                                layoutType = getArguments().getString(Constants.LAYOUT_TYPE_SELECTED);
                            }
                            arguments.putString(Constants.LAYOUT_TYPE_SELECTED, layoutType);
                            arguments.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                            setArguments(arguments);
                            viewHolder.mOthersParentLyt.setVisibility(View.GONE);
                            getDetails();
                        }
                    });
                    viewHolder.mOthersParentLyt.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mOthersParentLyt.setVisibility(View.GONE);
                }
            }
        }
    }


    private void setUpOtherChannelsLive(ListResonse listResonse) {
        if (listResonse != null) {
            othersChannel = listResonse;
            Data data = listResonse.getData();
            if (data != null) {
                List<CatalogListItem> items = data.getCatalogListItems();
                if (items != null && items.size() > 0) {


                    String theme = getArguments().getString(Constants.THEME);
                    if (!TextUtils.isEmpty(theme) && theme.equalsIgnoreCase("linear")) {
                        viewHolder.mOthersTitle.setText("Other channels");
                    }

                    adapter = new LinearTvAdapter(getActivity());
                    viewHolder.other_channels_recycler_view.setAdapter(adapter);
                    viewHolder.other_channels_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


                    adapter.appendSetTvItems(items);
//                    adapter.notifyDataSetChanged();
                    if (items != null && items.size() == 1) {
                        viewHolder.mOthersTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        viewHolder.mOthersTitle.setClickable(false);
                    } else {
                        viewHolder.mOthersTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_black, 0);
                        viewHolder.mOthersTitle.setClickable(true);
                    }
                    adapter.setItemClickListener(new LinearTvAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(CatalogListItem item) {
                            PAGEINDEX = 0;
                            Bundle arguments = getArguments();
                            arguments.putString(Constants.CATALOG_ID, item.getCatalogID());
                            arguments.putString(Constants.CONTENT_ID, item.getContentID());
                            arguments.putString(Constants.DISPLAY_TITLE, item.getTitle());
                            String layoutType = item.getCatalogObject().getLayoutType();
                            if (TextUtils.isEmpty(layoutType)) {
                                layoutType = getArguments().getString(Constants.LAYOUT_TYPE_SELECTED);
                            }
                            arguments.putString(Constants.LAYOUT_TYPE_SELECTED, layoutType);
                            arguments.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                            setArguments(arguments);
                            getDetails();
                        }
                    });
                    viewHolder.mOthersParentLyt.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.mOthersParentLyt.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setUpUI(Data data) {
        if (data != null) {
//            getSmartURL(data);
            appEvents = new AppEvents(1, "", Constants.LiveTvScreen, Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, data.getTitle(),
                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);

            Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.mImage);
            Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.vidImage);

//            viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
//            viewHolder.mImage.setVisibility(View.VISIBLE);

            viewHolder.mPlayIcon.setVisibility(View.GONE);
            viewHolder.mImage.setVisibility(View.GONE);
            viewHolder.vidImage.setVisibility(View.GONE);

            if (data.getItemCaption() != null) {
                viewHolder.meta_data.setVisibility(View.VISIBLE);
                viewHolder.meta_data.setText(data.getItemCaption());
            } else {
                viewHolder.meta_data.setVisibility(View.GONE);
            }

            String description = data.getDescription();
            if (!TextUtils.isEmpty(description)) {
                viewHolder.description.setVisibility(View.VISIBLE);
                viewHolder.description.setText(description);
                viewHolder.downArrowHolder.setVisibility(View.VISIBLE);
            } else {
                viewHolder.downArrowHolder.setVisibility(View.GONE);
                viewHolder.description.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(data.getTitle())) {
                displayTitle = data.getTitle();
                viewHolder.title.setText(displayTitle);
                viewHolder.header.setText(displayTitle);
                viewHolder.mPlayerTitleTxt.setText(displayTitle);

            }
            setChannelLogo(data);

            categoryName = data.getMediaType() != null ? data.getMediaType():"";
            categoryID = data.getCatalogId() != null ? data.getCatalogId():"";
            titleName = data.getTitle() != null ? data.getTitle():"";
            titleID = data.getContentId() != null ? data.getContentId():"";
            contentType =data.getMediaType() != null ? data.getMediaType():"";
            titleImage = data.getThumbnails().getLarge169().getUrl() != null ? data.getThumbnails().getLarge169().getUrl() :"";
            titleEvents = new TitleEvents(categoryName,categoryID,"","",titleName,contentType,titleImage,titleID);
            webEngageAnalytics.titleViewedEvent(titleEvents);

            /*if (data.getAccessControl() != null) {
                if (!data.getAccessControl().getIsFree())
                    viewHolder.premium.setVisibility(View.VISIBLE);
                else
                    viewHolder.premium.setVisibility(View.GONE);
            }*/
            if (getArguments().getString(Constants.THEME).equalsIgnoreCase("live")) {
                viewHolder.premium.setVisibility(View.VISIBLE);
                Picasso.get().load(R.drawable.live_rec).into(viewHolder.premium);
            }
        }

    }

    private void setChannelLogo(Data data) {
        ChannelLogo channelLogo = data.getChannelLogo();
        if (channelLogo != null) {
            sLogo169 xlLogo = channelLogo.getXlLogo();
            if (xlLogo != null) {
                String url = xlLogo.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    CHANNEL_LOGO = url;
//                    Picasso.get().load(url).into(mPlayerChannelLogoView);
                } else {
                    CHANNEL_LOGO = "";
                }
            }
        }
    }

    private Adds adds;
    private String ads;

    public void getSmartURL(Data currentItem) {
        if (currentItem != null && currentItem.getPlayUrl() != null && currentItem.getPlayUrl().getVidgyorUrl() != null && currentItem.getPlayUrl().getVidgyorUrl().getName() != null) {
            Helper.dismissProgressDialog();
            checkAccessControll();
            return;
        }
        // TODO: 25/09/18 Should change Auth Token
        adds = null;  // Resetting Ads state
        ads = "";
        String smartUrl = "";
        try {
            if (currentItem != null && currentItem.getPlayUrl().getSaranyu() != null && !TextUtils.isEmpty(currentItem.getPlayUrl().getSaranyu().getUrl())) {
                smartUrl = currentItem.getPlayUrl().getSaranyu().getUrl();
            } else if (currentItem != null && !TextUtils.isEmpty(currentItem.getPlayUrl().getUrl())) {
                smartUrl = currentItem.getPlayUrl().getUrl();
            } else if (currentItem != null && !TextUtils.isEmpty(currentItem.getSmartUrl())) {
                smartUrl = currentItem.getSmartUrl();
            }

        } catch (Exception e) {
            if (!TextUtils.isEmpty(currentItem.getPlayUrl().getUrl())) {
                smartUrl = currentItem.getPlayUrl().getUrl();
            }
            e.printStackTrace();
        }

        Helper.showProgressDialog(getActivity());
        SmartUrlFetcher2 urlFetcher = new SmartUrlFetcher2(smartUrl, "hls", Constants.PLAYER_AUTH_TOKEN);
        urlFetcher.setOnFinishListener(new SmartUrlFetcher2.SmartUrlFetcherFinishListener() {
            @Override
            public void onFinished(SmartUrlResponseV2 videoUrl) {

                if (videoUrl != null) {
                    getAdsIfPresent(currentItem);
                    adaptiveURL = Helper.getPlayBackURL(Constants.REGION, IS_SUBSCRIBED, videoUrl);
                    analytics_unique_id = UUID.randomUUID().toString();
                    analytics_time_spent = 0;
                    Helper.dismissProgressDialog();
                }

//                if (!adaptiveURL.isEmpty())
//                    setupPlayContent(adaptiveURL);

                checkAccessControll();          // added this for auto play.


            }

            @Override
            public void onError(Throwable throwable) {
                Helper.dismissProgressDialog();
                adaptiveURL = "";
            }
        });

        urlFetcher.getVideoUrls();
    }

    private void getAdsIfPresent(Data currentItem) {
        if (currentItem != null) {
            AccessControl accessControl = currentItem.getAccessControl();
            if (accessControl.getAdsAvailable()) {
//                adds = new Adds(Constants.getPreRoleAd(accessControl),
//                        Constants.getPostRoleAd(accessControl),
//                        Constants.getMidRoleAd(accessControl),
//                        Constants.getMidRoleAdPos(accessControl));


                if (accessControl.getVmap_url() != null) {
                    ads = accessControl.getVmap_url();
                    ads = ads.replace("{baseURL}", BuildConfig.ADS_URL);
                    ads = ads.replace("{token}", Constants.API_KEY);
                }

            }
        }
    }

    private void setupPlayContent(String adaptiveURL) {

        if (getActivity() == null || getActivity().isFinishing())
            return;

        if (viewHolder.mInstaPlayView != null) {
            if (!IS_CAST_CONNECTED)
                viewHolder.mInstaPlayView.pause();
            viewHolder.mInstaPlayView.stop();
            viewHolder.mInstaPlayView.release();
        }

        mEventListener = null;
        mEventListener = new EventListener();
        viewHolder.mInstaPlayView.addPlayerEventListener(mEventListener);
        viewHolder.mInstaPlayView.addPlayerUIListener(mEventListener);
        viewHolder.mInstaPlayView.addMediaTrackEventListener(mEventListener);

        InstaMediaItem mMediaItem;
        if (ads == null || ads.equals("") || IS_SUBSCRIBED) {
            mMediaItem = new InstaMediaItem(adaptiveURL);
        } else {
            mMediaItem = new InstaMediaItem(adaptiveURL, ads);

        }
        viewHolder.mInstaPlayView.setCastContext(mCastContext);
        mMediaItem.setInstaCastItem(getCastMediaItem(adaptiveURL));
        viewHolder.mInstaPlayView.setMediaItem(mMediaItem);
        viewHolder.mInstaPlayView.AutoPlayEnable(true);   // Added this to fix Ads playback
        viewHolder.mInstaPlayView.prepare();

        viewHolder.mInstaPlayView.setLanguageVisibility(false);
        viewHolder.mInstaPlayView.setCaptionVisibility(false);
        viewHolder.mInstaPlayView.setQualityVisibility(true);
        viewHolder.mInstaPlayView.setBufferVisibility(true);
        viewHolder.mInstaPlayView.setSeekBarVisibility(false);
        viewHolder.mInstaPlayView.setCurrentProgVisible(false);
        viewHolder.mInstaPlayView.setDurationTimeVisible(false);
        viewHolder.mInstaPlayView.setRewindVisible(false);
        viewHolder.mInstaPlayView.setForwardTimeVisible(false);
        viewHolder.mInstaPlayView.setHDVisibility(false);
        viewHolder.mInstaPlayView.setMuteVisible(false);
        viewHolder.mInstaPlayView.setVisibility(View.VISIBLE);
        viewHolder.mInstaPlayView.setIconsColor(R.color.white);
        viewHolder.mInstaPlayView.setVideoQualitySelectionText(InstaPlayView.VIDEO_QUALITY_TEXT_HEIGHT, SORT_ORDER_ASCENDING);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            viewHolder.mInstaPlayView.setFullScreen(true);

        try {
            // Additional view added to player skin, as they have to be shown behind player icons
            mPlayerChannelLogoView = viewHolder.mInstaPlayView.getInstaViewRef().findViewById(R.id.channel_logo);
            mPlayerLiveTag = viewHolder.mInstaPlayView.getInstaViewRef().findViewById(R.id.premium_tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!TextUtils.isEmpty(CHANNEL_LOGO))
            Picasso.get().load(CHANNEL_LOGO).into(mPlayerChannelLogoView);
        mPlayerChannelLogoView.setVisibility(View.INVISIBLE);

        mPlayerChannelLogoView.setVisibility(View.GONE);
    }

    public void vidgyorPlayerrelease() {
        try {
            if (player != null && Constants.FULLSCREENFLAG) {
                player.closeFullScreenMode(getActivity(), viewHolder.playerView, viewHolder.playerView.getOverlayFrameLayout());
                Constants.FULLSCREENFLAG = false;

            } else {
                isPlayerReleased = true;
                if (player != null) {
                    player.release();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // getActivity().finishAndRemoveTask();
                }
                VidgyorNetworkManager.from(getActivity()).stop();
                //super.onBackPressed();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class EventListener implements InstaPlayView.PlayerEventListener,
            InstaPlayView.PlayerUIListener, InstaPlayView.MediaTrackEventListener {

        @Override
        public void OnError(int i, String s) {
            mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.error);
            Log.e(TAG, "OnError: " + s);
        }

        @Override
        public void OnCompleted() {
            mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.finish);

            mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY_COMPLETE, currentItem.getData().getLanguage(),
                    MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                    viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                    PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
            mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

            checkAccessControll();
        }

        @Override
        public void OnStateChanged(int i) {

        }

        @Override
        public void OnTimeInfo(long l) {

        }

        @Override
        public void OnPositionChanged(long l, long l1) {

        }

        @Override
        public void OnPlay() {

            if (IS_TEMP_PAUSED) {
//                Log.e(TAG, "OnPlay: LIVE ");
                IS_TEMP_PAUSED = false;
                setupPlayContent(adaptiveURL);
                viewHolder.mInstaPlayView.play();
                return;
            }

            mAnalytics.reportVideoLaunchTime();

//            mPlayerChannelLogoView.setVisibility(View.VISIBLE);
            viewHolder.premium.setVisibility(View.GONE);

            assert getArguments() != null;
            String theme = getArguments().getString(Constants.THEME);
            if (!TextUtils.isEmpty(theme) && theme.equalsIgnoreCase("live"))
                mPlayerLiveTag.setVisibility(View.VISIBLE);
            else
                mPlayerLiveTag.setVisibility(View.GONE);

            if (getActivity() == null)
                return;

            String userId = PreferenceHandler.getAnalyticsUserId(getActivity());
            String userAge = PreferenceHandler.getUserAge(getActivity());
            String userGender = PreferenceHandler.getUserGender(getActivity());
            String userPeriod = PreferenceHandler.getUserPeriod(getActivity());
            String userPackName = PreferenceHandler.getPackName(getActivity());
            String userPlanType = PreferenceHandler.getUserPlanType(getActivity());


            updateUserState();


            try {

                titleEvents = new TitleEvents(categoryName,categoryID,"","",titleName,contentType,titleImage,titleID);
                webEngageAnalytics.titleStartedEvent(titleEvents);
                mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, "Video",
                        "InstaPlay", analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(),
                        currentItem.getData().getCatalogObject().getPlanCategoryType() + "",
                        currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0),
                        currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "",
                        userAge, userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId,
                        viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(), "");
                if (BUFFER_STARTED) {
                    mAnalytics.updateBufferDiff(Calendar.getInstance().getTimeInMillis());
                    BUFFER_STARTED = false;
                    double buff = mAnalytics.updateBufferDiff(Calendar.getInstance().getTimeInMillis());
                    int buffertime = (int) (Calendar.getInstance().getTimeInMillis() - (int) bufferStartTime);
                    double buff_sec = (buffertime / 1000.0);
                    mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, buff_sec, Constants.EVENT_TYPE_BUFFER, currentItem.getData().getLanguage(),
                            MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                            viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                            PreferenceHandler.getUserState(getActivity()), userPeriod, userPlanType, userId, "", Constants.CONTENT_OWNER_VALUE, "");
                    mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);
                }

                mAnalytics.setmVideoStartTime(Calendar.getInstance().getTime());

                if (!IS_PLAY_EVENT_FIRED) {
                    mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
                    startActiveMediaTimer(LIVETV_TIME_LIMIT - viewHolder.mInstaPlayView.getCurrentPosition(), ANALYTICS_ACTIVE_INTERVAL);
                    IS_PLAY_EVENT_FIRED = true;
                    mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY, currentItem.getData().getLanguage(),
                            MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                            viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                            PreferenceHandler.getUserState(getActivity()), userPeriod, userPlanType, userId, "", Constants.CONTENT_OWNER_VALUE, "");
                    mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);
                }
            } catch (Exception e) {

            }

            startAnalyticsTimer(LIVETV_TIME_LIMIT - viewHolder.mInstaPlayView.getCurrentPosition(), Constants.ANALYTICS_INTERVAL);


            /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, Constants.ANALYTICS_INTERVAL);*/


        }


        @Override
        public void OnPaused() {
            if (mCountdown != null && mAnalytics != null) {
                mCountdown.cancel();
                mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.pause);
                Date currentTime = Calendar.getInstance().getTime();
                mAnalytics.setmPageStartTime(currentTime);

                IS_TEMP_PAUSED = true;
            }
            if(handlerAds!=null){
                handlerAds.removeCallbacks(runnableAds);
            }
        }

        @Override
        public void OnBuffering() {
            BUFFER_STARTED = true;
            mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.buffering);
            mAnalytics.updateBufferStartTime(Calendar.getInstance().getTimeInMillis());
            bufferStartTime = Calendar.getInstance().getTimeInMillis();
        }

        @Override
        public void OnReady() {

        }

        @Override
        public void OnCastConnected() {
            IS_CAST_CONNECTED = true;
            if (getActivity() != null)
                (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

        @Override
        public void OnCastDisconnected() {
            IS_CAST_CONNECTED = false;
            if (getActivity() != null)
                (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        @Override
        public void OnMuteStateChanged(boolean b) {

        }

        @Override
        public void OnUIPlay() {

        }

        @Override
        public void OnUIPause() {

        }

        @Override
        public void OnUIFullScreen() {
            final int orientation = getResources().getConfiguration().orientation;

            switch (orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    if (getActivity() != null)
                        (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    if (getActivity() != null)
                        (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
            }

            Helper.resetToSensorOrientation(getActivity());
        }

        @Override
        public void OnUIReplay() {
            setupPlayContent(adaptiveURL);
            viewHolder.mInstaPlayView.play();
        }

        @Override
        public void OnUISeek() {

        }

        @Override
        public void OnUiTouch(boolean b) {

        }

        @Override
        public void OnUIShown() {
            if (getActivity() == null)
                return;

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                viewHolder.mPlayerTitleView.setVisibility(View.VISIBLE);
            else
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);

            try {
                if (getActivity() instanceof MainActivity) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void OnUIHidden() {
            viewHolder.mPlayerTitleView.setVisibility(View.GONE);
            try {
                if (getActivity() instanceof MainActivity) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void OnVideoTracks(List<VideoTrack> list) {

        }

        @Override
        public void OnVideoTrackChanged(VideoTrack videoTrack) {

            Constants.CURRENT_BITRATE = videoTrack.bitrate;
        }

        @Override
        public void OnAudioTracks(List<AudioTrack> list) {

        }

        @Override
        public void OnAudioTrackChanged(AudioTrack audioTrack) {

        }

        @Override
        public void OnCaptionTracks(List<CaptionTrack> list) {

        }

        @Override
        public void OnCaptionTrackChanged(CaptionTrack captionTrack) {

        }
    }

    private static CountDownTimer mCountdown;


    private void startActiveMediaTimer(long l, long analytics_active_interval) {
//        if (mActiveCountDown != null) {
//            mActiveCountDown.cancel();
//            mActiveCountDown = null;
//        }
//
////        l = analytics_active_interval;  // as they want it only once per video
//        mActiveCountDown = new CountDownTimer(l, analytics_active_interval) {
//            @Override
//            public void onTick(long l) {
//                //     Log.d(TAG, "onTick: mediaactive");
//                mAnalytics.updateMediaActive(viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "");
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();


        if (IS_MEDIA_ACTIVE_SENT)
            return;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewHolder.mInstaPlayView != null && viewHolder.mInstaPlayView.isPlaying()) {
                    mAnalytics.updateMediaActive(viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "");
                    IS_MEDIA_ACTIVE_SENT = true;
                }

            }
        }, analytics_active_interval);
    }

    private void startAnalyticsTimer(long timeFuture, long interval) {
        if (mCountdown != null) {
            mCountdown.cancel();
            mCountdown = null;
        }

        mCountdown = new CountDownTimer(timeFuture, interval) {
            @Override
            public void onTick(long l) {
                if (viewHolder.mInstaPlayView.isPlaying()) {
//                    Log.d(TAG, "!Run method ");

                    mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 30, Constants.EVENT_TYPE_PLAYBACK_TIME, currentItem.getData().getLanguage(),
                            MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                            viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                            PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                    mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);
                    analytics_time_spent = analytics_time_spent + 10;
                    mAnalytics.updateMedia(MEDIA_TYPE, AnalyticsProvider.EventName.seek, analytics_time_spent + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", getResources().getConfiguration().orientation == 1 ? "1" : "0", viewHolder.mInstaPlayView);
                    //  mAnalytics.updateMedia(MEDIA_TYPE, AnalyticsProvider.EventName.seek,viewHolder.instaplay.getCurrentPosition() / 1000 + "", viewHolder.instaplay.getCurrentPosition() / 1000 + "", getResources().getConfiguration().orientation == 1 ? "1" : "0");
                    titleEvents = new TitleEvents(categoryName,categoryID,"","",titleName,contentType,titleImage,titleID,viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "",currentItem.getData().getDurationString(),"");
                    webEngageAnalytics.titleProgressEvent(titleEvents);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCastContext.addCastStateListener(mCastStateListener);
        if (viewHolder.mInstaPlayView != null)
            viewHolder.mInstaPlayView.resumeSuspend();
    }

    private void showUI() {
        viewHolder.instaContainer.setVisibility(View.VISIBLE);
        viewHolder.scroll_layout.setVisibility(View.VISIBLE);
    }

    private void showEmptyMessage() {
        viewHolder.instaContainer.setVisibility(View.GONE);
        viewHolder.error_layout.setVisibility(View.VISIBLE);
        viewHolder.scroll_layout.setVisibility(View.GONE);
    }

    private void updateUI(String key, ArrayList value) {
        if (value != null && value.size() > 0) {
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.meta_data_item, viewHolder.parentPanel, false);
            GradientTextView keyView = inflate.findViewById(R.id.key);
            GradientTextView valueView = inflate.findViewById(R.id.value);
            keyView.setText(key);
            valueView.setText(TextUtils.join(",", value));
            viewHolder.meta_data_holder.addView(inflate);
        }
    }

    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {
        if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
                Picasso.get().load(layoutDbScheme.getImageUrl()).into(viewHolder.category_back_img);
                viewHolder.category_grad_back.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            } else {
                Picasso.get().load(R.color.white).into(viewHolder.category_back_img);
                viewHolder.category_grad_back.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        PAGEINDEX = 0;
        if (mOnEventListener != null) {
            mOnEventListener.onEvent(true);
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mCountdown != null && mAnalytics != null) {
            mCountdown.cancel();
            mCountdown = null;
            analytics_time_spent = 0;
        }
        if (mActiveCountDown != null) {
            mActiveCountDown.cancel();
            mActiveCountDown = null;
        }

        if (viewHolder.mInstaPlayView != null)
            viewHolder.mInstaPlayView.suspend();

        if (!IS_CAST_CONNECTED && viewHolder.mInstaPlayView != null) {
            viewHolder.mInstaPlayView.stop();
            viewHolder.mInstaPlayView.release();
        }

    }

    @Override
    public void onDestroy() {
        if (!IS_CAST_CONNECTED && viewHolder != null && viewHolder.mInstaPlayView != null) {
            viewHolder.mInstaPlayView.stop();
            viewHolder.mInstaPlayView.release();
        }
        Constants.FULLSCREENFLAG = false;
        if (nexAdapter != null) {
            nexAdapter.stopAlarmTimer();
        }
        releaseVidPlayer();
        super.onDestroy();
    }

    private void releaseVidPlayer() {
        try {
            VidgyorStatusInit.stopCasting();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Log.d(TAG, "onDestroy");
            try {
                isPlayerReleased = true;
                if (player != null) {
                    player.release();
                    viewHolder.vidRelative.setVisibility(View.GONE);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //getActivity().finishAndRemoveTask();
                }
                VidgyorNetworkManager.from(getActivity()).stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        AnalyticsProvider.sendMediaEventToCleverTap(getCleverTapData());

        IS_TEMP_PAUSED = false;
        if (viewHolder.mInstaPlayView != null) {
            if (viewHolder.mInstaPlayView.getCurrentPosition() > 0 && currentItem != null && currentItem.getData() != null) {
                long pos = viewHolder.mInstaPlayView.getCurrentPosition() / 1000;
                AnalyticsProvider.sendMediaEventToCleverTap(AnalyticsProvider.getCleverTapData(getActivity(), pos, currentItem.getData()), getActivity());
                // mAnalytics.branchMediaClickData(getActivity(), pos);
            }


            if (!IS_CAST_CONNECTED)
                if (viewHolder.mInstaPlayView.isPlaying()) {
                    viewHolder.mInstaPlayView.pause();
//                    IS_PAUSED_TEMP = true;
                }
        }
        Window window = getActivity().getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (viewHolder.mInstaPlayView.isPlaying())
            IS_TEMP_CAST = true;

        try {
            if (player != null)
                player.pausePlayer();
        } catch (Exception ex) {

        }
    }

    public void updateOrientationChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (currentItem != null && currentItem.getData()!=null && currentItem.getData().getPlayUrl() != null
                    && currentItem.getData().getPlayUrl().getVidgyorUrl() != null
                    && !TextUtils.isEmpty(currentItem.getData().getPlayUrl().getVidgyorUrl().getName())) {
                if (player != null) {
                    player.isInLandscape(false, false);
                }
                viewHolder.app_bar_layout.setVisibility(View.VISIBLE);
                viewHolder.scroll_layout.setVisibility(View.VISIBLE);
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                viewHolder.setWidthHeight(viewHolder.vidRelative);
                //Helper.setLightStatusBar(getActivity());
                viewHolder.mInstaPlayView.setResizeMode(InstaPlayView.AspectRatio.RESIZE_MODE_FIT);
                removeFullScreen();
                Constants.FULLSCREENFLAG = false;
            } else {
                viewHolder.app_bar_layout.setVisibility(View.VISIBLE);
                viewHolder.scroll_layout.setVisibility(View.VISIBLE);
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                viewHolder.mInstaPlayView.setFullScreen(false);
                viewHolder.setWidthHeight(viewHolder.instaContainer);
                //Helper.setLightStatusBar(getActivity());
                viewHolder.mInstaPlayView.setResizeMode(InstaPlayView.AspectRatio.RESIZE_MODE_FIT);
                removeFullScreen();
                Constants.FULLSCREENFLAG = false;
                //Helper.clearLightStatusBar(getActivity());
            }

        } else if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            if (currentItem != null && currentItem.getData() != null && currentItem.getData().getPlayUrl().getVidgyorUrl() != null && !TextUtils.isEmpty(currentItem.getData().getPlayUrl().getVidgyorUrl().getName())) {
                viewHolder.app_bar_layout.setVisibility(View.GONE);
                viewHolder.scroll_layout.setVisibility(View.GONE);
                showFullScreen();
                Constants.donoWhyButNeeded(getActivity());
                Constants.FULLSCREENFLAG = true;
                if (player != null) {
                    player.isInLandscape(true, false);
                }

                int navigationHight = Constants.getNavigationHight(getActivity());
                int deviceWidth;
                if (navigationHight > 0) {
                    deviceWidth = Constants.getDeviceWidth(getActivity());
                } else {
                    deviceWidth = Constants.getDeviceWidth(getActivity());
                }
                int deviceHeight = Constants.getDeviceHeight(getActivity());

                ViewGroup.LayoutParams layoutParams = viewHolder.vidRelative.getLayoutParams();
                layoutParams.width = deviceWidth;
                layoutParams.height = deviceHeight;
                viewHolder.vidRelative.setLayoutParams(layoutParams);
            } else {
                viewHolder.app_bar_layout.setVisibility(View.GONE);
                viewHolder.scroll_layout.setVisibility(View.GONE);
                viewHolder.mPlayerTitleView.setVisibility(View.VISIBLE);
                viewHolder.mInstaPlayView.setFullScreen(true);
                viewHolder.mInstaPlayView.setResizeMode(InstaPlayView.AspectRatio.RESIZE_MODE_FIXED_WIDTH);
                showFullScreen();
                Constants.donoWhyButNeeded(getActivity());
                Constants.FULLSCREENFLAG = true;
                int navigationHight = Constants.getNavigationHight(getActivity());
                int deviceWidth;
                if (navigationHight > 0) {
                    deviceWidth = Constants.getDeviceWidth(getActivity());
                } else {
                    deviceWidth = Constants.getDeviceWidth(getActivity());
                }
                int deviceHeight = Constants.getDeviceHeight(getActivity());

                ViewGroup.LayoutParams layoutParams = viewHolder.instaContainer.getLayoutParams();
                layoutParams.width = deviceWidth;
                layoutParams.height = deviceHeight;
                viewHolder.instaContainer.setLayoutParams(layoutParams);
            }
        }

        setChannelLogoSize(orientation);
    }

    private void setChannelLogoSize(int orientation) {
        if (mPlayerChannelLogoView != null) {
            ViewGroup.LayoutParams layoutParams = mPlayerChannelLogoView.getLayoutParams();
            ViewGroup.LayoutParams layoutParamsLive = mPlayerLiveTag.getLayoutParams();


            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutParams.width = (int) getResources().getDimension(R.dimen.px_54);
                layoutParamsLive.width = (int) getResources().getDimension(R.dimen.px_54);
            } else {
                layoutParams.width = (int) getResources().getDimension(R.dimen.px_96);
                layoutParamsLive.width = (int) getResources().getDimension(R.dimen.px_54);
            }

            mPlayerChannelLogoView.setLayoutParams(layoutParams);
            mPlayerChannelLogoView.requestLayout();
        }
    }

    private void removeFullScreen() {
        /*if (getActivity() != null) {
            int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags = flags ^ SYSTEM_UI_FLAG_FULLSCREEN |
                    SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        }*/
        //Helper.setLightStatusBar(getActivity());
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    public void showFullScreen() {
        if (getActivity() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            SYSTEM_UI_FLAG_FULLSCREEN |
                            SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    class ViewHolder {
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
        @BindView(R.id.no_internet_image)
        AppCompatImageView no_internet_image;
        @BindView(R.id.error_go_back)
        GradientTextView error_go_back;
        @BindView(R.id.no_int_container)
        LinearLayout no_int_container;
        @BindView(R.id.error_layout)
        RelativeLayout error_layout;
        @BindView(R.id.instaplay)
        InstaPlayView mInstaPlayView;
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.vid_image)
        ImageView vidImage;
        @BindView(R.id.play_icon)
        ImageView mPlayIcon;
        @BindView(R.id.premium)
        ImageView premium;
        @BindView(R.id.player_container)
        RelativeLayout instaContainer;
        @BindView(R.id.vid_player_view)
        PlayerView playerView;
        @BindView(R.id.vidgyor_container)
        RelativeLayout vidContainer;
        @BindView(R.id.vid_relative)
        RelativeLayout vidRelative;
        @BindView(R.id.title)
        GradientTextView title;
        @BindView(R.id.meta_data)
        GradientTextView meta_data;
        @BindView(R.id.view_count)
        GradientTextView view_count;
        @BindView(R.id.livecount)
        MyTextView livecount;
        @BindView(R.id.description)
        GradientTextView description;
        @BindView(R.id.gradient_shadow)
        TextView mDesGradient;
        @BindView(R.id.meta_data_holder)
        LinearLayout meta_data_holder;
        @BindView(R.id.downArrow)
        AppCompatImageView downArrow;
        @BindView(R.id.share)
        LinearLayout share;
        @BindView(R.id.others)
        GradientTextView mOthersTitle;
        @BindView(R.id.other_channels_recycler_view)
        RecyclerView other_channels_recycler_view;
        @BindView(R.id.comming_soon)
        MyTextView comming_soon;
        @BindView(R.id.coming_soon_layout)
        RelativeLayout coming_soon_layout;
        @BindView(R.id.scroll_layout)
        ScrollView scroll_layout;
        @BindView(R.id.parentPanel)
        LinearLayout parentPanel;
        @BindView(R.id.downArrowHolder)
        LinearLayout downArrowHolder;
        @BindView(R.id.play_next_recycler_view)
        RecyclerView nextPlayRecyclerView;
        @BindView(R.id.play_next)
        GradientTextView playNext;
        @BindView(R.id.others_parent_lyt)
        LinearLayout mOthersParentLyt;

        //        Landscape player back and title view
        @BindView(R.id.player_title_view)
        LinearLayout mPlayerTitleView;
        @BindView(R.id.player_back_btn)
        ImageView mPlayerBackBtn;
        @BindView(R.id.player_title_txt)
        TextView mPlayerTitleTxt;

        void setWidthHeight(RelativeLayout layout) {
            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            int deviceWidth = Constants.getDeviceWidth(layout.getContext());
            int height = (deviceWidth * 9) / 16;
            layoutParams.height = height;
            layoutParams.width = deviceWidth;
            layout.setLayoutParams(layoutParams);
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            close.setVisibility(View.GONE);
            setWidthHeight(instaContainer);
            mOthersTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pauseThePlayer();
                    MoreListingFragment fragment = new MoreListingFragment();
                    Bundle bundle = new Bundle();
                    ListResonse listResonse = othersChannel;
                    //bundle.putParcelable(Constants.ITEMS, listResonse);
                    final String catalog_id = getArguments().getString(Constants.CATALOG_ID);
                    bundle.putString(Constants.CATALOG_ID, catalog_id);
                    bundle.putString(Constants.FROM_PAGE, TAG);
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, plainCategory);
                    bundle.putString(Constants.THEME, getArguments().getString(Constants.THEME));
                    bundle.putString(Constants.LAYOUT_TYPE_SELECTED, getArguments().getString(Constants.LAYOUT_TYPE_SELECTED));
                    bundle.putString(Constants.DISPLAY_TITLE, viewHolder.mOthersTitle.getText().toString());
                    bundle.putString(Constants.LAYOUT_SCHEME, getArguments() == null ? "all" : getArguments().getString(Constants.LAYOUT_SCHEME));
                    fragment.setArguments(bundle);
                    Helper.addFragment(getActivity(), fragment, MoreListingFragment.TAG);
                }
            });
            error_go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
            mPlayIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 10/10/18 as per first release we dont have asscess control
                    //play();

                    if (viewHolder.mInstaPlayView != null && !TextUtils.isEmpty(viewHolder.mInstaPlayView.getCastingMedia())) {
                        if (viewHolder.mInstaPlayView.getCastingMedia().equalsIgnoreCase(adaptiveURL))
                            return;
                    }

                    checkAccessControll();
                    sendFirstPlayAnalyticsEvent();

                }
            });

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isExpanded) {
                        isExpanded = false;
                        description.setMaxLines(2);
                        meta_data_holder.setVisibility(View.GONE);
                        mDesGradient.setVisibility(View.VISIBLE);
                        RotateAnimation expandAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        expandAnim.setDuration(200);
                        expandAnim.setInterpolator(new LinearInterpolator());
                        expandAnim.setFillEnabled(true);
                        expandAnim.setFillAfter(true);
                        downArrow.startAnimation(expandAnim);
                    } else {
                        isExpanded = true;
                        description.setMaxLines(1000);
                        meta_data_holder.setVisibility(View.VISIBLE);
                        mDesGradient.setVisibility(View.GONE);
                        RotateAnimation minAnim = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        minAnim.setDuration(200);
                        minAnim.setInterpolator(new LinearInterpolator());
                        minAnim.setFillEnabled(true);
                        minAnim.setFillAfter(true);
                        downArrow.startAnimation(minAnim);
                    }
                }
            });

            mPlayerBackBtn.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        }


    }

    private void setMoreInTextColor(LayoutDbScheme layoutDbScheme) {
        if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getStartColor()) && !TextUtils.isEmpty(layoutDbScheme.getEndColor())) {
                int startColor = Color.parseColor("#" + layoutDbScheme.getStartColor());
                int endColor = Color.parseColor("#" + layoutDbScheme.getEndColor());
            }
        }


    }

    private void sendFirstPlayAnalyticsEvent() {
        if (getActivity() != null) {
            try {
                String userId = PreferenceHandler.getAnalyticsUserId(getActivity());
                String userAge = PreferenceHandler.getUserAge(getActivity());
                String userPeriod = PreferenceHandler.getUserPeriod(getActivity());
                String userPackName = PreferenceHandler.getPackName(getActivity());
                String userPlanType = PreferenceHandler.getUserPlanType(getActivity());
                String userGender = PreferenceHandler.getUserGender(getActivity());

                updateUserState();
                mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, "Video",
                        "InstaPlay", analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(), currentItem.getData().getCatalogObject().getPlanCategoryType() + "", currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0), currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", userAge, userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId, viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(), "");
                mAnalytics.firstPlayClick();

            } catch (Exception e) {

            }
        }
    }


    private void checkAccessControll() {
        if (currentItem != null) {
            Log.e("TestingHere", "testing");
            Data data = currentItem.getData();
            if (data != null) {
                AccessControl accessControl = data.getAccessControl();
                boolean isAccessLoginReq = accessControl.getLoginRequired();
//                boolean isAccessLoginReq = true;
                boolean isAccessFree = accessControl.getIsFree();

                if (isAccessLoginReq) {
                    boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
                    if (loggedIn) {
                        if (!isAccessFree) {
                            if (IS_SUBSCRIBED) {
                                play();  // Playing for now
                            } else {

                                if (Helper.getCurrentFragment(getActivity()) != null
                                        && Helper.getCurrentFragment(getActivity()) instanceof LiveTvDetailsFragment)
                                    viewHolder.mInstaPlayView.pause();
                                showLoginPopUp(true);

                            }
                        } else {

                            play();
                        }
                    } else {

                        if (viewHolder.mInstaPlayView.isPlaying()) {
                            viewHolder.mInstaPlayView.pause();
                        } else if (viewHolder.mInstaPlayView != null) {
                            viewHolder.mInstaPlayView.pause();
                        }
                        if (Helper.getCurrentFragment(getActivity()) != null
                                && Helper.getCurrentFragment(getActivity()) instanceof MePageFragment) {

                        } else {
                            showLoginPopUp(false);
                        }

                    }

                } else if (!isAccessFree) {
                    if (IS_SUBSCRIBED)
                        play();         // Playing for now
                    else {

                        if (viewHolder.mInstaPlayView.isPlaying()) {
                            viewHolder.mInstaPlayView.pause();
                        }
                        showLoginPopUp(false);
                    }
                } else {
                    //showLoginPopUp();
                    play();
                }
            }
        }

    }

    private void showLoginPopUp(boolean loginstatus) {

        viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
        viewHolder.mImage.setVisibility(View.VISIBLE);
        viewHolder.vidImage.setVisibility(View.VISIBLE);
        if (bottomSheetFragment != null) {
            if (bottomSheetFragment.getDialog() != null) {
                boolean showing = bottomSheetFragment.getDialog().isShowing();
                if (showing) {
                    return;
                }
            }
        }

        Helper.setToPortAndResetToSensorOrientation(getActivity());
        bottomSheetFragment = new SubscribeBottomSheetDialog();
        bottomSheetFragment.updateStatus(loginstatus);
        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), BottomSheetDialogFragment.class.getSimpleName());
        bottomSheetFragment.setBottomSheetClickListener(new SubscribeBottomSheetDialog.BottomSheetClickListener() {
            @Override
            public void onLoginClicked() {
                goToLoginPage();
            }

            @Override
            public void onSubscribeClick() {
                if (getActivity() != null) {
                    boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
                    if (loggedIn) {
                        //Toast.makeText(getActivity(), "Yet to be Implement, Playing for now", Toast.LENGTH_SHORT).show();
                        //play();    // TODO: Aditya handel
//                    Helper.addFragmentForDetailsScreen(getActivity(),new SubscriptionWebViewFragment(), SubscriptionWebViewFragment.TAG);
                        SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.FROM_WHERE, LiveTvDetailsFragment.TAG);
                        bundle.putBoolean(Constants.IS_LOGGED_IN, true);
                        // bundle.putBoolean(Constants.IS_FROM_DETAIL_PAGE,true);
                        subscriptionWebViewFragment.setArguments(bundle);
                        Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);

                    } else {
                        SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.FROM_WHERE, LiveTvDetailsFragment.TAG);
                        bundle.putBoolean(Constants.IS_LOGGED_IN, false);
                        //   bundle.putBoolean(Constants.IS_FROM_DETAIL_PAGE,true);
                        subscriptionWebViewFragment.setArguments(bundle);
                        Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
                    }
                } else {
                    Log.e(TAG, "onSubscribeClick: " + " how did we end up here ? ");
                }
            }

            @Override
            public void onCancelClick() {
//                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickLogin() {
                goToLoginPage();
            }
        });

        if (Helper.getCurrentFragment(getActivity()) instanceof InternetUpdateFragment) {
            bottomSheetFragment.dismiss();
        }

    }

    private PlayerManager player;
    boolean isPlayerReleased = false;

    private void play() {

        Data dataToCheckPlayer = currentItem.getData();
        Log.e("TestingHere", "");

        titleEvents = new TitleEvents(categoryName,categoryID,"","",titleName,contentType,titleImage,titleID);
        webEngageAnalytics.titlePlayedEvent(titleEvents);
        if (dataToCheckPlayer.getPlayUrl() != null && dataToCheckPlayer.getPlayUrl().getVidgyorUrl() != null && !TextUtils.isEmpty(dataToCheckPlayer.getPlayUrl().getVidgyorUrl().getName())) {
            /* Log.e("TestingHere", dataToCheckPlayer.getPlayUrlType());*/
            viewHolder.vidRelative.setVisibility(View.VISIBLE);
            viewHolder.instaContainer.setVisibility(View.GONE);
            viewHolder.playerView.setVisibility(View.VISIBLE);
            viewHolder.vidImage.setVisibility(View.GONE);
            player = new PlayerManager(getActivity(), viewHolder.playerView, dataToCheckPlayer.getPlayUrl().getVidgyorUrl().getName());
            if (player != null) {
                player.isInLandscape(false, false);
                player.resumePlayer();
                Log.e("TestingHere", "play");
            }
        } else {
            viewHolder.playerView.setVisibility(View.GONE);
            viewHolder.instaContainer.setVisibility(View.VISIBLE);
            if (!viewHolder.mInstaPlayView.isPlayerActive()) {
                if (!TextUtils.isEmpty(adaptiveURL)) {
                    setupPlayContent(adaptiveURL);
                    viewHolder.mPlayIcon.setVisibility(View.GONE);
                    viewHolder.mImage.setVisibility(View.GONE);
                    viewHolder.mInstaPlayView.play();
                }
            } else if (!TextUtils.isEmpty(adaptiveURL)) {

                viewHolder.mInstaPlayView.play();
                viewHolder.mPlayIcon.setVisibility(View.GONE);
                viewHolder.mImage.setVisibility(View.GONE);
                String theme = getArguments().getString(Constants.THEME);
                if (!TextUtils.isEmpty(theme) && theme.equalsIgnoreCase("live")) {
                    viewHolder.premium.setVisibility(View.GONE);
                }

                Picasso.get().load(R.drawable.live_rec).into(viewHolder.premium);
            } else {
                Helper.showToast(getActivity(), "Unable to Play! , Please try after some time", R.drawable.ic_error_icon);
            }
        }
    }

    private void goToLoginPage() {
        try {
            Intent intent = new Intent(getActivity(), OnBoardingActivity.class);
            intent.putExtra(Constants.FROM_PAGE, MovieDetailsFragment.TAG);
            startActivityForResult(intent, Constants.ACTION_LOGIN_CLICKED);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public InstaPlayView getPlayerInfo() {

        if (getActivity() != null && viewHolder != null && viewHolder.mInstaPlayView != null) {
            return viewHolder.mInstaPlayView;
        }
        return null;
    }

    public void pauseThePlayer() {
        if (IS_CAST_CONNECTED)
            return;

        if (player != null){
            player.pausePlayer();
        }
        if (viewHolder.mInstaPlayView != null) {
            viewHolder.mInstaPlayView.pause();
            viewHolder.mInstaPlayView.stop();
            viewHolder.mInstaPlayView.release();
            viewHolder.mImage.setVisibility(View.VISIBLE);
            viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
            if (currentItem != null && currentItem.getData() != null && currentItem.getData().getTheme().equalsIgnoreCase("live"))
                viewHolder.premium.setVisibility(View.VISIBLE);
        }
        if (player != null) {
            viewHolder.vidImage.setVisibility(View.VISIBLE);
        }
    }

    //   Data for checking subscription status -- responseData for smartURL
    private void getUserStates(ListResonse data, Data responseData) {
        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
        if (!loggedIn) {
            //showAds();
            isAdAvailableOnGoogle =true;
            return;
        }

        IS_MEDIA_ACTIVE_SENT = false;
        IS_PLAY_EVENT_FIRED = false;

        String sessionId = PreferenceHandler.getSessionId(getActivity());
        final String content_id = data.getData().getContentId();
        final String catalog_id = data.getData().getCatalogId();
        String category = data.getData().getCatalogObject().getPlanCategoryType();
        mApiService.getAllPlayListDetails(sessionId, catalog_id, content_id, category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PlayListResponse>() {
                    @Override
                    public void call(PlayListResponse playListResponse) {
                        if (playListResponse != null) {
                            Data response = playListResponse.getPlayListResponse();
                            if (response != null) {

                                IS_SUBSCRIBED = response.isSubscribed();
                                getSmartURL(responseData);    // Get_All_userdata response
                                if (!IS_SUBSCRIBED) {
                                    //showAds();
                                    isAdAvailableOnGoogle =true;
                                }
//                                PreferenceHandler.setIsSubscribed(getActivity(), response.isSubscribed());

                                UserInfo userInfo = response.getUserInfo();

                                if (userInfo != null) {
                                    String age = userInfo.getAge();
                                    if (age != null && !TextUtils.isEmpty(age)) {
                                        PreferenceHandler.setUserAge(getActivity(), age);
                                    }

                                    String analyticsUserId = userInfo.getAnalyticsUserId();
                                    if (analyticsUserId != null && !TextUtils.isEmpty(analyticsUserId)) {
                                        PreferenceHandler.setAnalyticsUserId(getActivity(), analyticsUserId);
                                    }

                                    String gender = userInfo.getGender();
                                    if (gender != null && !TextUtils.isEmpty(gender)) {
                                        PreferenceHandler.setUserGender(getActivity(), gender);
                                    }

                                    String userPeriod = userInfo.getUserPeriod();
                                    if (userPeriod != null && !TextUtils.isEmpty(userPeriod)) {
                                        PreferenceHandler.setUserPeriod(getActivity(), userPeriod);
                                    } else {
                                        PreferenceHandler.setUserPeriod(getActivity(), "");
                                    }

                                    String userPackName = userInfo.getUserPackName();
                                    if (userPackName != null && !TextUtils.isEmpty(userPackName)) {
                                        PreferenceHandler.setUserPackName(getActivity(), userPackName);
                                    } else {
                                        PreferenceHandler.setUserPackName(getActivity(), "");
                                    }

                                    String userPlanType = userInfo.getUserPlanType();
                                    if (userPlanType != null && !TextUtils.isEmpty(userPlanType)) {
                                        PreferenceHandler.setUserPlanType(getActivity(), userPlanType);
                                    } else {
                                        PreferenceHandler.setUserPlanType(getActivity(), "");
                                    }

                                    if (userPeriod != null && userPeriod.equalsIgnoreCase("unsubscribed")) {
                                        PreferenceHandler.setSVODActive(getActivity(), false);
                                    }

                                    updateUserState();


                                    mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, "InstaPlay", analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(), currentItem.getData().getCatalogObject().getPlanCategoryType() + "", currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0), currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", age, gender, USER_STATE, userPeriod, userPlanType, userPackName, analyticsUserId, viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(), "");
                                    fireScreenView();
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        DataError errorRes = Constants.getErrorMessage(throwable);
                        String errorMessage = errorRes.getError().getMessage();
                        int errorCode = errorRes.getError().getCode();
                        if (getActivity() != null)
                            if (errorCode == 1016 && ((retrofit2.adapter.rxjava.HttpException) throwable).code() == 422) {
                                Helper.clearLoginDetails(getActivity());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(new Intent(intent));
                                getActivity().finish();
                                Helper.showToast(getActivity(), errorMessage, R.drawable.ic_error_icon);
                                //ViewModelProviders.of(getActivity()).get(SearchPageViewModel.class).deleteAllSearHistory();
                                Helper.deleteSearchHistory(getActivity());
                            } else {
                                getSmartURL(responseData);    // Get_All_userdata response Error
                            }

                    }
                });
    }

    private void updateUserState() {
        if (getActivity() != null) {
            boolean isSubscribed = PreferenceHandler.getIsSubscribed(getActivity());
            if (isSubscribed) {
                USER_STATE = "subscribed";
            } else if (PreferenceHandler.isLoggedIn(getActivity())) {
                USER_STATE = "registered";
            } else {
                USER_STATE = "anonymous";
            }
        }
    }

    public void fireScreenView() {
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.LinearAndLiveScreen);
        pageEvents = new PageEvents(Constants.LiveTvScreen);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }


    public interface OnEventListener {
        void onEvent(boolean b);
    }

    private static OnEventListener mOnEventListener;
    private boolean isAdAvailableOnGoogle = false;
    public static void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    Handler handlerAds = new Handler();
    Runnable runnableAds = new Runnable() {
        @Override
        public void run() {
            if (mAdmanagerInterstitialAd != null && isAdAvailableOnGoogle) {
                mAdmanagerInterstitialAd.show(getActivity());
            } else {
                Log.d("Googleadloaded", "The interstitial ad wasn't ready yet.");
            }
        }
    };

    private void showAds() {
        handlerAds.postDelayed(runnableAds, Constants.ADS_WAITTIME);
    }
}
