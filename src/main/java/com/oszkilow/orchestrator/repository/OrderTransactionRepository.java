package com.oszkilow.orchestrator.model.repository;

import com.oszkilow.orchestrator.model.entity.OrderTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransactionEntity, UUID> {
    // Esto nos permitirá buscar órdenes por su código de negocio, no solo por ID
    boolean existsByBusinessReference(String businessReference);
}

