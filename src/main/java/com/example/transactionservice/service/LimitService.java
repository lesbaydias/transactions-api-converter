package com.example.transactionservice.service;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.repository.LimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LimitService {

    @Autowired
    private LimitRepository limitRepository;

    public Limit setNewLimit(Limit limit) {
        limit.setLimitDateTime(LocalDateTime.now());
        return limitRepository.save(limit);
    }

    public List<Limit> getAllLimits() {
        return limitRepository.findAll();
    }

    public boolean checkLimitExceeded(BigDecimal amount, String currency) {
        Optional<Limit> limitOpt = limitRepository.findLimitByLimitCurrencyShortName(currency);
        if (limitOpt.isPresent()) {
            Limit limit = limitOpt.get();
            return amount.compareTo(limit.getLimitSum()) > 0;
        }
        return false;
    }
}
