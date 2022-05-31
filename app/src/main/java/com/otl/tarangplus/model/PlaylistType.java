package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*'{"region":"IN", "auth_token":"3zZmzoHg8z6SM3wpDoyw", "playlist":{"name":"abc", "type":"music"}}*/
public class PlaylistType {

    @SerializedName("region")
    @Expose
    private String region;

    public PlayListItem getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlayListItem playlist) {
        this.playlist = playlist;
    }

    @SerializedName("auth_token")
    @Expose
    private String authToken;

    @SerializedName("playlist")
    @Expose
    private PlayListItem playlist;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }




}
