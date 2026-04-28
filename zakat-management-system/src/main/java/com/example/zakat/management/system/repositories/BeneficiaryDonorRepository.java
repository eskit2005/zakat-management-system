package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.BeneficiaryDonor;
import com.example.zakat.management.system.entities.BeneficiaryDonorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryDonorRepository extends JpaRepository<BeneficiaryDonor, BeneficiaryDonorId> {

    // @Query(value = "SELECT * FROM beneficiary_donor WHERE D_id = :DId", nativeQuery = true)
    // List<BeneficiaryDonor> findByDId(@Param("DId") Long DId);
    List<BeneficiaryDonor> findByIdDId(Long dId);

    // @Query(value = "SELECT * FROM beneficiary_donor WHERE B_id = :BId", nativeQuery = true)
    // List<BeneficiaryDonor> findByBId(@Param("BId") Long BId);
    List<BeneficiaryDonor> findByIdBId(Long bId);

    // @Query(value = "SELECT * FROM beneficiary_donor", nativeQuery = true)
    // List<BeneficiaryDonor> findAll();
    List<BeneficiaryDonor> findAll();
}