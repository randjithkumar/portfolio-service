package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.enums.ProcessedEventStatus;
import com.peplatform.portfolioservice.entity.InvestmentAuditEvent;
import com.peplatform.portfolioservice.entity.ProcessedEvent;
import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;
import com.peplatform.portfolioservice.repository.InvestmentAuditEventRepository;
import com.peplatform.portfolioservice.repository.ProcessedEventRepository;
import com.peplatform.portfolioservice.service.api.InvestmentAuditService;
import com.peplatform.portfolioservice.service.api.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentAuditServiceImpl implements InvestmentAuditService {

    private static final String CONSUMER_NAME =
            "portfolio-investment-audit-consumer";

    private final ProcessedEventService processedEventService;
    private final InvestmentAuditEventRepository auditEventRepository;

    @Override
    @Transactional
    public void processInvestmentCreatedEvent(InvestmentCreatedEvent event, KafkaMessageMetadata metadata) {
        boolean reserved =
                processedEventService.reserveEvent(
                        event,
                        CONSUMER_NAME,
                        metadata
                );

        if (!reserved) {
            log.info(
                    "Duplicate or currently processing event ignored: " +
                            "eventId={}",
                    event.eventId()
            );

            return;
        }

        try {
            InvestmentAuditEvent auditEvent =
                    InvestmentAuditEvent.builder()
                            .eventId(event.eventId())
                            .eventType(event.eventType())
                            .eventVersion(
                                    event.eventVersion()
                            )
                            .correlationId(
                                    event.correlationId()
                            )
                            .occurredAt(event.occurredAt())
                            .investmentId(
                                    event.investmentId()
                            )
                            .investorId(event.investorId())
                            .investorCode(
                                    event.investorCode()
                            )
                            .fundId(event.fundId())
                            .fundCode(event.fundCode())
                            .amount(event.amount())
                            .nav(event.nav())
                            .units(event.units())
                            .status(event.status())
                            .build();

            auditEventRepository.saveAndFlush(
                    auditEvent
            );

            processedEventService.markCompleted(
                    event.eventId(),
                    CONSUMER_NAME
            );

            log.info(
                    "Investment audit event processed: " +
                            "eventId={}, investmentId={}",
                    event.eventId(),
                    event.investmentId()
            );

        } catch (RuntimeException exception) {
            processedEventService.markFailed(
                    event.eventId(),
                    CONSUMER_NAME,
                    rootMessage(exception)
            );

            throw exception;
        }
    }

    private String rootMessage(
            Throwable throwable
    ) {
        Throwable current = throwable;

        while (current.getCause() != null) {
            current = current.getCause();
        }

        if (current.getMessage() == null
                || current.getMessage().isBlank()) {
            return current.getClass().getSimpleName();
        }

        return current.getMessage();
    }
}

