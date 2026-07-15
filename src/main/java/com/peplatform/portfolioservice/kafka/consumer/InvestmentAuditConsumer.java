package com.peplatform.portfolioservice.kafka.consumer;

import com.peplatform.portfolioservice.kafka.config.KafkaTopics;
import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;
import com.peplatform.portfolioservice.service.api.InvestmentAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvestmentAuditConsumer {

    private final InvestmentAuditService investmentAuditService;

    @RetryableTopic(
            attempts = "4",
            backOff = @BackOff(
                    delay = 2000,
                    multiplier = 2.0,
                    maxDelay = 10000
            ),
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-dlt",
            autoCreateTopics = "true",
            numPartitions = "3",
            replicationFactor = "1"
    )
    @KafkaListener(
            topics = KafkaTopics.INVESTMENT_CREATED,
            groupId = "portfolio-investment-audit-group"
    )
    public void consume(InvestmentCreatedEvent event,
                        ConsumerRecord<String, InvestmentCreatedEvent> record
    ) {
        KafkaMessageMetadata metadata =
                new KafkaMessageMetadata(
                        record.topic(),
                        record.partition(),
                        record.offset()
                );

        investmentAuditService
                .processInvestmentCreatedEvent(
                        event,
                        metadata
                );
    }

    private void processAuditEvent(
            InvestmentCreatedEvent event
    ) {
        // This will later save to an audit table.
        // Throwing an exception here activates retry processing.

        if (event.investmentId() == null) {
            throw new IllegalArgumentException(
                    "Investment ID cannot be null"
            );
        }
    }

    @DltHandler
    public void processDeadLetter(
            InvestmentCreatedEvent event, ConsumerRecord<String, InvestmentCreatedEvent> record
    ) {
        log.error("Investment event moved to DLT: eventId={}, investmentId={}, correlationId={}, " +
                        "topic={}, partition={}, offset={} ",
                event.eventId(),
                event.investmentId(),
                event.correlationId(),
                record.topic(),
                record.partition(),
                record.offset()
        );

        // Later:
        // 1. Save failure into kafka_failed_events.
        // 2. Raise an operational alert.
        // 3. Allow controlled replay.
    }
}