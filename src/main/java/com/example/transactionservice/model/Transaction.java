package com.example.transactionservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private String accountFrom;
    private String accountTo;
    private String currencyShortName;
    private BigDecimal sum;
    private String expenseCategory;
    private LocalDateTime dateTime;
    private Boolean limitExceeded;
}
