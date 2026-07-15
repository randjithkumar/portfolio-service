package com.peplatform.portfolioservice.kafka.producer;

import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortfolioKafkaProducer {

    /**
     * The Kafka template used for sending messages to the portfolio Kafka topic.
     */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Sends a {@link PortfolioEvent} to the specified Kafka topic.
     *
     * <p>Finds the user by the provided user ID using the `userService`, retrieves their name, and constructs an event containing all required metadata. The event is then sent through the configured Kafka template with appropriate headers set for message routing.
     *
     * @param topic the Kafka topic to which the event will be sent
     * @param key   a unique identifier for the event (optional)
     * @param event the event to be sent, must not be {@code null}
     */
    public CompletableFuture<SendResult<String, Object>> send(String topic, String key, PortfolioEvent event) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, key, event);

        addHeader(record, "eventId", event.eventId().toString());
        addHeader(record, "eventType", event.eventType());
        addHeader(record, "eventVersion", event.eventVersion());
        addHeader(record, "correlationId", event.correlationId());

        return kafkaTemplate.send(record);
//                .thenAccept(result ->
//                        log.info(
//                                "Kafka event published: " +
//                                        "topic={}, partition={}, offset={}, " +
//                                        "key={}, eventId={}",
//                                result.getRecordMetadata().topic(),
//                                result.getRecordMetadata().partition(),
//                                result.getRecordMetadata().offset(),
//                                key,
//                                event.eventId()
//                        )
//                )
//                .exceptionally(exception -> {
//                    log.error(
//                            "Kafka event publication failed: topic={}, key={}, eventId={}",
//                            topic, key, event.eventId(), exception);
//
//                    throw new KafkaPublicationException(
//                            "Unable to publish event "
//                                    + event.eventId(),
//                            exception
//                    );
//                });
    }

    /**
     * Adds a header to the Kafka producer record.
     *
     * @param record The producer record instance
     * @param name   The name of the header field
     * @param value  The value of the header field
     */
    private void addHeader(ProducerRecord<String, Object> record, String name, String value) {
        record.headers().add(
                new RecordHeader(name, value.getBytes(StandardCharsets.UTF_8)
                )
        );
    }
}