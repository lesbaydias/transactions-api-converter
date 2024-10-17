package com.example.transactionservice.service;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.LimitRepository;
import com.example.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        for (Transaction transaction : transactions) {
            log.info(limit+"     ref  "+transaction.getSumInUSD());
            limit = limit.add(transaction.getSumInUSD());
        }

        return limit;

    }

    public Limit setNewLimit(BigDecimal newLimit) {
        return limitRepository.save(Limit.builder()
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
        return limitRepository.findAll();
    }
}
