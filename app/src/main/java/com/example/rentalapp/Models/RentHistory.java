package com.example.rentalapp.Models;

public class RentHistory {
    private int transactionId;
    private int itemId;
    private String itemName;
    private String itemPrice;
    private String rentalStatus;
    private String transactionStatus;
    private String startDate;
    private String endDate;

    // Getters for each field
    public int getTransactionId() { return transactionId; }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getItemPrice() { return itemPrice; }
    public String getRentalStatus() { return rentalStatus; }
    public String getTransactionStatus() { return transactionStatus; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
}

