package com.oszkilow.orchestrator.exception;

import com.oszkilow.orchestrator.dto.ErrorResponseDTO;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponseDTO> handleFeignException(FeignException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .errorCode("EXTERNAL_SERVICE_ERROR")
                .message("No pudimos validar la orden con el sistema externo.")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("Ocurrió un error inesperado en el orquestador.")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
