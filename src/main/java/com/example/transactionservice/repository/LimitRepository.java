package com.example.transactionservice.repository;

import com.example.transactionservice.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    Limit findTopByOrderByLimitDateTimeDesc();
}