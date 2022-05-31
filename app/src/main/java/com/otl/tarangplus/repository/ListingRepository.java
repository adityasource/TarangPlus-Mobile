package com.otl.tarangplus.repository;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ListingRepository {

    private static ListingRepository instance;
    private ApiService mApiService;
    private MutableLiveData<Resource> listingResonse = new MutableLiveData<>();

    private ListingRepository(Context context) {
        mApiService = new RestClient(context).getApiService();
    }

    public static ListingRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ListingRepository(application);
            return instance;
        }
        return instance;
    }

    public MutableLiveData<Resource> getListData(String homeLinkID) {
        listingResonse.setValue(Resource.loading(""));
        mApiService.getListingData(homeLinkID,Constants.API_ITEMS_COUNT)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        listingResonse.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listingResonse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return listingResonse;
    }

    public MutableLiveData<Resource> getListData(String homeLinkID,int pageIndex) {
        listingResonse.setValue(Resource.loading(""));
        mApiService.getListingData(homeLinkID,pageIndex)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        listingResonse.setValue(Resource.success(listResonse));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listingResonse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return listingResonse;
    }
}
