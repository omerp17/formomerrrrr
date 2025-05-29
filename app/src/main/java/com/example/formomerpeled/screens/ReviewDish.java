package com.example.formomerpeled.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import com.example.formomerpeled.services.DatabaseService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReviewDish extends AppCompatActivity implements View.OnClickListener {





    private Dish dish=null ;

    private DatabaseService databaseService;


    Button btnSendReviewsDish, btnBackToShowDishesFromReview;
    RatingBar ratingbar;
    double rate;

    TextView txtDishNameInReviewPage;
    Intent takeit;


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

        initViews();






    }

    private void initViews() {
        btnBackToShowDishesFromReview = (Button)findViewById(R.id.btnBackToShowDishesFromReview);
        btnBackToShowDishesFromReview.setOnClickListener((View.OnClickListener) this);

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

        databaseService.updateDish(dish, new DatabaseService.DatabaseCallback<Void>() {


            @Override
            public void onCompleted(Void object) {

            }

            @Override
            public void onFailed(Exception e) {

            }
        });

    }
}