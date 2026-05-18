package com.oszkilow.orchestrator.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura errores de lógica de negocio (como el de referencia duplicada)
     * que lanzamos manualmente en el Service.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();

        // Identificamos nuestro error específico de duplicados
        if (message != null && message.contains("referencia de negocio ya existe")) {
            status = HttpStatus.CONFLICT; // Error 409
        }

        return buildResponse(status, message);
    }

    /**
     * Captura errores cuando se envían IDs (UUID) con formato incorrecto
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Formato de argumento inválido: " + ex.getMessage());
    }

    /**
     * Método auxiliar para construir la respuesta JSON
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(status.value())
                .status(status.name())
                .message(message)
                .build();

        return new ResponseEntity<>(error, status);
    }

    /**
     * Clase interna para la estructura del JSON de error
     */
    @Getter
    @Builder
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int code;
        private String status;
        private String message;
    }
}