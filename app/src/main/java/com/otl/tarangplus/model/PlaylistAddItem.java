package com.otl.tarangplus.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*'{"listitem":'*/
public class PlaylistAddItem {

    @SerializedName("listitem")
    @Expose
    private ListItemRequest listItemRequest;

    public ListItemRequest getListItemRequest() {
        return listItemRequest;
    }

    public void setListItemRequest(ListItemRequest listItemRequest) {
        this.listItemRequest = listItemRequest;
    }
}
