package com.tuckersoft.branchengine.dto;
import com.tuckersoft.branchengine.model.Playthrough;
import com.tuckersoft.branchengine.model.PlaythroughStatus;
import lombok.*;
import java.time.Instant;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlaythroughResponse {
    private Long id; private Long userId; private String userUsername;
    private Long currentNodeId; private String currentNodeKey;
    private PlaythroughStatus status; private Integer score;
    private Instant startedAt; private Instant finishedAt;
    private String endingKey; private Integer totalDecisions;
    public static PlaythroughResponse from(Playthrough p) {
        return PlaythroughResponse.builder().id(p.getId())
                .userId(p.getUser().getId()).userUsername(p.getUser().getUsername())
                .currentNodeId(p.getCurrentNode().getId()).currentNodeKey(p.getCurrentNode().getNodeKey())
                .status(p.getStatus()).score(p.getScore())
                .startedAt(p.getStartedAt()).finishedAt(p.getFinishedAt())
                .endingKey(p.getEndingKey()).totalDecisions(p.getTotalDecisions()).build();
    }
}
