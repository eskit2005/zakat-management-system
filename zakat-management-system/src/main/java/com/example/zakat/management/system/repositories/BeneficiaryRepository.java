package com.example.zakat.management.system.repositories;

import com.example.zakat.management.system.entities.Beneficiary;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    @Query(value = "SELECT * FROM beneficiaries WHERE user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<Beneficiary> findByUserId(@Param("userId") Long userId);

    @NullMarked
    @Query(value= "Select * From beneficiaries Where id = :id", nativeQuery = true)
    Optional<Beneficiary> findById(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM beneficiaries WHERE user_id = :userId", nativeQuery = true)
    int countByUserId(@Param("userId") Long userId);

    @Query(value="SELECT * FROM beneficiaries", nativeQuery = true)
    List<Beneficiary> findAll();

    @Query(value="Select Count(*) From beneficiaries",nativeQuery = true)
    long count();

    @Query(value = """
            SELECT b.* FROM beneficiaries b
            INNER JOIN eligibility_checks ec ON ec.beneficiary_id = b.id
            LEFT JOIN zakat_assignments za ON za.beneficiary_id = b.id
            WHERE ec.is_eligible = TRUE
              AND za.id IS NULL
            ORDER BY b.is_emergency DESC, b.priority_score DESC
            """, nativeQuery = true)
    List<Beneficiary> findEligibleQueue();

    //Meaning of this query:
    // a list of beneficiaries who are eligible according to eligibility checks,
    // have not yet received any Zakat assignment,
    // and we sort them so that emergencies and high-priority cases come first.


}
