package com.example.transactionservice.service;

import com.example.transactionservice.enums.TypesOfError;
import com.example.transactionservice.exception.CustomException;
import com.example.transactionservice.model.CurrencyRate;
import com.example.transactionservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    @Value("${fixer.api.key}")
    private String apiKey;

    public BigDecimal getCurrencyRate(String currencyPair) {
        LocalDate today = LocalDate.now();

        // 1. Checking the availability of the rate for the current day
        CurrencyRate todayRate = currencyRepository.findTopByCurrencyPairAndDateEquals(currencyPair, today);
        if (todayRate != null) return todayRate.getCloseRate();

        // 2. The rate was not found for today, we are trying to get data via the API
        BigDecimal apiRate = fetchCurrencyRateFromFixerApi(currencyPair);
        if (apiRate != null) return apiRate;

        // 3. If the API did not provide data (for example, a day off), we use the latest available rate
        return getLastAvailableRate(currencyPair, today)
                .orElseThrow(() ->  new CustomException(
                        String.format(TypesOfError.CURRENCY_RATE_NOT_FOUND_EXCEPTION.getMessage(), currencyPair),
                        TypesOfError.CURRENCY_RATE_NOT_FOUND_EXCEPTION.getStatus())
                );
    }


    private BigDecimal fetchCurrencyRateFromFixerApi(String currencyPair) {
        try {
            String[] currencies = currencyPair.split("/");
            String baseCurrency = currencies[0];
            String quoteCurrency = currencies[1];
            String url = buildApiUrl(baseCurrency, quoteCurrency);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return parseApiResponse(response, baseCurrency, quoteCurrency);
        } catch (Exception e) {
            // Error when requesting to API
            return null;
        }
    }


    private Optional<BigDecimal> getLastAvailableRate(String currencyPair, LocalDate today) {
        CurrencyRate previousRate = currencyRepository.findTopByCurrencyPairAndDateLessThanEqualOrderByDateDesc(currencyPair, today);
        return Optional.ofNullable(previousRate).map(CurrencyRate::getCloseRate);
    }



    private String buildApiUrl(String baseCurrency, String quoteCurrency) {
        return String.format("https://data.fixer.io/api/latest?access_key=%s&symbols=%s,%s", apiKey, baseCurrency, quoteCurrency);
    }



    private BigDecimal parseApiResponse(ResponseEntity<Map> response, String baseCurrency, String quoteCurrency) {
        Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
        BigDecimal baseRate = BigDecimal.valueOf(((Number) rates.get(baseCurrency)).doubleValue());
        BigDecimal quoteRate = BigDecimal.valueOf(((Number) rates.get(quoteCurrency)).doubleValue());
        BigDecimal calculatedRate = baseRate.divide(quoteRate, RoundingMode.HALF_UP);

        saveCurrencyRate(baseCurrency + "/" + quoteCurrency, calculatedRate);
        return calculatedRate;
    }


    private void saveCurrencyRate(String currencyPair, BigDecimal rate){
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setCurrencyPair(currencyPair);
        currencyRate.setDate(LocalDate.now());
        currencyRate.setCloseRate(rate);
        currencyRepository.save(currencyRate);
    }

}
