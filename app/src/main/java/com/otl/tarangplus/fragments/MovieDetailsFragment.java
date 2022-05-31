package com.otl.tarangplus.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.ads.mediationtestsuite.utils.AdManager;
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
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.inmobi.ads.AdMetaInfo;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.ads.listeners.InterstitialAdEventListener;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.BroadcastReciver.NetworkChangeReceiver;
import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.FullscreenActivity;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.adapters.MoreInMovieThemeAdapter;
import com.otl.tarangplus.adapters.MoreInShowThemeAdapter;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AccessControl;
import com.otl.tarangplus.model.AddPlayListItems;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.GenericResult;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.Listitem;
import com.otl.tarangplus.model.MediaplaybackEvents;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.PlayList;
import com.otl.tarangplus.model.PlayListItem;
import com.otl.tarangplus.model.PlayListResponse;
import com.otl.tarangplus.model.Preview;
import com.otl.tarangplus.model.ShowDetailsResponse;
import com.otl.tarangplus.model.TitleEvents;
import com.otl.tarangplus.model.UserInfo;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.DetailsPageViewModel;
import com.saranyu.ott.instaplaysdk.InstaCastItem;
import com.saranyu.ott.instaplaysdk.InstaMediaItem;
import com.saranyu.ott.instaplaysdk.InstaPlayView;
import com.saranyu.ott.instaplaysdk.ads.Adds;
import com.saranyu.ott.instaplaysdk.mediatracks.AudioTrack;
import com.saranyu.ott.instaplaysdk.mediatracks.CaptionTrack;
import com.saranyu.ott.instaplaysdk.mediatracks.VideoTrack;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlFetcher;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlFetcher2;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlResponseV2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static com.otl.tarangplus.Utils.Constants.HEARTBEAT_RATE;

public class MovieDetailsFragment extends UniversalFragment implements SwipeRefreshLayout.OnRefreshListener {

    //private InMobiInterstitial interstitialAd;
    private InterstitialAd mInterstitialAdGoogle;
    private AdManagerInterstitialAd mAdmanagerInterstitialAd;
    //Trailer
    private Boolean isTrailerAvailable = false;

    @Override
    protected void languageChanged() {
        PAGEINDEX = 0;
        getDetails();
    }

    public static final String TAG = MovieDetailsFragment.class.getName();
    private boolean IS_PLAY_EVENT_FIRED = false;

    Unbinder unbinder;
    private boolean IS_SUBSCRIBED = false;
    private long CUR_HEART_BEAT = 0;
    private long bufferStartTime;
    private boolean IS_PREVIEW_PLAYING = false;
    public boolean IS_PREVIEW_PLAYED = false;
    public boolean IS_FOR_LOGIN = false;

    private DetailsPageViewModel mViewModel;
    private ViewHolder viewHolder;
    private String adaptiveURL = "";
    private String displayTitle;
    private EventListener mEventListener;

    private Analytics mAnalyticsEvent;
    private MediaplaybackEvents mediaplaybackEvents;
    private AppEvents appEvents;

    private ApiService mApiService;
    private ShowDetailsResponse currentItem;

    private AnalyticsProvider mAnalytics;
    private static CountDownTimer mCountdown;
    private GenericResult genericResult;
    private int PAGEINDEX = 0;
    private MoreInShowThemeAdapter itemAdapter;
    private MoreInMovieThemeAdapter movieThemeAdapter;
    private boolean IS_LAST_ELEMENT = false;
    private String MEDIA_TYPE = "Video";
    private long previewStartTime, previewEndTime;
    private boolean isAccessLoginReq, isAccessFree;
    private String USER_STATE;
    public SubscribeBottomSheetDialog bottomSheetFragment;
    private boolean BUFFER_STARTED = false;
    private Adds adds;
    private String ads;
    private boolean IS_EXT_PREV_URL = false;
    private long ANALYTICS_ACTIVE_INTERVAL;
    private static CountDownTimer mActiveCountDown;
    private static String analytics_unique_id;
    private static long analytics_time_spent = 0;
    private static boolean IS_MEDIA_ACTIVE_SENT = false;

    private CastStateListener mCastStateListener;
    private CastContext mCastContext;
    private IntroductoryOverlay mIntroductoryOverlay;
    private MenuItem mediaRouteMenuItem;
    private InstaCastItem mInstaCastItem;
    private static boolean IS_CAST_CONNECTED = false;
    private ImageView mPlayerChannelLogoView;
    private YouTubePlayer YPlayer;
    private YouTubePlayerSupportFragment youTubePlayerFragment = null;
    private FragmentTransaction transaction;
    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;
    private TitleEvents titleEvents;

    private String categoryName;
    private String contentType;
    private String titleImage;
    private String categoryID;
    private String titleName;
    private String titleID;
    private String duration;
    private String currentTime;

    @SuppressLint("SourceLockedOrientationActivity")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details, container, false);
        viewHolder = new ViewHolder(view);
        hideUI();
        MainActivity mainActivity = (MainActivity) getActivity();
        genericResult = new GenericResult();
        RestClient mRestClient = new RestClient(getActivity());
        mApiService = mRestClient.getApiService();
        webEngageAnalytics = new WebEngageAnalytics();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //Helper.clearLightStatusBar(getActivity());
        /*** Analytics **/
        Helper.setLightStatusBar(getActivity());
        mAnalyticsEvent = Analytics.getInstance(getContext());

        //InMobi
      /*  interstitialAd = new InMobiInterstitial(getActivity(), 1598949363916L,
                mInterstitialAdEventListener);*/

        //Google admob
        //AdRequest adRequest = new AdRequest.Builder().build();
        //InterstitialAd.load(getActivity(), BuildConfig.GOOGLEADID, adRequest, null/*interstitialAdLoadCallback*/);
        //((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);

        //Google admanager
        AdManagerAdRequest adManagerRequest = new AdManagerAdRequest.Builder().build();
        AdManagerInterstitialAd.load(getActivity(), BuildConfig.GOOGLEADID, adManagerRequest,
                adManagerInterstitialAdLoadCallback);

        CUR_PROGRESS_TIME = 0;
        mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.setmPageStartTime(Calendar.getInstance().getTime());

        if (mOnEventListener != null) {
            mOnEventListener.onEvent(true);
        }
        Window window = getActivity().getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        mCastStateListener = state -> {
            if (state != CastState.NO_DEVICES_AVAILABLE) {
                showIntroductoryOverlay();
            }

            if (state == CastState.NOT_CONNECTED) {
                IS_CAST_CONNECTED = false;
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else if (state == CastState.CONNECTED) {
                IS_CAST_CONNECTED = true;
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        };
        mCastContext = CastContext.getSharedInstance((MainActivity) getActivity());
        if (mCastContext != null)
            if (mCastContext.getCastState() == 3 || mCastContext.getCastState() == 4) {
                IS_CAST_CONNECTED = true;
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                IS_CAST_CONNECTED = false;
                if (getActivity() != null)
                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    AdManagerInterstitialAdLoadCallback adManagerInterstitialAdLoadCallback = new AdManagerInterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull AdManagerInterstitialAd adManagerInterstitialAd) {
            Log.i(TAG + " ads", "Success");
            mAdmanagerInterstitialAd = adManagerInterstitialAd;
            showAds();
            mAdmanagerInterstitialAd.setFullScreenContentCallback(fullScreenContentCallbackAdManager);

        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.i(TAG + " ads", loadAdError.getMessage());
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

    /*FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
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
    };*/

/*
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
*/

    private void setupYoutube() {
        viewHolder.youtubeFragment.setVisibility(View.VISIBLE);
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        viewHolder.play_icon_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YPlayer.play();
                viewHolder.youtubehide.setVisibility(View.GONE);
                viewHolder.play_icon_youtube.setVisibility(View.GONE);
            }
        });
        viewHolder.play_icon_youtube_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YPlayer.seekToMillis(0);
                viewHolder.youtubehide.setVisibility(View.GONE);
                viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
            }
        });
        youTubePlayerFragment.initialize("${youtubeDeveloperKey}", new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

                if (!wasRestored) {
                    analytics_unique_id = UUID.randomUUID().toString();

                    YPlayer = player;
//                    YPlayer.setFullscreen(true);
//                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    if (currentItem != null && currentItem.getData() != null && currentItem.getData().getPlayUrl().getYoutube() != null
                            && currentItem.getData().getPlayUrl().getYoutube().getUrl() != null) {
                        YPlayer.loadVideo(currentItem.getData().getPlayUrl().getYoutube().getUrl().trim());
                        YPlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                            @Override
                            public void onPlaying() {
                                viewHohttps:
//drive.google.com/file/d/1NxU1pWhniM0cWyVrFhSndst7AVI4mLCt/view?usp=sharinglder.youtubehide.setVisibility(View.GONE);
                                viewHolder.play_icon_youtube.setVisibility(View.GONE);
                                viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onPaused() {
                                viewHolder.youtubehide.setVisibility(View.VISIBLE);
                                viewHolder.play_icon_youtube.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onStopped() {

                            }

                            @Override
                            public void onBuffering(boolean b) {

                            }

                            @Override
                            public void onSeekTo(int i) {

                            }
                        });
                        YPlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {
                                Log.d("YPlayer", "onLoading");
                            }

                            @Override
                            public void onLoaded(String s) {
                                Log.d("YPlayer", "onLoaded");
                            }

                            @Override
                            public void onAdStarted() {

                            }

                            @Override
                            public void onVideoStarted() {
                                Log.d("YPlayer", "onVideoStarted");
                                String userId = PreferenceHandler.getAnalyticsUserId(getActivity());
                                String userAge = PreferenceHandler.getUserAge(getActivity());
                                String userPeriod = PreferenceHandler.getUserPeriod(getActivity());
                                String userPackName = PreferenceHandler.getPackName(getActivity());
                                String userPlanType = PreferenceHandler.getUserPlanType(getActivity());
                                String userGender = PreferenceHandler.getUserGender(getActivity());
                                try {
                                    String playerType = "InstaPlay";
                                    String urlType = currentItem.getData().getPlayUrlType();
                                    if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
                                        playerType = "youtube";
                                    }
                                    titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                                    webEngageAnalytics.titleStartedEvent(titleEvents);

                                    mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY, currentItem.getData().getLanguage(),
                                            MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                                            YPlayer.getDurationMillis() / 1000, currentItem.getData().getTitle(),
                                            PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                                    mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                                    mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, playerType,
                                            analytics_time_spent + "", YPlayer.getDurationMillis() / 1000 + "",
                                            YPlayer.getCurrentTimeMillis() / 1000 + "",
                                            1080 + "",
                                            607 + "",
                                            getResources().getConfiguration().orientation + "", currentItem.getData().getPlayUrl().getYoutube().getUrl().trim(),
                                            currentItem.getData().getLanguage(),
                                            currentItem.getData().getCatalogObject().getPlanCategoryType() + "",
                                            currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0),
                                            currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", userAge,
                                            userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId,
                                            null, currentItem.getData().getContentId(), getActivity(), "");

                                    mAnalytics.setmVideoStartTime(Calendar.getInstance().getTime());

                                    if (!IS_PLAY_EVENT_FIRED) {
                                        mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
                                        IS_PLAY_EVENT_FIRED = true;
                                        Log.d("Onanalytics: play 1", "play");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                duration = String.valueOf(YPlayer.getDurationMillis());
                                currentTime = String.valueOf(YPlayer.getCurrentTimeMillis());
                                startAnalyticsTimer_youtube(YPlayer.getDurationMillis() - YPlayer.getCurrentTimeMillis(), Constants.ANALYTICS_INTERVAL);
                            }

                            @Override
                            public void onVideoEnded() {
                                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                                webEngageAnalytics.titleCompletedEvent(titleEvents);
                                viewHolder.youtubehide.setVisibility(View.VISIBLE);
                                viewHolder.play_icon_youtube_replay.setVisibility(View.VISIBLE);
                                mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.finish);
                                if (mCountdown != null) {
                                    mCountdown.cancel();
                                    mCountdown = null;
                                }
                                mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY_COMPLETE, currentItem.getData().getLanguage(),
                                        MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                                        YPlayer.getDurationMillis() / 1000, currentItem.getData().getTitle(),
                                        PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                                mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                                Log.d("YPlayer", "onVideoEnded");
                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {
                                Log.d("YPlayer", "onError"+errorReason.toString());
                            }
                        });
                    } else {
                        Helper.showToast(getActivity(), "Sorry something went wrong", R.drawable.ic_error_icon);
                        return;
                    }
                    YPlayer.play();
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub

            }
        });
        transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

    }

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

        final String content_id = getArguments().getString(Constants.CONTENT_ID);
        final String catalog_id = getArguments().getString(Constants.CATALOG_ID);
        String displayTitle = getArguments().getString(Constants.DISPLAY_TITLE);

        Helper.showProgressDialog(getActivity());

        String layoutScheme = getArguments().getString(Constants.LAYOUT_SCHEME);
        if (!TextUtils.isEmpty(layoutScheme))
            setTopbarUI(Constants.getSchemeColor(layoutScheme));
        mApiService.getShowDetailsResponse(catalog_id, content_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ShowDetailsResponse>() {
                    @Override
                    public void call(ShowDetailsResponse detailsPageResponse) {
                        currentItem = detailsPageResponse;
                        setWebEngagePrams(currentItem);
                        viewHolder.swipeRefreshLayout.setRefreshing(false);
                        if (detailsPageResponse != null && detailsPageResponse.getData() != null) {
                            Map<String, ArrayList> additionalData = new HashMap<>();
                            viewHolder.metaDataHolder.removeAllViews();
                            Data responseData = detailsPageResponse.getData();

                            genericResult.setShowDetailsResponse(detailsPageResponse);
                            if (responseData != null) {
                                additionalData = responseData.getItemAdditionalData();
                                if (additionalData != null && additionalData.size() > 0) {
                                    for (TreeMap.Entry<String, ArrayList> entry : additionalData.entrySet()) {
                                        String key = entry.getKey();
                                        ArrayList value = entry.getValue();
                                        updateUI(key, value);
                                    }
                                } else {
                                    additionalData = null;
                                }
                            }
                            Helper.dismissProgressDialog();
                            Data data = detailsPageResponse.getData();
                            if (data != null) {
                                String catalogId = data.getCatalogId();
                                List<String> genres = data.getGenres();
                                if (!TextUtils.isEmpty(data.getCatalogId()) && data.getGenres().size() > 0) {
//                                getMoreShows(detailsPageResponse);
                                    Log.d("somethingbugs", catalogId + "  " + genres);
                                    getMoreShowsV2(catalogId, genres);

                                }

                                if (data != null) {
                                    /*if (data.isAssociatedVideos() != null && data.isAssociatedVideos()) {
                                        getAssociatedVideos(data.getCatalogId(), data.getContentId());
                                        viewHolder.mAssociatedViewHolder.setVisibility(View.VISIBLE);
                                    } else {
                                        viewHolder.mAssociatedViewHolder.setVisibility(View.GONE);
                                    }*/

                                    if (data.isPlayType() != null && data.isPlayType()) {
                                        viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
                                        viewHolder.mCommingSoon.setVisibility(View.GONE);
                                    } else {
                                        viewHolder.mPlayIcon.setVisibility(View.GONE);
                                        viewHolder.mCommingSoon.setVisibility(View.VISIBLE);
                                        viewHolder.mPremium.setVisibility(View.GONE);
                                    }
                                }

                            }
                            setUpUI(detailsPageResponse, additionalData, data.isPlayType());
                            showUI();
                            appEvents = new AppEvents(1, "", Constants.MOVIE_DETAILS_PAGE, Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, data.getTitle(),
                                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                            mAnalyticsEvent.logAppEvents(appEvents);
                        } else {
                            Helper.dismissProgressDialog();
                            showEmptyMessage();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        showEmptyMessage();
                    }
                });
    }

    private void setWebEngagePrams(ShowDetailsResponse currentItem) {
        categoryName = currentItem.getData().getCatalogName() != null ? currentItem.getData().getCatalogName() : "";
        categoryID = currentItem.getData().getCatalogId() != null ? currentItem.getData().getCatalogId() : "";
        titleName = currentItem.getData().getDisplayTitle() != null ? currentItem.getData().getDisplayTitle() : "";
        titleID = currentItem.getData().getContentId() != null ? currentItem.getData().getContentId() : "";
        contentType = currentItem.getData().getMediaType() != null ? currentItem.getData().getMediaType() : "";
        titleImage = currentItem.getData().getThumbnails().getLarge169().getUrl() != null ? currentItem.getData().getThumbnails().getLarge169().getUrl() : "";
    }

    private boolean isItemInWatchLater = false;
    private boolean isItemInFavorite = false;
    private String listIdSelected = "";
    private String listIdSelectedFavorite = "";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity) getActivity()).setSelectedNavUI(Constants.TABS.HOME);   // todo TBR
        mViewModel = ViewModelProviders.of(this).get(DetailsPageViewModel.class);
        viewHolder.mMoreItemView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewHolder.mMoreItemView.setLayoutManager(manager);

        viewHolder.mAssociatedItemView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager managers = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewHolder.mAssociatedItemView.setLayoutManager(managers);


        getDetails();
       /* viewHolder.mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                if (currentItem.getData().getPreview() != null && !TextUtils.isEmpty(currentItem.getData().getPreview().getExtPreviewUrl())) {       // External URL should come
                    IS_PREVIEW_PLAYING = true;
                    IS_FOR_LOGIN = false;
                    IS_EXT_PREV_URL = true;

                    getAddsSmartURL(currentItem.getData().getPreview().getExtPreviewUrl());

                } else if (currentItem.getData().getPreview() != null && currentItem.getData().getPreview().isPreviewAvailable()
                        && !TextUtils.isEmpty(currentItem.getData().getPreview().getPreviewStart())
                        && !TextUtils.isEmpty(currentItem.getData().getPreview().getPreviewStart())) {

                    IS_PREVIEW_PLAYING = true;
                    IS_FOR_LOGIN = false;

                    playPreviewContent(currentItem.getData().getPreview());   // On preview button click

                } else {
                    Helper.showToast(getActivity(), "Preview Not Available!", R.drawable.ic_error_icon);
                }
            }
        });*/

        viewHolder.mWatchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.mWatchLater.setEnabled(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.mWatchLater.setEnabled(true);
                    }
                }, 500);

                if (!PreferenceHandler.isLoggedIn(getActivity())) {
//                    Helper.showToast(getActivity(), "please login", R.drawable.ic_cross);
                    showLoginConfirmationPopUp(Constants.WATCH_LATER);
                    return;
                }
                if (isItemInWatchLater) {
                    deleteItemFromWatchLater(Constants.WATCHLATER, listIdSelected);
                } else {
                    addToWatchlater(Constants.WATCHLATER);
                }
            }
        });
        /*viewHolder.mFavoritelin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.mFavoritelin.setEnabled(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.mFavoritelin.setEnabled(true);
                    }
                }, 500);

                if (!PreferenceHandler.isLoggedIn(getActivity())) {
//                    Helper.showToast(getActivity(), "please login", R.drawable.ic_cross);
                    showLoginConfirmationPopUp(Constants.FAVOURITE);
                    return;
                }
                if (isItemInFavorite) {
                    deleteItemFromWatchLater(Constants.FAVOURITE, listIdSelectedFavorite);
                } else {
                    addToWatchlater(Constants.FAVOURITE);
                }
            }
        });
        viewHolder.mPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PreferenceHandler.isLoggedIn(getActivity())) {
//                    Helper.showToast(getActivity(), "please login", R.drawable.ic_cross);
                    showLoginConfirmationPopUp(Constants.PLAYLIST);
                    return;
                }
                BottomsheetDialogFragment bottomSheetDialogFragment = new BottomsheetDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("contentId", currentItem.getData().getContentId());
                bundle.putString("catalogId", currentItem.getData().getCatalogId());
                bundle.putString("category", getArguments().getString(Constants.PLAIN_CATEGORY_TYPE));

                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "BottomsheetDialogFragment");
            }
        });
*/

        viewHolder.mShare.setOnClickListener(new View.OnClickListener() {
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
                    if (loggedIn) {
                        mAnalytics.reportAppShare(shareUrl);
                        appEvents = new AppEvents(1, Constants.SHARE, "", Constants.AN_APP_TYPE, "",
                                Constants.CLICK, currentItem.getData().getTitle(),
                                PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()),
                                PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                        mAnalyticsEvent.logAppEvents(appEvents);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewHolder.mMoreItemView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!viewHolder.mMoreItemView.canScrollHorizontally(1) && !IS_LAST_ELEMENT) {
//                        getDetails();

                        if (currentItem != null) {
                            String catalogId = currentItem.getData().getCatalogId();
                            List<String> genres = currentItem.getData().getGenres();
                            getMoreShowsV2(catalogId, genres);
                        }
                    }

                    if (!viewHolder.mMoreItemView.canScrollHorizontally(1)) {
                    }
                }
            });

            viewHolder.mAssociatedItemView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!viewHolder.mAssociatedItemView.canScrollHorizontally(1) && !IS_LAST_ELEMENT) {
//                        getDetails();

                        if (currentItem != null) {
                            String catalogId = currentItem.getData().getCatalogId();
                            List<String> genres = currentItem.getData().getGenres();
                            getMoreShowsV2(catalogId, genres);
                        }
                    }

                    if (!viewHolder.mAssociatedItemView.canScrollHorizontally(1)) {
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

    private void addToWatchlater(String listName) {
        AddPlayListItems addPlayListItems = new AddPlayListItems();
        Listitem listitem = new Listitem();
        final String content_id = getArguments().getString(Constants.CONTENT_ID);
        final String catalog_id = getArguments().getString(Constants.CATALOG_ID);
        listitem.setCatalogId(catalog_id);
        listitem.setContentId(content_id);
        addPlayListItems.setAuthToken(Constants.API_KEY);
        addPlayListItems.setListitem(listitem);
        mApiService.setWatchLater(PreferenceHandler.getSessionId(getActivity()), listName, addPlayListItems).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        if (listName.equals(Constants.WATCHLATER)) {
                            isItemInWatchLater = true;
                            viewHolder.mWatchlaterImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.otv_orange));
                            appEvents = new AppEvents(1, Constants.WATCH_LATER, "", Constants.AN_APP_TYPE, "", Constants.CLICK, currentItem.getData().getTitle(),
                                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                            mAnalyticsEvent.logAppEvents(appEvents);
                            Helper.showToast(getActivity(), getString(R.string.added_to_watch_list), R.drawable.ic_check);
                            JsonArray data = jsonObject.getAsJsonArray("data");
                            for (JsonElement je : data) {
                                listIdSelected = je.getAsJsonObject().get("listitem_id").getAsString();
                                titleEvents = new TitleEvents(categoryName, categoryID, "", "", displayTitle, contentType, titleImage, titleID);
                            }
                            webEngageAnalytics.watchLaterEvent(titleEvents);

                        } else {
                            //viewHolder.favoriteImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.otv_orange));
                            Helper.showToast(getActivity(), getString(R.string.added_to_favorite_list), R.drawable.ic_check);
                            isItemInFavorite = true;
                            JsonArray data = jsonObject.getAsJsonArray("data");
                            for (JsonElement je : data) {
                                listIdSelectedFavorite = je.getAsJsonObject().get("listitem_id").getAsString();
                            }
                        }
                        Helper.dismissProgressDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();

                        if (listName.equals(Constants.WATCHLATER)) {
                            isItemInWatchLater = false;
                        } else {
                            isItemInFavorite = false;
                        }
                        Helper.dismissProgressDialog();
                        Helper.showToast(getActivity(), Constants.getErrorMessage(throwable).getError().getMessage(), R.drawable.ic_cross);
                    }
                });

    }

    private void deleteItemFromWatchLater(String listName, String listId) {
        Helper.showProgressDialog(getActivity());
        mApiService.removeWatchLaterItem(PreferenceHandler.getSessionId(getActivity()), listName,
                listId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Helper.dismissProgressDialog();
                        if (listName.equals(Constants.WATCHLATER)) {
                            isItemInWatchLater = false;
                            Helper.showToast(getActivity(), "Item deleted from Watch later list", R.drawable.ic_check);
                            viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watchlist_deactive);
                            viewHolder.mWatchlaterImage.setColorFilter(null);
                        } else {
                            isItemInFavorite = false;
                            Helper.showToast(getActivity(), "Item deleted from favorite list", R.drawable.ic_check);
                            //viewHolder.favoriteImage.setImageResource(R.drawable.ic_heart);
                            //viewHolder.favoriteImage.setColorFilter(null);
                        }


                        //  mAnalytics.reportRemoveFromWatchList();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        throwable.printStackTrace();
                        if (listName.equals(Constants.WATCHLATER)) {
                            isItemInWatchLater = true;
                        } else {
                            isItemInFavorite = true;
                        }
                        Helper.dismissProgressDialog();
                        Helper.showToast(getActivity(), Constants.getErrorMessage(throwable).getError().getMessage(), R.drawable.ic_cross);
                    }
                });
    }

    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {
        if (layoutDbScheme != null) {
//            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
//                Picasso.get().load(layoutDbScheme.getImageUrl()).into(viewHolder.mTopbarImage);
//                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
//            } else {
//                Picasso.get().load(R.color.white).into(viewHolder.mTopbarImage);
//                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
//            }
        }
    }

    private void showEmptyMessage() {
        viewHolder.mPlayerContainer.setVisibility(View.GONE);
        viewHolder.mErrorLayout.setVisibility(View.VISIBLE);
        viewHolder.mScrollLayout.setVisibility(View.GONE);
    }

    private void hideUI() {
        viewHolder.mPlayerContainer.setVisibility(View.GONE);
        viewHolder.mScrollLayout.setVisibility(View.GONE);
    }

    private void showUI() {
        viewHolder.mPlayerContainer.setVisibility(View.VISIBLE);
        viewHolder.mScrollLayout.setVisibility(View.VISIBLE);
    }


    public void getMoreShowsV2(String catalogId, List<String> genres) {
        if (!TextUtils.isEmpty(catalogId) && genres.size() > 0) {
            mApiService.getMoreBasedOnGenre(catalogId, genres.get(0), 0)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ListResonse>() {
                        @Override
                        public void call(ListResonse listResonse) {
                            ListResonse detailsPageResponse = listResonse;
                            genericResult.setListResonse(detailsPageResponse);
                            if (detailsPageResponse != null) {
                                updateRecyclerView(detailsPageResponse);
                            }
                            PAGEINDEX++;
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            //Resource.error(throwable.getLocalizedMessage(), throwable);
                        }
                    });
        }
    }

    public void getMoreShows(String catalogId, List<String> genres) {
        if (!TextUtils.isEmpty(catalogId) && genres.size() > 0)

            mViewModel.getMoreBasedOnGenre(catalogId, genres.get(0), PAGEINDEX)
                    .observe(this, new Observer<Resource>() {
                        @Override
                        public void onChanged(@Nullable Resource resource) {
                            switch (resource.status) {

                                case LOADING:
//                                    Helper.showProgressDialog(getActivity());
                                    break;

                                case SUCCESS:

                                    ListResonse detailsPageResponse = (ListResonse) resource.data;
                                    genericResult.setListResonse(detailsPageResponse);
                                    if (detailsPageResponse != null) {
                                        updateRecyclerView(detailsPageResponse);
                                    }
                                    PAGEINDEX++;
//                                    Helper.dismissProgressDialog();
                                    break;

                                case ERROR:
//                                    Helper.dismissProgressDialog();
                                    break;
                            }
                        }
                    });

    }


    private void updateUI(String key, ArrayList value) {
        if (value != null && value.size() > 0) {
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.meta_data_item, viewHolder.parentPanel, false);
            GradientTextView keyView = inflate.findViewById(R.id.key);
            GradientTextView valueView = inflate.findViewById(R.id.value);
            keyView.setText(key);
            valueView.setText(TextUtils.join(",", value));
            // viewHolder.metaDataHolder.addView(inflate);
        }
    }

    private void updateRecyclerView(ListResonse detailsPageResponse) {

        if (detailsPageResponse.getData() != null && detailsPageResponse.getData().getTheme() != null) {
            if (detailsPageResponse.getData().getTheme().equalsIgnoreCase("movie") || detailsPageResponse.getData().getTheme().equalsIgnoreCase("movies")) {
                showMoreInMovieListing(detailsPageResponse);
            } else {
                showMoreInShowListing(detailsPageResponse);
            }
        } else {
            showMoreInShowListing(detailsPageResponse);
        }
    }

    private void showMoreInShowListing(ListResonse detailsPageResponse) {

        if (PAGEINDEX < 1) {
            itemAdapter = new MoreInShowThemeAdapter(getActivity());
            viewHolder.mMoreItemView.setAdapter(itemAdapter);
        }

        try {       // Removing duplicate data
            if (detailsPageResponse != null) {
                for (Iterator<Item> iterator = detailsPageResponse.getData().getItems().iterator(); iterator.hasNext(); ) {
                    Item next = iterator.next();
                    if (next.getContentId().equalsIgnoreCase(currentItem.getData().getContentId())) {
                        iterator.remove();
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (detailsPageResponse != null && itemAdapter != null) {
           /* if (PAGEINDEX < 1) {
                itemAdapter.updateItems(detailsPageResponse.getData().getItems());
                if (detailsPageResponse.getData().getItems().size() < 20) {
                    IS_LAST_ELEMENT = true;
                }
            } else {
                itemAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());
                if (detailsPageResponse.getData().getItems().size() < 20) {
                    IS_LAST_ELEMENT = true;
                }
            }*/
            if (detailsPageResponse.getData().getItems() == null || detailsPageResponse.getData().getItems().size() == 0) {
                viewHolder.linsecond.setVisibility(View.GONE);
                return;
            }
            viewHolder.linsecond.setVisibility(View.VISIBLE);
            itemAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());
            if (detailsPageResponse.getData().getItems().size() + 1 < Constants.PAGE_LIST_LIMIT) {
                IS_LAST_ELEMENT = true;
            }

            itemAdapter.setOnItemClickListener(new MoreInShowThemeAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Item item) {
                    PAGEINDEX = 0;
                    IS_PLAY_EVENT_FIRED = false;
                    Helper.showProgressDialog(getActivity());
                    Bundle bundle = getArguments();
                    bundle.putString(Constants.CONTENT_ID, item.getContentId());
                    bundle.putString(Constants.CATALOG_ID, item.getCatalogId());
                    if (item.getCatalogObject() != null && item.getCatalogObject().getPlanCategoryType() != null)
                        bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
                    bundle.putString(Constants.DISPLAY_TITLE, item.getTitle());
                    if (!TextUtils.isEmpty(item.getCatalogObject().getLayoutScheme()))
                        bundle.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                    //setArguments(bundle);

                    MovieDetailsFragment fragment = new MovieDetailsFragment();
                    fragment.setArguments(bundle);
//            addFragmentForDetailsScreen(fragment, ShowDetailsPageFragment.TAG);
                    if (viewHolder != null) {
                        viewHolder.mScrollLayout.scrollTo(0, 0);
                        if (viewHolder.isExpanded) {
                            viewHolder.isExpanded = false;
                            viewHolder.mDescription.setMaxLines(3);
                            viewHolder.mDesGradient.setVisibility(View.VISIBLE);
                            viewHolder.metaDataHolder.setVisibility(View.GONE);
                            RotateAnimation expandAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            expandAnim.setDuration(200);
                            expandAnim.setInterpolator(new LinearInterpolator());
                            expandAnim.setFillEnabled(true);
                            expandAnim.setFillAfter(true);
                            viewHolder.downArrow.startAnimation(expandAnim);
                        }
                        viewHolder.play_icon_youtube.setVisibility(View.GONE);
                        viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
                        viewHolder.youtubehide.setVisibility(View.GONE);
                        resetPlayerShowImage();
                    }

                    resetPreviewFlags();
                    getDetails();
                }
            });
        }
    }

    private void showMoreInMovieListing(ListResonse detailsPageResponse) {
        if (PAGEINDEX < 1) {
            movieThemeAdapter = new MoreInMovieThemeAdapter(getActivity());
            viewHolder.mMoreItemView.setAdapter(movieThemeAdapter);
        }
        Log.d("detailspagedata", detailsPageResponse.getData().getItems().size() + " ");
        try {       // Removing duplicate data
           /* if (detailsPageResponse != null)
                for (Item element : detailsPageResponse.getData().getItems()) {
                    if (element.getContentId().equalsIgnoreCase(currentItem.getData().getContentId()))
                        detailsPageResponse.getData().getItems().remove(element);
                }*/
            Iterator<Item> iterator = detailsPageResponse.getData().getItems().iterator();
            while (iterator.hasNext()) {
                Item next = iterator.next();
                if (next.getContentId().equalsIgnoreCase(currentItem.getData().getContentId())) {

                    detailsPageResponse.getData().getItems().remove(next);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (detailsPageResponse != null && movieThemeAdapter != null)
            /*if (PAGEINDEX < 1) {
                movieThemeAdapter.updateItems(detailsPageResponse.getData().getItems());
                if (detailsPageResponse.getData().getItems().size() < Constants.PAGE_LIST_LIMIT) {
                    IS_LAST_ELEMENT = true;
                }
            } else {
                movieThemeAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());
                if (detailsPageResponse.getData().getItems().size() < Constants.PAGE_LIST_LIMIT) {
                    IS_LAST_ELEMENT = true;
                }
            }*/

        /**
         * Applicable because we delete one duplicate item above - see recent try block
         */
            if (detailsPageResponse.getData().getItems().size() + 1 < Constants.PAGE_LIST_LIMIT) {
                IS_LAST_ELEMENT = true;
            }

        if (detailsPageResponse.getData().getItems() == null || detailsPageResponse.getData().getItems().size() == 0) {
            viewHolder.linsecond.setVisibility(View.GONE);
            return;
        }
        viewHolder.linsecond.setVisibility(View.VISIBLE);
        movieThemeAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());

        movieThemeAdapter.setOnItemClickListener(new MoreInMovieThemeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                PAGEINDEX = 0;
                IS_PLAY_EVENT_FIRED = false;
                Helper.showProgressDialog(getActivity());
                Bundle bundle = getArguments();
                bundle.putString(Constants.CONTENT_ID, item.getContentId());
                bundle.putString(Constants.CATALOG_ID, item.getCatalogId());
                if (item.getCatalogObject() != null && item.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.DISPLAY_TITLE, item.getTitle());
                if (!TextUtils.isEmpty(item.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                //setArguments(bundle);


                MovieDetailsFragment fragment = new MovieDetailsFragment();
                fragment.setArguments(bundle);
//            addFragmentForDetailsScreen(fragment, ShowDetailsPageFragment.TAG);
                if (viewHolder != null) {
                    viewHolder.mScrollLayout.scrollTo(0, 0);
                    if (viewHolder.isExpanded) {
                        viewHolder.isExpanded = false;
                        viewHolder.mDescription.setMaxLines(3);
                        viewHolder.mDesGradient.setVisibility(View.VISIBLE);
                        viewHolder.metaDataHolder.setVisibility(View.GONE);
                        RotateAnimation expandAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        expandAnim.setDuration(200);
                        expandAnim.setInterpolator(new LinearInterpolator());
                        expandAnim.setFillEnabled(true);
                        expandAnim.setFillAfter(true);
                        viewHolder.downArrow.startAnimation(expandAnim);
                    }
                    viewHolder.play_icon_youtube.setVisibility(View.GONE);
                    viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
                    viewHolder.youtubehide.setVisibility(View.GONE);
                    resetPlayerShowImage();
                }
                CUR_PROGRESS_TIME = 0;
                resetPreviewFlags();
                getDetails();
            }
        });
    }

    private void resetPreviewFlags() {
        IS_PREVIEW_PLAYED = false;
        IS_PREVIEW_PLAYING = false;
        IS_FOR_LOGIN = false;
    }

    private boolean isAdAvailableOnGoogle = false;

    private void setUpUI(ShowDetailsResponse detailsPageResponse, final Map<String, ArrayList> additionalData, boolean isComingSoonVisible) {
        if (detailsPageResponse != null) {
            Data data = detailsPageResponse.getData();
            if (data != null) {

                if (data.getItemCaption() != null) {
                    viewHolder.mMetaData.setVisibility(View.VISIBLE);
                    viewHolder.mMetaData.setText(data.getItemCaption());
                } else {
                    viewHolder.mMetaData.setVisibility(View.GONE);
                }

                if (detailsPageResponse.getData() != null && detailsPageResponse.getData().getPreview() != null
                        && detailsPageResponse.getData().getPreview().isPreviewAvailable()) {
                    isTrailerAvailable = true;
                } else {
                    isTrailerAvailable = false;
                }

                if (data.getGenres().size() > 0) {
                    //viewHolder.mMoreTitle.setText("All Related Videos");
                    viewHolder.mMoreTitle.setText("Recommended");
                } else {
                    viewHolder.mMoreTitle.setVisibility(View.GONE);
                }
                String description = data.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    viewHolder.mDescription.setVisibility(View.VISIBLE);
                    viewHolder.mDescription.setText(description);
                    viewHolder.downArrow.setVisibility(View.VISIBLE);
                } else {
                    //viewHolder.downArrow.setVisibility(View.GONE);
                    viewHolder.mDescription.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(description) && additionalData == null) {
                    viewHolder.downArrow.setVisibility(View.GONE);
                }
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.mImage);
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.youtubehide);


//                if (!IS_CAST_CONNECTED /*|| !viewHolder.mInstaPlayView.isPlaying()*/) {

                if (data.isPlayType() != null && !data.isPlayType()) {
                    viewHolder.mPlayIcon.setVisibility(View.GONE);
                } else
                    viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
                viewHolder.mImage.setVisibility(View.VISIBLE);
//                }


                viewHolder.mSkipPreview.setVisibility(View.GONE);

                if (data.getTitle() != null) {
                    viewHolder.header.setText(data.getTitle());
                    viewHolder.item_title.setText(data.getTitle());
                    viewHolder.mPlayerTitleTxt.setText(data.getTitle());
                    displayTitle = data.getTitle();
                }
                if (PreferenceHandler.isLoggedIn(getActivity())) {
                    getUserStates(data);
                } else {
                    isAdAvailableOnGoogle = true;
                    //showAds();
                    getSmartURL(data);
                }

                if (isTrailerAvailable) {
                    viewHolder.watchtrailer.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.watchtrailer.setVisibility(View.GONE);
                }

                viewHolder.watchtrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean connected = NetworkChangeReceiver.isNetworkConnected(getContext());
                        if (connected) {
                            if (currentItem.getData() != null && currentItem.getData().getPreview() != null && currentItem.getData().getPreview().isPreviewAvailable()) {
                                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                                intent.putExtra("contentId", currentItem.getData().getContentId());
                                intent.putExtra("catalogId", currentItem.getData().getCatalogId());
                                //intent.putExtra(Constants.TITLE, currentItem.getData().getTrailer().get(0).getTitle());
                                intent.putExtra(Constants.PLAY_URL, currentItem.getData().getPreview().getExtPreviewUrl());
                                getActivity().startActivity(intent);
                            }
                        } else {
                            InternetUpdateFragment framgment = new InternetUpdateFragment();
                            Helper.addFragment(getActivity(), framgment, InternetUpdateFragment.TAG);
                        }
                    }
                });

                if (data.getAccessControl() != null) {
                    if (!data.getAccessControl().getIsFree()) {
                        boolean premiumTag = data.getAccessControl().isPremiumTag();
                        if (premiumTag) {
                            if (!IS_CAST_CONNECTED) {
                                if (isComingSoonVisible)
                                    viewHolder.mPremium.setVisibility(View.VISIBLE);
                            }
                        } else {
                            viewHolder.mPremium.setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder.mPremium.setVisibility(View.GONE);
                    }
                }

              /*  if (data.getPreview() != null && data.getPreview().isPreviewAvailable()) {
                        viewHolder.mPreview.setVisibility(View.VISIBLE);
                } else
                    viewHolder.mPreview.setVisibility(View.GONE);*/


                //private String titleID;
                //currentItem

                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                webEngageAnalytics.titleViewedEvent(titleEvents);
            }
        }
    }

    List<PlayListItem> mylistPopup = new ArrayList<>();

    private void getUserStates(Data data) {
        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
        if (!loggedIn) {
            isAdAvailableOnGoogle = true;
            //showAds();
            return;
        }
        Constants.PLAYLISTITEMSFORCURRENTITEM.clear();
        setWatchLaterUI("");
        setFavoriteUI("");
//        CUR_PROGRESS_TIME = 0;
        IS_SUBSCRIBED = false;
        IS_MEDIA_ACTIVE_SENT = false;

        String sessionId = PreferenceHandler.getSessionId(getActivity());
        final String content_id = getArguments().getString(Constants.CONTENT_ID);
        final String catalog_id = getArguments().getString(Constants.CATALOG_ID);
        String category = getArguments().getString(Constants.PLAIN_CATEGORY_TYPE);

        mApiService.getAllPlayListDetails(sessionId, catalog_id, content_id, category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PlayListResponse>() {
                    @Override
                    public void call(PlayListResponse playListResponse) {
                        getSmartURL(data);
                        Constants.PLAYLISTITEMSFORCURRENTITEM.clear();
                        Constants.PLAYLISTITEMSFORCURRENTITEM = Helper.doDeepCopy(Constants.PLAYLISTITEMS);
                        if (playListResponse != null) {
                            Data response = playListResponse.getPlayListResponse();
                            if (response != null) {
                                List<PlayList> playLists = response.getPlayLists();
                                if (playLists != null && playLists.size() > 0) {
                                    //todo update the Playlists items.. :)
                                    for (PlayList playList : playLists) {
                                        if (playList.getName().equalsIgnoreCase(Constants.WATCH_LATER_TXT))
                                            setWatchLaterUI(playList.getListitemId());
                                        if (playList.getName().equalsIgnoreCase(Constants.WATCH_HISTORY_TXT))
                                            CUR_PROGRESS_TIME = Constants.parseTimeToMillis(playList.getPos());
                                        if (playList.getName().equalsIgnoreCase(Constants.FAVOURITE))
                                            setFavoriteUI(playList.getListitemId());

                                    }
                                    Helper helper = new Helper(getActivity());
                                    Constants.PLAYLISTITEMSFORCURRENTITEM.clear();
                                    Constants.PLAYLISTITEMSFORCURRENTITEM = Helper.doDeepCopy(helper.handleCustomLists(playLists));
                                } else {
//                                    Not in WatchHistory or Not in Continue watching
                                    setWatchLaterUI("");
                                    setFavoriteUI("");
                                }

                                IS_SUBSCRIBED = response.isSubscribed();
                                if (!IS_SUBSCRIBED) {
                                    //showAds();
                                    isAdAvailableOnGoogle = true;
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


                                    String urlType = currentItem.getData().getPlayUrlType();
                                    String playerType = "InstaPlay";
                                    if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
                                        playerType = "youtube";
                                        mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, playerType
                                                , analytics_time_spent + "",
                                                10000 + "",
                                                0 + "",
                                                1080 + "",
                                                607 + "",
                                                getResources().getConfiguration().orientation + "", adaptiveURL,
                                                currentItem.getData().getLanguage(),
                                                currentItem.getData().getCatalogObject().getPlanCategoryType() + "",
                                                currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0),
                                                currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", age,
                                                gender, USER_STATE, userPeriod, userPlanType, userPackName, analyticsUserId,
                                                null, currentItem.getData().getContentId(), getActivity(), "");
                                    } else {
                                        mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, playerType
                                                , analytics_time_spent + "",
                                                viewHolder.mInstaPlayView.getDuration() / 1000 + "",
                                                viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "",
                                                viewHolder.mInstaPlayView.getWidth() + "",
                                                viewHolder.mInstaPlayView.getHeight() + "",
                                                getResources().getConfiguration().orientation + "", adaptiveURL,
                                                currentItem.getData().getLanguage(),
                                                currentItem.getData().getCatalogObject().getPlanCategoryType() + "",
                                                currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0),
                                                currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", age,
                                                gender, USER_STATE, userPeriod, userPlanType, userPackName, analyticsUserId,
                                                viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(), "");
                                    }
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
                            if (errorCode == 1016 && ((HttpException) throwable).code() == 422) {
                                Helper.clearLoginDetails(getActivity());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(new Intent(intent));
                                getActivity().finish();
                                Helper.showToast(getActivity(), errorMessage, R.drawable.ic_error_icon);
                                //ViewModelProviders.of(getActivity()).get(SearchPageViewModel.class).deleteAllSearHistory();
                                Helper.deleteSearchHistory(getActivity());
                            } else {
                                getSmartURL(data);
                            }
                    }
                });
    }

    private void setWatchLaterUI(String listItemId) {
        if (!TextUtils.isEmpty(listItemId)) {
            //viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watch_list_sel);
            viewHolder.mWatchlaterImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.otv_orange));
            isItemInWatchLater = true;
            listIdSelected = listItemId;
        } else {
            listIdSelected = "";
            isItemInWatchLater = false;
            viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watchlist_deactive);
            viewHolder.mWatchlaterImage.setColorFilter(null);
        }
    }

    private void setFavoriteUI(String listItemId) {
        if (!TextUtils.isEmpty(listItemId)) {
            //viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watch_list_sel);
            //viewHolder.favoriteImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.otv_orange));
            isItemInFavorite = true;
            listIdSelectedFavorite = listItemId;
        } else {
            listIdSelectedFavorite = "";
            isItemInFavorite = false;
            //viewHolder.favoriteImage.setImageResource(R.drawable.ic_heart);
            //viewHolder.favoriteImage.setColorFilter(null);
        }
    }

    public void getSmartURL(Data currentItem) {
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
                    /*As Ads has to be loaded with media URL performing this action here*/
                    getAdsIfPresent(currentItem);
                    adaptiveURL = Helper.getPlayBackURL(Constants.REGION, IS_SUBSCRIBED, videoUrl);
//                    Needed for analytics
                    analytics_unique_id = UUID.randomUUID().toString();
                    analytics_time_spent = 0;

                    Helper.dismissProgressDialog();

                    if (getArguments() != null && getArguments().getString(Constants.FROM_PAGE) != null &&
                            getArguments().getString(Constants.FROM_PAGE).equals(ContinueWatchingFragment.TAG))
//                        if (!IS_CAST_CONNECTED)
                        checkAccessControl(); // Added for auto play for continue watching
                }
            }

            @Override
            public void onError(Throwable throwable) {
                adaptiveURL = "";
                Helper.dismissProgressDialog();
            }
        });

        urlFetcher.getVideoUrls();
    }

    private void getAdsIfPresent(Data currentItem) {
        if (currentItem != null) {
            AccessControl accessControl = currentItem.getAccessControl();
            if (accessControl.getAdsAvailable()) {
                adds = new Adds(Constants.getPreRoleAd(accessControl),
                        Constants.getPostRoleAd(accessControl),
                        Constants.getMidRoleAd(accessControl),
                        Constants.getMidRoleAdPos(accessControl));


                if (accessControl.getVmap_url() != null) {
                    ads = accessControl.getVmap_url();
                    ads = ads.replace("{baseURL}", BuildConfig.ADS_URL);
                    ads = ads.replace("{token}", Constants.API_KEY);
                }

            }
        }
    }


    boolean state = true;

    private void setupPlayContent(String adaptiveURL) {

//        if (viewHolder.mInstaPlayView != null && !TextUtils.isEmpty(viewHolder.mInstaPlayView.getCastingMedia())) {
//            if (viewHolder.mInstaPlayView.getCastingMedia().equalsIgnoreCase(adaptiveURL))
//                return;
//        }


        if (viewHolder.mInstaPlayView != null && viewHolder.mInstaPlayView.isPlaying())
            resetPlayerShowImage();

        viewHolder.mSkipPreview.setVisibility(View.GONE);
        viewHolder.mInstaPlayView.setCastContext(mCastContext);


        mEventListener = null;
        mEventListener = new EventListener();
        viewHolder.mInstaPlayView.addPlayerEventListener(mEventListener);
        viewHolder.mInstaPlayView.addPlayerUIListener(mEventListener);
        viewHolder.mInstaPlayView.addMediaTrackEventListener(mEventListener);
        viewHolder.mInstaPlayView.addAdEventListener(mEventListener);


        InstaMediaItem mMediaItem;
        if (ads == null || ads.equals("") || IS_SUBSCRIBED || IS_PREVIEW_PLAYING) {
            mMediaItem = new InstaMediaItem(adaptiveURL);
        } else {
            mMediaItem = new InstaMediaItem(adaptiveURL, adds);
        }

        mMediaItem.setInstaCastItem(getCastMediaItem(adaptiveURL));
        viewHolder.mInstaPlayView.setMediaItem(mMediaItem);
        viewHolder.mInstaPlayView.AutoPlayEnable(true);   // Added this to fix Ads playback
        viewHolder.mInstaPlayView.prepare();

        if (!IS_CAST_CONNECTED)
            viewHolder.mInstaPlayView.seekTo(CUR_PROGRESS_TIME);


        viewHolder.mInstaPlayView.setLanguageVisibility(false);
        viewHolder.mInstaPlayView.setCaptionVisibility(true);
        viewHolder.mInstaPlayView.setHDVisibility(false);


        if (IS_PREVIEW_PLAYING) {         // Set start duration based on preview
            viewHolder.mInstaPlayView.seekTo(previewStartTime);
            viewHolder.mInstaPlayView.setSeekBarVisibility(false);
            viewHolder.mInstaPlayView.setCurrentProgVisible(false);
            viewHolder.mInstaPlayView.setDurationTimeVisible(false);
            viewHolder.mInstaPlayView.setForwardTimeVisible(false);
            viewHolder.mInstaPlayView.setRewindVisible(false);
            viewHolder.mInstaPlayView.setQualityVisibility(false);
            viewHolder.mInstaPlayView.play();
        } else {
            viewHolder.mInstaPlayView.seekTo(CUR_PROGRESS_TIME);
            viewHolder.mInstaPlayView.setSeekBarVisibility(true);
            viewHolder.mInstaPlayView.setCurrentProgVisible(true);
            viewHolder.mInstaPlayView.setForwardTimeVisible(true);
            viewHolder.mInstaPlayView.setRewindVisible(true);
            viewHolder.mInstaPlayView.setDurationTimeVisible(true);
            viewHolder.mInstaPlayView.setMarkerWidth(5);
            viewHolder.mInstaPlayView.setMarkerColor(Color.RED);
            viewHolder.mInstaPlayView.play();
        }

        viewHolder.mInstaPlayView.setBufferVisibility(true);

        viewHolder.mInstaPlayView.setVideoQualitySelectionText(InstaPlayView.VIDEO_QUALITY_TEXT_HEIGHT, InstaPlayView.SORT_ORDER_DESCENDING);

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && (YPlayer == null || !YPlayer.isPlaying()))
            viewHolder.mInstaPlayView.setFullScreen(true);

        try {
            mPlayerChannelLogoView = viewHolder.mInstaPlayView.getInstaViewRef().findViewById(R.id.channel_logo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void resetPlayerShowImage() {
        if (youTubePlayerFragment != null) {
            if (YPlayer != null) {
                try {
                    YPlayer.release();
                    YPlayer = null;
                    if (mCountdown != null) {
                        mCountdown.cancel();
                        mCountdown = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (transaction != null) {
                    transaction.remove(youTubePlayerFragment);
                    viewHolder.youtubeFragment.setVisibility(View.GONE);
                }
            }
        } else if (viewHolder.mInstaPlayView != null) {
            if (!IS_CAST_CONNECTED)
                viewHolder.mInstaPlayView.pause();
            viewHolder.mInstaPlayView.stop();
            viewHolder.mInstaPlayView.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        AnalyticsProvider.sendMediaEventToCleverTap(getCleverTapData());
        if (viewHolder.mInstaPlayView != null) {
            if (viewHolder.mInstaPlayView.getCurrentPosition() > 0 && currentItem != null && currentItem.getData() != null) {
                long pos = viewHolder.mInstaPlayView.getCurrentPosition() / 1000;
                CUR_PROGRESS_TIME = viewHolder.mInstaPlayView.getCurrentPosition();
                AnalyticsProvider.sendMediaEventToCleverTap(AnalyticsProvider.getCleverTapData(getActivity(), pos, currentItem.getData()), getActivity());
                mAnalytics.branchMediaClickData(getActivity(), pos);
            }

            //if (!IS_CAST_CONNECTED)
            if (viewHolder.mInstaPlayView.isPlaying()) {
                viewHolder.mInstaPlayView.pause();
//                    IS_PAUSED_TEMP = true;
            }
        }
        Window window = getActivity().getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (handlerAds != null) {
            handlerAds.removeCallbacks(runnableAds);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mOnEventListener != null) {
            mOnEventListener.onEvent(true);
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        removeHeartBeatRunnable();
        if (mCountdown != null && mAnalytics != null) {
            mCountdown.cancel();
            mCountdown = null;
            analytics_time_spent = 0;
            mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.pause);
            Date currentTime = Calendar.getInstance().getTime();
//            mAnalytics.setmPageStartTime(currentTime);
        }
        if (mActiveCountDown != null) {
            mActiveCountDown.cancel();
            mActiveCountDown = null;
//            Log.e(TAG, "" + mActiveCountDown);
        }

//        if (viewHolder.mInstaPlayView != null)
//            viewHolder.mInstaPlayView.suspend();

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
//        viewHolder.mInstaPlayView.stop();

        viewHolder.mInstaPlayView.release();
        if (YPlayer != null) {
            try {
                YPlayer.release();
                YPlayer = null;
                if (mCountdown != null) {
                    mCountdown.cancel();
                    mCountdown = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.onDestroy();
    }

    public void updateOrientationChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewHolder.appBarLayout.setVisibility(View.VISIBLE);
            viewHolder.mScrollLayout.setVisibility(View.VISIBLE);
            viewHolder.mInstaPlayView.setFullScreen(false);

            if (YPlayer != null && YPlayer.isPlaying()) {
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
            }
            viewHolder.setWidthHeight(viewHolder.mPlayerContainer);

            viewHolder.mInstaPlayView.setResizeMode(InstaPlayView.AspectRatio.RESIZE_MODE_FIT);
            removeFullScreen();
            Constants.FULLSCREENFLAG = false;
        } else if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            Constants.donoWhyButNeeded(getActivity());
            showFullScreen();
            ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
            viewHolder.appBarLayout.setVisibility(View.GONE);
            viewHolder.mScrollLayout.setVisibility(View.GONE);


            if (YPlayer != null && YPlayer.isPlaying()) {
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
            }else {
                viewHolder.mPlayerTitleView.setVisibility(View.VISIBLE);
                viewHolder.mInstaPlayView.setFullScreen(true);
            }
            viewHolder.mInstaPlayView.setResizeMode(InstaPlayView.AspectRatio.RESIZE_MODE_FIXED_WIDTH);
            int navigationHight = Constants.getNavigationHight(getActivity());
            int deviceWidth;
            if (navigationHight > 0) {
                deviceWidth = Constants.getDeviceWidth(getActivity());
            } else {
                deviceWidth = Constants.getDeviceWidth(getActivity());
            }
            int deviceHeight = Constants.getDeviceHeight(getActivity());

            ViewGroup.LayoutParams layoutParams = viewHolder.mPlayerContainer.getLayoutParams();
            layoutParams.width = deviceWidth;
            layoutParams.height = deviceHeight;
            viewHolder.mPlayerContainer.setLayoutParams(layoutParams);
// TODO: 23/05/22 Aditya

            Constants.FULLSCREENFLAG = true;
        }

        setChannelLogoSize(mPlayerChannelLogoView, orientation);

    }

    private void setChannelLogoSize(ImageView mPlayerChannelLogo, int orientation) {
        if (mPlayerChannelLogo != null) {
            ViewGroup.LayoutParams layoutParams = mPlayerChannelLogo.getLayoutParams();

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutParams.width = (int) getResources().getDimension(R.dimen.px_60);
                mPlayerChannelLogo.setPadding(0, (int) getResources().getDimension(R.dimen.px_10), (int) getResources().getDimension(R.dimen.px_15), 0);
            } else {
                layoutParams.width = (int) getResources().getDimension(R.dimen.px_60);
                mPlayerChannelLogo.setPadding(0, (int) getResources().getDimension(R.dimen.px_10), 0, 0);
            }

            mPlayerChannelLogo.setLayoutParams(layoutParams);
            mPlayerChannelLogo.requestLayout();
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
                SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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

    @Override
    public void onRefresh() {
      /*  resetPlayerShowImage();
        PAGEINDEX = 0;
        getDetails();*/
        viewHolder.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class ViewHolder {
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.play_icon)
        ImageView mPlayIcon;
        @BindView(R.id.meta_data)
        GradientTextView mMetaData;
        @BindView(R.id.description)
        GradientTextView mDescription;
        @BindView(R.id.gradient_shadow)
        TextView mDesGradient;
        @BindView(R.id.item_title)
        MyTextView item_title;

        @BindView(R.id.youtubehide)
        ImageView youtubehide;
        @BindView(R.id.play_icon_youtube)
        ImageView play_icon_youtube;
        @BindView(R.id.play_icon_youtube_replay)
        ImageView play_icon_youtube_replay;

        //        @BindView(R.id.preview)
//        LinearLayout mPreview;
        @BindView(R.id.watchLater)
        LinearLayout mWatchLater;

     /*   @BindView(R.id.favoritelin)
        LinearLayout mFavoritelin;*/

       /* @BindView(R.id.playlist)
        LinearLayout mPlaylist;*/

        @BindView(R.id.share)
        LinearLayout mShare;
        @BindView(R.id.more_title)
        GradientTextView mMoreTitle;
        @BindView(R.id.more_item_view)
        RecyclerView mMoreItemView;
        @BindView(R.id.scroll_layout)
        ScrollView mScrollLayout;

        @BindView(R.id.back)
        AppCompatImageView back;
        @BindView(R.id.header)
        MyTextView header;
        @BindView(R.id.close)
        AppCompatImageView close;

        //        Top Bar UI
        @BindView(R.id.category_back_img)
        ImageView mTopbarImage;
        @BindView(R.id.category_grad_back)
        TextView mGradientBackground;

        @BindView(R.id.downArrow)
        AppCompatImageView downArrow;
        @BindView(R.id.instaplay)
        InstaPlayView mInstaPlayView;

        @BindView(R.id.player_container)
        RelativeLayout mPlayerContainer;
        @BindView(R.id.meta_data_holder)
        LinearLayout metaDataHolder;
        @BindView(R.id.parentPanel)
        LinearLayout parentPanel;
        @BindView(R.id.app_bar_layout)
        AppBarLayout appBarLayout;
        @BindView(R.id.error_layout)
        RelativeLayout mErrorLayout;
        boolean isExpanded = false;
        @BindView(R.id.error_go_back)
        GradientTextView mGoBackFromErrorLayout;
        @BindView(R.id.watchLater_img)
        ImageView mWatchlaterImage;
        /*   @BindView(R.id.favorite_img)
           ImageView favoriteImage;*/
        @BindView(R.id.premium)
        ImageView mPremium;
        @BindView(R.id.watchtrailer)
        LinearLayout watchtrailer;
        //        Landscape player back and title view
        @BindView(R.id.player_title_view)
        LinearLayout mPlayerTitleView;
        @BindView(R.id.player_back_btn)
        ImageView mPlayerBackBtn;
        @BindView(R.id.player_title_txt)
        TextView mPlayerTitleTxt;

        @BindView(R.id.swife_container)
        SwipeRefreshLayout swipeRefreshLayout;
        @BindView(R.id.skip_preview)
        TextView mSkipPreview;
        @BindView(R.id.associated_title)
        GradientTextView mAssociatedTitle;
        @BindView(R.id.associated_item_view)
        RecyclerView mAssociatedItemView;
        @BindView(R.id.associated_view_holder)
        LinearLayout mAssociatedViewHolder;
        @BindView(R.id.linsecond)
        LinearLayout linsecond;
        @BindView(R.id.comming_soon_image)
        ImageView mCommingSoon;
        @BindView(R.id.youtube_fragment)
        FrameLayout youtubeFragment;

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
            setWidthHeight(mPlayerContainer);


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                    //Helper.removeCurrentFragment(getActivity(),TAG);
                }
            });

            mMoreTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (genericResult != null) {
                        MoreListingFragment fragment = new MoreListingFragment();
                        Bundle bundle = new Bundle();
                        ListResonse listResonse = genericResult.getListResonse();
                        if (listResonse != null) {
                            //bundle.putParcelable(Constants.ITEMS, listResonse);
                            ShowDetailsResponse response = genericResult.getShowDetailsResponse();
                            if (response != null) {
                                Data data = response.getData();
                                String catalogId = data.getCatalogId();
                                String genres = data.getGenres().get(0);
                                bundle.putString(Constants.CATALOG_ID, catalogId);
                                bundle.putString(Constants.SELECTED_GENRE, genres);
                                bundle.putString(Constants.FROM_PAGE, TAG);

                                if (listResonse.getData() != null && listResonse.getData().getLayoutType() != null)
                                    bundle.putString(Constants.LAYOUT_TYPE_SELECTED, listResonse.getData().getLayoutType());
                                bundle.putString(Constants.DISPLAY_TITLE, mMoreTitle.getText().toString());
                                bundle.putString(Constants.LAYOUT_SCHEME, getArguments() == null ? "video" : getArguments().getString(Constants.LAYOUT_SCHEME));
                                fragment.setArguments(bundle);
                                pauseThePlayer();
                                Helper.addFragmentForDetailsScreen(getActivity(), fragment, MoreListingFragment.TAG + "Movie");
                            }
                        }
                    }
                }
            });

            mGoBackFromErrorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                    //Helper.removeCurrentFragment(getActivity(),TAG);
                }
            });

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isExpanded) {
                        isExpanded = false;
                        mDescription.setMaxLines(3);
                        mDesGradient.setVisibility(View.VISIBLE);
                        metaDataHolder.setVisibility(View.GONE);
                        RotateAnimation expandAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        expandAnim.setDuration(200);
                        expandAnim.setInterpolator(new LinearInterpolator());
                        expandAnim.setFillEnabled(true);
                        expandAnim.setFillAfter(true);
                        downArrow.startAnimation(expandAnim);
                    } else {
                        isExpanded = true;
                        mDescription.setMaxLines(1000);
                        mDesGradient.setVisibility(View.GONE);
                        metaDataHolder.setVisibility(View.VISIBLE);
                        RotateAnimation minAnim = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        minAnim.setDuration(200);
                        minAnim.setInterpolator(new LinearInterpolator());
                        minAnim.setFillEnabled(true);
                        minAnim.setFillAfter(true);
                        downArrow.startAnimation(minAnim);
                    }
                }
            });

            mPlayIcon.setOnClickListener(v -> {
                // TODO: 10/10/18 as per first release we don't have access control

                checkAccessControl();
                sendFirstPlayAnalyticsEvent();
            });

            mPlayerBackBtn.setOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

            mSkipPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSkipPreview.setVisibility(View.GONE);
                    finishPreview();
                }
            });
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

                updateUserState();
                String userGender = PreferenceHandler.getUserGender(getActivity());
                String playerType = "InstaPlay";
                String urlType = currentItem.getData().getPlayUrlType();
                if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
                    playerType = "youtube";
                    mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, playerType,
                            analytics_time_spent + "", 10000 + "",
                            0 + "",
                            1080 + "",
                            607 + "",
                            getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(),
                            currentItem.getData().getCatalogObject().getPlanCategoryType() + "", currentItem.getData().getTheme(),
                            currentItem.getData().getGenres().get(0), currentItem.getData().getCatalogName(),
                            Constants.CURRENT_BITRATE + "", userAge, userGender, USER_STATE, userPeriod, userPlanType,
                            userPackName, userId, null, currentItem.getData().getContentId(),
                            getActivity(), "");

                } else {
                    mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, playerType,
                            analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "",
                            viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "",
                            viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "",
                            getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(),
                            currentItem.getData().getCatalogObject().getPlanCategoryType() + "", currentItem.getData().getTheme(),
                            currentItem.getData().getGenres().get(0), currentItem.getData().getCatalogName(),
                            Constants.CURRENT_BITRATE + "", userAge, userGender, USER_STATE, userPeriod, userPlanType,
                            userPackName, userId, viewHolder.mInstaPlayView, currentItem.getData().getContentId(),
                            getActivity(), "");
                }
                mAnalytics.firstPlayClick();
            } catch (Exception e) {

            }

        }
    }

    private void play() {

        // TODO: 2019-09-26 check for which player before proceeding here
        String urlType = currentItem.getData().getPlayUrlType();
        boolean isYoutubeVideo = false;
        if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
            isYoutubeVideo = true;
        }
        titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
        webEngageAnalytics.titlePlayedEvent(titleEvents);
        if (isYoutubeVideo) {
            setupYoutube();
        } else {


            if (!viewHolder.mInstaPlayView.isPlayerActive()) {
                if (!TextUtils.isEmpty(adaptiveURL)) {
                    setupPlayContent(adaptiveURL);
                    viewHolder.mPlayIcon.setVisibility(View.GONE);
                    viewHolder.mImage.setVisibility(View.GONE);
                    viewHolder.mPremium.setVisibility(View.GONE);
                    viewHolder.mCommingSoon.setVisibility(View.GONE);
                }
            } else if (!TextUtils.isEmpty(adaptiveURL)) {
//            Log.e(TAG, "play: " + viewHolder.mInstaPlayView.getDuration());
                viewHolder.mInstaPlayView.seekTo(CUR_PROGRESS_TIME);
                viewHolder.mInstaPlayView.play();
                viewHolder.mPlayIcon.setVisibility(View.GONE);
                viewHolder.mImage.setVisibility(View.GONE);
                viewHolder.mPremium.setVisibility(View.GONE);
                viewHolder.mCommingSoon.setVisibility(View.GONE);
            } else {
                Helper.showToast(getActivity(), "Unable to Play! , Please try after some time", R.drawable.ic_error_icon);
            }

            if (currentItem.getData().getMediaType() != null && currentItem.getData().getMediaType().equalsIgnoreCase("audio")) {
                try {
                    ImageView audioImage = viewHolder.mInstaPlayView.getInstaViewRef().findViewById(R.id.audioplayerbgimage);
                    Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(currentItem.getData().getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(audioImage);
                    audioImage.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    ImageView audioImage = viewHolder.mInstaPlayView.getInstaViewRef().findViewById(R.id.audioplayerbgimage);
                    audioImage.setVisibility(View.GONE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewHolder.mSkipPreview.setVisibility(View.GONE);
        Log.e("moviedetailepage", "inside onsresume");

//        if (!IS_CAST_CONNECTED)
//            if (IS_PAUSED_TEMP)
//                mVideoPlayer.play();
//            else
//                playStreamingVideo(stoptime, false);
        mCastContext.addCastStateListener(mCastStateListener);
//        if (viewHolder.mInstaPlayView != null)
//            viewHolder.mInstaPlayView.resumeSuspend();
    }

    public InstaPlayView getPlayerInfo() {

        if (getActivity() != null && viewHolder != null && viewHolder.mInstaPlayView != null) {
            return viewHolder.mInstaPlayView;
        }

        return null;

    }

    private void showLoginPopUp(boolean loginstatus) {

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
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.ACCESS_CONTROL_IS_LOGIN_REG, isAccessLoginReq);
        bottomSheetFragment.setArguments(bundle);

        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), BottomSheetDialogFragment.class.getSimpleName());
        bottomSheetFragment.setBottomSheetClickListener(new SubscribeBottomSheetDialog.BottomSheetClickListener() {
            @Override
            public void onLoginClicked() {
                goToLoginPage();
            }

            @Override
            public void onSubscribeClick() {
                boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
                if (loggedIn) {
                    SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FROM_WHERE, MovieDetailsFragment.TAG);
                    bundle.putBoolean(Constants.IS_LOGGED_IN, true);
                    // bundle.putBoolean(Constants.IS_FROM_DETAIL_PAGE,true);
                    subscriptionWebViewFragment.setArguments(bundle);
                    Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
                } else {
                    SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FROM_WHERE, MovieDetailsFragment.TAG);
                    bundle.putBoolean(Constants.IS_LOGGED_IN, false);
                    // bundle.putBoolean(Constants.IS_FROM_DETAIL_PAGE,true);
                    subscriptionWebViewFragment.setArguments(bundle);
                    Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
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

    private void goToLoginPage() {
        Intent intent = new Intent(getContext(), OnBoardingActivity.class);
        intent.putExtra(Constants.FROM_PAGE, MovieDetailsFragment.TAG);
        startActivityForResult(intent, Constants.ACTION_LOGIN_CLICKED);
    }

    private void checkAccessControl() {
        IS_PREVIEW_PLAYING = false;
        IS_PREVIEW_PLAYED = false;
        IS_FOR_LOGIN = false;
        viewHolder.mSkipPreview.setVisibility(View.GONE);

        if (currentItem != null) {
            Data data = currentItem.getData();
            if (data != null) {
                AccessControl accessControl = data.getAccessControl();
                isAccessLoginReq = accessControl.getLoginRequired();
                isAccessFree = accessControl.getIsFree();

//                isAccessLoginReq = true;
//                isAccessFree = true;  // TODO: 21/01/19 to be removed

                if (isAccessLoginReq) {
                    boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
                    if (loggedIn) {
                        if (!isAccessFree) {
                            if (IS_SUBSCRIBED) {
                                play();  // Playing for now
                            } else {
                                showLoginPopUp(true);
                            }
                        } else {
                            play();
                        }
                    } else {
                        showLoginPopUp(false);
                    }

                } else if (!isAccessFree) {
                    if (IS_SUBSCRIBED)
                        play();         // Playing for now
                    else {
                        showLoginPopUp(false);
                    }
                } else {
                    play();
                }
            }
        }
    }


    private void playPreviewContent(Preview preview) {

        if (preview != null) {
            if (!TextUtils.isEmpty(preview.getExtPreviewUrl())) {         // External URL should come
                IS_EXT_PREV_URL = true;
                getAddsSmartURL(preview.getExtPreviewUrl());

            } else if (preview.isPreviewAvailable()) {

         /*       previewStartTime = Constants.parseTimeToMillis(preview.getPreviewStart());
                previewEndTime = Constants.parseTimeToMillis(preview.getPreviewEnd());*/

                if (previewStartTime > -1 && previewEndTime > -1 && previewEndTime > previewStartTime) {
                    if (!TextUtils.isEmpty(adaptiveURL))
                        setupPlayContent(adaptiveURL);
                    viewHolder.mImage.setVisibility(View.GONE);
                    viewHolder.mPlayIcon.setVisibility(View.GONE);
                    viewHolder.mPremium.setVisibility(View.GONE);
                    viewHolder.mCommingSoon.setVisibility(View.GONE);
                    Helper.showToast(getActivity(), getString(R.string.playing_preview_movie), R.drawable.ic_error_icon);
                } else {
                    IS_PREVIEW_PLAYING = false;
                    IS_FOR_LOGIN = false;
                }
            } else {
                IS_PREVIEW_PLAYING = false;
                IS_FOR_LOGIN = false;
            }
        } else {
            IS_PREVIEW_PLAYING = false;
            IS_FOR_LOGIN = false;
        }
    }


    private class EventListener implements
            InstaPlayView.PlayerEventListener,
            InstaPlayView.PlayerUIListener,
            InstaPlayView.MediaTrackEventListener,
            InstaPlayView.AdPlaybackEventListener {

        @Override
        public void OnError(int i, String s) {
            mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.error);
            removeHeartBeatRunnable();
        }

        @Override
        public void OnCompleted() {
            if (!IS_PREVIEW_PLAYING) {
                mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY_COMPLETE, currentItem.getData().getLanguage(),
                        MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                        viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                        PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                webEngageAnalytics.titleCompletedEvent(titleEvents);
                mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.finish);
                removeHeartBeatRunnable();
                reportHeartBeat(0);
                CUR_PROGRESS_TIME = 0;
                viewHolder.mInstaPlayView.setForwardTimeVisible(false);
                viewHolder.mInstaPlayView.setRewindVisible(false);
            } else {
                viewHolder.mSkipPreview.setVisibility(View.GONE);
            }

            if (IS_EXT_PREV_URL)
                finishPreview();
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
//            Log.d(TAG, "!Calling on play..");
            if (handler != null) {
                handler.removeCallbacks(heartBeatRunnable);
                handler.postDelayed(heartBeatRunnable, HEARTBEAT_RATE);
            }


            if (!IS_PREVIEW_PLAYING) {


                viewHolder.mInstaPlayView.setForwardTimeVisible(true);
                viewHolder.mInstaPlayView.setRewindVisible(true);

                mAnalytics.reportVideoLaunchTime();

                /*Analytics code should be started only during actual content playback not for preview*/

                if (getActivity() == null)
                    return;

                String userId = PreferenceHandler.getAnalyticsUserId(getActivity());
                String userAge = PreferenceHandler.getUserAge(getActivity());
                String userPeriod = PreferenceHandler.getUserPeriod(getActivity());
                String userPackName = PreferenceHandler.getPackName(getActivity());
                String userPlanType = PreferenceHandler.getUserPlanType(getActivity());
                String userGender = PreferenceHandler.getUserGender(getActivity());

                updateUserState();


                try {
                    titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                    webEngageAnalytics.titleStartedEvent(titleEvents);
                    mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, "InstaPlay",
                            analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "",
                            viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "",
                            viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "",
                            getResources().getConfiguration().orientation + "", adaptiveURL,
                            currentItem.getData().getLanguage(),
                            currentItem.getData().getCatalogObject().getPlanCategoryType() + "",
                            currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0),
                            currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", userAge,
                            userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId,
                            viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(), "");

                    if (BUFFER_STARTED) {
                        mAnalytics.updateBufferDiff(Calendar.getInstance().getTimeInMillis());

                        double buff = mAnalytics.updateBufferDiff(Calendar.getInstance().getTimeInMillis());
                        int buffertime = (int) (Calendar.getInstance().getTimeInMillis() - (int) bufferStartTime);
                        double buff_sec = (buffertime / 1000.0);
                        mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, buff_sec, Constants.EVENT_TYPE_BUFFER, currentItem.getData().getLanguage(),
                                MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                                viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                                PreferenceHandler.getUserState(getActivity()), userPeriod, userPlanType, userId, "", Constants.CONTENT_OWNER_VALUE, "");
                        mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                        BUFFER_STARTED = false;
                    }

                    mAnalytics.setmVideoStartTime(Calendar.getInstance().getTime());

                    if (!IS_PLAY_EVENT_FIRED) {
                        Log.d("Onanalytics: play 1", "" + AnalyticsProvider.EventName.play.toString());

                        mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY, currentItem.getData().getLanguage(),
                                MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                                viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                                PreferenceHandler.getUserState(getActivity()), userPeriod, userPlanType, userId, "", Constants.CONTENT_OWNER_VALUE, "");
                        mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                        mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
                        startActiveMediaTimer(viewHolder.mInstaPlayView.getDuration() - viewHolder.mInstaPlayView.getCurrentPosition(), ANALYTICS_ACTIVE_INTERVAL);
                        IS_PLAY_EVENT_FIRED = true;
                    }

//                    Log.d(TAG, ">>>>>>>>>>> OnPlay: " + AnalyticsProvider.EventName.play);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                duration = String.valueOf(viewHolder.mInstaPlayView.getDuration());
                currentTime = String.valueOf(viewHolder.mInstaPlayView.getCurrentPosition());
                startAnalyticsTimer(viewHolder.mInstaPlayView.getDuration() - viewHolder.mInstaPlayView.getCurrentPosition(), Constants.ANALYTICS_INTERVAL);
            } else {
//                Preview is playing
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 08/03/19 show skip button
                        if (IS_PREVIEW_PLAYING && viewHolder.mInstaPlayView.isPlaying())
                            if (!IS_CAST_CONNECTED) {
                                viewHolder.mSkipPreview.setVisibility(View.VISIBLE);
                            }
                    }
                }, 5000);

            }

        }

        @Override
        public void OnPaused() {
            mCastContext.removeCastStateListener(mCastStateListener);
            if (!IS_PREVIEW_PLAYING) {
                reportHeartBeat(viewHolder.mInstaPlayView.getCurrentPosition());
                if (mCountdown != null && mAnalytics != null) {
                    mCountdown.cancel();
                    mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.pause);
//                    Date currentTime = Calendar.getInstance().getTime();
//                    mAnalytics.setmPageStartTime(currentTime);
                }

                if (mActiveCountDown != null) {
                    mActiveCountDown.cancel();
                }
            }
            removeHeartBeatRunnable();
        }

        @Override
        public void OnBuffering() {
            if (!IS_PREVIEW_PLAYING) {
                BUFFER_STARTED = true;
                mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.buffering);
                mAnalytics.updateBufferStartTime(Calendar.getInstance().getTimeInMillis());
                bufferStartTime = Calendar.getInstance().getTimeInMillis();
            }
        }

        @Override
        public void OnReady() {

        }

        @Override
        public void OnCastConnected() {
            IS_CAST_CONNECTED = true;
            if (getActivity() != null)
                (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            if (viewHolder.mSkipPreview != null)
                viewHolder.mSkipPreview.setVisibility(View.GONE);
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

            IS_PLAY_EVENT_FIRED = false;
            IS_MEDIA_ACTIVE_SENT = false;
            analytics_time_spent = 0;
            analytics_unique_id = UUID.randomUUID().toString();

        }

        @Override
        public void OnUISeek() {

        }

        @Override
        public void OnUiTouch(boolean b) {

        }

        @Override
        public void OnUIShown() {
            try {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    viewHolder.mPlayerTitleView.setVisibility(View.VISIBLE);
                else
                    viewHolder.mPlayerTitleView.setVisibility(View.GONE);

                if (getActivity() instanceof MainActivity) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        //((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
                    }

                }

            } catch (Exception e) {
            }
        }

        @Override
        public void OnUIHidden() {

            try {
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                if (getActivity() instanceof MainActivity) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        //((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
                    }
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

        @Override
        public void OnAdEvent(InstaPlayView.AdPlaybackEvent adPlaybackEvent) {

        }

        @Override
        public void OnAdError(InstaPlayView.AdPlayerErrorCode adPlayerErrorCode, String s) {

        }

        @Override
        public void OnAdProgress(float v, float v1) {
        }
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
////                Log.d(TAG, "!onTick: mediaactive" + mActiveCountDown);
//                mAnalytics.updateMediaActive(viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "");
//            }
//
//            @Override
//            public void onFinish() {
//                Log.d(TAG, "!onTick:: finish()");
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


    /**
     * Analytics
     */
    private void startAnalyticsTimer(long timeFuture, long interval) {

        //handler.postDelayed(runnable,Constants.ANALYTICS_INTERVAL);
//        Log.d(TAG, "!Count down timer is create here.");
        if (mCountdown != null) {
            mCountdown.cancel();
            mCountdown = null;
        }
        mCountdown = new CountDownTimer(timeFuture, interval) {
            @Override
            public void onTick(long l) {
//                Log.d(TAG, "!run: Method" + mCountdown);
                try {
                    if (viewHolder.mInstaPlayView.isPlaying()) {
//                    Log.d(TAG, "!Starting analytics timer : " + timeFuture + " Interval : " + interval);
                        analytics_time_spent = analytics_time_spent + 10;
                        titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName,
                                contentType, titleImage, titleID, viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", "");
                        webEngageAnalytics.titleProgressEvent(titleEvents);

                        mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 30, Constants.EVENT_TYPE_PLAYBACK_TIME, currentItem.getData().getLanguage(),
                                MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                                viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getData().getTitle(),
                                PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                        mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                        mAnalytics.updateMedia(MEDIA_TYPE, AnalyticsProvider.EventName.seek, analytics_time_spent + "",
                                viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "",
                                getResources().getConfiguration().orientation == 1 ? "1" : "0", viewHolder.mInstaPlayView);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                Log.d(TAG, "!run: Method: finish()");
            }
        }.start();


    }

    private void startAnalyticsTimer_youtube(long timeFuture, long interval) {

        //handler.postDelayed(runnable,Constants.ANALYTICS_INTERVAL);
//        Log.d(TAG, "!Count down timer is create here.");
        if (mCountdown != null) {
            mCountdown.cancel();
            mCountdown = null;
        }
        mCountdown = new CountDownTimer(timeFuture, interval) {
            @Override
            public void onTick(long l) {
//                Log.d(TAG, "!run: Method" + mCountdown);
                try {

                    if (YPlayer != null && YPlayer.isPlaying()) {
                        titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName,
                                contentType, titleImage, titleID, YPlayer.getCurrentTimeMillis() / 1000 + "", YPlayer.getDurationMillis() / 1000 + "", "");
                        webEngageAnalytics.titleProgressEvent(titleEvents);
//                    Log.d(TAG, "!Starting analytics timer : " + timeFuture + " Interval : " + interval);
                        analytics_time_spent = analytics_time_spent + 10;
                        mAnalytics.updateMediaYoutube(MEDIA_TYPE, AnalyticsProvider.EventName.seek, analytics_time_spent + "",
                                YPlayer.getCurrentTimeMillis() / 1000 + "", getResources().getConfiguration().orientation == 1 ? "1" : "0", YPlayer);
                    }

                    mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 30, Constants.EVENT_TYPE_PLAYBACK_TIME, currentItem.getData().getLanguage(),
                            MEDIA_TYPE, currentItem.getData().getGenres().get(0), currentItem.getData().getContentId(), currentItem.getData().getCatalogName(),
                            (YPlayer.getDurationMillis() / 1000), currentItem.getData().getTitle(),
                            PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                    mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                Log.d(TAG, "!run: Method: finish()");
            }
        }.start();


    }

    public void pauseThePlayer() {
        if (IS_CAST_CONNECTED)
            return;

        if (viewHolder.mInstaPlayView != null) {
            CUR_PROGRESS_TIME = viewHolder.mInstaPlayView.getCurrentPosition();
            viewHolder.mInstaPlayView.pause();
            viewHolder.mInstaPlayView.stop();
            viewHolder.mInstaPlayView.release();

            viewHolder.mImage.setVisibility(View.VISIBLE);
            viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
            viewHolder.mSkipPreview.setVisibility(View.GONE);
        }
    }

    /*CONTINUE WATCHING*/

    private Handler handler = new Handler();
    private long CUR_PROGRESS_TIME = 0;

    private Runnable heartBeatRunnable = new Runnable() {           // Persistent Playback
        @Override
        public void run() {
//            Log.d(TAG, "!Persistent runnable..");
            if (IS_PREVIEW_PLAYING) {
                CUR_HEART_BEAT = viewHolder.mInstaPlayView.getCurrentPosition();
                checkPreviewEnd(CUR_HEART_BEAT);
                if (handler != null)
                    handler.postDelayed(heartBeatRunnable, HEARTBEAT_RATE);
            } else {
                if (viewHolder.mInstaPlayView != null) {
                    if (viewHolder.mInstaPlayView.isPlaying()) {
                        Helper.reportHeartBeat(getActivity(), mApiService, currentItem.getData().getContentId(), currentItem.getData().getCatalogId(), viewHolder.mInstaPlayView.getCurrentPosition());
                        if (handler != null)
                            handler.postDelayed(heartBeatRunnable, HEARTBEAT_RATE);
                    }
                }
            }
        }
    };


    private void reportHeartBeat(long pos) {
        Helper.reportHeartBeat(getActivity(), mApiService, currentItem.getData().getContentId(), currentItem.getData().getCatalogId(), pos);
    }

    private void removeHeartBeatRunnable() {
        if (handler != null)
            handler.removeCallbacks(heartBeatRunnable);
    }


    private void checkPreviewEnd(long curHeartBeat) {
        if (IS_EXT_PREV_URL)    // If external preview dont do below checks remaining chks in onCompleted
            return;

        if (curHeartBeat > previewEndTime) {
            finishPreview();
        }
    }

    private void finishPreview() {
        IS_EXT_PREV_URL = false;
        IS_PREVIEW_PLAYING = false;
        viewHolder.mInstaPlayView.stop();
        viewHolder.mInstaPlayView.release();
        viewHolder.mImage.setVisibility(View.VISIBLE);
        if (currentItem != null && currentItem.getData() != null && currentItem.getData().isPlayType() != null && currentItem.getData().isPlayType()) {
            viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mPlayIcon.setVisibility(View.GONE);
        }
        viewHolder.mSkipPreview.setVisibility(View.GONE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (IS_FOR_LOGIN)
            IS_PREVIEW_PLAYED = true;
        else
            return;

        //todo some fix
        //showLoginPopUp();
    }


    public void getAddsSmartURL(String smartUrl) {
        // TODO: 25/09/18 Should change Auth Token

        Helper.showProgressDialog(getActivity());
        SmartUrlFetcher2 urlFetcher = new SmartUrlFetcher2(smartUrl, "hls", Constants.PLAYER_AUTH_TOKEN);

        urlFetcher.setOnFinishListener(new SmartUrlFetcher2.SmartUrlFetcherFinishListener() {
            @Override
            public void onFinished(SmartUrlResponseV2 videoUrl) {

                if (videoUrl != null) {
                    String extAdsURL = Helper.getPlayBackURL(Constants.REGION, IS_SUBSCRIBED, videoUrl);
                    setupExternalPlayContent(extAdsURL);
                    Helper.dismissProgressDialog();
                    Helper.showToast(getActivity(), getString(R.string.playing_preview_movie), R.drawable.ic_error_icon);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Helper.dismissProgressDialog();
                Helper.showToast(getActivity(), "Unable to play video! , Please try again", R.drawable.ic_error_icon);
            }
        });

        urlFetcher.getVideoUrls();
    }

    private void setupExternalPlayContent(String adaptiveURL) {

        resetPlayerShowImage();

        mEventListener = new EventListener();
        viewHolder.mInstaPlayView.addPlayerEventListener(mEventListener);
        viewHolder.mInstaPlayView.addPlayerUIListener(mEventListener);
        viewHolder.mInstaPlayView.addMediaTrackEventListener(mEventListener);

        InstaMediaItem mMediaItem;

        mMediaItem = new InstaMediaItem(adaptiveURL);

        if (mCastContext != null)
        viewHolder.mInstaPlayView.setCastContext(mCastContext);
        mMediaItem.setInstaCastItem(getCastMediaItem(adaptiveURL));
        viewHolder.mInstaPlayView.setMediaItem(mMediaItem);
        viewHolder.mInstaPlayView.prepare();


        viewHolder.mInstaPlayView.setSeekBarVisibility(false);
        viewHolder.mInstaPlayView.setCurrentProgVisible(false);
        viewHolder.mInstaPlayView.setDurationTimeVisible(false);
        viewHolder.mInstaPlayView.setForwardTimeVisible(false);
        viewHolder.mInstaPlayView.setRewindVisible(false);
        viewHolder.mInstaPlayView.setQualityVisibility(false);
        viewHolder.mInstaPlayView.setMuteVisible(false);
        viewHolder.mInstaPlayView.play();


        viewHolder.mInstaPlayView.setBufferVisibility(true);
        viewHolder.mInstaPlayView.setLanguageVisibility(false);
        viewHolder.mInstaPlayView.setCaptionVisibility(false);
        viewHolder.mInstaPlayView.setHDVisibility(false);
        viewHolder.mInstaPlayView.setVideoQualitySelectionText(InstaPlayView.VIDEO_QUALITY_TEXT_HEIGHT, InstaPlayView.SORT_ORDER_DESCENDING);

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && (YPlayer == null || !YPlayer.isPlaying()))
            viewHolder.mInstaPlayView.setFullScreen(true);

        viewHolder.mImage.setVisibility(View.GONE);
        viewHolder.mPlayIcon.setVisibility(View.GONE);
        viewHolder.mPremium.setVisibility(View.GONE);
        viewHolder.mCommingSoon.setVisibility(View.GONE);

    }

    private void showLoginConfirmationPopUp(String watchLater) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);
        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("Login");
        if (watchLater.equalsIgnoreCase(Constants.WATCH_LATER))
            warningMessage.setText(R.string.add_to_watchlist_login);
        else if (watchLater.equalsIgnoreCase(Constants.PLAYLIST))
            warningMessage.setText(R.string.add_to_playlist_login);
        else
            warningMessage.setText(R.string.add_to_fav_login);

        cancel.setText(R.string.cancel);
        confirm.setText(R.string.proceed);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginPage();
                dialog.cancel();
            }
        });

    }


    public void fireScreenView() {
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.MovieDetailsScreen);
        pageEvents = new PageEvents(Constants.MovieDetailsScreen);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }


    public interface OnEventListener {
        void onEvent(boolean b);
    }

    private static OnEventListener mOnEventListener;

    public static void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    public void getAssociatedVideos(String catalogID, String contentID) {
        if (!TextUtils.isEmpty(contentID))
            mApiService.getAssociatedVideos(catalogID, contentID)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ListResonse>() {
                        @Override
                        public void call(ListResonse listResonse) {
                            ListResonse detailsPageResponse = listResonse;
                            genericResult.setListResonse(detailsPageResponse);
                            if (detailsPageResponse != null) {
                                updateRecyclerViewForAssociated(detailsPageResponse);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
    }

    private void updateRecyclerViewForAssociated(ListResonse detailsPageResponse) {

        if (detailsPageResponse.getData() != null && detailsPageResponse.getData().getTheme() != null) {
            if (detailsPageResponse.getData().getTheme().equalsIgnoreCase("movie") || detailsPageResponse.getData().getTheme().equalsIgnoreCase("movies")) {
                showMoreInMovieListingAssociated(detailsPageResponse);
            } else {
                showMoreInShowListingAssociated(detailsPageResponse);
            }
        } else {
            showMoreInShowListingAssociated(detailsPageResponse);
        }
    }


    private void showMoreInMovieListingAssociated(ListResonse detailsPageResponse) {
        if (PAGEINDEX < 1) {
            movieThemeAdapter = new MoreInMovieThemeAdapter(getActivity());
            viewHolder.mAssociatedItemView.setAdapter(movieThemeAdapter);
        }

        try {
            Iterator<Item> iterator = detailsPageResponse.getData().getItems().iterator();
            while (iterator.hasNext()) {
                Item next = iterator.next();
                if (next.getContentId().equalsIgnoreCase(currentItem.getData().getContentId())) {

                    detailsPageResponse.getData().getItems().remove(next);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (detailsPageResponse != null && movieThemeAdapter != null)
            if (detailsPageResponse.getData().getItems() == null || detailsPageResponse.getData().getItems().size() == 0) {
                viewHolder.mAssociatedViewHolder.setVisibility(View.GONE);
                return;
            }
        viewHolder.mAssociatedViewHolder.setVisibility(View.VISIBLE);
        movieThemeAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());
        /**
         * Applicable because we delete one duplicate item above - see recent try block
         */
        if (detailsPageResponse.getData().getItems().size() + 1 < Constants.PAGE_LIST_LIMIT) {
            IS_LAST_ELEMENT = true;
        }
        movieThemeAdapter.setOnItemClickListener(new MoreInMovieThemeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                PAGEINDEX = 0;
                IS_PLAY_EVENT_FIRED = false;
                Helper.showProgressDialog(getActivity());
                Bundle bundle = getArguments();
                bundle.putString(Constants.CONTENT_ID, item.getContentId());
                bundle.putString(Constants.CATALOG_ID, item.getCatalogId());
                if (item.getCatalogObject() != null && item.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.DISPLAY_TITLE, item.getTitle());
                if (!TextUtils.isEmpty(item.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                //setArguments(bundle);


                MovieDetailsFragment fragment = new MovieDetailsFragment();
                fragment.setArguments(bundle);
//            addFragmentForDetailsScreen(fragment, ShowDetailsPageFragment.TAG);
                if (viewHolder != null) {
                    viewHolder.mScrollLayout.scrollTo(0, 0);
                    if (viewHolder.isExpanded) {
                        viewHolder.isExpanded = false;
                        viewHolder.mDescription.setMaxLines(2);
                        viewHolder.mDesGradient.setVisibility(View.VISIBLE);
                        viewHolder.metaDataHolder.setVisibility(View.GONE);
                        RotateAnimation expandAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        expandAnim.setDuration(200);
                        expandAnim.setInterpolator(new LinearInterpolator());
                        expandAnim.setFillEnabled(true);
                        expandAnim.setFillAfter(true);
                        viewHolder.downArrow.startAnimation(expandAnim);
                    }

                    resetPlayerShowImage();
                    viewHolder.play_icon_youtube.setVisibility(View.GONE);
                    viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
                    viewHolder.youtubehide.setVisibility(View.GONE);
                }
                resetPreviewFlags();
                getDetails();
            }
        });
    }

    private void showMoreInShowListingAssociated(ListResonse detailsPageResponse) {

        itemAdapter = new MoreInShowThemeAdapter(getActivity());
        viewHolder.mAssociatedItemView.setAdapter(itemAdapter);

        try {       // Removing duplicate data
            if (detailsPageResponse != null) {
                Iterator<Item> iterator = detailsPageResponse.getData().getItems().iterator();
                while (iterator.hasNext()) {
                    Item next = iterator.next();
                    if (next.getContentId().equalsIgnoreCase(currentItem.getData().getContentId())) {
                        detailsPageResponse.getData().getItems().remove(next);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (detailsPageResponse != null && itemAdapter != null) {

            if (detailsPageResponse.getData().getItems().size() + 1 < Constants.PAGE_LIST_LIMIT) {
                IS_LAST_ELEMENT = true;
            }
            if (detailsPageResponse.getData().getItems() == null || detailsPageResponse.getData().getItems().size() == 0) {
                viewHolder.mAssociatedViewHolder.setVisibility(View.GONE);
                return;
            }
            viewHolder.mAssociatedViewHolder.setVisibility(View.VISIBLE);
            itemAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());

            itemAdapter.setOnItemClickListener(new MoreInShowThemeAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Item item) {
                    PAGEINDEX = 0;
                    IS_PLAY_EVENT_FIRED = false;
                    Helper.showProgressDialog(getActivity());
                    Bundle bundle = getArguments();
                    bundle.putString(Constants.CONTENT_ID, item.getContentId());
                    bundle.putString(Constants.CATALOG_ID, item.getCatalogId());
                    if (item.getCatalogObject() != null && item.getCatalogObject().getPlanCategoryType() != null)
                        bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
                    bundle.putString(Constants.DISPLAY_TITLE, item.getTitle());
                    if (!TextUtils.isEmpty(item.getCatalogObject().getLayoutScheme()))
                        bundle.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                    //setArguments(bundle);

                    MovieDetailsFragment fragment = new MovieDetailsFragment();
                    fragment.setArguments(bundle);
//            addFragmentForDetailsScreen(fragment, ShowDetailsPageFragment.TAG);
                    if (viewHolder != null) {
                        viewHolder.mScrollLayout.scrollTo(0, 0);
                        if (viewHolder.isExpanded) {
                            viewHolder.isExpanded = false;
                            viewHolder.mDescription.setMaxLines(2);
                            viewHolder.mDesGradient.setVisibility(View.VISIBLE);
                            viewHolder.metaDataHolder.setVisibility(View.GONE);
                            RotateAnimation expandAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            expandAnim.setDuration(200);
                            expandAnim.setInterpolator(new LinearInterpolator());
                            expandAnim.setFillEnabled(true);
                            expandAnim.setFillAfter(true);
                            viewHolder.downArrow.startAnimation(expandAnim);
                        }
                        viewHolder.play_icon_youtube.setVisibility(View.GONE);
                        viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
                        viewHolder.youtubehide.setVisibility(View.GONE);
                        resetPlayerShowImage();
                    }
                    CUR_PROGRESS_TIME = 0;
                    resetPreviewFlags();
                    getDetails();
                }
            });
        }
    }

    Handler handlerAds = new Handler();
    Runnable runnableAds = new Runnable() {
        @Override
        public void run() {
            if (mAdmanagerInterstitialAd != null && isAdAvailableOnGoogle) {
                //mInterstitialAdGoogle.show(getActivity());
                mAdmanagerInterstitialAd.show(getActivity());
            } else {
                Log.d(TAG + " Intertialads", "The interstitial ad wasn't ready yet.");
            }
        }
    };

    private void showAds() {
        handlerAds.postDelayed(runnableAds, Constants.ADS_WAITTIME);
    }

}

