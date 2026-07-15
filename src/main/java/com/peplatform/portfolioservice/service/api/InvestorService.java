package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.dto.request.InvestorRequest;
import com.peplatform.portfolioservice.dto.response.InvestorResponse;

import java.util.List;

/**
 * Service interface for managing investors.
 * Provides methods for creating, retrieving, and listing investors.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@users.noreply.github.com"
 * @since 8/7/2026 4:59 pm
 */
public interface InvestorService {

    /**
     * Creates a new investor.
     *
     * @param request the investor creation request
     * @return the created investor response
     */
    InvestorResponse createInvestor(InvestorRequest request);

    /**
     * Retrieves an investor by their unique identifier.
     *
     * @param id the unique identifier of the investor
     * @return the investor response
     */
    InvestorResponse getInvestorById(Long id);

    /**
     * Retrieves all available investors.
     *
     * @return a list of all investor responses
     */
    List<InvestorResponse> getAllInvestors();

    /**
     * Updates an existing investor.
     *
     * <p>Updates the details of an existing investor based on the provided `investorRequest`.
     * The method returns the updated investor response.
     *
     * @param id              The unique identifier of the investor to update.
     * @param investorRequest The update request for the investor.
     * @return InvestorResponse The updated investor response.
     * This exception is caught and converted into a `RestException` with a status
     * code of 400 (Bad Request).
     */
    InvestorResponse updateInvestor(Long id, InvestorRequest investorRequest);

    /**
     * Deletes an existing investor by their unique identifier.
     *
     * @param id the unique identifier of the investor
     */
    void deleteInvestor(Long id);
}