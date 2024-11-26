package com.example.rentalapp.Models;

import com.google.gson.annotations.SerializedName;

public class RentalResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("snapToken")
    private String snapToken;

    @SerializedName("paymentUrl")
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
