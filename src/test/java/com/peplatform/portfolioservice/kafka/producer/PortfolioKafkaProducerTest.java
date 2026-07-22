package com.peplatform.portfolioservice.kafka.producer;

import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;

/**
 * * portfolioKafkaProducerTest.java
 * - Class responsible for handling Kafka topic publishing in the `portfolio.kafkaproducertest` module.
 * <p>Handles requests and validates input before sending messages to Kafka topics. The class is designed to avoid interacting with infrastructure-level details, adhering to a pure object-oriented approach without explicit request-handling responsibilities.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.22
 * @since 0.0.1-SNAPSHOT
 */
class PortfolioKafkaProducerTest {

    /**
     * Kafka producer for portfolio-related messages.
     *
     * @see // PortfolioKafkaProducerFactory for instance creation
     * @see PortfolioEvent for event structure
     */
    private final PortfolioKafkaProducer producer =
            new PortfolioKafkaProducer(mock(KafkaTemplate.class), new SimpleMeterRegistry());

    /**
     * Tests the functionality to reject a blank topic.
     * <p>
     * Test scenario: When the provided topic is an empty string or null.
     * Expected result: An IllegalArgumentException should be thrown with a message indicating that the topic must not be blank.
     */
    @Test
    void shouldRejectBlankTopic() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> producer.send(" ", "key", mock(PortfolioEvent.class)))
                .withMessageContaining("topic");
    }

    /**
     * Tests the behavior of the `send` method when it receives a null event.
     * <p>
     * Test scenario:
     * 1. Create an instance of {@link PortfolioKafkaProducer} with mocks for `KafkaTemplate` and `SimpleMeterRegistry`.
     * 2. Call the `send` method with an invalid topic name (" ") and null event.
     * 3. Expect an IllegalArgumentException to be thrown if the given event is null.
     */
    @Test
    void shouldRejectNullEvent() {
        assertThatNullPointerException()
                .isThrownBy(() -> producer.send("portfolio.test", "key", null))
                .withMessageContaining("event");
    }
}
