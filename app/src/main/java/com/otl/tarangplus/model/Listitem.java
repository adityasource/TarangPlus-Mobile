package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Listitem {

    @SerializedName("content_id")
    @Expose
    private String contentId;
    @SerializedName("catalog_id")
    @Expose
    private String catalogId;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

}
