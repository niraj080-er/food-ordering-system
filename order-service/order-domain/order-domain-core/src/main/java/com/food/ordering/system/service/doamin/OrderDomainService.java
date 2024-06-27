package com.food.ordering.system.service.doamin;

import java.util.*;

import com.food.ordering.system.service.doamin.entity.Order;
import com.food.ordering.system.service.doamin.entity.Restaurant;
import com.food.ordering.system.service.doamin.events.OrderCencledEvent;
import com.food.ordering.system.service.doamin.events.OrderCreateEvent;
import com.food.ordering.system.service.doamin.events.OrderPaidEvent;

public interface OrderDomainService {
    
    OrderCreateEvent validateItemAndInitiateOrder(Order order , Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);
    
    OrderCencledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void approveOrder(Order order);

    void cencleOrder(Order order, List<String> failureMessages);


}
