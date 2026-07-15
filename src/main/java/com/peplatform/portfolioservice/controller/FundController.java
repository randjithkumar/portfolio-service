package com.peplatform.portfolioservice.controller;

import com.peplatform.portfolioservice.common.util.ApiResponseBuilder;
import com.peplatform.portfolioservice.dto.request.FundNavUpdateRequest;
import com.peplatform.portfolioservice.dto.request.FundRequest;
import com.peplatform.portfolioservice.dto.request.FundStatusUpdateRequest;
import com.peplatform.portfolioservice.dto.response.ApiResponse;
import com.peplatform.portfolioservice.dto.response.FundResponse;
import com.peplatform.portfolioservice.exception.ResourceNotFoundException;
import com.peplatform.portfolioservice.service.api.FundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service class for managing financial entities, such as funds.
 * <p>Provides methods to perform CRUD operations on Funds, including querying, creating, updating, and deleting them.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@users.noreply.github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/funds")
@RequiredArgsConstructor
public class FundController {

    /**
     * Fund service for handling fund-related operations.
     */
    private final FundService fundService;

    /**
     * Creates a new fund.
     * <p>Creates a new fund in the system and returns the resulting fund's response.
     *
     * @param request The fund creation request object containing the fund details.
     * @return Aresponse indicating the status of the operation (e.g., Created).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<FundResponse>> createFund(@Valid @RequestBody FundRequest request) {

        FundResponse response = fundService.createFund(request);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Fund Created successfully.",
                        response
                )
        );
    }


    /**
     * Retrieves a single fund by its ID.
     *
     * <p>
     * This method takes a long parameter representing the ID of the fund to retrieve. It throws an
     * {@link IllegalArgumentException} if the provided ID is not valid, or throws a
     * {@link ResourceNotFoundException} if no fund with the given ID exists.
     * </p>
     *
     * @param fundId The ID of the fund to retrieve
     * @return A response containing information about the retrieved fund, such as details, status, and navigation data
     */
    @GetMapping("/{fundId}")
    public ResponseEntity<ApiResponse<FundResponse>> getFundById(@PathVariable Long fundId) {
        FundResponse response = fundService.getFundById(fundId);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Fund retrieved successfully.",
                        response
                )
        );
    }

    /**
     * Retrieves a list of all funds.
     *
     * @return A list containing all available funds.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FundResponse>>> getAllFunds() {

        List<FundResponse> response = fundService.getAllFunds();
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Funds retrieved successfully.",
                        response
                ));
    }

    /**
     * Updates a fund by specifying its ID and the updated fund details.
     *
     * @param fundId  The identifier of the fund being updated.
     * @param request The request containing the updated fund information.
     * @return A response indicating the success or failure of the update operation.
     */
    @PutMapping("/{fundId}")
    public ResponseEntity<ApiResponse<FundResponse>> updateFund(@PathVariable Long fundId, @Valid @RequestBody FundRequest request) {
        FundResponse response = fundService.updateFund(fundId, request);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Fund updated successfully.",
                        response
                ));
    }

    /**
     * Updates the navigation settings for a specified fund.
     * <p>Modifies the navigation data associated with the given fund ID.</p>
     *
     * @param fundId  The ID of the fund to update
     * @param request A DTO containing the new navigation settings
     * @return A response indicating the success or failure of the update operation
     */
    @PatchMapping("/{fundId}/nav")
    public ResponseEntity<ApiResponse<FundResponse>> updateFundNav(@PathVariable Long fundId, @Valid @RequestBody FundNavUpdateRequest request) {
        FundResponse response = fundService.updateFundNav(fundId, request);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Fund Patched successfully based on fundId and nav.",
                        response
                ));
    }

    /**
     * Updates the status of a specific fund.
     *
     * <p>Updates the status of a fund with the specified identifier using the provided request. The update includes:
     * - Updating the name and description if new values are provided
     * - Setting the state to either active or inactive, if new values are provided
     *
     * @param fundId  The unique identifier of the fund
     * @param request Request containing new status information
     * @return A response indicating whether the operation was successful, including the updated fund information if applicable
     */
    @PatchMapping("/{fundId}/status")
    public ResponseEntity<ApiResponse<FundResponse>> updateFundStatus(@PathVariable Long fundId, @Valid @RequestBody FundStatusUpdateRequest request) {
        FundResponse response = fundService.updateFundStatus(fundId, request);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Fund Patched successfully based on fundId and Status.",
                        response
                ));
    }

    /**
     * Deletes the specified fund by its identifier.
     * This operation is performed through the {@link FundService}.
     *
     * @param fundId The ID of the fund to be deleted
     * @return
     */
    @DeleteMapping("/{fundId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiResponse<Object>> deleteFund(@PathVariable Long fundId) {

        fundService.deleteFund(fundId);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseBuilder.success(
                        HttpStatus.NO_CONTENT,
                        "Funds deleted successfully.",
                        null // No data to return since it's deleted
                ));
    }
}