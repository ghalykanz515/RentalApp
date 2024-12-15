package com.example.rentalapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalapp.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<Integer> images;
    private List<String> titles;

    public BannerAdapter(List<Integer> images, List<String> titles) {
        this.images = images;
        this.titles = titles;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.bind(images.get(position), titles.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bannerImageView);
            textView = itemView.findViewById(R.id.bannerText);
        }

        public void bind(int imageResId, String title) {
            imageView.setImageResource(imageResId);
            textView.setText(title);
        }
    }
}
