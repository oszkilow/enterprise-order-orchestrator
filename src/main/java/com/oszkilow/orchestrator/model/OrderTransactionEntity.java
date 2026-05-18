package com.oszkilow.orchestrator.model.entity;

import com.oszkilow.orchestrator.constant.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_order_orchestration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "internal_id", updatable = false, nullable = false)
    private UUID internalId;

    @Column(name = "business_reference", nullable = false, unique = true)
    private String businessReference;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    // --- CAMPOS DE AUDITORÍA AUTOMÁTICA ---

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
