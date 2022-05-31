package com.otl.tarangplus.fragments;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.MoreListingAdapter;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MoreListingFragment extends UniversalFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Override
    protected void languageChanged() {
        String fromPage = getArguments().getString(Constants.FROM_PAGE);
        if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(Constants.ALL_EPISODES)) {
            getAllEpisodesAndUpdateRecyclerView();
        } else if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(LiveTvDetailsFragment.TAG)) {
            String plainCategory = getArguments().getString(Constants.PLAIN_CATEGORY_TYPE);
            getOtherChannels(plainCategory);
        } else {
            getAllItemsAndUpdateRecyclerView();
        }
    }

    public static final String TAG = MoreListingFragment.class.getSimpleName();

    private ViewHolder viewHolder;
    private ApiService mApiService;
    private MoreListingAdapter mListAdapter;
    private int PAGE_INDEX = 0;
    private boolean IS_LAST_ITEM = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.general_listing_page, container, false);
        viewHolder = new ViewHolder(inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mApiService = new RestClient(getActivity()).getApiService();
        String layoutType = getArguments() == null ? "video" : getArguments().getString(Constants.LAYOUT_TYPE_SELECTED);
        String displayTitle = getArguments().getString(Constants.DISPLAY_TITLE);
        viewHolder.header.setVisibility(View.VISIBLE);
        viewHolder.header.setText(displayTitle);
        viewHolder.close.setVisibility(View.GONE);
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        setUpRecyclerView(layoutType);
        String layoutScheme = getArguments() == null ? null : getArguments().getString(Constants.LAYOUT_SCHEME);
        //if (!TextUtils.isEmpty(layoutScheme))
        //setTopbarUI(Constants.getSchemeColor(layoutScheme));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewHolder.recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!viewHolder.recyclerView.canScrollVertically(1) && !IS_LAST_ITEM) {
                        String fromPage = getArguments().getString(Constants.FROM_PAGE);
                        if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(Constants.ALL_EPISODES)) {
                            getAllEpisodesAndUpdateRecyclerView();
                        } else if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(LiveTvDetailsFragment.TAG)) {
                            String plainCategory = getArguments().getString(Constants.PLAIN_CATEGORY_TYPE);
                            getOtherChannels(plainCategory);
                        } else {
                            getAllItemsAndUpdateRecyclerView();
                        }
                    }
                }
            });
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

    private void setUpRecyclerView(String layoutType) {
        boolean isTwoThreeMovieLayout = false;
        mListAdapter = new MoreListingAdapter(getActivity(), layoutType);
        viewHolder.recyclerView.setAdapter(mListAdapter);

        if (!TextUtils.isEmpty(layoutType) && ("movies".equalsIgnoreCase(layoutType) || ("movie".equalsIgnoreCase(layoutType)) ||
                "t_2_3_movie".equalsIgnoreCase(layoutType))) {
            isTwoThreeMovieLayout = true;
        } else if (!TextUtils.isEmpty(layoutType) &&
                ("show".equalsIgnoreCase(layoutType) ||
                        ("shows".equalsIgnoreCase(layoutType)))) {
            isTwoThreeMovieLayout = false;
        } else {
            isTwoThreeMovieLayout = false;
        }

        if (isTwoThreeMovieLayout) {
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
            viewHolder.recyclerView.setLayoutManager(manager);
        } else {
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
            viewHolder.recyclerView.setLayoutManager(manager);
        }
        viewHolder.recyclerView.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.px_2),
                (int) getResources().getDimension(R.dimen.px_0),
                (int) getResources().getDimension(R.dimen.px_4),
                (int) getResources().getDimension(R.dimen.px_4)));

        String fromPage = getArguments().getString(Constants.FROM_PAGE);
        if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(Constants.ALL_EPISODES)) {
            getAllEpisodesAndUpdateRecyclerView();
        } else if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(LiveTvDetailsFragment.TAG)) {
            String plainCategory = getArguments().getString(Constants.PLAIN_CATEGORY_TYPE);
            getOtherChannels(plainCategory);
        } else {
            getAllItemsAndUpdateRecyclerView();
        }
    }

    private void getOtherChannels(String planCategoryType) {
        String theme = getArguments().getString(Constants.THEME);
        if (!TextUtils.isEmpty(theme)) {
            if (theme.equalsIgnoreCase("linear")) {
                Helper.showProgressDialog(getActivity());

                String category = "all";
                if (PreferenceHandler.isKidsProfileActive(getActivity()))
                    category = "kids";

                mApiService.getAllChannelList(theme, PAGE_INDEX, category)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ListResonse>() {
                            @Override
                            public void call(ListResonse listResonse) {
                                Helper.dismissProgressDialog();
                                if (listResonse != null) {

                                    updateCatalogListItems(listResonse.getData().getCatalogListItems(), TAG);
                                    PAGE_INDEX++;
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Helper.dismissProgressDialog();
                                throwable.printStackTrace();
                            }
                        });
            } else if (theme.equalsIgnoreCase("live")) {
                Helper.showProgressDialog(getActivity());
                final String catalog_id = getArguments().getString(Constants.CATALOG_ID);
                mApiService.getLiveChannels(catalog_id, planCategoryType, "any", PAGE_INDEX)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ListResonse>() {
                            @Override
                            public void call(ListResonse listResonse) {
                                Helper.dismissProgressDialog();
                                updateRecyclerViewItems(listResonse.getData().getItems(), planCategoryType);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Helper.dismissProgressDialog();
                                throwable.printStackTrace();
                            }
                        });
            }
        }
    }

    private void getAllItemsAndUpdateRecyclerView() {
        Bundle arguments = getArguments();
        String catalogId = arguments.getString(Constants.CATALOG_ID);
        String selectedGenres = arguments.getString(Constants.SELECTED_GENRE);
        Helper.showProgressDialog(getActivity());
        mApiService.getMoreBasedOnGenre(catalogId, selectedGenres, PAGE_INDEX)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        Helper.dismissProgressDialog();
                        if (listResonse != null) {
                            List<Item> items = listResonse.getData().getItems();
                            updateRecyclerViewItems(items);
                            PAGE_INDEX++;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();
                    }
                });
    }

    private void updateRecyclerViewItems(List<Item> items) {
        if (items != null && items.size() > 0) {
            if (items.size() < 20) {
                IS_LAST_ITEM = true;
            }
            mListAdapter.appendUpdateListItems(items);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private void updateCatalogListItems(List<CatalogListItem> items, String from) {
        if (items != null && items.size() > 0) {
            if (items.size() < 20) {
                IS_LAST_ITEM = true;
            }
            mListAdapter.updateListItems(items, from);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private void updateRecyclerViewItems(List<Item> items, String plainCategory) {

        if (items.size() < Constants.PAGE_LIST_LIMIT) {
            IS_LAST_ITEM = true;
        }
        if (items != null && items.size() > 0) {
            mListAdapter.appendUpdateListItems(items);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private void getAllEpisodesAndUpdateRecyclerView() {
        Bundle bundle = getArguments();
        String contentId = bundle.getString(Constants.CONTENT_ID);
        String catalogId = bundle.getString(Constants.CATALOG_ID);
        Helper.showProgressDialog(getActivity());
        Observable<ListResonse> episodesUnderShow = mApiService.getAllEpisodesUnderShow(catalogId, contentId, PAGE_INDEX, Constants.PAGE_LIST_LIMIT, Constants.DECENDINGDING_ORDER);
        episodesUnderShow.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse resonse) {
                        Helper.dismissProgressDialog();
                        if (resonse != null) {
                            List<Item> items = resonse.getData().getItems();
                            updateRecyclerViewItems(items);
                            PAGE_INDEX++;
                        }
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
    public void onRefresh() {
        viewHolder.swipeRefreshLayout.setRefreshing(false);
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
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.progress_bar)
        ProgressBar progress_bar;
        @BindView(R.id.no_internet_image)
        AppCompatImageView no_internet_image;
        @BindView(R.id.error_go_back)
        GradientTextView error_go_back;
        @BindView(R.id.no_int_container)
        LinearLayout no_int_container;
        @BindView(R.id.error_layout)
        RelativeLayout error_layout;
        @BindView(R.id.swife_container)
        SwipeRefreshLayout swipeRefreshLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }
    }
}
