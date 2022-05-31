package com.otl.tarangplus.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ankith on 29/2/16.
 */
public class AddPlayListItems implements Serializable {
    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("listitem")
    @Expose
    private Listitem listitem;

    /**
     * @return The authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken The auth_token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return The listitem
     */
    public Listitem getListitem() {
        return listitem;
    }

    /**
     * @param listitem The listitem
     */
    public void setListitem(Listitem listitem) {
        this.listitem = listitem;
    }
}

