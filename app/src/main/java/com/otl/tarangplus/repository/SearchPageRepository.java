package com.otl.tarangplus.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.otl.tarangplus.model.SearchListItems;
import com.otl.tarangplus.Database.AppDatabase;
import com.otl.tarangplus.Database.RecentSearch;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SearchPageRepository {

    private static final String TAG = "SearchPageRepo";
    private static SearchPageRepository ourInstance;
    private ApiService mApiService;
    private MutableLiveData<Resource> mSearchResults = new MutableLiveData<>();
    private MutableLiveData<Resource> mTrendingSearchResults = new MutableLiveData<>();


    private LiveData<List<RecentSearch>> mRecentSearchHistory;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static SearchPageRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SearchPageRepository(context);
        }
        return ourInstance;
    }

    private SearchPageRepository(Context context) {
        RestClient mRestClient = new RestClient(context);
        mApiService = mRestClient.getApiService();
        mDb = AppDatabase.getInstance(context);
        mRecentSearchHistory = getAllSearchHistoryData();

    }

    public MutableLiveData<Resource> getSearchResultsData(String input, boolean isKid) {
        return getSearchResultData(input, isKid);
    }

    public MutableLiveData<Resource> getTrendingSearchResultsData(boolean isKidProfile) {
        return getTrendingSearchResultData(isKidProfile);
    }


    private MutableLiveData<Resource> getSearchResultData(String input, boolean isKidProfile) {
        String filter = "category,all";
        String category = "all";
        if (input == null) {
            mSearchResults.setValue(Resource.error("Error", new Throwable()));
            return mSearchResults;
        }

        if (isKidProfile) {
            filter = "category,kids";
            category = "kids";
        }
        mSearchResults.setValue(Resource.loading(""));

        mApiService.searchDataSet(Constants.API_KEY, Constants.REGION, filter, category, input)
//        mApiService.searchDataSet(Constants.API_KEY, Constants.REGION, filter, input)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).debounce(600, TimeUnit.MILLISECONDS)
                .subscribe(searchItem -> mSearchResults.setValue(Resource.success(searchItem)),
                        throwable -> mSearchResults.setValue(Resource.error(throwable.getMessage(), throwable)));

        return mSearchResults;
    }


    private MutableLiveData<Resource> getTrendingSearchResultData(boolean isKidProfile) {

        String filter = "category,all";
        String category = "all";
        if (isKidProfile) {
            filter = "category,kids";
            category = "kids";
        }

        mTrendingSearchResults.setValue(Resource.loading(""));

        mApiService.getTrendingSearchDataSet(/*filter, category*/)
//        mApiService.getTrendingSearchDataSet(filter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SearchListItems>() {
                    @Override
                    public void call(SearchListItems searchItem) {
                        mTrendingSearchResults.setValue(Resource.success(searchItem));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mTrendingSearchResults.setValue(Resource.error(throwable.getMessage(), throwable));
                    }
                });

        return mTrendingSearchResults;
    }

    public LiveData<List<RecentSearch>> getAllSearchHistoryData() {
        return mDb.recentSearchDao().getAll();
    }

    public void deleteAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.recentSearchDao().deleteAllSearchHistory();
            }
        });
    }

    public void insertSearchHistory(final RecentSearch recentSearch) {
        executor.execute(() -> {
            try {
                mDb.recentSearchDao().insertSearchHistory(recentSearch);
                // TODO: 04/10/18 Added this to avoid  android.database.sqlite.SQLiteFullException: database or disk is full (code 13)
                // Please use micromax device for testing
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
