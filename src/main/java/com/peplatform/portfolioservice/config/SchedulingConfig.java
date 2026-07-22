package com.peplatform.portfolioservice.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * Annotates the class as Spring configuration for scheduling.
 * <p>
 * Configuration: enables scheduling capabilities, typically configured by properties that control how often tasks are executed.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT2M")
public class SchedulingConfig {

    /**
     * Provides the single application-wide ShedLock provider.
     *
     * <p>The lock information is stored in the {@code shedlock} database table.
     * This prevents scheduled jobs from running concurrently when multiple
     * portfolio-service instances are active.</p>
     *
     * @param dataSource application datasource
     * @return JDBC-based ShedLock provider
     */
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        .usingDbTime()
                        .build()
        );
    }
}