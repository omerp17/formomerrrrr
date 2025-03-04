package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Register extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    EditText etLname, etFname, etEmail, etPhone, etPassword;

    Button btnReg, btnBackReg;
    String lname, fname, email, phone, password;


    DatabaseService databaseService;
    AuthenticationService authenticationService ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        authenticationService=AuthenticationService.getInstance();
        databaseService=DatabaseService.getInstance();

    }

    private void initViews() {
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnReg = (Button)findViewById(R.id.btnReg);


        etPassword = findViewById(R.id.etPassword);

        btnReg.setOnClickListener(this);

        btnBackReg=findViewById(R.id.btnBackReg);
        btnBackReg.setOnClickListener(this);
    }
    public void onClick(View v) {
        if (v == btnBackReg) {
            Intent go = new Intent(this ,MainActivity2.class);
            startActivity(go);
        }
        if (v == btnReg) {
            fname = etFname.getText().toString();
            lname = etLname.getText().toString();
            phone = etPhone.getText().toString();
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            //check if registration is valid
            Boolean isValid=true;
            if (fname.length()<2){
               etFname.setError("הכנס שם מעל 2 תווים");
                isValid = false;
            }
            if (lname.length()<2){
                Toast.makeText(Register.this,"שם משפחה קצר מדי", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (phone.length()<9||phone.length()>10){
                Toast.makeText(Register.this,"מספר הטלפון לא תקין", Toast.LENGTH_LONG).show();
                isValid = false;
            }

            if (!email.contains("@")){
                Toast.makeText(Register.this,"כתובת האימייל לא תקינה", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if(password.length()<6){
                Toast.makeText(Register.this,"הסיסמה קצרה מדי", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if(password.length()>20){
                Toast.makeText(Register.this,"הסיסמה ארוכה מדי", Toast.LENGTH_LONG).show();
                isValid = false;
            }

            if (isValid==true){



                authenticationService.signUp(email, password, new  AuthenticationService.AuthCallback<String>() {

                    public void onCompleted(String uid) {
                        User user = new User();
                        user.setId(uid);
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setFname(fname);
                        user.setLname(lname);
                        user.setPhone(phone);
                        user.setAdmin(false);
                        databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {
                                Log.d(TAG, "onCompleted: User registered successfully");
                                                  //   / save the user to shared preferences
                                SharedPreferencesUtil.saveUser(Register.this, user);
                                Log.d(TAG, "onCompleted: Redirecting to MainActivity");
                                /// Redirect to MainActivity and clear back stack to prevent user from going back to register screen
                                Intent mainIntent = new Intent(Register.this, MainActivity2.class);
                                /// clear the back stack (clear history) and start the MainActivity
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Log.e(TAG, "onFailed: Failed to register user", e);
                            //    / show error message to user
                                Toast.makeText(Register.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                            //    / sign out the user if failed to register
                            //    / this is to prevent the user from being logged in again
                                authenticationService.signOut();
                            }
                        });
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to Sing up user", e);
                        /// show error message to user
                        Toast.makeText(Register.this, "Failed to Sing Up user", Toast.LENGTH_SHORT).show();
                    }
                });
            }




        }








        }


    }
