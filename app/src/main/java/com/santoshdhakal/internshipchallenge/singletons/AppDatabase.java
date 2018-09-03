package com.santoshdhakal.internshipchallenge.singletons;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.santoshdhakal.internshipchallenge.dao.CommentDao;
import com.santoshdhakal.internshipchallenge.dao.PostDao;
import com.santoshdhakal.internshipchallenge.dao.UserDao;
import com.santoshdhakal.internshipchallenge.models.CommentModel;
import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;

@Database(entities = {UserModel.class, PostModel.class, CommentModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();

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
