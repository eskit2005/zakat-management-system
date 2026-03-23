package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.response.ReportResponse;
import com.example.zakat.management.system.repositories.BeneficiaryRepository;
import com.example.zakat.management.system.repositories.DonationRepository;
import com.example.zakat.management.system.repositories.UserRepository;
import com.example.zakat.management.system.repositories.ZakatAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final DonationRepository donationRepository;
    private final ZakatAssignmentRepository zakatAssignmentRepository;
    private final UserRepository userRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public ReportResponse getReport() {
        BigDecimal totalDonated      = donationRepository.sumAllAmounts();
        BigDecimal totalDistributed  = zakatAssignmentRepository.sumAllAmounts();
        BigDecimal remaining         = totalDonated.subtract(totalDistributed);

        ReportResponse report = new ReportResponse();
        report.setTotalDonated(totalDonated);
        report.setTotalDistributed(totalDistributed);
        report.setRemaining(remaining);
        report.setTotalDonors(userRepository.countByRole("DONOR"));
        report.setTotalBeneficiaries(beneficiaryRepository.count());
        report.setTotalAssignments(zakatAssignmentRepository.count());
        return report;
    }
}
