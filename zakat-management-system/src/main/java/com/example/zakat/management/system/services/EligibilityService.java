package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.EligibilityCheckRequest;
import com.example.zakat.management.system.dtos.response.EligibilityCheckResponse;
import com.example.zakat.management.system.entities.Beneficiary;
import com.example.zakat.management.system.entities.EligibilityCheck;
import com.example.zakat.management.system.exceptions.DuplicateResourceException;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.mappers.EligibilityCheckMapper;
import com.example.zakat.management.system.repositories.BeneficiaryRepository;
import com.example.zakat.management.system.repositories.EligibilityCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EligibilityService {

    private static final BigDecimal NISAB_THRESHOLD = new BigDecimal("50000.00");

    private final EligibilityCheckRepository eligibilityCheckRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final EligibilityCheckMapper eligibilityCheckMapper;

    @Transactional
    public EligibilityCheckResponse submitCheck(EligibilityCheckRequest request) {
        if (eligibilityCheckRepository.countByBeneficiaryId(request.getBeneficiaryId()) > 0) {
            throw new DuplicateResourceException("Eligibility check already submitted for this beneficiary");
        }

        Beneficiary beneficiary = beneficiaryRepository.findById(request.getBeneficiaryId())
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + request.getBeneficiaryId()));

        boolean isEligible = request.getMonthlyIncome().compareTo(NISAB_THRESHOLD) < 0;
        String rejectionReason = isEligible ? null
                : "Monthly income of " + request.getMonthlyIncome() + " exceeds the nisab threshold of " + NISAB_THRESHOLD;

        int score = isEligible ? calculatePriorityScore(request, beneficiary.getIsEmergency()) : 0;

        EligibilityCheck check = new EligibilityCheck();
        check.setBeneficiary(beneficiary);
        check.setAge(request.getAge());
        check.setMonthlyIncome(request.getMonthlyIncome());
        check.setDependents(request.getDependents());
        check.setReason(request.getReason());
        check.setHasDebt(request.getHasDebt());
        check.setHasDisability(request.getHasDisability());
        check.setIsUnemployed(request.getIsUnemployed());
        check.setHasChronicIllness(request.getHasChronicIllness());
        check.setIsOrphan(request.getIsOrphan());
        check.setIsEligible(isEligible);
        check.setRejectionReason(rejectionReason);

        EligibilityCheck saved = eligibilityCheckRepository.save(check);

        beneficiary.setPriorityScore(score);
        beneficiaryRepository.save(beneficiary);

        EligibilityCheckResponse response = eligibilityCheckMapper.toResponse(saved);
        response.setCalculatedPriorityScore(score);
        return response;
    }

    // EligibilityCheckMapper accesses check.getBeneficiary().getId() (LAZY)
    @Transactional(readOnly = true)
    public EligibilityCheckResponse getByBeneficiaryId(Long beneficiaryId) {
        EligibilityCheck check = eligibilityCheckRepository.findByBeneficiaryId(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("No eligibility check found for beneficiary id: " + beneficiaryId));
        return eligibilityCheckMapper.toResponse(check);
    }

    private int calculatePriorityScore(EligibilityCheckRequest req, boolean isEmergency) {
        int score = 0;
        if (req.getMonthlyIncome().compareTo(BigDecimal.ZERO) == 0) score += 40;
        score += req.getDependents() * 5;
        if (Boolean.TRUE.equals(req.getHasDebt()))           score += 10;
        if (Boolean.TRUE.equals(req.getHasDisability()))     score += 15;
        if (Boolean.TRUE.equals(req.getIsUnemployed()))      score += 10;
        if (Boolean.TRUE.equals(req.getHasChronicIllness())) score += 15;
        if (Boolean.TRUE.equals(req.getIsOrphan()))          score += 20;
        if (isEmergency)                                      score += 30;
        return score;
    }
}
