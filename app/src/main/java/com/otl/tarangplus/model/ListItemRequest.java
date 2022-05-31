package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*{"catalog_id":"5d529839babd81605300004b", "content_id":"5e3be12786f9ab24a5000015,"},
"region":"IN", "auth_token":"3zZmzoHg8z6SM3wpDoyw", "user_id":"d0720ea1da1c3fbedc0fbf63d31c6ca1", "list_type":"music_album"}*/
public class ListItemRequest {

    @SerializedName("catalog_id")
    @Expose
    private String catalogId;
    @SerializedName("content_id")
    @Expose
    private String contentId;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("auth_token")
    @Expose
    private String authToken;

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("list_type")
    @Expose
    private String list_type;

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getList_type() {
        return list_type;
    }

    public void setList_type(String list_type) {
        this.list_type = list_type;
    }
}
