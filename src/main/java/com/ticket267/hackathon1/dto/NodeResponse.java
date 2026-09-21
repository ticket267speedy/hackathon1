package com.ticket267.hackathon1.dto;

import com.ticket267.hackathon1.model.NodeType;
import com.ticket267.hackathon1.model.WorkflowNode;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NodeResponse {
    private Long id;
    private String nodeKey;
    private String title;
    private String narrativeText;
    private NodeType nodeType;
    private String leftOption;
    private String rightOption;
    private Boolean isStart;
    private Boolean isEnding;

    public static NodeResponse from(WorkflowNode n) {
        return NodeResponse.builder()
                .id(n.getId())
                .nodeKey(n.getNodeKey())
                .title(n.getTitle())
                .narrativeText(n.getNarrativeText())
                .nodeType(n.getNodeType())
                .leftOption(n.getLeftOption())
                .rightOption(n.getRightOption())
                .isStart(n.getIsStart())
                .isEnding(n.getIsEnding())
                .build();
    }
}
