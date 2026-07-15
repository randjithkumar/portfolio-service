package com.peplatform.portfolioservice.observability;

import lombok.RequiredArgsConstructor;
// import org.springframework.boot.actuate.health.Health;
// import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Provides business-service health details in addition to Spring Boot's
 * standard datasource, disk-space and ping indicators.
 */
@Component("portfolioService")
@RequiredArgsConstructor
public class PortfolioHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            Integer databaseProbe = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            boolean databaseValid = connection.isValid(2) && Integer.valueOf(1).equals(databaseProbe);

            if (!databaseValid) {
                return Health.down()
                        .withDetail("service", "portfolio-service")
                        .withDetail("databaseReachable", false)
                        .build();
            }

            return Health.up()
                    .withDetail("service", "portfolio-service")
                    .withDetail("databaseReachable", true)
                    .withDetail("databaseProduct", connection.getMetaData().getDatabaseProductName())
                    .withDetail("databaseVersion", connection.getMetaData().getDatabaseProductVersion())
                    .build();
        } catch (Exception exception) {
            return Health.down(exception)
                    .withDetail("service", "portfolio-service")
                    .withDetail("databaseReachable", false)
                    .build();
        }
    }
}
