package com.otl.tarangplus.repository;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginRegisterRepo {

    private final ApiService mApiService;
    private static LoginRegisterRepo instance;
    private MutableLiveData<Resource> loginResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> otpResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> forgotPasswordResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> signUpResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> resend = new MutableLiveData<>();
    private MutableLiveData<Resource> otpCheckPostCallResponse = new MutableLiveData<>();
    private MutableLiveData<Resource> resetOtpResponse = new MutableLiveData<>();

    private LoginRegisterRepo(Application context) {
        mApiService = new RestClient(context).getApiService();
    }

    public static LoginRegisterRepo getInstance(Application application) {
        if (instance == null) {
            instance = new LoginRegisterRepo(application);
            return instance;
        }
        return instance;
    }

    public MutableLiveData<Resource> login(SignInRequest signInRequest) {
        loginResponse.setValue(Resource.loading(""));
        mApiService.login(signInRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loggedInData -> loginResponse.setValue(Resource.success(loggedInData)), throwable -> {
                    if (throwable instanceof HttpException) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        loginResponse.setValue(Resource.error(Constants.getErrorMessage(responseBody), throwable));
                    } else {
                        loginResponse.setValue(Resource.error(Constants.getErrorMessage(throwable).getError().getMessage(), throwable));
                    }

                });
        return loginResponse;
    }

//    public MutableLiveData<Resource> checkOtp(String otp, String type) {
//        otpResponse.setValue(Resource.loading(""));
//        mApiService.checkOtp(otp, type, Constants.DEVICE_TYPE, Constants.DEVICE_NAME, mobileNum)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(loginData -> otpResponse.setValue(Resource.success(loginData)), throwable -> {
//                    throwable.printStackTrace();
//                    if (throwable instanceof HttpException) {
//                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
//                        otpResponse.setValue(Resource.error(Constants.getErrorMessage(responseBody), throwable));
//                    } else {
//                        otpResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
//                    }
//                });
//        return otpResponse;
//    }

    public MutableLiveData<Resource> forgotPassword(SignInRequest request) {
        forgotPasswordResponse.setValue(Resource.loading(""));
        mApiService.forgotPassword(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(jsonObject -> forgotPasswordResponse.setValue(Resource.success(jsonObject)), throwable -> {
                    if (throwable instanceof HttpException) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        forgotPasswordResponse.setValue(Resource.error(Constants.getErrorMessage(responseBody), throwable));
                    } else {
                        if (throwable != null && throwable.getLocalizedMessage() != null)
                            forgotPasswordResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return forgotPasswordResponse;
    }

    public LiveData<Resource> signUp(SignInRequest request) {
        if (request == null) {  // this done to avoid success call with data from previous attempts
            signUpResponse.setValue(Resource.error("INIT", new Object()));
            return signUpResponse;
        }

        signUpResponse.setValue(Resource.loading(""));
        mApiService.signUp(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signedUpData -> signUpResponse.setValue(Resource.success(signedUpData)), throwable -> {
                    if (throwable instanceof HttpException) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        signUpResponse.setValue(Resource.error(Constants.getErrorMessage(responseBody), throwable));
                    } else {
                        signUpResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return signUpResponse;
    }

    public LiveData<Resource> resendOTP(SignInRequest signInRequest) {
        resend.setValue(Resource.loading(""));
        mApiService.resendVerificationCode(signInRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> resend.setValue(Resource.success(jsonObject)), throwable -> {
                    if (throwable instanceof HttpException) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        resend.setValue(Resource.error(Constants.getErrorMessage(responseBody), throwable));
                    } else {
                        resend.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return resend;
    }


    public LiveData<Resource> checkOtpWithPostCall(SignInRequest request) {
        otpCheckPostCallResponse.setValue(Resource.loading(""));
        mApiService.checkOtpWithPostCall(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        otpCheckPostCallResponse.setValue(Resource.success(jsonObject));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        otpCheckPostCallResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                    }
                });
        return otpCheckPostCallResponse;
    }

    public LiveData<Resource> resetOtp(SignInRequest request) {
        resetOtpResponse.setValue(Resource.loading(""));
        mApiService.resetPassword(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        resetOtpResponse.setValue(Resource.success(jsonObject));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        resetOtpResponse.setValue(Resource.error(throwable.getLocalizedMessage(), throwable));
                        throwable.printStackTrace();
                    }
                });
        return resetOtpResponse;
    }
}
