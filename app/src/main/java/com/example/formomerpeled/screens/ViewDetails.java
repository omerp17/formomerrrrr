package com.example.formomerpeled.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

public class ViewDetails extends AppCompatActivity implements View.OnClickListener {

    TextView txtRestaurantNameView, txtRestaurantDomainView, txtContactView, txtRestaurantPhoneNumberView;
    Button btnBackView, btnAddReview;
    private ImageView ivDViewDetails;
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;
    private String uid;


    Restaurant restaurant=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();
        uid = authenticationService.getCurrentUserId();


        init_views();

        Intent reciever = getIntent();
        restaurant= (Restaurant) reciever.getSerializableExtra("res");


        txtRestaurantNameView.setText(restaurant.getName());
       txtRestaurantPhoneNumberView.setText(restaurant.getPhoneNumber());


      //  String imS=reciever.getStringExtra("image");

        if(restaurant!=null) {
            txtRestaurantDomainView.setText(restaurant.getDomain());

            if (restaurant.getImageCode() != null) {
                Bitmap bitmap = ImageUtil.convertFrom64base(restaurant.getImageCode());
                ivDViewDetails.setImageBitmap(bitmap);
            }


        }

    }

    private void init_views() {
        txtRestaurantNameView = findViewById(R.id.txtRestaurantNameView);
        txtRestaurantDomainView = findViewById(R.id.txtRestaurantDomainView);
        txtContactView = findViewById(R.id.txtContactView);
        txtRestaurantPhoneNumberView=findViewById(R.id.txtRestaurantPhoneNumberView);
        btnBackView = findViewById(R.id.btnBackView);
        btnAddReview = findViewById(R.id.btnAddReview);
        ivDViewDetails=findViewById(R.id.ivResViewDetails);

        btnBackView.setOnClickListener(this);
        btnAddReview.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        if (v == btnBackView) {
            Intent go = new Intent(this, ShowRestaurants.class);
            startActivity(go);
        }
        if (v == btnAddReview) {
            Intent go = new Intent(this, AddReview.class);
            startActivity(go);
        }

    }

    public void saveFav(View view) {

        Toast.makeText(ViewDetails.this,"pppp[ ",Toast.LENGTH_LONG).show();
        if(restaurant!=null) {

            databaseService.userFavoriteRestaurant(uid, restaurant, new DatabaseService.DatabaseCallback<Void>() {

                @Override
              public   void onFailed(Exception e) {
                    Toast.makeText(ViewDetails.this,"not ",Toast.LENGTH_LONG).show();


                }

                @Override
              public   void onCompleted(Void object) {

                    Toast.makeText(ViewDetails.this,"Save",Toast.LENGTH_LONG).show();

                }
            });

        }
    }


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
                        Intent go = new Intent(ViewDetails.this, AdminPage.class);
                        startActivity(go);
                    }
                    else
                        Toast.makeText(ViewDetails.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(ViewDetails.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                }
            });
        }
        else if(id == R.id.action_home){
            Intent go = new Intent(ViewDetails.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(ViewDetails.this, Profile.class);
            startActivity(go);
        }
        else if (id == R.id.action_guide) {
//            Intent go = new Intent(ViewDetails.this, Guide.class);
//            startActivity(go);
        }
        else if (id == R.id.action_about) {
            Intent go = new Intent(ViewDetails.this, Odot.class);
            startActivity(go);
        }
        else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(ViewDetails.this, MainActivity2.class);
            startActivity(go);
        }


        return true;
    }
}

