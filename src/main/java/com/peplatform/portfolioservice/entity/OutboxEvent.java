package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import com.peplatform.portfolioservice.common.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Java utility class for outbox event database operations.
 * Implements entity layer functionality, providing methods to retrieve and manage outbox events using JPA repositories.
 */
@Entity
@Table(
        name = "outbox_events",
        indexes = {
                @Index(
                        name = "idx_outbox_status_created_at",
                        columnList = "status, created_at"
                ),
                @Index(
                        name = "idx_outbox_aggregate",
                        columnList = "aggregate_type, aggregate_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent extends BaseEntity {

    /** Database identifier for the outbox event record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique identifier for the outbox event.
     */
    @Column(
            name = "event_id",
            nullable = false,
            unique = true
    )
    private UUID eventId;

    /** Aggregate type of the outbox event (e.g., user, order) */
    @Column(
            name = "aggregate_type",
            nullable = false,
            length = 100
    )
    private String aggregateType;

    /** Unique identifier for the outbox event. */
    @Column(
            name = "aggregate_id",
            nullable = false,
            length = 100
    )
    private String aggregateId;

    /**
     * Event type of the outbox event
     */
    @Column(
            name = "event_type",
            nullable = false,
            length = 100
    )
    private String eventType;

    /**
     * Event version identifier
     */
    @Column(
            name = "event_version",
            nullable = false,
            length = 20
    )
    private String eventVersion;

    /**
     * Message topic that this event is published to.
     */
    @Column(
            name = "topic_name",
            nullable = false,
            length = 255
    )
    private String topicName;

    /**
     * Message key for the outbox event
     */
    @Column(
            name = "message_key",
            nullable = false,
            length = 255
    )
    private String messageKey;

    /**
     * Correlation identifier for the outbox event
     */
    @Column(
            name = "correlation_id",
            nullable = false,
            length = 100
    )
    private String correlationId;

    /**
     * The in-memory payload of an outbox event.
     *
     * @see OutboxEvent#setPayload(String) to set the payload.
     */
    @Lob
    @Column(
            name = "payload",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String payload;

    /**
     * Enumerated type representing the status of the outbox event.
     *
     * @see OutboxStatus
     */
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private OutboxStatus status;

    /**
     * The number of times the event has been retried.
     */
    @Column(
            name = "retry_count",
            nullable = false
    )
    private int retryCount;

    /**
     * The next retry time for processing the outbox event.
     */
    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;

    /**
     * The timestamp when the outbox event is last published.
     */
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    /**
     * Describes the field storing the last error message encountered during processing the outbox event.
     */
    @Column(
            name = "last_error",
            columnDefinition = "TEXT"
    )
    private String lastError;
}