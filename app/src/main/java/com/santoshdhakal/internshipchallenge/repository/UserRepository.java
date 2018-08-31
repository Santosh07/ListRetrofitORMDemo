package com.santoshdhakal.internshipchallenge.repository;

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
    List<UserModel> users;
    WebService service;

    public UserRepository(final Context context) {
        db = AppDatabase.getDatabase(context);
        service = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
    }

    public List<UserModel> getAll() {
        users = db.userDao().getAll();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<List<UserModel>> callable = new Callable<List<UserModel>>() {
            @Override
            public List<UserModel> call() throws Exception {
                return null;
            }
        };
        Future<List<UserModel>> future = executorService.submit(callable);

        if (users.size() <= 0) {
            users = getUsersFromNetwork();
            db.userDao().insertAll(users.toArray(new UserModel[users.size()]));
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

//        Asynchronous call to retrofit
//        callUser.enqueue(new Callback<List<UserModel>>() {
//            @Override
//            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
//                ArrayList<UserModel> usersList = (ArrayList<UserModel>) response.body();
//
//                insertAll(usersList.toArray(new UserModel[usersList.size()]));
//
//                System.out.println(usersList.size());
//
//                for (UserModel model: usersList) {
//                    System.out.println("Id : " + model.getUsername());
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//
//            }
//        });
    }
}
