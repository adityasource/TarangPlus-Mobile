package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.repository.LoginRegisterRepo;
import com.otl.tarangplus.rest.Resource;

public class PasswordResetViewModel extends AndroidViewModel {

    private LoginRegisterRepo mRepo;

    public PasswordResetViewModel(@NonNull Application application) {
        super(application);
        mRepo = LoginRegisterRepo.getInstance(application);
    }

    public LiveData<Resource> resetPassword(SignInRequest request) {
        return mRepo.resetOtp(request);
    }
}
