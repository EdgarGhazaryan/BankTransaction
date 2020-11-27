package com.bank.BankTransaction.service;

import com.bank.BankTransaction.model.Transaction;
import com.bank.BankTransaction.repository.TransactionRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
    public final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Pair<String, Boolean> validate(Transaction transaction) {
        Pair<String, Boolean> pair;
        if(transaction.getSum() <= 0) {
            pair = Pair.of("Invalid sum. Please mention sum greater than 0", false);
        } else if(!(Objects.equals(transaction.getTransactionType(), "DEPOSIT") || transaction.getTransactionType().equals("WITHDRAWAL"))){
            pair = Pair.of("Invalid transaction type. Please mention correct transaction type` DEPOSIT or WITHDRAWAL", false);
        } else {
            pair = Pair.of("", true);
        }
        return pair;
    }

    public void update(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction getById(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getByUserId(int id) {
        return transactionRepository.getByUserId(id);
    }

    public List<Transaction> getByDate(LocalDate date) {
        return transactionRepository.getByDate(date);
    }

    public List<Transaction> getByStatus(String status) {
        return transactionRepository.getByStatus(status);
    }
}
