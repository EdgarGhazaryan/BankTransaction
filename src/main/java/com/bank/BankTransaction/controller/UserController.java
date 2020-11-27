package com.bank.BankTransaction.controller;

import com.bank.BankTransaction.model.Account;
import com.bank.BankTransaction.model.Transaction;
import com.bank.BankTransaction.model.User;
import com.bank.BankTransaction.service.TransactionService;
import com.bank.BankTransaction.service.UserService;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    final UserService userService;
    final TransactionService transactionService;

    public UserController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setRole("USER");
        Pair<String, Boolean> validation = userService.validate(user);
        if(validation.getSecond()) {
            userService.save(user);
        }
        return validation.getFirst();
    }

    @PutMapping("/{idAdm}/{idUs}/{role}")
    public String setRole(@PathVariable int idAdm, @PathVariable int idUs, @PathVariable String role) {
        User admin = userService.getById(idAdm);
        if(admin == null) {
            return "Invalid admin id";
        }
        if(!admin.getRole().equals("ADMIN")) {
            return "Your role isn't ADMIN";
        }
        if(idAdm == idUs) {
            return "You can't set your role";
        }
        if (!(role.equals("ADMIN") || role.equals("USER"))) {
            return "Please mention correct role` USER or ADMIN";
        }
        User user = userService.getById(idUs);
        if(user == null) {
            return "Invalid user id";
        }
        user.setRole(role);
        userService.update(user);
        return "Role of user " + user.getFirstName() + " " + user.getLastName() + " is set to " + role;
    }

    @GetMapping("/{id}/transaction")
    public List<Transaction> getTransactions(@PathVariable int id) {
        User user = userService.getById(id);
        if(user == null) {
            return null;
        }
        List<Transaction> transactions = new ArrayList<>();
        List<Account> accounts = user.getAccounts();
        for(Account account : accounts) {
            transactions.addAll(account.getTransactions());
        }
        transactions.sort(Comparator.comparing(Transaction::getCreation_date));
        return transactions;
    }

    @GetMapping("/{id}/transaction/filter")
    public List<Transaction> getTransactionsByDate(@PathVariable int id, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Transaction> transactions = getTransactions(id);
        return transactions.stream().filter(x -> x.getCreation_date().compareTo(localDate) >= 0).collect(Collectors.toList());
    }

    @PutMapping("/cancel/{idUs}/{idTr}")
    public String cancelTransaction(@PathVariable int idUs, @PathVariable int idTr) {
        User user = userService.getById(idUs);
        if(user == null) {
            return "Invalid user id";
        }
        List<Transaction> transactions = getTransactions(idUs);
        Transaction transaction = null;
        for(Transaction tr : transactions) {
            if(tr.getId() == idTr) {
                transaction = tr;
            }
        }
        if(transaction == null) {
            return "Invalid transaction id";
        }
        if(!transaction.getStatus().equals("PENDING")) {
            return "Transaction status isn't PENDING";
        }
        transaction.setStatus("CANCELED");
        transactionService.update(transaction);
        return "Transaction is canceled";
    }
}
