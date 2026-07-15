package com.peplatform.portfolioservice.dto.response;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing the response containing Investment information.
 *
 * @author Randjith
 * @version 1.0.0
 * @email
 * @date 2026.07.09
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class InvestmentResponse {
    /**
     * The unique identifier for the investment record.
     */
    private Long id;

    /**
     * Key supplied by the caller to prevent duplicate investment creation.
     */
    private String idempotencyKey;

    /**
     * JPA optimistic-lock version returned to clients for diagnostics.
     */
    private Long version;

    /**
     * The investor's unique identifier.
     */
    private Long investorId;

    /**
     * The investor's code.
     */
    private String investorCode;

    /**
     * The investor's name.
     */
    private String investorName;

    /**
     * The fund's unique identifier.
     */
    private Long fundId;

    /**
     * The fund's code.
     */
    private String fundCode;

    /**
     * The fund's name.
     */
    private String fundName;

    /**
     * The amount invested in the fund.
     */
    private BigDecimal amount;

    /**
     * The net asset value (NAV) of the investment.
     */
    private BigDecimal nav;

    /**
     * The number of units held by the investor in the fund.
     */
    private BigDecimal units;

    /**
     * The status of the investment.
     */
    private InvestmentStatus status;

    /**
     * The date when the investment was made.
     */
    private LocalDateTime investmentDate;

    /**
     * The date and time when the investment record was created.
     */
    private LocalDateTime createdAt;
}

