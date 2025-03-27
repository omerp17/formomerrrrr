package com.example.formomerpeled.adapter;

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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantViewHolder> {

    private DatabaseService databaseService = DatabaseService.getInstance();
    private AuthenticationService authenticationService = AuthenticationService.getInstance();
    private List<Restaurant> restaurantList;
    private OnItemClickListener clickListener;

    // Initialize with the list of restaurants
    public RestaurantsAdapter(List<Restaurant> restaurantList, OnItemClickListener listener) {
        this.restaurantList = restaurantList;
        this.clickListener = listener;
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
            deleteButton = itemView.findViewById(R.id.btnDelete); // Initialize delete button
            ivD = itemView.findViewById(R.id.ivRes);
            restaurantRatingBar = itemView.findViewById(R.id.restaurantRatingBar);
        }

        public void bind(Restaurant restaurant) {
            // Set data for each TextView
            restaurantName.setText(restaurant.getName());
            restaurantCuisine.setText(restaurant.getCuisineType());
            restaurantAddress.setText(restaurant.getAddress() + " " + restaurant.getCity());
            restaurantPhoneNumber.setText(restaurant.getPhoneNumber());
            restaurantDomain.setText(restaurant.getDomain());
            restaurantRatingBar.setRating(restaurant.getRating());
            glutenFreeItems.setText(restaurant.getGlutenFreeMenuItems());

            // If image exists, convert Base64 to Bitmap and set to ImageView
            if (restaurant.getImageCode() != null) {
                Bitmap bitmap = ImageUtil.convertFrom64base(restaurant.getImageCode());
                ivD.setImageBitmap(bitmap);
            }

            if (clickListener != null) {
                viewDetailsButton.setOnClickListener(v -> clickListener.onItemClick(restaurant));
            }

            // Check if the current user is an admin to show the delete button
            databaseService.getUser(authenticationService.getCurrentUserId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if(user != null &&user.isAdmin()) {
                        deleteButton.setOnClickListener(v -> {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                databaseService.deleteRestaurant(restaurantList.get(position).getId(), new DatabaseService.DatabaseCallback<Void>() {
                                    @Override
                                    public void onCompleted(Void object) {
                                        restaurantList.remove(position); // Remove from local list
                                    }

                                    @Override
                                    public void onFailed(Exception e) {
                                        Log.e("RestaurantsAdapter", e.getMessage());
                                    }
                                });
                                notifyItemRemoved(position); // Update the RecyclerView
                            }
                        });
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("RestaurantsAdapter", e.getMessage());
                }
            });

            // Delete button for the owner of the restaurant
            if (authenticationService.getCurrentUserId() != null &&
                    authenticationService.getCurrentUserId().equals(restaurant.getuId())) {
                deleteButton.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        databaseService.deleteRestaurant(restaurantList.get(position).getId(), new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {
                                restaurantList.remove(position); // Remove from local list
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Log.e("RestaurantsAdapter", e.getMessage());
                            }
                        });
                        notifyItemRemoved(position); // Update the RecyclerView
                    }
                });
            }
        }
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Assuming XML layout is named item_restaurant.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    // Update the list of restaurants
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    // Interface to listen for item click events
    public interface OnItemClickListener {
        void onItemClick(Restaurant restaurant);
        void onLongClick(Restaurant restaurant);
    }
}

