
package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListItemObject implements Parcelable {

    @SerializedName("banner_image")
    @Expose
    private String bannerImage;
    @SerializedName("banner_flag")
    @Expose
    private String bannerFlag;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("overlay_text")
    @Expose
    private String overlayText;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("duration_string")
    @Expose
    private String durationString;
    @SerializedName("secondary_layer")
    @Expose
    private List<Object> secondaryLayer = null;

    protected ListItemObject(Parcel in) {
        bannerImage = in.readString();
        bannerFlag = in.readString();
        link = in.readString();
        overlayText = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        durationString = in.readString();
    }

    public static final Creator<ListItemObject> CREATOR = new Creator<ListItemObject>() {
        @Override
        public ListItemObject createFromParcel(Parcel in) {
            return new ListItemObject(in);
        }

        @Override
        public ListItemObject[] newArray(int size) {
            return new ListItemObject[size];
        }
    };

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerFlag() {
        return bannerFlag;
    }

    public void setBannerFlag(String bannerFlag) {
        this.bannerFlag = bannerFlag;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOverlayText() {
        return overlayText;
    }

    public void setOverlayText(String overlayText) {
        this.overlayText = overlayText;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public List<Object> getSecondaryLayer() {
        return secondaryLayer;
    }

    public void setSecondaryLayer(List<Object> secondaryLayer) {
        this.secondaryLayer = secondaryLayer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bannerImage);
        parcel.writeString(bannerFlag);
        parcel.writeString(link);
        parcel.writeString(overlayText);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(durationString);
    }
}
