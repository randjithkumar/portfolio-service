package com.peplatform.portfolioservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peplatform.portfolioservice.common.enums.OutboxStatus;
import com.peplatform.portfolioservice.entity.OutboxEvent;
import com.peplatform.portfolioservice.exception.BusinessRuleException;
import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import com.peplatform.portfolioservice.repository.OutboxEventRepository;
import com.peplatform.portfolioservice.service.api.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer implementing the business logic for OUTBOX events.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OutboxServiceImpl implements OutboxService {

    /**
     * Repository used for managing outbox events
     * <p>Stores and retrieves event details and metadata.
     */
    private final OutboxEventRepository outboxEventRepository;

    /**
     * Handles the serialization of PortfolioEvent objects for persistence in the Outbox.
     *
     * @see ObjectMapper objectMapper
     * @see OutboxEventRepository outboxEventRepository
     * @see OutboxEvent-Builder outboxEventBuilder
     * @see BusinessRuleException BusinessRuleException
     */
    private final ObjectMapper objectMapper;

    /**
     * Saves a portfolio event to the outbox repository.
     *
     * @param aggregateType The type of the aggregate.
     * @param aggregateId   The ID of the aggregate.
     * @param topic         The topic of the event.
     * @param messageKey    The message key of the event.
     * @param event         The portfolio event to be saved.
     */
    @Override
    public void saveEvent(
            String aggregateType,
            String aggregateId,
            String topic,
            String messageKey,
            PortfolioEvent event
    ) {
        if (outboxEventRepository.existsByEventId(event.eventId())) {
            return;
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .eventId(event.eventId())
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .eventType(event.eventType())
                .eventVersion(event.eventVersion())
                .topicName(topic)
                .messageKey(messageKey)
                .correlationId(event.correlationId())
                .payload(serialize(event))
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .build();

        outboxEventRepository.save(outboxEvent);
    }

    /**
     * Serializes the given `PortfolioEvent` into a JSON string.
     *
     * @param event The `PortfolioEvent` to be serialized.
     * @return A JSON string representing the given `PortfolioEvent`.
     */
    private String serialize(PortfolioEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            throw new BusinessRuleException(
                    "Unable to serialize outbox event: "
                            + event.eventId()
            );
        }
    }
}