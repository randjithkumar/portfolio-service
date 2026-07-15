package com.peplatform.portfolioservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the data structure for a fund navigation update request.
 *
 * <p>Object of this class is used to transmit information related to changes in fund NAV,
 * such as addition or subtraction of shares. This class is designed to encapsulate the details
 * needed to perform these operations smoothly through an API call.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class FundNavUpdateRequest {

    /**
     * Access to the financial account's latest quote.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal nav;
}