package com.peplatform.portfolioservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application-level Kafka settings used by topic creation, retry/DLT handling,
 * and consumer concurrency.
 */
@ConfigurationProperties(prefix = "application.kafka")
public record KafkaProperties(
        boolean enabled,
        int partitions,
        short replicationFactor,
        int consumerConcurrency,
        Retry retry
) {
    public KafkaProperties {
        partitions = partitions <= 0 ? 3 : partitions;
        replicationFactor = replicationFactor <= 0 ? 1 : replicationFactor;
        consumerConcurrency = consumerConcurrency <= 0 ? 3 : consumerConcurrency;
        retry = retry == null ? new Retry(4, 2000L, 2.0, 10000L) : retry;
    }

    public record Retry(int attempts, long delayMs, double multiplier, long maxDelayMs) {
        public Retry {
            attempts = attempts <= 0 ? 4 : attempts;
            delayMs = delayMs <= 0 ? 2000L : delayMs;
            multiplier = multiplier < 1.0 ? 2.0 : multiplier;
            maxDelayMs = maxDelayMs < delayMs ? 10000L : maxDelayMs;
        }
    }
}
