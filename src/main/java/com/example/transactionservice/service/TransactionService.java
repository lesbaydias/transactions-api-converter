package com.example.transactionservice.service;

import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.enums.TypesOfError;
import com.example.transactionservice.exception.CustomException;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final LimitService limitService;
    private final CurrencyService currencyService;

    @Transactional
    public TransactionResponse processTransaction(TransactionRequest transactionRequest) {
        BigDecimal currentLimitSum = limitService.getCurrentLimits();
        Limit limit = limitService.getCurrentLimit();
        BigDecimal transactionSumInUSD = convertToUSD(transactionRequest);

        boolean isLimitExceeded = isLimitExceeded(transactionSumInUSD, currentLimitSum, limit);

        Transaction transaction = createTransaction(transactionRequest, transactionSumInUSD, isLimitExceeded);

        transactionRepository.save(transaction);

        return TransactionResponse.builder()
                .id(transaction.getId())
                .sum(transaction.getSum())
                .accountFrom(transaction.getAccountFrom())
                .currencyShortName(transaction.getCurrencyShortName())
                .accountTo(transaction.getAccountTo())
                .dateTime(transaction.getDateTime())
                .expenseCategoryType(transaction.getExpenseCategoryType())
                .sumInUSD(transaction.getSumInUSD())
                .limitExceeded(transaction.isLimitExceeded())
                .build();
    }

    private BigDecimal convertToUSD(TransactionRequest transactionRequest) {
        String currency = transactionRequest.getCurrencyShortName();

        if (!currency.equalsIgnoreCase("USD")) {
            BigDecimal conversionRate = currencyService.getCurrencyRate(currency + "/USD");
            return transactionRequest.getSum().divide(conversionRate, RoundingMode.HALF_UP);
        }

        return transactionRequest.getSum();
    }

    private boolean isLimitExceeded(BigDecimal transactionSumInUSD, BigDecimal currentLimitSum, Limit limit) {
        if (limit == null)
            return false;

        return transactionSumInUSD.add(currentLimitSum).compareTo(limit.getLimitSum()) > 0;
    }

    private Transaction createTransaction(TransactionRequest transactionRequest, BigDecimal sumInUSD, boolean limitExceeded) {
        Transaction transaction = Transaction.builder()
                .accountFrom(transactionRequest.getAccountFrom())
                .accountTo(transactionRequest.getAccountTo())
                .currencyShortName(transactionRequest.getCurrencyShortName())
                .dateTime(transactionRequest.getDateTime())
                .expenseCategoryType(transactionRequest.getExpenseCategoryType())
                .sum(transactionRequest.getSum())
                .build();

        transaction.setSumInUSD(sumInUSD);
        transaction.setLimitExceeded(limitExceeded);
        transaction.setDateTime(LocalDateTime.now());
        return transaction;
    }


    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty())
            throw new CustomException(
                TypesOfError.TRANSACTION_NOT_FOUND.getMessage(),
                TypesOfError.TRANSACTION_NOT_FOUND.getStatus()
        );
        return transactions;
    }

    public List<Transaction> getExceededTransactions() {
        List<Transaction> exceededTransactions = transactionRepository.findByLimitExceeded(true);

        if (exceededTransactions.isEmpty()) {
            throw new CustomException(
                    TypesOfError.NOT_FOUND_EXCEEDED_TRANSACTIONS.getMessage(),
                    TypesOfError.NOT_FOUND_EXCEEDED_TRANSACTIONS.getStatus()
            );
        }

        return exceededTransactions;
    }
}
