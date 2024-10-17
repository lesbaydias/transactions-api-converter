package com.example.transactionservice.service;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.repository.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class LimitResetService {

    private final LimitRepository limitRepository;

    @Scheduled(cron = "0 0 0 1 * ?", zone = "UTC")
    public void resetMonthlyLimits() {
        BigDecimal defaultLimit = BigDecimal.valueOf(1000);

        YearMonth currentYearMonth = YearMonth.now();
        Limit existingLimit = limitRepository.findTopByOrderByLimitDateTimeDesc();

        if (existingLimit == null || !isSameMonthAndYear(existingLimit.getLimitDateTime(), currentYearMonth))
            limitRepository.save(
                Limit.builder()
                        .limitSum(defaultLimit)
                        .limitCurrencyShortName("USD")
                        .limitDateTime(LocalDateTime.now()).build()
            );

    }
    private boolean isSameMonthAndYear(LocalDateTime limitDate, YearMonth currentYearMonth) {
        YearMonth limitYearMonth = YearMonth.from(limitDate);
        return limitYearMonth.equals(currentYearMonth);
    }
}
