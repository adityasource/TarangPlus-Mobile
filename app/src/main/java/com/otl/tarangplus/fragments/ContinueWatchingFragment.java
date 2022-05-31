package com.otl.tarangplus.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.ContinueWatchingAdapter;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContinueWatchingFragment extends Fragment {

    public static final String TAG = ContinueWatchingFragment.class.getSimpleName();

    static RecyclerView recyclerView;
    static GradientTextView displayTitle;
    static LinearLayout mParentView;
    private static ApiService mApiService;
    private static ContinueWatchingAdapter recyclerViewAdapter;
    static ContinueWatchingFragment instance = new ContinueWatchingFragment();


    public static ContinueWatchingFragment getInstance(Context mContext) {
//        Log.e(TAG, "ContinueWatchingFragment: getInstance: ");
        if (instance == null || instance.getActivity() != mContext)
            instance = new ContinueWatchingFragment();
        return instance;


    }

    public ContinueWatchingFragment() {

        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e(TAG, "onResume: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.continue_watch_frag, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewItem);
        displayTitle = view.findViewById(R.id.display_title);
        mParentView = view.findViewById(R.id.parent_view);

        setDisplayTextGradientColor("All");
//        displayTitle.setOnClickListener(view -> {
//                Helper.moveToPageBasedOnTheme(getActivity(), object);
//        });
        displayTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeeMoreContinueWatchingFrag frag = new SeeMoreContinueWatchingFrag();
                Helper.addFragmentForDetailsScreen(getActivity(), frag, SeeMoreContinueWatchingFrag.TAG);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());

        mParentView.setVisibility(View.GONE);
        context = getActivity();
        RestClient mRestClient = new RestClient(getActivity());
        mApiService = mRestClient.getApiService();
        recyclerViewAdapter = new ContinueWatchingAdapter(getActivity(), "continue_watching_page");
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_5)));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);

        updateUi();

        if (!loggedIn) {
            mParentView.setVisibility(View.GONE);
        }
    }

    public static void setAdapterAndUpdate(boolean loggedIn, Context mContext) {

        if (recyclerView != null && recyclerViewAdapter != null) {
            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            try {
                RecyclerView.ItemDecoration itemDecoration = recyclerView.getItemDecorationAt(0);
                SpacesItemDecoration local = ((SpacesItemDecoration) itemDecoration);
                if (local.getRight() <= 0) {
                    recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) mContext.getResources().getDimension(R.dimen.px_5)));
                }
            } catch (Exception e) {
                recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) mContext.getResources().getDimension(R.dimen.px_5)));
//                Log.d("Exception: ", e.getMessage());
            }

            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        if (loggedIn)
            getContinueWatchingData();
        else if (mParentView != null) {
            mParentView.setVisibility(View.GONE);
//            Log.e(TAG, "ContinueWatchingFragment: Not logged in");
        }

    }

    public static void updateData(boolean loggedIn) {
        if (loggedIn)
            getContinueWatchingData();
        else if (mParentView != null) {
            mParentView.setVisibility(View.GONE);
//            Log.e(TAG, "ContinueWatchingFragment: Not logged in");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateUi() {
        getContinueWatchingData();
    }

    private static Activity context;

    private static void getContinueWatchingData() {
//        if (mApiService == null)
//            Log.e(TAG, "ContinueWatchingFragment: mApiService : null ");
//
//        if (TextUtils.isEmpty(Constants.CUR_SESSION_ID))
//            Log.e(TAG, "ContinueWatchingFragment: CUR_SESSION_ID : " + Constants.CUR_SESSION_ID);

        if (mApiService != null && !TextUtils.isEmpty(Constants.CUR_SESSION_ID)) {
            mApiService.getContinueWatchingList(Constants.CUR_SESSION_ID, 200)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> {
                        List<Item> listItems = item.getData().getItems();
                        if (listItems != null && listItems.size() > 0) {
                            mParentView.setVisibility(View.VISIBLE);

//                            Log.e(">>>>", "ContinueWatchingFragment: " + recyclerViewAdapter);
//                            Log.e(">>>>", "ContinueWatchingFragment: " + recyclerView);
//                            Log.e(">>>>", "ContinueWatchingFragment: " + listItems.size());

                            setUpRecyclerView(listItems);
                        } else {
                            mParentView.setVisibility(View.GONE);

                        }
                    }, throwable -> {
                        mParentView.setVisibility(View.GONE);
                        DataError errorRes = Constants.getErrorMessage(throwable);
                        String errorMessage = errorRes.getError().getMessage();
                        int errorCode = errorRes.getError().getCode();
                        if (errorCode == 1016 && ((retrofit2.adapter.rxjava.HttpException) throwable).code() == 422) {
                            Helper.clearLoginDetails(context);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Helper.showToast(context, errorMessage, R.drawable.ic_error_icon);
                            context.startActivity(new Intent(intent));
                            context.finish();

                        }
                    });
        } else {
            if (mParentView != null)
                mParentView.setVisibility(View.GONE);
        }
    }

    private static void setUpRecyclerView(List<Item> items) {
        recyclerViewAdapter.updateItems(items);
//        recyclerViewAdapter.notifyDataSetChanged();
        recyclerViewAdapter.setOnItemClickListener(data -> {
            if (TextUtils.isEmpty(Constants.CUR_SESSION_ID) || TextUtils.isEmpty(data.getId()))
                return;

            mApiService.removeContinueWatchItem(Constants.CUR_SESSION_ID, data.getListItemId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        items.remove(data);
                        recyclerViewAdapter.updateItems(items);
                        recyclerViewAdapter.notifyDataSetChanged();

                        if (items.size() == 0)
                            if (mParentView != null)
                                mParentView.setVisibility(View.GONE);

                    }, throwable -> throwable.printStackTrace());

        });

    }

    private void setDisplayTextGradientColor(String item) {

            displayTitle.setText(R.string.continue_watching_string);

    }
}
