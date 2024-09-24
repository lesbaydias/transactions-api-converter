package com.example.transactionservice.repository;

import com.example.transactionservice.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyRate, Long> {
    CurrencyRate findTopByCurrencyPairAndDateLessThanEqualOrderByDateDesc(String currencyPair, LocalDate date);
}
