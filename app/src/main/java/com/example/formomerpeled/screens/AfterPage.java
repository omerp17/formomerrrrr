package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class AfterPage extends AppCompatActivity implements View.OnClickListener {
    Button btnAddNewRestaurant, btnGoAllRestaurantFromMain, btnAboutUs;
    ImageButton btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

    }


    private void initViews() {

        btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(this);

        btnGoAllRestaurantFromMain = (Button) findViewById(R.id.btnGoAllRestaurantFromMain);
        btnGoAllRestaurantFromMain.setOnClickListener(this);
        btnAddNewRestaurant = (Button) findViewById(R.id.btnAddNewRestaurant);
        btnAddNewRestaurant.setOnClickListener(this);

        btnAboutUs = (Button)findViewById(R.id.btnAboutUs);
        btnAboutUs.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if (v == btnGoAllRestaurantFromMain) {
            Intent go = new Intent(this, ShowRestaurants.class);
            startActivity(go);
        }
        if (v == btnAccount) {
            Intent go = new Intent(this, Profile.class);
            startActivity(go);
        }
        if (v == btnAddNewRestaurant) {
            Intent go = new Intent(this, AddRestaurant.class);
            startActivity(go);
        }
        if (v == btnAboutUs) {
            Intent go = new Intent(this, Odot.class);
            startActivity(go);
        }
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
                         Intent go = new Intent(AfterPage.this, AdminPage.class);
                         startActivity(go);
                     }
                     else
                         Toast.makeText(AfterPage.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                 }

                 @Override
                 public void onFailed(Exception e) {
                     Toast.makeText(AfterPage.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                 }
             });
        }
        else if(id == R.id.action_home){
            Intent go = new Intent(AfterPage.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(AfterPage.this, Profile.class);
            startActivity(go);
        }

        else if (id == R.id.action_about) {
            Intent go = new Intent(AfterPage.this, Odot.class);
            startActivity(go);
        }
        else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(AfterPage.this, MainActivity2.class);
            startActivity(go);
        }


        return true;
    }



}