package com.example.formomerpeled.screens;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.RestaurantReview;
import com.example.formomerpeled.services.DatabaseService;
import com.example.formomerpeled.services.AuthenticationService;

import java.time.LocalDateTime;

public class AddReview extends AppCompatActivity {

    // Define variables for inputs
    EditText etFeedback;
    RatingBar ratingBar;
    Button submitButton;

    DatabaseService databaseService = DatabaseService.getInstance();
    AuthenticationService authenticationService = AuthenticationService.getInstance();

    String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_review);

        // Apply edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();

        restaurantId = getIntent().getStringExtra("restaurant_id");

        // Initialize the views
        initViews();
    }

    private void initViews() {
        // Initialize the input fields
        ratingBar = findViewById(R.id.userRatingBar);
        submitButton = findViewById(R.id.submitButton);

        // Set onClickListener for the submit button
        submitButton.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        // Get values from inputs
        String userId = authenticationService.getCurrentUserId();
        String feedback = etFeedback.getText().toString();
        float rating = ratingBar.getRating();

        // Validate inputs
        if (userId.isEmpty() || feedback.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a review object (you can adapt this to your database model)
        LocalDateTime currentDateTime = LocalDateTime.now();
        RestaurantReview review = new RestaurantReview(userId, feedback, rating, currentDateTime);

        // Call the database service to submit the review
        databaseService.submitReview(restaurantId,review, new DatabaseService.DatabaseCallback<Restaurant>() {
            @Override
            public void onCompleted(Restaurant restaurant) {
                // If successful, clear the fields and show success
                etFeedback.setText("");
                ratingBar.setRating(0);
                Toast.makeText(AddReview.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {
                // If there's an error, show a failure message
                Toast.makeText(AddReview.this, "Error submitting review", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
