package com.example.formomerpeled.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.Dish;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.screens.ShowUsers;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private Context context;
    private List<Dish> dishList;

    public DishAdapter(List<Dish> dishList, Context context) {
        this.dishList = dishList;
        this.context = context;
    }

    @NonNull
    @Override
    public DishAdapter.DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dish, parent, false);
        return new DishAdapter.DishViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.txtItemName.setText(dish.getName());
        holder.txtItemRestaurant.setText(dish.getRestaurant().getName());
        holder.txtItemPrice.setText(dish.getPrice() + "");
        holder.txtItemDetails.setText(dish.getDetails());


    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItemName, txtItemRestaurant, txtItemPrice, txtItemDetails;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemRestaurant = itemView.findViewById(R.id.txtItemRestaurant);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            txtItemDetails = itemView.findViewById(R.id.txtItemDetails);


        }
    }
}
