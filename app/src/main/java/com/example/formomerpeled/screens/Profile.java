package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.Utils.SharedPreferencesUtil;
import com.example.formomerpeled.services.DatabaseService;

public class Profile extends AppCompatActivity {

    private EditText etFirstName, etLastName, etPhone, etEmail, etPassword;
    private Button btnSaveChanges, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // מציאת משתנים
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnBack.getId()) {
                    Intent go = new Intent(Profile.this, AfterPage.class);
                    startActivity(go);
                }

            }
        });

        // קבלת המשתמש המחובר
        User user = SharedPreferencesUtil.getUser(getApplicationContext());

        if (user != null) {
            // הצגת פרטי המשתמש ב-EditTexts
            etFirstName.setText(user.getFname());
            etLastName.setText(user.getLname());
            etPhone.setText(user.getPhone());
            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
        } else {
            // אם אין משתמש מחובר, הצגת הודעה מתאימה
            etEmail.setText("User not logged in");
        }

        // כפתור שמירת שינויים
        btnSaveChanges.setOnClickListener(v -> {
            if (user != null) {
                // עדכון הערכים באובייקט המשתמש
                user.setFname(etFirstName.getText().toString());
                user.setLname(etLastName.getText().toString());
                user.setPhone(etPhone.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setPassword(etPassword.getText().toString());

                // שמירת הערכים המעודכנים ב-SharedPreferences
                SharedPreferencesUtil.saveUser(getApplicationContext(), user);

                // עדכון המשתמש ב-Firebase
                DatabaseService.getInstance().updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        Toast.makeText(Profile.this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Toast.makeText(Profile.this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ProfileError", "Error updating user", e);
                    }
                });
            } else {
                Toast.makeText(Profile.this, "No user found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
