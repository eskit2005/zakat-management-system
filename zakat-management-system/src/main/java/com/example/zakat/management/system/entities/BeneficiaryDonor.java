package com.example.zakat.management.system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "beneficiary_donor")
public class BeneficiaryDonor {

    @EmbeddedId
    private BeneficiaryDonorId id=new  BeneficiaryDonorId();

    @MapsId("DId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "D_id", insertable = false, updatable = false)
    private Donor donor;

    @MapsId("BId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "B_id", insertable = false, updatable = false)
    private Beneficiary beneficiary;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "donated_at", insertable = false, updatable = false)
    private Instant donatedAt;
}