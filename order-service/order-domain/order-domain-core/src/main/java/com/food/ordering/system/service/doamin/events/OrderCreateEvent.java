package com.food.ordering.system.service.doamin.events;

import java.time.ZonedDateTime;

import com.food.ordering.system.service.doamin.entity.Order;

public class OrderCreateEvent extends OrderEvent{

    public OrderCreateEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}