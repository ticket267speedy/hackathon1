package com.tuckersoft.branchengine.dto;
import com.tuckersoft.branchengine.model.*;
import lombok.*;
import java.time.Instant;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RealityLogResponse {
    private Long id; private Long decisionId; private Long userId; private String nodeKey;
    private BranchType branchType; private ImpactType impactType; private String impactLabel;
    private Boolean delivered; private Instant sentAt;
    public static RealityLogResponse from(RealityLog r) {
        return RealityLogResponse.builder().id(r.getId()).decisionId(r.getDecisionId())
                .userId(r.getUserId()).nodeKey(r.getNodeKey()).branchType(r.getBranchType())
                .impactType(r.getImpactType()).impactLabel(r.getImpactLabel())
                .delivered(r.getDelivered()).sentAt(r.getSentAt()).build();
    }
}
