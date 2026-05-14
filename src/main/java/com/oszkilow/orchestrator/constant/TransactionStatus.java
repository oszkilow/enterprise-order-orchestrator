package com.oszkilow.orchestrator.constant;

public enum TransactionStatus {
    RECEIVED,             // Recibido por la API
    PENDING_VALIDATION,   // Esperando respuesta de sistema externo (Integración)
    AUTHORIZED,           // Validado exitosamente
    REJECTED,             // Rechazado por reglas de negocio
    SYSTEM_ERROR          // Fallo técnico en la integración
}
