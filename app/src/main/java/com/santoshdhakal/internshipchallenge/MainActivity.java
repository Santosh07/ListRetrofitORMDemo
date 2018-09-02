package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
import com.santoshdhakal.internshipchallenge.repository.UserRepository;
import com.santoshdhakal.internshipchallenge.viewmodels.HomeViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    UserRepository userRepository;
    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textview_hello);

        textView.setOnClickListener(this);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.populateUserOfPost();
        homeViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        homeViewModel.getUsers().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> users) {

            }
        });

        homeViewModel.getPosts().observe(this, new Observer<List<PostModel>>() {
            @Override
            public void onChanged(@Nullable List<PostModel> postModels) {

            }
        });

        homeViewModel.getUserOfPost().observe(this, new Observer<List<UserOfPost>>() {
            @Override
            public void onChanged(@Nullable List<UserOfPost> userOfPosts) {
                System.out.println("Count " + userOfPosts.size());
            }
        });
    }

    @Override
    public void onClick(View v) {
        String test = "";
        homeViewModel.getMessage().setValue("Test Values");
    }
}
