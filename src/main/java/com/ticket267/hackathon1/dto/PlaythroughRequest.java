package com.ticket267.hackathon1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PlaythroughRequest {

    @NotNull
    private Long userId;

    @Size(max = 80)
    private String startNodeKey;
}
