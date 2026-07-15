package com.peplatform.portfolioservice.common.enums;

/**
 * Enum representing the status of a processed event.
 *
 * <p>This enum indicates the state of the processing of an event. It is used in business logic to track the progress and outcome
 * of the event handling process. The available states include PROCESSING, COMPLETED, and FAILED.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026-07-13
 * @since 0.0.1-SNAPSHOT
 */
public enum ProcessedEventStatus {
    PROCESSING,
    COMPLETED,
    FAILED
}