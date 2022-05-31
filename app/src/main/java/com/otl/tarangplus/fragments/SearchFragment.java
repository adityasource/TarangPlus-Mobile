package com.otl.tarangplus.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.widget.AppCompatImageView;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.SearchEvents;
import com.otl.tarangplus.model.SearchListItems;
import com.otl.tarangplus.viewModel.SearchPageViewModel;
import com.otl.tarangplus.Database.RecentSearch;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.KeyboardUtils;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.adapters.SearchAdapter;
import com.otl.tarangplus.adapters.TrendingSearchAdapter;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.otl.tarangplus.rest.Resource.Status.LOADING;
import static com.otl.tarangplus.rest.Resource.Status.SUCCESS;
import static com.otl.tarangplus.rest.Resource.Status.ERROR;
/**
 * Created by VPotadar on 06/09/18.
 */

public class SearchFragment extends UniversalFragment {
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;
    @Override
    protected void languageChanged() {
        mViewModel.getTrendingSearchResultData(isKidProfile).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    Helper.showProgressDialog(getActivity());
                    break;
                case SUCCESS:
                    Helper.dismissProgressDialog();
                    SearchListItems searchListItems = (SearchListItems) resource.data;
                    if (searchListItems.getData().getCatalogListItems().size() > 0) {
                        mSearchCount.setText("Trending Search");
                    }

                    if (searchListItems.getData().getCatalogListItems().size() == 0) {
                        mNoResultView.setVisibility(View.VISIBLE);
                    } else {
                        mNoResultView.setVisibility(View.GONE);
                    }
                    mTrendingSearchAdapter.updateSearchItems(searchListItems.getData().getCatalogListItems());
                    break;
                case ERROR:
                    Helper.dismissProgressDialog();
                    break;
            }
        });

    }

    public static String TAG = SearchFragment.class.getSimpleName();

    View mRootView;
    SearchPageViewModel mViewModel;
    @BindView(R.id.recyclerView)
    RecyclerView mSearchList;
    @BindView(R.id.TrendingRecyclerView)
    RecyclerView mTrendingRecyclerView;
    @BindView(R.id.searchEditText)
    MyEditText mSearchEditText;
    @BindView(R.id.close)
    AppCompatImageView mClose;
    @BindView(R.id.search_count)
    MyTextView mSearchCount;
    SearchAdapter mSearchAdapter;
    TrendingSearchAdapter mTrendingSearchAdapter;

    @BindView(R.id.no_search_result_container)
    RelativeLayout mNoResultView;
    @BindView(R.id.error_text)
    MyTextView mErrorSearchText;
    @BindView(R.id.voice_search)
    AppCompatImageView msearch_voice_btn;

    private ArrayAdapter<String> mDummySearchHistoryAdapter;
    ArrayList<String> mDummySearchHistoryList = new ArrayList<>();
    private boolean isKidProfile = false;
    AnalyticsProvider analyticsProvider;
    private final int REQ_CODE = 100;

    private WebEngageAnalytics webEngageAnalytics;
    private SearchEvents searchEvents;
    private PageEvents pageEvents;

    public SearchFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search_page, container, false);
        ButterKnife.bind(this, mRootView);
        webEngageAnalytics = new WebEngageAnalytics();
        mAnalyticsEvent = Analytics.getInstance(getContext());
        appEvents = new AppEvents(1, "", "Search Screen", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Search Screen",
                PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()),
                PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);
        ((MainActivity) getActivity()).setSelectedNavUI(Constants.TABS.SEARCH);
        KeyboardUtils.addKeyboardToggleListener(getActivity(), isVisible -> {
            if (isVisible) {
                Helper.fullScreenView(getActivity());
            } else {
                if (Helper.isKeyboardVisible(getActivity())) {
                    Helper.dismissKeyboard(getActivity());
                }
            }
            //Helper.setLightStatusBar(getActivity());
        });
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setLightStatusBar(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
        mViewModel = ViewModelProviders.of(this).get(SearchPageViewModel.class);
        Helper.showProgressDialog(getActivity());
        setDataToAdapter();
        analyticsProvider = new AnalyticsProvider(getActivity());

        isKidProfile = PreferenceHandler.isKidsProfileActive(getActivity());


        mViewModel.getTrendingSearchResultData(isKidProfile).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    Helper.showProgressDialog(getActivity());
                    break;
                case SUCCESS:
                    Helper.dismissProgressDialog();
                    SearchListItems searchListItems = (SearchListItems) resource.data;
                    if (searchListItems.getData().getCatalogListItems().size() > 0) {
                        mSearchCount.setText("Trending Search");
                    }
                    if (searchListItems.getData().getCatalogListItems().size() == 0) {
                        mNoResultView.setVisibility(View.VISIBLE);
                    } else {
                        mNoResultView.setVisibility(View.GONE);
                    }
                    mTrendingSearchAdapter.updateSearchItems(searchListItems.getData().getCatalogListItems());
                    break;
                case ERROR:
                    Helper.dismissProgressDialog();
                    break;
            }
        });


        mViewModel.getSearchResultData(null, isKidProfile).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    Helper.showProgressDialog(getActivity());
                    break;
                case SUCCESS:
                    Helper.dismissProgressDialog();
                    SearchListItems searchListItems = (SearchListItems) resource.data;
                    if (searchListItems.getData().getItems().size() > 0) {
                        mSearchCount.setText("Search Results (" + searchListItems.getData().getItems().size() + ")");
                        mSearchCount.setText("Search Results (" + searchListItems.getData().getItems().size() + ")");
                    }
                    if (searchListItems.getData().getItems().size() == 0) {
                        mNoResultView.setVisibility(View.VISIBLE);
                    } else {
                        mNoResultView.setVisibility(View.GONE);
                    }
                    mSearchAdapter.updateSearchItems(searchListItems.getData().getItems());
                    break;
                case ERROR:
                    Helper.dismissProgressDialog();
                    break;
            }
        });


        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable charSequence) {
                performSearch(charSequence.toString(), 1);
                searchEvents = new SearchEvents(charSequence.toString());
              //  searchEvents.setSearchQuery(charSequence.toString());

            }
        });

        mSearchAdapter.setOnSearchItemClickListner(item -> {
            if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                mViewModel.insertSearchHistory(new RecentSearch(mSearchEditText.getText().toString()));
            }

            webEngageAnalytics.search(searchEvents);

            /*if(item != null && !TextUtils.isEmpty(item.getDisplayTitle())){
                analyticsProvider.branchSearch(item,item.getDisplayTitle(),getActivity());
            } else  if (item != null && !TextUtils.isEmpty(item.getTitle())){
                analyticsProvider.branchSearch(item,item.getTitle(),getActivity());
            }*/
            Helper.dismissKeyboard(getActivity());
            Helper.moveToPageBasedOnTheme(getActivity(), item, "");
        });

        mTrendingSearchAdapter.setOnSearchItemClickListner(item -> {
            Helper.dismissKeyboard(getActivity());
            Helper.moveToPageBasedOnTheme(getActivity(), item);
        });

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Helper.dismissKeyboard(getActivity());
                } else if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    performSearch(textView.getText().toString(), 0);
                    if (!TextUtils.isEmpty(textView.getText().toString())) {
                        mViewModel.insertSearchHistory(new RecentSearch(textView.getText().toString()));
                    }

                    Helper.dismissKeyboard(getActivity());
                    return true;
                }

                return false;
            }
        });
        msearch_voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getActivity(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mClose.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                mSearchEditText.getText().clear();
                switchReclerViews(false);
            }
        });

        mViewModel.getAllSearchHistory().observe(this, recentSearches -> {

            mDummySearchHistoryList.clear();
            for (int i = 0; i < recentSearches.size(); i++) {
                mDummySearchHistoryList.add(recentSearches.get(i).getData());
            }
            mDummySearchHistoryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_row_recent_history, mDummySearchHistoryList);
            //mListView.setAdapter(mDummySearchHistoryAdapter);
            if (mDummySearchHistoryList.size() > 0) {
                searchHistoryVisibilityController(View.VISIBLE);
            }
            mDummySearchHistoryAdapter.notifyDataSetChanged();
        });

        /*mListView.setOnItemClickListener((adapterView, view12, position, l) -> {
            String value = (String) mListView.getItemAtPosition(position);
            mSearchEditText.setText(value);
        });

        mClear.setOnClickListener(view13 -> {
            Helper.deleteSearchHistory(getActivity());
            searchHistoryVisibilityController(View.GONE);
        });*/


//        mViewModel.getTrendingSearchResultData();
    }

    private void performSearch(String charSequence, int pos) {
        if (charSequence.trim().length() > pos) {
            mClose.setImageResource(R.drawable.ic_grey_cross);
            String key;
            key = charSequence;
            switchReclerViews(true);
            mViewModel.getSearchResultData(key, isKidProfile);
            if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 0) {
             try {
                 mErrorSearchText.setText(getString(R.string.sorry_txt_for_search));
             }catch (Exception ex){
                 ex.printStackTrace();
             }
                appEvents = new AppEvents(1, "", Constants.SEARCH, Constants.AN_APP_TYPE, key, Constants.SEARCH, "",
                        PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                mAnalyticsEvent.logAppEvents(appEvents);

            }
//                mErrorSearchText.setText(getString(R.string.sorry_txt_for_search) + " " + charSequence.toString());
            searchHistoryVisibilityController(View.GONE);
        } else if (charSequence.length() == 0) {
            mClose.setImageResource(R.drawable.ic_search_grey);
            searchHistoryVisibilityController(View.VISIBLE);
            if (mSearchAdapter != null) {
                mSearchAdapter.updateSearchItems(new ArrayList<Item>());
                mSearchCount.setText("");
            }
        }

        if (charSequence.trim().length() > 0) {
            mClose.setImageResource(R.drawable.ic_grey_cross);
        }
    }

    private void switchReclerViews(boolean isSearchResultsShown) {
        if (isSearchResultsShown) {
            mTrendingRecyclerView.setVisibility(View.GONE);
            mSearchList.setVisibility(View.VISIBLE);
        } else {
            mNoResultView.setVisibility(View.GONE);
            mSearchCount.setText("Trending Search");
            mTrendingRecyclerView.setVisibility(View.VISIBLE);
            mSearchList.setVisibility(View.GONE);
        }
    }

    public void searchHistoryVisibilityController(int visibility) {
        if (mSearchEditText.getText().toString().length() == 0) {
            if (View.VISIBLE == visibility) {
                if (mDummySearchHistoryList.size() > 0)
                 //   mSearchHistoryContainer.setVisibility(View.VISIBLE);
                mNoResultView.setVisibility(View.GONE);
            } else {
               // mSearchHistoryContainer.setVisibility(View.GONE);
            }
        } else {
            //mSearchHistoryContainer.setVisibility(View.GONE);
        }
    }

    public void setDataToAdapter() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSearchAdapter = new SearchAdapter(getActivity(), new ArrayList<Item>());
        mSearchList.setNestedScrollingEnabled(false);
        mSearchList.setHasFixedSize(true);
        mSearchList.setAdapter(mSearchAdapter);
        mSearchList.setLayoutManager(linearLayoutManager);


        final LinearLayoutManager TrendlinearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTrendingSearchAdapter = new TrendingSearchAdapter(getActivity(), new ArrayList<CatalogListItem>());
        mTrendingRecyclerView.setNestedScrollingEnabled(false);
        mTrendingRecyclerView.setHasFixedSize(true);
        mTrendingRecyclerView.setAdapter(mTrendingSearchAdapter);
        mTrendingRecyclerView.setLayoutManager(TrendlinearLayoutManager);

    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.Search);
        pageEvents = new PageEvents(Constants.Search);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }

    @Override
    public void onDestroy() {
        Helper.dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSearchEditText.setText(result.get(0));
                    mSearchEditText.setSelection(mSearchEditText.getText().length());
                }
                break;
            }
        }
    }
}
