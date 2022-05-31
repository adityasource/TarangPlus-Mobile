package com.otl.tarangplus.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.otl.tarangplus.Database.RecentSearch;
import com.otl.tarangplus.repository.SearchPageRepository;
import com.otl.tarangplus.rest.Resource;

import java.util.List;

public class SearchPageViewModel extends AndroidViewModel {
    SearchPageRepository searchPageRepository;

    public SearchPageViewModel(@NonNull Application application) {
        super(application);
        searchPageRepository = SearchPageRepository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<Resource> getSearchResultData(String input, boolean isKidProfile) {
        return searchPageRepository.getSearchResultsData(input,isKidProfile);
    }

    public MutableLiveData<Resource> getTrendingSearchResultData(boolean isKidProfile) {
        return searchPageRepository.getTrendingSearchResultsData(isKidProfile);
    }

    public LiveData<List<RecentSearch>> getAllSearchHistory() {
        return searchPageRepository.getAllSearchHistoryData();
    }

    public void deleteAllSearHistory() {
        searchPageRepository.deleteAll();
    }

    public void insertSearchHistory(RecentSearch recentSearch) {
        searchPageRepository.insertSearchHistory(recentSearch);
    }

}
