package com.food.ordering.system.kafka.producer;

import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.outbox.OutboxStatus;

@Slf4j
@Component
public class KafkaMessageHelper {
    private final ObjectMapper objectMapper;

    // generate constructor
    public KafkaMessageHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unused")
    public <T> T getOrderEventPayload(String payload,
            Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e) {
            log.error("Could not read object !", outputType.getName(), e);
            throw new OrderDomainException("Could not read " + outputType.getName() + " object !", e);
        }
    }

    public <T, U> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String responseTopicName,
            T avroModel, U outboxMessage,
            BiConsumer<U, OutboxStatus> outboxCallBack,
            String orderId, String avroModelName) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending {} with message: {} and outbox type: {} to topic {}",
                        avroModelName, avroModel.toString(), outboxMessage.getClass().getName(), ex);
                outboxCallBack.accept(outboxMessage, OutboxStatus.FAILED);
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {}" +
                        " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());
                outboxCallBack.accept(outboxMessage, OutboxStatus.COMPLETED);
            }
        };
    }
}
