package com.peplatform.portfolioservice.dto.response;

import com.peplatform.portfolioservice.common.enums.AssetClass;
import com.peplatform.portfolioservice.common.enums.FundStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing the response containing Fund information.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class FundResponse {

    /**
     * Unique identifier for the fund.
     */
    private Long id;

    /**
     * Unique code identifying the fund.
     */
    private String fundCode;

    /**
     * Official name of the fund.
     */
    private String fundName;

    /**
     * Classification of the fund's assets.
     */
    private AssetClass assetClass;

    /**
     * Currency code used by the fund.
     */
    private String currency;

    /**
     * Current Net Asset Value of the fund.
     */
    private BigDecimal nav;

    /**
     * Date when the fund was established.
     */
    private LocalDate inceptionDate;

    /**
     * Current operational status of the fund.
     */
    private FundStatus status;

    /**
     * Timestamp when the fund record was created in the system.
     */
    private LocalDateTime createdAt;
}