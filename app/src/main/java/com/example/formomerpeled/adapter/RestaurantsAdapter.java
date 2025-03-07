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

    // אתחול עם רשימת מסעדות
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
        private ImageButton deleteButton; // כפתור מחיקה
        private ImageView ivD;
        RatingBar restaurantRatingBar;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            // אתחול של כל רכיב
            restaurantName = itemView.findViewById(R.id.txtRestaurantName);
            restaurantCuisine = itemView.findViewById(R.id.txtRestaurantCuisine);
            restaurantAddress = itemView.findViewById(R.id.txtRestaurantAddress);
            restaurantPhoneNumber = itemView.findViewById(R.id.txtRestaurantPhoneNumber);
            restaurantDomain = itemView.findViewById(R.id.txtRestaurantDomain);
            glutenFreeItems = itemView.findViewById(R.id.txtGlutenFreeItems);
            viewDetailsButton = itemView.findViewById(R.id.btnViewDetails);
            deleteButton = itemView.findViewById(R.id.btnDelete); // אתחול כפתור מחיקה
            ivD = itemView.findViewById(R.id.ivRes);
            restaurantRatingBar = itemView.findViewById(R.id.restaurantRatingBar);
        }

        public void bind(Restaurant restaurant) {
            // קביעת המידע לכל אחד מה-TextViewים
            restaurantName.setText(restaurant.getName());
            restaurantCuisine.setText(restaurant.getCuisineType());
            restaurantAddress.setText(restaurant.getAddress() + " " + restaurant.getCity());
            restaurantPhoneNumber.setText(restaurant.getPhoneNumber());
            restaurantDomain.setText(restaurant.getDomain());
            restaurantRatingBar.setRating(restaurant.getRating());
            glutenFreeItems.setText(restaurant.getGlutenFreeMenuItems());

            // אם יש תמונה, המר את ה-Base64 ל-Bitmap ושים ב-ImageView
            if (restaurant.getImageCode() != null) {
                Bitmap bitmap = ImageUtil.convertFrom64base(restaurant.getImageCode());
                ivD.setImageBitmap(bitmap);
            }

            if (clickListener != null) {
                viewDetailsButton.setOnClickListener(v -> clickListener.onItemClick(restaurant));
            }

            databaseService.getUser(authenticationService.getCurrentUserId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if(user.isAdmin()) {
                        deleteButton.setOnClickListener(v -> {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                databaseService.deleteRestaurant(restaurantList.get(position).getId(), new DatabaseService.DatabaseCallback<Void>() {
                                    @Override
                                    public void onCompleted(Void object) {
                                        restaurantList.remove(position); // הסרה מהרשימה המקומית
                                    }

                                    @Override
                                    public void onFailed(Exception e) {
                                        Log.e("RestaurantsAdapter", e.getMessage());
                                    }
                                });
                                notifyItemRemoved(position); // עדכון ה-RecyclerView
                            }
                        });
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("RetaurantsAdapter", e.getMessage());
                }
            });

            // כפתור מחיקה
            if(authenticationService.getCurrentUserId().equals(restaurant.getuId())) {
                deleteButton.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        databaseService.deleteRestaurant(restaurantList.get(position).getId(), new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {
                                restaurantList.remove(position); // הסרה מהרשימה המקומית
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Log.e("RestaurantsAdapter", e.getMessage());
                            }
                        });
                        notifyItemRemoved(position); // עדכון ה-RecyclerView
                    }
                });
            }
        }
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // מניחים שמדובר ב-XML בשם item_restaurant.xml
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

    // עדכון רשימת המסעדות
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    // ממשק להאזנה לאירועים על פריטים ברשימה
    public interface OnItemClickListener {
        void onItemClick(Restaurant restaurant);
        void onLongClick(Restaurant restaurant);
    }
}
