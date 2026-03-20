package com.example.zakat.management.system;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "zakat_assignments", schema = "Zakat")
public class ZakatAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private Beneficiary beneficiary;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    @NotNull
    @Column(name = "amount_assigned", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountAssigned;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "assigned_at")
    private Instant assignedAt;

    @OneToMany(mappedBy = "assignment")
    private Set<DistributionHistory> distributionHistories = new LinkedHashSet<>();


}