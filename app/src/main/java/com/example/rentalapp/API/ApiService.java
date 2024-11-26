package com.example.rentalapp.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import com.example.rentalapp.Models.Item;
import com.example.rentalapp.Models.RentalResponse;
import com.example.rentalapp.Models.ServerResponse;
import com.example.rentalapp.Models.LoginRequest;
import com.example.rentalapp.Models.SignUpRequest;

import java.util.List;
import java.util.Map;

public interface ApiService {

    @POST("auth/signup")
    Call<ServerResponse> signup(@Body SignUpRequest signUpRequest);

    @POST("auth/login")
    Call<ServerResponse> login(@Body LoginRequest loginRequest);

    @GET("items")
    Call<List<Item>> getItems(@Header("Authorization") String token);

    @GET("items/{id}")
    Call<Item> getItemDetails(@Header("Authorization") String token, @Path("id") String itemId);

    // Initiate rent
    @POST("rent/item/{id}")
    Call<RentalResponse> initiateRent(
            @Header("Authorization") String token,
            @Path("id") int itemId,
            @Body Map<String, String> rentRequest // Pass startDate and endDate in request body
    );


}
