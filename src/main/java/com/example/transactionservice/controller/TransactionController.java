package com.example.transactionservice.controller;

import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.processTransaction(transactionRequest));
    }

    @GetMapping("/get-all-transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/exceeded")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Transaction>> getExceededTransactions() {
        List<Transaction> exceededTransactions = transactionService.getExceededTransactions();
        return ResponseEntity.ok(exceededTransactions);
    }
}
