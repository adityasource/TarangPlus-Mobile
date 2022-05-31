package com.otl.tarangplus.model;

public class PromoEvents {
    private String Banner_Name;
    private String Banner_ID;
    private int Position;

    public PromoEvents(String banner_Name, String banner_ID, int position) {
        Banner_Name = banner_Name;
        Banner_ID = banner_ID;
        Position = position;
    }

    public String getBanner_Name() {
        return Banner_Name;
    }

    public void setBanner_Name(String banner_Name) {
        Banner_Name = banner_Name;
    }

    public String getBanner_ID() {
        return Banner_ID;
    }

    public void setBanner_ID(String banner_ID) {
        Banner_ID = banner_ID;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }
}
