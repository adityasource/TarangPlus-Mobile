package com.otl.tarangplus.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.otl.tarangplus.repository.SettingsRepository;
import com.otl.tarangplus.rest.PasswordChangeRequest;
import com.otl.tarangplus.rest.Resource;

/**
 * Created by srishesh on 10/25/18.
 */

public class SettingsViewModel extends AndroidViewModel {
    SettingsRepository settingRepo;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settingRepo = SettingsRepository.getInstance(application);
    }

    public MutableLiveData<Resource> clearWatchHistory(String sessionId) {
        return settingRepo.clearWatchHistory(sessionId);
    }

    public MutableLiveData<Resource> changePassword(String sessionId, PasswordChangeRequest request) {
        return settingRepo.changePassword(sessionId,request);
    }
}
