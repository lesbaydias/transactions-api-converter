package com.example.transactionservice.controller;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/limits")
public class LimitController {

    private final LimitService limitService;

    @Autowired
    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @PostMapping("/set-limit")
    public ResponseEntity<Limit> setNewLimit(@RequestBody BigDecimal newLimit) {
        if (newLimit.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Limit limit = limitService.setNewLimit(newLimit);
        return ResponseEntity.status(HttpStatus.CREATED).body(limit);
    }

    @GetMapping("/current-limit")
    public ResponseEntity<BigDecimal> getCurrentLimit() {
        BigDecimal limit = limitService.getCurrentLimit();
        return ResponseEntity.ok(limit);
    }

    @GetMapping("/get-all-limit")
    public ResponseEntity<List<Limit>> getAllLimits() {
        List<Limit> limits = limitService.getAllLimits();
        return ResponseEntity.ok(limits);
    }
}
