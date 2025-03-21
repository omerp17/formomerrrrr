package com.example.formomerpeled.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.formomerpeled.R;
import com.example.formomerpeled.adapter.UserAdapter;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.DatabaseService;
import java.util.ArrayList;
import java.util.List;

public class ShowUsers extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ShowUsers";

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    private SearchView searchView;
    private DatabaseService databaseService;
    ImageButton ibBtnForShowUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        recyclerView = findViewById(R.id.rvUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ibBtnForShowUsers=findViewById(R.id.ibBtnForShowUsers);
        ibBtnForShowUsers.setOnClickListener(this);

        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        databaseService = DatabaseService.getInstance();

        loadUsers(); // Load users from the database

        initSearch(); // Initialize search functionality
    }

    private void loadUsers() {
        databaseService.getUsers(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                Log.d(TAG, "Users loaded: " + users);
                userList.clear();
                userList.addAll(users);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error loading users", e);
            }
        });
    }

    private void initSearch() {
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


               filterUsers(newText);
              return false;
            }
        });
    }

    private void filterUsers(String query) {
        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getFname().toLowerCase().equals(query.toLowerCase())) {
                filteredList.add(user); // Add user to filtered list if name matches exactly
            }
        }

        if (!filteredList.isEmpty()) {
            userAdapter.updateList(filteredList);
        } else {
            Toast.makeText(this, "אין משתמשים בשם הזה", Toast.LENGTH_SHORT).show(); // "No users with this name"
        }
    }

    @Override
    public void onClick(View v) {
        loadUsers();

    }
}
