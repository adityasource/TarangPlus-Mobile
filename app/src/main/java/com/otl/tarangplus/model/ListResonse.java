package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListResonse implements Parcelable{

    @SerializedName("data")
    @Expose
    private Data data;

    protected ListResonse(Parcel in) {
    }

    public static final Creator<ListResonse> CREATOR = new Creator<ListResonse>() {
        @Override
        public ListResonse createFromParcel(Parcel in) {
            return new ListResonse(in);
        }

        @Override
        public ListResonse[] newArray(int size) {
            return new ListResonse[size];
        }
    };

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
