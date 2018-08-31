package com.santoshdhakal.internshipchallenge.services;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebService {
    @GET("/posts")
    Call<List<PostModel>> getAllPosts();

    @GET("/users")
    Call<List<UserModel>> getAllUsers();
}
