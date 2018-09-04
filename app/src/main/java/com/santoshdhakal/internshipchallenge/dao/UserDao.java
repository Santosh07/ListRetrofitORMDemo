package com.santoshdhakal.internshipchallenge.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.santoshdhakal.internshipchallenge.models.UserModel;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    LiveData<List<UserModel>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserModel... users);
}
