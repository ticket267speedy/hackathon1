package com.ticket267.hackathon1.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workflow_nodes",
       uniqueConstraints = @UniqueConstraint(name = "uk_node_key", columnNames = "node_key"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkflowNode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_key", nullable = false, length = 80)
    private String nodeKey;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "narrative_text", columnDefinition = "TEXT")
    private String narrativeText;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", nullable = false, length = 20)
    private NodeType nodeType;

    @Column(name = "left_option", length = 200)
    private String leftOption;

    @Column(name = "right_option", length = 200)
    private String rightOption;

    @Column(name = "is_start", nullable = false)
    private Boolean isStart = false;

    @Column(name = "is_ending", nullable = false)
    private Boolean isEnding = false;
}
