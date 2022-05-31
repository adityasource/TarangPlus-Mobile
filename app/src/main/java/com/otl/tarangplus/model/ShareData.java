package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareData {

    @SerializedName("content_url")
    @Expose
    private String contentTypeUrl;


    public String getContentTypeUrl() {
        return contentTypeUrl;
    }

    public void setContentTypeUrl(String contentTypeUrl) {
        this.contentTypeUrl = contentTypeUrl;
    }
}
