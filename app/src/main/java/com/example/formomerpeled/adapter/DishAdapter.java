package com.example.formomerpeled.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.Dish;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.screens.AddDish;
import com.example.formomerpeled.screens.ReviewDish;
import com.example.formomerpeled.screens.ShowDishes;
import com.example.formomerpeled.screens.ShowUsers;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private Context context;
    private List<Dish> dishList;

    public DishAdapter(Context context, List<Dish> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishAdapter.DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dish, parent, false);
        return new DishAdapter.DishViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return this.dishList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.txtItemName.setText(dish.getName());

        holder.txtItemPrice.setText(dish.getPrice() + "");
        holder.txtItemDetails.setText(dish.getDetails());
        holder.ratingBarAdapter.setRating((float) dish.getRate());
        holder.btnAddReviewDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go= new Intent(context, ReviewDish.class);
                go.putExtra("dish",dish);
                go.putExtra("resId",dish.getResId());

                context.startActivity(go);

            }
        });


    }






    public void updateList(List<Dish> filteredList) {
        this.dishList.clear();
        this.dishList.addAll(filteredList);
        this.notifyDataSetChanged();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItemName, txtItemRestaurant, txtItemPrice, txtItemDetails;

        Button btnAddReviewDish, btnDeleteDish;


         RatingBar ratingBarAdapter;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtItemName);
        //    txtItemRestaurant = itemView.findViewById(R.id.txtItemRestaurant);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            txtItemDetails = itemView.findViewById(R.id.txtItemDetails);
            btnAddReviewDish=itemView.findViewById(R.id.btnAddDishReview);
            ratingBarAdapter=itemView.findViewById(R.id.ratingBarAdapter);
            //deleteButton2 = itemView.findViewById(R.id.btnDeleteDish);// Initialize delete button


// כפתור מחיקה
            holder.btnDeleteDish.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("מחיקת משתמש")
                        .setMessage("האם אתה בטוח שברצונך למחוק את המשתמש?")
                        .setPositiveButton("כן", (dialog, which) -> deleteDish(Dish.getId(), position))
                        .setNegativeButton("לא", (dialog, which) -> dialog.dismiss())
                        .show();
            });

        }
    }
}
