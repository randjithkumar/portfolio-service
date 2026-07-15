package com.peplatform.portfolioservice.kafka.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A custom event class representing significant changes in a fund's navigation.
 * <p>
 * This record type encapsulates all the necessary data about a fund nav update, including details such as correlation ID, fund ID, code, previous and updated Navs, currency, changed by user.
 * </p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
public record FundNavUpdatedEvent(
        UUID eventId,
        String eventType,
        String eventVersion,
        String correlationId,
        Instant occurredAt,
        Long fundId,
        String fundCode,
        BigDecimal previousNav,
        BigDecimal updatedNav,
        String currency,
        String changedBy
) implements PortfolioEvent {

    /**
     * This factory method creates a <code>FundNavUpdatedEvent</code> instance.
     *
     * @param correlationId The unique ID for the event related to this fund nav update in the portfolio system.
     * @param fundId        The ID of the organization's managed asset.
     * @param fundCode      The 3-8 character identification code assigned by the fund manager to identify a particular fund.
     * @param previousNav   The historical value of a security or an index, derived from the previous price changes.
     * @param updatedNav    The current value of a security or an index, derived from the most recent price changes.
     * @param currency      The currency in which the fund is invested. For example "USD".
     * @param changedBy     The user who made the record.
     * @return A new <code>FundNavUpdatedEvent</code> object initialized with the provided parameters.
     */
    public static FundNavUpdatedEvent create(
            String correlationId,
            Long fundId,
            String fundCode,
            BigDecimal previousNav,
            BigDecimal updatedNav,
            String currency,
            String changedBy
    ) {
        return new FundNavUpdatedEvent(
                UUID.randomUUID(),
                "FUND_NAV_UPDATED",
                "1.0",
                correlationId,
                Instant.now(),
                fundId,
                fundCode,
                previousNav,
                updatedNav,
                currency,
                changedBy
        );
    }
}