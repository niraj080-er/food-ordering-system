package com.food.ordering.system.order.service.domain.outbox.model.approval;


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
@NoArgsConstructor
@Builder
public class OrderApprovalOutboxMessage {

    private UUID id;
    private  UUID sagaId;
    private String type;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String payload;
    private SagaStatus status;
    private OrderStatus orderStatus;
    private OutboxStatus outboxStatus;
    private  int version;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(SagaStatus status) {
        this.status = status;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOutboxStatus(OutboxStatus outboxStatus) {
        this.outboxStatus = outboxStatus;
    }
}
