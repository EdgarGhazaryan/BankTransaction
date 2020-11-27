package com.bank.BankTransaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User owner;
    private double balance;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(int id, User owner, double balance, List<Transaction> transactions) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.transactions = transactions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
