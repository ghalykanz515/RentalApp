package com.example.rentalapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentalapp.API.ApiClient;
import com.example.rentalapp.API.ApiService;
import com.example.rentalapp.Models.Item;
import com.example.rentalapp.Models.RentLengthRequest;
import com.example.rentalapp.Models.RentalResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView itemName, itemDescription, itemPrice;
    private Button rentButton;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        itemName = findViewById(R.id.productTitle);
        itemDescription = findViewById(R.id.productDescription);
        itemPrice = findViewById(R.id.productPrice);
        rentButton = findViewById(R.id.rentButton);

        // Get item ID from Intent
        itemId = getIntent().getStringExtra("ITEM_ID");
        Log.d("ProductDetailActivity", "ITEM_ID received: " + itemId);

        if (itemId != null) {
            fetchItemDetails(itemId);
        } else {
            Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
            return;
        }

        rentButton.setOnClickListener(v -> initiateRent(itemId));
    }

    private void fetchItemDetails(String itemId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        String token = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                .getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "No token found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Item> call = apiService.getItemDetails("Bearer " + token, itemId);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Item item = response.body();
                    itemName.setText(item.getName());
                    itemDescription.setText(item.getDescription());
                    itemPrice.setText(String.valueOf(item.getPrice()));
                } else {
                    Log.e("API_ERROR", "Response failed: " + response.message());
                    Toast.makeText(ProductDetailActivity.this, "Failed to load item details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initiateRent(String itemId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        String token = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                .getString("token", "");
        Log.d("TOKEN_DEBUG", "Token: " + token);

        if (token.isEmpty()) {
            Toast.makeText(this, "No token found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int parsedItemId = Integer.parseInt(itemId);

            // Now passing only rent length in the request body
            RentLengthRequest rentLengthRequest = new RentLengthRequest(7); // Example: renting for 7 days

            Log.d("RENT_REQUEST", "Item ID: " + itemId);
            Log.d("RENT_REQUEST", "Rent Length: " + rentLengthRequest.getRentLength());

            Call<RentalResponse> call = apiService.initiateRent("Bearer " + token, parsedItemId, rentLengthRequest);

            call.enqueue(new Callback<RentalResponse>() {
                @Override
                public void onResponse(Call<RentalResponse> call, Response<RentalResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RentalResponse rentalResponse = response.body();
                        String paymentUrl = rentalResponse.getPaymentUrl();
                        openPaymentWebView(paymentUrl);
                    } else {
                        Log.e("API_ERROR", "Response failed: " + response.code() + " - " + response.message());
                        try {
                            if (response.errorBody() != null) {
                                Log.e("API_ERROR_BODY", response.errorBody().string());
                            }
                        } catch (Exception e) {
                            Log.e("API_ERROR_BODY", "Error reading error body", e);
                        }

                        Toast.makeText(ProductDetailActivity.this, "Failed to initiate rent: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RentalResponse> call, Throwable t) {
                    Log.e("API_ERROR", "Request failed: " + t.getMessage());
                    Toast.makeText(ProductDetailActivity.this, "Request failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Log.e("ID_PARSING_ERROR", "Invalid item ID: " + itemId);
            Toast.makeText(this, "Invalid item ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPaymentWebView(String paymentUrl) {
        WebViewActivity.start(this, paymentUrl);
    }
}