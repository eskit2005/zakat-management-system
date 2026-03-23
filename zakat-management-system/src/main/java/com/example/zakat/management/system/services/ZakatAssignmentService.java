package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.ZakatAssignmentRequest;
import com.example.zakat.management.system.dtos.response.ZakatAssignmentResponse;
import com.example.zakat.management.system.entities.Beneficiary;
import com.example.zakat.management.system.entities.DistributionHistory;
import com.example.zakat.management.system.entities.ZakatAssignment;
import com.example.zakat.management.system.events.ZakatAssignedEvent;
import com.example.zakat.management.system.exceptions.IneligibleBeneficiaryException;
import com.example.zakat.management.system.exceptions.InsufficientFundsException;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.mappers.ZakatAssignmentMapper;
import com.example.zakat.management.system.repositories.BeneficiaryRepository;
import com.example.zakat.management.system.repositories.DistributionHistoryRepository;
import com.example.zakat.management.system.repositories.DonationRepository;
import com.example.zakat.management.system.repositories.ZakatAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZakatAssignmentService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ZakatAssignmentRepository zakatAssignmentRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final DonationRepository donationRepository;
    private final DistributionHistoryRepository distributionHistoryRepository;
    private final ZakatAssignmentMapper zakatAssignmentMapper;

    @Transactional
    public ZakatAssignmentResponse assign(ZakatAssignmentRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findById(request.getBeneficiaryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Beneficiary not found with id: " + request.getBeneficiaryId()));

        if (beneficiary.getEligibilityCheck() == null
                || !beneficiary.getEligibilityCheck().getIsEligible()) {
            throw new IneligibleBeneficiaryException("Beneficiary is not eligible for zakat");
        }

        if (beneficiary.getZakatAssignment() != null) {
            throw new IneligibleBeneficiaryException("Beneficiary has already received zakat");
        }

        BigDecimal totalDonated  = donationRepository.sumAllAmounts();
        BigDecimal totalAssigned = zakatAssignmentRepository.sumAllAmounts();
        BigDecimal remaining     = totalDonated.subtract(totalAssigned);

        if (request.getAmountAssigned().compareTo(remaining) > 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Requested: " + request.getAmountAssigned()
                            + ", Available: " + remaining);
        }

        ZakatAssignment assignment = new ZakatAssignment();
        assignment.setBeneficiary(beneficiary);
        assignment.setAmountAssigned(request.getAmountAssigned());
        ZakatAssignment saved = zakatAssignmentRepository.save(assignment);

        DistributionHistory history = new DistributionHistory();
        history.setAssignment(saved);
        history.setBeneficiaryName(beneficiary.getFullName());
        history.setAmountReceived(request.getAmountAssigned());
        history.setDistributionDate(LocalDate.now());
        distributionHistoryRepository.save(history);

        // Event carries plain values (not lazy entities) so @Async listener runs safely
        // after the transaction commits with no session dependency
        applicationEventPublisher.publishEvent(new ZakatAssignedEvent(
                beneficiary.getUser().getEmail(),
                beneficiary.getFullName(),
                saved.getAmountAssigned()
        ));

        return zakatAssignmentMapper.toResponse(saved);
    }

    // ZakatAssignmentMapper accesses assignment.getBeneficiary().getId/fullName (LAZY)
    @Transactional(readOnly = true)
    public List<ZakatAssignmentResponse> getAllAssignments() {
        return zakatAssignmentRepository.findAll().stream()
                .map(zakatAssignmentMapper::toResponse)
                .toList();
    }
}
