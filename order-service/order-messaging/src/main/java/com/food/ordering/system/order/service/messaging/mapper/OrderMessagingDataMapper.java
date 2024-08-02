package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

        // no use currently --> if it's required in future, then we can use it

        // public PaymentRequestAvroModel
        // orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent
        // orderCreatedEvent) {
        // Order order = orderCreatedEvent.getOrder();
        // return PaymentRequestAvroModel.newBuilder()
        // .setId(UUID.randomUUID().toString())
        // .setSagaId("")
        // .setCustomerId(order.getCustomerId().getValue().toString())
        // .setOrderId(order.getId().getValue().toString())
        // .setPrice(order.getPrice().getAmount())
        // .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
        // .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
        // .build();
        // }

        // public PaymentRequestAvroModel
        // orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent
        // orderCancelledEvent) {
        // Order order = orderCancelledEvent.getOrder();
        // return PaymentRequestAvroModel.newBuilder()
        // .setId(UUID.randomUUID().toString())
        // .setSagaId("")
        // .setCustomerId(order.getCustomerId().getValue().toString())
        // .setOrderId(order.getId().getValue().toString())
        // .setPrice(order.getPrice().getAmount())
        // .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
        // .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
        // .build();
        // }

        // public RestaurantApprovalRequestAvroModel
        // orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent
        // orderPaidEvent) {
        // Order order = orderPaidEvent.getOrder();
        // return RestaurantApprovalRequestAvroModel.newBuilder()
        // .setId(UUID.randomUUID().toString())
        // .setSagaId("")
        // .setOrderId(order.getId().getValue().toString())
        // .setRestaurantId(order.getRestaurantId().getValue().toString())
        // .setOrderId(order.getId().getValue().toString())
        // .setRestaurantOrderStatus(com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus
        // .valueOf(order.getOrderStatus().name()))
        // .setProducts(order.getItems().stream().map(orderItem ->
        // com.food.ordering.system.kafka.order.avro.model.Product.newBuilder()
        // .setId(orderItem.getProduct().getId().getValue().toString())
        // .setQuantity(orderItem.getQuantity())
        // .build()).collect(Collectors.toList()))
        // .setPrice(order.getPrice().getAmount())
        // .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
        // .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
        // .build();
        // }

        public PaymentResponse paymentResponseAvroModelToPaymentResponse(
                        PaymentResponseAvroModel paymentResponseAvroModel) {
                return PaymentResponse.builder()
                                .id(paymentResponseAvroModel.getId())
                                .sagaId(paymentResponseAvroModel.getSagaId())
                                .paymentId(paymentResponseAvroModel.getPaymentId())
                                .customerId(paymentResponseAvroModel.getCustomerId())
                                .orderId(paymentResponseAvroModel.getOrderId())
                                .price(paymentResponseAvroModel.getPrice())
                                .createdAt(paymentResponseAvroModel.getCreatedAt())
                                .paymentStatus(com.food.ordering.system.domain.valueobject.PaymentStatus.valueOf(
                                                paymentResponseAvroModel.getPaymentStatus().name()))
                                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                                .build();
        }

        public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(
                        RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
                return RestaurantApprovalResponse.builder()
                                .id(restaurantApprovalResponseAvroModel.getId())
                                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                                .orderApprovalStatus(
                                                com.food.ordering.system.domain.valueobject.OrderApprovalStatus.valueOf(
                                                                restaurantApprovalResponseAvroModel
                                                                                .getOrderApprovalStatus().name()))
                                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                                .build();
        }

        public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId,
                        OrderPaymentEventPayload orderPaymentEventPayload) {
                return PaymentRequestAvroModel.newBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setSagaId(sagaId)
                                .setCustomerId(orderPaymentEventPayload.getCustomerId())
                                .setOrderId(orderPaymentEventPayload.getOrderId())
                                .setPrice(orderPaymentEventPayload.getPrice())
                                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                                .setPaymentOrderStatus(PaymentOrderStatus
                                                .valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                                .build();
        }

        public RestaurantApprovalRequestAvroModel orderApprovalToRestaurantApprovalRequestAvroModel(String sagaId,
                        OrderApprovalEventPayload orderApprovalEventPayload) {
                return RestaurantApprovalRequestAvroModel.newBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setSagaId(sagaId)
                                .setOrderId(orderApprovalEventPayload.getOrderId())
                                .setRestaurantId(orderApprovalEventPayload.getRestaurantId())
                                .setProducts(orderApprovalEventPayload.getProducts().stream().map(
                                                orderItem -> com.food.ordering.system.kafka.order.avro.model.Product
                                                                .newBuilder()
                                                                .setId(orderItem.getId())
                                                                .setQuantity(orderItem.getQuantity())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .setPrice(orderApprovalEventPayload.getPrice())
                                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                                .setRestaurantOrderStatus(RestaurantOrderStatus
                                                .valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                                .build();
        }
}
