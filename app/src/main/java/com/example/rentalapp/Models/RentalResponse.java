package com.example.rentalapp.Models;

public class RentalResponse {

    private String message;

    private String orderId;

    private String snapToken;

    private String paymentUrl;

    // Getters
    public String getMessage() {
        return message;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSnapToken() {
        return snapToken;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }
}
