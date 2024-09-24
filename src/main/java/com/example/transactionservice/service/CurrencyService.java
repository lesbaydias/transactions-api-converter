package com.example.transactionservice.service;

import com.example.transactionservice.FeignClientImpl.CurrencyClient;
import com.example.transactionservice.dto.CurrencyResponse;
import com.example.transactionservice.model.CurrencyRate;
import com.example.transactionservice.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    @Value("${fixer.api.key}")
    private String apiKey;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, RestTemplate restTemplate) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplate;
    }

    public BigDecimal getCurrencyRate(String currencyPair, LocalDate date) {
        CurrencyRate rate = currencyRepository.findTopByCurrencyPairAndDateLessThanEqualOrderByDateDesc(currencyPair, date);

        if (rate != null) {
            return rate.getCloseRate();
        }

        return fetchCurrencyRateFromFixerApi(currencyPair);
    }

    private BigDecimal fetchCurrencyRateFromFixerApi(String currencyPair) {
        String[] currencies = currencyPair.split("/");
        String baseCurrency = currencies[0];
        String quoteCurrency = currencies[1];
        String url = "https://data.fixer.io/api/latest?access_key=" + apiKey + "&symbols=" + baseCurrency + "," + quoteCurrency;
        System.out.println(url);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        System.out.println(response);
        Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
        System.out.println(rates);
        BigDecimal rate = BigDecimal.valueOf(((Number) rates.get(quoteCurrency)).doubleValue());

        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setCurrencyPair(currencyPair);
        currencyRate.setDate(LocalDate.now());
        currencyRate.setCloseRate(rate);
        currencyRepository.save(currencyRate);

        return rate;
    }
}
