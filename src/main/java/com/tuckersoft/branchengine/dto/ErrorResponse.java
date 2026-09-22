package com.tuckersoft.branchengine.dto;
import lombok.*;
import java.time.OffsetDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorResponse {
    private String error; private String message; private String timestamp; private String path;
    public static ErrorResponse of(String e, String m, String p) {
        return ErrorResponse.builder().error(e).message(m).timestamp(OffsetDateTime.now().toString()).path(p).build();
    }
}
