package com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper;

import org.springframework.stereotype.Component;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

@Component
public class PaymentOutboxDataAccessMapper {

    public PaymentOutboxEntity orderPaymentOutboxEntityToOutboxEntity(OrderPaymentOutboxMessage orderPaymentOutbox) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutbox.getId())
                .sagaId(orderPaymentOutbox.getSagaId())
                .createdAt(orderPaymentOutbox.getCreatedAt())
                .processedAt(orderPaymentOutbox.getProcessedAt())
                .type(orderPaymentOutbox.getType())
                .payload(orderPaymentOutbox.getPayload())
                .sagaStatus(orderPaymentOutbox.getStatus())
                .orderStatus(orderPaymentOutbox.getOrderStatus())
                .outboxStatus(orderPaymentOutbox.getOutboxStatus())
                .version(orderPaymentOutbox.getVersion())
                .build();
    }

    public OrderPaymentOutboxMessage paymentOutboxEntityToOrderPaymentOutboxMessage(PaymentOutboxEntity paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .processedAt(paymentOutboxEntity.getProcessedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .status(paymentOutboxEntity.getSagaStatus())
                .orderStatus(paymentOutboxEntity.getOrderStatus())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }

}
