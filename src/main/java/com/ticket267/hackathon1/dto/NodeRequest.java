package com.ticket267.hackathon1.dto;

import com.ticket267.hackathon1.model.NodeType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NodeRequest {

    @NotBlank @Size(min = 3, max = 80)
    private String nodeKey;

    @NotBlank @Size(min = 3, max = 120)
    private String title;

    @Size(max = 2000)
    private String narrativeText;

    @NotNull
    private NodeType nodeType;

    @Size(max = 200)
    private String leftOption;

    @Size(max = 200)
    private String rightOption;
}
