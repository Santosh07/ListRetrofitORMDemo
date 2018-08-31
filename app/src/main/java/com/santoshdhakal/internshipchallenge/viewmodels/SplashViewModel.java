package com.santoshdhakal.internshipchallenge.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.repository.UserRepository;

import java.util.List;

public class SplashViewModel extends AndroidViewModel {
    private MutableLiveData<String> message;
    UserRepository userRepository;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext(), getMessage());

        new DoInBackground().execute(userRepository);
    }

    public MutableLiveData<String> getMessage() {
        if (message == null) {
            message = new MutableLiveData<>();
        }
        return message;
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
