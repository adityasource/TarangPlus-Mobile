package com.otl.tarangplus.repository;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PlayListRepository {

    private static PlayListRepository ourInstance;
    private final ApiService mApiService;
    private MutableLiveData<Resource> playListResponse = new MutableLiveData<>();

    private PlayListRepository(Context context) {
        RestClient mRestClient = new RestClient(context);
        mApiService = mRestClient.getApiService();
    }

    public static PlayListRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new PlayListRepository(context);
        }
        return ourInstance;
    }

    public MutableLiveData<Resource> getPlayListResponse(String sessionId, String playID) {
        playListResponse.setValue(Resource.loading(""));
        mApiService.getPlayLists(sessionId, playID,0)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        playListResponse.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        playListResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return playListResponse;
    }
}
