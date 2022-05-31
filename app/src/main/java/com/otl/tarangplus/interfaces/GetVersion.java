package com.otl.tarangplus.interfaces;

/**
 * Created by Aditya on 11/5/2019.
 */
public interface GetVersion{
    public interface VersionCheck {
        void OnVersionChanged(Long version, boolean forcedUpgrade, String url);
    }
}
