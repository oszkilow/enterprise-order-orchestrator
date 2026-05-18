package com.oszkilow.orchestrator.config;

import com.oszkilow.orchestrator.model.entity.OrderTransactionEntity;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${SPRING_KAFKA_BOOTSTRAP_SERVERS:kafka:29092}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, OrderTransactionEntity> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 👇 Con esto le indicamos a Kafka que transforme tu Entity a un JSON válido
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderTransactionEntity> kafkaTemplate() {
        // 👇 Registramos el Bean exacto que tu OrderOrchestratorService está pidiendo a gritos
        return new KafkaTemplate<>(producerFactory());
    }
}
