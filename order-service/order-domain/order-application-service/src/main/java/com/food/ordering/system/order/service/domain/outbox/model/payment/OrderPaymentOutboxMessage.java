package com.food.ordering.system.order.service.domain.outbox.model.payment;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderPaymentOutboxMessage {
private UUID id;
private  UUID sagaId;
private String type;
private ZonedDateTime createdAt;
private ZonedDateTime processedAt;
private String payload;
private SagaStatus status;
private OrderStatus orderStatus;
private OutboxStatus outboxStatus;

}
