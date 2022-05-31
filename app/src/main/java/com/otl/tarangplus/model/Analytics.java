package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kiran on 12/20/18.
 */

public class Analytics {
    @SerializedName("media_active_x_min")
    @Expose
    private String mediaActiveXMin;
    @SerializedName("clevertap_mediaevents")
    @Expose
    private boolean clevertap_mediaevents;

    public boolean isClevertap_mediaevents() {
        return clevertap_mediaevents;
    }

    public void setClevertap_mediaevents(boolean clevertap_mediaevents) {
        this.clevertap_mediaevents = clevertap_mediaevents;
    }

    public String getMediaActiveXMin() {
        return mediaActiveXMin;
    }

    public void setMediaActiveXMin(String mediaActiveXMin) {
        this.mediaActiveXMin = mediaActiveXMin;
    }
}
