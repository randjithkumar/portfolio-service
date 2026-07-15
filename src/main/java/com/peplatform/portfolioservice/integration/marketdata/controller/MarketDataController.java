package com.peplatform.portfolioservice.integration.marketdata.controller;

import com.peplatform.portfolioservice.common.util.ApiResponseBuilder;
import com.peplatform.portfolioservice.dto.response.ApiResponse;
import com.peplatform.portfolioservice.integration.marketdata.dto.MarketPriceResponse;
import com.peplatform.portfolioservice.integration.marketdata.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/market-data")
@RequiredArgsConstructor
@Tag(
        name = "Market Data",
        description = "External financial market-data integration"
)
@SecurityRequirement(name = "bearerAuth")
public class MarketDataController {

    private final MarketDataService marketDataService;

    @GetMapping("/prices/{symbol}")
    @Operation(
            summary = "Get the latest market price",
            description =
                    "Retrieves the latest quote from Financial Modeling Prep. "
                            + "The integration is protected by Retry and "
                            + "Circuit Breaker policies."
    )
    public ResponseEntity<ApiResponse<MarketPriceResponse>> getLatestPrice(
            @Parameter(
                    description = "Market ticker symbol",
                    example = "AAPL",
                    required = true
            )
            @PathVariable String symbol
    ) {
        MarketPriceResponse response =
                marketDataService.getLatestPrice(symbol);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Latest market price retrieved",
                        response
                )
        );
    }
}