package com.peplatform.portfolioservice.controller;

import com.peplatform.portfolioservice.common.util.ApiResponseBuilder;
import com.peplatform.portfolioservice.dto.request.InvestorRequest;
import com.peplatform.portfolioservice.dto.response.ApiResponse;
import com.peplatform.portfolioservice.dto.response.InvestorResponse;
import com.peplatform.portfolioservice.service.api.InvestorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for managing investments, facilitating operations like creating new investors, retrieving details
 * of existing ones, and performing updates. This controller adheres to best practices in object-oriented design
 * to prioritize encapsulation of business rules while avoiding unnecessary infrastructure dependencies.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026-07-10
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/investors")
@RequiredArgsConstructor
public class InvestorController {

    /**
     * Service for investor-related business logic.
     */
    private final InvestorService investorService;

    /**
     * Creates a new investor.
     *
     * @param request the investor creation request
     * @return the created investor response
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<InvestorResponse>> createInvestor(@Valid @RequestBody InvestorRequest request) {
        InvestorResponse response = investorService.createInvestor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponseBuilder.success(
                                HttpStatus.CREATED,
                                "Investor created successfully.",
                                response
                        )
                );
    }

    /**
     * Retrieves an investor by their ID.
     *
     * @param id the ID of the investor to retrieve
     * @return the investor response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestorResponse>>
    getInvestorById(@PathVariable Long id) {
        InvestorResponse response = investorService.getInvestorById(id);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investor retrieved successfully.",
                        response
                )
        );
    }

    /**
     * Retrieves all investors.
     *
     * @return a list of all investor responses
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InvestorResponse>>> getAllInvestors() {
        List<InvestorResponse> response = investorService.getAllInvestors();
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investors retrieved successfully.",
                        response
                )
        );

    }

    /**
     * Updates a specific investor by ID.
     *
     * <p>
     * This method takes an existing investor as input and updates its details. The updates are applied using the information provided in the `InvestorRequest`.
     * If no changes are made to any properties of the investor, this method will return the updated investor object as is.
     * </p>
     *
     * @param id      The ID of the investor to update.
     * @param request An instance of `InvestorRequest` containing the new details for the investor.
     * @return A `ResponseEntity` containing a message and an optional `UpdatedInvestor` object.
     * If the update is successful, the status code will be {@link HttpStatus.Series}, and both the message and updated investor will be returned in the response body. If the ID does not exist or any validation errors are encountered, appropriate status codes and error objects will be returned.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestorResponse>> updateInvestor(
            @PathVariable Long id,
            @Valid @RequestBody InvestorRequest request
    ) {
        InvestorResponse response = investorService.updateInvestor(id, request);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investor updated successfully.",
                        response
                ));
    }

    /**
     * Deletes an investor with the specified ID.
     *
     * <p>Performs a deletion operation on the investor identified by the given ID.</p>
     *
     * @param id The ID of the investor to be deleted
     */
    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiResponse<Void>> deleteInvestor(@PathVariable Long id) {
        investorService.deleteInvestor(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseBuilder.success(
                        HttpStatus.NO_CONTENT,
                        "Investor deleted successfully.",
                        null // No data to return since it's deleted
                ));
    }
}