package com.peplatform.portfolioservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Randjithkumar
 * @since 9/7/2026 3:47 pm
 * DTO used to represent an investment request.
 */
@Getter
@Setter
public class InvestmentRequest {
    /**
     * The ID of the investor making the investment.
     */
    @NotNull
    private Long investorId;

    /**
     * The ID of the fund in which the investment is made.
     */
    @NotNull
    private Long fundId;

    /**
     * The amount invested in the fund, must be a positive decimal value.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}
