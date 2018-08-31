package com.santoshdhakal.internshipchallenge;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.santoshdhakal.internshipchallenge.models.UserModel;
import com.santoshdhakal.internshipchallenge.singletons.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AppDatabase db = AppDatabase.getDatabase(this);

        new PopulateDbAsync(db).execute();

    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, ArrayList<UserModel>> {

        AppDatabase db;

        public PopulateDbAsync(AppDatabase db) {
            this.db = db;
        }

        @Override
        protected ArrayList<UserModel> doInBackground(Void... voids) {
            List<UserModel> users =  db.userDao().getAll();
            return (ArrayList<UserModel>)users;
        }

        @Override
        protected void onPostExecute(ArrayList<UserModel> userModels) {
            super.onPostExecute(userModels);

            for (UserModel user: userModels) {
                System.out.println("Name = " + user.getUsername());
            }
        }
    }
}
