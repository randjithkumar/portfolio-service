package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.dto.response.PortfolioSummaryResponse;

/**
 * A service providing portfolio handling functionalities.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
public interface PortfolioService {
    /**
     * Retrieves the investor's portfolio summary.
     *
     * <p>Finds the user by the specified user ID and returns the investor's portfolio details.</p>
     *
     * @param investorId The user ID of the investor whose portfolio summary is to be retrieved.
     * @return A `PortfolioSummaryResponse` containing detailed information about the investor's portfolio.
     */
    PortfolioSummaryResponse getInvestorPortfolioSummary(Long investorId);
}
