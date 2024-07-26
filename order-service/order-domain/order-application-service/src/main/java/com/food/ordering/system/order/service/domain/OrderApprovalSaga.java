package com.food.ordering.system.order.service.domain;

import org.springframework.stereotype.Component;

import com.food.ordering.system.domain.event.EmptyEvent;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.ordering.system.saga.SagaStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderApprovalSaga implements SagaStep< RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent > {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;

    public OrderApprovalSaga(OrderDomainService orderDomainService, 
                            OrderSagaHelper orderSagaHelper, 
                            OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
        this.orderCancelledPaymentRequestMessagePublisher = orderCancelledPaymentRequestMessagePublisher;
    }

    @Override
    public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Order approved for order id: {}", restaurantApprovalResponse.getOrderId());
        Order order=orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.approveOrder(order); 
        orderSagaHelper.saveOrder(order);
        log.info("Order approved for id {}", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

    @Override
    public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Order approval rollback for order id: {}", restaurantApprovalResponse.getOrderId());
        Order order=orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent domaEvent= orderDomainService.cancelOrderPayment(order,
                                                                            restaurantApprovalResponse.getFailureMessages(),
                                                                            orderCancelledPaymentRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order approval rollback for id {}", order.getId().getValue());
        return domaEvent;
    }

}
