package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AddDish extends AppCompatActivity implements View.OnClickListener {

    EditText etDishName, etDishPrice, etDishDetails;
    String dishName,details;
    double price;

    ArrayList<String> resName=new ArrayList<>();
    ArrayAdapter<String> stringArrayAdapter;

    DatabaseService databaseService;

    Spinner spNames;
    String restName;



    Button addDishButton;

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


        stringArrayAdapter=new ArrayAdapter<>(AddDish.this, android.R.layout.simple_spinner_item,resName);
        spNames.setAdapter(stringArrayAdapter);


        databaseService=DatabaseService.getInstance();
        databaseService.getRestaurantsNames(new DatabaseService.DatabaseCallback<List<String>>() {
            @Override
           public void onCompleted(List<String> object) {

                resName.addAll(object);

            }

            @Override
            public void onFailed(Exception e) {

            }
        });
  }

    private void initViews() {

        etDishDetails=findViewById(R.id.et_dishDetails);
        etDishPrice=findViewById(R.id.et_dishPrice);
        etDishName=findViewById(R.id.et_dishName);
        spNames = findViewById(R.id.spResName);
        addDishButton = findViewById(R.id.addDishButton);
        addDishButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        details=etDishDetails.getText().toString();
        price= Double.parseDouble(etDishPrice.getText().toString());


        restName=spNames.getSelectedItem().toString();



        Dish newDish=new Dish(databaseService.generateDishId(), dishName,restName,price,details);
        databaseService.createNewDish(newDish, new DatabaseService.DatabaseCallback<Void>() {


            @Override
          public   void onCompleted(Void object) {

                Intent go = new Intent(AddDish.this, MainActivity2.class);
                startActivity(go);

            }

            @Override
           public void onFailed(Exception e) {

            }
        });



    }
}