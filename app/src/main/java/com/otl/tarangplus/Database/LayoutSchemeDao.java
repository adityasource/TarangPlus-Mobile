package com.otl.tarangplus.Database;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

/**
 * Created by srishesh on 9/3/18.
 */
@Dao
public interface LayoutSchemeDao {

    @Query("SELECT * FROM layout_scheme")
    List<LayoutDbScheme> getAll();

    @Query("SELECT * from layout_scheme where scheme_name = :scheme")
    LiveData<LayoutDbScheme> findByName(String scheme);

    @Query("SELECT COUNT(*) from layout_scheme")
    int countSchemes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(LayoutDbScheme ... schemes);

}


