package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.DirectDonationRequest;
import com.example.zakat.management.system.entities.Beneficiary;
import com.example.zakat.management.system.entities.BeneficiaryDonor;
import com.example.zakat.management.system.entities.BeneficiaryDonorId;
import com.example.zakat.management.system.entities.User;
import com.example.zakat.management.system.events.ZakatAssignedEvent;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.repositories.BeneficiaryDonorRepository;
import com.example.zakat.management.system.repositories.BeneficiaryRepository;
import com.example.zakat.management.system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectDonationService {

    private final BeneficiaryDonorRepository beneficiaryDonorRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryService beneficiaryService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void donateToBeneficiary(DirectDonationRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findById(request.getBeneficiaryId())
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + request.getBeneficiaryId()));

        BeneficiaryDonor beneficiaryDonor = new BeneficiaryDonor();
        beneficiaryDonor.getId().setDId(request.getDonorId());
        beneficiaryDonor.getId().setBId(request.getBeneficiaryId());
        beneficiaryDonor.setAmount(request.getAmount());

        beneficiaryDonorRepository.save(beneficiaryDonor);

        BigDecimal currentTotal = beneficiary.getTotalReceivedValue() != null
                ? beneficiary.getTotalReceivedValue()
                : BigDecimal.ZERO;
        beneficiary.setTotalReceivedValue(currentTotal.add(request.getAmount()));
        beneficiaryService.recalculatePriorityScore(beneficiary);
        beneficiaryRepository.save(beneficiary);

        User user = userRepository.findById(beneficiary.getId()).orElse(null);
        if (user != null) {
            applicationEventPublisher.publishEvent(new ZakatAssignedEvent(
                    user.getEmail(),
                    user.getName(),
                    request.getAmount()
            ));
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getBeneficiariesDonatedTo(Long donorId) {
        return beneficiaryDonorRepository.findByDId(donorId).stream()
                .map(bd -> bd.getId().getBId())
                .toList();
    }
}