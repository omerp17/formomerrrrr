package com.example.formomerpeled.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

public class ViewDetails extends AppCompatActivity implements View.OnClickListener {

    TextView txtRestaurantNameView, txtRestaurantDomainView, txtContactView, txtRestaurantPhoneNumberView;
    Button btnBackView;
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


        txtRestaurantNameView.setText(reciever.getStringExtra("name"));
        txtRestaurantPhoneNumberView.setText(reciever.getStringExtra("phone"));
        txtRestaurantDomainView.setText(reciever.getStringExtra("website"));

        String imS=reciever.getStringExtra("image");


        if (imS != null) {
            Bitmap bitmap = ImageUtil.convertFrom64base(imS);
            ivDViewDetails.setImageBitmap(bitmap);
        }




    }

    private void init_views() {
        txtRestaurantNameView = findViewById(R.id.txtRestaurantNameView);
        txtRestaurantDomainView = findViewById(R.id.txtRestaurantDomainView);
        txtContactView = findViewById(R.id.txtContactView);
        txtRestaurantPhoneNumberView=findViewById(R.id.txtRestaurantPhoneNumberView);
        btnBackView = findViewById(R.id.btnBackView);
        ivDViewDetails=findViewById(R.id.ivResViewDetails);

        btnBackView.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        if (v == btnBackView) {
            Intent go = new Intent(this, ShowRestaurants.class);
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
}

