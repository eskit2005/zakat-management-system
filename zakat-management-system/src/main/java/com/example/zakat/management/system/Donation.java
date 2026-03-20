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
@Table(name = "donations", schema = "Zakat")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "donated_at")
    private Instant donatedAt;

    @OneToMany(mappedBy = "donation")
    private Set<Receipt> receipts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "donation")
    private Set<ZakatAssignment> zakatAssignments = new LinkedHashSet<>();


}