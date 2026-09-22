package com.tuckersoft.branchengine.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "decisions",
       uniqueConstraints = @UniqueConstraint(name = "uk_decision_idem", columnNames = "idempotency_key"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Decision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playthrough_id", nullable = false)
    private Playthrough playthrough;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "node_id", nullable = false)
    private StoryNode node;

    @Column(name = "input_text", columnDefinition = "TEXT")
    private String inputText;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_type", nullable = false, length = 20)
    private BranchType branchType;

    @Enumerated(EnumType.STRING)
    @Column(name = "impact_type", nullable = false, length = 20)
    private ImpactType impactType;

    @Column(name = "impact_label", nullable = false, length = 200)
    private String impactLabel;

    @Column(name = "chosen_option_code", length = 20)
    private String chosenOptionCode;

    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    @Column(name = "timed_out", nullable = false)
    private Boolean timedOut = false;

    @Column(name = "response_millis", nullable = false)
    private Long responseMillis;

    @Column(name = "decided_at", nullable = false)
    private Instant decidedAt;

    @PrePersist void onCreate() {
        if (decidedAt == null) decidedAt = Instant.now();
        if (timedOut == null) timedOut = false;
    }
}
