package com.food.ordering.system.order.service.domain;

import java.util.Optional;
import java.util.UUID;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.saga.SagaStatus;
import org.springframework.stereotype.Component;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderSagaHelper {

    private final OrderRepository orderRepository;
    // generate constructor
    public OrderSagaHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

   public Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if(orderResponse.isEmpty()){
            log.error("Order not found for order id: {}", orderId);
            throw new OrderNotFoundException("Order not found for order id: "+orderId);
        }
        return orderResponse.get();
    }
    void saveOrder(Order order) {
        orderRepository.save(order);
    }
    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus){
        switch (orderStatus){
            case PAID :
                return SagaStatus.PROCESSING;
            case APPROVED:
                return SagaStatus.SUCED;
            case CANCELLING:
                return SagaStatus.COMPENSATING;
            case CANCELLED:
                return SagaStatus.COMPENSTED;
            default:
                return SagaStatus.STARTED;
                
        }
    }
}