package com.peplatform.portfolioservice.exception;

/**
 * Exception thrown when a requested resource is not found.
 *
 * @author Randjith
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}