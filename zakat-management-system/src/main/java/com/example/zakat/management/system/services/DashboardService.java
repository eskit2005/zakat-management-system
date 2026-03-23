package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.response.DashboardResponse;
import com.example.zakat.management.system.mappers.DistributionHistoryMapper;
import com.example.zakat.management.system.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final EligibilityCheckRepository eligibilityCheckRepository;
    private final ZakatAssignmentRepository zakatAssignmentRepository;
    private final DonationRepository donationRepository;
    private final DistributionHistoryRepository distributionHistoryRepository;
    private final DistributionHistoryMapper distributionHistoryMapper;

    // @Transactional(readOnly = true) keeps the session open so DistributionHistoryMapper
    // can access history.getAssignment().getId() (LAZY OneToOne)
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        BigDecimal totalDonated     = donationRepository.sumAllAmounts();
        BigDecimal totalDistributed = zakatAssignmentRepository.sumAllAmounts();

        DashboardResponse dashboard = new DashboardResponse();
        dashboard.setTotalDonors(userRepository.countByRole("DONOR"));
        dashboard.setTotalBeneficiaries(beneficiaryRepository.count());
        dashboard.setPendingEligibility(
                beneficiaryRepository.count() - eligibilityCheckRepository.count()
        );
        dashboard.setEligibleBeneficiaries(eligibilityCheckRepository.countByIsEligible(true));
        dashboard.setAssignedBeneficiaries(zakatAssignmentRepository.count());
        dashboard.setTotalDonated(totalDonated);
        dashboard.setTotalDistributed(totalDistributed);
        dashboard.setRemaining(totalDonated.subtract(totalDistributed));
        dashboard.setRecentDistributions(
                distributionHistoryRepository.findAllByOrderByDistributionDateDesc()
                        .stream()
                        .limit(5)
                        .map(distributionHistoryMapper::toResponse)
                        .toList()
        );
        return dashboard;
    }
}
