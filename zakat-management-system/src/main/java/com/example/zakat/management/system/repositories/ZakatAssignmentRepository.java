package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.ZakatAssignment;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ZakatAssignmentRepository extends JpaRepository<ZakatAssignment, Long> {

    @Query(value = "SELECT COALESCE(SUM(amount_assigned), 0) FROM zakat_assignments", nativeQuery = true)
    BigDecimal sumAllAmounts();

    @NullMarked
    @Query(value="SELECT * FROM zakat_assignments", nativeQuery = true)
    List<ZakatAssignment> findAll();

    @NullMarked
    @Query(value="Select * From zakat_assignments where id = :id", nativeQuery = true)
    Optional<ZakatAssignment> findById(@Param("id") Long id);
}
