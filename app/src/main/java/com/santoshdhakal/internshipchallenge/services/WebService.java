package com.santoshdhakal.internshipchallenge.services;

import android.arch.lifecycle.LiveData;

import com.santoshdhakal.internshipchallenge.models.CommentModel;
import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebService {
    @GET("/posts")
    Call<List<PostModel>> getAllPosts();

    @GET("/users")
    Call<List<UserModel>> getAllUsers();

    @GET("/posts/{id}/comments")
    Call<List<CommentModel>> getAllComments(@Path("id")int postId);
}
