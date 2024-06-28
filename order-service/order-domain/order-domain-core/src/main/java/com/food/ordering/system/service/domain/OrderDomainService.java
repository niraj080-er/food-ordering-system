package com.food.ordering.system.service.domain;

import java.util.*;

import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.events.OrderCencledEvent;
import com.food.ordering.system.service.domain.events.OrderCreateEvent;
import com.food.ordering.system.service.domain.events.OrderPaidEvent;

public interface OrderDomainService {
    
    OrderCreateEvent validateItemAndInitiateOrder(Order order , Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);
    
    OrderCencledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void approveOrder(Order order);

    void cencleOrder(Order order, List<String> failureMessages);


}
