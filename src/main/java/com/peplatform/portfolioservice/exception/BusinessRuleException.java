package com.peplatform.portfolioservice.exception;

/**
 * Business rule exception class.
 * <p>Extends the {@link RuntimeException} to provide business-related exceptions.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
public class BusinessRuleException extends RuntimeException {

    /**
     * Constructs a new instance of {@link BusinessRuleException}.
     *
     * @param message The specific message describing the exception.
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}