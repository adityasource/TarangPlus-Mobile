package com.otl.tarangplus.fragments;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.CategoryEvents;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.HomeScreenResponse;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.HomeRecyclerViewAdapter;
import com.otl.tarangplus.adapters.HomeViewPagerItemAdapter;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.rd.PageIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomePageItemsFragment extends Fragment {

    public static final String TAG = HomePageItemsFragment.class.getSimpleName();
    @BindView(R.id.homePageItem)
    LinearLayout homePageItem;
    @BindView(R.id.nestedScrollview)
    NestedScrollView nestedScrollview;

    ApiService apiService;
    private boolean hasLoadedOnce = false;
    private MutableLiveData<Object> selectedTitle;
    String mDisplayTitle;
    String mHomeLink;
    int startColor = 0;
    int endColor = 0;
    private ContinueWatchingFragment continueFrag;
    private View continueWatchView;
    CategoryEvents categoryEvents;

    public HomePageItemsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View pageItem = inflater.inflate(R.layout.home_page_item, container, false);
        ButterKnife.bind(this, pageItem);
        RestClient restClient = new RestClient(getActivity());
        apiService = restClient.getApiService();
        selectedTitle = new MutableLiveData<>();
        setRetainInstance(true);
        return pageItem;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            mDisplayTitle = arguments.getString(Constants.DISPLAY_TITLE);
            mHomeLink = arguments.getString(Constants.HOME_LINK);

            if (!TextUtils.isEmpty(mDisplayTitle) && mDisplayTitle.equalsIgnoreCase("kids_profile")) {
                setHomeScreenBasedOnLink(mHomeLink);
                return;
            }

            if (!TextUtils.isEmpty(mDisplayTitle) && mDisplayTitle.equalsIgnoreCase("ALL")) {
                 setHomeScreenBasedOnLink(mHomeLink);
            }

        }
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !hasLoadedOnce) {
                final Bundle arguments = getArguments();

                if (arguments != null) {
                    mDisplayTitle = arguments.getString(Constants.DISPLAY_TITLE);
                    mHomeLink = arguments.getString(Constants.HOME_LINK);
                    if (!TextUtils.isEmpty(mDisplayTitle)) {
                        setHomeScreenBasedOnLink(mHomeLink);
                    }
                }
                hasLoadedOnce = true;
            }
        }
    }

    public void setHomeScreenBasedOnLink(String link) {
        Helper.showProgressDialog(getActivity());
        apiService.getHomeScreenDetailsBasedOnHomeLink(link)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HomeScreenResponse>() {
            @Override
            public void call(HomeScreenResponse homeScreenResponse) {
                Helper.dismissProgressDialog();
                updateUI(homeScreenResponse);

//                WebEngageAnalytics webEngageAnalytics = new WebEngageAnalytics();
//                webEngageAnalytics.categoryEvent(new CategoryEvents(homeScreenResponse.getData().getDisplayTitle(),homeScreenResponse.getData().getListId()));
//                Log.d("midhun",homeScreenResponse.getData().getListId().toString());

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

    private void updateUI(HomeScreenResponse homeScreenResponse) {
        Data data = homeScreenResponse.getData();
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
                } else if (listItem.getLayoutType() != null && listItem.getLayoutType().equalsIgnoreCase(Constants.T_16_9_EPG)) {
//                    Add Fragment Here Some How .. Figure it out buddy..!!!!! Bounty 2000K
                    View viewPagerItem = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_epg, homePageItem, false);

                    EpgListFragment frag = new EpgListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.ITEMS, listItem);
                    frag.setArguments(bundle);
                    FragmentManager fragmentManager = getChildFragmentManager();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    View myFragment = viewPagerItem.findViewById(R.id.myFragment);
                    int id = myFragment.getId();
                    fragmentTransaction.replace(id, frag, TAG);
                    fragmentTransaction.commit();

                    homePageItem.addView(viewPagerItem);
                    homePageItem.requestLayout();
                } else if (listItem.getLayoutType() != null && listItem.getLayoutType().equalsIgnoreCase(Constants.T_CONTINUE_WATCHING)) {

                    if (continueFrag == null) {
                        try {
                             continueWatchView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_epg, homePageItem, false);
                            continueFrag = ContinueWatchingFragment.getInstance(getActivity());
//                            Log.e(">>>>", "ContinueWatchingFragment: " + continueFrag);

                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constants.ITEMS, listItem);
                            continueFrag.setArguments(bundle);
                            FragmentManager fragmentManager = getChildFragmentManager();
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            View myFragment = continueWatchView.findViewById(R.id.myFragment);
                            int id = myFragment.getId();
                            fragmentTransaction.replace(id, continueFrag, TAG);
                            fragmentTransaction.commitAllowingStateLoss();

                            homePageItem.addView(continueWatchView);
                            homePageItem.requestLayout();
                            ContinueWatchingFragment.updateData(PreferenceHandler.isLoggedIn(getActivity()));
                        } catch (IllegalStateException ignored) {
                            // There's no way to avoid getting this if saveInstanceState has already been called.
                            ignored.printStackTrace();
//                            Log.e(">>>>", "ContinueWatchingFragment: IllegalStateException" );
                        }

                    } else {
                        homePageItem.addView(continueWatchView);
                        homePageItem.requestLayout();
                    }
                } else {
                    View recyclerItem = LayoutInflater.from(getActivity()).inflate(R.layout.home_page_recycler_view_item, homePageItem, false);
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
        HomeViewPagerItemAdapter homeViewPagerItemAdapter = new HomeViewPagerItemAdapter(getActivity(),
                catalogListItem.getCatalogListItems(), catalogListItem, true);
        holder.viewPager.setAdapter(homeViewPagerItemAdapter);


        holder.viewPager.setClipToPadding(false);

        //For clipping the view
        holder.viewPager.setPadding(
                (int) (getResources().getDimension(R.dimen.px_0)), 0, (int) (getResources().getDimension(R.dimen.px_0)), 0);
//        holder.viewPager.setPageMargin(0);

        ViewGroup.LayoutParams layoutparams = holder.viewPager.getLayoutParams();
        layoutparams.height = Helper.get16By9Height(getActivity(), 1).get("HEIGHT");
        holder.viewPager.setLayoutParams(layoutparams);
        holder.pageIndicatorView.setCount(holder.viewPager.getIndicatorCount());
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

    private void setUpRecyclerView(final RecyclerItemViewHolder holder, final CatalogListItem item) {
        HomeRecyclerViewAdapter recyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity(), item);
        holder.recyclerViewItem.setAdapter(recyclerViewAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerViewItem.setLayoutManager(manager);
        // TODO: 19/09/18 Set color based on API

        if (mDisplayTitle.equalsIgnoreCase("ALL")) {

            if (Constants.getSchemeColor(item.getHomeLink().replace('-', '_')) != null) {
                startColor = Color.parseColor("#" + Constants.getSchemeColor(item.getHomeLink().replace('-', '_')).getStartColor());
                endColor = Color.parseColor("#" + Constants.getSchemeColor(item.getHomeLink().replace('-', '_')).getEndColor());
                //holder.displayTitle.setStartAndEndColor(startColor, endColor);
            }
            holder.displayTitle.setText(item.getDisplayTitle());
        } else {
            if (Constants.getSchemeColor(mHomeLink.replace('-', '_')) != null) {
                startColor = Color.parseColor("#" + Constants.getSchemeColor(mHomeLink.replace('-', '_')).getStartColor());
                endColor = Color.parseColor("#" + Constants.getSchemeColor(mHomeLink.replace('-', '_')).getEndColor());
                //holder.displayTitle.setStartAndEndColor(startColor, endColor);
                holder.displayTitle.setText(item.getDisplayTitle());
            }else{
                holder.displayTitle.setText(item.getDisplayTitle());
            }
        }

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
            holder.displayTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }else {
            holder.displayTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_next_black,0);
        }

        holder.displayTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String displayTitle = item.getDisplayTitle();
                if (Constants.tabsPresent.contains(displayTitle.toLowerCase())) {
                    selectedTitle.setValue(displayTitle);
                    if (listener != null) {
                        listener.onClickDisplayTitle(displayTitle);
                    }
                } else if (!TextUtils.isEmpty(mDisplayTitle) && !TextUtils.isEmpty(item.getHomeLink())) {
                    ListingFragment fragment = new ListingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DISPLAY_TITLE, item.getDisplayTitle());
                    bundle.putString(Constants.HOME_LINK, item.getHomeLink());
                    bundle.putString(Constants.THEME, item.getTheme());
                    bundle.putString(Constants.LAYOUT_SCHEME, item.getLayoutScheme());
                    bundle.putString(Constants.FROM_WHERE, TAG);
                    fragment.setArguments(bundle);

                    WebEngageAnalytics webEngageAnalytics = new WebEngageAnalytics();

                    String subCat = item.getDisplayTitle() != null ? item.getDisplayTitle() : "";
                    String subCatId = item.getListId() != null ? item.getListId() : "";

                    webEngageAnalytics.subCategoryEvent(new CategoryEvents("","",subCat,subCatId));
                    Helper.addFragment(getActivity(), fragment, ListingFragment.TAG);
                }

            }
        });
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public interface DisplayTitleClickListener {
        void onClickDisplayTitle(String title);
    }

    private DisplayTitleClickListener listener;

    public void setOnDisplayClickListener(DisplayTitleClickListener listener) {
        this.listener = listener;
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
