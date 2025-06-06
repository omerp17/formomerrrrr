package com.example.formomerpeled.screens;

import android.app.PendingIntent;
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

    private EditText etFirstName, etLastName, etPhone;
    private Button btnSaveChanges;

    DatabaseService databaseService;
    private AuthenticationService authenticationService;
    String uid;
    private User user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseService=DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();
        uid=authenticationService.getCurrentUserId();


        // מציאת משתנים
        etFirstName = findViewById(R.id.etFirstNameChange);
        etLastName = findViewById(R.id.etLastNameChange);
        etPhone = findViewById(R.id.etPhoneChange);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);




        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user2) {
                user=user2;


                if (user != null) {
                    // הצגת פרטי המשתמש ב-EditTexts
                    etFirstName.setText(user.getFname());
                    etLastName.setText(user.getLname());
                    etPhone.setText(user.getPhone());


                }


            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(Profile.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
            }
        });






        // קבלת המשתמש המחובר


        // כפתור שמירת שינויים
        btnSaveChanges.setOnClickListener(v -> {
            if (user != null) {
                // עדכון הערכים באובייקט המשתמש
                user.setFname(etFirstName.getText().toString());
                user.setLname(etLastName.getText().toString());
                user.setPhone(etPhone.getText().toString());

                finish();

                // שמירת הערכים המעודכנים ב-SharedPreferences
                SharedPreferencesUtil.saveUser(getApplicationContext(), user);

                // עדכון המשתמש ב-Firebase
               databaseService.updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        menu.removeItem(R.id.action_admin);
        if(user!=null) {
            if (user.isAdmin()) {

                menu.add(R.id.action_admin);


            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_admin){



                if (user.isAdmin()) {
                    Intent go = new Intent(Profile.this, AdminPage.class);
                    startActivity(go);
                } else {
                    Toast.makeText(Profile.this, "אינך מנהל", Toast.LENGTH_SHORT).show();

                }


            }

       if(id == R.id.action_home){
            Intent go = new Intent(Profile.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(Profile.this, Profile.class);
            startActivity(go);
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
