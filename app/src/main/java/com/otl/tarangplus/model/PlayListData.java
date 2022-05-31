package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*{"id":290,"user_id":710,"profile_id":"634018574","listitems":[],"name":"MyPlaylist ",
"playlist_type":"","playlist_id":"49773556","created_at":"2020-04-27T11:55:56.051Z",
"updated_at":"2020-04-27T11:55:56.051Z"}}*/
public class PlayListData {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("playlist_id")
    @Expose
    private String playlistId;

    @SerializedName("items")
    @Expose
    private List<PlayListItem> playlistItems = new ArrayList<PlayListItem>();

    public List<PlayListItem> getPlaylistItems() {
        return playlistItems;
    }

    public void setPlaylistItems(List<PlayListItem> playlistItems) {
        this.playlistItems = playlistItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }



}
