package com.otl.tarangplus.fragments;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.viewModel.ListingViewModel;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.ListAdapter;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ListingFragment extends UniversalFragment implements SwipeRefreshLayout.OnRefreshListener {

    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;

    @Override
    protected void languageChanged() {
        PAGEINDEX = 0;
        getList();
    }

    public static final String TAG = ListingFragment.class.getSimpleName();
    private ViewHolder holder;
    private ListAdapter mListAdapter;
    private ListingViewModel listingViewModel;
    private boolean isTwoThreeMovieLayout;
    private int PAGEINDEX = 0;
    private boolean IS_LAST_ELEMENT = false;
    private ApiService mApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.general_listing_page, container, false);
        holder = new ViewHolder(view);
        mApiService = new RestClient(getContext()).getApiService();
        mAnalyticsEvent = Analytics.getInstance(getContext());
        return view;
    }

    private void getList() {
        String homeLink = getArguments().getString(Constants.HOME_LINK);
        String displayTitle = getArguments().getString(Constants.DISPLAY_TITLE);
        Helper.showProgressDialog(getActivity());
        mApiService.getListingData(homeLink,PAGEINDEX)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        holder.swifeRefreshLayout.setRefreshing(false);
                        Data data = listResonse.getData();
                        if (data != null) {
                            PAGEINDEX++;
                            if (!TextUtils.isEmpty(data.getDisplayTitle()))
                                holder.header.setText(data.getDisplayTitle());
                            else
                                holder.header.setText(displayTitle);

                            if (!TextUtils.isEmpty(data.getLayoutType()) &&
                                    ("movies".equalsIgnoreCase(data.getLayoutType()) ||
                                            "t_2_3_movie".equalsIgnoreCase(data.getLayoutType()))) {
                                isTwoThreeMovieLayout = true;
                            } else if (!TextUtils.isEmpty(data.getLayoutType()) &&
                                    ("show".equalsIgnoreCase(data.getLayoutType()) ||
                                            ("shows".equalsIgnoreCase(data.getLayoutType())))) {
                                isTwoThreeMovieLayout = false;
                            }else if (!TextUtils.isEmpty(data.getLayoutType()) &&
                                    ("t_2_3_big_meta".equalsIgnoreCase(data.getLayoutType()))) {
                                isTwoThreeMovieLayout = true;
                            }

                            List<CatalogListItem> listItems = data.getCatalogListItems();
                            if (listItems.size() < 20) {
                                IS_LAST_ELEMENT = true;
                            }
                            if(PAGEINDEX-1==0) {
                                setUpRecyclerView(data.getLayoutType());
                            }

                            mListAdapter.appendUpdatedList(listItems);
                            if (PAGEINDEX == 0 &&listItems != null && listItems.size() <= 0) {
                                showEmptyMessage();
                            }


                        } else {
                            showEmptyMessage();
                        }
                        appEvents = new AppEvents(1, "", "Listing Screen", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, data.getTitle(),
                                PreferenceHandler.getUserState(getActivity()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                        mAnalyticsEvent.logAppEvents(appEvents);
                        Helper.dismissProgressDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        showEmptyMessage();
                        Helper.dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listingViewModel = ViewModelProviders.of(this).get(ListingViewModel.class);
        Helper.showProgressDialog(getActivity());

        holder.recyclerView.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.px_2),
                (int) getResources().getDimension(R.dimen.px_0),
                (int) getResources().getDimension(R.dimen.px_4),
                (int) getResources().getDimension(R.dimen.px_4)));

        holder.swifeRefreshLayout.setOnRefreshListener(this);
        Bundle arguments = getArguments();
        String layoutScheme = arguments.getString(Constants.LAYOUT_SCHEME);
//        holder.header.setText(displayTitle);

        if (!TextUtils.isEmpty(layoutScheme))
            setTopbarUI(Constants.getSchemeColor(layoutScheme));

        getList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!holder.recyclerView.canScrollVertically(1) && !IS_LAST_ELEMENT) {

                        getList();
                    }

                }
            });
        }
    }

    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {

        if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
                Picasso.get().load(layoutDbScheme.getImageUrl()).into(holder.mTopbarImage);
                holder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            } else {
                Picasso.get().load(R.color.white).into(holder.mTopbarImage);
                holder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            }
        }

    }

    private void showEmptyMessage() {
        holder.swifeRefreshLayout.setVisibility(View.GONE);
        holder.recyclerView.setVisibility(View.GONE);
        holder.mErrorLayout.setVisibility(View.VISIBLE);
    }

    private void setUpRecyclerView(String layoutType) {
        mListAdapter = new ListAdapter(getActivity(), layoutType);

        holder.recyclerView.setAdapter(mListAdapter);
        if (isTwoThreeMovieLayout) {
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
            holder.recyclerView.setLayoutManager(manager);
        } else {
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
            holder.recyclerView.setLayoutManager(manager);
        }

        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        PAGEINDEX = 0;
        getList();

    }

    class ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

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

        @BindView(R.id.error_layout)
        RelativeLayout mErrorLayout;
        @BindView(R.id.error_go_back)
        GradientTextView mGoBackFromErrorLayout;

        @BindView(R.id.swife_container)
        SwipeRefreshLayout swifeRefreshLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            close.setVisibility(View.GONE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Helper.removeCurrentFragment(getActivity(),TAG);
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            });
            mGoBackFromErrorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.removeCurrentFragment(getActivity(), TAG);
                }
            });
        }
    }
}
