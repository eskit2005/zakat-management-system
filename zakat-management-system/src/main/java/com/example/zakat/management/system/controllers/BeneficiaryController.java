package com.example.zakat.management.system.controllers;

import com.example.zakat.management.system.dtos.request.BeneficiaryRegisterRequest;
import com.example.zakat.management.system.dtos.response.BeneficiaryResponse;
import com.example.zakat.management.system.services.BeneficiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> register(@Valid @RequestBody BeneficiaryRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryService.register(request));
    }

    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getAllBeneficiaries() {
        return ResponseEntity.ok(beneficiaryService.getAllBeneficiaries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaryService.getById(id));
    }

    @GetMapping("/queue")
    public ResponseEntity<List<BeneficiaryResponse>> getEligibleQueue() {
        return ResponseEntity.ok(beneficiaryService.getEligibleQueue());
    }
}
