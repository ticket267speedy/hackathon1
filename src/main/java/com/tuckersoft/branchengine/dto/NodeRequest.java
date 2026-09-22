package com.tuckersoft.branchengine.dto;
import com.tuckersoft.branchengine.model.BranchType;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NodeRequest {
    @NotBlank @Size(min=3,max=80) private String nodeKey;
    @NotBlank @Size(min=3,max=120) private String title;
    @Size(max=2000) private String narrativeText;
    @NotNull private BranchType branchType;
    @Size(max=120) private String leftOptionCode;
    @Size(max=120) private String rightOptionCode;
}
