package com.otl.tarangplus.Database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by srishesh on 9/3/18.
 */

@Entity(tableName = "layout_scheme")
public class LayoutDbScheme {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "scheme_name")
    private String scheme;
    @ColumnInfo(name = "start_color")
    private String startColor;
    @ColumnInfo(name = "end_color")
    private String EndColor;
    @ColumnInfo(name = "middle_color")
    private String middleColor;
    @ColumnInfo(name = "image_url")
    private String imageUrl;


    public LayoutDbScheme() {
    }

    public LayoutDbScheme(String scheme, String startColor, String endColor, String middleColor,String imageUrl) {
        this.scheme = scheme;
        this.startColor = startColor;
        EndColor = endColor;
        this.middleColor = middleColor;
        this.imageUrl = imageUrl;
    }

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
        return EndColor;
    }

    public void setEndColor(String endColor) {
        EndColor = endColor;
    }
}
