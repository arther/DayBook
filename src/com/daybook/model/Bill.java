package com.daybook.model;

public class Bill {
    private int number;
    private int amount;

    public Bill(int billNumber, int billAmount) {
        this.number = billNumber;
        this.amount = billAmount;
    }

    public int getAmount() {
        return amount;
    }

    public int getNumber() {
        return number;
    }
}
