package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.Utils.SharedPreferencesUtil;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

public class Profile extends AppCompatActivity {

    private EditText etFirstName, etLastName, etPhone, etEmail, etPassword;
    private Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // מציאת משתנים
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);


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
                        Intent go = new Intent(Profile.this, AdminPage.class);
                        startActivity(go);
                    }
                    else
                        Toast.makeText(Profile.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(Profile.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                }
            });
        }
        else if(id == R.id.action_home){
            Intent go = new Intent(Profile.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(Profile.this, Profile.class);
            startActivity(go);
        }
        else if (id == R.id.action_guide) {
//            Intent go = new Intent(Profile.this, Guide.class);
//            startActivity(go);
        }
        else if (id == R.id.action_about) {
            Intent go = new Intent(Profile.this, Odot.class);
            startActivity(go);
        }
        else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(Profile.this, MainActivity2.class);
            startActivity(go);
        }


        return true;
    }
}
