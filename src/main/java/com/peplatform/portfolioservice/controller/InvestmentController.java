package com.peplatform.portfolioservice.controller;

import com.peplatform.portfolioservice.common.util.ApiResponseBuilder;
import com.peplatform.portfolioservice.dto.request.InvestmentRequest;
import com.peplatform.portfolioservice.dto.request.InvestmentStatusUpdateRequest;
import com.peplatform.portfolioservice.dto.response.ApiResponse;
import com.peplatform.portfolioservice.dto.response.InvestmentResponse;
import com.peplatform.portfolioservice.service.api.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Investment service class for managing investments in the system.
 * <p>It provides methods to create, retrieve, update, delete, and list investments based on various criteria.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
public class InvestmentController {
    /**
     * Investment service
     */
    private final InvestmentService investmentService;

    /**
     * Create a new investment.
     *
     * @param request The request containing the investment details.
     * @return The created investment response.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<InvestmentResponse>> createInvestment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                            @Valid @RequestBody InvestmentRequest request) {
        InvestmentResponse response = investmentService.createInvestment(idempotencyKey, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseBuilder.success(
                        HttpStatus.CREATED,
                        "Investment created successfully.",
                        response
                ));
    }

    /**
     * Get an investment by its ID.
     *
     * @param id The ID of the investment to retrieve.
     * @return The retrieved investment response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestmentResponse>> getInvestmentById(@PathVariable Long id) {
        InvestmentResponse response = investmentService.getInvestmentById(id);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investment retrieved successfully.",
                        response
                )
        );
    }

    /**
     * Get all investments.
     *
     * @return A list of all investment responses.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InvestmentResponse>>> getAllInvestments() {

        List<InvestmentResponse> response = investmentService.getAllInvestments();
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investments retrieved successfully.",
                        response
                )
        );
    }

    /**
     * Get investments by investor ID.
     *
     * @param investorId The ID of the investor to retrieve investments for.
     * @return A list of all investment responses for the specified investor.
     */
    @GetMapping("/investor/{investorId}")
    public ResponseEntity<ApiResponse<List<InvestmentResponse>>> getInvestmentsByInvestor(@PathVariable Long investorId) {
        List<InvestmentResponse> response = investmentService.getAllInvestmentByInvestorId(investorId);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investments retrieved successfully based on investorId.",
                        response
                )
        );
    }

    /**
     * Get investments by fund ID.
     *
     * @param fundId The ID of the fund to retrieve investments for.
     * @return A list of all investment responses for the specified fund.
     */
    @GetMapping("/fund/{fundId}")
    public ResponseEntity<ApiResponse<List<InvestmentResponse>>> getInvestmentsByFund(@PathVariable Long fundId) {
        List<InvestmentResponse> response = investmentService.getAllInvestmentByFundId(fundId);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investments retrieved successfully based on fundId.",
                        response
                )
        );
    }

    /**
     * Updates the status of an investment.
     *
     * <p>Finds the investment by the specified ID and updates its status. Currently, this method only supports updating investor's payment status to "PAID" or "PARTIAL_PAID".</p>
     *
     * @param id      The ID of the investment to update.
     * @param request The request containing the new status for the investment.
     * @see InvestmentRequest
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<InvestmentResponse>> updateInvestmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody InvestmentStatusUpdateRequest request
    ) {
        InvestmentResponse response = investmentService.updateInvestmentStatus(id, request);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Investment Patched successfully based on investmentId and Status.",
                        response
                ));
    }
}
