package com.example.formomerpeled.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.screens.ShowUsers;
import com.example.formomerpeled.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList; // יצירת עותק של הרשימה
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.etFname.setText(user.getFname());
        holder.etLname.setText(user.getLname());
        holder.etPhone.setText(user.getPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowUsers.class);
            intent.putExtra("user_id", user.getId());
            context.startActivity(intent);
        });

        // כפתור מחיקה
        holder.btnDeleteUser.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("מחיקת משתמש")
                    .setMessage("האם אתה בטוח שברצונך למחוק את המשתמש?")
                    .setPositiveButton("כן", (dialog, which) -> deleteUser(user.getId(), position))
                    .setNegativeButton("לא", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void deleteUser(String userId, int position) {
        DatabaseService.getInstance().deleteUser(userId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size());
                Toast.makeText(context, "המשתמש נמחק בהצלחה", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(context, "שגיאה במחיקת המשתמש", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateList(List<User> newList) {
        userList.clear();
        userList.addAll(newList);
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView etFname;
        TextView etLname;
        TextView etPhone;
        ImageButton btnDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            etFname = itemView.findViewById(R.id.etFname);
            etLname = itemView.findViewById(R.id.etLname);
            etPhone = itemView.findViewById(R.id.etPhone);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
