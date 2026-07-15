package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.entity.OutboxEvent;

import java.util.List;

/**
 * [Identify the primary functionality of this interface dynamically based on the input written in English]
 *
 * <p>[Detailed explanation of usage scenarios or responsibilities in English]
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
public interface OutboxBatchService {

    /**
     * Retrieves the list of claimable events from the batch and publishes them.
     *
     * @param batchSize The number of events to claim in a single batch
     * @return A list containing all the claimable events that have been successfully published
     */
    List<OutboxEvent> claimPublishableEvents(int batchSize);

    /**
     * Marks the specified `id` as published in the outbox batch service.
     *
     * @param id The ID of the event to be marked as published
     */
    void markPublished(Long id);

    /**
     * Marks an event as failed with a specific error message.
     *
     * @param id           The ID of the event to be marked failed
     * @param errorMessage The error message indicating why the event was failed
     */
    void markFailed(Long id, String errorMessage);

    /**
     * Resets all stale processing events in the outbox batch service.
     *
     * <p>This method triggers a process to remove any outdated or no longer relevant processing events from the system. It ensures that only actively and relevant events are considered when performing further operations such as publishing claims.
     *
     * @since 0.0.1-SNAPSHOT Added resetStaleProcessingEvents method
     */
    void resetStaleProcessingEvents();
}