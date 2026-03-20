package com.example.zakat.management.system;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "receipts", schema = "Zakat")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    @Size(max = 50)
    @NotNull
    @Column(name = "receipt_number", nullable = false, length = 50)
    private String receiptNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "donor_name", nullable = false, length = 100)
    private String donorName;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "issued_at")
    private Instant issuedAt;


}