package com.food.ordering.system.order.service.domain.outbox.model.payment;


import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderPaymentEventPayload {
    @JsonProperty
    private  String orderId;
    @JsonProperty
    private String customerId;
    @JsonProperty
    private ZonedDateTime createdAt;
    @JsonProperty
    private BigDecimal price;
    @JsonProperty
    private  String paymentOrderStatus;


}
