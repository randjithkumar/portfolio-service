package com.peplatform.portfolioservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing a request to create or update an Investor.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class InvestorRequest {

    /**
     * Unique code identifying the investor.
     */
    @NotBlank
    private String investorCode;

    /**
     * Full name of the investor.
     */
    @NotBlank
    private String name;

    /**
     * Contact email address of the investor.
     */
    @Email
    @NotBlank
    private String email;

    /**
     * Country of residence of the investor.
     */
    @NotBlank
    private String country;

    /**
     * Risk profile classification of the investor.
     */
    @NotBlank
    private String riskProfile;

    /**
     * Capital amount committed by the investor. Must be greater than zero.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal committedAmount;
}