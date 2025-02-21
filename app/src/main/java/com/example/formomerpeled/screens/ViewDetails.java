package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.Restaurant;

public class ViewDetails extends AppCompatActivity implements View.OnClickListener {

    TextView txtRestaurantNameView, txtRestaurantDomainView, txtContactView, txtRestaurantPhoneNumberView;
    Button btnBackView;
    private ImageView ivDViewDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init_views();

        Intent reciever = getIntent();


        txtRestaurantNameView.setText(reciever.getStringExtra("name"));
        txtRestaurantPhoneNumberView.setText(reciever.getStringExtra("phone"));
        txtRestaurantDomainView.setText(reciever.getStringExtra("website"));


    }

    private void init_views() {
        txtRestaurantNameView = findViewById(R.id.txtRestaurantNameView);
        txtRestaurantDomainView = findViewById(R.id.txtRestaurantDomainView);
        txtContactView = findViewById(R.id.txtContactView);
        txtRestaurantPhoneNumberView=findViewById(R.id.txtRestaurantPhoneNumberView);
        btnBackView = findViewById(R.id.btnBackView);

        btnBackView.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        if (v == btnBackView) {
            Intent go = new Intent(this, ShowRestaurants.class);
            startActivity(go);
        }

    }
}

