package com.peplatform.portfolioservice.common.enums;

/**
 * Represents the possible operational statuses of a fund in the system.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 0.0.1-SNAPSHOT
 */
public enum FundStatus {
    /**
     * Indicates that the fund is currently active and operational.
     */
    ACTIVE,

    /**
     * Indicates that the fund is closed and no longer operational.
     */
    CLOSED,

    /**
     * Indicates that the fund is temporarily suspended.
     */
    SUSPENDED
}