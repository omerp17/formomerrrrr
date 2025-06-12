package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.adapter.DishAdapter;
import com.example.formomerpeled.models.Dish;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowDishes extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ShowDishes";

    Restaurant res;

    private RecyclerView rvDishes;
    private DishAdapter dishAdapter;
    private List<Dish> dishList = new ArrayList<>();
    private SearchView searchView;
    private DatabaseService databaseService;
    private ImageButton ibSearchDish;

    Button btnAddDish, btnShowDishesBackToView, btnAddDishReview;
String resId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dishes);
        databaseService = DatabaseService.getInstance();
        initViews();
        res = (Restaurant) getIntent().getSerializableExtra("res");
        resId= (String) getIntent().getSerializableExtra("resId");
        if(res!=null)

                resId=res.getId();
        loadDishes();


    }

    private void initViews() {

        btnAddDish=findViewById(R.id.btnAddDish);
        btnAddDish.setOnClickListener(this);
        rvDishes = findViewById(R.id.rvDishes);
        rvDishes.setLayoutManager(new LinearLayoutManager(this));

        initSearch();

    }

    private void loadDishes() {

        if(resId!=null) {

            dishAdapter = new DishAdapter(ShowDishes.this, dishList);
            rvDishes.setAdapter(dishAdapter);

            databaseService.getRestaurantDishes(resId, new DatabaseService.DatabaseCallback<List<Dish>>() {
                @Override
                public void onCompleted(List<Dish> dishes) {
                    Log.d(TAG, "Dishes loaded: " + dishes);
                    dishList.clear();
                    dishList.addAll(dishes);
                    dishAdapter.notifyDataSetChanged();


                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "Error loading dishes", e);
                    Toast.makeText(ShowDishes.this, "שגיאה בטעינת מנות", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {   Toast.makeText(ShowDishes.this, "שגיאה  מנות", Toast.LENGTH_SHORT).show();   }
    }

    private void initSearch() {
        ibSearchDish= findViewById(R.id.ibSearchDish);
        ibSearchDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        searchView = findViewById(R.id.searchViewDishes);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterDishes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDishes(newText);
                return false;
            }
        });
    }

    private void filterDishes(String query) {
        List<Dish> filteredList = new ArrayList<>();
        for (Dish dish : dishList) {
            if (dish.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(dish);
            }
        }

        if (!filteredList.isEmpty()) {
            dishAdapter.updateList(filteredList);
        } else {
            Toast.makeText(this, "אין מנות בשם הזה", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        if(v==ibSearchDish){

           // loadDishes();
        }

        if(v==btnAddDish){

            Intent go= new Intent(ShowDishes.this, AddDish.class);
            go.putExtra("resId", res.getId());
            go.putExtra("res", res);
            startActivity(go);
        }

        if(v==btnAddDishReview){

            Intent go= new Intent(ShowDishes.this, ReviewDish.class);
            go.putExtra("res", res);
            startActivity(go);
        }

        if(v==btnShowDishesBackToView){

            Intent go= new Intent(ShowDishes.this, ViewDetails.class);
            startActivity(go);
        }

    }
    private AuthenticationService authenticationService = AuthenticationService.getInstance();

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
                        Intent go = new Intent(ShowDishes.this, AdminPage.class);
                        startActivity(go);
                    }
                    else
                        Toast.makeText(ShowDishes.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(ShowDishes.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                }
            });
        }
        else if(id == R.id.action_home){
            Intent go = new Intent(ShowDishes.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_restaurants) {
            Intent go = new Intent(ShowDishes.this, ShowRestaurants.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(ShowDishes.this, Profile.class);
            startActivity(go);
        }

        else if (id == R.id.action_about) {
            Intent go = new Intent(ShowDishes.this, Odot.class);
            startActivity(go);
        }
        else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(ShowDishes.this, MainActivity2.class);
            startActivity(go);
        }


        return true;
    }

}
