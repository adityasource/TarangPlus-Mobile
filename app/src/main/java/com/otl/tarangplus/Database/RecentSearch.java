package com.otl.tarangplus.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recentSearch")
public class RecentSearch {
    @PrimaryKey
    @NonNull
    private String data;

    @Ignore
    public RecentSearch() {
    }

    public RecentSearch(String data) {
        this.data = data;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return getData();
    }
}
