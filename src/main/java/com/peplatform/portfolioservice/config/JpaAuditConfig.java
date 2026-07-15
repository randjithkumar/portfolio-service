package com.peplatform.portfolioservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuration class for auditing-related aspects in a Java Spring application.
 *
 * <p>The purpose of this configuration is to provide services and methods that help manage auditing,
 * such as logging user activities or recording the who made changes. The configuration includes beans for
 * auditorAware, an implementation of the AuditorAware interface that retrieves the logged-in user's name.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com)"
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class JpaAuditConfig {

    /**
     * @return The AuditorAware instance configured.
     */
    @Bean
    AuditorAware<String> auditorProvider() {

        return () -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.of("SYSTEM");
            }
            return Optional.of(authentication.getName());
        };

        //Optional.of("SYSTEM");

//        return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}