package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.Utils.SharedPreferencesUtil;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // קבלת משתנים של ה-TextView ושל כפתור חזור
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvfName = findViewById(R.id.tvFirstName);
        TextView tvLName = findViewById(R.id.tvLastName);
        TextView tvPassword = findViewById(R.id.tvPassword);
        Button btnBack = findViewById(R.id.btnBack); // הכפתור חזור

        // קבלת המשתמש המחובר
        User user = SharedPreferencesUtil.getUser(getApplicationContext());

        if (user != null) {
            // הצגת פרטי המשתמש
            tvEmail.setText("Email: " + user.getEmail());
            tvPhone.setText("Phone: " + (user.getPhone() != null ? user.getPhone() : "N/A"));
            tvfName.setText("Name: " + (user.getFname() != null ? user.getFname() : "N/A"));
            tvLName.setText("Name: " + (user.getLname() != null ? user.getLname() : "N/A"));
            tvPassword.setText("Name: " + (user.getPassword() != null ? user.getPassword() : "N/A"));
        } else {
            tvEmail.setText("User not logged in");
        }

        // פעולה על לחיצה על כפתור חזור
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, MainActivity2.class);
            startActivity(intent);
            finish(); // סוגר את המסך הנוכחי כדי שלא יחזור אליו בלחיצה על כפתור ה-back
        });
    }
}
