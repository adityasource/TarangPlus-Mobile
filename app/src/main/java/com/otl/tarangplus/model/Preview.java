
package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Preview {

    @SerializedName("preview_available")
    @Expose
    private boolean previewAvailable;

    @SerializedName("preview_start")
    @Expose
    private String previewStart;
    @SerializedName("preview_end")
    @Expose
    private String previewEnd;

    @SerializedName("preview_url")
    @Expose
    private String extPreviewUrl;


    public boolean isPreviewAvailable() {
        return previewAvailable;
    }

    public void setPreviewAvailable(boolean previewAvailable) {
        this.previewAvailable = previewAvailable;
    }

    public String getExtPreviewUrl() {
        return extPreviewUrl;
    }

    public void setExtPreviewUrl(String extPreviewUrl) {
        this.extPreviewUrl = extPreviewUrl;
    }

    public String getPreviewStart() {
        return previewStart;
    }

    public void setPreviewStart(String previewStart) {
        this.previewStart = previewStart;
    }

    public String getPreviewEnd() {
        return previewEnd;
    }

    public void setPreviewEnd(String previewEnd) {
        this.previewEnd = previewEnd;
    }
}
