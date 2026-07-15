package com.peplatform.portfolioservice.common.enums;

/**
 * Enum representing the distinct classes of investment assets within the financial system.
 * Defines the primary asset categories that can be included in a portfolio.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 0.0.1-SNAPSHOT
 */
public enum AssetClass {
    /**
     * Private equity assets.
     */
    PRIVATE_EQUITY,

    /**
     * Real estate assets.
     */
    REAL_ESTATE,

    /**
     * Infrastructure assets.
     */
    INFRASTRUCTURE,

    /**
     * Private debt assets.
     */
    PRIVATE_DEBT,

    /**
     * Hedge fund assets.
     */
    HEDGE_FUND
}