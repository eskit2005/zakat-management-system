package com.example.zakat.management.system.controllers;

import com.example.zakat.management.system.dtos.request.DonationRequest;
import com.example.zakat.management.system.dtos.response.DonationResponse;
import com.example.zakat.management.system.services.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping("")
    public ResponseEntity<DonationResponse> donate(@Valid @RequestBody DonationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(donationService.donate(request));
    }

    @GetMapping("")
    public ResponseEntity<List<DonationResponse>> getAllDonations() {
        return ResponseEntity.ok(donationService.getAllDonations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationResponse> getDonationById(@PathVariable Long id) {
        return ResponseEntity.ok(donationService.getDonationById(id));
    }
}
