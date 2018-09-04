package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.models.UserOfPost;
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

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onViewClick(View view, int position, int postId) {
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }
        };
        postsAdapter = new PostsAdapter(listener);

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
        RecyclerViewClickListener listener;

        public PostsAdapter(RecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public class PostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            RecyclerViewClickListener listener;

            public TextView firstLetterTextView;
            public TextView userNameTextView;
            public TextView addressAndCompanyTextView;
            public TextView postTitleTextView;
            public TextView postBodyTextView;
            public TextView commentIconTextView;

            public PostsViewHolder(View itemView, RecyclerViewClickListener listener) {
                super(itemView);

                this.listener = listener;

                firstLetterTextView = (TextView) itemView.findViewById(R.id.textView_first_letter);
                userNameTextView = (TextView) itemView.findViewById(R.id.textView_user_name);
                addressAndCompanyTextView = (TextView) itemView.findViewById(R.id.textView_address_and_company);
                postTitleTextView = (TextView) itemView.findViewById(R.id.textView_post_title);
                postBodyTextView =(TextView) itemView.findViewById(R.id.textView_post_body);
                commentIconTextView = (TextView) itemView.findViewById(R.id.textView_comment_icon);

                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa-regular-400.ttf");
                commentIconTextView.setTypeface(typeface);

                commentIconTextView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                listener.onViewClick(v, getAdapterPosition(), userOfPosts.get(getAdapterPosition()).postModel.getId());
            }
        }

        @NonNull
        @Override
        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_posts, parent, false);

            return new PostsViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
            if (userOfPosts != null) {
                UserOfPost userOfPost = userOfPosts.get(position);

                UserModel user = userOfPost.user.get(0);

                holder.firstLetterTextView.setText(user.getName().substring(0,1));
                holder.userNameTextView.setText(user.getName());
                holder.addressAndCompanyTextView.setText(user.getCompany().getName() + ", " + user.getEmail());
                holder.postTitleTextView.setText(userOfPost.postModel.getTitle());
                holder.postBodyTextView.setText(userOfPost.postModel.getBody());
            } else {
                holder.userNameTextView.setText("");
                holder.firstLetterTextView.setText("");
                holder.addressAndCompanyTextView.setText("");
                holder.postTitleTextView.setText("");
                holder.postBodyTextView.setText("");
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

    public interface RecyclerViewClickListener {
        void onViewClick(View view, int position, int postId);
    }
}
