package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import com.peplatform.portfolioservice.common.enums.ProcessedEventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "processed_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_processed_events_event_consumer",
                        columnNames = {
                                "event_id",
                                "consumer_name"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_processed_events_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_processed_events_processed_at",
                        columnList = "processed_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "event_id",
            nullable = false
    )
    private UUID eventId;

    @Column(
            name = "event_type",
            nullable = false,
            length = 100
    )
    private String eventType;

    @Column(
            name = "consumer_name",
            nullable = false,
            length = 150
    )
    private String consumerName;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private ProcessedEventStatus status;

    @Column(
            name = "topic_name",
            nullable = false,
            length = 255
    )
    private String topicName;

    @Column(name = "partition_number")
    private Integer partitionNumber;

    @Column(name = "message_offset")
    private Long messageOffset;

    @Column(
            name = "correlation_id",
            length = 100
    )
    private String correlationId;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(
            name = "failure_reason",
            columnDefinition = "TEXT"
    )
    private String failureReason;
}