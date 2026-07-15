package com.peplatform.portfolioservice.kafka.producer;

import com.peplatform.portfolioservice.entity.OutboxEvent;
import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import com.peplatform.portfolioservice.kafka.mapper.OutboxEventMapper;
import com.peplatform.portfolioservice.service.api.OutboxBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "application.outbox",
        name = "enabled",
        havingValue = "true"
)
public class OutboxPublisher {

    /**
     * Accessor to the `outboxBatchService` field. This is responsible for handling the batch processing of outgoing events. The `outboxBatchService` instance provides various methods to claim pending events, deserialize them, send them over Kafka, and mark events as processed.
     */
    private final OutboxBatchService outboxBatchService;
    /**
     * Mapper to convert `OutboxEvent` objects into their corresponding `PortfolioEvent` objects, handling serialization issues as needed.
     */
    private final OutboxEventMapper outboxEventMapper;
    /**
     * Producer for sending portfolio Kafka messages to the specified topic.
     */
    private final PortfolioKafkaProducer kafkaProducer;

    /**
     * Maximum number of events to publish in a single batch, defaulting to 50.
     */
    @Value("${application.outbox.batch-size:50}")
    private int batchSize;

    /**
     * The number of seconds to wait for the Kafka producer to send a message before giving up.
     */
    @Value("${application.outbox.send-timeout-seconds:10}")
    private long sendTimeoutSeconds;

    /**
     * Publishes pending outbox events.
     *
     * <p>
     * Finds events from the batch service that are claimable for publication and then sends them to Kafka.
     *
     * @Scheduled has the delay string
     * @ScheduledLock locks the outboxPublisher so only one application instance
     * (for example if there are replicas)
     * can start the schedule and remaining will be skipped and will not be queued.
     * </p>
     *
     */
    @Scheduled(
            fixedDelayString = "${application.outbox.poll-delay-ms:2000}"
    )
    @SchedulerLock(
            name = "outboxPublisher.publishPendingEvents",
            lockAtMostFor = "PT2M",
            lockAtLeastFor = "PT1S"
    )
    public void publishPendingEvents() {
        outboxBatchService.resetStaleProcessingEvents();
        List<OutboxEvent> events =
                outboxBatchService.claimPublishableEvents(batchSize);

        for (OutboxEvent outboxEvent : events) {
            publish(outboxEvent);
        }
    }

    /**
     * Publishes pending events to a Kafka topic.
     *
     * <pre>{@code
     * public void publishPendingEvents(){
     * List<OutboxEvent>events=
     * outboxBatchService.claimPublishableEvents(batchSize);
     * for(OutboxEvent outboxEvent : events){
     * publish(outboxEvent);
     * }
     * }}</pre>
     * In this method:
     * - The `claimPublishableEvents` method is used to retrieve a list of pending events to be published.
     * - Each event is deserialized using the `outboxEventMapper`, then sent to the Kafka topic specified by `outboxEvent.getTopicName()`.
     * - The message key is derived from the serialized form, which can be customized based on the requirement.
     * Once sent, the event is marked as published in the `outboxBatchService` using `outboxBatchService.markPublished(outboxEvent.getId())`.
     * If an error occurs during the publishing process, the event is marked as failed, and an error log is generated.
     * The process repeats until all pending events are successfully published or no more events are available based on the batch size.
     * </pre>
     *
     * @author Your Name
     * @since 1.0
     * @deprecated Since version 2.0, this method should be replaced with improved error handling and logging.
     * <a href="https://github.com/yourusername/yourproject/blob/main/src/main/java/com/example/outbox publishing/publisher.java">Details of the class</a>
     *
     * <pre>{@code
     * @Service("outboxPublisher")
     * public class OutboxPublisher {
     * }}</pre>
     */
    private void publish(OutboxEvent outboxEvent) {
        try {
            PortfolioEvent portfolioEvent =
                    outboxEventMapper.deserialize(outboxEvent);

            SendResult<String, Object> result = kafkaProducer.send(
                            outboxEvent.getTopicName(),
                            outboxEvent.getMessageKey(),
                            portfolioEvent
                    )
                    .get(
                            sendTimeoutSeconds,
                            TimeUnit.SECONDS
                    );

            outboxBatchService.markPublished(
                    outboxEvent.getId()
            );

            log.info(
                    "Outbox event published: eventId={}, topic={}, " +
                            "partition={}, offset={}",
                    outboxEvent.getEventId(),
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset()
            );
        } catch (Exception exception) {
            outboxBatchService.markFailed(
                    outboxEvent.getId(),
                    rootMessage(exception)
            );

            log.error(
                    "Outbox publication failed: eventId={}, topic={}",
                    outboxEvent.getEventId(),
                    outboxEvent.getTopicName(),
                    exception
            );
        }
    }

    /**
     * Retrieves the user's root error message.
     * <p>
     * This method finds the root cause of a {@code Throwable} and returns its message. It is useful for debugging purposes, as it can provide a human-readable description of the exception that occurred.
     * </p>
     *
     * @param throwable An instance of {@code Throwable}.
     * @return The root error message of the exception.
     */
    private String rootMessage(Throwable throwable) {
        Throwable current = throwable;

        while (current.getCause() != null) {
            current = current.getCause();
        }

        return current.getMessage() == null
                ? current.getClass().getSimpleName()
                : current.getMessage();
    }
}