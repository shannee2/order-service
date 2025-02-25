package com.order.model.money;

import jakarta.persistence.*;


@Embeddable
public class Money {
    @Column(name = "grand_total_amount") // Explicit mapping
    private double amount;

    @Column(name = "grand_total_currency") // Match Liquibase column
    private String currency;

    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money() {
    }

    public Money(double unitPrice) {
        this.amount = unitPrice;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}