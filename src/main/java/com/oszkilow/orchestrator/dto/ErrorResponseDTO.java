package com.oszkilow.orchestrator.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
}
