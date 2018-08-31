package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.santoshdhakal.internshipchallenge.repository.UserRepository;
import com.santoshdhakal.internshipchallenge.viewmodels.SplashViewModel;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    UserRepository userRepository;
    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);

        splashViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView = (TextView) findViewById(R.id.textview_hello);

        textView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String test = "";
        splashViewModel.getMessage().setValue("Test Values");
    }
}
