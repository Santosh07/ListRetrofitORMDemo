package com.santoshdhakal.internshipchallenge.services;

import com.santoshdhakal.internshipchallenge.models.PostModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebService {
    @GET("/posts")
    Call<List<PostModel>> getAllPosts();
}
