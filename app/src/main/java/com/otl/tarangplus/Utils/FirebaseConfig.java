package com.otl.tarangplus.Utils;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.interfaces.GetVersion;

/**
 * Created by Aditya on 11/5/2019.
 */
public class FirebaseConfig {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 43200;
    Boolean forced;
    GetVersion.VersionCheck versionCheck;
    public FirebaseConfig(GetVersion.VersionCheck versionCheck) {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(100)
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        this.versionCheck = versionCheck;
    }

    private static final String KEY_UPDATE_REQUIRED = "update_required";
    private static final String KEY_CURRENT_VERSION = "current_version";
    private static final String KEY_CURRENT_VERSION_URL = "current_version_url";

    public void fetchNewImage(Context context) {
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener((Activity) context, new OnCompleteListener<Boolean>() {
            private Long version;
            private String version_url = "https://play.google.com/store/apps/details?id=com.otl.tarangplus";
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                // If is successful, activated fetched
                if (task.isSuccessful()) {
                    // mFirebaseRemoteConfig.activateFetched();
                    if(task.getResult()) {
                        forced = mFirebaseRemoteConfig.getBoolean(KEY_UPDATE_REQUIRED);
                        version = mFirebaseRemoteConfig.getLong(KEY_CURRENT_VERSION);
                        version_url = mFirebaseRemoteConfig.getString(KEY_CURRENT_VERSION_URL);
                        //versionCheck.OnVersionChanged(version, forced, version_url);
                    }
                } else {

                }
            }
        });
        // return imgUrl;
    }
/*    public long getCacheExpiration() {
        // If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }*/
}
