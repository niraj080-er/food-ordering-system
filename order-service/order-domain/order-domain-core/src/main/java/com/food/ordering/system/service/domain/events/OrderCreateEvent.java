package com.food.ordering.system.service.domain.events;

import java.time.ZonedDateTime;

import com.food.ordering.system.service.domain.entity.Order;

public class OrderCreateEvent extends OrderEvent{

    public OrderCreateEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}