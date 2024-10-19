package com.example.transactionservice.model;

import com.example.transactionservice.enums.ExpenseCategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accountFrom", nullable = false)
    private Long accountFrom;

    @Column(name = "accountTo", nullable = false)
    private Long accountTo;

    @Column(name = "currencyShortName", nullable = false)
    private String currencyShortName;

    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @Column(name = "sumInUSD", nullable = false)
    private BigDecimal sumInUSD;

    @Enumerated(EnumType.STRING)
    @Column(name = "expenseCategoryType", nullable = false)
    private ExpenseCategoryType expenseCategoryType;

    @Column(name = "dateTime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "limitExceeded", nullable = false)
    private boolean limitExceeded = false;
}
