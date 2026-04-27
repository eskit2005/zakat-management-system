package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.BeneficiaryFormRequest;
import com.example.zakat.management.system.dtos.response.BeneficiaryResponse;
import com.example.zakat.management.system.entities.Beneficiary;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.mappers.BeneficiaryMapper;
import com.example.zakat.management.system.repositories.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryMapper beneficiaryMapper;

    @Transactional
    public BeneficiaryResponse submitForm(BeneficiaryFormRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findById(request.getBeneficiaryId())
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + request.getBeneficiaryId()));

        if (beneficiary.getCheckedAt() != null) {
            throw new IllegalStateException("Form already submitted for this beneficiary");
        }

        beneficiary.setReason(request.getReason());
        beneficiary.setDependents(request.getDependents());
        beneficiary.setIncome(request.getIncome());
        beneficiary.setEmergency(request.getEmergency());
        beneficiary.setDisability(request.getDisability());
        beneficiary.setAge(request.getAge());
        beneficiary.setIsOrphan(request.getIsOrphan());
        beneficiary.setHasDebt(request.getHasDebt());
        beneficiary.setUnemployed(request.getUnemployed());
        beneficiary.setIllness(request.getIllness());
        beneficiary.setCheckedAt(Instant.now());

        recalculatePriorityScore(beneficiary);

        return beneficiaryMapper.toResponse(beneficiaryRepository.save(beneficiary));
    }

    public void recalculatePriorityScore(Beneficiary beneficiary) {
        int score = 0;

        if (Boolean.TRUE.equals(beneficiary.getEmergency())) {
            score += 1000;
        }

        if (Boolean.TRUE.equals(beneficiary.getIsOrphan())) score += 25;
        if (Boolean.TRUE.equals(beneficiary.getDisability())) score += 20;
        if (Boolean.TRUE.equals(beneficiary.getIllness())) score += 15;
        if (Boolean.TRUE.equals(beneficiary.getUnemployed())) score += 15;
        if (Boolean.TRUE.equals(beneficiary.getHasDebt())) score += 10;

        if (beneficiary.getAge() != null && beneficiary.getAge() >= 65) {
            score += 10;
        }

        if (beneficiary.getIncome() != null) {
            if (beneficiary.getIncome().compareTo(BigDecimal.ZERO) == 0) {
                score += 20;
            } else if (beneficiary.getIncome().compareTo(new BigDecimal("5000")) < 0) {
                score += 10;
            }
        }

        if (beneficiary.getDependents() != null) {
            int dependentPoints = Math.min(beneficiary.getDependents() * 5, 25);
            score += dependentPoints;
        }

        if (beneficiary.getTotalReceivedValue() != null && beneficiary.getTotalReceivedValue().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal penalty = beneficiary.getTotalReceivedValue()
                    .divide(new BigDecimal("1000"), 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("10"));
            score -= penalty.intValue();
            if (score < 0) score = 0;
        }

        beneficiary.setPriorityScore(score);
    }

    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getAllBeneficiaries() {
        return beneficiaryRepository.findAll().stream()
                .map(beneficiaryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BeneficiaryResponse getById(Long id) {
        return beneficiaryMapper.toResponse(
                beneficiaryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id))
        );
    }

    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getEligibleQueue() {
        return beneficiaryRepository.findEligibleQueue().stream()
                .map(beneficiaryMapper::toResponse)
                .toList();
    }
}