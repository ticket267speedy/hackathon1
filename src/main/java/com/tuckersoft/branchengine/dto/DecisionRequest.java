package com.tuckersoft.branchengine.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DecisionRequest {
    @NotBlank @Size(min=1,max=500) private String inputText;
    private Boolean timedOut;
    private Long responseMillis;
    @Size(max=10) private String chosenOptionCode;
}
