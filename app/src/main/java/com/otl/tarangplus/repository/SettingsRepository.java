package com.otl.tarangplus.repository;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.google.gson.JsonObject;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.PasswordChangeRequest;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by srishesh on 10/25/18.
 */

public class SettingsRepository {
    private static SettingsRepository ourInstance;
    private final ApiService mApiService;
    private MutableLiveData<Resource> mSettingsData;
    private String TAG = SettingsRepository.class.getSimpleName();

    public static SettingsRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SettingsRepository(context);
        }
        return ourInstance;
    }

    private SettingsRepository(Context context) {
        RestClient mRestClient = new RestClient(context);
        mApiService = mRestClient.getApiService();
        mSettingsData = new MutableLiveData<>();

    }

    public MutableLiveData<Resource> clearWatchHistory(String sessionId) {

        return clearWatchHistoryApi(sessionId);
    }


    private MutableLiveData<Resource> clearWatchHistoryApi(String sessionId) {
        mSettingsData.setValue(Resource.loading(""));
        mApiService.clearWatchHistory(sessionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        if (object != null) {
                            mSettingsData.setValue(Resource.success(object));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSettingsData.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                        throwable.printStackTrace();
                    }
                });

       /* mApiService.getAccountDetails(sessionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Data>() {
                    @Override
                    public void call(Data data) {
                        if (data != null) {
                            mAccountsData.setValue(Resource.success(data));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mAccountsData.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                        throwable.printStackTrace();
                    }
                });*/

        return mSettingsData;
    }


    public MutableLiveData<Resource> changePassword(String sessionId, PasswordChangeRequest request) {
        return changePasswordApi(sessionId,request);
    }

    private MutableLiveData<Resource> changePasswordApi(String sessionId, PasswordChangeRequest request) {

        mSettingsData.setValue(Resource.loading(""));
        mApiService.changePassword(sessionId,request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        if (object != null) {
                            mSettingsData.setValue(Resource.success(object));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (throwable instanceof HttpException) {
                            ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                            mSettingsData.setValue(Resource.error(Constants.getErrorMessage(responseBody), throwable));
                        } else {
                            mSettingsData.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                        }
                    }
                });
        return mSettingsData;
    }
}
