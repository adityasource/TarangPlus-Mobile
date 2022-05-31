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
public interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserProfile(UserProfileDBScheme noteEntity);

    @Delete
    void deleteUserProfile(UserProfileDBScheme noteEntity);

    @Query("SELECT * FROM userProfile WHERE profile_id = :profile_id")
    UserProfileDBScheme getUserProfileById(int profile_id);

    @Query("SELECT * FROM userProfile  ORDER BY profile_id DESC")
    LiveData<List<UserProfileDBScheme>> getAll();

    @Query("DELETE FROM userProfile")
    int deleteAllUserProfiles();

    @Query("SELECT COUNT(*) FROM userProfile")
    int getCount();

}
