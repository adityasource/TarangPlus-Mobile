package com.otl.tarangplus.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;

import androidx.fragment.app.FragmentTransaction;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.otl.tarangplus.adapters.EpisodesAdapter;
import com.otl.tarangplus.adapters.RecommendedAdapter;
import com.otl.tarangplus.adapters.SeasonAdapter;
import com.otl.tarangplus.adapters.SeasonItemAdapter;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.interfaces.APICallValuesRetrurn;
import com.otl.tarangplus.interfaces.SeasonAdapterClickListner;
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
import com.otl.tarangplus.model.PlayListResponse;
import com.otl.tarangplus.model.Preview;
import com.otl.tarangplus.model.ShowDetailsResponse;
import com.otl.tarangplus.model.SingleEpisode;
import com.otl.tarangplus.model.TitleEvents;
import com.otl.tarangplus.model.UserInfo;
import com.otl.tarangplus.rest.ApiService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static com.otl.tarangplus.Utils.Constants.HEARTBEAT_RATE;

public class ShowDetailsPageFragment extends UniversalFragment
        implements SwipeRefreshLayout.OnRefreshListener, APICallValuesRetrurn.GetSeasonIDandItems, SeasonAdapterClickListner {
    @Override
    protected void languageChanged() {
        PAGE_OINDEX = 0;
        getDetails();
    }

    private Analytics mAnalyticsEvent;
    private MediaplaybackEvents mediaplaybackEvents;
    private AppEvents appEvents;
    private long bufferStartTime;

    //private InMobiInterstitial interstitialAd;
    private InterstitialAd mInterstitialAdGoogle;
    private AdManagerInterstitialAd mAdmanagerInterstitialAd;

    //Trailer
    private Boolean isTrailerAvailable = false;

    public static final String TAG = ShowDetailsPageFragment.class.getName();
    private static boolean IS_MEDIA_ACTIVE_SENT = false;
    private boolean IS_PLAY_EVENT_FIRED = false;
    private static long ANALYTICS_ACTIVE_INTERVAL = 0;
    private static boolean IS_FIRST_EPISODE_SET = false;
    private boolean IS_SUBSCRIBED = false;
    private long CUR_HEART_BEAT = 0;
    private SeasonAdapterClickListner seasonAdapterClickListner;
    private boolean IS_PREVIEW_PLAYING = false;
    public boolean IS_PREVIEW_PLAYED = false;
    public boolean IS_FOR_LOGIN = false;

    private boolean isAdAvailableOnGoogle = false;

    Unbinder unbinder;
    private DetailsPageViewModel mViewModel;
    private ViewHolder viewHolder;
    private String adaptiveURL = "";
    private boolean episodeSelected = false;
    private Item currentItem;
    private ApiService mApiService;
    private AnalyticsProvider mAnalytics;
    private static CountDownTimer mCountdown;
    //private GenericResult genericResult;
    private String shareUrl;
    private int PAGE_EINDEX = 0;
    private int PAGE_OINDEX = 0;
    private RecommendedAdapter classicAdapter;
    private boolean IS_LAST_ELEMENT = false;

    private boolean isItemInWatchLater = false;
    private boolean isItemInFavorite = false;
    private String listIdSelectedFavorite = "";

    private String listIdSelected = "";
    private long previewStartTime, previewEndTime;
    private boolean isAccessLoginReq, isAccessFree;
    private EpisodesAdapter episodesAdapter;
    private String USER_STATE;
    private String MEDIA_TYPE = "MediaVideo";
    public SubscribeBottomSheetDialog bottomSheetFragment;

    private boolean BUFFER_STARTED = false;
    private Adds adds;
    private String ads;
    private boolean IS_EXT_PREV_URL = false;
    private static CountDownTimer mActiveCountDown;
    private static String analytics_unique_id;
    private static long analytics_time_spent = 0;

    private CastStateListener mCastStateListener;
    private CastContext mCastContext;
    private IntroductoryOverlay mIntroductoryOverlay;
    private MenuItem mediaRouteMenuItem;
    private InstaCastItem mInstaCastItem;
    private static boolean IS_CAST_CONNECTED = false;
    private GenericResult genericResult2 = new GenericResult();
    private SeasonItemAdapter seasonTitleAdapter;
    private ImageView mPlayerChannelLogoView;
    private YouTubePlayer YPlayer;
    private YouTubePlayerSupportFragment youTubePlayerFragment = null;
    private FragmentTransaction transaction;
    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;
    private TitleEvents titleEvents;

    //Webengage params
    private String categoryName;
    private String contentType;
    private String titleImage;
    private String categoryID;
    private String titleName;
    private String titleID;
    private String duration;
    private String currentTime;
    //End

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_page, container, false);
        viewHolder = new ViewHolder(view);
        CUR_PROGRESS_TIME = 0;
        mAnalyticsEvent = Analytics.getInstance(getContext());
        RestClient mRestClient = new RestClient(getActivity());
        mApiService = mRestClient.getApiService();
        webEngageAnalytics = new WebEngageAnalytics();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        Analytics
        Helper.setLightStatusBar(getActivity());
        mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.setmPageStartTime(Calendar.getInstance().getTime());
        unbinder = ButterKnife.bind(this, view);
        if (mOnEventListener != null) {
            mOnEventListener.onEvent(true);
        }

        //InMobi
   /*     interstitialAd = new InMobiInterstitial(getActivity(), 1598949363916L,
                mInterstitialAdEventListener);*/
        //google ad mob
        //AdRequest adRequest = new AdRequest.Builder().build();
        //InterstitialAd.load(getActivity(), BuildConfig.GOOGLEADID, adRequest, /*interstitialAdLoadCallback*/null);

        //google ad manager
        AdManagerAdRequest adManagerRequest = new AdManagerAdRequest.Builder().build();
        AdManagerInterstitialAd.load(getActivity(), BuildConfig.GOOGLEADID, adManagerRequest, adManagerInterstitialAdLoadCallback/*null*/);


        seasonAdapterClickListner = this;
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
        if (currentItem.getDescription().length() > 250)
            des = currentItem.getDescription().substring(0, 250);

        String portImage = currentItem.getThumbnails().getLarge23().getUrl();
        if (!TextUtils.isEmpty(portImage))
            portImage = currentItem.getThumbnails().getLarge169().getUrl();


        return new InstaCastItem(currentItem.getTitle(),
                des,
                adaptiveUrl,
                portImage,
                currentItem.getThumbnails().getLarge169().getUrl());
    }

    ShowDetailsResponse showDetailsResponse;

    /*Flat map utilization*/
    public Observable<GenericResult> getShowData(String catalog_id, String content_id) {
        return mApiService.getShowDetailsResponse(catalog_id, content_id).flatMap(detailsResponse2 -> {
            this.showDetailsResponse = detailsResponse2;
            Observable<ShowDetailsResponse> showDetailsResponseObservable = Observable.just(detailsResponse2);
            if (detailsResponse2.getData().getSubCategoriesList() == null ||
                    detailsResponse2.getData().getSubCategoriesList().size() == 0) {
                Observable<ListResonse> listResonseObservable = mApiService.getAllEpisodesUnderShow(catalog_id, content_id, 0,
                        Constants.API_ITEMS_COUNT, Constants.DECENDINGDING_ORDER);
                Observable<SingleEpisode> singleEpisode = Observable.just(new SingleEpisode());
                return Observable.zip(showDetailsResponseObservable, listResonseObservable, singleEpisode, new Func3<ShowDetailsResponse, ListResonse, SingleEpisode, GenericResult>() {
                    @Override
                    public GenericResult call(ShowDetailsResponse showDetailsResponse, ListResonse listResonse, SingleEpisode listResonseSingle) {
                        genericResult2.setShowDetailsResponse(showDetailsResponse);

                        genericResult2.setListResonse(listResonse);
                        genericResult2.setSeasonAvailable(false);
                        genericResult2.setSingleEpisode(null);
                        return genericResult2;
                    }
                });
            } else {
                Observable<ListResonse> listResonseObservable = mApiService.getItemsInSeasons(detailsResponse2.getData().getSubCategoriesList().get(0).getContentId());
                boolean isFromEpisode = getArguments().getBoolean(Constants.IS_EPISODE);
                if (isFromEpisode) {
                    Observable<SingleEpisode> singleResonseObservable = mApiService.getEpisodesInSeasons(detailsResponse2.getData().getSubCategoriesList().get(0).getContentId(), getArguments().getString(Constants.CONTENT_ID));
                    return Observable.zip(showDetailsResponseObservable, listResonseObservable, singleResonseObservable,
                            new Func3<ShowDetailsResponse, ListResonse, SingleEpisode, GenericResult>() {
                                @Override
                                public GenericResult call(ShowDetailsResponse showDetailsResponse, ListResonse listResonse, SingleEpisode singleitem) {
                                    genericResult2.setShowDetailsResponse(showDetailsResponse);
                                    genericResult2.setListResonse(listResonse);
                                    genericResult2.setSeasonAvailable(true);
                                    genericResult2.setSingleEpisode(singleitem);
                                    return genericResult2;
                                }
                            });
                } else {
                    Observable<SingleEpisode> singleEpisode = Observable.just(new SingleEpisode());
                    return Observable.zip(showDetailsResponseObservable, listResonseObservable, singleEpisode,
                            new Func3<ShowDetailsResponse, ListResonse, SingleEpisode, GenericResult>() {
                                @Override
                                public GenericResult call(ShowDetailsResponse showDetailsResponse, ListResonse listResonse, SingleEpisode singleitem) {
                                    genericResult2.setShowDetailsResponse(showDetailsResponse);
                                    genericResult2.setListResonse(listResonse);
                                    genericResult2.setSeasonAvailable(true);
                                    genericResult2.setSingleEpisode(null);
                                    return genericResult2;
                                }
                            });
                }

            }
        });
    }

    /**/
    public void getDetails() {

        String content_id = getArguments().getString(Constants.CONTENT_ID);
//        checkInDb(content_id);
        boolean isFromEpisode = getArguments().getBoolean(Constants.IS_EPISODE);
        if (isFromEpisode) {
            content_id = getArguments().getString(Constants.SHOW_ID);
        }
        final String catalog_id = getArguments().getString(Constants.CATALOG_ID);

        String layoutScheme = getArguments().getString(Constants.LAYOUT_SCHEME);
        if (!TextUtils.isEmpty(layoutScheme))
            setTopbarUI(Constants.getSchemeColor(layoutScheme));

        Helper.showProgressDialog(getActivity());

//        Observable<ShowDetailsResponse> detailsResponse = mApiService.getShowDetailsResponse(catalog_id, content_id);
//        Observable<ListResonse> episodesUnderShow = mApiService.getAllEpisodesUnderShow(catalog_id, content_id, 0, Constants.API_ITEMS_COUNT, Constants.ASCENDING_ORDER);


        /*testing for flat map*/

        getShowData(catalog_id, content_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GenericResult>() {
                    @Override
                    public void call(GenericResult genericResult) {

                        if (!genericResult.isSeasonAvailable()) {                                         //Default UI
                            setItemsWithAPIResponse();
                        } else {                                                                         //Season UI
                            setItemsWithAPIResponseForSeason();
                        }
                        appEvents = new AppEvents(1, "", Constants.SHOW_DEATILS, Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, currentItem.getTitle(),
                                PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                        mAnalyticsEvent.logAppEvents(appEvents);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                        /*todo just temp*/
                        viewHolder.coming_soon_error.setVisibility(View.VISIBLE);
                    }
                });

        /**/

//        Observable.zip(detailsResponse, episodesUnderShow, new Func2<ShowDetailsResponse, ListResonse, GenericResult>() {
//            @Override
//            public GenericResult call(ShowDetailsResponse showDetailsResponse, ListResonse listResonse) {
//                genericResult.setShowDetailsResponse(showDetailsResponse);
//                genericResult.setListResonse(listResonse);
//                return genericResult;
//            }
//        })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<GenericResult>() {
//                    @Override
//                    public void call(GenericResult genericResult) {
//                        setItemsWithAPIResponse();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Helper.dismissProgressDialog();
//                        throwable.printStackTrace();
//                    }
//                });

    }

    @Override
    public void OnseasonClick(String contentId) {
        //progress
        viewHolder.recycler_view_episodes.setVisibility(View.GONE);
        viewHolder.progressbarrecyle.setVisibility(View.VISIBLE);
        long sessionLong = -1;
        IS_PLAY_EVENT_FIRED = false;
        mApiService.getItemsInSeasons(contentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        viewHolder.progressbarrecyle.setVisibility(View.GONE);
                        GenericResult genericResult = new GenericResult();
                        genericResult.setListResonse(listResonse);
                        showSeasonItemRecycler(genericResult);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        viewHolder.progressbarrecyle.setVisibility(View.GONE);
                        throwable.printStackTrace();
                    }
                });

    }

    private void setItemsWithAPIResponseForSeason() {
        Helper.dismissProgressDialog();
        viewHolder.allEpisodes.setVisibility(View.GONE);
        viewHolder.episode_recycler_view.setVisibility(View.GONE);
        viewHolder.otherLinear.setVisibility(View.GONE);

        viewHolder.swipeRefreshLayout.setRefreshing(false);
        ShowDetailsResponse detailsPageResponse = genericResult2.getShowDetailsResponse();


        if (!episodeSelected) {
            shareUrl = detailsPageResponse.getData().getShareUrl();
        }
        boolean aBoolean = getArguments().getBoolean(Constants.IS_EPISODE);

        if (aBoolean && genericResult2.getSingleEpisode() != null) {
            Item item = genericResult2.getSingleEpisode().getData();
            viewHolder.header.setText(item.getTitle());
            viewHolder.titleEpisode.setText(item.getTitle());
            currentItem = item;
            setWebEngagePrams(currentItem);
            shareUrl = item.getShareUrl();
            showUI();
            setUpUI2(currentItem);
        } else {
            viewHolder.header.setText(detailsPageResponse.getData().getTitle());
            viewHolder.titleEpisode.setText(detailsPageResponse.getData().getTitle());
        }
        viewHolder.mScrollLayout.smoothScrollTo(0, 0);
        //viewHolder.titleEpisode.setVisibility(View.GONE);
        ListResonse listResonse = genericResult2.getListResonse();

        showSeasonRecycler(detailsPageResponse);
        showSeasonItemRecycler(genericResult2);

        if (!aBoolean) {
            showUI();
            setUpUI2(detailsPageResponse);

            currentItem = listResonse.getData().getItems().get(0);
            setWebEngagePrams(currentItem);
            /*
             * IS_FIRST_EPISODE_SET flag is used to determine if the item set in player is first episode of show, we will
             * use this later to update while user click play icon with the episode related data.
             * */
            IS_FIRST_EPISODE_SET = true;
            if (PreferenceHandler.isLoggedIn(getActivity())) {
//                                getCurHeartBeat(currentItem);
//                                getUserStates(currentItem);   // in Get Details if show is clicked
                Item temp = new Item();
                temp.setContentId(currentItem.getContentId());
                temp.setCatalogId(currentItem.getCatalogId());
                temp.setCatalogObject(currentItem.getCatalogObject());
                getUserStates(temp);  // if isLoggedIn

            } else {

                getUserStates(currentItem);
            }
        } else {

            getUserStates(currentItem);
        }

    }

    private void setWebEngagePrams(Item currentItem) {
        categoryName = currentItem.getCatalogName() != null ? currentItem.getCatalogName() : "";
        categoryID = currentItem.getCatalogId() != null ? currentItem.getCatalogId() : "";
        titleName = currentItem.getTitle() != null ? currentItem.getTitle() : "";
        titleID = currentItem.getContentId() != null ? currentItem.getContentId() : "";
        contentType = currentItem.getMediaType() != null ? currentItem.getMediaType() : "";
        titleImage = currentItem.getThumbnails().getLarge169().getUrl() != null ? currentItem.getThumbnails().getLarge169().getUrl() : "";
        titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
        webEngageAnalytics.titleViewedEvent(titleEvents);

    }

    private void showSeasonItemRecycler(GenericResult genericResult2) {

        ListResonse listForEachItem = genericResult2.getListResonse();
        List<Item> listItems = listForEachItem.getData().getItems();

        if (listItems == null || listItems.size() == 0) {
            viewHolder.recycler_view_episodes.setVisibility(View.GONE);
            viewHolder.coming_soon.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.coming_soon.setVisibility(View.GONE);
        viewHolder.recycler_view_episodes.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false));
        viewHolder.recycler_view_episodes.setNestedScrollingEnabled(false);
       /* DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);
        mDividerItemDecoration.setDrawable(getActivity().getResources().getDrawable(R.drawable.season_line_divider));*/

        viewHolder.recycler_view_episodes.setVisibility(View.VISIBLE);
        SeasonAdapter seasonAdapter = new SeasonAdapter(listItems, getActivity(), seasonAdapterClickListner);
        viewHolder.recycler_view_episodes.setAdapter(seasonAdapter);
        seasonAdapter.notifyDataSetChanged();

    }

    private void showSeasonRecycler(ShowDetailsResponse detailsPageResponse) {
        viewHolder.season_relative.setVisibility(View.VISIBLE);
        viewHolder.recycler_view_seasons.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        List<Data> dataForSeason = detailsPageResponse.getData().getSubCategoriesList();
        seasonTitleAdapter = new SeasonItemAdapter(dataForSeason, getActivity(), this);
        viewHolder.recycler_view_seasons.setNestedScrollingEnabled(false);
        viewHolder.recycler_view_seasons.setAdapter(seasonTitleAdapter);
        viewHolder.recycler_view_seasons.addItemDecoration(new SpacesItemDecoration(0,
                (int) getResources().getDimension(R.dimen.px_10)));
    }

    private void setItemsWithAPIResponse() {

        Helper.dismissProgressDialog();
        viewHolder.swipeRefreshLayout.setRefreshing(false);
        ShowDetailsResponse detailsPageResponse = genericResult2.getShowDetailsResponse();
        if (!episodeSelected) {
            shareUrl = detailsPageResponse.getData().getShareUrl();
        }
        viewHolder.header.setText(detailsPageResponse.getData().getTitle());

        viewHolder.titleEpisode.setText(detailsPageResponse.getData().getTitle());

        ListResonse listResonse = genericResult2.getListResonse();


        if (detailsPageResponse != null && detailsPageResponse.getData() != null &&
                detailsPageResponse.getData().getCatalogObject() != null && detailsPageResponse.getData().getTheme() != null
                && detailsPageResponse.getData().getCatalogObject().getLayoutType() != null) {
            if (detailsPageResponse.getData().getTheme().equalsIgnoreCase("show")
                    && !detailsPageResponse.getData().getCatalogObject().getLayoutType().equalsIgnoreCase("songs")) {
                viewHolder.allEpisodes.setText("All Episodes");
            } else {
                viewHolder.allEpisodes.setText("Songs");
            }
        }
        setEpisodesRecyclerView(listResonse);
        /*getRecommendedList(detailsPageResponse);*/
        getRecommondedListInFragment(detailsPageResponse);

        boolean aBoolean = getArguments().getBoolean(Constants.IS_EPISODE);
        if (!aBoolean) {
            showUI();
            setUpUI(detailsPageResponse);
            /*
             * IS_FIRST_EPISODE_SET flag is used to determine if the item set in player is first episode of show, we will
             * use this later to update while user click play icon with the episode related data.
             * */
            IS_FIRST_EPISODE_SET = true;
            if (PreferenceHandler.isLoggedIn(getActivity())) {
//                                getCurHeartBeat(currentItem);
//                                getUserStates(currentItem);   // in Get Details if show is clicked
                Item temp = new Item();
                temp.setContentId(currentItem.getContentId());
                temp.setCatalogId(currentItem.getCatalogId());
                temp.setCatalogObject(currentItem.getCatalogObject());
                getUserStates(temp);  // if isLoggedIn

            } else {

                getUserStates(currentItem);
            }
        } else {
            getUserStates(currentItem);
        }
    }

    private void setUpUI2(ShowDetailsResponse detailsPageResponse) {
        if (detailsPageResponse != null) {
            Data data = detailsPageResponse.getData();
            if (data != null) {
                if (data.getItemCaption() != null) {
                    viewHolder.meta_data.setVisibility(View.VISIBLE);
                    viewHolder.meta_data.setText(data.getItemCaption());
                } else {
                    viewHolder.meta_data.setVisibility(View.GONE);
                }
                String description = data.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    viewHolder.downArrow.setVisibility(View.VISIBLE);
                    viewHolder.description.setVisibility(View.VISIBLE);
                    viewHolder.description.setText(description);
                } else {
                    //viewHolder.downArrow.setVisibility(View.GONE);
                    viewHolder.description_lin.setVisibility(View.GONE);
                }
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.mImage);
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.youtubehide);

                if (data.getAccessControl() != null) {
                    if (!data.getAccessControl().getIsFree()) {
                        boolean premiumTag = data.getAccessControl().isPremiumTag();
                        if (premiumTag) {
                            if (!IS_CAST_CONNECTED)
                                viewHolder.mPremium.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.mPremium.setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder.mPremium.setVisibility(View.GONE);
                    }
                }

                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                webEngageAnalytics.titleViewedEvent(titleEvents);

            }
        }
    }

    private void setUpUI2(Item data) {
        if (data != null) {
            if (data != null) {
                if (data.getItemCaption() != null) {
                    viewHolder.meta_data.setVisibility(View.VISIBLE);
                    viewHolder.meta_data.setText(data.getItemCaption());
                } else {
                    viewHolder.meta_data.setVisibility(View.GONE);
                }
                String description = data.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    viewHolder.downArrow.setVisibility(View.VISIBLE);
                    viewHolder.description.setVisibility(View.VISIBLE);
                    viewHolder.description.setText(description);
                } else {
                    //viewHolder.downArrow.setVisibility(View.GONE);
                    viewHolder.description_lin.setVisibility(View.GONE);
                }
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.mImage);
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.youtubehide);

                if (data.getAccessControl() != null) {
                    if (!data.getAccessControl().getIsFree()) {
                        boolean premiumTag = data.getAccessControl().isPremiumTag();
                        if (premiumTag) {
                            if (!IS_CAST_CONNECTED)
                                viewHolder.mPremium.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.mPremium.setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder.mPremium.setVisibility(View.GONE);
                    }
                }

                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                webEngageAnalytics.titleViewedEvent(titleEvents);

            }
        }
    }


    private void setUpUI(ShowDetailsResponse detailsPageResponse) {
        if (detailsPageResponse != null) {
            Data data = detailsPageResponse.getData();
            if (data != null) {

                if (!detailsPageResponse.getData().getCatalogObject().getLayoutType().equalsIgnoreCase("songs")) {
                    viewHolder.others.setText("Other " + data.getCatalogName());
                } else if (data.getGenres().size() > 0) {
                    viewHolder.others.setText("More in albums");
                }

                if (data.getItemCaption() != null) {
                    viewHolder.meta_data.setVisibility(View.VISIBLE);
                    viewHolder.meta_data.setText(data.getItemCaption());
                } else {
                    viewHolder.meta_data.setVisibility(View.GONE);
                }
                if (detailsPageResponse.getData() != null && detailsPageResponse.getData().getPreview() != null
                        && detailsPageResponse.getData().getPreview().isPreviewAvailable()) {
                    isTrailerAvailable = true;
                } else {
                    isTrailerAvailable = false;
                }

               /* if (isTrailerAvailable) {
                    viewHolder.watchtrailer.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.watchtrailer.setVisibility(View.GONE);
                }

                viewHolder.watchtrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean connected = NetworkChangeReceiver.isNetworkConnected(getContext());
                        if (connected) {
                            if (detailsPageResponse.getData().getPreview() != null && detailsPageResponse.getData().getPreview().isPreviewAvailable()) {
                                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                                intent.putExtra("contentId", detailsPageResponse.getData().getContentId());
                                intent.putExtra("catalogId", detailsPageResponse.getData().getCatalogId());
                                //intent.putExtra(Constants.TITLE, currentItem.getPreview().get(0).getTitle());
                                intent.putExtra(Constants.PLAY_URL, detailsPageResponse.getData().getPreview().getExtPreviewUrl());
                                getActivity().startActivity(intent);
                            }
                        } else {
                            InternetUpdateFragment framgment = new InternetUpdateFragment();
                            Helper.addFragment(getActivity(), framgment, InternetUpdateFragment.TAG);
                        }
                    }
                });*/


                String description = data.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    viewHolder.downArrow.setVisibility(View.VISIBLE);
                    viewHolder.description.setVisibility(View.VISIBLE);
                    viewHolder.description.setText(description);
                } else {
                    //viewHolder.downArrow.setVisibility(View.GONE);
                    viewHolder.description.setVisibility(View.GONE);
                }
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.mImage);
                Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.youtubehide);

                if (data.getAccessControl() != null) {
                    if (!data.getAccessControl().getIsFree()) {
                        boolean premiumTag = data.getAccessControl().isPremiumTag();
                        if (premiumTag) {
                            if (!IS_CAST_CONNECTED)
                                viewHolder.mPremium.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.mPremium.setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder.mPremium.setVisibility(View.GONE);
                    }
                }

                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                webEngageAnalytics.titleViewedEvent(titleEvents);






               /* if (data.getPreview() != null && data.getPreview().isPreviewAvailable())
                    viewHolder.mPreview.setVisibility(View.VISIBLE);
                else
                    viewHolder.mPreview.setVisibility(View.GONE);*/
            }
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity) getActivity()).setSelectedNavUI(Constants.TABS.HOME);
        mViewModel = ViewModelProviders.of(this).get(DetailsPageViewModel.class);

        hideUI();

        boolean aBoolean = getArguments().getBoolean(Constants.IS_EPISODE);
        // genericResult = new GenericResult();
        getDetails();
    /*    viewHolder.mPreview.setOnClickListener(view1 -> {


            if (currentItem.getPreview() != null && !TextUtils.isEmpty(currentItem.getPreview().getExtPreviewUrl())) {       // External URL should come
                IS_PREVIEW_PLAYING = true;
                IS_FOR_LOGIN = false;
                IS_EXT_PREV_URL = true;

                getAddsSmartURL(currentItem.getPreview().getExtPreviewUrl());

            } else if (currentItem.getPreview() != null && currentItem.getPreview().isPreviewAvailable()
                    && !TextUtils.isEmpty(currentItem.getPreview().getPreviewStart())
                    && !TextUtils.isEmpty(currentItem.getPreview().getPreviewStart())) {

                IS_PREVIEW_PLAYING = true;
                IS_FOR_LOGIN = false;
                playPreviewContent(currentItem.getPreview());   // On preview button click

            } else {
                Helper.showToast(getActivity(), "Preview Not Available!", R.drawable.ic_error_icon);
            }

        });*/
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        viewHolder.mWatchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.mWatchLater.setEnabled(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.mWatchLater.setEnabled(true);
                    }
                }, 300);

                if (!PreferenceHandler.isLoggedIn(getActivity())) {
//                    Helper.showToast(getActivity(), getString(R.string.login_warning), R.drawable.ic_cross);
                    // TODO: 18/01/19 put message box
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
/*
        viewHolder.mFavoritelin.setOnClickListener(new View.OnClickListener() {
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
*/
/*
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
                */
/*
                *         listitem.setCatalogId(getArguments().getString(Constants.CATALOG_ID));
        listitem.setContentId(getArguments().getString(Constants.CONTENT_ID));*//*

                bundle.putString("contentId", getArguments().getString(Constants.CONTENT_ID));
                bundle.putString("catalogId", getArguments().getString(Constants.CATALOG_ID));
                bundle.putString("category", getArguments().getString(Constants.PLAIN_CATEGORY_TYPE));

                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "BottomsheetDialogFragment");
            }
        });
*/
        viewHolder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(shareUrl)) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    assert getArguments() != null;
                    boolean isFromEpisode = getArguments().getBoolean(Constants.IS_EPISODE);
                    String shareText = "Hey !" +
                            "\n" +
                            "I'm watching some really amazing videos on TarangPlus. Check this one. It's one of my favourites!";
                    if (!TextUtils.isEmpty(shareUrl)) {
                        i.putExtra(Intent.EXTRA_TEXT, shareText + "\n" + shareUrl.toLowerCase());
                    }
                    startActivity(Intent.createChooser(i, "Share via"));

                    boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
                    if (loggedIn) {
                        mAnalytics.reportAppShare(shareUrl);
                        appEvents = new AppEvents(1, Constants.SHARE, "", Constants.AN_APP_TYPE, "", Constants.CLICK, currentItem.getTitle(),
                                PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                        mAnalyticsEvent.logAppEvents(appEvents);
                    }
                }
            }
        });

        viewHolder.episode_recycler_view.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        viewHolder.other_items_view.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewHolder.other_items_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!viewHolder.other_items_view.canScrollHorizontally(1) && !IS_LAST_ELEMENT) {
                        getDetails();
                    }
                }
            });
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewHolder.episode_recycler_view.setLayoutManager(manager);
        episodesAdapter = new EpisodesAdapter(getActivity());
        viewHolder.episode_recycler_view.setAdapter(episodesAdapter);

        LinearLayoutManager classicManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewHolder.other_items_view.setLayoutManager(classicManager);

        String mediaActiveInterval = PreferenceHandler.getMediaActiveInterval(getActivity());

        if (mediaActiveInterval != null) {
            long l = Long.parseLong(mediaActiveInterval);
            ANALYTICS_ACTIVE_INTERVAL = l * 60 * 1000;
        } else {
            ANALYTICS_ACTIVE_INTERVAL = 120000;
        }

        Window window = getActivity().getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    private void addToWatchlater(String listName) {
        AddPlayListItems addPlayListItems = new AddPlayListItems();
        Listitem listitem = new Listitem();


        listitem.setCatalogId(getArguments().getString(Constants.CATALOG_ID));
        listitem.setContentId(getArguments().getString(Constants.CONTENT_ID));

//        listitem.setCatalogId(currentItem.getCatalogId());
//        listitem.setContentId(currentItem.getContentId());
        addPlayListItems.setAuthToken(Constants.API_KEY);
        addPlayListItems.setListitem(listitem);
        Helper.showProgressDialog(getActivity());


        mApiService.setWatchLater(PreferenceHandler.getSessionId(getActivity()), listName, addPlayListItems).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        if (listName.equals(Constants.WATCHLATER)) {
                            isItemInWatchLater = true;
                            viewHolder.mWatchlaterImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.otv_orange));
                            Helper.showToast(getActivity(), getString(R.string.added_to_watch_list), R.drawable.ic_check);
                            JsonArray data = jsonObject.getAsJsonArray("data");
                            for (JsonElement je : data) {
                                listIdSelected = je.getAsJsonObject().get("listitem_id").getAsString();
                                titleEvents = new TitleEvents(categoryName, je.getAsJsonObject().get("catalog_id").getAsString(), "", "", je.getAsJsonObject().get("title").getAsString(), contentType, titleImage, je.getAsJsonObject().get("content_id").getAsString());
                            }
                            appEvents = new AppEvents(1, Constants.WATCH_LATER, "", Constants.AN_APP_TYPE, "", Constants.CLICK, currentItem.getTitle(),
                                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                            mAnalyticsEvent.logAppEvents(appEvents);
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
                        /*         isItemInWatchLater = true;

                        JsonArray data = jsonObject.getAsJsonArray("data");
                        for (JsonElement je : data) {
                            listIdSelected = je.getAsJsonObject().get("listitem_id").getAsString();
                        }


//                        playListInstance = PlaylistCheck.getPlayListInstance();
//                        if (playListInstance != null) {
//                            playListInstance.updatePlayList(addPlayListItems.getListitem().getContentId());
//                        }
                        Helper.dismissProgressDialog();
                        //viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watch_list_sel);
                        viewHolder.mWatchlaterImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.otv_orange));
                        Helper.showToast(getActivity(), getString(R.string.added_to_watch_list), R.drawable.ic_check);
                        mAnalytics.reportAddToWatchList();*/
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
        mApiService.removeWatchLaterItem(PreferenceHandler.getSessionId(getActivity()), listName
                , listId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Helper.dismissProgressDialog();
                       /* isItemInWatchLater = false;
                        Helper.showToast(getActivity(), "Item deleted from Watch later list", R.drawable.ic_check);
                        viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watchlist_deactive);
                        viewHolder.mWatchlaterImage.setColorFilter(null);
                        mAnalytics.reportRemoveFromWatchList();*/
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
                        Helper.dismissProgressDialog();
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
        viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
        viewHolder.mImage.setVisibility(View.VISIBLE);
    }


    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {
       /* if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
                Picasso.get().load(layoutDbScheme.getImageUrl()).into(viewHolder.mTopbarImage);
                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            } else {
                Picasso.get().load(R.color.white).into(viewHolder.mTopbarImage);
                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            }
        }*/
    }

    /*public void getRecommendedList(ShowDetailsResponse detailsPageResponse) {
        Data data = detailsPageResponse.getData();
        if (data != null && !TextUtils.isEmpty(data.getCatalogId()) && data.getGenres().size() > 0)
            mViewModel.getMoreBasedOnGenre(data.getCatalogId(), data.getGenres().get(0), PAGE_OINDEX)
                    .observe(this, new Observer<Resource>() {
                        @Override
                        public void onChanged(@Nullable Resource resource) {
                            switch (resource.status) {

                                case LOADING:
//                                    Helper.showProgressDialog(getActivity());
                                    break;

                                case SUCCESS:

                                    ListResonse detailsPageResponse = (ListResonse) resource.data;
                                    genericResult.setRecommendedList(detailsPageResponse);
                                    setRecommendedRecyclerView(detailsPageResponse);
//                                    Helper.dismissProgressDialog();
                                    PAGE_OINDEX++;
                                    break;

                                case ERROR:
//                                    Helper.dismissProgressDialog();
                                    break;
                            }
                        }
                    });

    }*/

    public void getRecommondedListInFragment(ShowDetailsResponse detailsResponse) {

        Data data = detailsResponse.getData();
        if (data == null) {
            return;
        }

        String home_link = data.getCatalogId();
        String genre = data.getGenres().get(0);
        int pageIndex = PAGE_OINDEX;

        mApiService.getMoreBasedOnGenre(home_link, genre, pageIndex)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        genericResult2.setRecommendedList(listResonse);
                        setRecommendedRecyclerView(listResonse);
//                                    Helper.dismissProgressDialog();
                        PAGE_OINDEX++;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();

                    }
                });

    }

    List<Item> listEpisodeItems = new ArrayList<>();

    private void setEpisodesRecyclerView(ListResonse detailsPageResponse) {

        listEpisodeItems = detailsPageResponse.getData().getItems();
        if (detailsPageResponse != null && detailsPageResponse.getData() != null
                && detailsPageResponse.getData().getItems().size() <= 0) {
            /*viewHolder.mComingSoon.setVisibility(View.VISIBLE);
            viewHolder.episode_recycler_view.setVisibility(View.GONE);*/
            viewHolder.mErrorLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mErrorLayout.setVisibility(View.GONE);
            Data data = detailsPageResponse.getData();
            List<Item> items = data.getItems();
            if (items != null && items.size() > 0) {
                Item item = items.get(0);
                Bundle arguments = getArguments();
                int size = items.size();
                if (size == 1) {
                    viewHolder.allEpisodes.setClickable(false);
                    viewHolder.allEpisodes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    viewHolder.allEpisodes.setClickable(true);
                    viewHolder.allEpisodes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_black, 0);
                }
                if (arguments != null) {
                    boolean aBoolean = arguments.getBoolean(Constants.IS_EPISODE);
                    if (aBoolean) {
                        String clickedContentId = arguments.getString(Constants.CONTENT_ID);
                        for (Item myItem : items) {
                            String contentId = myItem.getContentId();
                            if (!TextUtils.isEmpty(contentId) && contentId.equalsIgnoreCase(clickedContentId)) {
                                currentItem = myItem;
                                setWebEngagePrams(currentItem);
                                shareUrl = myItem.getShareUrl();
                                break;
                            }
                        }
                        showUI();
                        setUpUI(currentItem);
                    } else {
                        currentItem = item;
                        setWebEngagePrams(currentItem);
                    }
                } else {
                    currentItem = item;
                    setWebEngagePrams(currentItem);
                    showUI();
                    setUpUI(currentItem);
                }

                if (!TextUtils.isEmpty(currentItem.getTitle()))
                    viewHolder.mPlayerTitleTxt.setText(currentItem.getTitle());

            }
        }

        try {       // Removing duplicate data
            if (detailsPageResponse != null) {
                Iterator<Item> iterator = detailsPageResponse.getData().getItems().iterator();
                while (iterator.hasNext()) {
                    Item next = iterator.next();
                    if (next.getContentId().equalsIgnoreCase(currentItem.getContentId())) {
                        detailsPageResponse.getData().getItems().remove(next);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        episodesAdapter.updateItems(detailsPageResponse.getData().getItems());

        episodesAdapter.setOnItemClickListener(new EpisodesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                List<Item> localList = new ArrayList<>();
                localList.addAll(listEpisodeItems);
                IS_PLAY_EVENT_FIRED = false;
                try {       // Removing duplicate data
                    if (listEpisodeItems != null) {
                        Iterator<Item> iterator = listEpisodeItems.iterator();
                        while (iterator.hasNext()) {
                            Item next = iterator.next();
                            if (next.getContentId().equalsIgnoreCase(item.getContentId())) {
                                localList.remove(next);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewHolder.play_icon_youtube.setVisibility(View.GONE);
                viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
                viewHolder.youtubehide.setVisibility(View.GONE);
                episodesAdapter.updateItems(localList);
                onClickReset(item);
                CUR_PROGRESS_TIME = 0;
            }
        });
    }

    public void onClickReset(Item item) {

        updateEpisodeBundle(item);
//                getDetails();  // on Episode Click
        IS_FIRST_EPISODE_SET = false;
        resetPreviewFlags();
        resetPlayer();
        currentItem = item;
        setWebEngagePrams(currentItem);

        if (viewHolder != null) {
            viewHolder.mScrollLayout.scrollTo(0, 0);
            if (viewHolder.isExpanded) {
                viewHolder.isExpanded = false;
                viewHolder.description.setMaxLines(3);
                viewHolder.mDesGradient.setVisibility(View.VISIBLE);
                viewHolder.metaDataHolder.setVisibility(View.GONE);
                RotateAnimation expandAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expandAnim.setDuration(200);
                expandAnim.setInterpolator(new LinearInterpolator());
                expandAnim.setFillEnabled(true);
                expandAnim.setFillAfter(true);
                viewHolder.downArrow.startAnimation(expandAnim);
            }


        }
        viewHolder.header.setText(item.getTitle());
        viewHolder.titleEpisode.setText(item.getTitle());

        showUI();
        setUpUI(item);       // on Episode Click
        getUserStates(item);     // on Episode Click

        viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
        viewHolder.mImage.setVisibility(View.VISIBLE);
    }

    private void resetPreviewFlags() {
        IS_PREVIEW_PLAYED = false;
        IS_PREVIEW_PLAYING = false;
        IS_FOR_LOGIN = false;
    }

    private void updateEpisodeBundle(Item item) {
        if (item == null) {
            return;
        }
        if (!TextUtils.isEmpty(item.getTitle()))
            viewHolder.mPlayerTitleTxt.setText(item.getTitle());

        episodeSelected = true;
        if (item != null && item.getShareUrl() != null)
            shareUrl = item.getShareUrl();
        else
            shareUrl = "";
        Bundle bundle = getArguments();
        bundle.putString(Constants.CONTENT_ID, item.getContentId());
        bundle.putString(Constants.CATALOG_ID, item.getCatalogId());
        if (item.getCatalogObject() != null && item.getCatalogObject().getPlanCategoryType() != null)
            bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
        bundle.putBoolean(Constants.IS_EPISODE, true);
        bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
        bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
        bundle.putString(Constants.SHOW_ID, item.getShowThemeId());
        if (!TextUtils.isEmpty(item.getCatalogObject().getLayoutScheme()))
            bundle.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
        try {
            setArguments(bundle);
        } catch (Exception ex) {
            try {
                getArguments().clear();
                getArguments().putAll(bundle);
            } catch (Exception e) {
                try {
                    ShowDetailsPageFragment showDetailsPageFragment = new ShowDetailsPageFragment();
                    showDetailsPageFragment.setArguments(bundle);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                e.printStackTrace();
            }
            ex.printStackTrace();
        }


    }

    private void getUserStates(Item data) {
        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
        if (!loggedIn) {
            getSmartURL(currentItem);
            //showAds();
            isAdAvailableOnGoogle = true;
            return;
        } else {
            Constants.PLAYLISTITEMSFORCURRENTITEM.clear();
            // getSmartURL(currentItem);
        }
        setWatchLaterUI("");
        setFavoriteUI("");
        // IS_SUBSCRIBED = false;
        IS_MEDIA_ACTIVE_SENT = false;
        //IS_PLAY_EVENT_FIRED = false;

        String sessionId = PreferenceHandler.getSessionId(getActivity());
        if (sessionId == null || sessionId.equals("")) {
            return;
        }
        String content_id = "";
        if (IS_FIRST_EPISODE_SET) {
            // content_id = getArguments().getString(Constants.CONTENT_ID);
            boolean aBoolean = getArguments().getBoolean(Constants.IS_EPISODE);
            if (aBoolean) {
                content_id = currentItem.getContentId();
            } else {
                content_id = getArguments().getString(Constants.CONTENT_ID);
            }
        } else {
            content_id = data.getContentId();
        }
        final String catalog_id = data.getCatalogId();
        String category = data.getCatalogObject().getPlanCategoryType();

        mApiService.getAllPlayListDetails(sessionId, catalog_id, content_id, category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PlayListResponse>() {
                    @Override
                    public void call(PlayListResponse playListResponse) {
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
                                        //todo added new code below this lines for only continue watching
                                        if (playList.getName().equalsIgnoreCase(Constants.WATCH_HISTORY_TXT))
                                            CUR_PROGRESS_TIME = Constants.parseTimeToMillis(playList.getPos());
                                        if (playList.getName().equalsIgnoreCase(Constants.FAVOURITE))
                                            setFavoriteUI(playList.getListitemId());
                                    }
//                                    Log.e(TAG, "IS_SUBSCRIBED: " + IS_SUBSCRIBED);
//                                    Log.e(TAG, "CUR_PROGRESS_TIME: " + CUR_PROGRESS_TIME);
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

                                    String urlType = currentItem.getPlayUrlType();
                                    String playerType = "InstaPlay";
                                    if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
                                        playerType = "youtube";
                                        mAnalytics.initProvider(analytics_unique_id, currentItem.getTitle(),
                                                adaptiveURL, MEDIA_TYPE, playerType,
                                                analytics_time_spent + "",
                                                viewHolder.mInstaPlayView.getDuration() / 1000 + "",
                                                viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "",
                                                viewHolder.mInstaPlayView.getWidth() + "",
                                                viewHolder.mInstaPlayView.getHeight() + "",
                                                getResources().getConfiguration().orientation + "",
                                                adaptiveURL, currentItem.getLanguage(),
                                                currentItem.getCatalogObject().getPlanCategoryType() + "",
                                                currentItem.getTheme(), currentItem.getGenres().get(0),
                                                currentItem.getCatalogName(), Constants.CURRENT_BITRATE + "",
                                                age, gender, USER_STATE, userPeriod, userPlanType, userPackName,
                                                analyticsUserId, viewHolder.mInstaPlayView,
                                                currentItem.getContentId(), getActivity(),
                                                currentItem.getShowName());
                                    } else {
                                        mAnalytics.initProvider(analytics_unique_id, currentItem.getTitle(),
                                                adaptiveURL, MEDIA_TYPE, playerType,
                                                analytics_time_spent + "",
                                                viewHolder.mInstaPlayView.getDuration() / 1000 + "",
                                                viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "",
                                                viewHolder.mInstaPlayView.getWidth() + "",
                                                viewHolder.mInstaPlayView.getHeight() + "",
                                                getResources().getConfiguration().orientation + "",
                                                adaptiveURL, currentItem.getLanguage(),
                                                currentItem.getCatalogObject().getPlanCategoryType() + "",
                                                currentItem.getTheme(), currentItem.getGenres().get(0),
                                                currentItem.getCatalogName(), Constants.CURRENT_BITRATE + "",
                                                age, gender, USER_STATE, userPeriod, userPlanType, userPackName,
                                                analyticsUserId, viewHolder.mInstaPlayView,
                                                currentItem.getContentId(), getActivity(),
                                                currentItem.getShowName());
                                    }

                                    fireScreenView(IS_FIRST_EPISODE_SET);
                                }
                            }
                        }
                        getSmartURL(currentItem);      //  getUserStates -> getAllPlayListDetails
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
                                getSmartURL(currentItem);       // Throwable  getUserStates -> getAllPlayListDetails
                            }

                    }
                });


        /*
         *//*Some improvement*//*
        mApiService.getAllPlayListDetails(sessionId, currentItem.getCatalogId(), currentItem.getContentId(), category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PlayListResponse>() {
                    @Override
                    public void call(PlayListResponse playListResponse) {
                        if (playListResponse != null) {
                            Data response = playListResponse.getPlayListResponse();
                            if (response != null) {
                                List<PlayList> playLists = response.getPlayLists();
                                if (playLists != null && playLists.size() > 0) {
                                    //todo update the Playlists items.. :)
                                    for (PlayList playList : playLists) {
                                        if (playList.getName().equalsIgnoreCase(Constants.WATCH_HISTORY_TXT))
                                            CUR_PROGRESS_TIME = Constants.parseTimeToMillis(playList.getPos());
                                    }
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
                            }
                    }
                });*/
    }


    private void setUpUI(Item data) {

        if (data != null) {
           /* if (data.getGenres().size() > 0) {
                viewHolder.others.setText("Other " + data.getCatalogName());
            }*/

            String urlType = data.getPlayUrlType();
            boolean isYoutubeVideo = false;
            if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
                isYoutubeVideo = true;

            }

            if (data.getTitle() != null) {
                viewHolder.titleEpisode.setVisibility(View.VISIBLE);
                viewHolder.titleEpisode.setText(data.getTitle());
            } else {
                //viewHolder.meta_data.setVisibility(View.GONE);
            }

            if (data.getGenres().size() > 0 && !data.getCatalogObject().getLayoutType().equalsIgnoreCase("songs")) {
                viewHolder.others.setText("Other " + data.getCatalogName());
            } else if (data.getGenres().size() > 0) {
                viewHolder.others.setText("More in albums");
            }

            if (data.getItemCaption() != null) {
                viewHolder.meta_data.setVisibility(View.VISIBLE);
                viewHolder.meta_data.setText(data.getItemCaption());
            } else {
                viewHolder.meta_data.setVisibility(View.GONE);
            }
            //Log.e("testpreview",data.getTrailer().toString());

            /*if (data != null && data.getTrailer() != null && data.getTrailer().getPreviewAvailable()!= null && data.getTrailer().getPreviewAvailable()) {
                isTrailerAvailable = true;
            } else {
                isTrailerAvailable = false;
            }*/

            String description = data.getDescription();
            if (!TextUtils.isEmpty(description)) {
                viewHolder.description.setVisibility(View.VISIBLE);
                viewHolder.description.setText(description);
                viewHolder.downArrow.setVisibility(View.VISIBLE);
            } else {
                //viewHolder.downArrow.setVisibility(View.GONE);
                viewHolder.description.setVisibility(View.GONE);
            }
            Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.mImage);
            Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(data.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(viewHolder.youtubehide);

            viewHolder.metaDataHolder.removeAllViews();

            Map<String, ArrayList> additionalData = data.getItemAdditionalData();
            if (additionalData != null && additionalData.size() > 0) {
                for (TreeMap.Entry<String, ArrayList> entry : additionalData.entrySet()) {
                    String key = entry.getKey();
                    ArrayList value = entry.getValue();
                    updateUI(key, value);
                }
            }

            if (TextUtils.isEmpty(description) && additionalData == null) {
                viewHolder.downArrow.setVisibility(View.GONE);
            }


            viewHolder.titleEpisode.setVisibility(View.VISIBLE);

            titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
            webEngageAnalytics.titleViewedEvent(titleEvents);

/*            if (isTrailerAvailable) {
                viewHolder.watchtrailer.setVisibility(View.VISIBLE);
            } else {
                viewHolder.watchtrailer.setVisibility(View.GONE);
            }

            viewHolder.watchtrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean connected = NetworkChangeReceiver.isNetworkConnected(getContext());
                    if (connected) {
                        if (currentItem.getTrailer() != null && currentItem.getTrailer().getPreviewAvailable() != null && currentItem.getTrailer().getPreviewAvailable()) {
                            Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                            intent.putExtra("contentId", currentItem.getContentId());
                            intent.putExtra("catalogId", currentItem.getCatalogId());
                            //intent.putExtra(Constants.TITLE, currentItem.getTrailer().get(0).getTitle());
                            intent.putExtra(Constants.PLAY_URL, currentItem.getTrailer().getExtPreviewUrl());
                            getActivity().startActivity(intent);
                        }
                    } else {
                        InternetUpdateFragment framgment = new InternetUpdateFragment();
                        Helper.addFragment(getActivity(), framgment, InternetUpdateFragment.TAG);
                    }
                }
            });*/

            //currentItem
            //data

           /* if (data.getPreview() != null && data.getPreview().isPreviewAvailable())
                viewHolder.mPreview.setVisibility(View.VISIBLE);
            else
                viewHolder.mPreview.setVisibility(View.GONE);*/

        }


    }

    private void setRecommendedRecyclerView(ListResonse detailsPageResponse) {
        if (PAGE_OINDEX < 1) {
            classicAdapter = new RecommendedAdapter(getActivity());
            viewHolder.other_items_view.setAdapter(classicAdapter);
        }

        try {       // Removing duplicate data
            if (detailsPageResponse != null) {
                Iterator<Item> iterator = detailsPageResponse.getData().getItems().iterator();
                while (iterator.hasNext()) {
                    Item next = iterator.next();
                    if (next.getContentId().equalsIgnoreCase(currentItem.getShowThemeId())) {
                        detailsPageResponse.getData().getItems().remove(next);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (detailsPageResponse != null && detailsPageResponse.getData().getItems().size() > 0) {
            viewHolder.others.setVisibility(View.VISIBLE);
            viewHolder.other_items_view.setVisibility(View.VISIBLE);
           /* if (PAGEINDEX < 1) {
                classicAdapter.updateListItems(detailsPageResponse.getData().getItems());
                if(detailsPageResponse.getData().getItems().size()+1<20) {
                    IS_LAST_ELEMENT=true;
                }
            } else {
                classicAdapter.appendUpdateListItems(detailsPageResponse.getData().getItems());
                if(detailsPageResponse.getData().getItems().size()+1<20) {
                    IS_LAST_ELEMENT=true;
                }*/
            classicAdapter.appendUpdateListItems(detailsPageResponse.getData().getItems());
            if (detailsPageResponse.getData().getItems().size() + 1 < 20) {
                IS_LAST_ELEMENT = true;
            }
            if (detailsPageResponse.getData().getItems().size() == 1) {
                viewHolder.others.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                viewHolder.others.setClickable(false);
            } else {
                viewHolder.others.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_black, 0);
                viewHolder.others.setClickable(true);
            }
        } else {
            viewHolder.others.setVisibility(View.GONE);
            viewHolder.other_items_view.setVisibility(View.GONE);
        }
        classicAdapter.setOnItemClickListener(new RecommendedAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                IS_PLAY_EVENT_FIRED = false;
                Bundle bundle = getArguments();
                bundle.putString(Constants.CONTENT_ID, item.getContentId());
                bundle.putString(Constants.CATALOG_ID, item.getCatalogId());
                if (item.getCatalogObject() != null && item.getCatalogObject().getPlanCategoryType() != null)
                    bundle.putString(Constants.PLAIN_CATEGORY_TYPE, item.getCatalogObject().getPlanCategoryType());
                bundle.putString(Constants.DISPLAY_TITLE, item.getTitle());
                bundle.putBoolean(Constants.IS_EPISODE, false);

                if (!TextUtils.isEmpty(item.getCatalogObject().getLayoutScheme()))
                    bundle.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                setArguments(bundle);
                PAGE_OINDEX = 0;

                if (viewHolder != null) {
                    viewHolder.mScrollLayout.scrollTo(0, 0);
                    if (viewHolder.isExpanded) {
                        viewHolder.isExpanded = false;
                        viewHolder.description.setMaxLines(3);
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
                }

                CUR_PROGRESS_TIME = 0;
                resetPreviewFlags();
                getDetails();
                resetPlayer();
            }
        });
        viewHolder.mScrollLayout.smoothScrollTo(0, 0);

    }

    public void getSmartURL(Item currentItem) {
        // TODO: 25/09/18 Should change Auth Token
        adds = null;  // Resetting Ads state
        //ads = "";
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

                getAdsIfPresent(currentItem);
                adaptiveURL = Helper.getPlayBackURL(Constants.REGION, IS_SUBSCRIBED, videoUrl);
                analytics_unique_id = UUID.randomUUID().toString();
                analytics_time_spent = 0;

                Helper.dismissProgressDialog();

                if (getArguments() != null && getArguments().getString(Constants.FROM_PAGE)
                        != null && getArguments().getString(Constants.FROM_PAGE).equals(ContinueWatchingFragment.TAG))
                    if (!IS_CAST_CONNECTED)
                        checkAccessControl(); // Added for auto play for continue watching
            }

            @Override
            public void onError(Throwable throwable) {
                adaptiveURL = "";
                Helper.dismissProgressDialog();
            }
        });

        urlFetcher.getVideoUrls();
    }


    private void setupPlayContent(String adaptiveURL) {

        resetPlayer();
        viewHolder.mSkipPreview.setVisibility(View.GONE);

        EventListener mEventListener = new EventListener();
        viewHolder.mInstaPlayView.addPlayerEventListener(mEventListener);
        viewHolder.mInstaPlayView.addPlayerUIListener(mEventListener);
        viewHolder.mInstaPlayView.addMediaTrackEventListener(mEventListener);
        viewHolder.mInstaPlayView.addAdEventListener(mEventListener);

        InstaMediaItem mMediaItem;

        /*todo remove*/
        if (ads == null || ads.equals("") || IS_SUBSCRIBED || IS_PREVIEW_PLAYING) {
            mMediaItem = new InstaMediaItem(adaptiveURL);
        } else {
            mMediaItem = new InstaMediaItem(adaptiveURL, adds);
        }

        viewHolder.mInstaPlayView.setCastContext(mCastContext);
        mMediaItem.setInstaCastItem(getCastMediaItem(adaptiveURL));
        viewHolder.mInstaPlayView.setMediaItem(mMediaItem);
        viewHolder.mInstaPlayView.AutoPlayEnable(true);   // Added this to fix Ads playback
        viewHolder.mInstaPlayView.prepare();

        viewHolder.mInstaPlayView.setLanguageVisibility(false);
        viewHolder.mInstaPlayView.setCaptionVisibility(true);
        viewHolder.mInstaPlayView.setHDVisibility(false);

        if (IS_PREVIEW_PLAYING) {         // Set start duration based on preview
            viewHolder.mInstaPlayView.seekTo(previewStartTime);
            viewHolder.mInstaPlayView.setSeekBarVisibility(false);
            viewHolder.mInstaPlayView.setCurrentProgVisible(false);
            viewHolder.mInstaPlayView.setDurationTimeVisible(false);
            viewHolder.mInstaPlayView.setMuteVisible(false);
            viewHolder.mInstaPlayView.setForwardTimeVisible(false);
            viewHolder.mInstaPlayView.setQualityVisibility(false);
            viewHolder.mInstaPlayView.setRewindVisible(false);
            viewHolder.mInstaPlayView.play();
        } else {
            viewHolder.mInstaPlayView.seekTo(CUR_PROGRESS_TIME);
            viewHolder.mInstaPlayView.setSeekBarVisibility(true);
            viewHolder.mInstaPlayView.setCurrentProgVisible(true);
            viewHolder.mInstaPlayView.setForwardTimeVisible(true);
            viewHolder.mInstaPlayView.setMuteVisible(false);
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

    private void resetPlayer() {
        if (youTubePlayerFragment != null) {
            if (YPlayer != null) {
                try {
                    YPlayer.pause();
                    YPlayer.release();
                    YPlayer = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (transaction != null) {
                    transaction.remove(youTubePlayerFragment);

                }
                viewHolder.youtubeFragment.setVisibility(View.GONE);
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

            if (viewHolder.mInstaPlayView.getCurrentPosition() > 0 && currentItem != null) {
                long pos = viewHolder.mInstaPlayView.getCurrentPosition() / 1000;
                CUR_PROGRESS_TIME = viewHolder.mInstaPlayView.getCurrentPosition();
                AnalyticsProvider.sendMediaEventToCleverTap(AnalyticsProvider.getCleverTapData(getActivity(), pos, currentItem), getActivity());
                mAnalytics.branchMediaClickData(getActivity(), pos);
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

        if (handlerAds != null) {
            handlerAds.removeCallbacks(runnableAds);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        removeHeartBeatRunnable();
        if (mCountdown != null && mAnalytics != null) {
            mCountdown.cancel();
            mCountdown = null;
            analytics_time_spent = 0;
            mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.pause);
        }
        if (mActiveCountDown != null) {
            mActiveCountDown.cancel();
            mActiveCountDown = null;
//            Log.e(TAG, "" + mActiveCountDown);
        }
        if (mOnEventListener != null) {
            mOnEventListener.onEvent(true);
        }

        PAGE_OINDEX = 0;

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

        if (viewHolder != null && viewHolder.mInstaPlayView != null)
            viewHolder.mInstaPlayView.release();

        if (YPlayer != null) {
            try {
                YPlayer.pause();
                YPlayer.release();
                YPlayer = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        resetPlayer();
        PAGE_OINDEX = 0;
        PAGE_EINDEX = 0;
        getDetails();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void OnseasonClick(Item seasonClickItem) {
        CUR_PROGRESS_TIME = 0;
        IS_PLAY_EVENT_FIRED = false;
        onClickReset(seasonClickItem);
    }


    class ViewHolder {

        @BindView(R.id.image)
        ImageView mImage;
        /*@BindView(R.id.watchtrailer)
        LinearLayout watchtrailer;*/
        @BindView(R.id.play_icon)
        ImageView mPlayIcon;
        @BindView(R.id.meta_data)
        GradientTextView meta_data;

        @BindView(R.id.youtubehide)
        ImageView youtubehide;
        @BindView(R.id.play_icon_youtube)
        ImageView play_icon_youtube;
        @BindView(R.id.play_icon_youtube_replay)
        ImageView play_icon_youtube_replay;

        @BindView(R.id.description)
        GradientTextView description;
        @BindView(R.id.gradient_shadow)
        TextView mDesGradient;
        @BindView(R.id.title_episode)
        MyTextView titleEpisode;
        @BindView(R.id.episode_recycler_view)
        RecyclerView episode_recycler_view;
        @BindView(R.id.others)
        GradientTextView others;
        @BindView(R.id.other_items_view)
        RecyclerView other_items_view;
        /*@BindView(R.id.favoritelin)
        LinearLayout mFavoritelin;*/
  /*      @BindView(R.id.playlist)
        LinearLayout mPlaylist;*/
        @BindView(R.id.back)
        AppCompatImageView back;
        @BindView(R.id.header)
        MyTextView header;
        @BindView(R.id.close)
        AppCompatImageView close;

        /* @BindView(R.id.preview)
         LinearLayout mPreview;*/
        @BindView(R.id.watchLater)
        LinearLayout mWatchLater;
        @BindView(R.id.share)
        LinearLayout mShare;
        /*     @BindView(R.id.favorite_img)
             ImageView favoriteImage;*/
        @BindView(R.id.player_container)
        RelativeLayout mPlayerContainer;

        @BindView(R.id.downArrow)
        AppCompatImageView downArrow;
        @BindView(R.id.instaplay)
        InstaPlayView mInstaPlayView;

        //        Top Bar UI
        @BindView(R.id.category_back_img)
        ImageView mTopbarImage;
        @BindView(R.id.category_grad_back)
        TextView mGradientBackground;

        @BindView(R.id.parentPanel)
        LinearLayout parentPanel;
        @BindView(R.id.meta_data_holder)
        LinearLayout metaDataHolder;
        @BindView(R.id.scroll_layout)
        ScrollView mScrollLayout;
        @BindView(R.id.error_layout)
        RelativeLayout mErrorLayout;
        @BindView(R.id.coming_soon_layout)
        RelativeLayout mComingSoon;

        @BindView(R.id.coming_soon_error)
        TextView coming_soon_error;
        @BindView(R.id.app_bar_layout)
        AppBarLayout appBarLayout;

        @BindView(R.id.error_go_back)
        GradientTextView mGoBackFromErrorLayout;
        @BindView(R.id.watchLater_img)
        ImageView mWatchlaterImage;
        @BindView(R.id.premium)
        ImageView mPremium;

        @BindView(R.id.all_episodes)
        GradientTextView allEpisodes;

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
        @BindView(R.id.other_linear)
        LinearLayout otherLinear;

        @BindView(R.id.season_relative)
        RelativeLayout season_relative;
        @BindView(R.id.recycler_view_seasons)
        RecyclerView recycler_view_seasons;

        @BindView(R.id.description_lin)
        LinearLayout description_lin;

        @BindView(R.id.recycler_view_episodes)
        RecyclerView recycler_view_episodes;

        @BindView(R.id.progressbarrecyle)
        ProgressBar progressbarrecyle;


        @BindView(R.id.coming_soon)
        MyTextView coming_soon;
        /*     @BindView(R.id.skip_preview)
             TextView mSkipPreview;

             @BindView(R.id.skip_preview)
             TextView mSkipPreview;*/
        @BindView(R.id.youtube_fragment)
        FrameLayout youtubeFragment;
        boolean isExpanded = false;

        void setWidthHight(RelativeLayout layout) {
            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            int deviceWidth = Constants.getDeviceWidth(layout.getContext());
            int height = (deviceWidth * 9) / 16;
            layoutParams.height = height;
            layoutParams.width = deviceWidth;
            mPlayerContainer.setLayoutParams(layoutParams);
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            close.setVisibility(View.GONE);
            setWidthHight(mPlayerContainer);

            allEpisodes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String content_id = getArguments().getString(Constants.CONTENT_ID);

                    boolean isFromEpisode = getArguments().getBoolean(Constants.IS_EPISODE);
                    if (isFromEpisode) {
                        content_id = getArguments().getString(Constants.SHOW_ID);
                    }
                    final String catalog_id = getArguments().getString(Constants.CATALOG_ID);

                    MoreListingFragment fragment = new MoreListingFragment();
                    Bundle bundle = new Bundle();
                    ListResonse listResonse = genericResult2.getListResonse();
                    //bundle.putParcelable(Constants.ITEMS, listResonse);
                    bundle.putString(Constants.CATALOG_ID, catalog_id);
                    bundle.putString(Constants.CONTENT_ID, content_id);
                    bundle.putString(Constants.FROM_PAGE, Constants.ALL_EPISODES);

                    bundle.putString(Constants.LAYOUT_TYPE_SELECTED, genericResult2.getShowDetailsResponse().getData().getLayoutType());
                    bundle.putString(Constants.DISPLAY_TITLE, listResonse.getData().getTitle());
                    bundle.putString(Constants.LAYOUT_SCHEME, getArguments() == null ? "all" : getArguments().getString(Constants.LAYOUT_SCHEME));
                    fragment.setArguments(bundle);
                    pauseThePlayer();
                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, MoreListingFragment.TAG + "Show");
                }
            });

            others.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MoreListingFragment fragment = new MoreListingFragment();
                    Bundle bundle = new Bundle();
                    if (genericResult2 != null) {
                        ListResonse listResonse = genericResult2.getRecommendedList();
                        if (listResonse != null) {
                            ShowDetailsResponse response = genericResult2.getShowDetailsResponse();
                            if (response != null) {
                                //bundle.putParcelable(Constants.ITEMS, listResonse);
                                Data data = response.getData();
                                String catalogId = data.getCatalogId();
                                String genres = data.getGenres().get(0);
                                bundle.putString(Constants.CATALOG_ID, catalogId);
                                bundle.putString(Constants.SELECTED_GENRE, genres);
                                bundle.putString(Constants.FROM_PAGE, TAG);

                                if (listResonse.getData() != null && listResonse.getData().getLayoutType() != null)
                                    bundle.putString(Constants.LAYOUT_TYPE_SELECTED, genericResult2.getShowDetailsResponse().getData().getLayoutType());

                                if (!TextUtils.isEmpty(listResonse.getData().getTitle()))
                                    bundle.putString(Constants.DISPLAY_TITLE, listResonse.getData().getTitle());
                                else if (!TextUtils.isEmpty(listResonse.getData().getDisplayTitle()))
                                    bundle.putString(Constants.DISPLAY_TITLE, listResonse.getData().getTitle());
                                else
                                    bundle.putString(Constants.DISPLAY_TITLE, others.getText().toString());

                                bundle.putString(Constants.LAYOUT_SCHEME, getArguments() == null ? "all" : getArguments().getString(Constants.LAYOUT_SCHEME));


                                pauseThePlayer();

                                fragment.setArguments(bundle);
                                Helper.addFragment(getActivity(), fragment, MoreListingFragment.TAG);
                            }
                        }
                    }
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                    // Helper.removeCurrentFragment(getActivity(),TAG);
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
                        description.setMaxLines(3);
                        metaDataHolder.setVisibility(View.GONE);
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
                        metaDataHolder.setVisibility(View.VISIBLE);
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

            mPlayIcon.setOnClickListener(v -> {

                if (viewHolder.mInstaPlayView != null && !TextUtils.isEmpty(viewHolder.mInstaPlayView.getCastingMedia())) {
                    if (viewHolder.mInstaPlayView.getCastingMedia().equalsIgnoreCase(adaptiveURL))
                        return;
                }

                if (IS_FIRST_EPISODE_SET) {
                    IS_FIRST_EPISODE_SET = false;
                    setUpUI(currentItem);
                    updateEpisodeBundle(currentItem);
                    getUserStates(currentItem);
                }

                checkAccessControl();
                sendFirstPlayAnalyticsEvent();

            });

            mPlayerBackBtn.setOnClickListener(v ->
                    (Objects.requireNonNull(getActivity())).onBackPressed()

            );

            mSkipPreview.setOnClickListener((View.OnClickListener) v -> {
                mSkipPreview.setVisibility(View.GONE);
                finishPreview();
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
                String urlType = currentItem.getPlayUrlType();
                if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
                    playerType = "youtube";
                    mAnalytics.initProvider(analytics_unique_id, currentItem.getTitle(), adaptiveURL, "Video", playerType, analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getLanguage(), currentItem.getCatalogObject().getPlanCategoryType() + "", currentItem.getTheme(), currentItem.getGenres().get(0), currentItem.getCatalogName(), Constants.CURRENT_BITRATE + "", userAge, userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId, viewHolder.mInstaPlayView, currentItem.getContentId(), getActivity(), currentItem.getShowName());
                } else {
                    mAnalytics.initProvider(analytics_unique_id, currentItem.getTitle(), adaptiveURL, "Video", playerType, analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getLanguage(), currentItem.getCatalogObject().getPlanCategoryType() + "", currentItem.getTheme(), currentItem.getGenres().get(0), currentItem.getCatalogName(), Constants.CURRENT_BITRATE + "", userAge, userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId, viewHolder.mInstaPlayView, currentItem.getContentId(), getActivity(), currentItem.getShowName());
                }
                mAnalytics.firstPlayClick();
            } catch (Exception e) {

            }

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

    private void checkAccessControl() {
        IS_PREVIEW_PLAYING = false;
        IS_PREVIEW_PLAYED = false;
        IS_FOR_LOGIN = false;

        if (viewHolder.mSkipPreview != null)
            viewHolder.mSkipPreview.setVisibility(View.GONE);

        if (currentItem != null) {
            AccessControl accessControl = currentItem.getAccessControl();
            isAccessLoginReq = accessControl.getLoginRequired();
//            isAccessLoginReq = true;
            isAccessFree = accessControl.getIsFree();

        /*    // TODO: 12/12/18 remove this
            isAccessLoginReq = false;
            isAccessFree = true;*/

//            Log.e(TAG, "isFree: " + isAccessFree);

            if (isAccessLoginReq) {
                boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
                if (loggedIn) {
                    if (!isAccessFree) {
                        if (IS_SUBSCRIBED) {
                            play();  // Playing for now
                        } else {
                            //                        Removed as preview logic is not needed during play button
                            /*if (currentItem.getPreview() != null && currentItem.getPreview().isPreviewAvailable()) {
                                if (IS_PREVIEW_PLAYED)
                                    showLoginPopUp();
                                else {
                                    IS_PREVIEW_PLAYING = true;
                                    IS_FOR_LOGIN = true;
                                    playPreviewContent(currentItem.getPreview());
                                }
                            } else*/

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
        bundle.putBoolean(Constants.IS_FROM_DETAIL_PAGE, true);
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
                    //Toast.makeText(getActivity(), "Yet to be Implement, Playing for now", Toast.LENGTH_SHORT).show();
                    //play();    // TODO: Aditya handel
                    SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FROM_WHERE, ShowDetailsPageFragment.TAG);
                    bundle.putBoolean(Constants.IS_LOGGED_IN, true);
                    //  bundle.putBoolean(Constants.IS_FROM_DETAIL_PAGE,true);
                    subscriptionWebViewFragment.setArguments(bundle);
                    Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);

                    //Helper.addFragmentForDetailsScreen(getActivity(),new SubscriptionWebViewFragment(), SubscriptionWebViewFragment.TAG);
                } else {
                    SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FROM_WHERE, ShowDetailsPageFragment.TAG);
                    bundle.putBoolean(Constants.IS_LOGGED_IN, false);
                    //   bundle.putBoolean(Constants.IS_FROM_DETAIL_PAGE,true);
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
        Intent intent = new Intent(getActivity(), OnBoardingActivity.class);
        intent.putExtra(Constants.FROM_PAGE, MovieDetailsFragment.TAG);
        startActivityForResult(intent, Constants.ACTION_LOGIN_CLICKED);
    }

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

                try {
                    YPlayer.seekToMillis(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                viewHolder.youtubehide.setVisibility(View.GONE);
                viewHolder.play_icon_youtube_replay.setVisibility(View.GONE);
            }
        });
        youTubePlayerFragment.initialize("AIzaSyDWuMjLUr3juGELVAKT5lGRcT4WOYllCUI", new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

                if (!wasRestored) {
                    YPlayer = player;
                    analytics_unique_id = UUID.randomUUID().toString();

//                    YPlayer.setFullscreen(true);
//                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    if (currentItem != null && currentItem.getPlayUrl() != null
                            && currentItem.getPlayUrl().getUrl() != null) {
                        YPlayer.loadVideo(currentItem.getPlayUrl().getUrl().trim());
                        YPlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                            @Override
                            public void onPlaying() {
                                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                                ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
                                viewHolder.youtubehide.setVisibility(View.GONE);
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
                                    String urlType = currentItem.getPlayUrlType();
                                    if (!TextUtils.isEmpty(urlType) && urlType.equalsIgnoreCase("youtube")) {
                                        playerType = "youtube";
                                    }
                                    titleEvents = new TitleEvents(categoryName, categoryID, "", "",
                                            titleName, contentType, titleImage, titleID);
                                    webEngageAnalytics.titleStartedEvent(titleEvents);


                                    mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY, currentItem.getLanguage(),
                                            MEDIA_TYPE, currentItem.getGenres().get(0), currentItem.getContentId(), currentItem.getCatalogName(),
                                            YPlayer.getDurationMillis() / 1000, currentItem.getTitle(),
                                            PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                                    mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                                    mAnalytics.initProvider(analytics_unique_id, currentItem.getDisplayTitle(), adaptiveURL, MEDIA_TYPE, playerType,
                                            analytics_time_spent + "", YPlayer.getDurationMillis() / 1000 + "",
                                            YPlayer.getCurrentTimeMillis() / 1000 + "",
                                            0 + "", 0 + "",
                                            getResources().getConfiguration().orientation + "", currentItem.getPlayUrl().getYoutube().getUrl().trim(),
                                            currentItem.getLanguage(),
                                            currentItem.getCatalogObject().getPlanCategoryType() + "",
                                            currentItem.getTheme(), currentItem.getGenres().get(0),
                                            currentItem.getCatalogName(), Constants.CURRENT_BITRATE + "", userAge,
                                            userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId,
                                            null, currentItem.getContentId(), getActivity(), "");

                                    mAnalytics.setmVideoStartTime(Calendar.getInstance().getTime());

                                    if (!IS_PLAY_EVENT_FIRED) {
                                        mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
                                        IS_PLAY_EVENT_FIRED = true;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                duration = String.valueOf(YPlayer.getDurationMillis());
                                currentTime = String.valueOf(YPlayer.getCurrentTimeMillis());
                                startAnalyticsTimer_youtube(YPlayer.getDurationMillis()
                                        - YPlayer.getCurrentTimeMillis(), Constants.ANALYTICS_INTERVAL);
                            }

                            @Override
                            public void onVideoEnded() {
                                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                                webEngageAnalytics.titleCompletedEvent(titleEvents);

                                mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY_COMPLETE, currentItem.getLanguage(),
                                        MEDIA_TYPE, currentItem.getGenres().get(0), currentItem.getContentId(), currentItem.getCatalogName(),
                                        YPlayer.getDurationMillis() / 1000, currentItem.getTitle(),
                                        PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                                mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);


                                viewHolder.youtubehide.setVisibility(View.VISIBLE);
                                viewHolder.play_icon_youtube_replay.setVisibility(View.VISIBLE);
                                viewHolder.play_icon_youtube.setVisibility(View.GONE);
                                mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.finish);
                                Log.d("YPlayer", "onVideoEnded");
                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {

                                Log.d("YPlayer", "onError" + errorReason.toString());
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
                if (arg1.isUserRecoverableError()) {
                    arg1.getErrorDialog(getActivity(), 10101).show();
                } else {
                    Log.d("YPlayer", "onError");
                    //String error = String.format(getString(R.string.player_error), errorReason.toString());
                    //Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                }
            }
        });
        transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

    }

    private void play() {
        // TODO: 2019-09-26 check for which player before proceeding here
        String urlType = currentItem.getPlayUrlType();
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
                }
            } else if (!TextUtils.isEmpty(adaptiveURL)) {
//            Log.e(TAG, "play: " + viewHolder.mInstaPlayView.getDuration());
                viewHolder.mInstaPlayView.play();
                viewHolder.mPlayIcon.setVisibility(View.GONE);
                viewHolder.mImage.setVisibility(View.GONE);
                viewHolder.mPremium.setVisibility(View.GONE);
            } else {
                Helper.showToast(getActivity(), "Unable to Play! , Please try after some time", R.drawable.ic_error_icon);
            }

            if (currentItem.getMediaType() != null && currentItem.getMediaType().equalsIgnoreCase("audio")) {
                try {
                    ImageView audioImage = viewHolder.mInstaPlayView.getInstaViewRef().findViewById(R.id.audioplayerbgimage);
                    Picasso.get().load(ThumnailFetcher.fetchAppropriateThumbnail(currentItem.getThumbnails(), Constants.T_16_9_BANNER)).placeholder(R.drawable.place_holder_16x9).into(audioImage);
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

    public InstaPlayView getPlayerInfo() {

        if (getActivity() != null && viewHolder != null && viewHolder.mInstaPlayView != null) {
            return viewHolder.mInstaPlayView;
        }

        return null;

    }

    private class EventListener implements InstaPlayView.PlayerEventListener,
            InstaPlayView.PlayerUIListener, InstaPlayView.MediaTrackEventListener, InstaPlayView.AdPlaybackEventListener {

        @Override
        public void OnError(int i, String s) {
            mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.error);
            removeHeartBeatRunnable();
        }

        @Override
        public void OnCompleted() {

            if (!IS_PREVIEW_PLAYING) {
                mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.finish);
                titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                webEngageAnalytics.titleCompletedEvent(titleEvents);

                mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY_COMPLETE, currentItem.getLanguage(),
                        MEDIA_TYPE, currentItem.getGenres().get(0), currentItem.getContentId(), currentItem.getCatalogName(),
                        viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getTitle(),
                        PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                // mAnalytics.initProvider(viewHolder.mInstaPlayView.getId() + "", currentItem.getTitle(), adaptiveURL, "Video", "InstaPlay", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL);
                //mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
                /*Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAnalyticsTimer(viewHolder.mInstaPlayView.getDuration() - viewHolder.mInstaPlayView.getCurrentPosition(), Constants.ANALYTICS_INTERVAL);
                        startActiveMediaTimer(viewHolder.mInstaPlayView.getDuration() - viewHolder.mInstaPlayView.getCurrentPosition(), ANALYTICS_ACTIVE_INTERVAL);

                    }
                }, Constants.ANALYTICS_INTERVAL);*/
                CUR_PROGRESS_TIME = 0;
                reportHeartBeat(0);
                removeHeartBeatRunnable();
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

        private void startActiveMediaTimer(long l, long analytics_active_interval) {
//            if (mActiveCountDown != null) {
//                mActiveCountDown.cancel();
//                mActiveCountDown = null;
//            }
//
//
////            l = analytics_active_interval;  // as they want it only once per video
//            mActiveCountDown = new CountDownTimer(l, analytics_active_interval) {
//                @Override
//                public void onTick(long l) {
////                    Log.d(TAG, "!onTick: mediaactive");
//                    mAnalytics.updateMediaActive(viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "");
//                }
//
//                @Override
//                public void onFinish() {
//
//                }
//            }.start();

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


        @Override
        public void OnPlay() {
            if (handler != null) {
                handler.removeCallbacks(heartBeatRunnable);
                handler.postDelayed(heartBeatRunnable, HEARTBEAT_RATE);
            }


//            Log.d(TAG, "!Calling on play..");
            if (!IS_PREVIEW_PLAYING) {

                viewHolder.mInstaPlayView.setForwardTimeVisible(true);
                viewHolder.mInstaPlayView.setRewindVisible(true);

                mAnalytics.reportVideoLaunchTime();

                /*Analytics code should be started only during actual content playback not for preview*/

                if (getActivity() == null)
                    return;

                String userAge = PreferenceHandler.getUserAge(getActivity());
                String gender = PreferenceHandler.getUserGender(getActivity());
                String userPeriod = PreferenceHandler.getUserPeriod(getActivity());
                String userPackName = PreferenceHandler.getPackName(getActivity());
                String userPlanType = PreferenceHandler.getUserPlanType(getActivity());
                String userId = PreferenceHandler.getAnalyticsUserId(getActivity());

                updateUserState();

                try {
                    titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage, titleID);
                    webEngageAnalytics.titleStartedEvent(titleEvents);
                    mAnalytics.initProvider(analytics_unique_id, currentItem.getTitle(), adaptiveURL, "Video", "InstaPlay", analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getLanguage(), currentItem.getCatalogObject().getPlanCategoryType() + "", currentItem.getTheme(), currentItem.getGenres().get(0), currentItem.getCatalogName(), Constants.CURRENT_BITRATE + "", userAge, gender, USER_STATE, userPeriod, userPlanType, userPackName, userId, viewHolder.mInstaPlayView, currentItem.getContentId(), getActivity(), currentItem.getShowName());


                    if (BUFFER_STARTED) {
                        int buffertime = (int) (Calendar.getInstance().getTimeInMillis() - (int) bufferStartTime);
                        double buff_sec = (buffertime / 1000.0);
                        mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, buff_sec, Constants.EVENT_TYPE_BUFFER, currentItem.getLanguage(),
                                MEDIA_TYPE, currentItem.getGenres().get(0), currentItem.getContentId(), currentItem.getCatalogName(),
                                viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getTitle(),
                                PreferenceHandler.getUserState(getActivity()), userPeriod, userPlanType, userId, "", Constants.CONTENT_OWNER_VALUE, "");
                        mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);
                        mAnalytics.updateBufferDiff(Calendar.getInstance().getTimeInMillis());
                        BUFFER_STARTED = false;
                    }

                    mAnalytics.setmVideoStartTime(Calendar.getInstance().getTime());

                    if (!IS_PLAY_EVENT_FIRED) {
                        Log.d("Onanalytics: play 1", "" + AnalyticsProvider.EventName.play.toString());
                        mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 0, Constants.EVENT_TYPE_PLAY, currentItem.getLanguage(),
                                MEDIA_TYPE, currentItem.getGenres().get(0), currentItem.getContentId(), currentItem.getCatalogName(),
                                viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getTitle(),
                                PreferenceHandler.getUserState(getActivity()), userPeriod, userPlanType, userId, currentItem.getShowName(), Constants.CONTENT_OWNER_VALUE, "");
                        mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);
                        mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
                        startActiveMediaTimer(viewHolder.mInstaPlayView.getDuration() - viewHolder.mInstaPlayView.getCurrentPosition(), ANALYTICS_ACTIVE_INTERVAL);
                        IS_PLAY_EVENT_FIRED = true;
                    }


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
                            if (!IS_CAST_CONNECTED)
                                viewHolder.mSkipPreview.setVisibility(View.VISIBLE);
                    }
                }, 5000);

            }
        }

        @Override
        public void OnPaused() {
            if (!IS_PREVIEW_PLAYING) {
                reportHeartBeat(viewHolder.mInstaPlayView.getCurrentPosition());

                if (mCountdown != null && mAnalytics != null) {
                    mCountdown.cancel();
                    mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.pause);
                }
                // mAnalytics.initProvider(viewHolder.mInstaPlayView.getId() + "", currentItem.getTitle(), adaptiveURL, "Video", "InstaPlay", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL);
            }
            removeHeartBeatRunnable();
        }

        @Override
        public void OnBuffering() {
            if (!IS_PREVIEW_PLAYING) {
                BUFFER_STARTED = true;
                bufferStartTime = Calendar.getInstance().getTimeInMillis();
                mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.buffering);
                mAnalytics.updateBufferStartTime(Calendar.getInstance().getTimeInMillis());
                // mAnalytics.initProvider(viewHolder.mInstaPlayView.getId() + "", currentItem.getTitle(), adaptiveURL, "Video", "InstaPlay", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL);
                //mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
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
                    Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
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
                        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
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


    private void startAnalyticsTimer(long l, long analyticsInterval) {
        if (mCountdown != null) {
            mCountdown.cancel();
            mCountdown = null;
        }
        mCountdown = new CountDownTimer(l, analyticsInterval) {
            @Override
            public void onTick(long l) {
                try {
                    if (viewHolder.mInstaPlayView.isPlaying()) {

                        analytics_time_spent = analytics_time_spent + 10;
                        mAnalytics.updateMedia(MEDIA_TYPE, AnalyticsProvider.EventName.seek, analytics_time_spent + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", getResources().getConfiguration().orientation == 1 ? "1" : "0", viewHolder.mInstaPlayView);

                        mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 30, Constants.EVENT_TYPE_PLAYBACK_TIME, currentItem.getLanguage(),
                                MEDIA_TYPE, currentItem.getGenres().get(0), currentItem.getContentId(), currentItem.getCatalogName(),
                                viewHolder.mInstaPlayView.getDuration() / 1000, currentItem.getTitle(),
                                PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                        mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

                        //mAnalytics.updateMedia(MEDIA_TYPE, AnalyticsProvider.EventName.seek,l / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", getResources().getConfiguration().orientation == 1 ? "1" : "0");
                        titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType,
                                titleImage, titleID, viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", "");
                        webEngageAnalytics.titleProgressEvent(titleEvents);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {

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
                        titleEvents = new TitleEvents(categoryName, categoryID, "", "", titleName, contentType, titleImage,
                                titleID, YPlayer.getCurrentTimeMillis() / 1000 + "", YPlayer.getDurationMillis() / 1000 + "", "");
                        webEngageAnalytics.titleProgressEvent(titleEvents);

                        mediaplaybackEvents = new MediaplaybackEvents(Constants.DEFAULT_EVENT_VALUE, Constants.AN_APP_TYPE, 30, Constants.EVENT_TYPE_PLAYBACK_TIME, currentItem.getLanguage(),
                                MEDIA_TYPE, currentItem.getGenres().get(0), currentItem.getContentId(), currentItem.getCatalogName(),
                                (YPlayer.getDurationMillis() / 1000), currentItem.getTitle(),
                                PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()), "", Constants.CONTENT_OWNER_VALUE, "");
                        mAnalyticsEvent.logMediaEvent(mediaplaybackEvents);

//                    Log.d(TAG, "!Starting analytics timer : " + timeFuture + " Interval : " + interval);
                        analytics_time_spent = analytics_time_spent + 10;
                        mAnalytics.updateMediaYoutube(MEDIA_TYPE, AnalyticsProvider.EventName.seek, analytics_time_spent + "", YPlayer.getCurrentTimeMillis() / 1000 + "", getResources().getConfiguration().orientation == 1 ? "1" : "0", YPlayer);
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

    public void updateOrientationChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewHolder.appBarLayout.setVisibility(View.VISIBLE);
            viewHolder.mScrollLayout.setVisibility(View.VISIBLE);


            if (YPlayer != null && YPlayer.isPlaying()) {
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
            }
            //viewHolder.mPlayerTitleView.setVisibility(View.GONE);
            viewHolder.setWidthHight(viewHolder.mPlayerContainer);
            //Helper.setLightStatusBar(getActivity());

            removeFullScreen();
            Constants.FULLSCREENFLAG = false;
            //Helper.clearLightStatusBar(getActivity());
        } else if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            viewHolder.appBarLayout.setVisibility(View.GONE);
            viewHolder.mScrollLayout.setVisibility(View.GONE);

            if (YPlayer != null) {
                viewHolder.mPlayerTitleView.setVisibility(View.GONE);
                ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
            } else {
                ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
                viewHolder.mPlayerTitleView.setVisibility(View.VISIBLE);
                viewHolder.mInstaPlayView.setFullScreen(true);
            }
            //viewHolder.mPlayerTitleView.setVisibility(View.VISIBLE);

            showFullScreen();
            Constants.donoWhyButNeeded(getActivity());

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
            viewHolder.mPlayerContainer.requestLayout();
            Constants.FULLSCREENFLAG = true;
        }
        if (YPlayer == null)
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
            if (IS_PREVIEW_PLAYING) {
                CUR_HEART_BEAT = viewHolder.mInstaPlayView.getCurrentPosition();
                checkPreviewEnd(CUR_HEART_BEAT);
                if (handler != null)
                    handler.postDelayed(heartBeatRunnable, HEARTBEAT_RATE);
            } else {
                if (viewHolder.mInstaPlayView != null) {
                    if (viewHolder.mInstaPlayView.isPlaying()) {
                        Helper.reportHeartBeat(getActivity(), mApiService, currentItem.getContentId(), currentItem.getCatalogId(), viewHolder.mInstaPlayView.getCurrentPosition());
//                        Log.e(TAG, "run: " + currentItem.getContentId() + " >>> " + currentItem.getCatalogId() + " >>> " + viewHolder.mInstaPlayView.getCurrentPosition());
                        if (handler != null)
                            handler.postDelayed(heartBeatRunnable, HEARTBEAT_RATE);
                    }
                }
            }
        }
    };

    private void reportHeartBeat(long pos) {
        Helper.reportHeartBeat(getActivity(), mApiService, currentItem.getContentId(), currentItem.getCatalogId(), pos);
//        Log.e(TAG, "run: " + currentItem.getContentId() + " >>> " + currentItem.getCatalogId() + " >>> " + pos);
    }

    private void removeHeartBeatRunnable() {
        if (handler != null)
            handler.removeCallbacks(heartBeatRunnable);
    }





    /*PREVIEW*/

    private void playPreviewContent(Preview preview) {
        if (preview != null) {

            if (!TextUtils.isEmpty(preview.getExtPreviewUrl())) {       // External URL should come
                IS_EXT_PREV_URL = true;
                getAddsSmartURL(preview.getExtPreviewUrl());

            } else if (preview.isPreviewAvailable()) {


               /* previewStartTime = Constants.parseTimeToMillis(preview.getPreviewStart());
                previewEndTime = Constants.parseTimeToMillis(preview.getPreviewEnd());*/

                if (previewStartTime > -1 && previewEndTime > -1 && previewEndTime > previewStartTime) {
                    if (!TextUtils.isEmpty(adaptiveURL))
                        setupPlayContent(adaptiveURL);
                    Helper.showToast(getActivity(), getString(R.string.playing_preview), R.drawable.ic_error_icon);
                    viewHolder.mImage.setVisibility(View.GONE);
                    viewHolder.mPlayIcon.setVisibility(View.GONE);
                    viewHolder.mPremium.setVisibility(View.GONE);
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

    private void checkPreviewEnd(long curHeartBeat) {

        if (IS_EXT_PREV_URL)    // If external preview dont do below checks remaining chks in onCompleted
            return;

        if (curHeartBeat > previewEndTime) {
            finishPreview();
        }

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
                    Helper.showToast(getActivity(), getString(R.string.playing_preview), R.drawable.ic_error_icon);

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

        resetPlayer();

        EventListener mEventListener = new EventListener();
        viewHolder.mInstaPlayView.addPlayerEventListener(mEventListener);
        viewHolder.mInstaPlayView.addPlayerUIListener(mEventListener);
        viewHolder.mInstaPlayView.addMediaTrackEventListener(mEventListener);

        InstaMediaItem mMediaItem;

        mMediaItem = new InstaMediaItem(adaptiveURL);

        viewHolder.mInstaPlayView.setCastContext(mCastContext);
        mMediaItem.setInstaCastItem(getCastMediaItem(adaptiveURL));
        viewHolder.mInstaPlayView.setMediaItem(mMediaItem);
        viewHolder.mInstaPlayView.prepare();

        viewHolder.mInstaPlayView.setLanguageVisibility(false);
        viewHolder.mInstaPlayView.setCaptionVisibility(false);
        viewHolder.mInstaPlayView.setHDVisibility(false);
        viewHolder.mInstaPlayView.setMuteVisible(false);
        viewHolder.mInstaPlayView.setSeekBarVisibility(false);
        viewHolder.mInstaPlayView.setCurrentProgVisible(false);
        viewHolder.mInstaPlayView.setDurationTimeVisible(false);
        viewHolder.mInstaPlayView.setForwardTimeVisible(false);
        viewHolder.mInstaPlayView.setRewindVisible(false);
        viewHolder.mInstaPlayView.setQualityVisibility(false);
        viewHolder.mInstaPlayView.play();


        viewHolder.mInstaPlayView.setBufferVisibility(true);

        viewHolder.mInstaPlayView.setVideoQualitySelectionText(InstaPlayView.VIDEO_QUALITY_TEXT_HEIGHT, InstaPlayView.SORT_ORDER_DESCENDING);

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && (YPlayer == null || !YPlayer.isPlaying()))
            viewHolder.mInstaPlayView.setFullScreen(true);

        viewHolder.mImage.setVisibility(View.GONE);
        viewHolder.mPlayIcon.setVisibility(View.GONE);
        viewHolder.mPremium.setVisibility(View.GONE);

    }

    private void getAdsIfPresent(Item currentItem) {
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

    private void finishPreview() {
        IS_EXT_PREV_URL = false;
        IS_PREVIEW_PLAYING = false;
        viewHolder.mInstaPlayView.stop();
        viewHolder.mInstaPlayView.release();
        viewHolder.mImage.setVisibility(View.VISIBLE);
        viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
        viewHolder.mSkipPreview.setVisibility(View.GONE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (IS_FOR_LOGIN)
            IS_PREVIEW_PLAYED = true;
        else
            return;

        //showLoginPopUp();
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

    public void fireScreenView(boolean isFirstEpisode) {
        if (isFirstEpisode) {
            mAnalytics.fireScreenView(AnalyticsProvider.Screens.ShowDetailsScreen);
            pageEvents = new PageEvents(Constants.ShowDetailsScreen);
            webEngageAnalytics.screenViewedEvent(pageEvents);
        } else {
            mAnalytics.fireScreenView(AnalyticsProvider.Screens.EpisodeDetailsScreen);
            pageEvents = new PageEvents(Constants.EpisodeDetailsScreen);
            webEngageAnalytics.screenViewedEvent(pageEvents);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        viewHolder.mSkipPreview.setVisibility(View.GONE);
        mCastContext.addCastStateListener(mCastStateListener);
        Log.e("moviedetailepage", "inside onsresume");
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

    Handler handlerAds = new Handler();
    Runnable runnableAds = new Runnable() {
        @Override
        public void run() {
            if (mAdmanagerInterstitialAd != null && isAdAvailableOnGoogle) {
                mAdmanagerInterstitialAd.show(getActivity());
            } else {
                Log.d("Intertialads", "The interstitial ad wasn't ready yet.");
            }
        }
    };

    public void showAds() {
        handlerAds.postDelayed(runnableAds, Constants.ADS_WAITTIME);
    }
}
