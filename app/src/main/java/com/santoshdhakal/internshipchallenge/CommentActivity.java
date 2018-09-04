package com.santoshdhakal.internshipchallenge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.santoshdhakal.internshipchallenge.models.CommentModel;
import com.santoshdhakal.internshipchallenge.utils.Utils;
import com.santoshdhakal.internshipchallenge.viewmodels.CommentViewModel;

import org.w3c.dom.Text;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        CommentViewModel commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);

        final CommentAdapter adapter = new CommentAdapter();

        RecyclerView commentRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_comments);
        final ProgressBar commentProgressBar = (ProgressBar) findViewById(R.id.progressBar_comments);

        commentProgressBar.setVisibility(View.VISIBLE);

        commentViewModel.getComments(getIntent().getIntExtra("postId", 1)).observe(this, new Observer<List<CommentModel>>() {
            @Override
            public void onChanged(@Nullable List<CommentModel> commentModels) {
                commentProgressBar.setVisibility(View.INVISIBLE);
                adapter.setData(commentModels);
            }
        });

        commentViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if (s.equals(Utils.INTERNET_UNAVAILABLE)) {
                    finish();
                }
            }
        });

        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentRecyclerView.setAdapter(adapter);
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>  {
        List<CommentModel> comments;

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_comments, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            if (comments != null) {
                holder.emailListView.setText(comments.get(position).getEmail());
                holder.nameListView.setText(comments.get(position).getName());
                holder.bodyListView.setText(comments.get(position).getBody());
            }
        }

        @Override
        public int getItemCount() {
            if (comments != null) {
                return comments.size();
            } else {
                return 0;
            }
        }

        public void setData(List<CommentModel> comments) {
            this.comments = comments;
            notifyDataSetChanged();
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {
            TextView emailListView;
            TextView nameListView;
            TextView bodyListView;

            public CommentViewHolder(View itemView) {
                super(itemView);

                emailListView = (TextView) itemView.findViewById(R.id.textView_comment_email);
                nameListView = (TextView) itemView.findViewById(R.id.textView_comment_name);
                bodyListView = (TextView) itemView.findViewById(R.id.textView_comment_body);
            }
        }
    }


}
