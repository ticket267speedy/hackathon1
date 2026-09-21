package com.ticket267.hackathon1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics",
       uniqueConstraints = @UniqueConstraint(name = "uk_analytics_playthrough", columnNames = "playthrough_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Analytics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playthrough_id", nullable = false)
    private Playthrough playthrough;

    @Column(name = "total_decisions", nullable = false)
    private Integer totalDecisions;

    @Column(name = "unique_nodes", nullable = false)
    private Integer uniqueNodes;

    @Column(name = "endings_reached", nullable = false)
    private Integer endingsReached;

    @Column(name = "duration_seconds", nullable = false)
    private Long durationSeconds;

    @Column(nullable = false)
    private Boolean completed;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @PrePersist
    void onCreate() { if (calculatedAt == null) calculatedAt = LocalDateTime.now(); }
}
