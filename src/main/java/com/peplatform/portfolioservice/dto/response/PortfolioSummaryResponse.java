package com.peplatform.portfolioservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;


/**
 * Provides a summary of an investor's portfolio, encapsulating essential financial metrics and holdings.
 * This DTO is used in API responses to convey investor identification, risk profile, committed and invested amounts,
 * and a list of fund holdings.
 *
 * @author Randjith
 * @version 1.0.0
 * @email mailto:randjithkumar@no-reply@github.com
 * @date 2026.07.10
 * @since 10/7/2026 10:11 am
 */
@Getter
@Builder
public class PortfolioSummaryResponse {
    /**
     * Investor identifier.
     */
    private Long investorId;

    /**
     * Investor code.
     */
    private String investorCode;

    /**
     * Investor name.
     */
    private String investorName;

    /**
     * Risk profile of the investor.
     */
    private String riskProfile;

    /**
     * The committed amount allocated to the investor's portfolio.
     */
    private BigDecimal committedAmount;

    /**
     * The total investment amount in the investor's portfolio.
     */
    private BigDecimal totalInvestmentAmount;

    /**
     * The remaining commitment available for the investor.
     */
    private BigDecimal remainingCommitment;

    /**
     * The current portfolio value of the investor.
     */
    private BigDecimal currentPortfolioValue;

    /**
     * The total unrealized gain or loss in the investor's portfolio.
     */
    private BigDecimal totalUnrealizedGainLoss;

    /**
     * The overall return percentage for the investor.
     */
    private BigDecimal overallReturnPercentage;

    /**
     * Total number of funds in the investor's portfolio.
     */
    private int totalFunds;

    /**
     * List of fund holdings in the investor's portfolio.
     */
    private List<FundHoldingResponse> holdings;
}

