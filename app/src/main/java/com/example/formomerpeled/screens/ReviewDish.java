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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.adapter.DishAdapter;
import com.example.formomerpeled.models.Dish;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReviewDish extends AppCompatActivity implements View.OnClickListener {





    private Dish dish=null ;

    private DatabaseService databaseService;


    Button btnSendReviewsDish;
    RatingBar ratingbar;
    double rate;

    TextView txtDishNameInReviewPage;
    Intent takeit;
    private Restaurant res=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_dish);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService=DatabaseService.getInstance();
        takeit=getIntent();
        dish= (Dish) takeit.getSerializableExtra("dish");

        res = (Restaurant) getIntent().getSerializableExtra("res");


        initViews();






    }

    private void initViews() {


        ratingbar=findViewById(R.id.ratingBarDish);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                dish.setNumberRate();
                dish.setNewSumRate(rating);
                dish.setRate(dish.getRate());

            }
        });

        btnSendReviewsDish = findViewById(R.id.btnSendReviewsDish);
        btnSendReviewsDish.setOnClickListener( this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnSendReviewsDish.getId()) {



            databaseService.updateDish(dish, new DatabaseService.DatabaseCallback<Void>() {


                @Override
                public void onCompleted(Void object) {
                    Intent go = new Intent(ReviewDish.this, ShowDishes.class);



                    go.putExtra("resId", dish.getResId());
                    startActivity(go);
                }

                @Override
                public void onFailed(Exception e) {

                }
            });


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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_admin) {
            databaseService.getUser(authenticationService.getCurrentUserId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if (user.isAdmin()) {
                        Intent go = new Intent(ReviewDish.this, AdminPage.class);
                        startActivity(go);
                    } else
                        Toast.makeText(ReviewDish.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(ReviewDish.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                }
            });
        } else if (id == R.id.action_home) {
            Intent go = new Intent(ReviewDish.this, AfterPage.class);
            startActivity(go);
        } else if (id == R.id.action_restaurants) {
            Intent go = new Intent(ReviewDish.this, ShowRestaurants.class);
            startActivity(go);
        } else if (id == R.id.action_update) {
            Intent go = new Intent(ReviewDish.this, Profile.class);
            startActivity(go);
        } else if (id == R.id.action_about) {
            Intent go = new Intent(ReviewDish.this, Odot.class);
            startActivity(go);
        } else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(ReviewDish.this, MainActivity2.class);
            startActivity(go);
        }
        return true;
    }
}