package com.peplatform.portfolioservice.kafka.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peplatform.portfolioservice.entity.OutboxEvent;
import com.peplatform.portfolioservice.kafka.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * An OutboxEventMapper class负责将OutboxEvent deserialization成PortfolioEvent.
 * It utilizes ObjectMapper to map the serialized data of OutboxEvent to instances of PortfolioEvent.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class OutboxEventMapper {

    /**
     * Represents an object mapper for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    /**
     * Deserializes an OutboxEvent into a PortfolioEvent instance.
     * <p>
     * Parses the payload of the OutboxEvent and uses an ObjectMapper to create an instance of the corresponding PortfolioEvent class.
     *
     * @param outboxEvent The OutboxEvent to deserialized
     * @return A new PortfolioEvent instance created from the payload
     */
    public PortfolioEvent deserialize(OutboxEvent outboxEvent) {
        Class<? extends PortfolioEvent> eventClass =
                resolveEventClass(outboxEvent.getEventType());

        try {
            return objectMapper.readValue(
                    outboxEvent.getPayload(),
                    eventClass
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Unable to deserialize outbox event "
                            + outboxEvent.getEventId(),
                    exception
            );
        }
    }

    /**
     * Resolves the type of a portfolio event based on the event type.
     * <p>Sets the value of this instance using JSON from input byte array.</p>
     *
     * @param eventType The name of the desired event; one of "INVESTMENT_CREATED", "INVESTMENT_STATUS_CHANGED", or "FUND_NAV_UPDATED"
     * @return A class that implements PortfolioEvent
     */
    private Class<? extends PortfolioEvent> resolveEventClass(
            String eventType
    ) {
        return switch (eventType) {
            case "INVESTMENT_CREATED" -> InvestmentCreatedEvent.class;

            case "INVESTMENT_STATUS_CHANGED" -> InvestmentStatusChangedEvent.class;

            case "FUND_NAV_UPDATED" -> FundNavUpdatedEvent.class;

            default -> throw new IllegalArgumentException(
                    "Unsupported outbox event type: " + eventType
            );
        };
    }
}