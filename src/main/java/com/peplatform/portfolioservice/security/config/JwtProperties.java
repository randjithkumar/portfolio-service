package com.peplatform.portfolioservice.security.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * JWT properties configuration
 * <p>Contains properties required for generating and validating JSON Web Tokens (JWTs).
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Validated
@ConfigurationProperties(prefix = "application.security.jwt")
public record JwtProperties(

        @NotBlank
        String secret,

        @Positive
        long accessTokenExpirationMs
) {
}