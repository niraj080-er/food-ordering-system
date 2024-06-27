package com.food.ordering.system.service.doamin.events;

import java.time.ZonedDateTime;


import com.food.ordering.system.service.doamin.entity.Order;

public class OrderPaidEvent extends OrderEvent {

    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
