package com.food.odering.system.service.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.events.OrderCencledEvent;
import com.food.ordering.system.service.domain.events.OrderCreateEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OderMessagingDataMapper {


public PaymentRequestAvroModel orderCreateEventToPaymentRequestAvroModel(OrderCreateEvent orderCreateEvent) {
    Order order = orderCreateEvent.getOrder();
    return PaymentRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setCustomerId(order.getCustomerId().getValue().toString())
            .setOrderId(order.getId().getValue().toString())
            .setPrice(order.getPrice().getAmount())
            .setCreatedAt(orderCreateEvent.getCreatedAt().toInstant())
            .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
            .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCencledEvent orderCencledEvent) {
        Order order = orderCencledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCencledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreateEvent domainEvent) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orderCreatedEventToPaymentRequestAvroModel'");
    }




    
}
