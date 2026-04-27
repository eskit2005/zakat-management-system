package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

    @Query(value = "SELECT * FROM donor WHERE id = :id", nativeQuery = true)
    Optional<Donor> findById(@Param("id") Long id);
}