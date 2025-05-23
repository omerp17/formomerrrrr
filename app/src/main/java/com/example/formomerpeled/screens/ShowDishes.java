package com.example.formomerpeled.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.adapter.DishAdapter;
import com.example.formomerpeled.models.Dish;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowDishes extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ShowDishes";

    private RecyclerView rvDishes;
    private DishAdapter dishAdapter;
    private List<Dish> dishList = new ArrayList<>();
    private SearchView searchView;
    private DatabaseService databaseService;
    private ImageButton ibSearchDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dishes);

        rvDishes = findViewById(R.id.rvDishes);
        rvDishes.setLayoutManager(new LinearLayoutManager(this));
        dishAdapter = new DishAdapter(this, dishList);
        rvDishes.setAdapter(dishAdapter);

        ibSearchDish = findViewById(R.id.ibSearchDish);
        ibSearchDish.setOnClickListener(this);

        databaseService = DatabaseService.getInstance();

        loadDishes();
        initSearch();
    }

    private void loadDishes() {
        databaseService.getRestaurantDishes(new DatabaseService.DatabaseCallback<List<Dish>>() {
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

    private void initSearch() {
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
        loadDishes();
    }
}
