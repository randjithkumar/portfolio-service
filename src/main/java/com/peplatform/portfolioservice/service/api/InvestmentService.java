package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.dto.request.InvestmentRequest;
import com.peplatform.portfolioservice.dto.request.InvestmentStatusUpdateRequest;
import com.peplatform.portfolioservice.dto.response.InvestmentResponse;

import java.util.List;

/**
 * Service interface for managing investment.
 * Provides methods for creating, retrieving, and listing investments.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 9/7/2026 3:44 pm
 */
public interface InvestmentService {
    /**
     * Creates a new investment based on the given request.
     *
     * @param request The investment request containing details of the investment.
     * @return A response object containing details of the created investment.
     */
    InvestmentResponse createInvestment(InvestmentRequest request);

    /**
     * Creates a new investment based on the given request.
     *
     * @param idempotencyKey For unique idempotencyKey for investment data via kafka
     * @param request        The investment request containing details of the investment.
     * @return A response object containing details of the created investment.
     */
    InvestmentResponse createInvestment(String idempotencyKey, InvestmentRequest request);

    /**
     * Retrieves an investment by its ID.
     *
     * @param id The ID of the investment to retrieve.
     * @return A response object containing details of the retrieved investment.
     */
    InvestmentResponse getInvestmentById(Long id);

    /**
     * Retrieves all investments.
     *
     * @return A list of entities representing all investments.
     */
    List<InvestmentResponse> getAllInvestments();

    /**
     * Retrieves all investments made by a specific investor.
     *
     * @param investorId The ID of the investor whose investments are to be retrieved.
     * @return A list of response objects containing details of the retrieved investments.
     */
    List<InvestmentResponse> getAllInvestmentByInvestorId(Long investorId);

    /**
     * Retrieves all investments for a specific fund.
     *
     * @param fundId The ID of the fund whose investments are to be retrieved.
     * @return A list of response objects containing details of the retrieved investments.
     */
    List<InvestmentResponse> getAllInvestmentByFundId(Long fundId);

    /**
     * Updates the status of an investment.
     * <p>
     * This method takes the ID of the investment to be updated and the new status details as input. It updates the status of the specified investment and returns a response object containing the updated investment details or an error message if the update fails.
     *
     * @param id      The ID of the investment to update
     * @param request The request object containing the status details of the investment
     * @return A response object containing the updated investment details or detailed error information in case of failure
     */
    InvestmentResponse updateInvestmentStatus(Long id, InvestmentStatusUpdateRequest request);
}
