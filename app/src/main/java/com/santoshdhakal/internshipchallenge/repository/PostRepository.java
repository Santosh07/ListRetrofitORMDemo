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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    AppDatabase db;
    WebService webService;
    Context context;

    public PostRepository(final Context context) {
        db = AppDatabase.getDatabase(context);
        webService = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
        this.context = context;
    }

    public LiveData<List<PostModel>> getAll() {
        final MediatorLiveData<List<PostModel>> postModelMediator = new MediatorLiveData<>();

        postModelMediator.addSource(db.postDao().getAll(), new Observer<List<PostModel>>() {
            @Override
            public void onChanged(@Nullable List<PostModel> postModels) {
                if (postModels.size() <= 0) {
                    if (Utils.isInternetAvailable(context)) {
                        postModelMediator.addSource(getPostsFromNetwork(), new Observer<List<PostModel>>() {
                            @Override
                            public void onChanged(@Nullable List<PostModel> postModels) {
                                insertAll(postModels.toArray(new PostModel[postModels.size()]));
                                postModelMediator.setValue(postModels);
                            }
                        });
                    } else {
                        //send message
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

    public LiveData<List<UserOfPost>> getUserOfPost(UserRepository userRepository) {
        final MediatorLiveData<List<UserOfPost>> postMediatorLiveData = new MediatorLiveData<>();

        postMediatorLiveData.addSource(userRepository.getAll(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable final List<UserModel> userModels) {
                postMediatorLiveData.addSource(getAll(), new Observer<List<PostModel>>() {
                    @Override
                    public void onChanged(@Nullable List<PostModel> postModels) {
                        postMediatorLiveData.addSource(db.postDao().getUserOfPost(), new Observer<List<UserOfPost>>() {
                            @Override
                            public void onChanged(@Nullable List<UserOfPost> userOfPosts) {
                                postMediatorLiveData.setValue(userOfPosts);
                            }
                        });
                    }
                });
            }
        });

        return postMediatorLiveData;
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
