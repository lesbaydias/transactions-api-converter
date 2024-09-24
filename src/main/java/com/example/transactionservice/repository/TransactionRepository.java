package com.example.transactionservice.repository;

import com.example.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByLimitExceeded(boolean limitExceeded);

}
