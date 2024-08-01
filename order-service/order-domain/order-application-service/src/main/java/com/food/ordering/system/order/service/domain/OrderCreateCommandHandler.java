package com.food.ordering.system.order.service.domain;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordering.system.outbox.OutboxStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderCreateCommandHandler {

	private final OrderCreateHelper orderCreateHelper;
	private final OrderDataMapper orderDataMapper;
	private final PaymentOutboxHelper paymentOutboxHelper;
	private final OrderSagaHelper orderSagaHelper;
	
	// create constructor
	public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, PaymentOutboxHelper paymentOutboxHelper, OrderSagaHelper orderSagaHelper) {
		this.orderCreateHelper = orderCreateHelper;
		this.orderDataMapper = orderDataMapper;
		this.paymentOutboxHelper = paymentOutboxHelper;
		this.orderSagaHelper = orderSagaHelper;
	}

	@Transactional
	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
		OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
		log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());		
		CreateOrderResponse createOrderResponse = orderDataMapper.orderToCreateOrderResponse(
                orderCreatedEvent.getOrder(), "Order is created successfully");
			paymentOutboxHelper.savePaymentOutboxMessage(orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent), 
												orderCreatedEvent.getOrder().getOrderStatus(),
												orderSagaHelper.orderStatusToSagaStatus(
												orderCreatedEvent.getOrder().getOrderStatus()),
												OutboxStatus.STARTED,
												UUID.randomUUID());
	log.info("OrderPaymentOutboxMessage is saved successfully for order id: {}", orderCreatedEvent.getOrder().getId().getValue());
	return createOrderResponse;
	}
}
