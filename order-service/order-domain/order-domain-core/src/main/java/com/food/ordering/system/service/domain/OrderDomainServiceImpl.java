package com.food.ordering.system.service.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.events.OrderCencledEvent;
import com.food.ordering.system.service.domain.events.OrderCreateEvent;
import com.food.ordering.system.service.domain.events.OrderPaidEvent;
import com.food.ordering.system.service.domain.exception.OrderDoaminException;

import lombok.extern.slf4j.Slf4j;



@Slf4j // Lombok annotation for logger

public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreateEvent validateItemAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id {} is created", order.getId().getValue());
        return new OrderCreateEvent(order, ZonedDateTime.now(ZoneId.systemDefault()));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.systemDefault()));
    }

    @Override
    public OrderCencledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order with id {} is canceled", order.getId().getValue());
        return new OrderCencledEvent(order, ZonedDateTime.now(ZoneId.systemDefault()));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id {} is approved", order.getId().getValue());
    }

    @Override
    public void cencleOrder(Order order, List<String> failureMessages) {
      
        order.cancel(failureMessages);
        log.info("Order with id {} is canceled", order.getId().getValue());

    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(restaurant.isActive()){
            throw new OrderDoaminException("Resturant with id " + restaurant.getId().getValue() + " is not active");

        }
    }
}