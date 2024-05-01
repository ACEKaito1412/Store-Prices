package com.example.storeprices;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView txtName, txtPrice, txtDate;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.itemName);
        txtPrice = itemView.findViewById(R.id.itemPrice);
        txtDate = itemView.findViewById(R.id.itemDate);
    }
}
