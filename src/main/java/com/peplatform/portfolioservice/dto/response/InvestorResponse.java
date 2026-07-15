package com.peplatform.portfolioservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing the response containing Investor information.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class InvestorResponse {

    /**
     * Unique identifier for the investor.
     */
    private Long id;

    /**
     * Unique code identifying the investor.
     */
    private String investorCode;

    /**
     * Full name of the investor.
     */
    private String name;

    /**
     * Contact email address of the investor.
     */
    private String email;

    /**
     * Country of residence of the investor.
     */
    private String country;

    /**
     * Risk profile classification of the investor.
     */
    private String riskProfile;

    /**
     * Total amount committed by the investor.
     */
    private BigDecimal committedAmount;

    /**
     * Timestamp when the investor record was created in the system.
     */
    private LocalDateTime createdAt;
}