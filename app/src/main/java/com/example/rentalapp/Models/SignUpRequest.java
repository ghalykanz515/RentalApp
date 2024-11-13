package com.example.rentalapp.Models;

public class SignUpRequest {

    private String username;
    private String password;

    public SignUpRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}