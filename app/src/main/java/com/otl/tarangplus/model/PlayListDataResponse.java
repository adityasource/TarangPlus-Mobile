package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayListDataResponse {
    @SerializedName("data")
    @Expose
    private PlayListData playListResponse;

    public PlayListData getPlayListResponse() {
        return playListResponse;
    }

    public void setPlayListResponse(PlayListData playListResponse) {
        this.playListResponse = playListResponse;
    }
}
