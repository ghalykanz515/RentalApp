package com.example.rentalapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalapp.Models.RentHistory;
import com.example.rentalapp.R;

import java.util.ArrayList;
import java.util.List;

public class RentHistoryAdapter extends RecyclerView.Adapter<RentHistoryAdapter.ViewHolder> {

    private List<RentHistory> rentHistoryList = new ArrayList<>();

    public void setRentHistoryList(List<RentHistory> rentHistoryList) {
        this.rentHistoryList = rentHistoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rent_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentHistory rentHistory = rentHistoryList.get(position);
        holder.itemName.setText(rentHistory.getItemName());
        holder.itemPrice.setText("Price: " + rentHistory.getItemPrice());
        holder.startDate.setText("Start Date: " + rentHistory.getStartDate());
        holder.endDate.setText("End Date: " + rentHistory.getEndDate());
        holder.rentalStatus.setText("Status: " + rentHistory.getRentalStatus());
    }

    @Override
    public int getItemCount() {
        return rentHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, startDate, endDate, rentalStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            startDate = itemView.findViewById(R.id.start_date);
            endDate = itemView.findViewById(R.id.end_date);
            rentalStatus = itemView.findViewById(R.id.status);
        }
    }
}



