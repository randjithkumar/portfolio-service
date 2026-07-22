package com.peplatform.portfolioservice.kafka.config;

import com.peplatform.portfolioservice.config.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration Class for Kafka Topics**
 * <p>Defines the configuration details for Kafka topics used in the application, including topic names and their
 * associated properties.
 * <p>
 * **@author Randjith**
 *
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.17
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application.kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
public class KafkaTopicConfig {

    private final KafkaProperties properties;

    /**
     * Retrieves the user's name by user ID.
     *
     * <p>Finds the user by the specified user ID and returns the user's name.</p>
     *
     * @return The user's name
     */
    @Bean
    NewTopic investmentCreatedTopic() {
        return buildTopic(KafkaTopics.INVESTMENT_CREATED);
//        return new NewTopic("investment-created", 3, (short) 1);
        /*return TopicBuilder.name(KafkaTopics.INVESTMENT_CREATED)
                .partitions(3)
                .replicas(1)
                .build();*/
    }

    /**
     * Retrieves the latest Fund information.
     *
     * @return The latest fund navigation history.
     */
    @Bean
    NewTopic fundNavUpdatedTopic() {
        return buildTopic(KafkaTopics.FUND_NAV_UPDATED);
//        return new NewTopic("fund-nav-updated", 3, (short) 1);
    }

    /**
     * Creates a new topic for investment status updates.
     *
     * @return {@link NewTopic} representing the newly created invest status change Kafka topic
     */
    @Bean
    NewTopic investmentStatusChangedTopic() {
        return buildTopic(KafkaTopics.INVESTMENT_STATUS_CHANGED);
//        return new NewTopic("investment-status", 3, (short) 1);
        /*return TopicBuilder.name(
                        KafkaTopics.INVESTMENT_STATUS_CHANGED
                )
                .partitions(3)
                .replicas(1)
                .build();*/
    }

    /**
     * Generates a new Kafka topic with customized settings.
     *
     * <p>
     * The method takes a topic name, partition count, and replica factor as input parameters. It then creates a new Kafka topic using the `TopicBuilder` class,
     * configuring the necessary properties such as cleanup policy and retention.ms. This method is useful for dynamically configuring topics based on application
     * requirements.
     *
     * @param name The name of the Kafka topic to be created
     * @return A `NewTopic` object representing the newly created Kafka topic
     */
    private NewTopic buildTopic(String name) {
        return TopicBuilder.name(name)
                .partitions(properties.partitions())
                .replicas(properties.replicationFactor())
                .config("cleanup.policy", "delete")
                .config("retention.ms", "604800000")
                .build();
    }
}