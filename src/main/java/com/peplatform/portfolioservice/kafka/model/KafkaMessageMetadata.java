package com.peplatform.portfolioservice.kafka.model;

public record KafkaMessageMetadata(
        String topic,
        int partition,
        long offset
) {
}