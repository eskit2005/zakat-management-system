package com.example.zakat.management.system;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "distribution_history", schema = "Zakat")
public class DistributionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private ZakatAssignment assignment;

    @Size(max = 100)
    @NotNull
    @Column(name = "beneficiary_name", nullable = false, length = 100)
    private String beneficiaryName;

    @NotNull
    @Column(name = "amount_received", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountReceived;

    @NotNull
    @Column(name = "distribution_date", nullable = false)
    private LocalDate distributionDate;


}