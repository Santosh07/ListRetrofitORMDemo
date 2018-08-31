package com.santoshdhakal.internshipchallenge.singletons;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.santoshdhakal.internshipchallenge.dao.UserDao;
import com.santoshdhakal.internshipchallenge.models.UserModel;

@Database(entities = {UserModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"main_database.db").build();
                }
            }
        }
        return INSTANCE;
    }

}
