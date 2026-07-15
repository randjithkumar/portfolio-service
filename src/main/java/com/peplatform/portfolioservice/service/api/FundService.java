package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.dto.request.FundNavUpdateRequest;
import com.peplatform.portfolioservice.dto.request.FundRequest;
import com.peplatform.portfolioservice.dto.request.FundStatusUpdateRequest;
import com.peplatform.portfolioservice.dto.response.FundResponse;

import java.util.List;

/**
 * Service interface for managing Funds.
 * Provides methods for creating, retrieving, and listing Funds.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 9/7/2026 3:44 pm
 */
public interface FundService {


    /**
     * Creates a new fund using the provided request.
     *
     * @param request The fund request containing the necessary information to create the fund.
     * @return The created fund response.
     */
    FundResponse createFund(FundRequest request);

    /**
     * Retrieves a fund by its ID.
     *
     * @param id The ID of the fund to retrieve.
     * @return The fund response.
     */
    FundResponse getFundById(Long id);

    /**
     * Retrieves all funds.
     *
     * @return A list of fund responses.
     */
    List<FundResponse> getAllFunds();

    /**
     * Updates an existing fund.
     *
     * @param id      The ID of the fund to update.
     * @param request The fund request containing the new information for the fund.
     * @return The updated fund response.
     */
    FundResponse updateFund(Long id, FundRequest request);

    /**
     * Updates the navigation information of a fund.
     *
     * @param id      The ID of the fund to update.
     * @param request The update request containing the new navigation details for the fund.
     * @return The updated fund response.
     */
    FundResponse updateFundNav(
            Long id,
            FundNavUpdateRequest request
    );

    /**
     * Updates the status of a fund.
     *
     * @param id      The ID of the fund to update.
     * @param request The fund status update request containing the new status information for the fund.
     * @return The updated fund response.
     */
    FundResponse updateFundStatus(
            Long id,
            FundStatusUpdateRequest request
    );

    /**
     * Deletes a fund.
     *
     * @param id The ID of the fund to delete.
     */
    void deleteFund(Long id);

}