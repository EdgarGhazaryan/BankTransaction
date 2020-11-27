package com.bank.BankTransaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int id;
    private LocalDate creation_date;
    private String status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private String transactionType;
    private double sum;

    public Transaction() {
    }

    public Transaction(int id, LocalDate creation_date, String status, Account account, String transactionType, double sum) {
        this.id = id;
        this.creation_date = creation_date;
        this.status = status;
        this.account = account;
        this.transactionType = transactionType;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    //Overriding equals() for tests
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id &&
                Double.compare(that.sum, sum) == 0 &&
                Objects.equals(creation_date, that.creation_date) &&
                Objects.equals(status, that.status) &&
                Objects.equals(account.getId(), that.account.getId()) &&
                Objects.equals(transactionType, that.transactionType);
    }
}
