package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query(value = "SELECT * FROM receipts WHERE donation_id = :donationId LIMIT 1", nativeQuery = true)
    Optional<Receipt> findByDonationId(@Param("donationId") Long donationId);
}
