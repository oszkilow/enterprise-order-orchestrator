package com.oszkilow.orchestrator.service;

import com.oszkilow.orchestrator.constant.TransactionStatus;
import com.oszkilow.orchestrator.model.entity.OrderTransactionEntity;
import com.oszkilow.orchestrator.model.repository.OrderTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOrchestratorService {

    private final OrderTransactionRepository repository;

    // Inyección de Kafka para notificaciones asíncronas
    private final KafkaTemplate<String, OrderTransactionEntity> kafkaTemplate;

    // Nombre del tópico de Kafka
    private static final String TOPIC = "orders-authorized";

    /**
     * Guarda una nueva orden validando que la referencia no sea duplicada.
     */
    @Transactional
    public OrderTransactionEntity saveOrder(OrderTransactionEntity entity) {
        log.info("💾 Intentando registrar orden con referencia: {}", entity.getBusinessReference());

        if (repository.existsByBusinessReference(entity.getBusinessReference())) {
            log.warn("⚠️ Intento de duplicado para la referencia: {}", entity.getBusinessReference());
            throw new RuntimeException("La referencia de negocio ya existe.");
        }

        return repository.save(entity);
    }

    /**
     * Obtiene todas las órdenes registradas en la DB.
     */
    public List<OrderTransactionEntity> getAllOrders() {
        log.info("📋 Listando todas las órdenes.");
        return repository.findAll();
    }

    /**
     * Busca una orden específica por su ID interno.
     */
    public OrderTransactionEntity getOrderById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    /**
     * Actualiza el estado de la orden y, si es AUTHORIZED, notifica a Kafka.
     */
    @Transactional
    public OrderTransactionEntity updateStatus(UUID id, TransactionStatus newStatus) {
        log.info("⚙️ Cambiando estado de la orden {} a {}", id, newStatus);

        OrderTransactionEntity order = getOrderById(id);
        order.setStatus(newStatus);

        OrderTransactionEntity updatedOrder = repository.save(order);

        // Lógica de Integración: Solo notificamos si el estado final es AUTHORIZED
        if (newStatus == TransactionStatus.AUTHORIZED) {
            sendKafkaEvent(updatedOrder);
        }

        return updatedOrder;
    }

    /**
     * Método privado para desacoplar la lógica de Kafka.
     */
    private void sendKafkaEvent(OrderTransactionEntity order) {
        try {
            log.info("🚀 Notificando a Kafka (Tópico: {}): Orden {}", TOPIC, order.getBusinessReference());

            // Enviamos el objeto completo. Spring Kafka usará el JsonSerializer configurado.
            kafkaTemplate.send(TOPIC, order.getBusinessReference(), order);

        } catch (Exception e) {
            log.error("❌ Falló el envío a Kafka para la orden {}: {}",
                    order.getBusinessReference(), e.getMessage());
            // No lanzamos excepción para no hacer rollback de la DB si Kafka falla (opcional)
        }
    }

    /**
     * Elimina una orden por su UUID.
     */
    @Transactional
    public void deleteOrder(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: ID no existe.");
        }
        repository.deleteById(id);
        log.info("🗑️ Orden {} eliminada exitosamente.", id);
    }
}