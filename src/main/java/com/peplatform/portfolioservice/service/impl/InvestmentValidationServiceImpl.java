package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.entity.Investment;
import com.peplatform.portfolioservice.exception.ResourceNotFoundException;
import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.event.InvestmentStatusChangedEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;
import com.peplatform.portfolioservice.kafka.config.KafkaTopics;
import com.peplatform.portfolioservice.repository.InvestmentRepository;
import com.peplatform.portfolioservice.service.api.InvestmentValidationService;
import com.peplatform.portfolioservice.service.api.OutboxService;
import com.peplatform.portfolioservice.service.validator.InvestmentStatusTransitionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for investment validation.
 * <p>
 * This service orchestrates the validation of investment details such as amount, NAV,
 * and units. It ensures the data integrity of investments by enforcing business rules
 * and updating the status accordingly. If any validation fails, it transitions the
 * investment to a 'FAILED' status and retries once more on a secondary channel.
 * The service interacts with the database through an InvestmentRepository to store and retrieve
 * investment records.
 */
@Service
@RequiredArgsConstructor
public class InvestmentValidationServiceImpl implements InvestmentValidationService {

    /**
     * Repository of investments, responsible for managing investment data.
     */
    private final InvestmentRepository investmentRepository;
    /**
     * Investment status transition validator
     */
    private final InvestmentStatusTransitionValidator transitionValidator;
    /**
     * Instance of the OutboxService for processing event queues.
     */
    private final OutboxService outboxService;

    /**
     * Validates an investment.
     * <p>
     * Finds the investment by the specified user ID, performs business validation,
     * updates the investment status, and saves it to the database.
     *
     * @param event    the investment creation event
     * @param metadata the Kafka message metadata
     */
    @Override
    @Transactional
    public void validateInvestment(
            InvestmentCreatedEvent event,
            KafkaMessageMetadata metadata
    ) {
        Investment investment = investmentRepository
                .findById(event.investmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Investment not found with id: "
                                + event.investmentId()
                ));

        if (investment.getStatus() != InvestmentStatus.PENDING) {
            return;
        }

        InvestmentStatus previousStatus = investment.getStatus();

        try {
            performBusinessValidation(investment);

            transitionValidator.validate(
                    previousStatus,
                    InvestmentStatus.VALIDATED
            );

            investment.setStatus(InvestmentStatus.VALIDATED);

        } catch (RuntimeException exception) {
            transitionValidator.validate(
                    previousStatus,
                    InvestmentStatus.FAILED
            );

            investment.setStatus(InvestmentStatus.FAILED);
        }

        Investment saved = investmentRepository.save(investment);

        InvestmentStatusChangedEvent statusEvent =
                InvestmentStatusChangedEvent.create(
                        event.correlationId(),
                        saved.getId(),
                        saved.getInvestor().getId(),
                        saved.getFund().getId(),
                        previousStatus,
                        saved.getStatus(),
                        "kafka-validation-consumer"
                );

        outboxService.saveEvent(
                "INVESTMENT",
                saved.getId().toString(),
                KafkaTopics.INVESTMENT_STATUS_CHANGED,
                saved.getId().toString(),
                statusEvent
        );
    }

    /**
     * Validates the business rules for an investment.
     *
     * @param investment The investment instance to validate
     */
    private void performBusinessValidation(Investment investment) {
        if (investment.getAmount() == null
                || investment.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Investment amount must be greater than zero"
            );
        }

        if (investment.getNav() == null
                || investment.getNav().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Investment NAV must be greater than zero"
            );
        }

        if (investment.getUnits() == null
                || investment.getUnits().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Investment units must be greater than zero"
            );
        }
    }
}