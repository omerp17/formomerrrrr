package com.example.formomerpeled.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.adapter.RestaurantsAdapter;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowRestaurants extends AppCompatActivity {

    /// tag for logging
    private static final String TAG = "ShowRestaurants";

    private RecyclerView recyclerView;
    private RestaurantsAdapter restaurantsAdapter;
    private List<Restaurant> restaurantList=new ArrayList<>();



    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurants); // Make sure this XML exists in your res/layout folder

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set the layout manager (linear vertical list)

        // Sample data for restaurants
        restaurantList = new ArrayList<>();

        // Create the adapter and set it to the RecyclerView
        restaurantsAdapter = new RestaurantsAdapter(restaurantList);
        recyclerView.setAdapter(restaurantsAdapter);



        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();

      
        /// Adapter for the restaurant recycler view
        /// @see ArrayAdapter
        /// @see Restaurant
      

        /// Adapter for the restaurant spinner
        /// @see RestaurantSpinnerAdapter
        /// @see Restaurant
        
        /// get all the restaurants from the database

        databaseService.getRestaurants(new DatabaseService.DatabaseCallback<List<Restaurant>>() {



            @Override
            public void onCompleted(List<Restaurant> object) {
                Log.d(TAG, "onCompleted: " + object);
                restaurantList.clear();
                restaurantList.addAll(object);
                /// notify the adapter that the data has changed
                /// this specifies that the data has changed
                /// and the adapter should update the view
                /// @see RestaurantSpinnerAdapter#notifyDataSetChanged()


                restaurantsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);
            }
        });


    }






}
