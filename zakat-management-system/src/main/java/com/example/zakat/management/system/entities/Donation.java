package com.example.zakat.management.system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "donations", schema = "Zakat")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Generated //generated annotation tells hibernate to fetch the default value from the db so the field won't stay at null.
    @Column(name = "donated_at", insertable = false, updatable = false)
    private Instant donatedAt;

    @OneToOne(mappedBy = "donation", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Receipt receipt;
}
