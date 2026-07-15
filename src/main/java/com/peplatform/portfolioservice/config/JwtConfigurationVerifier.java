package com.peplatform.portfolioservice.config;

import com.peplatform.portfolioservice.security.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Fetches the access token expiration duration in milliseconds.
 *
 * @author Randjith
 * @version 1.0.0
 * @return Access token expiration duration in milliseconds.
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@RequiredArgsConstructor
public class JwtConfigurationVerifier implements CommandLineRunner {

    /**
     * Configuration for JWT generation and verification.
     * <p>
     * `JwtProperties` holds various configuration properties that determine how the tokens are generated,
     * verified, and handled.
     */
    private final JwtProperties jwtProperties;

    /**
     * Retrieves and outputs the access token's expiration duration in milliseconds.
     * <p>
     * Retrieves the value of `accessTokenExpirationMs` from the `JwtProperties` bean and prints it out.
     * </p>
     *
     */
    @Override
    public void run(String... args) {
        System.out.println(
                "JWT expiration configured as: "
                        + jwtProperties.accessTokenExpirationMs()
                        + " ms"
        );
    }
}