package com.santoshdhakal.internshipchallenge.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
import com.santoshdhakal.internshipchallenge.repository.PostRepository;
import com.santoshdhakal.internshipchallenge.repository.UserRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<String> message;

    private UserRepository userRepository;
    private PostRepository postRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext()); //TODO replace with DI
        postRepository = new PostRepository(application.getApplicationContext());
    }

    public LiveData<List<PostModel>> getPosts() {
        return postRepository.getAll();
    }

    public LiveData<List<UserModel>> getUsers() {
        return userRepository.getAll();
    }

    public LiveData<List<UserOfPost>> getUserOfPost() {
        return postRepository.getUserOfPost();
    }

    public LiveData<String> getMessageFromUserRepo() {
        return userRepository.getMessage();
    }

    public LiveData<String> getMessageFromPostRepo() {
        return postRepository.getMessage();
    }
}
