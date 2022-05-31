package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.otl.tarangplus.repository.PlayListRepository;
import com.otl.tarangplus.rest.Resource;

public class PlayListViewModel extends AndroidViewModel {

    private PlayListRepository playListRepository;

    public PlayListViewModel(@NonNull Application application) {
        super(application);
        playListRepository = PlayListRepository.getInstance(application);
    }

    public LiveData<Resource> getPlayLists(String userID, String playListID) {
        return playListRepository.getPlayListResponse(userID, playListID);
    }
}
