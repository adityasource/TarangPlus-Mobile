package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayListResponse {

    @SerializedName("data")
    @Expose
    private Data playListResponse;

    public Data getPlayListResponse() {
        return playListResponse;
    }

    public void setPlayListResponse(Data playListResponse) {
        this.playListResponse = playListResponse;
    }
}
