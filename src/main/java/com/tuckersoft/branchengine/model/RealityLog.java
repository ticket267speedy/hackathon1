package com.tuckersoft.branchengine.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "reality_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RealityLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "decision_id", nullable = false)
    private Long decisionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "node_key", nullable = false, length = 80)
    private String nodeKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_type", nullable = false, length = 20)
    private BranchType branchType;

    @Enumerated(EnumType.STRING)
    @Column(name = "impact_type", nullable = false, length = 20)
    private ImpactType impactType;

    @Column(name = "impact_label", length = 200)
    private String impactLabel;

    @Column(nullable = false)
    private Boolean delivered = false;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (delivered == null) delivered = false;
    }
}
