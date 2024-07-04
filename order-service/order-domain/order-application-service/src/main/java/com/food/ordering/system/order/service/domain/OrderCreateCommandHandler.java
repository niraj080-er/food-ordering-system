package com.food.ordering.system.order.service.domain;


import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMesagePublisher;
import com.food.ordering.system.service.domain.events.OrderCreateEvent;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


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

    @SuppressWarnings("unchecked")
    public TrackOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreateEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMesagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToTrackOrderResponse(orderCreatedEvent.getOrder(), "Order Created Successfully");
    }
       
}
