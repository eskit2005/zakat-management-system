package com.example.zakat.management.system.controllers;

import com.example.zakat.management.system.dtos.response.ReceiptResponse;
import com.example.zakat.management.system.services.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping
    public ResponseEntity<List<ReceiptResponse>> getAllReceipts() {
        return ResponseEntity.ok(receiptService.getAllReceipts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptResponse> getReceiptById(@PathVariable Long id) {
        return ResponseEntity.ok(receiptService.getReceiptById(id));
    }

    @GetMapping("/donation/{donationId}")
    public ResponseEntity<ReceiptResponse> getReceiptByDonationId(@PathVariable Long donationId) {
        return ResponseEntity.ok(receiptService.getReceiptByDonationId(donationId));
    }
}
