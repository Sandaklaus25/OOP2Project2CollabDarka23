package com.example.storageinventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CASH_REGISTER")
public class CashRegister {

    @Id
    @Column(name = "id")
    private Long id = 1L; // Винаги ще е 1, защото имаме само една каса

    @Column(name = "balance", nullable = false)
    private Double balance;

    public CashRegister() {
    }

    public CashRegister(Double balance) {
        this.id = 1L;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}