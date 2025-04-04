package com.example.formomerpeled.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;


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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        setTitle("תפריט מסעדות");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_main) {
            startActivity(new Intent(this, MainActivity2.class));
            return true;
        } else if (id == R.id.action_register) {
            startActivity(new Intent(this, Register.class));
            return true;
        } else if (id == R.id.action_login) {
            startActivity(new Intent(this, Login.class));
            return true;
        }else if (id == R.id.action_addRes) {
            startActivity(new Intent(this, AddRestaurant.class));
            return true;
        } else if (id == R.id.action_restaurants) {
            startActivity(new Intent(this, ShowRestaurants.class));
            return true;
        } else if (id == R.id.action_users) {
            startActivity(new Intent(this, ShowUsers.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, Odot.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}