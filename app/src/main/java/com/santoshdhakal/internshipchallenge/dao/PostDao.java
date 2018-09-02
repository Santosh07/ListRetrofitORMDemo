package com.santoshdhakal.internshipchallenge.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;

import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(PostModel... posts);

    @Query("SELECT * FROM posts")
    List<PostModel> getAll();

    @Query("SELECT * FROM posts ")
    LiveData<List<UserOfPost>> getUserOfPost();
}
