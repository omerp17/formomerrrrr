package com.example.formomerpeled.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.formomerpeled.R;

import java.util.List;

public class RestaurantNameAdapter extends ArrayAdapter<String> {
        Context context;
        List<String> objects;

    public  RestaurantNameAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            this.context=context;
            this.objects=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.nameres, parent, false);

            TextView tvname = view.findViewById(R.id.tvResName);



            String temp = objects.get(position);
            tvname.setText(temp);

            return view;
        }


    }

