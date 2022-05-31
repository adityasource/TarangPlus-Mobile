package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.otl.tarangplus.repository.ListingRepository;
import com.otl.tarangplus.rest.Resource;

public class ListingViewModel extends AndroidViewModel {

    private ListingRepository mRepo;

    public ListingViewModel(@NonNull Application application) {
        super(application);
        mRepo = ListingRepository.getInstance(application);
    }

    public LiveData<Resource> getListingData(String homeLinkID) {
        return mRepo.getListData(homeLinkID);
    }

    public LiveData<Resource> getListingData(String homeLinkID,int pageIndex) {
        return mRepo.getListData(homeLinkID,pageIndex);
    }
}
