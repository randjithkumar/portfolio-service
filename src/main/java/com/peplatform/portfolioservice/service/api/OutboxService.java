package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;

/**
 * Defines the method to save an event into the outbox.
 * <p>
 * Required arguments:
 * - `aggregateType`: The type of the aggregate being processed.
 * - `aggregateId`: The unique identifier for the aggregate.
 * - `topic`: The topic associated with the message. Usually indicates a specific subsystem or domain model.
 * - `messageKey`: A unique key for the message to ensure it is correctly identified within the outbox infrastructure.
 * - `event`: An instance of PortfolioEvent encapsulating the business information related to the event being processed.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
public interface OutboxService {

    /**
     * Saves a business event to the outbox.
     *
     * @param aggregateType The type of the aggregate that is being updated
     * @param aggregateId   The ID of the aggregate that is being updated
     * @param topic         The topic for the business event
     * @param messageKey    The message key for the business event
     * @param event         The business event to be saved
     */
    void saveEvent(
            String aggregateType,
            String aggregateId,
            String topic,
            String messageKey,
            PortfolioEvent event
    );
}