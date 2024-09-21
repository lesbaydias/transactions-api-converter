package com.example.transactionservice.controller;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/limits")
public class LimitController {

    @Autowired
    private LimitService limitService;

    @PostMapping
    public ResponseEntity<Limit> setNewLimit(@RequestBody Limit limit) {
        Limit newLimit = limitService.setNewLimit(limit);
        return ResponseEntity.ok(newLimit);
    }

    @GetMapping
    public ResponseEntity<List<Limit>> getAllLimits() {
        List<Limit> limits = limitService.getAllLimits();
        return ResponseEntity.ok(limits);
    }
}
