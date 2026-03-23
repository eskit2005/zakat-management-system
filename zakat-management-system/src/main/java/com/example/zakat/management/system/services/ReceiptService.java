package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.response.ReceiptResponse;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.mappers.ReceiptMapper;
import com.example.zakat.management.system.repositories.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    // ReceiptMapper accesses receipt.getDonation().getId() (LAZY OneToOne)
    @Transactional(readOnly = true)
    public List<ReceiptResponse> getAllReceipts() {
        return receiptRepository.findAll().stream()
                .map(receiptMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReceiptResponse getReceiptById(Long id) {
        return receiptMapper.toResponse(
                receiptRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Receipt not found with id: " + id))
        );
    }

    @Transactional(readOnly = true)
    public ReceiptResponse getReceiptByDonationId(Long donationId) {
        return receiptMapper.toResponse(
                receiptRepository.findByDonationId(donationId)
                        .orElseThrow(() -> new ResourceNotFoundException("No receipt found for donation id: " + donationId))
        );
    }
}
