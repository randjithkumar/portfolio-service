package com.peplatform.portfolioservice.kafka.producer;

import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 端口领域事件发布逻辑.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.12
 * @since 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class PortfolioDomainEventPublisher {

    /**
     * An event publisher responsible for managing and dispatching various Domain events published by the PortfolioDomain.
     * <p>
     * This publication mechanism enables applications to respond flexibly to various portfolio changes and user preferences.
     * The Publisher serves as a central point where all domain-related notifications and events are handled accordingly.
     *
     * @see ApplicationEventPublisher
     */
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes a portfolio event.
     *
     * @param event The portfolio event to be published
     */
    public void publish(PortfolioEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}