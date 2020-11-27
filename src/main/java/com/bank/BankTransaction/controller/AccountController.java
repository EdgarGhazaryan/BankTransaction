package com.bank.BankTransaction.controller;

import com.bank.BankTransaction.model.Account;
import com.bank.BankTransaction.model.User;
import com.bank.BankTransaction.service.AccountService;
import com.bank.BankTransaction.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    final AccountService accountService;
    final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @PostMapping("/{idAdm}/{idUs}")
    public String createAccount(@PathVariable int idAdm, @PathVariable int idUs) {
        User admin = userService.getById(idAdm);
        if(admin == null) {
            return "Invalid admin id";
        }
        if(!admin.getRole().equals("ADMIN")) {
            return "Your role isn't ADMIN";
        }
        User user = userService.getById(idUs);
        if(user == null) {
            return "Invalid user id";
        }
        Account account = new Account();
        account.setOwner(user);
        accountService.save(account);
        return "Account for user " + user.getFirstName() + " " + user.getLastName() + " is created";
    }
}
