package com.oszkilow.orchestrator.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "external-validator", url = "https://jsonplaceholder.typicode.com")public interface ExternalValidationClient {

    @GetMapping("/todos/{id}")
        // Usamos un endpoint público real para probar la conexión
    Object validateWithExternalSystem(@PathVariable("id") int id);
}





