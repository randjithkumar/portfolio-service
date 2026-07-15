package com.peplatform.portfolioservice.common.enums;

/**
 * Enum class representing different user roles within the application.
 * <p>Each value in the enum represents a specific role that users can have access to, fulfilling different responsibilities.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@github.com"
 * @date 2026.07.11
 * @see UserRole#ADMIN
 * @see UserRole#PORTFOLIO_MANAGER
 * @see UserRole#OPERATIONS_USER
 * @see UserRole#ANALYST
 * @see UserRole#AUDITOR
 * @since 0.0.1-SNAPSHOT
 */
public enum UserRole {

    /**
     * Enum representing different user roles
     */
    ADMIN,

    /**
     * User role for portfolio management.
     */
    PORTFOLIO_MANAGER,

    /**
     * Enum representing different user roles within an application.
     *
     * @see UserRole
     */
    OPERATIONS_USER,

    /**
     * Represents the level of access for a user.
     *
     */
    ANALYST,

    /**
     * Role of an administrator user
     */
    AUDITOR

}