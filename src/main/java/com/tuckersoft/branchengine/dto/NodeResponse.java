package com.tuckersoft.branchengine.dto;
import com.tuckersoft.branchengine.model.BranchType;
import com.tuckersoft.branchengine.model.StoryNode;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NodeResponse {
    private Long id; private String nodeKey; private String title;
    private String narrativeText; private BranchType branchType;
    private String leftOptionCode; private String rightOptionCode;
    public static NodeResponse from(StoryNode n) {
        return NodeResponse.builder().id(n.getId()).nodeKey(n.getNodeKey()).title(n.getTitle())
                .narrativeText(n.getNarrativeText()).branchType(n.getBranchType())
                .leftOptionCode(n.getLeftOptionCode()).rightOptionCode(n.getRightOptionCode()).build();
    }
}
