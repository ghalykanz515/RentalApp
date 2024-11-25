package com.example.rentalapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentalapp.API.ApiClient;
import com.example.rentalapp.API.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
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

        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateRent(itemId);
            }
        });
    }

    private void fetchItemDetails(String itemId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Retrieve token from SharedPreferences
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

                // To Check if its authorize
                Log.d("API_RESPONSE", "Status Code: " + response.code());
                Log.d("API_RESPONSE", "Raw Response: " + response.raw());

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

        // Get user token from SharedPreferences
        String token = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                .getString("token", "");
        Log.d("TOKEN_DEBUG", "Token: " + token);

        if (token.isEmpty()) {
            Toast.makeText(this, "No token found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int parsedItemId = Integer.parseInt(itemId);
            Call<ResponseBody> call = apiService.initiateRent("Bearer " + token, parsedItemId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("API_RESPONSE", "Status Code: " + response.code());
                    Log.d("API_RESPONSE", "Raw Response: " + response.raw());

                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            String paymentUrl = jsonObject.getString("paymentUrl");

                            // Open WebView for payment
                            openPaymentWebView(paymentUrl);
                        } catch (IOException | JSONException e) {
                            Log.e("PARSING_ERROR", "Failed to parse response: " + e.getMessage());
                            Toast.makeText(ProductDetailActivity.this, "Failed to parse response.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error details";
                            Log.e("API_ERROR", "Response failed: " + errorBody);
                            Toast.makeText(ProductDetailActivity.this, "Failed to initiate rent: " + response.message(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e("API_ERROR", "Failed to read error body: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("API_ERROR", "Request failed: " + t.getMessage());
                    Toast.makeText(ProductDetailActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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