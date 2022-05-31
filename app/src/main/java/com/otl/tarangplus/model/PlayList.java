package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayList {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("playlist_id")
    @Expose
    private String playlistId;
    @SerializedName("listitem_id")
    @Expose
    private String listitemId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getListitemId() {
        return listitemId;
    }

    public void setListitemId(String listitemId) {
        this.listitemId = listitemId;
    }
}
