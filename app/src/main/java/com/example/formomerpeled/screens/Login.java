package com.example.formomerpeled.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "loginToFireBase";
    TextView tvLog;
    EditText etEmail2, etPass2;
    Button btnLog, btnBackLogin;

    String email2, pass2;

    public static final String MyPREFERENCES = "MyPrefs";

    AuthenticationService authenticationService;
    DatabaseService databaseService;
    SharedPreferences sharedpreferences;


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


        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        init_views();
        email2 = sharedpreferences.getString("email", "");
        pass2 = sharedpreferences.getString("password", "");
        etEmail2.setText(email2);
        etPass2.setText(pass2);
        btnLog.setOnClickListener(this);
    }

    private void init_views() {
        btnLog = findViewById(R.id.btnLog);
        etEmail2 = findViewById(R.id.etEmail2);
        etPass2 = findViewById(R.id.etPassword2);
        btnBackLogin=findViewById(R.id.btnBackLogin);

        btnLog.setOnClickListener(this);
        btnBackLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnBackLogin) {
            Intent go = new Intent(this, MainActivity2.class);
            startActivity(go);
        }
        if(btnLog==v) {
            email2 = etEmail2.getText().toString();
            pass2 = etPass2.getText().toString();

            authenticationService.signIn(email2, pass2, new AuthenticationService.AuthCallback<String>() {
                @Override
                public void onCompleted(String id) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("email", email2);
                    editor.putString("password", pass2);

                    editor.commit();
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");

                    databaseService.getUser(id, new DatabaseService.DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User user) {

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

                @Override
                public void onFailed(Exception e) {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", e);
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
