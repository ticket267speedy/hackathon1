package com.ticket267.hackathon1.dto;

import com.ticket267.hackathon1.model.Playthrough;
import com.ticket267.hackathon1.model.PlaythroughStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlaythroughResponse {
    private Long id;
    private Long playerId;
    private String playerUsername;
    private Long currentNodeId;
    private String currentNodeKey;
    private String currentNodeTitle;
    private PlaythroughStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String endingKey;
    private Integer totalDecisions;

    public static PlaythroughResponse from(Playthrough p) {
        return PlaythroughResponse.builder()
                .id(p.getId())
                .playerId(p.getPlayer().getId())
                .playerUsername(p.getPlayer().getUsername())
                .currentNodeId(p.getCurrentNode().getId())
                .currentNodeKey(p.getCurrentNode().getNodeKey())
                .currentNodeTitle(p.getCurrentNode().getTitle())
                .status(p.getStatus())
                .startedAt(p.getStartedAt())
                .finishedAt(p.getFinishedAt())
                .endingKey(p.getEndingKey())
                .totalDecisions(p.getTotalDecisions())
                .build();
    }
}
