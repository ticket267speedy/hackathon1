package com.tuckersoft.branchengine.dto;
import com.tuckersoft.branchengine.model.BranchType;
import com.tuckersoft.branchengine.model.PlaythroughStatus;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DecisionAcceptedResponse {
    private Long id; private Long playthroughId; private BranchType branchType;
    private String impactLabel; private PlaythroughStatus playthroughStatus;
    private Integer score; private String currentNodeKey;
}
