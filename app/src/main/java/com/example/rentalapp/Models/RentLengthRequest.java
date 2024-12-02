package com.example.rentalapp.Models;

public class RentLengthRequest {
    private int rentLength;

    public RentLengthRequest(int rentLength) {
        this.rentLength = rentLength;
    }

    public int getRentLength() {
        return rentLength;
    }

    public void setRentLength(int rentLength) {
        this.rentLength = rentLength;
    }
}
