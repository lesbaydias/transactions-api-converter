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

    private final LimitRepository limitRepository;

    @Autowired
    public LimitService(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    public BigDecimal getCurrentLimit() {
        Limit currentLimit = limitRepository.findTopByOrderByLimitDateTimeDesc();
        return currentLimit != null ? currentLimit.getLimitSum() : new BigDecimal("1000"); // Дефолтный лимит $1000
    }

    public Limit setNewLimit(BigDecimal newLimit) {
        Limit limit = new Limit();
        limit.setLimitSum(newLimit);
        limit.setLimitDateTime(LocalDateTime.now());
        limit.setLimitCurrencyShortName("USD");
        return limitRepository.save(limit);
    }

    public List<Limit> getAllLimits() {
        return limitRepository.findAll();
    }
}
