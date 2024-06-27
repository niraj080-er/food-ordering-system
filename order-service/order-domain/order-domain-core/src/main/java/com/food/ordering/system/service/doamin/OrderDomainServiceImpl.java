package com.food.ordering.system.service.doamin;

import java.util.List;

import com.food.ordering.system.service.doamin.entity.Order;
import com.food.ordering.system.service.doamin.entity.Restaurant;
import com.food.ordering.system.service.doamin.events.OrderCencledEvent;
import com.food.ordering.system.service.doamin.events.OrderCreateEvent;
import com.food.ordering.system.service.doamin.events.OrderPaidEvent;

public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreateEvent validateItemAndInitiateOrder(Order order, Restaurant restaurant) {
         validateRestaurant(restaurant);
         setOrderProductInformation(order, restaurant);
        



         return null;
    }



    @Override
    public OrderPaidEvent payOrder(Order order) {
      
        return null;
    }

    @Override
    public OrderCencledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        
        return null;
    }

    @Override
    public void approveOrder(Order order) {
      
    }

    @Override
    public void cencleOrder(Order order, List<String> failureMessages) {
       
    }



    private void setOrderProductInformation(Order order, Restaurant restaurant) {

    }

    private void validateRestaurant(Restaurant restaurant) {


    }

}
