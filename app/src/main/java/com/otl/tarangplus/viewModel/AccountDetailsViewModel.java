package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.otl.tarangplus.repository.AccountDetailsRepo;
import com.otl.tarangplus.rest.AccountUpdateRequest;
import com.otl.tarangplus.rest.Resource;

/**
 * Created by srishesh on 10/25/18.
 */

public class AccountDetailsViewModel extends AndroidViewModel {
    AccountDetailsRepo accountDetailsRepo;

    public AccountDetailsViewModel(@NonNull Application application) {
        super(application);
        accountDetailsRepo = AccountDetailsRepo.getInstance(application);
    }

    public MutableLiveData<Resource> getAccountDetails(String sessionId){
        return accountDetailsRepo.getAccountDetails(sessionId);
    }

    public MutableLiveData<Resource> updateAccountDetails(String sessionId, AccountUpdateRequest request){
        return accountDetailsRepo.updateAccountDetails(sessionId,request);
    }
}
