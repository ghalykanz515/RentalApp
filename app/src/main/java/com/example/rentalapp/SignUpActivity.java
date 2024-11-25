package com.example.rentalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentalapp.API.ApiClient;
import com.example.rentalapp.API.ApiService;
import com.example.rentalapp.Models.ServerResponse;
import com.example.rentalapp.Models.SignUpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputUsername, inputPassword, inputConfirmPassword;
    private TextView linkLogin;
    private CheckBox inputPolicyCheckBox;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        linkLogin = findViewById(R.id.login_link);
        linkLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        inputUsername = findViewById(R.id.username_input);
        inputPassword = findViewById(R.id.password_input);
        inputConfirmPassword = findViewById(R.id.confirm_password_input);
        inputPolicyCheckBox = findViewById(R.id.agree_policy_checkbox);
        signUpButton = findViewById(R.id.signup_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                String confirmPassword = inputConfirmPassword.getText().toString();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!inputPolicyCheckBox.isChecked()) {
                    Toast.makeText(SignUpActivity.this, "Please agree to the policy", Toast.LENGTH_SHORT).show();
                    return;
                }

                SignUpRequest signUpRequest = new SignUpRequest(username, password);
                signUpUser(signUpRequest);
            }
        });

//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = inputName.getText().toString();
//                String password = inputPassword.getText().toString();
//                String confirmPassword = inputConfirmPassword.getText().toString();
//
//                if (!password.equals(confirmPassword)) {
//                    Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!inputPolicyCheckBox.isChecked()) {
//                    Toast.makeText(SignUpActivity.this, "Please agree to the policy", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void signUpUser(SignUpRequest signUpRequest) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ServerResponse> call = apiService.signup(signUpRequest);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                    // Handle token or move to login screen
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Signup Failed: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}