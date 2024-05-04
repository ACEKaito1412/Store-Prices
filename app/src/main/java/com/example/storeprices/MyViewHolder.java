package com.example.storeprices;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MyViewHolder";

    TextView txtName, txtPrice, txtDate;
    LinearLayout layout;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.itemName);
        txtPrice = itemView.findViewById(R.id.itemPrice);
        txtDate = itemView.findViewById(R.id.itemDate);
        layout = itemView.findViewById(R.id.itemLinearLayout);
    }
    

}
