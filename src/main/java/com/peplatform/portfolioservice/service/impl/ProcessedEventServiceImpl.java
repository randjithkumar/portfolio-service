package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.enums.ProcessedEventStatus;
import com.peplatform.portfolioservice.entity.ProcessedEvent;
import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;
import com.peplatform.portfolioservice.repository.ProcessedEventRepository;
import com.peplatform.portfolioservice.service.api.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessedEventServiceImpl implements ProcessedEventService {

    private static final int MAX_FAILURE_REASON_LENGTH = 4000;

    private final ProcessedEventRepository processedEventRepository;

    /**
     * Attempts to reserve the Kafka event for the specified consumer.
     * <p>
     * Return values:
     * <p>
     * true:
     * - Event is new and successfully reserved.
     * - Existing event previously failed and is reserved for retry.
     * <p>
     * false:
     * - Event was already completed.
     * - Event is currently being processed.
     * - Another concurrent thread reserved the same event.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean reserveEvent(
            PortfolioEvent event,
            String consumerName,
            KafkaMessageMetadata metadata
    ) {
        Optional<ProcessedEvent> existingEvent =
                processedEventRepository
                        .findByEventIdAndConsumerName(
                                event.eventId(),
                                consumerName
                        );

        if (existingEvent.isPresent()) {
            return handleExistingEvent(
                    existingEvent.get(),
                    event,
                    consumerName,
                    metadata
            );
        }

        return createReservation(
                event,
                consumerName,
                metadata
        );
    }

    /**
     * Marks a reserved event as successfully processed.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompleted(
            UUID eventId,
            String consumerName
    ) {
        ProcessedEvent processedEvent =
                findProcessedEvent(
                        eventId,
                        consumerName
                );

        processedEvent.setStatus(
                ProcessedEventStatus.COMPLETED
        );
        processedEvent.setProcessedAt(
                LocalDateTime.now()
        );
        processedEvent.setFailureReason(null);

        processedEventRepository.save(processedEvent);

        log.info(
                "Kafka event processing completed: " +
                        "eventId={}, consumer={}",
                eventId,
                consumerName
        );
    }

    /**
     * Marks processing as failed so that retry-topic delivery can reserve
     * and process the same event again.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(
            UUID eventId,
            String consumerName,
            String failureReason
    ) {
        ProcessedEvent processedEvent =
                findProcessedEvent(
                        eventId,
                        consumerName
                );

        processedEvent.setStatus(
                ProcessedEventStatus.FAILED
        );
        processedEvent.setFailureReason(
                truncateFailureReason(failureReason)
        );

        processedEventRepository.save(processedEvent);

        log.warn(
                "Kafka event processing marked as failed: " +
                        "eventId={}, consumer={}, reason={}",
                eventId,
                consumerName,
                failureReason
        );
    }

    /**
     * Handles an event that already has a reservation record.
     */
    private boolean handleExistingEvent(
            ProcessedEvent processedEvent,
            PortfolioEvent event,
            String consumerName,
            KafkaMessageMetadata metadata
    ) {
        ProcessedEventStatus currentStatus =
                processedEvent.getStatus();

        if (currentStatus == ProcessedEventStatus.COMPLETED) {
            log.info(
                    "Duplicate completed Kafka event ignored: " +
                            "eventId={}, consumer={}",
                    event.eventId(),
                    consumerName
            );

            return false;
        }

        if (currentStatus == ProcessedEventStatus.PROCESSING) {
            log.info(
                    "Kafka event is already being processed: " +
                            "eventId={}, consumer={}",
                    event.eventId(),
                    consumerName
            );

            return false;
        }

        if (currentStatus == ProcessedEventStatus.FAILED) {
            processedEvent.setStatus(
                    ProcessedEventStatus.PROCESSING
            );
            processedEvent.setFailureReason(null);
            processedEvent.setProcessedAt(null);

            updateKafkaMetadata(
                    processedEvent,
                    event,
                    metadata
            );

            processedEventRepository.saveAndFlush(
                    processedEvent
            );

            log.info(
                    "Failed Kafka event reserved for retry: " +
                            "eventId={}, consumer={}",
                    event.eventId(),
                    consumerName
            );

            return true;
        }

        log.warn(
                "Kafka event has unsupported processed status: " +
                        "eventId={}, consumer={}, status={}",
                event.eventId(),
                consumerName,
                currentStatus
        );

        return false;
    }

    /**
     * Creates a new event reservation.
     * <p>
     * The database unique constraint on event_id and consumer_name provides
     * the final protection against concurrent duplicate delivery.
     */
    private boolean createReservation(
            PortfolioEvent event,
            String consumerName,
            KafkaMessageMetadata metadata
    ) {
        try {
            ProcessedEvent processedEvent =
                    ProcessedEvent.builder()
                            .eventId(event.eventId())
                            .eventType(event.eventType())
                            .consumerName(consumerName)
                            .status(
                                    ProcessedEventStatus.PROCESSING
                            )
                            .topicName(metadata.topic())
                            .partitionNumber(metadata.partition())
                            .messageOffset(metadata.offset())
                            .correlationId(
                                    event.correlationId()
                            )
                            .build();

            processedEventRepository.saveAndFlush(
                    processedEvent
            );

            log.info(
                    "Kafka event reserved successfully: " +
                            "eventId={}, consumer={}, " +
                            "topic={}, partition={}, offset={}",
                    event.eventId(),
                    consumerName,
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset()
            );

            return true;

        } catch (DataIntegrityViolationException exception) {
            /*
             * Another consumer thread or application instance inserted
             * the same event reservation concurrently.
             */
            log.info(
                    "Concurrent duplicate Kafka event ignored: " +
                            "eventId={}, consumer={}",
                    event.eventId(),
                    consumerName
            );

            return false;
        }
    }

    private void updateKafkaMetadata(
            ProcessedEvent processedEvent,
            PortfolioEvent event,
            KafkaMessageMetadata metadata
    ) {
        processedEvent.setEventType(
                event.eventType()
        );
        processedEvent.setTopicName(
                metadata.topic()
        );
        processedEvent.setPartitionNumber(
                metadata.partition()
        );
        processedEvent.setMessageOffset(
                metadata.offset()
        );
        processedEvent.setCorrelationId(
                event.correlationId()
        );
    }

    private ProcessedEvent findProcessedEvent(
            UUID eventId,
            String consumerName
    ) {
        return processedEventRepository
                .findByEventIdAndConsumerName(
                        eventId,
                        consumerName
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Processed event not found: "
                                        + "eventId="
                                        + eventId
                                        + ", consumer="
                                        + consumerName
                        )
                );
    }

    private String truncateFailureReason(
            String failureReason
    ) {
        if (failureReason == null
                || failureReason.isBlank()) {
            return "Unknown event-processing failure";
        }

        if (failureReason.length()
                <= MAX_FAILURE_REASON_LENGTH) {
            return failureReason;
        }

        return failureReason.substring(
                0,
                MAX_FAILURE_REASON_LENGTH
        );
    }
}