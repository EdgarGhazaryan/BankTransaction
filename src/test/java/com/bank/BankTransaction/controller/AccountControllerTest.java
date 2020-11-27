package com.bank.BankTransaction.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountControllerTest {
    //In my DB user 1 is ADMIN, user 2 is USER
    //set this ids
    private static final int adminId = 1;
    private static final int userId= 2;

    @Autowired
    private AccountController accountController;

    @Test
    void createAccount() {
        assertEquals(accountController.createAccount(userId, adminId), "Your role isn't ADMIN");
        assertEquals(accountController.createAccount(-1, userId), "Invalid admin id");
    }
}