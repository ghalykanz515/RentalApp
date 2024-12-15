package com.example.rentalapp.ui.history;

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

import com.example.rentalapp.API.ApiClient;
import com.example.rentalapp.API.ApiService;
import com.example.rentalapp.Adapters.RentHistoryAdapter;
import com.example.rentalapp.Models.RentHistory;
import com.example.rentalapp.Models.RentHistoryResponse;
import com.example.rentalapp.databinding.FragmentRentHistoryBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentHistoryFragment extends Fragment {

    private FragmentRentHistoryBinding binding;
    private RentHistoryAdapter rentHistoryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Setup RecyclerView
        RecyclerView rentHistoryRecyclerView = binding.rentHistoryRecyclerView;
        rentHistoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rentHistoryAdapter = new RentHistoryAdapter();
        rentHistoryRecyclerView.setAdapter(rentHistoryAdapter);

        // Fetch token from SharedPreferences
        String token = requireActivity()
                .getSharedPreferences("user_data", getContext().MODE_PRIVATE)
                .getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "No token found. Please login again.", Toast.LENGTH_SHORT).show();
            return root;
        }

        // Make API call
        String currentToken = "Bearer " + token;
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getRentHistory(currentToken).enqueue(new Callback<RentHistoryResponse>() {
            @Override
            public void onResponse(Call<RentHistoryResponse> call, Response<RentHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RentHistoryResponse rentHistoryResponse = response.body();

                    // Access the transaction history array
                    List<RentHistory> rentHistoryList = rentHistoryResponse.getTransactionHistory();

                    // Update the adapter with the new data
                    rentHistoryAdapter.setRentHistoryList(rentHistoryList);
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RentHistoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Request failed: " + t.getMessage());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
