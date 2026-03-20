package com.example.zakat.management.system;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "eligibility_checks", schema = "Zakat")
public class EligibilityCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary;

    @NotNull
    @Column(name = "age", nullable = false)
    private Short age;

    @NotNull
    @Column(name = "monthly_income", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyIncome;

    @NotNull
    @Column(name = "dependents", nullable = false)
    private Integer dependents;

    @NotNull
    @Lob
    @Column(name = "reason", nullable = false)
    private String reason;

    @NotNull
    @Column(name = "has_debt", nullable = false)
    private Boolean hasDebt;

    @NotNull
    @Column(name = "has_disability", nullable = false)
    private Boolean hasDisability;

    @NotNull
    @Column(name = "is_unemployed", nullable = false)
    private Boolean isUnemployed;

    @NotNull
    @Column(name = "has_chronic_illness", nullable = false)
    private Boolean hasChronicIllness;

    @NotNull
    @Column(name = "is_orphan", nullable = false)
    private Boolean isOrphan;

    @NotNull
    @Column(name = "is_eligible", nullable = false)
    private Boolean isEligible;

    @Lob
    @Column(name = "rejection_reason")
    private String rejectionReason;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "checked_at")
    private Instant checkedAt;


}