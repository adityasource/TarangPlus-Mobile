package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by saranyu on 1/23/17.
 */

public class HeartBeatData implements Serializable {

    @SerializedName("content_id")
    @Expose
    private String mContentID;
    @SerializedName("catalog_id")
    @Expose
    private String mCatalogID;
    @SerializedName("play_back_time")
    @Expose
    private String mPlaybackTime;


    public String getContentID() {
        return mContentID;
    }

    public void setContentID(String contentID) {
        mContentID = contentID;
    }

    public String getCatalogID() {
        return mCatalogID;
    }

    public void setCatalogID(String catalogID) {
        mCatalogID = catalogID;
    }

    public String getPlaybackTime() {
        return mPlaybackTime;
    }

    public void setPlaybackTime(String playbackTime) {
        mPlaybackTime = playbackTime;
    }
}

