package com.peplatform.portfolioservice.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Configuration class for managing Flyway database repairs.</p>
 *
 * <p>This configuration class defines a Spring Bean named `repairFlyway` that runs
 * an executable CommandLineRunner when the application context starts. It uses
 * the {@link Flyway} bean to repair any pending database migrations
 * or inconsistencies.</p>
 *
 * @author Randjithkumar
 * @version 1.0-SNAPSHOT
 * @Configuration - Marks this class as a Spring configuration component.
 * @Bean - Declares `repairFlyway` as a single-instance Bean that can be injected into other beans.
 * CommandLineRunner is used by Spring Boot apps to run multiple command-line operations at application startup.
 * Flyway repair functionality is provided using the {@link Flyway} bean,
 * which interacts with the database to ensure it is up-to-date and consistent.</p>
 *
 * <p>This class adheres to best practices in dependency injection within a
 * Spring Boot context, ensuring that Flyway's repair operation is handled efficiently.</p>
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.14
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@Configuration
public class FlywayRepairConfig {


    /**
     * @param flyway The flyway object to repair.
     * @return The flyway object after repair.
     */
    @Bean
    @ConditionalOnProperty(
            name = "app.flyway.repair-enabled",
            havingValue = "true"
    )
    ApplicationRunner flywayRepairRunner(Flyway flyway) {
        return args -> {
            log.warn("Flyway repair is enabled.");

            var result = flyway.repair();

            log.info(
                    "Flyway repair completed. Removed failed migrations: {}, "
                            + "aligned migrations: {}, deleted migrations: {}",
                    result.migrationsRemoved.size(),
                    result.migrationsAligned.size(),
                    result.migrationsDeleted.size()
            );
        };
    }
}