package com.tuckersoft.branchengine.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PlaythroughRequest {
    @NotNull private Long userId;
    @Size(max=80) private String startNodeKey;
}
