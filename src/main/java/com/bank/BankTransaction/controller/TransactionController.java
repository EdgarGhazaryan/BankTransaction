package com.bank.BankTransaction.controller;

import com.bank.BankTransaction.model.Account;
import com.bank.BankTransaction.model.Transaction;
import com.bank.BankTransaction.model.User;
import com.bank.BankTransaction.service.AccountService;
import com.bank.BankTransaction.service.TransactionService;
import com.bank.BankTransaction.service.UserService;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    final TransactionService transactionService;
    final AccountService accountService;
    final UserService userService;

    public TransactionController(TransactionService transactionService, AccountService accountService, UserService userService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
    }

    @PostMapping("/{idUs}/{idAcc}")
    public String createTransaction(@PathVariable int idUs, @PathVariable int idAcc, @RequestBody Transaction transaction) {
        User user = userService.getById(idUs);
        if(user == null) {
            return "Invalid user id";
        }
        List<Account> accounts = user.getAccounts();
        boolean validAccountid = false;
        for(Account account : accounts) {
            if(account.getId() == idAcc) {
                validAccountid = true;
            }
        }
        if(!validAccountid) {
            return "User " + user.getFirstName() + " " + user.getLastName() + "hasn't account with id " + idAcc;
        }
        Account account = accountService.getById(idAcc);
        if (account == null) {
            return "Invalid account id";
        }
        transaction.setStatus("PENDING");
        transaction.setAccount(account);
        transaction.setCreation_date(LocalDate.now());
        Pair<String, Boolean> validation = transactionService.validate(transaction);
        if (validation.getSecond()) {
            transactionService.save(transaction);
            return "Transaction for account " + account.getId() + " is created";
        } else {
            return validation.getFirst();
        }
    }

    @GetMapping("/{idAdm}/filter")
    public List<Transaction> getTransactions(@PathVariable int idAdm, @RequestParam(required = false) Integer idUs,
                                             @RequestParam(required = false) String date, @RequestParam(required = false) String status)  {
        User admin = userService.getById(idAdm);
        if(admin == null || !admin.getRole().equals("ADMIN")) {
            return null;
        }
        Set<Transaction> set = new HashSet<>();
        if(idUs != null) {
            set.addAll(transactionService.getByUserId(idUs));
        }
        if(date != null) {
            List<Transaction> list = transactionService.getByDate(LocalDate.parse(date));
            if(set.isEmpty()) {
                set.addAll(list);
            } else {
                set.removeIf(tr -> !list.contains(tr));
            }
        }
        if(status != null) {
            if(!(status.equals("CANCELED") || status.equals("PENDING") || status.equals("ACCEPTED"))) {
                return null;
            }
            List<Transaction> list = transactionService.getByStatus(status);
            if(set.isEmpty()) {
                set.addAll(list);
            } else {
                set.removeIf(tr -> !list.contains(tr));
            }
        }
        return new ArrayList<>(set);
    }

    @Transactional
    @PatchMapping("/{idAdm}/{idTr}")
    public String acceptTransaction(@PathVariable int idAdm, @PathVariable int idTr) {
        User admin = userService.getById(idAdm);
        if(admin == null) {
            return "Invalid user id";
        }
        if(!admin.getRole().equals("ADMIN")) {
            return "Your role isn't ADMIN";
        }
        Transaction transaction = transactionService.getById(idTr);
        if(transaction == null) {
            return "Invalid transaction";
        }
        if(!transaction.getStatus().equals("PENDING")) {
            return "Transaction status isn't PENDING";
        }
        Account account = transaction.getAccount();
        if(transaction.getTransactionType().equals("DEPOSIT")) {
            account.setBalance(account.getBalance() + transaction.getSum());
        } else {
            if(account.getBalance() < transaction.getSum()) {
                transaction.setStatus("CANCELED");
                transactionService.update(transaction);
                return "Not enough resources in account balance. Transaction canceled";
            }
            account.setBalance(account.getBalance() - transaction.getSum());
        }
        transaction.setStatus("ACCEPTED");
        transactionService.update(transaction);
        accountService.update(account);
        return "Transaction is accepted";
    }
}
