package com.example.rentalapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rentalapp.API.ApiClient;
import com.example.rentalapp.API.ApiService;
import com.example.rentalapp.BannerAdapter;
import com.example.rentalapp.Models.Item;
import com.example.rentalapp.ItemAdapter;
import com.example.rentalapp.R;
import com.example.rentalapp.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private ViewPager2 bannerViewPager;
    private CircleIndicator3 bannerIndicator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        bannerViewPager = binding.bannerViewPager;
        bannerIndicator = binding.bannerIndicator;

        // Set up the banner
        setupBanner();


        // Initialize item list
        itemList = new ArrayList<>();

        // Get the token from SharedPreferences
        String token = getActivity().getSharedPreferences("user_data", getContext().MODE_PRIVATE)
                .getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "No token found. Please login again.", Toast.LENGTH_SHORT).show();
            return root;
        }

        // Make the API call to get items with the cards
        String currentToken = "Bearer " + token;
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Item>> call = apiService.getItems(currentToken);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemList = response.body();
                    itemAdapter = new ItemAdapter(itemList, getContext());
                    recyclerView.setAdapter(itemAdapter);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Error body: " + errorBody);
                        Toast.makeText(getContext(), "Failed to load items: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to load items", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("API_ERROR", "Request failed: " + t.getMessage());
                Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void setupBanner() {
        // Sample data for banner images and titles
        List<Integer> bannerImages = Arrays.asList(
                R.drawable.image_5, // Replace with actual drawable resources
                R.drawable.image_5
        );

        List<String> bannerTitles = Arrays.asList(
                "Welcome To Our Library",
                "Explore Thousands of Books"
        );

        // Set up the BannerAdapter
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages, bannerTitles);
        bannerViewPager.setAdapter(bannerAdapter);

        // Connect CircleIndicator with ViewPager2
        bannerIndicator.setViewPager(bannerViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
