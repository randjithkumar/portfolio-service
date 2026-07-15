package com.peplatform.portfolioservice.kafka.producer;

import com.peplatform.portfolioservice.kafka.config.KafkaTopics;
import com.peplatform.portfolioservice.kafka.event.FundNavUpdatedEvent;
import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.event.InvestmentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * An event listener for handling various portfolio-related events.
 * <p>
 * This class is responsible for listening to investment creation, status changes, and fund navigation updates.
 * It leverages Kafka for asynchronous event publication.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PortfolioKafkaEventListener {

    /**
     * Kafka producer used to publish investment creation, status change, and fund navigation events.
     */
    private final PortfolioKafkaProducer kafkaProducer;

    /**
     * Processes an investment created event by topic.
     *
     * @param event the investment created event to handle
     */
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleInvestmentCreated(
            InvestmentCreatedEvent event
    ) {
        kafkaProducer.send(
                KafkaTopics.INVESTMENT_CREATED,
                event.investmentId().toString(),
                event
        );

        log.debug(
                "Investment-created event submitted after commit: {}",
                event.eventId()
        );
    }

    /**
     * Handles the purchase of an investment.
     * Sends a message to a Kafka topic to notify investors about the change in status.
     *
     * @param event The investment status change event
     */
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleInvestmentStatusChanged(
            InvestmentStatusChangedEvent event
    ) {
        kafkaProducer.send(
                KafkaTopics.INVESTMENT_STATUS_CHANGED,
                event.investmentId().toString(),
                event
        );
    }

    /**
     * Handles a fund navigation update event by sending the updated navigation data to a specified topic using a Kafka producer.
     *
     * @param event The fund navigation update event containing the necessary details such as the fund ID and navigation data.
     */
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleFundNavUpdated(
            FundNavUpdatedEvent event
    ) {
        kafkaProducer.send(
                KafkaTopics.FUND_NAV_UPDATED,
                event.fundId().toString(),
                event
        );
    }
}