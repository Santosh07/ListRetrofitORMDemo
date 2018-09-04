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
    private MutableLiveData<List<UserModel>> users;
    private MutableLiveData<List<PostModel>> posts;

    private LiveData<List<UserOfPost>> userOfPost;

    private UserRepository userRepository;
    private PostRepository postRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext(), getMessage());
        postRepository = new PostRepository(application.getApplicationContext());
    }

    public MutableLiveData<String> getMessage() {
        if (message == null) {
            message = new MutableLiveData<>();
        }
        return message;
    }

    public MutableLiveData<List<UserModel>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
        }
        return users;
    }

    public MutableLiveData<List<PostModel>> getPosts() {
        if (posts == null) {
            posts = new MutableLiveData<>();
        }
        return posts;
    }

    public LiveData<List<UserOfPost>> getUserOfPost() {
        if (userOfPost == null) {
            userOfPost = postRepository.getUserOfPost();
        }
        return userOfPost;
    }

    public void populateUsers() {
        new PopulateUsersInBackground().execute();
    }

    public void populatePosts() {
        new PopulatePostsInBackground().execute();
    }

    public void populateUserOfPost() {
        new PopulateUserOfPostInBackground().execute();
    }

    private class PopulateUsersInBackground extends AsyncTask<Void , Void, List<UserModel>> {
        @Override
        protected List<UserModel> doInBackground(Void... voids) {
            List<UserModel> users = userRepository.getAll();
            return users;
        }

        @Override
        protected void onPostExecute(List<UserModel> userModels) {
            super.onPostExecute(userModels);
            users.setValue(userModels);
        }
    }

    public class PopulatePostsInBackground extends AsyncTask<Void, Void, List<PostModel>> {
        @Override
        protected List<PostModel> doInBackground(Void... voids) {
            return postRepository.getAll();
        }

        @Override
        protected void onPostExecute(List<PostModel> postModels) {
            super.onPostExecute(postModels);
            posts.setValue(postModels);
        }
    }

    public class PopulateUserOfPostInBackground extends AsyncTask<Void, Void, LiveData<List<UserOfPost>>> {

        @Override
        protected LiveData<List<UserOfPost>> doInBackground(Void... voids) {
            return postRepository.getUserOfPost();
        }

        @Override
        protected void onPostExecute(LiveData<List<UserOfPost>> userOfPosts) {
            super.onPostExecute(userOfPosts);
        }
    }

}
