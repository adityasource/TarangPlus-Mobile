package com.otl.tarangplus.repository;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.rest.AccountUpdateRequest;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by srishesh on 10/25/18.
 */

public class AccountDetailsRepo {
    private static AccountDetailsRepo ourInstance;
    private final ApiService mApiService;
    private MutableLiveData<Resource> mAccountsData;
    private String TAG = AccountDetailsRepo.class.getSimpleName();

    public static AccountDetailsRepo getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AccountDetailsRepo(context);
        }
        return ourInstance;
    }

    private AccountDetailsRepo(Context context) {
        RestClient mRestClient = new RestClient(context);
        mApiService = mRestClient.getApiService();
        mAccountsData = new MutableLiveData<>();

    }

    public MutableLiveData<Resource> getAccountDetails(String sessionId) {

        return getAccountDetailsApi(sessionId);
    }


    private MutableLiveData<Resource> getAccountDetailsApi(String sessionId) {
        mAccountsData.setValue(Resource.loading(""));
        mApiService.getAccountDetails(sessionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse listResonse) {
                        mAccountsData.setValue(Resource.success(listResonse));

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mAccountsData.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
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

        return mAccountsData;
    }


    public MutableLiveData<Resource> updateAccountDetails(String sessionId, AccountUpdateRequest request) {
        return updateAccounts(sessionId,request);
    }

    private MutableLiveData<Resource> updateAccounts(String sessionId, AccountUpdateRequest request) {
        mAccountsData.setValue(Resource.loading(""));
        mApiService.updateAccountDetails(sessionId,request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse resonse) {
                        if (resonse != null) {
                            mAccountsData.setValue(Resource.success(resonse));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mAccountsData.setValue(Resource.error(throwable.getLocalizedMessage(),throwable));
                    }
                });
                /*.subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        if (object != null) {
                            mAccountsData.setValue(Resource.success(object));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mAccountsData.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });*/
        return mAccountsData;
    }
}
