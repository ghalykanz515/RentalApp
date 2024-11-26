package com.example.rentalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentalapp.API.ApiClient;
import com.example.rentalapp.API.ApiService;
import com.example.rentalapp.Models.LoginRequest;
import com.example.rentalapp.Models.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private TextView linkSignup;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private Button loginGuestButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linkSignup = findViewById(R.id.signup_link);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        rememberMeCheckBox = findViewById(R.id.remember_me);
        loginButton = findViewById(R.id.login_button);
        loginGuestButton = findViewById(R.id.login_guest_button);

        linkSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                login(username, password);
            }
        });

        loginGuestButton.setOnClickListener(v -> {
            String username = "ghaly@gmail.com";
            String password = "ghaly123";

            login(username, password);
        });
    }

    private void login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.login(loginRequest).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServerResponse serverResponse = response.body();

                    String token = serverResponse.getToken();
                    String username = serverResponse.getUser() != null ? serverResponse.getUser().getUsername() : "Unknown";

                    // Log token and username for debugging
                    Log.d("LoginDebug", "Token: " + token);
                    Log.d("LoginDebug", "Username: " + username);

                    // Save token and username in SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.putString("username", username);
                    editor.apply();

                    // Navigate to MainActivity (or wherever you want to show items)
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void login(String username, String password) {
//        username = usernameInput.getText().toString().trim();
//        password = passwordInput.getText().toString().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            // Show an error message if fields are empty
//            Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Show progress dialog
////        progressDialog = new ProgressDialog(LoginActivity.this);
////        progressDialog.setMessage("Logging in...");
////        progressDialog.show();
//
//        // Create LoginRequest object
//        LoginRequest loginRequest = new LoginRequest(username, password);
//
//        // Get the API client and service
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//        // Make the API call to login
//        apiService.login(loginRequest).enqueue(new Callback<ServerResponse>() {
//            @Override
//            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
//                //progressDialog.dismiss();
//
//                if (response.isSuccessful() && response.body() != null) {
//                    ServerResponse serverResponse = response.body();
//
//                    // Access the token
//                    String token = serverResponse.getToken();
//
//                    // Access the username from the nested User object
//                    String username = serverResponse.getUser() != null ? serverResponse.getUser().getUsername() : "Unknown";
//
//                    // Log the token and username for debugging
//                    Log.d("LoginDebug", "Token: " + token);
//                    Log.d("LoginDebug", "Username: " + username);
//
//                    // Display the username in a Toast
//                    Toast.makeText(LoginActivity.this, "Welcome, " + username, Toast.LENGTH_SHORT).show();
//
//                    // Save token and username in SharedPreferences
//                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("token", token);
//                    editor.putString("username", username);
//                    editor.apply();
//
//                    // Navigate to MainActivity
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//
//                } else {
//                    // Handle failure in response
//                    //Toast.makeText(LoginActivity.this, "Login failed. Try again.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                progressDialog.dismiss();
//                // Handle failure in making the API call
//                Toast.makeText(LoginActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
