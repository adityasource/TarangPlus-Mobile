package com.otl.tarangplus.fragments;

import androidx.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.HomeRecyclerViewAdapter;
import com.otl.tarangplus.adapters.HomeViewPagerItemAdapter;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.rd.PageIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TvFragment extends UniversalFragment {
    @Override
    protected void languageChanged() {
        setLiveTVData();
    }

    public static String TAG = TvFragment.class.getSimpleName();
    @BindView(R.id.homePageItem)
    LinearLayout homePageItem;
    ApiService apiService;
    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.close)
    AppCompatImageView close;
    private int PAGEINDEX = 0;
    private boolean IS_LAST_ELEMENT = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View pageItem = inflater.inflate(R.layout.tv_layout, container, false);
        ButterKnife.bind(this, pageItem);
        header.setText("TV Channels");
        close.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        RestClient restClient = new RestClient(getActivity());
        apiService = restClient.getApiService();
        setRetainInstance(true);
        return pageItem;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle arguments = getArguments();
        ((MainActivity) getActivity()).setSelectedNavUI(Constants.TABS.TV);
        setLiveTVData();
        mDisplayTitle = "LiveTV";
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }


    public void setLiveTVData() {
        Helper.showProgressDialog(getActivity());
        apiService.getAllChannelList("", 0, "all").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ListResonse>() {
            @Override
            public void call(ListResonse homeScreenResponse) {
                Helper.dismissProgressDialog();
                updateUI(homeScreenResponse);

                AnalyticsProvider analyticsProvider = new AnalyticsProvider(getContext());
                analyticsProvider.reportApplaunchTime();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.toString());
                Helper.dismissProgressDialog();
            }
        });
    }

    private void updateUI(ListResonse livescreen) {
        Data data = livescreen.getData();
        if (data != null) {
            List<CatalogListItem> catalogListItems = data.getCatalogListItems();
            if (catalogListItems != null && catalogListItems.size() >= 0) {
                inflateItems(catalogListItems);
            }
        }
    }

    private void inflateItems(List<CatalogListItem> catalogListItems) {
        try {
            homePageItem.removeAllViews();

            for (CatalogListItem listItem : catalogListItems) {

                if (listItem == null)
                    continue;

                if (listItem.getCatalogListItems() == null) {
                    continue;
                }

                if (listItem.getLayoutType() != null && listItem.getLayoutType().equalsIgnoreCase(Constants.T_16_9_BANNER)) {
                    View viewPagerItem = LayoutInflater.from(getActivity()).inflate(R.layout.home_page_view_pager_item, homePageItem,
                            false);
                    ViewPagerItemViewHolder holder = new ViewPagerItemViewHolder(viewPagerItem);
                    attachViewPagerData(holder, listItem);

                    if (catalogListItems.get(catalogListItems.size() - 1).equals(listItem))     // Adding padding at the end, so that the content is visible above bottom navigation view.
                        viewPagerItem.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.px_20));

                    homePageItem.addView(viewPagerItem);
                } else {
                    View recyclerItem = LayoutInflater.from(getActivity()).inflate(R.layout.home_page_recycler_view_item,
                            homePageItem, false);
                    RecyclerItemViewHolder holder = new RecyclerItemViewHolder(recyclerItem);
                    setUpRecyclerView(holder, listItem);

                    if (catalogListItems.get(catalogListItems.size() - 1).equals(listItem))    // Adding padding at the end, so that the content is visible above bottom navigation view.
                        recyclerItem.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.px_20));

                    homePageItem.addView(recyclerItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int total = 0;
    int currentPage;
    Handler handler = new Handler();
    Runnable update;
    private int size = 0;

    private void attachViewPagerData(final ViewPagerItemViewHolder holder, CatalogListItem catalogListItem) {
        HomeViewPagerItemAdapter homeViewPagerItemAdapter = new HomeViewPagerItemAdapter(getActivity(), catalogListItem.getCatalogListItems(), catalogListItem, true);
        holder.viewPager.setAdapter(homeViewPagerItemAdapter);


        holder.viewPager.setClipToPadding(false);

        //For clipping the view
        holder.viewPager.setPadding(
                (int) (getResources().getDimension(R.dimen.px_0)), 0, (int) (getResources().getDimension(R.dimen.px_0)), 0);
//        holder.viewPager.setPageMargin(0);

        ViewGroup.LayoutParams layoutparams = holder.viewPager.getLayoutParams();
        layoutparams.height = Helper.get16By9Height(getActivity(), 1).get("HEIGHT");
        holder.viewPager.setLayoutParams(layoutparams);
        holder.pageIndicatorView.setCount(6);
        holder.viewPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
                holder.pageIndicatorView.setProgress(selectingPosition, progress);
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
//                indicatorView.setSelection(newIndicatorPosition);
            }
        });
        size = catalogListItem.getCatalogListItems().size();
        total = size;
        currentPage = 0;


        holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                // handler.removeCallbacks(update);
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    String mDisplayTitle;
    String mHomeLink;
    int startColor = 0;
    int endColor = 0;
    private MutableLiveData<Object> selectedTitle;

    private void setUpRecyclerView(final RecyclerItemViewHolder holder, final CatalogListItem item) {
        HomeRecyclerViewAdapter recyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity(), item);
        holder.recyclerViewItem.setAdapter(recyclerViewAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerViewItem.setLayoutManager(manager);
        // TODO: 19/09/18 Set color based on API
        holder.displayTitle.setText(item.getDisplayTitle());

        holder.recyclerViewItem.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        if (item != null && item.getCatalogListItems() == null || item.getCatalogListItems().size() <= 0) {
            holder.recyclerViewItem.setVisibility(View.GONE);
            holder.displayTitle.setVisibility(View.GONE);
        } else {
            holder.recyclerViewItem.setVisibility(View.VISIBLE);
            holder.displayTitle.setVisibility(View.VISIBLE);
        }

        String homeLink = item.getHomeLink();
        if (TextUtils.isEmpty(homeLink)) {
            holder.displayTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            holder.displayTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_black, 0);
        }

        holder.displayTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String displayTitle = item.getDisplayTitle();
                if (!TextUtils.isEmpty(mDisplayTitle) && !mDisplayTitle.equalsIgnoreCase("ALL") && !TextUtils.isEmpty(item.getHomeLink())) {
                    ListingFragment fragment = new ListingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DISPLAY_TITLE, item.getDisplayTitle());
                    bundle.putString(Constants.HOME_LINK, item.getHomeLink());
                    bundle.putString(Constants.THEME, item.getTheme());
                    bundle.putString(Constants.LAYOUT_SCHEME, item.getLayoutScheme());
                    bundle.putString(Constants.FROM_WHERE, TAG);
                    fragment.setArguments(bundle);
                    Helper.addFragment(getActivity(), fragment, ListingFragment.TAG);
                }

            }
        });
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    static class RecyclerItemViewHolder {
        @BindView(R.id.display_title)
        GradientTextView displayTitle;
        @BindView(R.id.recyclerViewItem)
        RecyclerView recyclerViewItem;

        RecyclerItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public int convertDipToPixels(float dips) {
        return (int) (dips * getActivity().getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    static class ViewPagerItemViewHolder {

        @BindView(R.id.myviewpager)
        LoopingViewPager viewPager;
        @BindView(R.id.indicator)
        PageIndicatorView pageIndicatorView;

        ViewPagerItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
