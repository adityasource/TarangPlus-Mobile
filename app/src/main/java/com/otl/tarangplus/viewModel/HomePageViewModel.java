package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.repository.HomePageRepository;

/**
 * Created by VPotadar on 28/08/18.
 */

public class HomePageViewModel extends AndroidViewModel {
    private HomePageRepository mRepository;


    public HomePageViewModel(@NonNull Application application) {
        super(application);

        mRepository = HomePageRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<Resource> getmHomeScreenTabData() {
        return mRepository.getHomeScreenTabs();
    }

    public LiveData<Resource> getHomeScreenResponceUsingDisplayTitle(String id) {
        return mRepository.getHomeScreenResponceUsingDisplayTitl(id);
    }

    public MutableLiveData<Resource> addLayoutTypesToDB() {
        return mRepository.getConfigLayout();
    }

    public MutableLiveData<Resource> getRegions() {
        return mRepository.getRegions();
    }

    public LiveData<LayoutDbScheme> getByScheme(String scheme) {
        return mRepository.getLayoutByScheme(scheme);
    }
}
