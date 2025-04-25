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

public class Odot extends AppCompatActivity  {
    Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_odot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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
                        Intent go = new Intent(Odot.this, AdminPage.class);
                        startActivity(go);
                    }
                    else
                        Toast.makeText(Odot.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(Odot.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("Odot.thisMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                }
            });
        }
        else if(id == R.id.action_home){
            Intent go = new Intent(Odot.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(Odot.this, Profile.class);
            startActivity(go);
        }
        else if (id == R.id.action_guide) {
//            Intent go = new Intent(Odot.this, Guide.class);
//            startActivity(go);
        }
        else if (id == R.id.action_about) {
            Intent go = new Intent(Odot.this, Odot.class);
            startActivity(go);
        }
        else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(Odot.this, MainActivity2.class);
            startActivity(go);
        }


        return true;
    }

}