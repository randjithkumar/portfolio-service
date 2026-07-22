package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "kafka_failed_events", indexes = {
        @Index(name = "idx_kafka_failed_status", columnList = "replayed"),
        @Index(name = "idx_kafka_failed_event_id", columnList = "event_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KafkaFailedEvent extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id") private UUID eventId;
    @Column(name = "event_type", length = 100) private String eventType;
    @Column(name = "correlation_id", length = 100) private String correlationId;
    @Column(name = "source_topic", nullable = false, length = 255) private String sourceTopic;
    @Column(name = "source_partition") private Integer sourcePartition;
    @Column(name = "source_offset") private Long sourceOffset;
    @Column(name = "message_key", length = 255) private String messageKey;
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT") private String payload;
    @Column(name = "failure_reason", nullable = false, columnDefinition = "TEXT") private String failureReason;
    @Builder.Default @Column(nullable = false) private boolean replayed = false;
}
