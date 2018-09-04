package com.santoshdhakal.internshipchallenge.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.santoshdhakal.internshipchallenge.models.CommentModel;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CommentModel... commentModels);

    @Query("SELECT * FROM comments")
    LiveData<List<CommentModel>> getAll();

    @Query("SELECT * FROM comments "
                + "JOIN posts "
                + "ON comments.postId = posts.id "
                + "WHERE comments.postId = :postId")
    LiveData<List<CommentModel>> getAllCommentOfPost(Integer postId);
}
