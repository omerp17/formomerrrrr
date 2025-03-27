package com.example.formomerpeled.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.SharedPreferencesUtil;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

import org.jetbrains.annotations.NotNull;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "loginToFireBase";
    TextView tvLog;
    EditText etEmail2, etPass2;
    Button btnLog, btnBackLogin, btnCreateNewUser;

    String email2, pass2;


    public static final String MyPREFERENCES = "MyPrefs";


    AuthenticationService authenticationService;
    DatabaseService databaseService;
    SharedPreferences sharedpreferences;


    public static boolean isAdmin = false;
    private User user2 = null;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init_views();
        authenticationService = AuthenticationService.getInstance();

        uid = authenticationService.getCurrentUserId();

        databaseService = DatabaseService.getInstance();
        user2 = SharedPreferencesUtil.getUser(Login.this);
        if (user2 != null) {
            etEmail2.setText(user2.getEmail());
            etPass2.setText(user2.getPassword());
        }


        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                user2 = object;
                if (user2 != null) {
                    etEmail2.setText(user2.getEmail());
                    etPass2.setText(user2.getPassword());
                }
            }

            @Override
            public void onFailed(Exception e) {

            }
        });


        btnLog.setOnClickListener(this);
        btnCreateNewUser.setOnClickListener(this);
    }

    private void init_views() {
        btnLog = findViewById(R.id.btnLog);
        etEmail2 = findViewById(R.id.etEmail2);
        etPass2 = findViewById(R.id.etPassword2);
        btnBackLogin = findViewById(R.id.btnBackLogin);
        btnCreateNewUser = findViewById(R.id.btnCreateNewUser);

        btnCreateNewUser.setOnClickListener(this);
        btnLog.setOnClickListener(this);
        btnBackLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnBackLogin) {
            Intent go = new Intent(this, MainActivity2.class);
            startActivity(go);
        }

        if (v == btnCreateNewUser) {
            Intent go = new Intent(this, Register.class);
            startActivity(go);
        }

        if (btnLog == v) {
            email2 = etEmail2.getText().toString();
            pass2 = etPass2.getText().toString();

            authenticationService.signIn(email2, pass2, new AuthenticationService.AuthCallback<String>() {
                @Override
                public void onCompleted(String id) {


                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");


                    databaseService.getUser(id, new DatabaseService.DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User object) {
                            user2 = object;


                            SharedPreferencesUtil.saveUser(Login.this, user2);


                            if (user2.isAdmin()) {
                                isAdmin = true;
                                Intent go = new Intent(Login.this, AdminPage.class);
                                startActivity(go);
                            } else {

                                Intent go = new Intent(Login.this, MainActivity2.class);
                                startActivity(go);
                            }

                            // TODO add intent to main page
                        }


                        @Override
                        public void onFailed(Exception e) {

                        }
                    });


                }

                @Override
                public void onFailed(Exception e) {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", e);
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    authenticationService.signOut();
                }
            });
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        } else if (id == R.id.action_addRes) {
            startActivity(new Intent(this, AddRestaurant.class));
            return true;
        }
        else if (id == R.id.action_about) {
            startActivity(new Intent(this, Odot.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
