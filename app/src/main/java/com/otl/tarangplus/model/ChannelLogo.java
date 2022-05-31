package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelLogo implements Parcelable{
    protected ChannelLogo(Parcel in) {
    }

    public static final Creator<ChannelLogo> CREATOR = new Creator<ChannelLogo>() {
        @Override
        public ChannelLogo createFromParcel(Parcel in) {
            return new ChannelLogo(in);
        }

        @Override
        public ChannelLogo[] newArray(int size) {
            return new ChannelLogo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    @SerializedName("s_logo_16_9")
    @Expose
    private sLogo169 xlLogo;


    public sLogo169 getXlLogo() {
        return xlLogo;
    }

    public void setXlLogo(sLogo169 xlLogo) {
        this.xlLogo = xlLogo;
    }
}
