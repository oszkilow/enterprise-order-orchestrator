package com.oszkilow.orchestrator.model;

import com.oszkilow.orchestrator.constant.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;


@Entity
@Table(name= "tbl_order_orchestacion")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID internalId;

    @Column(nullable = false,unique = true)
    private String businessReference;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Double totalAmount;

    private LocalDateTime processendAt;

    @PrePersist
    protected  void onCreate(){
        this.processendAt=LocalDateTime.now();
    }

}
