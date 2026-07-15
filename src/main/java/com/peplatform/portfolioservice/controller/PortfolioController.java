package com.peplatform.portfolioservice.controller;


import com.peplatform.portfolioservice.common.util.ApiResponseBuilder;
import com.peplatform.portfolioservice.dto.response.ApiResponse;
import com.peplatform.portfolioservice.dto.response.InvestorResponse;
import com.peplatform.portfolioservice.dto.response.PortfolioSummaryResponse;
import com.peplatform.portfolioservice.service.api.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller is responsible for handling operations related to user portfolios, including querying and managing investor portfolios.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/portfolios")   // /api/portfolio/investor/{investorId}/summary
@RequiredArgsConstructor
public class PortfolioController {

    /**
     * Service for managing user portfolios, including querying and updating investor portfolios.
     *
     * @see PortfolioService
     */
    private final PortfolioService portfolioService;


    /**
     * Retrieves the summary of an investor's portfolio.
     *
     * @param investorId The ID of the investor whose portfolio is to be retrieved.
     * @return A `PortfolioSummaryResponse` object representing the investor's portfolio.
     */
    @GetMapping("/investor/{investorId}/summary")
    public ResponseEntity<ApiResponse<PortfolioSummaryResponse>> getInvestorPortfolioSummary(@PathVariable Long investorId) {

        PortfolioSummaryResponse response = portfolioService.getInvestorPortfolioSummary(investorId);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Portfolio Summary retrieved successfully.",
                        response
                )
        );
    }
}
