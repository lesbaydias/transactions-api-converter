package com.example.transactionservice.service;

import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LimitService limitService;

    public Transaction processTransaction(Transaction transaction) {
        boolean limitExceeded = limitService.checkLimitExceeded(transaction.getSum(), transaction.getCurrencyShortName());
        transaction.setLimitExceeded(limitExceeded);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsThatExceededLimit() {
        return transactionRepository.findByLimitExceededTrue();
    }
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
