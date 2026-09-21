package com.ticket267.hackathon1.dto;

import com.ticket267.hackathon1.model.Player;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public static PlayerResponse from(Player p) {
        return PlayerResponse.builder()
                .id(p.getId())
                .username(p.getUsername())
                .email(p.getEmail())
                .role(p.getRole().name())
                .createdAt(p.getCreatedAt())
                .lastLogin(p.getLastLogin())
                .build();
    }
}
