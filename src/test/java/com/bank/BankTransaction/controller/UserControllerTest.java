package com.bank.BankTransaction.controller;

import com.bank.BankTransaction.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Test
    void register() {
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("mail@gmail.com");
        user.setPassword("qwerty");

        assertEquals(userController.register(user), "You are successfully registered");
        user.setEmail("invalidgmail.com");
        assertNotEquals(userController.register(user), "You are successfully registered");
    }


}