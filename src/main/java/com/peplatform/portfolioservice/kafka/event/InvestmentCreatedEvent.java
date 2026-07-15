package com.peplatform.portfolioservice.kafka.event;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * **InvestmentCreatedEvent** represents an event indicating
 * the creation of a new investment in a portfolio, containing all necessary details such as
 * unique ID, type, version, correlation identifiers, event timestamps,
 * investment and investor IDs, codes, fund details, amount, NAV, units, and status.
 * This class provides a structured way to encapsulate and manage the creation process of an investment,
 * adhering to design principles of avoidance of infrastructure details, encapsulation of business rules,
 * object-oriented design, and separation of concerns.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
public record InvestmentCreatedEvent(
        UUID eventId,
        String eventType,
        String eventVersion,
        String correlationId,
        Instant occurredAt,
        Long investmentId,
        Long investorId,
        String investorCode,
        Long fundId,
        String fundCode,
        BigDecimal amount,
        BigDecimal nav,
        BigDecimal units,
        InvestmentStatus status
) implements PortfolioEvent {

    /**
     * Retrieves the user's name by user ID.
     *
     * @param userId The user ID
     * @return The user's name
     */
    public static InvestmentCreatedEvent create(
            String correlationId,
            Long investmentId,
            Long investorId,
            String investorCode,
            Long fundId,
            String fundCode,
            BigDecimal amount,
            BigDecimal nav,
            BigDecimal units,
            InvestmentStatus status
    ) {
        return new InvestmentCreatedEvent(
                UUID.randomUUID(),
                "INVESTMENT_CREATED",
                "1.0",
                correlationId,
                Instant.now(),
                investmentId,
                investorId,
                investorCode,
                fundId,
                fundCode,
                amount,
                nav,
                units,
                status
        );
    }
}