package com.otl.tarangplus.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Listitems {

    @SerializedName("catalog_id")
    @Expose
    private String catalog_id;

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    @SerializedName("content_id")
    @Expose
    private String content_id;

}
