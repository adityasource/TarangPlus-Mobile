package com.otl.tarangplus.repository;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TvRepo {

    private static TvRepo myInstance;
    private ApiService apiService;
    private MutableLiveData<Resource> liveData = new MutableLiveData<>();

    private TvRepo(Context context) {
        apiService = new RestClient(context).getApiService();
    }

    public static TvRepo getInstance(Application application) {
        if (myInstance == null) {
            myInstance = new TvRepo(application);
        }
        return myInstance;
    }

    public MutableLiveData<Resource> getAllChannels(String status,int pageIndex,String category) {
        liveData.setValue(Resource.loading(""));
        apiService.getAllChannelList(status,pageIndex,category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        liveData.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        liveData.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                        throwable.printStackTrace();
                    }
                });
        return liveData;
    }
}
