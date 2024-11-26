package com.example.rentalapp.Models;

public class RentRequest {
    private int rentLength;

    public RentRequest(int rentLength) {
        this.rentLength = rentLength;
    }

    public int getRentLength() {
        return rentLength;
    }

    public void setRentLength(int rentLength) {
        this.rentLength = rentLength;
    }
}
