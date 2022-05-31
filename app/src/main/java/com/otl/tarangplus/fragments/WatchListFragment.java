package com.otl.tarangplus.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.WatchHistoryAdapter;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WatchListFragment extends Fragment {

    public static final String TAG = WatchListFragment.class.getSimpleName();
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
    @BindView(R.id.who_is_watching_list)
    RecyclerView who_is_watching_list;

    @BindView(R.id.browse_shemaroo)
    GradientTextView browseShemaroo;
    @BindView(R.id.no_watchlist_container)
    RelativeLayout noWatchlistContainer;
    @BindView(R.id.watch_descr_text)
    MyTextView watchDescrText;
    @BindView(R.id.fav_descr_text)
    MyTextView favDescrText;
    @BindView(R.id.playlist_text)
    MyTextView playlistText;


    private WatchHistoryAdapter watchHistoryAdapter;
    private ApiService mApiService;
    private int PAGE_INDEX = 0;
    private boolean IS_LAST_ITEM = false;
    private String what;
    private String where;
    private String title;
    private String playListId;
    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.watch_list, container, false);
        ButterKnife.bind(this, inflate);
        mApiService = new RestClient(getContext()).getApiService();
        webEngageAnalytics = new WebEngageAnalytics();
        what = getArguments().getString(Constants.WHAT);
        //pageEvents.setPage_Name("Watch List");

        if (!TextUtils.isEmpty(what) && what.equalsIgnoreCase("watchlater")) {
            header.setText(R.string.watch_list);
            where = Constants.WATCH_LATER;
            watchDescrText.setVisibility(View.VISIBLE);
            favDescrText.setVisibility(View.GONE);
            playlistText.setVisibility(View.GONE);
        }else if(!TextUtils.isEmpty(what) && what.equalsIgnoreCase("Playlist")) {
            title = getArguments().getString("title");
            String id = getArguments().getString("playlistId");
            header.setText(title);
            where = id ;
            watchDescrText.setVisibility(View.GONE);
            favDescrText.setVisibility(View.GONE);
            playlistText.setVisibility(View.VISIBLE);
        } else {
            header.setText(R.string.favorite);
            where = Constants.FAVOURITE;
            watchDescrText.setVisibility(View.GONE);
            favDescrText.setVisibility(View.VISIBLE);
            playlistText.setVisibility(View.GONE);
        }
        close.setVisibility(View.GONE);
        setUpRecycleView();
        return inflate;
    }

    private void setUpRecycleView() {
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        watchHistoryAdapter = new WatchHistoryAdapter(getActivity(), listener, where);

        who_is_watching_list.setNestedScrollingEnabled(false);
        who_is_watching_list.setHasFixedSize(true);
        who_is_watching_list.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.px_2),
                (int) getResources().getDimension(R.dimen.px_0),
                (int) getResources().getDimension(R.dimen.px_4),
                (int) getResources().getDimension(R.dimen.px_4)));
        who_is_watching_list.setAdapter(watchHistoryAdapter);
        who_is_watching_list.setLayoutManager(linearLayoutManager);

        //watchHistoryAdapter.setOnUpdateEmptyListener(listener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            who_is_watching_list.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!who_is_watching_list.canScrollVertically(1) && !IS_LAST_ITEM) {
                        getData();
                    }
                }
            });
        }
    }

    WatchHistoryAdapter.UpdateEmptyListener listener = new WatchHistoryAdapter.UpdateEmptyListener() {
        @Override
        public void onAllDelete() {
            who_is_watching_list.setVisibility(View.GONE);
            noWatchlistContainer.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
        getData();


    }

    public void resetAndUpdate() {
        watchHistoryAdapter = new WatchHistoryAdapter(getActivity(), listener, where);
        who_is_watching_list.setAdapter(watchHistoryAdapter);
//        watchHistoryAdapter.updateData(new ArrayList<>());
        PAGE_INDEX = 0;
        getData();
    }

    public void getData() {
        Helper.showProgressDialog(getActivity());
        String sessionId = PreferenceHandler.getSessionId(getActivity());
        String apicall = "";
        if(!TextUtils.isEmpty(what) && what.equalsIgnoreCase("Playlist")) {
             playListId  = getArguments().getString("playlistId");
            mApiService.getAllItemsPlaylist(PreferenceHandler.getSessionId(getActivity()), playListId ,0, 20)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ListResonse>() {
                        @Override
                        public void call(ListResonse resonse) {
                            Helper.dismissProgressDialog();
                            Data data = resonse.getData();
                            if (data != null) {
                                List<Item> items = data.getItems();
                                watchHistoryAdapter.updateData(items, where);
                                webEngageAnalytics.watchlistViewed(pageEvents);
                                if (items.size() > 0) {
                                    who_is_watching_list.setVisibility(View.VISIBLE);
                                    noWatchlistContainer.setVisibility(View.GONE);
                                } else {
                                    if (watchHistoryAdapter.getItemCount() <= 0) {
                                        who_is_watching_list.setVisibility(View.GONE);
                                        noWatchlistContainer.setVisibility(View.VISIBLE);
                                    }
                                }

                                if (items.size() < 20) {
                                    IS_LAST_ITEM = true;
                                } else {
                                    IS_LAST_ITEM = false;
                                }
                                PAGE_INDEX++;
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Helper.dismissProgressDialog();
                            who_is_watching_list.setVisibility(View.GONE);
                            noWatchlistContainer.setVisibility(View.VISIBLE);

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
                                    Helper.deleteSearchHistory(getActivity());
                                }
                        }
                    });

        } else{
            if (!TextUtils.isEmpty(what) && what.equalsIgnoreCase("watchlater"))
                apicall = Constants.WATCH_LATER;
            else
                apicall = Constants.FAVOURITE;

            mApiService.getPlayLists(sessionId, /*Constants.WATCH_LATER*/apicall, PAGE_INDEX)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ListResonse>() {
                        @Override
                        public void call(ListResonse resonse) {
                            Helper.dismissProgressDialog();
                            Data data = resonse.getData();
                            if (data != null) {
                                List<Item> items = data.getItems();
                                watchHistoryAdapter.updateData(items, where);
                                webEngageAnalytics.watchlistViewed(pageEvents);
                                if (items.size() > 0) {
                                    who_is_watching_list.setVisibility(View.VISIBLE);
                                    noWatchlistContainer.setVisibility(View.GONE);
                                } else {
                                    if (watchHistoryAdapter.getItemCount() <= 0) {
                                        who_is_watching_list.setVisibility(View.GONE);
                                        noWatchlistContainer.setVisibility(View.VISIBLE);
                                    }
                                }

                                if (items.size() < 20) {
                                    IS_LAST_ITEM = true;
                                } else {
                                    IS_LAST_ITEM = false;
                                }
                                PAGE_INDEX++;
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Helper.dismissProgressDialog();
                            who_is_watching_list.setVisibility(View.GONE);
                            noWatchlistContainer.setVisibility(View.VISIBLE);

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
                                    Helper.deleteSearchHistory(getActivity());
                                }
                        }
                    });
        }

    }


    @OnClick({R.id.back, R.id.close, R.id.browse_shemaroo})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            getActivity().onBackPressed();
        } else if (id == R.id.close) {
            getActivity().onBackPressed();
        } else if (id == R.id.browse_shemaroo) {
            //Helper.addWithoutBackStackAndKeepHome(getActivity(), new HomePageFragment(), HomePageFragment.TAG, 2);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            /*getActivity().onBackPressed();
            getActivity().onBackPressed();*/
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        PAGE_INDEX = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.PlayList);

        pageEvents = new PageEvents(Constants.WatchList);
        pageEvents.setPageName("Watch List");
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }
}
