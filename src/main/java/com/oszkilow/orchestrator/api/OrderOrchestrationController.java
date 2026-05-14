package com.oszkilow.orchestrator.api;

import com.oszkilow.orchestrator.dto.OrderPlacementDTO;
import com.oszkilow.orchestrator.model.OrderTransactionEntity;
import com.oszkilow.orchestrator.service.OrderOrchestratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orchestrator")
@RequiredArgsConstructor
public class OrderOrchestrationController {
    private final OrderOrchestratorService service;

    @PostMapping("/process")
    public ResponseEntity<OrderTransactionEntity> placeOrder(@Valid @RequestBody OrderPlacementDTO dto) {
        OrderTransactionEntity processedOrder = service.processInitialOrder(dto);
        return new ResponseEntity<>(processedOrder, HttpStatus.CREATED);
    }
}
