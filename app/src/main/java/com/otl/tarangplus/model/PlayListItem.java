package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayListItem {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("playlist_id")
    @Expose
    private String playlistId;

    @SerializedName("type")
    @Expose
    private String type;

    private boolean isSelected;
    private boolean previouslySelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public boolean isPreviouslySelected() {
        return previouslySelected;
    }

    public void setPreviouslySelected(boolean previouslySelected) {
        this.previouslySelected = previouslySelected;
    }
}
