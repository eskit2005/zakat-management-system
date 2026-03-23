package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.DistributionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistributionHistoryRepository extends JpaRepository<DistributionHistory, Long> {

    @Query(value = "SELECT * FROM distribution_history ORDER BY distribution_date DESC", nativeQuery = true)
    List<DistributionHistory> findAllByOrderByDistributionDateDesc();
}
