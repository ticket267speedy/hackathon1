package com.ticket267.hackathon1.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorResponse {
    private String error;
    private String message;
    private String timestamp;
    private String path;

    public static ErrorResponse of(String error, String message, String path) {
        return ErrorResponse.builder()
                .error(error)
                .message(message)
                .timestamp(OffsetDateTime.now().toString())
                .path(path)
                .build();
    }
}
