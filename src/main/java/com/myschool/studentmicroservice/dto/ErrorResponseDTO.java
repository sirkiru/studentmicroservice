package com.myschool.studentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response format")
public class ErrorResponseDTO {
    @Schema(description = "HTTP status code", example = "404")
    private int status;
    
    @Schema(description = "Error message", example = "Student not found")
    private String message;
    
    @Schema(description = "Timestamp of error", example = "2023-12-01T12:00:00Z")
    private String timestamp = java.time.Instant.now().toString();

    public ErrorResponseDTO(int status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = java.time.Instant.now().toString();
}
}