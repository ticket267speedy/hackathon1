package com.tuckersoft.branchengine.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "playthroughs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Playthrough {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "current_node_id", nullable = false)
    private StoryNode currentNode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlaythroughStatus status;

    @Column(nullable = false)
    private Integer score = 0;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "ending_key", length = 80)
    private String endingKey;

    @Column(name = "total_decisions", nullable = false)
    private Integer totalDecisions = 0;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist void onCreate() {
        if (startedAt == null) startedAt = Instant.now();
        if (createdAt == null) createdAt = Instant.now();
        if (updatedAt == null) updatedAt = Instant.now();
        if (status == null) status = PlaythroughStatus.IN_PROGRESS;
        if (score == null) score = 0;
        if (totalDecisions == null) totalDecisions = 0;
    }
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }
}
