package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.otl.tarangplus.repository.TvRepo;
import com.otl.tarangplus.rest.Resource;

public class LiveTvViewModel extends AndroidViewModel {

    private TvRepo mTvRepo;

    public LiveTvViewModel(@NonNull Application application) {
        super(application);
        mTvRepo = TvRepo.getInstance(application);
    }

    public LiveData<Resource> getTvChannels(String status, int pageIndex, String category) {
        return mTvRepo.getAllChannels(status,pageIndex,category);
    }
}
