package com.example.formomerpeled.adapter;



import static androidx.core.content.ContextCompat.getContextForLanguage;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.models.Restaurant;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private OnItemClickListener clickListener;
    // אתחול עם רשימת מסעדות
    public RestaurantsAdapter(List<Restaurant> restaurantList, OnItemClickListener listener) {
        this.restaurantList = restaurantList;
        this.clickListener = listener;
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        private TextView restaurantName;
        private TextView restaurantCuisine;
        private TextView restaurantAddress;
        private TextView restaurantPhoneNumber;
        private TextView restaurantDomain;
        private TextView glutenFreeItems;
        private Button viewDetailsButton;

        private ImageView ivD;

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
            ivD= itemView.findViewById(R.id.ivRes);
        }

        public void bind(Restaurant restaurant) {
            // קביעת המידע לכל אחד מה-TextViewים
            restaurantName.setText(restaurant.getName());
            restaurantCuisine.setText(restaurant.getCuisineType());
            restaurantAddress.setText(restaurant.getAddress() + " " + restaurant.getCity());
            restaurantPhoneNumber.setText(restaurant.getPhoneNumber());

            restaurantDomain.setText(restaurant.getDomain());
            

            restaurantDomain.setMovementMethod(LinkMovementMethod.getInstance());
            glutenFreeItems.setText(restaurant.getGlutenFreeMenuItems());

            // אם יש תמונה, המר את ה-Base64 ל-Bitmap ושים ב-ImageView
            if (restaurant.getImageCode() != null) {
                Bitmap bitmap = ImageUtil.convertFrom64base(restaurant.getImageCode());
                ivD.setImageBitmap(bitmap);
            }


        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Restaurant restaurant);
    }


    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // מניחים שמדובר ב-XML בשם item_restaurant.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);




        view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
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


}