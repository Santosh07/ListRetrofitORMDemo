package com.santoshdhakal.internshipchallenge.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.santoshdhakal.internshipchallenge.models.CommentModel;
import com.santoshdhakal.internshipchallenge.repository.CommentRepository;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    CommentRepository commentRepository;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        commentRepository = new CommentRepository(application.getApplicationContext());
    }

    public LiveData<List<CommentModel>> getComments(Integer postId) {
        return commentRepository.getAll(postId);
    }

    public MutableLiveData<String> getMessage() {
        return commentRepository.getMessage();
    }
}
