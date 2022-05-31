package com.otl.tarangplus.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RecentSearch.class, UserProfileDBScheme.class,LayoutDbScheme.class,PlayListDbScheme.class}, version = 12, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract RecentSearchDao recentSearchDao();

    public abstract UserProfileDao userProfileDao();

    public abstract LayoutSchemeDao layoutSchemeDao();

    public abstract IPlayListDb playListDb();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();

                }
            }
        }

        return instance;
    }

}
