package com.otl.tarangplus.model;

/**
 * Created by ankith on 25/11/15.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayUrl implements Serializable{

    @SerializedName("vidgyor_url")
    @Expose
    private VidgyorUrl vidgyorUrl;
    @SerializedName("saranyu")
    @Expose
    private Saranyu saranyu;
    @SerializedName("youtube")
    @Expose
    private Saranyu youtube;
    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VidgyorUrl getVidgyorUrl() {
        return vidgyorUrl;
    }

    public void setVidgyorUrl(VidgyorUrl vidgyorUrl) {
        this.vidgyorUrl = vidgyorUrl;
    }

    /**
     *
     * @return
     *     The saranyu
     */
    public Saranyu getSaranyu() {
        return saranyu;
    }

    public Saranyu getYoutube() {
        return youtube;
    }

    public void setYoutube(Saranyu youtube) {
        this.youtube = youtube;
    }

    /**
     *
     * @param saranyu
     *     The saranyu
     */
    public void setSaranyu(Saranyu saranyu) {
        this.saranyu = saranyu;
    }

}