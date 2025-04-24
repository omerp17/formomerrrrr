package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;

public class AfterPage extends AppCompatActivity implements View.OnClickListener {
    Button btnAddNewRestaurant, btnGoAllRestaurantFromMain;
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
    }
}