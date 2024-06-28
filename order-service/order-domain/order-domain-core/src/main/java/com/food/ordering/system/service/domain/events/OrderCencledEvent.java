package com.food.ordering.system.service.domain.events;

import java.time.ZonedDateTime;

import com.food.ordering.system.service.domain.entity.Order;

public class OrderCencledEvent  extends OrderEvent{
    
        public OrderCencledEvent(Order order, ZonedDateTime createdAt) {
            super(order, createdAt);
        }
}