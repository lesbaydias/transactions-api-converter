package com.example.transactionservice.dto;

import com.example.transactionservice.enums.ExpenseCategoryType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {
    private Long accountFrom;

    private Long accountTo;

    private String currencyShortName;

    private BigDecimal sum;

    private BigDecimal sumInUSD;

    private ExpenseCategoryType expenseCategoryType;

    private LocalDateTime dateTime;

    private boolean limitExceeded = false;
}
