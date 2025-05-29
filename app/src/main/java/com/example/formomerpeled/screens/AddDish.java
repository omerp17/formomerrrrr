package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.adapter.RestaurantNameAdapter;
import com.example.formomerpeled.models.Dish;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AddDish extends AppCompatActivity implements View.OnClickListener{

    EditText etDishName, etDishPrice, etDishDetails;
    String dishName,details;
    double price;



    DatabaseService databaseService;



    Button addDishButton;
    String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_dish);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        Intent takeit = getIntent();
        restaurantId= (String) takeit.getSerializableExtra("resId");

        databaseService=DatabaseService.getInstance();


    }

    private void initViews() {

        etDishDetails=findViewById(R.id.et_dishDetails);
        etDishPrice=findViewById(R.id.et_dishPrice);
        etDishName=findViewById(R.id.et_dishName);

        addDishButton = findViewById(R.id.addDishButton);
        addDishButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==addDishButton) {
            details = etDishDetails.getText().toString();
            price = Double.parseDouble(etDishPrice.getText().toString());
            dishName = etDishName.getText().toString();

            String dishId=databaseService.generateDishId();
            Dish newDish = new Dish(dishId, dishName, restaurantId, price, details, 0, 0.0, 0.0);
            databaseService.createNewDish(newDish, new DatabaseService.DatabaseCallback<Void>() {


                @Override
                public void onCompleted(Void object) {

                    //   Intent go = new Intent(AddDish.this, MainActivity2.class);
                  //  startActivity(go);

                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        }



    }


}