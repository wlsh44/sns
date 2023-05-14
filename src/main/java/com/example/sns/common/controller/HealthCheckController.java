package com.example.sns.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/heath-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok(String.valueOf(System.currentTimeMillis()));
    }
}
