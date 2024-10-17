package com.example.transactionservice.service;

import com.example.transactionservice.enums.TypesOfError;
import com.example.transactionservice.exception.CustomException;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.LimitRepository;
import com.example.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitService {

    private final LimitRepository limitRepository;
    private final TransactionRepository transactionRepository;

    public BigDecimal getCurrentLimits() {
        List<Transaction> transactions = getTransactionsByBetweenDateTime();
        BigDecimal limit = BigDecimal.ZERO;

        if (transactions.isEmpty()) return BigDecimal.ZERO;

        for (Transaction transaction : transactions)
            limit = limit.add(transaction.getSumInUSD());

        return limit;

    }

    public Limit setNewLimit(BigDecimal newLimit) {
        if (newLimit == null || newLimit.compareTo(BigDecimal.ZERO) <= 0)
            throw new CustomException(
                    TypesOfError.LIMIT_MUST_BE_GREATER_THAN_ZERO.getMessage(),
                    TypesOfError.LIMIT_MUST_BE_GREATER_THAN_ZERO.getStatus()
            );

        return limitRepository.save(
                Limit.builder()
                .limitDateTime(LocalDateTime.now())
                .limitSum(newLimit)
                .limitCurrencyShortName("USD")
                .build());
    }
    public List<Transaction> getTransactionsByBetweenDateTime() {
        LocalDateTime startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = LocalDateTime.now();
        return transactionRepository.findTransactionsByDateTimeBetween(startDate, endDate);
    }

    public Limit getCurrentLimit() {
        return limitRepository.findTopByOrderByLimitDateTimeDesc();
    }

    public List<Limit> getLimits() {
        List<Limit> limits = limitRepository.findAll();
        if(limits.isEmpty())
            throw new CustomException(
                TypesOfError.LIMIT_NOT_FOUND.getMessage(),
                TypesOfError.LIMIT_NOT_FOUND.getStatus()
        );
        return limits;
    }
}
