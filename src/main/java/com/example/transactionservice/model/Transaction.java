package com.example.transactionservice.model;

import com.example.transactionservice.enums.ExpenseCategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    @Column(name = "expenseCategoryType", nullable = false)
    private ExpenseCategoryType expenseCategoryType;

    @Column(name = "dateTime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "limitExceeded", nullable = false)
    private boolean limitExceeded = false;
}
