package com.example.formomerpeled.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.adapter.UserAdapter;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowUsers extends AppCompatActivity {

    /// tag for logging
    private static final String TAG = "ShowUsers";

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();

    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users); // Make sure this XML exists in your res/layout folder

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set the layout manager (linear vertical list)

        // Sample data for users
        userList = new ArrayList<>();

        // Create the adapter and set it to the RecyclerView
        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        // Get the instance of the database service
        databaseService = DatabaseService.getInstance();

        // Get all the users from the database
        databaseService.getUsers(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> object) {
                Log.d(TAG, "onCompleted: " + object);
                userList.clear();
                userList.addAll(object);
                // Notify the adapter that the data has changed
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);
            }
        });
    }
}
