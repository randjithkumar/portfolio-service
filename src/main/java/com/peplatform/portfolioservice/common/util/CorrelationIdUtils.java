package com.peplatform.portfolioservice.common.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Provides utility methods for handling correlation IDs.
 * <p>
 * The class ensures that the retrieval of a correlation ID from the MDC (Mapped Diagnostic Context)
 * is thread-safe by acquiring and releasing a lock. When no correlation ID is found in the MDC,
 * it generates a new UUID.
 * </p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026-07-12
 * @since 0.0.1-SNAPSHOT
 */
public final class CorrelationIdUtils {

    /**
     * Correlation ID key used in the MDC context
     */
    public static final String MDC_KEY = "correlationId";

    /**
     * *Access the MDC key for correlation IDs**
     */
    private CorrelationIdUtils() {
    }

    /**
     * Retrieves a unique correlation ID.
     * <p>Finds and returns the current correlation ID stored in the MDC context (Map for Cross-Origin Request Sharing).
     * If no existing correlation ID is found, it generates a new one using UUID and stores it in the MDC for future use.
     *
     * @return A unique correlation ID
     **/
    public static String getOrCreate() {
        String correlationId = MDC.get(MDC_KEY);

        return correlationId == null || correlationId.isBlank()
                ? UUID.randomUUID().toString()
                : correlationId;
    }
}