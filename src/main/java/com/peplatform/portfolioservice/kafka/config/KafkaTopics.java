package com.peplatform.portfolioservice.kafka.config;

/**
 * Defines all Kafka topics used in the portfolio application.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
public final class KafkaTopics {

    /**
     * Kafka topic name for investment creation notifications.
     */
    public static final String INVESTMENT_CREATED =
            "portfolio.investment.created.v1";

    /**
     * Configuration for investing. Stores basic information about user's settings.
     *
     */
    public static final String INVESTMENT_STATUS_CHANGED =
            "portfolio.investment.status-changed.v1";

    /**
     * Access code to the constant value representing the latest version of the daily fund navigation update topic.
     */
    public static final String FUND_NAV_UPDATED =
            "portfolio.fund.nav-updated.v1";

    /**
     * Retrieves the user's name by user ID.
     * Finds the user by the specified user ID and returns the user's name.
     *
     */
    private KafkaTopics() {
    }
}