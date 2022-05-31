package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Aditya on 8/29/2019.
 */
public class SingleEpisode {

    @SerializedName("data")
    @Expose
    private Item item;

    public Item getData() {
        return item;
    }

    public void setData(Item data) {
        this.item = data;
    }
}
