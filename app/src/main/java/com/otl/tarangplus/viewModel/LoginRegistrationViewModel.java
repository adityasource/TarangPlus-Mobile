package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.repository.LoginRegisterRepo;
import com.otl.tarangplus.rest.Resource;

public class LoginRegistrationViewModel extends AndroidViewModel {

    private LoginRegisterRepo mRepo;

    public LoginRegistrationViewModel(@NonNull Application application) {
        super(application);
        mRepo = LoginRegisterRepo.getInstance(application);
    }

    public LiveData<Resource> login(SignInRequest jsonObject) {
        return mRepo.login(jsonObject);
    }

//    public LiveData<Resource> doOtpVerification(String otp,String type) {
//        return mRepo.checkOtp(otp,type);
//    }

    public LiveData<Resource> doOtpVerification(SignInRequest request) {
        return mRepo.checkOtpWithPostCall(request);
    }

    public MutableLiveData<Resource> forgotPassword(SignInRequest request) {
        return mRepo.forgotPassword(request);
    }

    public LiveData<Resource> signUp(SignInRequest request) {
        return mRepo.signUp(request);
    }

    public LiveData<Resource> assignProfile(SignInRequest request) {
        return mRepo.signUp(request);
    }


}
