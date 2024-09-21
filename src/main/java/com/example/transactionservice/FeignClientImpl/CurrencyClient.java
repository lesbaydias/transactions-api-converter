package com.example.transactionservice.FeignClientImpl;

import com.example.transactionservice.dto.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency-api", url = "https://api.exchangeratesapi.io")
public interface CurrencyClient {
    @GetMapping("/latest")
    CurrencyResponse getExchangeRates(@RequestParam("base") String baseCurrency);
}
