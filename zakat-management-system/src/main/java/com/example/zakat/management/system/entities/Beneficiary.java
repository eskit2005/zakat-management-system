package com.example.zakat.management.system.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "beneficiaries", schema = "Zakat")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @ColumnDefault("0")
    @Column(name = "priority_score", nullable = false)
    private Integer priorityScore = 0;

    @ColumnDefault("0")
    @Column(name = "is_emergency", nullable = false)
    private Boolean isEmergency = false;

    @Column(name = "registered_at", insertable = false, updatable = false)
    private Instant registeredAt;

    @OneToOne(mappedBy = "beneficiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private EligibilityCheck eligibilityCheck;

    @OneToOne(mappedBy = "beneficiary")
    private ZakatAssignment zakatAssignment;
}
