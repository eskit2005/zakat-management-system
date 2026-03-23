package com.example.zakat.management.system.controllers;

import com.example.zakat.management.system.dtos.request.EligibilityCheckRequest;
import com.example.zakat.management.system.dtos.response.EligibilityCheckResponse;
import com.example.zakat.management.system.services.EligibilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityController {

    private final EligibilityService eligibilityService;

    @PostMapping
    public ResponseEntity<EligibilityCheckResponse> submitCheck(@Valid @RequestBody EligibilityCheckRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eligibilityService.submitCheck(request));
    }

    @GetMapping("/{beneficiaryId}")
    public ResponseEntity<EligibilityCheckResponse> getByBeneficiaryId(@PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(eligibilityService.getByBeneficiaryId(beneficiaryId));
    }
}
