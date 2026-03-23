package com.example.zakat.management.system.controllers;

import com.example.zakat.management.system.dtos.response.DistributionHistoryResponse;
import com.example.zakat.management.system.services.DistributionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class DistributionHistoryController {

    private final DistributionHistoryService distributionHistoryService;

    @GetMapping("")
    public ResponseEntity<List<DistributionHistoryResponse>> getAll() {
        return ResponseEntity.ok(distributionHistoryService.getAll());
    }
}
