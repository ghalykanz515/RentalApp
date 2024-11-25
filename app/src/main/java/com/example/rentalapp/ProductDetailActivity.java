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
        ApiService apiServices = ApiClient.getClient().create(ApiService.class);

        Call<Item> call = apiServices.getItemDetails(itemId);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Item item = response.body();
                    itemName.setText(item.getName());
                    itemDescription.setText(item.getDescription());
                    itemPrice.setText(String.valueOf(item.getPrice()));
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to load item details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initiateRent(String itemId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Get user token from SharedPreferences
        Context context = getApplicationContext();
        String token = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                .getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "No token found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ResponseBody> call = apiService.initiateRent("Bearer " + token, Integer.parseInt(itemId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String paymentUrl = jsonObject.getString("paymentUrl");

                        // Open WebView with payment URL
                        openPaymentWebView(paymentUrl);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ProductDetailActivity.this, "Failed to parse response.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to initiate rent.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR", "Request failed: " + t.getMessage());
                Toast.makeText(ProductDetailActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openPaymentWebView(String paymentUrl) {
        WebViewActivity.start(this, paymentUrl);
    }
}