package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.otl.tarangplus.model.AddPlayListItems;
import com.otl.tarangplus.repository.DetailsPageRepo;
import com.otl.tarangplus.rest.Resource;

public class DetailsPageViewModel extends AndroidViewModel {

    private DetailsPageRepo mRepo;
    final MutableLiveData<String> catalogId = new MutableLiveData<>();
    final MutableLiveData<String> contentId = new MutableLiveData<>();
    final LiveData<Resource> showDetailResponce;


    public DetailsPageViewModel(@NonNull Application application) {
        super(application);
        mRepo = DetailsPageRepo.getInstance(application);

        showDetailResponce = Transformations.switchMap(catalogId, catalogId -> {
            return mRepo.getDetailsResponse(catalogId, contentId.getValue());
        });

    }

    public LiveData<Resource> getShowDetailsResponse() {
        return showDetailResponce;
    }

    public void setCatalogAndContentId(String catalogId, String contentId) {
        this.catalogId.setValue(catalogId);
        this.contentId.setValue(contentId);
    }

    public LiveData<Resource> getAllEpisodes(String catalogId, String contentid) {
        return mRepo.getAllEpisodes(catalogId, contentid);
    }

    public LiveData<Resource> addToWatchList(String sessionId, AddPlayListItems addPlayListItems) {
        return mRepo.addToWatchLater(sessionId, addPlayListItems);

    }

    public MutableLiveData<Resource> getMoreBasedOnGenre(String home_link, String genre,int pageIndex) {
        return mRepo.getMoreBasedOnGenre(home_link, genre,pageIndex);
    }

    public MutableLiveData<Resource> getAssociatedListVideos(String catalogID, String contentID,int pageIndex) {
        return mRepo.getAssociatedVideos(catalogID,contentID ,pageIndex);
    }

}
