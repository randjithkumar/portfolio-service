package com.peplatform.portfolioservice.exception;


/**
 * Custom exception thrown by {@link com.example.service.UserService}.
 * <p>
 * This exception is used to indicate that a duplicate resource has been found.
 * It wraps the given message and provides additional context about why
 * a duplicate resource occurred, such as the operation attempted or the unique
 * identifier of the existing resource being duplicated.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}