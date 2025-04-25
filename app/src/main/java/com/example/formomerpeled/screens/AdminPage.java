package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;


public class AdminPage extends AppCompatActivity implements View.OnClickListener {


    Button btnAddRestaurant, btnGoUsersPage, btnGoAfterLoginMain, btnGoAllRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.adminpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {

        btnAddRestaurant = findViewById(R.id.btnAddRestaurant2);
        btnAddRestaurant.setOnClickListener(this);
        btnGoUsersPage = findViewById(R.id.btnGoUsersPage);
        btnGoUsersPage.setOnClickListener(this);
        btnGoAfterLoginMain = findViewById(R.id.btnGoAfterLoginMain);
        btnGoAfterLoginMain.setOnClickListener(this);
        btnGoAllRestaurants = findViewById(R.id.btnGoAllRestaurants);
        btnGoAllRestaurants.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == btnAddRestaurant.getId()) {
            Intent go = new Intent(this, AddRestaurant.class);
            startActivity(go);
        }
        if (v.getId() == btnGoAfterLoginMain.getId()) {
            Intent go = new Intent(this, MainActivity2.class);
            startActivity(go);
        }
        if (v.getId() == btnGoAllRestaurants.getId()) {
            Intent go = new Intent(this, ShowRestaurants.class);
            startActivity(go);
        }
        if (v.getId() == btnGoUsersPage.getId()) {
            Intent go = new Intent(this, ShowUsers.class);
            startActivity(go);
        }
   /* @Override
    public void onClick(View v) {

        Intent go = new Intent(getApplicationContext(),SearchRestaurant.class);
        startActivity(go);
    }*/
    }


    private AuthenticationService authenticationService = AuthenticationService.getInstance();
    private DatabaseService databaseService = DatabaseService.getInstance();
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_admin){
            databaseService.getUser(authenticationService.getCurrentUserId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if(user.isAdmin())
                    {
                        Intent go = new Intent(AdminPage.this, AdminPage.class);
                        startActivity(go);
                    }
                    else
                        Toast.makeText(AdminPage.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AdminPage.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                }
            });
        }
        else if(id == R.id.action_home){
            Intent go = new Intent(AdminPage.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(AdminPage.this, Profile.class);
            startActivity(go);
        }
        else if (id == R.id.action_guide) {
//            Intent go = new Intent(AdminPage.this, Guide.class);
//            startActivity(go);
        }
        else if (id == R.id.action_about) {
            Intent go = new Intent(AdminPage.this, Odot.class);
            startActivity(go);
        }
        else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(AdminPage.this, MainActivity2.class);
            startActivity(go);
        }


        return true;
    }
}