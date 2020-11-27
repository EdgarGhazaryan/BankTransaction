package com.bank.BankTransaction.repository;

import com.bank.BankTransaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM transaction tr WHERE tr.account_id IN (SELECT account_id FROM account a WHERE a.user_id = :id)", nativeQuery = true)
    List<Transaction> getByUserId(int id);

    @Query(value = "SELECT * FROM transaction tr WHERE tr.creation_date >= :date" , nativeQuery = true)
    List<Transaction> getByDate(LocalDate date);

    @Query(value = "SELECT * FROM transaction tr WHERE tr.status = :status", nativeQuery = true)
    List<Transaction> getByStatus(String status);
}
