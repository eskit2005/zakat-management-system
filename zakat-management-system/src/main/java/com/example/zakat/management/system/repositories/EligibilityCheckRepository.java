package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.EligibilityCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EligibilityCheckRepository extends JpaRepository<EligibilityCheck, Long> {

    @Query(value = "SELECT * FROM eligibility_checks WHERE beneficiary_id = :beneficiaryId LIMIT 1", nativeQuery = true)
    Optional<EligibilityCheck> findByBeneficiaryId(@Param("beneficiaryId") Long beneficiaryId);

    @Query(value = "SELECT COUNT(*) FROM eligibility_checks WHERE beneficiary_id = :beneficiaryId", nativeQuery = true)
    int countByBeneficiaryId(@Param("beneficiaryId") Long beneficiaryId);

    @Query(value = "SELECT COUNT(*) FROM eligibility_checks WHERE is_eligible = :isEligible", nativeQuery = true)
    long countByIsEligible(@Param("isEligible") boolean isEligible);

    @Query(value="Select Count(*) From eligibility_checks",nativeQuery = true)
    long count();

}
