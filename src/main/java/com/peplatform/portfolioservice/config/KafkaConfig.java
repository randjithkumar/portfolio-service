package com.peplatform.portfolioservice.config;

import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;


/**
 * Enables Kafka infrastructure while leaving producer and consumer factories to
 * Spring Boot auto-configuration. All client properties are externalized in
 * {@code application.yml}; no broker address or serializer is hard-coded.
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {
    @Bean
    public ProducerFactory<String, Object> kafkaProducerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {

        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120_000);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30_000);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put("spring.json.add.type.headers", true);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean(name = "kafkaTemplate")
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public ConsumerFactory<String, InvestmentCreatedEvent> kafkaConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.consumer.group-id:portfolio-investment-audit-group}") String groupId) {

        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);

        JacksonJsonDeserializer<InvestmentCreatedEvent> valueDeserializer =
                new JacksonJsonDeserializer<>(InvestmentCreatedEvent.class);
        valueDeserializer.addTrustedPackages("com.peplatform.portfolioservice.kafka.event");
        valueDeserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                properties,
                new StringDeserializer(),
                valueDeserializer
        );
    }

    /**
     * The aliases are intentional:
     * kafkaListenerContainerFactory is Spring Kafka's conventional listener factory name;
     * defaultRetryTopicListenerContainerFactory is required by @RetryableTopic.
     */
    @Bean(name = {"kafkaListenerContainerFactory", "defaultRetryTopicListenerContainerFactory"})
    public ConcurrentKafkaListenerContainerFactory<String, InvestmentCreatedEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, InvestmentCreatedEvent> kafkaConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, InvestmentCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setMissingTopicsFatal(false);
        return factory;
    }
}
