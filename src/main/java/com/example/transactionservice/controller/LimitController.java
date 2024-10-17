package com.example.transactionservice.controller;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/limits")
public class LimitController {

    private final LimitService limitService;

    @PostMapping("/set-limit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Limit> setNewLimit(@RequestParam BigDecimal newLimit) {
        Limit createdLimit = limitService.setNewLimit(newLimit);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLimit);
    }

    @GetMapping("/get-all-limit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Limit>> getAllLimits() {
        List<Limit> limits = limitService.getLimits();
        return ResponseEntity.ok(limits);
    }
}
