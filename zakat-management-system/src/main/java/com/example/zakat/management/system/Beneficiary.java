package com.example.zakat.management.system;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "beneficiaries", schema = "Zakat")
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 100)
    @NotNull
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "priority_score", nullable = false)
    private Integer priorityScore;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_emergency", nullable = false)
    private Boolean isEmergency;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "registered_at")
    private Instant registeredAt;

    @OneToOne(mappedBy = "beneficiary")
    private EligibilityCheck eligibilityCheck;

    @OneToMany(mappedBy = "beneficiary")
    private Set<ZakatAssignment> zakatAssignments = new LinkedHashSet<>();


}