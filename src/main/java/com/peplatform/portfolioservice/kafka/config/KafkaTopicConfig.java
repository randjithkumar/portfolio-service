package com.peplatform.portfolioservice.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * 注册 Kafka 主题配置.
 * <p>配置用于投资创建通知,基金净值更新及回报状态通知.这些主题只适用于内部使用.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Retrieves the user's name by user ID.
     *
     * <p>Finds the user by the specified user ID and returns the user's name.</p>
     *
     * @return The user's name
     */
    @Bean
    NewTopic investmentCreatedTopic() {
//        return new NewTopic("investment-created", 3, (short) 1);
        return TopicBuilder.name(KafkaTopics.INVESTMENT_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Retrieves the latest Fund information.
     *
     * @return The latest fund navigation history.
     */
    @Bean
    NewTopic fundNavUpdatedTopic() {
        return new NewTopic("fund-nav-updated", 3, (short) 1);
    }

    /**
     * Creates a new topic for investment status updates.
     *
     * @return {@link NewTopic} representing the newly created invest status change Kafka topic
     */
    @Bean
    NewTopic investmentStatusChangedTopic() {
//        return new NewTopic("investment-status", 3, (short) 1);
        return TopicBuilder.name(
                        KafkaTopics.INVESTMENT_STATUS_CHANGED
                )
                .partitions(3)
                .replicas(1)
                .build();
    }
}