package com.otl.tarangplus.Database;

import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_play_list")
public class PlayListDbScheme {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "content_id")
    private String contentId;

    @ColumnInfo(name = "catalog_id")
    private String catalogId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "list_id")
    private String listId;

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public PlayListDbScheme() {

    }

    @Ignore
    public PlayListDbScheme(@NonNull String contentId, String catalogId, String title,String listId) {
        this.contentId = contentId;
        this.catalogId = catalogId;
        this.title = title;
        this.listId = listId;
    }

    @NonNull
    public String getContentId() {
        return contentId;
    }

    public void setContentId(@NonNull String contentId) {
        this.contentId = contentId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
