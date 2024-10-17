package com.example.transactionservice.dto;

import com.example.transactionservice.enums.ExpenseCategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private Long accountFrom;

    private Long accountTo;

    private String currencyShortName;

    private BigDecimal sum;

    private ExpenseCategoryType expenseCategoryType;
}
