package com.example.formomerpeled.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.adapter.UserAdapter;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowUsers extends AppCompatActivity {

    private static final String TAG = "ShowUsers";

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    private SearchView searchView;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        recyclerView = findViewById(R.id.rvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchView);

        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        databaseService = DatabaseService.getInstance();

        loadUsers();
        setupSearch();
    }

    private void loadUsers() {
        databaseService.getUsers(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> object) {
                Log.d(TAG, "onCompleted: " + object);
                userList.clear();
                userList.addAll(object);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);
            }
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                ArrayList<User> filteredList = new ArrayList<>();
                databaseService.getUsers(new DatabaseService.DatabaseCallback<List<User>>() {
                    @Override
                    public void onCompleted(List<User> users) {
                        for (User user : users) {
                            if (user.getFname().equalsIgnoreCase(txt)) {
                                filteredList.add(user);
                            }
                        }

                        if (!filteredList.isEmpty()) {
                            userAdapter = new UserAdapter(filteredList, ShowUsers.this);
                            recyclerView.setAdapter(userAdapter);
                        } else {
                            Toast.makeText(ShowUsers.this, "אין משתמשים בשם הזה", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "Error searching users", e);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}