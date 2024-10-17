package com.example.transactionservice.service;

import com.example.transactionservice.FeignClientImpl.CurrencyClient;
import com.example.transactionservice.dto.CurrencyResponse;
import com.example.transactionservice.model.CurrencyRate;
import com.example.transactionservice.repository.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class CurrencyService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);
    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    @Value("${fixer.api.key}")
    private String apiKey;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, RestTemplate restTemplate) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplate;
    }

    public BigDecimal getCurrencyRate(String currencyPair) {
        LocalDate today = LocalDate.now();

        // 1. Проверяем наличие курса на текущий день
        CurrencyRate todayRate = currencyRepository.findTopByCurrencyPairAndDateEquals(currencyPair, today);
        if (todayRate != null) return todayRate.getCloseRate();

        // 2. Курс на сегодня не найден, пробуем получить данные через API
        BigDecimal apiRate = fetchCurrencyRateFromFixerApi(currencyPair);
        if (apiRate != null) return apiRate;

        // 3. Если API не дал данные (например, выходной), используем последний доступный курс
        CurrencyRate previousRate = currencyRepository.findTopByCurrencyPairAndDateLessThanEqualOrderByDateDesc(currencyPair, today);
        if (previousRate != null)
            return previousRate.getCloseRate();

        throw new RuntimeException("Не удалось получить курс валюты для " + currencyPair);
    }

    private BigDecimal fetchCurrencyRateFromFixerApi(String currencyPair) {
        try {
            String[] currencies = currencyPair.split("/");
            String baseCurrency = currencies[0];
            String quoteCurrency = currencies[1];
            String url = "https://data.fixer.io/api/latest?access_key=" + apiKey + "&symbols=" + baseCurrency + "," + quoteCurrency;

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");

            BigDecimal rate = BigDecimal.valueOf(((Number) rates.get(baseCurrency)).doubleValue() / ((Number) rates.get(quoteCurrency)).doubleValue());


            saveCurrencyRate(currencyPair,rate);
            return rate;

        }catch (Exception e){
            // Логируем ошибку запроса к API и возвращаем null, чтобы потом использовать курс из базы
            System.err.println("Ошибка при запросе к API: " + e.getMessage());
            return null;
        }
    }

    private void saveCurrencyRate(String currencyPair, BigDecimal rate){
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setCurrencyPair(currencyPair);
        currencyRate.setDate(LocalDate.now());
        currencyRate.setCloseRate(rate);
        currencyRepository.save(currencyRate);
    }
}
