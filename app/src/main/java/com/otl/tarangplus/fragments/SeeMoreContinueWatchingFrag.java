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
import com.otl.tarangplus.model.ContinueWatchingResponse;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.ContinueWatchingListAdapter;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SeeMoreContinueWatchingFrag extends Fragment {

    public static final String TAG = SeeMoreContinueWatchingFrag.class.getSimpleName();

    @BindView(R.id.category_back_img)
    ImageView categoryBackImg;
    @BindView(R.id.category_grad_back)
    TextView categoryGradBack;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //    @BindView(R.id.swife_container)
//    SwipeRefreshLayout swifeContainer;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_internet_image)
    AppCompatImageView noInternetImage;
    @BindView(R.id.error_go_back)
    GradientTextView errorGoBack;
    @BindView(R.id.no_int_container)
    LinearLayout noIntContainer;
    @BindView(R.id.error_layout)
    RelativeLayout errorLayout;

    private ApiService mApiService;
    private int PAGE_INDEX = 0;
    private boolean IS_LAST_ITEM = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.see_more_continue_watching, container, false);
        ButterKnife.bind(this, inflate);
        close.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Helper.removeCurrentFragment(getActivity(),TAG);
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });
        mApiService = new RestClient(getActivity()).getApiService();
        errorGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)(getActivity()).onBackPressed();
            }
        });
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        header.setText(getString(R.string.continue_watching_string));
        getAllContinueWatchingData();
    }

    private void setUpRecyclerView() {

        final GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAdapter = new ContinueWatchingListAdapter(getActivity(), "listing_page");
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.px_2),
                (int) getResources().getDimension(R.dimen.px_0),
                (int) getResources().getDimension(R.dimen.px_4),
                (int) getResources().getDimension(R.dimen.px_4)));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter.setOnUpdateEmptyListener(new ContinueWatchingListAdapter.UpdateEmptyListener() {
            @Override
            public void onAllDelete() {
                recyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (!recyclerView.canScrollVertically(1) && !IS_LAST_ITEM) {
                        getAllContinueWatchingData();
                    }
                }
            });
        }


        recyclerViewAdapter.setOnItemClickListener(data -> {
            if (TextUtils.isEmpty(Constants.CUR_SESSION_ID) || TextUtils.isEmpty(data.getId()))
                return;

            mApiService.removeContinueWatchItem(Constants.CUR_SESSION_ID, data.getListItemId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        recyclerViewAdapter.deleteItem(data);
                        getAllContinueWatchingData();

                    }, throwable -> throwable.printStackTrace());

        });


    }

    private ContinueWatchingListAdapter recyclerViewAdapter;

    private void getAllContinueWatchingData() {
        Helper.showProgressDialog(getActivity());
        mApiService.getContinueWatchingList1(Constants.CUR_SESSION_ID, PAGE_INDEX)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ContinueWatchingResponse>() {
                    @Override
                    public void call(ContinueWatchingResponse continueWatchingResponse) {
                        Helper.dismissProgressDialog();
                        if (continueWatchingResponse != null) {
                            Data data = continueWatchingResponse.getData();
                            List<Item> items = data.getItems();
                            if (items != null && items.size() > 0) {
                                recyclerViewAdapter.updateItems(items);
                            } else {

                                if (recyclerViewAdapter.getItemCount() <= 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    errorLayout.setVisibility(View.VISIBLE);
                                }
                            }


                            if (items != null && items.size() < 20) {
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
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                });
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
}
