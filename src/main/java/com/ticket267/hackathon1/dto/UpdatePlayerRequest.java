package com.ticket267.hackathon1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdatePlayerRequest {
    @Email @Size(max = 120)
    private String email;

    private String currentPassword;

    @Size(min = 8, max = 100)
    private String newPassword;
}
