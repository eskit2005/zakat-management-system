package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query(value = "SELECT * FROM admin WHERE id = :id", nativeQuery = true)
    Optional<Admin> findById(@Param("id") Long id);
}