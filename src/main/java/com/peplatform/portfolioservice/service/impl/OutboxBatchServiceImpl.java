package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.enums.OutboxStatus;
import com.peplatform.portfolioservice.entity.OutboxEvent;
import com.peplatform.portfolioservice.exception.ResourceNotFoundException;
import com.peplatform.portfolioservice.repository.OutboxEventRepository;
import com.peplatform.portfolioservice.service.api.OutboxBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * **OutboxBatchService**: Outbox Batch Service implementation for handling batch publishing of events.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class OutboxBatchServiceImpl implements OutboxBatchService {

    /**
     * Maximum number of retries before giving up on publishing an event.
     * <p>
     * //     * @see OutboxEventRepository#findPublishableEventsForUpdate(int, int, LocalDateTime, PageRequest)
     * //     * @see InBoxBatchService#markPublished(Long, String)
     * //     * @see InBoxBatchService#markFailed(Long, String)
     */
    private static final int MAX_RETRIES = 5;

    /**
     * Repository providing access to outbox events
     */
    private final OutboxEventRepository outboxEventRepository;


    /**
     * Claims and publishes a batch of outbox events.
     *
     * @param batchSize The size of the.batch to retrieve from the repository.
     * @return A list of {@link OutboxEvent} objects that have been successfully claimed and published.
     */
    @Override
    @Transactional
    public List<OutboxEvent> claimPublishableEvents(int batchSize) {
        List<OutboxEvent> events =
                outboxEventRepository.findPublishableEventsForUpdate(
                        OutboxStatus.PENDING,
                        OutboxStatus.FAILED,
                        LocalDateTime.now(),
                        PageRequest.of(0, batchSize)
                );

        events.forEach(event ->
                event.setStatus(OutboxStatus.PROCESSING)
        );

        return outboxEventRepository.saveAll(events);
    }

    /**
     * Marks the specified `OutboxEvent` as published.
     *
     * @param id The ID of the `OutboxEvent` to mark as published.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPublished(Long id) {
        OutboxEvent event = findEvent(id);

        event.setStatus(OutboxStatus.PUBLISHED);
        event.setPublishedAt(LocalDateTime.now());
        event.setLastError(null);
        event.setNextRetryAt(null);

        outboxEventRepository.save(event);
    }

    /**
     * Claims and publishes a batch of events. This method retrieves publishable events from the repository,
     * processes them, and updates their statuses accordingly. If an event fails more than the maximum number of retries, it is marked as failed.
     *
     * @param id           The ID of the `OutboxEvent` to mark as Failed.
     * @param errorMessage The error message for events to process
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(
            Long id,
            String errorMessage
    ) {
        OutboxEvent event = findEvent(id);

        int retryCount = event.getRetryCount() + 1;
        event.setRetryCount(retryCount);
        event.setLastError(truncate(errorMessage, 4000));

        if (retryCount >= MAX_RETRIES) {
            event.setStatus(OutboxStatus.FAILED);
            event.setNextRetryAt(null);
        } else {
            event.setStatus(OutboxStatus.FAILED);
            event.setNextRetryAt(
                    LocalDateTime.now()
                            .plusSeconds(calculateBackoff(retryCount))
            );
        }

        outboxEventRepository.save(event);
    }


    /**
     * Resets events in the Outbox that are either_processing or pending
     * and have exceeded a 5-minute timeout.
     */
    @Override
    @Transactional
    public void resetStaleProcessingEvents() {
        outboxEventRepository.resetStaleProcessingEvents(
                OutboxStatus.PROCESSING,
                OutboxStatus.PENDING,
                LocalDateTime.now().minusMinutes(5)
        );
    }

    /**
     * Retrieves an Outbox event by its ID.
     *
     * <p>This method finds the Outbox event with the specified ID in the repository and returns it. If the event does not exist, a {@link ResourceNotFoundException} is thrown.
     *
     * @param id The ID of the Outbox event to retrieve
     * @return The Outbox event with the specified ID
     */
    private OutboxEvent findEvent(Long id) {
        return outboxEventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Outbox event not found with id: " + id
                        )
                );
    }

    /**
     * Calculates the backoff time for a given retry count.
     * <p>
     * This calculates the exponential backoff time for a given retry count using a fixed base (2) and scaling factor (5 seconds).
     *
     * @param retryCount The current retry count
     * @return The calculated backoff time in seconds
     */
    private long calculateBackoff(int retryCount) {
        return Math.min(
                300,
                (long) Math.pow(2, retryCount) * 5
        );
    }

    /**
     * Truncates the provided string to a maximum length.
     * If the string is null, it returns a default unknown Kafka publication failure message.
     *
     * @param value         The input string to be truncated.
     * @param maximumLength The maximum length to which the string should be truncated.
     * @return The truncated or default value.
     */
    private String truncate(String value, int maximumLength) {
        if (value == null) {
            return "Unknown Kafka publication failure";
        }

        return value.length() <= maximumLength
                ? value
                : value.substring(0, maximumLength);
    }
}