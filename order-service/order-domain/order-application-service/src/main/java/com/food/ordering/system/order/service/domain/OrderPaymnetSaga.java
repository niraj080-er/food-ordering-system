package com.food.ordering.system.order.service.domain;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.ordering.system.domain.event.EmptyEvent;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.saga.SagaStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderPaymnetSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent>{
    
        private final OrderDomainService orderDomainService;
        private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;
        private final OrderSagaHelper orderSagaHelper;
    // generate constructor
    public OrderPaymnetSaga(OrderDomainService orderDomainService, 
                            OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher,
                            OrderSagaHelper orderSagaHelper) {
        this.orderDomainService = orderDomainService;
        this.orderPaidRestaurantRequestMessagePublisher = orderPaidRestaurantRequestMessagePublisher;
        this.orderSagaHelper = orderSagaHelper;
    }
    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info("Compliting order payment for order id: {}", paymentResponse.getOrderId());
        Order order=orderSagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent domaEvent= orderDomainService.payOrder(order, orderPaidRestaurantRequestMessagePublisher);
       orderSagaHelper.saveOrder(order);
        log.info("Order paid for id {}", order.getId().getValue());
        return domaEvent;
    }
    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info("Rollback order payment for order id: {}", paymentResponse.getOrderId());
        Order order=orderSagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order payment rollback for id {}", order.getId().getValue());
        return  EmptyEvent.INSTANCE;
        
    }
}
