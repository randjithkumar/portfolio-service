package com.peplatform.portfolioservice.dto.request;

import com.peplatform.portfolioservice.common.enums.AssetClass;
import com.peplatform.portfolioservice.common.enums.FundStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object representing a request to create or update a Fund.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class FundRequest {

    /**
     * Unique code identifying the fund.
     */
    @NotBlank
    private String fundCode;

    /**
     * Official name of the fund.
     */
    @NotBlank
    private String fundName;

    /**
     * Classification of the fund's assets.
     */
    @NotNull
    private AssetClass assetClass;

    /**
     * Three-letter currency code (e.g., USD, EUR).
     */
    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;

    /**
     * Net Asset Value of the fund. Must be greater than zero.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal nav;

    /**
     * Date when the fund was established.
     */
    @NotNull
    private LocalDate inceptionDate;

    /**
     * Current operational status of the fund.
     */
    @NotNull
    private FundStatus status;
}