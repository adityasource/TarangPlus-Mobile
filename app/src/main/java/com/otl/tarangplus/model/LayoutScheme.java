package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LayoutScheme {

    @SerializedName("scheme")
    @Expose
    private String scheme;
    @SerializedName("start_color")
    @Expose
    private String startColor;
    @SerializedName("end_color")
    @Expose
    private String endColor;
    @SerializedName("middle_color")
    @Expose
    private String middleColor;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMiddleColor() {
        return middleColor;
    }

    public void setMiddleColor(String middleColor) {
        this.middleColor = middleColor;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getStartColor() {
        return startColor;
    }

    public void setStartColor(String startColor) {
        this.startColor = startColor;
    }

    public String getEndColor() {
        return endColor;
    }

    public void setEndColor(String endColor) {
        this.endColor = endColor;
    }

}