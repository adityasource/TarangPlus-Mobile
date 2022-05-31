package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpData {

    @SerializedName("data")
    @Expose
    private SignUpDetailsData data;

    public SignUpDetailsData getData() {
        return data;
    }

    public void setData(SignUpDetailsData data) {
        this.data = data;
    }

}
