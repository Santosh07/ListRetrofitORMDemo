package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.santoshdhakal.internshipchallenge.models.CommentModel;
import com.santoshdhakal.internshipchallenge.viewmodels.CommentViewModel;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        CommentViewModel commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);

        commentViewModel.getComments(1).observe(this, new Observer<List<CommentModel>>() {
            @Override
            public void onChanged(@Nullable List<CommentModel> commentModels) {
                Toast.makeText(getApplicationContext(), " Comment counts = " + commentModels.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
