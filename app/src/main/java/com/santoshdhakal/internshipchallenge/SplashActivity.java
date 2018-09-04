package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
import com.santoshdhakal.internshipchallenge.viewmodels.HomeViewModel;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.getUserOfPost().observe(this, new Observer<List<UserOfPost>>() {
            @Override
            public void onChanged(@Nullable List<UserOfPost> userOfPosts) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
