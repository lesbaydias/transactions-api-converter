package com.example.transactionservice.service;

import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.mapper.TransactionMapper;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final LimitService limitService;
    private final CurrencyService currencyService;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponse processTransaction(TransactionRequest transactionRequest) {
        BigDecimal currentLimit = limitService.getCurrentLimits();
        Limit limit = limitService.getCurrentLimit();

        boolean limitExceeded = false;
        BigDecimal sumInUSD = convertToUSD(transactionRequest);

        log.info(currentLimit.toString());

        if (limit != null && sumInUSD.add(currentLimit).compareTo(limit.getLimitSum()) > 0)
            limitExceeded = true;

        Transaction transaction = transactionMapper.toEntity(transactionRequest);

        transaction.setLimitExceeded(limitExceeded);
        transaction.setSumInUSD(sumInUSD);
        transaction.setDateTime(LocalDateTime.now());

        transactionRepository.save(transaction);
        return transactionMapper.toResponse(transaction);
    }


    private BigDecimal convertToUSD(TransactionRequest transactionRequest) {
        if (!transactionRequest.getCurrencyShortName().equalsIgnoreCase("USD"))
            return transactionRequest.getSum()
                    .divide
                            (currencyService.getCurrencyRate
                                    (transactionRequest.getCurrencyShortName()+"/USD"), RoundingMode.HALF_UP);


        return transactionRequest.getSum();
    }


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getExceededTransactions() {
        return transactionRepository.findByLimitExceeded(true);
    }
}
