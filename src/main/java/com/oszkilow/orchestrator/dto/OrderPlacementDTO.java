package com.oszkilow.orchestrator.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderPlacementDTO {
    @NotBlank(message = "Business reference is required")
    private String businessReference;

    @Positive(message = "Amount must be greater than zero")
    private Double totalAmount;

    private String customerIdentifier;
}
