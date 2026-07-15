package com.peplatform.portfolioservice.dto.response;

import com.peplatform.portfolioservice.common.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing a JSON Web Token (JWT) response.
 * <p>This class encapsulates the essential information from a JWT response, including access token, token type, and expiration time.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class JwtResponse {

    /**
     * Access token. This field is typically stored in a JWT.
     */
    private String accessToken;

    /**
     * Access token type
     */
    private String tokenType;

    /**
     * Access token validity time, in milliseconds. Default value: 3600000 (1 hour).
     */
    private Long expiresIn;

    /**
     * User's unique identifier or primary key
     */
    private String username;

    /**
     * User role information
     */
    private UserRole role;

}