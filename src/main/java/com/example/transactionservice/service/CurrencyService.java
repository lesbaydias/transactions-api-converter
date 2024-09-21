package com.example.transactionservice.service;

import com.example.transactionservice.FeignClientImpl.CurrencyClient;
import com.example.transactionservice.dto.CurrencyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyClient currencyClient;

    public BigDecimal convertToUSD(BigDecimal amount, String currency) {
        CurrencyResponse response = currencyClient.getExchangeRates(currency);
        BigDecimal rateToUSD = response.getRates().get("USD");
        return amount.multiply(rateToUSD);
    }
}
