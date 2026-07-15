package com.peplatform.portfolioservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Represents a fund's holding details as a response object.
 *
 * <p>This class encapsulates various financial metrics for a specific fund, including
 * its basic identifiers (fund ID, code, name, currency), investment amounts, unit information,
 * and performance indicators such as Net Asset Value (NAV), current value, unrealized gain/loss,
 * and return percentage.
 *
 * <p>It is typically used in fund management and reporting systems to convey the current
 * holding status of a fund to clients or internal processes. The class is annotated with
 * {@code @Getter} and {@code @Builder} to provide convenient access and immutable construction.
 *
 * <pre>{@code
 * @Getter
 * @Builder
 * }</pre>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 10/7/2026 10:18 am
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class FundHoldingResponse {

    /**
     * The unique identifier of the fund.
     */
    private Long fundId;

    /**
     * A code representing the fund.
     */
    private String fundCode;

    /**
     * The name of the fund.
     */
    private String fundName;

    /**
     * The currency in which the fund's units are denominated.
     */
    private String currency;

    /**
     * The total amount invested in the fund.
     */
    private BigDecimal investedAmount;

    /**
     * The total number of units held by the fund.
     */
    private BigDecimal totalUnits;

    /**
     * The average purchase Net Asset Value (NAV) for the fund's units.
     */
    private BigDecimal averagePurchaseNav;

    /**
     * The current Net Asset Value (NAV) for the fund's units.
     */
    private BigDecimal currentNav;

    /**
     * The current value of the fund's holdings based on its current price.
     */
    private BigDecimal currentValue;

    /**
     * The unrealized gain or loss in the fund's holdings.
     */
    private BigDecimal unrealizedGainLoss;

    /**
     * The return percentage achieved by the fund over a period.
     */
    private BigDecimal returnPercentage;
}
