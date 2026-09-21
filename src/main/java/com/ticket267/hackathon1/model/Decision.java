package com.ticket267.hackathon1.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "decisions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Decision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playthrough_id", nullable = false)
    private Playthrough playthrough;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "node_id", nullable = false)
    private WorkflowNode node;

    @Enumerated(EnumType.STRING)
    @Column(name = "chosen_option", nullable = false, length = 10)
    private OptionSide chosenOption;

    @Column(name = "decision_text", length = 300)
    private String decisionText;

    @Column(length = 300)
    private String consequence;

    @Column(name = "was_time_out", nullable = false)
    private Boolean wasTimeOut = false;

    @Column(name = "decided_at", nullable = false)
    private LocalDateTime decidedAt;

    @Column(name = "response_millis", nullable = false)
    private Long responseMillis;

    @PrePersist
    void onCreate() {
        if (decidedAt == null)  decidedAt = LocalDateTime.now();
        if (wasTimeOut == null) wasTimeOut = false;
    }
}
