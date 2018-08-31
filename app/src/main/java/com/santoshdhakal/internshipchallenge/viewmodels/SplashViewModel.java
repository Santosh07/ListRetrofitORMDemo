package com.santoshdhakal.internshipchallenge.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.santoshdhakal.internshipchallenge.MainActivity;
import com.santoshdhakal.internshipchallenge.SplashActivity;
import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.repository.UserRepository;
import com.santoshdhakal.internshipchallenge.services.WebService;
import com.santoshdhakal.internshipchallenge.singletons.AppDatabase;
import com.santoshdhakal.internshipchallenge.singletons.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends AndroidViewModel {
    List<UserModel> users;
    UserRepository userRepository;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());

        new DoInBackground().execute(userRepository);
    }

    private static class DoInBackground extends AsyncTask<UserRepository, Void, List<UserModel>> {

        @Override
        protected List<UserModel> doInBackground(UserRepository... userRepositories) {
            List<UserModel> users = userRepositories[0].getAll();
            return users;
        }

        @Override
        protected void onPostExecute(List<UserModel> userModels) {
            super.onPostExecute(userModels);

            for (UserModel user: userModels) {
                System.out.println("Names = " + user.getUsername());
            }
        }
    }
}
