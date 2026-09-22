package com.tuckersoft.branchengine.dto;
import com.tuckersoft.branchengine.model.User;
import lombok.*;
import java.time.Instant;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponse {
    private Long id; private String username; private String email; private String role;
    private Instant createdAt; private Instant lastLogin;
    public static UserResponse from(User u) {
        return UserResponse.builder().id(u.getId()).username(u.getUsername()).email(u.getEmail())
                .role(u.getRole().name()).createdAt(u.getCreatedAt()).lastLogin(u.getLastLogin()).build();
    }
}
