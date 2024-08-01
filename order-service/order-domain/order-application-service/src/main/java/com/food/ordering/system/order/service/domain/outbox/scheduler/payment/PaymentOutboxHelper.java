package com.food.ordering.system.order.service.domain.outbox.scheduler.payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.exception.DomainException;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import lombok.extern.slf4j.Slf4j;

import static com.food.ordering.system.saga.order.SagaConstant.ORDER_SAGA_NAME;

@Slf4j
@Component
public class PaymentOutboxHelper {

    private final PaymentOutboxRepository paymentOutboxRepository;
    private final ObjectMapper objectMapper;
    // generate constructor
    public PaymentOutboxHelper(PaymentOutboxRepository paymentOutboxRepository) {
        this.paymentOutboxRepository = paymentOutboxRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
    SagaStatus... sagaStatus) {
        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,outboxStatus, sagaStatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... sagaStatuses) {
        return paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId, sagaStatuses);
        
    }
    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
       OrderPaymentOutboxMessage response= paymentOutboxRepository.save(orderPaymentOutboxMessage);
       if(response==null){
        log.error("Failed to save OrderPaymentOutboxMessage with id:{}", orderPaymentOutboxMessage.getId());
        throw new DomainException("Failed to save OrderPaymentOutboxMessage with id:"+orderPaymentOutboxMessage.getId());
       }
       log.info("OrderPaymentOutboxMessage with id:{} saved successfully", orderPaymentOutboxMessage.getId());
    }
    @Transactional
    public void savePaymentOutboxMessage(OrderPaymentEventPayload orderPaymentEventPayload, 
                                        OrderStatus orderStatus, 
                                        SagaStatus sagaStatus,
                                        OutboxStatus outboxStatus,
                                        UUID sagaId) {
                    save(OrderPaymentOutboxMessage.builder()
                    .id(UUID.randomUUID())
                    .sagaId(sagaId)
                    .createdAt(orderPaymentEventPayload.getCreatedAt())
                    .type(ORDER_SAGA_NAME)
                    .payload(ccreatePayload(orderPaymentEventPayload))
                    .orderStatus(orderStatus)
                    .outboxStatus(outboxStatus)
                    .status(sagaStatus)
                    .build());
    }

    private String ccreatePayload(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload);
        } catch (JsonProcessingException e) {
            log.error("could not create OrderPaymentEventPayload for Order id: {}", orderPaymentEventPayload.getOrderId(),e);
            throw new OrderDomainException("could not create OrderPaymentEventPayload for Order id: "+orderPaymentEventPayload.getOrderId());
        }
    }

    @Transactional
    public void deletePaymentOutboxMessageByOrderStatusAndSagaStatus(OutboxStatus orderStatus,
                                                                    SagaStatus... sagaStatus) {
    paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, orderStatus, sagaStatus);
    
    }

}
