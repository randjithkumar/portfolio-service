package com.peplatform.portfolioservice.config;

import com.peplatform.portfolioservice.security.filter.JwtAuthenticationFilter;
import com.peplatform.portfolioservice.security.handler.RestAccessDeniedHandler;
import com.peplatform.portfolioservice.security.handler.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration class that defines HTTP security settings for the application.
 * <p>Creates a {@code SecurityFilterChain} bean which disables CSRF protection, permits access to API, Swagger, and actuator endpoints,
 * and requires authentication for all other requests.
 *
 * @author Randjith
 * @version 1.0.0
 * @email mailto:randjithkumar@users.noreply.github.com
 * @date 2026.07.09
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * The JwtAuthenticationFilter instance that is used to authenticate requests.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * The {@link AuthenticationProvider} used to authenticate Spring Security users during login and registration.
     * This provider is responsible for verifying user credentials and creating the appropriate {@code UserDetails}.
     *
     * @see AuthenticationProvider
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * Entry point for handling authentication-related exceptions.
     */
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Access handler for authorization exceptions.
     */
    private final RestAccessDeniedHandler accessDeniedHandler;


    /**
     * Generates a {@code SecurityFilterChain} bean that configures HTTP security settings for the application.
     * The filter chain disables CSRF protection and does not manage sessions. It allows access to the following routes:
     * - /api/auth/login
     * - /api/auth/register
     * - /swagger-ui/**, /swagger-ui.html, /v3/api-docs/**, /actuator/health
     * For all other requests, authentication is required.
     * Payload for /api/auth/login
     * {
     * "username": "portfolio.admin",
     * "password": "AdminPassword@2026"
     * }
     *
     * @param http The HTTP security configuration for the application
     * @return A configured {@code SecurityFilterChain} bean
     * @throws Exception If an exception occurs while setting up the security filter chain
     */
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                                .requestMatchers(
                                        "/api/auth/login",
//                                        "/api/auth/register",  // commented after registering the first admin user. So only admin can create new users.
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/actuator/health"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/auth/register" // after commenting registration from permitAll then securing registration only to hasRole("ADMIN")
                                ).hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/portfolios/**"
                                ).hasAnyRole(
                                        "ADMIN",
                                        "PORTFOLIO_MANAGER",
                                        "ANALYST",
                                        "AUDITOR"
                                )

                                .requestMatchers(
                                        "/api/investors/**",
                                        "/api/funds/**"
                                ).hasAnyRole(
                                        "ADMIN",
                                        "PORTFOLIO_MANAGER"
                                )

                                .requestMatchers(
                                        "/api/investments/**"
                                ).hasAnyRole(
                                        "ADMIN",
                                        "PORTFOLIO_MANAGER",
                                        "OPERATIONS_USER"
                                )

                                .requestMatchers("/actuator/**")
                                .hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                ).exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler))
                .build();
    }
}