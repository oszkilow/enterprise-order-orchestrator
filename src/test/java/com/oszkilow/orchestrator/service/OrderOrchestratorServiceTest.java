package com.oszkilow.orchestrator.service;

import com.oszkilow.orchestrator.client.ExternalValidationClient;
import com.oszkilow.orchestrator.dto.OrderPlacementDTO;
import com.oszkilow.orchestrator.model.OrderTransactionEntity;
import com.oszkilow.orchestrator.repository.OrderTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Soluciona class ExtendWith
import org.mockito.InjectMocks; // Soluciona class InjectMocks
import org.mockito.Mock; // Soluciona class Mock
import org.mockito.junit.jupiter.MockitoExtension; // Soluciona class MockitoExtension

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderOrchestratorServiceTest {

    @Mock
    private OrderTransactionRepository repository;

    @Mock
    private ExternalValidationClient validationClient;

    @InjectMocks
    private OrderOrchestratorService service;

    @Test
    void whenProcessOrder_thenSuccess(){
        //Reparacion
        OrderPlacementDTO dto=new OrderPlacementDTO();
        dto.setBusinessReference("TEST-123");
        dto.setTotalAmount(100.0);

        when(validationClient.validateWithExternalSystem(1)).thenReturn("OK");
        when(repository.save(any())).thenReturn(new OrderTransactionEntity());

        // WHEN (Ejecución)
        OrderTransactionEntity result = service.processInitialOrder(dto);

        // THEN (Verificación)
        assertNotNull(result);
        verify(validationClient, times(1)).validateWithExternalSystem(1);
        verify(repository, times(1)).save(any());
    }
}
