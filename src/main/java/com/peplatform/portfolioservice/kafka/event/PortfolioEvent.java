package com.peplatform.portfolioservice.kafka.event;

import java.time.Instant;
import java.util.UUID;

/**
 * **PortfolioEvent**: An interface that defines the behavior for tracking portfolio events within a system.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
public interface PortfolioEvent {

    /**
     * Retrieves a portfolio event by its ID.
     *
     * <p>Finds the portfolio event with the specified ID and returns it.
     *
     * @return A PortfolioEvent object representing the retrieved event
     */
    UUID eventId();

    /**
     * Retrieves the type of an event.
     * <p>The method takes an entity and returns the event type based on a predefined association or mapping.
     *
     * @return The event type associated with the given entity
     */
    String eventType();

    /**
     * Retrieves the version of a portfolio event.
     * <p>
     * This method queries the `PortfolioEvent` entity by its unique identifier (`eventId`) and returns its version number.
     *
     * @return The version number of the portfolio event
     */
    String eventVersion();

    /**
     * Retrieves the correlation ID for a portfolio event.
     * <p>
     * This method is responsible for returning the correlation ID as specified in the `PortfolioEvent` interface.
     *
     * @return The correlation ID of the event
     */
    String correlationId();

    /**
     * Retrieves the timestamp when this event was occurred.
     *
     * @return The timestamp of when this event was occurred, in {@link Instant}.
     */
    Instant occurredAt();
}