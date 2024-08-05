package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import static com.food.ordering.system.domain.DomainConstants.UTC;
import static com.food.ordering.system.saga.order.SagaConstant.ORDER_SAGA_NAME;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.exception.PaymentDomainException;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.output.repository.OrderOutboxRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderOutboxHelper {
    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    // create constructcor
    public OrderOutboxHelper(OrderOutboxRepository orderOutboxRepository) {
        this.orderOutboxRepository = orderOutboxRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID sagaId,
            PaymentStatus paymentStatus) {
        return orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(ORDER_SAGA_NAME, sagaId,
                paymentStatus, OutboxStatus.COMPLETED);

    }

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessgeByOutboxStatus(OutboxStatus status) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, status);
    }

    @Transactional
    public void updateOrderOutboxStatus(OrderOutboxMessage message, OutboxStatus status) {
        message.setOutboxStatus(status);
        save(message);
        log.info("Order outbox table status is updated as: {}", status.name());
    }

    @Transactional
    public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus status) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(ORDER_SAGA_NAME, status);
    }

    @Transactional
    public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload, PaymentStatus paymentStatus,
            OutboxStatus outboxStatus, UUID sagaId) {
        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .type(ORDER_SAGA_NAME)
                .payload(createPayLoad(orderEventPayload))
                .paymentStatus(paymentStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private void save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderOutbox");
            throw new PaymentDomainException("Could not save OrderOutbox");
        }
        log.info("Saved OrderOutboxMessage with orrder id: {}", orderOutboxMessage.getId());

    }

    private String createPayLoad(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrdeEventPayload json", e);
            throw new PaymentDomainException("Could not create OrdeEventPayload json", e);
        }
    }

    @Transactional
    public void updateOutboxMessage(OrderOutboxMessage orderOutboxMessage, OutboxStatus outboxStatus) {
        orderOutboxMessage.setOutboxStatus(outboxStatus);
        save(orderOutboxMessage);
        log.info("Order outbox table is updating as : {}", outboxStatus.name());
    }
}
