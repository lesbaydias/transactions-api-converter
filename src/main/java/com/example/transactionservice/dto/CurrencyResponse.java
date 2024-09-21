package com.example.transactionservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponse {
    private String base;
    private Map<String, BigDecimal> rates;
}
