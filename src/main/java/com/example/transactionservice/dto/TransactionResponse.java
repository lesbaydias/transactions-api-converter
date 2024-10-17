package com.example.transactionservice.dto;

import com.example.transactionservice.enums.ExpenseCategoryType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String accountFrom;
    private String accountTo;
    private String currencyShortName;
    private BigDecimal sum;
    private BigDecimal sumInUSD;
    private ExpenseCategoryType expenseCategoryType;
    private LocalDateTime dateTime;
    private Boolean limitExceeded;
}
