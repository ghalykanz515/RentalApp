package com.example.rentalapp.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import com.example.rentalapp.Item;
import com.example.rentalapp.Models.ServerResponse;
import com.example.rentalapp.Models.LoginRequest;
import com.example.rentalapp.Models.SignUpRequest;

import java.util.List;

public interface ApiService {

    @POST("auth/signup")
    Call<ServerResponse> signup(@Body SignUpRequest signUpRequest);

    @POST("auth/login")
    Call<ServerResponse> login(@Body LoginRequest loginRequest);

    @GET("items")
    Call<List<Item>> getItems(@Header("Authorization") String token);

    @GET("items/{id}")
    Call<Item> getItemDetails(@Path("id") String id);

    @POST("rent/item/{id}")
    Call<ResponseBody> initiateRent(
            @Header("Authorization") String token,
            @Path("id") int itemId
    );

}
