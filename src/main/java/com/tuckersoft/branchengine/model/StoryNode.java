package com.tuckersoft.branchengine.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "story_nodes",
       uniqueConstraints = @UniqueConstraint(name = "uk_node_key", columnNames = "node_key"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StoryNode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_key", nullable = false, length = 80)
    private String nodeKey;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "narrative_text", columnDefinition = "TEXT")
    private String narrativeText;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_type", nullable = false, length = 20)
    private BranchType branchType;

    @Column(name = "left_option_code", length = 120)
    private String leftOptionCode;

    @Column(name = "right_option_code", length = 120)
    private String rightOptionCode;
}
