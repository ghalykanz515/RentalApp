package com.example.rentalapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bookName.setText(item.getName());
        holder.categoryBadge.setText(item.getCategory());
        holder.bookPrice.setText(String.format("%.2f", item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView bookName;
        TextView categoryBadge;
        TextView bookPrice;
//        ImageView bookImage;
//        TextView bookGenre;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookTitle);
            categoryBadge = itemView.findViewById(R.id.categoryBadge);
            bookPrice = itemView.findViewById(R.id.bookPrice);

//            categoryBadge = itemView.findViewById(R.id.categoryBadge);
//            bookImage = itemView.findViewById(R.id.bookImage);
//            bookTitle = itemView.findViewById(R.id.bookTitle);
//            bookGenre = itemView.findViewById(R.id.bookGenre);
        }
    }
}
