package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.Utils.SharedPreferencesUtil;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.adapter.RestaurantsAdapter;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.DatabaseService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowRestaurants extends AppCompatActivity {

    /// tag for logging
    private static final String TAG = "ShowRestaurants";

    private RecyclerView rvList;
    private RestaurantsAdapter restaurantsAdapter;
    private List<Restaurant> restaurantList=new ArrayList<>();

    private androidx.appcompat.widget.SearchView search;

    private DatabaseService databaseService;

    private ImageButton ibBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurants); // Make sure this XML exists in your res/layout folder
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();

        // Initialize RecyclerView
        rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this)); // Set the layout manager (linear vertical list)

        // Sample data for restaurants
        restaurantList = new ArrayList<>();

        // Create the adapter and set it to the RecyclerView
        restaurantsAdapter = new RestaurantsAdapter(restaurantList, ShowRestaurants.this);
        rvList.setAdapter(restaurantsAdapter);



        initViews();

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





        /// Adapter for the restaurant recycler view
        /// @see ArrayAdapter
        /// @see Restaurant


        /// Adapter for the restaurant spinner
        /// @see RestaurantSpinnerAdapter
        /// @see Restaurant

        /// get all the restaurants from the database



    }

    private void initViews() {
        ibBtn= findViewById(R.id.ibBtn);
        ibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        search = findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                ArrayList<Restaurant> list = new ArrayList<>();
                databaseService.getRestaurants(new DatabaseService.DatabaseCallback<List<Restaurant>>() {
                    @Override
                    public void onCompleted(List<Restaurant> restaurants) {
                        for(int i=0; i<restaurants.size();i++){
                            if(restaurants.get(i).getName().equals(txt))
                                list.add(restaurants.get(i));
                        }

                        if(list.size() != 0) {

                            rvList.setLayoutManager(new LinearLayoutManager(ShowRestaurants.this)); // Set the layout manager (linear vertical list)

                            restaurantsAdapter = new RestaurantsAdapter(list, ShowRestaurants.this) ;



                            rvList.setAdapter(restaurantsAdapter);
                        }
                        else
                            Toast.makeText(ShowRestaurants.this, "אין מסעדות בעלות השם הזה", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("ShowRestaurantsError", e.getMessage());
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurants, menu);
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
        } else if (id == R.id.action_login) {
            startActivity(new Intent(this, Login.class));
            return true;
        }
        else if (id == R.id.action_addRes) {
            startActivity(new Intent(this, AddRestaurant.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, Odot.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


//public void onLongClick(Restaurant restaurant) {
//    User user = SharedPreferencesUtil.getUser(ShowRestaurants.this);
//
//    if (user.isAdmin()) {
//        databaseService.deleteRestaurant(restaurant.getId(), new DatabaseService.DatabaseCallback<Void>() {
//            @Override
//            public void onCompleted(Void object) {
//                Toast.makeText(ShowRestaurants.this, "the res delete", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onFailed(Exception e) {
//
//            }
//        });
//
//
//    }}}
