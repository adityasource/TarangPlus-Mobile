package com.otl.tarangplus.repository;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.google.gson.JsonObject;
import com.otl.tarangplus.model.AddPlayListItems;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.ShowDetailsResponse;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DetailsPageRepo {

    private static DetailsPageRepo ourInstance;
    private final ApiService mApiService;
    private MutableLiveData<Resource> showDetailsResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> moreComediesResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> moreEpisodesBasedOnGenre = new MutableLiveData<>();
    private MutableLiveData<Resource> allEpisodesResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> addToWatchList = new MutableLiveData<>();
    private MutableLiveData<Resource> associateedVideosList = new MutableLiveData<>();

    private DetailsPageRepo(Context context) {
        RestClient mRestClient = new RestClient(context);
        mApiService = mRestClient.getApiService();
    }

    public static DetailsPageRepo getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DetailsPageRepo(context);
        }
        return ourInstance;
    }

    public MutableLiveData<Resource> getDetailsResponse(String catId, String itemID) {
        return getDetails(catId, itemID);
    }

    // http://18.210.75.7:3000/catalogs/bollywood-retro/items/5b90dc99c1df41753000000c?region=IN&auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN&page_size=20

    private MutableLiveData<Resource> getDetails(String catId, String contentID) {
        showDetailsResponse.setValue(Resource.loading(""));

        mApiService.getShowDetailsResponse(catId, contentID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ShowDetailsResponse>() {
                    @Override
                    public void call(ShowDetailsResponse response) {
                        showDetailsResponse.setValue(Resource.success(response));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showDetailsResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return showDetailsResponse;
    }

    public MutableLiveData<Resource> getMoreComedies() {
        moreComediesResponse.setValue(Resource.loading(""));
        mApiService.getMoreComedy()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        moreComediesResponse.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        moreComediesResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return moreComediesResponse;
    }

    public MutableLiveData<Resource> getMoreBasedOnGenre(String home_link, String genre, int pageIndex) {
        moreEpisodesBasedOnGenre.setValue(Resource.loading(""));
        mApiService.getMoreBasedOnGenre(home_link, genre, pageIndex)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        moreEpisodesBasedOnGenre.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        moreEpisodesBasedOnGenre.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return moreEpisodesBasedOnGenre;
    }

    public MutableLiveData<Resource> getAssociatedVideos(String catalogID,String contentID,int pageIndex) {
        associateedVideosList.setValue(Resource.loading(""));
        mApiService.getAssociatedVideos(catalogID,contentID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        associateedVideosList.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        associateedVideosList.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return associateedVideosList;
    }

    public MutableLiveData<Resource> getAllEpisodes(String catalogId, String contentId) {
        allEpisodesResponse.setValue(Resource.loading(""));

        mApiService.getAllEpisodesUnderShow(catalogId, contentId, 0, Constants.API_ITEMS_COUNT, Constants.DECENDINGDING_ORDER).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        allEpisodesResponse.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        allEpisodesResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });

        return allEpisodesResponse;
    }

    public MutableLiveData<Resource> addToWatchLater(String sessionId, AddPlayListItems listitem) {
        addToWatchList.setValue(Resource.loading(""));

        mApiService.setWatchLater(sessionId, Constants.WATCHLATER, listitem).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        addToWatchList.setValue(Resource.success(jsonObject));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        addToWatchList.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return addToWatchList;
    }
}
