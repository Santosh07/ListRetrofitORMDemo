package com.santoshdhakal.internshipchallenge.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
import com.santoshdhakal.internshipchallenge.services.WebService;
import com.santoshdhakal.internshipchallenge.singletons.AppDatabase;
import com.santoshdhakal.internshipchallenge.singletons.RetrofitClientInstance;
import com.santoshdhakal.internshipchallenge.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    AppDatabase db;
    WebService webService;
    Context context;

    MutableLiveData<String> message;

    public PostRepository(final Context context) {
        db = AppDatabase.getDatabase(context);
        webService = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
        this.context = context;
    }

    public LiveData<List<PostModel>> getAll() {
        final MediatorLiveData<List<PostModel>> postModelMediator = new MediatorLiveData<>();

        final LiveData<List<PostModel>> source1 = db.postDao().getAll();

        postModelMediator.addSource(source1, new Observer<List<PostModel>>() {
            @Override
            public void onChanged(@Nullable List<PostModel> postModels) {
                if (postModels.size() <= 0) {
                    if (Utils.isInternetAvailable(context)) {

                        final LiveData<List<PostModel>> source2 = getPostsFromNetwork();

                        postModelMediator.addSource(source2, new Observer<List<PostModel>>() {
                            @Override
                            public void onChanged(@Nullable List<PostModel> postModels) {
                                postModelMediator.removeSource(source1);
                                postModelMediator.removeSource(source2);
                                postModelMediator.postValue(postModels);
                                insertAll(postModels.toArray(new PostModel[postModels.size()]));
                            }
                        });
                    } else {
                        getMessage().setValue(Utils.INTERNET_UNAVAILABLE);
                    }
                }
                postModelMediator.setValue(postModels);
            }
        });

        return postModelMediator;
    }

    public void insertAll(PostModel... postModels) {
        new PopulatePostInBackground().execute(postModels);
    }

    public LiveData<List<UserOfPost>> getUserOfPost() {
        return db.postDao().getUserOfPost();
    }

    private LiveData<List<PostModel>> getPostsFromNetwork() {
        final MutableLiveData<List<PostModel>> mutablePostModel = new MutableLiveData<>();

        Call<List<PostModel>> callPosts = webService.getAllPosts();

        callPosts.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful()) {
                    mutablePostModel.setValue(response.body());
                } else {
                    // send message
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {

            }
        });

        return mutablePostModel;
    }

    public MutableLiveData<String> getMessage() {
        if (message == null) {
            message = new MutableLiveData<>();
        }
        return message;
    }

    public class PopulatePostInBackground extends AsyncTask<PostModel, Void, Void> {

        @Override
        protected Void doInBackground(PostModel... postModels) {
            db.postDao().insertAll(postModels);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
