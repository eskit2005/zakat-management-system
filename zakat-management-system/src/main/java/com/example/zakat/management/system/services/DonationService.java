package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.DonationRequest;
import com.example.zakat.management.system.dtos.response.DonationResponse;
import com.example.zakat.management.system.entities.Donation;
import com.example.zakat.management.system.entities.Receipt;
import com.example.zakat.management.system.entities.User;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.mappers.DonationMapper;
import com.example.zakat.management.system.repositories.DonationRepository;
import com.example.zakat.management.system.repositories.ReceiptRepository;
import com.example.zakat.management.system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final DonationMapper donationMapper;

    // @Transactional keeps the session open across both saves and the final re-fetch
    // so DonationMapper can access donor (LAZY ManyToOne) and receipt (LAZY OneToOne)
    @Transactional
    public DonationResponse donate(DonationRequest request) {
        User donor = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + request.getUserId()));

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setAmount(request.getAmount());
        Donation saved = donationRepository.save(donation);

        Receipt receipt = new Receipt();
        receipt.setDonation(saved);
        receipt.setReceiptNumber("ZKT-" + Year.now().getValue() + "-" + String.format("%05d", saved.getId()));
        receipt.setDonorName(donor.getName());
        receipt.setAmount(saved.getAmount());
        Receipt savedReceipt = receiptRepository.save(receipt);

        saved.setReceipt(savedReceipt);

        return donationMapper.toResponse(saved);
    }

    // DonationMapper accesses donation.getDonor().getId/Name (LAZY) and donation.getReceipt() (LAZY)
    @Transactional(readOnly = true)
    public List<DonationResponse> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(donationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DonationResponse getDonationById(Long id) {
        return donationMapper.toResponse(
                donationRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id: " + id))
        );
    }
}
