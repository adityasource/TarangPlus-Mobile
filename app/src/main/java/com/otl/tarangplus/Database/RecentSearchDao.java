package com.otl.tarangplus.Database;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSearchHistory(RecentSearch noteEntity);

    @Delete
    void deleteSearchHistory(RecentSearch noteEntity);

    @Query("SELECT * FROM recentSearch WHERE data = :id")
    RecentSearch getSearchHistoryId(String id);

    @Query("SELECT * FROM recentSearch")
    LiveData<List<RecentSearch>> getAll();

    @Query("DELETE FROM recentSearch")
    int deleteAllSearchHistory();

    @Query("SELECT COUNT(*) FROM recentSearch")
    int getCount();

}
