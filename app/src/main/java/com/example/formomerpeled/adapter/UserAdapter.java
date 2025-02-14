package com.example.formomerpeled.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.screens.ShowUsers;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view); // מתקן את השגיאה כאן
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.etFname.setText(user.getFname());
        holder.etLname.setText(user.getLname());
        holder.etPhone.setText(user.getPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowUsers.class); // ודא ש-UsersActivity קיימת ומיובאת
            intent.putExtra("user_id", user.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView etFname;
        TextView etLname;
        TextView etPhone;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            etFname = itemView.findViewById(R.id.etFname);
            etLname = itemView.findViewById(R.id.etLname);
            etPhone = itemView.findViewById(R.id.etPhone);
        }
    }
}
