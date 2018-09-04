package com.santoshdhakal.internshipchallenge.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.santoshdhakal.internshipchallenge.MainActivity;
import com.santoshdhakal.internshipchallenge.SplashActivity;
import com.santoshdhakal.internshipchallenge.dao.UserDao;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.services.WebService;
import com.santoshdhakal.internshipchallenge.singletons.AppDatabase;
import com.santoshdhakal.internshipchallenge.singletons.RetrofitClientInstance;
import com.santoshdhakal.internshipchallenge.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    AppDatabase db;
    WebService service;
    Context context;

    public UserRepository(final Context context) {
        db = AppDatabase.getDatabase(context);
        service = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
        this.context = context;
    }

    public LiveData<List<UserModel>> getAll() {
        final MediatorLiveData<List<UserModel>> mediatorUserModels = new MediatorLiveData<>();

        mediatorUserModels.addSource(db.userDao().getAll(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> userModels) {
                if (userModels.size() <= 0) {
                    if (Utils.isInternetAvailable(context)) {
                        mediatorUserModels.addSource(getUsersFromNetwork(), new Observer<List<UserModel>>() {
                            @Override
                            public void onChanged(@Nullable List<UserModel> userModels) {
                                insertAll(userModels.toArray(new UserModel[userModels.size()]));
                                mediatorUserModels.setValue(userModels);
                            }
                        });
                    } else {
                        //send internet not available message.
                    }
                }
                mediatorUserModels.setValue(userModels);
            }
        });
        return mediatorUserModels;
    }

    public void insertAll(UserModel... users) {
        new PopulateUsersInBackground().execute(users);
    }

    private LiveData<List<UserModel>> getUsersFromNetwork() {
        final MutableLiveData<List<UserModel>> mutableUserModels = new MutableLiveData<>();

        Call<List<UserModel>> callUser = service.getAllUsers();
        callUser.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    mutableUserModels.setValue(response.body());
                } else {
                    //send message
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }
        });

        return mutableUserModels;
    }

    public class PopulateUsersInBackground extends AsyncTask<UserModel, Void, Void> {

        @Override
        protected Void doInBackground(UserModel... userModels) {
            db.userDao().insertAll(userModels);
            return null;
        }
    }
}
