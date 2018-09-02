package com.santoshdhakal.internshipchallenge.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
import com.santoshdhakal.internshipchallenge.services.WebService;
import com.santoshdhakal.internshipchallenge.singletons.AppDatabase;
import com.santoshdhakal.internshipchallenge.singletons.RetrofitClientInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PostRepository {
    AppDatabase db;
    WebService webService;

    public PostRepository(final Context context) {
        db = AppDatabase.getDatabase(context);
        webService = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
    }

    public List<PostModel> getAll() {
        List<PostModel> posts = db.postDao().getAll();

        if (posts.size() <= 0) {
            posts = getPostsFromNetwork();
            db.postDao().insertAll(posts.toArray(new PostModel[posts.size()]));
        }

        return posts;
    }

    public LiveData<List<UserOfPost>> getUserOfPost() {
        return db.postDao().getUserOfPost();
    }


    private List<PostModel> getPostsFromNetwork() {
        List<PostModel> posts = new ArrayList<>();

        Call<List<PostModel>> callPosts = webService.getAllPosts();

        try {
            Response<List<PostModel>> response = callPosts.execute();

            if (response.isSuccessful()) {
                posts = response.body();
            } else {
                Log.d(this.toString(), " **** Retrofit Error :: " + response.message() + " ****");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(this.toString(), " **** Network Error ****");
        }

        return posts;
    }
}
