package com.tuckersoft.branchengine.dto;
import com.tuckersoft.branchengine.model.*;
import lombok.*;
import java.time.Instant;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DecisionResponse {
    private Long id; private Long playthroughId; private Long nodeId; private String nodeKey;
    private String inputText; private BranchType branchType; private ImpactType impactType;
    private String impactLabel; private String chosenOptionCode; private Boolean timedOut;
    private Long responseMillis; private Instant decidedAt;
    public static DecisionResponse from(Decision d) {
        return DecisionResponse.builder().id(d.getId()).playthroughId(d.getPlaythrough().getId())
                .nodeId(d.getNode().getId()).nodeKey(d.getNode().getNodeKey())
                .inputText(d.getInputText()).branchType(d.getBranchType())
                .impactType(d.getImpactType()).impactLabel(d.getImpactLabel())
                .chosenOptionCode(d.getChosenOptionCode()).timedOut(d.getTimedOut())
                .responseMillis(d.getResponseMillis()).decidedAt(d.getDecidedAt()).build();
    }
}
