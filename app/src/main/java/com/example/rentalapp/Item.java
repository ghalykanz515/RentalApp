package com.example.rentalapp;

public class Item {
    private String name;
    private String category;
    private String description;
    private double price;
    private int minimumRent;
    private String status;
    private int lenderId;

    public Item(String name, String category, String description, double price, int minimumRent, String status, int lenderId) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.minimumRent = minimumRent;
        this.status = status;
        this.lenderId =lenderId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getMinimumRent() {
        return minimumRent;
    }

    public String getStatus() {
        return status;
    }

    public int getLenderId() {
        return  lenderId;
    }
}

