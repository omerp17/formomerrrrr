package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    Button btnRegMain, btnLogMain, btnAddNewRestaurant,btnAboutUs, btnGoAllRestaurantFromMain;
            ImageButton btnAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
    }
    private void initViews() {
        btnRegMain = (Button)findViewById(R.id.btnRegMain);
        btnRegMain.setOnClickListener(this);
        btnLogMain = (Button)findViewById(R.id.btnLogMain);
        btnLogMain.setOnClickListener(this);
        btnAboutUs = (Button)findViewById(R.id.btnAboutUs);
        btnAboutUs.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v == btnRegMain) {
            Intent go = new Intent(this, Register.class);
            startActivity(go);

        }
        if (v == btnLogMain) {
            Intent go = new Intent(this, Login.class);
            startActivity(go);
        }
        if (v == btnAboutUs) {
            Intent go = new Intent(this ,Odot.class);
            startActivity(go);
        }
    }

}

