package com.otl.tarangplus.fragments;


import android.app.Dialog;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.TrailerObject;
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
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Database.LayoutDbScheme;
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
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.GenericResult;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.Listitem;
import com.otl.tarangplus.model.PlayList;
import com.otl.tarangplus.model.PlayListResponse;
import com.otl.tarangplus.model.Preview;
import com.otl.tarangplus.model.ShowDetailsResponse;
import com.otl.tarangplus.model.UserInfo;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.DetailsPageViewModel;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static com.otl.tarangplus.Utils.Constants.HEARTBEAT_RATE;

public class TrailerDetailfragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = TrailerDetailfragment.class.getName();
    private static boolean IS_PLAY_EVENT_FIRED = false;
    private boolean IS_SUBSCRIBED = false;
    private long CUR_HEART_BEAT = 0;

    private boolean IS_PREVIEW_PLAYING = false;
    public boolean IS_PREVIEW_PLAYED = false;
    public boolean IS_FOR_LOGIN = false;

    private DetailsPageViewModel mViewModel;
    private ViewHolder viewHolder;
    private String adaptiveURL = "";
    private String displayTitle;
    private EventListener mEventListener;

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

    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trailer_details, container, false);
        viewHolder = new ViewHolder(view);
        hideUI();
        genericResult = new GenericResult();
        RestClient mRestClient = new RestClient(getActivity());
        mApiService = mRestClient.getApiService();
        webEngageAnalytics = new WebEngageAnalytics();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //Helper.clearLightStatusBar(getActivity());
        /*** Analytics **/
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

        return view;
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
                                    getMoreShows(catalogId, genres);

                                    // TODO: 30/05/19 checking and later need to be removed 
                                    getAssociatedVideos(catalogId,genres);
                                }
                            }
                            setUpUI(detailsPageResponse, additionalData);
                            showUI();
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

    private boolean isItemInWatchLater = false;
    private String listIdSelected = "";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity) getActivity()).setSelectedNavUI(Constants.TABS.HOME);   // todo TBR
        mViewModel =  ViewModelProviders.of(this).get(DetailsPageViewModel.class);
        viewHolder.mMoreItemView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewHolder.mMoreItemView.setLayoutManager(manager);


        viewHolder.mAssociatedItemView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager managers = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewHolder.mAssociatedItemView.setLayoutManager(managers);



        getDetails();
        viewHolder.mPreview.setOnClickListener(new View.OnClickListener() {
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

                    //playPreviewContent(currentItem.getData().getPreview());   // On preview button click

                } else {
                    Helper.showToast(getActivity(), "Preview Not Available!", R.drawable.ic_error_icon);
                }
            }
        });

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
                    showLoginConfirmationPopUp();
                    return;
                }
                if (isItemInWatchLater) {
                    deleteItemFromWatchLater();
                } else {
                    addToWatchlater();
                }
            }
        });
        viewHolder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareUrl = currentItem.getData().getShareUrl();
                if (!TextUtils.isEmpty(shareUrl)) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    String shareText = "Hey !" +
                            "\n" +
                            "I'm watching some really amazing videos on ShemarooMe. Check this one. It's one of my favourites!";
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
            viewHolder.mMoreItemView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!viewHolder.mMoreItemView.canScrollHorizontally(1) && !IS_LAST_ELEMENT) {
//                        getDetails();

                        if (currentItem != null) {
                            String catalogId = currentItem.getData().getCatalogId();
                            List<String> genres = currentItem.getData().getGenres();
                            getMoreShows(catalogId, genres);
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
                            getMoreShows(catalogId, genres);
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

    private void addToWatchlater() {
        AddPlayListItems addPlayListItems = new AddPlayListItems();
        Listitem listitem = new Listitem();
        final String content_id = getArguments().getString(Constants.CONTENT_ID);
        final String catalog_id = getArguments().getString(Constants.CATALOG_ID);
        listitem.setCatalogId(catalog_id);
        listitem.setContentId(content_id);
        addPlayListItems.setAuthToken(Constants.API_KEY);
        addPlayListItems.setListitem(listitem);
        mApiService.setWatchLater(PreferenceHandler.getSessionId(getActivity()), Constants.WATCHLATER,addPlayListItems).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        isItemInWatchLater = true;
                        JsonArray data = jsonObject.getAsJsonArray("data");
                        for (JsonElement je : data) {
                            listIdSelected = je.getAsJsonObject().get("listitem_id").getAsString();
                        }

//                        playListInstance = PlaylistCheck.getPlayListInstance();
//                        if (playListInstance != null) {
//                            playListInstance.updatePlayList(addPlayListItems.getListitem().getContentId());
//                        }
                        Helper.dismissProgressDialog();
                        viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watch_list_sel);
                        Helper.showToast(getActivity(), getString(R.string.added_to_watch_list), R.drawable.ic_check);

                        mAnalytics.reportAddToWatchList();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        isItemInWatchLater = false;
                        Helper.dismissProgressDialog();
                        Helper.showToast(getActivity(), Constants.getErrorMessage(throwable).getError().getMessage(), R.drawable.ic_cross);
                    }
                });

    }

    private void deleteItemFromWatchLater() {
        Helper.showProgressDialog(getActivity());
        mApiService.removeWatchLaterItem(PreferenceHandler.getSessionId(getActivity()),""
                , listIdSelected)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Helper.dismissProgressDialog();
                        isItemInWatchLater = false;
                        Helper.showToast(getActivity(), "Item deleted from Watch later list", R.drawable.ic_check);
                        viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watchlist_deactive);

                        mAnalytics.reportRemoveFromWatchList();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        throwable.printStackTrace();
                        isItemInWatchLater = true;
                        Helper.dismissProgressDialog();
                        Helper.showToast(getActivity(), Constants.getErrorMessage(throwable).getError().getMessage(), R.drawable.ic_cross);
                    }
                });
    }

    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {
        if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
                Picasso.get().load(layoutDbScheme.getImageUrl()).into(viewHolder.mTopbarImage);
                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            } else {
                Picasso.get().load(R.color.white).into(viewHolder.mTopbarImage);
                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            }
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

    public void getAssociatedVideos(String catalogId, List<String> genres) {
        if (!TextUtils.isEmpty(catalogId) && genres.size() > 0)

            //todo need to change this for the associated data.
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
                                        updateRecyclerViewForAssociated(detailsPageResponse);
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
            viewHolder.metaDataHolder.addView(inflate);
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

    private void showMoreInShowListing(ListResonse detailsPageResponse) {

        if (PAGEINDEX < 1) {
            itemAdapter = new MoreInShowThemeAdapter(getActivity());
            viewHolder.mMoreItemView.setAdapter(itemAdapter);
        }

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
            itemAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());
            if (detailsPageResponse.getData().getItems().size() + 1 < Constants.PAGE_LIST_LIMIT) {
                IS_LAST_ELEMENT = true;
            }

            itemAdapter.setOnItemClickListener(new MoreInShowThemeAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Item item) {
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
                }
                resetPreviewFlags();
                getDetails();
            }
        });
    }


    private void showMoreInShowListingAssociated(ListResonse detailsPageResponse) {

        if (PAGEINDEX < 1) {
            itemAdapter = new MoreInShowThemeAdapter(getActivity());
            viewHolder.mAssociatedItemView.setAdapter(itemAdapter);
        }

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
            itemAdapter.appendUpdateItems(detailsPageResponse.getData().getItems());
            if (detailsPageResponse.getData().getItems().size() + 1 < Constants.PAGE_LIST_LIMIT) {
                IS_LAST_ELEMENT = true;
            }

            itemAdapter.setOnItemClickListener(new MoreInShowThemeAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Item item) {
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
                    }

                    resetPreviewFlags();
                    getDetails();
                }
            });
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
                }
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


    private void setUpUI(ShowDetailsResponse detailsPageResponse, final Map<String, ArrayList> additionalData) {
        if (detailsPageResponse != null) {
            Data data = detailsPageResponse.getData();
            if (data != null) {

                if (data.getItemCaption() != null) {
                    viewHolder.mMetaData.setVisibility(View.VISIBLE);
                    viewHolder.mMetaData.setText(data.getItemCaption());
                } else {
                    viewHolder.mMetaData.setVisibility(View.GONE);
                }


                if (data.getGenres().size() > 0) {
                    //viewHolder.mMoreTitle.setText("All Related Videos");
                    viewHolder.mMoreTitle.setText("More in " + data.getGenres().get(0));
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


//                if (!IS_CAST_CONNECTED /*|| !viewHolder.mInstaPlayView.isPlaying()*/) {
                viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
                viewHolder.mImage.setVisibility(View.VISIBLE);
//                }


                viewHolder.mSkipPreview.setVisibility(View.GONE);

                if (data.getTitle() != null) {
                    viewHolder.header.setText(data.getTitle());
                    viewHolder.mPlayerTitleTxt.setText(data.getTitle());
                    displayTitle = data.getTitle();
                }
                if (PreferenceHandler.isLoggedIn(getActivity()))
                    getUserStates(data);
//                    getCurHeartBeat(data);
                else
                    getSmartURL(data);

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

                if (data.getPreview() != null && data.getPreview().isPreviewAvailable())
                    viewHolder.mPreview.setVisibility(View.VISIBLE);
                else
                    viewHolder.mPreview.setVisibility(View.GONE);
            }
        }
    }

    private void getUserStates(Data data) {
        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
        if (!loggedIn) {
            return;
        }
        setWatchLaterUI("");
        CUR_PROGRESS_TIME = 0;
        IS_SUBSCRIBED = false;
        IS_MEDIA_ACTIVE_SENT = false;
        IS_PLAY_EVENT_FIRED = false;

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
                                    }

                                } else {
//                                    Not in WatchHistory or Not in Continue watching
                                    setWatchLaterUI("");
                                }

                                IS_SUBSCRIBED = response.isSubscribed();

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

                                    mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, "InstaPlay", analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(), currentItem.getData().getCatalogObject().getPlanCategoryType() + "", currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0), currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", age, gender, USER_STATE, userPeriod, userPlanType, userPackName, analyticsUserId, viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(),"");
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
                                getSmartURL(data);
                            }
                    }
                });
    }

    private void setWatchLaterUI(String listItemId) {
        if (!TextUtils.isEmpty(listItemId)) {
            viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watch_list_sel);
            isItemInWatchLater = true;
            listIdSelected = listItemId;
        } else {
            listIdSelected = "";
            isItemInWatchLater = false;
            viewHolder.mWatchlaterImage.setImageResource(R.drawable.ic_watchlist_deactive);
        }
    }

    public void getSmartURL(Data currentItem) {
        // TODO: 25/09/18 Should change Auth Token
        adds = null;  // Resetting Ads state

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

        if(TextUtils.isEmpty(smartUrl)){
            viewHolder.mPlayIcon.setVisibility(View.GONE);
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

                    if (getArguments() != null && getArguments().getString(Constants.FROM_PAGE) != null && getArguments().getString(Constants.FROM_PAGE).equals(ContinueWatchingFragment.TAG))
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
        if (adds == null || IS_SUBSCRIBED || IS_PREVIEW_PLAYING) {
            mMediaItem = new InstaMediaItem(adaptiveURL);
        } else {
            mMediaItem = new InstaMediaItem(adaptiveURL, adds);
        }

        mMediaItem.setInstaCastItem(getCastMediaItem(adaptiveURL));
        viewHolder.mInstaPlayView.setMediaItem(mMediaItem);
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
            viewHolder.mInstaPlayView.play();
        }

        viewHolder.mInstaPlayView.setBufferVisibility(true);

        viewHolder.mInstaPlayView.setVideoQualitySelectionText(InstaPlayView.VIDEO_QUALITY_TEXT_HEIGHT, InstaPlayView.SORT_ORDER_DESCENDING);

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            viewHolder.mInstaPlayView.setFullScreen(true);

    }


    private void resetPlayerShowImage() {
        if (viewHolder.mInstaPlayView != null) {
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
                AnalyticsProvider.sendMediaEventToCleverTap(AnalyticsProvider.getCleverTapData(getActivity(), pos, currentItem.getData()), getActivity());
                mAnalytics.branchMediaClickData(getActivity(),pos);
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
        if (!IS_CAST_CONNECTED) {
            viewHolder.mInstaPlayView.stop();
            viewHolder.mInstaPlayView.release();
        }
//        viewHolder.mInstaPlayView.stop();
        viewHolder.mInstaPlayView.release();
        super.onDestroy();
    }

    public void updateOrientationChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewHolder.appBarLayout.setVisibility(View.VISIBLE);
            viewHolder.mScrollLayout.setVisibility(View.VISIBLE);
            viewHolder.mPlayerTitleView.setVisibility(View.GONE);
            viewHolder.mInstaPlayView.setFullScreen(false);
            viewHolder.setWidthHeight(viewHolder.mPlayerContainer);
            Helper.setLightStatusBar(getActivity());
            //Helper.clearLightStatusBar(getActivity());
        } else if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            viewHolder.appBarLayout.setVisibility(View.GONE);
            viewHolder.mScrollLayout.setVisibility(View.GONE);
            viewHolder.mInstaPlayView.setFullScreen(true);
            viewHolder.mPlayerTitleView.setVisibility(View.VISIBLE);

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
        }
    }


    public void showFullScreen() {
        if (getActivity() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | SYSTEM_UI_FLAG_FULLSCREEN
                            | SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onRefresh() {
        resetPlayerShowImage();
        PAGEINDEX = 0;
        getDetails();
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


        @BindView(R.id.preview)
        LinearLayout mPreview;
        @BindView(R.id.watchLater)
        LinearLayout mWatchLater;
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
        @BindView(R.id.premium)
        ImageView mPremium;

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

            mAssociatedTitle.setOnClickListener(new View.OnClickListener() {
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
                                Helper.addFragmentForDetailsScreen(getActivity(), fragment, MoreListingFragment.TAG + "Trailer");
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
                        mDescription.setMaxLines(2);
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


                mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, "InstaPlay", analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(), currentItem.getData().getCatalogObject().getPlanCategoryType() + "", currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0), currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", userAge, userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId, viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(),"");

                mAnalytics.firstPlayClick();
            } catch (Exception e) {

            }

        }
    }

    private void play() {

        if (!viewHolder.mInstaPlayView.isPlayerActive()) {
            if (!TextUtils.isEmpty(adaptiveURL)) {
                setupPlayContent(adaptiveURL);
                viewHolder.mPlayIcon.setVisibility(View.GONE);
                viewHolder.mImage.setVisibility(View.GONE);
                viewHolder.mPremium.setVisibility(View.GONE);
            }
        } else if (!TextUtils.isEmpty(adaptiveURL)) {
//            Log.e(TAG, "play: " + viewHolder.mInstaPlayView.getDuration());
            viewHolder.mInstaPlayView.seekTo(CUR_PROGRESS_TIME);
            viewHolder.mInstaPlayView.play();
            viewHolder.mPlayIcon.setVisibility(View.GONE);
            viewHolder.mImage.setVisibility(View.GONE);
            viewHolder.mPremium.setVisibility(View.GONE);
        } else {
            Helper.showToast(getActivity(), "Unable to Play! , Please try after some time", R.drawable.ic_error_icon);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewHolder.mSkipPreview.setVisibility(View.GONE);

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

    private void showLoginPopUp() {

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
                    subscriptionWebViewFragment.setArguments(bundle);
                    Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
                } else {
                    goToLoginPage();
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
//                isAccessLoginReq = true;
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
                                //                        Removed as preview logic is not needed during play button
                                /*if (currentItem.getData().getPreview() != null && currentItem.getData().getPreview().isPreviewAvailable()) {
                                    if (IS_PREVIEW_PLAYED)
                                        showLoginPopUp();
                                    else {
                                        IS_PREVIEW_PLAYING = true;
                                        IS_FOR_LOGIN = true;
                                        playPreviewContent(currentItem.getData().getPreview());  // Access control -> login Needed !isFree -> !subscribed

                                    }
                                } else*/
                                showLoginPopUp();
                            }
                        } else
                            play();
                    } else {
                        //                        Removed as preview logic is not needed during play button
                       /* if (currentItem.getData().getPreview() != null && currentItem.getData().getPreview().isPreviewAvailable()) {
                            if (IS_PREVIEW_PLAYED)
                                showLoginPopUp();
                            else {
                                IS_PREVIEW_PLAYING = true;
                                IS_FOR_LOGIN = true;
                                playPreviewContent(currentItem.getData().getPreview());   // login needed but not logged in

                            }
                        } else*/
                        showLoginPopUp();
                    }

                } else if (!isAccessFree) {
                    if (IS_SUBSCRIBED)
                        play();         // Playing for now
                    else {
//                        Removed as preview logic is not needed during play button
                        /*if (currentItem.getData().getPreview() != null && currentItem.getData().getPreview().isPreviewAvailable()) {
                            if (IS_PREVIEW_PLAYED)
                                showLoginPopUp();
                            else {
                                IS_PREVIEW_PLAYING = true;
                                IS_FOR_LOGIN = true;
                                playPreviewContent(currentItem.getData().getPreview());  // Access control -> login Not Needed !isFree -> !subscribed

                            }
                        } else*/
                        showLoginPopUp();
                    }
                } else {
                    //                    showLoginPopUp();
                    play();
                }
            }
        }
    }


/*
    private void playPreviewContent(TrailerObject trailerObject) {

        if (trailerObject != null) {
            if (!TextUtils.isEmpty(trailerObject.getExtPreviewUrl())) {         // External URL should come
                IS_EXT_PREV_URL = true;
                getAddsSmartURL(trailerObject.getExtPreviewUrl());

            } else if (trailerObject.isPreviewAvailable()
                    && !TextUtils.isEmpty(trailerObject.getPreviewStart())
                    && !TextUtils.isEmpty(trailerObject.getPreviewStart())) {

                previewStartTime = Constants.parseTimeToMillis(trailerObject.getPreviewStart());
                previewEndTime = Constants.parseTimeToMillis(trailerObject.getPreviewEnd());

                if (previewStartTime > -1 && previewEndTime > -1 && previewEndTime > previewStartTime) {
                    if (!TextUtils.isEmpty(adaptiveURL))
                        setupPlayContent(adaptiveURL);
                    viewHolder.mImage.setVisibility(View.GONE);
                    viewHolder.mPlayIcon.setVisibility(View.GONE);
                    viewHolder.mPremium.setVisibility(View.GONE);
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
*/


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
                    mAnalytics.initProvider(analytics_unique_id, displayTitle, adaptiveURL, MEDIA_TYPE, "InstaPlay", analytics_time_spent + "", viewHolder.mInstaPlayView.getDuration() / 1000 + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", viewHolder.mInstaPlayView.getWidth() + "", viewHolder.mInstaPlayView.getHeight() + "", getResources().getConfiguration().orientation + "", adaptiveURL, currentItem.getData().getLanguage(), currentItem.getData().getCatalogObject().getPlanCategoryType() + "", currentItem.getData().getTheme(), currentItem.getData().getGenres().get(0), currentItem.getData().getCatalogName(), Constants.CURRENT_BITRATE + "", userAge, userGender, USER_STATE, userPeriod, userPlanType, userPackName, userId, viewHolder.mInstaPlayView, currentItem.getData().getContentId(), getActivity(),"");
                    if (BUFFER_STARTED) {
                        mAnalytics.updateBufferDiff(Calendar.getInstance().getTimeInMillis());
                        BUFFER_STARTED = false;
                    }

                    mAnalytics.setmVideoStartTime(Calendar.getInstance().getTime());

                    if (!IS_PLAY_EVENT_FIRED) {
                        mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.play);
                        startActiveMediaTimer(viewHolder.mInstaPlayView.getDuration() - viewHolder.mInstaPlayView.getCurrentPosition(), ANALYTICS_ACTIVE_INTERVAL);
                        IS_PLAY_EVENT_FIRED = true;
                    }

//                    Log.d(TAG, ">>>>>>>>>>> OnPlay: " + AnalyticsProvider.EventName.play);

                } catch (Exception e) {

                }
                startAnalyticsTimer(viewHolder.mInstaPlayView.getDuration() - viewHolder.mInstaPlayView.getCurrentPosition(), Constants.ANALYTICS_INTERVAL);
            } else {
//                Preview is playing
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 08/03/19 show skip button
                        if (IS_PREVIEW_PLAYING && viewHolder.mInstaPlayView.isPlaying())
                            if (!IS_CAST_CONNECTED)
                                viewHolder.mSkipPreview.setVisibility(View.GONE);
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
                        mAnalytics.updateMedia(MEDIA_TYPE, AnalyticsProvider.EventName.seek, analytics_time_spent + "", viewHolder.mInstaPlayView.getCurrentPosition() / 1000 + "", getResources().getConfiguration().orientation == 1 ? "1" : "0", viewHolder.mInstaPlayView);
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
    private static long CUR_PROGRESS_TIME = 0;

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
        viewHolder.mPlayIcon.setVisibility(View.VISIBLE);
        viewHolder.mSkipPreview.setVisibility(View.GONE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (IS_FOR_LOGIN)
            IS_PREVIEW_PLAYED = true;
        else
            return;

        showLoginPopUp();
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
        viewHolder.mInstaPlayView.play();


        viewHolder.mInstaPlayView.setBufferVisibility(true);
        viewHolder.mInstaPlayView.setLanguageVisibility(false);
        viewHolder.mInstaPlayView.setCaptionVisibility(false);
        viewHolder.mInstaPlayView.setHDVisibility(false);
        viewHolder.mInstaPlayView.setVideoQualitySelectionText(InstaPlayView.VIDEO_QUALITY_TEXT_HEIGHT, InstaPlayView.SORT_ORDER_DESCENDING);

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            viewHolder.mInstaPlayView.setFullScreen(true);

        viewHolder.mImage.setVisibility(View.GONE);
        viewHolder.mPlayIcon.setVisibility(View.GONE);
        viewHolder.mPremium.setVisibility(View.GONE);

    }

    private void showLoginConfirmationPopUp() {

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
        warningMessage.setText(R.string.add_to_watchlist_login);
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
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.TrailerDetailsScreen);
        pageEvents = new PageEvents(Constants.TrailerDetailsScreen);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }


    public interface OnEventListener {
        void onEvent(boolean b);
    }

    private static OnEventListener mOnEventListener;

    public static void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

}


