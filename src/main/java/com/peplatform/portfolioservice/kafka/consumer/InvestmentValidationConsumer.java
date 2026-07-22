package com.peplatform.portfolioservice.kafka.consumer;

import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;
import com.peplatform.portfolioservice.service.api.InvestmentValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * This utility class provides services for validating investment events.
 * It accepts an {@link InvestmentCreatedEvent} and a {@link KafkaMessageMetadata}.
 * It delegates the validation process to the {@link InvestmentValidationService}.
 * The validated investment metadata is logged upon completion, providing a trace of the event flow.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026/07/21 05:50 PM
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InvestmentValidationConsumer {

    private final InvestmentValidationService validationService;

    /**
     * Listens to investment creation events and validates the corresponding investments.
     * <p>Finds the relevant investment using the provided user ID and validates it.
     *
     * @param event  The investment creation event containing information about the investment that needs to be validated.
     * @param record Kafka metadata for the consumed message including topic, partition, and offset.
     */
    @KafkaListener(
            topics = "${application.kafka.topics.investment-created:"
                    + "portfolio.investment.created.v1}",
            groupId = "portfolio-investment-validation-consumer"
    )
    public void consume(
            InvestmentCreatedEvent event,
            ConsumerRecord<String, InvestmentCreatedEvent> record
    ) {
        validationService.validateInvestment(
                event,
                new KafkaMessageMetadata(
                        record.topic(), record.partition(), record.offset()
                )
        );

        log.info(
                "Investment validation completed: eventId={}, investmentId={}",
                event.eventId(),
                event.investmentId()
        );
    }
}