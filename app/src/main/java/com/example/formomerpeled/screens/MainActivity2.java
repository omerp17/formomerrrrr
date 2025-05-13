package com.example.formomerpeled.screens;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.NPH;
import com.example.formomerpeled.Utils.NotificationHelper;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    Button btnRegMain, btnLogMain, btnAddNewRestaurant,btnAboutUs, btnGoAllRestaurantFromMain;
            ImageButton btnAccount;


    private ActivityResultCallback<Boolean> isGranted;
    ActivityResultLauncher<String> requestNotificationPermissionLauncher;




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


        requestNotificationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted-> {
            if (!isGranted) {
                Toast.makeText(this, "עלייך לאשר", Toast.LENGTH_SHORT).show();
            }
        });

        if (!NPH.hasNP(this)) {
            NPH.requestNP(requestNotificationPermissionLauncher);
        }

        NotificationHelper.sendNotification(this, "Fine G-Free", "Welcome to Fine G-Free");
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