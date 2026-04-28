package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.ZakatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZakatAssignmentRepository extends JpaRepository<ZakatAssignment, Long> {

    // @Query(value = "SELECT * FROM zucchini_assignment WHERE id = :id", nativeQuery = true)
    // Optional<ZakatAssignment> findById(@Param("id") Long id);
    Optional<ZakatAssignment> findById(Long id);

    // @Query(value = "SELECT * FROM zucchini_assignment", nativeQuery = true)
    // List<ZakatAssignment> findAll();
    List<ZakatAssignment> findAll();

    @Query("SELECT COALESCE(SUM(z.amountAssigned), 0) FROM ZakatAssignment z")
    Double sumAllAmounts();

    // @Query(value = "SELECT * FROM zucchini_assignment WHERE inven_id = :invenId", nativeQuery = true)
    // List<ZakatAssignment> findByInvenId(@Param("invenId") Long invenId);
    List<ZakatAssignment> findByInventoryId(Long invenId);
}