package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;

/**
 * Defines a service for validating investment events that are received via Kafka messages.
 * The service validates the investment based on predefined rules and logs validation outcomes.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026/07/21 05:30 PM
 * @since 0.0.1-SNAPSHOT
 */
public interface InvestmentValidationService {

    /**
     * Validates the investment.
     * <p>Finds the investment by the specified event and metadata, then validates the investment details.</p>
     *
     * @param event    Theinvestment creation event
     * @param metadata Metadata about the Kafka message
     */
    void validateInvestment(
            InvestmentCreatedEvent event,
            KafkaMessageMetadata metadata
    );
}