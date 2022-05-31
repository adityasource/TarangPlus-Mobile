package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerObject implements Parcelable {
    @SerializedName("preview_available")
    @Expose
    private Boolean previewAvailable;
    @SerializedName("preview_start")
    @Expose
    private String previewStart;
    @SerializedName("preview_end")
    @Expose
    private String previewEnd;
    @SerializedName("ext_preview_url")
    @Expose
    private String extPreviewUrl;
    @SerializedName("ext_previews")
    @Expose
    private List<Object> extPreviews = null;

    protected TrailerObject(Parcel in) {
        byte tmpPreviewAvailable = in.readByte();
        previewAvailable = tmpPreviewAvailable == 0 ? null : tmpPreviewAvailable == 1;
        previewStart = in.readString();
        previewEnd = in.readString();
        extPreviewUrl = in.readString();
    }

    public static final Creator<TrailerObject> CREATOR = new Creator<TrailerObject>() {
        @Override
        public TrailerObject createFromParcel(Parcel in) {
            return new TrailerObject(in);
        }

        @Override
        public TrailerObject[] newArray(int size) {
            return new TrailerObject[size];
        }
    };

    public Boolean getPreviewAvailable() {
        return previewAvailable;
    }

    public void setPreviewAvailable(Boolean previewAvailable) {
        this.previewAvailable = previewAvailable;
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

    public String getExtPreviewUrl() {
        return extPreviewUrl;
    }

    public void setExtPreviewUrl(String extPreviewUrl) {
        this.extPreviewUrl = extPreviewUrl;
    }

    public List<Object> getExtPreviews() {
        return extPreviews;
    }

    public void setExtPreviews(List<Object> extPreviews) {
        this.extPreviews = extPreviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (previewAvailable == null ? 0 : previewAvailable ? 1 : 2));
        dest.writeString(previewStart);
        dest.writeString(previewEnd);
        dest.writeString(extPreviewUrl);
    }
}
