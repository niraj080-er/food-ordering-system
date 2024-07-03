package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.exception.DomainExecption;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMesagePublisher;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.service.domain.OrderDomainService;
import com.food.ordering.system.service.domain.entity.Customer;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.events.OrderCreateEvent;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderCreateCommandHandler {

private  OrderCreateHelper orderCreateHelper;

private final OrderDataMapper orderDataMapper;

private final OrderCreatedPaymentRequestMesagePublisher orderCreatedPaymentRequestMesagePublisher;



    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, OrderCreatedPaymentRequestMesagePublisher orderCreatedPaymentRequestMesagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMesagePublisher = orderCreatedPaymentRequestMesagePublisher;
    }

    public CreateOrderCommand createOrder(CreateOrderCommand createOrderCommand){
     OrderCreateEvent  orderCreateEvent= orderCreateHelper.persistOrder(createOrderCommand);
     log.info("order is created");
        orderCreatedPaymentRequestMesagePublisher.publish(orderCreateEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreateEvent.getOrder());

    }

       
}
