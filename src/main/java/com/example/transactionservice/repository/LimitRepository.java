package com.example.transactionservice.repository;

import com.example.transactionservice.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findLimitByLimitCurrencyShortName(String limitCurrencyShortName);
}
