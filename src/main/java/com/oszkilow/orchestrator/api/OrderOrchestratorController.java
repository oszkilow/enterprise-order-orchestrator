package com.oszkilow.orchestrator.controller;

import com.oszkilow.orchestrator.constant.TransactionStatus;
import com.oszkilow.orchestrator.model.entity.OrderTransactionEntity;
import com.oszkilow.orchestrator.service.OrderOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orchestrator")
@RequiredArgsConstructor
public class OrderOrchestratorController {

    private final OrderOrchestratorService service;

    // CREATE
    @PostMapping("/process")
    public ResponseEntity<OrderTransactionEntity> processOrder(@RequestBody OrderTransactionEntity order) {
        return new ResponseEntity<>(service.saveOrder(order), HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<OrderTransactionEntity>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderTransactionEntity> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    // UPDATE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderTransactionEntity> updateStatus(
            @PathVariable UUID id,
            @RequestParam TransactionStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}