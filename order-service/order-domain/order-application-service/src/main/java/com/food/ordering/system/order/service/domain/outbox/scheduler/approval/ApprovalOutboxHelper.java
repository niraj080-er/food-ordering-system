package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import static com.food.ordering.system.saga.order.SagaConstant.ORDER_SAGA_NAME;

import java.util.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApprovalOutboxHelper {
    private final ApprovalOutboxRepository approvalOutboxRepository;
    private final ObjectMapper objectMapper;

    // generate constructor
    public ApprovalOutboxHelper(ApprovalOutboxRepository approvalOutboxRepository, ObjectMapper objectMapper) {
        this.approvalOutboxRepository = approvalOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>> getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus,
                sagaStatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID id,
            SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, id, sagaStatus);
    }

    @Transactional
    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        OrderApprovalOutboxMessage response = approvalOutboxRepository.save(orderApprovalOutboxMessage);
        if (response == null) {
            log.error("Failed to save OrderApprovalOutboxMessage with id:{}", orderApprovalOutboxMessage.getId());
            throw new OrderDomainException(
                    "Failed to save OrderApprovalOutboxMessage with id:" + orderApprovalOutboxMessage.getId());
        }
        log.info("OrderApprovalOutboxMessage with id:{} saved successfully", orderApprovalOutboxMessage.getId());
    }

    @Transactional
    public void deleteApprovalOutboxmessageByOutboxStatusAndSagaStus(OutboxStatus outboxStatus,
            SagaStatus... sagaStus) {
        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStus);
    }

    @Transactional
    public void savaApprovalOutboxHelper(OrderApprovalEventPayload orderApprovalEventPayload,
            OrderStatus orderStatus,
            SagaStatus sagaStatus,
            OutboxStatus outboxStatus,
            UUID sagaId) {
        save(OrderApprovalOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderApprovalEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderApprovalEventPayload))
                .orderStatus(orderStatus)
                .status(sagaStatus) 
                .outboxStatus(outboxStatus)
                .build());
    }

    private String createPayload(OrderApprovalEventPayload orderApprovalEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderApprovalEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create to OrderApprovalEventPayload for order id: {}", orderApprovalEventPayload.getOrderId(), e);
            throw new OrderDomainException("Could not create to OrderApprovalEventPayload for order id: " + orderApprovalEventPayload.getOrderId(),e);
        }

    }

}
