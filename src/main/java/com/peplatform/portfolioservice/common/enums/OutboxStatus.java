package com.peplatform.portfolioservice.common.enums;

/**
 * Enum representing the possible states of an outbound message.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
public enum OutboxStatus {
    PENDING,
    PROCESSING,
    PUBLISHED,
    FAILED,
    DEAD
}