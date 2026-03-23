package com.example.zakat.management.system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "eligibility_checks", schema = "Zakat")
public class EligibilityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", nullable = false, unique = true)
    private Beneficiary beneficiary;

    @Column(name = "age", nullable = false)
    private Short age;

    @Column(name = "monthly_income", nullable = false)
    private BigDecimal monthlyIncome;

    @Column(name = "dependents", nullable = false)
    private Integer dependents;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "has_debt", nullable = false)
    private Boolean hasDebt;

    @Column(name = "has_disability", nullable = false)
    private Boolean hasDisability;

    @Column(name = "is_unemployed", nullable = false)
    private Boolean isUnemployed;

    @Column(name = "has_chronic_illness", nullable = false)
    private Boolean hasChronicIllness;

    @Column(name = "is_orphan", nullable = false)
    private Boolean isOrphan;

    @Column(name = "is_eligible", nullable = false)
    private Boolean isEligible;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "checked_at", insertable = false, updatable = false)
    private Instant checkedAt;
}
