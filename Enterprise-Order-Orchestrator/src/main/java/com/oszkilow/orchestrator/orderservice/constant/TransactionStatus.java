package com.oszkilow.orchestrator.orderservice.constant;

public enum TransactionStatus {RECEIVED,             // El pedido entró al sistema
    VALIDATING_RESOURCES, // Verificando con otros microservicios (Integración)
    ORCHESTRATING,        // Procesando reglas de negocio
    COMPLETED,            // Finalizado con éxito
    REJECTED,             // Falló por reglas de negocio
    FAILED_INTEGRATION    // El sistema externo no respondió
}
