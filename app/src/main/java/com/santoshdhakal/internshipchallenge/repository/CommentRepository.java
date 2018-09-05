package com.santoshdhakal.internshipchallenge.repository;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.santoshdhakal.internshipchallenge.models.CommentModel;
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

public class CommentRepository {
    AppDatabase db;
    WebService webService;
    Context context;

    private MutableLiveData<String> message;

    public CommentRepository(final Context context) {
        db = AppDatabase.getDatabase(context);
        webService = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
        this.context = context;
    }

    public MutableLiveData<String> getMessage() {
        if (message == null) {
            message = new MutableLiveData<>();
        }
        return message;
    }

    public LiveData<List<CommentModel>> getAll(final Integer postId) {
        final MediatorLiveData<List<CommentModel>> mediatorLiveData = new MediatorLiveData<>();

        final LiveData<List<CommentModel>> source1 = db.commentDao().getAllCommentOfPost(postId);

        mediatorLiveData.addSource(source1, new Observer<List<CommentModel>>() {
            @Override
            public void onChanged(@Nullable List<CommentModel> commentModels) {
                if (commentModels.size() <= 0) {
                    final LiveData<List<CommentModel>> commentsFromNetwork = getCommentsFromNetwork(postId);
                    if (commentsFromNetwork != null) {
                        mediatorLiveData.addSource(commentsFromNetwork, new Observer<List<CommentModel>>() {
                            @Override
                            public void onChanged(@Nullable List<CommentModel> commentModels) {
                                mediatorLiveData.removeSource(source1);
                                mediatorLiveData.removeSource(commentsFromNetwork);

                                mediatorLiveData.postValue(commentModels);
                                insertAll(commentModels.toArray(new CommentModel[commentModels.size()]));
                            }
                        });
                    }
                } else {
                    mediatorLiveData.postValue(commentModels);
                }
            }
        });

        return mediatorLiveData;
    }

    private LiveData<List<CommentModel>> getCommentsFromNetwork(Integer postId) {
        if (!Utils.isInternetAvailable(context)) {
            getMessage().setValue(Utils.INTERNET_UNAVAILABLE);
            return null;
        }

        final MutableLiveData<List<CommentModel>> commentModels = new MutableLiveData<>();

        Call<List<CommentModel>> callModel = webService.getAllComments(postId);

        callModel.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if (response.isSuccessful()) {
                    commentModels.postValue(response.body());
                } else {
                    Log.d(this.toString(), " **** Retrofit Error :: " + response.message() + " ****");
                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });

        return commentModels;
    }

    public void insertAll(CommentModel... commentModels) {
        new PopulateCommentsInBackground().execute(commentModels);
    }

    public class PopulateCommentsInBackground extends AsyncTask<CommentModel, Void, Void> {

        @Override
        protected Void doInBackground(CommentModel... commentModels) {
            db.commentDao().insertAll(commentModels);
            return null;
        }
    }
}
