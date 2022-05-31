package com.otl.tarangplus.Database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IPlayListDb {

    @Query("SELECT * FROM user_play_list")
    List<PlayListDbScheme> getAll();

    @Query("SELECT * from user_play_list where content_id = :contentId")
    PlayListDbScheme findByContentId(String contentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(PlayListDbScheme ... schemes);

    @Query("DELETE FROM user_play_list")
    int deleteAllPlayLists();
}
