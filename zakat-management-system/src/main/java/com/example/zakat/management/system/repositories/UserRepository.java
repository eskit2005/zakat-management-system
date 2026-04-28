package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    // Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByEmail(String email);

    // @Query(value = "SELECT COUNT(*) FROM users WHERE email = :email", nativeQuery = true)
    // int countByEmail(@Param("email") String email);
    int countByEmail(String email);

    // @Query(value = "SELECT COUNT(*) FROM users WHERE role = :role", nativeQuery = true)
    // long countByRole(@Param("role") String role);
    long countByRole(String role);

    // @Query(value = "SELECT * FROM users", nativeQuery = true)
    // List<User> findAll();
    List<User> findAll();
}