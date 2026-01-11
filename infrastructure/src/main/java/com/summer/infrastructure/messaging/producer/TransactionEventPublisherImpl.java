package com.summer.infrastructure.messaging.producer;

import com.summer.domain.event.TransactionEventPublisher;
import com.summer.domain.model.TransactionInfo;
import com.summer.infrastructure.messaging.dto.TransactionEventDTO;
import com.summer.infrastructure.messaging.mapper.TransactionEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka adapter implementation for publishing transaction events.
 *
 * This is the IMPLEMENTATION (Adapter) of the domain port in infrastructure layer.
 * Uses Hexagonal Architecture pattern.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventPublisherImpl implements TransactionEventPublisher {

    private final KafkaTemplate<String, TransactionEventDTO> kafkaTemplate;
    private final TransactionEventMapper eventMapper;

    @Value("${kafka.topic.transaction-created:transaction-created}")
    private String transactionCreatedTopic;

    @Override
    public void publishTransactionCreated(TransactionInfo transactionInfo) {
        try {
            TransactionEventDTO eventDTO = eventMapper.toEventDTO(transactionInfo);
            String key = transactionInfo.getId().toString();

            log.info("Publishing transaction created event: id={}, topic={}",
                    transactionInfo.getId(), transactionCreatedTopic);

            CompletableFuture<SendResult<String, TransactionEventDTO>> future =
                    kafkaTemplate.send(transactionCreatedTopic, key, eventDTO);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully published transaction event: id={}, partition={}, offset={}",
                            transactionInfo.getId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish transaction event: id={}",
                            transactionInfo.getId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing transaction created event: id={}",
                    transactionInfo.getId(), e);
            // Don't throw exception to avoid rollback of the save operation
            // Event publishing is considered a non-critical operation
        }
    }
}

