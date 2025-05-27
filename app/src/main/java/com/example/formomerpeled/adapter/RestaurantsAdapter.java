package com.example.formomerpeled.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.Utils.SharedPreferencesUtil;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.screens.ViewDetails;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantViewHolder> {

    AuthenticationService authenticationService = AuthenticationService.getInstance();
    DatabaseService databaseService = DatabaseService.getInstance();
    private List<Restaurant> restaurantList;
    private   Context context;


    // Initialize with the list of restaurants


    public RestaurantsAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        final int pos = position;
        Restaurant restaurant = restaurantList.get(position);
        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantCuisine.setText(restaurant.getCuisineType());
        holder.restaurantAddress.setText(restaurant.getAddress() + " " + restaurant.getCity());
        holder.restaurantPhoneNumber.setText(restaurant.getPhoneNumber());
        holder.restaurantDomain.setText(restaurant.getDomain());

        holder.glutenFreeItems.setText(restaurant.getGlutenFreeMenuItems());
        holder.viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(context, ViewDetails.class);
                go.putExtra("res", restaurant);
                context.startActivity(go);
            }
        });

        String current_uid =authenticationService.getCurrentUserId();
        databaseService.getUser(current_uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                if(!(user.isAdmin() || user.getId().equals(restaurant.getuId())))
                    holder.deleteButton.setVisibility(View.GONE);

            }

            @Override
            public void onFailed(Exception e) {
                Log.e("error", e.getMessage());
            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_uid =authenticationService.getCurrentUserId();
                User user = SharedPreferencesUtil.getUser(context);
                assert user != null;
                if(user.isAdmin() || user.getId().equals(restaurant.getuId())) {
                    databaseService.deleteRestaurant(restaurant.getId(), new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {
                            restaurantList.remove(pos);
                            notifyItemRemoved(pos);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    });
                }

            }
        });


        // If image exists, convert Base64 to Bitmap and set to ImageView
        if (restaurant.getImageCode() != null) {
            Bitmap bitmap = ImageUtil.convertFrom64base(restaurant.getImageCode());
            holder.ivD.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return this.restaurantList.size();
    }


    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        private TextView restaurantName;
        private TextView restaurantCuisine;
        private TextView restaurantAddress;
        private TextView restaurantPhoneNumber;
        private TextView restaurantDomain;
        private TextView glutenFreeItems;
        private Button viewDetailsButton;
        private ImageButton deleteButton; // Delete button
        private ImageView ivD;
        RatingBar restaurantRatingBar;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            // Initialize all components
            restaurantName = itemView.findViewById(R.id.txtRestaurantName);
            restaurantCuisine = itemView.findViewById(R.id.txtRestaurantCuisine);
            restaurantAddress = itemView.findViewById(R.id.txtRestaurantAddress);
            restaurantPhoneNumber = itemView.findViewById(R.id.txtRestaurantPhoneNumber);
            restaurantDomain = itemView.findViewById(R.id.txtRestaurantDomain);
            glutenFreeItems = itemView.findViewById(R.id.txtGlutenFreeItems);
            viewDetailsButton = itemView.findViewById(R.id.btnViewDetails);
            deleteButton = itemView.findViewById(R.id.btnDelete);// Initialize delete button

            ivD = itemView.findViewById(R.id.ivRes);
        }
    }
}

