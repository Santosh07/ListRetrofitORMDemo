package com.santoshdhakal.internshipchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.santoshdhakal.internshipchallenge.models.PostModel;
import com.santoshdhakal.internshipchallenge.network.RetrofitClientInstance;
import com.santoshdhakal.internshipchallenge.services.WebService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        WebService service = RetrofitClientInstance.getRetrofitInstance().create(WebService.class);
        Call<List<PostModel>> call = service.getAllPosts();
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                ArrayList<PostModel> postLists = (ArrayList<PostModel>) response.body();

                System.out.println(postLists.size());

                for (PostModel model: postLists) {
                    System.out.println("Id : " + model.getId());
                }

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {

            }
        });
    }

}
