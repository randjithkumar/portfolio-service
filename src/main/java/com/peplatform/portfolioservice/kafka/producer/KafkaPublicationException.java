package com.peplatform.portfolioservice.kafka.producer;

public class KafkaPublicationException extends RuntimeException {

    public KafkaPublicationException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}