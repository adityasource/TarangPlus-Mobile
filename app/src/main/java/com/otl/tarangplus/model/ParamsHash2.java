package com.otl.tarangplus.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParamsHash2 {

    @SerializedName("config_params")
    @Expose
    private ConfigParams configParams;

    public ConfigParams getConfigParams() {
        return configParams;
    }

    public void setConfigParams(ConfigParams configParams) {
        this.configParams = configParams;
    }
}
