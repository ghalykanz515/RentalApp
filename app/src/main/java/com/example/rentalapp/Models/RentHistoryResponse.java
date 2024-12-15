package com.example.rentalapp.Models;

import java.util.List;

public class RentHistoryResponse {
    private String message;
    private List<RentHistory> transactionHistory;

    public String getMessage() {
        return message;
    }

    public List<RentHistory> getTransactionHistory() {
        return transactionHistory;
    }
}
