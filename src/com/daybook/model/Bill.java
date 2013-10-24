package com.daybook.model;

public class Bill {
    private int billNumber;
    private int billAmount;

    public Bill(int billNumber, int billAmount) {
        this.billNumber = billNumber;
        this.billAmount = billAmount;
    }

    public int getBillAmount() {
        return billAmount;
    }

    public int getBillNumber() {
        return billNumber;
    }
}
