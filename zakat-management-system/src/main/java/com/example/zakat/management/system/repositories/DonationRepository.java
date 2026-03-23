package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.Donation;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query(value = "SELECT * FROM donations WHERE donor_id = :donorId", nativeQuery = true)
    List<Donation> findByDonorId(@Param("donorId") Long donorId);

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM donations", nativeQuery = true)
    BigDecimal sumAllAmounts();

    @NullMarked
    @Query(value= "Select * From donations Where id = :id", nativeQuery = true)
    Optional<Donation> findById(@Param("id") Long id);

    @NullMarked
    @Query(value="SELECT * FROM donations", nativeQuery = true)
    List<Donation> findAll();
}
