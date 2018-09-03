package com.santoshdhakal.internshipchallenge.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.santoshdhakal.internshipchallenge.models.CommentModel;
import com.santoshdhakal.internshipchallenge.repository.CommentRepository;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    LiveData<List<CommentModel>> comments;
    CommentRepository commentRepository;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        commentRepository = new CommentRepository(application.getApplicationContext());
    }

    public LiveData<List<CommentModel>> getComments(Integer postId) {
        if (comments == null) {
            comments = commentRepository.getAll(postId);
        }
        return comments;
    }
}
