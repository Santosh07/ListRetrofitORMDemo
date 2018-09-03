package com.santoshdhakal.internshipchallenge.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.santoshdhakal.internshipchallenge.MainActivity;
import com.santoshdhakal.internshipchallenge.SplashActivity;
import com.santoshdhakal.internshipchallenge.dao.UserDao;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.services.WebService;
import com.santoshdhakal.internshipchallenge.singletons.AppDatabase;
import com.santoshdhakal.internshipchallenge.singletons.RetrofitClientInstance;

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

    private MutableLiveData<String> message;

    public UserRepository(final Context context, MutableLiveData<String> message) {
        db = AppDatabase.getDatabase(context);
        service = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
        this.message = message;
    }

    public List<UserModel> getAll() {
        List<UserModel> users = db.userDao().getAll();

//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Callable<List<UserModel>> callable = new Callable<List<UserModel>>() {
//            @Override
//            public List<UserModel> call() throws Exception {
//                return null;
//            }
//        };
//        Future<List<UserModel>> future = executorService.submit(callable);

        if (users.size() <= 0) {
            users = getUsersFromNetwork();
            insertAll(users.toArray(new UserModel[users.size()]));
            message.postValue("Data Saved successfully from Netowrk. User count : " + users.size());
        }

        return users;
    }

    public void insertAll(UserModel... users) {
        db.userDao().insertAll(users);
    }

    private List<UserModel> getUsersFromNetwork() {
        List<UserModel> users = new ArrayList<UserModel>();
        Call<List<UserModel>> callUser = service.getAllUsers();

        try {
            Response<List<UserModel>> response = callUser.execute();

            if (response.isSuccessful()) {
                users = response.body();
            } else {
                Log.d(this.toString(), " **** Retrofit Error :: " + response.message() + " ****");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(this.toString(), " **** Network Error ****");
        }

        return users;
    }
}
