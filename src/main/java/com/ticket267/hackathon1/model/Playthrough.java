package com.ticket267.hackathon1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playthroughs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Playthrough {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "current_node_id", nullable = false)
    private WorkflowNode currentNode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlaythroughStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "ending_key", length = 80)
    private String endingKey;

    @Column(name = "total_decisions", nullable = false)
    private Integer totalDecisions = 0;

    @PrePersist
    void onCreate() {
        if (startedAt == null)      startedAt = LocalDateTime.now();
        if (status == null)         status = PlaythroughStatus.IN_PROGRESS;
        if (totalDecisions == null) totalDecisions = 0;
    }
}
