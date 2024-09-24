package com.example.transactionservice.service;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LimitService limitService;

    @Autowired
    private CurrencyService currencyService;

    public Transaction processTransaction(Transaction transaction) {
        BigDecimal sumInUSD = convertToUSD(transaction);
        BigDecimal currentLimit = limitService.getCurrentLimit();

        List<Limit> limits = limitService.getAllLimits();
        LocalDate transactionDate = transaction.getDateTime().toLocalDate();

        boolean limitExceeded = false;

        for (Limit limit : limits) {
            if (limit.getLimitDateTime().toLocalDate().equals(transactionDate) &&
                    sumInUSD.compareTo(limit.getLimitSum()) > 0) {
                limitExceeded = true;
                break;
            }
        }

        transaction.setCurrencyShortName("USD");
        transaction.setSum(sumInUSD);
        transaction.setLimitExceeded(limitExceeded);
        return transactionRepository.save(transaction);
    }


    private BigDecimal convertToUSD(Transaction transaction) {
        if (!transaction.getCurrencyShortName().equalsIgnoreCase("USD"))
            return currencyService.getCurrencyRate(transaction.getCurrencyShortName() + "/USD", transaction.getDateTime().toLocalDate());

        return transaction.getSum();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getExceededTransactions() {
        return transactionRepository.findByLimitExceeded(true);
    }
}
