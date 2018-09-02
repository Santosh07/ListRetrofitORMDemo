package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
import com.santoshdhakal.internshipchallenge.repository.UserRepository;
import com.santoshdhakal.internshipchallenge.viewmodels.HomeViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    HomeViewModel homeViewModel;
    PostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView postsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_posts);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.populateUserOfPost();

        postsAdapter = new PostsAdapter();

        setObservers();

        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setAdapter(postsAdapter);
    }

    private void setObservers() {
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
                postsAdapter.setData(userOfPosts);
                System.out.println("Count " + userOfPosts.size());
            }
        });
    }

    public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {
        List<UserOfPost> userOfPosts;

        public class PostsViewHolder extends RecyclerView.ViewHolder {
            public TextView test;

            public PostsViewHolder(View itemView) {
                super(itemView);

                test = (TextView) itemView.findViewById(R.id.textView);
            }
        }

        @NonNull
        @Override
        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_row, parent, false);

            return new PostsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
            if (userOfPosts != null) {
                UserOfPost userOfPost = userOfPosts.get(position);

                holder.test.setText(userOfPost.postModel.getTitle() + " - " + userOfPost.user.get(0).getName());
            } else {
                holder.test.setText("");
            }
        }

        @Override
        public int getItemCount() {
            if (userOfPosts != null) {
                return userOfPosts.size();
            } else {
                return 0;
            }

        }

        public void setData(List<UserOfPost> userOfPosts) {
            this.userOfPosts = userOfPosts;
            notifyDataSetChanged();
        }
    }


}
