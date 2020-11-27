package com.bank.BankTransaction.controller;

import com.bank.BankTransaction.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionControllerTest {
    //In my DB user 1 is ADMIN, user 2 is USER and has account 1
    //set this ids
    private static final int adminId = 1;
    private static final int userId = 2;
    private static final int accountId = 1;

    @Autowired
    private TransactionController transactionController;

    @Test
    void createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionType("DEPOSIT");
        transaction.setSum(1000);
        assertEquals(transactionController.createTransaction(userId, accountId, transaction),
                "Transaction for account " + 1 + " is created");
        transaction.setSum(-100);
        assertEquals(transactionController.createTransaction(userId, accountId, transaction),
                "Invalid sum. Please mention sum greater than 0");
    }

    @Test
    void getTransactions() {
        List<Transaction> transactions = transactionController.getTransactions(adminId, null, "2020-11-27", "PENDING");
        assertEquals(transactions.get(0).getStatus(), "PENDING");
    }
}