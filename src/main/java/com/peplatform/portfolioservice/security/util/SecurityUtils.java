package com.peplatform.portfolioservice.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>The {@code SecurityUtils} class provides utility methods for managing user credentials and security contexts. It encapsulates the main behavior of handling authentication-related functionalities within the {@code securityutils} module.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
public final class SecurityUtils {

    /**
     * Retrieves the user's name by user ID.
     * <p>Finds the user by the specified user ID and returns the user's name.
     *
     */
    private SecurityUtils() {
    }

    /**
     * Retrieves the current user's username.
     *
     * @return The current user's name
     */
    public static String currentUsername() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "SYSTEM";
        }

        return authentication.getName();
    }
}