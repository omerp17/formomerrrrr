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

public class AddDish extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText etDishName, etDishPrice, etDishDetails;
    String dishName,details;
    double price;

    ArrayList<String> resNameList=new ArrayList<>();
    ArrayList<Restaurant> resList=new ArrayList<>();
    ArrayAdapter<String> stringArrayAdapter;

    DatabaseService databaseService;

    Spinner spNames;
    String restName;



    Button addDishButton;
    Restaurant restaurant;

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


        stringArrayAdapter=new ArrayAdapter<>(AddDish.this, android.R.layout.simple_spinner_item,resNameList);
        spNames.setAdapter(stringArrayAdapter);
        spNames.setOnItemSelectedListener(this);


        databaseService=DatabaseService.getInstance();
        databaseService.getRestaurants(new DatabaseService.DatabaseCallback<List<Restaurant>>() {
            @Override
           public void onCompleted(List<Restaurant> object) {
                resList.clear();
                resList.addAll(object);

                for(int i=0;i<resList.size();i++){
                    resNameList.add(resList.get(i).getName());

                   stringArrayAdapter.notifyDataSetChanged();



                }


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
        dishName=etDishName.getText().toString();





        Dish newDish=new Dish(databaseService.generateDishId(), dishName,restaurant,price,details);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        restaurant=resList.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}