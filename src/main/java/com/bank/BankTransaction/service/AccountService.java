package com.bank.BankTransaction.service;

import com.bank.BankTransaction.model.Account;
import com.bank.BankTransaction.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public Account getById(int id) {
        return accountRepository.findById(id).orElse(null);
    }

    public void update(Account account) {
        accountRepository.save(account);
    }
}
