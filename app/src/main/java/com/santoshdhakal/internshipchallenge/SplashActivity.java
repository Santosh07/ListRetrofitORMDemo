package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
import com.santoshdhakal.internshipchallenge.utils.Utils;
import com.santoshdhakal.internshipchallenge.viewmodels.HomeViewModel;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    boolean isPostsReceived, isUsersReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.getUsers().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> userModels) {
                if (userModels.size() > 0) {
                    isUsersReceived = true;
                }
                isAllDataAvailable();
            }
        });

        homeViewModel.getPosts().observe(this, new Observer<List<PostModel>>() {
            @Override
            public void onChanged(@Nullable List<PostModel> postModels) {
                if (postModels.size() > 0) {
                    isPostsReceived = true;
                }
                isAllDataAvailable();
            }
        });

        homeViewModel.getMessageFromPostRepo().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals(Utils.INTERNET_UNAVAILABLE)) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        homeViewModel.getMessageFromUserRepo().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals(Utils.INTERNET_UNAVAILABLE)) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    public void isAllDataAvailable() {
        if (isPostsReceived && isUsersReceived) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
