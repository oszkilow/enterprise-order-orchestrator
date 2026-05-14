package com.oszkilow.orchestrator.service;

import com.oszkilow.orchestrator.client.ExternalValidationClient;
import com.oszkilow.orchestrator.constant.TransactionStatus;
import com.oszkilow.orchestrator.dto.OrderPlacementDTO;
import com.oszkilow.orchestrator.model.OrderTransactionEntity;
import com.oszkilow.orchestrator.repository.OrderTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderOrchestratorService {

    private final OrderTransactionRepository repository;
    private final ExternalValidationClient validationClient;

    public OrderTransactionEntity processInitialOrder(OrderPlacementDTO dto) {
        // Log de inicio con emojis para escaneo visual rápido en tu ThinkCentre
        log.info("🚀 Iniciando orquestación de pedido. Ref: {} | Cliente: {}",
                dto.getBusinessReference(), dto.getCustomerIdentifier());

        // Definimos el estado inicial como PENDING o similar si fuera necesario,
        // pero aquí usaremos una lógica de validación.
        TransactionStatus finalStatus = TransactionStatus.AUTHORIZED;

        // Paso Senior: Llamada al sistema externo mediante Feign
        try {
            log.info("🔍 Paso 2: Consultando validación externa para Ref: {}", dto.getBusinessReference());

            // Suponiendo que enviamos un ID o dato del DTO
            Object externalResponse = validationClient.validateWithExternalSystem(1);

            log.info("✅ Paso 3: Validación externa exitosa. Respuesta: {}", externalResponse);
        } catch (Exception e) {
            log.error("❌ Paso 3: Fallo en comunicación con sistema externo. Motivo: {}", e.getMessage());
            // Si la validación falla, cambiamos el estado para no autorizar algo erróneo
            finalStatus = TransactionStatus.REJECTED;
        }

        // Construcción de la entidad con el estado resultante
        OrderTransactionEntity entity = OrderTransactionEntity.builder()
                .businessReference(dto.getBusinessReference())
                .customerIdentifier(dto.getCustomerIdentifier()) // Asegúrate de tener este campo en tu Entity
                .totalAmount(dto.getTotalAmount())
                .status(finalStatus)
                .createdAt(LocalDateTime.now()) // Siempre es bueno tener el timestamp
                .build();

        OrderTransactionEntity savedEntity = repository.save(entity);

        log.info("💾 Paso 4: Transacción persistida en BD con ID: {} y Status: {}",
                savedEntity.getInternalId(), savedEntity.getStatus());

        return savedEntity;
    }
}